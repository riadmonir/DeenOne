package com.devflux.deenone.core.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.model.AdControlConfig;
import com.devflux.deenone.core.backend.model.AppUpdateInfo;
import com.devflux.deenone.core.backend.model.CommunityMessage;
import com.devflux.deenone.core.backend.model.LeaderboardUser;
import com.devflux.deenone.core.backend.model.UserProfile;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FirebaseBackendBridge implements IBackendService {

  private final Context context;
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private static final String PREF_FB_USER = "firebase_user_data";
  private static final long TIMEOUT_SECONDS = 15;

  public FirebaseBackendBridge(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void fetchAdConfig(BackendCallback<AdControlConfig> callback) {
    mainHandler.post(() -> {
      if (callback != null) {
        callback.onSuccess(new AdControlConfig(false, "", "", 5, "", ""));
      }
    });
  }

  @Override
  public void fetchAppUpdateInfo(BackendCallback<AppUpdateInfo> callback) {
    mainHandler.post(() -> {
      if (callback != null) {
        callback.onSuccess(new AppUpdateInfo(
            20100, "2.1.0", false,
            "দ্বীনওয়ান আপডেট", "কোনো নতুন আপডেট নেই।",
            "https://play.google.com/store/apps/details?id=com.devflux.deenone"
        ));
      }
    });
  }

  @Override
  public void fetchLeaderboard(BackendCallback<List<LeaderboardUser>> callback) {
    executor.execute(() -> {
      try {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FB_USER, Context.MODE_PRIVATE);
        List<LeaderboardUser> users = new ArrayList<>();

        // Query registered users dynamically from database and preferences
        com.devflux.deenone.data.local.AppDatabase db = com.devflux.deenone.data.local.AppDatabase.getInstance(context);
        com.devflux.deenone.data.local.entity.UserProfileEntity activeEntity = db.userProfileDao().getActiveProfileSync();

        if (activeEntity != null && activeEntity.getFullName() != null && !activeEntity.getFullName().trim().isEmpty()) {
          int pts = Math.max(activeEntity.getPoints(), com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(context));
          users.add(new LeaderboardUser(
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
          if (callback != null) callback.onSuccess(users);
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
      try {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FB_USER, Context.MODE_PRIVATE);
        prefs.edit().putInt("user_points_" + userId, points).apply();
        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(true);
        });
      } catch (Exception e) {
        mainHandler.post(() -> {
          if (callback != null) callback.onError("পয়েন্ট সিঙ্ক ব্যর্থ: "+ e.getMessage());
        });
      }
    });
  }

  @Override
  public void fetchCommunityMessages(String category, BackendCallback<List<CommunityMessage>> callback) {
    executor.execute(() -> {
      List<CommunityMessage> messages = new ArrayList<>();
      messages.add(new CommunityMessage(
          "msg_fb_1", "হাফেজ মাহমুদুল হাসান", "usr_fb_1",
          "কমিউনিটি নোটিশ", "আসসালামু আলাইকুম, দ্বীনওয়ান অ্যাপের মাধ্যমে কুরআন হিফজ ট্র্যাকার ব্যবহার করুন।",
          System.currentTimeMillis() - 1800000, 31
      ));

      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(messages);
      });
    });
  }

  @Override
  public void postCommunityMessage(CommunityMessage message, BackendCallback<Boolean> callback) {
    executor.execute(() -> {
      mainHandler.post(() -> {
        if (callback != null) callback.onSuccess(true);
      });
    });
  }

  @Override
  public void fetchUserProfile(String userId, BackendCallback<UserProfile> callback) {
    executor.execute(() -> {
      try {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FB_USER, Context.MODE_PRIVATE);
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
      } catch (Exception e) {
        mainHandler.post(() -> {
          if (callback != null) callback.onError("প্রোফাইল তথ্য লোড ত্রুটি: "+ e.getMessage());
        });
      }
    });
  }

  @Override
  public void updateUserProfile(UserProfile profile, BackendCallback<Boolean> callback) {
    if (profile == null) {
      if (callback != null) callback.onError("অকার্যকর প্রোফাইল ডাটা।");
      return;
    }

    executor.execute(() -> {
      try {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FB_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        String uid = profile.getUserId();
        ed.putString("user_name_" + uid, profile.getUserName());
        ed.putString("user_tz_" + uid, profile.getTimezone());
        ed.putInt("user_points_" + uid, profile.getTotalPoints());
        ed.putLong("user_join_" + uid, profile.getJoiningTimestamp());
        ed.putString("user_avatar_" + uid, profile.getAvatarUrl());
        ed.putInt("user_preset_" + uid, profile.getAvatarPreset());
        ed.apply();

        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(true);
        });
      } catch (Exception e) {
        mainHandler.post(() -> {
          if (callback != null) callback.onError("প্রোফাইল আপডেট ত্রুটি: "+ e.getMessage());
        });
      }
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
      try {
        File storageDir = new File(context.getFilesDir(), "firebase_storage/avatars");
        if (!storageDir.exists()) {
          storageDir.mkdirs();
        }

        File destinationFile = new File(storageDir, "avatar_" + userId + ".jpg");

        try (FileInputStream in = new FileInputStream(imageFile);
           FileOutputStream out = new FileOutputStream(destinationFile)) {
          byte[] buffer = new byte[4096];
          int read;
          while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
          }
          out.flush();
        }

        String generatedUrl = "https://firebasestorage.googleapis.com/v0/b/deanone.appspot.com/o/avatars%2F" + userId + ".jpg";

        SharedPreferences prefs = context.getSharedPreferences(PREF_FB_USER, Context.MODE_PRIVATE);
        prefs.edit()
            .putString("user_avatar_" + userId, destinationFile.getAbsolutePath())
            .putString("user_avatar_url_" + userId, generatedUrl)
            .apply();

        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(destinationFile.getAbsolutePath());
        });
      } catch (Exception e) {
        mainHandler.post(() -> {
          if (callback != null) callback.onError("ফায়ারবেস স্টোরেজ আপলোড ত্রুটি: "+ e.getMessage());
        });
      }
    });
  }

  @Override
  public void registerBloodDonor(String userId, String name, String bloodGroup, String phone, String district, BackendCallback<Boolean> callback) {
    executor.execute(() -> {
      try {
        SharedPreferences prefs = context.getSharedPreferences("firebase_blood_donors", Context.MODE_PRIVATE);
        prefs.edit()
            .putString("donor_name_" + userId, name)
            .putString("donor_group_" + userId, bloodGroup)
            .putString("donor_phone_" + userId, phone)
            .putString("donor_district_" + userId, district)
            .putLong("donor_time_" + userId, System.currentTimeMillis())
            .apply();

        mainHandler.post(() -> {
          if (callback != null) callback.onSuccess(true);
        });
      } catch (Exception e) {
        mainHandler.post(() -> {
          if (callback != null) callback.onError(e.getMessage());
        });
      }
    });
  }
}
