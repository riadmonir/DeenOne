package com.devflux.deenone.domain.repository;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;

public interface IPrayerRepository {
    LiveData<PrayerScheduleEntity> getLatestSchedule();
    void syncPrayerTimes(String city, String country);
}
