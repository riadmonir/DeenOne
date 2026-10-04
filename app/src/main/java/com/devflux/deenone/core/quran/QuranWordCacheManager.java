package com.devflux.deenone.core.quran;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.core.localization.LocaleManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuranWordCacheManager {

    private static final String TAG = "QuranWordCacheManager";
    private static final String WBW_DIR_NAME = "quran_wbw";
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface WbwDownloadListener {
        void onProgress(int progressPercent);
        void onSuccess(int surahNumber, String language, List<QuranWordItem> ayahWords);
        void onError(String errorMessage);
    }

    private static File getWbwDirectory(Context context) {
        File dir = new File(context.getFilesDir(), WBW_DIR_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private static File getSurahWbwFile(Context context, int surahNumber, String language) {
        String langCode = (language != null && language.toLowerCase().startsWith("en")) ? "en" : "bn";
        return new File(getWbwDirectory(context), "surah_" + surahNumber + "_" + langCode + ".json");
    }

    public static boolean isSurahWordDataDownloaded(Context context, int surahNumber, String language) {
        if (context == null) return false;
        File file = getSurahWbwFile(context, surahNumber, language);
        return file.exists() && file.length() > 50;
    }

    public static boolean isSurahWordDataDownloaded(Context context, int surahNumber) {
        if (context == null) return false;
        String lang = LocaleManager.isBengali(context) ? "bn" : "en";
        return isSurahWordDataDownloaded(context, surahNumber, lang);
    }

    public static List<QuranWordItem> getAyahWords(Context context, int surahNumber, int ayahNumber, String language) {
        List<QuranWordItem> result = new ArrayList<>();
        if (context == null) return result;

        // Ayah 0 represents canonical Bismillah for Surahs 2 to 114
        if (ayahNumber == 0) {
            return QuranBismillahHelper.getBismillahWords(surahNumber);
        }

        String langCode = (language != null && language.toLowerCase().startsWith("en")) ? "en" : "bn";
        File file = getSurahWbwFile(context, surahNumber, langCode);
        if (!file.exists()) {
            return result;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject root = new JSONObject(sb.toString());
            JSONArray verses = root.optJSONArray("verses");
            if (verses != null) {
                for (int v = 0; v < verses.length(); v++) {
                    JSONObject verseObj = verses.getJSONObject(v);
                    int vNum = verseObj.optInt("verse_number", v + 1);
                    if (vNum == ayahNumber) {
                        JSONArray wordsArr = verseObj.optJSONArray("words");
                        if (wordsArr != null) {
                            for (int w = 0; w < wordsArr.length(); w++) {
                                JSONObject wObj = wordsArr.getJSONObject(w);
                                String charType = wObj.optString("char_type_name", "word");
                                if ("end".equalsIgnoreCase(charType)) {
                                    continue; // Skip verse end rosette
                                }
                                int pos = wObj.optInt("position", w + 1);
                                String ar = wObj.optString("text_uthmani", wObj.optString("text", ""));
                                String meaning = wObj.optString("meaning", "");

                                if (!ar.trim().isEmpty()) {
                                    if ("bn".equals(langCode)) {
                                        result.add(new QuranWordItem(surahNumber, ayahNumber, pos, ar, meaning, ""));
                                    } else {
                                        result.add(new QuranWordItem(surahNumber, ayahNumber, pos, ar, "", meaning));
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading ayah words from cache for Surah " + surahNumber + ", Ayah " + ayahNumber, e);
        }

        return result;
    }

    public static List<QuranWordItem> getAyahWords(Context context, int surahNumber, int ayahNumber) {
        String lang = LocaleManager.isBengali(context) ? "bn" : "en";
        return getAyahWords(context, surahNumber, ayahNumber, lang);
    }

    public static void downloadSurahWords(Context context, int surahNumber, int targetAyahNumber, String language, WbwDownloadListener listener) {
        if (context == null) {
            if (listener != null) listener.onError("Invalid Context");
            return;
        }

        final String langCode = (language != null && language.toLowerCase().startsWith("en")) ? "en" : "bn";

        executor.execute(() -> {
            try {
                if (listener != null) {
                    mainHandler.post(() -> listener.onProgress(20));
                }

                // 1. Fetch word-by-word JSON for selected language from Quran.com API v4
                String urlStr = "https://api.quran.com/api/v4/verses/by_chapter/" + surahNumber
                        + "?language=" + langCode + "&words=true&word_fields=text_uthmani&translation_fields=text&per_page=300";
                
                String jsonStr = fetchHttpString(urlStr);

                if (listener != null) {
                    mainHandler.post(() -> listener.onProgress(75));
                }

                if (jsonStr == null || jsonStr.trim().isEmpty()) {
                    if (listener != null) {
                        mainHandler.post(() -> listener.onError("Network connection failed"));
                    }
                    return;
                }

                // 2. Parse and structure JSON for offline usage
                JSONObject root = new JSONObject(jsonStr);
                JSONArray verses = root.optJSONArray("verses");

                JSONObject structuredRoot = new JSONObject();
                structuredRoot.put("surah_number", surahNumber);
                structuredRoot.put("language", langCode);
                structuredRoot.put("downloaded_at", System.currentTimeMillis());

                JSONArray structuredVerses = new JSONArray();

                if (verses != null) {
                    for (int v = 0; v < verses.length(); v++) {
                        JSONObject vObj = verses.getJSONObject(v);
                        int verseNum = vObj.optInt("verse_number", v + 1);

                        JSONObject structuredVerse = new JSONObject();
                        structuredVerse.put("verse_number", verseNum);

                        JSONArray wordsArr = vObj.optJSONArray("words");
                        JSONArray structuredWords = new JSONArray();

                        if (wordsArr != null) {
                            for (int w = 0; w < wordsArr.length(); w++) {
                                JSONObject wObj = wordsArr.getJSONObject(w);
                                String charType = wObj.optString("char_type_name", "word");
                                int pos = wObj.optInt("position", w + 1);
                                String arText = wObj.optString("text_uthmani", wObj.optString("text", ""));

                                String meaning = "";
                                JSONObject tr = wObj.optJSONObject("translation");
                                if (tr != null) {
                                    meaning = tr.optString("text", "").trim();
                                }

                                JSONObject structuredWord = new JSONObject();
                                structuredWord.put("position", pos);
                                structuredWord.put("char_type_name", charType);
                                structuredWord.put("text_uthmani", arText);
                                structuredWord.put("meaning", meaning);

                                structuredWords.put(structuredWord);
                            }
                        }

                        structuredVerse.put("words", structuredWords);
                        structuredVerses.put(structuredVerse);
                    }
                }

                structuredRoot.put("verses", structuredVerses);

                // 3. Atomic write to local storage
                File outputFile = getSurahWbwFile(context, surahNumber, langCode);
                File tempFile = new File(outputFile.getAbsolutePath() + ".tmp");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(structuredRoot.toString(2).getBytes(StandardCharsets.UTF_8));
                }
                if (tempFile.exists()) {
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    tempFile.renameTo(outputFile);
                }

                // 4. Extract target ayah words
                List<QuranWordItem> targetWords = getAyahWords(context, surahNumber, targetAyahNumber, langCode);

                if (listener != null) {
                    mainHandler.post(() -> {
                        listener.onProgress(100);
                        listener.onSuccess(surahNumber, langCode, targetWords);
                    });
                }

            } catch (Exception e) {
                Log.e(TAG, "Error during WBW download for Surah " + surahNumber + " (" + langCode + ")", e);
                if (listener != null) {
                    mainHandler.post(() -> listener.onError(e.getMessage()));
                }
            }
        });
    }

    private static String fetchHttpString(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(12000);
            conn.setRequestProperty("User-Agent", "DeenOne-App/1.0");

            if (conn.getResponseCode() == 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "HTTP fetch failed for " + urlStr + ": " + e.getMessage());
        }
        return null;
    }

    public static boolean deleteSurahWords(Context context, int surahNumber, String language) {
        if (context == null) return false;
        File file = getSurahWbwFile(context, surahNumber, language);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
