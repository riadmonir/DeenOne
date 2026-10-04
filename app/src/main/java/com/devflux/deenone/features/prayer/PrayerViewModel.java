package com.devflux.deenone.features.prayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.data.repository.PrayerRepository;
import com.devflux.deenone.domain.usecase.GetPrayerScheduleUseCase;

public class PrayerViewModel extends AndroidViewModel {

    private final GetPrayerScheduleUseCase getPrayerScheduleUseCase;

    public PrayerViewModel(@NonNull Application application) {
        super(application);
        PrayerRepository repo = new PrayerRepository(application);
        this.getPrayerScheduleUseCase = new GetPrayerScheduleUseCase(repo);
    }

    public LiveData<PrayerScheduleEntity> getPrayerSchedule() {
        return getPrayerScheduleUseCase.execute();
    }
}
