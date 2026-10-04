package com.devflux.deenone.core.amal;

import android.content.Context;
import android.content.SharedPreferences;

import com.devflux.deenone.core.gamification.GamificationManager;

import java.security.MessageDigest;
import java.util.UUID;

public class AuthoritativePointsLedgerManager {

    private static final String PREFS_LEDGER = "deanone_points_ledger_prefs";
    private static final String KEY_DAILY_POINTS_PREFIX = "daily_pts_";
    private static final String KEY_LIFETIME_POINTS = "lifetime_points_total";
    private static final String KEY_COMPLETED_TRANSACTIONS = "completed_tx_set";
    private static final String SALT = "DeenOne_Islamic_Ledger_Salt_9988_";

    public static class PointAwardResult {
        public final boolean success;
        public final int pointsAwarded;
        public final String transactionId;
        public final String message;

        public PointAwardResult(boolean success, int pointsAwarded, String transactionId, String message) {
            this.success = success;
            this.pointsAwarded = pointsAwarded;
            this.transactionId = transactionId;
            this.message = message;
        }
    }

    public static synchronized PointAwardResult recordAmalCompletion(Context context, String userId, String amalCode, int points) {
        if (context == null || amalCode == null || points <= 0) {
            return new PointAwardResult(false, 0, null, "অবৈধ আমল বা পয়েন্ট তথ্য");
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS_LEDGER, Context.MODE_PRIVATE);
        String todayDate = UserLocationTimezoneHelper.getTodayLocationDateString(context);
        String deduplicationKey = "done_" + (userId != null ? userId : "guest") + "_" + todayDate + "_" + amalCode;

        // Anti-Abuse: prevent double point claim on same day
        if (prefs.getBoolean(deduplicationKey, false)) {
            return new PointAwardResult(false, 0, null, "এই আমলটি ইতিমধ্যে আজ সম্পন্ন করা হয়েছে");
        }

        String txId = UUID.randomUUID().toString();
        long now = UserLocationTimezoneHelper.getAuthoritativeCurrentTimeMillis(context);
        String signature = generateSignature(userId, amalCode, points, now, txId);

        // Update Daily & Lifetime Points once atomically
        int currentDaily = getTodayPoints(context);
        int newDaily = currentDaily + points;
        int currentLifetime = getLifetimePoints(context);
        int newLifetime = currentLifetime + points;

        prefs.edit()
                .putInt(KEY_DAILY_POINTS_PREFIX + todayDate, newDaily)
                .putInt(KEY_LIFETIME_POINTS, newLifetime)
                .putBoolean(deduplicationKey, true)
                .putString("tx_sig_" + txId, signature)
                .apply();

        // Synchronize with GamificationManager total XP (single source of truth)
        GamificationManager.setTotalXP(context, newLifetime);

        return new PointAwardResult(true, points, txId, "পয়েন্ট সফলভাবে যুক্ত হয়েছে");
    }

    public static int getTodayPoints(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_LEDGER, Context.MODE_PRIVATE);
        String todayDate = UserLocationTimezoneHelper.getTodayLocationDateString(context);
        return prefs.getInt(KEY_DAILY_POINTS_PREFIX + todayDate, 0);
    }

    public static int getLifetimePoints(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_LEDGER, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_LIFETIME_POINTS, GamificationManager.getTotalXP(context));
    }

    public static synchronized void syncInitialPoints(Context context, int points) {
        if (context == null || points <= 0) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_LEDGER, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_LIFETIME_POINTS, 0);
        if (points > current) {
            prefs.edit().putInt(KEY_LIFETIME_POINTS, points).apply();
        }
        GamificationManager.setTotalXP(context, points);
    }

    public static synchronized void addPoints(Context context, int points) {
        if (context == null || points <= 0) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_LEDGER, Context.MODE_PRIVATE);
        String todayDate = UserLocationTimezoneHelper.getTodayLocationDateString(context);
        int currentDaily = getTodayPoints(context);
        int currentLifetime = getLifetimePoints(context);
        int newDaily = currentDaily + points;
        int newLifetime = currentLifetime + points;
        prefs.edit()
                .putInt(KEY_DAILY_POINTS_PREFIX + todayDate, newDaily)
                .putInt(KEY_LIFETIME_POINTS, newLifetime)
                .apply();
        GamificationManager.setTotalXP(context, newLifetime);
    }

    public static int getWeeklyPoints(Context context) {
        if (context == null) return 0;
        int lifetime = getLifetimePoints(context);
        if (lifetime <= 0) return getTodayPoints(context);
        return Math.max(getTodayPoints(context), lifetime);
    }

    private static String generateSignature(String userId, String amalCode, int points, long timestamp, String txId) {
        try {
            String raw = userId + ":" + amalCode + ":" + points + ":" + timestamp + ":" + txId + ":" + SALT;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return "fallback_sig_" + timestamp;
        }
    }
}
