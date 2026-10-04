package com.devflux.deenone.core.quran;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.dao.AyahDao;
import com.devflux.deenone.data.local.dao.SurahDao;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.data.repository.QuranRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * High-performance, multi-threaded Quran Offline Download Manager.
 * Downloads and caches all 114 Surahs (6,236 Ayahs) in local Room SQLite database.
 */
public class QuranOfflineDownloadManager {

    private static final String TAG = "QuranOfflineDownload";
    public static final int TOTAL_SURAHS = 114;
    public static final int TOTAL_AYAHS = 6236;

    private static volatile QuranOfflineDownloadManager instance;
    private final ExecutorService downloadExecutor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final AtomicBoolean isDownloading = new AtomicBoolean(false);
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);

    public interface DownloadListener {
        void onProgress(int completedSurahs, int totalSurahs, int progressPercent, String currentSurahBn, String currentSurahEn);
        void onSuccess();
        void onError(String errorMessage);
        void onCancelled();
    }

    public interface StatusCallback {
        void onResult(boolean isDownloaded, int downloadedAyahCount, int totalAyahs);
    }

    public static synchronized QuranOfflineDownloadManager getInstance() {
        if (instance == null) {
            instance = new QuranOfflineDownloadManager();
        }
        return instance;
    }

    private QuranOfflineDownloadManager() {}

    public boolean isCurrentlyDownloading() {
        return isDownloading.get();
    }

    public void cancelDownload() {
        isCancelled.set(true);
        isDownloading.set(false);
    }

    public void checkDownloadStatus(Context context, StatusCallback callback) {
        if (context == null || callback == null) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            int count = db.ayahDao().getTotalAyahCount();
            boolean complete = count >= TOTAL_AYAHS;
            mainHandler.post(() -> callback.onResult(complete, count, TOTAL_AYAHS));
        });
    }

    public static boolean isFullQuranDownloadedSync(Context context) {
        if (context == null) return false;
        try {
            AppDatabase db = AppDatabase.getInstance(context);
            return db.ayahDao().getTotalAyahCount() >= TOTAL_AYAHS;
        } catch (Exception e) {
            return false;
        }
    }

    public void startFullQuranDownload(Context context, DownloadListener listener) {
        if (context == null) return;
        if (isDownloading.get()) {
            Log.w(TAG, "Download is already in progress.");
            return;
        }

        isDownloading.set(true);
        isCancelled.set(false);

        AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
        AyahDao ayahDao = db.ayahDao();
        SurahDao surahDao = db.surahDao();

        // Seed 114 Surahs if missing
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (surahDao.getSurahCount() < TOTAL_SURAHS) {
                surahDao.insertSurahs(QuranSurahDataSeeder.get114Surahs());
            }

            List<QuranSurahEntity> allSurahs = QuranSurahDataSeeder.get114Surahs();
            AtomicInteger completedCount = new AtomicInteger(0);
            AtomicInteger failedCount = new AtomicInteger(0);

            // First check how many surahs already have full ayahs in Room DB
            List<Integer> surahsToDownload = new ArrayList<>();
            for (QuranSurahEntity surah : allSurahs) {
                List<QuranAyahEntity> existing = ayahDao.getAyahsForSurahSync(surah.getNumber());
                if (existing != null && existing.size() >= surah.getNumberOfAyahs() && surah.getNumberOfAyahs() > 0) {
                    completedCount.incrementAndGet();
                } else {
                    surahsToDownload.add(surah.getNumber());
                }
            }

            int initialCompleted = completedCount.get();
            if (initialCompleted >= TOTAL_SURAHS) {
                isDownloading.set(false);
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onProgress(TOTAL_SURAHS, TOTAL_SURAHS, 100, "আল-কুরআন", "Al-Quran");
                        listener.onSuccess();
                    }
                });
                return;
            }

            mainHandler.post(() -> {
                if (listener != null) {
                    int percent = (initialCompleted * 100) / TOTAL_SURAHS;
                    listener.onProgress(initialCompleted, TOTAL_SURAHS, percent, "প্রস্তুতি চলছে...", "Preparing...");
                }
            });

            // Process Surahs
            for (int surahNum : surahsToDownload) {
                if (isCancelled.get()) {
                    break;
                }

                downloadExecutor.execute(() -> {
                    if (isCancelled.get()) return;

                    QuranSurahEntity meta = getSurahMeta(allSurahs, surahNum);
                    String nameBn = meta != null ? meta.getNameBengali() : ("সূরা " + surahNum);
                    String nameEn = meta != null ? meta.getNameEnglish() : ("Surah " + surahNum);

                    List<QuranAyahEntity> ayahs = fetchSurahWithRetry(surahNum, 3);
                    if (ayahs != null && !ayahs.isEmpty()) {
                        ayahDao.insertAyahs(ayahs);
                        int done = completedCount.incrementAndGet();
                        int pct = (done * 100) / TOTAL_SURAHS;

                        mainHandler.post(() -> {
                            if (listener != null && !isCancelled.get()) {
                                listener.onProgress(done, TOTAL_SURAHS, pct, nameBn, nameEn);
                            }
                        });
                    } else {
                        failedCount.incrementAndGet();
                    }

                    // Check if all surahs are processed
                    int totalProcessed = completedCount.get() + failedCount.get();
                    if (totalProcessed >= TOTAL_SURAHS) {
                        isDownloading.set(false);
                        mainHandler.post(() -> {
                            if (isCancelled.get()) {
                                if (listener != null) listener.onCancelled();
                            } else if (completedCount.get() >= TOTAL_SURAHS) {
                                if (listener != null) listener.onSuccess();
                            } else {
                                if (listener != null) {
                                    listener.onError("কিছু সূরা ডাউনলোড হতে সমস্যা হয়েছে (" + failedCount.get() + " টি ব্যর্থ)। অনুগ্রহ করে আবার চেষ্টা করুন।");
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private QuranSurahEntity getSurahMeta(List<QuranSurahEntity> list, int surahNum) {
        if (list == null) return null;
        for (QuranSurahEntity s : list) {
            if (s.getNumber() == surahNum) return s;
        }
        return null;
    }

    private List<QuranAyahEntity> fetchSurahWithRetry(int surahNumber, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            if (isCancelled.get()) return null;
            try {
                List<QuranAyahEntity> res = fetchFromTanzilCloud(surahNumber);
                if (res != null && !res.isEmpty()) {
                    return res;
                }
            } catch (Exception ignored) {}

            try {
                List<QuranAyahEntity> res = fetchFromJsDelivrCdn(surahNumber);
                if (res != null && !res.isEmpty()) {
                    return res;
                }
            } catch (Exception ignored) {}

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                break;
            }
        }
        return null;
    }

    private List<QuranAyahEntity> fetchFromTanzilCloud(int surahNumber) {
        try {
            String urlStr = "https://api.alquran.cloud/v1/surah/" + surahNumber + "/editions/quran-uthmani,bn.bengali,en.sahih";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-HifzHub/1.0");
            conn.setConnectTimeout(9000);
            conn.setReadTimeout(15000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONArray editions = root.getJSONArray("data");
                if (editions.length() >= 3) {
                    JSONArray arAyahs = editions.getJSONObject(0).getJSONArray("ayahs");
                    JSONArray bnAyahs = editions.getJSONObject(1).getJSONArray("ayahs");
                    JSONArray enAyahs = editions.getJSONObject(2).getJSONArray("ayahs");

                    List<QuranAyahEntity> result = new ArrayList<>();
                    for (int i = 0; i < arAyahs.length(); i++) {
                        JSONObject arObj = arAyahs.getJSONObject(i);
                        int numInSurah = arObj.getInt("numberInSurah");
                        String arText = QuranRepository.sanitizeArabicVerse(surahNumber, numInSurah, arObj.getString("text"));
                        int juz = arObj.optInt("juz", 1);
                        int page = arObj.optInt("page", 1);

                        String bnText = i < bnAyahs.length() ? bnAyahs.getJSONObject(i).getString("text") : "";
                        String enText = i < enAyahs.length() ? enAyahs.getJSONObject(i).getString("text") : "";
                        String audioUrl = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surahNumber, numInSurah);

                        result.add(new QuranAyahEntity(
                                surahNumber,
                                numInSurah,
                                arText,
                                bnText,
                                enText,
                                "",
                                audioUrl,
                                juz,
                                page
                        ));
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Tanzil cloud fetch failed for Surah " + surahNumber + ": " + e.getMessage());
        }
        return null;
    }

    private List<QuranAyahEntity> fetchFromJsDelivrCdn(int surahNumber) {
        try {
            String bnUrlStr = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ben-muhiuddinkhan/" + surahNumber + ".json";
            String arUrlStr = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ara-quranuthmani/" + surahNumber + ".json";
            String enUrlStr = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/eng-sahih/" + surahNumber + ".json";

            String bnJson = fetchHttpString(bnUrlStr);
            String arJson = fetchHttpString(arUrlStr);
            String enJson = fetchHttpString(enUrlStr);

            if (bnJson != null && arJson != null) {
                JSONObject bnRoot = new JSONObject(bnJson);
                JSONObject arRoot = new JSONObject(arJson);
                JSONObject enRoot = enJson != null ? new JSONObject(enJson) : null;

                JSONArray bnAyahs = bnRoot.getJSONArray("chapter");
                JSONArray arAyahs = arRoot.getJSONArray("chapter");
                JSONArray enAyahs = enRoot != null ? enRoot.optJSONArray("chapter") : null;

                List<QuranAyahEntity> result = new ArrayList<>();
                for (int i = 0; i < arAyahs.length(); i++) {
                    JSONObject arObj = arAyahs.getJSONObject(i);
                    int verseNumber = arObj.getInt("verse");
                    String arText = QuranRepository.sanitizeArabicVerse(surahNumber, verseNumber, arObj.getString("text"));

                    String bnText = i < bnAyahs.length() ? bnAyahs.getJSONObject(i).getString("text") : "";
                    String enText = (enAyahs != null && i < enAyahs.length()) ? enAyahs.getJSONObject(i).getString("text") : "";
                    String audioUrl = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surahNumber, verseNumber);

                    result.add(new QuranAyahEntity(
                            surahNumber,
                            verseNumber,
                            arText,
                            bnText,
                            enText,
                            "",
                            audioUrl,
                            1,
                            1
                    ));
                }
                return result;
            }
        } catch (Exception e) {
            Log.w(TAG, "jsDelivr fetch failed for Surah " + surahNumber + ": " + e.getMessage());
        }
        return null;
    }

    private String fetchHttpString(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-HifzHub/1.0");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(12000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                return sb.toString();
            }
        } catch (Exception ignored) {}
        return null;
    }
}
