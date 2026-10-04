package com.devflux.deenone.core.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.features.community.data.CommunityRepository;
import com.devflux.deenone.features.profile.ProfileStatsAggregator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Enterprise User Activity Sync & Cloud Restore Engine for DeenOne.
 * Synchronizes in real time:
 *   1. Namaz / Salah Tracker (user_prayer_logs)
 *   2. Nek Amal Tracker (user_amal_logs)
 *   3. Quiz Results & Scores (user_quiz_results)
 *   4. Continuous Streak & Calendar (daily_streak, last_streak_date)
 *   5. Restores all user activities from MySQL database upon login or device sync.
 *   6. Propagates Name / Nickname changes to MySQL database and Community.
 */
public class UserActivitySyncManager {

    private static final String TAG = "UserActivitySyncMgr";
    private static volatile UserActivitySyncManager instance;
    private final Context context;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public interface OnSyncCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    private UserActivitySyncManager(Context context) {
        this.context = context.getApplicationContext();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static UserActivitySyncManager getInstance(Context context) {
        if (instance == null) {
            synchronized (UserActivitySyncManager.class) {
                if (instance == null) {
                    instance = new UserActivitySyncManager(context);
                }
            }
        }
        return instance;
    }

    private String getActiveUserId() {
        if (!AuthManager.isLoggedIn(context)) {
            return "";
        }
        AuthManager.UserSession session = AuthManager.getCurrentSession(context);
        if (session != null && !TextUtils.isEmpty(session.userId)) {
            return session.userId;
        }
        SharedPreferences prefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
        return prefs.getString("profile_user_id", prefs.getString("user_uid", ""));
    }

    // =========================================================================
    // 1. Real-Time Prayer / Salah Log Sync
    // =========================================================================
    public void syncPrayerLog(String prayerKey, String prayerDate, String status,
                              boolean isPrayed, boolean isJamah, boolean isQaza, int points) {
        syncPrayerLog(prayerKey, prayerDate, isPrayed, isJamah, isQaza, status, points);
    }

    public void syncPrayerLog(String prayerName, String prayerDate, boolean isPrayed,
                              boolean isJamah, boolean isQaza, String status, int points) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) return;

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "sync_prayer");
                payload.addProperty("user_id", userId);
                payload.addProperty("prayer_name", prayerName);
                payload.addProperty("prayer_date", prayerDate);
                payload.addProperty("is_prayed", isPrayed ? 1 : 0);
                payload.addProperty("is_jamah", isJamah ? 1 : 0);
                payload.addProperty("is_qaza", isQaza ? 1 : 0);
                payload.addProperty("status", status != null ? status : "NONE");
                payload.addProperty("points", points);

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Prayer synced: " + prayerName + " on " + prayerDate);
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "syncPrayerLog error: " + e.getMessage());
            }
        });
    }

    // =========================================================================
    // 2. Real-Time Daily Amal Sync
    // =========================================================================
    public void syncAmalItem(String logDate, String amalKey, String title, int points, boolean isCompleted) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context);
                int earnedPoints = db.dailyAmalDao().getEarnedPointsByDateSync(logDate);

                syncAmalLog(logDate, true, true, true, true, true, false, false, 15, true, false, 33, earnedPoints);
            } catch (Exception ignored) {}
        });
    }

    public void syncAmalLog(String logDate, boolean fajr, boolean dhuhr, boolean asr,
                            boolean maghrib, boolean isha, boolean tahajjud, boolean dhuha,
                            int quranMinutes, boolean sadaqah, boolean fasting, int dhikrCount, int score) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) return;

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "sync_amal");
                payload.addProperty("user_id", userId);
                payload.addProperty("log_date", logDate);
                payload.addProperty("fajr", fajr ? 1 : 0);
                payload.addProperty("dhuhr", dhuhr ? 1 : 0);
                payload.addProperty("asr", asr ? 1 : 0);
                payload.addProperty("maghrib", maghrib ? 1 : 0);
                payload.addProperty("isha", isha ? 1 : 0);
                payload.addProperty("tahajjud", tahajjud ? 1 : 0);
                payload.addProperty("dhuha", dhuha ? 1 : 0);
                payload.addProperty("quran_minutes", quranMinutes);
                payload.addProperty("sadaqah", sadaqah ? 1 : 0);
                payload.addProperty("fasting", fasting ? 1 : 0);
                payload.addProperty("dhikr_count", dhikrCount);
                payload.addProperty("score", score);

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Amal log synced for " + logDate);
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "syncAmalLog error: " + e.getMessage());
            }
        });
    }

    // =========================================================================
    // 2b. Real-Time Tasbih & Dhikr Challenge Sync
    // =========================================================================
    public void syncTasbihLog(int dhikrIndex, String dhikrTitle, int tapCount, int completedTimes, int pointsEarned) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) return;

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "sync_tasbih");
                payload.addProperty("user_id", userId);
                payload.addProperty("dhikr_index", dhikrIndex);
                payload.addProperty("dhikr_title", dhikrTitle != null ? dhikrTitle : "Dhikr #" + (dhikrIndex + 1));
                payload.addProperty("tap_count", tapCount);
                payload.addProperty("completed_times", completedTimes);
                payload.addProperty("points_earned", pointsEarned);
                payload.addProperty("log_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Tasbih synced: " + dhikrTitle + " (" + tapCount + " taps, " + completedTimes + " rounds)");
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "syncTasbihLog error: " + e.getMessage());
            }
        });
    }

    // =========================================================================
    // 2c. Real-Time Specific Amal Completion Sync
    // =========================================================================
    public void syncAmalCompletion(String amalCode, String amalTitle, int points) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) return;

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "sync_amal_record");
                payload.addProperty("user_id", userId);
                payload.addProperty("amal_code", amalCode != null ? amalCode : "AMAL_GENERIC");
                payload.addProperty("amal_title", amalTitle != null ? amalTitle : "Daily Good Deed");
                payload.addProperty("points", points);
                payload.addProperty("log_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Amal record synced: " + amalTitle + " (+" + points + " pts)");
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "syncAmalCompletion error: " + e.getMessage());
            }
        });
    }

    // =========================================================================
    // 3. Real-Time Quiz Result & Score Sync
    // =========================================================================
    public void syncQuizResult(String categoryId, int score, int totalQuestions, int correctCount, int pointsEarned) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) return;

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "sync_quiz");
                payload.addProperty("user_id", userId);
                payload.addProperty("category_id", categoryId != null ? categoryId : "general");
                payload.addProperty("score", score);
                payload.addProperty("total_questions", totalQuestions);
                payload.addProperty("correct_count", correctCount);
                payload.addProperty("points_earned", pointsEarned);

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Quiz synced: +" + pointsEarned + " points");
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "syncQuizResult error: " + e.getMessage());
            }
        });
    }

    public void syncQuizAnswer(String categoryId, boolean isCorrect, int points) {
        syncQuizResult(categoryId, isCorrect ? 100 : 0, 1, isCorrect ? 1 : 0, points);
    }

    // =========================================================================
    // 4. Real-Time Streak & Continuous Calendar Sync
    // =========================================================================
    public void syncStreak(int streak) {
        syncStreak(streak, new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));
    }

    public void syncStreak(int streak, String dateIso) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) return;

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) return;

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "sync_streak");
                payload.addProperty("user_id", userId);
                payload.addProperty("daily_streak", streak);
                payload.addProperty("last_streak_date", dateIso != null ? dateIso : new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()));

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Streak synced: " + streak + " days");
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "syncStreak error: " + e.getMessage());
            }
        });
    }

    // =========================================================================
    // 5. Update User Name / Nickname in MySQL Database & Community
    // =========================================================================
    public void updateUserNickname(String newName, OnSyncCallback callback) {
        updateUserNickname(newName, null, callback);
    }

    public void updateUserNickname(String newName, String newUsername, OnSyncCallback callback) {
        String userId = getActiveUserId();
        if (TextUtils.isEmpty(userId)) {
            if (callback != null) callback.onError("No active user session.");
            return;
        }

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) {
                    if (callback != null) mainHandler.post(() -> callback.onError("অফলাইন মোড: ইন্টারনেট সংযোগ চালু করুন।"));
                    return;
                }

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                JsonObject payload = new JsonObject();
                payload.addProperty("action", "update_nickname");
                payload.addProperty("user_id", userId);
                payload.addProperty("name", newName);
                if (newUsername != null) payload.addProperty("username", newUsername);

                RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .post(body)
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String respStr = response.body().string();
                        JsonObject resObj = gson.fromJson(respStr, JsonObject.class);
                        if (resObj != null && resObj.has("success") && resObj.get("success").getAsBoolean()) {
                            CommunityRepository.getInstance(context).recalculateAndNotifyStats();
                            if (callback != null) {
                                mainHandler.post(() -> callback.onSuccess("নাম ও ইউজারনেম ডাটাবেজে সফলভাবে সংরক্ষিত হয়েছে!"));
                            }
                            return;
                        }
                    }
                }
                if (callback != null) {
                    mainHandler.post(() -> callback.onError("সার্ভার থেকে নাম আপডেট করা সম্ভব হয়নি।"));
                }
            } catch (Exception e) {
                if (callback != null) {
                    mainHandler.post(() -> callback.onError(e.getMessage()));
                }
            }
        });
    }

    // =========================================================================
    // 5b. Upload Pre-Login / Offline Activities to MySQL Database upon Login
    // =========================================================================
    public void uploadPreLoginOrPendingActivities(String targetUserId, Runnable onComplete) {
        final String userId = !TextUtils.isEmpty(targetUserId) ? targetUserId : getActiveUserId();
        if (TextUtils.isEmpty(userId) || "guest".equals(userId) || "usr_guest".equals(userId)) {
            if (onComplete != null) mainHandler.post(onComplete);
            return;
        }

        executor.execute(() -> {
            try {
                if (NetworkConnectivityHelper.isOnline(context)) {
                    AppDatabase db = AppDatabase.getInstance(context);
                    db.prayerLogDao().deleteDuplicatesSync();

                    // 1. Batch upload local prayers
                    List<PrayerLogEntity> localPrayers = db.prayerLogDao().getAllLogsSync();
                    if (localPrayers != null && !localPrayers.isEmpty()) {
                        String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                        JsonObject payload = new JsonObject();
                        payload.addProperty("action", "sync_batch_prayers");
                        payload.addProperty("user_id", userId);

                        JsonArray prayersArray = new JsonArray();
                        for (PrayerLogEntity pl : localPrayers) {
                            if (pl.getPrayerName() != null && pl.getDate() != null) {
                                JsonObject p = new JsonObject();
                                p.addProperty("prayer_name", pl.getPrayerName());
                                p.addProperty("prayer_date", pl.getDate());
                                p.addProperty("is_prayed", pl.isPrayed() ? 1 : 0);
                                p.addProperty("is_jamah", pl.isWithJamat() ? 1 : 0);
                                p.addProperty("is_qaza", pl.isQaza() ? 1 : 0);
                                p.addProperty("status", pl.getStatus() != null ? pl.getStatus() : "NONE");
                                p.addProperty("points", pl.getPoints());
                                prayersArray.add(p);
                            }
                        }
                        payload.add("prayers", prayersArray);

                        RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                        Request request = new Request.Builder()
                                .url(url)
                                .addHeader("User-Agent", "DeenOne-App/1.0")
                                .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                                .post(body)
                                .build();
                        try (Response response = httpClient.newCall(request).execute()) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Batch uploaded " + localPrayers.size() + " pre-login prayers to MySQL");
                            }
                        }
                    }

                    // 2. Batch upload local amals
                    List<String> completedDates = db.dailyAmalDao().getAllCompletedDatesSync();
                    if (completedDates != null && !completedDates.isEmpty()) {
                        String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php");
                        JsonObject payload = new JsonObject();
                        payload.addProperty("action", "sync_batch_amal");
                        payload.addProperty("user_id", userId);

                        JsonArray amalsArray = new JsonArray();
                        for (String dateStr : completedDates) {
                            int score = db.dailyAmalDao().getEarnedPointsByDateSync(dateStr);
                            List<DailyAmalEntity> amals = db.dailyAmalDao().getAmalsByDateSync(dateStr);
                            JsonObject a = new JsonObject();
                            a.addProperty("log_date", dateStr);
                            a.addProperty("score", score);
                            if (amals != null) {
                                for (DailyAmalEntity item : amals) {
                                    if (item.isCompleted()) {
                                        String code = item.getAmalCode() != null ? item.getAmalCode() : (item.getTitle() != null ? item.getTitle() : "");
                                        String k = code.toLowerCase(Locale.US);
                                        if (k.contains("quran")) a.addProperty("quran_minutes", 15);
                                        if (k.contains("sadaqah")) a.addProperty("sadaqah", 1);
                                        if (k.contains("fast")) a.addProperty("fasting", 1);
                                        if (k.contains("dhikr")) a.addProperty("dhikr_count", 33);
                                    }
                                }
                            }
                            amalsArray.add(a);
                        }
                        payload.add("amals", amalsArray);

                        RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
                        Request request = new Request.Builder()
                                .url(url)
                                .addHeader("User-Agent", "DeenOne-App/1.0")
                                .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                                .post(body)
                                .build();
                        try (Response response = httpClient.newCall(request).execute()) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Batch uploaded " + completedDates.size() + " pre-login amals to MySQL");
                            }
                        }
                    }

                    // 3. Sync Qaza to server
                    com.devflux.deenone.core.prayer.QazaCalculatorManager.syncQazaToServer(context);
                }
            } catch (Exception e) {
                Log.w(TAG, "uploadPreLoginOrPendingActivities error: " + e.getMessage());
            } finally {
                // 4. Finally restore all activities & merge from server
                restoreActivitiesFromServer(userId, onComplete);
            }
        });
    }

    // =========================================================================
    // 6. Full Cloud Restore on Login (ডাটাবেজ থেকে নামাজ, আমল ও কুইজ ডাটা রিস্টোর)
    // =========================================================================
    public void restoreActivitiesFromServer(String targetUserId, Runnable onComplete) {
        final String userId = !TextUtils.isEmpty(targetUserId) ? targetUserId : getActiveUserId();
        if (TextUtils.isEmpty(userId) || "guest".equals(userId)) {
            if (onComplete != null) mainHandler.post(onComplete);
            return;
        }

        executor.execute(() -> {
            try {
                if (!NetworkConnectivityHelper.isOnline(context)) {
                    if (onComplete != null) mainHandler.post(onComplete);
                    return;
                }

                String url = BackendConfigManager.getPhpApiEndpoint(context, "activity_sync.php?action=restore_activities&user_id=" + userId);
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonStr = response.body().string();
                        JsonObject root = gson.fromJson(jsonStr, JsonObject.class);

                        if (root != null && root.has("success") && root.get("success").getAsBoolean()) {
                            AppDatabase db = AppDatabase.getInstance(context);
                            db.prayerLogDao().deleteDuplicatesSync();

                            // 1. Restore Prayer Logs into Room SQLite
                            if (root.has("prayer_logs") && root.get("prayer_logs").isJsonArray()) {
                                JsonArray prayers = root.getAsJsonArray("prayer_logs");
                                for (JsonElement el : prayers) {
                                    JsonObject p = el.getAsJsonObject();
                                    String pName = p.has("prayer_name") ? p.get("prayer_name").getAsString() : "";
                                    String pDate = p.has("prayer_date") ? p.get("prayer_date").getAsString() : "";
                                    boolean isPrayed = p.has("is_prayed") && p.get("is_prayed").getAsInt() == 1;
                                    boolean isJamah = p.has("is_jamah") && p.get("is_jamah").getAsInt() == 1;
                                    boolean isQaza = p.has("is_qaza") && p.get("is_qaza").getAsInt() == 1;
                                    String status = p.has("status") && !p.get("status").isJsonNull() ? p.get("status").getAsString() : "NONE";
                                    int points = p.has("points") ? p.get("points").getAsInt() : 0;

                                    if (!pName.isEmpty() && !pDate.isEmpty()) {
                                        PrayerLogEntity entity = new PrayerLogEntity(
                                                pDate, pName, isPrayed, isJamah, isQaza, status, points, System.currentTimeMillis()
                                        );
                                        db.prayerLogDao().insertOrUpdate(entity);
                                    }
                                }
                                db.prayerLogDao().deleteDuplicatesSync();
                            }

                            int serverPrayedCount = 0;
                            if (root.has("prayer_summary") && root.get("prayer_summary").isJsonObject()) {
                                serverPrayedCount = root.getAsJsonObject("prayer_summary").has("total_prayed") ?
                                        root.getAsJsonObject("prayer_summary").get("total_prayed").getAsInt() : 0;
                            }

                            int serverAmalCount = 0;
                            if (root.has("amal_summary") && root.get("amal_summary").isJsonObject()) {
                                serverAmalCount = root.getAsJsonObject("amal_summary").has("total_deeds") ?
                                        root.getAsJsonObject("amal_summary").get("total_deeds").getAsInt() : 0;
                                if (serverAmalCount > 0) {
                                    SharedPreferences amalPrefs = context.getSharedPreferences("amal_tracker_prefs", Context.MODE_PRIVATE);
                                    int currentAmals = amalPrefs.getInt("total_completed_amals_count", 0);
                                    if (serverAmalCount > currentAmals) {
                                        amalPrefs.edit().putInt("total_completed_amals_count", serverAmalCount).apply();
                                    }
                                }
                            }

                            int serverQuizCount = 0;
                            if (root.has("quiz_summary") && root.get("quiz_summary").isJsonObject()) {
                                serverQuizCount = root.getAsJsonObject("quiz_summary").has("total_played") ?
                                        root.getAsJsonObject("quiz_summary").get("total_played").getAsInt() : 0;
                                if (serverQuizCount > 0) {
                                    com.devflux.deenone.core.quiz.QuizManager.getInstance().setLifetimeQuizzesPlayedCount(context, serverQuizCount);
                                }
                            }

                            // 2. Restore User Profile & Streak Details
                            if (root.has("user") && root.get("user").isJsonObject()) {
                                JsonObject u = root.getAsJsonObject("user");
                                int totalPoints = u.has("total_points") ? u.get("total_points").getAsInt() : 0;
                                int quizPoints = u.has("quiz_points") ? u.get("quiz_points").getAsInt() : 0;
                                int dailyStreak = u.has("daily_streak") ? u.get("daily_streak").getAsInt() : 1;
                                String avatar = u.has("avatar") && !u.get("avatar").isJsonNull() ? u.get("avatar").getAsString() : "avatar_1";
                                String name = u.has("name") && !u.get("name").isJsonNull() ? u.get("name").getAsString() : "";
                                String username = u.has("username") && !u.get("username").isJsonNull() ? u.get("username").getAsString() : "";

                                if (totalPoints > 0) {
                                    com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.syncInitialPoints(context, totalPoints);
                                }

                                SharedPreferences gamificationPrefs = context.getSharedPreferences("deanone_gamification_prefs", Context.MODE_PRIVATE);
                                int currentXp = gamificationPrefs.getInt("key_total_xp", 0);
                                int finalPoints = Math.max(currentXp, totalPoints);
                                gamificationPrefs.edit()
                                        .putInt("key_total_xp", finalPoints)
                                        .putInt("key_current_streak", dailyStreak)
                                        .apply();

                                SharedPreferences quizPrefs = context.getSharedPreferences("deanone_quiz_prefs", Context.MODE_PRIVATE);
                                quizPrefs.edit().putInt("key_deen_points", quizPoints).apply();

                                SharedPreferences profilePrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor pEdit = profilePrefs.edit();
                                if (!name.isEmpty()) {
                                    pEdit.putString("profile_full_name", name);
                                    pEdit.putString("user_full_name", name);
                                }
                                if (!avatar.isEmpty()) {
                                    pEdit.putString("profile_avatar", avatar);
                                    if (avatar.startsWith("http") || avatar.startsWith("uploads/")) {
                                        pEdit.putString("user_avatar_uri", avatar);
                                    }
                                }
                                int curProfPoints = profilePrefs.getInt("profile_points", 0);
                                pEdit.putInt("profile_points", Math.max(curProfPoints, finalPoints));
                                pEdit.apply();

                                UserProfileEntity profile = db.userProfileDao().getUserProfileSync(userId);
                                if (profile != null) {
                                    if (!name.isEmpty()) profile.setFullName(name);
                                    if (finalPoints > profile.getPoints()) profile.setPoints(finalPoints);
                                    if (dailyStreak > profile.getStreakDays()) profile.setStreakDays(dailyStreak);
                                    if (!avatar.isEmpty()) profile.setAvatarUri(avatar);
                                    if (serverPrayedCount > profile.getSalahCompletedTotal()) profile.setSalahCompletedTotal(serverPrayedCount);
                                    if (serverAmalCount > profile.getAmalCompletedTotal()) profile.setAmalCompletedTotal(serverAmalCount);
                                    if (serverQuizCount > profile.getQuizCompletedTotal()) profile.setQuizCompletedTotal(serverQuizCount);
                                    db.userProfileDao().insertOrUpdateProfile(profile);
                                }
                            }

                            // 3. Recalculate 4-Grid statistics and calendar
                            ProfileStatsAggregator.refreshAndSyncAllStatistics(context, userId, null);

                            // 4. Restore Qaza lifetime records from MySQL
                            com.devflux.deenone.core.prayer.QazaCalculatorManager.fetchQazaFromServer(context, null);

                            Log.d(TAG, "All activities and stats restored from MySQL database successfully.");
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "restoreActivitiesFromServer error: " + e.getMessage());
            } finally {
                if (onComplete != null) mainHandler.post(onComplete);
            }
        });
    }
}
