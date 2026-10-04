package com.devflux.deenone.features.leaderboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendRepository;
import com.devflux.deenone.core.backend.IBackendService;
import com.devflux.deenone.core.backend.model.LeaderboardUser;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.databinding.PageLeaderboardRankBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardRankPageDialog {

  private static final String PREF_PROFILE = "user_profile_prefs";
  private static final String KEY_USER_NAME = "user_full_name";
  private static final String KEY_LOCATION = "user_location";

  public static void show(Activity activity) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    PageLeaderboardRankBinding binding = PageLeaderboardRankBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(activity);
    binding.tvLeaderboardHeaderTitle.setText(isBn ? "\u09b2\u09c0\u09a1\u09be\u09b0\u09ac\u09cb\u09b0\u09cd\u09a1 \u0993 \u09a6\u09cd\u09ac\u09c0\u09a8\u09bf \u09b0\u09cd\u09af\u09be\u0982\u0995" : "Leaderboard & Islamic Ranks");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnBackLeaderboard);
    binding.btnBackLeaderboard.setOnClickListener(v -> dialog.dismiss());

    // Bottom Navigation Tabs & Touch Feedback
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabHome);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabSalat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabAmal);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabRank);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabCommunity);
    binding.tabHome.setOnClickListener(v -> dialog.dismiss());
    binding.tabSalat.setOnClickListener(v -> {
      dialog.dismiss();
      if (activity instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) activity).showSalahTrackerSheet();
      }
    });
    binding.tabAmal.setOnClickListener(v -> {
      dialog.dismiss();
      if (activity instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) activity).showAmalTrackerSheet();
      }
    });
    binding.tabRank.setOnClickListener(v -> {
      // Already on Rank
    });
    binding.tabCommunity.setOnClickListener(v -> {
      dialog.dismiss();
      com.devflux.deenone.features.community.CommunityFeedDialog.show(activity);
    });

    // Load dynamic real-time leaderboard data
    loadLeaderboardData(activity, binding);

    dialog.show();
  }

  private static void loadLeaderboardData(Context context, PageLeaderboardRankBinding binding) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      SharedPreferences profilePrefs = context.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE);
      AppDatabase db = AppDatabase.getInstance(context);

      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
      com.devflux.deenone.core.auth.AuthManager.UserSession session =
          com.devflux.deenone.core.auth.AuthManager.getCurrentSession(context);
      boolean isLoggedIn = session != null && session.isLoggedIn;

      UserProfileEntity activeProfile = db.userProfileDao().getActiveProfileSync();
      String currentUserId = (isLoggedIn && session.userId != null && !session.userId.trim().isEmpty())
          ? session.userId
          : (activeProfile != null && activeProfile.getUserId() != null ? activeProfile.getUserId() : "usr_guest");

      String currentUserName = (isLoggedIn && session.name != null && !session.name.trim().isEmpty())
          ? session.name
          : (activeProfile != null && activeProfile.getFullName() != null && !activeProfile.getFullName().trim().isEmpty()
              ? activeProfile.getFullName()
              : profilePrefs.getString(KEY_USER_NAME, profilePrefs.getString("full_name", isBn ? "\u09a6\u09cd\u09ac\u09c0\u09a8\u0993\u09af\u09bc\u09be\u09a8 \u09ac\u09cd\u09af\u09ac\u09b9\u09be\u09b0\u0995\u09be\u09b0\u09c0" : "DeenOne User")));

      com.devflux.deenone.core.location.LocationProvider.Coordinates coords =
          com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(context);
      String currentLocation = (coords != null && coords.locationName != null && !coords.locationName.trim().isEmpty())
          ? coords.locationName
          : (activeProfile != null && activeProfile.getTimezone() != null ? activeProfile.getTimezone() : (isBn ? "\u09a2\u09be\u0995\u09be" : "Dhaka"));

      int currentXP = GamificationManager.getTotalXP(context);
      int level = GamificationManager.getLevel(context);
      String levelBadge = getLevelBadgeTitle(level, isBn);

      BackendRepository.getInstance(context).fetchLeaderboard(new IBackendService.BackendCallback<List<LeaderboardUser>>() {
        @Override
        public void onSuccess(List<LeaderboardUser> backendUsers) {
          processAndRenderLeaderboard(context, binding, backendUsers, currentUserId, currentUserName, currentLocation, currentXP, levelBadge);
        }

        @Override
        public void onError(String errorMessage) {
          processAndRenderLeaderboard(context, binding, new ArrayList<>(), currentUserId, currentUserName, currentLocation, currentXP, levelBadge);
        }
      });
    });
  }

  private static void processAndRenderLeaderboard(Context context,
                          PageLeaderboardRankBinding binding,
                          List<LeaderboardUser> backendUsers,
                          String currentUserId,
                          String currentUserName,
                          String currentLocation,
                          int currentXP,
                          String levelBadge) {
    List<LeaderboardUser> combinedList = new ArrayList<>();

    if (backendUsers != null && !backendUsers.isEmpty()) {
      combinedList.addAll(backendUsers);
    }

    // Merge / Update Active Logged-in User
    boolean found = false;
    for (LeaderboardUser u : combinedList) {
      if (u.getUserId().equalsIgnoreCase(currentUserId) || u.getUserName().equalsIgnoreCase(currentUserName)) {
        found = true;
        u.setUserName(currentUserName);
        u.setLocation(currentLocation);
        u.setAmalPoints(Math.max(u.getPoints(), currentXP));
        u.setBadgeTitle(levelBadge);
        break;
      }
    }

    if (!found) {
      combinedList.add(new LeaderboardUser(
          combinedList.size() + 1,
          currentUserId,
          currentUserName,
          "",
          currentLocation,
          currentXP,
          1,
          levelBadge
      ));
    }

    // Sort descending by points, then streak days, then name
    Collections.sort(combinedList, (a, b) -> {
      if (b.getPoints() != a.getPoints()) {
        return Integer.compare(b.getPoints(), a.getPoints());
      }
      if (b.getStreakDays() != a.getStreakDays()) {
        return Integer.compare(b.getStreakDays(), a.getStreakDays());
      }
      return a.getUserName().compareToIgnoreCase(b.getUserName());
    });

    // Re-index ranks
    for (int i = 0; i < combinedList.size(); i++) {
      combinedList.get(i).setRank(i + 1);
    }

    new Handler(Looper.getMainLooper()).post(() -> {
      renderLeaderboard(context, binding, combinedList, currentUserId, currentUserName);
    });
  }

  private static void renderLeaderboard(Context context,
                      PageLeaderboardRankBinding binding,
                      List<LeaderboardUser> users,
                      String currentUserId,
                      String currentUserName) {
    if (users == null) return;

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // 1. DYNAMIC TOP 3 PODIUM BINDING (Real Data from Sorted List)
    // Center: Rank 1 (Gold Crown)
    if (users.size() > 0) {
      LeaderboardUser u1 = users.get(0);
      boolean isU1Current = (!currentUserId.equals("usr_guest") && u1.getUserId().equalsIgnoreCase(currentUserId))
          || u1.getUserName().equalsIgnoreCase(currentUserName);
      binding.tvPodium1Name.setText(u1.getUserName() + (isU1Current ? (isBn ? " (\u0986\u09aa\u09a8\u09bf)" : " (You)") : ""));
      binding.tvPodium1Points.setText(isBn
          ? (BengaliNumberUtil.toBengali(u1.getPoints()) + " \u09aa\u09df\u09c7\u09a8\u09cd\u099f")
          : (u1.getPoints() + " Points"));
    } else {
      binding.tvPodium1Name.setText("-");
      binding.tvPodium1Points.setText("-");
    }

    // Left: Rank 2 (Silver Medal)
    if (users.size() > 1) {
      LeaderboardUser u2 = users.get(1);
      boolean isU2Current = (!currentUserId.equals("usr_guest") && u2.getUserId().equalsIgnoreCase(currentUserId))
          || u2.getUserName().equalsIgnoreCase(currentUserName);
      binding.tvPodium2Name.setText(u2.getUserName() + (isU2Current ? (isBn ? " (\u0986\u09aa\u09a8\u09bf)" : " (You)") : ""));
      binding.tvPodium2Points.setText(isBn
          ? (BengaliNumberUtil.toBengali(u2.getPoints()) + " \u09aa\u09df\u09c7\u09a8\u09cd\u099f")
          : (u2.getPoints() + " Points"));
    } else {
      binding.tvPodium2Name.setText("-");
      binding.tvPodium2Points.setText("-");
    }

    // Right: Rank 3 (Bronze Medal)
    if (users.size() > 2) {
      LeaderboardUser u3 = users.get(2);
      boolean isU3Current = (!currentUserId.equals("usr_guest") && u3.getUserId().equalsIgnoreCase(currentUserId))
          || u3.getUserName().equalsIgnoreCase(currentUserName);
      binding.tvPodium3Name.setText(u3.getUserName() + (isU3Current ? (isBn ? " (\u0986\u09aa\u09a8\u09bf)" : " (You)") : ""));
      binding.tvPodium3Points.setText(isBn
          ? (BengaliNumberUtil.toBengali(u3.getPoints()) + " \u09aa\u09df\u09c7\u09a8\u09cd\u099f")
          : (u3.getPoints() + " Points"));
    } else {
      binding.tvPodium3Name.setText("-");
      binding.tvPodium3Points.setText("-");
    }

    // 2. DYNAMIC FULL RANKED LIST BINDING (Starting from #1 to N)
    binding.layoutLeaderboardList.removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(context);

    for (int i = 0; i < users.size(); i++) {
      LeaderboardUser u = users.get(i);
      View itemView = inflater.inflate(R.layout.item_leaderboard_user, binding.layoutLeaderboardList, false);

      MaterialCardView cardView = itemView.findViewById(R.id.cardLeaderboardUser);
      TextView tvRank = itemView.findViewById(R.id.tvLeaderboardRank);
      TextView tvName = itemView.findViewById(R.id.tvLeaderboardName);
      TextView tvStreak = itemView.findViewById(R.id.tvLeaderboardStreak);
      TextView tvPoints = itemView.findViewById(R.id.tvLeaderboardPoints);

      // Format Rank Badge
      if (tvRank != null) {
        tvRank.setText(isBn ? ("#" + BengaliNumberUtil.toBengali(u.getRank())) : ("#" + u.getRank()));
        if (u.getRank() == 1) {
          tvRank.setTextColor(Color.parseColor("#FFB300")); // Gold
        } else if (u.getRank() == 2) {
          tvRank.setTextColor(Color.parseColor("#FFA000")); // Amber
        } else if (u.getRank() == 3) {
          tvRank.setTextColor(Color.parseColor("#F59E0B")); // Bronze
        } else {
          tvRank.setTextColor(Color.parseColor("#9CA3AF")); // Muted Gray
        }
      }

      // User Name & Active User Badge
      boolean isCurrentUser = (!currentUserId.equals("usr_guest") && u.getUserId().equalsIgnoreCase(currentUserId))
          || u.getUserName().equalsIgnoreCase(currentUserName);
      if (tvName != null) {
        if (isCurrentUser) {
          tvName.setText(u.getUserName() + (isBn ? " (\u0986\u09aa\u09a8\u09bf)" : " (You)"));
        } else {
          tvName.setText(u.getUserName());
        }
      }

      // Subtitle: City • Level Badge (e.g. ঢাকা • নবাগত)
      if (tvStreak != null) {
        tvStreak.setText(u.getLocation() + " \u2022 " + u.getBadgeTitle());
      }

      // Points text in mint green
      if (tvPoints != null) {
        tvPoints.setText(isBn
            ? (BengaliNumberUtil.toBengali(u.getPoints()) + " \u09aa\u09df\u09c7\u09a8\u09cd\u099f")
            : (u.getPoints() + " Points"));
      }

      // Highlight card if it's the active user (NO touch animation on CardView per Rule 7)
      if (cardView != null) {
        if (isCurrentUser) {
          cardView.setStrokeColor(ContextCompat.getColor(context, R.color.accent_mint));
          cardView.setStrokeWidth((int) (1.5f * context.getResources().getDisplayMetrics().density));
        } else {
          cardView.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
          cardView.setStrokeWidth((int) (1.2f * context.getResources().getDisplayMetrics().density));
        }
      }

      binding.layoutLeaderboardList.addView(itemView);
    }
  }

  private static String getLevelBadgeTitle(int level, boolean isBn) {
    switch (level) {
      case 1: return isBn ? "\u09a8\u09ac\u09be\u0997\u09a4" : "Novice";
      case 2: return isBn ? "\u09ae\u09c1\u09ae\u09bf\u09a8" : "Mu'min";
      case 3: return isBn ? "\u09ae\u09c1\u0996\u09b2\u09bf\u09b8" : "Mukhlas";
      case 4: return isBn ? "\u09b8\u09be\u09b2\u09c7\u09b9\u09c0\u09a8" : "Saleheen";
      case 5: default: return isBn ? "\u09ae\u09c1\u09a4\u09cd\u09a4\u09be\u0995\u09c0" : "Muttaqi";
    }
  }
}