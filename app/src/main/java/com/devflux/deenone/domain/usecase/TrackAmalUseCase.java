package com.devflux.deenone.domain.usecase;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.AmalRecordEntity;
import com.devflux.deenone.domain.repository.IAmalRepository;

public class TrackAmalUseCase {

    private final IAmalRepository repository;

    public TrackAmalUseCase(IAmalRepository repository) {
        this.repository = repository;
    }

    public LiveData<AmalRecordEntity> execute() {
        return repository.getLatestRecord();
    }

    public void updateAmalRecord(AmalRecordEntity record) {
        repository.updateRecord(record);
    }
}
