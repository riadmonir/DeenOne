package com.devflux.deenone.features.ramadan.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.devflux.deenone.data.local.AppDatabase;

import java.util.concurrent.Executors;

/**
 * Persistent favorites manager for Roza Hadiths.
 * Supports instant in-memory read & write, SharedPreferences persistence,
 * and asynchronous background sync with Room database.
 */
public final class RozaHadithFavoritesManager {

    private static final String PREFS_NAME = "roza_hadith_favorites";
    private static final String KEY_PREFIX_HADITH = "fav_hadith_";

    private RozaHadithFavoritesManager() {}

    public static boolean isFavorite(Context context, int hadithNumber) {
        if (context == null) return false;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_PREFIX_HADITH + hadithNumber, false);
    }

    public static boolean toggleFavorite(Context context, int hadithNumber) {
        if (context == null) return false;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean current = prefs.getBoolean(KEY_PREFIX_HADITH + hadithNumber, false);
        boolean newState = !current;
        prefs.edit().putBoolean(KEY_PREFIX_HADITH + hadithNumber, newState).apply();

        // Sync with local Room database asynchronously if hadith exists in database
        final Context appContext = context.getApplicationContext();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(appContext);
                if (db != null && db.hadithDao() != null) {
                    // Update any matching records in hadith_table
                    db.getOpenHelper().getWritableDatabase().execSQL(
                            "UPDATE hadith_table SET isBookmarked = " + (newState ? "1" : "0") +
                                    " WHERE hadithNumber = " + hadithNumber
                    );
                }
            } catch (Exception ignored) {}
        });

        return newState;
    }
}
