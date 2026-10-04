package com.devflux.deenone.core.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.data.repository.NotificationRepository;

import java.io.Serializable;

public class AdminNotificationManager extends BroadcastReceiver {

    private static final String TAG = "AdminNotificationMgr";

    public static final String ACTION_DISPATCH_ADMIN_NOTIF = "com.devflux.deenone.ACTION_DISPATCH_ADMIN_NOTIF";
    public static final String EXTRA_ADMIN_ITEM = "extra_admin_item";

    public static class AdminNotificationItem implements Serializable {
        public long id;
        public String title;
        public String body;
        public String imageUrl;
        public String type; // "announcement", "app_update", "emergency", "maintenance", "new_feature", "force_update", "community"
        public String targetAudience; // "all", "premium", "new_users", "group_dhaka"
        public String deepLink;
        public String priority; // "normal", "high", "critical"
        public String soundType; // "default", "alert", "silent"
        public long scheduledTimeMillis;
        public boolean isSent;
        public int readCount;

        public AdminNotificationItem(String title, String body, String type, String deepLink, String priority) {
            this.id = System.currentTimeMillis();
            this.title = title;
            this.body = body;
            this.type = type;
            this.deepLink = deepLink;
            this.priority = priority;
            this.targetAudience = "all";
            this.soundType = "default";
            this.scheduledTimeMillis = System.currentTimeMillis();
            this.isSent = false;
            this.readCount = 0;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        AdminNotificationItem item = (AdminNotificationItem) intent.getSerializableExtra(EXTRA_ADMIN_ITEM);
        if (item != null) {
            dispatchImmediate(context, item);
        }
    }

    public static void dispatchImmediate(Context context, AdminNotificationItem item) {
        if (context == null || item == null) return;

        if ("force_update".equalsIgnoreCase(item.type) || "critical".equalsIgnoreCase(item.priority)) {
            NotificationHelper.sendForceCriticalNotification(context, item.title, item.body, item.deepLink);
        } else {
            NotificationHelper.sendDynamicNotification(
                    context,
                    item.title,
                    item.body,
                    item.type != null ? item.type : "admin_announcement",
                    item.deepLink,
                    item.priority != null ? item.priority : "high"
            );
        }

        item.isSent = true;
        Log.d(TAG, "Admin notification dispatched: " + item.title);
    }

    public static void scheduleAdminNotification(Context context, AdminNotificationItem item, long triggerAtMillis) {
        if (context == null || item == null) return;
        if (triggerAtMillis <= System.currentTimeMillis()) {
            dispatchImmediate(context, item);
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, AdminNotificationManager.class);
        intent.setAction(ACTION_DISPATCH_ADMIN_NOTIF);
        intent.putExtra(EXTRA_ADMIN_ITEM, item);

        int reqCode = (int) (item.id % 1000000);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
            Log.d(TAG, "Admin notification scheduled for: " + triggerAtMillis);
        } catch (Exception e) {
            Log.w(TAG, "Error scheduling admin notification: " + e.getMessage());
        }
    }

    public static void simulateServerPush(Context context, String type, String title, String message, String deepLink, String priority) {
        AdminNotificationItem item = new AdminNotificationItem(title, message, type, deepLink, priority);
        dispatchImmediate(context, item);
    }
}
