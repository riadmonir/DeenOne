package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.AllahNameEntity;

import java.util.List;

@Dao
public interface AllahNameDao {

    @Query("SELECT * FROM allah_names ORDER BY serialNumber ASC")
    LiveData<List<AllahNameEntity>> getAllNames();

    @Query("SELECT * FROM allah_names WHERE serialNumber = :serialNumber LIMIT 1")
    AllahNameEntity getNameBySerial(int serialNumber);

    @Query("SELECT * FROM allah_names WHERE pronunciation LIKE '%' || :query || '%' OR banglaMeaning LIKE '%' || :query || '%' OR arabic LIKE '%' || :query || '%'")
    LiveData<List<AllahNameEntity>> searchNames(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AllahNameEntity> names);

    @Query("SELECT COUNT(*) FROM allah_names")
    int getCount();
}
