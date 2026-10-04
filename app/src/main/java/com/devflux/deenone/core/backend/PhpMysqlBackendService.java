package com.devflux.deenone.core.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.core.admin.AdminRemoteConfigManager;
import com.devflux.deenone.core.backend.model.AdControlConfig;
import com.devflux.deenone.core.backend.model.AppUpdateInfo;
import com.devflux.deenone.core.backend.model.CommunityMessage;
import com.devflux.deenone.core.backend.model.LeaderboardUser;
import com.devflux.deenone.core.backend.model.UserProfile;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Production PHP MySQL Database Backend Service.
 * Connects directly to the DeenOne PHP REST APIs with complete
 * offline-first fallback to local Room SQLite Database and SharedPreferences.
 */
public class PhpMysqlBackendService implements IBackendService {

  private static final String TAG = "PhpMysqlBackend";
  private final Context context;
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private final OkHttpClient httpClient;
  private final Gson gson;
  private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

  private static final String PREF_PHP_USER = "php_sql_user_data";
  private static final String PREF_PHP_BLOOD = "php_sql_blood_donors";

  public PhpMysqlBackendService(Context context) {
    this.context = context.getApplicationContext();
    this.httpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build();
    this.gson = new Gson();
  }

  private Request.Builder newAuthenticatedRequestBuilder(String url) {
    return new Request.Builder()
        .url(url)
        .addHeader("User-Agent", "DeenOne-App/1.0")
        .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context));
  }

  @Override
  public void fetchAdConfig(BackendCallback<AdControlConfig> callback) {
    AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(context);

    if (!NetworkConnectivityHelper.isOnline(context)) {
      mainHandler.post(() -> {
        AdControlConfig fallback = new AdControlConfig(
            rc.isAdsEnabled(),
            rc.getBannerAdUnitId(),
            rc.getInterstitialAdUnitId(),
            rc.getAdFrequencyInterval(),
            rc.getNativeAdUnitId(),
            rc.getRewardedAdUnitId()
        );
        if (callback != null) callback.onSuccess(fallback);
      });
      return;
    }

    executor.execute(() -> {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "get_config.php");
      try {
        Request request = newAuthenticatedRequestBuilder(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
          if (response.isSuccessful() && response.body() != null) {
            String jsonStr = response.body().string();
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);

            if (obj != null && obj.has("admob")) {
              JsonObject admob = obj.getAsJsonObject("admob");
              boolean enabled = admob.has("enabled") ? admob.get("enabled").getAsBoolean() : rc.isAdsEnabled();
              String banner = admob.has("banner_id") ? admob.get("banner_id").getAsString() : rc.getBannerAdUnitId();
              String interstitial = admob.has("interstitial_id") ? admob.get("interstitial_id").getAsString() : rc.getInterstitialAdUnitId();
              int freq = admob.has("frequency_interval") ? admob.get("frequency_interval").getAsInt() : rc.getAdFrequencyInterval();
              String nativeId = admob.has("native_id") ? admob.get("native_id").getAsString() : rc.getNativeAdUnitId();
              String rewarded = admob.has("rewarded_id") ? admob.get("rewarded_id").getAsString() : rc.getRewardedAdUnitId();

              AdControlConfig config = new AdControlConfig(enabled, banner, interstitial, freq, nativeId, rewarded);
              mainHandler.post(() -> {
                if (callback != null) callback.onSuccess(config);
              });
              return;
            }
          }
        }
      } catch (Exception e) {
        Log.w(TAG, "fetchAdConfig failed: " + e.getMessage());
      }

      // Fallback
      mainHandler.post(() -> {
        AdControlConfig fallback = new AdControlConfig(
            rc.isAdsEnabled(),
            rc.getBannerAdUnitId(),
            rc.getInterstitialAdUnitId(),
            rc.getAdFrequencyInterval(),
            rc.getNativeAdUnitId(),
            rc.getRewardedAdUnitId()
        );
        if (callback != null) callback.onSuccess(fallback);
      });
    });
  }

  @Override
  public void fetchAppUpdateInfo(BackendCallback<AppUpdateInfo> callback) {
    if (!NetworkConnectivityHelper.isOnline(context)) {
      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(new AppUpdateInfo());
      });
      return;
    }

    executor.execute(() -> {
      String url = BackendConfigManager.getPhpApiEndpoint(context, "get_config.php");
      try {
        Request request = newAuthenticatedRequestBuilder(url).build();
        try (Response response = httpClient.newCall(request).execute()) {
          if (response.isSuccessful() && response.body() != null) {
            String jsonStr = response.body().string();
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);

            if (obj != null) {
              int latestVersion = obj.has("latest_version_code") ? obj.get("latest_version_code").getAsInt() : 1;
              boolean force = obj.has("force_update") && obj.get("force_update").getAsBoolean();
              String updateUrl = obj.has("update_url") ? obj.get("update_url").getAsString() : "https://play.google.com/store/apps/details?id=com.devflux.deenone";

              AppUpdateInfo info = new AppUpdateInfo(
                  latestVersion, "1.0." + latestVersion, force,
                  "দ্বীনওয়ান আপডেট", "নতুন ফিচার এবং বাগ ফিক্স সংযুক্ত করা হয়েছে।",
                  updateUrl
              );
              mainHandler.post(() -> {
                if (callback != null) callback.onSuccess(info);
              });
              return;
            }
          }
        }
      } catch (Exception e) {
        Log.w(TAG, "fetchAppUpdateInfo failed: " + e.getMessage());
      }

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(new AppUpdateInfo());
      });
    });
  }

  @Override
  public void fetchLeaderboard(BackendCallback<List<LeaderboardUser>> callback) {
    executor.execute(() -> {
      if (NetworkConnectivityHelper.isOnline(context)) {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "leaderboard.php");
        try {
          Request request = newAuthenticatedRequestBuilder(url).build();
          try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
              String jsonStr = response.body().string();
              JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);

              if (obj != null && obj.has("leaderboard")) {
                JsonArray arr = obj.getAsJsonArray("leaderboard");
                List<LeaderboardUser> users = new ArrayList<>();

                for (JsonElement el : arr) {
                  JsonObject u = el.getAsJsonObject();
                  users.add(new LeaderboardUser(
                      u.has("rank") ? u.get("rank").getAsInt() : (users.size() + 1),
                      u.has("user_id") ? u.get("user_id").getAsString() : "",
                      u.has("user_name") ? u.get("user_name").getAsString() : "ইউজার",
                      u.has("avatar_url") ? u.get("avatar_url").getAsString() : "avatar_1",
                      u.has("timezone") ? u.get("timezone").getAsString() : "ঢাকা",
                      u.has("points") ? u.get("points").getAsInt() : 0,
                      u.has("streak_days") ? u.get("streak_days").getAsInt() : 1,
                      u.has("tier_title") ? u.get("tier_title").getAsString() : "নবাগত"
                  ));
                }

                if (!users.isEmpty()) {
                  mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(users);
                  });
                  return;
                }
              }
            }
          }
        } catch (Exception e) {
          Log.w(TAG, "fetchLeaderboard remote call failed: " + e.getMessage());
        }
      }

      // Local Room SQLite & Preferences fallback
      try {
        List<LeaderboardUser> localUsers = new ArrayList<>();
        com.devflux.deenone.data.local.AppDatabase db = com.devflux.deenone.data.local.AppDatabase.getInstance(context);
        com.devflux.deenone.data.local.entity.UserProfileEntity activeEntity = db.userProfileDao().getActiveProfileSync();

        if (activeEntity != null && activeEntity.getFullName() != null && !activeEntity.getFullName().trim().isEmpty()) {
          int pts = Math.max(activeEntity.getPoints(), com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(context));
          localUsers.add(new LeaderboardUser(
              1,
              activeEntity.getUserId() != null ? activeEntity.getUserId() : "usr_main",
              activeEntity.getFullName(),
              activeEntity.getAvatarUri() != null ? activeEntity.getAvatarUri() : "",
              activeEntity.getTimezone() != null ? activeEntity.getTimezone() : "ঢাকা",
              pts,
              activeEntity.getStreakDays() > 0 ? activeEntity.getStreakDays() : 1,
              "নবাগত"
          ));
        }

        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(localUsers);
        });
      } catch (Exception e) {
        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(new ArrayList<>());
        });
      }
    });
  }

  @Override
  public void submitUserPoints(String userId, String userName, int points, BackendCallback<Boolean> callback) {
    executor.execute(() -> {
      // 1. Cache locally
      SharedPreferences prefs = context.getSharedPreferences(PREF_PHP_USER, Context.MODE_PRIVATE);
      prefs.edit().putInt("user_points_" + userId, points).apply();

      // 2. Sync to PHP MySQL Backend
      if (NetworkConnectivityHelper.isOnline(context)) {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "user_sync.php");
        try {
          JsonObject payload = new JsonObject();
          payload.addProperty("user_id", userId);
          payload.addProperty("name", userName);
          payload.addProperty("points", points);

          SharedPreferences authPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
          String currentAvatar = authPrefs.getString("profile_avatar", authPrefs.getString("user_avatar_uri", ""));
          if (currentAvatar != null && !currentAvatar.isEmpty() && !"avatar_1".equals(currentAvatar)) {
            payload.addProperty("avatar", currentAvatar);
          }

          RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
          Request request = newAuthenticatedRequestBuilder(url).post(body).build();

          try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
              Log.d(TAG, "submitUserPoints remote sync success.");
            }
          }
        } catch (Exception e) {
          Log.w(TAG, "submitUserPoints remote sync failed: " + e.getMessage());
        }
      }

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(true);
      });
    });
  }

  @Override
  public void fetchCommunityMessages(String category, BackendCallback<List<CommunityMessage>> callback) {
    executor.execute(() -> {
      if (NetworkConnectivityHelper.isOnline(context)) {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
        try {
          Request request = newAuthenticatedRequestBuilder(url).build();
          try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
              String jsonStr = response.body().string();
              JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);

              if (obj != null && obj.has("posts")) {
                JsonArray arr = obj.getAsJsonArray("posts");
                List<CommunityMessage> messages = new ArrayList<>();

                for (JsonElement el : arr) {
                  JsonObject p = el.getAsJsonObject();
                  messages.add(new CommunityMessage(
                      p.has("id") ? "msg_" + p.get("id").getAsString() : "msg_1",
                      p.has("author_name") ? p.get("author_name").getAsString() : "ব্যবহারকারী",
                      p.has("user_id") ? p.get("user_id").getAsString() : "usr_anon",
                      "সাধারণ",
                      p.has("content") ? p.get("content").getAsString() : "",
                      System.currentTimeMillis(),
                      p.has("likes_count") ? p.get("likes_count").getAsInt() : 0
                  ));
                }

                if (!messages.isEmpty()) {
                  mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(messages);
                  });
                  return;
                }
              }
            }
          }
        } catch (Exception e) {
          Log.w(TAG, "fetchCommunityMessages remote call failed: " + e.getMessage());
        }
      }

      // Offline Default Messages
      List<CommunityMessage> defaultMessages = new ArrayList<>();
      defaultMessages.add(new CommunityMessage(
          "msg_1", "মাওলানা আব্দুল করিম", "usr_admin",
          "দ্বীনি আলোচনা", "রমজানের শেষ দশকে বেশি বেশি ইতিকাফ ও কুরআন তিলাওয়াত করুন।",
          System.currentTimeMillis() - 3600000, 42
      ));
      defaultMessages.add(new CommunityMessage(
          "msg_2", "মুহাম্মদ রাফি", "usr_201",
          "প্রশ্নোত্তর", "তাহাজ্জুদ নামাজের সর্বনিম্ন ও সর্বোচ্চ রাকাত সংখ্যা কত?",
          System.currentTimeMillis() - 7200000, 18
      ));

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(defaultMessages);
      });
    });
  }

  @Override
  public void postCommunityMessage(CommunityMessage message, BackendCallback<Boolean> callback) {
    if (message == null) {
      if (callback != null) callback.onError("অকার্যকর বার্তা।");
      return;
    }

    executor.execute(() -> {
      if (NetworkConnectivityHelper.isOnline(context)) {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "community_feed.php");
        try {
          JsonObject payload = new JsonObject();
          payload.addProperty("action", "create_post");
          payload.addProperty("user_id", message.getSenderId());
          payload.addProperty("author_name", message.getSenderName());
          payload.addProperty("content", message.getContent());

          RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
          Request request = newAuthenticatedRequestBuilder(url).post(body).build();

          try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
              mainHandler.post(() -> {
                if (callback != null) callback.onSuccess(true);
              });
              return;
            }
          }
        } catch (Exception e) {
          Log.w(TAG, "postCommunityMessage remote sync failed: " + e.getMessage());
        }
      }

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(true);
      });
    });
  }

  @Override
  public void fetchUserProfile(String userId, BackendCallback<UserProfile> callback) {
    executor.execute(() -> {
      SharedPreferences prefs = context.getSharedPreferences(PREF_PHP_USER, Context.MODE_PRIVATE);
      SharedPreferences profilePrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);

      String name = prefs.getString("user_name_" + userId, profilePrefs.getString("user_full_name", profilePrefs.getString("profile_full_name", "দ্বীনওয়ান ব্যবহারকারী")));
      String timezone = prefs.getString("user_tz_" + userId, profilePrefs.getString("user_location", "Asia/Dhaka"));
      int points = prefs.getInt("user_points_" + userId, 0);
      long joinMs = prefs.getLong("user_join_" + userId, profilePrefs.getLong("user_joined_timestamp", System.currentTimeMillis()));
      String avatarUrl = prefs.getString("user_avatar_" + userId, profilePrefs.getString("user_avatar_uri", ""));
      int avatarPreset = prefs.getInt("user_preset_" + userId, profilePrefs.getInt("user_avatar_preset", 0));

      UserProfile profile = new UserProfile(
          userId, name, "user@deenone.top", "",
          avatarUrl, avatarPreset, points, joinMs, timezone, true
      );

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(profile);
      });
    });
  }

  @Override
  public void updateUserProfile(UserProfile profile, BackendCallback<Boolean> callback) {
    if (profile == null) {
      if (callback != null) callback.onError("অকার্যকর প্রোফাইল ডাটা।");
      return;
    }

    executor.execute(() -> {
      String uid = profile.getUserId();
      SharedPreferences prefs = context.getSharedPreferences(PREF_PHP_USER, Context.MODE_PRIVATE);
      SharedPreferences.Editor ed = prefs.edit();
      ed.putString("user_name_" + uid, profile.getUserName());
      ed.putString("user_tz_" + uid, profile.getTimezone());
      ed.putInt("user_points_" + uid, profile.getTotalPoints());
      ed.putLong("user_join_" + uid, profile.getJoiningTimestamp());
      ed.putString("user_avatar_" + uid, profile.getAvatarUrl());
      ed.putInt("user_preset_" + uid, profile.getAvatarPreset());
      ed.apply();

      if (NetworkConnectivityHelper.isOnline(context)) {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "user_sync.php");
        try {
          JsonObject payload = new JsonObject();
          payload.addProperty("user_id", uid);
          payload.addProperty("name", profile.getUserName());
          payload.addProperty("district", profile.getTimezone());
          payload.addProperty("points", profile.getTotalPoints());
          payload.addProperty("avatar", profile.getAvatarUrl());

          RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
          Request request = newAuthenticatedRequestBuilder(url).post(body).build();
          httpClient.newCall(request).execute().close();
        } catch (Exception e) {
          Log.w(TAG, "updateUserProfile remote sync failed: " + e.getMessage());
        }
      }

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(true);
      });
    });
  }

  @Override
  public void uploadProfileImage(String userId, File imageFile, BackendCallback<String> callback) {
    if (imageFile == null || !imageFile.exists()) {
      mainHandler.post(() -> {
        if (callback != null) callback.onError("অকার্যকর ছবি ফাইল পাওয়া গেছে।");
      });
      return;
    }

    executor.execute(() -> {
      File dir = new File(context.getFilesDir(), "backend_storage/avatars");
      if (!dir.exists()) dir.mkdirs();

      String fileName = imageFile.getName().toLowerCase();
      String ext = ".jpg";
      String mimeType = "image/jpeg";
      if (fileName.endsWith(".gif")) {
        ext = ".gif";
        mimeType = "image/gif";
      } else if (fileName.endsWith(".png")) {
        ext = ".png";
        mimeType = "image/png";
      } else if (fileName.endsWith(".webp")) {
        ext = ".webp";
        mimeType = "image/webp";
      }

      File target = new File(dir, "avatar_" + userId + ext);

      try (FileInputStream in = new FileInputStream(imageFile);
           FileOutputStream out = new FileOutputStream(target)) {
        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        out.flush();
      } catch (Exception ignored) {}

      String localPath = target.getAbsolutePath();
      SharedPreferences prefs = context.getSharedPreferences(PREF_PHP_USER, Context.MODE_PRIVATE);
      prefs.edit().putString("user_avatar_" + userId, localPath).apply();
      SharedPreferences authPrefs = context.getSharedPreferences("user_profile_prefs", Context.MODE_PRIVATE);
      authPrefs.edit()
          .putString("profile_avatar", localPath)
          .putString("user_avatar_uri", localPath)
          .putInt("user_avatar_preset", -1)
          .apply();

      if (!NetworkConnectivityHelper.isOnline(context)) {
        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(localPath);
        });
        return;
      }

      try {
        String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "upload_avatar.php");
        MultipartBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("user_id", userId)
            .addFormDataPart("avatar", imageFile.getName(),
                RequestBody.create(imageFile, MediaType.parse(mimeType)))
            .build();

        Request request = new Request.Builder()
            .url(endpoint)
            .addHeader("User-Agent", "DeenOne-App/1.0 (Android)")
            .addHeader("X-API-KEY", BackendConfigManager.getPhpApiKey(context))
            .post(requestBody)
            .build();

        try (Response response = httpClient.newCall(request).execute()) {
          if (response.isSuccessful() && response.body() != null) {
            String resStr = response.body().string();
            JsonObject resObj = gson.fromJson(resStr, JsonObject.class);
            if (resObj != null && resObj.has("avatar_url")) {
              String avatarUrl = resObj.get("avatar_url").getAsString();
              prefs.edit().putString("user_avatar_" + userId, avatarUrl).apply();
              authPrefs.edit()
                  .putString("profile_avatar", avatarUrl)
                  .putString("user_avatar_uri", avatarUrl)
                  .putInt("user_avatar_preset", -1)
                  .apply();

              mainHandler.post(() -> {
                if (callback != null) callback.onSuccess(avatarUrl);
              });
              return;
            }
          }
        }
      } catch (Exception e) {
        Log.e(TAG, "uploadProfileImage remote error: " + e.getMessage());
      }

      // Offline or network fallback
      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(localPath);
      });
    });
  }

  @Override
  public void registerBloodDonor(String userId, String name, String bloodGroup, String phone, String district, BackendCallback<Boolean> callback) {
    executor.execute(() -> {
      // 1. Local Cache
      SharedPreferences prefs = context.getSharedPreferences(PREF_PHP_BLOOD, Context.MODE_PRIVATE);
      prefs.edit()
          .putString("donor_name_" + userId, name)
          .putString("donor_group_" + userId, bloodGroup)
          .putString("donor_phone_" + userId, phone)
          .putString("donor_district_" + userId, district)
          .putLong("donor_time_" + userId, System.currentTimeMillis())
          .apply();

      // 2. Remote Sync
      if (NetworkConnectivityHelper.isOnline(context)) {
        String url = BackendConfigManager.getPhpApiEndpoint(context, "blood_donors.php");
        try {
          JsonObject payload = new JsonObject();
          payload.addProperty("action", "register_donor");
          payload.addProperty("user_id", userId);
          payload.addProperty("name", name);
          payload.addProperty("blood_group", bloodGroup);
          payload.addProperty("phone", phone);
          payload.addProperty("district", district);
          payload.addProperty("is_available", 1);

          RequestBody body = RequestBody.create(payload.toString(), JSON_MEDIA_TYPE);
          Request request = newAuthenticatedRequestBuilder(url).post(body).build();
          httpClient.newCall(request).execute().close();
        } catch (Exception e) {
          Log.w(TAG, "registerBloodDonor remote sync failed: " + e.getMessage());
        }
      }

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(true);
      });
    });
  }
}
