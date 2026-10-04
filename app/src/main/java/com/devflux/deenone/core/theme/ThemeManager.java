package com.devflux.deenone.core.theme;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREF_NAME = "deanone_theme_prefs";
    private static final String KEY_THEME_MODE = "key_theme_mode";

    public static final int THEME_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
    public static final int THEME_LIGHT = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int THEME_DARK = AppCompatDelegate.MODE_NIGHT_YES;

    public static void applySavedTheme(Context context) {
        int savedMode = getSavedThemeMode(context);
        AppCompatDelegate.setDefaultNightMode(savedMode);
    }

    public static void setThemeMode(Context context, int mode) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME_MODE, mode).apply();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public static int getSavedThemeMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME_MODE, THEME_DARK); // Default to Dark Theme per spec
    }

    public static boolean isLightMode(Context context) {
        if (context == null) return false;
        int mode = getSavedThemeMode(context);
        if (mode == THEME_LIGHT) return true;
        if (mode == THEME_DARK) return false;
        int nightModeFlags = context.getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags != android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }
}
