package com.devflux.deenone.domain.usecase;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.domain.repository.IPrayerRepository;

public class GetPrayerScheduleUseCase {

    private final IPrayerRepository repository;

    public GetPrayerScheduleUseCase(IPrayerRepository repository) {
        this.repository = repository;
    }

    public LiveData<PrayerScheduleEntity> execute() {
        return repository.getLatestSchedule();
    }
}
