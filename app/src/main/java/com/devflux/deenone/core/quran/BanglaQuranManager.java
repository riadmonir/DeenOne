package com.devflux.deenone.core.quran;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.notifications.NotificationHelper;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.data.repository.QuranRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BanglaQuranManager {

    private static final String TAG = "BanglaQuranManager";
    private static final String PREFS_NAME = "bangla_quran_prefs";
    private static final String KEY_TRANSLATOR = "active_translator";
    private static final String KEY_SHOW_ARABIC = "show_arabic_text";
    private static final String KEY_ARABIC_FONT_SCALE = "arabic_font_scale";
    private static final String KEY_BANGLA_FONT_SIZE = "bangla_font_size_sp";
    private static final String KEY_BOOKMARKED_AYAHS = "bookmarked_ayahs_set";
    private static final String KEY_BOOKMARKED_SURAHS = "bookmarked_surahs_set";
    private static final String KEY_BOOKMARKED_PARAS = "bookmarked_paras_set";

    public static final int NOTIFICATION_ID_BANGLA_DOWNLOAD = 2400;

    public interface Callback<T> {
        void onResult(T result);
    }

    public interface DownloadListener {
        void onProgress(int current, int total, int percentage, String surahNameBn, String surahNameEn);
        void onSuccess();
        void onError(String message);
        void onCancelled();
    }

    private static volatile BanglaQuranManager instance;
    private final Context appContext;
    private final SharedPreferences prefs;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Map<Integer, List<BanglaQuranAyahItem>> memoryCache = new ConcurrentHashMap<>();
    private final AtomicBoolean isBatchDownloading = new AtomicBoolean(false);
    private final AtomicBoolean isDownloadCancelled = new AtomicBoolean(false);

    private BanglaQuranManager(Context context) {
        this.appContext = context.getApplicationContext();
        this.prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static BanglaQuranManager getInstance(Context context) {
        if (instance == null) {
            synchronized (BanglaQuranManager.class) {
                if (instance == null) {
                    instance = new BanglaQuranManager(context);
                }
            }
        }
        return instance;
    }

    // --- Settings & Preferences ---

    public BanglaQuranTranslator getActiveTranslator() {
        String id = prefs.getString(KEY_TRANSLATOR, BanglaQuranTranslator.MUHIUDDIN_KHAN.id);
        return BanglaQuranTranslator.fromId(id);
    }

    public void setActiveTranslator(BanglaQuranTranslator translator) {
        if (translator != null) {
            prefs.edit().putString(KEY_TRANSLATOR, translator.id).apply();
        }
    }

    public boolean isArabicShown() {
        return prefs.getBoolean(KEY_SHOW_ARABIC, true);
    }

    public void setArabicShown(boolean shown) {
        prefs.edit().putBoolean(KEY_SHOW_ARABIC, shown).apply();
    }

    public float getArabicFontScale() {
        return prefs.getFloat(KEY_ARABIC_FONT_SCALE, 1.0f);
    }

    public void setArabicFontScale(float scale) {
        prefs.edit().putFloat(KEY_ARABIC_FONT_SCALE, scale).apply();
    }

    public int getBanglaFontSizeSp() {
        return prefs.getInt(KEY_BANGLA_FONT_SIZE, 15);
    }

    public void setBanglaFontSizeSp(int sp) {
        prefs.edit().putInt(KEY_BANGLA_FONT_SIZE, sp).apply();
    }

    // --- Data Fetching & Caching ---

    public void getSurahAyahs(int surahNumber, Callback<List<BanglaQuranAyahItem>> callback) {
        // 1. Check memory cache
        List<BanglaQuranAyahItem> mem = memoryCache.get(surahNumber);
        if (mem != null && !mem.isEmpty()) {
            if (callback != null) {
                applyAyahBookmarkStates(mem);
                callback.onResult(new ArrayList<>(mem));
            }
            return;
        }

        // 2. Background retrieval (Disk -> Cloud -> Room DB)
        executorService.execute(() -> {
            List<BanglaQuranAyahItem> result = loadSurahFromDisk(surahNumber);

            if (result == null || result.isEmpty()) {
                result = fetchFromTanzilCloud(surahNumber);
            }

            if (result == null || result.isEmpty()) {
                result = fetchFromJsDelivrCdn(surahNumber);
            }

            if (result == null || result.isEmpty()) {
                result = loadFromRoomDatabase(surahNumber);
            }

            if (result != null && !result.isEmpty()) {
                saveSurahToDisk(surahNumber, result);
                memoryCache.put(surahNumber, result);
            }

            final List<BanglaQuranAyahItem> finalResult = result != null ? result : new ArrayList<>();
            applyAyahBookmarkStates(finalResult);

            mainHandler.post(() -> {
                if (callback != null) {
                    callback.onResult(finalResult);
                }
            });
        });
    }

    public void getParaAyahs(int paraNumber, Callback<List<BanglaQuranAyahItem>> callback) {
        executorService.execute(() -> {
            QuranParaItem para = QuranParaItem.getParaByNumber(paraNumber);
            List<BanglaQuranAyahItem> combined = new ArrayList<>();

            if (para != null) {
                for (int s = para.getStartSurahNumber(); s <= para.getEndSurahNumber(); s++) {
                    List<BanglaQuranAyahItem> surahAyahs = loadSurahSync(s);
                    if (surahAyahs != null) {
                        for (BanglaQuranAyahItem ayah : surahAyahs) {
                            boolean inRange = true;
                            if (s == para.getStartSurahNumber() && ayah.getAyahNumber() < para.getStartAyahNumber()) {
                                inRange = false;
                            }
                            if (s == para.getEndSurahNumber() && ayah.getAyahNumber() > para.getEndAyahNumber()) {
                                inRange = false;
                            }
                            if (inRange) {
                                combined.add(ayah);
                            }
                        }
                    }
                }
            }

            applyAyahBookmarkStates(combined);
            mainHandler.post(() -> {
                if (callback != null) {
                    callback.onResult(combined);
                }
            });
        });
    }

    private List<BanglaQuranAyahItem> loadSurahSync(int surahNumber) {
        List<BanglaQuranAyahItem> mem = memoryCache.get(surahNumber);
        if (mem != null && !mem.isEmpty()) return mem;

        List<BanglaQuranAyahItem> disk = loadSurahFromDisk(surahNumber);
        if (disk != null && !disk.isEmpty()) {
            memoryCache.put(surahNumber, disk);
            return disk;
        }

        List<BanglaQuranAyahItem> net = fetchFromTanzilCloud(surahNumber);
        if (net == null || net.isEmpty()) {
            net = fetchFromJsDelivrCdn(surahNumber);
        }
        if (net == null || net.isEmpty()) {
            net = loadFromRoomDatabase(surahNumber);
        }
        if (net != null && !net.isEmpty()) {
            saveSurahToDisk(surahNumber, net);
            memoryCache.put(surahNumber, net);
            return net;
        }
        return new ArrayList<>();
    }

    // --- Tanzil Cloud API Tier 1 ---
    private List<BanglaQuranAyahItem> fetchFromTanzilCloud(int surahNumber) {
        try {
            String urlStr = "https://api.alquran.cloud/v1/surah/" + surahNumber + "/editions/quran-uthmani,bn.bengali,bn.hoque";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-BanglaQuran/1.0");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(12000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
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
                    JSONArray bnMuhiuddin = editions.getJSONObject(1).getJSONArray("ayahs");
                    JSONArray bnHoque = editions.getJSONObject(2).getJSONArray("ayahs");

                    List<BanglaQuranAyahItem> list = new ArrayList<>();
                    for (int i = 0; i < arAyahs.length(); i++) {
                        JSONObject arObj = arAyahs.getJSONObject(i);
                        int numInSurah = arObj.getInt("numberInSurah");
                        String arText = QuranRepository.sanitizeArabicVerse(surahNumber, numInSurah, arObj.getString("text"));
                        int juz = arObj.optInt("juz", 1);
                        int page = arObj.optInt("page", 1);

                        String muhiuddinText = i < bnMuhiuddin.length() ? bnMuhiuddin.getJSONObject(i).getString("text") : "";
                        String hoqueText = i < bnHoque.length() ? bnHoque.getJSONObject(i).getString("text") : "";

                        list.add(new BanglaQuranAyahItem(
                                surahNumber,
                                numInSurah,
                                arText,
                                muhiuddinText,
                                hoqueText,
                                juz,
                                page
                        ));
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Tanzil cloud fetch failed for surah " + surahNumber + ": " + e.getMessage());
        }
        return null;
    }

    // --- jsDelivr CDN API Tier 2 ---
    private List<BanglaQuranAyahItem> fetchFromJsDelivrCdn(int surahNumber) {
        try {
            String arUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ara-quranuthmani/" + surahNumber + ".json";
            String muhiuddinUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ben-muhiuddinkhan/" + surahNumber + ".json";
            String hoqueUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ben-zohurulhoque/" + surahNumber + ".json";

            String arJson = fetchHttp(arUrl);
            String muhiuddinJson = fetchHttp(muhiuddinUrl);
            String hoqueJson = fetchHttp(hoqueUrl);

            if (arJson != null && (muhiuddinJson != null || hoqueJson != null)) {
                JSONObject arRoot = new JSONObject(arJson);
                JSONArray arArray = arRoot.getJSONArray("chapter");

                JSONArray muhiuddinArray = muhiuddinJson != null ? new JSONObject(muhiuddinJson).optJSONArray("chapter") : null;
                JSONArray hoqueArray = hoqueJson != null ? new JSONObject(hoqueJson).optJSONArray("chapter") : null;

                List<BanglaQuranAyahItem> list = new ArrayList<>();
                for (int i = 0; i < arArray.length(); i++) {
                    JSONObject arObj = arArray.getJSONObject(i);
                    int ayahNum = arObj.getInt("verse");
                    String arText = QuranRepository.sanitizeArabicVerse(surahNumber, ayahNum, arObj.getString("text"));

                    String muhiuddinText = "";
                    if (muhiuddinArray != null && i < muhiuddinArray.length()) {
                        muhiuddinText = muhiuddinArray.getJSONObject(i).optString("text", "");
                    }

                    String hoqueText = "";
                    if (hoqueArray != null && i < hoqueArray.length()) {
                        hoqueText = hoqueArray.getJSONObject(i).optString("text", "");
                    }

                    list.add(new BanglaQuranAyahItem(
                            surahNumber,
                            ayahNum,
                            arText,
                            muhiuddinText,
                            hoqueText,
                            1,
                            1
                    ));
                }
                return list;
            }
        } catch (Exception e) {
            Log.w(TAG, "jsDelivr fallback fetch failed for surah " + surahNumber + ": " + e.getMessage());
        }
        return null;
    }

    private String fetchHttp(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-BanglaQuran/1.0");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(10000);
            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
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

    // --- Tier 3: Local Room Database ---
    private List<BanglaQuranAyahItem> loadFromRoomDatabase(int surahNumber) {
        try {
            List<QuranAyahEntity> entities = AppDatabase.getInstance(appContext).ayahDao().getAyahsForSurahSync(surahNumber);
            if (entities != null && !entities.isEmpty()) {
                List<BanglaQuranAyahItem> list = new ArrayList<>();
                for (QuranAyahEntity e : entities) {
                    list.add(new BanglaQuranAyahItem(
                            e.getSurahNumber(),
                            e.getAyahNumber(),
                            e.getTextArabic(),
                            e.getTranslationBengali(),
                            "",
                            e.getJuzNumber(),
                            e.getPageNumber()
                    ));
                }
                return list;
            }
        } catch (Exception ignored) {}
        return null;
    }

    // --- Disk Storage Cache ---

    private File getSurahCacheFile(int surahNumber) {
        File dir = new File(appContext.getFilesDir(), "bangla_quran_cache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, "surah_" + surahNumber + ".json");
    }

    public boolean isSurahDownloaded(int surahNumber) {
        File file = getSurahCacheFile(surahNumber);
        return file.exists() && file.length() > 50;
    }

    public boolean isSurahAudioDownloaded(int surahNumber) {
        return QuranAudioCacheManager.isBanglaSurahAudioDownloaded(appContext, surahNumber);
    }

    public boolean isAllQuranDownloaded() {
        for (int i = 1; i <= 114; i++) {
            if (!isSurahDownloaded(i)) return false;
        }
        return true;
    }

    private void saveSurahToDisk(int surahNumber, List<BanglaQuranAyahItem> ayahs) {
        if (ayahs == null || ayahs.isEmpty()) return;
        try {
            JSONObject root = new JSONObject();
            root.put("surahNumber", surahNumber);
            JSONArray array = new JSONArray();
            for (BanglaQuranAyahItem item : ayahs) {
                JSONObject obj = new JSONObject();
                obj.put("ayahNumber", item.getAyahNumber());
                obj.put("textArabic", item.getTextArabic());
                obj.put("translationMuhiuddin", item.getTranslationMuhiuddin());
                obj.put("translationZohurul", item.getTranslationZohurul());
                obj.put("juz", item.getJuzNumber());
                obj.put("page", item.getPageNumber());
                array.put(obj);
            }
            root.put("ayahs", array);

            File target = getSurahCacheFile(surahNumber);
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(target), StandardCharsets.UTF_8)) {
                writer.write(root.toString());
                writer.flush();
            }
        } catch (Exception e) {
            Log.w(TAG, "Error saving surah " + surahNumber + " to disk: " + e.getMessage());
        }
    }

    private List<BanglaQuranAyahItem> loadSurahFromDisk(int surahNumber) {
        File file = getSurahCacheFile(surahNumber);
        if (!file.exists() || file.length() < 10) return null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject root = new JSONObject(sb.toString());
            JSONArray array = root.getJSONArray("ayahs");
            List<BanglaQuranAyahItem> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                list.add(new BanglaQuranAyahItem(
                        surahNumber,
                        obj.getInt("ayahNumber"),
                        obj.optString("textArabic", ""),
                        obj.optString("translationMuhiuddin", ""),
                        obj.optString("translationZohurul", ""),
                        obj.optInt("juz", 1),
                        obj.optInt("page", 1)
                ));
            }
            return list;
        } catch (Exception e) {
            Log.w(TAG, "Error loading surah " + surahNumber + " from disk: " + e.getMessage());
        }
        return null;
    }

    // --- Offline Batch Downloader ---

    public boolean isBatchDownloading() {
        return isBatchDownloading.get();
    }

    public void cancelBatchDownload() {
        isDownloadCancelled.set(true);
        NotificationHelper.cancelDownloadNotification(appContext, NOTIFICATION_ID_BANGLA_DOWNLOAD);
    }

    public void downloadAllBanglaQuranOffline(DownloadListener listener) {
        if (isBatchDownloading.getAndSet(true)) {
            if (listener != null) listener.onError("ডাউনলোড ইতিমধ্যে চলছে...");
            return;
        }
        isDownloadCancelled.set(false);

        executorService.execute(() -> {
            boolean isBn = LocaleManager.isBengali(appContext);
            String title = isBn ? "কুরআন বাংলা অফলাইন ডাউনলোড" : "Bangla Quran Offline Download";
            NotificationHelper.updateDownloadProgressNotification(appContext, NOTIFICATION_ID_BANGLA_DOWNLOAD, title, isBn ? "ডাউনলোড শুরু হচ্ছে..." : "Starting download...", 0);

            List<QuranSurahEntity> allSurahs = AppDatabase.getInstance(appContext).surahDao().getAllSurahsSync();
            final int total = 114;
            AtomicInteger completed = new AtomicInteger(0);
            AtomicInteger failed = new AtomicInteger(0);

            for (int s = 1; s <= total; s++) {
                if (isDownloadCancelled.get()) break;

                final int surahNum = s;
                QuranSurahEntity meta = null;
                if (allSurahs != null) {
                    for (QuranSurahEntity se : allSurahs) {
                        if (se.getNumber() == surahNum) {
                            meta = se;
                            break;
                        }
                    }
                }
                String nameBn = meta != null ? meta.getNameBengali() : ("সূরা " + surahNum);
                String nameEn = meta != null ? meta.getNameEnglish() : ("Surah " + surahNum);

                List<BanglaQuranAyahItem> ayahs = loadSurahSync(surahNum);
                if (ayahs != null && !ayahs.isEmpty()) {
                    int done = completed.incrementAndGet();
                    int pct = (done * 100) / total;

                    NotificationHelper.updateDownloadProgressNotification(
                            appContext,
                            NOTIFICATION_ID_BANGLA_DOWNLOAD,
                            title,
                            isBn ? (nameBn + " ডাউনলোড সম্পন্ন (" + BengaliNumberUtil.toBengali(pct) + "%)") : (nameEn + " downloaded (" + pct + "%)"),
                            pct
                    );

                    mainHandler.post(() -> {
                        if (listener != null && !isDownloadCancelled.get()) {
                            listener.onProgress(done, total, pct, nameBn, nameEn);
                        }
                    });
                } else {
                    failed.incrementAndGet();
                }

                try {
                    Thread.sleep(150);
                } catch (InterruptedException ignored) {}
            }

            isBatchDownloading.set(false);
            if (isDownloadCancelled.get()) {
                NotificationHelper.cancelDownloadNotification(appContext, NOTIFICATION_ID_BANGLA_DOWNLOAD);
                mainHandler.post(() -> {
                    if (listener != null) listener.onCancelled();
                });
            } else if (completed.get() >= total - 2) {
                NotificationHelper.completeDownloadNotification(
                        appContext,
                        NOTIFICATION_ID_BANGLA_DOWNLOAD,
                        title,
                        isBn ? "১১৪ টি সূরার বাংলা অনুবাদ সফলভাবে ডাউনলোড হয়েছে।" : "All 114 Surahs Bangla translation downloaded successfully."
                );
                mainHandler.post(() -> {
                    if (listener != null) listener.onSuccess();
                });
            } else {
                NotificationHelper.cancelDownloadNotification(appContext, NOTIFICATION_ID_BANGLA_DOWNLOAD);
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onError(isBn ? "কিছু সূরা ডাউনলোড হতে সমস্যা হয়েছে। অনুগ্রহ করে আবার চেষ্টা করুন।" : "Some surahs failed to download. Please retry.");
                    }
                });
            }
        });
    }

    // --- Bookmarking & State Helpers ---

    private String makeAyahKey(int surahNumber, int ayahNumber) {
        return surahNumber + ":" + ayahNumber;
    }

    public boolean isAyahBookmarked(int surahNumber, int ayahNumber) {
        Set<String> set = prefs.getStringSet(KEY_BOOKMARKED_AYAHS, null);
        return set != null && set.contains(makeAyahKey(surahNumber, ayahNumber));
    }

    public boolean toggleAyahBookmark(int surahNumber, int ayahNumber) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_BOOKMARKED_AYAHS, new HashSet<>()));
        String key = makeAyahKey(surahNumber, ayahNumber);
        boolean isNowBookmarked;
        if (set.contains(key)) {
            set.remove(key);
            isNowBookmarked = false;
        } else {
            set.add(key);
            isNowBookmarked = true;
        }
        prefs.edit().putStringSet(KEY_BOOKMARKED_AYAHS, set).apply();

        // Also update Room DB
        executorService.execute(() -> {
            try {
                AppDatabase.getInstance(appContext).ayahDao().setAyahBookmarked(surahNumber, ayahNumber, isNowBookmarked);
            } catch (Exception ignored) {}
        });

        return isNowBookmarked;
    }

    public boolean isSurahBookmarked(int surahNumber) {
        Set<String> set = prefs.getStringSet(KEY_BOOKMARKED_SURAHS, null);
        return set != null && set.contains(String.valueOf(surahNumber));
    }

    public boolean toggleSurahBookmark(int surahNumber) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_BOOKMARKED_SURAHS, new HashSet<>()));
        String key = String.valueOf(surahNumber);
        boolean isNowBookmarked;
        if (set.contains(key)) {
            set.remove(key);
            isNowBookmarked = false;
        } else {
            set.add(key);
            isNowBookmarked = true;
        }
        prefs.edit().putStringSet(KEY_BOOKMARKED_SURAHS, set).apply();

        executorService.execute(() -> {
            try {
                QuranRepository.getInstance(appContext).toggleSurahFavorite(surahNumber, isNowBookmarked);
            } catch (Exception ignored) {}
        });

        return isNowBookmarked;
    }

    public boolean isParaBookmarked(int paraNumber) {
        Set<String> set = prefs.getStringSet(KEY_BOOKMARKED_PARAS, null);
        return set != null && set.contains(String.valueOf(paraNumber));
    }

    public boolean toggleParaBookmark(int paraNumber) {
        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_BOOKMARKED_PARAS, new HashSet<>()));
        String key = String.valueOf(paraNumber);
        boolean isNowBookmarked;
        if (set.contains(key)) {
            set.remove(key);
            isNowBookmarked = false;
        } else {
            set.add(key);
            isNowBookmarked = true;
        }
        prefs.edit().putStringSet(KEY_BOOKMARKED_PARAS, set).apply();
        return isNowBookmarked;
    }

    public void getBookmarkedAyahs(Callback<List<BanglaQuranAyahItem>> callback) {
        executorService.execute(() -> {
            Set<String> set = prefs.getStringSet(KEY_BOOKMARKED_AYAHS, new HashSet<>());
            List<BanglaQuranAyahItem> list = new ArrayList<>();
            for (String key : set) {
                String[] parts = key.split(":");
                if (parts.length == 2) {
                    try {
                        int s = Integer.parseInt(parts[0]);
                        int a = Integer.parseInt(parts[1]);
                        List<BanglaQuranAyahItem> surahAyahs = loadSurahSync(s);
                        if (surahAyahs != null) {
                            for (BanglaQuranAyahItem item : surahAyahs) {
                                if (item.getAyahNumber() == a) {
                                    item.setBookmarked(true);
                                    list.add(item);
                                    break;
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(list);
            });
        });
    }

    private void applyAyahBookmarkStates(List<BanglaQuranAyahItem> list) {
        if (list == null || list.isEmpty()) return;
        Set<String> set = prefs.getStringSet(KEY_BOOKMARKED_AYAHS, null);
        for (BanglaQuranAyahItem item : list) {
            String key = makeAyahKey(item.getSurahNumber(), item.getAyahNumber());
            item.setBookmarked(set != null && set.contains(key));
        }
    }
}
