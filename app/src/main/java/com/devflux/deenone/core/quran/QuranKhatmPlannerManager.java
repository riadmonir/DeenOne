package com.devflux.deenone.core.quran;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QuranKhatmPlannerManager {

    public static final int TOTAL_QURAN_PAGES = 604;
    private static final String PREF_KHATM = "deanone_quran_khatm_prefs";
    private static final String KEY_PLAN_DAYS = "key_khatm_plan_days";
    private static final String KEY_PAGES_READ = "key_khatm_pages_read";
    private static final String KEY_START_TIMESTAMP = "key_khatm_start_time";
    private static final String KEY_IS_ACTIVE = "key_khatm_is_active";

    private static volatile QuranKhatmPlannerManager instance;

    public static QuranKhatmPlannerManager getInstance() {
        if (instance == null) {
            synchronized (QuranKhatmPlannerManager.class) {
                if (instance == null) {
                    instance = new QuranKhatmPlannerManager();
                }
            }
        }
        return instance;
    }

    public int getPlanDays(Context context) {
        if (context == null) return 30;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_PLAN_DAYS, 30);
    }

    public void setPlanDays(Context context, int days) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_PLAN_DAYS, days).apply();
    }

    public int getPagesRead(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_PAGES_READ, 0);
    }

    public void setPagesRead(Context context, int pages) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_PAGES_READ, Math.min(TOTAL_QURAN_PAGES, Math.max(0, pages))).apply();
    }

    public void addPagesRead(Context context, int count) {
        if (context == null) return;
        int current = getPagesRead(context);
        setPagesRead(context, current + count);
    }

    public boolean isPlanActive(Context context) {
        if (context == null) return false;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_ACTIVE, true);
    }

    public void setPlanActive(Context context, boolean active) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_IS_ACTIVE, active).apply();
    }

    public void resetPlan(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_KHATM, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_PAGES_READ, 0)
                .putLong(KEY_START_TIMESTAMP, System.currentTimeMillis())
                .putBoolean(KEY_IS_ACTIVE, true)
                .apply();
    }

    public int getDailyTargetPages(int planDays) {
        if (planDays <= 0) planDays = 30;
        return (int) Math.ceil((double) TOTAL_QURAN_PAGES / planDays);
    }

    public int getPerPrayerTargetPages(int dailyTarget) {
        return (int) Math.ceil((double) dailyTarget / 5.0);
    }

    public int getProgressPercentage(int pagesRead) {
        return (int) Math.min(100, Math.round(((double) pagesRead / TOTAL_QURAN_PAGES) * 100));
    }

    public String getEstimatedCompletionDate(Context context) {
        int planDays = getPlanDays(context);
        int pagesRead = getPagesRead(context);
        int remaining = Math.max(0, TOTAL_QURAN_PAGES - pagesRead);
        int dailyTarget = getDailyTargetPages(planDays);

        int daysLeft = (int) Math.ceil((double) remaining / dailyTarget);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, daysLeft);
        return new SimpleDateFormat("dd MMMM, yyyy", Locale.US).format(cal.getTime());
    }
}