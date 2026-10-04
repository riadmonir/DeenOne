package com.devflux.deenone.domain.repository;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.entity.AmalRecordEntity;

public interface IAmalRepository {
    LiveData<AmalRecordEntity> getLatestRecord();
    void updateRecord(AmalRecordEntity record);
}
