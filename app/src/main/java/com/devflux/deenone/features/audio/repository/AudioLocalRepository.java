package com.devflux.deenone.features.audio.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.util.Log;

import com.devflux.deenone.features.audio.model.IslamicAudioItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Local persistence for audio catalog offline caching, favorites,
 * recently-played history, offline downloads, and playback progress positions.
 *
 * Implements intelligent cache management:
 *   - Offline-first instant loading
 *   - Deduplicated merging of live API items
 *   - Protected favorites & downloaded items during cache pruning
 */
public class AudioLocalRepository {

    private static final String TAG = "AudioLocalRepo";
    private static final String PREF_NAME = "deanone_audio_local";
    private static final String KEY_FAVORITES = "audio_favorites";
    private static final String KEY_RECENTLY_PLAYED = "audio_recently_played";
    private static final String KEY_DOWNLOADS = "audio_downloads";
    private static final String KEY_PLAY_POSITIONS = "audio_play_positions_";
    private static final String KEY_CATALOG_CACHE = "audio_catalog_cache_";
    private static final int MAX_RECENTLY_PLAYED = 40;
    private static final int MAX_CACHE_PER_CATEGORY = 200;

    private final Context appContext;
    private final SharedPreferences prefs;
    private final Gson gson;

    public AudioLocalRepository(Context context) {
        this.appContext = context.getApplicationContext();
        this.prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // ---- Network Check ----

    public static boolean isOnline(Context context) {
        if (context == null) return false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) return false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                android.net.Network active = cm.getActiveNetwork();
                if (active == null) return false;
                NetworkCapabilities caps = cm.getNetworkCapabilities(active);
                return caps != null && (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                android.net.NetworkInfo info = cm.getActiveNetworkInfo();
                return info != null && info.isConnected();
            }
        } catch (Exception e) {
            return false;
        }
    }

    // ---- Catalog Offline Cache ----

    public List<IslamicAudioItem> getCachedCatalog(String category) {
        String json = prefs.getString(KEY_CATALOG_CACHE + category, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<List<IslamicAudioItem>>(){}.getType();
            List<IslamicAudioItem> list = gson.fromJson(json, type);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            Log.w(TAG, "getCachedCatalog parse error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveCachedCatalog(String category, List<IslamicAudioItem> newItems) {
        if (newItems == null || newItems.isEmpty()) return;

        List<IslamicAudioItem> current = getCachedCatalog(category);
        Set<String> seenIds = new HashSet<>();

        List<IslamicAudioItem> merged = new ArrayList<>();

        // Add new items first
        for (IslamicAudioItem item : newItems) {
            if (item != null && item.getId() != null && seenIds.add(item.getId())) {
                merged.add(item);
            }
        }

        // Add existing cached items
        for (IslamicAudioItem item : current) {
            if (item != null && item.getId() != null && seenIds.add(item.getId())) {
                merged.add(item);
            }
        }

        // Prune if exceeds max cache, but NEVER drop favorites or downloaded
        if (merged.size() > MAX_CACHE_PER_CATEGORY) {
            List<IslamicAudioItem> pruned = new ArrayList<>();
            Set<String> favIds = new HashSet<>();
            for (IslamicAudioItem f : getFavorites()) favIds.add(f.getId());
            for (IslamicAudioItem d : getDownloadedItems()) favIds.add(d.getId());

            for (IslamicAudioItem item : merged) {
                if (pruned.size() < MAX_CACHE_PER_CATEGORY || favIds.contains(item.getId())) {
                    pruned.add(item);
                }
            }
            merged = pruned;
        }

        try {
            prefs.edit().putString(KEY_CATALOG_CACHE + category, gson.toJson(merged)).apply();
        } catch (Exception e) {
            Log.w(TAG, "saveCachedCatalog write error: " + e.getMessage());
        }
    }

    // ---- Favorites ----

    public void addFavorite(IslamicAudioItem item) {
        List<IslamicAudioItem> favorites = getFavorites();
        favorites.removeIf(f -> f.getId().equals(item.getId()));
        favorites.add(0, item);
        saveFavorites(favorites);
    }

    public void removeFavorite(String id) {
        List<IslamicAudioItem> favorites = getFavorites();
        favorites.removeIf(f -> f.getId().equals(id));
        saveFavorites(favorites);
    }

    public boolean isFavorite(String id) {
        for (IslamicAudioItem f : getFavorites()) {
            if (f.getId() != null && f.getId().equals(id)) return true;
        }
        return false;
    }

    public List<IslamicAudioItem> getFavorites() {
        String json = prefs.getString(KEY_FAVORITES, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<List<IslamicAudioItem>>(){}.getType();
            List<IslamicAudioItem> list = gson.fromJson(json, type);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            Log.w(TAG, "getFavorites parse error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveFavorites(List<IslamicAudioItem> list) {
        prefs.edit().putString(KEY_FAVORITES, gson.toJson(list)).apply();
    }

    // ---- Recently Played ----

    public void markPlayed(IslamicAudioItem item) {
        List<IslamicAudioItem> history = getRecentlyPlayed();
        history.removeIf(h -> h.getId().equals(item.getId()));
        history.add(0, item);
        if (history.size() > MAX_RECENTLY_PLAYED) {
            history = history.subList(0, MAX_RECENTLY_PLAYED);
        }
        prefs.edit().putString(KEY_RECENTLY_PLAYED, gson.toJson(history)).apply();
    }

    public List<IslamicAudioItem> getRecentlyPlayed() {
        String json = prefs.getString(KEY_RECENTLY_PLAYED, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<List<IslamicAudioItem>>(){}.getType();
            List<IslamicAudioItem> list = gson.fromJson(json, type);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            Log.w(TAG, "getRecentlyPlayed parse error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ---- Offline Downloads ----

    public void saveDownloadedItem(IslamicAudioItem item) {
        List<IslamicAudioItem> list = getDownloadedItems();
        list.removeIf(i -> i.getId().equals(item.getId()));
        list.add(0, item);
        prefs.edit().putString(KEY_DOWNLOADS, gson.toJson(list)).apply();
    }

    public void removeDownloadedItem(String audioId) {
        List<IslamicAudioItem> list = getDownloadedItems();
        list.removeIf(i -> i.getId().equals(audioId));
        prefs.edit().putString(KEY_DOWNLOADS, gson.toJson(list)).apply();
    }

    public List<IslamicAudioItem> getDownloadedItems() {
        String json = prefs.getString(KEY_DOWNLOADS, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<List<IslamicAudioItem>>(){}.getType();
            List<IslamicAudioItem> list = gson.fromJson(json, type);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            Log.w(TAG, "getDownloadedItems parse error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ---- Playback Position ----

    public void savePlaybackPosition(String audioId, long positionMs) {
        prefs.edit().putLong(KEY_PLAY_POSITIONS + audioId, positionMs).apply();
    }

    public long getSavedPlaybackPosition(String audioId) {
        return prefs.getLong(KEY_PLAY_POSITIONS + audioId, 0L);
    }

    public void clearPlaybackPosition(String audioId) {
        prefs.edit().remove(KEY_PLAY_POSITIONS + audioId).apply();
    }
}