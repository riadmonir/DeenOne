package com.devflux.deenone.core.quran;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * High-performance, persistent state manager for Hafiz's daily routines:
 * - Sabaq (সবক - New Daily Lesson)
 * - Sabaqi (সবকী - Recent Revision)
 * - Muraja'ah / Dor (মুরাজাআ - 30 Para Revision Cycle)
 * - Mutashabihat & Mistakes Marker (আটকে যাওয়া আয়াতের তালিকা)
 * - Hifz Mask Mode & Audio Loop Settings
 */
public class HifzProgressManager {

    private static final String PREF_NAME = "deenone_hifz_prefs";
    private static volatile HifzProgressManager instance;
    private final SharedPreferences prefs;

    // Keys
    private static final String KEY_SABAQ_PARA = "key_sabaq_para";
    private static final String KEY_SABAQ_PAGE = "key_sabaq_page";
    private static final String KEY_SABAQ_TARGET_PAGES = "key_sabaq_target_pages";
    private static final String KEY_LAST_SABAQ_DATE = "key_last_sabaq_date";
    private static final String KEY_SABAQI_DONE_DATE = "key_sabaqi_done_date";
    private static final String KEY_MURAJAAH_CURRENT_PARA = "key_murajaah_current_para";
    private static final String KEY_MURAJAAH_DAILY_TARGET = "key_murajaah_daily_target";
    private static final String KEY_MURAJAAH_COMPLETED_DATE = "key_murajaah_completed_date";
    private static final String KEY_MEMORIZED_PARAS_SET = "key_memorized_paras_set";
    private static final String KEY_MISTAKES_JSON = "key_mistakes_json";
    private static final String KEY_MASK_MODE = "key_mask_mode";
    private static final String KEY_LOOP_COUNT = "key_loop_count";
    private static final String KEY_STREAK_DAYS = "key_streak_days";
    private static final String KEY_LAST_ACTIVE_DATE = "key_last_active_date";

    public static synchronized HifzProgressManager getInstance(Context context) {
        if (instance == null) {
            instance = new HifzProgressManager(context.getApplicationContext());
        }
        return instance;
    }

    private HifzProgressManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private String getTodayString() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    // ==========================================
    // 1. Sabaq (সবক)
    // ==========================================
    public int getSabaqPara() {
        return prefs.getInt(KEY_SABAQ_PARA, 1);
    }

    public void setSabaqPara(int para) {
        prefs.edit().putInt(KEY_SABAQ_PARA, Math.max(1, Math.min(30, para))).apply();
    }

    public int getSabaqPage() {
        return prefs.getInt(KEY_SABAQ_PAGE, 1);
    }

    public void setSabaqPage(int page) {
        prefs.edit().putInt(KEY_SABAQ_PAGE, Math.max(1, Math.min(604, page))).apply();
    }

    public int getSabaqTargetPages() {
        return prefs.getInt(KEY_SABAQ_TARGET_PAGES, 1);
    }

    public void setSabaqTargetPages(int count) {
        prefs.edit().putInt(KEY_SABAQ_TARGET_PAGES, Math.max(1, count)).apply();
    }

    public boolean isTodaySabaqDone() {
        return getTodayString().equals(prefs.getString(KEY_LAST_SABAQ_DATE, ""));
    }

    public void setTodaySabaqDone(boolean done) {
        if (done) {
            prefs.edit().putString(KEY_LAST_SABAQ_DATE, getTodayString()).apply();
            updateStreak();
        } else {
            prefs.edit().remove(KEY_LAST_SABAQ_DATE).apply();
        }
    }

    // ==========================================
    // 2. Sabaqi (সবকী)
    // ==========================================
    public boolean isTodaySabaqiDone() {
        return getTodayString().equals(prefs.getString(KEY_SABAQI_DONE_DATE, ""));
    }

    public void setTodaySabaqiDone(boolean done) {
        if (done) {
            prefs.edit().putString(KEY_SABAQI_DONE_DATE, getTodayString()).apply();
            updateStreak();
        } else {
            prefs.edit().remove(KEY_SABAQI_DONE_DATE).apply();
        }
    }

    // ==========================================
    // 3. Muraja'ah (মুরাজাআ / মনজিল দৌড়)
    // ==========================================
    public int getMurajaahCurrentPara() {
        return prefs.getInt(KEY_MURAJAAH_CURRENT_PARA, 1);
    }

    public void setMurajaahCurrentPara(int para) {
        prefs.edit().putInt(KEY_MURAJAAH_CURRENT_PARA, Math.max(1, Math.min(30, para))).apply();
    }

    public int getMurajaahDailyTarget() {
        return prefs.getInt(KEY_MURAJAAH_DAILY_TARGET, 1);
    }

    public void setMurajaahDailyTarget(int count) {
        prefs.edit().putInt(KEY_MURAJAAH_DAILY_TARGET, Math.max(1, count)).apply();
    }

    public boolean isTodayMurajaahDone() {
        return getTodayString().equals(prefs.getString(KEY_MURAJAAH_COMPLETED_DATE, ""));
    }

    public void completeTodayMurajaah() {
        int next = getMurajaahCurrentPara() + getMurajaahDailyTarget();
        if (next > 30) next = (next % 30 == 0) ? 30 : (next % 30);
        prefs.edit()
                .putString(KEY_MURAJAAH_COMPLETED_DATE, getTodayString())
                .putInt(KEY_MURAJAAH_CURRENT_PARA, next)
                .apply();
        updateStreak();
    }

