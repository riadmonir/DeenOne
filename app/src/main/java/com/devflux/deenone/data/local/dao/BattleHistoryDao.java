package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.BattleHistoryEntity;

import java.util.List;

@Dao
public interface BattleHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBattleHistory(BattleHistoryEntity entity);

    @Query("SELECT * FROM battle_history ORDER BY timestamp DESC")
    LiveData<List<BattleHistoryEntity>> getAllBattleHistoryLive();

    @Query("SELECT * FROM battle_history ORDER BY timestamp DESC LIMIT 50")
    List<BattleHistoryEntity> getRecentBattleHistorySync();

    @Query("SELECT COUNT(*) FROM battle_history WHERE isWinner = 1")
    int getTotalWinsSync();

    @Query("SELECT SUM(deenXpEarned) FROM battle_history")
    int getTotalDeenXpSync();
}
