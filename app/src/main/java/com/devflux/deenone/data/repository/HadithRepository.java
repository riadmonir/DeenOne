package com.devflux.deenone.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.core.ai.IslamicHadithScraperEngine;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.HadithDao;
import com.devflux.deenone.data.local.entity.HadithEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HadithRepository {

    public interface SyncCallback {
        void onSyncSuccess(int count);
        void onSyncError(String error);
    }

    private final HadithDao hadithDao;
    private final Context context;

    public HadithRepository(Context context) {
        this.context = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        this.hadithDao = db.hadithDao();

        // Ensure seed assets (1,600+ authentic hadiths) are seeded if database has few records
        ensureSeedHadithsLoaded();
    }

    public void ensureSeedHadithsLoaded() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                if (hadithDao.getHadithCount() < 50) {
                    List<HadithEntity> seeds = IslamicHadithScraperEngine.getInstance()
                            .loadSeedHadithsFromAssets(context, "all");
                    if (seeds != null && !seeds.isEmpty()) {
                        hadithDao.insertAll(seeds);
                    }
                }
            } catch (Exception ignored) {}
        });
    }

    public LiveData<List<HadithEntity>> getAllHadith() {
        return hadithDao.getAllHadith();
    }

    public LiveData<List<HadithEntity>> getHadithByCollection(String collectionId) {
        if (collectionId == null || "all".equalsIgnoreCase(collectionId.trim())) {
            return hadithDao.getAllHadith();
        }
        return hadithDao.getHadithByCollection(collectionId.trim());
    }

    public LiveData<List<HadithEntity>> getHadithByTopic(String topic) {
        if (topic == null || "all".equalsIgnoreCase(topic.trim()) || "সকল".equalsIgnoreCase(topic.trim())) {
            return hadithDao.getAllHadith();
        }
        return hadithDao.getHadithByTopic(topic.trim());
    }

    public LiveData<List<HadithEntity>> getHadithByCollectionAndTopic(String collectionId, String topic) {
        boolean noCol = collectionId == null || "all".equalsIgnoreCase(collectionId.trim());
        boolean noTopic = topic == null || "all".equalsIgnoreCase(topic.trim()) || "সকল".equalsIgnoreCase(topic.trim());

        if (noCol && noTopic) {
            return hadithDao.getAllHadith();
        } else if (noCol) {
            return hadithDao.getHadithByTopic(topic.trim());
        } else if (noTopic) {
            return hadithDao.getHadithByCollection(collectionId.trim());
        } else {
            return hadithDao.getHadithByCollectionAndTopic(collectionId.trim(), topic.trim());
        }
    }

    public LiveData<List<HadithEntity>> searchHadith(String query) {
        return hadithDao.searchHadith(query);
    }

    public LiveData<List<HadithEntity>> searchHadith(String collectionId, String query) {
        if (collectionId == null || "all".equalsIgnoreCase(collectionId.trim())) {
            return hadithDao.searchHadith(query);
        }
        return hadithDao.searchHadithInCollection(collectionId.trim(), query);
    }

    public LiveData<List<HadithEntity>> getBookmarkedHadith() {
        return hadithDao.getBookmarkedHadith();
    }

    public LiveData<HadithEntity> getRandomHadith() {
        return hadithDao.getRandomHadith();
    }

    public LiveData<Integer> getHadithCountLive() {
        return hadithDao.getHadithCountLive();
    }

    public void setBookmark(long hadithId, boolean bookmarked) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            hadithDao.updateBookmarkStatus(hadithId, bookmarked);
        });
    }

    public void insertHadithList(List<HadithEntity> list) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            hadithDao.insertAll(list);
        });
    }

    private static final String PREFS_HADITH_SYNC = "deanone_hadith_sync_prefs";
    private static final String KEY_SECTION_PREFIX = "sync_section_";

    public int getNextSyncSection(String collectionId) {
        String col = (collectionId == null || "all".equalsIgnoreCase(collectionId.trim())) ? "bukhari" : collectionId.trim().toLowerCase();
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_HADITH_SYNC, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_SECTION_PREFIX + col, 1);
    }

    public void incrementSyncSection(String collectionId) {
        String col = (collectionId == null || "all".equalsIgnoreCase(collectionId.trim())) ? "bukhari" : collectionId.trim().toLowerCase();
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_HADITH_SYNC, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_SECTION_PREFIX + col, 1);
        prefs.edit().putInt(KEY_SECTION_PREFIX + col, current + 1).apply();
    }

    /**
     * Real-time Live Online Synchronization with strict SQLite Deduplication.
     * Uses ONLY DeenOne Official Backend API get_hadiths.php.
     */
    public void syncNextHadithsFromOnline(String collectionId, SyncCallback callback) {
        String col = (collectionId == null || "all".equalsIgnoreCase(collectionId.trim())) ? "bukhari" : collectionId.trim().toLowerCase();
        int page = getNextSyncSection(col);
        syncSectionWithRetry(col, page, 1, callback);
    }

    private void syncSectionWithRetry(String collectionId, int page, int retryCount, SyncCallback callback) {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            if (callback != null) callback.onSyncSuccess(0);
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            HttpURLConnection conn = null;
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(
                        context, "get_hadiths.php?book=" + collectionId + "&page=" + page + "&limit=20"
                );
                URL url = new URL(endpoint);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
                    if (root != null && root.has("success") && root.get("success").getAsBoolean() && root.has("hadiths")) {
                        JsonArray arr = root.getAsJsonArray("hadiths");
                        List<HadithEntity> fetchedList = new ArrayList<>();
                        for (JsonElement el : arr) {
                            JsonObject obj = el.getAsJsonObject();
                            int num = obj.has("hadith_number") ? obj.get("hadith_number").getAsInt() : 1;
                            String ar = obj.has("arabic_text") ? obj.get("arabic_text").getAsString() : "";
                            String bn = obj.has("bangla_text") ? obj.get("bangla_text").getAsString() : "";
                            String narratorBn = obj.has("narrator_bn") ? obj.get("narrator_bn").getAsString() : "রাসূলুল্লাহ ﷺ";
                            String bookName = obj.has("book_name") ? obj.get("book_name").getAsString() : collectionId;
                            String ref = obj.has("reference") ? obj.get("reference").getAsString() : (collectionId + ": " + num);
                            String grade = obj.has("grade") ? obj.get("grade").getAsString() : "সহীহ (Sahih)";
                            fetchedList.add(new HadithEntity(
                                    collectionId, bookName, num, bookName, "সহীহ হাদিস", narratorBn, ar, bn, "", "", grade, ref, false, "হাদিস ও সুন্নাহ"
                            ));
                        }

                        if (!fetchedList.isEmpty()) {
                            List<Integer> existingNums = hadithDao.getExistingNumbersForCollection(collectionId);
                            Set<Integer> existingSet = new HashSet<>(existingNums != null ? existingNums : new ArrayList<>());
                            List<HadithEntity> genuinelyNew = new ArrayList<>();
                            for (HadithEntity h : fetchedList) {
                                if (!existingSet.contains(h.getHadithNumber())) {
                                    genuinelyNew.add(h);
                                }
                            }

                            if (!genuinelyNew.isEmpty()) {
                                hadithDao.insertAll(genuinelyNew);
                                incrementSyncSection(collectionId);
                                if (callback != null) callback.onSyncSuccess(genuinelyNew.size());
                                return;
                            } else {
                                incrementSyncSection(collectionId);
                                if (retryCount <= 2 && page < 50) {
                                    syncSectionWithRetry(collectionId, page + 1, retryCount + 1, callback);
                                    return;
                                }
                            }
                        }
                    }
                }
                if (callback != null) callback.onSyncSuccess(0);
            } catch (Exception e) {
                if (callback != null) callback.onSyncError(e.getMessage());
            } finally {
                if (conn != null) conn.disconnect();
            }
        });
    }

    public void syncHadithsFromOnline(String collectionId, int page, SyncCallback callback) {
        String col = (collectionId == null || "all".equalsIgnoreCase(collectionId.trim())) ? "bukhari" : collectionId.trim().toLowerCase();
        syncSectionWithRetry(col, page, 1, callback);
    }
}