    // ==========================================
    // 4. Memorized Paras Progress
    // ==========================================
    public boolean isParaMemorized(int para) {
        Set<String> set = prefs.getStringSet(KEY_MEMORIZED_PARAS_SET, null);
        return set != null && set.contains(String.valueOf(para));
    }

    public void setParaMemorized(int para, boolean memorized) {
        Set<String> oldSet = prefs.getStringSet(KEY_MEMORIZED_PARAS_SET, new HashSet<>());
        Set<String> newSet = new HashSet<>(oldSet);
        if (memorized) {
            newSet.add(String.valueOf(para));
        } else {
            newSet.remove(String.valueOf(para));
        }
        prefs.edit().putStringSet(KEY_MEMORIZED_PARAS_SET, newSet).apply();
    }

    public int getMemorizedParasCount() {
        Set<String> set = prefs.getStringSet(KEY_MEMORIZED_PARAS_SET, null);
        return set != null ? set.size() : 0;
    }

    public int getOverallCompletionPercent() {
        return (getMemorizedParasCount() * 100) / 30;
    }

    // ==========================================
    // 5. Mistakes & Mutashabihat Tracker
    // ==========================================
    public static class HifzMistakeItem {
        public final int surahNumber;
        public final int ayahNumber;
        public final String surahNameBn;
        public final String surahNameEn;
        public final String textArabic;
        public final String note;
        public final long timestamp;

        public HifzMistakeItem(int surahNumber, int ayahNumber, String surahNameBn, String surahNameEn, String textArabic, String note, long timestamp) {
            this.surahNumber = surahNumber;
            this.ayahNumber = ayahNumber;
            this.surahNameBn = surahNameBn;
            this.surahNameEn = surahNameEn;
            this.textArabic = textArabic;
            this.note = note;
            this.timestamp = timestamp;
        }
    }

    public synchronized List<HifzMistakeItem> getAllMarkedMistakes() {
        List<HifzMistakeItem> list = new ArrayList<>();
        String jsonStr = prefs.getString(KEY_MISTAKES_JSON, "[]");
        try {
            JSONArray arr = new JSONArray(jsonStr);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                list.add(new HifzMistakeItem(
                        obj.getInt("surahNumber"),
                        obj.getInt("ayahNumber"),
                        obj.optString("surahNameBn", ""),
                        obj.optString("surahNameEn", ""),
                        obj.optString("textArabic", ""),
                        obj.optString("note", ""),
                        obj.optLong("timestamp", System.currentTimeMillis())
                ));
            }
        } catch (Exception ignored) {}
        return list;
    }

    public synchronized boolean isAyahMarkedMistake(int surahNumber, int ayahNumber) {
        List<HifzMistakeItem> list = getAllMarkedMistakes();
        for (HifzMistakeItem item : list) {
            if (item.surahNumber == surahNumber && item.ayahNumber == ayahNumber) {
                return true;
            }
        }
        return false;
    }

    public synchronized void toggleAyahMistake(int surahNumber, int ayahNumber, String surahNameBn, String surahNameEn, String textArabic, String note) {
        List<HifzMistakeItem> list = getAllMarkedMistakes();
        int foundIdx = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).surahNumber == surahNumber && list.get(i).ayahNumber == ayahNumber) {
                foundIdx = i;
                break;
            }
        }

        if (foundIdx >= 0) {
            list.remove(foundIdx);
        } else {
            list.add(new HifzMistakeItem(surahNumber, ayahNumber, surahNameBn, surahNameEn, textArabic, note, System.currentTimeMillis()));
        }

        // Save back
        JSONArray arr = new JSONArray();
        for (HifzMistakeItem item : list) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("surahNumber", item.surahNumber);
                obj.put("ayahNumber", item.ayahNumber);
                obj.put("surahNameBn", item.surahNameBn);
                obj.put("surahNameEn", item.surahNameEn);
                obj.put("textArabic", item.textArabic);
                obj.put("note", item.note);
                obj.put("timestamp", item.timestamp);
                arr.put(obj);
            } catch (Exception ignored) {}
        }
        prefs.edit().putString(KEY_MISTAKES_JSON, arr.toString()).apply();
    }

    // ==========================================
    // 6. Mask Mode & Repetition Settings
    // ==========================================
    public boolean isMaskMode() {
        return prefs.getBoolean(KEY_MASK_MODE, false);
    }

    public void setMaskMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_MASK_MODE, enabled).apply();
    }

    public int getLoopCount() {
        return prefs.getInt(KEY_LOOP_COUNT, 3);
    }

    public void setLoopCount(int count) {
        prefs.edit().putInt(KEY_LOOP_COUNT, count).apply();
    }

    // ==========================================
    // 7. Streak
    // ==========================================
    public int getStreakDays() {
        return prefs.getInt(KEY_STREAK_DAYS, 1);
    }

    private void updateStreak() {
        String today = getTodayString();
        String lastActive = prefs.getString(KEY_LAST_ACTIVE_DATE, "");
        if (today.equals(lastActive)) return;

        int currentStreak = prefs.getInt(KEY_STREAK_DAYS, 0);
        prefs.edit()
                .putString(KEY_LAST_ACTIVE_DATE, today)
                .putInt(KEY_STREAK_DAYS, currentStreak + 1)
                .apply();
    }
}
