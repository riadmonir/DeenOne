package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.KalemaEntity;

import java.util.List;

@Dao
public interface KalemaDao {

    @Query("SELECT * FROM kalemas ORDER BY orderNumber ASC")
    LiveData<List<KalemaEntity>> getAllKalemas();

    @Query("SELECT * FROM kalemas WHERE orderNumber = :orderNumber LIMIT 1")
    KalemaEntity getKalemaByOrder(int orderNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<KalemaEntity> kalemas);

    @Query("SELECT COUNT(*) FROM kalemas")
    int getCount();
}
