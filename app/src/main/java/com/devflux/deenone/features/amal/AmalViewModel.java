package com.devflux.deenone.features.amal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.AmalRecordEntity;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.data.repository.AmalRepository;
import com.devflux.deenone.domain.usecase.TrackAmalUseCase;

import java.util.List;

public class AmalViewModel extends AndroidViewModel {

    private final AmalRepository repository;
    private final TrackAmalUseCase trackAmalUseCase;

    public AmalViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AmalRepository(application);
        this.trackAmalUseCase = new TrackAmalUseCase(repository);
    }

    public LiveData<List<DailyAmalEntity>> getTodayAmals() {
        return repository.getTodayAmals();
    }

    public LiveData<Integer> getTodayCompletedCount() {
        return repository.getTodayCompletedCount();
    }

    public LiveData<Integer> getTodayTotalCount() {
        return repository.getTodayTotalCount();
    }

    public LiveData<Integer> getEarnedPointsToday() {
        return repository.getEarnedPointsToday();
    }

    public void completeAmal(DailyAmalEntity amal) {
        repository.completeAmal(amal);
    }

    public void toggleAmalCompletion(DailyAmalEntity amal) {
        repository.toggleAmalCompletion(amal);
    }

    public LiveData<AmalRecordEntity> getAmalRecord() {
        return trackAmalUseCase.execute();
    }
}
