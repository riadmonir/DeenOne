package com.devflux.deenone.core.gamification;

import android.content.Context;
import android.content.SharedPreferences;

public class GamificationManager {

    private static final String PREF_NAME = "deanone_gamification_prefs";
    private static final String KEY_TOTAL_XP = "key_total_xp";
    private static final String KEY_CURRENT_STREAK = "key_current_streak";
    private static final String KEY_LAST_ACTIVE_DATE = "key_last_active_date";

    private static final String KEY_DAILY_XP_PREFIX = "key_daily_xp_";

    public static int getTotalXP(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_TOTAL_XP, 0);
    }

    public static int getTodayXP(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date());
        return prefs.getInt(KEY_DAILY_XP_PREFIX + today, 0);
    }

    public static void setTotalXP(Context context, int totalXP) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_TOTAL_XP, 0);
        int newTotal = Math.max(current, totalXP);
        prefs.edit().putInt(KEY_TOTAL_XP, newTotal).apply();
        try {
            com.devflux.deenone.core.auth.AuthManager.updateUserPoints(context, newTotal);
        } catch (Exception ignored) {}
    }

    public static void addXP(Context context, int xp) {
        if (context == null || xp <= 0) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_TOTAL_XP, 0);
        int newTotal = Math.max(0, current + xp);
        
        String today = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date());
        int currentDaily = prefs.getInt(KEY_DAILY_XP_PREFIX + today, 0);
        int newDaily = Math.max(0, currentDaily + xp);

        prefs.edit()
                .putInt(KEY_TOTAL_XP, newTotal)
                .putInt(KEY_DAILY_XP_PREFIX + today, newDaily)
                .apply();

        try {
            com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.syncInitialPoints(context, newTotal);
            com.devflux.deenone.core.auth.AuthManager.updateUserPoints(context, newTotal);
        } catch (Exception ignored) {}

        syncToBackend(context, newTotal);
    }

    public static void deductXP(Context context, int xp) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_TOTAL_XP, 0);
        int newTotal = Math.max(0, current - Math.abs(xp));
        prefs.edit().putInt(KEY_TOTAL_XP, newTotal).apply();
        try {
            com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.syncInitialPoints(context, newTotal);
            com.devflux.deenone.core.auth.AuthManager.updateUserPoints(context, newTotal);
        } catch (Exception ignored) {}
        syncToBackend(context, newTotal);
    }

    public static int getLevel(Context context) {
        int totalXP = getTotalXP(context);
        return (totalXP / 500) + 1;
    }

    public static int getStreakDays(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_CURRENT_STREAK, 0);
    }

    public static void setStreakDays(Context context, int streak) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_CURRENT_STREAK, Math.max(0, streak)).apply();
    }

    public static void incrementStreak(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int streak = prefs.getInt(KEY_CURRENT_STREAK, 0);
        int newStreak = streak + 1;
        prefs.edit().putInt(KEY_CURRENT_STREAK, newStreak).apply();
        syncToBackend(context, getTotalXP(context));
        try {
            com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context).syncStreak(newStreak);
        } catch (Exception ignored) {}
    }

    private static void syncToBackend(Context context, int points) {
        try {
            com.devflux.deenone.core.auth.AuthManager.UserSession session =
                    com.devflux.deenone.core.auth.AuthManager.getCurrentSession(context);
            String userId = (session != null && !session.userId.isEmpty()) ? session.userId : "usr_active";
            String userName = (session != null && !session.name.isEmpty()) ? session.name : "ব্যবহারকারী";
            com.devflux.deenone.core.backend.BackendRepository.getInstance(context).submitUserPoints(userId, userName, points, null);
        } catch (Exception ignored) {}
    }
}
