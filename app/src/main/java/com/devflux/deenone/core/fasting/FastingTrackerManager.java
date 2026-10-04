package com.devflux.deenone.core.fasting;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class FastingTrackerManager {

    private static final String PREF_FASTING = "deanone_fasting_tracker_prefs";
    private static final String KEY_COMPLETED_DAYS = "key_completed_fasting_days";
    private static final String KEY_QAZA_TOTAL = "key_qaza_fasts_total";
    private static final String KEY_QAZA_COMPLETED = "key_qaza_fasts_completed";
    private static final String KEY_FASTING_NOTE_PREFIX = "key_note_";

    public enum FastingType {
        RAMADAN("ফরজ রমজান রোজা", "পবিত্র রমজান মাসের ফরজ সিয়াম"),
        MONDAY_SUNNAH("সোমবারের সুন্নাত রোজা", "নবীজী (ﷺ) সোমবারে রোজা রাখতেন ও বলতেন: এই দিনে আমার জন্ম হয়েছে ও ওহী নাযিল হয়েছে (সহীহ মুসলিম: ১১৬২)"),
        THURSDAY_SUNNAH("বৃহস্পতিবারের সুন্নাত রোজা", "বৃহস্পতিবারে আমল আল্লাহর কাছে পেশ করা হয় (জামে তিরমিযী: ৭৪৭)"),
        AYYAM_AL_BID("আইয়ামে বীজ (১৩, ১৪, ১৫ হিজরি)", "প্রতি চান্দ্র মাসের ১৩, ১৪ ও ১৫ তারিখের ৩টি রোজা সারাবছর রোজা রাখার সমতুল্য (সহীহ বুখারী: ১৯৮১)"),
        ARAFAH("আরাফার দিনের রোজা (৯ জিলহজ)", "বিগত এক বছর ও আগামী এক বছরের গুনাহের কাফফারা (সহীহ মুসলিম: ১১৬২)"),
        ASHURA("আশুরার রোজা (৯ ও ১০ মুহাররম)", "বিগত এক বছরের গুনাহের কাফফারা (সহীহ মুসলিম: ১১৬২)"),
        SHAWWAL_SIX("শাওয়ালের ৬ রোজা", "রমজানের পর শাওয়ালের ৬ রোজা রাখলে পূর্ণ এক বছর রোজা রাখার সওয়াব (সহীহ মুসলিম: ১১৬৪)"),
        QAZA("কাজা রোজা পূরণ", "অতীতের ছুটে যাওয়া ফরজ রোজার কাজা আদায়");

        public final String title;
        public final String description;

        FastingType(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    private static volatile FastingTrackerManager instance;

    public static FastingTrackerManager getInstance() {
        if (instance == null) {
            synchronized (FastingTrackerManager.class) {
                if (instance == null) {
                    instance = new FastingTrackerManager();
                }
            }
        }
        return instance;
    }

    public static String getTodayKey() {
        return new SimpleDateFormat("yyyy_MM_dd", Locale.US).format(new Date());
    }

    public static String getDateKey(Calendar cal) {
        if (cal == null) return getTodayKey();
        return new SimpleDateFormat("yyyy_MM_dd", Locale.US).format(cal.getTime());
    }

    public static String getDateKey(int year, int monthZeroBased, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthZeroBased, dayOfMonth);
        return getDateKey(cal);
    }

    // 3-State Roza Tracking Status Constants
    public static final int STATUS_UNMARKED = 0;   // Yellow circular indicator (Pending / Unmarked)
    public static final int STATUS_FASTED = 1;     // Green circular indicator (Confirmed Fasted)
    public static final int STATUS_NOT_FASTED = 2; // Red circular indicator (Not Fasting / Missed)

    private static final String PREF_STATUS_PREFIX = "roza_status_";

    public int getFastingStatus(Context context, String dateKey) {
        if (context == null || dateKey == null) return STATUS_UNMARKED;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        if (prefs.contains(PREF_STATUS_PREFIX + dateKey)) {
            return prefs.getInt(PREF_STATUS_PREFIX + dateKey, STATUS_UNMARKED);
        }
        // Fallback for previous data in KEY_COMPLETED_DAYS
        Set<String> completed = prefs.getStringSet(KEY_COMPLETED_DAYS, new HashSet<>());
        if (completed.contains(dateKey)) {
            return STATUS_FASTED;
        }
        return STATUS_UNMARKED;
    }

    public void setFastingStatus(Context context, String dateKey, int status) {
        if (context == null || dateKey == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_STATUS_PREFIX + dateKey, status);

        // Keep KEY_COMPLETED_DAYS in sync for backward compatibility
        Set<String> completed = new HashSet<>(prefs.getStringSet(KEY_COMPLETED_DAYS, new HashSet<>()));
        if (status == STATUS_FASTED) {
            completed.add(dateKey);
        } else {
            completed.remove(dateKey);
        }
        editor.putStringSet(KEY_COMPLETED_DAYS, completed);
        editor.apply();
    }

    public boolean isFastingDate(Context context, String dateKey) {
        return getFastingStatus(context, dateKey) == STATUS_FASTED;
    }

    public void setFastingDate(Context context, String dateKey, boolean isFasting) {
        setFastingStatus(context, dateKey, isFasting ? STATUS_FASTED : STATUS_NOT_FASTED);
    }

    public int getTotalCompletedCount(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        Set<String> completed = prefs.getStringSet(KEY_COMPLETED_DAYS, new HashSet<>());
        return completed.size();
    }

    public boolean isFastingToday(Context context) {
        return isFastingDate(context, getTodayKey());
    }

    public void setFastingToday(Context context, boolean isFasting) {
        setFastingDate(context, getTodayKey(), isFasting);
    }

    public int getMonthlyCompletedCount(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        Set<String> completed = prefs.getStringSet(KEY_COMPLETED_DAYS, new HashSet<>());
        String currentMonthPrefix = new SimpleDateFormat("yyyy_MM", Locale.US).format(new Date());
        int count = 0;
        for (String key : completed) {
            if (key.startsWith(currentMonthPrefix)) {
                count++;
            }
        }
        return count;
    }

    public int getYearlyCompletedCount(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        Set<String> completed = prefs.getStringSet(KEY_COMPLETED_DAYS, new HashSet<>());
        String currentYearPrefix = new SimpleDateFormat("yyyy", Locale.US).format(new Date());
        int count = 0;
        for (String key : completed) {
            if (key.startsWith(currentYearPrefix)) {
                count++;
            }
        }
        return count;
    }

    public int getQazaRemaining(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        int total = prefs.getInt(KEY_QAZA_TOTAL, 0);
        int completed = prefs.getInt(KEY_QAZA_COMPLETED, 0);
        return Math.max(0, total - completed);
    }

    public void addQazaFast(Context context, int count) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        int total = prefs.getInt(KEY_QAZA_TOTAL, 0) + count;
        prefs.edit().putInt(KEY_QAZA_TOTAL, total).apply();
    }

    public void completeOneQazaFast(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        int completed = prefs.getInt(KEY_QAZA_COMPLETED, 0) + 1;
        prefs.edit().putInt(KEY_QAZA_COMPLETED, completed).apply();
    }

    private static String getRamadanDayKey(int dayNumber) {
        return "key_ramadan_day_" + dayNumber;
    }

    public boolean isRamadanDayCompleted(Context context, int dayNumber) {
        if (context == null || dayNumber < 1 || dayNumber > 30) return false;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        return prefs.getBoolean(getRamadanDayKey(dayNumber), false);
    }

    public void setRamadanDayCompleted(Context context, int dayNumber, boolean completed) {
        if (context == null || dayNumber < 1 || dayNumber > 30) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(getRamadanDayKey(dayNumber), completed).apply();
    }

    public int getCompletedRamadanDaysCount(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_FASTING, Context.MODE_PRIVATE);
        int count = 0;
        for (int i = 1; i <= 30; i++) {
            if (prefs.getBoolean(getRamadanDayKey(i), false)) {
                count++;
            }
        }
        return count;
    }
}