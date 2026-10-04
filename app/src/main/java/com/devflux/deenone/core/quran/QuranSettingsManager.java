package com.devflux.deenone.core.quran;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * QuranSettingsManager — Centralized settings manager for Quran reader & audio player.
 * Persists:
 *  - Pronunciation visibility (উচ্চারণসহ পড়ুন)
 *  - Translation visibility (অর্থসহ পড়ুন)
 *  - Arabic font size (আরবি ফন্ট সাইজ)
 *  - Translation / Pronunciation font size (বাংলা ফন্ট সাইজ)
 *  - Audio Repeat mode (0 = Off, 1 = Repeat Ayah, 2 = Repeat Surah, 3 = Continuous All)
 *  - Audio Playback speed (0.75x, 1.0x, 1.25x, 1.5x, 2.0x)
 */
public class QuranSettingsManager {

    private static final String PREF_NAME = "deenone_quran_settings";
    private static final String KEY_SHOW_PRONUNCIATION = "show_pronunciation";
    private static final String KEY_SHOW_TRANSLATION = "show_translation";
    private static final String KEY_ARABIC_FONT_SIZE = "arabic_font_size";
    private static final String KEY_TRANSLATION_FONT_SIZE = "translation_font_size";
    private static final String KEY_REPEAT_MODE = "audio_repeat_mode";
    private static final String KEY_PLAYBACK_SPEED = "audio_playback_speed";

    public static final int REPEAT_OFF = 0;
    public static final int REPEAT_AYAH = 1;
    public static final int REPEAT_SURAH = 2;
    public static final int REPEAT_ALL = 3;

    public static final float DEFAULT_ARABIC_FONT_SIZE = 24.0f;
    public static final float DEFAULT_TRANSLATION_FONT_SIZE = 14.0f;

    private static volatile QuranSettingsManager instance;
    private final SharedPreferences prefs;

    private QuranSettingsManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static QuranSettingsManager getInstance(Context context) {
        if (instance == null) {
            synchronized (QuranSettingsManager.class) {
                if (instance == null) {
                    instance = new QuranSettingsManager(context);
                }
            }
        }
        return instance;
    }

    public boolean isShowPronunciation() {
        return prefs.getBoolean(KEY_SHOW_PRONUNCIATION, true);
    }

    public void setShowPronunciation(boolean show) {
        prefs.edit().putBoolean(KEY_SHOW_PRONUNCIATION, show).apply();
    }

    public boolean isShowTranslation() {
        return prefs.getBoolean(KEY_SHOW_TRANSLATION, true);
    }

    public void setShowTranslation(boolean show) {
        prefs.edit().putBoolean(KEY_SHOW_TRANSLATION, show).apply();
    }

    public float getArabicFontSize() {
        return prefs.getFloat(KEY_ARABIC_FONT_SIZE, DEFAULT_ARABIC_FONT_SIZE);
    }

    public void setArabicFontSize(float sizeSp) {
        prefs.edit().putFloat(KEY_ARABIC_FONT_SIZE, sizeSp).apply();
    }

    public float getTranslationFontSize() {
        return prefs.getFloat(KEY_TRANSLATION_FONT_SIZE, DEFAULT_TRANSLATION_FONT_SIZE);
    }

    public void setTranslationFontSize(float sizeSp) {
        prefs.edit().putFloat(KEY_TRANSLATION_FONT_SIZE, sizeSp).apply();
    }

    public int getRepeatMode() {
        return prefs.getInt(KEY_REPEAT_MODE, REPEAT_OFF);
    }

    public void setRepeatMode(int mode) {
        prefs.edit().putInt(KEY_REPEAT_MODE, mode).apply();
    }

    public float getPlaybackSpeed() {
        return prefs.getFloat(KEY_PLAYBACK_SPEED, 1.0f);
    }

    public void setPlaybackSpeed(float speed) {
        prefs.edit().putFloat(KEY_PLAYBACK_SPEED, speed).apply();
    }

    public static int getRepeatMode(Context context) {
        return getInstance(context).getRepeatMode();
    }

    public static float getPlaybackSpeed(Context context) {
        return getInstance(context).getPlaybackSpeed();
    }
}
