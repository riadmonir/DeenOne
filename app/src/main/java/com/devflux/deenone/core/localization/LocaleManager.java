package com.devflux.deenone.core.localization;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

public class LocaleManager {

    private static final String PREF_NAME = "deanone_locale_prefs";
    private static final String KEY_LANGUAGE = "key_app_language";

    public static final String LANGUAGE_BENGALI = "bn";
    public static final String LANGUAGE_ENGLISH = "en";

    public static Context wrapContext(Context context) {
        if (context == null) return null;
        String language = getSavedLanguage(context);
        Locale targetLocale = new Locale(language);
        Locale.setDefault(targetLocale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLayoutDirection(targetLocale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(targetLocale));
            try {
                resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            } catch (Throwable ignored) {}
            return context.createConfigurationContext(configuration);
        } else {
            configuration.setLocale(targetLocale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
            return new ContextWrapper(context);
        }
    }

    public static void setLanguage(Context context, String languageCode) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
        applyLocale(context, languageCode);
        try {
            androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(
                    androidx.core.os.LocaleListCompat.forLanguageTags(languageCode)
            );
        } catch (Throwable ignored) {}
    }

    public static void applyLocale(Context context, String languageCode) {
        if (context == null) return;
        Locale targetLocale = new Locale(languageCode);
        Locale.setDefault(targetLocale);

        try {
            Resources resources = context.getResources();
            Configuration configuration = new Configuration(resources.getConfiguration());
            configuration.setLayoutDirection(targetLocale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(new LocaleList(targetLocale));
            } else {
                configuration.setLocale(targetLocale);
            }
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        } catch (Throwable ignored) {}

        try {
            Context appCtx = context.getApplicationContext();
            if (appCtx != null && appCtx != context) {
                Resources appRes = appCtx.getResources();
                Configuration appConfig = new Configuration(appRes.getConfiguration());
                appConfig.setLayoutDirection(targetLocale);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    appConfig.setLocales(new LocaleList(targetLocale));
                } else {
                    appConfig.setLocale(targetLocale);
                }
                appRes.updateConfiguration(appConfig, appRes.getDisplayMetrics());
            }
        } catch (Throwable ignored) {}
    }

    public static String getSavedLanguage(Context context) {
        if (context == null) return LANGUAGE_BENGALI;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_BENGALI); // Default is Bangla-BD
    }

    public static boolean isBengali(Context context) {
        return LANGUAGE_BENGALI.equals(getSavedLanguage(context));
    }
}
