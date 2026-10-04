package com.devflux.deenone.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.AyahDao;
import com.devflux.deenone.data.local.dao.BookmarkDao;
import com.devflux.deenone.data.local.dao.SurahDao;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranBookmarkEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuranRepository {

    private static final String TAG = "QuranRepository";
    private static volatile QuranRepository instance;
    private final SurahDao surahDao;
    private final AyahDao ayahDao;
    private final BookmarkDao bookmarkDao;

    public static synchronized QuranRepository getInstance(Context context) {
        if (instance == null) {
            instance = new QuranRepository(context.getApplicationContext());
        }
        return instance;
    }

    public QuranRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.surahDao = db.surahDao();
        this.ayahDao = db.ayahDao();
        this.bookmarkDao = db.bookmarkDao();
    }

    public LiveData<List<QuranSurahEntity>> getAllSurahs() {
        return surahDao.getAllSurahs();
    }

    public LiveData<QuranSurahEntity> getSurahByNumber(int surahNumber) {
        return surahDao.getSurahByNumber(surahNumber);
    }

    public QuranSurahEntity getSurahByNumberSync(int surahNumber) {
        return surahDao.getSurahByNumberSync(surahNumber);
    }

    public LiveData<List<QuranSurahEntity>> searchSurahs(String query) {
        int numberQuery = -1;
        try {
            numberQuery = Integer.parseInt(query.trim());
        } catch (Exception ignored) {}
        return surahDao.searchSurahs(query, numberQuery);
    }

    public LiveData<List<QuranSurahEntity>> getFavoriteSurahs() {
        return surahDao.getFavoriteSurahs();
    }

    public LiveData<QuranSurahEntity> getLastReadSurah() {
        return surahDao.getLastReadSurah();
    }

    public QuranSurahEntity getLastReadSurahSync() {
        return surahDao.getLastReadSurahSync();
    }

    public LiveData<List<QuranAyahEntity>> getAyahsForSurah(int surahNumber) {
        fetchAyahsFromNetworkIfMissing(surahNumber);
        return ayahDao.getAyahsForSurah(surahNumber);
    }

    public LiveData<List<QuranAyahEntity>> getBookmarkedAyahs() {
        return ayahDao.getBookmarkedAyahs();
    }

    public LiveData<List<QuranAyahEntity>> searchAyahs(String query) {
        return ayahDao.searchAyahs(query);
    }

    public void updateLastRead(int surahNumber, int ayahNumber) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            surahDao.updateLastReadPosition(surahNumber, ayahNumber, System.currentTimeMillis());
        });
    }

    public void setAyahBookmarked(int surahNumber, int ayahNumber, boolean isBookmarked, String surahName, String ayahSnippet) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            ayahDao.setAyahBookmarked(surahNumber, ayahNumber, isBookmarked);
            if (isBookmarked) {
                bookmarkDao.insertBookmark(new QuranBookmarkEntity(surahNumber, ayahNumber, surahName, ayahSnippet, System.currentTimeMillis()));
            } else {
                bookmarkDao.removeBookmark(surahNumber, ayahNumber);
            }
        });
    }

    public void toggleSurahFavorite(int surahNumber, boolean isFavorite) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            surahDao.updateFavorite(surahNumber, isFavorite);
        });
    }

    public static String sanitizeArabicVerse(int surahNumber, int numInSurah, String text) {
        if (text == null) return "";
        String clean = text.replace("\uFEFF", "").trim();
        if (surahNumber > 1 && numInSurah == 1) {
            // Tanzil / AlQuran.cloud prepends Bismillah to Ayah 1 of all surahs > 1
            String strippedRegex = clean.replaceAll("^[\\s\\uFEFF]*بِسْمِ[\\s\\S]*?رَّحِيمِ[\\s]*", "").trim();
            if (!strippedRegex.isEmpty()) {
                clean = strippedRegex;
            }
            String[] prefixes = {
                "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ",
                "بِسْمِ اللهِ الرَّحْمَنِ الرَّحِيمِ",
                "بِسْمِ اللَّهِ الرَّحْمٰنِ الرَّحِيمِ"
            };
            for (String prefix : prefixes) {
                if (clean.startsWith(prefix)) {
                    String stripped = clean.substring(prefix.length()).trim();
                    if (!stripped.isEmpty()) {
                        clean = stripped;
                        break;
                    }
                }
            }
        }
        return clean;
    }

    public void fetchAyahsFromNetworkIfMissing(int surahNumber) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            QuranSurahEntity surah = surahDao.getSurahByNumberSync(surahNumber);
            int expectedCount = surah != null ? surah.getNumberOfAyahs() : 0;
            List<QuranAyahEntity> existing = ayahDao.getAyahsForSurahSync(surahNumber);
            if (existing != null && expectedCount > 0 && existing.size() >= expectedCount) {
                if (surahNumber > 1 && !existing.isEmpty()) {
                    QuranAyahEntity first = existing.get(0);
                    if (first.getAyahNumber() == 1) {
                        String sanitized = sanitizeArabicVerse(surahNumber, 1, first.getTextArabic());
                        if (!sanitized.equals(first.getTextArabic())) {
                            first.setTextArabic(sanitized);
                            ayahDao.insertAyahs(java.util.Collections.singletonList(first));
                        }
                    }
                }
                return; // Already cached full surah locally in Room
            }

            List<QuranAyahEntity> fetchedAyahs = fetchFromTanzilCloud(surahNumber);
            if (fetchedAyahs == null || fetchedAyahs.isEmpty() || (expectedCount > 0 && fetchedAyahs.size() < expectedCount)) {
                // Fallback to jsDelivr / Fawaz Ahmed API
                fetchedAyahs = fetchFromJsDelivrCdn(surahNumber);
            }
            if (fetchedAyahs == null || fetchedAyahs.isEmpty() || (expectedCount > 0 && fetchedAyahs.size() < expectedCount)) {
                // Fallback to Quran.com API v4
                fetchedAyahs = fetchFromQuranComApi(surahNumber);
            }

            if (fetchedAyahs != null && !fetchedAyahs.isEmpty()) {
                ayahDao.insertAyahs(fetchedAyahs);
                Log.d(TAG, "Successfully cached " + fetchedAyahs.size() + " ayahs for Surah " + surahNumber);
            }
        });
    }

    private List<QuranAyahEntity> fetchFromTanzilCloud(int surahNumber) {
        try {
            // Source: Tanzil Project (tanzil.net) Uthmani text & quran-api translations
            String urlStr = "https://api.alquran.cloud/v1/surah/" + surahNumber + "/editions/quran-uthmani,bn.bengali,en.sahih";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-App/1.0");
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

                JSONObject root = new JSONObject(sb.toString());
                JSONArray editions = root.getJSONArray("data");
                if (editions.length() >= 3) {
                    JSONArray arabicAyahs = editions.getJSONObject(0).getJSONArray("ayahs");
                    JSONArray bnAyahs = editions.getJSONObject(1).getJSONArray("ayahs");
                    JSONArray enAyahs = editions.getJSONObject(2).getJSONArray("ayahs");

                    List<QuranAyahEntity> newAyahs = new ArrayList<>();
                    for (int i = 0; i < arabicAyahs.length(); i++) {
                        JSONObject arObj = arabicAyahs.getJSONObject(i);
                        int numInSurah = arObj.getInt("numberInSurah");
                        String arText = sanitizeArabicVerse(surahNumber, numInSurah, arObj.getString("text"));
                        int juz = arObj.optInt("juz", 1);
                        int page = arObj.optInt("page", 1);

                        String bnText = i < bnAyahs.length() ? bnAyahs.getJSONObject(i).getString("text") : "";
                        String enText = i < enAyahs.length() ? enAyahs.getJSONObject(i).getString("text") : "";
                        String audioUrl = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surahNumber, numInSurah);

                        newAyahs.add(new QuranAyahEntity(
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
                    return newAyahs;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Tanzil cloud fetch failed: " + e.getMessage());
        }
        return null;
    }

    private List<QuranAyahEntity> fetchFromJsDelivrCdn(int surahNumber) {
        try {
            // Source: jsDelivr Fawaz Ahmed Quran API
            String bnUrlStr = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ben-muhiuddinkhan/" + surahNumber + ".json";
            String arUrlStr = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/ara-quranuthmani/" + surahNumber + ".json";
            String enUrlStr = "https://cdn.jsdelivr.net/gh/fawazahmed0/quran-api@1/editions/eng-sahih/" + surahNumber + ".json";

            String bnJson = fetchHttpString(bnUrlStr);
            String arJson = fetchHttpString(arUrlStr);
            String enJson = fetchHttpString(enUrlStr);

            if (bnJson != null && arJson != null) {
                JSONArray bnArr = new JSONObject(bnJson).getJSONArray("chapter");
                JSONArray arArr = new JSONObject(arJson).getJSONArray("chapter");
                JSONArray enArr = enJson != null ? new JSONObject(enJson).optJSONArray("chapter") : null;

                List<QuranAyahEntity> list = new ArrayList<>();
                int length = arArr.length();
                for (int i = 0; i < length; i++) {
                    JSONObject arObj = arArr.getJSONObject(i);
                    int verseNum = arObj.optInt("verse", i + 1);
                    String arText = sanitizeArabicVerse(surahNumber, verseNum, arObj.optString("text", ""));

                    String bnText = "";
                    if (i < bnArr.length()) {
                        bnText = bnArr.getJSONObject(i).optString("text", "");
                    }

                    String enText = "";
                    if (enArr != null && i < enArr.length()) {
                        enText = enArr.getJSONObject(i).optString("text", "");
                    }

                    String audioUrl = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surahNumber, verseNum);

                    list.add(new QuranAyahEntity(
                            surahNumber,
                            verseNum,
                            arText,
                            bnText,
                            enText,
                            "",
                            audioUrl,
                            1,
                            1
                    ));
                }
                return list;
            }
        } catch (Exception e) {
            Log.w(TAG, "jsDelivr fetch failed: " + e.getMessage());
        }
        return null;
    }

    private String fetchHttpString(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-App/1.0");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(8000);
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

    private List<QuranAyahEntity> fetchFromQuranComApi(int surahNumber) {
        try {
            // Source: Quran.com API v4 (api.quran.com) with per_page=300 to fetch full surah
            String urlStr = "https://api.quran.com/api/v4/verses/by_chapter/" + surahNumber + "?language=bn&words=false&translations=161,20&fields=text_uthmani,chapter_id,verse_number,juz_number,page_number&per_page=300";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "DeenOne-App/1.0");
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

                JSONObject root = new JSONObject(sb.toString());
                JSONArray verses = root.optJSONArray("verses");
                if (verses != null && verses.length() > 0) {
                    List<QuranAyahEntity> newAyahs = new ArrayList<>();
                    for (int i = 0; i < verses.length(); i++) {
                        JSONObject vObj = verses.getJSONObject(i);
                        int numInSurah = vObj.optInt("verse_number", i + 1);
                        String arText = sanitizeArabicVerse(surahNumber, numInSurah, vObj.optString("text_uthmani", ""));
                        int juz = vObj.optInt("juz_number", 1);
                        int page = vObj.optInt("page_number", 1);

                        String bnText = "";
                        String enText = "";
                        JSONArray translations = vObj.optJSONArray("translations");
                        if (translations != null) {
                            for (int t = 0; t < translations.length(); t++) {
                                JSONObject tr = translations.getJSONObject(t);
                                int resId = tr.optInt("resource_id", 0);
                                String text = tr.optString("text", "").replaceAll("<[^>]*>", "").trim();
                                if (resId == 161 || resId == 213) {
                                    bnText = text;
                                } else {
                                    enText = text;
                                }
                            }
                        }

                        String audioUrl = QuranCdnAudioHelper.getQuranComAudioUrl(surahNumber, numInSurah);

                        newAyahs.add(new QuranAyahEntity(
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
                    return newAyahs;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Quran.com API fetch failed: " + e.getMessage());
        }
        return null;
    }
}
