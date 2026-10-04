package com.devflux.deenone.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.DuaDao;
import com.devflux.deenone.data.local.dao.DuaLogDao;
import com.devflux.deenone.data.local.entity.DuaEntity;
import com.devflux.deenone.data.local.entity.DuaLogEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DuaRepository {

    private final Context appContext;
    private final DuaDao duaDao;
    private final DuaLogDao duaLogDao;

    private static final String PREF_DUA_SYNC = "dua_sync_prefs";
    private static final String KEY_DUA_SYNC_IDX = "next_dua_sync_index";

    public interface DuaSyncCallback {
        void onSuccess(int newlyAddedCount, int totalCount, String sourceBook);
        void onError(String errorMessage);
    }

    public DuaRepository(Context context) {
        this.appContext = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        this.duaDao = db.duaDao();
        this.duaLogDao = db.duaLogDao();
    }

    public LiveData<List<DuaEntity>> getAllDuas() {
        return duaDao.getAllDuas();
    }

    public LiveData<Integer> getDuaCountLive() {
        return duaDao.getDuaCountLive();
    }

    public LiveData<List<DuaEntity>> getDuasByCategory(String category) {
        if ("all".equalsIgnoreCase(category) || "সকল দোয়া".equalsIgnoreCase(category)) {
            return duaDao.getAllDuas();
        } else if ("Favorites".equalsIgnoreCase(category) || "প্রিয় দোয়া".equalsIgnoreCase(category)) {
            return duaDao.getFavoriteDuas();
        }
        return duaDao.getDuasByCategory(category);
    }

    public LiveData<List<DuaEntity>> getFavoriteDuas() {
        return duaDao.getFavoriteDuas();
    }

    public LiveData<List<DuaEntity>> searchDuas(String query) {
        return duaDao.searchDuas(query);
    }

    public void toggleFavorite(long id, boolean isFav) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            duaDao.toggleFavorite(id, isFav);
        });
    }

    public void insertAll(List<DuaEntity> duas) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            duaDao.insertAll(duas);
        });
    }

    /**
     * Ensures that all 160+ authentic offline Duas from app/src/main/assets/dua/*.json
     * are seeded into SQLite Room without duplicates.
     */
    public void ensureSeedDuasLoaded(Runnable onComplete) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                duaDao.cleanRawNarrativeDumps();
            } catch (Exception ignored) {}

            List<DuaEntity> assetDuas = com.devflux.deenone.core.ai.IslamicDuaScraperEngine
                    .getInstance().loadSeedDuasFromAssets(appContext, "all");

            List<String> existingTitles = duaDao.getAllExistingTitles();
            List<String> existingArabic = duaDao.getAllExistingArabicTexts();
            java.util.Set<String> titleSet = new java.util.HashSet<>(existingTitles);
            java.util.Set<String> arabicSet = new java.util.HashSet<>(existingArabic);

            List<DuaEntity> toInsert = new java.util.ArrayList<>();
            for (DuaEntity d : assetDuas) {
                boolean titleExists = d.getTitle() != null && titleSet.contains(d.getTitle());
                boolean arabicExists = d.getArabic() != null && !d.getArabic().isEmpty() && arabicSet.contains(d.getArabic());
                if (!titleExists && !arabicExists) {
                    toInsert.add(d);
                    if (d.getTitle() != null) titleSet.add(d.getTitle());
                    if (d.getArabic() != null && !d.getArabic().isEmpty()) arabicSet.add(d.getArabic());
                }
            }

            if (!toInsert.isEmpty()) {
                duaDao.insertAll(toInsert);
            }
            if (onComplete != null) {
                new android.os.Handler(android.os.Looper.getMainLooper()).post(onComplete);
            }
        });
    }

    /**
     * Real-time online scraping for authentic Duas.
     * Concurrently scrapes Bukhari, Muslim, Tirmidhi, Abu Dawud supplications,
     * checks existing Arabic, titles, and references to strictly avoid duplicates, and saves to SQLite.
     * Never increments or produces fake counts if already up-to-date.
     */
    public void syncNextDuasFromOnline(DuaSyncCallback callback) {
        android.content.SharedPreferences prefs = appContext.getSharedPreferences(PREF_DUA_SYNC, Context.MODE_PRIVATE);
        int currentIdx = prefs.getInt(KEY_DUA_SYNC_IDX, 0);
        com.devflux.deenone.core.ai.IslamicDuaScraperEngine.SupplicationSource source =
                com.devflux.deenone.core.ai.IslamicDuaScraperEngine.ONLINE_SOURCES[currentIdx % com.devflux.deenone.core.ai.IslamicDuaScraperEngine.ONLINE_SOURCES.length];

        com.devflux.deenone.core.ai.IslamicDuaScraperEngine.getInstance().scrapeOnlineSupplications(currentIdx, new com.devflux.deenone.core.ai.IslamicDuaScraperEngine.DuaScrapeCallback() {
            @Override
            public void onDuaScraped(List<DuaEntity> scrapedDuas) {
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    List<String> existingTitles = duaDao.getAllExistingTitles();
                    List<String> existingArabic = duaDao.getAllExistingArabicTexts();
                    List<String> existingRefs = duaDao.getAllExistingReferences();

                    java.util.Set<String> titleSet = new java.util.HashSet<>(existingTitles);
                    java.util.Set<String> arabicSet = new java.util.HashSet<>(existingArabic);
                    java.util.Set<String> refSet = new java.util.HashSet<>(existingRefs);

                    List<DuaEntity> nonDuplicates = new java.util.ArrayList<>();
                    for (DuaEntity d : scrapedDuas) {
                        boolean titleExists = d.getTitle() != null && titleSet.contains(d.getTitle());
                        boolean arabicExists = d.getArabic() != null && !d.getArabic().isEmpty() && arabicSet.contains(d.getArabic());
                        boolean refExists = d.getReference() != null && !d.getReference().isEmpty() && refSet.contains(d.getReference());

                        if (!titleExists && !arabicExists && !refExists) {
                            nonDuplicates.add(d);
                            if (d.getTitle() != null) titleSet.add(d.getTitle());
                            if (d.getArabic() != null && !d.getArabic().isEmpty()) arabicSet.add(d.getArabic());
                            if (d.getReference() != null && !d.getReference().isEmpty()) refSet.add(d.getReference());
                        }
                    }

                    if (!nonDuplicates.isEmpty()) {
                        duaDao.insertAll(nonDuplicates);
                    }

                    prefs.edit().putInt(KEY_DUA_SYNC_IDX, currentIdx + 1).apply();
                    int totalCount = duaDao.getDuaCount();

                    if (callback != null) {
                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            callback.onSuccess(nonDuplicates.size(), totalCount, source.bookName);
                        });
                    }
                });
            }

            @Override
            public void onError(String errorReason) {
                if (callback != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                        callback.onError(errorReason);
                    });
                }
            }
        });
    }

    // --- Dua Tracking Operations ---

    public void logDuaRead(long duaId, String title, String category) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(new Date());
            DuaLogEntity log = new DuaLogEntity(duaId, title, category, System.currentTimeMillis(), todayDate);
            duaLogDao.insertLog(log);
        });
    }

    public LiveData<Integer> getTodayDuaCount(String dateString) {
        return duaLogDao.getTodayDuaCount(dateString);
    }

    public LiveData<List<DuaLogEntity>> getRecentHistory(int limit) {
        return duaLogDao.getRecentHistory(limit);
    }

    public LiveData<DuaLogDao.DuaCountStat> getMostReadDua() {
        return duaLogDao.getMostReadDua();
    }

    public LiveData<List<DuaLogDao.DuaCountStat>> getTopReadDuas() {
        return duaLogDao.getTopReadDuas();
    }

    public LiveData<List<DuaLogDao.CategoryUsageStat>> getCategoryUsage() {
        return duaLogDao.getCategoryUsage();
    }

    public LiveData<Integer> getTotalDuaCount() {
        return duaLogDao.getTotalDuaCount();
    }
}
