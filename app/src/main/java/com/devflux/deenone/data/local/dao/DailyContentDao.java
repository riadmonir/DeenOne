package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.DailyContentEntity;

@Dao
public interface DailyContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertOrUpdate(DailyContentEntity content);

    @Query("SELECT * FROM daily_contents WHERE date = :date LIMIT 1")
    LiveData<DailyContentEntity> getContentByDate(String date);

    @Query("SELECT * FROM daily_contents ORDER BY id DESC LIMIT 1")
    LiveData<DailyContentEntity> getLatestContent();

    @Query("SELECT * FROM daily_contents ORDER BY id DESC LIMIT 1")
    DailyContentEntity getLatestContentSync();

    @Query("UPDATE daily_contents SET isDuaRead = :isRead WHERE id = :id")
    void updateDuaReadStatus(long id, boolean isRead);
}
