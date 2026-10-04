package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.AmalRecordEntity;

@Dao
public interface AmalRecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrUpdate(AmalRecordEntity record);

    @Query("SELECT * FROM amal_records WHERE date = :date LIMIT 1")
    LiveData<AmalRecordEntity> getRecordByDate(String date);

    @Query("SELECT * FROM amal_records ORDER BY id DESC LIMIT 1")
    LiveData<AmalRecordEntity> getLatestRecord();

    @Query("SELECT * FROM amal_records ORDER BY id DESC LIMIT 1")
    AmalRecordEntity getLatestRecordSync();
}
