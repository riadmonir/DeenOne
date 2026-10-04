package com.devflux.deenone.features.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendRepository;
import com.devflux.deenone.core.backend.IBackendService;
import com.devflux.deenone.core.backend.model.UserProfile;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.quiz.QuizManager;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.data.repository.NotificationRepository;
import com.devflux.deenone.databinding.PageFullProfileBinding;
import com.devflux.deenone.features.community.CommunityFeedDialog;
import com.devflux.deenone.features.community.data.CommunityRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;

import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.auth.AuthDialogManager;
import com.devflux.deenone.core.backend.BackendConfigManager;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FullProfileDialog {

  private static final String PREF_PROFILE = "user_profile_prefs";
  private static final String KEY_USER_ID = "user_uid";
  private static final String KEY_USER_NAME = "user_full_name";
  private static final String KEY_USER_LOCATION = "user_location";
  private static final String KEY_USER_JOINED_MS = "user_joined_timestamp";
  private static final String KEY_USER_AVATAR_URI = "user_avatar_uri";
  private static final String KEY_USER_AVATAR_PRESET = "user_avatar_preset";

  private static final String PREF_BLOOD = "blood_donation_prefs";
  private static final String KEY_DONOR_REGISTERED = "is_registered_donor";
  private static final String KEY_DONOR_NAME = "donor_name";
  private static final String KEY_DONOR_GROUP = "donor_blood_group";
  private static final String KEY_DONOR_PHONE = "donor_phone";
  private static final String KEY_DONOR_DISTRICT = "donor_district";
  private static final String KEY_DONOR_LAST_DATE = "donor_last_donation_date";

  private static PageFullProfileBinding activeBinding;
  private static Activity activeActivity;
  private static final android.util.LruCache<String, android.graphics.Bitmap> avatarMemoryCache = new android.util.LruCache<>(4);
  private static long lastRemoteSyncTimeMs = 0;

  public static void clearAvatarCache() {
    avatarMemoryCache.evictAll();
  }

  public static void show(Activity activity) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    PageFullProfileBinding binding = PageFullProfileBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(activity) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    binding.ivProfileThemeIcon.setImageResource(isDark ? R.drawable.ic_sun : R.drawable.ic_moon);

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseFullProfile);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnProfileThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnProfileNotifications);

    binding.btnCloseFullProfile.setOnClickListener(v -> dialog.dismiss());
    binding.btnProfileThemeToggle.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).toggleAppTheme();
        dialog.dismiss();
      }
    });
    binding.btnProfileNotifications.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).showNotificationHistorySheet();
      }
    });

    activeBinding = binding;
    activeActivity = activity;

    SharedPreferences profilePrefs = activity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
    SharedPreferences bloodPrefs = activity.getSharedPreferences(PREF_BLOOD, Context.MODE_PRIVATE);
    SharedPreferences salahPrefs = activity.getSharedPreferences("salah_tracker_prefs", Context.MODE_PRIVATE);
    SharedPreferences amalPrefs = activity.getSharedPreferences("amal_tracker_prefs", Context.MODE_PRIVATE);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);

    // Dynamic Dual-Language Localization of all Section Labels, Units and Headings
    if (binding.tvProfileSalahUnit != null) binding.tvProfileSalahUnit.setText(isBn ? "ওয়াক্ত" : "Times");
    if (binding.tvProfileSalahLabel != null) binding.tvProfileSalahLabel.setText(isBn ? "ওয়াক্ত নামাজ" : "Daily Salah");
    if (binding.tvProfileAmalUnit != null) binding.tvProfileAmalUnit.setText(isBn ? "বার" : "Times");
    if (binding.tvProfileAmalLabel != null) binding.tvProfileAmalLabel.setText(isBn ? "নেক আমল" : "Good Deeds");
    if (binding.tvProfileQuizUnit != null) binding.tvProfileQuizUnit.setText(isBn ? "টি" : "Quizzes");
    if (binding.tvProfileQuizLabel != null) binding.tvProfileQuizLabel.setText(isBn ? "অংশগ্রহণকৃত কুইজ" : "Played Quizzes");
    if (binding.tvProfilePointsUnit != null) binding.tvProfilePointsUnit.setText(isBn ? "\u09aa\u09df\u09c7\u09a8\u09cd\u099f" : "Points");
    if (binding.tvProfilePointsLabel != null) binding.tvProfilePointsLabel.setText(isBn ? "\u09ae\u09cb\u099f \u09aa\u09df\u09c7\u09a8\u09cd\u099f" : "Total Points");

    // Social Activity Section (সামাজিক সক্রিয়তা)
    if (binding.tvSocialActivityTitle != null) binding.tvSocialActivityTitle.setText(isBn ? "সামাজিক সক্রিয়তা" : "Social Activity");
    if (binding.tvSocialPostLabel != null) binding.tvSocialPostLabel.setText(isBn ? "পোস্ট" : "Posts");
    if (binding.tvSocialCommentLabel != null) binding.tvSocialCommentLabel.setText(isBn ? "কমেন্ট" : "Comments");
    if (binding.tvSocialLikeLabel != null) binding.tvSocialLikeLabel.setText(isBn ? "লাইক" : "Likes");

    // Monthly Streak & Calendar Section (ধারাবাহিকতা / Consistency)
    if (binding.tvMonthlyStreakTitle != null) binding.tvMonthlyStreakTitle.setText(isBn ? "ধারাবাহিকতা ও ক্যালেন্ডার" : "Consistency & Calendar");
    if (binding.tvMonthlyStreakSubtitle != null) binding.tvMonthlyStreakSubtitle.setText(isBn ? "মাসিক পয়েন্ট অর্জনের ইতিহাস ও স্ট্রিক..." : "Monthly points history and streak...");
    if (binding.tvDaySat != null) binding.tvDaySat.setText(isBn ? "শনি" : "Sat");
    if (binding.tvDaySun != null) binding.tvDaySun.setText(isBn ? "রবি" : "Sun");
    if (binding.tvDayMon != null) binding.tvDayMon.setText(isBn ? "সোম" : "Mon");
    if (binding.tvDayTue != null) binding.tvDayTue.setText(isBn ? "মঙ্গল" : "Tue");
    if (binding.tvDayWed != null) binding.tvDayWed.setText(isBn ? "বুধ" : "Wed");
    if (binding.tvDayThu != null) binding.tvDayThu.setText(isBn ? "বৃহঃ" : "Thu");
    if (binding.tvDayFri != null) binding.tvDayFri.setText(isBn ? "শুক্র" : "Fri");

    // Spiritual Journey Section (আধ্যাত্মিক যাত্রা)
    if (binding.tvSpiritualJourneyTitle != null) binding.tvSpiritualJourneyTitle.setText(isBn ? "আধ্যাত্মিক যাত্রা" : "Spiritual Journey");
    if (binding.tvSpiritualJourneySubtitle != null) {
      binding.tvSpiritualJourneySubtitle.setText(isBn
          ? "নিয়মিত সৎ কাজ করুন, নেক আমল ট্র্যাক করুন এবং ঈমানী শক্তি বৃদ্ধি করুন।"
          : "Perform good deeds regularly, track good deeds and strengthen your faith.");
    }

    AppDatabase db = AppDatabase.getInstance(activity);
    String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());

    boolean isLoggedIn = com.devflux.deenone.core.auth.AuthManager.isLoggedIn(activity);
    com.devflux.deenone.core.auth.AuthManager.UserSession session = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(activity);

    // 1. User Identity & Persistence
    String userId = (isLoggedIn && session != null && !session.userId.isEmpty() && !"usr_guest".equals(session.userId))
        ? session.userId
        : "usr_guest";

    // 2. Load User Profile Header from Actual Room SQLite Database & DAO (with LRU Cache)
    loadUserAvatar(activity, profilePrefs, binding);

    String defaultGuestName = isBn ? "দ্বীনওয়ান ব্যবহারকারী" : "DeenOne User";
    String initialFullName = (isLoggedIn && session != null && session.name != null && !session.name.trim().isEmpty() && !session.name.equals("usr_main") && !session.name.equals("usr_guest"))
        ? session.name
        : defaultGuestName;

    String initialDistrict = com.devflux.deenone.core.location.LocationProvider.formatCityNameOnly(
        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(activity).locationName, isBn);
    if (initialDistrict == null || initialDistrict.isEmpty()) {
      initialDistrict = isBn ? "ঢাকা" : "Dhaka";
    }

    long initialJoinDate = isLoggedIn ? profilePrefs.getLong(KEY_USER_JOINED_MS, 0) : 0;
    if (initialJoinDate <= 0) {
      initialJoinDate = getAppInstallTime(activity);
    }

    binding.tvProfileFullName.setText(initialFullName);
    binding.tvProfileLocation.setText(initialDistrict);
    binding.tvProfileJoinedDate.setText(isBn ? ("যোগদানের তারিখ: " + formatJoinDate(initialJoinDate, true)) : ("Joined: " + formatJoinDate(initialJoinDate, false)));
    binding.badgeVerified.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);

    if (!isLoggedIn) {
      binding.tvProfileSalahCount.setText(isBn ? "০" : "0");
      binding.tvProfileAmalCount.setText(isBn ? "০" : "0");
      binding.tvProfileQuizCount.setText(isBn ? "০" : "0");
      binding.tvProfilePoints.setText(isBn ? "০" : "0");
      binding.tvProfileTotalPoints.setText(isBn ? "০" : "0");
    }

    if (activity instanceof LifecycleOwner) {
      LifecycleOwner owner = (LifecycleOwner) activity;

      // Reactive Profile Observer (Active Logged-in User)
      db.userProfileDao().getActiveProfile().observe(owner, profileEntity -> {
        boolean loggedIn = com.devflux.deenone.core.auth.AuthManager.isLoggedIn(activity);
        if (!loggedIn) {
          binding.tvProfileFullName.setText(isBn ? "দ্বীনওয়ান ব্যবহারকারী" : "DeenOne User");
          binding.tvProfilePoints.setText(isBn ? "০" : "0");
          binding.tvProfileTotalPoints.setText(isBn ? "০" : "0");
          binding.badgeVerified.setVisibility(View.GONE);
          return;
        }
        if (profileEntity != null) {
          com.devflux.deenone.core.auth.AuthManager.UserSession currentSession = com.devflux.deenone.core.auth.AuthManager.getCurrentSession(activity);

          String resolvedName;
          if (currentSession != null && currentSession.name != null && !currentSession.name.trim().isEmpty()) {
            resolvedName = currentSession.name;
          } else if (profileEntity.getFullName() != null && !profileEntity.getFullName().trim().isEmpty() && !profileEntity.getFullName().equals("usr_main") && !profileEntity.getFullName().equals("usr_guest")) {
            resolvedName = profileEntity.getFullName();
          } else {
            resolvedName = isBn ? "দ্বীনওয়ান ব্যবহারকারী" : "DeenOne User";
          }

          String district = com.devflux.deenone.core.location.LocationProvider.formatCityNameOnly(
              com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(activity).locationName, isBn);
          if (district == null || district.isEmpty()) {
            district = isBn ? "ঢাকা" : "Dhaka";
          }

          long joinTs = profileEntity.getJoiningDateTimestamp() > 0
              ? profileEntity.getJoiningDateTimestamp()
              : (profilePrefs.getLong(KEY_USER_JOINED_MS, 0) > 0 ? profilePrefs.getLong(KEY_USER_JOINED_MS, 0) : getAppInstallTime(activity));

          binding.tvProfileFullName.setText(resolvedName);
          binding.tvProfileLocation.setText(district);
          binding.tvProfileJoinedDate.setText(isBn ? ("যোগদানের তারিখ: " + formatJoinDate(joinTs, true)) : ("Joined: " + formatJoinDate(joinTs, false)));
          binding.badgeVerified.setVisibility(View.VISIBLE);
          int curPts = profileEntity.getPoints();
          int ledgerPts = com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.getLifetimePoints(activity);
          int sessPts = (currentSession != null) ? currentSession.points : 0;
          int displayPoints = Math.max(curPts, Math.max(ledgerPts, sessPts));
          binding.tvProfilePoints.setText(isBn ? BengaliNumberUtil.toBengali(displayPoints) : String.valueOf(displayPoints));
          binding.tvProfileTotalPoints.setText(isBn ? BengaliNumberUtil.toBengali(displayPoints) : String.valueOf(displayPoints));
        }
      });

      // Reactive 4-Grid Statistics Observers:
      // 1. ওয়াক্ত নামাজ (User's Lifetime Total Prayers Prayed: Only 5 Fard Waqts on-time, excluding Qaza and Nafl)
      db.prayerLogDao().getTotalPrayedCount().observe(owner, prayedCount -> {
        if (!com.devflux.deenone.core.auth.AuthManager.isLoggedIn(activity)) {
          binding.tvProfileSalahCount.setText(isBn ? "০" : "0");
          return;
        }
        int count = prayedCount != null ? prayedCount : 0;
        binding.tvProfileSalahCount.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));
      });

      // 2. নেক আমল (User's Lifetime Total Completed Daily Amals)
      db.dailyAmalDao().getTotalCompletedCount().observe(owner, completedAmals -> {
        if (!com.devflux.deenone.core.auth.AuthManager.isLoggedIn(activity)) {
          binding.tvProfileAmalCount.setText(isBn ? "০" : "0");
          return;
        }
        int count = completedAmals != null ? completedAmals : 0;
        binding.tvProfileAmalCount.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));
      });

      // 3. কুইজ বিজয়ী / সঠিক কুইজ (Actual Quizzes Won / Correctly Answered)
      db.quizDao().getTotalQuizzesPlayedCount().observe(owner, quizCount -> {
        if (!com.devflux.deenone.core.auth.AuthManager.isLoggedIn(activity)) {
          binding.tvProfileQuizCount.setText(isBn ? "০" : "0");
          return;
        }
        int count = quizCount != null ? quizCount : 0;
        binding.tvProfileQuizCount.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));
      });
    }

    int completedPrayers = 0;
    int completedAmals = 0;
    int playedQuizzes = 0;
    int totalEarnedPoints = 0;
    int quizPoints = 0;
    int ledgerPoints = 0;
    int gamificationXP = 0;

    if (isLoggedIn) {
      // Authoritative Synchronization of all 7 Profile Statistics directly from source data
      ProfileStatsAggregator.refreshAndSyncAllStatistics(activity, userId, null);

      // Synchronize with Backend PHP MySQL Database (Debounced to prevent duplicate requests)
      long now = System.currentTimeMillis();
      if (now - lastRemoteSyncTimeMs > 60000) {
        lastRemoteSyncTimeMs = now;
        BackendRepository.getInstance(activity).fetchUserProfile(userId, new IBackendService.BackendCallback<UserProfile>() {
          @Override
          public void onSuccess(UserProfile remoteProfile) {
            if (remoteProfile != null && activity != null && !activity.isFinishing()) {
              AppDatabase.databaseWriteExecutor.execute(() -> {
                UserProfileEntity entity = db.userProfileDao().getUserProfileSync(userId);
                if (entity == null) {
                  entity = new UserProfileEntity(
                      remoteProfile.getUserId(),
                      remoteProfile.getUserName(),
                      remoteProfile.getEmail(),
                      remoteProfile.getPhone(),
                      remoteProfile.getTimezone(),
                      remoteProfile.getJoiningTimestamp(),
                      remoteProfile.getTotalPoints(),
                      1, 0, true,
                      remoteProfile.getAvatarUrl(),
                      remoteProfile.getAvatarPreset(),
                      0, 0, 0,
                      System.currentTimeMillis()
                  );
                } else {
                  entity.setFullName(remoteProfile.getUserName());
                  entity.setTimezone(remoteProfile.getTimezone());
                  entity.setPoints(remoteProfile.getTotalPoints());
                  entity.setJoiningDateTimestamp(remoteProfile.getJoiningTimestamp());
                  if (remoteProfile.getAvatarUrl() != null && !remoteProfile.getAvatarUrl().isEmpty() && !"avatar_1".equals(remoteProfile.getAvatarUrl())) {
                    entity.setAvatarUri(remoteProfile.getAvatarUrl());
                  }
                }
                db.userProfileDao().insertOrUpdateProfile(entity);
              });

              String remoteAvatar = remoteProfile.getAvatarUrl();
              if (remoteAvatar != null && !remoteAvatar.isEmpty() && !"avatar_1".equals(remoteAvatar)) {
                profilePrefs.edit()
                    .putString(KEY_USER_AVATAR_URI, remoteAvatar)
                    .putString(AuthManager.KEY_AVATAR, remoteAvatar)
                    .apply();
                activity.runOnUiThread(() -> loadUserAvatar(activity, profilePrefs, binding));
              }
            }
          }

          @Override
          public void onError(String errorMessage) {}
        });
      }

      // 3. Dynamic Calculation of Real Live Points from DAOs
      completedPrayers = salahPrefs.getInt("prayers_completed_today", 0);
      completedAmals = amalPrefs.getInt("completed_amals_count", 0);
      playedQuizzes = QuizManager.getInstance().getDailyQuizzesPlayedCount(activity);

      int sessionPoints = (session != null) ? session.points : 0;
      int savedProfPoints = profilePrefs.getInt("profile_points", 0);
      ledgerPoints = com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.getLifetimePoints(activity);
      gamificationXP = GamificationManager.getTotalXP(activity);
      quizPoints = QuizManager.getInstance().getDeenPoints(activity);

      totalEarnedPoints = Math.max(sessionPoints, Math.max(savedProfPoints, Math.max(ledgerPoints, Math.max(gamificationXP, quizPoints))));

      binding.tvProfilePoints.setText(isBn ? BengaliNumberUtil.toBengali(totalEarnedPoints) : String.valueOf(totalEarnedPoints));
      binding.tvProfileTotalPoints.setText(isBn ? BengaliNumberUtil.toBengali(totalEarnedPoints) : String.valueOf(totalEarnedPoints));

      if (totalEarnedPoints > 0) {
        com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.syncInitialPoints(activity, totalEarnedPoints);
        GamificationManager.setTotalXP(activity, totalEarnedPoints);
        profilePrefs.edit().putInt("profile_points", totalEarnedPoints).apply();
        final int fTotalPoints = totalEarnedPoints;
        AppDatabase.databaseWriteExecutor.execute(() -> {
          db.userProfileDao().updatePoints(userId, fTotalPoints, System.currentTimeMillis());
        });
        BackendRepository.getInstance(activity).submitUserPoints(userId, binding.tvProfileFullName.getText().toString(), totalEarnedPoints, null);
      }
    } else {
      binding.tvProfilePoints.setText(isBn ? "০" : "0");
      binding.tvProfileTotalPoints.setText(isBn ? "০" : "0");
      binding.tvProfileSalahCount.setText(isBn ? "০" : "0");
      binding.tvProfileAmalCount.setText(isBn ? "০" : "0");
      binding.tvProfileQuizCount.setText(isBn ? "০" : "0");
    }

    // 4. Top Bar Controls
    binding.btnCloseFullProfile.setOnClickListener(v -> dialog.dismiss());

    binding.btnProfileThemeToggle.setOnClickListener(v -> {
      int currentMode = ThemeManager.getSavedThemeMode(activity);
      int nextMode = (currentMode == ThemeManager.THEME_DARK) ? ThemeManager.THEME_LIGHT : ThemeManager.THEME_DARK;
      ThemeManager.setThemeMode(activity, nextMode);
      String themeMsg = (nextMode == ThemeManager.THEME_DARK)
          ? (isBn ? "ডার্ক মোড সক্রিয় করা হয়েছে" : "Dark mode activated")
          : (isBn ? "লাইট মোড সক্রিয় করা হয়েছে" : "Light mode activated");
      Toast.makeText(activity, themeMsg, Toast.LENGTH_SHORT).show();
    });

    try {
      NotificationRepository notifRepo = new NotificationRepository(activity);
      notifRepo.getUnreadCount().observeForever(unread -> {
        int count = unread != null ? unread : 0;
        if (binding.viewProfileNotifBadge != null) {
          binding.viewProfileNotifBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        }
      });
    } catch (Exception ignored) {}

    binding.btnProfileNotifications.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).showNotificationHistorySheet();
      } else {
        Toast.makeText(activity, "নোটিফিকেশন ইনবক্স লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
      }
    });

    // 5. Hero Card Interactive Actions
    final int finalEarnedPoints = totalEarnedPoints;
    final int finalPrayers = completedPrayers;
    final int finalAmals = completedAmals;
    final int finalQuizzes = playedQuizzes;

    final int finalQuizPoints = quizPoints;

    View.OnClickListener pointsClickListener = v -> {
      int level = GamificationManager.getLevel(activity);
      int streak = GamificationManager.getStreakDays(activity);
      String title = isBn ? "দ্বীনি অর্জন ও পয়েন্টের বিবরণ" : "Spiritual Achievements & Points Summary";
      String message = isBn
          ? ("• মোট অর্জিত পয়েন্ট: " + BengaliNumberUtil.toBengali(finalEarnedPoints) + " পয়েন্ট\n" +
          "• আপনার দ্বীনি লেভেল: লেভেল " + BengaliNumberUtil.toBengali(level) + " (" + getLevelBadgeTitle(level, true) + ")\n" +
          "• চলমান সক্রিয় স্ট্রিক: " + BengaliNumberUtil.toBengali(streak) + " দিন\n\n" +
          "পয়েন্ট অর্জনের উৎসসমূহ:\n" +
          "দৈনিক সালাত (" + BengaliNumberUtil.toBengali(finalPrayers) + " ওয়াক্ত): " + BengaliNumberUtil.toBengali(finalPrayers * 30) + " পয়েন্ট\n" +
          "নেক আমল (" + BengaliNumberUtil.toBengali(finalAmals) + "টি সম্পন্ন): " + BengaliNumberUtil.toBengali(finalAmals * 25) + " পয়েন্ট\n" +
          "ইসলামিক কুইজ (" + BengaliNumberUtil.toBengali(finalQuizzes) + "টি অংশগ্রহণ): " + BengaliNumberUtil.toBengali(finalQuizPoints) + " পয়েন্ট\n" +
          "উম্মাহ কমিউনিটি সক্রিয়তা: বোনাস পয়েন্ট")
          : ("• Total Earned Points: " + finalEarnedPoints + " Points\n" +
          "• Spiritual Level: Level " + level + " (" + getLevelBadgeTitle(level, false) + ")\n" +
          "• Active Streak: " + streak + " days\n\n" +
          "Points Breakdown:\n" +
          "Daily Salah (" + finalPrayers + " times): " + (finalPrayers * 30) + " Points\n" +
          "Daily Deeds (" + finalAmals + " completed): " + (finalAmals * 25) + " Points\n" +
          "Islamic Quiz (" + finalQuizzes + " played): " + finalQuizPoints + " Points\n" +
          "Ummah Community: Bonus Points");

      new AlertDialog.Builder(activity)
          .setTitle(title)
          .setMessage(message)
          .setPositiveButton(isBn ? "মাশাআল্লাহ" : "MashaAllah", null)
          .show();
    };
    // Touch animations ONLY on interactive buttons/pills (Rule 7)
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.pillUserPoints);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnChangePhoto);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.ivProfileAvatarLarge);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.badgeVerified);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnEditUserName);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnEditLocation);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnRegisterBloodDonor);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnPrevMonth);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnNextMonth);

    binding.pillUserPoints.setOnClickListener(pointsClickListener);
    binding.cardProfileStatPoints.setOnClickListener(v -> com.devflux.deenone.features.leaderboard.LeaderboardRankPageDialog.show(activity));

    // Fully Functional Camera & Image Selection Flow
    View.OnClickListener changePhotoListener = v -> {
      if (!AuthManager.isLoggedIn(activity)) {
        AuthDialogManager.showLoginDialog(activity, newSession -> {
          boolean currentBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
          Toast.makeText(activity, (currentBn ? "স্বাগতম, " : "Welcome, ") + newSession.name, Toast.LENGTH_SHORT).show();
          dialog.dismiss();
          FullProfileDialog.show(activity);
        });
        return;
      }
      ProfileImageUploadManager.startImageSelectionFlow(activity, (newPath, circularBitmap) -> {
        loadUserAvatar(activity, profilePrefs, binding);
        if (newPath != null) {
          AuthManager.updateUserAvatar(activity, newPath);
          AppDatabase.databaseWriteExecutor.execute(() -> {
            db.userProfileDao().updateAvatar(userId, newPath, -1, System.currentTimeMillis());
          });
        }
      });
    };
    binding.btnChangePhoto.setOnClickListener(changePhotoListener);
    binding.ivProfileAvatarLarge.setOnClickListener(changePhotoListener);

    // Verified Member Status Badge
    binding.badgeVerified.setOnClickListener(v -> {
      new AlertDialog.Builder(activity)
          .setTitle(isBn ? "ভেরিফাইড মুসলিম প্রোফাইল" : "Verified Muslim Profile")
          .setMessage(isBn
              ? ("• সদস্য আইডি: #" + BengaliNumberUtil.toBengali(finalEarnedPoints) + "\n" +
              "• স্থিতি: সক্রিয় উম্মাহ সদস্য\n" +
              "• নিরাপত্তা: লোকাল অন-ডিভাইস এনক্রিপশন সক্রিয়\n" +
              "• তথ্য গোপনীয়তা: আপনার আমল ও ইবাদতের তথ্য সুরক্ষিত রয়েছে।")
              : ("• Member ID: #" + finalEarnedPoints + "\n" +
              "• Status: Active Ummah Member\n" +
              "• Security: Local On-Device Encryption Active\n" +
              "• Privacy: Your spiritual records are protected."))
          .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
          .show();
    });

    // Edit User Name Dialog (Room SQL DB Update + Preferences + Cloud Sync)
    binding.btnEditUserName.setOnClickListener(v -> {
      if (!AuthManager.isLoggedIn(activity)) {
        AuthDialogManager.showLoginDialog(activity, newSession -> {
          boolean currentBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
          Toast.makeText(activity, (currentBn ? "স্বাগতম, " : "Welcome, ") + newSession.name, Toast.LENGTH_SHORT).show();
          dialog.dismiss();
          FullProfileDialog.show(activity);
        });
        return;
      }
      EditText input = new EditText(activity);
      input.setText(binding.tvProfileFullName.getText().toString());
      input.setSelection(input.getText().length());
      input.setHint(isBn ? "আপনার পূর্ণ নাম লিখুন" : "Enter your full name");
      input.setPadding(36, 24, 36, 24);

      new AlertDialog.Builder(activity)
          .setTitle(isBn ? "আপনার নাম পরিবর্তন করুন" : "Edit Your Name")
          .setView(input)
          .setPositiveButton(isBn ? "সংরক্ষণ" : "Save", (d, w) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
              binding.tvProfileFullName.setText(newName);
              profilePrefs.edit()
                  .putString(KEY_USER_NAME, newName)
                  .putString(AuthManager.KEY_FULL_NAME, newName)
                  .apply();

              AuthManager.updateUserName(activity, newName);

              AppDatabase.databaseWriteExecutor.execute(() -> {
                db.userProfileDao().updateFullName(userId, newName, System.currentTimeMillis());
              });

              UserProfile updatedProfile = new UserProfile(
                  userId, newName, "", "", "", 0,
                  finalEarnedPoints, System.currentTimeMillis(),
                  binding.tvProfileLocation.getText().toString(), true
              );
              BackendRepository.getInstance(activity).updateUserProfile(updatedProfile, null);

              // Real-time synchronization of nickname with MySQL database
              com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(activity).updateUserNickname(newName, null);
            }
          })
          .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
          .show();
    });

    // Location / Timezone Picker (Unified Rich Location Selector)
    binding.btnEditLocation.setOnClickListener(v -> {
      com.devflux.deenone.features.location.LocationSelectorPageDialog.show(activity);
    });

    // 6. 4-Grid Activity Stats Clicks (Open Full Live Tracking Sheets)
    binding.cardProfileStatSalah.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).showSalahTrackerSheet();
      } else {
        Toast.makeText(activity, "সালাত ট্র্যাকার লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
      }
    });

    binding.cardProfileStatAmal.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).showAmalTrackerSheet();
      } else {
        Toast.makeText(activity, "আমল ট্র্যাকার লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
      }
    });

    binding.cardProfileStatQuiz.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).showQuizSheet();
      } else {
        Toast.makeText(activity, "ইসলামিক কুইজ লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
      }
    });

    // 7. Blood Donor Hub Integration
    updateBloodDonorCardState(activity, bloodPrefs, binding);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnRegisterBloodDonor);
    binding.btnRegisterBloodDonor.setOnClickListener(v -> {
      showBloodDonorRegistrationOrHelplineDialog(activity, bloodPrefs, binding);
    });

    SharedPreferences.OnSharedPreferenceChangeListener bloodListener = (sp, key) -> {
      if (KEY_DONOR_REGISTERED.equals(key) || KEY_DONOR_GROUP.equals(key)) {
        if (activeActivity != null && !activeActivity.isFinishing() && activeBinding != null) {
          activeActivity.runOnUiThread(() -> updateBloodDonorCardState(activeActivity, sp, activeBinding));
        }
      }
    };
    bloodPrefs.registerOnSharedPreferenceChangeListener(bloodListener);

    // 8. Social Activity Real Counters & Navigation (Single Source of Truth: CommunityRepository)
    CommunityRepository communityRepo = CommunityRepository.getInstance(activity);
    if (activity instanceof LifecycleOwner) {
      communityRepo.getSocialStatsLiveData().observe((LifecycleOwner) activity, stats -> {
        if (stats != null && binding != null) {
          binding.tvSocialPostCount.setText(isBn ? BengaliNumberUtil.toBengali(stats.postCount) : String.valueOf(stats.postCount));
          binding.tvSocialCommentCount.setText(isBn ? BengaliNumberUtil.toBengali(stats.commentCount) : String.valueOf(stats.commentCount));
          binding.tvSocialLikeCount.setText(isBn ? BengaliNumberUtil.toBengali(stats.likeCount) : String.valueOf(stats.likeCount));
        }
      });
    }
    communityRepo.recalculateAndNotifyStats();

    View.OnClickListener openCommunityFeedListener = v -> {
      CommunityFeedDialog.show(activity);
    };
    binding.cardSocialPost.setOnClickListener(openCommunityFeedListener);
    binding.cardSocialComment.setOnClickListener(openCommunityFeedListener);
    binding.cardSocialLike.setOnClickListener(openCommunityFeedListener);

    // 9. Interactive Monthly Streak Calendar Engine
    Calendar activeCalendar = Calendar.getInstance();
    renderMonthlyCalendar(activity, activeCalendar, binding);

    binding.btnPrevMonth.setOnClickListener(v -> {
      activeCalendar.add(Calendar.MONTH, -1);
      renderMonthlyCalendar(activity, activeCalendar, binding);
    });

    binding.btnNextMonth.setOnClickListener(v -> {
      activeCalendar.add(Calendar.MONTH, 1);
      renderMonthlyCalendar(activity, activeCalendar, binding);
    });

    // 10. Spiritual Journey Banner
    binding.cardSpiritualJourney.setOnClickListener(v -> {
      if (activity instanceof MainActivity) {
        ((MainActivity) activity).showAmalTrackerSheet();
      } else {
        Toast.makeText(activity, "আধ্যাত্মিক আমল লক্ষ্য লোড হচ্ছে...", Toast.LENGTH_SHORT).show();
      }
    });

    dialog.setOnDismissListener(d -> {
      bloodPrefs.unregisterOnSharedPreferenceChangeListener(bloodListener);
      activeBinding = null;
      activeActivity = null;
    });

    if (!activity.isFinishing() && !activity.isDestroyed()) {
      dialog.show();
    }
  }

  public static void handleImagePicked(Activity activity, Uri uri) {
    if (activity == null || uri == null) return;
    ProfileImageUploadManager.processAndUploadImage(activity, uri, (newImagePath, circularBitmap) -> {
      if (activeBinding != null && activeActivity != null) {
        SharedPreferences prefs = activeActivity.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
        loadUserAvatar(activeActivity, prefs, activeBinding);

        String userId = prefs.getString(KEY_USER_ID, "usr_main");
        if (newImagePath != null) {
          AuthManager.updateUserAvatar(activeActivity, newImagePath);
          AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase.getInstance(activeActivity).userProfileDao().updateAvatar(userId, newImagePath, -1, System.currentTimeMillis());
          });
        }
      }
    });
  }

  // =========================================================================
  // Avatar Loader & Cached Image Renderer
  // =========================================================================
  public static void loadUserAvatar(Context context, SharedPreferences prefs, PageFullProfileBinding binding) {
    if (context == null || binding == null) return;
    boolean isLoggedIn = AuthManager.isLoggedIn(context);
    if (!isLoggedIn) {
      avatarMemoryCache.evictAll();
      binding.ivProfileAvatarLarge.setImageResource(R.drawable.ic_user_circle_avatar);
      return;
    }
    String customUriStr = prefs != null ? prefs.getString(KEY_USER_AVATAR_URI, null) : null;
    if (customUriStr == null || customUriStr.isEmpty()) {
      customUriStr = prefs != null ? prefs.getString(AuthManager.KEY_AVATAR, null) : null;
    }
    int presetIndex = prefs != null ? prefs.getInt(KEY_USER_AVATAR_PRESET, -1) : -1;

    if (customUriStr != null && !customUriStr.isEmpty() && !"avatar_1".equals(customUriStr)) {
      android.graphics.Bitmap cached = avatarMemoryCache.get(customUriStr);
      if (cached != null && !cached.isRecycled()) {
        binding.ivProfileAvatarLarge.setImageBitmap(cached);
        return;
      }

      // Check if it's a local file
      File file = new File(customUriStr);
      if (file.exists() && file.length() > 0) {
        try {
          android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(file.getAbsolutePath());
          if (bitmap != null) {
            android.graphics.Bitmap circular = ProfileImageUploadManager.getCircularCroppedBitmap(bitmap, 250);
            avatarMemoryCache.put(customUriStr, circular);
            binding.ivProfileAvatarLarge.setImageBitmap(circular);
            return;
          }
        } catch (Exception ignored) {}
      }

      // Check if it is a local content URI
      if (customUriStr.startsWith("content://") || customUriStr.startsWith("file://")) {
        try {
          Uri uri = Uri.parse(customUriStr);
          InputStream is = context.getContentResolver().openInputStream(uri);
          if (is != null) {
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(is);
            is.close();
            if (bitmap != null) {
              android.graphics.Bitmap circular = ProfileImageUploadManager.getCircularCroppedBitmap(bitmap, 250);
              avatarMemoryCache.put(customUriStr, circular);
              binding.ivProfileAvatarLarge.setImageBitmap(circular);
              return;
            }
          }
        } catch (Exception ignored) {}
      }

      // If it is a remote URL (Google avatar https://... or backend uploads/avatars/...)
      final String remoteUrl;
      if (customUriStr.startsWith("http://") || customUriStr.startsWith("https://")) {
        remoteUrl = customUriStr;
      } else if (customUriStr.startsWith("uploads/")) {
        remoteUrl = BackendConfigManager.getPhpApiEndpoint(context, "").replace("/api/", "/") + customUriStr;
      } else {
        remoteUrl = null;
      }

      if (remoteUrl != null) {
        final String cacheKey = customUriStr;
        File avatarDir = new File(context.getFilesDir(), "profile_avatars");
        if (!avatarDir.exists()) avatarDir.mkdirs();
        File diskCacheFile = new File(avatarDir, "cache_" + Math.abs(remoteUrl.hashCode()) + ".img");

        // 1. Check persistent local disk cache first
        if (diskCacheFile.exists() && diskCacheFile.length() > 0) {
          try {
            android.graphics.Bitmap cachedBmp = android.graphics.BitmapFactory.decodeFile(diskCacheFile.getAbsolutePath());
            if (cachedBmp != null) {
              android.graphics.Bitmap circular = ProfileImageUploadManager.getCircularCroppedBitmap(cachedBmp, 250);
              avatarMemoryCache.put(cacheKey, circular);
              binding.ivProfileAvatarLarge.setImageBitmap(circular);
              return;
            }
          } catch (Exception ignored) {}
        }

        // 2. Fetch from remote asynchronously with proper headers & timeout
        AppDatabase.databaseWriteExecutor.execute(() -> {
          try {
            java.net.URL url = new java.net.URL(remoteUrl);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "DeenOne-App/1.0 (Android)");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.connect();
            if (conn.getResponseCode() == 200) {
              try (InputStream is = conn.getInputStream();
                   java.io.FileOutputStream fos = new java.io.FileOutputStream(diskCacheFile)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = is.read(buffer)) != -1) {
                  fos.write(buffer, 0, read);
                }
                fos.flush();
              }

              android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeFile(diskCacheFile.getAbsolutePath());
              if (bmp != null) {
                android.graphics.Bitmap circular = ProfileImageUploadManager.getCircularCroppedBitmap(bmp, 250);
                avatarMemoryCache.put(cacheKey, circular);
                if (context instanceof Activity) {
                  ((Activity) context).runOnUiThread(() -> {
                    if (binding != null && binding.ivProfileAvatarLarge != null) {
                      binding.ivProfileAvatarLarge.setImageBitmap(circular);
                    }
                  });
                }
              }
            }
          } catch (Exception ignored) {}
        });
      }
    }

    if (presetIndex >= 0) {
      int[] presets = {
          R.drawable.ic_user_circle_avatar,
          R.drawable.ic_deenone_emblem,
          R.drawable.ic_feat_mosque,
          R.drawable.ic_star,
          R.drawable.ic_sparkle,
          R.drawable.ic_hands_praying
      };
      if (presetIndex < presets.length) {
        binding.ivProfileAvatarLarge.setImageResource(presets[presetIndex]);
        return;
      }
    }

    binding.ivProfileAvatarLarge.setImageResource(R.drawable.ic_user_circle_avatar);
  }

  // =========================================================================
  // Blood Donor Registration & Emergency Services
  // =========================================================================
  private static void updateBloodDonorCardState(Context context, SharedPreferences prefs, PageFullProfileBinding binding) {
    if (context == null || binding == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    boolean isRegistered = prefs != null && prefs.getBoolean(KEY_DONOR_REGISTERED, false);
    String group = prefs != null ? prefs.getString(KEY_DONOR_GROUP, "") : "";
    if (!isRegistered || group == null || group.trim().isEmpty()) {
      AuthManager.UserSession session = AuthManager.getCurrentSession(context);
      if (session != null && session.bloodGroup != null && !session.bloodGroup.trim().isEmpty()) {
        isRegistered = true;
        if (group == null || group.trim().isEmpty()) {
          group = session.bloodGroup.trim();
        }
      }
    }
    if (group == null || group.trim().isEmpty()) {
      group = "O+";
    }

    if (isRegistered) {
      binding.tvBloodDonorTitle.setText(isBn ? "আপনি একজন রক্তদাতা হিসেবে যুক্ত আছেন" : "You are registered as a Blood Donor");
      binding.tvBloodDonorSubtitle.setText(isBn
          ? ("রক্তের গ্রুপ: " + group + " • উম্মাহ রক্তদাতা নেটওয়ার্কে আপনি একজন নিবন্ধিত জীবন রক্ষাকারী দাতা। জরুরি রক্ত সহায়তায় আপনার ভূমিকা অপরিসীম।")
          : ("Blood Group: " + group + " • You are a registered life-saving donor in the Ummah Blood Donation Network."));
      binding.btnRegisterBloodDonor.setText(isBn ? "তথ্য আপডেট / হেল্পলাইন" : "Update Info / Helpline");
    } else {
      binding.tvBloodDonorTitle.setText(isBn ? "রক্তদাতা হিসেবে যোগ দিন" : "Join as a Blood Donor");
      binding.tvBloodDonorSubtitle.setText(isBn
          ? "আপনার এক ব্যাগ রক্ত বাঁচাতে পারে একটি মুমূর্ষু মানুষের জীবন!"
          : "Your one bag of blood can save a precious human life!");
      binding.btnRegisterBloodDonor.setText(isBn ? "নিবন্ধন করুন" : "Register Now");
    }
  }

  private static void showBloodDonorRegistrationOrHelplineDialog(Activity activity, SharedPreferences prefs, PageFullProfileBinding binding) {
    com.devflux.deenone.features.blood.BloodDonationNetworkDialog.show(activity);
  }

  private static void dialPhone(Activity activity, String number) {
    try {
      Intent intent = new Intent(Intent.ACTION_DIAL);
      intent.setData(Uri.parse("tel:" + number));
      activity.startActivity(intent);
    } catch (Exception e) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
      Toast.makeText(activity, (isBn ? "ডায়াল করা যায়নি: " : "Could not dial: ") + number, Toast.LENGTH_SHORT).show();
    }
  }

  // =========================================================================
  // Monthly Streak & Points Interactive Calendar Engine (100% Dynamic & DB Backed)
  // =========================================================================
  private static void renderMonthlyCalendar(Activity activity, Calendar cal, PageFullProfileBinding binding) {
    if (binding.layoutCalendarGrid == null) return;
    binding.layoutCalendarGrid.removeAllViews();

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);

    int month = cal.get(Calendar.MONTH);
    int year = cal.get(Calendar.YEAR);

    String[] bnMonths = {
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };
    String[] enMonths = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    binding.tvCalendarMonthYear.setText(isBn ? bnMonths[month] : enMonths[month]);
    binding.tvCalendarYear.setText(isBn ? BengaliNumberUtil.toBengali(year) : String.valueOf(year));

    int streak = GamificationManager.getStreakDays(activity);
    binding.tvCalendarStatusPill.setText(isBn
        ? (bnMonths[month] + " মাসের ধারাবাহিক আমল সক্রিয় (চলমান স্ট্রিক: " + BengaliNumberUtil.toBengali(streak) + " দিন)")
        : (enMonths[month] + " Consistent Deeds Active (Current Streak: " + streak + " days)"));

    Calendar monthCal = (Calendar) cal.clone();
    monthCal.set(Calendar.DAY_OF_MONTH, 1);
    int daysInMonth = monthCal.getActualMaximum(Calendar.DAY_OF_MONTH);

    int dayOfWeekJava = monthCal.get(Calendar.DAY_OF_WEEK);
    int firstDayOffset = (dayOfWeekJava == Calendar.SATURDAY) ? 0 : dayOfWeekJava;

    Calendar todayCal = Calendar.getInstance();
    boolean isCurrentMonthAndYear = (month == todayCal.get(Calendar.MONTH) && year == todayCal.get(Calendar.YEAR));
    int currentDayNumber = todayCal.get(Calendar.DAY_OF_MONTH);

    AppDatabase.databaseWriteExecutor.execute(() -> {
      AppDatabase db = AppDatabase.getInstance(activity);
      String monthPrefix = String.format(Locale.US, "%04d-%02d", year, month + 1);

      java.util.Map<Integer, Integer> prayerCountMap = new java.util.HashMap<>();
      java.util.Map<Integer, Integer> amalCountMap = new java.util.HashMap<>();

      for (int day = 1; day <= daysInMonth; day++) {
        String dayIso = String.format(Locale.US, "%s-%02d", monthPrefix, day);
        int pCount = db.prayerLogDao().getPrayedCountForDateSync(dayIso);
        int aCount = db.dailyAmalDao().getCompletedCountByDateSync(dayIso);

        if (isCurrentMonthAndYear && day == currentDayNumber) {
          SharedPreferences salahPrefs = activity.getSharedPreferences("salah_tracker_prefs", Context.MODE_PRIVATE);
          SharedPreferences amalPrefs = activity.getSharedPreferences("amal_tracker_prefs", Context.MODE_PRIVATE);
          if (pCount == 0) pCount = salahPrefs.getInt("prayers_completed_today", 0);
          if (aCount == 0) aCount = amalPrefs.getInt("completed_amals_count", 0);
        }

        prayerCountMap.put(day, pCount);
        amalCountMap.put(day, aCount);
      }

      activity.runOnUiThread(() -> {
        if (binding.layoutCalendarGrid == null) return;
        binding.layoutCalendarGrid.removeAllViews();

        int totalCells = firstDayOffset + daysInMonth;
        int totalRows = (int) Math.ceil(totalCells / 7.0);
        int dayCounter = 1;

        for (int r = 0; r < totalRows; r++) {
          LinearLayout rowLayout = new LinearLayout(activity);
          rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT
          ));
          rowLayout.setOrientation(LinearLayout.HORIZONTAL);
          rowLayout.setGravity(Gravity.CENTER_VERTICAL);
          rowLayout.setPadding(0, 6, 0, 6);

          for (int c = 0; c < 7; c++) {
            int cellIndex = (r * 7) + c;
            FrameLayout cellFrame = new FrameLayout(activity);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            cellFrame.setLayoutParams(lp);

            if (cellIndex >= firstDayOffset && dayCounter <= daysInMonth) {
              final int thisDay = dayCounter;
              int pCount = prayerCountMap.containsKey(thisDay) ? prayerCountMap.get(thisDay) : 0;
              int aCount = amalCountMap.containsKey(thisDay) ? amalCountMap.get(thisDay) : 0;

              TextView tvDay = new TextView(activity);
              int sizePx = dpToPx(activity, 34);
              FrameLayout.LayoutParams tvLp = new FrameLayout.LayoutParams(sizePx, sizePx, Gravity.CENTER);
              tvDay.setLayoutParams(tvLp);
              tvDay.setGravity(Gravity.CENTER);
              tvDay.setText(isBn ? BengaliNumberUtil.toBengali(thisDay) : String.valueOf(thisDay));
              tvDay.setTextSize(13);
              tvDay.setTypeface(null, Typeface.BOLD);

              boolean isFuture = (year > todayCal.get(Calendar.YEAR)) ||
                  (year == todayCal.get(Calendar.YEAR) && month > todayCal.get(Calendar.MONTH)) ||
                  (isCurrentMonthAndYear && thisDay > currentDayNumber);

              boolean isToday = isCurrentMonthAndYear && (thisDay == currentDayNumber);

              if (isFuture) {
                // Future day: Unlit neutral
                tvDay.setTextColor(Color.parseColor("#529982"));
              } else {
                // Past or Today: 3-Color Coding Engine (Green, Yellow, Red)
                if (pCount >= 5 || (pCount >= 4 && aCount >= 3) || (pCount + aCount >= 6)) {
                  // GREEN: পূর্ণাঙ্গ আমল ও ৫ ওয়াক্ত সালাত সম্পন্ন
                  tvDay.setBackgroundResource(R.drawable.circle_progress_bg);
                  tvDay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#14532D")));
                  tvDay.setTextColor(Color.parseColor("#34D399"));
                } else if (pCount > 0 || aCount > 0) {
                  // YELLOW: আংশিক আমল / শুরু করা দিন (১-৪ ওয়াক্ত নামাজ বা আমল)
                  tvDay.setBackgroundResource(R.drawable.circle_progress_bg);
                  tvDay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#451A03")));
                  tvDay.setTextColor(Color.parseColor("#FBBF24"));
                } else {
                  if (isToday) {
                    // Today with 0 activities yet
                    tvDay.setBackgroundResource(R.drawable.circle_progress_bg);
                    tvDay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#1F2937")));
                    tvDay.setTextColor(Color.parseColor("#9CA3AF"));
                  } else {
                    // RED: কোনো ডেটা নেই / সম্পূর্ণ মিসড দিন
                    tvDay.setBackgroundResource(R.drawable.circle_progress_bg);
                    tvDay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#3B1114")));
                    tvDay.setTextColor(Color.parseColor("#F87171"));
                  }
                }
              }

              cellFrame.setClickable(true);
              cellFrame.setFocusable(true);
              cellFrame.setOnClickListener(v -> {
                showDaySummaryDialog(activity, thisDay, month, year, isBn ? bnMonths[month] : enMonths[month], isToday, pCount, aCount, isBn);
              });

              cellFrame.addView(tvDay);
              dayCounter++;
            } else {
              View spacer = new View(activity);
              cellFrame.addView(spacer);
            }

            rowLayout.addView(cellFrame);
          }

          binding.layoutCalendarGrid.addView(rowLayout);
        }
      });
    });
  }

  private static void showDaySummaryDialog(Activity activity, int day, int monthIndex, int year, String monthName, boolean isToday, int pCount, int aCount, boolean isBn) {
    String dateStr = String.format(Locale.US, "%04d-%02d-%02d", year, monthIndex + 1, day);
    String dateTitle = isBn
        ? (BengaliNumberUtil.toBengali(day) + " " + monthName + ", " + BengaliNumberUtil.toBengali(year))
        : (day + " " + monthName + ", " + year);

    AppDatabase.databaseWriteExecutor.execute(() -> {
      AppDatabase db = AppDatabase.getInstance(activity);
      int prayersCount = pCount;
      int completedAmalsCount = aCount;

      List<DailyAmalEntity> amalsList = db.dailyAmalDao().getAmalsByDateSync(dateStr);
      StringBuilder amalsSummary = new StringBuilder();
      if (amalsList != null && !amalsList.isEmpty()) {
        for (DailyAmalEntity a : amalsList) {
          if (a.isCompleted()) {
            if (amalsSummary.length() > 0) amalsSummary.append(", ");
            amalsSummary.append(a.getTitle());
          }
        }
      }

      String statusBadge;
      String statusAdvice;
      if (prayersCount >= 5 || (prayersCount >= 4 && completedAmalsCount >= 3) || (prayersCount + completedAmalsCount >= 6)) {
        statusBadge = isBn ? "পূর্ণাঙ্গ আমল ও সালাত সম্পন্ন" : "Full Prayers & Deeds Completed";
        statusAdvice = isBn ? "মাশাআল্লাহ! আপনি এই দিনে পূর্ণ একাগ্রতায় সকল ইবাদত পালন করেছেন।" : "MashaAllah! You completed all prayers with devotion on this day.";
      } else if (prayersCount > 0 || completedAmalsCount > 0) {
        statusBadge = isBn ? "আংশিক আমল সম্পন্ন" : "Partial Deeds Completed";
        statusAdvice = isBn ? "আলহামদুলিল্লাহ! দ্বীনি আমল চালু রয়েছে, পূর্ণাঙ্গ ৫ ওয়াক্ত নামাজে যত্নবান হোন।" : "Alhamdulillah! Keep continuing and strive for full 5 daily prayers.";
      } else {
        statusBadge = isBn ? "কোনো আমল রেকর্ড করা হয়নি" : "No Activity Recorded";
        statusAdvice = isBn ? "এই দিনে কোনো সালাত বা নেক আমল সম্পন্ন করা হয়নি। নিয়মিত আমল বজায় রাখুন।" : "No salah or deeds were recorded on this day. Maintain consistency in worship.";
      }

      int earnedPoints = (prayersCount * 30) + (completedAmalsCount * 25);
      int finalPrayers = prayersCount;
      int finalAmals = completedAmalsCount;
      String defaultSummary = isBn ? (completedAmalsCount > 0 ? "দৈনিক নেক আমল" : "কোনো আমল নেই") : (completedAmalsCount > 0 ? "Daily Good Deeds" : "No Deeds");
      String finalSummary = amalsSummary.length() > 0 ? amalsSummary.toString() : defaultSummary;

      activity.runOnUiThread(() -> {
        String message = isBn
            ? ("এই দিনের আমল ও ইবাদতের বাস্তব রেকর্ড:\n\n" +
            "• স্থিতি: " + statusBadge + "\n" +
            "• সালাত আদায়: " + BengaliNumberUtil.toBengali(finalPrayers) + "/৫ ওয়াক্ত\n" +
            "• সম্পন্নকৃত আমল: " + BengaliNumberUtil.toBengali(finalAmals) + "টি (" + finalSummary + ")\n" +
            "• এই দিনে অর্জিত পয়েন্ট: " + BengaliNumberUtil.toBengali(earnedPoints) + "XP\n" +
            "• দৈনিক অগ্রগতি: " + BengaliNumberUtil.toBengali((finalPrayers * 20)) + "% সম্পন্ন\n\n" +
            statusAdvice)
            : ("Activity and Worship Record for this Day:\n\n" +
            "• Status: " + statusBadge + "\n" +
            "• Salah Prayed: " + finalPrayers + "/5 Prayers\n" +
            "• Completed Deeds: " + finalAmals + " (" + finalSummary + ")\n" +
            "• Points Earned: " + earnedPoints + " XP\n" +
            "• Daily Progress: " + (finalPrayers * 20) + "% Completed\n\n" +
            statusAdvice);

        new AlertDialog.Builder(activity)
            .setTitle(dateTitle)
            .setMessage(message)
            .setPositiveButton(isBn ? "ঠিক আছে" : "OK", null)
            .show();
      });
    });
  }

  private static String formatJoinDate(long timestamp, boolean isBn) {
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(timestamp);
    int day = c.get(Calendar.DAY_OF_MONTH);
    int month = c.get(Calendar.MONTH);
    int year = c.get(Calendar.YEAR);

    String[] bnMonths = {
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };
    String[] enMonths = {
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    if (isBn) {
      return BengaliNumberUtil.toBengali(day) + " " + bnMonths[month] + ", " + BengaliNumberUtil.toBengali(year);
    } else {
      return day + " " + enMonths[month] + ", " + year;
    }
  }

  private static String getLevelBadgeTitle(int level, boolean isBn) {
    if (level >= 5) return isBn ? "মুত্তাকী ও সালেহীন" : "Righteous Muttaqi";
    if (level >= 3) return isBn ? "অগ্রগামী মুমিন" : "Advanced Believer";
    if (level >= 2) return isBn ? "দ্বীনি সাধক" : "Devoted Seeker";
    return isBn ? "দ্বীনওয়ান শিক্ষার্থী" : "DeenOne Learner";
  }

  private static long getAppInstallTime(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
    } catch (Exception e) {
      return System.currentTimeMillis();
    }
  }

  private static int dpToPx(Context context, int dp) {
    return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
  }
}