package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.TasbihEntity;

import java.util.List;

@Dao
public interface TasbihDao {

    @Query("SELECT * FROM tasbih_logs ORDER BY sortOrder ASC")
    LiveData<List<TasbihEntity>> getAllDhikr();

    @Query("SELECT * FROM tasbih_logs WHERE id = :id LIMIT 1")
    TasbihEntity getDhikrById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TasbihEntity tasbih);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TasbihEntity> list);

    @Update
    void update(TasbihEntity tasbih);

    @Query("UPDATE tasbih_logs SET currentCount = :count WHERE id = :id")
    void updateCount(long id, int count);

    @Query("UPDATE tasbih_logs SET currentCount = 0, completedCycles = completedCycles + 1, totalLifetimeCount = totalLifetimeCount + targetCount WHERE id = :id")
    void completeCycleAndReset(long id);

    @Query("UPDATE tasbih_logs SET currentCount = 0 WHERE id = :id")
    void resetCount(long id);

    @Query("UPDATE tasbih_logs SET targetCount = :target WHERE id = :id")
    void updateTarget(long id, int target);

    @Query("UPDATE tasbih_logs SET vibrationEnabled = :enabled WHERE id = :id")
    void setVibration(long id, boolean enabled);

    @Query("UPDATE tasbih_logs SET soundEnabled = :enabled WHERE id = :id")
    void setSound(long id, boolean enabled);

    @Query("SELECT SUM(totalLifetimeCount) FROM tasbih_logs")
    LiveData<Long> getTotalLifetimeDhikrCount();

    @Query("SELECT COUNT(*) FROM tasbih_logs")
    int getDhikrCount();

    @Query("DELETE FROM tasbih_logs WHERE id = :id")
    void deleteById(long id);
}
