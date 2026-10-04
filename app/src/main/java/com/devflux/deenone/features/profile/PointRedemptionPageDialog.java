package com.devflux.deenone.features.profile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.BackendRepository;
import com.devflux.deenone.core.backend.PhpMysqlBackendService;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.UserProfileEntity;
import com.devflux.deenone.databinding.PagePointRedemptionBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class PointRedemptionPageDialog {

  public static void show(Activity activity) {
    if (activity == null || activity.isFinishing()) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
    PagePointRedemptionBinding binding = PagePointRedemptionBinding.inflate(LayoutInflater.from(activity));
    dialog.setContentView(binding.getRoot());

    boolean isRedeemDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(activity) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    binding.ivRedeemThemeIcon.setImageResource(isRedeemDark ? R.drawable.ic_sun : R.drawable.ic_moon);

    binding.btnBackRedeem.setOnClickListener(v -> dialog.dismiss());

    binding.btnRedeemThemeToggle.setOnClickListener(v -> {
      if (activity instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) activity).toggleAppTheme();
        dialog.dismiss();
      }
    });

    binding.btnRedeemNotification.setOnClickListener(v -> {
      if (activity instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) activity).showNotificationHistorySheet();
      }
    });

    updateUi(activity, binding);

    // 1. Gift Box Voucher (2000 XP)
    binding.btnClaimReward1.setOnClickListener(v -> {
      handleRedeemAttempt(activity, binding, "ইসলামিক গিফট বক্স ভাউচার (কুরআন শরীফ, জায়নামাজ ও আতর)", 2000);
    });

    // 2. Water Project Sadaqah (1000 XP)
    binding.btnClaimReward2.setOnClickListener(v -> {
      handleRedeemAttempt(activity, binding, "উম্মাহ টিউবওয়েল ও বিশুদ্ধ পানি প্রকল্পে সদকাহ অনুদান", 1000);
    });

    // 3. Quran Distribution (800 XP)
    binding.btnClaimReward3.setOnClickListener(v -> {
      handleRedeemAttempt(activity, binding, "মাদরাসা শিক্ষার্থীদের জন্য কুরআন বিতরণ ফান্ড", 800);
    });

    // 4. Audio Pack (500 XP)
    binding.btnClaimReward4.setOnClickListener(v -> {
      handleRedeemAttempt(activity, binding, "প্রিমিয়াম হারামাইন কারীদের তিলাওয়াত অডিও প্যাক", 500);
    });

    dialog.show();
  }

  private static void updateUi(Context context, PagePointRedemptionBinding binding) {
    int currentXP = GamificationManager.getTotalXP(context);
    int level = GamificationManager.getLevel(context);

    binding.tvRedeemTotalPoints.setText(BengaliNumberUtil.toBengali(currentXP));
    binding.tvRedeemCurrentLevel.setText("লেভেল "+ BengaliNumberUtil.toBengali(level) + ": "+ getLevelTitle(level));

    int nextLevelTarget = level * 500;
    int levelStart = (level - 1) * 500;
    int currentLevelProgress = Math.max(0, currentXP - levelStart);

    binding.tvRedeemNextLevelTarget.setText("পরবর্তী: লেভেল "+ BengaliNumberUtil.toBengali(level + 1) + "("+ BengaliNumberUtil.toBengali(nextLevelTarget) + "XP)");
    binding.pbRedeemLevelProgress.setMax(500);
    binding.pbRedeemLevelProgress.setProgress(Math.min(500, currentLevelProgress));
  }

  private static void handleRedeemAttempt(Activity activity, PagePointRedemptionBinding binding, String rewardTitle, int costXP) {
    int currentXP = GamificationManager.getTotalXP(activity);

    if (currentXP < costXP) {
      int needed = costXP - currentXP;
      new MaterialAlertDialogBuilder(activity)
          .setTitle("অপর্যাপ্ত পয়েন্ট")
          .setMessage("এই পুরস্কারটি রিডিম করার জন্য আরও "+ BengaliNumberUtil.toBengali(needed) + "XP পয়েন্ট প্রয়োজন।\n\nপ্রতিদিন ৫ ওয়াক্ত সালাত, নেক আমল ও ইসলামিক কুইজ খেলে পয়েন্ট অর্জন করুন!")
          .setPositiveButton("পয়েন্ট অর্জন করুন", null)
          .show();
      return;
    }

    new MaterialAlertDialogBuilder(activity)
        .setTitle("রিডিম নিশ্চিতকরণ")
        .setMessage("আপনি কি "+ BengaliNumberUtil.toBengali(costXP) + "XP পয়েন্ট ব্যবহার করে '" + rewardTitle + "' রিডিম করতে চান?")
        .setPositiveButton("হ্যাঁ, রিডিম করুন", (d, w) -> {
          // Deduct XP
          GamificationManager.addXP(activity, -costXP);
          int remainingXP = GamificationManager.getTotalXP(activity);

          AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(activity);
            UserProfileEntity profile = db.userProfileDao().getActiveProfileSync();
            if (profile != null) {
              profile.setPoints(remainingXP);
              db.userProfileDao().insertOrUpdateProfile(profile);
            }
          });

          updateUi(activity, binding);

          new MaterialAlertDialogBuilder(activity)
              .setTitle("মাশাআল্লাহ! রিডিম সফল")
              .setMessage("অভিনন্দন! আপনার '" + rewardTitle + "' সফলভাবে রিডিম সম্পন্ন হয়েছে। আপনার নতুন ব্যালেন্স: "+ BengaliNumberUtil.toBengali(remainingXP) + "XP।")
              .setPositiveButton("আলহামদুলিল্লাহ", null)
              .show();
        })
        .setNegativeButton("বাতিল", null)
        .show();
  }

  private static String getLevelTitle(int level) {
    switch (level) {
      case 1: return "মুত্তাকী";
      case 2: return "সালেহীন";
      case 3: return "সাবেকূনা বিল খাইরাত";
      case 4: return "মুহসিনীন";
      case 5: default: return "আল্লাহর ওলী";
    }
  }
}
