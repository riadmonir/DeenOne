package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;

@Dao
public interface PrayerScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrUpdate(PrayerScheduleEntity schedule);

    @Query("SELECT * FROM prayer_schedules WHERE date = :date LIMIT 1")
    LiveData<PrayerScheduleEntity> getScheduleByDate(String date);

    @Query("SELECT * FROM prayer_schedules ORDER BY id DESC LIMIT 1")
    LiveData<PrayerScheduleEntity> getLatestSchedule();

    @Query("SELECT * FROM prayer_schedules WHERE date = :date LIMIT 1")
    PrayerScheduleEntity getScheduleByDateSync(String date);

    @Query("SELECT * FROM prayer_schedules ORDER BY id DESC LIMIT 1")
    PrayerScheduleEntity getLatestScheduleSync();

    @Query("DELETE FROM prayer_schedules")
    void clearAll();
}
