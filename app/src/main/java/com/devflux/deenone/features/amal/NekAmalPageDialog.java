package com.devflux.deenone.features.amal;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager;
import com.devflux.deenone.core.amal.DailyAmalRotationEngine;
import com.devflux.deenone.core.amal.UserLocationTimezoneHelper;
import com.devflux.deenone.core.tasbih.TasbihTapProtectionManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.databinding.DialogConfirmAmalCompletionBinding;
import com.devflux.deenone.databinding.DialogSelectDhikrBinding;
import com.devflux.deenone.databinding.ItemDhikrSelectRowBinding;
import com.devflux.deenone.databinding.PageNekAmalBinding;
import com.devflux.deenone.features.amal.adapter.NekAmalCardAdapter;
import com.devflux.deenone.features.community.CommunityFeedDialog;
import com.devflux.deenone.features.leaderboard.LeaderboardRankPageDialog;
import com.devflux.deenone.features.prayer.SalahTrackerPageDialog;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class NekAmalPageDialog {

  private static CountDownTimer activeCooldownTimer = null;
  private static Handler warningDismissHandler = new Handler(Looper.getMainLooper());

  public static void show(@NonNull Context context) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    PageNekAmalBinding binding = PageNekAmalBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    // Ensure 10-Amal Daily Rotation for User's Location Timezone
    DailyAmalRotationEngine.ensureDailyRotation(context);

    // ViewModel setup
    ViewModelStoreOwner owner = (context instanceof ViewModelStoreOwner) ? (ViewModelStoreOwner) context : null;
    AmalViewModel amalVm = (owner != null)
        ? new ViewModelProvider(owner).get(AmalViewModel.class)
        : new ViewModelProvider((ViewModelStoreOwner) ((Activity) context)).get(AmalViewModel.class);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // Static text localization
    if (isBn) {
      binding.tvAmalHeaderTitle.setText("নেক আমল");
      binding.tvAmalHeroTitle.setText("আমল রেকর্ড");
      binding.tvAmalHeroSubtitle.setText("প্রতিদিনের পুণ্যকর্ম রেকর্ড করুন এবং আধ্যাত্মিক পয়েন্ট অর্জন করুন");
      binding.tvTotalPointsLabel.setText("মোট পয়েন্ট");
      binding.tvTodayCompletedLabel.setText("আজকের ইবাদত");
      binding.tvAmalAnalyticsTitle.setText("বিস্তারিত রিপোর্ট ও অ্যানালিটিক্স দেখুন");
      binding.tvTasbihChallengeHeader.setText("— তাসবীহ চ্যালেঞ্জ —");
      binding.tvTasbihRewardBadge.setText("পুরস্কার: +৫০ পয়েন্ট");
      binding.tvAmalContestBadge.setText("আজকের নেক আমল প্রতিযোগিতা");
      binding.tvAmalListSectionTitle.setText("নেক আমল সমূহ");
      binding.tvResetTimeLabel.setText("১২:০০ AM রিসেট");
      binding.tvWeeklyRankBadge.setText("● সাপ্তাহিক র‍্যাঙ্ক");
      binding.tvSpiritualProgressTitle.setText("আপনার\nরুহানি উন্নতি");
      binding.tvSpiritualProgressDesc.setText("সুবহানাল্লাহ! আপনি ঈমান ও নেক আমলে প্রতিনিয়ত এগিয়ে যাচ্ছেন।");
      binding.tvWeeklyPointsLabel.setText("সাপ্তাহিক মোট পয়েন্ট");
    } else {
      binding.tvAmalHeaderTitle.setText("Good Deeds");
      binding.tvAmalHeroTitle.setText("Amal Record");
      binding.tvAmalHeroSubtitle.setText("Track daily deeds and earn spiritual points");
      binding.tvTotalPointsLabel.setText("Total Points");
      binding.tvTodayCompletedLabel.setText("Today's Deeds");
      binding.tvAmalAnalyticsTitle.setText("View Detailed Report & Analytics");
      binding.tvTasbihChallengeHeader.setText("— Tasbih Challenge —");
      binding.tvTasbihRewardBadge.setText("Reward: +50 Points");
      binding.tvAmalContestBadge.setText("Today's Good Deeds Competition");
      binding.tvAmalListSectionTitle.setText("Good Deeds");
      binding.tvResetTimeLabel.setText("12:00 AM Reset");
      binding.tvWeeklyRankBadge.setText("● Weekly Rank");
      binding.tvSpiritualProgressTitle.setText("Your\nSpiritual Growth");
      binding.tvSpiritualProgressDesc.setText("SubhanAllah! You are continuously growing in faith and good deeds.");
      binding.tvWeeklyPointsLabel.setText("Weekly Total Points");
    }

    // 1. Month & Year Title
    binding.tvAmalMonthYear.setText(DailyAmalRotationEngine.getTodayMonthYearFormatted(context, isBn));

    // 2. Real Points & Today's Completed Count
    Runnable updatePointsDisplay = () -> {
      int currentXp = AuthoritativePointsLedgerManager.getLifetimePoints(context);
      int weeklyXp = AuthoritativePointsLedgerManager.getWeeklyPoints(context);
      binding.tvTotalPoints.setText(isBn ? BengaliNumberUtil.toBengali(currentXp) : String.valueOf(currentXp));
      binding.tvWeeklyPoints.setText(isBn ? BengaliNumberUtil.toBengali(weeklyXp) : String.valueOf(weeklyXp));
    };
    updatePointsDisplay.run();

    // 3. Section: Tasbih Challenge Interactive Dial with Anti-Spam Tap Protection & Dhikr Switcher
    final int[] currentDhikrIndex = {DailyAmalRotationEngine.getSelectedDhikrIndex(context)};
    final DailyAmalRotationEngine.TasbihChallengeInfo[] currentChallenge = {DailyAmalRotationEngine.getTasbihChallengeByIndex(context, currentDhikrIndex[0])};
    final int[] tasbihCount = {DailyAmalRotationEngine.getTodayDhikrCount(context, currentDhikrIndex[0])};
    final int[] tasbihCompletedTimes = {DailyAmalRotationEngine.getTodayDhikrCompletedTimes(context, currentDhikrIndex[0])};

    binding.tvTasbihDuaTitle.setText(currentChallenge[0].duaTitle);
    binding.tvTasbihDuaMeaning.setText(currentChallenge[0].duaMeaning);
    binding.progressTasbihDial.setMax(currentChallenge[0].targetCount);

    Runnable applyNormalTheme = () -> {
      binding.layoutTasbihButton.setEnabled(true);
      binding.layoutTasbihButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
      binding.knobTasbihIndicator.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
      binding.layoutTasbihProtectionBanner.setVisibility(View.GONE);
      binding.tvTasbihCooldownCountdown.setVisibility(View.GONE);
      binding.tvTasbihCount.setTextColor(ContextCompat.getColor(context, R.color.bg_main));
      binding.tvTasbihCountSubtitle.setText(isBn ? "বার" : "times");
      binding.tvTasbihCountSubtitle.setTextColor(ContextCompat.getColor(context, R.color.bg_main));
      binding.ivTasbihStatusIcon.setVisibility(View.GONE);
      binding.tvTasbihCount.setVisibility(View.VISIBLE);
    };

    Runnable updateTasbihUI = () -> {
      binding.tvTasbihCount.setText(isBn ? BengaliNumberUtil.toBengali(tasbihCount[0]) : String.valueOf(tasbihCount[0]));
      binding.progressTasbihDial.setProgress(tasbihCount[0]);
      binding.tvTasbihTargetPill.setText(isBn
          ? ("" + BengaliNumberUtil.toBengali(currentChallenge[0].targetCount) + "বার লক্ষ্য • সম্পন্ন: " + BengaliNumberUtil.toBengali(tasbihCompletedTimes[0]) + "বার")
          : ("Target: " + currentChallenge[0].targetCount + " • Done: " + tasbihCompletedTimes[0] + " times"));
      binding.tvTasbihCompletedUsers.setText(isBn
          ? ("আজ সম্পন্ন করেছে: " + BengaliNumberUtil.toBengali(Math.max(1, tasbihCompletedTimes[0])) + "জন")
          : ("Completed today: " + Math.max(1, tasbihCompletedTimes[0]) + " users"));
    };
    updateTasbihUI.run();

    // Helper to start real-time countdown timer
    java.util.function.Consumer<Long> startCooldownUI = (cooldownMillis) -> {
      if (activeCooldownTimer != null) {
        activeCooldownTimer.cancel();
      }

      binding.layoutTasbihButton.setEnabled(false);
      binding.layoutTasbihButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_red)));
      binding.knobTasbihIndicator.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_red)));
      binding.layoutTasbihProtectionBanner.setVisibility(View.VISIBLE);
      binding.layoutTasbihProtectionBanner.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg_card_secondary)));
      binding.tvTasbihProtectionWarning.setText(isBn ? "তাসবিহ সাময়িকভাবে বন্ধ" : "Tasbih Temporarily Paused");
      binding.tvTasbihProtectionWarning.setTextColor(ContextCompat.getColor(context, R.color.accent_red));
      binding.tvTasbihCooldownCountdown.setVisibility(View.VISIBLE);

      binding.tvTasbihCount.setText("");
      binding.tvTasbihCountSubtitle.setText(isBn ? "অপেক্ষা করুন" : "Please wait");
      binding.tvTasbihCountSubtitle.setTextColor(ContextCompat.getColor(context, R.color.text_primary));

      activeCooldownTimer = new CountDownTimer(cooldownMillis, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
          String timeStr = TasbihTapProtectionManager.formatDuration(millisUntilFinished);
          binding.tvTasbihCooldownCountdown.setText(isBn
              ? ("দয়া করে " + BengaliNumberUtil.toBengali(timeStr) + "মিনিট অপেক্ষা করুন")
              : ("Please wait " + timeStr + " min"));
          int secRemaining = (int) (millisUntilFinished / 1000);
          binding.progressTasbihDial.setProgress(Math.min(100, secRemaining));
        }

        @Override
        public void onFinish() {
          TasbihTapProtectionManager.clearCooldown(context);
          applyNormalTheme.run();
          updateTasbihUI.run();
          Toast.makeText(context, isBn ? "কুলডাউন শেষ হয়েছে। এখন আপনি স্বাভাবিক গতিতে তাসবিহ পড়তে পারেন।" : "Cooldown ended. You can now resume tasbih.", Toast.LENGTH_SHORT).show();
        }
      }.start();
    };

    // Check if cooldown is already active
    if (TasbihTapProtectionManager.isCooldownActive(context)) {
      startCooldownUI.accept(TasbihTapProtectionManager.getRemainingCooldownMs(context));
    }

    // Switch Dhikr Callback
    java.util.function.Consumer<Integer> switchDhikrAction = (newIndex) -> {
      currentDhikrIndex[0] = newIndex;
      DailyAmalRotationEngine.setSelectedDhikrIndex(context, newIndex);
      currentChallenge[0] = DailyAmalRotationEngine.getTasbihChallengeByIndex(context, newIndex);
      tasbihCount[0] = DailyAmalRotationEngine.getTodayDhikrCount(context, newIndex);
      tasbihCompletedTimes[0] = DailyAmalRotationEngine.getTodayDhikrCompletedTimes(context, newIndex);

      binding.tvTasbihDuaTitle.setText(currentChallenge[0].duaTitle);
      binding.tvTasbihDuaMeaning.setText(currentChallenge[0].duaMeaning);
      binding.progressTasbihDial.setMax(currentChallenge[0].targetCount);

      applyNormalTheme.run();
      updateTasbihUI.run();
    };

    // Dua Switcher Bottom Sheet Trigger (Zero Touch Animation on Card)
    binding.cardDuaAccordion.setOnClickListener(v -> {
      showDhikrSelectBottomSheet(context, currentDhikrIndex[0], switchDhikrAction, isBn);
    });

    // Tap on circular button with Anti-Spam Tap Protection (Button touch spring attached below)
    binding.layoutTasbihButton.setOnClickListener(v -> {
      TasbihTapProtectionManager.ValidationResult vResult = TasbihTapProtectionManager.validateTap(context);

      if (vResult.status == TasbihTapProtectionManager.TapStatus.COOLDOWN_ACTIVE) {
        startCooldownUI.accept(vResult.remainingCooldownMs);
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        return;
      }

      if (vResult.status == TasbihTapProtectionManager.TapStatus.RAPID_TAP_WARNING) {
        // Warning State: Orange Theme + Do not count tap
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        binding.layoutTasbihButton.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_gold)));
        binding.knobTasbihIndicator.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_gold)));
        binding.layoutTasbihProtectionBanner.setVisibility(View.VISIBLE);
        binding.layoutTasbihProtectionBanner.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg_card_secondary)));
        binding.tvTasbihProtectionWarning.setText("" + vResult.warningMessage);
        binding.tvTasbihProtectionWarning.setTextColor(ContextCompat.getColor(context, R.color.accent_gold));
        binding.tvTasbihCooldownCountdown.setVisibility(View.GONE);

        warningDismissHandler.removeCallbacksAndMessages(null);
        warningDismissHandler.postDelayed(() -> {
          if (!TasbihTapProtectionManager.isCooldownActive(context)) {
            applyNormalTheme.run();
            updateTasbihUI.run();
          }
        }, 3000);
        return;
      }

      // Valid Normal Tap
      v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
      applyNormalTheme.run();

      // Bounce Animation
      ScaleAnimation anim = new ScaleAnimation(0.92f, 1.0f, 0.92f, 1.0f,
          Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
      anim.setDuration(120);
      v.startAnimation(anim);

      tasbihCount[0]++;
      int pointsEarnedThisStep = 0;
      if (tasbihCount[0] >= currentChallenge[0].targetCount) {
        tasbihCount[0] = 0;
        tasbihCompletedTimes[0]++;
        DailyAmalRotationEngine.incrementTodayDhikrCompletedTimes(context, currentDhikrIndex[0]);
        pointsEarnedThisStep = currentChallenge[0].rewardPoints;

        // Authoritative Point Award
        AuthoritativePointsLedgerManager.recordAmalCompletion(context, "user_main", "TASBIH_" + currentDhikrIndex[0] + "_" + currentChallenge[0].targetCount, currentChallenge[0].rewardPoints);
        updatePointsDisplay.run();

        Toast.makeText(context, isBn ? "মাশাআল্লাহ! তাসবীহ লক্ষ্য পূরণ হয়েছে • ৫০ পয়েন্ট অর্জিত" : "MashaAllah! Tasbih goal reached • 50 points earned", Toast.LENGTH_LONG).show();
      }
      DailyAmalRotationEngine.saveTodayDhikrCount(context, currentDhikrIndex[0], tasbihCount[0]);
      updateTasbihUI.run();

      // Cloud & MySQL Database Sync
      if (pointsEarnedThisStep > 0 || tasbihCount[0] % 10 == 0 || tasbihCount[0] == 1) {
        com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context).syncTasbihLog(
            currentDhikrIndex[0], currentChallenge[0].duaTitle, tasbihCount[0], tasbihCompletedTimes[0], pointsEarnedThisStep
        );
      }
    });

    // 4. Daily Amal Cards RecyclerView & 10-Amal Progress
    NekAmalCardAdapter adapter = new NekAmalCardAdapter(new ArrayList<>(), (amal, position) -> {
      showConfirmationDialog(context, amal, () -> {
        amalVm.completeAmal(amal);
        new Handler(Looper.getMainLooper()).postDelayed(updatePointsDisplay::run, 150);

        Toast.makeText(context, isBn
            ? ("মাশাআল্লাহ! '" + amal.getTitle() + "' সম্পন্ন হয়েছে • " + BengaliNumberUtil.toBengali(amal.getPoints()) + " পয়েন্ট অর্জিত")
            : ("MashaAllah! '" + amal.getTitle() + "' completed • " + amal.getPoints() + " points earned"), Toast.LENGTH_SHORT).show();
      });
    });

    binding.rvDailyAmalList.setLayoutManager(new LinearLayoutManager(context));
    binding.rvDailyAmalList.setAdapter(adapter);

    amalVm.getTodayAmals().observe((AppCompatActivity) context, list -> {
      if (list != null) {
        adapter.updateList(list);
      }
    });

    final int[] completedCount = {0};
    final int[] totalCount = {10};

    Runnable updateProgressCard = () -> {
      int total = totalCount[0] > 0 ? totalCount[0] : 10;
      int completed = completedCount[0];
      int pct = Math.min(100, (completed * 100) / total);

      binding.tvTodayCompletedCount.setText(isBn ? BengaliNumberUtil.toBengali(completed) : String.valueOf(completed));
      binding.tvDailyAmalFraction.setText(isBn
          ? ("আজকের নেক আমল: " + BengaliNumberUtil.toBengali(completed) + "/" + BengaliNumberUtil.toBengali(total) + "টি সম্পন্ন (" + BengaliNumberUtil.toBengali(pct) + "%)")
          : ("Daily Good Deeds: " + completed + "/" + total + " completed (" + pct + "%)"));
      binding.progressDailyAmal.setMax(total);
      binding.progressDailyAmal.setProgress(completed);
      updatePointsDisplay.run();
    };

    amalVm.getTodayCompletedCount().observe((AppCompatActivity) context, completed -> {
      completedCount[0] = (completed != null) ? completed : 0;
      updateProgressCard.run();
    });

    amalVm.getTodayTotalCount().observe((AppCompatActivity) context, total -> {
      if (total != null && total > 0) {
        totalCount[0] = total;
      }
      updateProgressCard.run();
    });

    // 5. Analytics Card (Strict Zero Touch Animation on Cards as per Rule 7)
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.layoutTasbihButton);
    binding.cardAmalAnalytics.setOnClickListener(v -> {
      AmalAnalyticsPageDialog.show(context);
    });

    // 6. Header Buttons
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseAmal);
    binding.btnCloseAmal.setOnClickListener(v -> {
      if (activeCooldownTimer != null) activeCooldownTimer.cancel();
      dialog.dismiss();
    });

    // 7. Bottom Navigation Tabs & Touch Feedback
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabHome);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabSalat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabAmal);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabRank);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabCommunity);
    binding.tabHome.setOnClickListener(v -> {
      if (activeCooldownTimer != null) activeCooldownTimer.cancel();
      dialog.dismiss();
    });
    binding.tabSalat.setOnClickListener(v -> {
      if (activeCooldownTimer != null) activeCooldownTimer.cancel();
      dialog.dismiss();
      if (context instanceof Activity) {
        SalahTrackerPageDialog.show((Activity) context);
      }
    });
    binding.tabAmal.setOnClickListener(v -> binding.scrollAmalContent.smoothScrollTo(0, 0));
    binding.tabRank.setOnClickListener(v -> {
      if (activeCooldownTimer != null) activeCooldownTimer.cancel();
      dialog.dismiss();
      if (context instanceof Activity) {
        LeaderboardRankPageDialog.show((Activity) context);
      }
    });
    binding.tabCommunity.setOnClickListener(v -> {
      if (activeCooldownTimer != null) activeCooldownTimer.cancel();
      dialog.dismiss();
      CommunityFeedDialog.show(context);
    });

    dialog.setOnDismissListener(d -> {
      if (activeCooldownTimer != null) activeCooldownTimer.cancel();
    });

    dialog.show();
  }

  private static void showDhikrSelectBottomSheet(Context context, int currentSelected, java.util.function.Consumer<Integer> onSelected, boolean isBn) {
    BottomSheetDialog sheetDialog = new BottomSheetDialog(context);
    DialogSelectDhikrBinding sheetBinding = DialogSelectDhikrBinding.inflate(LayoutInflater.from(context));
    sheetDialog.setContentView(sheetBinding.getRoot());

    sheetBinding.tvSelectDhikrTitle.setText(isBn ? "জিকির নির্বাচন করুন" : "Select Dhikr");
    sheetBinding.tvSelectDhikrSubtitle.setText(isBn ? "যেকোনো জিকির বেছে নিয়ে তাসবীহ চ্যালেঞ্জ পড়ুন" : "Choose any Dhikr to recite for your daily challenge");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseDhikrSelect);
    sheetBinding.btnCloseDhikrSelect.setOnClickListener(v -> sheetDialog.dismiss());

    List<DailyAmalRotationEngine.TasbihChallengeInfo> list = DailyAmalRotationEngine.getAllTasbihChallenges(context);
    sheetBinding.rvDhikrSelectList.setLayoutManager(new LinearLayoutManager(context));
    sheetBinding.rvDhikrSelectList.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
      @NonNull
      @Override
      public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDhikrSelectRowBinding rowBinding = ItemDhikrSelectRowBinding.inflate(LayoutInflater.from(context), parent, false);
        return new RecyclerView.ViewHolder(rowBinding.getRoot()) {};
      }

      @Override
      public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemDhikrSelectRowBinding row = ItemDhikrSelectRowBinding.bind(holder.itemView);
        DailyAmalRotationEngine.TasbihChallengeInfo item = list.get(position);

        row.tvDhikrNumber.setText(isBn ? BengaliNumberUtil.toBengali(position + 1) : String.valueOf(position + 1));
        row.tvDhikrRowTitle.setText(item.duaTitle);
        row.tvDhikrRowMeaning.setText(item.duaMeaning);

        int todayCount = DailyAmalRotationEngine.getTodayDhikrCount(context, position) + (DailyAmalRotationEngine.getTodayDhikrCompletedTimes(context, position) * 100);
        row.tvDhikrRowCount.setText(isBn ? (BengaliNumberUtil.toBengali(todayCount) + " বার") : (todayCount + " times"));

        boolean isSelected = (position == currentSelected);
        row.ivDhikrSelectedCheck.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        if (isSelected) {
          row.cardDhikrRow.setStrokeColor(ContextCompat.getColor(context, R.color.accent_mint));
          row.cardDhikrRow.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_card_active));
          row.tvDhikrRowTitle.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
        } else {
          row.cardDhikrRow.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
          row.cardDhikrRow.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_card_secondary));
          row.tvDhikrRowTitle.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }

        row.cardDhikrRow.setOnClickListener(v -> {
          sheetDialog.dismiss();
          if (onSelected != null) {
            onSelected.accept(position);
          }
        });
      }

      @Override
      public int getItemCount() {
        return list.size();
      }
    });

    sheetDialog.show();
  }

  private static void showConfirmationDialog(Context context, DailyAmalEntity amal, Runnable onConfirmed) {
    BottomSheetDialog confirmDialog = new BottomSheetDialog(context);
    DialogConfirmAmalCompletionBinding confirmBinding = DialogConfirmAmalCompletionBinding.inflate(LayoutInflater.from(context));
    confirmDialog.setContentView(confirmBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    confirmBinding.tvConfirmTitle.setText(isBn ? "আপনি কি নিশ্চিত?" : "Are you sure?");
    confirmBinding.tvConfirmDesc.setText(isBn
        ? "আপনি কি সত্যিই এই নেক আমলটি সম্পন্ন করেছেন? মিথ্যা তথ্য প্রদান করলে পয়েন্ট কর্তন করা হতে পারে।"
        : "Did you truly complete this good deed? Providing false information may cause deduction of points.");
    confirmBinding.btnConfirmYes.setText(isBn ? "হ্যাঁ, করেছি" : "Yes, I did");
    confirmBinding.btnConfirmNo.setText(isBn ? "এখনো না" : "Not yet");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(confirmBinding.btnConfirmYes);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(confirmBinding.btnConfirmNo);

    confirmBinding.btnConfirmYes.setOnClickListener(v -> {
      confirmDialog.dismiss();
      if (onConfirmed != null) {
        onConfirmed.run();
      }
    });

    confirmBinding.btnConfirmNo.setOnClickListener(v -> confirmDialog.dismiss());

    confirmDialog.show();
  }
}
