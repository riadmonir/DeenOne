package com.devflux.deenone.core.tasbih;

import android.content.Context;
import android.content.SharedPreferences;

public class TasbihTapProtectionManager {

    private static final String PREFS_NAME = "tasbih_protection_prefs";
    private static final String KEY_COOLDOWN_UNTIL = "cooldown_until_timestamp";
    private static final String KEY_VIOLATION_COUNT = "total_violations_count";

    // Thresholds
    private static final long MIN_HUMAN_INTERVAL_MS = 220; // 220ms realistic min Dhikr speed
    private static final int RAPID_TAP_WARNING_STREAK = 2; // 2 rapid taps trigger warning
    private static final int RAPID_TAP_COOLDOWN_STREAK = 4; // 4 rapid taps trigger 3.5 min cooldown
    private static final long COOLDOWN_DURATION_MS = 210 * 1000L; // 3.5 minutes (210 seconds)

    public enum TapStatus {
        VALID_TAP,
        RAPID_TAP_WARNING,
        COOLDOWN_ACTIVE
    }

    public static class ValidationResult {
        public final TapStatus status;
        public final long remainingCooldownMs;
        public final String warningMessage;

        public ValidationResult(TapStatus status, long remainingCooldownMs, String warningMessage) {
            this.status = status;
            this.remainingCooldownMs = remainingCooldownMs;
            this.warningMessage = warningMessage;
        }
    }

    // In-memory runtime tracking per session
    private static long lastTapTimestamp = 0L;
    private static int consecutiveRapidTaps = 0;
    private static boolean isWarningState = false;

    public static synchronized ValidationResult validateTap(Context context) {
        long now = System.currentTimeMillis();
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

        // 1. Check if persistent cooldown is active
        long remainingCooldown = getRemainingCooldownMs(context);
        if (remainingCooldown > 0) {
            String msg = isBn ? "তাসবিহ সাময়িকভাবে বন্ধ" : "Tasbih temporarily paused";
            return new ValidationResult(TapStatus.COOLDOWN_ACTIVE, remainingCooldown, msg);
        }

        // 2. Calculate tap interval
        long interval = now - lastTapTimestamp;
        lastTapTimestamp = now;

        if (interval < MIN_HUMAN_INTERVAL_MS) {
            consecutiveRapidTaps++;

            // If user violated repeatedly -> Enter Cooldown
            if (consecutiveRapidTaps >= RAPID_TAP_COOLDOWN_STREAK || (isWarningState && consecutiveRapidTaps >= 2)) {
                startCooldown(context, COOLDOWN_DURATION_MS);
                isWarningState = false;
                consecutiveRapidTaps = 0;
                String msg = isBn 
                        ? "অস্বাভাবিক দ্রুত ট্যাপের কারণে তাসবিহ সাময়িকভাবে বন্ধ" 
                        : "Tasbih temporarily paused due to unusually rapid tapping";
                return new ValidationResult(TapStatus.COOLDOWN_ACTIVE, COOLDOWN_DURATION_MS, msg);
            }

            // First warning level
            if (consecutiveRapidTaps >= RAPID_TAP_WARNING_STREAK) {
                isWarningState = true;
                String msg = isBn 
                        ? "অনুগ্রহ করে ধীরে তাসবিহ পড়ুন। দ্রুত ট্যাপ করা যাবে না।" 
                        : "Please recite tasbih slowly. Avoid rapid tapping.";
                return new ValidationResult(TapStatus.RAPID_TAP_WARNING, 0, msg);
            }
        } else {
            // Normal human pace -> reset streak
            if (interval > 500) {
                consecutiveRapidTaps = 0;
                isWarningState = false;
            }
        }

        return new ValidationResult(TapStatus.VALID_TAP, 0, null);
    }

    public static long getRemainingCooldownMs(Context context) {
        if (context == null) return 0L;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long cooldownUntil = prefs.getLong(KEY_COOLDOWN_UNTIL, 0L);
        long now = System.currentTimeMillis();
        return Math.max(0L, cooldownUntil - now);
    }

    public static boolean isCooldownActive(Context context) {
        return getRemainingCooldownMs(context) > 0L;
    }

    public static void startCooldown(Context context, long durationMs) {
        if (context == null) return;
        long until = System.currentTimeMillis() + durationMs;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int violations = prefs.getInt(KEY_VIOLATION_COUNT, 0) + 1;
        prefs.edit()
                .putLong(KEY_COOLDOWN_UNTIL, until)
                .putInt(KEY_VIOLATION_COUNT, violations)
                .apply();
    }

    public static void clearCooldown(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_COOLDOWN_UNTIL).apply();
        consecutiveRapidTaps = 0;
        isWarningState = false;
    }

    public static String formatDuration(long millis) {
        long totalSeconds = millis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
