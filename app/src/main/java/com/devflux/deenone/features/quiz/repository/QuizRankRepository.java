package com.devflux.deenone.features.quiz.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.core.quiz.QuizManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.features.quiz.model.QuizRankUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizRankRepository {

    private static final String TAG = "QuizRankRepository";
    private static final String PREF_QUIZ_RANK = "pref_quiz_rank_data";
    private static final String KEY_CACHED_USERS = "key_cached_quiz_users";
    private static final String KEY_USER_RANK = "key_last_user_rank";

    private static volatile QuizRankRepository instance;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Gson gson = new Gson();

    public interface RankCallback {
        void onLoaded(List<QuizRankUser> users, int myRank, int myQuizPoints);
    }

    private QuizRankRepository() {}

    public static QuizRankRepository getInstance() {
        if (instance == null) {
            synchronized (QuizRankRepository.class) {
                if (instance == null) {
                    instance = new QuizRankRepository();
                }
            }
        }
        return instance;
    }

    /**
     * Load Quiz Rankings:
     * 1. Delivers cached local data immediately for 60 FPS 0ms latency.
     * 2. Asynchronously fetches real rankings from PHP/MySQL server and syncs user's actual quiz points.
     */
    public void getQuizRankings(Context context, RankCallback callback) {
        if (context == null) return;
        final Context appContext = context.getApplicationContext();

        executor.execute(() -> {
            // Step 1: Read local quiz points (Pure Quiz Hub Points Only)
            int myQuizPoints = QuizManager.getInstance().getDeenPoints(appContext);

            // Fetch Current User Identity
            String currentUserId = "usr_current";
            String currentUserName = "ব্যবহারকারী";
            String currentLocation = "বাংলাদেশ";

            try {
                AppDatabase db = AppDatabase.getInstance(appContext);
                UserProfileEntity profile = db.userProfileDao().getActiveProfileSync();
                if (profile != null) {
                    if (profile.getUserId() != null && !profile.getUserId().trim().isEmpty()) {
                        currentUserId = profile.getUserId();
                    }
                    if (profile.getFullName() != null && !profile.getFullName().trim().isEmpty()) {
                        currentUserName = profile.getFullName();
                    }
                    if (profile.getTimezone() != null && !profile.getTimezone().trim().isEmpty()) {
                        currentLocation = profile.getTimezone();
                    }
                }
            } catch (Exception ignored) {}

            // Step 2: Read cached rankings from Local Storage
            SharedPreferences prefs = appContext.getSharedPreferences(PREF_QUIZ_RANK, Context.MODE_PRIVATE);
            String cachedJson = prefs.getString(KEY_CACHED_USERS, null);
            int cachedRank = prefs.getInt(KEY_USER_RANK, myQuizPoints > 0 ? 1 : 509);

            List<QuizRankUser> cachedList = new ArrayList<>();
            if (cachedJson != null && !cachedJson.trim().isEmpty()) {
                try {
                    Type listType = new TypeToken<List<QuizRankUser>>(){}.getType();
                    cachedList = gson.fromJson(cachedJson, listType);
                } catch (Exception e) {
                    Log.w(TAG, "Failed parsing cached rankings: " + e.getMessage());
                }
            }

            // Immediately post cached data to UI
            final List<QuizRankUser> initialList = new ArrayList<>(cachedList);
            final int initialRank = cachedRank;
            final int finalMyPoints = myQuizPoints;
            mainHandler.post(() -> {
                if (callback != null) {
                    callback.onLoaded(initialList, initialRank, finalMyPoints);
                }
            });

            // Step 3: Fetch fresh real data from Server Backend
            fetchFromRemoteAndSync(appContext, currentUserId, currentUserName, currentLocation, myQuizPoints, callback);
        });
    }

    private void fetchFromRemoteAndSync(Context context, String userId, String userName, String district,
                                       int myQuizPoints, RankCallback callback) {
        if (!NetworkConnectivityHelper.isOnline(context)) {
            return;
        }

        try {
            // A. First, sync user's real quiz points to server
            syncMyQuizPointsToServer(context, userId, userName, district, myQuizPoints);

            // B. Fetch real rankings from PHP MySQL backend (Up to 50 competitors)
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "quiz_leaderboard.php")
                    + "?limit=50&user_id=" + java.net.URLEncoder.encode(userId, "UTF-8");

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject json = gson.fromJson(response.toString(), JsonObject.class);
                if (json != null && json.has("success") && json.get("success").getAsBoolean()) {
                    JsonArray arr = json.has("leaderboard") ? json.getAsJsonArray("leaderboard") : new JsonArray();
                    List<QuizRankUser> remoteUsers = new ArrayList<>();

                    for (JsonElement el : arr) {
                        JsonObject u = el.getAsJsonObject();
                        int rank = u.has("rank") ? u.get("rank").getAsInt() : remoteUsers.size() + 1;
                        String uId = u.has("user_id") ? u.get("user_id").getAsString() : "";
                        String uName = u.has("name") ? u.get("name").getAsString() : "ব্যবহারকারী";
                        String uDistrict = u.has("district") ? u.get("district").getAsString() : "বাংলাদেশ";
                        String uAvatar = u.has("avatar") ? u.get("avatar").getAsString() : "";
                        int uPoints = u.has("quiz_points") ? u.get("quiz_points").getAsInt() : 0;

                        remoteUsers.add(new QuizRankUser(rank, uId, uName, uDistrict, uAvatar, uPoints));
                    }

                    int calculatedRank = json.has("user_rank") ? json.get("user_rank").getAsInt() : (myQuizPoints == 0 ? 509 : 1);

                    // If user is inside the top 50, ensure rank reflects their exact position
                    for (int i = 0; i < remoteUsers.size(); i++) {
                        if (remoteUsers.get(i).getUserId().equalsIgnoreCase(userId)) {
                            calculatedRank = i + 1;
                            break;
                        }
                    }

                    // Save to local cache
                    SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ_RANK, Context.MODE_PRIVATE);
                    prefs.edit()
                            .putString(KEY_CACHED_USERS, gson.toJson(remoteUsers))
                            .putInt(KEY_USER_RANK, calculatedRank)
                            .apply();

                    final List<QuizRankUser> finalRemoteUsers = remoteUsers;
                    final int finalRank = calculatedRank;
                    mainHandler.post(() -> {
                        if (callback != null) {
                            callback.onLoaded(finalRemoteUsers, finalRank, myQuizPoints);
                        }
                    });
                }
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.w(TAG, "fetchFromRemoteAndSync failed: " + e.getMessage());
        }
    }

    private void syncMyQuizPointsToServer(Context context, String userId, String userName, String district, int quizPoints) {
        try {
            String urlStr = BackendConfigManager.getPhpApiEndpoint(context, "user_sync.php");
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            JsonObject payload = new JsonObject();
            payload.addProperty("user_id", userId);
            payload.addProperty("name", userName);
            payload.addProperty("district", district);
            payload.addProperty("quiz_points", quizPoints);

            SharedPreferences authPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
            String currentAvatar = authPrefs.getString("profile_avatar", authPrefs.getString("user_avatar_uri", ""));
            if (currentAvatar != null && !currentAvatar.isEmpty() && !"avatar_1".equals(currentAvatar)) {
                payload.addProperty("avatar", currentAvatar);
            }

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            conn.getResponseCode();
            conn.disconnect();
        } catch (Exception e) {
            Log.w(TAG, "syncMyQuizPointsToServer error: " + e.getMessage());
        }
    }
}
