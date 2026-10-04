package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.DuaLogEntity;

import java.util.List;

@Dao
public interface DuaLogDao {

    public static class DuaCountStat {
        public String duaTitle;
        public int count;
    }

    public static class CategoryUsageStat {
        public String category;
        public int count;
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLog(DuaLogEntity log);

    @Query("SELECT COUNT(*) FROM dua_logs WHERE dateString = :dateString")
    LiveData<Integer> getTodayDuaCount(String dateString);

    @Query("SELECT COUNT(*) FROM dua_logs WHERE dateString = :dateString")
    int getTodayDuaCountSync(String dateString);

    @Query("SELECT * FROM dua_logs ORDER BY readTimestamp DESC LIMIT :limit")
    LiveData<List<DuaLogEntity>> getRecentHistory(int limit);

    @Query("SELECT duaTitle, COUNT(*) as count FROM dua_logs GROUP BY duaTitle ORDER BY count DESC LIMIT 1")
    LiveData<DuaCountStat> getMostReadDua();

    @Query("SELECT duaTitle, COUNT(*) as count FROM dua_logs GROUP BY duaTitle ORDER BY count DESC LIMIT 5")
    LiveData<List<DuaCountStat>> getTopReadDuas();

    @Query("SELECT category, COUNT(*) as count FROM dua_logs GROUP BY category ORDER BY count DESC")
    LiveData<List<CategoryUsageStat>> getCategoryUsage();

    @Query("SELECT COUNT(*) FROM dua_logs")
    LiveData<Integer> getTotalDuaCount();

    @Query("DELETE FROM dua_logs WHERE id = :id")
    void deleteLog(long id);

    @Query("DELETE FROM dua_logs")
    void clearAllLogs();
}
