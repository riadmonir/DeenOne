package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.AzkarEntity;

import java.util.List;

@Dao
public interface AzkarDao {

    @Query("SELECT * FROM azkar ORDER BY sortOrder ASC")
    LiveData<List<AzkarEntity>> getAllAzkar();

    @Query("SELECT * FROM azkar WHERE category = :category ORDER BY sortOrder ASC")
    LiveData<List<AzkarEntity>> getAzkarByCategory(String category);

    @Query("SELECT DISTINCT category FROM azkar ORDER BY sortOrder ASC")
    LiveData<List<String>> getCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AzkarEntity> azkarList);

    @Update
    void updateAzkar(AzkarEntity azkar);

    @Query("UPDATE azkar SET currentCount = currentCount + 1 WHERE id = :id")
    void incrementCount(long id);

    @Query("UPDATE azkar SET currentCount = 0 WHERE category = :category")
    void resetCategoryCount(String category);

    @Query("UPDATE azkar SET currentCount = 0")
    void resetAllCounts();

    @Query("SELECT COUNT(*) FROM azkar")
    int getAzkarCount();
}
