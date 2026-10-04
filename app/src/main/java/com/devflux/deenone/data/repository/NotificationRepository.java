package com.devflux.deenone.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.NotificationDao;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;

import java.util.List;

public class NotificationRepository {

    private static volatile NotificationRepository instance;
    private final Context appContext;
    private final NotificationDao notificationDao;

    public static NotificationRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (NotificationRepository.class) {
                if (instance == null && context != null) {
                    instance = new NotificationRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public NotificationRepository(Context context) {
        this.appContext = context != null ? context.getApplicationContext() : null;
        AppDatabase db = AppDatabase.getInstance(context);
        this.notificationDao = db.notificationDao();
    }

    public LiveData<List<NotificationMessageEntity>> getAllNotifications() {
        return notificationDao.getAllNotifications();
    }

    public LiveData<Integer> getUnreadCount() {
        return notificationDao.getUnreadCount();
    }

    public void saveNotification(NotificationMessageEntity notification) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.insertNotification(notification);
        });
    }

    public void markAsRead(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.markAsRead(id);
        });
    }

    public void markAllAsRead() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.markAllAsRead();
        });
    }

    public void deleteNotification(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.deleteNotification(id);
        });
    }

    public void clearAll() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            notificationDao.clearAll();
        });
    }

    /**
     * Performs automatic midnight maintenance (Raat 12-ta Purge):
     * 1. Cancels yesterday's active prayer/adhan notifications from the Android status bar.
     * 2. Purges yesterday's daily prayer notifications from Room SQLite database so the new day begins fresh for Fajr.
     * 3. Preserves Daily Dua, Daily Hadith, Daily Amal, Ramadan, Eid, and Islamic Events.
     * 4. Purges server admin announcements older than 7 days.
     */
    public void performMidnightCleanup() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // 1. Cancel active status bar notifications for daily prayers
                if (appContext != null) {
                    android.app.NotificationManager nm =
                            (android.app.NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    if (nm != null) {
                        int[] waqtIds = {1000, 1001, 1002, 1003, 1004, 1005, 1006, 2000, 2001, 2002, 2003, 2004, 2005, 2006};
                        for (int id : waqtIds) {
                            nm.cancel(id);
                        }
                    }
                }

                // 2. Today's 12:00 AM (00:00:00.000)
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
                cal.set(java.util.Calendar.MINUTE, 0);
                cal.set(java.util.Calendar.SECOND, 0);
                cal.set(java.util.Calendar.MILLISECOND, 0);
                long midnightCutoff = cal.getTimeInMillis();

                // 3. 7 days ago timestamp for admin announcements
                long sevenDaysCutoff = System.currentTimeMillis() - (7L * 24 * 60 * 60 * 1000);

                notificationDao.deleteExpiredDailyPrayerNotifications(midnightCutoff);
                notificationDao.deleteExpiredAdminNotifications(sevenDaysCutoff);
            } catch (Exception e) {
                android.util.Log.w("NotificationRepo", "performMidnightCleanup error: " + e.getMessage());
            }
        });
    }

    /**
     * Seeds realistic notifications matching the user's reference screenshot if table is empty.
     */
    public void seedInitialNotificationsIfEmpty() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                if (notificationDao.getTotalCountSync() == 0) {
                    long now = System.currentTimeMillis();
                    java.util.List<NotificationMessageEntity> list = new java.util.ArrayList<>();

                    list.add(new NotificationMessageEntity(
                            "নতুন সুন্নাহ যুক্ত হয়েছে",
                            "নখ কাটার সময়সীমা সুন্নাহটি এখন আপনি পালন করতে পারবেন।",
                            "sunnah",
                            now - (3600 * 1000L), // 1 hour ago
                            false,
                            "sunnah_tracker",
                            "normal",
                            "system"
                    ));

                    list.add(new NotificationMessageEntity(
                            "নতুন সুন্নাহ যুক্ত হয়েছে",
                            "নিয়মিত নখ কাটা সুন্নাহটি এখন আপনি পালন করতে পারবেন।",
                            "sunnah",
                            now - (2 * 3600 * 1000L), // 2 hours ago
                            false,
                            "sunnah_tracker",
                            "normal",
                            "system"
                    ));

                    list.add(new NotificationMessageEntity(
                            "নতুন আমল যুক্ত হয়েছে",
                            "১০০০ বার ইস্তেগফার পাঠ আমলটি এখন আপনার তালিকায় যুক্ত হয়েছে।",
                            "amal",
                            now - (14 * 3600 * 1000L), // 14 hours ago
                            false,
                            "amal_tracker",
                            "normal",
                            "system"
                    ));

                    list.add(new NotificationMessageEntity(
                            "আসরের সালাতের ওয়াক্ত হয়েছে",
                            "আসরের ফরজ ৪ রাকাত সালাতের সময় শুরু হয়েছে। অজু করে জামাতের জন্য মসজিদে উপস্থিত হোন।",
                            "prayer",
                            now - (4 * 3600 * 1000L),
                            true,
                            "salah_tracker",
                            "high",
                            "system"
                    ));

                    list.add(new NotificationMessageEntity(
                            "আজকের নির্বাচিত সহীহ হাদিস",
                            "রাসূলুল্লাহ ﷺ বলেছেন: 'যে ব্যক্তি কোনো সৎকাজের পথ দেখায়, সে ঐ কাজ সম্পাদনকারীর সমান সওয়াব পাবে।' [সহীহ মুসলিম: ১৮৯৩]",
                            "hadith",
                            now - (22 * 3600 * 1000L),
                            true,
                            "daily_hadith",
                            "normal",
                            "system"
                    ));

                    notificationDao.insertAll(list);
                }
            } catch (Exception e) {
                android.util.Log.w("NotificationRepo", "seedInitialNotifications error: " + e.getMessage());
            }
        });
    }

    /**
     * Fetches active push announcements and broadcast notifications from PHP Admin Panel
     * (get_notifications.php) and synchronizes them into local Room SQLite database in real-time.
     */
    public void fetchAndSyncAdminNotifications() {
        if (appContext == null || !com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(appContext)) {
            return;
        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                android.content.SharedPreferences sp = appContext.getSharedPreferences("deanone_admin_notif_prefs", Context.MODE_PRIVATE);
                long lastNotifiedId = sp.getLong("last_dispatched_notif_id", 0);
                boolean isFirstRun = !sp.contains("last_dispatched_notif_id");

                String userIdParam = "";
                try {
                    com.devflux.deenone.data.local.entity.UserProfileEntity profile = AppDatabase.getInstance(appContext).userProfileDao().getActiveProfileSync();
                    if (profile != null && profile.getUserId() != null && !profile.getUserId().trim().isEmpty()) {
                        userIdParam = "&user_id=" + java.net.URLEncoder.encode(profile.getUserId().trim(), "UTF-8");
                    }
                } catch (Exception ignored) {}

                String queryParams = "?since_id=" + lastNotifiedId + userIdParam;
                String endpoint = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(appContext, "get_notifications.php" + queryParams);
                java.net.URL url = new java.net.URL(endpoint);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);

                if (conn.getResponseCode() == java.net.HttpURLConnection.HTTP_OK) {
                    java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(conn.getInputStream(), java.nio.charset.StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    com.google.gson.JsonObject json = new com.google.gson.Gson().fromJson(sb.toString(), com.google.gson.JsonObject.class);
                    if (json != null && json.has("notifications")) {
                        com.google.gson.JsonArray arr = json.getAsJsonArray("notifications");
                        if (arr == null || arr.size() == 0) {
                            return;
                        }

                        if (isFirstRun) {
                            // On fresh install, register highest ID to avoid retroactive notification flood
                            long maxId = 0;
                            for (com.google.gson.JsonElement el : arr) {
                                com.google.gson.JsonObject obj = el.getAsJsonObject();
                                long notifId = obj.has("id") ? obj.get("id").getAsLong() : 0;
                                if (notifId > maxId) maxId = notifId;
                            }
                            sp.edit().putLong("last_dispatched_notif_id", maxId).apply();
                            return;
                        }

                        // Strictly dispatch alert for the single newest notification
                        com.google.gson.JsonObject newestNotif = null;
                        long highestId = lastNotifiedId;

                        for (com.google.gson.JsonElement el : arr) {
                            com.google.gson.JsonObject obj = el.getAsJsonObject();
                            long notifId = obj.has("id") ? obj.get("id").getAsLong() : 0;
                            if (notifId > highestId) {
                                highestId = notifId;
                                newestNotif = obj;
                            }
                        }

                        if (newestNotif != null) {
                            String title = newestNotif.has("title") ? newestNotif.get("title").getAsString() : "";
                            String body = newestNotif.has("body") ? newestNotif.get("body").getAsString() : "";
                            String type = newestNotif.has("notification_type") ? newestNotif.get("notification_type").getAsString() : "ANNOUNCEMENT";
                            String action = newestNotif.has("target_action") ? newestNotif.get("target_action").getAsString() : "default";
                            String priority = newestNotif.has("priority") ? newestNotif.get("priority").getAsString() : "normal";

                            if (!title.trim().isEmpty() && !body.trim().isEmpty()) {
                                // Save to Room SQLite if not already present
                                if (notificationDao.countByTitleAndMessage(title, body) == 0) {
                                    NotificationMessageEntity entity = new NotificationMessageEntity(
                                            title,
                                            body,
                                            type.toLowerCase(),
                                            System.currentTimeMillis(),
                                            false,
                                            action,
                                            priority.toLowerCase(),
                                            "admin_broadcast"
                                    );
                                    notificationDao.insertNotification(entity);
                                }

                                // Play single notification tone and vibration
                                com.devflux.deenone.core.notifications.NotificationHelper.sendDynamicNotification(
                                        appContext,
                                        title,
                                        body,
                                        type.toLowerCase(),
                                        action,
                                        priority.toLowerCase()
                                );
                            }

                            sp.edit().putLong("last_dispatched_notif_id", highestId).apply();
                        }
                    }
                }
            } catch (Exception e) {
                android.util.Log.w("NotificationRepo", "fetchAndSyncAdminNotifications error: " + e.getMessage());
            }
        });
    }
}
