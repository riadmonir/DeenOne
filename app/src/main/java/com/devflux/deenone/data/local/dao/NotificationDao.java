package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.devflux.deenone.data.local.entity.NotificationMessageEntity;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM notification_history ORDER BY timestamp DESC")
    LiveData<List<NotificationMessageEntity>> getAllNotifications();

    @Query("SELECT COUNT(*) FROM notification_history WHERE isRead = 0")
    LiveData<Integer> getUnreadCount();

    @Query("SELECT COUNT(*) FROM notification_history")
    int getTotalCountSync();

    @Query("SELECT COUNT(*) FROM notification_history WHERE title = :title AND message = :message")
    int countByTitleAndMessage(String title, String message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotification(NotificationMessageEntity notification);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NotificationMessageEntity> notifications);

    @Query("UPDATE notification_history SET isRead = 1 WHERE id = :id")
    void markAsRead(long id);

    @Query("UPDATE notification_history SET isRead = 1")
    void markAllAsRead();

    @Query("DELETE FROM notification_history WHERE id = :id")
    void deleteNotification(long id);

    @Query("DELETE FROM notification_history")
    void clearAll();

    // Midnight cleanup: delete daily prayer notifications older than today's 12:00 AM midnight
    @Query("DELETE FROM notification_history WHERE (type IN ('adhan', 'prayer', 'tahajjud')) AND timestamp < :midnightCutoff")
    void deleteExpiredDailyPrayerNotifications(long midnightCutoff);

    // Expired admin cleanup: delete server/admin notifications older than 7 days
    @Query("DELETE FROM notification_history WHERE (source = 'server_admin' OR type IN ('admin_announcement', 'critical_update')) AND timestamp < :sevenDaysCutoff")
    void deleteExpiredAdminNotifications(long sevenDaysCutoff);
}
