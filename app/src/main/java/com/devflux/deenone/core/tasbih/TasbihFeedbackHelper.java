package com.devflux.deenone.core.tasbih;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.view.View;

public class TasbihFeedbackHelper {

    private static final String PREF_NAME = "tasbih_feedback_prefs";
    private static final String KEY_VIBRATION = "pref_tasbih_vibration";
    private static final String KEY_SOUND = "pref_tasbih_sound";

    private static ToneGenerator toneGenerator;

    static {
        try {
            toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 65);
        } catch (Exception ignored) {}
    }

    public static boolean isVibrationEnabled(Context context) {
        if (context == null) return true;
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_VIBRATION, true);
    }

    public static boolean toggleVibration(Context context) {
        if (context == null) return true;
        boolean newState = !isVibrationEnabled(context);
        setVibrationEnabled(context, newState);
        return newState;
    }

    public static void setVibrationEnabled(Context context, boolean enabled) {
        if (context == null) return;
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_VIBRATION, enabled).apply();
    }

    public static boolean isSoundEnabled(Context context) {
        if (context == null) return false;
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_SOUND, false);
    }

    public static boolean toggleSound(Context context) {
        if (context == null) return false;
        boolean newState = !isSoundEnabled(context);
        setSoundEnabled(context, newState);
        return newState;
    }

    public static void setSoundEnabled(Context context, boolean enabled) {
        if (context == null) return;
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_SOUND, enabled).apply();
    }

    /**
     * Executes crisp, zero-latency tactile vibration and optional audio click on every single tap.
     */
    public static void playTapFeedback(Context context, View view) {
        if (context == null) return;

        // 1. Tactile Haptic Vibration
        if (isVibrationEnabled(context)) {
            // A. View-level hardware haptic trigger
            if (view != null) {
                try {
                    view.performHapticFeedback(
                            HapticFeedbackConstants.KEYBOARD_TAP,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    );
                } catch (Exception ignored) {}
            }

            // B. Direct system vibrator motor trigger for guaranteed physical feedback
            try {
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null && v.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        v.vibrate(45);
                    }
                }
            } catch (Exception ignored) {}
        }

        // 2. Sound Click
        if (isSoundEnabled(context)) {
            try {
                if (toneGenerator != null) {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 35);
                }
            } catch (Exception ignored) {}
        }
    }

    /**
     * Executes celebratory milestone vibration and chime when completing a cycle (e.g. 33, 100).
     */
    public static void playCycleMilestoneFeedback(Context context) {
        if (context == null) return;

        if (isVibrationEnabled(context)) {
            try {
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (v != null && v.hasVibrator()) {
                    long[] pattern = {0, 90, 60, 160};
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    } else {
                        v.vibrate(pattern, -1);
                    }
                }
            } catch (Exception ignored) {}
        }

        if (isSoundEnabled(context)) {
            try {
                if (toneGenerator != null) {
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 130);
                }
            } catch (Exception ignored) {}
        }
    }
}
