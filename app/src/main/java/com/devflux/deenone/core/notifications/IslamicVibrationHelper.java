package com.devflux.deenone.core.notifications;

import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;

public class IslamicVibrationHelper {

    private static final String TAG = "IslamicVibrationHelper";

    public enum PatternType {
        NORMAL(new long[]{0, 500, 200, 500}),
        STRONG_ALARM(new long[]{0, 800, 300, 800, 300, 1000, 300, 1000}),
        DOUBLE_PULSE(new long[]{0, 300, 150, 300}),
        LONG_PULSE(new long[]{0, 1500, 400, 1500});

        public final long[] timings;

        PatternType(long[] timings) {
            this.timings = timings;
        }
    }

    public static void triggerVibration(Context context, PatternType patternType, boolean isAlarm) {
        if (context == null) return;

        // Check if vibration is globally enabled
        if (!NotificationSettingsManager.isVibrationEnabled(context)) {
            Log.d(TAG, "Vibration is disabled in user settings.");
            return;
        }

        try {
            Vibrator vibrator;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager != null ? vibratorManager.getDefaultVibrator() : null;
            } else {
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }

            if (vibrator == null || !vibrator.hasVibrator()) {
                Log.d(TAG, "Device does not have vibration hardware.");
                return;
            }

            long[] pattern = patternType != null ? patternType.timings : PatternType.NORMAL.timings;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int[] amplitudes = new int[pattern.length];
                for (int i = 0; i < pattern.length; i++) {
                    amplitudes[i] = (i % 2 == 0) ? 0 : 255; // Full strength on pulses
                }

                VibrationEffect effect = VibrationEffect.createWaveform(pattern, amplitudes, -1);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    AudioAttributes attributes = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(isAlarm ? AudioAttributes.USAGE_ALARM : AudioAttributes.USAGE_NOTIFICATION)
                            .build();
                    vibrator.vibrate(effect, attributes);
                } else {
                    vibrator.vibrate(effect);
                }
            } else {
                vibrator.vibrate(pattern, -1);
            }

            Log.d(TAG, "Vibration triggered successfully with pattern: " + (patternType != null ? patternType.name() : "NORMAL"));

        } catch (Exception e) {
            Log.e(TAG, "Error triggering device vibration: " + e.getMessage());
        }
    }

    public static void cancelVibration(Context context) {
        if (context == null) return;
        try {
            Vibrator vibrator;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                vibrator = vibratorManager != null ? vibratorManager.getDefaultVibrator() : null;
            } else {
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            }
            if (vibrator != null) {
                vibrator.cancel();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cancelling vibration: " + e.getMessage());
        }
    }
}