package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.PrayerLogEntity;

import java.util.List;

@Dao
public interface PrayerLogDao {

    @Query("SELECT * FROM prayer_logs WHERE date = :date")
    LiveData<List<PrayerLogEntity>> getPrayerLogsForDate(String date);

    @Query("SELECT * FROM prayer_logs WHERE date = :date")
    List<PrayerLogEntity> getPrayerLogsForDateSync(String date);

    @Query("SELECT * FROM prayer_logs WHERE date = :date AND prayerName = :prayerName LIMIT 1")
    PrayerLogEntity getLog(String date, String prayerName);

    @Query("SELECT COUNT(DISTINCT prayerName) FROM prayer_logs WHERE date = :date AND isPrayed = 1 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status IN ('JAMAAT', 'EKAKI', 'DERI')")
    LiveData<Integer> getPrayedCountForDate(String date);

    @Query("SELECT COUNT(DISTINCT prayerName) FROM prayer_logs WHERE date = :date AND isPrayed = 1 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status IN ('JAMAAT', 'EKAKI', 'DERI')")
    int getPrayedCountForDateSync(String date);

    @Query("SELECT COUNT(*) FROM prayer_logs WHERE date = :date AND isWithJamat = 1 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha')")
    int getJamatCountForDateSync(String date);

    @Query("SELECT COUNT(*) FROM prayer_logs WHERE date = :date AND isQaza = 1 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha')")
    int getQazaCountForDateSync(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PrayerLogEntity log);

    @Update
    void update(PrayerLogEntity log);

    @Query("DELETE FROM prayer_logs WHERE date = :date AND prayerName = :prayerName")
    void deleteLog(String date, String prayerName);

    @Query("SELECT * FROM prayer_logs WHERE date LIKE :monthPrefix || '%'")
    List<PrayerLogEntity> getLogsForMonth(String monthPrefix);

    @Query("SELECT * FROM prayer_logs WHERE date >= :startDate ORDER BY date DESC")
    List<PrayerLogEntity> getLogsSinceDate(String startDate);

    @Query("SELECT COUNT(DISTINCT date || '_' || prayerName) FROM prayer_logs WHERE isPrayed = 1 AND isQaza = 0 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status IN ('JAMAAT', 'EKAKI', 'DERI')")
    LiveData<Integer> getTotalPrayedCount();

    @Query("SELECT COUNT(DISTINCT date || '_' || prayerName) FROM prayer_logs WHERE isPrayed = 1 AND isQaza = 0 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status IN ('JAMAAT', 'EKAKI', 'DERI')")
    int getTotalPrayedCountSync();

    @Query("SELECT COUNT(DISTINCT date || '_' || prayerName) FROM prayer_logs WHERE isPrayed = 1 AND isWithJamat = 1 AND isQaza = 0 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status = 'JAMAAT'")
    LiveData<Integer> getTotalJamatCount();

    @Query("SELECT COUNT(DISTINCT date || '_' || prayerName) FROM prayer_logs WHERE isPrayed = 1 AND isWithJamat = 1 AND isQaza = 0 AND prayerName IN ('Fajr', 'Dhuhr', 'Asr', 'Maghrib', 'Isha') AND status = 'JAMAAT'")
    int getTotalJamatCountSync();

    @Query("SELECT COALESCE(SUM(points), 0) FROM prayer_logs WHERE date = :date AND isPrayed = 1")
    LiveData<Integer> getEarnedPointsForDate(String date);

    @Query("SELECT COALESCE(SUM(points), 0) FROM prayer_logs WHERE date = :date AND isPrayed = 1")
    int getEarnedPointsForDateSync(String date);

    @Query("SELECT COUNT(DISTINCT date || '_' || prayerName) FROM prayer_logs WHERE isQaza = 1")
    LiveData<Integer> getTotalQazaCount();

    @Query("SELECT COUNT(DISTINCT date || '_' || prayerName) FROM prayer_logs WHERE isQaza = 1")
    int getTotalQazaCountSync();

    @Query("DELETE FROM prayer_logs WHERE id NOT IN (SELECT MIN(id) FROM prayer_logs GROUP BY date, prayerName)")
    void deleteDuplicatesSync();

    @Query("DELETE FROM prayer_logs")
    void deleteAllLogsSync();

    default void insertOrUpdate(PrayerLogEntity entity) {
        if (entity == null) return;
        PrayerLogEntity existing = getLog(entity.getDate(), entity.getPrayerName());
        if (existing != null) {
            entity.setId(existing.getId());
            update(entity);
        } else {
            insert(entity);
        }
    }

    @Query("SELECT * FROM prayer_logs ORDER BY date DESC")
    List<PrayerLogEntity> getAllLogsSync();
}
