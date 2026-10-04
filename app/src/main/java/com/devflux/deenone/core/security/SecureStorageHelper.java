package com.devflux.deenone.core.security;

import android.content.Context;
import android.content.SharedPreferences;

public class SecureStorageHelper {

    private static final String PREF_NAME = "deanone_secure_prefs";

    private SecureStorageHelper() {
        // Utility class
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveString(Context context, String key, String value) {
        getPreferences(context).edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPreferences(context).getString(key, defaultValue);
    }
}
