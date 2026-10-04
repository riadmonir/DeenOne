package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.DailyAmalEntity;

import java.util.List;

@Dao
public interface DailyAmalDao {

    @Query("SELECT * FROM daily_amals WHERE dateString = :dateString ORDER BY id ASC")
    LiveData<List<DailyAmalEntity>> getAmalsByDate(String dateString);

    @Query("SELECT * FROM daily_amals WHERE dateString = :dateString ORDER BY id ASC")
    List<DailyAmalEntity> getAmalsByDateSync(String dateString);

    @Query("SELECT COUNT(*) FROM daily_amals WHERE dateString = :dateString")
    int getCountByDate(String dateString);

    @Query("SELECT COUNT(*) FROM daily_amals WHERE dateString = :dateString AND isCompleted = 1")
    LiveData<Integer> getCompletedCountByDate(String dateString);

    @Query("SELECT COUNT(*) FROM daily_amals WHERE dateString = :dateString AND isCompleted = 1")
    int getCompletedCountByDateSync(String dateString);

    @Query("SELECT COUNT(*) FROM daily_amals WHERE dateString = :dateString")
    LiveData<Integer> getTotalCountByDate(String dateString);

    @Query("SELECT COALESCE(SUM(points), 0) FROM daily_amals WHERE dateString = :dateString AND isCompleted = 1")
    LiveData<Integer> getEarnedPointsByDate(String dateString);

    @Query("SELECT COALESCE(SUM(points), 0) FROM daily_amals WHERE dateString = :dateString AND isCompleted = 1")
    int getEarnedPointsByDateSync(String dateString);

    @Query("SELECT COUNT(*) FROM daily_amals WHERE isCompleted = 1")
    LiveData<Integer> getTotalCompletedCount();

    @Query("SELECT COUNT(*) FROM daily_amals WHERE isCompleted = 1")
    int getTotalCompletedCountSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DailyAmalEntity> amals);

    @Update
    void updateAmal(DailyAmalEntity amal);

    @Query("UPDATE daily_amals SET isCompleted = :isCompleted WHERE id = :id")
    void setCompletionStatus(long id, boolean isCompleted);

    @Query("SELECT DISTINCT dateString FROM daily_amals WHERE isCompleted = 1")
    List<String> getAllCompletedDatesSync();

    @Query("UPDATE daily_amals SET isCompleted = 0")
    void resetAllCompletedSync();
}
