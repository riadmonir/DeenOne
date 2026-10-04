package com.devflux.deenone;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.auth.AuthDialogManager;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.devflux.deenone.core.theme.ThemeManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.TypedValue;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.location.LocationProvider;

import com.devflux.deenone.core.notifications.NotificationHelper;
import com.devflux.deenone.core.permissions.PermissionHelper;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.devflux.deenone.databinding.ActivityMainBinding;

import com.devflux.deenone.databinding.BottomSheetSidebarMenuBinding;
import com.devflux.deenone.features.home.HomeViewModel;
import com.devflux.deenone.features.home.adapter.FeatureGridAdapter;
import com.devflux.deenone.features.home.adapter.NextPrayerCarouselAdapter;
import com.devflux.deenone.features.home.model.FeatureItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.PrayerCalculator;
import com.devflux.deenone.core.ui.FullScreenPageDialog;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainActivity extends AppCompatActivity {

  private ActivityMainBinding binding;
  private HomeViewModel viewModel;

  private NextPrayerCarouselAdapter nextPrayerCarouselAdapter;
  private com.devflux.deenone.data.repository.QuranRepository quranRepository;
  private com.devflux.deenone.data.repository.NotificationRepository notificationRepository;
  private com.devflux.deenone.features.amal.AmalViewModel amalViewModel;
  private com.devflux.deenone.data.local.entity.PrayerScheduleEntity cachedPrayerSchedule;
  private String cachedSunrise = "6:04 am";
  private String cachedSunset = "7:12 pm";
  private String cachedNextName = "আসর";
  private String cachedNextTime = "5:18 pm";
  private String cachedNextCountdown = "03:41:40";
  private final Handler carouselAutoSlideHandler = new Handler(Looper.getMainLooper());
  private Runnable carouselAutoSlideRunnable;
  private FullScreenPageDialog activeQuranHubDialog;

  private final ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
      new ActivityResultContracts.RequestMultiplePermissions(),
      result -> {
        NotificationHelper.createNotificationChannels(this);
        requestCurrentGpsLocationUpdate();
        checkAndPromptBatteryOptimization();
      }
  );

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(com.devflux.deenone.core.localization.LocaleManager.wrapContext(newBase));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {
      EdgeToEdge.enable(this);
    } catch (Throwable ignored) {}

    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
      int topPad = systemBars.top + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
      binding.layoutTopHeader.setPadding(
          binding.layoutTopHeader.getPaddingLeft(),
          topPad,
          binding.layoutTopHeader.getPaddingRight(),
          binding.layoutTopHeader.getPaddingBottom()
      );
      int baseNavHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 68, getResources().getDisplayMetrics());
      int topNavPad = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
      int bottomPad = systemBars.bottom + topNavPad;
      ViewGroup.LayoutParams lp = binding.bottomNavContainer.getLayoutParams();
      if (lp != null) {
        lp.height = baseNavHeight + systemBars.bottom;
        binding.bottomNavContainer.setLayoutParams(lp);
      }
      binding.bottomNavContainer.setPadding(
          binding.bottomNavContainer.getPaddingLeft(),
          topNavPad,
          binding.bottomNavContainer.getPaddingRight(),
          bottomPad
      );
      return insets;
    });

    initViewModel();
    setupFeatureRecyclerView();
    setupNextPrayerCarousel();
    setupDailyAyahHadithWidget();
    observeViewModelData();
    setupInteractiveListeners();
    handlePendingPageReopen();

    // Initialize Remote Controlled AdMob System
    try {
      com.devflux.deenone.core.ads.AdManager.getInstance().initialize(this);
      com.devflux.deenone.core.admin.AdminRemoteConfigManager.getInstance(this).fetchRemoteConfigAsync(success -> {
        runOnUiThread(() -> {
          setupRemoteBannerAd();
          com.devflux.deenone.core.admin.ForceUpdateDialog.showIfNeeded(MainActivity.this);
          if (notificationRepository != null) {
            notificationRepository.fetchAndSyncAdminNotifications();
          }
        });
      });
      com.devflux.deenone.core.admin.ForceUpdateDialog.showIfNeeded(this);
      setupRemoteBannerAd();
    } catch (Throwable ignored) {}

    binding.getRoot().post(() -> {
      com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
        try {
          scheduleDailyPrayerAlarms();
          startAutoDataSync();
          com.devflux.deenone.core.jummah.JummahNotificationScheduler.scheduleAllJummahReminders(MainActivity.this);
          com.devflux.deenone.core.eid.EidNotificationScheduler.scheduleEidReminders(MainActivity.this);
          com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(MainActivity.this);
          if (notificationRepository != null) {
            notificationRepository.performMidnightCleanup();
          }
        } catch (Exception e) {
          android.util.Log.e("MainActivity", "Error during startup background tasks: " + e.getMessage(), e);
        }
      });
      try {
        checkAndRequestAppPermissions();
        handleIncomingDeepLink(getIntent());
      } catch (Exception e) {
        android.util.Log.e("MainActivity", "Error handling permissions/deep links: " + e.getMessage(), e);
      }
    });

    try {
      refreshHomeDynamicCards();
      updateThemeToggleIcon(com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(this));
    } catch (Exception e) {
      android.util.Log.e("MainActivity", "Error refreshing dynamic cards: " + e.getMessage(), e);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    startCarouselAutoSlide();
    refreshHomeDynamicCards();
    updateThemeToggleIcon(com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(this));
    com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(this);
    com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(this);
    com.devflux.deenone.widget.WidgetDataHelper.updateAllWidgets(this);
    com.devflux.deenone.core.prayer.QazaCalculatorManager.autoCalculateMissedPrayers(this);
    setupRemoteBannerAd();
  }

  private void setupRemoteBannerAd() {
    if (binding == null || isFinishing() || isDestroyed()) return;
    com.devflux.deenone.core.ads.AdManager.getInstance().loadBanner(this, binding.layoutAdmobBannerContainer);
  }

  @Override
  protected void onPause() {
    super.onPause();
    stopCarouselAutoSlide();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopCarouselAutoSlide();
    if (activeQuranHubDialog != null && activeQuranHubDialog.isShowing()) {
      try {
        activeQuranHubDialog.dismiss();
      } catch (Exception ignored) {}
      activeQuranHubDialog = null;
    }
    binding = null;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (com.devflux.deenone.core.auth.GoogleAuthHelper.handleActivityResult(this, requestCode, resultCode, data)) {
      return;
    }
    if (resultCode == RESULT_OK && data != null && data.getData() != null) {
      com.devflux.deenone.features.profile.FullProfileDialog.handleImagePicked(this, data.getData());
    }
  }

  private void setupNextPrayerCarousel() {
    nextPrayerCarouselAdapter = new NextPrayerCarouselAdapter(new NextPrayerCarouselAdapter.OnCarouselClickListener() {
      @Override
      public void onSunriseSunsetClick() {
        showSalahTrackerSheet();
      }

      @Override
      public void onNextPrayerClick() {
        showSalahTrackerSheet();
      }

      @Override
      public void onQuizHubClick() {
        showQuizSheet();
      }

      @Override
      public void onQuranLearningClick() {
        showQuranHubBottomSheet();
      }
    });

    binding.viewPagerNextPrayerCarousel.setAdapter(nextPrayerCarouselAdapter);
    binding.viewPagerNextPrayerCarousel.setOffscreenPageLimit(3);

    binding.viewPagerNextPrayerCarousel.registerOnPageChangeCallback(new androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
      @Override
      public void onPageSelected(int position) {
        updateCarouselIndicatorDots(position);
      }
    });

    startCarouselAutoSlide();
  }

  private void updateCarouselIndicatorDots(int activeIndex) {
    View[] dots = new View[]{
        binding.indicatorDot0,
        binding.indicatorDot1,
        binding.indicatorDot2,
        binding.indicatorDot3
    };

    for (int i = 0; i < dots.length; i++) {
      if (dots[i] == null) continue;
      ViewGroup.LayoutParams lp = dots[i].getLayoutParams();
      if (i == activeIndex) {
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        dots[i].setLayoutParams(lp);
        dots[i].setBackgroundResource(R.drawable.bg_indicator_pill_active);
      } else {
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
        dots[i].setLayoutParams(lp);
        dots[i].setBackgroundResource(R.drawable.bg_indicator_dot_inactive);
      }
    }
  }

  private void startCarouselAutoSlide() {
    stopCarouselAutoSlide();
    carouselAutoSlideRunnable = new Runnable() {
      @Override
      public void run() {
        if (binding != null) {
          int currentItem = binding.viewPagerNextPrayerCarousel.getCurrentItem();
          int nextItem = (currentItem + 1) % 4;
          binding.viewPagerNextPrayerCarousel.setCurrentItem(nextItem, true);
        }
        carouselAutoSlideHandler.postDelayed(this, 5000);
      }
    };
    carouselAutoSlideHandler.postDelayed(carouselAutoSlideRunnable, 5000);
  }

  private void stopCarouselAutoSlide() {
    if (carouselAutoSlideRunnable != null) {
      carouselAutoSlideHandler.removeCallbacks(carouselAutoSlideRunnable);
    }
  }

  private void setupDailyAyahHadithWidget() {
    com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().getLiveContent().observe(this, content -> {
      if (content != null && binding != null) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
        if (content.ayah != null) {
          binding.tvDailyAyahArabic.setText(content.ayah.arabic != null ? content.ayah.arabic : "");
          binding.tvDailyAyahSurahBadge.setText(content.ayah.getSurahName(isBn));
          binding.tvDailyAyahText.setText(content.ayah.getMeaning(isBn));
          binding.tvDailyAyahReference.setText(content.ayah.getReference(isBn));
        }
        if (content.hadith != null) {
          binding.tvDailyHadithArabic.setText(content.hadith.arabic != null ? content.hadith.arabic : "");
          binding.tvDailyHadithGrade.setText(content.hadith.getCollection(isBn));
          binding.tvDailyHadithText.setText(content.hadith.getMeaning(isBn));
          binding.tvDailyHadithReference.setText(content.hadith.getReference(isBn));
        }
      }
    });

    // Initial resolution / daily midnight rotation
    com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(this);

    // Copy Ayah
    binding.btnCopyAyah.setOnClickListener(v -> {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      String arabic = binding.tvDailyAyahArabic.getText().toString();
      String text = binding.tvDailyAyahText.getText().toString();
      String ref = binding.tvDailyAyahReference.getText().toString();
      copyToClipboard(isBn ? "দৈনিক আয়াত" : "Daily Ayah", arabic + "\n\n" + text + "\n" + ref);
      Toast.makeText(this, isBn ? "কুরআনের আয়াত কপি করা হয়েছে" : "Quran Ayah copied to clipboard", Toast.LENGTH_SHORT).show();
    });

    // Copy Hadith
    binding.btnCopyHadith.setOnClickListener(v -> {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      String arabic = binding.tvDailyHadithArabic.getText().toString();
      String text = binding.tvDailyHadithText.getText().toString();
      String ref = binding.tvDailyHadithReference.getText().toString();
      copyToClipboard(isBn ? "সহীহ হাদিস" : "Sahih Hadith", arabic + "\n\n" + text + "\n" + ref);
      Toast.makeText(this, isBn ? "সহীহ হাদিস কপি করা হয়েছে" : "Hadith copied to clipboard", Toast.LENGTH_SHORT).show();
    });

    // Card Clicks
    binding.cardDailyAyah.setOnClickListener(v -> showAyahOfTheDaySheet());
    binding.cardDailyHadith.setOnClickListener(v -> showHadithOfTheDaySheet());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    handleIncomingDeepLink(intent);
  }

  private void scheduleDailyPrayerAlarms() {
    java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
      try {
        com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(MainActivity.this);
        com.devflux.deenone.core.notifications.DailyIslamicReminderScheduler.scheduleAllDailyReminders(MainActivity.this);
        com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(MainActivity.this);
      } catch (Exception ignored) {}
    });
  }

  private void startAutoDataSync() {
    com.devflux.deenone.core.location.LocationProvider.Coordinates coords = viewModel.getCurrentCoordinates();
    com.devflux.deenone.core.sync.DataSyncManager.getInstance().startAutoSyncOnConnectivity(
        this, coords.latitude, coords.longitude
    );
    com.devflux.deenone.core.sync.SunriseSunsetSyncManager.getInstance().start4HourPeriodicSync(this);
    com.devflux.deenone.features.books.sync.BookSyncScheduler.schedulePeriodicSync(this);
    com.devflux.deenone.features.donation.DonationCacheManager.preloadDonationData(this);
    if (notificationRepository != null) {
      notificationRepository.fetchAndSyncAdminNotifications();
    }
  }

  private void checkAndRequestAppPermissions() {
    if (!PermissionHelper.hasAllPermissions(this)) {
      PermissionHelper.requestAppPermissions(permissionLauncher);
    } else {
      NotificationHelper.createNotificationChannels(this);
      requestCurrentGpsLocationUpdate();
      checkAndPromptBatteryOptimization();
    }
  }

  private void checkAndPromptBatteryOptimization() {
    if (!PermissionHelper.isIgnoringBatteryOptimizations(this)) {
      android.content.SharedPreferences sp = getSharedPreferences("deenone_battery_pref", Context.MODE_PRIVATE);
      boolean alreadyPrompted = sp.getBoolean("battery_opt_prompted", false);
      if (!alreadyPrompted) {
        sp.edit().putBoolean("battery_opt_prompted", true).apply();
        binding.getRoot().postDelayed(this::showBatteryOptimizationPromptDialog, 1200);
      }
    }
  }

  public void showBatteryOptimizationPromptDialog() {
    if (isFinishing() || isDestroyed()) return;
    com.google.android.material.bottomsheet.BottomSheetDialog dialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetBatteryOptimizationBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetBatteryOptimizationBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvBatterySheetBadge.setText(isBn ? "ব্যাকগ্রাউন্ড পারমিশন" : "Background Permission");
    sheetBinding.tvBatterySheetSubtitle.setText(isBn ? "আযান ও অ্যালার্ট পারমিশন" : "Azan & Alert Permission");
    sheetBinding.tvBatterySheetTitle.setText(isBn ? "সঠিক সময়ে আযান ও সালাত অ্যালার্ট নিশ্চিত করুন" : "Ensure Accurate Azan & Prayer Alerts");
    sheetBinding.tvBatterySheetDescription.setText(isBn ? "ফোন লক বা সাইলেন্ট থাকা অবস্থাতেও ওয়াক্তমতো নির্ভুল আযান এবং নামাজের নোটিফিকেশন পেতে DeenOne-এর ব্যাকগ্রাউন্ড অ্যাক্টিভিটি সর্বদা সচল রাখা প্রয়োজন।" : "To receive timely azan and prayer notifications even when your phone is locked or on silent, DeenOne requires background activity permission.");
    sheetBinding.tvBatterySheetBenefit1.setText(isBn ? "✓  লক স্ক্রিনে যথাসময়ে পূর্ণ আযান বেজে উঠবে" : "✓  Full Azan will sound accurately on lock screen");
    sheetBinding.tvBatterySheetBenefit2.setText(isBn ? "✓  ফোন সাইলেন্ট বা স্লিপ মোডে থাকলেও আযান মিস হবে না" : "✓  Never miss prayer times even in sleep or silent mode");
    sheetBinding.tvBatterySheetBenefit3.setText(isBn ? "✓  অটো-স্টার্ট ও ব্যাকগ্রাউন্ড সিঙ্ক চালু থাকবে" : "✓  Auto-start & background prayer sync will remain active");
    sheetBinding.btnBatterySheetLater.setText(isBn ? "পরে করবো" : "Maybe Later");
    sheetBinding.btnBatterySheetAllow.setText(isBn ? "অনুমতি দিন" : "Allow Permission");

    sheetBinding.btnBatterySheetClose.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnBatterySheetLater.setOnClickListener(v -> dialog.dismiss());

    sheetBinding.btnBatterySheetAllow.setOnClickListener(v -> {
      dialog.dismiss();
      PermissionHelper.requestIgnoreBatteryOptimizations(MainActivity.this);
    });

    dialog.show();
  }

  private void requestCurrentGpsLocationUpdate() {
    String mode = LocationProvider.getLocationMode(this);
    if ("AUTO_GPS".equals(mode)) {
      com.devflux.deenone.core.location.AutoLocationUpdateManager.getInstance().startAutoLocationUpdates(this);
    } else {
      LocationProvider.Coordinates saved = LocationProvider.getSavedOrCurrentLocation(this);
      if (viewModel != null) {
        viewModel.setLocation(saved);
      }
      String locName = LocationProvider.formatCityNameOnly(saved.locationName);
      binding.tvLocationName.setText(locName);
      if (binding.tvPrayerScheduleLocation != null) {
        binding.tvPrayerScheduleLocation.setVisibility(View.GONE);
      }
    }
    com.devflux.deenone.core.location.AutoLocationUpdateManager.getInstance().getLiveCoordinates().observe(this, coordinates -> {
      if (coordinates != null) {
        String currentMode = LocationProvider.getLocationMode(this);
        if ("AUTO_GPS".equals(currentMode)) {
          if (viewModel != null) {
            viewModel.setLocation(coordinates);
          }
          String locName = LocationProvider.formatCityNameOnly(coordinates.locationName);
          binding.tvLocationName.setText(locName);
          if (binding.tvPrayerScheduleLocation != null) {
            binding.tvPrayerScheduleLocation.setVisibility(View.GONE);
          }
        }
      }
    });
  }

  private void initViewModel() {
    viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    amalViewModel = new ViewModelProvider(this).get(com.devflux.deenone.features.amal.AmalViewModel.class);
    quranRepository = new com.devflux.deenone.data.repository.QuranRepository(this);
    notificationRepository = new com.devflux.deenone.data.repository.NotificationRepository(this);
  }

  private void setupFeatureRecyclerView() {
    FeatureGridAdapter featureAdapter = new FeatureGridAdapter(new ArrayList<>(), this::handleFeatureClick);
    binding.rvFeatureGrid.setLayoutManager(new GridLayoutManager(this, 3));
    binding.rvFeatureGrid.setAdapter(featureAdapter);
  }

  private void observeViewModelData() {
    // 1. Real-time Hijri & Gregorian Dates
    viewModel.getHijriDate().observe(this, binding.tvHijriDate::setText);

    viewModel.getGregorianDate().observe(this, binding.tvGregorianDate::setText);

    binding.tvHijriDate.setOnClickListener(v -> showHijriAdjustmentSheet());
    binding.tvGregorianDate.setOnClickListener(v -> showIslamicCalendarSheet());

    // Observe Unread Notification Count
    notificationRepository.getUnreadCount().observe(this, count -> {
      boolean hasUnread = count != null && count > 0;
      binding.badgeNotificationDot.setVisibility(hasUnread ? View.VISIBLE : View.GONE);
    });

    // Observe Real-Time Quran Last Read (Home Screen Card)
    quranRepository.getLastReadSurah().observe(this, lastRead -> {
      if (lastRead != null && binding != null) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
        int ayah = lastRead.getReadingProgressAyah() > 0 ? lastRead.getReadingProgressAyah() : 1;
        binding.tvQuranLastReadArabicName.setText(lastRead.getNameArabic());
        if (isBn) {
          binding.tvQuranLastReadSurahTitle.setText(String.format(java.util.Locale.US, "%s • আয়াত %s", lastRead.getNameBengali(), BengaliNumberUtil.toBengali(ayah)));
          binding.tvQuranLastReadSurahMeta.setText(String.format(java.util.Locale.US, "পারা %s • %s আয়াত", BengaliNumberUtil.toBengali(lastRead.getJuzNumber()), BengaliNumberUtil.toBengali(lastRead.getNumberOfAyahs())));
        } else {
          String sNameEn = (lastRead.getNameEnglish() != null && !lastRead.getNameEnglish().isEmpty()) ? lastRead.getNameEnglish() : "Surah Al-Fatiha";
          binding.tvQuranLastReadSurahTitle.setText(String.format(java.util.Locale.US, "%s • Verse %d", sNameEn, ayah));
          binding.tvQuranLastReadSurahMeta.setText(String.format(java.util.Locale.US, "Para %d • %d Verses", lastRead.getJuzNumber(), lastRead.getNumberOfAyahs()));
        }
      }
    });

    // Observe Daily Goals / Amal Progress (Home Screen Card)
    amalViewModel.getTodayCompletedCount().observe(this, completed -> {
      if (binding == null) return;
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      int comp = (completed != null) ? completed : 0;
      Integer total = amalViewModel.getTodayTotalCount().getValue();
      int tot = (total != null && total > 0) ? total : 5;
      int percent = Math.min(100, (comp * 100) / tot);
      if (isBn) {
        binding.tvGoalsCompletedRatio.setText("আজকের অর্জিত লক্ষ্য: " + BengaliNumberUtil.toBengali(comp) + " / " + BengaliNumberUtil.toBengali(tot));
        binding.tvGoalsPercentage.setText(BengaliNumberUtil.toBengali(percent) + "%");
      } else {
        binding.tvGoalsCompletedRatio.setText("Today's Achieved Goals: " + comp + " / " + tot);
        binding.tvGoalsPercentage.setText(percent + "%");
      }
      binding.pbIslamicGoalsProgress.setProgress(percent);
    });

    amalViewModel.getTodayTotalCount().observe(this, total -> {
      if (binding == null) return;
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      int tot = (total != null && total > 0) ? total : 5;
      Integer compVal = amalViewModel.getTodayCompletedCount().getValue();
      int comp = (compVal != null) ? compVal : 0;
      int percent = Math.min(100, (comp * 100) / tot);
      if (isBn) {
        binding.tvGoalsCompletedRatio.setText("আজকের অর্জিত লক্ষ্য: " + BengaliNumberUtil.toBengali(comp) + " / " + BengaliNumberUtil.toBengali(tot));
        binding.tvGoalsPercentage.setText(BengaliNumberUtil.toBengali(percent) + "%");
      } else {
        binding.tvGoalsCompletedRatio.setText("Today's Achieved Goals: " + comp + " / " + tot);
        binding.tvGoalsPercentage.setText(percent + "%");
      }
      binding.pbIslamicGoalsProgress.setProgress(percent);
    });

    // 2. Feature Grid items
    viewModel.getFeatureList().observe(this, items -> {
      if (items != null) {
        binding.rvFeatureGrid.setAdapter(new FeatureGridAdapter(items, this::handleFeatureClick));
      }
    });

    // 3. Real Prayer Schedule
    viewModel.getPrayerSchedule().observe(this, schedule -> {
      if (schedule != null) {
        cachedPrayerSchedule = schedule;
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
        String loc = LocationProvider.formatCityNameOnly(schedule.getLocation(), isBn);
        binding.tvLocationName.setText(loc);
        if (binding.tvPrayerScheduleLocation != null) {
          binding.tvPrayerScheduleLocation.setVisibility(View.GONE);
        }
        updatePrayerScheduleStripUI(schedule, isBn);
        binding.tvSehriTime.setText(formatTimeWithLanguage(schedule.getSehri(), isBn));
        binding.tvIftarTime.setText(formatTimeWithLanguage(schedule.getIftar(), isBn));
      }
    });

    // 4. Real User Activity Tracking
    viewModel.getAmalRecord().observe(this, record -> {
      if (record != null) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
        if (isBn) {
          binding.tvAmalPrayerRatio.setText(BengaliNumberUtil.toBengali(record.getCompletedPrayersCount()) + "/৫");
          binding.tvAmalPointsEarned.setText(BengaliNumberUtil.toBengali(record.getPointsEarned()));
          binding.tvOverallAmalPercentage.setText(BengaliNumberUtil.toBengali(String.format(java.util.Locale.US, "%.0f", record.getOverallPercentage())) + "%");
          binding.tvAmalCount.setText(BengaliNumberUtil.toBengali(record.getCompletedGoalsCount()));
        } else {
          binding.tvAmalPrayerRatio.setText(record.getCompletedPrayersCount() + "/5");
          binding.tvAmalPointsEarned.setText(String.valueOf(record.getPointsEarned()));
          binding.tvOverallAmalPercentage.setText(String.format(java.util.Locale.US, "%.0f", record.getOverallPercentage()) + "%");
          binding.tvAmalCount.setText(String.valueOf(record.getCompletedGoalsCount()));
        }
      }
    });

    // Real Quizzes Played Tracking (Daily Count)
    String todayDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date());
    com.devflux.deenone.data.local.AppDatabase.getInstance(this).quizDao().getDailyQuizzesPlayedCount(todayDateStr).observe(this, quizCount -> {
      int count = (quizCount != null && quizCount > 0) ? quizCount : com.devflux.deenone.core.quiz.QuizManager.getInstance().getDailyQuizzesPlayedCount(this);
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      binding.tvQuizCount.setText(isBn ? BengaliNumberUtil.toBengali(count) : String.valueOf(count));
    });

    // 5. Real-Time Dynamic Waqt & Forbidden Times Dual Widget
    viewModel.getDynamicPrayerWidget().observe(this, state -> {
      if (state == null) return;

      int borderCardColor = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.border_card);
      boolean isLightMode = com.devflux.deenone.core.theme.ThemeManager.isLightMode(MainActivity.this);
      long curTime = System.currentTimeMillis();

      long sRise = state.sunriseMillis;
      long sSet = state.sunsetMillis;
      if (sRise <= 0) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 6);
        cal.set(java.util.Calendar.MINUTE, 5);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        sRise = cal.getTimeInMillis();
      }
      if (sSet <= 0) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 18);
        cal.set(java.util.Calendar.MINUTE, 15);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        sSet = cal.getTimeInMillis();
      }
      boolean isDaytime = (curTime >= sRise && curTime < sSet);

      if (state.isForbidden) {
        // Show Forbidden Widget, Hide Current Waqt Widget
        binding.cardForbiddenWaqtWidget.setVisibility(View.VISIBLE);
        binding.cardCurrentWaqtWidget.setVisibility(View.GONE);

        binding.tvForbiddenTitle.setText(state.forbiddenTitle);
        binding.tvForbiddenRange.setText(state.forbiddenRange);
        binding.tvForbiddenRemainingTime.setText(state.forbiddenRemainingText);
        binding.progressForbiddenWaqt.setProgress(state.forbiddenProgress);

        binding.cardForbiddenWaqtWidget.setStrokeColor(borderCardColor);

        if (isLightMode && isDaytime) {
          binding.tvForbiddenTitle.setTextColor(Color.parseColor("#0F172A"));
          binding.tvForbiddenRemainingTime.setTextColor(Color.parseColor("#475569"));
        } else {
          binding.tvForbiddenTitle.setTextColor(Color.parseColor("#FFFFFF"));
          binding.tvForbiddenRemainingTime.setTextColor(isLightMode ? Color.parseColor("#CBD5E1") : Color.parseColor("#94A3B8"));
        }

        if (binding.skyForbiddenBackground != null) {
          binding.skyForbiddenBackground.setPrayerTimes(
              state.fajrMillis,
              state.sunriseMillis,
              state.zohrMillis,
              state.asrMillis,
              state.sunsetMillis,
              state.ishaMillis
          );
        }
      } else {
        // Show Current Waqt Widget, Hide Forbidden Widget
        binding.cardForbiddenWaqtWidget.setVisibility(View.GONE);
        binding.cardCurrentWaqtWidget.setVisibility(View.VISIBLE);

        binding.tvCurrentWaqtName.setText(state.waqtName);
        binding.tvCurrentWaqtRange.setText(state.waqtRange);
        binding.tvWaqtRemainingTime.setText(state.waqtRemainingText);
        binding.progressCurrentWaqt.setProgress(state.waqtProgress);

        binding.cardCurrentWaqtWidget.setStrokeColor(borderCardColor);

        if (isLightMode && isDaytime) {
          binding.tvCurrentWaqtName.setTextColor(Color.parseColor("#0F172A"));
          binding.tvWaqtRemainingTime.setTextColor(Color.parseColor("#475569"));
          binding.tvCurrentWaqtRange.setTextColor(Color.parseColor("#0D9488"));
          binding.tvCurrentWaqtHeader.setTextColor(Color.parseColor("#0D9488"));
        } else {
          binding.tvCurrentWaqtName.setTextColor(Color.parseColor("#FFFFFF"));
          binding.tvWaqtRemainingTime.setTextColor(isLightMode ? Color.parseColor("#CBD5E1") : Color.parseColor("#94A3B8"));
          binding.tvCurrentWaqtRange.setTextColor(Color.parseColor("#34D399"));
          binding.tvCurrentWaqtHeader.setTextColor(Color.parseColor("#34D399"));
        }

        if (binding.skyPrayerBackground != null) {
          binding.skyPrayerBackground.setPrayerTimes(
              state.fajrMillis,
              state.sunriseMillis,
              state.zohrMillis,
              state.asrMillis,
              state.sunsetMillis,
              state.ishaMillis
          );
        }
      }
    });

    // Sunrise & Sunset Carousel Updates
    viewModel.getSunriseTime().observe(this, sunrise -> {
      cachedSunrise = sunrise;
      if (nextPrayerCarouselAdapter != null) {
        nextPrayerCarouselAdapter.updateSunriseSunset(cachedSunrise, cachedSunset);
      }
    });

    viewModel.getSunsetTime().observe(this, sunset -> {
      cachedSunset = sunset;
      if (nextPrayerCarouselAdapter != null) {
        nextPrayerCarouselAdapter.updateSunriseSunset(cachedSunrise, cachedSunset);
      }
    });

    // Next Prayer Carousel Updates
    viewModel.getNextPrayerName().observe(this, name -> {
      cachedNextName = name;
      if (nextPrayerCarouselAdapter != null) {
        nextPrayerCarouselAdapter.updateNextPrayer(cachedNextName, cachedNextTime, cachedNextCountdown);
      }
    });

    viewModel.getNextPrayerTime().observe(this, time -> {
      cachedNextTime = time;
      if (nextPrayerCarouselAdapter != null) {
        nextPrayerCarouselAdapter.updateNextPrayer(cachedNextName, cachedNextTime, cachedNextCountdown);
      }
    });

    viewModel.getNextPrayerCountdown().observe(this, countdown -> {
      cachedNextCountdown = countdown;
      if (nextPrayerCarouselAdapter != null) {
        nextPrayerCarouselAdapter.updateNextPrayer(cachedNextName, cachedNextTime, cachedNextCountdown);
      }
    });

    viewModel.getFastingCountdownTitle().observe(this, title -> {
      if (title != null && binding.tvFastingCountdownTitle != null) {
        binding.tvFastingCountdownTitle.setText(title);
      }
    });

    viewModel.getFastingCountdownValue().observe(this, countdown -> {
      if (countdown != null && binding.tvIftarCountdown != null) {
        binding.tvIftarCountdown.setText(countdown);
      }
    });

    // 6. Live Notification History Unread Count Badge
    notificationRepository.getUnreadCount().observe(this, count -> {
      boolean hasUnread = count != null && count > 0;
      binding.badgeNotificationDot.setVisibility(hasUnread ? View.VISIBLE : View.GONE);
    });
  }

  private void updatePrayerScheduleStripUI(com.devflux.deenone.data.local.entity.PrayerScheduleEntity schedule, boolean isBn) {
    if (schedule == null || binding == null) return;
    binding.tvWaqtFajr.setText(extractPrayerTimeDigits(schedule.getFajr(), isBn));
    binding.tvWaqtZohr.setText(extractPrayerTimeDigits(schedule.getZohr(), isBn));
    binding.tvWaqtAsr.setText(extractPrayerTimeDigits(schedule.getAsr(), isBn));
    binding.tvWaqtMaghrib.setText(extractPrayerTimeDigits(schedule.getMaghrib(), isBn));
    binding.tvWaqtIsha.setText(extractPrayerTimeDigits(schedule.getIsha(), isBn));

    if (binding.tvWaqtFajrPeriod != null) binding.tvWaqtFajrPeriod.setText(extractPrayerPeriod(schedule.getFajr()));
    if (binding.tvWaqtZohrPeriod != null) binding.tvWaqtZohrPeriod.setText(extractPrayerPeriod(schedule.getZohr()));
    if (binding.tvWaqtAsrPeriod != null) binding.tvWaqtAsrPeriod.setText(extractPrayerPeriod(schedule.getAsr()));
    if (binding.tvWaqtMaghribPeriod != null) binding.tvWaqtMaghribPeriod.setText(extractPrayerPeriod(schedule.getMaghrib()));
    if (binding.tvWaqtIshaPeriod != null) binding.tvWaqtIshaPeriod.setText(extractPrayerPeriod(schedule.getIsha()));
  }

  private static String extractPrayerTimeDigits(String rawTime, boolean isBn) {
    if (rawTime == null || rawTime.isEmpty()) return "";
    String digits = rawTime.replaceAll("(?i)\\s*(am|pm)", "").trim();
    if (digits.startsWith("0") && digits.length() > 2 && digits.charAt(2) == ':') {
      digits = digits.substring(1);
    }
    return isBn ? BengaliNumberUtil.toBengali(digits) : digits;
  }

  private static String extractPrayerPeriod(String rawTime) {
    if (rawTime == null || rawTime.isEmpty()) return "AM";
    return rawTime.toLowerCase(java.util.Locale.US).contains("pm") ? "PM" : "AM";
  }

  private static String formatTimeWithLanguage(String rawTime, boolean isBn) {
    if (rawTime == null || rawTime.isEmpty()) return "";
    if (isBn) {
      String timeDigits = rawTime.replaceAll("(?i)\\s*(am|pm)", "").trim();
      String period = rawTime.toLowerCase(java.util.Locale.US).contains("pm") ? " PM" : " AM";
      return BengaliNumberUtil.toBengali(timeDigits) + period;
    }
    return rawTime;
  }

  private void setupInteractiveListeners() {
    // Dynamic Prayer Widget Clicks & Touch Springs
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnForbiddenDetails);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.pillLocation);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tvLocationName);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnFullProfile);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnNotification);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnMenu);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCopyAyah);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCopyHadith);

    binding.cardForbiddenWaqtWidget.setOnClickListener(v -> showForbiddenTimesSheet());
    binding.btnForbiddenDetails.setOnClickListener(v -> showForbiddenTimesSheet());
    binding.cardCurrentWaqtWidget.setOnClickListener(v -> showSalahTrackerSheet());
    binding.cardNextPrayerCarousel.setOnClickListener(v -> showSalahTrackerSheet());

    // Ramadan / Sehri-Iftar Card Click
    binding.cardSehriIftar.setOnClickListener(v -> showRamadanSheet());

    // Location Selector
    binding.pillLocation.setOnClickListener(v -> showLocationPickerSheet());
    binding.tvLocationName.setOnClickListener(v -> showLocationPickerSheet());

    // Full Profile Button
    binding.btnFullProfile.setOnClickListener(v -> showUserProfileSheet());

    // Muhasabah (আত্মসমালোচনা)
    binding.cardMuhasabah.setOnClickListener(v -> showAmalTrackerSheet());

    // Fajr Call (ফজরের ডাক)
    binding.cardFajrCall.setOnClickListener(v -> showPrayerAlarmConfigSheet("fajr"));

    // Quran Sleep Mode (কুরআন স্লিপ মোড)
    binding.cardQuranSleep.setOnClickListener(v -> showSleepModeSheet());

    // Top bar buttons
    binding.btnThemeToggle.setOnClickListener(v -> toggleAppTheme());

    binding.btnNotification.setOnClickListener(v -> showNotificationHistorySheet());

    binding.btnMenu.setOnClickListener(v -> showSidebarMenuSheet());

    // Bottom Navigation Tabs & Touch Feedback
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabHome);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabSalat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabAmal);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabRank);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabCommunity);
    binding.tabHome.setOnClickListener(v -> binding.nestedScrollView.smoothScrollTo(0, 0));
    binding.tabSalat.setOnClickListener(v -> showSalahTrackerSheet());
    binding.tabAmal.setOnClickListener(v -> showAmalTrackerSheet());
    binding.tabRank.setOnClickListener(v -> com.devflux.deenone.features.leaderboard.LeaderboardRankPageDialog.show(this));
    binding.tabCommunity.setOnClickListener(v -> com.devflux.deenone.features.community.CommunityFeedDialog.show(this));

    // Pre-cache community posts & donation data in background on app startup (even after clear data or offline)
    com.devflux.deenone.features.community.data.CommunityRepository.getInstance(this).syncFromRemote(false, null);
    com.devflux.deenone.features.donation.DonationCacheManager.preloadDonationData(this);
    com.devflux.deenone.core.network.NetworkConnectivityHelper.getNetworkStatusLiveData(this).observe(this, isOnline -> {
      if (Boolean.TRUE.equals(isOnline)) {
        com.devflux.deenone.features.community.data.CommunityRepository.getInstance(this).syncFromRemote(false, null);
        com.devflux.deenone.features.donation.DonationCacheManager.preloadDonationData(this);
      }
    });

    // 1. Quran Last Read Card
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnResumeQuranReading);
    binding.cardQuranLastRead.setOnClickListener(v -> resumeLastReadQuran());
    binding.btnResumeQuranReading.setOnClickListener(v -> resumeLastReadQuran());

    // 2. Daily Islamic Goals Card
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tvOpenGoalsTracker);
    binding.cardIslamicGoalsProgress.setOnClickListener(v -> showAmalTrackerSheet());
    binding.tvOpenGoalsTracker.setOnClickListener(v -> showAmalTrackerSheet());



    // 3. Daily Selected Dua Card
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnMarkDuaRead);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tvDailyDuaViewAll);
    final boolean[] isDuaRead = {true};
    binding.btnMarkDuaRead.setOnClickListener(v -> {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      isDuaRead[0] = !isDuaRead[0];
      if (isDuaRead[0]) {
        binding.btnMarkDuaRead.setText(isBn ? "পঠিত " : "Read ");
        binding.btnMarkDuaRead.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(R.color.accent_mint)));
        binding.btnMarkDuaRead.setTextColor(android.graphics.Color.parseColor("#0B131E"));
        Toast.makeText(this, isBn ? "দোয়াটি পঠিত হিসেবে চিহ্নিত করা হয়েছে" : "Dua marked as read", Toast.LENGTH_SHORT).show();
      } else {
        binding.btnMarkDuaRead.setText(isBn ? "পঠিত চিহ্নিত করুন" : "Mark as Read");
        binding.btnMarkDuaRead.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#144A3A")));
        binding.btnMarkDuaRead.setTextColor(getColor(R.color.text_secondary));
      }
    });
    binding.tvDailyDuaViewAll.setOnClickListener(v -> showDuaSheet());
    binding.cardDailySelectedDua.setOnClickListener(v -> showDuaSheet());

    // 4. Nearby Mosque Card
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tvAllMosquesLink);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnNavigateNearbyMosque);
    binding.cardNearbyMosqueHome.setOnClickListener(v -> showMosqueFinderSheet());
    binding.tvAllMosquesLink.setOnClickListener(v -> showMosqueFinderSheet());
    binding.btnNavigateNearbyMosque.setOnClickListener(v -> {
      try {
        android.net.Uri gmmIntentUri = android.net.Uri.parse("geo:0,0?q=mosque");
        android.content.Intent mapIntent = new android.content.Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
          startActivity(mapIntent);
        } else {
          startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://www.google.com/maps/search/mosque/")));
        }
      } catch (Exception e) {
        showMosqueFinderSheet();
      }
    });

    // 5. Hajj & Umrah Quick Guide Card
    binding.cardHajjUmrahQuick.setOnClickListener(v -> showHajjGuideSheet());



    // 7. Halal Food & Nutrition Guide Card
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tvHalalViewAll);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.pillEcodeChecker);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.pillHalalZabiha);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.pillHalalRestaurants);

    binding.cardHalalFoodNutrition.setOnClickListener(v -> showHalalFoodsSheet());
    binding.tvHalalViewAll.setOnClickListener(v -> showHalalFoodsSheet());
    binding.cardFeaturedSunnahFood.setOnClickListener(v -> showHalalFoodsSheet("SUNNAH_FOOD"));
    binding.pillEcodeChecker.setOnClickListener(v -> showHalalFoodsSheet("E_CODE"));
    binding.pillHalalZabiha.setOnClickListener(v -> showHalalFoodsSheet("PRINCIPLES"));
    binding.pillHalalRestaurants.setOnClickListener(v -> showHalalFoodsSheet());

  }

  private void resumeLastReadQuran() {
    android.content.SharedPreferences quranPrefs = getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
    int lastSurahNum = quranPrefs.getInt("last_surah_number", 1);
    int lastAyahNum = quranPrefs.getInt("last_ayah_number", 1);

    com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
      com.devflux.deenone.data.local.entity.QuranSurahEntity targetSurah = quranRepository.getLastReadSurahSync();
      int finalAyah = lastAyahNum;
      if (targetSurah != null) {
        if (targetSurah.getReadingProgressAyah() > 0) {
          finalAyah = targetSurah.getReadingProgressAyah();
        }
      } else {
        targetSurah = quranRepository.getSurahByNumberSync(lastSurahNum);
        if (targetSurah == null) {
          targetSurah = quranRepository.getSurahByNumberSync(1);
        }
      }
      final com.devflux.deenone.data.local.entity.QuranSurahEntity finalSurah = targetSurah;
      final int targetAyah = finalAyah;
      runOnUiThread(() -> {
        if (activeQuranHubDialog == null || !activeQuranHubDialog.isShowing()) {
          showQuranHubBottomSheet();
        }
        if (finalSurah != null) {
          showSurahReaderBottomSheet(finalSurah, targetAyah);
        }
      });
    });
  }

  private void refreshHomeDynamicCards() {
    if (binding == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // 0. Update all Home static and card dual-language labels
    updateHomeDualLanguageTexts(isBn);

    // 1. Quran Last Read Card (Dynamic Real-time Data)
    android.content.SharedPreferences quranPrefs = getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
    String lastSurahNameBn = quranPrefs.getString("last_surah_name_bn", "সূরা আল-ফাতিহা");
    String lastSurahNameEn = quranPrefs.getString("last_surah_name_en", "Surah Al-Fatiha");
    String lastArabicName = quranPrefs.getString("last_surah_name_ar", "الفاتحة");
    int lastAyah = quranPrefs.getInt("last_ayah_number", 1);
    int totalAyahs = quranPrefs.getInt("last_total_ayahs", 7);
    int lastPara = quranPrefs.getInt("last_para_number", 1);

    binding.tvQuranLastReadArabicName.setText(lastArabicName);
    if (isBn) {
      binding.tvQuranLastReadSurahTitle.setText(lastSurahNameBn + " • আয়াত " + BengaliNumberUtil.toBengali(lastAyah));
      binding.tvQuranLastReadSurahMeta.setText("পারা " + BengaliNumberUtil.toBengali(lastPara) + " • " + BengaliNumberUtil.toBengali(totalAyahs) + " আয়াত");
    } else {
      binding.tvQuranLastReadSurahTitle.setText(lastSurahNameEn + " • Verse " + lastAyah);
      binding.tvQuranLastReadSurahMeta.setText("Para " + lastPara + " • " + totalAyahs + " Verses");
    }

    // 2. Daily Islamic Goals & Progress Card (Dynamic Room Calculation)
    int completedGoals = 0;
    int totalGoals = 5;
    if (amalViewModel != null && amalViewModel.getTodayCompletedCount().getValue() != null) {
      completedGoals = amalViewModel.getTodayCompletedCount().getValue();
    }
    if (amalViewModel != null && amalViewModel.getTodayTotalCount().getValue() != null && amalViewModel.getTodayTotalCount().getValue() > 0) {
      totalGoals = amalViewModel.getTodayTotalCount().getValue();
    }
    int goalPercent = (completedGoals * 100) / totalGoals;

    if (isBn) {
      binding.tvGoalsCompletedRatio.setText("আজকের অর্জিত লক্ষ্য: " + BengaliNumberUtil.toBengali(completedGoals) + " / " + BengaliNumberUtil.toBengali(totalGoals));
      binding.tvGoalsPercentage.setText(BengaliNumberUtil.toBengali(goalPercent) + "%");
    } else {
      binding.tvGoalsCompletedRatio.setText("Today's Achieved Goals: " + completedGoals + " / " + totalGoals);
      binding.tvGoalsPercentage.setText(goalPercent + "%");
    }
    binding.pbIslamicGoalsProgress.setProgress(goalPercent);

    // 3. Daily Selected Dua Card (Dynamic Calendar Rotation)
    java.util.Calendar cal = java.util.Calendar.getInstance();
    int dayOfYear = cal.get(java.util.Calendar.DAY_OF_YEAR);

    String[][] dailyDuas = {
        {"দুনিয়া ও আখিরাতে কল্যাণের দোয়া", "Dua for Good in This World & Hereafter", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ", "হে আমাদের পালনকর্তা! আমাদেরকে দুনিয়াতে কল্যাণ দিন এবং আখিরাতেও কল্যাণ দান করুন এবং আমাদেরকে জাহান্নামের আযাব থেকে রক্ষা করুন।", "“Our Lord, give us in this world that which is good and in the Hereafter that which is good and save us from the torment of the Fire.”", "সূরা আল-বাকারা: ২০১ - সহীহ বুখারী: ৪৫২২", "Surah Al-Baqarah: 201 - Sahih Bukhari: 4522"},
        {"জ্ঞান ও প্রজ্ঞা বৃদ্ধির দোয়া", "Dua for Increase in Knowledge", "رَبِّ زِدْنِي عِلْمًا", "হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন।", "“My Lord, increase me in knowledge.”", "সূরা ত্বাহা: ১১৪", "Surah Taha: 114"},
        {"পিতা-মাতার জন্য রহমতের দোয়া", "Dua for Parents' Mercy", "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا", "হে আমার রব! তাদের উভয়ের প্রতি দয়া করুন, যেমন তারা আমাকে শৈশবে লালন-পালন করেছেন।", "“My Lord, have mercy upon them as they brought me up when I was small.”", "সূরা আল-ইসরা: ২৪", "Surah Al-Isra: 24"},
        {"বিপদ ও দুশ্চিন্তা মুক্তির দোয়া - দোয়ায়ে ইউনুস", "Dua of Yunus for Distress & Calamity", "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ", "তুমি ছাড়া কোনো সত্য উপাস্য নেই, তুমি পবিত্র মহান; নিশ্চয় আমি জালিমদের অন্তর্ভুক্ত ছিলাম।", "“There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.”", "সূরা আল-আম্বিয়া: ৮৭", "Surah Al-Anbiya: 87"},
        {"ঈমান ও অন্তরের অবিচলতার দোয়া", "Dua for Steadfastness in Faith", "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَىٰ دِينِكَ", "হে অন্তরসমূহের পরিবর্তনকারী! আমার অন্তরকে আপনার দ্বীনের উপর দৃঢ় রাখুন।", "“O Controller of the hearts, make my heart steadfast in Your religion.”", "জামে তিরমিযী: ৩৫২২", "Jami` at-Tirmidhi: 3522"},
        {"গুনাহ মাফ ও হেদায়েতের দোয়া", "Dua for Forgiveness and Steadfastness", "رَبَّنَا اغْفِرْ لَنَا ذُنُوبَنَا وَإِسْرَافَنَا فِي أَمْرِنَا وَثَبِّتْ أَقْدَامَنَا", "হে আমাদের রব! আমাদের পাপসমূহ এবং আমাদের কাজের বাড়াবাড়ি ক্ষমা করুন এবং আমাদের পদযুগলকে দৃঢ় রাখুন।", "“Our Lord, forgive us our sins and the excess in our affairs and plant firmly our feet.”", "সূরা আলে ইমরান: ১৪৭", "Surah Ali 'Imran: 147"},
        {"উত্তম জীবিকা ও কল্যাণের দোয়া", "Dua for Goodness and Provision", "رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ", "হে আমার রব! আপনি আমার প্রতি যে অনুগ্রহই নাজিল করবেন, নিশ্চয় আমি তার মুখাপেক্ষী।", "“My Lord, truly I am in need of whatever good that You bestow on me.”", "সূরা আল-কাসাস: ২৪", "Surah Al-Qasas: 24"}
    };
    int selectedIndex = dayOfYear % dailyDuas.length;
    String[] selectedDua = dailyDuas[selectedIndex];

    binding.tvDailyDuaTitle.setText(isBn ? selectedDua[0] : selectedDua[1]);
    binding.tvDailyDuaArabic.setText(selectedDua[2]);
    binding.tvDailyDuaBengali.setText(isBn ? selectedDua[3] : selectedDua[4]);
    binding.tvDailyDuaReference.setText(isBn ? ("রেফারেন্স: " + selectedDua[5]) : ("Reference: " + selectedDua[6]));

    android.content.SharedPreferences duaPrefs = getSharedPreferences("daily_dua_prefs", Context.MODE_PRIVATE);
    boolean isDuaReadToday = duaPrefs.getBoolean("dua_read_" + dayOfYear, false);
    if (isDuaReadToday) {
      binding.btnMarkDuaRead.setText(isBn ? "পঠিত " : "Read ");
      binding.btnMarkDuaRead.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(R.color.accent_mint)));
      binding.btnMarkDuaRead.setTextColor(android.graphics.Color.parseColor("#0B131E"));
    } else {
      binding.btnMarkDuaRead.setText(isBn ? "পঠিত চিহ্নিত করুন" : "Mark as Read");
      binding.btnMarkDuaRead.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#144A3A")));
      binding.btnMarkDuaRead.setTextColor(getColor(R.color.text_secondary));
    }

    // 4. Nearby Mosque Real-time Information based on Location
    binding.tvNearbyMosqueName.setText(isBn ? "নিকটবর্তী মসজিদ অনুসন্ধান করুন" : "Search Nearby Mosques");
    binding.tvNearbyMosqueAddress.setText(isBn ? "আপনার অবস্থান অনুযায়ী অনুসন্ধান করতে ট্যাপ করুন" : "Tap to find mosques near your live location");
    binding.tvNearbyMosqueWalkBadge.setText(isBn ? "লাইভ জিপিএস ও ম্যাপ ডিরেকশন" : "Live GPS & Map Directions");

    com.devflux.deenone.core.location.LocationProvider.Coordinates coords =
        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(this);
    if (coords.latitude != 0.0 && coords.longitude != 0.0) {
      com.devflux.deenone.data.repository.MosqueRepository.getInstance()
          .fetchLiveNearbyMosquesAsync(coords.latitude, coords.longitude, "all", "", new com.devflux.deenone.data.repository.MosqueRepository.OnMosquesLoadedCallback() {
            @Override
            public void onLoaded(java.util.List<com.devflux.deenone.features.mosque.model.MosqueItem> mosques) {
              if (binding == null || mosques == null || mosques.isEmpty()) return;
              com.devflux.deenone.features.mosque.model.MosqueItem closest = mosques.get(0);
              String mName = closest.getName() != null && !closest.getName().isEmpty() ? closest.getName() : (isBn ? "নিকটবর্তী মসজিদ" : "Nearby Mosque");
              binding.tvNearbyMosqueName.setText(mName);
              binding.tvNearbyMosqueAddress.setText(closest.getAddress() + " • " + closest.getDistanceFormatted());
              binding.tvNearbyMosqueWalkBadge.setText(closest.getWalkingTimeFormatted());
            }
          });
    }

    // 5. Halal Sunnah Food Rotated Daily
    int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
    String[][] sunnahFoods = {
        {"কালোজিরা", "Black Seed", "মৃত্যু ব্যতীত সকল রোগের উপশম - সহীহ বুখারী: ৫৬৮৭", "Healing for every disease except death - Sahih Bukhari: 5687"},
        {"খাঁটি মধু", "Pure Honey", "মানবজাতির জন্য শেফা ও রোগ নিরাময় - সূরা আন-নাহল: ৬৯", "Healing for humankind - Surah An-Nahl: 69"},
        {"আজওয়া খেজুর", "Ajwa Dates", "বিষ ও ক্ষতিকর প্রভাব থেকে নিরাপত্তা - সহীহ বুখারী: ৫৪৪৫", "Protection from poison and harm - Sahih Bukhari: 5445"},
        {"জয়তুনের তেল", "Olive Oil", "বরকতময় বৃক্ষের খাঁটি নির্যাস - তিরমিজি: ১৮৫১", "Pure extract from a blessed tree - Jami` at-Tirmidhi: 1851"},
        {"ডালিম বা বেদানা", "Pomegranate", "জান্নাতুল ফেরদৌসের সুস্বাদু ফল - সূরা আর-রহমান: ৬৮", "Delicious fruit of Paradise - Surah Ar-Rahman: 68"},
        {"খাঁটি দুধ", "Pure Milk", "পরিপূর্ণ পুষ্টি ও সুন্নাহ পানীয় - সুনানে নাসায়ী: ৫৩২৪", "Complete nutrition & Sunnah beverage - Sunan an-Nasa'i: 5324"},
        {"লাউ বা কদু", "Bottle Gourd", "রাসুলুল্লাহ সাল্লাল্লাহু আলাইহি ওয়া সাল্লামের প্রিয় খাবার - সহীহ বুখারী: ৫৪৩৩", "Beloved vegetable of Prophet Muhammad - Sahih Bukhari: 5433"}
    };
    int foodIndex = (dayOfWeek - 1) % sunnahFoods.length;
    binding.tvFeaturedSunnahFoodTitle.setText(isBn ? sunnahFoods[foodIndex][0] : sunnahFoods[foodIndex][1]);
    binding.tvFeaturedSunnahFoodSubtitle.setText(isBn ? sunnahFoods[foodIndex][2] : sunnahFoods[foodIndex][3]);
  }

  public void updateHomeDualLanguageTexts(boolean isBn) {
    if (binding == null) return;

    // 0. Location city names
    com.devflux.deenone.core.location.LocationProvider.Coordinates locCoords =
        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(this);
    String currentCity = com.devflux.deenone.core.location.LocationProvider.formatCityNameOnly(locCoords.locationName, isBn);
    binding.tvLocationName.setText(currentCity);
    if (binding.tvPrayerScheduleLocation != null) {
      binding.tvPrayerScheduleLocation.setVisibility(View.GONE);
    }

    // 1. Dynamic Forbidden / Current Waqt widget headers
    binding.tvForbiddenHeaderTitle.setText(isBn ? "নিষিদ্ধ নামাজের সময়" : "Forbidden Prayer Times");
    binding.tvCurrentWaqtHeader.setText(isBn ? "বর্তমান ওয়াক্ত" : "Current Waqt");
    binding.btnForbiddenDetails.setText(isBn ? "বিস্তারিত সময়" : "Details");

    // 2. Ramadan Card (Sehri & Iftar)
    binding.tvSehriLabel.setText(isBn ? "সাহরী শেষ" : "Sehri Ends");
    binding.tvIftarLabel.setText(isBn ? "ইফতার" : "Iftar");
    if (binding.tvFastingCountdownTitle != null) {
      binding.tvFastingCountdownTitle.setText(isBn ? "ইফতার হতে বাকি" : "Time until Iftar");
    }

    // 3. Prayer Schedule Card
    binding.tvPrayerScheduleTitle.setText(isBn ? "আজকের নামাজের সময়সূচী" : "Today's Prayer Schedule");
    binding.tvFajrLabel.setText(isBn ? "ফজর" : "Fajr");
    binding.tvZohrLabel.setText(isBn ? "যোহর" : "Dhuhr");
    binding.tvAsrLabel.setText(isBn ? "আসর" : "Asr");
    binding.tvMaghribLabel.setText(isBn ? "মাগরিব" : "Maghrib");
    binding.tvIshaLabel.setText(isBn ? "ইশা" : "Isha");
    if (cachedPrayerSchedule != null) {
      updatePrayerScheduleStripUI(cachedPrayerSchedule, isBn);
      binding.tvSehriTime.setText(formatTimeWithLanguage(cachedPrayerSchedule.getSehri(), isBn));
      binding.tvIftarTime.setText(formatTimeWithLanguage(cachedPrayerSchedule.getIftar(), isBn));
    }

    // 4. Quran Last Read Card
    binding.tvQuranLastReadHeader.setText(isBn ? "কুরআন তিলাওয়াত - সর্বশেষ পঠিত" : "Quran Recitation - Last Read");
    binding.btnResumeQuranReading.setText(isBn ? "পড়ুন " : "Read ");

    // 5. Daily Islamic Goals & Progress Card
    binding.tvIslamicGoalsTitle.setText(isBn ? "দৈনিক ইসলামিক লক্ষ্য ও অগ্রগতি" : "Daily Islamic Goals & Progress");
    binding.tvOpenGoalsTracker.setText(isBn ? "লক্ষ্য ট্র্যাকার খুলুন " : "Open Goals Tracker ");

    // 6. Daily Selected Dua Card
    binding.tvDailySelectedDuaHeader.setText(isBn ? "আজকের নির্বাচিত দোয়া" : "Today's Selected Dua");
    binding.tvDailyDuaViewAll.setText(isBn ? "সকল দোয়া " : "All Duas ");

    // 7. Amal Dashboard Card
    binding.tvAmalPrayerLabel.setText(isBn ? "নামাজ" : "Salah");
    binding.tvAmalPointsLabel.setText(isBn ? "পয়েন্ট" : "Points");
    binding.tvAmalCountLabel.setText(isBn ? "আমল" : "Deeds");
    binding.tvQuizCountLabel.setText(isBn ? "কুইজ" : "Quiz");
    binding.btnFullProfile.setText(isBn ? "পূর্ণাঙ্গ প্রোফাইল" : "Full Profile");

    // 8. 3 Habit Cards
    binding.tvMuhasabahTitle.setText(isBn ? "আত্মসমালোচনা" : "Self-Accountability");
    binding.tvMuhasabahSubtitle.setText(isBn ? "নিজের আমল পর্যবেক্ষণ ও আত্মশুদ্ধি করুন" : "Reflect on your deeds and spiritual purification");
    binding.tvFajrCallTitle.setText(isBn ? "ফজরের ডাক" : "Fajr Call");
    binding.tvFajrCallSubtitle.setText(isBn ? "প্রিয়জনকে নামাজের জন্য জাগিয়ে তুলুন" : "Awaken your loved ones for prayer");
    binding.tvQuranSleepTitle.setText(isBn ? "কুরআন স্লিপ মোড" : "Quran Sleep Mode");
    binding.tvQuranSleepSubtitle.setText(isBn ? "ঘুমানোর পূর্বে প্রশান্তিদায়ক তিলাওয়াত শুনুন" : "Listen to peaceful Quran recitation for sleep");

    // 9. Nearby Mosque Card
    binding.tvNearbyMosqueHeader.setText(isBn ? "নিকটবর্তী মসজিদ" : "Nearby Mosque");
    binding.tvAllMosquesLink.setText(isBn ? "সকল মসজিদ " : "All Mosques ");

    // 10. Hajj & Umrah Quick Guide Card
    binding.tvHajjGuideTitle.setText(isBn ? "হজ ও ওমরাহ্ পূর্ণাঙ্গ A-Z গাইডলাইন" : "Hajj & Umrah Complete A-Z Guide");
    binding.tvHajjGuideSubtitle.setText(isBn ? "ইহরাম, মীকাত, তাওয়াফ, সায়ী, আরাফাহ ও রমি করার ধারাবাহিক সহীহ নিয়ম।" : "Guidelines for Ihram, Miqat, Tawaf, Sa'i, Arafah and Rami.");



    // 12. Halal Food & Nutrition Guide Card
    binding.tvHalalFoodTitle.setText(isBn ? "হালাল খাবার ও পুষ্টি নির্দেশিকা" : "Halal Food & Nutrition Guide");
    binding.tvHalalFoodSubtitle.setText(isBn ? "সুন্নাহ ফুড ও খাদ্য উপাদান (E-Code) যাচাই নির্দেশিকা" : "Sunnah Foods & E-Code Verification Guide");
    binding.tvHalalViewAll.setText(isBn ? "সব দেখুন" : "View All");
    binding.tvSunnahFeaturedBadge.setText(isBn ? "আজকের বরকতময় খাবার" : "Today's Blessed Food");
    binding.pillEcodeChecker.setText(isBn ? "E120, E471 কোড চেকার" : "E120, E471 Code Checker");
    binding.pillHalalZabiha.setText(isBn ? "হালাল জবাই ও যবেহ নীতি" : "Halal Slaughter & Zabiha");
    binding.pillHalalRestaurants.setText(isBn ? "জনপ্রিয় খাবার" : "Popular Foods");



    // 13. Features Grid Header
    binding.tvFeatureGridHeader.setText(isBn ? "প্রয়োজনীয় ইসলামিক ফিচারসমূহ" : "Essential Islamic Features");
    if (binding.rvFeatureGrid.getAdapter() != null) {
      binding.rvFeatureGrid.getAdapter().notifyDataSetChanged();
    }

    // 14. Daily Ayah & Hadith Section Header & Card Titles
    binding.tvDailyAyahHadithSectionHeader.setText(isBn ? "দৈনিক আয়াত ও সহীহ হাদিস" : "Daily Verse & Sahih Hadith");
    binding.tvDailyAyahCardTitle.setText(isBn ? "আল-কুরআন" : "Al Quran");
    binding.tvDailyHadithCardTitle.setText(isBn ? "সহীহ হাদিস" : "Sahih Hadith");
    com.devflux.deenone.core.content.DailyAyahHadithManager.DailyDisplayContent curContent =
        com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().getLiveContent().getValue();
    if (curContent != null) {
      if (curContent.ayah != null) {
        binding.tvDailyAyahSurahBadge.setText(curContent.ayah.getSurahName(isBn));
        binding.tvDailyAyahText.setText(curContent.ayah.getMeaning(isBn));
        binding.tvDailyAyahReference.setText(curContent.ayah.getReference(isBn));
      }
      if (curContent.hadith != null) {
        binding.tvDailyHadithGrade.setText(curContent.hadith.getCollection(isBn));
        binding.tvDailyHadithText.setText(curContent.hadith.getMeaning(isBn));
        binding.tvDailyHadithReference.setText(curContent.hadith.getReference(isBn));
      }
    }

    // 15. Bottom Navigation Bar Tabs
    binding.tvNavHome.setText(isBn ? "হোম" : "Home");
    binding.tvNavSalat.setText(isBn ? "সালাত" : "Salat");
    binding.tvNavAmal.setText(isBn ? "আমল" : "Amal");
    binding.tvNavRank.setText(isBn ? "র‍্যাংক" : "Rank");
    binding.tvNavCommunity.setText(isBn ? "কমিউনিটি" : "Community");

    // 16. Carousel adapter
    if (nextPrayerCarouselAdapter != null) {
      nextPrayerCarouselAdapter.notifyDataSetChanged();
    }

    // 17. Re-calculate Real-time ViewModel live data
    if (viewModel != null) {
      viewModel.updateRealTimeCalculations();
    }
  }

  public void showSidebarMenuSheet() {
    AppCompatDialog dialog = new AppCompatDialog(this, R.style.Theme_DeenOne_SideDrawer);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    BottomSheetSidebarMenuBinding sheetBinding = BottomSheetSidebarMenuBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    Window window = dialog.getWindow();
    if (window != null) {
      window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      WindowCompat.setDecorFitsSystemWindows(window, false);

      WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
      lp.copyFrom(window.getAttributes());
      int screenWidth = getResources().getDisplayMetrics().widthPixels;
      lp.width = Math.min((int) (screenWidth * 0.80f), (int) (320 * getResources().getDisplayMetrics().density));
      lp.height = WindowManager.LayoutParams.MATCH_PARENT;
      lp.gravity = Gravity.START | Gravity.TOP;
      lp.x = 0;
      lp.y = 0;
      window.setAttributes(lp);
      window.setLayout(lp.width, WindowManager.LayoutParams.MATCH_PARENT);
      if (window.getDecorView() != null) {
        window.getDecorView().setPadding(0, 0, 0, 0);
      }

      window.setWindowAnimations(R.style.DeenOneSideDrawerAnimation);
      window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
      window.setDimAmount(0.60f);

      boolean isDark = ThemeManager.getSavedThemeMode(this) == ThemeManager.THEME_DARK;
      WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
      if (controller != null) {
        controller.setAppearanceLightStatusBars(!isDark);
      }
    }

    ViewCompat.setOnApplyWindowInsetsListener(sheetBinding.getRoot(), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
      int padH = (int) (16 * getResources().getDisplayMetrics().density);
      v.setPadding(
          padH,
          systemBars.top + (int) (10 * getResources().getDisplayMetrics().density),
          padH,
          systemBars.bottom + (int) (16 * getResources().getDisplayMetrics().density)
      );
      return insets;
    });

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    boolean isLoggedIn = AuthManager.isLoggedIn(this);
    AuthManager.UserSession session = AuthManager.getCurrentSession(this);

    // Dynamic Dual-Language Sidebar Titles
    sheetBinding.tvSidebarTopBarTitle.setText(isBn ? "মেনু" : "Menu");
    sheetBinding.tvSidebarFullProfileTitle.setText(isBn ? "পূর্ণাঙ্গ প্রোফাইল" : "Full Profile");
    sheetBinding.tvSidebarNotificationsTitle.setText(isBn ? "নোটিফিকেশনসমূহ" : "Notifications");
    sheetBinding.tvSidebarAppSettingsTitle.setText(isBn ? "সেটিংস" : "Settings");
    sheetBinding.tvSidebarSoundNotifTitle.setText(isBn ? "নোটিফিকেশন ও সাউন্ড" : "Notification & Sound");
    sheetBinding.tvSidebarLangTitle.setText(isBn ? "অ্যাপের ভাষা" : "App Language");
    sheetBinding.tvSidebarDarkModeTitle.setText(isBn ? "ডার্ক মোড" : "Dark Mode");
    sheetBinding.tvSidebarHelpSupportTitle.setText(isBn ? "সাহায্য ও সাপোর্ট" : "Help & Support");
    sheetBinding.tvSidebarAboutUsTitle.setText(isBn ? "আমাদের সম্পর্কে" : "About Us");
    sheetBinding.tvSidebarPrivacyPolicyTitle.setText(isBn ? "প্রাইভেসি পলিসি" : "Privacy Policy");
    sheetBinding.tvSidebarSupportBadge.setText(isBn ? "সহায়তা" : "SUPPORT");
    sheetBinding.tvSidebarSupportTitle.setText(isBn ? "দ্বীনওয়ান খেদমত" : "DEENONE");
    sheetBinding.tvSidebarLogoutTitle.setText(isBn ? "লগআউট" : "Logout");

    int userPoints = Math.max(
        session.points,
        Math.max(
            com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(MainActivity.this),
            Math.max(
                com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.getLifetimePoints(MainActivity.this),
                com.devflux.deenone.core.quiz.QuizManager.getInstance().getDeenPoints(MainActivity.this)
            )
        )
    );
    if (isLoggedIn) {
      sheetBinding.tvSidebarUserName.setText(session.name);
      sheetBinding.tvSidebarUserSubtitle.setText(userPoints > 0
          ? (isBn ? BengaliNumberUtil.toBengali(userPoints) + " \u09aa\u09df\u09c7\u09a8\u09cd\u099f" : userPoints + " Points")
          : (isBn ? "\u09e6 \u09aa\u09df\u09c7\u09a8\u09cd\u099f" : "0 Points"));
      sheetBinding.ivSidebarAvatar.setImageTintList(null);
      sheetBinding.ivSidebarAvatar.setPadding(0, 0, 0, 0);
      sheetBinding.ivSidebarAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
      com.devflux.deenone.features.profile.ProfileImageUploadManager.loadAvatarIntoImageView(MainActivity.this, sheetBinding.ivSidebarAvatar, session.avatar, R.drawable.ic_user_circle_avatar);
      sheetBinding.ivSidebarVerifiedBadge.setVisibility(View.VISIBLE);
      sheetBinding.btnSidebarLogout.setVisibility(View.VISIBLE);

      sheetBinding.cardSidebarProfile.setOnClickListener(v -> {
        showUserProfileSheet();
      });
    } else {
      sheetBinding.tvSidebarUserName.setText(isBn ? "\u09b2\u0997\u0987\u09a8 / \u09b8\u09be\u0987\u09a8 \u0986\u09aa" : "Login / Sign Up");
      sheetBinding.tvSidebarUserSubtitle.setText(isBn ? "\u0986\u09aa\u09a8\u09be\u09b0 \u0985\u09ac\u09cd\u09af\u09be\u0995\u09be\u0989\u09a8\u09cd\u099f\u09c7 \u09af\u09c1\u0995\u09cd\u09a4 \u09b9\u09cb\u09a8" : "Connect to your account");
      int pad = (int) (11 * getResources().getDisplayMetrics().density);
      sheetBinding.ivSidebarAvatar.setPadding(pad, pad, pad, pad);
      sheetBinding.ivSidebarAvatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      sheetBinding.ivSidebarAvatar.setImageTintList(android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_mint)));
      sheetBinding.ivSidebarAvatar.setImageResource(R.drawable.ic_login);
      sheetBinding.ivSidebarVerifiedBadge.setVisibility(View.GONE);
      sheetBinding.btnSidebarLogout.setVisibility(View.GONE);

      sheetBinding.cardSidebarProfile.setOnClickListener(v -> {
        AuthDialogManager.showLoginDialog(MainActivity.this, newSession -> {
          boolean currentBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(MainActivity.this);
          Toast.makeText(MainActivity.this, (currentBn ? "\u09b8\u09cd\u09ac\u09be\u0997\u09a4\u09ae, " : "Welcome, ") + newSession.name, Toast.LENGTH_SHORT).show();
          sheetBinding.tvSidebarUserName.setText(newSession.name);
          int pts = Math.max(
              newSession.points,
              Math.max(
                  com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(MainActivity.this),
                  Math.max(
                      com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager.getLifetimePoints(MainActivity.this),
                      com.devflux.deenone.core.quiz.QuizManager.getInstance().getDeenPoints(MainActivity.this)
                  )
              )
          );
          sheetBinding.tvSidebarUserSubtitle.setText(pts > 0
              ? (currentBn ? BengaliNumberUtil.toBengali(pts) + " \u09aa\u09df\u09c7\u09a8\u09cd\u099f" : pts + " Points")
              : (currentBn ? "\u09eb\u09e6 \u09aa\u09df\u09c7\u09a8\u09cd\u099f" : "50 Points"));
          sheetBinding.ivSidebarAvatar.setImageTintList(null);
          sheetBinding.ivSidebarAvatar.setPadding(0, 0, 0, 0);
          sheetBinding.ivSidebarAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
          com.devflux.deenone.features.profile.ProfileImageUploadManager.loadAvatarIntoImageView(MainActivity.this, sheetBinding.ivSidebarAvatar, newSession.avatar, R.drawable.ic_user_circle_avatar);
          sheetBinding.ivSidebarVerifiedBadge.setVisibility(View.VISIBLE);
          sheetBinding.btnSidebarLogout.setVisibility(View.VISIBLE);
          sheetBinding.cardSidebarProfile.setOnClickListener(vProfile -> showUserProfileSheet());
        });
      });
    }

    // Touch animations ONLY on buttons (Rule 7)
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseSidebar);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnSidebarLangBn);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnSidebarLangEn);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnSidebarLogout);

    sheetBinding.btnCloseSidebar.setOnClickListener(v -> dialog.dismiss());

    sheetBinding.menuItemFullProfile.setOnClickListener(v -> {
      showUserProfileSheet();
    });

    sheetBinding.menuItemNotifications.setOnClickListener(v -> {
      showNotificationHistorySheet();
    });

    sheetBinding.menuItemAppSettings.setOnClickListener(v -> {
      showAppSettingsSheet();
    });

    sheetBinding.menuItemSoundAndNotification.setOnClickListener(v -> {
      showSoundAndNotificationSettingsSheet();
    });

    // 5. App Language Segment Control
    Runnable updateLangUi = () -> {
      boolean isBengali = com.devflux.deenone.core.localization.LocaleManager.isBengali(MainActivity.this);
      if (isBengali) {
        sheetBinding.btnSidebarLangBn.setBackgroundResource(R.drawable.bg_sidebar_segment_active);
        sheetBinding.btnSidebarLangBn.setTextColor(Color.BLACK);
        sheetBinding.btnSidebarLangEn.setBackgroundColor(Color.TRANSPARENT);
        sheetBinding.btnSidebarLangEn.setTextColor(getColor(R.color.accent_mint));
      } else {
        sheetBinding.btnSidebarLangEn.setBackgroundResource(R.drawable.bg_sidebar_segment_active);
        sheetBinding.btnSidebarLangEn.setTextColor(Color.BLACK);
        sheetBinding.btnSidebarLangBn.setBackgroundColor(Color.TRANSPARENT);
        sheetBinding.btnSidebarLangBn.setTextColor(getColor(R.color.accent_mint));
      }
    };
    updateLangUi.run();

    sheetBinding.btnSidebarLangBn.setOnClickListener(v -> {
      if (!com.devflux.deenone.core.localization.LocaleManager.isBengali(this)) {
        com.devflux.deenone.core.localization.LocaleManager.setLanguage(this, com.devflux.deenone.core.localization.LocaleManager.LANGUAGE_BENGALI);
        updateLangUi.run();
        boolean curDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(this) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
        com.devflux.deenone.core.auth.AuthManager.syncUserSettings(this, "bn", curDark ? "dark" : "light");
        dialog.dismiss();
        recreate();
        Toast.makeText(this, "বাংলা ভাষা সক্রিয় করা হয়েছে", Toast.LENGTH_SHORT).show();
      }
    });

    sheetBinding.btnSidebarLangEn.setOnClickListener(v -> {
      if (com.devflux.deenone.core.localization.LocaleManager.isBengali(this)) {
        com.devflux.deenone.core.localization.LocaleManager.setLanguage(this, com.devflux.deenone.core.localization.LocaleManager.LANGUAGE_ENGLISH);
        updateLangUi.run();
        boolean curDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(this) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
        com.devflux.deenone.core.auth.AuthManager.syncUserSettings(this, "en", curDark ? "dark" : "light");
        dialog.dismiss();
        recreate();
        Toast.makeText(this, "English language activated", Toast.LENGTH_SHORT).show();
      }
    });

    // 6. Dark Mode Toggle Switch
    boolean isDarkModeActive = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(this) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    sheetBinding.switchSidebarDarkMode.setChecked(isDarkModeActive);
    sheetBinding.tvSidebarDarkModeTitle.setText(isBn ? "ডার্ক মোড" : "Dark Mode");

    sheetBinding.switchSidebarDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
      int targetMode = isChecked ? com.devflux.deenone.core.theme.ThemeManager.THEME_DARK : com.devflux.deenone.core.theme.ThemeManager.THEME_LIGHT;
      if (com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(MainActivity.this) != targetMode) {
        com.devflux.deenone.core.theme.ThemeManager.setThemeMode(MainActivity.this, targetMode);
        updateThemeToggleIcon(targetMode);
        boolean currentBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(MainActivity.this);
        sheetBinding.tvSidebarDarkModeTitle.setText(currentBn ? "ডার্ক মোড" : "Dark Mode");
        String currentLang = com.devflux.deenone.core.localization.LocaleManager.getSavedLanguage(MainActivity.this);
        com.devflux.deenone.core.auth.AuthManager.syncUserSettings(MainActivity.this, currentLang, isChecked ? "dark" : "light");
        Toast.makeText(MainActivity.this, isChecked
            ? (currentBn ? "ডার্ক মোড সক্রিয় করা হয়েছে" : "Dark Mode activated")
            : (currentBn ? "লাইট মোড সক্রিয় করা হয়েছে" : "Light Mode activated"), Toast.LENGTH_SHORT).show();
      }
    });

    sheetBinding.layoutSidebarDarkMode.setOnClickListener(v -> {
      sheetBinding.switchSidebarDarkMode.toggle();
    });

    sheetBinding.menuItemHelpSupport.setOnClickListener(v -> {
      showHelpSupportDialog();
    });

    sheetBinding.menuItemAboutUs.setOnClickListener(v -> {
      showAboutUsDialog();
    });

    sheetBinding.menuItemPrivacyPolicy.setOnClickListener(v -> {
      showPrivacyPolicyDialog();
    });

    sheetBinding.cardSupportDeenOne.setOnClickListener(v -> {
      showSupportDeenOneDialog();
    });

    sheetBinding.btnSidebarLogout.setOnClickListener(v -> {
      AuthManager.logout(MainActivity.this);
      boolean currentBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      sheetBinding.tvSidebarUserName.setText(currentBn ? "\u09b2\u0997\u0987\u09a8 / \u09b8\u09be\u0987\u09a8 \u0986\u09aa" : "Login / Sign Up");
      sheetBinding.tvSidebarUserSubtitle.setText(currentBn ? "\u0986\u09aa\u09a8\u09be\u09b0 \u0985\u09ac\u09cd\u09af\u09be\u0995\u09be\u0989\u09a8\u09cd\u099f\u09c7 \u09af\u09c1\u0995\u09cd\u09a4 \u09b9\u09cb\u09a8" : "Connect to your account");
      int pad = (int) (11 * getResources().getDisplayMetrics().density);
      sheetBinding.ivSidebarAvatar.setPadding(pad, pad, pad, pad);
      sheetBinding.ivSidebarAvatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      sheetBinding.ivSidebarAvatar.setImageTintList(android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_mint)));
      sheetBinding.ivSidebarAvatar.setImageResource(R.drawable.ic_login);
      sheetBinding.ivSidebarVerifiedBadge.setVisibility(View.GONE);
      sheetBinding.btnSidebarLogout.setVisibility(View.GONE);
      sheetBinding.cardSidebarProfile.setOnClickListener(vLogin -> {
        AuthDialogManager.showLoginDialog(MainActivity.this, newSession -> {
          Toast.makeText(MainActivity.this, (currentBn ? "\u09b8\u09cd\u09ac\u09be\u0997\u09a4\u09ae, " : "Welcome, ") + newSession.name, Toast.LENGTH_SHORT).show();
          dialog.dismiss();
        });
      });
      Toast.makeText(this, currentBn ? "\u09b8\u09ab\u09b2\u09ad\u09be\u09ac\u09c7 \u09b2\u0997\u0986\u0989\u099f \u09b8\u09ae\u09cd\u09aa\u09a8\u09cd\u09a8 \u09b9\u09af\u09bc\u09c7\u099b\u09c7" : "Logged out successfully", Toast.LENGTH_SHORT).show();
    });

    dialog.show();
  }

  private void showHelpSupportDialog() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setBackgroundColor(getColor(R.color.bg_main));
    layout.setPadding(48, 48, 48, 48);

    TextView title = new TextView(this);
    title.setText(isBn ? "সহযোগিতা ও সাপোর্ট" : "Help & Support");
    title.setTextSize(18);
    title.setTextColor(getColor(R.color.accent_mint));
    title.setTypeface(null, android.graphics.Typeface.BOLD);
    layout.addView(title);

    TextView body = new TextView(this);
    body.setText(isBn
        ? "\nদ্বীনওয়ান অ্যাপ সম্পর্কিত যেকোনো প্রশ্ন, বাগ রিপোর্ট বা পরামর্শের জন্য আমাদের সাথে যোগাযোগ করুন:\n\n• ইমেইল: support@deenone.top\n• অফিসিয়াল ওয়েবসাইট: https://deenone.top\n• টেলিগ্রাম দ্বীনি কমিউনিটি: @DeenOneCommunity\n\nআমরা দ্রুততম সময়ে আপনার বার্তা পর্যালোচনা করে ব্যবস্থা গ্রহণ করব ইনশাআল্লাহ।"
        : "\nFor any questions, bug reports, or suggestions regarding the DeenOne app, please reach out to us:\n\n• Email: support@deenone.top\n• Official Website: https://deenone.top\n• Telegram Community: @DeenOneCommunity\n\nWe will review your message promptly InshaAllah.");
    body.setTextSize(14);
    body.setTextColor(getColor(R.color.text_primary));
    body.setLineSpacing(6, 1.2f);
    layout.addView(body);

    com.google.android.material.button.MaterialButton btnClose = new com.google.android.material.button.MaterialButton(this);
    btnClose.setText(isBn ? "ঠিক আছে" : "OK");
    btnClose.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getColor(R.color.accent_mint)));
    btnClose.setTextColor(getColor(R.color.bg_main));
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(btnClose);
    btnClose.setOnClickListener(v -> dialog.dismiss());
    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.topMargin = 40;
    layout.addView(btnClose, lp);

    dialog.setContentView(layout);
    dialog.show();
  }

  private void showAboutUsDialog() {
    com.devflux.deenone.features.about.AboutUsPageDialog.show(this);
  }

  private void showPrivacyPolicyDialog() {
    com.devflux.deenone.features.privacy.PrivacyPolicyPageDialog.show(this);
  }

  private void showSupportDeenOneDialog() {
    com.devflux.deenone.features.donation.DonationPageDialog.show(this);
  }

  // =========================================================================
  // Halal Foods & E-Code Hub (হালাল ও হারাম খাবার নির্দেশিকা)
  // =========================================================================
  private void showHalalFoodsSheet() {
    showHalalFoodsSheet("ALL");
  }

  private void showHalalFoodsSheet(String initialCategory) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetHalalFoodsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetHalalFoodsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseHalalSheet);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabHalalStatusAll);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabHalalStatusHalal);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabHalalStatusHaram);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabHalalStatusMushbooh);

    sheetBinding.btnCloseHalalSheet.setOnClickListener(v -> dialog.dismiss());

    // Localize Top Bar (Mathematically Centered)
    sheetBinding.tvHalalSheetTitle.setText(isBn ? "হালাল ফুড ও পুষ্টি গাইড" : "Halal Food & Nutrition Guide");
    sheetBinding.tvHalalSyncStatus.setText(isBn ? "১০০% সহীহ রেফারেন্স ও ফিকহি বিশ্লেষণ" : "100% Authentic References & Fiqh Analysis");

    // Localize Search Hint
    sheetBinding.etSearchHalal.setHint(isBn ? "খাবার, E-Code (যেমন E120), বা উপাদান খুঁজুন..." : "Search foods, E-Codes (e.g. E120), or ingredients...");

    // Localize Status Filter Tabs
    sheetBinding.tabHalalStatusAll.setText(isBn ? "সব" : "All");
    sheetBinding.tabHalalStatusHalal.setText(isBn ? "হালাল" : "Halal");
    sheetBinding.tabHalalStatusHaram.setText(isBn ? "হারাম" : "Haram");
    sheetBinding.tabHalalStatusMushbooh.setText(isBn ? "সন্দেহজনক" : "Doubtful");

    com.devflux.deenone.core.halal.HalalFoodAdapter adapter =
        new com.devflux.deenone.core.halal.HalalFoodAdapter(this::showHalalFoodDetailDialog);

    sheetBinding.rvHalalFoods.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvHalalFoods.setAdapter(adapter);

    final String[] selectedStatus = {"ALL"};
    final String[] currentQuery = {""};

    Runnable filterRunnable = () -> {
      List<com.devflux.deenone.core.halal.HalalFoodItem> filtered =
          com.devflux.deenone.core.halal.HalalFoodManager.getInstance().searchAndFilter(
              this, currentQuery[0], "ALL", selectedStatus[0]
          );
      adapter.updateData(filtered);
      if (isBn) {
        sheetBinding.tvHalalListCount.setText("মোট রেকর্ড: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(filtered.size()) + "টি উপাদান প্রদর্শিত");
      } else {
        sheetBinding.tvHalalListCount.setText("Total Records: " + filtered.size() + " items displayed");
      }
    };

    // Background Offline/Online Sync
    com.devflux.deenone.core.halal.HalalFoodManager.getInstance().syncWithRemoteSource(this, () -> {
      runOnUiThread(() -> {
        sheetBinding.tvHalalSyncStatus.setText(isBn ? "১০০% সহীহ রেফারেন্স ও ফিকহি বিশ্লেষণ" : "100% Authentic References & Fiqh Analysis");
        filterRunnable.run();
      });
    });

    // Search Input Listener
    sheetBinding.etSearchHalal.addTextChangedListener(new android.text.TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentQuery[0] = s.toString();
        filterRunnable.run();
      }
      @Override public void afterTextChanged(android.text.Editable s) {}
    });

    // Status Filter Pills Styling
    int unselectedBg = R.drawable.bg_badge_pill;
    int selectedBg = R.drawable.bg_btn_mint_pill;
    int unselectedText = getColor(R.color.text_primary);
    int selectedText = 0xFFFFFFFF;

    Runnable resetStatusPills = () -> {
      sheetBinding.tabHalalStatusAll.setBackgroundResource(unselectedBg);
      sheetBinding.tabHalalStatusAll.setTextColor(unselectedText);
      sheetBinding.tabHalalStatusHalal.setBackgroundResource(unselectedBg);
      sheetBinding.tabHalalStatusHalal.setTextColor(unselectedText);
      sheetBinding.tabHalalStatusHaram.setBackgroundResource(unselectedBg);
      sheetBinding.tabHalalStatusHaram.setTextColor(unselectedText);
      sheetBinding.tabHalalStatusMushbooh.setBackgroundResource(unselectedBg);
      sheetBinding.tabHalalStatusMushbooh.setTextColor(unselectedText);
    };

    // Pre-select status pill
    sheetBinding.tabHalalStatusAll.setBackgroundResource(selectedBg);
    sheetBinding.tabHalalStatusAll.setTextColor(selectedText);

    // Initial Data Load
    filterRunnable.run();

    // Status Click Listeners
    sheetBinding.tabHalalStatusAll.setOnClickListener(v -> {
      resetStatusPills.run();
      sheetBinding.tabHalalStatusAll.setBackgroundResource(selectedBg);
      sheetBinding.tabHalalStatusAll.setTextColor(selectedText);
      selectedStatus[0] = "ALL";
      filterRunnable.run();
    });

    sheetBinding.tabHalalStatusHalal.setOnClickListener(v -> {
      resetStatusPills.run();
      sheetBinding.tabHalalStatusHalal.setBackgroundResource(selectedBg);
      sheetBinding.tabHalalStatusHalal.setTextColor(selectedText);
      selectedStatus[0] = "HALAL";
      filterRunnable.run();
    });

    sheetBinding.tabHalalStatusHaram.setOnClickListener(v -> {
      resetStatusPills.run();
      sheetBinding.tabHalalStatusHaram.setBackgroundResource(selectedBg);
      sheetBinding.tabHalalStatusHaram.setTextColor(selectedText);
      selectedStatus[0] = "HARAM";
      filterRunnable.run();
    });

    sheetBinding.tabHalalStatusMushbooh.setOnClickListener(v -> {
      resetStatusPills.run();
      sheetBinding.tabHalalStatusMushbooh.setBackgroundResource(selectedBg);
      sheetBinding.tabHalalStatusMushbooh.setTextColor(selectedText);
      selectedStatus[0] = "MUSHBOOH";
      filterRunnable.run();
    });

    dialog.show();
  }

  private void showHalalFoodDetailDialog(com.devflux.deenone.core.halal.HalalFoodItem item) {
    if (item == null) return;
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.DialogHalalFoodDetailBinding binding =
        com.devflux.deenone.databinding.DialogHalalFoodDetailBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseHalalDetail);
    binding.btnCloseHalalDetail.setOnClickListener(v -> dialog.dismiss());

    // Localize Section Headers
    binding.tvDetailTopTitle.setText(isBn ? "হালাল খাদ্য ও পুষ্টি বিবরণ" : "Halal Food & Nutrition Details");
    binding.tvDetailBenefitsHeader.setText(isBn ? "পুষ্টিগুণ ও স্বাস্থ্য উপকারিতা:" : "Nutritional & Health Benefits:");
    binding.tvDetailHowToConsumeHeader.setText(isBn ? "খাওয়ার সুন্নাহ নিয়ম ও নির্দেশনা:" : "Sunnah Guidelines & Consumption Method:");
    binding.tvDetailDescriptionHeader.setText(isBn ? "শারীয়াহ বিধান ও বিস্তারিত আলোচনা:" : "Shariah Ruling & Detailed Discussion:");
    binding.tvDetailAlternativeHeader.setText(isBn ? "💡 নিরাপদ ও স্বাস্থ্যকর বিকল্প হালাল উপাদান:" : "💡 Safe & Healthy Halal Alternative:");
    binding.tvDetailMadhhabNoteHeader.setText(isBn ? "চার মাযহাবের ওলামায়ে কেরামের দৃষ্টিভঙ্গি:" : "Views of the Four Madhhabs:");
    binding.tvDetailReferenceHeader.setText(isBn ? "নির্ভরযোগ্য সূত্র ও দলীল:" : "Authentic Sources & Evidence:");

    // Load Hero Image asynchronously
    com.devflux.deenone.utils.AsyncImageLoader.getInstance(this).loadImage(
        binding.ivDetailFoodImage,
        item.getImageUrl(),
        R.drawable.bg_card_secondary
    );

    binding.tvDetailTitle.setText(item.getTitle(isBn));

    if (item.getArabicName() != null && !item.getArabicName().isEmpty()) {
      binding.tvDetailArabicName.setVisibility(View.VISIBLE);
      binding.tvDetailArabicName.setText(item.getArabicName());
    } else {
      binding.tvDetailArabicName.setVisibility(View.GONE);
    }

    String sci = item.getScientificName(isBn);
    binding.tvDetailScientificName.setText((isBn ? "বৈজ্ঞানিক / সাধারণ নাম: " : "Scientific / Common Name: ") + (sci != null ? sci : ""));

    String source = item.getSourceOrigin(isBn);
    binding.tvDetailSourceOrigin.setText((isBn ? "মূল উৎস: " : "Primary Source: ") + (source != null ? source : ""));

    String benefits = item.getNutritionBenefits(isBn);
    if (benefits != null && !benefits.isEmpty()) {
      binding.tvDetailBenefits.setText(benefits);
    } else {
      binding.tvDetailBenefits.setText(isBn ? "প্রাকৃতিক স্বাস্থ্যকর পুষ্টি উপাদান সমৃদ্ধ।" : "Rich in natural healthy nutrients.");
    }

    String howTo = item.getHowToConsume(isBn);
    if (howTo != null && !howTo.isEmpty()) {
      binding.tvDetailHowToConsume.setText(howTo);
    } else {
      binding.tvDetailHowToConsume.setText(isBn ? "সুন্নাহসম্মত উপায়ে খাদ্য বা উপাদান হিসেবে গ্রহণ করা।" : "Consume in accordance with Sunnah practices.");
    }

    binding.tvDetailDescription.setText(item.getDescription(isBn));
    binding.tvDetailMadhhabNote.setText(item.getMadhhabNote(isBn));
    binding.tvDetailReference.setText((isBn ? "প্রামাণিক সূত্র:\n" : "Authentic Reference:\n") + item.getReference(isBn));

    String status = item.getStatus();
    if ("HARAM".equalsIgnoreCase(status)) {
      binding.tvDetailStatusBadge.setText(isBn ? "হারাম" : "Haram");
      binding.tvDetailStatusBadge.setTextColor(android.graphics.Color.parseColor("#EF4444"));
      binding.tvDetailStatusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2E0808")));
    } else if ("MUSHBOOH".equalsIgnoreCase(status)) {
      binding.tvDetailStatusBadge.setText(isBn ? "সন্দেহজনক" : "Doubtful");
      binding.tvDetailStatusBadge.setTextColor(android.graphics.Color.parseColor("#F59E0B"));
      binding.tvDetailStatusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2E1C05")));
    } else {
      binding.tvDetailStatusBadge.setText(isBn ? "হালাল" : "Halal");
      binding.tvDetailStatusBadge.setTextColor(android.graphics.Color.parseColor("#10B981"));
      binding.tvDetailStatusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#052E22")));
    }

    String alt = item.getHalalAlternative(isBn);
    if (alt != null && !alt.trim().isEmpty()) {
      binding.layoutDetailAlternative.setVisibility(View.VISIBLE);
      binding.tvDetailAlternative.setText(alt);
    } else {
      binding.layoutDetailAlternative.setVisibility(View.GONE);
    }

    dialog.show();
  }

  @SuppressWarnings("unused")
  private void showLanguagePickerSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetLanguagePickerBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetLanguagePickerBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvBengaliBadge.setText(isBn ? "সক্রিয়" : "");
    sheetBinding.tvEnglishBadge.setText(!isBn ? "Active" : "");

    sheetBinding.btnLangBengali.setOnClickListener(v -> {
      com.devflux.deenone.core.localization.LocaleManager.setLanguage(this, com.devflux.deenone.core.localization.LocaleManager.LANGUAGE_BENGALI);
      dialog.dismiss();
      recreate();
      Toast.makeText(this, "বাংলা ভাষা সক্রিয় করা হয়েছে", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnLangEnglish.setOnClickListener(v -> {
      com.devflux.deenone.core.localization.LocaleManager.setLanguage(this, com.devflux.deenone.core.localization.LocaleManager.LANGUAGE_ENGLISH);
      dialog.dismiss();
      recreate();
      Toast.makeText(this, "English language activated", Toast.LENGTH_SHORT).show();
    });

    dialog.show();
  }

  public static String pendingReopenPage = null;

  public void toggleAppTheme(String reopenPage) {
    pendingReopenPage = reopenPage;
    toggleAppTheme();
  }

  public void toggleAppTheme() {
    int currentMode = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(this);
    int newMode = (currentMode == com.devflux.deenone.core.theme.ThemeManager.THEME_LIGHT)
        ? com.devflux.deenone.core.theme.ThemeManager.THEME_DARK
        : com.devflux.deenone.core.theme.ThemeManager.THEME_LIGHT;
    com.devflux.deenone.core.theme.ThemeManager.setThemeMode(this, newMode);
    updateThemeToggleIcon(newMode);
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    String msg = (newMode == com.devflux.deenone.core.theme.ThemeManager.THEME_LIGHT)
        ? (isBn ? "লাইট মোড সক্রিয় হয়েছে" : "Light mode activated")
        : (isBn ? "ডার্ক মোড সক্রিয় হয়েছে" : "Dark mode activated");
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  private void handlePendingPageReopen() {
    if (pendingReopenPage != null) {
      final String page = pendingReopenPage;
      pendingReopenPage = null;
      binding.getRoot().post(() -> {
        if ("SALAT".equals(page)) {
          showSalahTrackerSheet();
        } else if ("AMAL".equals(page)) {
          showAmalTrackerSheet();
        } else if ("RANK".equals(page)) {
          com.devflux.deenone.features.leaderboard.LeaderboardRankPageDialog.show(this);
        } else if ("COMMUNITY".equals(page)) {
          com.devflux.deenone.features.community.CommunityFeedDialog.show(this);
        }
      });
    }
  }

  public void updateThemeToggleIcon(int themeMode) {
    if (binding != null && binding.ivHeaderThemeIcon != null) {
      if (themeMode == com.devflux.deenone.core.theme.ThemeManager.THEME_LIGHT) {
        binding.ivHeaderThemeIcon.setImageResource(R.drawable.ic_moon);
      } else {
        binding.ivHeaderThemeIcon.setImageResource(R.drawable.ic_sun);
      }
    }
  }

  public void showLocationPickerSheet() {
    com.devflux.deenone.features.location.LocationSelectorPageDialog.show(this);
  }

  public HomeViewModel getViewModel() {
    return viewModel;
  }

  private void handleFeatureClick(FeatureItem item) {
    if (item == null) return;
    com.devflux.deenone.core.ads.AdManager.getInstance().recordNavigationAndShowInterstitial(
        this,
        false,
        () -> executeFeatureClickAction(item)
    );
  }

  private void executeFeatureClickAction(FeatureItem item) {
    String key = item.getActionKey();
    String title = item.getTitle();

    if ("action_salat".equals(key) || title.equals("সালাত")) {
      showSalahTrackerSheet();
    } else if ("action_namaz_shikha".equals(key) || title.equals("নামাজ শিক্ষা")) {
      showSalahGuideSheet();
    } else if ("action_quran".equals(key) || title.equals("কুরআন মাজিদ") || title.contains("কুরআন")) {
      showQuranHubBottomSheet();
    } else if ("action_audio".equals(key) || title.contains("অডিও")) {
      showIslamicAudioHub();
    } else if ("action_books".equals(key) || title.contains("বই")) {
      showIslamicBooksHubSheet();
    } else if ("action_hadith".equals(key) || title.contains("হাদিস শরিফ")) {
      showHadithViewerSheet();
    } else if ("action_dua".equals(key) || title.contains("দোয়া")) {
      showDuaSheet();
    } else if ("action_amal".equals(key) || title.contains("আমল ট্র্যাকার")) {
      showAmalTrackerSheet();
    } else if ("action_quiz".equals(key) || title.contains("কুইজ")) {
      showQuizSheet();
    } else if ("action_mosque".equals(key) || title.contains("মসজিদ সন্ধান")) {
      showMosqueFinderSheet();
    } else if ("action_safar".equals(key) || title.contains("সফর মোড")) {
      showTravelModeSheet();
    } else if ("action_jummah".equals(key) || title.contains("জুম্মা")) {
      showJummahModeSheet();
    } else if ("action_eid".equals(key) || title.contains("ঈদ")) {
      showEidModeSheet();
    } else if ("action_janaza".equals(key) || title.contains("জানাযা")) {
      showJanazaGuideSheet();
    } else if ("action_roza_ramadan".equals(key) || "action_roza".equals(key) || "action_ramadan".equals(key) || title.contains("রোজা") || title.contains("রমজান") || title.contains("Fasting") || title.contains("Ramadan")) {
      com.devflux.deenone.features.ramadan.ui.UnifiedRamadanRozaPageDialog.show(this, viewModel != null ? viewModel.getCurrentCoordinates() : null);
    } else if ("action_prophets".equals(key) || title.contains("নবী") || title.contains("Prophet")) {
      com.devflux.deenone.features.prophets.ui.ProphetsStoriesPageDialog.show(this);
    } else if ("action_khatm".equals(key) || title.contains("খতম")) {
      showQuranKhatmPlannerSheet();
    } else if ("action_daily_ayah".equals(key) || title.contains("আজকের আয়াত")) {
      showAyahOfTheDaySheet();
    } else if ("action_hajj".equals(key) || title.contains("হজ ও উমরাহ")) {
      showHajjGuideSheet();
    } else if ("action_calendar".equals(key) || title.contains("ক্যালেন্ডার")) {
      showIslamicCalendarSheet();
    } else if ("action_zakat".equals(key) || title.contains("যাকাত")) {
      showZakatSheet();
    } else if ("action_tasbih".equals(key) || title.contains("তাসবিহ")) {
      showTasbihSheet();
    } else if ("action_azkar".equals(key) || title.contains("আজকার")) {
      showAzkarSheet();
    } else if ("action_qibla".equals(key) || title.contains("কিবলা")) {
      showQiblaSheet();
    } else if ("action_marriage".equals(key) || "action_journey".equals(key) || title.contains("বিবাহ") || title.contains("বিয়ে") || title.contains("Marriage")) {
      showMuslimMarriageSheet();
    } else if ("action_faraid".equals(key) || title.contains("উত্তরাধিকার") || title.contains("ফারায়েজ") || title.contains("ফরায়েজ")) {
      showFaraidCalculatorSheet();
    } else if ("action_sleep".equals(key) || title.contains("স্লিপ")) {
      showSleepModeSheet();
    } else if ("action_blood".equals(key) || title.contains("রক্তদান")) {
      showBloodDonationSheet();
    } else if ("action_allah_names".equals(key) || title.contains("আল্লাহর ৯৯ নাম") || title.contains("৯৯ নাম") || title.contains("আসমাউল")) {
      showAllahNamesSheet();
    } else if ("action_six_kalima".equals(key) || title.contains("৬ কালিমা") || title.contains("কালিমা")) {
      showSixKalimaSheet();
    } else if ("action_knowledge_battle".equals(key) || title.contains("নলেজ ব্যাটেল") || title.contains("ব্যাটেল")) {
      showKnowledgeBattleSheet();
    } else if ("action_halal".equals(key) || title.contains("হালাল") || title.contains("খাদ্য") || title.contains("পুষ্টি") || title.contains("ই-কোড")) {
      showHalalFoodsSheet();
    } else {
      showAmalTrackerSheet();
    }
  }

  public void showMuslimMarriageSheet() {
    try {
      com.devflux.deenone.features.marriage.MuslimMarriagePageDialog.show(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showKnowledgeBattleSheet() {
    try {
      com.devflux.deenone.features.battle.KnowledgeBattlePageDialog.show(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void showFaraidCalculatorSheet() {
    try {
      com.devflux.deenone.features.faraid.ui.FaraidCalculatorPageDialog.show(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showForbiddenTimesSheet() {
    com.devflux.deenone.core.location.LocationProvider.Coordinates coords = viewModel.getCurrentCoordinates();
    PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocationWithContext(
        this, coords.latitude, coords.longitude, coords.timezone, java.util.Calendar.getInstance()
    );
    com.devflux.deenone.features.prayer.SalahTrackerPageDialog.showForbiddenTimesBottomSheet(this, res);
  }

  private void showHadithViewerSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetHadithViewerBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetHadithViewerBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseHadith);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnBackFromChapters);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnBackFromReader);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnReaderSettings);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnBackToCategories);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvHadithViewerTitle.setText(isBn ? "হাদিস" : "Hadith");

    // Setup Hadiths ViewModel & Adapter (for fallback/search)
    com.devflux.deenone.features.hadith.HadithViewModel hadithVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.hadith.HadithViewModel.class);

    com.devflux.deenone.features.hadith.adapter.HadithAdapter hadithAdapter =
        new com.devflux.deenone.features.hadith.adapter.HadithAdapter(new ArrayList<>(), (item, pos) -> {
          hadithVm.toggleBookmark(item);
        });
    hadithAdapter.setLanguage(isBn ? "bn" : "en");
    sheetBinding.rvHadithList.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvHadithList.setAdapter(hadithAdapter);

    hadithVm.getHadithList().observe(this, list -> {
      if (list != null) {
        hadithAdapter.updateData(list);
      }
    });

    final String[] currentSelectedBookSlug = new String[]{""};
    final String[] currentSelectedBookName = new String[]{""};

    // Setup Hadith Reader Adapter
    com.devflux.deenone.features.hadith.adapter.HadithReaderAdapter hadithReaderAdapter =
        new com.devflux.deenone.features.hadith.adapter.HadithReaderAdapter((hadithItem, anchorView) -> {
          com.devflux.deenone.features.hadith.ui.HadithReaderHelper.showMoreOptionsBottomSheet(
              MainActivity.this, hadithItem, anchorView, () -> {}
          );
        });
    sheetBinding.rvChapterHadithReader.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvChapterHadithReader.setAdapter(hadithReaderAdapter);

    sheetBinding.btnBackFromReader.setOnClickListener(v -> {
      sheetBinding.loadingViewHadith.hide();
      sheetBinding.layoutHadithReaderView.setVisibility(View.GONE);
      sheetBinding.layoutChapterView.setVisibility(View.VISIBLE);
    });

    sheetBinding.btnReaderSettings.setOnClickListener(v -> {
      com.devflux.deenone.features.hadith.ui.HadithReaderHelper.showDisplaySettingsBottomSheet(
          MainActivity.this, hadithReaderAdapter
      );
    });

    // Setup Hadith Chapters Adapter
    com.devflux.deenone.features.hadith.adapter.HadithChapterAdapter chapterAdapter =
        new com.devflux.deenone.features.hadith.adapter.HadithChapterAdapter(this, chapter -> {
          // Switch to Hadith Reader View for selected chapter
          sheetBinding.layoutChapterView.setVisibility(View.GONE);
          sheetBinding.layoutHadithReaderView.setVisibility(View.VISIBLE);

          String chapTitle = isBn ? chapter.getTitleBn() : chapter.getTitleEn();
          sheetBinding.tvReaderChapterTitle.setText(chapTitle);

          // Clear previous hadiths immediately to avoid stale data
          hadithReaderAdapter.setItems(new ArrayList<>());
          sheetBinding.rvChapterHadithReader.setVisibility(View.GONE);
          sheetBinding.layoutEmptyHadiths.setVisibility(View.GONE);
          sheetBinding.loadingViewHadith.show();
          sheetBinding.loadingViewHadith.setMessage(isBn ? "হাদিস লোড হচ্ছে..." : "Loading Hadiths...");

          // Fetch and display Hadiths & Section Headers for this chapter
          com.devflux.deenone.features.hadith.repository.ChapterHadithRepository.getInstance().getChapterHadiths(
              MainActivity.this,
              chapter.getBookSlug(),
              chapter.getChapterNumber(),
              readerItems -> {
                sheetBinding.loadingViewHadith.hide();
                if (readerItems != null && !readerItems.isEmpty()) {
                  hadithReaderAdapter.setItems(readerItems);
                  sheetBinding.rvChapterHadithReader.setVisibility(View.VISIBLE);
                  sheetBinding.layoutEmptyHadiths.setVisibility(View.GONE);
                } else {
                  hadithReaderAdapter.setItems(new ArrayList<>());
                  sheetBinding.rvChapterHadithReader.setVisibility(View.GONE);
                  sheetBinding.layoutEmptyHadiths.setVisibility(View.VISIBLE);
                }
              }
          );
        });

    sheetBinding.rvHadithChapters.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvHadithChapters.setAdapter(chapterAdapter);

    // Setup 25 Hadith Categories Adapter
    com.devflux.deenone.features.hadith.adapter.HadithCategoryAdapter categoryAdapter =
        new com.devflux.deenone.features.hadith.adapter.HadithCategoryAdapter(this, category -> {
          currentSelectedBookSlug[0] = category.getSlug();
          currentSelectedBookName[0] = category.getDisplayName(isBn);

          sheetBinding.tvChapterBookTitle.setText(category.getDisplayName(isBn));
          sheetBinding.etSearchChapter.setText("");

          // Clear previous chapters immediately to avoid cross-category duplicate pollution
          chapterAdapter.setChapters(new ArrayList<>());
          sheetBinding.rvHadithChapters.setVisibility(View.GONE);
          sheetBinding.layoutEmptyChapters.setVisibility(View.GONE);

          // Switch to Chapter View
          sheetBinding.layoutHadithHeader.setVisibility(View.GONE);
          sheetBinding.rvHadithCategories.setVisibility(View.GONE);
          sheetBinding.rvHadithList.setVisibility(View.GONE);
          sheetBinding.layoutChapterView.setVisibility(View.VISIBLE);

          if (!com.devflux.deenone.features.hadith.repository.HadithDatabaseManager.getInstance(MainActivity.this).isDatabaseReady()) {
            sheetBinding.loadingViewHadith.show();
            sheetBinding.loadingViewHadith.setMessage(isBn ? "হাদিস ডাটাবেজ প্রস্তুত হচ্ছে..." : "Preparing Hadith Database...");
          }

          // Load Chapters strictly for this specific book
          com.devflux.deenone.features.hadith.repository.HadithChapterRepository.getInstance()
              .loadChapters(this, category.getSlug(), chapters -> {
                sheetBinding.loadingViewHadith.hide();
                if (chapters != null && !chapters.isEmpty()) {
                  chapterAdapter.setChapters(chapters);
                  sheetBinding.rvHadithChapters.setVisibility(View.VISIBLE);
                  sheetBinding.layoutEmptyChapters.setVisibility(View.GONE);
                } else {
                  chapterAdapter.setChapters(new ArrayList<>());
                  sheetBinding.rvHadithChapters.setVisibility(View.GONE);
                  sheetBinding.layoutEmptyChapters.setVisibility(View.VISIBLE);
                }
              });
        });

    sheetBinding.rvHadithCategories.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvHadithCategories.setAdapter(categoryAdapter);

    // Progress Listener for background SQLite Database download from GitHub CDN
    com.devflux.deenone.features.hadith.repository.HadithDatabaseManager.DownloadProgressListener progressListener =
        (percent, currentBytes, totalBytes) -> {
          if (sheetBinding.loadingViewHadith.getVisibility() == View.VISIBLE) {
            String msg = isBn
                ? ("হাদিস ডাটাবেজ ডাউনলোড ও প্রস্তুত হচ্ছে... " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(percent) + "%")
                : ("Downloading Hadith Database... " + percent + "%");
            sheetBinding.loadingViewHadith.setMessage(msg);
          }
        };
    com.devflux.deenone.features.hadith.repository.HadithDatabaseManager.getInstance(this).addProgressListener(progressListener);

    // Back from Chapters Screen to 25 Categories Screen
    sheetBinding.btnBackFromChapters.setOnClickListener(v -> {
      sheetBinding.loadingViewHadith.hide();
      sheetBinding.layoutChapterView.setVisibility(View.GONE);
      sheetBinding.layoutHadithHeader.setVisibility(View.VISIBLE);
      sheetBinding.layoutSelectedCategoryBar.setVisibility(View.GONE);
      sheetBinding.rvHadithCategories.setVisibility(View.VISIBLE);
      sheetBinding.rvHadithList.setVisibility(View.GONE);
      currentSelectedBookSlug[0] = "";
      categoryAdapter.filter("");
      com.devflux.deenone.features.hadith.repository.HadithCategoryRepository.getInstance()
          .getCategories(MainActivity.this, categoryAdapter::setCategories);
    });

    // Back to Chapters Screen (or Categories)
    sheetBinding.btnBackToCategories.setOnClickListener(v -> {
      sheetBinding.loadingViewHadith.hide();
      if (!currentSelectedBookSlug[0].isEmpty()) {
        sheetBinding.layoutSelectedCategoryBar.setVisibility(View.GONE);
        sheetBinding.layoutHadithHeader.setVisibility(View.GONE);
        sheetBinding.rvHadithList.setVisibility(View.GONE);
        sheetBinding.layoutChapterView.setVisibility(View.VISIBLE);
      } else {
        sheetBinding.layoutSelectedCategoryBar.setVisibility(View.GONE);
        sheetBinding.rvHadithCategories.setVisibility(View.VISIBLE);
        sheetBinding.rvHadithList.setVisibility(View.GONE);
        categoryAdapter.filter("");
        hadithVm.setCollection("all");
      }
    });

    // Search filter in Chapters view
    sheetBinding.etSearchChapter.addTextChangedListener(new android.text.TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        chapterAdapter.getFilter().filter(s);
      }
      @Override public void afterTextChanged(android.text.Editable s) {}
    });

    // Load & Synchronize 25 Hadith Categories with Real Database Counts
    com.devflux.deenone.features.hadith.repository.HadithCategoryRepository.getInstance()
        .getCategories(this, categories -> {
          categoryAdapter.setCategories(categories);
        });

    sheetBinding.btnCloseHadith.setOnClickListener(v -> {
      com.devflux.deenone.features.hadith.repository.HadithDatabaseManager.getInstance(MainActivity.this).removeProgressListener(progressListener);
      dialog.dismiss();
    });
    dialog.setOnDismissListener(d -> {
      com.devflux.deenone.features.hadith.repository.HadithDatabaseManager.getInstance(MainActivity.this).removeProgressListener(progressListener);
    });
    dialog.show();
  }

  private void showDuaSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetDuaHubBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetDuaHubBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());
    // Full screen page mode

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvDuaHubMainTitle.setText(isBn ? "দোয়া ভাণ্ডার" : "Dua Hub");
    sheetBinding.tvDuaHubMainSubtitle.setText(isBn ? "কুরআন ও সুন্নাহর প্রামাণ্য মাসনুন দোয়া" : "Authentic Masnoon Duas from Quran & Sunnah");
    sheetBinding.etSearchDua.setHint(isBn ? "দোয়া, আরবি বা ফজিলত দিয়ে অনুসন্ধান করুন..." : "Search Dua by title, arabic or virtue...");
    sheetBinding.tvOpenDuaTracker.setText(isBn ? "দোয়া ট্র্যাকার" : "Dua Tracker");

    com.devflux.deenone.features.dua.DuaViewModel duaVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.dua.DuaViewModel.class);

    com.devflux.deenone.features.dua.adapter.DuaAdapter adapter =
        new com.devflux.deenone.features.dua.adapter.DuaAdapter(
            new ArrayList<>(),
            (item, pos) -> {
              duaVm.toggleFavorite(item);
            },
            (item, pos) -> {
              duaVm.markDuaAsRead(item);
              com.devflux.deenone.core.gamification.GamificationManager.addXP(MainActivity.this, 10);
            }
        );

    sheetBinding.rvDuaList.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvDuaList.setAdapter(adapter);

    duaVm.ensureSeedDuasLoaded();

    duaVm.getTotalDuaCountLive().observe(this, count -> {
      int c = (count != null) ? count : 0;
      sheetBinding.tvDuaCountBadge.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(c) + "টি প্রামাণ্য মাসনুন দোয়া সংরক্ষিত") : (c + " Masnoon Duas Stored"));
    });

    sheetBinding.btnSyncOnlineDua.setOnClickListener(v -> duaVm.triggerOnlineSync());

    duaVm.getIsSyncing().observe(this, isSyncing -> {
      sheetBinding.pbDuaSync.setVisibility(Boolean.TRUE.equals(isSyncing) ? View.VISIBLE : View.GONE);
      sheetBinding.btnSyncOnlineDua.setEnabled(!Boolean.TRUE.equals(isSyncing));
      sheetBinding.btnSyncOnlineDua.setAlpha(Boolean.TRUE.equals(isSyncing) ? 0.6f : 1.0f);
    });

    duaVm.getSyncStatusMessage().observe(this, msg -> {
      if (msg != null) {
        sheetBinding.tvDuaSyncStatus.setText(msg);
      }
    });

    duaVm.getDuas().observe(this, list -> {
      if (list != null) {
        adapter.updateData(list);
      }
    });

    // Language setup (auto from app locale per Rule 5)
    adapter.setLanguage(isBn ? "bn" : "en");

    // Category Dropdown Selection
    String[] catKeys = new String[]{
        "all", "Favorites",
        "Morning Dua", "Evening Dua",
        "Before Sleeping", "After Waking",
        "Before Eating", "After Eating",
        "Travel", "Protection",
        "Anxiety/Worry", "Forgiveness",
        "Parents", "Rizq",
        "Health", "Guidance",
        "Ramadan", "Hajj",
        "Umrah", "Salah-related Duas",
        "Daily Life"
    };

    String[] catTitles = isBn ? new String[]{
        "সকল দোয়া", "প্রিয় দোয়া",
        "সকালের দোয়া", "সন্ধ্যার দোয়া",
        "ঘুমানোর পূর্বে", "ঘুম থেকে ওঠার পর",
        "খাওয়ার পূর্বে", "খাওয়ার পর",
        "সফর ও ভ্রমণ", "সুরক্ষা ও হেফাজত",
        "দুশ্চিন্তা ও পেরেশানি", "ক্ষমা ও তাওবা",
        "পিতামাতা", "রিজিক ও বরকত",
        "সুস্থতা ও রোগমুক্তি", "হেদায়াত",
        "রমজান ও রোজা", "হজ",
        "উমরাহ", "সালাত সম্পর্কিত",
        "দৈনন্দিন জীবন"
    } : new String[]{
        "All Duas", "Favorites",
        "Morning Dua", "Evening Dua",
        "Before Sleep", "Waking Up",
        "Before Eating", "After Eating",
        "Travel", "Protection",
        "Anxiety & Worry", "Forgiveness",
        "Parents", "Rizq & Blessing",
        "Health & Healing", "Guidance",
        "Ramadan & Fasting", "Hajj",
        "Umrah", "Salah-related",
        "Daily Life"
    };

    final int[] selectedCategoryIndex = {0};
    sheetBinding.tvSelectedCategory.setText(catTitles[0]);

    sheetBinding.btnDuaCategoryDropdown.setOnClickListener(v -> {
      new com.google.android.material.dialog.MaterialAlertDialogBuilder(MainActivity.this)
          .setTitle(isBn ? "দোয়া ক্যাটাগরি নির্বাচন করুন" : "Select Dua Category")
          .setSingleChoiceItems(catTitles, selectedCategoryIndex[0], (d, which) -> {
            selectedCategoryIndex[0] = which;
            sheetBinding.tvSelectedCategory.setText(catTitles[which]);
            duaVm.setCategory(catKeys[which]);
            d.dismiss();
          })
          .show();
    });

    // Search
    sheetBinding.etSearchDua.addTextChangedListener(new android.text.TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        duaVm.searchDuas(s.toString()).observe(MainActivity.this, results -> {
          if (results != null) adapter.updateData(results);
        });
      }
      @Override public void afterTextChanged(android.text.Editable s) {}
    });

    // Theme Icon reflection
    boolean isDark = (getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
        == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    sheetBinding.ivDuaThemeIcon.setImageResource(isDark ? R.drawable.ic_sun : R.drawable.ic_moon);

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnOpenDuaTracker);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseDua);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDuaThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDuaNotification);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnSyncOnlineDua);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDuaCategoryDropdown);

    sheetBinding.btnOpenDuaTracker.setOnClickListener(v -> showDuaTrackerSheet());
    sheetBinding.btnCloseDua.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnDuaThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });
    sheetBinding.btnDuaNotification.setOnClickListener(v -> showNotificationHistorySheet());
    dialog.show();
  }

  private void showDuaTrackerSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetDuaTrackerBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetDuaTrackerBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());
    // Full screen page mode

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvDuaTrackerTitle.setText(isBn ? "দোয়া ট্র্যাকার" : "Dua Tracker");
    sheetBinding.tvDuaTrackerSubtitle.setText(isBn ? "ব্যক্তিগত দোয়া পাঠ ও ইবাদতের হিসাব" : "Personal Dua Tracking & Daily Amal");
    sheetBinding.tvDuaHistoryTitle.setText(isBn ? "সাম্প্রতিক দোয়া পাঠের ইতিহাস" : "Recent Dua Recitation History");

    com.devflux.deenone.features.dua.DuaViewModel duaVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.dua.DuaViewModel.class);

    // 1. Observe Today's Count & Daily Goal
    final int[] currentGoal = {7};
    final int[] currentCount = {0};

    duaVm.getDailyGoal().observe(this, goal -> {
      if (goal != null) {
        currentGoal[0] = goal;
        updateDuaGoalUI(sheetBinding, goal);
        updateDuaProgressUI(sheetBinding, currentCount[0], currentGoal[0]);
      }
    });

    duaVm.getTodayDuaCount().observe(this, count -> {
      currentCount[0] = (count != null) ? count : 0;
      updateDuaProgressUI(sheetBinding, currentCount[0], currentGoal[0]);
    });

    // Goal Chip Listeners
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseDuaTracker);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnGoal3);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnGoal5);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnGoal7);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnGoal10);

    sheetBinding.btnGoal3.setOnClickListener(v -> duaVm.setDailyGoal(3));
    sheetBinding.btnGoal5.setOnClickListener(v -> duaVm.setDailyGoal(5));
    sheetBinding.btnGoal7.setOnClickListener(v -> duaVm.setDailyGoal(7));
    sheetBinding.btnGoal10.setOnClickListener(v -> duaVm.setDailyGoal(10));

    // 2. Most Read Dua
    duaVm.getMostReadDua().observe(this, stat -> {
      if (stat != null && stat.count > 0) {
        sheetBinding.tvMostReadTitle.setText(stat.duaTitle);
        sheetBinding.tvMostReadCount.setText(isBn ? ("পাঠ সংখ্যা: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(stat.count) + " বার") : ("Read count: " + stat.count + " times"));
      } else {
        sheetBinding.tvMostReadTitle.setText(isBn ? "এখনো কোনো রেকর্ড নেই" : "No records yet");
        sheetBinding.tvMostReadCount.setText(isBn ? "পাঠ সংখ্যা: ০ বার" : "Read count: 0 times");
      }
    });

    // 3. Total Dua Count
    duaVm.getTotalDuaCount().observe(this, total -> {
      int val = (total != null) ? total : 0;
      sheetBinding.tvTotalReadCount.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(val) + "টি") : (val + " total"));
    });

    // 4. Category-wise Usage
    duaVm.getCategoryUsage().observe(this, list -> {
      sheetBinding.layoutCategoryUsageContainer.removeAllViews();
      if (list == null || list.isEmpty()) {
        TextView emptyTv = new TextView(this);
        emptyTv.setText(isBn ? "কোনো দোয়া পাঠের রেকর্ড পাওয়া যায়নি। দোয়া পড়ার পর 'পড়া হয়েছে' ট্যাপ করুন।" : "No Dua logs found yet. Tap 'Mark as Read' after reciting a Dua.");
        emptyTv.setTextColor(getColor(R.color.text_secondary));
        emptyTv.setTextSize(12);
        sheetBinding.layoutCategoryUsageContainer.addView(emptyTv);
      } else {
        for (com.devflux.deenone.data.local.dao.DuaLogDao.CategoryUsageStat stat : list) {
          LinearLayout row = new LinearLayout(this);
          row.setOrientation(LinearLayout.HORIZONTAL);
          row.setPadding(0, 6, 0, 6);

          TextView tvCat = new TextView(this);
          tvCat.setText(isBn ? com.devflux.deenone.features.dua.adapter.DuaAdapter.DuaViewHolder.getBengaliCategoryName(stat.category) : stat.category);
          tvCat.setTextColor(getColor(R.color.text_primary));
          tvCat.setTextSize(13);
          LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
          tvCat.setLayoutParams(lp1);

          TextView tvCount = new TextView(this);
          tvCount.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(stat.count) + " বার পঠিত") : (stat.count + " times recited"));
          tvCount.setTextColor(getColor(R.color.accent_mint));
          tvCount.setTextSize(12);
          tvCount.setTypeface(null, android.graphics.Typeface.BOLD);

          row.addView(tvCat);
          row.addView(tvCount);
          sheetBinding.layoutCategoryUsageContainer.addView(row);
        }
      }
    });

    // 5. Recent History
    java.text.SimpleDateFormat timeFmt = new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.ENGLISH);
    duaVm.getRecentHistory(8).observe(this, logs -> {
      sheetBinding.layoutHistoryContainer.removeAllViews();
      if (logs == null || logs.isEmpty()) {
        TextView emptyTv = new TextView(this);
        emptyTv.setText(isBn ? "এখনো কোনো দোয়া পড়া হয়নি।" : "No Duas recited yet.");
        emptyTv.setTextColor(getColor(R.color.text_secondary));
        emptyTv.setTextSize(12);
        sheetBinding.layoutHistoryContainer.addView(emptyTv);
      } else {
        for (com.devflux.deenone.data.local.entity.DuaLogEntity log : logs) {
          LinearLayout row = new LinearLayout(this);
          row.setOrientation(LinearLayout.HORIZONTAL);
          row.setPadding(0, 6, 0, 6);

          TextView tvTitle = new TextView(this);
          tvTitle.setText(log.getDuaTitle());
          tvTitle.setTextColor(getColor(R.color.text_primary));
          tvTitle.setTextSize(13);
          tvTitle.setEllipsize(android.text.TextUtils.TruncateAt.END);
          tvTitle.setMaxLines(1);
          LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
          tvTitle.setLayoutParams(lp);

          TextView tvTime = new TextView(this);
          tvTime.setText(timeFmt.format(new java.util.Date(log.getReadTimestamp())));
          tvTime.setTextColor(getColor(R.color.text_secondary));
          tvTime.setTextSize(11);

          row.addView(tvTitle);
          row.addView(tvTime);
          sheetBinding.layoutHistoryContainer.addView(row);
        }
      }
    });

    sheetBinding.btnCloseDuaTracker.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  private void updateDuaGoalUI(com.devflux.deenone.databinding.BottomSheetDuaTrackerBinding sheetBinding, int goal) {
    TextView[] chips = new TextView[]{sheetBinding.btnGoal3, sheetBinding.btnGoal5, sheetBinding.btnGoal7, sheetBinding.btnGoal10};
    int[] goals = new int[]{3, 5, 7, 10};

    for (int i = 0; i < chips.length; i++) {
      if (goals[i] == goal) {
        chips[i].setBackgroundResource(R.drawable.bg_card_active);
        chips[i].setTextColor(getColor(R.color.accent_mint));
        chips[i].setTypeface(null, android.graphics.Typeface.BOLD);
      } else {
        chips[i].setBackgroundResource(R.drawable.bg_badge_pill);
        chips[i].setTextColor(getColor(R.color.text_secondary));
        chips[i].setTypeface(null, android.graphics.Typeface.NORMAL);
      }
    }
  }

  private void updateDuaProgressUI(com.devflux.deenone.databinding.BottomSheetDuaTrackerBinding sheetBinding, int count, int goal) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvTodayDuaFraction.setText(
        isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(count) + "/" +
            com.devflux.deenone.utils.BengaliNumberUtil.toBengali(goal) + "টি সম্পন্ন")
             : (count + "/" + goal + " completed")
    );
    int pct = goal > 0 ? Math.min(100, (count * 100) / goal) : 0;
    sheetBinding.progressDuaToday.setProgress(pct);
  }

  public void showQuizSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetQuizHubBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetQuizHubBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseQuizSheet);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnQuizThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnQuizNotification);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnQuizRankListSheet);

    sheetBinding.btnCloseQuizSheet.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnQuizThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });
    sheetBinding.btnQuizNotification.setOnClickListener(v -> showNotificationHistorySheet());

    // Quiz Hub Hero Card inside sheet ( হুবহু স্ক্রিনশট অনুযায়ী ব্যানার )
    sheetBinding.btnQuizRankListSheet.setOnClickListener(v -> com.devflux.deenone.features.quiz.dialog.QuizRankPageDialog.show(MainActivity.this));

    boolean isBnLocale = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvQuizTagBadgeSheet.setText(isBnLocale ? "ঈমান বৃদ্ধি ও যাচাই" : "Iman Boost & Assessment");
    sheetBinding.tvQuizHubMainTitleSheet.setText(isBnLocale ? "কুইজ খেলে জ্ঞান অর্জন করুন" : "Gain Knowledge by Playing Quiz");
    sheetBinding.tvQuizHubMainSubtitleSheet.setText(isBnLocale ? "ইসলামিক কুইজ প্রতিযোগিতায় অংশ নিয়ে আপনার মেধা যাচাই করুন এবং পয়েন্ট অর্জন করুন।" : "Participate in Islamic quiz competitions, test your intellect, and earn reward points.");
    sheetBinding.tvQuizRankBtnTextSheet.setText(isBnLocale ? "কুইজ র‍্যাংক তালিকা" : "Quiz Rank List");

    sheetBinding.tvQuizHubTitle.setText(isBnLocale ? "ইসলামিক কুইজ হাব" : "Islamic Quiz Hub");
    sheetBinding.tvQuizHubSubtitle.setText(isBnLocale ? "দৈনিক কুইজ ও সহীহ হাদিস রেফারেন্স" : "Daily Quizzes with Sahih Hadith References");
    sheetBinding.tvQuizCategoriesSectionTitle.setText(isBnLocale ? "কুইজ বিষয় ও ক্যাটাগরি" : "Quiz Topics & Categories");

    int initPoints = com.devflux.deenone.core.quiz.QuizManager.getInstance().getDeenPoints(this);
    sheetBinding.tvQuizPointsBadge.setText(isBnLocale ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(initPoints) + " পয়েন্ট") : (initPoints + " Points"));

    // Vertical Category List Setup (হুবহু স্ক্রিনশট স্পেসিফিকেশন)
    com.devflux.deenone.features.quiz.adapter.QuizCategoryVerticalAdapter verticalCategoryAdapter =
        new com.devflux.deenone.features.quiz.adapter.QuizCategoryVerticalAdapter(category -> {
          if (com.devflux.deenone.features.quiz.repository.QuizResultStorage.isCategoryCompletedToday(MainActivity.this, category.getId())) {
            com.devflux.deenone.features.quiz.dialog.QuizResultPageDialog.showForCategoryIfCompleted(MainActivity.this, category.getId());
          } else {
            com.devflux.deenone.features.quiz.dialog.QuizPlayPageDialog.show(
                MainActivity.this,
                category.getId(),
                isBnLocale ? category.getTitleBn() : category.getTitleEn()
            );
          }
        });
    sheetBinding.rvQuizCategoriesVertical.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvQuizCategoriesVertical.setAdapter(verticalCategoryAdapter);
    List<com.devflux.deenone.features.battle.model.BattleCategory> allCats =
        com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository.getAllCategories();
    verticalCategoryAdapter.setCategories(allCats);
    sheetBinding.tvQuizCategoriesCountBadge.setText(isBnLocale
        ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(allCats.size()) + " টি বিষয়")
        : (allCats.size() + " Topics"));

    // Trigger automatic silent background sync from DeenOne PHP backend & verified Islamic APIs (throttled)
    com.devflux.deenone.core.quiz.QuizManager.getInstance().syncOnlineQuestionsAsync(this, null);

    dialog.show();
  }

  // =========================================================================
  // 4-Madhhab Comparative Fiqh Information Sheet (চার মাযহাবের তথ্যভাণ্ডার)
  // =========================================================================
  private void showMadhhabInfoSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetMadhhabInfoBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetMadhhabInfoBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    sheetBinding.btnCloseMadhhabSheet.setOnClickListener(v -> dialog.dismiss());

    java.lang.Runnable renderIssues = () -> {
      String query = sheetBinding.etMadhhabSearch.getText().toString();
      List<com.devflux.deenone.core.madhhab.MadhhabManager.MadhhabIssueItem> issues =
          com.devflux.deenone.core.madhhab.MadhhabManager.getInstance().searchIssues(query);

      sheetBinding.layoutMadhhabIssuesContainer.removeAllViews();
      for (com.devflux.deenone.core.madhhab.MadhhabManager.MadhhabIssueItem issue : issues) {
        com.devflux.deenone.databinding.ItemMadhhabComparisonCardBinding itemBinding =
            com.devflux.deenone.databinding.ItemMadhhabComparisonCardBinding.inflate(LayoutInflater.from(this), sheetBinding.layoutMadhhabIssuesContainer, false);

        itemBinding.tvIssueTopic.setText(issue.topic);
        itemBinding.tvHanafiView.setText(issue.hanafiView);
        itemBinding.tvHanafiRef.setText("সূত্র: "+ issue.hanafiRef);

        itemBinding.tvMalikiView.setText(issue.malikiView);
        itemBinding.tvMalikiRef.setText("সূত্র: "+ issue.malikiRef);

        itemBinding.tvShafiiView.setText(issue.shafiiView);
        itemBinding.tvShafiiRef.setText("সূত্র: "+ issue.shafiiRef);

        itemBinding.tvHanbaliView.setText(issue.hanbaliView);
        itemBinding.tvHanbaliRef.setText("সূত্র: "+ issue.hanbaliRef);

        itemBinding.tvDalilSummary.setText("দলীল ও ব্যাখ্যা: "+ issue.dalilSummary);

        sheetBinding.layoutMadhhabIssuesContainer.addView(itemBinding.getRoot());
      }
    };

    sheetBinding.etMadhhabSearch.addTextChangedListener(new android.text.TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        renderIssues.run();
      }
      @Override
      public void afterTextChanged(android.text.Editable s) {}
    });

    renderIssues.run();
    dialog.show();
  }

  // =========================================================================
  // Salah Jamaat Times Sheet (মসজিদভিত্তিক জামাতের সময়সূচী)
  // =========================================================================
  private void showJamaatTimesSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetJamaatTimesBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetJamaatTimesBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    sheetBinding.btnCloseJamaatSheet.setOnClickListener(v -> dialog.dismiss());

    java.lang.Runnable updateTimes = () -> {
      com.devflux.deenone.core.jamaat.JamaatPrayerManager.MosqueJamaatSchedule schedule =
          com.devflux.deenone.core.jamaat.JamaatPrayerManager.getInstance().getActiveMosqueSchedule(this);

      sheetBinding.tvMosqueNameSubtitle.setText(schedule.mosqueName + "("+ schedule.location + ")");

      if (schedule.isVerified && schedule.fajrJamaat != null) {
        sheetBinding.tvJamaatFajr.setText(schedule.fajrJamaat);
        sheetBinding.tvJamaatDhuhr.setText(schedule.dhuhrJamaat);
        sheetBinding.tvJamaatAsr.setText(schedule.asrJamaat);
        sheetBinding.tvJamaatMaghrib.setText(schedule.maghribJamaat);
        sheetBinding.tvJamaatIsha.setText(schedule.ishaJamaat);
        sheetBinding.tvJamaatJummah.setText(schedule.jummahJamaat);
      } else {
        String na = "তথ্য নেই (মসজিদ থেকে জেনে নিন)";
        sheetBinding.tvJamaatFajr.setText(na);
        sheetBinding.tvJamaatDhuhr.setText(na);
        sheetBinding.tvJamaatAsr.setText(na);
        sheetBinding.tvJamaatMaghrib.setText(na);
        sheetBinding.tvJamaatIsha.setText(na);
        sheetBinding.tvJamaatJummah.setText(na);
      }
    };

    sheetBinding.chipMosqueBaitul.setOnClickListener(v -> {
      sheetBinding.chipMosqueBaitul.setTextColor(android.graphics.Color.parseColor("#34D399"));
      sheetBinding.chipMosqueKakrail.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.chipMosqueCustom.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      com.devflux.deenone.core.jamaat.JamaatPrayerManager.getInstance().selectMosque(this, "baitul_mukarram");
      updateTimes.run();
    });

    sheetBinding.chipMosqueKakrail.setOnClickListener(v -> {
      sheetBinding.chipMosqueBaitul.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.chipMosqueKakrail.setTextColor(android.graphics.Color.parseColor("#34D399"));
      sheetBinding.chipMosqueCustom.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      com.devflux.deenone.core.jamaat.JamaatPrayerManager.getInstance().selectMosque(this, "kakrail");
      updateTimes.run();
    });

    sheetBinding.chipMosqueCustom.setOnClickListener(v -> {
      sheetBinding.chipMosqueBaitul.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.chipMosqueKakrail.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.chipMosqueCustom.setTextColor(android.graphics.Color.parseColor("#34D399"));
      com.devflux.deenone.core.jamaat.JamaatPrayerManager.getInstance().selectMosque(this, "custom");
      updateTimes.run();
    });

    updateTimes.run();
    dialog.show();
  }

  // =========================================================================
  // Islamic Fasting Tracker Sheet (সিয়াম ও রোজা ট্র্যাকার)
  // =========================================================================
  private void showFastingTrackerSheet() {
    com.devflux.deenone.features.ramadan.ui.UnifiedRamadanRozaPageDialog.show(this, viewModel != null ? viewModel.getCurrentCoordinates() : null);
  }

  // =========================================================================
  // Quran Khatm Planner Sheet (কুরআন খতম প্ল্যানার)
  // =========================================================================
  private void showQuranKhatmPlannerSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetQuranKhatmPlannerBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetQuranKhatmPlannerBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseKhatmSheet);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipPlan7Days);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipPlan15Days);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipPlan30Days);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAdd4Pages);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAdd20Pages);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnResetKhatmPlan);

    sheetBinding.btnCloseKhatmSheet.setOnClickListener(v -> dialog.dismiss());

    java.lang.Runnable updateView = () -> {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      int planDays = com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().getPlanDays(this);
      int pagesRead = com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().getPagesRead(this);
      int dailyTarget = com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().getDailyTargetPages(planDays);
      int perPrayerTarget = com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().getPerPrayerTargetPages(dailyTarget);
      int percent = com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().getProgressPercentage(pagesRead);

      sheetBinding.chipPlan7Days.setTextColor(planDays == 7 ? android.graphics.Color.parseColor("#34D399") : android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.chipPlan15Days.setTextColor(planDays == 15 ? android.graphics.Color.parseColor("#34D399") : android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.chipPlan30Days.setTextColor(planDays == 30 ? android.graphics.Color.parseColor("#34D399") : android.graphics.Color.parseColor("#9CA3AF"));

      sheetBinding.tvKhatmProgressText.setText(isBn ? ("খতম প্রগ্রেস: " + BengaliNumberUtil.toBengali(percent) + "%") : ("Khatm Progress: " + percent + "%"));
      sheetBinding.tvKhatmPagesCount.setText(isBn ? (BengaliNumberUtil.toBengali(pagesRead) + "/ ৬০৪ পৃষ্ঠা") : (pagesRead + "/ 604 pages"));
      sheetBinding.progressBarKhatm.setProgress(pagesRead);

      sheetBinding.tvDailyTargetPages.setText(isBn ? (BengaliNumberUtil.toBengali(dailyTarget) + " পৃষ্ঠা") : (dailyTarget + " pages"));
      sheetBinding.tvPerPrayerTargetPages.setText(isBn ? (BengaliNumberUtil.toBengali(perPrayerTarget) + " পৃষ্ঠা") : (perPrayerTarget + " pages"));

      sheetBinding.tvEstimatedFinishDate.setText((isBn ? "সম্ভাব্য সমাপ্তির তারিখ: " : "Est. Completion: ") + com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().getEstimatedCompletionDate(this));
    };

    sheetBinding.chipPlan7Days.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().setPlanDays(this, 7);
      updateView.run();
    });

    sheetBinding.chipPlan15Days.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().setPlanDays(this, 15);
      updateView.run();
    });

    sheetBinding.chipPlan30Days.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().setPlanDays(this, 30);
      updateView.run();
    });

    sheetBinding.btnAdd4Pages.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().addPagesRead(this, 4);
      updateView.run();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, isBn ? "+৪ পৃষ্ঠা যুক্ত করা হয়েছে" : "+4 pages added", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnAdd20Pages.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().addPagesRead(this, 20);
      updateView.run();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, isBn ? "মাশাআল্লাহ! +১ পারা (+২০ পৃষ্ঠা) যুক্ত করা হয়েছে" : "MashaAllah! +1 Juz (+20 pages) added", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnResetKhatmPlan.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranKhatmPlannerManager.getInstance().resetPlan(this);
      updateView.run();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, isBn ? "খতম প্ল্যান রিসেট করা হয়েছে" : "Khatm plan reset successfully", Toast.LENGTH_SHORT).show();
    });

    updateView.run();
    dialog.show();
  }

  // =========================================================================
  // Ayah of the Day Sheet (আজকের নির্বাচিত আয়াত ও তাফসীর)
  // =========================================================================
  private void showAyahOfTheDaySheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetAyahOfTheDayBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetAyahOfTheDayBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseAyahSheet);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAyahBookmarkTop);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAyahFavorite);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAyahCopy);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAyahShare);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnOpenFullSurah);

    sheetBinding.btnCloseAyahSheet.setOnClickListener(v -> dialog.dismiss());

    com.devflux.deenone.core.content.DailyAyahHadithManager.DailyDisplayContent content =
        com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(this);

    if (content != null && content.ayah != null) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      com.devflux.deenone.core.content.DailyAyahHadithManager.DailyAyahItem ayah = content.ayah;

      sheetBinding.tvAyahSheetTitle.setText(isBn ? "আজকের নির্বাচিত আয়াত" : "Ayah of the Day");
      sheetBinding.tvDailyAyahDate.setText((isBn ? "তারিখ: " : "Date: ") + (isBn ? BengaliNumberUtil.toBengali(content.date) : content.date));
      sheetBinding.tvAyahSurahBadge.setText(ayah.getSurahName(isBn));
      sheetBinding.tvAyahNumberBadge.setText(ayah.ayahNumber != null ? ((isBn ? "আয়াত: " : "Ayah: ") + (isBn ? BengaliNumberUtil.toBengali(ayah.ayahNumber) : ayah.ayahNumber)) : "");
      sheetBinding.tvAyahArabicText.setText(ayah.arabic);
      sheetBinding.tvAyahBengaliText.setText(ayah.getMeaning(isBn));
      sheetBinding.tvAyahReferenceText.setText(ayah.getReference(isBn));
      sheetBinding.tvAyahTranslationLabel.setText(isBn ? "অনুবাদ" : "Translation");
      sheetBinding.tvAyahTafsirLabel.setText(isBn ? "প্রাসঙ্গিক তাফসীর সংক্ষেপ" : "Tafsir Summary");
      sheetBinding.tvAyahTafsirText.setText(ayah.tafsirSummary != null ? ayah.tafsirSummary : (isBn ? "নিয়মিত কুরআন তিলাওয়াত ও তাফসীর অধ্যয়নের মাধ্যমে অন্তরকে আলোকিত রাখুন।" : "Keep your heart illuminated through regular Quran recitation and tafsir reflection."));
      sheetBinding.btnAyahCopy.setText(isBn ? "কপি" : "Copy");
      sheetBinding.btnAyahShare.setText(isBn ? "শেয়ার" : "Share");
      sheetBinding.btnOpenFullSurah.setText(isBn ? "সম্পূর্ণ সূরাটি তিলাওয়াত করুন →" : "Recite Full Surah →");

      java.lang.Runnable updateFavIcon = () -> {
        boolean isFav = com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().isAyahFavorite(this, ayah.id);
        sheetBinding.btnAyahBookmarkTop.setImageResource(isFav ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        sheetBinding.btnAyahFavorite.setText(isFav ? (isBn ? "প্রিয় তালিকাভুক্ত" : "Favorited") : (isBn ? "প্রিয়" : "Favorite"));
      };

      updateFavIcon.run();

      android.view.View.OnClickListener favListener = v -> {
        com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().toggleFavoriteAyah(this, ayah.id);
        updateFavIcon.run();
      };

      sheetBinding.btnAyahBookmarkTop.setOnClickListener(favListener);
      sheetBinding.btnAyahFavorite.setOnClickListener(favListener);

      // Copy Action
      sheetBinding.btnAyahCopy.setOnClickListener(v -> {
        String fullText = ayah.arabic + "\n\n" + ayah.getMeaning(isBn) + "\n" + ayah.getReference(isBn) + "\n\n" + (ayah.tafsirSummary != null ? ayah.tafsirSummary : "") + "\n\n" + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");
        copyToClipboard(isBn ? "কুরআনের আয়াত" : "Quran Ayah", fullText);
      });

      // Share Action
      sheetBinding.btnAyahShare.setOnClickListener(v -> {
        String shareText = (isBn ? "আজকের নির্বাচিত আয়াত\n\n" : "Ayah of the Day\n\n") + ayah.arabic + "\n\n" + ayah.getMeaning(isBn) + "\n" + ayah.getReference(isBn) + "\n\n" + (ayah.tafsirSummary != null ? ayah.tafsirSummary : "") + "\n\n" + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, (isBn ? "আজকের আয়াত: " : "Daily Ayah: ") + ayah.getSurahName(isBn));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, isBn ? "আয়াত শেয়ার করুন" : "Share Ayah"));
      });

      // Open Full Surah
      sheetBinding.btnOpenFullSurah.setOnClickListener(v -> {
        dialog.dismiss();
        showQuranHubBottomSheet();
      });
    }

    dialog.show();
  }

  // =========================================================================
  // Hadith of the Day Sheet (আজকের নির্বাচিত সহীহ হাদিস ও শিক্ষা)
  // =========================================================================
  private void showHadithOfTheDaySheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetHadithOfTheDayBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetHadithOfTheDayBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseHadithSheet);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnHadithBookmarkTop);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnHadithFavorite);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnHadithCopy);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnHadithShare);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnOpenHadithLibrary);

    sheetBinding.btnCloseHadithSheet.setOnClickListener(v -> dialog.dismiss());

    com.devflux.deenone.core.content.DailyAyahHadithManager.DailyDisplayContent content =
        com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(this);

    if (content != null && content.hadith != null) {
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      com.devflux.deenone.core.content.DailyAyahHadithManager.DailyHadithItem hadith = content.hadith;

      sheetBinding.tvHadithSheetTitle.setText(isBn ? "আজকের নির্বাচিত সহীহ হাদিস" : "Sahih Hadith of the Day");
      sheetBinding.tvDailyHadithDate.setText((isBn ? "তারিখ: " : "Date: ") + (isBn ? BengaliNumberUtil.toBengali(content.date) : content.date));
      sheetBinding.tvHadithNarratorBadge.setText(hadith.narrator != null ? hadith.narrator : (isBn ? "সাহাবী (রা.)" : "Sahabi (RA)"));
      sheetBinding.tvHadithAuthenticityBadge.setText(hadith.getGrade(isBn));
      sheetBinding.tvHadithArabicText.setText(hadith.arabic != null ? hadith.arabic : "");
      sheetBinding.tvHadithBengaliText.setText(hadith.getMeaning(isBn));
      sheetBinding.tvHadithReferenceText.setText(hadith.getReference(isBn));
      sheetBinding.tvHadithTranslationLabel.setText(isBn ? "অনুবাদ" : "Translation");
      sheetBinding.tvHadithLessonLabel.setText(isBn ? "হাদিসের শিক্ষা ও তাৎপর্য" : "Lessons & Significance");
      sheetBinding.tvHadithLessonText.setText(hadith.lessonExplanation != null ? hadith.lessonExplanation : (isBn ? "রাসূলুল্লাহ (ﷺ)-এর সুন্নাতকে ভালোবেসে নিজের দৈনন্দিন জীবনে ধারণ করুন।" : "Embrace and practice the Sunnah of the Prophet (ﷺ) with love in your daily life."));
      sheetBinding.btnHadithCopy.setText(isBn ? "কপি" : "Copy");
      sheetBinding.btnHadithShare.setText(isBn ? "শেয়ার" : "Share");
      sheetBinding.btnOpenHadithLibrary.setText(isBn ? "হাদিস লাইব্রেরি ও কিতাবসমূহ দেখুন →" : "View Hadith Library & Books →");

      java.lang.Runnable updateFavIcon = () -> {
        boolean isFav = com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().isHadithFavorite(this, hadith.id);
        sheetBinding.btnHadithBookmarkTop.setImageResource(isFav ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark);
        sheetBinding.btnHadithFavorite.setText(isFav ? (isBn ? "প্রিয় তালিকাভুক্ত" : "Favorited") : (isBn ? "প্রিয়" : "Favorite"));
      };

      updateFavIcon.run();

      android.view.View.OnClickListener favListener = v -> {
        com.devflux.deenone.core.content.DailyAyahHadithManager.getInstance().toggleFavoriteHadith(this, hadith.id);
        updateFavIcon.run();
      };

      sheetBinding.btnHadithBookmarkTop.setOnClickListener(favListener);
      sheetBinding.btnHadithFavorite.setOnClickListener(favListener);

      // Copy Action
      sheetBinding.btnHadithCopy.setOnClickListener(v -> {
        String fullText = (hadith.arabic != null ? hadith.arabic + "\n\n" : "")
            + hadith.getMeaning(isBn) + "\n"
            + (isBn ? "বর্ণনাকারী: " : "Narrator: ") + hadith.narrator + "\n"
            + (isBn ? "মান: " : "Grade: ") + hadith.getGrade(isBn) + "\n"
            + hadith.getReference(isBn) + "\n\n"
            + (hadith.lessonExplanation != null ? ((isBn ? "শিক্ষা: " : "Lesson: ") + hadith.lessonExplanation + "\n\n") : "")
            + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");
        copyToClipboard(isBn ? "সহীহ হাদিস" : "Sahih Hadith", fullText);
      });

      // Share Action
      sheetBinding.btnHadithShare.setOnClickListener(v -> {
        String shareText = (isBn ? "আজকের নির্বাচিত সহীহ হাদিস\n\n" : "Sahih Hadith of the Day\n\n")
            + (hadith.arabic != null ? hadith.arabic + "\n\n" : "")
            + hadith.getMeaning(isBn) + "\n\n"
            + (isBn ? "বর্ণনায়: " : "Narrator: ") + hadith.narrator + "\n"
            + (isBn ? "কিতাব: " : "Book: ") + hadith.getCollection(isBn) + " (" + (isBn ? ("হাদিস: " + BengaliNumberUtil.toBengali(hadith.hadithNumber)) : ("Hadith: " + hadith.hadithNumber)) + ")\n"
            + (isBn ? "মান: " : "Grade: ") + hadith.getGrade(isBn) + "\n"
            + hadith.getReference(isBn) + "\n\n"
            + (hadith.lessonExplanation != null ? ((isBn ? "শিক্ষা: " : "Lesson: ") + hadith.lessonExplanation + "\n\n") : "")
            + (isBn ? "— দ্বীন ওয়ান ইসলামিক অ্যাপ" : "— DeenOne Islamic App");
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, isBn ? "আজকের হাদিস" : "Daily Hadith");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(shareIntent, isBn ? "হাদিস শেয়ার করুন" : "Share Hadith"));
      });

      // Open Full Hadith Book
      sheetBinding.btnOpenHadithLibrary.setOnClickListener(v -> {
        dialog.dismiss();
        showHadithViewerSheet();
      });
    }

    dialog.show();
  }

  // =========================================================================
  // Islamic Personal Goal & Routine System (ব্যক্তিগত ইসলামিক লক্ষ্য ও রুটিন)
  // =========================================================================
  private void showIslamicGoalsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetIslamicGoalsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetIslamicGoalsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    sheetBinding.btnCloseGoalsSheet.setOnClickListener(v -> dialog.dismiss());

    sheetBinding.btnAddGoalTop.setOnClickListener(v -> {
      showCreateEditGoalDialog(null, () -> {
        renderIslamicGoals(sheetBinding);
      });
    });

    renderIslamicGoals(sheetBinding);
    dialog.show();
  }

  private void renderIslamicGoals(com.devflux.deenone.databinding.BottomSheetIslamicGoalsBinding sheetBinding) {
    List<com.devflux.deenone.core.goals.IslamicGoalItem> goals =
        com.devflux.deenone.core.goals.IslamicGoalManager.getInstance().getGoals(this);

    int completedCount = 0;
    for (com.devflux.deenone.core.goals.IslamicGoalItem item : goals) {
      if (item.isCompletedToday) completedCount++;
    }

    sheetBinding.tvActiveGoalsCount.setText(BengaliNumberUtil.toBengali(goals.size()) + "টি");
    sheetBinding.tvCompletedTodayCount.setText(BengaliNumberUtil.toBengali(completedCount) + "টি");

    sheetBinding.layoutGoalsContainer.removeAllViews();
    for (com.devflux.deenone.core.goals.IslamicGoalItem item : goals) {
      com.devflux.deenone.databinding.ItemIslamicGoalCardBinding cardBinding =
          com.devflux.deenone.databinding.ItemIslamicGoalCardBinding.inflate(LayoutInflater.from(this), sheetBinding.layoutGoalsContainer, false);

      cardBinding.tvGoalCategoryBadge.setText(item.category.displayName);
      cardBinding.tvGoalStreakBadge.setText("" + BengaliNumberUtil.toBengali(item.streakDays) + " দিন স্ট্রিক");
      cardBinding.tvGoalTitle.setText(item.title);

      String progressStr = BengaliNumberUtil.toBengali(item.currentProgress) + " / " + BengaliNumberUtil.toBengali(item.dailyTarget) + " " + item.targetUnit + (item.isCompletedToday ? " (সম্পন্ন)" : "");
      cardBinding.tvGoalProgressText.setText(progressStr);

      int percent = item.getProgressPercentage();
      cardBinding.tvGoalPercentText.setText(BengaliNumberUtil.toBengali(percent) + "%");
      cardBinding.progressBarGoal.setProgress(percent);

      if (item.isReminderEnabled) {
        cardBinding.tvGoalReminderTime.setText("রিমাইন্ডার: " + item.reminderTime);
      } else {
        cardBinding.tvGoalReminderTime.setText("রিমাইন্ডার বন্ধ");
      }

      cardBinding.btnGoalQuickAdd.setOnClickListener(v -> {
        com.devflux.deenone.core.goals.IslamicGoalManager.getInstance().incrementProgress(this, item.id, 1);
        Toast.makeText(this, "+১ " + item.targetUnit + " অগ্রগতি যোগ করা হয়েছে", Toast.LENGTH_SHORT).show();
        renderIslamicGoals(sheetBinding);
      });

      cardBinding.btnGoalOptions.setOnClickListener(v -> {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, v);
        popup.getMenu().add(0, 1, 0, "সম্পাদনা করুন");
        popup.getMenu().add(0, 2, 1, "মুছে ফেলুন");
        popup.setOnMenuItemClickListener(menuItem -> {
          if (menuItem.getItemId() == 1) {
            showCreateEditGoalDialog(item, () -> {
              renderIslamicGoals(sheetBinding);
            });
          } else if (menuItem.getItemId() == 2) {
            com.devflux.deenone.core.goals.IslamicGoalManager.getInstance().deleteGoal(this, item.id);
            Toast.makeText(this, "লক্ষ্যটি মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show();
            renderIslamicGoals(sheetBinding);
          }
          return true;
        });
        popup.show();
      });

      sheetBinding.layoutGoalsContainer.addView(cardBinding.getRoot());
    }
  }

  private void showCreateEditGoalDialog(com.devflux.deenone.core.goals.IslamicGoalItem editItem, Runnable onSaved) {
    com.google.android.material.bottomsheet.BottomSheetDialog dialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.DialogCreateEditGoalBinding binding =
        com.devflux.deenone.databinding.DialogCreateEditGoalBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(binding.getRoot());

    com.devflux.deenone.core.goals.IslamicGoalItem.Category[] categories =
        com.devflux.deenone.core.goals.IslamicGoalItem.Category.values();
    String[] catNames = new String[categories.length];
    for (int i = 0; i < categories.length; i++) {
      catNames[i] = categories[i].displayName;
    }

    android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
        this, android.R.layout.simple_spinner_dropdown_item, catNames);
    binding.spinnerGoalCategory.setAdapter(adapter);

    if (editItem != null) {
      binding.tvGoalDialogTitle.setText("ইসলামিক লক্ষ্য সম্পাদনা");
      binding.etGoalTitle.setText(editItem.title);
      binding.etGoalTargetAmount.setText(String.valueOf(editItem.dailyTarget));
      binding.etGoalTargetUnit.setText(editItem.targetUnit);
      binding.switchGoalReminder.setChecked(editItem.isReminderEnabled);
      for (int i = 0; i < categories.length; i++) {
        if (categories[i] == editItem.category) {
          binding.spinnerGoalCategory.setSelection(i);
          break;
        }
      }
    }

    binding.btnCancelGoalDialog.setOnClickListener(v -> dialog.dismiss());

    binding.btnSaveGoalDialog.setOnClickListener(v -> {
      String title = binding.etGoalTitle.getText().toString().trim();
      String amountStr = binding.etGoalTargetAmount.getText().toString().trim();
      String unit = binding.etGoalTargetUnit.getText().toString().trim();
      boolean reminder = binding.switchGoalReminder.isChecked();

      if (title.isEmpty()) {
        Toast.makeText(this, "অনুগ্রহ করে লক্ষ্যের শিরোনাম দিন", Toast.LENGTH_SHORT).show();
        return;
      }

      int amount = 1;
      try {
        amount = Integer.parseInt(amountStr);
      } catch (Exception ignored) {}

      if (unit.isEmpty()) unit = "বার";

      int selectedCatIdx = binding.spinnerGoalCategory.getSelectedItemPosition();
      com.devflux.deenone.core.goals.IslamicGoalItem.Category category =
          (selectedCatIdx >= 0 && selectedCatIdx < categories.length) ? categories[selectedCatIdx] : com.devflux.deenone.core.goals.IslamicGoalItem.Category.AMAL;

      if (editItem == null) {
        com.devflux.deenone.core.goals.IslamicGoalManager.getInstance().addGoal(this, category, title, unit, amount, reminder, "06:00 AM");
        Toast.makeText(this, "নতুন লক্ষ্য যুক্ত হয়েছে", Toast.LENGTH_SHORT).show();
      } else {
        com.devflux.deenone.core.goals.IslamicGoalManager.getInstance().updateGoal(this, editItem.id, title, unit, amount, reminder, editItem.reminderTime);
        Toast.makeText(this, "লক্ষ্য আপডেট হয়েছে", Toast.LENGTH_SHORT).show();
      }

      dialog.dismiss();
      if (onSaved != null) onSaved.run();
    });

    dialog.show();
  }

  // =========================================================================

  // =========================================================================
  // Hijri Date Adjustment Sheet (হিজরী তারিখ সমন্বয়)
  // =========================================================================
  public void showHijriAdjustmentSheet() {
    com.google.android.material.bottomsheet.BottomSheetDialog dialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetHijriAdjustmentBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetHijriAdjustmentBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    sheetBinding.btnCloseHijriAdjustSheet.setOnClickListener(v -> dialog.dismiss());

    int currentAdj = com.devflux.deenone.utils.HijriCalendarUtil.getHijriAdjustment(this);
    switch (currentAdj) {
      case -2:
        sheetBinding.rbHijriMinus2.setChecked(true);
        break;
      case -1:
        sheetBinding.rbHijriMinus1.setChecked(true);
        break;
      case 1:
        sheetBinding.rbHijriPlus1.setChecked(true);
        break;
      case 2:
        sheetBinding.rbHijriPlus2.setChecked(true);
        break;
      default:
        sheetBinding.rbHijriZero.setChecked(true);
        break;
    }

    final int[] tempSelected = {currentAdj};

    // Helper for updating preview
    Runnable updatePreviewAction = () -> {
      int adj = tempSelected[0];
      java.util.Calendar cal = java.util.Calendar.getInstance();
      com.devflux.deenone.utils.HijriCalendarUtil.HijriDateResult res =
          com.devflux.deenone.utils.HijriCalendarUtil.getRealHijriDate(cal, adj);
      sheetBinding.tvHijriPreviewDate.setText(res.getFormattedBengali());
    };

    updatePreviewAction.run();

    sheetBinding.rbHijriMinus2.setOnClickListener(v -> { tempSelected[0] = -2; updatePreviewAction.run(); });
    sheetBinding.rbHijriMinus1.setOnClickListener(v -> { tempSelected[0] = -1; updatePreviewAction.run(); });
    sheetBinding.rbHijriZero.setOnClickListener(v -> { tempSelected[0] = 0; updatePreviewAction.run(); });
    sheetBinding.rbHijriPlus1.setOnClickListener(v -> { tempSelected[0] = 1; updatePreviewAction.run(); });
    sheetBinding.rbHijriPlus2.setOnClickListener(v -> { tempSelected[0] = 2; updatePreviewAction.run(); });

    sheetBinding.btnSaveHijriAdjustment.setOnClickListener(v -> {
      com.devflux.deenone.utils.HijriCalendarUtil.setHijriAdjustment(this, tempSelected[0]);
      dialog.dismiss();

      // Refresh ViewModel display
      if (viewModel != null) {
        viewModel.setLocation(viewModel.getCurrentCoordinates());
      }
      Toast.makeText(this, "হিজরী তারিখ সমন্বয় সফলভাবে সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show();
    });

    dialog.show();
  }

  // =========================================================================
  // Wear OS Smartwatch & Screen Widget Settings Sheet
  // =========================================================================
  private void showWearOsWidgetSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetWearOsWidgetSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetWearOsWidgetSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    sheetBinding.btnCloseWearOsSheet.setOnClickListener(v -> dialog.dismiss());

    // Initial Values
    sheetBinding.switchWearOsSync.setChecked(com.devflux.deenone.core.wearable.WearOsSyncManager.isWearSyncEnabled(this));
    sheetBinding.switchLockScreenWidget.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isLockScreenWidgetEnabled(this));

    String currentTheme = com.devflux.deenone.core.wearable.WearOsSyncManager.getWearTheme(this);
    switch (currentTheme) {
      case "OLED_DARK":
        sheetBinding.rbThemeOled.setChecked(true);
        break;
      case "NAVY":
        sheetBinding.rbThemeNavy.setChecked(true);
        break;
      case "GOLD":
        sheetBinding.rbThemeGold.setChecked(true);
        break;
      default:
        sheetBinding.rbThemeEmerald.setChecked(true);
        break;
    }

    // Live Smartwatch Preview
    com.devflux.deenone.core.wearable.WearOsSyncManager.WearablePrayerPayload payload =
        com.devflux.deenone.core.wearable.WearOsSyncManager.getInstance().computeCurrentPayload(this);

    sheetBinding.tvWearPreviewWaqt.setText(""+ payload.currentWaqt);
    sheetBinding.tvWearPreviewCountdown.setText("বাকি "+ payload.remainingTime);
    sheetBinding.tvWearPreviewNext.setText("পরবর্তী: "+ payload.nextWaqt + "("+ payload.nextTime.toLowerCase() + ")");
    sheetBinding.tvWearPreviewQibla.setText("কিবলা: "+ payload.qiblaBearing + "°");

    sheetBinding.switchWearOsSync.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.wearable.WearOsSyncManager.setWearSyncEnabled(this, isChecked);
      if (isChecked) {
        com.devflux.deenone.core.wearable.WearOsSyncManager.getInstance().syncWithWearableDevices(this);
      }
    });

    sheetBinding.switchLockScreenWidget.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setLockScreenWidgetEnabled(this, isChecked);
      if (isChecked) {
        com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(this);
      } else {
        com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.cancelWidget(this);
      }
    });

    sheetBinding.rgWearTheme.setOnCheckedChangeListener((group, checkedId) -> {
      String selectedTheme = "EMERALD";
      if (checkedId == R.id.rbThemeOled) selectedTheme = "OLED_DARK";
      else if (checkedId == R.id.rbThemeNavy) selectedTheme = "NAVY";
      else if (checkedId == R.id.rbThemeGold) selectedTheme = "GOLD";

      com.devflux.deenone.core.wearable.WearOsSyncManager.setWearTheme(this, selectedTheme);
    });

    sheetBinding.btnForceWearSync.setOnClickListener(v -> {
      com.devflux.deenone.core.wearable.WearOsSyncManager.getInstance().syncWithWearableDevices(this);
      com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(this);
      com.devflux.deenone.widget.DeenOnePrayerAppWidgetProvider.updateAllWidgets(this);
      Toast.makeText(this, "স্মার্টওয়াচ ও স্ক্রিন উইজেটে সফলভাবে সিঙ্ক সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show();
      dialog.dismiss();
    });

    dialog.show();
  }

  public void showAmalTrackerSheet() {
    com.devflux.deenone.features.amal.NekAmalPageDialog.show(this);
  }

  private void showUmrahGuideSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetUmrahGuideBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetUmrahGuideBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());
    // Full screen page mode

    com.devflux.deenone.features.hajj.HajjViewModel hajjVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.hajj.HajjViewModel.class);

    TextView[] tabs = new TextView[]{
        sheetBinding.tabStage1,
        sheetBinding.tabStage2,
        sheetBinding.tabStage3,
        sheetBinding.tabStage4,
        sheetBinding.tabStage5,
        sheetBinding.tabStage6,
        sheetBinding.tabStage7
    };

    for (int i = 0; i < tabs.length; i++) {
      final int index = i;
      tabs[i].setOnClickListener(v -> {
        hajjVm.selectUmrahStage(index);
      });
    }

    hajjVm.getSelectedUmrahStage().observe(this, stage -> {
      if (stage == null) return;
      sheetBinding.tvStageTitle.setText(stage.getStageTitle());
      sheetBinding.tvStageDescription.setText(stage.getDescription());
      sheetBinding.tvPillObligatory.setText(stage.getObligatoryInfo());
      sheetBinding.tvPillSunnah.setText(stage.getSunnahInfo());
      sheetBinding.tvDuaArabic.setText(stage.getDuaArabic());
      sheetBinding.tvDuaTransliteration.setText(stage.getDuaTransliteration());
      sheetBinding.tvDuaMeaning.setText(stage.getDuaMeaning());
      sheetBinding.tvDuaReference.setText(stage.getDuaReference());
      sheetBinding.tvRulingsContent.setText(stage.getRulingsAndProhibitions());

      int selectedIdx = stage.getStageNumber() - 1;
      for (int j = 0; j < tabs.length; j++) {
        if (j == selectedIdx) {
          tabs[j].setBackgroundResource(R.drawable.bg_card_active);
          tabs[j].setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint));
        } else {
          tabs[j].setBackgroundResource(R.drawable.bg_card_secondary);
          tabs[j].setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
        }
      }
    });

    sheetBinding.btnCloseUmrahGuide.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
  }

  private void showHajjGuideSheet() {
    com.devflux.deenone.features.hajj.HajjGuidePageDialog.show(this);
  }

  private void showRamadanSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetRamadanBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetRamadanBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());
    // Full screen page mode

    com.devflux.deenone.features.ramadan.RamadanViewModel ramadanVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.ramadan.RamadanViewModel.class);

    ramadanVm.setLocation(viewModel.getCurrentCoordinates());

    TextView[] tabs = new TextView[]{
        sheetBinding.tabRamadan1,
        sheetBinding.tabRamadan2,
        sheetBinding.tabRamadan3,
        sheetBinding.tabRamadan4,
        sheetBinding.tabRamadan5,
        sheetBinding.tabRamadan6,
        sheetBinding.tabRamadan7,
        sheetBinding.tabRamadan8,
        sheetBinding.tabRamadan9,
        sheetBinding.tabRamadan10
    };

    for (int i = 0; i < tabs.length; i++) {
      final int index = i;
      tabs[i].setOnClickListener(v -> ramadanVm.selectTopic(index));
    }

    ramadanVm.getTopics().observe(this, topicsList -> {
      if (topicsList == null) return;
      for (int i = 0; i < tabs.length && i < topicsList.size(); i++) {
        tabs[i].setText(topicsList.get(i).getTabTitle());
      }
    });

    // Observe Live Ramadan Status
    ramadanVm.getRamadanStatus().observe(this, status -> {
      if (status == null) return;
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      sheetBinding.tvRamadanCountdownTitle.setText(status.countdownTitle);
      sheetBinding.tvRamadanCountdownTime.setText(status.countdownValue);
      sheetBinding.progressFastingDay.setProgress(status.progressPercent);
      sheetBinding.tvRamadanDateInfo.setText(status.ramadanStartDateBengali);

      sheetBinding.tvRamadanDaysRemainingPill.setText(
          isBn ? ("রমজান বাকি: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(status.remainingRamadanDays) + " দিন")
               : ("Days to Ramadan: " + status.remainingRamadanDays)
      );
      if (status.isCurrentlyRamadan) {
        sheetBinding.tvRamadanStatusBadge.setText(
            isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(status.currentRamadanDay) + "ই রমজান")
                 : ("Ramadan Day " + status.currentRamadanDay)
        );
      } else {
        sheetBinding.tvRamadanStatusBadge.setText(isBn ? "রমজান প্রস্তুতি" : "Ramadan Prep");
      }

      sheetBinding.tvLiveSehriTime.setText(status.sehriTimeStr);
      sheetBinding.tvLiveFajrTime.setText(status.fajrTimeStr);
      sheetBinding.tvLiveSunsetTime.setText(status.maghribTimeStr);
      sheetBinding.tvLiveIftarTime.setText(status.iftarTimeStr);
    });

    // Observe Selected Topic
    ramadanVm.getSelectedTopic().observe(this, topic -> {
      if (topic == null) return;
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      sheetBinding.tvRamadanTopicCategoryPill.setText(topic.getCategoryBadge());
      sheetBinding.tvRamadanTopicTitle.setText(topic.getTitle());
      sheetBinding.tvRamadanTopicSummary.setText(topic.getSummary());
      sheetBinding.tvRamadanTopicInstructions.setText(topic.getDetailedInstructions());
      sheetBinding.tvRamadanTopicAllowedOrProhibited.setText(topic.getAllowedOrProhibited());
      sheetBinding.tvRamadanTopicArabicDua.setText(topic.getArabicDua());
      sheetBinding.tvRamadanTopicTransliteration.setText(topic.getTransliteration());
      sheetBinding.tvRamadanTopicTranslation.setText(topic.getTranslation());
      sheetBinding.tvRamadanTopicReference.setText((isBn ? "সূত্র: " : "Reference: ") + topic.getAuthenticReference());

      int selectedIdx = topic.getId() - 1;
      for (int j = 0; j < tabs.length; j++) {
        if (j == selectedIdx) {
          tabs[j].setBackgroundResource(R.drawable.bg_card_active);
          tabs[j].setTextColor(getColor(R.color.accent_mint));
        } else {
          tabs[j].setBackgroundResource(R.drawable.bg_badge_pill);
          tabs[j].setTextColor(getColor(R.color.text_secondary));
        }
      }
    });

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseRamadanSheet);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnRamadanThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnRamadanNotification);

    for (TextView tab : tabs) {
      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(tab);
    }

    sheetBinding.btnCloseRamadanSheet.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnRamadanThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });
    sheetBinding.btnRamadanNotification.setOnClickListener(v -> showNotificationHistorySheet());
    dialog.show();
  }

  public void showMosqueFinderSheet() {
    com.devflux.deenone.features.mosque.NearbyMosquePageDialog.show(this);
  }

  public void showSalahGuideSheet() {
    com.devflux.deenone.features.salahguide.NamazShikhaHubPageDialog.show(this);
  }

  public void showJanazaGuideSheet() {
    com.devflux.deenone.features.janaza.JanazaGuidePageDialog.show(this);
  }

  public void showNamazShikhaHub() {
    com.devflux.deenone.features.salahguide.NamazShikhaHubPageDialog.show(this);
  }

  public void showIslamicCalendarSheet() {
    com.devflux.deenone.features.calendar.IslamicCalendarPageDialog.show(this);
  }

  public void showTasbihSheet() {
    com.devflux.deenone.features.tasbih.TasbihPageDialog.show(this);
  }

  private void showQiblaSheet() {
    com.devflux.deenone.features.qibla.QiblaCompassPageDialog.show(this, viewModel);
  }

  private void showZakatSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetZakatBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetZakatBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseZakat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnZakatThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnZakatNotification);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCalculateZakat);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    if (!isBn) {
      sheetBinding.tvZakatSheetTitle.setText("Zakat Calculator");
      sheetBinding.tvZakatSheetSubtitle.setText("Calculate your obligatory 2.5% annual Zakat based on Nisab");
      sheetBinding.tvZakatPayableLabel.setText("Total Zakat Payable (2.5%)");
      sheetBinding.tvZakatPayable.setText("৳ 0.00");
      sheetBinding.tvNisabStatus.setText("Nisab Threshold: ৳ 110,224 (52.5 Tola Silver equivalent)");
      sheetBinding.tilCash.setHint("Cash in Hand & Bank Balance (৳)");
      sheetBinding.tilGoldSilver.setHint("Total Market Value of Gold & Silver (৳)");
      sheetBinding.tilBusiness.setHint("Business Goods & Investments (৳)");
      sheetBinding.tilDebts.setHint("Immediate Debts & Liabilities (৳ - to deduct)");
      sheetBinding.btnCalculateZakat.setText("Calculate Zakat");
    } else {
      sheetBinding.tvZakatSheetTitle.setText("যাকাত ক্যালকুলেটর");
      sheetBinding.tvZakatSheetSubtitle.setText("নেসাব এবং বাৎসরিক ২.৫% বাধ্যতামূলক যাকাতের হিসাব");
      sheetBinding.tvZakatPayableLabel.setText("প্রদেয় মোট যাকাত (২.৫%)");
      sheetBinding.tvZakatPayable.setText("৳ ০.০০");
      sheetBinding.tvNisabStatus.setText("নেসাব থ্রেশহোল্ড: ৳ ১,১০,২২৪ (৫২.৫ তোলা রূপা)");
      sheetBinding.tilCash.setHint("নগদ টাকা ও ব্যাংক ব্যালেন্স (৳)");
      sheetBinding.tilGoldSilver.setHint("স্বর্ণ ও রূপার মোট বাজারমূল্য (৳)");
      sheetBinding.tilBusiness.setHint("ব্যবসায়িক পণ্য ও বিনিয়োগ (৳)");
      sheetBinding.tilDebts.setHint("তাত্ক্ষণিক ঋণ ও দায়দেনা (৳ - বিয়োগ হবে)");
      sheetBinding.btnCalculateZakat.setText("যাকাত হিসাব করুন");
    }

    sheetBinding.btnCloseZakat.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnZakatThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });
    sheetBinding.btnZakatNotification.setOnClickListener(v -> showNotificationHistorySheet());

    sheetBinding.btnCalculateZakat.setOnClickListener(v -> {
      double cash = parseDouble(sheetBinding.etCash.getText() != null ? sheetBinding.etCash.getText().toString() : "");
      double gold = parseDouble(sheetBinding.etGoldSilver.getText() != null ? sheetBinding.etGoldSilver.getText().toString() : "");
      double business = parseDouble(sheetBinding.etBusiness.getText() != null ? sheetBinding.etBusiness.getText().toString() : "");
      double debts = parseDouble(sheetBinding.etDebts.getText() != null ? sheetBinding.etDebts.getText().toString() : "");

      com.devflux.deenone.core.calculations.ZakatCalculator.ZakatResult result =
          com.devflux.deenone.core.calculations.ZakatCalculator.calculateZakat(cash, gold, 0, business, 0, debts, 0);

      boolean isLocaleBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      if (result.isEligibleForZakat) {
        String formatted = String.format(java.util.Locale.US, "%,.2f", result.zakatPayable);
        sheetBinding.tvZakatPayable.setText(isLocaleBn ? ("৳ " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(formatted)) : ("৳ " + formatted));
        sheetBinding.tvNisabStatus.setText(isLocaleBn ? "আপনার সম্পদ নেসাব পরিমাণ অতিক্রম করেছে (যাকাত ফরজ)" : "Your wealth exceeds Nisab threshold (Zakat is obligatory)");
        sheetBinding.tvNisabStatus.setTextColor(getColor(R.color.accent_mint));
      } else {
        sheetBinding.tvZakatPayable.setText(isLocaleBn ? "৳ ০.০০" : "৳ 0.00");
        sheetBinding.tvNisabStatus.setText(isLocaleBn ? "সম্পদ নেসাব সীমার নিচে রয়েছে (যাকাত প্রযোজ্য নয়)" : "Wealth is below Nisab threshold (Zakat not applicable)");
        sheetBinding.tvNisabStatus.setTextColor(getColor(R.color.text_secondary));
      }
    });

    dialog.show();
  }

  private double parseDouble(String str) {
    try {
      return Double.parseDouble(str.trim());
    } catch (Exception e) {
      return 0.0;
    }
  }

  private void showAzkarSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetAzkarBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetAzkarBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());
    // Full screen page mode

    // Init ViewModel
    com.devflux.deenone.features.azkar.AzkarViewModel azkarVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.azkar.AzkarViewModel.class);

    // Setup RecyclerView with AzkarAdapter
    com.devflux.deenone.features.home.adapter.AzkarAdapter azkarAdapter =
        new com.devflux.deenone.features.home.adapter.AzkarAdapter(azkar -> {
          azkarVm.incrementCount(azkar);
          com.devflux.deenone.core.gamification.GamificationManager.addXP(this, 5);
        });
    sheetBinding.recyclerAzkar.setAdapter(azkarAdapter);

    // Observe filtered azkar
    azkarVm.getFilteredAzkar().observe(this, azarList -> {
      if (azarList != null) azkarAdapter.submitList(azarList);
    });

    // Attach touch springs
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAzkarReset);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipMorning);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipEvening);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipAfterSalah);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipBeforeSleep);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipUponWaking);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipProtection);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipRizq);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipForgiveness);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipTravel);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipParents);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipRamadan);

    // Category chips
    sheetBinding.chipMorning.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("morning"); });
    sheetBinding.chipEvening.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("evening"); });
    sheetBinding.chipAfterSalah.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("after_salah"); });
    sheetBinding.chipBeforeSleep.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("before_sleep"); });
    sheetBinding.chipUponWaking.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("upon_waking"); });
    sheetBinding.chipProtection.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("protection"); });
    sheetBinding.chipRizq.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("rizq"); });
    sheetBinding.chipForgiveness.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("forgiveness"); });
    sheetBinding.chipTravel.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("travel"); });
    sheetBinding.chipParents.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("parents"); });
    sheetBinding.chipRamadan.setOnCheckedChangeListener((b, checked) -> { if (checked) azkarVm.selectCategory("ramadan"); });

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    if (!isBn) {
      sheetBinding.tvAzkarTitle.setText("Azkar & Dua");
      sheetBinding.btnAzkarReset.setContentDescription("Reset All");
      sheetBinding.chipMorning.setText("Morning");
      sheetBinding.chipEvening.setText("Evening");
      sheetBinding.chipAfterSalah.setText("After Salah");
      sheetBinding.chipBeforeSleep.setText("Before Sleep");
      sheetBinding.chipUponWaking.setText("Upon Waking");
      sheetBinding.chipProtection.setText("Protection");
      sheetBinding.chipRizq.setText("Rizq & Wealth");
      sheetBinding.chipForgiveness.setText("Forgiveness");
      sheetBinding.chipTravel.setText("Travel");
      sheetBinding.chipParents.setText("Parents");
      sheetBinding.chipRamadan.setText("Ramadan");
    } else {
      sheetBinding.tvAzkarTitle.setText("আযকার ও দু'আ");
      sheetBinding.btnAzkarReset.setContentDescription("সব রিসেট");
      sheetBinding.chipMorning.setText("সকাল");
      sheetBinding.chipEvening.setText("সন্ধ্যা");
      sheetBinding.chipAfterSalah.setText("নামাজের পর");
      sheetBinding.chipBeforeSleep.setText("ঘুমের আগে");
      sheetBinding.chipUponWaking.setText("ঘুম থেকে উঠে");
      sheetBinding.chipProtection.setText("সুরক্ষা");
      sheetBinding.chipRizq.setText("রিযক");
      sheetBinding.chipForgiveness.setText("ক্ষমা");
      sheetBinding.chipTravel.setText("সফর");
      sheetBinding.chipParents.setText("পিতামাতা");
      sheetBinding.chipRamadan.setText("রমজান");
    }

    // Reset button
    sheetBinding.btnAzkarReset.setOnClickListener(v -> {
      azkarVm.resetCategoryCount();
    });

    dialog.show();
  }

  @SuppressWarnings("unused")
  private void showHalalSheet() {
    showHalalFoodsSheet();
  }

  public void showSalahTrackerSheet() {
    com.devflux.deenone.features.prayer.SalahTrackerPageDialog.show(this);
  }

  @SuppressWarnings("unused")
  private void showKalemaSheet() {
    showSixKalimaSheet();
  }
  public void showAllahNamesSheet() {
    com.devflux.deenone.features.allahnames.AllahNamesPageDialog.show(this);
  }

  @SuppressWarnings("unused")
  public void showPrayerTimeCalculationPageDialog() {
    showPrayerTimeCalculationPageDialog(null);
  }

  public void showPrayerTimeCalculationPageDialog(Runnable onSaved) {
    com.devflux.deenone.features.prayer.PrayerTimeCalculationPageDialog.show(this, (isAuto, savedMethod) -> {
      if (viewModel != null) {
        viewModel.updateRealTimeCalculations();
      }
      if (onSaved != null) {
        onSaved.run();
      }
    });
  }

  @SuppressWarnings("unused")
  public void showSalahTimeAdjustmentDialog() {
    showSalahTimeAdjustmentDialog(null);
  }

  public void showSalahTimeAdjustmentDialog(Runnable onSaved) {
    com.devflux.deenone.features.prayer.SalahTimeAdjustmentPageDialog.show(this, () -> {
      if (viewModel != null) {
        viewModel.updateRealTimeCalculations();
      }
      if (onSaved != null) {
        onSaved.run();
      }
    });
  }

  @SuppressWarnings("unused")
  public void showMazhabSelectorDialog() {
    showMazhabSelectorDialog(null);
  }

  public void showMazhabSelectorDialog(Runnable onSaved) {
    com.devflux.deenone.features.prayer.MazhabSelectorPageDialog.show(this, juristic -> {
      if (viewModel != null) {
        viewModel.updateRealTimeCalculations();
      }
      if (onSaved != null) {
        onSaved.run();
      }
    });
  }

  private void showPrayerSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetPrayerSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetPrayerSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    updatePrayerSettingsUI(sheetBinding);

    sheetBinding.btnHanafi.setOnClickListener(v -> {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(
          this, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI
      );
      updatePrayerSettingsUI(sheetBinding);
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, isBn ? "হানাফি পদ্ধতি সক্রিয় করা হয়েছে" : "Hanafi method activated", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnShafi.setOnClickListener(v -> {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(
          this, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.SHAFI
      );
      updatePrayerSettingsUI(sheetBinding);
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, isBn ? "শাফেয়ী পদ্ধতি সক্রিয় করা হয়েছে" : "Shafi'i method activated", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnMethodSelector.setOnClickListener(v -> {
      com.devflux.deenone.features.prayer.PrayerTimeCalculationPageDialog.show(this, (isAuto, m) -> {
        updatePrayerSettingsUI(sheetBinding);
        if (viewModel != null) {
          viewModel.updateRealTimeCalculations();
        }
      });
    });

    sheetBinding.btnSavePrayerSettings.setOnClickListener(v -> {
      dialog.dismiss();
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, isBn ? "নামাজের ওয়াক্ত সেটিংস সংরক্ষিত হয়েছে" : "Prayer settings saved successfully", Toast.LENGTH_SHORT).show();
    });

    dialog.show();
  }

  private void updatePrayerSettingsUI(com.devflux.deenone.databinding.BottomSheetPrayerSettingsBinding sheetBinding) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod method =
        com.devflux.deenone.core.prayer.PrayerSettingsManager.getCalculationMethod(this);
    com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod juristic =
        com.devflux.deenone.core.prayer.PrayerSettingsManager.getJuristicMethod(this);

    sheetBinding.tvSheetPrayerSettingsTitle.setText(isBn ? "নামাজের ওয়াক্ত ও সময়সূচী সেটিংস" : "Prayer Times & Schedule Settings");
    sheetBinding.tvSheetPrayerSettingsSubtitle.setText(isBn ? "গণনা পদ্ধতি, মাযহাব ও সময়সূচীর সমন্বয়" : "Calculation Method, Juristic & Timing Setup");
    sheetBinding.tvCalculationMethodHeading.setText(isBn ? "গণনা পদ্ধতি" : "Calculation Method");
    sheetBinding.tvJuristicMethodHeading.setText(isBn ? "আসর ওয়াক্তের মাযহাব পদ্ধতি" : "Asr Juristic Method");
    sheetBinding.btnHanafi.setText(isBn ? "হানাফি" : "Hanafi");
    sheetBinding.btnShafi.setText(isBn ? "শাফেয়ী ও অন্যান্য" : "Shafi'i & Others");
    sheetBinding.tvAllWaqtsHeading.setText(isBn ? "আজকের সম্পূর্ণ নামাজের ওয়াক্ত ও বিশেষ সময়সূচী" : "Today's Full Prayer & Special Schedule");
    sheetBinding.tvLabelFajr.setText(isBn ? "ফজর" : "Fajr");
    sheetBinding.tvLabelSunrise.setText(isBn ? "সূর্যোদয়" : "Sunrise");
    sheetBinding.tvLabelDhuhr.setText(isBn ? "যোহর" : "Dhuhr");
    sheetBinding.tvLabelAsr.setText(isBn ? "আসর" : "Asr");
    sheetBinding.tvLabelMaghrib.setText(isBn ? "মাগরিব" : "Maghrib");
    sheetBinding.tvLabelIsha.setText(isBn ? "এশা" : "Isha");
    sheetBinding.tvLabelMidnight.setText(isBn ? "ইসলামিক মধ্যরাত" : "Islamic Midnight");
    sheetBinding.tvLabelTahajjud.setText(isBn ? "তাহাজ্জুদ" : "Tahajjud");
    sheetBinding.btnSavePrayerSettings.setText(isBn ? "সেটিংস সেভ করুন" : "Save Settings");

    sheetBinding.tvSelectedMethodName.setText(method.getLocalizedName(this));

    if (juristic == com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI) {
      sheetBinding.btnHanafi.setBackgroundResource(R.drawable.bg_card_active);
      sheetBinding.btnHanafi.setTextColor(getColor(R.color.accent_mint));
      sheetBinding.btnShafi.setBackgroundResource(R.drawable.bg_card_secondary);
      sheetBinding.btnShafi.setTextColor(getColor(R.color.text_secondary));
    } else {
      sheetBinding.btnShafi.setBackgroundResource(R.drawable.bg_card_active);
      sheetBinding.btnShafi.setTextColor(getColor(R.color.accent_mint));
      sheetBinding.btnHanafi.setBackgroundResource(R.drawable.bg_card_secondary);
      sheetBinding.btnHanafi.setTextColor(getColor(R.color.text_secondary));
    }

    // Calculate dynamic live prayer times for location
    double lat = com.devflux.deenone.core.constants.AppConstants.DEFAULT_LATITUDE;
    double lng = com.devflux.deenone.core.constants.AppConstants.DEFAULT_LONGITUDE;
    double tz = com.devflux.deenone.core.constants.AppConstants.DEFAULT_TIMEZONE;

    com.devflux.deenone.utils.PrayerCalculator.PrayerTimesResult res =
        com.devflux.deenone.utils.PrayerCalculator.calculateForLocationWithContext(this, lat, lng, tz, java.util.Calendar.getInstance());

    sheetBinding.tvSetFajr.setText(res.fajrStr);
    sheetBinding.tvSetSunrise.setText(res.sunriseStr);
    sheetBinding.tvSetDhuhr.setText(res.zohrStr);
    sheetBinding.tvSetAsr.setText(res.asrStr);
    sheetBinding.tvSetMaghrib.setText(res.maghribStr);
    sheetBinding.tvSetIsha.setText(res.ishaStr);
    sheetBinding.tvSetMidnight.setText(res.midnightStr);
    sheetBinding.tvSetLastThird.setText(res.lastThirdOfNightStr);
  }

  private String getWaqtDisplayName(String key) {
    return com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.getWaqtDisplayName(key);
  }

  private void copyToClipboard(String label, String text) {
    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = ClipData.newPlainText(label, text);
    if (clipboard != null) {
      clipboard.setPrimaryClip(clip);
      Toast.makeText(this, label + " কপি করা হয়েছে", Toast.LENGTH_SHORT).show();
    }
  }

  private void showQuranHubBottomSheet() {
    boolean isDownloaded = com.devflux.deenone.core.quran.QuranUnifiedDownloadManager.isAllSurahsDownloaded(this);
    boolean hasDismissed = com.devflux.deenone.core.quran.QuranUnifiedDownloadManager.hasUserDismissedPrompt(this);

    if (!isDownloaded && !hasDismissed) {
      com.devflux.deenone.features.quran.QuranUnifiedDownloadDialog.showIfNeeded(
          this,
          this::showQuranHubBottomSheetInternal,
          this::showQuranHubBottomSheetInternal
      );
    } else {
      showQuranHubBottomSheetInternal();
    }
  }

  private void showQuranHubBottomSheetInternal() {
    if (activeQuranHubDialog != null && activeQuranHubDialog.isShowing()) {
      return;
    }
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    activeQuranHubDialog = dialog;
    dialog.setOnDismissListener(d -> {
      activeQuranHubDialog = null;
      refreshHomeDynamicCards();
    });
    com.devflux.deenone.databinding.BottomSheetQuranHubBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetQuranHubBinding.inflate(getLayoutInflater());
    dialog.setContentView(sheetBinding.getRoot());
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    sheetBinding.tvQuranHubTitle.setText(isBn ? "আল-কুরআন" : "Al-Quran");
    sheetBinding.tvLastReadHeader.setText(isBn ? "সর্বশেষ পঠিত স্থান" : "Last Read");
    sheetBinding.tvContinueReadingText.setText(isBn ? "পড়া চালিয়ে যান" : "Continue Reading");
    sheetBinding.tvQuranJourneyText.setText(isBn ? "কুরআন যাত্রা" : "Quran Journey");
    sheetBinding.tvCardBanglaAudio.setText(isBn ? "কুরআন বাংলা" : "Quran Audio");
    sheetBinding.tvCardHifzHub.setText(isBn ? "হিফজ হাব" : "Hifz Hub");
    sheetBinding.tvCardSleepMode.setText(isBn ? "স্লিপ মোড" : "Sleep Mode");
    sheetBinding.etSearchQuran.setHint(isBn ? "সূরা বা পারা খুঁজুন..." : "Search Surah or Para...");
    sheetBinding.tabAllSurahs.setText(isBn ? "১১৪ টি সূরা" : "114 Surahs");
    sheetBinding.tabParas.setText(isBn ? "৩০ টি পারা" : "30 Paras");
    sheetBinding.tabQuranNotes.setText(isBn ? "বুকমার্ক" : "Bookmarks");
    sheetBinding.tvQuranNotesEmptyTitle.setText(isBn ? "কোনো বুকমার্ক সংরক্ষিত নেই" : "No bookmarks saved");
    sheetBinding.tvQuranNotesEmptySubtitle.setText(isBn ? "সূরা পড়ার সময় যে কোনো আয়াত বুকমার্ক করলে তা এখানে দেখতে পাবেন।" : "Bookmarked ayahs while reading will appear here.");

    // Global Top Bar
    sheetBinding.btnCloseQuran.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnQuranThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseQuran);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnQuranThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnQuranReciter);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnContinueReading);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnQuranJourney);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabAllSurahs);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabParas);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.tabQuranNotes);

    // Dynamic Streak & Daily Goal Engine
    Runnable updateGoalAndStreakUi = () -> {
      com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalProgressInfo progressInfo =
          com.devflux.deenone.core.quran.QuranStreakGoalManager.getInstance().getProgressInfo(MainActivity.this);

      // Streak Badge: Fire, Crying, or Angry
      sheetBinding.tvStreakText.setText(progressInfo.getFormattedStreakText(isBn));
      switch (progressInfo.streakState) {
        case ACTIVE_FIRE:
          sheetBinding.layoutStreakBadge.setBackgroundResource(R.drawable.bg_badge_streak_fire);
          sheetBinding.ivStreakIcon.setImageResource(R.drawable.ic_flame);
          sheetBinding.tvStreakText.setTextColor(getColor(R.color.accent_gold));
          break;
        case PENDING_SAD:
          sheetBinding.layoutStreakBadge.setBackgroundResource(R.drawable.bg_badge_streak_sad);
          sheetBinding.ivStreakIcon.setImageResource(R.drawable.ic_streak_crying);
          sheetBinding.tvStreakText.setTextColor(getColor(R.color.text_secondary));
          break;
        case MISSED_ANGRY:
          sheetBinding.layoutStreakBadge.setBackgroundResource(R.drawable.bg_badge_streak_angry);
          sheetBinding.ivStreakIcon.setImageResource(R.drawable.ic_streak_angry);
          sheetBinding.tvStreakText.setTextColor(getColor(R.color.accent_red));
          break;
      }

      // Daily Goal Progress Text & Progress Bar
      sheetBinding.tvDailyGoalProgressText.setText(progressInfo.getFormattedGoalSubtitle(isBn));
      sheetBinding.tvDailyGoalPercentage.setText(progressInfo.percentage + "%");
      sheetBinding.progressBarDailyGoal.setProgress(progressInfo.percentage);
    };

    updateGoalAndStreakUi.run();

    com.devflux.deenone.core.quran.QuranStreakGoalManager.getInstance().getProgressLiveData().observe(this, info -> {
      if (info != null) {
        updateGoalAndStreakUi.run();
      }
    });

    View.OnClickListener editGoalListener = v -> showQuranDailyGoalBottomSheet(updateGoalAndStreakUi);
    sheetBinding.layoutGoalRow.setOnClickListener(editGoalListener);
    sheetBinding.layoutStreakBadge.setOnClickListener(editGoalListener);

    // Last Read Surah & Continue Reading
    final com.devflux.deenone.data.local.entity.QuranSurahEntity[] lastReadHolder =
        new com.devflux.deenone.data.local.entity.QuranSurahEntity[1];
    final int[] lastAyahHolder = {1};

    quranRepository.getLastReadSurah().observe(this, lastRead -> {
      if (lastRead != null) {
        lastReadHolder[0] = lastRead;
        int ayahNum = lastRead.getReadingProgressAyah() > 0 ? lastRead.getReadingProgressAyah() : 1;
        lastAyahHolder[0] = ayahNum;
        sheetBinding.tvLastReadSurahTitle.setText(isBn
            ? (lastRead.getNameBengali() + " (আয়াত " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(ayahNum) + ")")
            : (lastRead.getNameEnglish() + " (Ayah " + ayahNum + ")"));
      } else {
        quranRepository.getAllSurahs().observe(MainActivity.this, surahs -> {
          if (surahs != null && !surahs.isEmpty() && lastReadHolder[0] == null) {
            lastReadHolder[0] = surahs.get(0);
            sheetBinding.tvLastReadSurahTitle.setText(isBn
                ? (lastReadHolder[0].getNameBengali() + " (আয়াত ১)")
                : (lastReadHolder[0].getNameEnglish() + " (Ayah 1)"));
          }
        });
      }
    });

    sheetBinding.btnContinueReading.setOnClickListener(v -> {
      if (lastReadHolder[0] != null) {
        showSurahReaderBottomSheet(lastReadHolder[0], lastAyahHolder[0]);
      }
    });
    sheetBinding.cardQuranLastRead.setOnClickListener(v -> {
      if (lastReadHolder[0] != null) {
        showSurahReaderBottomSheet(lastReadHolder[0], lastAyahHolder[0]);
      }
    });

    // Quran Journey ("কুরআন যাত্রা")
    sheetBinding.btnQuranJourney.setOnClickListener(v -> new com.devflux.deenone.features.quran.QuranJourneyPageDialog(MainActivity.this).show());

    // 3 Feature Cards
    sheetBinding.cardBanglaQuranAudio.setOnClickListener(v -> new com.devflux.deenone.features.quran.BanglaQuranHubDialog(MainActivity.this).show());
    sheetBinding.cardHifzHub.setOnClickListener(v -> showQuranHifzHubBottomSheet());
    sheetBinding.cardSleepMode.setOnClickListener(v -> showSleepModeSheet());

    // Reciter Selector Picker
    sheetBinding.btnQuranReciter.setOnClickListener(v -> {
      com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter[] reciters =
          com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter.values();
      String[] reciterNames = new String[reciters.length];
      for (int i = 0; i < reciters.length; i++) {
        reciterNames[i] = reciters[i].displayName;
      }
      new com.google.android.material.dialog.MaterialAlertDialogBuilder(MainActivity.this)
          .setTitle(isBn ? "কারী (তেলাওয়াতকারী) নির্বাচন করুন" : "Select Reciter (Qari)")
          .setItems(reciterNames, (d, which) -> {
            com.devflux.deenone.service.QuranAudioService svc =
                com.devflux.deenone.service.QuranAudioService.getInstance();
            if (svc != null) {
              svc.setReciter(reciters[which]);
            }
          })
          .show();
    });

    // Adapters: 114 Surahs (Item Layout & UX Strictly Preserved)
    final com.devflux.deenone.features.home.adapter.QuranSurahAdapter[] surahAdapterHolder =
        new com.devflux.deenone.features.home.adapter.QuranSurahAdapter[1];
    surahAdapterHolder[0] = new com.devflux.deenone.features.home.adapter.QuranSurahAdapter(surah -> {
      showSurahReaderBottomSheet(surah);
    }, (surah, position) -> {
      boolean newFavorite = !surah.isFavorite();
      surah.setFavorite(newFavorite);
      quranRepository.toggleSurahFavorite(surah.getNumber(), newFavorite);
      if (surahAdapterHolder[0] != null) {
        surahAdapterHolder[0].notifyItemChanged(position);
      }
    });
    sheetBinding.rvQuranSurahs.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvQuranSurahs.setHasFixedSize(true);
    sheetBinding.rvQuranSurahs.setItemViewCacheSize(20);
    sheetBinding.rvQuranSurahs.setAdapter(surahAdapterHolder[0]);

    quranRepository.getAllSurahs().observe(this, surahs -> {
      if (surahs != null && !surahs.isEmpty()) {
        surahAdapterHolder[0].setSurahs(surahs);
        android.content.SharedPreferences quranPrefs = getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
        int lastSurahNum = quranPrefs.getInt("last_surah_number", 1);
        int targetPos = Math.max(0, lastSurahNum - 1);
        if (targetPos < surahs.size()) {
          sheetBinding.rvQuranSurahs.post(() -> {
            androidx.recyclerview.widget.LinearLayoutManager lm =
                (androidx.recyclerview.widget.LinearLayoutManager) sheetBinding.rvQuranSurahs.getLayoutManager();
            if (lm != null) {
              lm.scrollToPositionWithOffset(targetPos, 0);
            } else {
              sheetBinding.rvQuranSurahs.scrollToPosition(targetPos);
            }
          });
        }
      }
    });

    // 30 Paras Adapter with Smart Lazy Loading
    final com.devflux.deenone.features.home.adapter.QuranParaAdapter[] paraAdapterHolder =
        new com.devflux.deenone.features.home.adapter.QuranParaAdapter[1];
    final java.util.List<com.devflux.deenone.core.quran.QuranParaItem> all30Paras =
        com.devflux.deenone.core.quran.QuranParaItem.getAll30Paras();
    sheetBinding.rvQuranParas.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvQuranParas.setHasFixedSize(true);
    sheetBinding.rvQuranParas.setItemViewCacheSize(20);

    Runnable ensureParasLoaded = () -> {
      if (paraAdapterHolder[0] == null) {
        paraAdapterHolder[0] = new com.devflux.deenone.features.home.adapter.QuranParaAdapter(
            para -> showParaSurahsBottomSheet(para),
            (para, pos) -> {
              // Bookmark toggled dynamically
            });
        sheetBinding.rvQuranParas.setAdapter(paraAdapterHolder[0]);
        paraAdapterHolder[0].setParas(all30Paras);
      }
    };

    // Unified Bookmarks (সূরা | পারা | আয়াত) with Sub-filter Chips & Smart Lazy Loading
    final com.devflux.deenone.features.home.adapter.QuranSurahAdapter[] bookmarkedSurahAdapterHolder =
        new com.devflux.deenone.features.home.adapter.QuranSurahAdapter[1];
    final com.devflux.deenone.features.home.adapter.QuranParaAdapter[] bookmarkedParaAdapterHolder =
        new com.devflux.deenone.features.home.adapter.QuranParaAdapter[1];
    final com.devflux.deenone.features.home.adapter.QuranAyahAdapter[] bookmarkedAyahAdapterHolder =
        new com.devflux.deenone.features.home.adapter.QuranAyahAdapter[1];

    final int[] bookmarkSubFilterHolder = {0}; // 0 = সব (All), 1 = সূরা (Surahs), 2 = পারা (Paras), 3 = আয়াত (Ayahs)
    final boolean[] bookmarksLoadedHolder = {false};

    final java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> cachedBookmarkedSurahs = new java.util.ArrayList<>();
    final java.util.List<com.devflux.deenone.core.quran.QuranParaItem> cachedBookmarkedParas = new java.util.ArrayList<>();
    final java.util.List<com.devflux.deenone.data.local.entity.QuranAyahEntity> cachedBookmarkedAyahs = new java.util.ArrayList<>();

    // Sub-Filter View Updater
    Runnable refreshBookmarksFilterUi = () -> {
      int subFilter = bookmarkSubFilterHolder[0];
      String query = sheetBinding.etSearchQuran.getText() != null ? sheetBinding.etSearchQuran.getText().toString().trim() : "";

      // Update Sub-filter chip styles (Rule 7: Spring touch animation attached to buttons/chips)
      sheetBinding.chipBookmarkAll.setBackgroundResource(subFilter == 0 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.chipBookmarkAll.setTextColor(getColor(subFilter == 0 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.chipBookmarkAll.setTypeface(null, subFilter == 0 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      sheetBinding.chipBookmarkSurahs.setBackgroundResource(subFilter == 1 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.chipBookmarkSurahs.setTextColor(getColor(subFilter == 1 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.chipBookmarkSurahs.setTypeface(null, subFilter == 1 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      sheetBinding.chipBookmarkParas.setBackgroundResource(subFilter == 2 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.chipBookmarkParas.setTextColor(getColor(subFilter == 2 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.chipBookmarkParas.setTypeface(null, subFilter == 2 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      sheetBinding.chipBookmarkAyahs.setBackgroundResource(subFilter == 3 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.chipBookmarkAyahs.setTextColor(getColor(subFilter == 3 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.chipBookmarkAyahs.setTypeface(null, subFilter == 3 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      // Filter Surahs
      java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> displaySurahs = new java.util.ArrayList<>();
      if (subFilter == 0 || subFilter == 1) {
        for (com.devflux.deenone.data.local.entity.QuranSurahEntity s : cachedBookmarkedSurahs) {
          if (query.isEmpty() || (s.getNameBengali() != null && s.getNameBengali().contains(query))
              || (s.getNameEnglish() != null && s.getNameEnglish().toLowerCase().contains(query.toLowerCase()))
              || String.valueOf(s.getNumber()).contains(query)) {
            displaySurahs.add(s);
          }
        }
      }

      // Filter Paras
      java.util.List<com.devflux.deenone.core.quran.QuranParaItem> displayParas = new java.util.ArrayList<>();
      if (subFilter == 0 || subFilter == 2) {
        for (com.devflux.deenone.core.quran.QuranParaItem p : cachedBookmarkedParas) {
          if (query.isEmpty() || (p.getBengaliName() != null && p.getBengaliName().contains(query))
              || (p.getEnglishName() != null && p.getEnglishName().toLowerCase().contains(query.toLowerCase()))
              || String.valueOf(p.getJuzNumber()).contains(query)) {
            displayParas.add(p);
          }
        }
      }

      // Filter Ayahs
      java.util.List<com.devflux.deenone.data.local.entity.QuranAyahEntity> displayAyahs = new java.util.ArrayList<>();
      if (subFilter == 0 || subFilter == 3) {
        for (com.devflux.deenone.data.local.entity.QuranAyahEntity a : cachedBookmarkedAyahs) {
          if (query.isEmpty() || (a.getTranslationBengali() != null && a.getTranslationBengali().contains(query))
              || (a.getTextArabic() != null && a.getTextArabic().contains(query))
              || (a.getTranslationEnglish() != null && a.getTranslationEnglish().toLowerCase().contains(query.toLowerCase()))
              || String.valueOf(a.getAyahNumber()).contains(query)) {
            displayAyahs.add(a);
          }
        }
      }

      if (bookmarkedSurahAdapterHolder[0] != null) bookmarkedSurahAdapterHolder[0].setSurahs(displaySurahs);
      if (bookmarkedParaAdapterHolder[0] != null) bookmarkedParaAdapterHolder[0].setParas(displayParas);
      if (bookmarkedAyahAdapterHolder[0] != null) bookmarkedAyahAdapterHolder[0].setAyahs(displayAyahs);

      boolean hasSurahs = (subFilter == 0 || subFilter == 1) && !displaySurahs.isEmpty();
      boolean hasParas = (subFilter == 0 || subFilter == 2) && !displayParas.isEmpty();
      boolean hasAyahs = (subFilter == 0 || subFilter == 3) && !displayAyahs.isEmpty();

      sheetBinding.layoutBookmarkedSurahsSection.setVisibility(hasSurahs ? View.VISIBLE : View.GONE);
      sheetBinding.layoutBookmarkedParasSection.setVisibility(hasParas ? View.VISIBLE : View.GONE);
      sheetBinding.layoutBookmarkedAyahsSection.setVisibility(hasAyahs ? View.VISIBLE : View.GONE);

      boolean totalEmpty = !hasSurahs && !hasParas && !hasAyahs;
      sheetBinding.layoutQuranNotesEmpty.setVisibility(totalEmpty ? View.VISIBLE : View.GONE);
      sheetBinding.nsvBookmarksContent.setVisibility(totalEmpty ? View.GONE : View.VISIBLE);
    };

    Runnable loadBookmarkedParas = () -> {
      cachedBookmarkedParas.clear();
      android.content.SharedPreferences sp = getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
      java.util.Set<String> set = sp.getStringSet("bookmarked_paras", null);
      if (set != null) {
        for (com.devflux.deenone.core.quran.QuranParaItem p : all30Paras) {
          if (set.contains(String.valueOf(p.getJuzNumber()))) {
            cachedBookmarkedParas.add(p);
          }
        }
      }
      refreshBookmarksFilterUi.run();
    };

    Runnable ensureBookmarksLoaded = () -> {
      if (!bookmarksLoadedHolder[0]) {
        bookmarksLoadedHolder[0] = true;

        // 1. Surahs Recycler
        bookmarkedSurahAdapterHolder[0] = new com.devflux.deenone.features.home.adapter.QuranSurahAdapter(
            surah -> showSurahReaderBottomSheet(surah),
            (surah, position) -> {
              boolean newFav = !surah.isFavorite();
              surah.setFavorite(newFav);
              quranRepository.toggleSurahFavorite(surah.getNumber(), newFav);
            }
        );
        sheetBinding.rvBookmarkedSurahs.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        sheetBinding.rvBookmarkedSurahs.setHasFixedSize(true);
        sheetBinding.rvBookmarkedSurahs.setAdapter(bookmarkedSurahAdapterHolder[0]);

        // 2. Paras Recycler
        bookmarkedParaAdapterHolder[0] = new com.devflux.deenone.features.home.adapter.QuranParaAdapter(
            para -> showParaSurahsBottomSheet(para),
            (para, pos) -> loadBookmarkedParas.run()
        );
        sheetBinding.rvBookmarkedParas.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        sheetBinding.rvBookmarkedParas.setHasFixedSize(true);
        sheetBinding.rvBookmarkedParas.setAdapter(bookmarkedParaAdapterHolder[0]);

        // 3. Ayahs Recycler
        bookmarkedAyahAdapterHolder[0] = new com.devflux.deenone.features.home.adapter.QuranAyahAdapter(
            new com.devflux.deenone.features.home.adapter.QuranAyahAdapter.OnAyahActionListener() {
              @Override
              public void onBookmarkToggle(com.devflux.deenone.data.local.entity.QuranAyahEntity ayah) {
                boolean nextState = !ayah.isBookmarked();
                quranRepository.setAyahBookmarked(ayah.getSurahNumber(), ayah.getAyahNumber(), nextState, "", "");
              }

              @Override
              public void onPlayAyah(com.devflux.deenone.data.local.entity.QuranAyahEntity ayah, int position) {
                openSurahReaderByNumber(ayah.getSurahNumber(), ayah.getAyahNumber(), true);
              }

              @Override
              public void onWordByWord(com.devflux.deenone.data.local.entity.QuranAyahEntity ayah, int position) {
                com.devflux.deenone.features.quran.QuranWordByWordDialog.show(
                    MainActivity.this,
                    ayah.getSurahNumber(),
                    "",
                    "",
                    ayah.getAyahNumber()
                );
              }
            }
        );
        sheetBinding.rvQuranNotes.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        sheetBinding.rvQuranNotes.setHasFixedSize(true);
        sheetBinding.rvQuranNotes.setAdapter(bookmarkedAyahAdapterHolder[0]);

        // Observe Room LiveData for Surahs & Ayahs
        quranRepository.getFavoriteSurahs().observe(MainActivity.this, favSurahs -> {
          cachedBookmarkedSurahs.clear();
          if (favSurahs != null) cachedBookmarkedSurahs.addAll(favSurahs);
          refreshBookmarksFilterUi.run();
        });

        quranRepository.getBookmarkedAyahs().observe(MainActivity.this, bookmarkedAyahs -> {
          cachedBookmarkedAyahs.clear();
          if (bookmarkedAyahs != null) cachedBookmarkedAyahs.addAll(bookmarkedAyahs);
          refreshBookmarksFilterUi.run();
        });

        loadBookmarkedParas.run();

        // Sub-filter chip click listeners with Spring Animation
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipBookmarkAll);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipBookmarkSurahs);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipBookmarkParas);
        com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipBookmarkAyahs);

        sheetBinding.chipBookmarkAll.setOnClickListener(v -> {
          bookmarkSubFilterHolder[0] = 0;
          refreshBookmarksFilterUi.run();
        });
        sheetBinding.chipBookmarkSurahs.setOnClickListener(v -> {
          bookmarkSubFilterHolder[0] = 1;
          refreshBookmarksFilterUi.run();
        });
        sheetBinding.chipBookmarkParas.setOnClickListener(v -> {
          bookmarkSubFilterHolder[0] = 2;
          refreshBookmarksFilterUi.run();
        });
        sheetBinding.chipBookmarkAyahs.setOnClickListener(v -> {
          bookmarkSubFilterHolder[0] = 3;
          refreshBookmarksFilterUi.run();
        });
      } else {
        loadBookmarkedParas.run();
      }
    };

    // Tabs Controller (১১৪ টি সূরা | ৩০ টি পারা | বুকমার্ক)
    final int[] activeTabHolder = {1};
    Runnable updateTabUi = () -> {
      int tab = activeTabHolder[0];

      sheetBinding.tabAllSurahs.setBackgroundResource(tab == 1 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.tabAllSurahs.setTextColor(getColor(tab == 1 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.tabAllSurahs.setTypeface(null, tab == 1 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      sheetBinding.tabParas.setBackgroundResource(tab == 2 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.tabParas.setTextColor(getColor(tab == 2 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.tabParas.setTypeface(null, tab == 2 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      sheetBinding.tabQuranNotes.setBackgroundResource(tab == 3 ? R.drawable.bg_tab_active : R.drawable.bg_card_secondary);
      sheetBinding.tabQuranNotes.setTextColor(getColor(tab == 3 ? R.color.accent_mint : R.color.text_secondary));
      sheetBinding.tabQuranNotes.setTypeface(null, tab == 3 ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);

      if (tab == 2) {
        ensureParasLoaded.run();
      } else if (tab == 3) {
        ensureBookmarksLoaded.run();
      }

      sheetBinding.rvQuranSurahs.setVisibility(tab == 1 ? View.VISIBLE : View.GONE);
      sheetBinding.rvQuranParas.setVisibility(tab == 2 ? View.VISIBLE : View.GONE);
      sheetBinding.layoutQuranBookmarksContainer.setVisibility(tab == 3 ? View.VISIBLE : View.GONE);
    };

    sheetBinding.tabAllSurahs.setOnClickListener(v -> {
      activeTabHolder[0] = 1;
      updateTabUi.run();
    });
    sheetBinding.tabParas.setOnClickListener(v -> {
      activeTabHolder[0] = 2;
      updateTabUi.run();
    });
    sheetBinding.tabQuranNotes.setOnClickListener(v -> {
      activeTabHolder[0] = 3;
      updateTabUi.run();
    });

    // Real-time Search Filter across active tab
    sheetBinding.etSearchQuran.addTextChangedListener(new android.text.TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString().trim();
        if (activeTabHolder[0] == 1) {
          if (query.isEmpty()) {
            quranRepository.getAllSurahs().observe(MainActivity.this, surahAdapterHolder[0]::setSurahs);
          } else {
            quranRepository.searchSurahs(query).observe(MainActivity.this, surahAdapterHolder[0]::setSurahs);
          }
        } else if (activeTabHolder[0] == 2) {
          ensureParasLoaded.run();
          if (query.isEmpty()) {
            if (paraAdapterHolder[0] != null) paraAdapterHolder[0].setParas(all30Paras);
          } else {
            java.util.List<com.devflux.deenone.core.quran.QuranParaItem> filtered = new java.util.ArrayList<>();
            for (com.devflux.deenone.core.quran.QuranParaItem p : all30Paras) {
              if (p.getBengaliName().contains(query) || p.getEnglishName().toLowerCase().contains(query.toLowerCase())
                  || String.valueOf(p.getJuzNumber()).contains(query) || p.getRangeBengali().contains(query)
                  || p.getArabicName().contains(query)) {
                filtered.add(p);
              }
            }
            if (paraAdapterHolder[0] != null) paraAdapterHolder[0].setParas(filtered);
          }
        } else if (activeTabHolder[0] == 3) {
          ensureBookmarksLoaded.run();
          refreshBookmarksFilterUi.run();
        }
      }
      @Override public void afterTextChanged(android.text.Editable s) {}
    });

    dialog.show();
  }

  // Daily Goal Selector Bottom Sheet (Verbatim matching Screenshot 1)
  private void showQuranDailyGoalBottomSheet(Runnable onGoalChanged) {
    com.google.android.material.bottomsheet.BottomSheetDialog dialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetQuranDailyGoalBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetQuranDailyGoalBinding.inflate(getLayoutInflater());
    dialog.setContentView(sheetBinding.getRoot());
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    com.devflux.deenone.core.quran.QuranStreakGoalManager manager =
        com.devflux.deenone.core.quran.QuranStreakGoalManager.getInstance();


    sheetBinding.tvDailyGoalSheetTitle.setText(isBn ? "দৈনিক লক্ষ্য নির্ধারণ করুন" : "Set Daily Goal");
    sheetBinding.tvGoalTextAyahs.setText(isBn ? "৫ আয়াত" : "5 Ayahs");
    sheetBinding.tvGoalTextPage.setText(isBn ? "১ পৃষ্ঠা" : "1 Page");
    sheetBinding.tvGoalTextTime.setText(isBn ? "১০ মিনিট" : "10 Minutes");
    sheetBinding.tvGoalTextSurah.setText(isBn ? "১ সূরা" : "1 Surah");

    Runnable updateGoalSelectionUi = () -> {
      com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalProgressInfo info =
          manager.getProgressInfo(MainActivity.this);
      com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType activeType = info.goalType;

      boolean isAyahs = (activeType == com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.AYAH);
      sheetBinding.cardGoalOptionAyahs.setBackgroundResource(isAyahs ? R.drawable.bg_goal_item_selected : R.drawable.bg_goal_item_unselected);
      sheetBinding.ivCheckGoalAyahs.setVisibility(isAyahs ? View.VISIBLE : View.GONE);

      boolean isPage = (activeType == com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.PAGE);
      sheetBinding.cardGoalOptionPage.setBackgroundResource(isPage ? R.drawable.bg_goal_item_selected : R.drawable.bg_goal_item_unselected);
      sheetBinding.ivCheckGoalPage.setVisibility(isPage ? View.VISIBLE : View.GONE);

      boolean isTime = (activeType == com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.TIME);
      sheetBinding.cardGoalOptionTime.setBackgroundResource(isTime ? R.drawable.bg_goal_item_selected : R.drawable.bg_goal_item_unselected);
      sheetBinding.ivCheckGoalTime.setVisibility(isTime ? View.VISIBLE : View.GONE);

      boolean isSurah = (activeType == com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.SURAH);
      sheetBinding.cardGoalOptionSurah.setBackgroundResource(isSurah ? R.drawable.bg_goal_item_selected : R.drawable.bg_goal_item_unselected);
      sheetBinding.ivCheckGoalSurah.setVisibility(isSurah ? View.VISIBLE : View.GONE);
    };

    updateGoalSelectionUi.run();

    sheetBinding.cardGoalOptionAyahs.setOnClickListener(v -> {
      manager.setGoal(MainActivity.this, com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.AYAH, 5);
      updateGoalSelectionUi.run();
      if (onGoalChanged != null) onGoalChanged.run();
      sheetBinding.getRoot().postDelayed(dialog::dismiss, 200);
    });

    sheetBinding.cardGoalOptionPage.setOnClickListener(v -> {
      manager.setGoal(MainActivity.this, com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.PAGE, 1);
      updateGoalSelectionUi.run();
      if (onGoalChanged != null) onGoalChanged.run();
      sheetBinding.getRoot().postDelayed(dialog::dismiss, 200);
    });

    sheetBinding.cardGoalOptionTime.setOnClickListener(v -> {
      manager.setGoal(MainActivity.this, com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.TIME, 10);
      updateGoalSelectionUi.run();
      if (onGoalChanged != null) onGoalChanged.run();
      sheetBinding.getRoot().postDelayed(dialog::dismiss, 200);
    });

    sheetBinding.cardGoalOptionSurah.setOnClickListener(v -> {
      manager.setGoal(MainActivity.this, com.devflux.deenone.core.quran.QuranStreakGoalManager.GoalType.SURAH, 1);
      updateGoalSelectionUi.run();
      if (onGoalChanged != null) onGoalChanged.run();
      sheetBinding.getRoot().postDelayed(dialog::dismiss, 200);
    });

    dialog.show();
  }

  // Bangla Quran Translation Audio Player
  private void showBanglaQuranAudioBottomSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetBanglaQuranAudioBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetBanglaQuranAudioBinding.inflate(getLayoutInflater());
    dialog.setContentView(sheetBinding.getRoot());
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    sheetBinding.tvBanglaAudioTitle.setText(isBn ? "কুরআন বাংলা অনুবাদ অডিও" : "Bangla Quran Audio");
    sheetBinding.tvBanglaAudioSubtitle.setText(isBn ? "মিশারী রশিদ আল-আফাসী ও ইসলামিক ফাউন্ডেশন বাংলাদেশ অনুবাদ" : "Reciter Mishary Rashid Al-Afasy & Islamic Foundation Bangladesh");

    sheetBinding.btnCloseBanglaAudio.setOnClickListener(v -> dialog.dismiss());
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseBanglaAudio);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnBanglaAudioPlayPause);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnBanglaAudioPrev);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnBanglaAudioNext);

    final int[] currentSurahHolder = {1};
    final android.media.MediaPlayer[] playerHolder = new android.media.MediaPlayer[1];
    final android.os.Handler handler = new android.os.Handler(android.os.Looper.getMainLooper());
    final boolean[] isUserSeeking = {false};

    Runnable updateSurahInfo = () -> {
      quranRepository.getAllSurahs().observe(MainActivity.this, surahs -> {
        if (surahs != null) {
          for (com.devflux.deenone.data.local.entity.QuranSurahEntity s : surahs) {
            if (s.getNumber() == currentSurahHolder[0]) {
              sheetBinding.tvCurrentPlayingSurah.setText(isBn ? s.getNameBengali() : s.getNameEnglish());
              sheetBinding.tvCurrentPlayingSurahEn.setText(isBn
                  ? (s.getNameEnglish() + " • " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(s.getNumber()) + "নং সূরা")
                  : (s.getNameEnglish() + " • Surah " + s.getNumber()));
              break;
            }
          }
        }
      });
    };

    updateSurahInfo.run();

    final Runnable updateProgressTask = new Runnable() {
      @Override
      public void run() {
        if (playerHolder[0] != null && playerHolder[0].isPlaying() && !isUserSeeking[0]) {
          int currentPos = playerHolder[0].getCurrentPosition();
          sheetBinding.seekBarBanglaAudio.setProgress(currentPos);
          sheetBinding.tvAudioCurrentTime.setText(formatMediaDuration(currentPos));
          com.devflux.deenone.core.quran.QuranStreakGoalManager.getInstance().recordListeningSeconds(MainActivity.this, 1);
        }
        handler.postDelayed(this, 1000);
      }
    };

    final java.lang.Runnable[] playActionHolder = new java.lang.Runnable[1];
    playActionHolder[0] = () -> {
      try {
        if (playerHolder[0] != null) {
          try {
            if (playerHolder[0].isPlaying()) playerHolder[0].stop();
            playerHolder[0].release();
          } catch (Exception ignored) {}
          playerHolder[0] = null;
        }

        sheetBinding.pbBanglaAudioBuffering.setVisibility(View.VISIBLE);
        sheetBinding.ivBanglaAudioPlayPauseIcon.setVisibility(View.GONE);

        android.net.Uri audioUri = com.devflux.deenone.core.quran.QuranAudioCacheManager.getBanglaSurahPlaybackUri(MainActivity.this, currentSurahHolder[0]);

        android.media.MediaPlayer player = new android.media.MediaPlayer();
        playerHolder[0] = player;
        player.setAudioAttributes(new android.media.AudioAttributes.Builder()
            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
            .build());
        player.setDataSource(MainActivity.this, audioUri);
        player.prepareAsync();
        if (!com.devflux.deenone.core.quran.QuranAudioCacheManager.isBanglaSurahAudioDownloaded(MainActivity.this, currentSurahHolder[0])) {
          com.devflux.deenone.core.quran.QuranAudioCacheManager.cacheBanglaSurahAudioInBackground(MainActivity.this, currentSurahHolder[0]);
        }
        player.setOnPreparedListener(mp -> {
          sheetBinding.pbBanglaAudioBuffering.setVisibility(View.GONE);
          sheetBinding.ivBanglaAudioPlayPauseIcon.setVisibility(View.VISIBLE);
          sheetBinding.ivBanglaAudioPlayPauseIcon.setImageResource(R.drawable.ic_pause);
          mp.start();
          sheetBinding.seekBarBanglaAudio.setMax(mp.getDuration());
          sheetBinding.tvAudioTotalTime.setText(formatMediaDuration(mp.getDuration()));
          handler.post(updateProgressTask);
        });
        player.setOnCompletionListener(mp -> {
          sheetBinding.ivBanglaAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
          com.devflux.deenone.core.quran.QuranStreakGoalManager.getInstance().recordSurahCompleted(MainActivity.this, currentSurahHolder[0]);
        });
        player.setOnErrorListener((mp, what, extra) -> {
          sheetBinding.pbBanglaAudioBuffering.setVisibility(View.GONE);
          sheetBinding.ivBanglaAudioPlayPauseIcon.setVisibility(View.VISIBLE);
          sheetBinding.ivBanglaAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
          return true;
        });
      } catch (Exception e) {
        sheetBinding.pbBanglaAudioBuffering.setVisibility(View.GONE);
        sheetBinding.ivBanglaAudioPlayPauseIcon.setVisibility(View.VISIBLE);
      }
    };

    sheetBinding.btnBanglaAudioPlayPause.setOnClickListener(v -> {
      if (playerHolder[0] != null && playerHolder[0].isPlaying()) {
        playerHolder[0].pause();
        sheetBinding.ivBanglaAudioPlayPauseIcon.setImageResource(R.drawable.ic_play_arrow);
      } else if (playerHolder[0] != null) {
        playerHolder[0].start();
        sheetBinding.ivBanglaAudioPlayPauseIcon.setImageResource(R.drawable.ic_pause);
      } else {
        playActionHolder[0].run();
      }
    });

    sheetBinding.btnBanglaAudioNext.setOnClickListener(v -> {
      if (currentSurahHolder[0] < 114) {
        currentSurahHolder[0]++;
        updateSurahInfo.run();
        playActionHolder[0].run();
      }
    });

    sheetBinding.btnBanglaAudioPrev.setOnClickListener(v -> {
      if (currentSurahHolder[0] > 1) {
        currentSurahHolder[0]--;
        updateSurahInfo.run();
        playActionHolder[0].run();
      }
    });

    sheetBinding.seekBarBanglaAudio.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(android.widget.SeekBar sb, int progress, boolean fromUser) {
        if (fromUser) {
          sheetBinding.tvAudioCurrentTime.setText(formatMediaDuration(progress));
        }
      }
      @Override public void onStartTrackingTouch(android.widget.SeekBar sb) {
        isUserSeeking[0] = true;
      }
      @Override public void onStopTrackingTouch(android.widget.SeekBar sb) {
        isUserSeeking[0] = false;
        if (playerHolder[0] != null) {
          playerHolder[0].seekTo(sb.getProgress());
        }
      }
    });

    dialog.setOnDismissListener(d -> {
      handler.removeCallbacksAndMessages(null);
      if (playerHolder[0] != null) {
        try {
          if (playerHolder[0].isPlaying()) playerHolder[0].stop();
          playerHolder[0].release();
        } catch (Exception ignored) {}
        playerHolder[0] = null;
      }
    });

    dialog.show();
  }

  private String formatMediaDuration(long ms) {
    long totalSecs = ms / 1000;
    long mins = totalSecs / 60;
    long secs = totalSecs % 60;
    return String.format(java.util.Locale.US, "%02d:%02d", mins, secs);
  }

  // Hifz Hub & Hafezi Quran Page Dialog
  private void showQuranHifzHubBottomSheet() {
    com.devflux.deenone.features.quran.QuranHifzHubPageDialog.open(this, 1);
  }

  // 30 Paras - Surahs of Para Bottom Sheet (Matching Screenshot 2)
  private void showParaSurahsBottomSheet(com.devflux.deenone.core.quran.QuranParaItem para) {
    if (para == null) return;
    com.google.android.material.bottomsheet.BottomSheetDialog dialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetQuranParaSurahsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetQuranParaSurahsBinding.inflate(getLayoutInflater());
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvParaSurahsTitle.setText(isBn
        ? ("পারা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(para.getJuzNumber()) + " এর সূরাসমূহ")
        : ("Surahs of Para " + para.getJuzNumber()));

    sheetBinding.btnCloseParaSurahs.setOnClickListener(v -> dialog.dismiss());
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseParaSurahs);

    sheetBinding.rvParaSurahs.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvParaSurahs.setHasFixedSize(true);
    sheetBinding.rvParaSurahs.setItemViewCacheSize(20);
    com.devflux.deenone.features.home.adapter.QuranParaSurahAdapter adapter =
        new com.devflux.deenone.features.home.adapter.QuranParaSurahAdapter(surah -> {
          dialog.dismiss();
          showSurahReaderBottomSheet(surah, 1, true);
        });
    sheetBinding.rvParaSurahs.setAdapter(adapter);

    dialog.setOnShowListener(d -> {
      com.google.android.material.bottomsheet.BottomSheetDialog bsd = (com.google.android.material.bottomsheet.BottomSheetDialog) d;
      android.widget.FrameLayout bottomSheet = bsd.findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (bottomSheet != null) {
        com.google.android.material.bottomsheet.BottomSheetBehavior<android.widget.FrameLayout> behavior =
            com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet);
        behavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
        behavior.setSkipCollapsed(true);
      }
    });

    java.util.List<Integer> surahNumbers = para.getSurahNumbers();
    quranRepository.getAllSurahs().observe(this, allSurahs -> {
      if (allSurahs != null) {
        java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> paraSurahs = new java.util.ArrayList<>();
        for (com.devflux.deenone.data.local.entity.QuranSurahEntity s : allSurahs) {
          if (surahNumbers.contains(s.getNumber())) {
            paraSurahs.add(s);
          }
        }
        adapter.setSurahs(paraSurahs);
      }
    });

    dialog.show();
  }

  private void showCommonInfoBottomSheet(String title, String description) {
    com.google.android.material.bottomsheet.BottomSheetDialog sheet =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetCommonInfoBinding b =
        com.devflux.deenone.databinding.BottomSheetCommonInfoBinding.inflate(getLayoutInflater());
    sheet.setContentView(b.getRoot());
    b.tvSheetTitle.setText(title);
    b.tvSheetDescription.setText(description);
    b.btnCloseSheet.setOnClickListener(v -> sheet.dismiss());
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(b.btnCloseSheet);
    sheet.show();
  }

  private void openSurahReaderByNumber(int surahNumber, int initialAyah) {
    openSurahReaderByNumber(surahNumber, initialAyah, false);
  }

  private void openSurahReaderByNumber(int surahNumber, int initialAyah, boolean autoPlay) {
    androidx.lifecycle.LiveData<java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity>> liveData =
        quranRepository.getAllSurahs();
    androidx.lifecycle.Observer<java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity>> observer =
        new androidx.lifecycle.Observer<java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity>>() {
          @Override
          public void onChanged(java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> surahs) {
            if (surahs != null && !surahs.isEmpty()) {
              liveData.removeObserver(this);
              for (com.devflux.deenone.data.local.entity.QuranSurahEntity s : surahs) {
                if (s.getNumber() == surahNumber) {
                  showSurahReaderBottomSheet(s, initialAyah, autoPlay);
                  break;
                }
              }
            }
          }
        };
    liveData.observe(this, observer);
  }

  private void showOfflineNoticeDialog(com.devflux.deenone.data.local.entity.QuranSurahEntity surah,
                                       com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter reciter,
                                       Runnable onDownloadTriggered) {
    if (surah == null) return;
    AppCompatDialog dialog = new AppCompatDialog(this);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    com.devflux.deenone.databinding.DialogNoticeOfflineAudioBinding dialogBinding =
        com.devflux.deenone.databinding.DialogNoticeOfflineAudioBinding.inflate(getLayoutInflater());
    dialog.setContentView(dialogBinding.getRoot());

    if (dialog.getWindow() != null) {
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
      dialog.getWindow().setLayout(
          (int) (getResources().getDisplayMetrics().widthPixels * 0.88),
          ViewGroup.LayoutParams.WRAP_CONTENT
      );
      dialog.getWindow().setGravity(Gravity.CENTER);
    }

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    dialogBinding.tvNoticeTitle.setText(isBn ? "নোটিশ" : "Notice");
    dialogBinding.tvNoticeMessage.setText(isBn
        ? "এই অডিওটি এখনও ডাউনলোড করা হয়নি। ইন্টারনেট সংযোগ ছাড়া শুনতে প্রথমে অডিওটি ডাউনলোড করে নিন।"
        : "This audio has not been downloaded yet. To listen without an internet connection, please download the audio first.");
    dialogBinding.tvNoticeBtnText.setText(isBn ? "ডাউনলোড করুন" : "Download Audio");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(dialogBinding.btnCloseNotice);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(dialogBinding.btnNoticeDownload);

    dialogBinding.btnCloseNotice.setOnClickListener(v -> dialog.dismiss());
    dialogBinding.btnNoticeDownload.setOnClickListener(v -> {
      dialog.dismiss();
      if (!com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(MainActivity.this)) {
        Toast.makeText(MainActivity.this, isBn ? "ডাউনলোড করতে ইন্টারনেট সংযোগ প্রয়োজন" : "Internet connection required to download", Toast.LENGTH_SHORT).show();
      } else {
        if (onDownloadTriggered != null) {
          onDownloadTriggered.run();
        }
      }
    });

    dialog.show();
  }

  public void showSurahReaderBottomSheet(com.devflux.deenone.data.local.entity.QuranSurahEntity surah) {
    int startAyah = (surah != null && surah.getReadingProgressAyah() > 0) ? surah.getReadingProgressAyah() : 1;
    showSurahReaderBottomSheet(surah, startAyah, false);
  }

  private void showSurahReaderBottomSheet(com.devflux.deenone.data.local.entity.QuranSurahEntity surah, int initialScrollAyah) {
    showSurahReaderBottomSheet(surah, initialScrollAyah, false);
  }

  private void showSurahReaderBottomSheet(com.devflux.deenone.data.local.entity.QuranSurahEntity surah, int initialScrollAyah, boolean autoPlay) {
    if (surah == null) return;

    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetSurahReaderBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetSurahReaderBinding.inflate(getLayoutInflater());
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    String meaning = isBn ? surah.getMeaningBengali() : surah.getMeaningEnglish();
    String titleText = isBn
        ? (surah.getNameBengali() + (meaning != null && !meaning.isEmpty() ? " (" + meaning + ")" : ""))
        : (surah.getNameEnglish() + (meaning != null && !meaning.isEmpty() ? " (" + meaning + ")" : ""));
    sheetBinding.tvReaderSurahTitle.setText(titleText);

    // Rule 7: Touch Animation ONLY on Buttons. Zero touch animation on CardViews.
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseReader);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnSelectReciter);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDownloadSurahAudio);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnOpenQuranSettings);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMiniPlayerRepeat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMiniPlayerSpeed);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMiniPlayerPrev);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMiniPlayerPlayPause);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMiniPlayerNext);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMiniPlayerClose);

    sheetBinding.btnCloseReader.setOnClickListener(v -> dialog.dismiss());

    // Update Last Read
    int startAyah = initialScrollAyah > 0 ? initialScrollAyah : 1;
    quranRepository.updateLastRead(surah.getNumber(), startAyah);
    android.content.SharedPreferences quranPrefs = getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
    quranPrefs.edit()
        .putInt("last_surah_number", surah.getNumber())
        .putString("last_surah_name_bn", surah.getNameBengali())
        .putString("last_surah_name_ar", surah.getNameArabic())
        .putString("last_surah_name_en", surah.getNameEnglish())
        .putInt("last_ayah_number", startAyah)
        .putInt("last_total_ayahs", surah.getNumberOfAyahs())
        .putInt("last_para_number", surah.getJuzNumber())
        .putLong("last_read_timestamp", System.currentTimeMillis())
        .apply();
    refreshHomeDynamicCards();

    final com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter[] selectedReciter = {
        com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY
    };

    // Offline Download State & Actions
    Runnable updateOfflineState = () -> {
      boolean isDownloaded = com.devflux.deenone.core.quran.QuranAudioCacheManager.isSurahAudioDownloaded(
          MainActivity.this, selectedReciter[0], surah.getNumber(), surah.getNumberOfAyahs()
      );
      if (isDownloaded) {
        sheetBinding.ivDownloadSurahIcon.setVisibility(View.VISIBLE);
        sheetBinding.ivDownloadSurahIcon.setImageResource(R.drawable.ic_check);
        sheetBinding.pbDownloadSurahProgress.setVisibility(View.GONE);
        sheetBinding.tvDownloadSurahPercent.setVisibility(View.GONE);
      } else {
        sheetBinding.ivDownloadSurahIcon.setVisibility(View.VISIBLE);
        sheetBinding.ivDownloadSurahIcon.setImageResource(R.drawable.ic_download);
        sheetBinding.pbDownloadSurahProgress.setVisibility(View.GONE);
        sheetBinding.tvDownloadSurahPercent.setVisibility(View.GONE);
      }
    };

    updateOfflineState.run();

    com.devflux.deenone.core.quran.QuranAudioCacheManager.DownloadListener downloadListener =
        new com.devflux.deenone.core.quran.QuranAudioCacheManager.DownloadListener() {
      @Override
      public void onProgress(int downloadedAyahs, int totalAyahs, int progressPercent) {
        runOnUiThread(() -> {
          sheetBinding.ivDownloadSurahIcon.setVisibility(View.GONE);
          sheetBinding.pbDownloadSurahProgress.setVisibility(View.GONE);
          sheetBinding.tvDownloadSurahPercent.setVisibility(View.VISIBLE);
          sheetBinding.tvDownloadSurahPercent.setText(progressPercent + "%");
        });
      }

      @Override
      public void onComplete(int totalAyahs) {
        runOnUiThread(updateOfflineState);
      }

      @Override
      public void onError(String message) {
        runOnUiThread(updateOfflineState);
      }
    };

    sheetBinding.btnDownloadSurahAudio.setOnClickListener(v -> {
      boolean isDownloaded = com.devflux.deenone.core.quran.QuranAudioCacheManager.isSurahAudioDownloaded(
          MainActivity.this, selectedReciter[0], surah.getNumber(), surah.getNumberOfAyahs()
      );
      if (isDownloaded) {
        showCommonInfoBottomSheet(
            isBn ? "সূরা " + surah.getNameBengali() + " (অফলাইন প্রস্তুত)" : "Surah " + surah.getNameEnglish() + " (Offline Ready)",
            isBn ? "এই সূরাটির সমস্ত আয়াত ও তেলাওয়াত আপনার ডিভাইসে অফলাইনে সংরক্ষিত আছে। আপনি ইন্টারনেট সংযোগ ছাড়াই যেকোনো সময় সম্পূর্ণ অফলাইনে পড়তে এবং শুনতে পারবেন।"
                 : "All verses and recitation audio for this Surah are stored offline on your device. You can read and listen anytime without internet."
        );
      } else {
        sheetBinding.ivDownloadSurahIcon.setVisibility(View.GONE);
        sheetBinding.pbDownloadSurahProgress.setVisibility(View.GONE);
        sheetBinding.tvDownloadSurahPercent.setVisibility(View.VISIBLE);
        sheetBinding.tvDownloadSurahPercent.setText("0%");
        com.devflux.deenone.core.quran.QuranAudioCacheManager.downloadSurahAudio(
            MainActivity.this, selectedReciter[0], surah.getNumber(), surah.getNumberOfAyahs(), downloadListener
        );
      }
    });

    final com.devflux.deenone.features.home.adapter.QuranAyahAdapter[] ayahAdapterHolder = new com.devflux.deenone.features.home.adapter.QuranAyahAdapter[1];

    // Quran Settings Action (TT icon in top header)
    sheetBinding.btnOpenQuranSettings.setOnClickListener(v -> showQuranSettingsBottomSheet(ayahAdapterHolder[0]));

    final boolean[] isUserSeeking = {false};

    final com.devflux.deenone.features.home.adapter.QuranAyahAdapter.OnAyahActionListener ayahActionListener =
        new com.devflux.deenone.features.home.adapter.QuranAyahAdapter.OnAyahActionListener() {
      @Override
      public void onBookmarkToggle(com.devflux.deenone.data.local.entity.QuranAyahEntity ayah) {
        quranRepository.setAyahBookmarked(
            ayah.getSurahNumber(),
            ayah.getAyahNumber(),
            ayah.isBookmarked(),
            surah.getNameBengali(),
            ayah.getTranslationBengali()
        );
      }

      @Override
      public void onPlayAyah(com.devflux.deenone.data.local.entity.QuranAyahEntity ayah, int position) {
        int targetAyahNumber = ayah.getAyahNumber();
        int recordAyahNumber = targetAyahNumber > 0 ? targetAyahNumber : 1;

        boolean isAudioCached = com.devflux.deenone.core.quran.QuranAudioCacheManager.isAyahAudioDownloaded(
            MainActivity.this, selectedReciter[0], surah.getNumber(), targetAyahNumber
        );
        boolean isOnline = com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(MainActivity.this);

        if (!isAudioCached && !isOnline) {
          showOfflineNoticeDialog(surah, selectedReciter[0], () -> {
            sheetBinding.btnDownloadSurahAudio.performClick();
          });
          return;
        }

        // Update Last Read
        quranPrefs.edit()
            .putInt("last_surah_number", surah.getNumber())
            .putString("last_surah_name_bn", surah.getNameBengali())
            .putString("last_surah_name_ar", surah.getNameArabic())
            .putString("last_surah_name_en", surah.getNameEnglish())
            .putInt("last_ayah_number", recordAyahNumber)
            .putInt("last_total_ayahs", surah.getNumberOfAyahs())
            .putInt("last_para_number", surah.getJuzNumber())
            .putLong("last_read_timestamp", System.currentTimeMillis())
            .apply();
        quranRepository.updateLastRead(surah.getNumber(), recordAyahNumber);
        refreshHomeDynamicCards();

        // Play via QuranAudioService Foreground Service
        Intent svcIntent = new Intent(MainActivity.this, com.devflux.deenone.service.QuranAudioService.class);
        svcIntent.setAction(com.devflux.deenone.service.QuranAudioService.ACTION_PLAY_AYAH);
        svcIntent.putExtra(com.devflux.deenone.service.QuranAudioService.EXTRA_SURAH_NUMBER, surah.getNumber());
        svcIntent.putExtra(com.devflux.deenone.service.QuranAudioService.EXTRA_AYAH_NUMBER, targetAyahNumber);
        svcIntent.putExtra(com.devflux.deenone.service.QuranAudioService.EXTRA_TOTAL_AYAHS, surah.getNumberOfAyahs());
        svcIntent.putExtra(com.devflux.deenone.service.QuranAudioService.EXTRA_SURAH_NAME_EN, surah.getNameEnglish());
        svcIntent.putExtra(com.devflux.deenone.service.QuranAudioService.EXTRA_SURAH_NAME_BN, surah.getNameBengali());
        svcIntent.putExtra(com.devflux.deenone.service.QuranAudioService.EXTRA_RECITER_INDEX, selectedReciter[0].ordinal());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          startForegroundService(svcIntent);
        } else {
          startService(svcIntent);
        }

        // Show Sticky Mini-Player matching Screenshot 3
        sheetBinding.cardQuranMiniPlayer.setVisibility(View.VISIBLE);
        String ayahTitle = isBn
            ? ("সূরা " + surah.getNameBengali() + " • আয়াত " + (targetAyahNumber == 0 ? "১" : BengaliNumberUtil.toBengali(targetAyahNumber)))
            : ("Surah " + surah.getNameEnglish() + " • Ayah " + (targetAyahNumber == 0 ? "1" : targetAyahNumber));
        sheetBinding.tvMiniPlayerTitle.setText(ayahTitle);
        sheetBinding.tvMiniPlayerSubtitle.setText(selectedReciter[0].displayName.split("\\(")[0].trim());
        sheetBinding.ivMiniPlayerPlayPauseIcon.setImageResource(R.drawable.ic_pause);
        ayahAdapterHolder[0].setActivePlayingAyah(targetAyahNumber, true);
      }

      @Override
      public void onWordByWord(com.devflux.deenone.data.local.entity.QuranAyahEntity ayah, int position) {
        com.devflux.deenone.features.quran.QuranWordByWordDialog.show(
            MainActivity.this,
            ayah.getSurahNumber(),
            surah.getNameBengali(),
            surah.getNameEnglish(),
            ayah.getAyahNumber()
        );
      }
    };

    com.devflux.deenone.core.quran.QuranSettingsManager settingsManager =
        com.devflux.deenone.core.quran.QuranSettingsManager.getInstance(this);

    ayahAdapterHolder[0] = new com.devflux.deenone.features.home.adapter.QuranAyahAdapter(ayahActionListener);
    ayahAdapterHolder[0].setSurahNumber(surah.getNumber());
    ayahAdapterHolder[0].setArabicFontSize(settingsManager.getArabicFontSize());
    ayahAdapterHolder[0].setTranslationFontSize(settingsManager.getTranslationFontSize());
    ayahAdapterHolder[0].setShowPronunciation(settingsManager.isShowPronunciation());
    ayahAdapterHolder[0].setShowTranslation(settingsManager.isShowTranslation());
    ayahAdapterHolder[0].setReciter(selectedReciter[0]);

    sheetBinding.rvSurahAyahs.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvSurahAyahs.setAdapter(ayahAdapterHolder[0]);

    sheetBinding.rvSurahAyahs.addOnScrollListener(new androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(@androidx.annotation.NonNull androidx.recyclerview.widget.RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
          androidx.recyclerview.widget.LinearLayoutManager lm = (androidx.recyclerview.widget.LinearLayoutManager) recyclerView.getLayoutManager();
          if (lm != null) {
            int pos = lm.findFirstVisibleItemPosition();
            if (pos >= 0 && ayahAdapterHolder[0] != null) {
              com.devflux.deenone.data.local.entity.QuranAyahEntity visibleAyah = ayahAdapterHolder[0].getAyahAt(pos);
              if (visibleAyah != null && visibleAyah.getAyahNumber() > 0) {
                quranPrefs.edit()
                    .putInt("last_surah_number", surah.getNumber())
                    .putString("last_surah_name_bn", surah.getNameBengali())
                    .putString("last_surah_name_ar", surah.getNameArabic())
                    .putString("last_surah_name_en", surah.getNameEnglish())
                    .putInt("last_ayah_number", visibleAyah.getAyahNumber())
                    .putInt("last_total_ayahs", surah.getNumberOfAyahs())
                    .putInt("last_para_number", surah.getJuzNumber())
                    .putLong("last_read_timestamp", System.currentTimeMillis())
                    .apply();
                quranRepository.updateLastRead(surah.getNumber(), visibleAyah.getAyahNumber());
                refreshHomeDynamicCards();
              }
            }
          }
        }
      }
    });

    // Load Ayahs
    final boolean[] autoPlayTriggered = {false};
    quranRepository.getAyahsForSurah(surah.getNumber()).observe(this, ayahs -> {
      com.devflux.deenone.data.local.entity.QuranAyahEntity firstAyah = null;
      if (ayahs != null && !ayahs.isEmpty()) {
        ayahAdapterHolder[0].setSurahNumber(surah.getNumber());
        ayahAdapterHolder[0].setAyahs(ayahs);
        firstAyah = ayahs.get(0);
      } else {
        if (surah.getNumber() == 1) {
          java.util.List<com.devflux.deenone.data.local.entity.QuranAyahEntity> fallbackList = new java.util.ArrayList<>();
          fallbackList.add(new com.devflux.deenone.data.local.entity.QuranAyahEntity(
              1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
              "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
              "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
              "বিসমিল্লাহির রাহমানির রাহিম",
              com.devflux.deenone.core.quran.QuranCdnAudioHelper.getAyahAudioUrl(selectedReciter[0], 1, 1),
              1, 1
          ));
          ayahAdapterHolder[0].setSurahNumber(1);
          ayahAdapterHolder[0].setAyahs(fallbackList);
          firstAyah = fallbackList.get(0);
        }
      }

      if (autoPlay && !autoPlayTriggered[0] && firstAyah != null) {
        autoPlayTriggered[0] = true;
        final com.devflux.deenone.data.local.entity.QuranAyahEntity playTarget = firstAyah;
        sheetBinding.rvSurahAyahs.post(() -> ayahActionListener.onPlayAyah(playTarget, 0));
      }

      if (initialScrollAyah > 1) {
        sheetBinding.rvSurahAyahs.post(() -> {
          androidx.recyclerview.widget.LinearLayoutManager lm =
              (androidx.recyclerview.widget.LinearLayoutManager) sheetBinding.rvSurahAyahs.getLayoutManager();
          int targetPos = ayahAdapterHolder[0].getPositionForAyahNumber(initialScrollAyah);
          if (lm != null) {
            lm.scrollToPositionWithOffset(targetPos, 0);
          } else {
            sheetBinding.rvSurahAyahs.scrollToPosition(targetPos);
          }
        });
      }
    });

    // Setup Sticky Mini-Player Controls (Matching Screenshot 3)
    sheetBinding.btnMiniPlayerPlayPause.setOnClickListener(v -> {
      com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
      if (svc != null) {
        svc.togglePlayPause();
      }
    });

    sheetBinding.btnMiniPlayerNext.setOnClickListener(v -> {
      com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
      if (svc != null) {
        svc.playNextAyah();
      }
    });

    sheetBinding.btnMiniPlayerPrev.setOnClickListener(v -> {
      com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
      if (svc != null) {
        svc.playPreviousAyah();
      }
    });

    // Speed cycling (0.75x, 1.0x, 1.25x, 1.5x, 2.0x)
    float curSpeed = settingsManager.getPlaybackSpeed();
    updateMiniPlayerSpeedLabel(sheetBinding.tvMiniPlayerSpeedLabel, curSpeed, isBn);

    sheetBinding.btnMiniPlayerSpeed.setOnClickListener(v -> {
      float[] speeds = {0.75f, 1.0f, 1.25f, 1.5f, 2.0f};
      float current = settingsManager.getPlaybackSpeed();
      int nextIdx = 1;
      for (int i = 0; i < speeds.length; i++) {
        if (Math.abs(speeds[i] - current) < 0.05f) {
          nextIdx = (i + 1) % speeds.length;
          break;
        }
      }
      float nextSpeed = speeds[nextIdx];
      settingsManager.setPlaybackSpeed(nextSpeed);
      com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
      if (svc != null) {
        svc.setPlaybackSpeed(nextSpeed);
      }
      updateMiniPlayerSpeedLabel(sheetBinding.tvMiniPlayerSpeedLabel, nextSpeed, isBn);
    });

    // Repeat Mode cycling (Ayah -> Surah -> All -> Off)
    int curRepeat = settingsManager.getRepeatMode();
    updateMiniPlayerRepeatIcon(sheetBinding.ivMiniPlayerRepeatIcon, curRepeat);

    sheetBinding.btnMiniPlayerRepeat.setOnClickListener(v -> {
      int mode = settingsManager.getRepeatMode();
      int nextMode;
      if (mode == com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_AYAH) {
        nextMode = com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_SURAH;
      } else if (mode == com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_SURAH) {
        nextMode = com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_ALL;
      } else if (mode == com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_ALL) {
        nextMode = com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_OFF;
      } else {
        nextMode = com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_AYAH;
      }
      settingsManager.setRepeatMode(nextMode);
      updateMiniPlayerRepeatIcon(sheetBinding.ivMiniPlayerRepeatIcon, nextMode);
    });

    // Seekbar scrubbing
    sheetBinding.sbMiniPlayerProgress.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
          if (svc != null && svc.getDuration() > 0) {
            long targetMs = (progress * svc.getDuration()) / 1000L;
            sheetBinding.tvMiniPlayerCurrentTime.setText(formatAudioDuration(targetMs, isBn));
          }
        }
      }

      @Override
      public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
        isUserSeeking[0] = true;
      }

      @Override
      public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
        isUserSeeking[0] = false;
        com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
        if (svc != null && svc.getDuration() > 0) {
          long targetMs = (seekBar.getProgress() * svc.getDuration()) / 1000L;
          svc.seekTo(targetMs);
        }
      }
    });

    View.OnClickListener reciterPickerListener = v -> {
      com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter[] reciters = com.devflux.deenone.core.quran.QuranCdnAudioHelper.Reciter.values();
      String[] reciterNames = new String[reciters.length];
      for (int i = 0; i < reciters.length; i++) {
        reciterNames[i] = reciters[i].displayName;
      }

      new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
          .setTitle(isBn ? "কারী (তেলাওয়াতকারী) নির্বাচন করুন" : "Select Reciter (Qari)")
          .setItems(reciterNames, (d, which) -> {
            selectedReciter[0] = reciters[which];
            ayahAdapterHolder[0].setReciter(selectedReciter[0]);
            com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
            if (svc != null) {
              svc.setReciter(selectedReciter[0]);
            }
            sheetBinding.tvMiniPlayerSubtitle.setText(selectedReciter[0].displayName.split("\\(")[0].trim());
            updateOfflineState.run();
          })
          .show();
    };

    sheetBinding.btnSelectReciter.setOnClickListener(reciterPickerListener);

    sheetBinding.btnMiniPlayerClose.setOnClickListener(v -> {
      com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
      if (svc != null) {
        svc.stopPlayback();
      }
      sheetBinding.cardQuranMiniPlayer.setVisibility(View.GONE);
      ayahAdapterHolder[0].setActivePlayingAyah(-1, false);
    });

    // Listen for active Ayah and progress changes from Service
    com.devflux.deenone.service.QuranAudioService.setPlaybackEventListener(new com.devflux.deenone.service.QuranAudioService.OnPlaybackEventListener() {
      @Override
      public void onAyahChanged(int surahNumber, int ayahNumber, String surahNameBn, boolean isPlaying) {
        runOnUiThread(() -> {
          if (surahNumber == surah.getNumber()) {
            boolean isBnLocale = com.devflux.deenone.core.localization.LocaleManager.isBengali(MainActivity.this);
            sheetBinding.cardQuranMiniPlayer.setVisibility(View.VISIBLE);
            String ayahTitle = isBnLocale
                ? ("সূরা " + surah.getNameBengali() + " • আয়াত " + (ayahNumber == 0 ? "১" : BengaliNumberUtil.toBengali(ayahNumber)))
                : ("Surah " + surah.getNameEnglish() + " • Ayah " + (ayahNumber == 0 ? "1" : ayahNumber));
            sheetBinding.tvMiniPlayerTitle.setText(ayahTitle);
            sheetBinding.tvMiniPlayerSubtitle.setText(selectedReciter[0].displayName.split("\\(")[0].trim());
            sheetBinding.ivMiniPlayerPlayPauseIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
            ayahAdapterHolder[0].setActivePlayingAyah(ayahNumber, isPlaying);
            int scrollPos = ayahAdapterHolder[0].getPositionForAyahNumber(ayahNumber);
            sheetBinding.rvSurahAyahs.smoothScrollToPosition(scrollPos);
          }
        });
      }

      @Override
      public void onPlaybackStateChanged(boolean isPlaying) {
        runOnUiThread(() -> {
          sheetBinding.ivMiniPlayerPlayPauseIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
          com.devflux.deenone.service.QuranAudioService svc = com.devflux.deenone.service.QuranAudioService.getInstance();
          if (svc != null) {
            ayahAdapterHolder[0].setActivePlayingAyah(svc.getCurrentAyahNumber(), isPlaying);
          }
        });
      }

      @Override
      public void onPlaybackStopped() {
        runOnUiThread(() -> {
          sheetBinding.cardQuranMiniPlayer.setVisibility(View.GONE);
          ayahAdapterHolder[0].setActivePlayingAyah(-1, false);
        });
      }

      @Override
      public void onProgressUpdate(long currentPositionMs, long durationMs) {
        runOnUiThread(() -> {
          if (!isUserSeeking[0] && durationMs > 0) {
            boolean isBnLocale = com.devflux.deenone.core.localization.LocaleManager.isBengali(MainActivity.this);
            int progress = (int) ((currentPositionMs * 1000L) / durationMs);
            sheetBinding.sbMiniPlayerProgress.setProgress(progress);
            sheetBinding.tvMiniPlayerCurrentTime.setText(formatAudioDuration(currentPositionMs, isBnLocale));
            sheetBinding.tvMiniPlayerTotalTime.setText(formatAudioDuration(durationMs, isBnLocale));
          }
        });
      }
    });

    // Check if already playing this Surah
    com.devflux.deenone.service.QuranAudioService currentSvc = com.devflux.deenone.service.QuranAudioService.getInstance();
    if (currentSvc != null && currentSvc.isPlaying() && currentSvc.getCurrentSurahNumber() == surah.getNumber()) {
      boolean isBnLocale = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      sheetBinding.cardQuranMiniPlayer.setVisibility(View.VISIBLE);
      String ayahTitle = isBnLocale
          ? ("সূরা " + surah.getNameBengali() + " • আয়াত " + (currentSvc.getCurrentAyahNumber() == 0 ? "১" : BengaliNumberUtil.toBengali(currentSvc.getCurrentAyahNumber())))
          : ("Surah " + surah.getNameEnglish() + " • Ayah " + (currentSvc.getCurrentAyahNumber() == 0 ? "1" : currentSvc.getCurrentAyahNumber()));
      sheetBinding.tvMiniPlayerTitle.setText(ayahTitle);
      sheetBinding.tvMiniPlayerSubtitle.setText(selectedReciter[0].displayName.split("\\(")[0].trim());
      sheetBinding.ivMiniPlayerPlayPauseIcon.setImageResource(R.drawable.ic_pause);
      ayahAdapterHolder[0].setActivePlayingAyah(currentSvc.getCurrentAyahNumber(), true);
    }

    dialog.setOnDismissListener(d -> {
      if (ayahAdapterHolder[0] != null && sheetBinding.rvSurahAyahs != null) {
        androidx.recyclerview.widget.LinearLayoutManager lm =
            (androidx.recyclerview.widget.LinearLayoutManager) sheetBinding.rvSurahAyahs.getLayoutManager();
        if (lm != null) {
          int pos = lm.findFirstVisibleItemPosition();
          if (pos >= 0) {
            com.devflux.deenone.data.local.entity.QuranAyahEntity visibleAyah = ayahAdapterHolder[0].getAyahAt(pos);
            if (visibleAyah != null) {
              quranPrefs.edit()
                  .putInt("last_surah_number", surah.getNumber())
                  .putString("last_surah_name_bn", surah.getNameBengali())
                  .putString("last_surah_name_ar", surah.getNameArabic())
                  .putString("last_surah_name_en", surah.getNameEnglish())
                  .putInt("last_ayah_number", visibleAyah.getAyahNumber())
                  .putInt("last_total_ayahs", surah.getNumberOfAyahs())
                  .putInt("last_para_number", surah.getJuzNumber())
                  .putLong("last_read_timestamp", System.currentTimeMillis())
                  .apply();
              quranRepository.updateLastRead(surah.getNumber(), visibleAyah.getAyahNumber());
            }
          }
        }
      }
      refreshHomeDynamicCards();
    });

    dialog.show();
  }

  private void showQuranSettingsBottomSheet(com.devflux.deenone.features.home.adapter.QuranAyahAdapter adapter) {
    com.google.android.material.bottomsheet.BottomSheetDialog settingsDialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetQuranSettingsBinding b =
        com.devflux.deenone.databinding.BottomSheetQuranSettingsBinding.inflate(getLayoutInflater());
    settingsDialog.setContentView(b.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    com.devflux.deenone.core.quran.QuranSettingsManager settings =
        com.devflux.deenone.core.quran.QuranSettingsManager.getInstance(this);

    // Dual Language Localization (Screenshot 2)
    b.tvQuranSettingsTitle.setText(isBn ? "কুরআন সেটিংস" : "Quran Settings");
    b.tvPronunciationTitle.setText(isBn ? "উচ্চারণসহ পড়ুন" : "Read with Pronunciation");
    b.tvPronunciationSubtitle.setText(isBn ? "আরবির নিচে বাংলা উচ্চারণ" : "Pronunciation below Arabic");
    b.tvTranslationTitle.setText(isBn ? "অর্থসহ পড়ুন" : "Read with Translation");
    b.tvTranslationSubtitle.setText(isBn ? "আয়াতের বাংলা অনুবাদ" : "Verse meaning translation");
    b.tvArabicFontSizeTitle.setText(isBn ? "আরবি ফন্ট সাইজ" : "Arabic Font Size");
    b.tvBanglaFontSizeTitle.setText(isBn ? "বাংলা ফন্ট সাইজ" : "Translation Font Size");
    b.tvBanglaFontSizeSubtitle.setText(isBn ? "উচ্চারণ ও অর্থ — দুটোরই আকার" : "Size of both pronunciation & meaning");
    b.tvBanglaPreview.setText(isBn ? "যাবতীয় প্রশংসা আল্লাহ তাআলার" : "All praise is due to Allah");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(b.btnArabicFontDecrease);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(b.btnArabicFontIncrease);

    // 1. Pronunciation Switch
    b.switchPronunciation.setChecked(settings.isShowPronunciation());
    b.switchPronunciation.setOnCheckedChangeListener((btn, isChecked) -> {
      settings.setShowPronunciation(isChecked);
      if (adapter != null) {
        adapter.setShowPronunciation(isChecked);
      }
    });

    // 2. Translation Switch
    b.switchTranslation.setChecked(settings.isShowTranslation());
    b.switchTranslation.setOnCheckedChangeListener((btn, isChecked) -> {
      settings.setShowTranslation(isChecked);
      if (adapter != null) {
        adapter.setShowTranslation(isChecked);
      }
    });

    // 3. Arabic Font Size (Base: 18sp..38sp, standard 24sp = 100%)
    float currentAr = settings.getArabicFontSize();
    int arProgress = Math.max(0, Math.min(20, Math.round(currentAr - 18.0f)));
    b.sliderArabicFontSize.setProgress(arProgress);
    int pct = Math.round((currentAr / 24.0f) * 100);
    b.tvArabicFontPercentBadge.setText(pct + "%");
    b.tvArabicPreview.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, currentAr);

    b.sliderArabicFontSize.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        float size = 18.0f + progress;
        int percent = Math.round((size / 24.0f) * 100);
        b.tvArabicFontPercentBadge.setText(percent + "%");
        b.tvArabicPreview.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size);
        settings.setArabicFontSize(size);
        if (adapter != null) adapter.setArabicFontSize(size);
      }
      @Override
      public void onStartTrackingTouch(android.widget.SeekBar seekBar) {}
      @Override
      public void onStopTrackingTouch(android.widget.SeekBar seekBar) {}
    });

    b.btnArabicFontDecrease.setOnClickListener(v -> {
      int p = Math.max(0, b.sliderArabicFontSize.getProgress() - 2);
      b.sliderArabicFontSize.setProgress(p);
    });
    b.btnArabicFontIncrease.setOnClickListener(v -> {
      int p = Math.min(20, b.sliderArabicFontSize.getProgress() + 2);
      b.sliderArabicFontSize.setProgress(p);
    });

    // 4. Bangla / Translation Font Size (Base: 12sp..24sp, standard 18sp badge)
    float currentBn = settings.getTranslationFontSize();
    int bnProgress = Math.max(0, Math.min(12, Math.round(currentBn - 12.0f)));
    b.sliderBanglaFontSize.setProgress(bnProgress);
    int badgeVal = Math.round(currentBn);
    b.tvBanglaFontSizeBadge.setText(isBn ? BengaliNumberUtil.toBengali(badgeVal) : String.valueOf(badgeVal));
    b.tvBanglaPreview.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, currentBn);

    b.sliderBanglaFontSize.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        float size = 12.0f + progress;
        int val = Math.round(size);
        b.tvBanglaFontSizeBadge.setText(isBn ? BengaliNumberUtil.toBengali(val) : String.valueOf(val));
        b.tvBanglaPreview.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size);
        settings.setTranslationFontSize(size);
        if (adapter != null) adapter.setTranslationFontSize(size);
      }
      @Override
      public void onStartTrackingTouch(android.widget.SeekBar seekBar) {}
      @Override
      public void onStopTrackingTouch(android.widget.SeekBar seekBar) {}
    });

    settingsDialog.show();
  }

  private String formatAudioDuration(long ms, boolean isBengali) {
    long totalSeconds = Math.max(0, ms / 1000);
    long minutes = totalSeconds / 60;
    long seconds = totalSeconds % 60;
    String formatted = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds);
    return isBengali ? BengaliNumberUtil.toBengali(formatted) : formatted;
  }

  private void updateMiniPlayerSpeedLabel(android.widget.TextView tv, float speed, boolean isBengali) {
    String str;
    if (Math.abs(speed - 0.75f) < 0.05f) {
      str = isBengali ? "০.৭৫×" : "0.75x";
    } else if (Math.abs(speed - 1.25f) < 0.05f) {
      str = isBengali ? "১.২৫×" : "1.25x";
    } else if (Math.abs(speed - 1.5f) < 0.05f) {
      str = isBengali ? "১.৫×" : "1.5x";
    } else if (Math.abs(speed - 2.0f) < 0.05f) {
      str = isBengali ? "২×" : "2x";
    } else {
      str = isBengali ? "১×" : "1x";
    }
    tv.setText(str);
  }

  private void updateMiniPlayerRepeatIcon(android.widget.ImageView iv, int repeatMode) {
    if (repeatMode == com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_AYAH ||
        repeatMode == com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_SURAH) {
      iv.setImageResource(R.drawable.ic_repeat_one);
      iv.setColorFilter(androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint));
    } else if (repeatMode == com.devflux.deenone.core.quran.QuranSettingsManager.REPEAT_ALL) {
      iv.setImageResource(R.drawable.ic_repeat);
      iv.setColorFilter(androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint));
    } else {
      iv.setImageResource(R.drawable.ic_repeat);
      iv.setColorFilter(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
    }
  }

  private void showQuranSourcesAttributionDialog() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.DialogQuranSourcesCreditsBinding binding =
        com.devflux.deenone.databinding.DialogQuranSourcesCreditsBinding.inflate(getLayoutInflater());
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Apply bilingual text
    binding.tvCreditsDialogTitle.setText(isBn ? "উৎস ও কৃতজ্ঞতা" : "Sources & Attribution");
    binding.tvCreditsBannerHeader.setText(isBn ? "পবিত্র কুরআন ডেটাসেট ও ডিজিটাল সূত্র" : "Holy Quran Dataset & Digital Sources");
    binding.tvCreditsBannerSub.setText(isBn
        ? "দ্বীনওয়ান (DeenOne) অ্যাপে ব্যবহৃত পবিত্র কুরআনুল কারীমের টেক্সট, অনুবাদ, উচ্চারণ ও অডিও তেলাওয়াত আন্তর্জাতিকভাবে স্বীকৃত ও নির্ভরযোগ্য ডিজিটাল কুরআন প্রজেক্টসমূহ থেকে সংরক্ষিত।"
        : "The Holy Quran text, translations, pronunciations, and audio recitations in DeenOne are curated from internationally recognized, verified Quranic projects.");

    binding.tvTanzilTitle.setText(isBn ? "১. আরবি কুরআন টেক্সট" : "1. Arabic Quran Text");
    binding.tvTanzilDetails.setText(isBn
        ? "উৎস: Tanzil Project (tanzil.net) • CC BY 3.0 • অবিকৃত ও ১০০% বিশুদ্ধ কপি। বিশ্বব্যাপী সর্বাধিক নির্ভরযোগ্য ও স্বীকৃত উসমানী কুরআন টেক্সট।"
        : "Source: Tanzil Project (tanzil.net) • CC BY 3.0 • Unmodified pure copy. Globally recognized authentic Uthmani script.");
    binding.tvVisitTanzilText.setText(isBn ? "tanzil.net দেখুন" : "Visit tanzil.net");

    binding.tvTranslationTitle.setText(isBn ? "২. বাংলা ও ইংরেজি অনুবাদ" : "2. Bengali & English Translations");
    binding.tvTranslationDetails.setText(isBn
        ? "উৎস: quran-api সংগ্রহ, ইসলামিক ফাউন্ডেশন বাংলাদেশ ও সহীহ ইন্টারন্যাশনাল (Sahih International) অনুবাদ।"
        : "Source: quran-api collection, Islamic Foundation Bangladesh & Sahih International translations.");
    binding.tvVisitQuranComText.setText(isBn ? "quran.com দেখুন" : "Visit quran.com");

    binding.tvTransliterationTitle.setText(isBn ? "৩. কুরআনের বাংলা উচ্চারণ" : "3. Bengali Transliteration");
    binding.tvTransliterationDetails.setText(isBn
        ? "উৎস: দ্বীনওয়ান (DeenOne) অ্যাপের নিজস্ব সংকলিত ও যাচাইকৃত সহজ-সরল বাংলা উচ্চারণ পদ্ধতি।"
        : "Source: DeenOne App in-house compiled and verified native pronunciation system.");

    binding.tvAudioSourceTitle.setText(isBn ? "৪. অডিও তেলাওয়াত ও কারী" : "4. Audio Recitation & Qaris");
    binding.tvAudioSourceDetails.setText(isBn
        ? "উৎস: everyayah.com ও quran.com • ক্বারী: শায়খ মিশারী রশিদ আল-আফাসী (Mishary Rashid Al-Afasy), শায়খ আস-সুদাইস, শায়খ মাহের আল-মুয়াইক্লি সহ অন্যান্য প্রখ্যাত ক্বারীগণ।"
        : "Source: everyayah.com & quran.com • Reciter: Sheikh Mishary Rashid Al-Afasy, Sheikh As-Sudais, Sheikh Maher Al-Muaiqly and renowned Qaris.");
    binding.tvVisitEveryAyahText.setText(isBn ? "everyayah.com দেখুন" : "Visit everyayah.com");
    binding.btnDismissCredits.setText(isBn ? "ঠিক আছে" : "Got it");

    // Touch animations exclusively on buttons (Rule 7)
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCloseCredits);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnVisitTanzil);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnVisitQuranCom);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnVisitEveryAyah);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnDismissCredits);

    binding.btnCloseCredits.setOnClickListener(v -> dialog.dismiss());
    binding.btnDismissCredits.setOnClickListener(v -> dialog.dismiss());

    binding.btnVisitTanzil.setOnClickListener(v -> openWebUrl("https://tanzil.net"));
    binding.btnVisitQuranCom.setOnClickListener(v -> openWebUrl("https://quran.com"));
    binding.btnVisitEveryAyah.setOnClickListener(v -> openWebUrl("https://everyayah.com"));

    dialog.show();
  }

  private void openWebUrl(String url) {
    if (url == null || url.isEmpty()) return;
    try {
      android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url));
      startActivity(intent);
    } catch (Exception ignored) {
    }
  }

  private void handleIncomingDeepLink(Intent intent) {
    if (intent == null) return;

    String notifTitle = intent.getStringExtra("extra_notification_title");
    String notifBody = intent.getStringExtra("extra_notification_body");
    String notifType = intent.getStringExtra("extra_notification_type");
    String action = intent.getStringExtra("extra_deep_link");
    if (action == null || action.isEmpty()) {
      action = intent.getStringExtra("navigate_to");
    }
    if (action == null || action.isEmpty()) {
      action = intent.getStringExtra("DEEP_LINK_FEATURE");
    }

    navigateToNotificationTarget(action, notifType, notifTitle, notifBody, intent);
  }

  public void navigateToNotificationTarget(String action, String type, String title, String body, Intent sourceIntent) {
    final String normAction = action != null ? action.trim().toLowerCase() : "";
    final String normType = type != null ? type.trim().toLowerCase() : "";

    // Blood Donation Request Deep-Link (Matches user voice note & Screenshot 1, 2)
    if (normAction.startsWith("blood_request") || normAction.startsWith("feature_blood_request")
        || normAction.startsWith("blood") || normType.contains("blood")) {
      int requestId = 80;
      if (action != null && action.contains(":")) {
        try {
          String idPart = action.substring(action.lastIndexOf(":") + 1).trim();
          requestId = Integer.parseInt(idPart);
        } catch (Exception ignored) {}
      }
      com.devflux.deenone.features.blood.model.BloodRequestModel model =
          new com.devflux.deenone.features.blood.model.BloodRequestModel(
              requestId,
              "usr_" + requestId,
              "Farhan Akil",
              "AB+",
              1,
              "ফেনী সদর হাসপাতাল,ফেনী",
              "ফেনী",
              "সদর",
              "01800000000",
              "01800000000",
              "EMERGENCY",
              "2026-09-20",
              "OPEN",
              System.currentTimeMillis() - 9L * 86400000L
          );
      com.devflux.deenone.features.blood.BloodRequestDetailDialog.show(this, model, null);
      return;
    }

    // Quran Journey Notification Deep-Link
    if (normAction.equals("quran_journey") || normAction.equals("feature_quran_journey") || "quran_journey".equalsIgnoreCase(normType)) {
      new com.devflux.deenone.features.quran.QuranJourneyPageDialog(this).show();
      return;
    }

    // 1. Community Notifications (Accurate Post ID & Auto Comment Sheet)
    if (normAction.startsWith("feature_community") || normAction.equals("action_community") 
        || normAction.equals("community_feed") || normAction.equals("community") 
        || normType.contains("community")) {
      String targetPostId = null;
      if (action != null && action.startsWith("feature_community:post_id:")) {
        targetPostId = action.substring("feature_community:post_id:".length()).trim();
      } else if (action != null && action.startsWith("post_id:")) {
        targetPostId = action.substring("post_id:".length()).trim();
      } else if (sourceIntent != null && sourceIntent.hasExtra("extra_post_id")) {
        targetPostId = sourceIntent.getStringExtra("extra_post_id");
      }
      boolean openComments = normType.contains("comment") || normType.contains("reply")
          || normAction.contains("comment") || normAction.contains("reply")
          || (title != null && (title.contains("মন্তব্য") || title.contains("উত্তর")))
          || (body != null && (body.contains("মন্তব্য") || body.contains("উত্তর")));
      com.devflux.deenone.features.community.CommunityFeedDialog.show(this, targetPostId, openComments);
      return;
    }

    // 2. Quran Surah Direct Deep-Link
    if (normAction.equals("feature_quran_surah") || (sourceIntent != null && sourceIntent.hasExtra("extra_surah_number"))) {
      int surahNum = (sourceIntent != null) ? sourceIntent.getIntExtra("extra_surah_number", 1) : 1;
      int ayahNum = (sourceIntent != null) ? sourceIntent.getIntExtra("extra_ayah_number", 1) : 1;
      java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> allSurahs = com.devflux.deenone.data.local.QuranSurahDataSeeder.get114Surahs();
      com.devflux.deenone.data.local.entity.QuranSurahEntity targetSurah = null;
      for (com.devflux.deenone.data.local.entity.QuranSurahEntity s : allSurahs) {
        if (s.getNumber() == surahNum) {
          targetSurah = s;
          break;
        }
      }
      if (targetSurah != null) {
        showSurahReaderBottomSheet(targetSurah, ayahNum);
      } else {
        showQuranHubBottomSheet();
      }
      return;
    }

    // 3. Daily Hadith (আজকের নির্বাচিত সহীহ হাদিস)
    if (normAction.equals("daily_hadith") || normAction.equals("feature_daily_hadith") 
        || normAction.equals("hadith_of_the_day") || normAction.equals("feature_hadith") 
        || normAction.equals("action_hadith") || normAction.equals("hadith") 
        || normType.equals("daily_hadith") || normType.equals("hadith")) {
      showHadithOfTheDaySheet();
      return;
    }

    // 4. Daily Ayah (আজকের নির্বাচিত আয়াত ও তাফসীর)
    if (normAction.equals("daily_content") || normAction.equals("feature_daily_ayah") 
        || normAction.equals("ayah_of_the_day") || normAction.equals("daily_ayah") 
        || normAction.equals("daily_content") || normType.equals("daily_ayah") 
        || normType.equals("ayah") || normType.equals("daily_content")) {
      showAyahOfTheDaySheet();
      return;
    }

    // 5. Quran Hub (আল-কুরআন মাজীদ ও তিলাওয়াত)
    if (normAction.equals("feature_quran") || normAction.equals("action_quran") 
        || normAction.equals("quran") || normType.contains("quran") 
        || normType.contains("surah") || normType.contains("kahf")) {
      showQuranHubBottomSheet();
      return;
    }

    // 6. Dua & Azkar (মাসনূন দোয়া ও সকাল-সন্ধ্যার জিকির)
    if (normAction.equals("feature_dua") || normAction.equals("action_dua") 
        || normAction.equals("feature_azkar") || normAction.equals("action_azkar") 
        || normAction.equals("feature_dhikr") || normAction.equals("dua") 
        || normAction.equals("azkar") || normAction.equals("dhikr") 
        || normAction.equals("daily_dua") || normAction.equals("dua_tracker") 
        || normType.contains("dua") || normType.contains("azkar") || normType.contains("dhikr")) {
      showDuaSheet();
      return;
    }

    // 7. Islamic Quiz (ইসলামিক কুইজ ও প্রতিযোগিতা)
    if (normAction.equals("feature_quiz") || normAction.equals("action_quiz") 
        || normAction.equals("quiz") || normAction.equals("daily_quiz") 
        || normType.contains("quiz")) {
      showQuizSheet();
      return;
    }

    // 8. Daily Amal & Sunnah Tracker (দৈনিক আমল ও সুন্নাহ ট্র্যাকার)
    if (normAction.equals("feature_amal") || normAction.equals("action_amal") 
        || normAction.equals("amal_tracker") || normAction.equals("sunnah_tracker") 
        || normAction.equals("daily_amal") || normAction.equals("feature_sunnah") 
        || normAction.equals("amal") || normAction.equals("sunnah") 
        || normType.contains("amal") || normType.contains("sunnah") || normType.contains("muhasabah")) {
      showAmalTrackerSheet();
      return;
    }

    // 9. Salah & Prayer Tracker (সালাত ও ওয়াক্ত ট্র্যাকার)
    if (normAction.equals("feature_salah") || normAction.equals("feature_prayer") 
        || normAction.equals("action_salat") || normAction.equals("salah_tracker") 
        || normAction.equals("prayer") || normAction.equals("adhan") 
        || normType.contains("adhan") || normType.contains("prayer") 
        || normType.contains("salah") || normType.contains("tahajjud") || normType.contains("reminder")) {
      showSalahTrackerSheet();
      return;
    }

    // 10. Ramadan & Fasting (সেহরি, ইফতার ও রোজা)
    if (normAction.equals("feature_fasting") || normAction.equals("feature_ramadan") 
        || normAction.equals("fasting_tracker") || normAction.equals("action_roza") 
        || normAction.equals("action_ramadan") || normAction.equals("sehri") || normAction.equals("iftar") 
        || normType.contains("sehri") || normType.contains("iftar") 
        || normType.contains("ramadan") || normType.contains("fasting")) {
      showFastingTrackerSheet();
      return;
    }

    // 11. Jummah Mode (জুমু'আ মোড ও সূরা কাহাফ)
    if (normAction.equals("feature_jummah") || normAction.equals("jummah_mode") 
        || normAction.equals("action_jummah") || normAction.equals("jumuah_kahf") 
        || normAction.equals("jumuah") || normAction.equals("jummah") 
        || normType.contains("jummah") || normType.contains("jumuah")) {
      showJummahModeSheet();
      return;
    }

    // 12. Eid Mode (ঈদ মোড ও তাকবীর)
    if (normAction.equals("feature_eid") || normAction.equals("eid_mode") 
        || normAction.equals("action_eid") || normAction.equals("eid") 
        || normType.contains("eid")) {
      showEidModeSheet();
      return;
    }

    // 13. Knowledge Battle (দ্বীনি জ্ঞানযুদ্ধ)
    if (normAction.equals("feature_battle") || normAction.equals("action_knowledge_battle") 
        || normAction.equals("knowledge_battle") || normType.contains("battle")) {
      showKnowledgeBattleSheet();
      return;
    }

    // 13B. Muslim Marriage Guide (মুসলিম বিবাহ গাইড)
    if (normAction.equals("feature_marriage") || normAction.equals("action_marriage") 
        || normAction.equals("marriage") || normType.contains("marriage") || normType.contains("nikah")) {
      showMuslimMarriageSheet();
      return;
    }

    // 14. User Profile / Gamification / Points Recap (ব্যবহারকারীর প্রোফাইল ও পয়েন্ট সারাংশ)
    if (normAction.equals("feature_profile") 
        || normAction.equals("daily_recap") || normType.contains("recap") || normType.contains("profile")) {
      showUserProfileSheet();
      return;
    }

    // 15. Islamic Books & Library (ইসলামিক বই ও কিতাবখানা)
    if (normAction.equals("feature_books") || normAction.equals("feature_book") 
        || normAction.equals("action_books") || normAction.equals("books") 
        || normType.contains("book")) {
      showIslamicBooksHubSheet();
      return;
    }

    // 16. Islamic Audio Hub (ইসলামিক অডিও ও বয়ান)
    if (normAction.equals("feature_audio") || normAction.equals("action_audio") 
        || normAction.equals("audio") || normType.contains("audio")) {
      showIslamicAudioHub();
      return;
    }

    // 17. Blood Donation Network (রক্তদান নেটওয়ার্ক)
    if (normAction.equals("feature_blood") || normAction.equals("action_blood") 
        || normAction.equals("blood") || normType.contains("blood")) {
      showBloodDonationSheet();
      return;
    }

    // 18. Zakat Calculator (যাকাত ক্যালকুলেটর)
    if (normAction.equals("feature_zakat") || normAction.equals("action_zakat") 
        || normAction.equals("zakat") || normType.contains("zakat")) {
      showZakatSheet();
      return;
    }

    // 19. Digital Tasbih (ডিজিটাল তাসবিহ)
    if (normAction.equals("feature_tasbih") || normAction.equals("action_tasbih") 
        || normAction.equals("tasbih") || normType.contains("tasbih")) {
      showTasbihSheet();
      return;
    }

    // 20. Islamic Q&A / Masala / Fatwa (প্রশ্নোত্তর ও মাসআলা)
    if (normAction.equals("feature_qa") || normAction.equals("feature_fatwa") 
        || normAction.equals("feature_masala") || normAction.equals("qa_mode") 
        || normType.contains("qa") || normType.contains("fatwa") || normType.contains("masala")) {
      showIslamicQASheet();
      return;
    }

    // 21. Mosque Finder (মসজিদ অনুসন্ধান)
    if (normAction.equals("feature_mosque") || normAction.equals("action_mosque") 
        || normType.contains("mosque")) {
      showMosqueFinderSheet();
      return;
    }

    // 22. Islamic Calendar (হিজরি ও বাংলা ক্যালেন্ডার)
    if (normAction.equals("feature_calendar") || normAction.equals("action_calendar") 
        || normType.contains("calendar")) {
      showIslamicCalendarSheet();
      return;
    }

    // 23. App Update Action
    if (normAction.equals("feature_update") || normAction.equals("app_update")) {
      String updateUrl = com.devflux.deenone.core.admin.AdminRemoteConfigManager.getInstance(this).getUpdateUrl();
      if (updateUrl == null || updateUrl.trim().isEmpty()) {
        updateUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
      }
      try {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(updateUrl));
        startActivity(browserIntent);
      } catch (Exception e) {
        Toast.makeText(this, "DeenOne এর সর্বশেষ সংস্করণ সক্রিয় রয়েছে।", Toast.LENGTH_LONG).show();
      }
      return;
    }

    // 24. Infer destination from Title and Body keywords
    String combined = ((title != null ? title : "") + " " + (body != null ? body : "")).toLowerCase();
    if (combined.contains("কমিউনিটি") || combined.contains("মন্তব্য") || combined.contains("উত্তর দিয়েছেন") || (combined.contains("পোস্টে") && combined.contains("লাইক"))) {
      com.devflux.deenone.features.community.CommunityFeedDialog.show(this, null, combined.contains("মন্তব্য") || combined.contains("উত্তর"));
    } else if (combined.contains("কুরআন") || combined.contains("সূরা") || combined.contains("কাহাফ") || combined.contains("তিলাওয়াত") || combined.contains("আয়াত")) {
      showQuranHubBottomSheet();
    } else if (combined.contains("হাদিস") || combined.contains("সুন্নাত") || combined.contains("বুখারী") || combined.contains("মুসলিম") || combined.contains("তিরমিযী")) {
      showHadithOfTheDaySheet();
    } else if (combined.contains("নামাজ") || combined.contains("সালাত") || combined.contains("আযান") || combined.contains("তাহাজ্জুদ") || combined.contains("ওয়াক্ত")) {
      showSalahTrackerSheet();
    } else if (combined.contains("দোয়া") || combined.contains("দোয়া") || combined.contains("জিকির") || combined.contains("আজকার") || combined.contains("মুনাজাত")) {
      showDuaSheet();
    } else if (combined.contains("আমল") || combined.contains("নেকি") || combined.contains("পয়েন্ট") || combined.contains("মুহাসাবা")) {
      showAmalTrackerSheet();
    } else if (combined.contains("কুইজ")) {
      showQuizSheet();
    } else if (combined.contains("রোজা") || combined.contains("রমজান") || combined.contains("সেহরি") || combined.contains("ইফতার")) {
      showFastingTrackerSheet();
    } else if (combined.contains("হালাল") || combined.contains("খাদ্য")) {
      showHalalFoodsSheet();
    } else if (combined.contains("বই") || combined.contains("কিতাব")) {
      showIslamicBooksHubSheet();
    } else if (combined.contains("জুমু") || combined.contains("জুমা")) {
      showJummahModeSheet();
    } else if (combined.contains("ঈদ") || combined.contains("তাকবীর")) {
      showEidModeSheet();
    } else if (combined.contains("রক্তদান") || combined.contains("ব্লাড ডোনেশন") || combined.contains("রক্তদাতা")) {
      showBloodDonationSheet();
    } else if (combined.contains("যাকাত")) {
      showZakatSheet();
    } else if (combined.contains("তাসবিহ") || combined.contains("তাসবীহ") || combined.contains("গণনা")) {
      showTasbihSheet();
    } else {
      // Default to Home Launchpad
      if (binding != null) {
        binding.nestedScrollView.smoothScrollTo(0, 0);
      }
    }
  }

  public void showNotificationHistorySheet() {
    if (notificationRepository != null) {
      notificationRepository.seedInitialNotificationsIfEmpty();
      notificationRepository.performMidnightCleanup();
    }
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetNotificationHistoryBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetNotificationHistoryBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Dynamic Dual-Language Header & Empty State
    sheetBinding.tvNotifHeroBadge.setText(isBn ? "• আপডেট ও নোটিশ" : "• Updates & Notices");
    sheetBinding.tvNotifHeroTitle.setText(isBn ? "নোটিফিকেশন" : "Notifications");
    sheetBinding.tvNotifHeroSubtitle.setText(isBn ? "আপনার সর্বশেষ আপডেট" : "Your Latest Updates");
    sheetBinding.tvNotifEmptyTitle.setText(isBn ? "কোনো নোটিফিকেশন নেই" : "No Notifications");
    sheetBinding.tvNotifEmptySubtitle.setText(isBn
        ? "পরবর্তী আযান বা আমলের নোটিফিকেশন এখানে সংরক্ষিত হবে"
        : "Upcoming adhan or deed notifications will appear here");

    com.devflux.deenone.features.notifications.adapter.NotificationHistoryAdapter adapter =
        new com.devflux.deenone.features.notifications.adapter.NotificationHistoryAdapter(
            new com.devflux.deenone.features.notifications.adapter.NotificationHistoryAdapter.OnNotificationClickListener() {
              @Override
              public void onNotificationClick(com.devflux.deenone.data.local.entity.NotificationMessageEntity item) {
                if (item == null) return;
                notificationRepository.markAsRead(item.getId());
                dialog.dismiss();
                final String dLink = item.getDeepLinkAction();
                final String nType = item.getType();
                final String nTitle = item.getTitle();
                final String nMsg = item.getMessage();
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                  navigateToNotificationTarget(dLink, nType, nTitle, nMsg, null);
                }, 200);
              }

              @Override
              public void onDeleteClick(com.devflux.deenone.data.local.entity.NotificationMessageEntity item) {
                notificationRepository.deleteNotification(item.getId());
              }
            }
        );

    sheetBinding.rvNotificationHistory.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvNotificationHistory.setAdapter(adapter);

    notificationRepository.getAllNotifications().observe(this, list -> {
      adapter.setItems(list);
      boolean isEmpty = list == null || list.isEmpty();
      sheetBinding.layoutNotifEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
      sheetBinding.rvNotificationHistory.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
      int total = list != null ? list.size() : 0;
      sheetBinding.tvUnreadCountBadge.setText(isBn
          ? ("মোট: " + BengaliNumberUtil.toBengali(total))
          : ("Total: " + total));
    });

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnNotifThemeToggle);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnNotifBellAction);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseNotifHistorySheet);

    sheetBinding.btnNotifThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });
    sheetBinding.btnNotifBellAction.setOnClickListener(v -> {
      boolean curBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
      Toast.makeText(this, curBn ? "আপনি নোটিফিকেশন স্ক্রিনে আছেন" : "You are currently on the notifications screen", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnCloseNotifHistorySheet.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  // =========================================================================
  // General App Settings Sheet (অ্যাপ সেটিংস)
  // =========================================================================
  public void showAppSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetAppSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetAppSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Dynamic Dual-Language Labels & Headers
    sheetBinding.tvAppSettingsTitle.setText(isBn ? "অ্যাপ সেটিংস" : "App Settings");
    sheetBinding.tvAppSettingsSubtitle.setText(isBn
        ? "ওয়াক্ত গণনা, হিজরী সমন্বয় ও সাধারণ সেটিংস"
        : "Prayer Calculation, Hijri Adjustments & General Settings");

    sheetBinding.tvSectionPrimarySetup.setText(isBn ? "প্রাথমিক সেটআপ" : "Primary Setup");
    sheetBinding.tvSettingLocationTitle.setText(isBn ? "অবস্থান" : "Location");
    sheetBinding.tvSettingPrayerMethodTitle.setText(isBn ? "নামাজের সময় গণনা" : "Prayer Time Calculation");
    sheetBinding.tvSettingSalahAdjustmentTitle.setText(isBn ? "নামাজের সময় সমন্বয়" : "Salah Time Adjustment");
    sheetBinding.tvSettingMazhabTitle.setText(isBn ? "মাযহাব" : "Mazhab");

    sheetBinding.tvSectionCalendarSettings.setText(isBn ? "ক্যালেন্ডার সেটিংস" : "Calendar Settings");
    sheetBinding.tvSettingHijriDateSourceTitle.setText(isBn ? "হিজরী তারিখের উৎস" : "Hijri Date Source");
    sheetBinding.tvSettingHijriChangeTimingTitle.setText(isBn ? "হিজরী পরিবর্তনের সময়" : "Hijri Date Change Timing");
    sheetBinding.tvSettingBengaliCalendarTitle.setText(isBn ? "বঙ্গাব্দ (বাংলা ক্যালেন্ডার)" : "Bengali Calendar");

    sheetBinding.tvSettingHomeScreenWidgetsTitle.setText(isBn ? "হোম স্ক্রিন উইজেট" : "Home Screen Widgets");
    sheetBinding.tvSettingHomeScreenWidgetsSubtitle.setText(isBn
        ? "১৫টি অনন্য ডিজাইনের লাইভ উইজেট হোমস্ক্রিনে যুক্ত করুন"
        : "Add 15 beautiful live widgets to your home screen");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseAppSettingsSheet);

    sheetBinding.btnCloseAppSettingsSheet.setOnClickListener(v -> dialog.dismiss());

    // Update all initial summaries
    updateAppSettingsSummaries(sheetBinding);

    // 0. Location
    sheetBinding.cardSettingLocation.setOnClickListener(v -> {
      com.devflux.deenone.features.location.LocationSelectorPageDialog.show(this, () -> {
        if (viewModel != null) {
          viewModel.updateRealTimeCalculations();
        }
        updateAppSettingsSummaries(sheetBinding);
      });
    });

    // 1. Prayer time calculation method
    sheetBinding.cardSettingPrayerMethod.setOnClickListener(v -> {
      showPrayerTimeCalculationPageDialog(() -> updateAppSettingsSummaries(sheetBinding));
    });

    // 2. Salah time adjustment
    sheetBinding.cardSettingSalahAdjustment.setOnClickListener(v -> {
      showSalahTimeAdjustmentDialog(() -> updateAppSettingsSummaries(sheetBinding));
    });

    // 3. Mazhab
    sheetBinding.cardSettingMazhab.setOnClickListener(v -> {
      showMazhabSelectorDialog(() -> updateAppSettingsSummaries(sheetBinding));
    });

    // --- Calendar Settings (ক্যালেন্ডার সেটিংস) ---
    // 1. Hijri date source
    sheetBinding.cardSettingHijriDateSource.setOnClickListener(v -> {
      com.devflux.deenone.features.calendar.HijriDateSourcePageDialog.show(this, (source, offset) -> {
        if (viewModel != null) {
          viewModel.updateRealTimeCalculations();
        }
        updateAppSettingsSummaries(sheetBinding);
      });
    });

    // 2. Hijri date change timing
    sheetBinding.cardSettingHijriChangeTiming.setOnClickListener(v -> {
      com.devflux.deenone.features.calendar.HijriDateChangeTimingPageDialog.show(this, timing -> {
        if (viewModel != null) {
          viewModel.updateRealTimeCalculations();
        }
        updateAppSettingsSummaries(sheetBinding);
      });
    });

    // 3. Bengali Calendar / বঙ্গাব্দ
    sheetBinding.cardSettingBengaliCalendar.setOnClickListener(v -> {
      com.devflux.deenone.features.calendar.BengaliCalendarSettingsPageDialog.show(this, (enabled, bMethod) -> {
        if (viewModel != null) {
          viewModel.updateRealTimeCalculations();
        }
        updateAppSettingsSummaries(sheetBinding);
      });
    });

    // 3. Home Screen Widgets
    sheetBinding.cardSettingHomeScreenWidgets.setOnClickListener(v -> {
      com.devflux.deenone.widget.BottomSheetHomeScreenWidget.show(this);
    });

    dialog.show();
  }

  private void updateAppSettingsSummaries(com.devflux.deenone.databinding.BottomSheetAppSettingsBinding sheetBinding) {
    if (sheetBinding == null) return;

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // 0. Location
    com.devflux.deenone.core.location.LocationProvider.Coordinates coords =
        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(this);
    String locName = coords != null && coords.locationName != null
        ? coords.locationName.replace("📍", "").trim()
        : (isBn ? "বর্তমান অবস্থান" : "Current Location");
    sheetBinding.tvCurrentLocationSummary.setText(locName);

    // 1. Prayer time calculation method
    boolean isAutoMethod = com.devflux.deenone.core.prayer.PrayerSettingsManager.isAutoMethod(this);
    com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod method =
        com.devflux.deenone.core.prayer.PrayerSettingsManager.getCalculationMethod(this);
    String methodName = (method != null) ? method.getLocalizedName(this) : (isBn ? "ইসলামিক ফাউন্ডেশন বাংলাদেশ" : "Islamic Foundation Bangladesh");
    String methodSummary = isAutoMethod
        ? (isBn ? ("স্বয়ংক্রিয় (" + methodName + ")") : ("Automatic (" + methodName + ")"))
        : methodName;
    sheetBinding.tvCurrentPrayerMethodSummary.setText(methodSummary);

    // 2. Salah time adjustment
    sheetBinding.tvCurrentSalahAdjustmentSummary.setText(
        com.devflux.deenone.core.prayer.PrayerSettingsManager.getFormattedOffsetSummary(this)
    );

    // 3. Mazhab
    sheetBinding.tvCurrentMazhabSummary.setText(
        com.devflux.deenone.core.prayer.PrayerSettingsManager.getMazhabSubtitle(this)
    );

    // 4. Hijri date source
    sheetBinding.tvCurrentHijriDateSourceSummary.setText(
        com.devflux.deenone.core.calendar.CalendarSettingsManager.getHijriSourceSummary(this)
    );

    // 5. Hijri date change timing
    sheetBinding.tvCurrentHijriChangeTimingSummary.setText(
        com.devflux.deenone.core.calendar.CalendarSettingsManager.getHijriChangeTimingSummary(this)
    );

    // 6. Bengali Calendar / বঙ্গাব্দ
    sheetBinding.tvCurrentBengaliCalendarSummary.setText(
        com.devflux.deenone.core.calendar.CalendarSettingsManager.getBengaliCalendarSummary(this)
    );
  }

  private boolean deleteDir(java.io.File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      if (children != null) {
        for (String child : children) {
          boolean success = deleteDir(new java.io.File(dir, child));
          if (!success) {
            return false;
          }
        }
      }
      return dir.delete();
    } else if (dir != null && dir.isFile()) {
      return dir.delete();
    } else {
      return false;
    }
  }

  private com.devflux.deenone.databinding.BottomSheetSoundAndNotificationBinding soundAndNotifHubBinding;

  public void showSoundAndNotificationSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetSoundAndNotificationBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetSoundAndNotificationBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());
    soundAndNotifHubBinding = sheetBinding;

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvSoundNotifTitle.setText(isBn ? "নোটিফিকেশন ও সাউন্ড" : "Notification & Sound");
    sheetBinding.tvPermissionWarningTitle.setText(isBn ? "নোটিফিকেশন অনুমতি প্রয়োজন" : "Notification Permission Required");
    sheetBinding.tvPermissionWarningDesc.setText(isBn ? "সময়মতো সালাত ও গুরুত্বপূর্ণ অ্যালার্ট পেতে নোটিফিকেশন পারমিশন চালু করুন।" : "Enable notification permission to receive timely prayer and critical alerts.");
    sheetBinding.btnGrantNotificationPermission.setText(isBn ? "অনুমতি দিন" : "Grant Permission");
    sheetBinding.tvTitlePrayerAlert.setText(isBn ? "নামাজের অ্যালার্ট ও আজান" : "Prayer Alert & Adhan");
    sheetBinding.tvSummaryPrayerAlert.setText(isBn ? "ওয়াক্তভিত্তিক আজান, প্রি-অ্যালার্ট ও রিংটোন" : "Waqt-based adhan, pre-alerts & ringtone");
    sheetBinding.tvTitleGeneralNotif.setText(isBn ? "সাধারণ নোটিফিকেশন সেটিংস" : "General Notification Settings");
    sheetBinding.tvSummaryGeneralNotif.setText(isBn ? "দৈনিক আমল, হাদীস ও কুইজ নোটিফিকেশন" : "Daily amal, hadith & quiz alerts");
    sheetBinding.tvTitleRamadan.setText(isBn ? "রমজান ও নফল সিয়াম রিমাইন্ডার" : "Ramadan & Sunnah Fasting");
    sheetBinding.tvSummaryRamadan.setText(isBn ? "সেহরি-ইফতার অ্যালার্ট ও নফল সিয়াম স্মরণিকা" : "Sehri-iftar alerts & fasting reminders");
    sheetBinding.tvTitleDurud.setText(isBn ? "দরূদ স্মরণিকা" : "Durood Reminder");
    sheetBinding.tvSummaryDurud.setText(isBn ? "নির্দিষ্ট বিরতিতে অডিও ও নোটিফিকেশন স্মরণিকা" : "Periodic audio & notif reminder");
    sheetBinding.tvTitleIstigfar.setText(isBn ? "ইস্তিগফার স্মরণিকা" : "Istighfar Reminder");
    sheetBinding.tvSummaryIstigfar.setText(isBn ? "পরিমিত বিরতিতে ক্ষমা প্রার্থনার অডিও স্মরণিকা" : "Periodic forgiveness audio reminder");

    // Update status badges and summaries
    updateSoundAndNotificationHubSummaries(sheetBinding);

    // Permission check
    boolean notifsPermitted = androidx.core.app.NotificationManagerCompat.from(this).areNotificationsEnabled();
    sheetBinding.cardPermissionWarning.setVisibility(notifsPermitted ? View.GONE : View.VISIBLE);
    sheetBinding.btnGrantNotificationPermission.setOnClickListener(v -> {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
      } else {
        try {
          Intent intent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
          intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName());
          startActivity(intent);
        } catch (Exception e) {
          Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
          startActivity(intent);
        }
      }
    });

    // 1. নামাজের অ্যালার্ট ও আজান
    sheetBinding.cardOptionPrayerAlertAndAdhan.setOnClickListener(v -> {
      showPrayerAlertAndAdhanSettingsSheet();
    });

    // 2. Notification Settings
    sheetBinding.cardOptionNotificationSettings.setOnClickListener(v -> {
      showNotificationSettingsSheet();
    });

    // 3. Ramadan Reminder
    sheetBinding.cardOptionRamadanReminder.setOnClickListener(v -> {
      showRamadanReminderSettingsSheet();
    });

    // 4. Durud Reminder
    sheetBinding.cardOptionDurudReminder.setOnClickListener(v -> {
      showDurudReminderSettingsSheet();
    });

    // 5. Istigfar Reminder
    sheetBinding.cardOptionIstigfarReminder.setOnClickListener(v -> {
      showIstigfarReminderSettingsSheet();
    });

    sheetBinding.btnCloseSoundNotifSheet.setOnClickListener(v -> dialog.dismiss());
    dialog.setOnDismissListener(d -> soundAndNotifHubBinding = null);
    dialog.show();
  }

  private void updateSoundAndNotificationHubSummaries() {
    if (soundAndNotifHubBinding != null) {
      updateSoundAndNotificationHubSummaries(soundAndNotifHubBinding);
    }
  }

  private void updateSoundAndNotificationHubSummaries(com.devflux.deenone.databinding.BottomSheetSoundAndNotificationBinding sheetBinding) {
    if (sheetBinding == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // 1. Prayer & Adhan status (checks if any waqt reminder is active)
    boolean anyWaqtOn = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.isAnyWaqtEnabled(this);
    sheetBinding.ivStatusPrayerAlert.setImageResource(anyWaqtOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);
    if (anyWaqtOn) {
      sheetBinding.tvSummaryPrayerAlert.setText(isBn ? "ওয়াক্তভিত্তিক আজান ও অ্যালার্ট সক্রিয়" : "Waqt-based adhan & alerts active");
    } else {
      sheetBinding.tvSummaryPrayerAlert.setText(isBn ? "সকল ওয়াক্তের আজান ও অ্যালার্ট বন্ধ" : "All waqt adhan & alerts off");
    }

    // 2. General Notification Settings
    boolean dailyAmalOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isDailyAmalEnabled(this);
    boolean quizOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isDailyQuizEnabled(this);
    boolean generalOn = dailyAmalOn || quizOn;
    sheetBinding.ivStatusGeneralNotif.setImageResource(generalOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);
    if (generalOn) {
      sheetBinding.tvSummaryGeneralNotif.setText(isBn ? "আমল, হাদীস ও কুইজ নোটিফিকেশন চালু" : "Daily amal, hadith & quiz alerts on");
    } else {
      sheetBinding.tvSummaryGeneralNotif.setText(isBn ? "সাধারণ পুশ নোটিফিকেশন বন্ধ আছে" : "General push notifications off");
    }

    // 3. Ramadan status
    boolean ramadanOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanReminderEnabled(this);
    int sehriMin = com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanSehriOffsetMinutes(this);
    sheetBinding.ivStatusRamadan.setImageResource(ramadanOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);
    if (ramadanOn) {
      String sehriText = isBn ? ("সেহরি " + BengaliNumberUtil.toBengali(sehriMin) + " মি. পূর্বে ও ইফতার অ্যালার্ট") : ("Sehri " + sehriMin + "m before & iftar alert");
      sheetBinding.tvSummaryRamadan.setText(sehriText);
    } else {
      sheetBinding.tvSummaryRamadan.setText(isBn ? "সেহরি ও ইফতার রিমাইন্ডার বন্ধ আছে" : "Sehri & iftar reminders off");
    }

    // 4. Durud status
    boolean durudOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isDurudReminderEnabled(this);
    int durudMin = com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudIntervalMinutes(this);
    sheetBinding.ivStatusDurud.setImageResource(durudOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);
    if (durudOn) {
      String durudIntervalLabel = isBn ? (durudMin < 60 ? (BengaliNumberUtil.toBengali(durudMin) + " মিনিট পরপর দরূদ স্মরণিকা") : (BengaliNumberUtil.toBengali(durudMin / 60) + " ঘণ্টা পরপর দরূদ স্মরণিকা")) : (durudMin < 60 ? ("Reminder every " + durudMin + " min") : ("Reminder every " + (durudMin / 60) + " hr"));
      sheetBinding.tvSummaryDurud.setText(durudIntervalLabel);
    } else {
      sheetBinding.tvSummaryDurud.setText(isBn ? "দরূদ অডিও ও নোটিফিকেশন বন্ধ" : "Durood audio & notif off");
    }

    // 5. Istigfar status
    boolean istigfarOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isIstigfarReminderEnabled(this);
    int istigfarMin = com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarIntervalMinutes(this);
    sheetBinding.ivStatusIstigfar.setImageResource(istigfarOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);
    if (istigfarOn) {
      String intervalLabel = isBn ? (istigfarMin < 60 ? (BengaliNumberUtil.toBengali(istigfarMin) + " মিনিট পরপর ইস্তিগফার স্মরণিকা") : (BengaliNumberUtil.toBengali(istigfarMin / 60) + " ঘণ্টা পরপর ইস্তিগফার স্মরণিকা")) : (istigfarMin < 60 ? ("Reminder every " + istigfarMin + " min") : ("Reminder every " + (istigfarMin / 60) + " hr"));
      sheetBinding.tvSummaryIstigfar.setText(intervalLabel);
    } else {
      sheetBinding.tvSummaryIstigfar.setText(isBn ? "ইস্তিগফার অডিও ও নোটিফিকেশন বন্ধ" : "Istighfar audio & notif off");
    }
  }

  public void showPrayerAlertAndAdhanSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetPrayerAlertAndAdhanBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetPrayerAlertAndAdhanBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Dynamic dual-language localization
    sheetBinding.tvSetAlarmHeaderTitle.setText(isBn ? "নামাজের অ্যালার্ম সেটিংস" : "Prayer Alarm Settings");
    sheetBinding.tvAllPrayerAlarmsTitle.setText(isBn ? "সকল নামাজের অ্যালার্ম" : "All Prayer Alarms");
    sheetBinding.tvBatterySettingsTitle.setText(isBn ? "অ্যালার্ম সেটিংস কনফিগার করুন" : "Configure Alarm Settings");
    sheetBinding.tvBatterySettingsSubtitle.setText(isBn ? "নামাজের সঠিক সময়ে রিমাইন্ডার পেতে ব্যাটারি অপটিমাইজেশন সেটিংস কনফিগার করুন" : "Configure your device's battery settings to get prayer reminders working correctly");
    sheetBinding.btnConfigureBattery.setText(isBn ? "কনফিগার" : "Configure");
    sheetBinding.tvFarjSectionTitle.setText(isBn ? "ফরজ নামাজ" : "Farj Prayers");
    sheetBinding.tvNaflSectionTitle.setText(isBn ? "নফল নামাজ" : "Nafl Prayers");

    // Prayer titles
    sheetBinding.tvTitleFajr.setText(isBn ? "ফজর" : "Fajr");
    sheetBinding.tvTitleDhuhr.setText(isBn ? "যোহর" : "Dhuhr");
    sheetBinding.tvTitleJummah.setText(isBn ? "জুমুআ" : "Jummah");
    sheetBinding.tvTitleAsr.setText(isBn ? "আসর" : "Asr");
    sheetBinding.tvTitleMaghrib.setText(isBn ? "মাগরিব" : "Maghrib");
    sheetBinding.tvTitleIsha.setText(isBn ? "এশা" : "Isha");
    sheetBinding.tvTitleTahajjud.setText(isBn ? "তাহাজ্জুদ" : "Tahajjud");
    sheetBinding.tvTitleIshraq.setText(isBn ? "ইশরাক" : "Ishraq");
    sheetBinding.tvTitleChasht.setText(isBn ? "চাশত" : "Chasht");
    sheetBinding.tvTitleAwwabin.setText(isBn ? "আওয়াবিন" : "Awwabin");

    // Helper to refresh all Farj and Nafl rows
    Runnable refreshAllPrayerRows = () -> {
      boolean isAnyOn = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.isAnyWaqtEnabled(this);
      sheetBinding.switchAllPrayerAlarmsMaster.setChecked(isAnyOn);
      sheetBinding.tvMasterAlarmStatusSubtitle.setText(isAnyOn ? (isBn ? "অ্যালার্ম সক্রিয়" : "Alarm Enabled") : (isBn ? "অ্যালার্ম বন্ধ" : "Alarm Disabled"));
      int mintColor = androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint);
      int secondaryColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);
      sheetBinding.tvMasterAlarmStatusSubtitle.setTextColor(isAnyOn ? mintColor : secondaryColor);
      sheetBinding.ivMasterAlarmBell.setImageResource(isAnyOn ? R.drawable.ic_notifications : R.drawable.ic_notifications_off);
      sheetBinding.ivMasterAlarmBell.setColorFilter(isAnyOn ? mintColor : secondaryColor);

      // Farj Prayers
      updatePrayerRowUI("fajr", sheetBinding.ivIconFajr, sheetBinding.tvTitleFajr, sheetBinding.tvWaqtFajrStatus, sheetBinding.ivBellFajr);
      updatePrayerRowUI("dhuhr", sheetBinding.ivIconDhuhr, sheetBinding.tvTitleDhuhr, sheetBinding.tvWaqtDhuhrStatus, sheetBinding.ivBellDhuhr);
      updatePrayerRowUI("jummah", sheetBinding.ivIconJummah, sheetBinding.tvTitleJummah, sheetBinding.tvWaqtJummahStatus, sheetBinding.ivBellJummah);
      updatePrayerRowUI("asr", sheetBinding.ivIconAsr, sheetBinding.tvTitleAsr, sheetBinding.tvWaqtAsrStatus, sheetBinding.ivBellAsr);
      updatePrayerRowUI("maghrib", sheetBinding.ivIconMaghrib, sheetBinding.tvTitleMaghrib, sheetBinding.tvWaqtMaghribStatus, sheetBinding.ivBellMaghrib);
      updatePrayerRowUI("isha", sheetBinding.ivIconIsha, sheetBinding.tvTitleIsha, sheetBinding.tvWaqtIshaStatus, sheetBinding.ivBellIsha);

      // Nafl Prayers
      updatePrayerRowUI("tahajjud", sheetBinding.ivIconTahajjud, sheetBinding.tvTitleTahajjud, sheetBinding.tvWaqtTahajjudStatus, sheetBinding.ivBellTahajjud);
      updatePrayerRowUI("ishraq", sheetBinding.ivIconIshraq, sheetBinding.tvTitleIshraq, sheetBinding.tvWaqtIshraqStatus, sheetBinding.ivBellIshraq);
      updatePrayerRowUI("chasht", sheetBinding.ivIconChasht, sheetBinding.tvTitleChasht, sheetBinding.tvWaqtChashtStatus, sheetBinding.ivBellChasht);
      updatePrayerRowUI("awwabin", sheetBinding.ivIconAwwabin, sheetBinding.tvTitleAwwabin, sheetBinding.tvWaqtAwwabinStatus, sheetBinding.ivBellAwwabin);
    };

    refreshAllPrayerRows.run();

    // 1. Master "All Prayer Alarms" Switch
    sheetBinding.switchAllPrayerAlarmsMaster.setOnCheckedChangeListener((btn, isChecked) -> {
      if (!btn.isPressed()) return;
      com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.setAllWaqtEnabled(this, isChecked);
      com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(this);
      refreshAllPrayerRows.run();
      updateSoundAndNotificationHubSummaries();
      Toast.makeText(this, isChecked ? (isBn ? "সকল নামাজের অ্যালার্ম সক্রিয় করা হয়েছে" : "All prayer alarms enabled") : (isBn ? "সকল নামাজের অ্যালার্ম বন্ধ করা হয়েছে" : "All prayer alarms disabled"), Toast.LENGTH_SHORT).show();
    });

    // 2. Battery & Alarm Permission Settings Button
    View.OnClickListener openBatteryConfig = v -> {
      try {
        Intent intent = new Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
      } catch (Exception e) {
        try {
          Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
          intent.setData(android.net.Uri.parse("package:" + getPackageName()));
          startActivity(intent);
        } catch (Exception ignored) {
          Toast.makeText(this, isBn ? "ডিভাইস সেটিংস ওপেন করা যায়নি" : "Could not open device settings", Toast.LENGTH_SHORT).show();
        }
      }
    };
    sheetBinding.btnConfigureBattery.setOnClickListener(openBatteryConfig);
    sheetBinding.cardConfigureBatterySettings.setOnClickListener(openBatteryConfig);

    // 3. Farj Prayers Card Clicks
    sheetBinding.cardWaqtFajr.setOnClickListener(v -> showPrayerAlarmConfigSheet("fajr", refreshAllPrayerRows));
    sheetBinding.cardWaqtDhuhr.setOnClickListener(v -> showPrayerAlarmConfigSheet("dhuhr", refreshAllPrayerRows));
    sheetBinding.cardWaqtJummah.setOnClickListener(v -> showPrayerAlarmConfigSheet("jummah", refreshAllPrayerRows));
    sheetBinding.cardWaqtAsr.setOnClickListener(v -> showPrayerAlarmConfigSheet("asr", refreshAllPrayerRows));
    sheetBinding.cardWaqtMaghrib.setOnClickListener(v -> showPrayerAlarmConfigSheet("maghrib", refreshAllPrayerRows));
    sheetBinding.cardWaqtIsha.setOnClickListener(v -> showPrayerAlarmConfigSheet("isha", refreshAllPrayerRows));

    // 4. Nafl Prayers Card Clicks
    sheetBinding.cardWaqtTahajjud.setOnClickListener(v -> showPrayerAlarmConfigSheet("tahajjud", refreshAllPrayerRows));
    sheetBinding.cardWaqtIshraq.setOnClickListener(v -> showPrayerAlarmConfigSheet("ishraq", refreshAllPrayerRows));
    sheetBinding.cardWaqtChasht.setOnClickListener(v -> showPrayerAlarmConfigSheet("chasht", refreshAllPrayerRows));
    sheetBinding.cardWaqtAwwabin.setOnClickListener(v -> showPrayerAlarmConfigSheet("awwabin", refreshAllPrayerRows));

    sheetBinding.btnBackPrayerAdhanSheet.setOnClickListener(v -> dialog.dismiss());
    dialog.setOnDismissListener(d -> updateSoundAndNotificationHubSummaries());
    dialog.show();
  }

  private void updatePrayerRowUI(String waqtKey, android.widget.ImageView ivIcon, android.widget.TextView tvTitle, android.widget.TextView tvStatus, android.widget.ImageView ivBell) {
    if (tvStatus == null || ivBell == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.PrayerAlarmConfig config =
        com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.getConfig(this, waqtKey);

    int mintColor = androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint);
    int secondaryColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);
    int whiteColor = 0xFFFFFFFF;

    if (config.isEnabled) {
      if (config.prePrayerReminderMinutes == 0) {
        tvStatus.setText(isBn ? "সঠিক সময়ে" : "On Exact time");
      } else {
        tvStatus.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(config.prePrayerReminderMinutes) + " মিনিট আগে") : (config.prePrayerReminderMinutes + " min before"));
      }
      tvStatus.setTextColor(0xFFCBD5E1);
      if (tvTitle != null) {
        tvTitle.setTextColor(whiteColor);
      }
      if (ivIcon != null) {
        ivIcon.setColorFilter(mintColor);
      }
      ivBell.setImageResource(R.drawable.ic_notifications);
      ivBell.setColorFilter(whiteColor);
    } else {
      tvStatus.setText(isBn ? "অ্যালার্ম বন্ধ" : "Alarm Off");
      tvStatus.setTextColor(secondaryColor);
      if (tvTitle != null) {
        tvTitle.setTextColor(secondaryColor);
      }
      if (ivIcon != null) {
        ivIcon.setColorFilter(secondaryColor);
      }
      ivBell.setImageResource(R.drawable.ic_notifications_off);
      ivBell.setColorFilter(secondaryColor);
    }
  }

  private android.media.MediaPlayer prayerPreviewPlayer;

  public void showPrayerAlarmConfigSheet(String initialWaqt) {
    showPrayerAlarmConfigSheet(initialWaqt, null);
  }

  public void showPrayerAlarmConfigSheet(String initialWaqt, Runnable onSaveCallback) {
    if (initialWaqt == null) initialWaqt = "fajr";
    final String waqtKey = initialWaqt.toLowerCase().trim();

    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetPrayerAlarmConfigBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetPrayerAlarmConfigBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.PrayerAlarmConfig config =
        com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.getConfig(this, waqtKey);

    // 1. Waqt Title & Static Labels Localization
    String waqtName = isBn ? com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.getWaqtDisplayName(waqtKey)
        : com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.getWaqtEnglishName(waqtKey);
    sheetBinding.tvConfiguringWaqtTitle.setText(waqtName);
    sheetBinding.tvPrayerAlarmTitle.setText(isBn ? "নামাজের অ্যালার্ম" : "Prayer Alarm");
    sheetBinding.tvTimeAdjustmentTitle.setText(isBn ? "সময় সমন্বয়" : "Time Adjustment");
    sheetBinding.tvSliderMinusLabel.setText(isBn ? "-৩০ মি." : "-30 min");
    sheetBinding.tvSliderPlusLabel.setText(isBn ? "+৩০ মি." : "+30 min");
    sheetBinding.tvAlarmSoundTitle.setText(isBn ? "অ্যালার্মের সুর" : "Alarm Sound");
    sheetBinding.tvTitleSilent.setText(isBn ? "নিঃশব্দ" : "Silent");
    if ("fajr".equalsIgnoreCase(waqtKey)) {
      sheetBinding.tvTitleAzanDefault.setText(isBn ? "ফজর বিশেষ আযান" : "Azan (Fajr Special)");
    } else {
      sheetBinding.tvTitleAzanDefault.setText(isBn ? "ডিফল্ট আযান" : "Azan (Default)");
    }
    sheetBinding.tvSubtitleAzanDefault.setText(isBn ? "ওয়াক্তের নির্ধারিত আযান" : "Default Waqt Azan");
    sheetBinding.tvTitleAzanCommon.setText(isBn ? "সাধারণ আযান" : "Azan (Common)");
    sheetBinding.tvTitleAzanStandard.setText(isBn ? "স্ট্যান্ডার্ড আযান" : "Azan (Standard)");
    sheetBinding.tvTitleBeep.setText(isBn ? "মৃদু বীপ" : "Beep");
    sheetBinding.tvTitleRing.setText(isBn ? "রিংটোন" : "Ring");
    sheetBinding.tvTitleNotification.setText(isBn ? "নোটিফিকেশন সুর" : "Notification");
    sheetBinding.tvVibrationTitle.setText(isBn ? "ভাইব্রেশন" : "Vibration");
    sheetBinding.tvVibrationSubtitle.setText(isBn ? "অ্যালার্ম বাজার সময় ভাইব্রেট করবে" : "Vibrate with alarm");
    sheetBinding.btnSavePrayerAlarmConfig.setText(isBn ? "সংরক্ষণ করুন" : "Save");

    int iconRes;
    switch (waqtKey) {
      case "fajr":
      case "ishraq":
        iconRes = R.drawable.ic_sunrise_vector;
        break;
      case "dhuhr":
      case "chasht":
      case "duha":
      case "asr":
        iconRes = R.drawable.ic_sun;
        break;
      case "jummah":
        iconRes = R.drawable.ic_feat_jumma;
        break;
      case "maghrib":
      case "awwabin":
        iconRes = R.drawable.ic_sunset_vector;
        break;
      case "isha":
        iconRes = R.drawable.ic_moon;
        break;
      default:
        iconRes = R.drawable.ic_mosque;
        break;
    }
    sheetBinding.ivWaqtBadgeIcon.setImageResource(iconRes);

    int mintColor = androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint);
    int secondaryColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);
    int whiteColor = 0xFFFFFFFF;

    // 2. Prayer Alarm Master Switch for this Waqt
    sheetBinding.switchWaqtAlarmEnabled.setChecked(config.isEnabled);

    // Audio Preview Helper
    final String[] currentPlayingSound = new String[]{""};
    Runnable stopPreviewPlayer = () -> {
      if (prayerPreviewPlayer != null) {
        try {
          if (prayerPreviewPlayer.isPlaying()) {
            prayerPreviewPlayer.stop();
          }
          prayerPreviewPlayer.release();
        } catch (Exception ignored) {}
        prayerPreviewPlayer = null;
      }
      currentPlayingSound[0] = "";
      sheetBinding.ivPlayIconAzanDefault.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAzanCommon.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAzanStandard.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconBeep.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconRing.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconNotification.setImageResource(R.drawable.ic_play_arrow);
    };

    java.util.function.Consumer<Boolean> updateAlarmEnableState = isEnabled -> {
      sheetBinding.tvPrayerAlarmSubtitle.setText(isEnabled ? (isBn ? "অ্যালার্ম সক্রিয়" : "Alarm Enabled") : (isBn ? "অ্যালার্ম বন্ধ" : "Alarm Disabled"));
      sheetBinding.tvPrayerAlarmSubtitle.setTextColor(isEnabled ? mintColor : secondaryColor);
      sheetBinding.ivWaqtBadgeIcon.setColorFilter(isEnabled ? mintColor : secondaryColor);

      float targetAlpha = isEnabled ? 1.0f : 0.35f;
      sheetBinding.cardTimeAdjustment.setAlpha(targetAlpha);
      sheetBinding.cardAlarmSoundList.setAlpha(targetAlpha);
      sheetBinding.cardVibration.setAlpha(targetAlpha);

      sheetBinding.seekBarTimeAdjustment.setEnabled(isEnabled);
      sheetBinding.switchVibration.setEnabled(isEnabled);

      if (!isEnabled) {
        stopPreviewPlayer.run();
      }
    };

    updateAlarmEnableState.accept(config.isEnabled);

    sheetBinding.switchWaqtAlarmEnabled.setOnCheckedChangeListener((btn, isChecked) -> {
      updateAlarmEnableState.accept(isChecked);
    });

    // 3. Time Adjustment Slider & Badge
    String basePrayerTimeStr = "05:00";
    long basePrayerMillis = 0;
    try {
      double lat = com.devflux.deenone.core.constants.AppConstants.DEFAULT_LATITUDE;
      double lng = com.devflux.deenone.core.constants.AppConstants.DEFAULT_LONGITUDE;
      double tz = com.devflux.deenone.core.constants.AppConstants.DEFAULT_TIMEZONE;
      com.devflux.deenone.utils.PrayerCalculator.PrayerTimesResult pr =
          com.devflux.deenone.utils.PrayerCalculator.calculateForLocationWithContext(this, lat, lng, tz, java.util.Calendar.getInstance());
      switch (waqtKey) {
        case "fajr": basePrayerTimeStr = pr.fajrStr; basePrayerMillis = pr.fajrMillis; break;
        case "dhuhr":
        case "jummah": basePrayerTimeStr = pr.zohrStr; basePrayerMillis = pr.zohrMillis; break;
        case "asr": basePrayerTimeStr = pr.asrStr; basePrayerMillis = pr.asrMillis; break;
        case "maghrib": basePrayerTimeStr = pr.maghribStr; basePrayerMillis = pr.maghribMillis; break;
        case "isha": basePrayerTimeStr = pr.ishaStr; basePrayerMillis = pr.ishaMillis; break;
        case "tahajjud": basePrayerTimeStr = pr.tahajjudStr; basePrayerMillis = pr.tahajjudMillis; break;
        case "ishraq": basePrayerTimeStr = pr.ishraqStr; basePrayerMillis = pr.ishraqMillis; break;
        case "chasht":
        case "duha": basePrayerTimeStr = pr.chashtStr; basePrayerMillis = pr.chashtMillis; break;
        case "awwabin": basePrayerTimeStr = pr.awwabinStr; basePrayerMillis = pr.awwabinMillis; break;
        case "sunrise": basePrayerTimeStr = pr.sunriseStr; basePrayerMillis = pr.sunriseMillis; break;
      }
    } catch (Exception ignored) {}

    final long finalBaseMillis = basePrayerMillis;
    final String finalBaseTimeStr = basePrayerTimeStr;

    int initialOffset = -config.prePrayerReminderMinutes;
    if (config.prePrayerReminderMinutes == 0) initialOffset = 0;
    int initialProgress = 30 + initialOffset;
    if (initialProgress < 0) initialProgress = 0;
    if (initialProgress > 60) initialProgress = 60;
    sheetBinding.seekBarTimeAdjustment.setProgress(initialProgress);

    class SliderUpdater {
      void update(int progress) {
        int offset = progress - 30;
        if (offset == 0) {
          sheetBinding.tvSliderCenterLabel.setText(isBn ? "সঠিক সময়ে" : "Exact time");
          sheetBinding.tvSliderCenterLabel.setTextColor(mintColor);
        } else if (offset < 0) {
          sheetBinding.tvSliderCenterLabel.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(Math.abs(offset)) + " মিনিট আগে") : (Math.abs(offset) + " min before"));
          sheetBinding.tvSliderCenterLabel.setTextColor(whiteColor);
        } else {
          sheetBinding.tvSliderCenterLabel.setText(isBn ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(offset) + " মিনিট পরে") : (offset + " min after"));
          sheetBinding.tvSliderCenterLabel.setTextColor(whiteColor);
        }

        if (finalBaseMillis > 0) {
          long adjustedMillis = finalBaseMillis + (offset * 60 * 1000L);
          java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm", java.util.Locale.getDefault());
          String formattedTime = isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(sdf.format(new java.util.Date(adjustedMillis))) : sdf.format(new java.util.Date(adjustedMillis));
          sheetBinding.tvAdjustedTimeBadge.setText("🔔 " + formattedTime);
        } else {
          sheetBinding.tvAdjustedTimeBadge.setText("🔔 " + (isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(finalBaseTimeStr) : finalBaseTimeStr));
        }
      }
    }
    SliderUpdater sliderUpdater = new SliderUpdater();
    sliderUpdater.update(initialProgress);

    sheetBinding.seekBarTimeAdjustment.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
        sliderUpdater.update(progress);
      }
      @Override public void onStartTrackingTouch(android.widget.SeekBar seekBar) {}
      @Override public void onStopTrackingTouch(android.widget.SeekBar seekBar) {}
    });

    // 4. Sound Selection Logic
    final String[] selectedSoundHolder = new String[]{
        config.soundType != null ? config.soundType : com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_DEFAULT
    };

    if ("fajr".equalsIgnoreCase(waqtKey)) {
      sheetBinding.tvTitleAzanDefault.setText("Azan (Fajr Special)");
    } else {
      sheetBinding.tvTitleAzanDefault.setText("Azan (Default)");
    }

    Runnable updateSoundRadiosUI = () -> {
      String sel = selectedSoundHolder[0];
      int checkedRes = R.drawable.ic_radio_circle_checked;
      int emptyRes = R.drawable.ic_radio_circle_empty;
      int selectedBg = 0xFF0E2F27;
      int unselectedBg = 0x00000000;

      // Silent
      boolean isSilent = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_SILENT.equalsIgnoreCase(sel);
      sheetBinding.ivRadioSilent.setImageResource(isSilent ? checkedRes : emptyRes);
      sheetBinding.rowSoundSilent.setBackgroundColor(isSilent ? selectedBg : unselectedBg);

      // Azan Default
      boolean isAzanDef = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_DEFAULT.equalsIgnoreCase(sel)
          || "default".equalsIgnoreCase(sel) || "azan".equalsIgnoreCase(sel);
      sheetBinding.ivRadioAzanDefault.setImageResource(isAzanDef ? checkedRes : emptyRes);
      sheetBinding.rowSoundAzanDefault.setBackgroundColor(isAzanDef ? selectedBg : unselectedBg);

      // Azan Common
      boolean isAzanCom = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_COMMON.equalsIgnoreCase(sel);
      sheetBinding.ivRadioAzanCommon.setImageResource(isAzanCom ? checkedRes : emptyRes);
      sheetBinding.rowSoundAzanCommon.setBackgroundColor(isAzanCom ? selectedBg : unselectedBg);

      // Azan Standard
      boolean isAzanStd = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_STANDARD.equalsIgnoreCase(sel)
          || "read_azan".equalsIgnoreCase(sel);
      sheetBinding.ivRadioAzanStandard.setImageResource(isAzanStd ? checkedRes : emptyRes);
      sheetBinding.rowSoundAzanStandard.setBackgroundColor(isAzanStd ? selectedBg : unselectedBg);

      // Beep
      boolean isBeep = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_BEEP.equalsIgnoreCase(sel);
      sheetBinding.ivRadioBeep.setImageResource(isBeep ? checkedRes : emptyRes);
      sheetBinding.rowSoundBeep.setBackgroundColor(isBeep ? selectedBg : unselectedBg);

      // Ring
      boolean isRing = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_RING.equalsIgnoreCase(sel);
      sheetBinding.ivRadioRing.setImageResource(isRing ? checkedRes : emptyRes);
      sheetBinding.rowSoundRing.setBackgroundColor(isRing ? selectedBg : unselectedBg);

      // Notification
      boolean isNotif = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_NOTIFICATION.equalsIgnoreCase(sel);
      sheetBinding.ivRadioNotification.setImageResource(isNotif ? checkedRes : emptyRes);
      sheetBinding.rowSoundNotification.setBackgroundColor(isNotif ? selectedBg : unselectedBg);
    };

    updateSoundRadiosUI.run();

    java.util.function.Consumer<String> playPreviewSound = soundKey -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;

      if (prayerPreviewPlayer != null) {
        try {
          if (prayerPreviewPlayer.isPlaying()) {
            prayerPreviewPlayer.stop();
          }
          prayerPreviewPlayer.release();
        } catch (Exception ignored) {}
        prayerPreviewPlayer = null;
      }

      sheetBinding.ivPlayIconAzanDefault.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAzanCommon.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAzanStandard.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconBeep.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconRing.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconNotification.setImageResource(R.drawable.ic_play_arrow);

      if (soundKey.equals(currentPlayingSound[0])) {
        currentPlayingSound[0] = "";
        return;
      }

      String audioUrl = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.getAzanAudioUrl(this, waqtKey, soundKey);
      if (audioUrl.isEmpty()) return;

      try {
        currentPlayingSound[0] = soundKey;
        android.net.Uri soundUri = android.net.Uri.parse(audioUrl);
        prayerPreviewPlayer = new android.media.MediaPlayer();
        prayerPreviewPlayer.setDataSource(this, soundUri);
        prayerPreviewPlayer.prepare();
        prayerPreviewPlayer.start();

        if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_DEFAULT.equalsIgnoreCase(soundKey)) {
          sheetBinding.ivPlayIconAzanDefault.setImageResource(R.drawable.ic_pause);
        } else if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_COMMON.equalsIgnoreCase(soundKey)) {
          sheetBinding.ivPlayIconAzanCommon.setImageResource(R.drawable.ic_pause);
        } else if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_STANDARD.equalsIgnoreCase(soundKey)) {
          sheetBinding.ivPlayIconAzanStandard.setImageResource(R.drawable.ic_pause);
        } else if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_BEEP.equalsIgnoreCase(soundKey)) {
          sheetBinding.ivPlayIconBeep.setImageResource(R.drawable.ic_pause);
        } else if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_RING.equalsIgnoreCase(soundKey)) {
          sheetBinding.ivPlayIconRing.setImageResource(R.drawable.ic_pause);
        } else if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_NOTIFICATION.equalsIgnoreCase(soundKey)) {
          sheetBinding.ivPlayIconNotification.setImageResource(R.drawable.ic_pause);
        }

        prayerPreviewPlayer.setOnCompletionListener(mp -> {
          currentPlayingSound[0] = "";
          sheetBinding.ivPlayIconAzanDefault.setImageResource(R.drawable.ic_play_arrow);
          sheetBinding.ivPlayIconAzanCommon.setImageResource(R.drawable.ic_play_arrow);
          sheetBinding.ivPlayIconAzanStandard.setImageResource(R.drawable.ic_play_arrow);
          sheetBinding.ivPlayIconBeep.setImageResource(R.drawable.ic_play_arrow);
          sheetBinding.ivPlayIconRing.setImageResource(R.drawable.ic_play_arrow);
          sheetBinding.ivPlayIconNotification.setImageResource(R.drawable.ic_play_arrow);
        });
      } catch (Exception ignored) {}
    };

    // Row click listeners
    sheetBinding.rowSoundSilent.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_SILENT;
      updateSoundRadiosUI.run();
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_SILENT);
    });

    sheetBinding.rowSoundAzanDefault.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_DEFAULT;
      updateSoundRadiosUI.run();
    });
    sheetBinding.btnPlayAzanDefault.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_DEFAULT);
    });

    sheetBinding.rowSoundAzanCommon.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_COMMON;
      updateSoundRadiosUI.run();
    });
    sheetBinding.btnPlayAzanCommon.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_COMMON);
    });

    sheetBinding.rowSoundAzanStandard.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_STANDARD;
      updateSoundRadiosUI.run();
    });
    sheetBinding.btnPlayAzanStandard.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_AZAN_STANDARD);
    });

    sheetBinding.rowSoundBeep.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_BEEP;
      updateSoundRadiosUI.run();
    });
    sheetBinding.btnPlayBeep.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_BEEP);
    });

    sheetBinding.rowSoundRing.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_RING;
      updateSoundRadiosUI.run();
    });
    sheetBinding.btnPlayRing.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_RING);
    });

    sheetBinding.rowSoundNotification.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      selectedSoundHolder[0] = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_NOTIFICATION;
      updateSoundRadiosUI.run();
    });
    sheetBinding.btnPlayNotification.setOnClickListener(v -> {
      if (!sheetBinding.switchWaqtAlarmEnabled.isChecked()) return;
      playPreviewSound.accept(com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_NOTIFICATION);
    });

    // 5. Vibration Switch
    sheetBinding.switchVibration.setChecked(config.isVibrationEnabled);

    // 6. Save Button
    sheetBinding.btnSavePrayerAlarmConfig.setOnClickListener(v -> {
      config.isEnabled = sheetBinding.switchWaqtAlarmEnabled.isChecked();
      config.isVibrationEnabled = sheetBinding.switchVibration.isChecked();
      config.soundType = selectedSoundHolder[0];

      int offset = sheetBinding.seekBarTimeAdjustment.getProgress() - 30;
      config.prePrayerReminderMinutes = -offset;

      if (com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.SOUND_SILENT.equalsIgnoreCase(config.soundType)) {
        config.isSoundEnabled = false;
        config.alertMode = com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.ALERT_MODE_SILENT;
      } else {
        config.isSoundEnabled = true;
        config.isAzanEnabled = config.soundType.startsWith("azan") || "default".equalsIgnoreCase(config.soundType);
      }

      com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager.saveConfig(this, config);
      com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(this);

      if (onSaveCallback != null) {
        onSaveCallback.run();
      }

      dialog.dismiss();
      Toast.makeText(this, isBn ? "সেটিংস সংরক্ষিত হয়েছে" : "Settings saved", Toast.LENGTH_SHORT).show();
    });

    sheetBinding.btnBackWaqtConfig.setOnClickListener(v -> dialog.dismiss());

    dialog.setOnDismissListener(d -> {
      stopPreviewPlayer.run();
    });

    dialog.show();
  }

  public void showRamadanReminderSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetRamadanReminderSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetRamadanReminderSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvRamadanSheetTitle.setText(isBn ? "রমজান রিমাইন্ডার" : "Ramadan Reminder");
    sheetBinding.tvRamadanDesc.setText(isBn ? "রমজান ও নফল রোজার সেহরি সমাপ্তির সতর্কবার্তা এবং ইফতারের বরকতময় দোয়া ও আযান অ্যালার্ট।" : "Alerts for Sehri conclusion and blessed Maghrib adhan & dua alerts for Ramadan and voluntary fasting.");
    sheetBinding.tvTitleSehriSection.setText(isBn ? "সেহরির সময় অ্যালার্ট" : "Sehri Time Alert");
    sheetBinding.tvSummarySehri.setText(isBn ? "ফজরের পূর্বে সেহরি শেষ করার সতর্কবার্তা ও রিমাইন্ডার" : "Alert & reminder before Sehri ends prior to Fajr");
    sheetBinding.tvTitleIftarSection.setText(isBn ? "ইফতারের সময় অ্যালার্ট" : "Iftar Time Alert");
    sheetBinding.tvSummaryIftar.setText(isBn ? "সূর্যাস্ত ও মাগরিবের সময়ে ইফতারের দোয়া, আযান ও অ্যালার্ট" : "Iftar dua, adhan & alert at sunset and Maghrib");
    sheetBinding.tvSectionSunnahFasting.setText(isBn ? "সুন্নাত ও নফল রোজা স্মরণিকা" : "Sunnah & Voluntary Fasting Reminder");
    sheetBinding.tvLabelSunnahMonThu.setText(isBn ? "সোম ও বৃহস্পতিবারের রোজা" : "Monday & Thursday Fasting");
    sheetBinding.tvDescSunnahMonThu.setText(isBn ? "প্রতি রবি ও বুধবার সন্ধ্যায় সেহরির প্রস্তুতি স্মরণিকা" : "Preparation reminder on Sunday & Wednesday evenings");
    sheetBinding.tvLabelSunnahAyyamBeed.setText(isBn ? "আইয়ামে বীজ - ১৩, ১৪ ও ১৫ তারিখের রোজা" : "Ayyam al-Beed - 13, 14 & 15th Fasting");
    sheetBinding.tvDescSunnahAyyamBeed.setText(isBn ? "প্রতি হিজরি মাসের পূর্ণিমা রজনীর ৩টি রোজা" : "3 days of voluntary fasting each lunar month");
    sheetBinding.btnTestSehriNotification.setText(isBn ? "সেহরি নোটিফিকেশন টেস্ট" : "Test Sehri Notification");
    sheetBinding.btnTestIftarNotification.setText(isBn ? "ইফতার নোটিফিকেশন টেস্ট" : "Test Iftar Notification");

    // Update Live Status Indicators
    Runnable updateHubBadges = () -> {
      boolean sehriOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanSehriEnabled(this);
      sheetBinding.ivStatusSehri.setImageResource(sehriOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);

      boolean iftarOn = com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanIftarEnabled(this);
      sheetBinding.ivStatusIftar.setImageResource(iftarOn ? R.drawable.ic_status_active_glow_sphere : R.drawable.ic_status_inactive_glow_sphere);
    };
    updateHubBadges.run();

    // 1. Click on Sehri Time Alert -> Open Sehri settings
    sheetBinding.cardRamadanSehriSection.setOnClickListener(v -> {
      showRamadanSehriAlertSettingsSheet(updateHubBadges);
    });

    // 2. Click on Iftar Time Alert -> Open Iftar settings
    sheetBinding.cardRamadanIftarSection.setOnClickListener(v -> {
      showRamadanIftarAlertSettingsSheet(updateHubBadges);
    });

    // 3. Sunnah Fasting Switches
    sheetBinding.switchSunnahMonThu.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isSunnahMonThuEnabled(this));
    sheetBinding.switchSunnahAyyamBeed.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isSunnahAyyamBeedEnabled(this));

    sheetBinding.switchSunnahMonThu.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setSunnahMonThuEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchSunnahAyyamBeed.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setSunnahAyyamBeedEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    // Test buttons - Rule 8: Zero unnecessary toasts
    sheetBinding.btnTestSehriNotification.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.SehriIftarReminderScheduler.sendTestSehriNotification(this);
    });

    sheetBinding.btnTestIftarNotification.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.SehriIftarReminderScheduler.sendTestIftarNotification(this);
    });

    sheetBinding.btnBackRamadanSheet.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  public void showRamadanSehriAlertSettingsSheet(Runnable onDismissCallback) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetRamadanSehriAlertSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetRamadanSehriAlertSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    int mintBtnTextColor = androidx.core.content.ContextCompat.getColor(this, R.color.btn_save_text);

    // Header & Section text
    sheetBinding.tvSehriSheetTitle.setText(isBn ? "সেহরির সময় অ্যালার্ট" : "Sehri Time Alert");
    sheetBinding.tvSehriSheetSubtitle.setText(isBn ? "সেহরি অ্যালার্ম, ঘড়ির সময় ও সুর নির্বাচন" : "Sehri alarm, clock time & tone selection");
    sheetBinding.tvSehriSheetDesc.setText(isBn ? "ফজরের পূর্বে সেহরি শেষ করার অ্যালার্ম, নির্ধারিত সময় এবং ব্যাকগ্রাউন্ড রিংটোন।" : "Alarm, scheduled time and background ringtone before Sehri ends prior to Fajr.");
    sheetBinding.tvLabelSehriAlertMaster.setText(isBn ? "সেহরি অ্যালার্ট সক্রিয় করুন" : "Enable Sehri Alert");
    sheetBinding.tvDescSehriAlertMaster.setText(isBn ? "নির্ধারিত সময়ে সেহরির অ্যালার্ম ও সুর বাজবে" : "Alarm & ringtone will play at scheduled time");
    sheetBinding.tvLabelSehriAlarmTime.setText(isBn ? "সেহরি অ্যালার্মের সময়" : "Sehri Alarm Time");
    sheetBinding.btnPreset1HourBefore.setText(isBn ? "১ ঘণ্টা পূর্বে" : "1 Hour Before");
    sheetBinding.btnOpenTimeClockPicker.setText(isBn ? "ঘড়ি থেকে সেট করুন" : "Set from Clock");
    sheetBinding.tvTitleSehriRingtone.setText(isBn ? "অ্যালার্ম সুর ও রিংটোন" : "Alarm Tone & Ringtone");
    sheetBinding.tvSoundDefaultLabel.setText(isBn ? "আল্লাহু আল্লাহু - ডিফল্ট" : "Allahu Allahu - Default");
    sheetBinding.tvSoundIslamicRingtoneLabel.setText(isBn ? "ইসলামিক রিংটোন" : "Islamic Ringtone");
    sheetBinding.tvSoundAlhamdulillahLabel.setText(isBn ? "আলহামদুলিল্লাহ - খাবিব" : "Alhamdulillah - Khabib");
    sheetBinding.tvSoundAllahHuBeautifulLabel.setText(isBn ? "আল্লাহু আল্লাহু - সুরময়" : "Allahu Allahu - Melodic");
    sheetBinding.tvSoundAwesomeRamadanLabel.setText(isBn ? "রমজান স্পেশাল নাসিদ" : "Ramadan Special Nasheed");
    sheetBinding.tvSoundFsRamadanLabel.setText(isBn ? "রমজান কারিম টিউন" : "Ramadan Kareem Tune");
    sheetBinding.tvSoundIslamicSpiritualLabel.setText(isBn ? "ইসলামিক স্পিরিচুয়াল সুর" : "Spiritual Islamic Tone");
    sheetBinding.tvSoundMuhammadSawLabel.setText(isBn ? "মুহাম্মাদুর রাসূলুল্লাহ (ﷺ)" : "Muhammadur Rasulullah (ﷺ)");
    sheetBinding.tvSoundSalamEajizanaLabel.setText(isBn ? "সালামে আজিজানা" : "Salam-e-Ajizana");
    sheetBinding.tvTitleSehriRepeatCount.setText(isBn ? "অ্যালার্ম রিপিট সংখ্যা" : "Alarm Repeat Count");
    sheetBinding.tvDescSehriRepeatCount.setText(isBn ? "সেহরি অ্যালার্ম বাজার পর সুর কতবার প্লে হবে" : "How many times the audio will repeat");
    sheetBinding.btnRepeat1.setText(isBn ? "১ বার" : "1 time");
    sheetBinding.btnRepeat2.setText(isBn ? "২ বার" : "2 times");
    sheetBinding.btnRepeat3.setText(isBn ? "৩ বার" : "3 times");
    sheetBinding.btnRepeat4.setText(isBn ? "৪ বার" : "4 times");
    sheetBinding.btnRepeat5.setText(isBn ? "৫ বার" : "5 times");
    sheetBinding.tvLabelSehriVibration.setText(isBn ? "অ্যালার্ম ভাইব্রেশন" : "Alarm Vibration");
    sheetBinding.tvDescSehriVibration.setText(isBn ? "অ্যালার্ম রিংটোন বাজার সময় ভাইব্রেশন হবে" : "Vibration during alarm ringtone");
    sheetBinding.tvTestSehriAlertLabel.setText(isBn ? "সেহরি অ্যালার্ম ও সুর টেস্ট করুন" : "Test Sehri Alarm & Tone");
    sheetBinding.btnCancelSehriSheet.setText(isBn ? "বাতিল" : "Cancel");
    sheetBinding.btnSaveSehriSheet.setText(isBn ? "সেভ করুন" : "Save");

    // 1. Local Draft State (Do not write to preferences until user taps "সেভ করুন")
    final boolean[] draftMasterEnabled = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanSehriEnabled(this)};
    final boolean[] draftIsCustomTime = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanSehriCustomTime(this)};
    final int[] draftAlarmHour = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanSehriAlarmHour(this)};
    final int[] draftAlarmMinute = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanSehriAlarmMinute(this)};
    final String[] draftSoundKey = new String[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanSehriSoundType(this)};
    final int[] draftRepeatCount = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanSehriRepeatCount(this)};
    final boolean[] draftVibration = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanSehriVibrationEnabled(this)};

    final android.media.MediaPlayer[] previewPlayer = new android.media.MediaPlayer[1];
    final String[] currentPlayingSound = new String[]{null};

    // Calculate prayer times for context
    java.util.Calendar cal = java.util.Calendar.getInstance();
    long fajrMillis = 0;
    String fajrTimeStr = isBn ? "০৪:৩০ AM" : "04:30 AM";
    try {
      com.devflux.deenone.core.location.LocationProvider.Coordinates loc =
          com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(this);
      com.devflux.deenone.utils.PrayerCalculator.PrayerTimesResult times =
          com.devflux.deenone.utils.PrayerCalculator.calculateForLocationWithContext(this, loc.latitude, loc.longitude, loc.timezone, cal);
      if (times != null) {
        fajrMillis = times.fajrMillis;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.ENGLISH);
        String rawFajr = sdf.format(new java.util.Date(fajrMillis));
        fajrTimeStr = isBn ? BengaliNumberUtil.toBengali(rawFajr) : rawFajr;
      }
    } catch (Exception ignored) {}

    final String finalFajrTimeStr = fajrTimeStr;
    final long finalFajrMillis = fajrMillis;

    // Master Switch
    sheetBinding.switchSehriAlertMaster.setChecked(draftMasterEnabled[0]);
    sheetBinding.layoutSehriActiveOptions.setVisibility(draftMasterEnabled[0] ? View.VISIBLE : View.GONE);
    sheetBinding.switchSehriAlertMaster.setOnCheckedChangeListener((btn, isChecked) -> {
      draftMasterEnabled[0] = isChecked;
      sheetBinding.layoutSehriActiveOptions.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      if (isChecked && !com.devflux.deenone.core.permissions.PermissionHelper.hasNotificationPermission(this)) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
          requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }
      }
    });

    // Time Clock UI updater
    Runnable updateTimeClockUI = () -> {
      if (draftIsCustomTime[0]) {
        int h = draftAlarmHour[0];
        int m = draftAlarmMinute[0];
        sheetBinding.tvSehriAlarmTimeDisplay.setText(formatTime12H(h, m));
        sheetBinding.badgeSehriTimeMode.setText(isBn ? "কাস্টম সময়" : "Custom Time");
        sheetBinding.tvSehriAlarmContext.setText(isBn
            ? ("আজকের সেহরি শেষ: " + finalFajrTimeStr + " • নির্ধারিত সময়ে অ্যালার্ম বাজবে")
            : ("Today's Sehri ends: " + finalFajrTimeStr + " • Scheduled alarm"));

        sheetBinding.btnPreset1HourBefore.setBackgroundResource(R.drawable.bg_badge_pill);
        sheetBinding.btnPreset1HourBefore.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
        sheetBinding.btnOpenTimeClockPicker.setBackgroundResource(R.drawable.bg_btn_mint_pill);
        sheetBinding.btnOpenTimeClockPicker.setTextColor(mintBtnTextColor);
      } else {
        long alarmMillis = finalFajrMillis > 0 ? (finalFajrMillis - 60 * 60 * 1000L) : 0;
        if (alarmMillis > 0) {
          java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a", java.util.Locale.ENGLISH);
          String rawAlarm = sdf.format(new java.util.Date(alarmMillis));
          sheetBinding.tvSehriAlarmTimeDisplay.setText(isBn ? BengaliNumberUtil.toBengali(rawAlarm) : rawAlarm);
        } else {
          sheetBinding.tvSehriAlarmTimeDisplay.setText(isBn ? "০৩:৩০ AM" : "03:30 AM");
        }
        sheetBinding.badgeSehriTimeMode.setText(isBn ? "১ ঘণ্টা পূর্বে" : "1 hr before");
        sheetBinding.tvSehriAlarmContext.setText(isBn
            ? ("আজকের সেহরি শেষ: " + finalFajrTimeStr + " • ১ ঘণ্টা পূর্বে বাজবে")
            : ("Today's Sehri ends: " + finalFajrTimeStr + " • Rings 1 hr before"));

        sheetBinding.btnPreset1HourBefore.setBackgroundResource(R.drawable.bg_btn_mint_pill);
        sheetBinding.btnPreset1HourBefore.setTextColor(mintBtnTextColor);
        sheetBinding.btnOpenTimeClockPicker.setBackgroundResource(R.drawable.bg_badge_pill);
        sheetBinding.btnOpenTimeClockPicker.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary));
      }
    };
    updateTimeClockUI.run();

    // Time Picker Dialog opener
    Runnable openTimePicker = () -> {
      int initialH = draftAlarmHour[0];
      int initialM = draftAlarmMinute[0];
      if (!draftIsCustomTime[0] && finalFajrMillis > 0) {
        java.util.Calendar fc = java.util.Calendar.getInstance();
        fc.setTimeInMillis(finalFajrMillis - 60 * 60 * 1000L);
        initialH = fc.get(java.util.Calendar.HOUR_OF_DAY);
        initialM = fc.get(java.util.Calendar.MINUTE);
      }

      com.google.android.material.timepicker.MaterialTimePicker timePicker =
          new com.google.android.material.timepicker.MaterialTimePicker.Builder()
              .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
              .setHour(initialH)
              .setMinute(initialM)
              .setTitleText(isBn ? "সেহরি অ্যালার্মের সময় নির্ধারণ করুন" : "Set Sehri Alarm Time")
              .setInputMode(com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK)
              .build();

      timePicker.addOnPositiveButtonClickListener(pv -> {
        draftAlarmHour[0] = timePicker.getHour();
        draftAlarmMinute[0] = timePicker.getMinute();
        draftIsCustomTime[0] = true;
        updateTimeClockUI.run();
      });

      timePicker.show(getSupportFragmentManager(), "ramadan_sehri_time_picker");
    };

    sheetBinding.cardSehriTimePicker.setOnClickListener(v -> openTimePicker.run());
    sheetBinding.btnOpenTimeClockPicker.setOnClickListener(v -> openTimePicker.run());

    sheetBinding.btnPreset1HourBefore.setOnClickListener(v -> {
      draftIsCustomTime[0] = false;
      updateTimeClockUI.run();
    });

    // Audio Preview & Selection
    Runnable stopPreviewAudio = () -> {
      if (previewPlayer[0] != null) {
        try {
          if (previewPlayer[0].isPlaying()) previewPlayer[0].stop();
          previewPlayer[0].reset();
          previewPlayer[0].release();
        } catch (Exception ignored) {}
        previewPlayer[0] = null;
      }
      currentPlayingSound[0] = null;
      sheetBinding.ivPlayIconDefault.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconIslamicRingtone.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAlhamdulillah.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAllahHuBeautiful.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconAwesomeRamadan.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconFsRamadan.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconIslamicSpiritual.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconMuhammadSaw.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconSalamEajizana.setImageResource(R.drawable.ic_play_arrow);
    };

    Runnable updateSoundSelectionUI = () -> {
      String cur = draftSoundKey[0];
      int mintColor = androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint);
      int secColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);

      boolean isDef = "allah_hu_allah_default".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundDefault.setImageResource(isDef ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundDefault.setColorFilter(isDef ? mintColor : secColor);

      boolean isRing = "islamic_ringtone".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundIslamicRingtone.setImageResource(isRing ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundIslamicRingtone.setColorFilter(isRing ? mintColor : secColor);

      boolean isAlham = "alhamdulillah_khabib".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundAlhamdulillah.setImageResource(isAlham ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundAlhamdulillah.setColorFilter(isAlham ? mintColor : secColor);

      boolean isBeauty = "allah_ho_allah_beautiful".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundAllahHuBeautiful.setImageResource(isBeauty ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundAllahHuBeautiful.setColorFilter(isBeauty ? mintColor : secColor);

      boolean isAwesome = "awesome_ramadan".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundAwesomeRamadan.setImageResource(isAwesome ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundAwesomeRamadan.setColorFilter(isAwesome ? mintColor : secColor);

      boolean isFs = "fs_ramadan".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundFsRamadan.setImageResource(isFs ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundFsRamadan.setColorFilter(isFs ? mintColor : secColor);

      boolean isSpirit = "islamic_spiritual".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundIslamicSpiritual.setImageResource(isSpirit ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundIslamicSpiritual.setColorFilter(isSpirit ? mintColor : secColor);

      boolean isMuham = "muhammad_saw".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundMuhammadSaw.setImageResource(isMuham ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundMuhammadSaw.setColorFilter(isMuham ? mintColor : secColor);

      boolean isSalam = "salam_e_ajizana".equalsIgnoreCase(cur);
      sheetBinding.ivSelectSoundSalamEajizana.setImageResource(isSalam ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectSoundSalamEajizana.setColorFilter(isSalam ? mintColor : secColor);
    };
    updateSoundSelectionUI.run();

    // Row selection handlers
    sheetBinding.rowSoundDefault.setOnClickListener(v -> { draftSoundKey[0] = "allah_hu_allah_default"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundIslamicRingtone.setOnClickListener(v -> { draftSoundKey[0] = "islamic_ringtone"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundAlhamdulillah.setOnClickListener(v -> { draftSoundKey[0] = "alhamdulillah_khabib"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundAllahHuBeautiful.setOnClickListener(v -> { draftSoundKey[0] = "allah_ho_allah_beautiful"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundAwesomeRamadan.setOnClickListener(v -> { draftSoundKey[0] = "awesome_ramadan"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundFsRamadan.setOnClickListener(v -> { draftSoundKey[0] = "fs_ramadan"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundIslamicSpiritual.setOnClickListener(v -> { draftSoundKey[0] = "islamic_spiritual"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundMuhammadSaw.setOnClickListener(v -> { draftSoundKey[0] = "muhammad_saw"; updateSoundSelectionUI.run(); });
    sheetBinding.rowSoundSalamEajizana.setOnClickListener(v -> { draftSoundKey[0] = "salam_e_ajizana"; updateSoundSelectionUI.run(); });

    // Audio Playback previews helper
    java.util.function.BiConsumer<String, Integer> playPreviewTrack = (key, rawRes) -> {
      if (key.equalsIgnoreCase(currentPlayingSound[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, rawRes);
        if (previewPlayer[0] != null) {
          currentPlayingSound[0] = key;
          android.widget.ImageView targetIv = null;
          if ("allah_hu_allah_default".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconDefault;
          else if ("islamic_ringtone".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconIslamicRingtone;
          else if ("alhamdulillah_khabib".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconAlhamdulillah;
          else if ("allah_ho_allah_beautiful".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconAllahHuBeautiful;
          else if ("awesome_ramadan".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconAwesomeRamadan;
          else if ("fs_ramadan".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconFsRamadan;
          else if ("islamic_spiritual".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconIslamicSpiritual;
          else if ("muhammad_saw".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconMuhammadSaw;
          else if ("salam_e_ajizana".equalsIgnoreCase(key)) targetIv = sheetBinding.ivPlayIconSalamEajizana;

          if (targetIv != null) targetIv.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    };

    sheetBinding.btnPlaySoundDefault.setOnClickListener(v -> playPreviewTrack.accept("allah_hu_allah_default", R.raw.read_allah_hu_allah_default));
    sheetBinding.btnPlaySoundIslamicRingtone.setOnClickListener(v -> playPreviewTrack.accept("islamic_ringtone", R.raw.read_islamic_rintone));
    sheetBinding.btnPlaySoundAlhamdulillah.setOnClickListener(v -> playPreviewTrack.accept("alhamdulillah_khabib", R.raw.read_alhamdulillah_khabib));
    sheetBinding.btnPlaySoundAllahHuBeautiful.setOnClickListener(v -> playPreviewTrack.accept("allah_ho_allah_beautiful", R.raw.read_allah_ho_allah_beautiful));
    sheetBinding.btnPlaySoundAwesomeRamadan.setOnClickListener(v -> playPreviewTrack.accept("awesome_ramadan", R.raw.read_awesome_ramadan));
    sheetBinding.btnPlaySoundFsRamadan.setOnClickListener(v -> playPreviewTrack.accept("fs_ramadan", R.raw.read_fs_ramadan));
    sheetBinding.btnPlaySoundIslamicSpiritual.setOnClickListener(v -> playPreviewTrack.accept("islamic_spiritual", R.raw.read_islamic_spiritual));
    sheetBinding.btnPlaySoundMuhammadSaw.setOnClickListener(v -> playPreviewTrack.accept("muhammad_saw", R.raw.read_muhammad_saw));
    sheetBinding.btnPlaySoundSalamEajizana.setOnClickListener(v -> playPreviewTrack.accept("salam_e_ajizana", R.raw.read_salam_e_ajizana));

    // Repeat Count Selector (1 to 5) - Zero bracket pollution
    Runnable updateRepeatCountUI = () -> {
      int count = draftRepeatCount[0];
      String countBadgeText;
      if (isBn) {
        switch (count) {
          case 1: countBadgeText = "১ বার"; break;
          case 2: countBadgeText = "২ বার"; break;
          case 4: countBadgeText = "৪ বার"; break;
          case 5: countBadgeText = "৫ বার"; break;
          case 3:
          default:
            countBadgeText = "৩ বার - ডিফল্ট"; break;
        }
      } else {
        switch (count) {
          case 1: countBadgeText = "1 time"; break;
          case 2: countBadgeText = "2 times"; break;
          case 4: countBadgeText = "4 times"; break;
          case 5: countBadgeText = "5 times"; break;
          case 3:
          default:
            countBadgeText = "3 times - Default"; break;
        }
      }
      sheetBinding.badgeSehriRepeatCount.setText(countBadgeText);

      int secTextColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);

      sheetBinding.btnRepeat1.setBackgroundResource(count == 1 ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
      sheetBinding.btnRepeat1.setTextColor(count == 1 ? mintBtnTextColor : secTextColor);

      sheetBinding.btnRepeat2.setBackgroundResource(count == 2 ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
      sheetBinding.btnRepeat2.setTextColor(count == 2 ? mintBtnTextColor : secTextColor);

      sheetBinding.btnRepeat3.setBackgroundResource(count == 3 ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
      sheetBinding.btnRepeat3.setTextColor(count == 3 ? mintBtnTextColor : secTextColor);

      sheetBinding.btnRepeat4.setBackgroundResource(count == 4 ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
      sheetBinding.btnRepeat4.setTextColor(count == 4 ? mintBtnTextColor : secTextColor);

      sheetBinding.btnRepeat5.setBackgroundResource(count == 5 ? R.drawable.bg_btn_mint_pill : R.drawable.bg_badge_pill);
      sheetBinding.btnRepeat5.setTextColor(count == 5 ? mintBtnTextColor : secTextColor);
    };
    updateRepeatCountUI.run();

    sheetBinding.btnRepeat1.setOnClickListener(v -> { draftRepeatCount[0] = 1; updateRepeatCountUI.run(); });
    sheetBinding.btnRepeat2.setOnClickListener(v -> { draftRepeatCount[0] = 2; updateRepeatCountUI.run(); });
    sheetBinding.btnRepeat3.setOnClickListener(v -> { draftRepeatCount[0] = 3; updateRepeatCountUI.run(); });
    sheetBinding.btnRepeat4.setOnClickListener(v -> { draftRepeatCount[0] = 4; updateRepeatCountUI.run(); });
    sheetBinding.btnRepeat5.setOnClickListener(v -> { draftRepeatCount[0] = 5; updateRepeatCountUI.run(); });

    // Vibration
    sheetBinding.switchSehriVibration.setChecked(draftVibration[0]);
    sheetBinding.switchSehriVibration.setOnCheckedChangeListener((btn, isChecked) -> {
      draftVibration[0] = isChecked;
    });

    // Test Button - Rule 8: Zero unnecessary toasts
    sheetBinding.btnTestSehriAlert.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.SehriIftarReminderScheduler.sendTestSehriNotification(this);
    });

    // Cancel Button (Dismiss without saving)
    sheetBinding.btnCancelSehriSheet.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    // Save Button (Explicitly persist all draft state) - Rule 8: Zero unnecessary toasts
    sheetBinding.btnSaveSehriSheet.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriEnabled(this, draftMasterEnabled[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriCustomTime(this, draftIsCustomTime[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriAlarmHour(this, draftAlarmHour[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriAlarmMinute(this, draftAlarmMinute[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriSoundType(this, draftSoundKey[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriRepeatCount(this, draftRepeatCount[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanSehriVibrationEnabled(this, draftVibration[0]);

      com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(this);
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    dialog.setOnDismissListener(d -> {
      stopPreviewAudio.run();
      if (onDismissCallback != null) onDismissCallback.run();
    });

    sheetBinding.btnBackSehriSheet.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });
    dialog.show();
  }

  public void showRamadanIftarAlertSettingsSheet(Runnable onDismissCallback) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetRamadanIftarAlertSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetRamadanIftarAlertSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Apply strict dual-language localization
    sheetBinding.tvIftarSheetTitle.setText(isBn ? "ইফতারের সময় অ্যালার্ট" : "Iftar Alert Time");
    sheetBinding.tvIftarSheetDesc.setText(isBn
        ? "সূর্যাস্ত ও মাগরিবের সময়ে ইফতারের দোয়া, আযান ও বরকতময় অ্যালার্ম।"
        : "Iftar Dua, Adhan and blessed alarms at Sunset & Maghrib time.");
    sheetBinding.tvLabelIftarAlertMaster.setText(isBn ? "ইফতার অ্যালার্ট সক্রিয় করুন" : "Enable Iftar Alert");
    sheetBinding.tvDescIftarAlertMaster.setText(isBn
        ? "মাগরিব ও ইফতারের সময়ে আযান ও দোয়ার অ্যালার্ট বাজবে"
        : "Adhan and Dua alarm will play at Maghrib and Iftar time");
    sheetBinding.tvTitleIftarOffsetSection.setText(isBn ? "ইফতারের কতক্ষণ পূর্বে প্রস্তুতি রিমাইন্ডার চান?" : "Preparation reminder before Iftar");
    sheetBinding.tvTitleIftarAudioSection.setText(isBn ? "মাগরিবের সময় আযান ও দোয়া নির্বাচন" : "Select Adhan & Dua for Maghrib");
    sheetBinding.tvIftarAzanDuaLabel.setText(isBn ? "মাগরিবের পূর্ণ আযান ও দোয়া" : "Full Maghrib Adhan & Dua");
    sheetBinding.tvIftarDuaOnlyLabel.setText(isBn ? "শুধুমাত্র ইফতারের দোয়া" : "Iftar Dua Only");
    sheetBinding.tvIslamicToneLabel.setText(isBn ? "ইসলামিক সুর" : "Islamic Spiritual Tone");
    sheetBinding.tvGentleBeepLabel.setText(isBn ? "মৃদু নোটিফিকেশন টোন" : "Gentle Beep Tone");
    sheetBinding.tvLabelIftarVibration.setText(isBn ? "অ্যালার্ম ভাইব্রেশন" : "Alarm Vibration");
    sheetBinding.tvTestIftarAlertLabel.setText(isBn ? "ইফতার অ্যালার্ট টেস্ট করুন" : "Test Iftar Alert");
    sheetBinding.btnCancelIftarSheet.setText(isBn ? "বাতিল" : "Cancel");
    sheetBinding.btnSaveIftarSheet.setText(isBn ? "সেভ করুন" : "Save Changes");

    // 1. Local Draft State (Do not write to preferences until user taps "সেভ করুন")
    final boolean[] draftMasterEnabled = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanIftarEnabled(this)};
    final int[] draftOffsetMinutes = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanIftarOffsetMinutes(this)};
    final String[] draftSoundType = new String[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getRamadanIftarSoundType(this)};
    final boolean[] draftVibration = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isRamadanIftarVibrationEnabled(this)};

    final android.media.MediaPlayer[] previewPlayer = new android.media.MediaPlayer[1];
    final String[] currentPlayingSound = new String[]{null};

    // Master Switch
    sheetBinding.switchIftarAlertMaster.setChecked(draftMasterEnabled[0]);
    sheetBinding.layoutIftarActiveOptions.setVisibility(draftMasterEnabled[0] ? View.VISIBLE : View.GONE);
    sheetBinding.switchIftarAlertMaster.setOnCheckedChangeListener((btn, isChecked) -> {
      draftMasterEnabled[0] = isChecked;
      sheetBinding.layoutIftarActiveOptions.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      if (isChecked && !com.devflux.deenone.core.permissions.PermissionHelper.hasNotificationPermission(this)) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
          requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }
      }
    });

    // Pre-Iftar Offset Pills UI updater
    Runnable updatePillsUI = () -> {
      int m = draftOffsetMinutes[0];
      int selectedBg = R.drawable.bg_btn_mint_pill;
      int unselectedBg = R.drawable.bg_badge_pill;
      int selectedText = androidx.core.content.ContextCompat.getColor(this, R.color.btn_save_text);
      int unselectedText = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);

      sheetBinding.btnIftarOffset0.setText(isBn ? "ঠিক সময়ে" : "On Time");
      sheetBinding.btnIftarOffset0.setBackgroundResource(m == 0 ? selectedBg : unselectedBg);
      sheetBinding.btnIftarOffset0.setTextColor(m == 0 ? selectedText : unselectedText);

      sheetBinding.btnIftarOffset5.setText(isBn ? "৫ মি. পূর্বে" : "5 min before");
      sheetBinding.btnIftarOffset5.setBackgroundResource(m == 5 ? selectedBg : unselectedBg);
      sheetBinding.btnIftarOffset5.setTextColor(m == 5 ? selectedText : unselectedText);

      sheetBinding.btnIftarOffset10.setText(isBn ? "১০ মি. পূর্বে" : "10 min before");
      sheetBinding.btnIftarOffset10.setBackgroundResource(m == 10 ? selectedBg : unselectedBg);
      sheetBinding.btnIftarOffset10.setTextColor(m == 10 ? selectedText : unselectedText);

      sheetBinding.btnIftarOffset15.setText(isBn ? "১৫ মি. পূর্বে" : "15 min before");
      sheetBinding.btnIftarOffset15.setBackgroundResource(m == 15 ? selectedBg : unselectedBg);
      sheetBinding.btnIftarOffset15.setTextColor(m == 15 ? selectedText : unselectedText);
    };
    updatePillsUI.run();

    sheetBinding.btnIftarOffset0.setOnClickListener(v -> { draftOffsetMinutes[0] = 0; updatePillsUI.run(); });
    sheetBinding.btnIftarOffset5.setOnClickListener(v -> { draftOffsetMinutes[0] = 5; updatePillsUI.run(); });
    sheetBinding.btnIftarOffset10.setOnClickListener(v -> { draftOffsetMinutes[0] = 10; updatePillsUI.run(); });
    sheetBinding.btnIftarOffset15.setOnClickListener(v -> { draftOffsetMinutes[0] = 15; updatePillsUI.run(); });

    // Audio Selection & Preview
    Runnable stopPreviewAudio = () -> {
      if (previewPlayer[0] != null) {
        try {
          if (previewPlayer[0].isPlaying()) previewPlayer[0].stop();
          previewPlayer[0].reset();
          previewPlayer[0].release();
        } catch (Exception ignored) {}
        previewPlayer[0] = null;
      }
      currentPlayingSound[0] = null;
      sheetBinding.ivPlayIconIftarAzanDua.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconIftarDuaOnly.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconIftarIslamic.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconIftarBeep.setImageResource(R.drawable.ic_play_arrow);
    };

    Runnable updateSoundUI = () -> {
      boolean isAzanDua = "azan_dua".equalsIgnoreCase(draftSoundType[0]);
      boolean isDuaOnly = "dua_only".equalsIgnoreCase(draftSoundType[0]);
      boolean isIslamic = "islamic".equalsIgnoreCase(draftSoundType[0]);
      boolean isBeep = "beep".equalsIgnoreCase(draftSoundType[0]);

      sheetBinding.ivSelectIftarAzanDua.setImageResource(isAzanDua ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectIftarAzanDua.setColorFilter(isAzanDua ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectIftarDuaOnly.setImageResource(isDuaOnly ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectIftarDuaOnly.setColorFilter(isDuaOnly ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectIftarIslamic.setImageResource(isIslamic ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectIftarIslamic.setColorFilter(isIslamic ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectIftarBeep.setImageResource(isBeep ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectIftarBeep.setColorFilter(isBeep ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
    };
    updateSoundUI.run();

    sheetBinding.rowAudioIftarAzanDua.setOnClickListener(v -> { draftSoundType[0] = "azan_dua"; updateSoundUI.run(); });
    sheetBinding.rowAudioIftarDuaOnly.setOnClickListener(v -> { draftSoundType[0] = "dua_only"; updateSoundUI.run(); });
    sheetBinding.rowAudioIftarIslamic.setOnClickListener(v -> { draftSoundType[0] = "islamic"; updateSoundUI.run(); });
    sheetBinding.rowAudioIftarBeep.setOnClickListener(v -> { draftSoundType[0] = "beep"; updateSoundUI.run(); });

    // Audio Play / Preview click handlers
    sheetBinding.btnPlayIftarAzanDua.setOnClickListener(v -> {
      if ("azan_dua".equalsIgnoreCase(currentPlayingSound[0])) { stopPreviewAudio.run(); return; }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_azan_default);
        if (previewPlayer[0] != null) {
          currentPlayingSound[0] = "azan_dua";
          sheetBinding.ivPlayIconIftarAzanDua.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) { stopPreviewAudio.run(); }
    });

    sheetBinding.btnPlayIftarDuaOnly.setOnClickListener(v -> {
      if ("dua_only".equalsIgnoreCase(currentPlayingSound[0])) { stopPreviewAudio.run(); return; }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_awesome_ramadan);
        if (previewPlayer[0] != null) {
          currentPlayingSound[0] = "dua_only";
          sheetBinding.ivPlayIconIftarDuaOnly.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) { stopPreviewAudio.run(); }
    });

    sheetBinding.btnPlayIftarIslamic.setOnClickListener(v -> {
      if ("islamic".equalsIgnoreCase(currentPlayingSound[0])) { stopPreviewAudio.run(); return; }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_fs_ramadan);
        if (previewPlayer[0] != null) {
          currentPlayingSound[0] = "islamic";
          sheetBinding.ivPlayIconIftarIslamic.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) { stopPreviewAudio.run(); }
    });

    sheetBinding.btnPlayIftarBeep.setOnClickListener(v -> {
      if ("beep".equalsIgnoreCase(currentPlayingSound[0])) { stopPreviewAudio.run(); return; }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.beep);
        if (previewPlayer[0] != null) {
          currentPlayingSound[0] = "beep";
          sheetBinding.ivPlayIconIftarBeep.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) { stopPreviewAudio.run(); }
    });

    // Vibration
    sheetBinding.switchIftarVibration.setChecked(draftVibration[0]);
    sheetBinding.switchIftarVibration.setOnCheckedChangeListener((btn, isChecked) -> {
      draftVibration[0] = isChecked;
    });

    // Test Button - Rule 8: Zero unnecessary toasts
    sheetBinding.btnTestIftarAlert.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.SehriIftarReminderScheduler.sendTestIftarNotification(this);
    });

    // Cancel Button (Dismiss without saving)
    sheetBinding.btnCancelIftarSheet.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    // Save Button (Explicitly persist all draft state) - Rule 8: Zero unnecessary toasts
    sheetBinding.btnSaveIftarSheet.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanIftarEnabled(this, draftMasterEnabled[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanIftarOffsetMinutes(this, draftOffsetMinutes[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanIftarSoundType(this, draftSoundType[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setRamadanIftarVibrationEnabled(this, draftVibration[0]);

      com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(this);
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    dialog.setOnDismissListener(d -> {
      stopPreviewAudio.run();
      if (onDismissCallback != null) onDismissCallback.run();
    });

    sheetBinding.btnBackIftarSheet.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });
    dialog.show();
  }

  public void showDurudReminderSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetDurudReminderSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetDurudReminderSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Apply strict dual-language localization
    sheetBinding.tvDurudSheetTitle.setText(isBn ? "দরূদ স্মরণিকা" : "Durood Reminder");
    sheetBinding.tvDurudSheetDesc.setText(isBn
        ? "দিনের বিভিন্ন সময়ে বিশ্বনবী (ﷺ)-এর ওপর দরূদ পাঠের বরকতময় স্মরণিকা।"
        : "Blessed reminders to send peace and blessings upon the Prophet (ﷺ) throughout the day.");
    sheetBinding.tvLabelDurudMaster.setText(isBn ? "দরূদ স্মরণিকা" : "Durood Reminder");
    sheetBinding.tvDurudIntervalLabel.setText(isBn ? "স্মরণিকার বিরতি" : "Reminder Interval");
    sheetBinding.tvTitleDurudAudio.setText(isBn ? "রিমাইন্ডার অডিও" : "Reminder Audio");
    sheetBinding.tvAudioBanglaLabel.setText(isBn ? "বাংলা" : "Bengali");
    sheetBinding.tvAudioEnglishLabel.setText(isBn ? "ইংরেজি" : "English");
    sheetBinding.tvAudioArabic1Label.setText(isBn ? "আরবি ১" : "Arabic 1");
    sheetBinding.tvAudioArabic2Label.setText(isBn ? "আরবি ২" : "Arabic 2");
    sheetBinding.tvAudioArabic3Label.setText(isBn ? "আরবি ৩" : "Arabic 3");
    sheetBinding.tvTitleDurudSilentHours.setText(isBn ? "সাইলেন্ট আওয়ার" : "Silent Hours");
    sheetBinding.tvDescDurudSilentHours.setText(isBn
        ? "ঘুম বা কাজের সময় স্মরণিকা নিঃশব্দ রাখতে সাইলেন্ট আওয়ার সেট করুন।"
        : "Set silent hours to mute reminders during sleep or busy hours.");
    sheetBinding.tvAddSilentHoursLabel.setText(isBn ? "+ সাইলেন্ট আওয়ার যোগ করুন" : "+ Add Silent Hours");
    sheetBinding.btnCancelDurud.setText(isBn ? "বাতিল" : "Cancel");
    sheetBinding.btnSaveDurud.setText(isBn ? "সেভ করুন" : "Save Changes");

    // 1. Initial State from preferences
    boolean isMasterEnabled = com.devflux.deenone.core.notifications.NotificationSettingsManager.isDurudReminderEnabled(this);
    final int[] selectedIntervalMinutes = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudIntervalMinutes(this)};
    final String[] selectedAudioKey = new String[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudAudioKey(this)};
    final boolean[] silentHoursEnabled = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isDurudSilentHoursEnabled(this)};
    final int[] silentStartHour = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudSilentStartHour(this)};
    final int[] silentStartMinute = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudSilentStartMinute(this)};
    final int[] silentEndHour = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudSilentEndHour(this)};
    final int[] silentEndMinute = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getDurudSilentEndMinute(this)};

    final android.media.MediaPlayer[] previewPlayer = new android.media.MediaPlayer[1];
    final String[] currentPlayingKey = new String[]{null};

    // Master Switch state
    sheetBinding.switchDurudReminder.setChecked(isMasterEnabled);
    sheetBinding.layoutDurudActiveOptions.setVisibility(isMasterEnabled ? View.VISIBLE : View.GONE);
    sheetBinding.switchDurudReminder.setOnCheckedChangeListener((btn, isChecked) -> {
      sheetBinding.layoutDurudActiveOptions.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      if (isChecked && !com.devflux.deenone.core.permissions.PermissionHelper.hasNotificationPermission(this)) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
          requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }
      }
    });

    // 2. Interval text updater
    Runnable updateIntervalDisplay = () -> {
      int m = selectedIntervalMinutes[0];
      String text;
      if (m == 15) text = isBn ? "১৫ মিনিট" : "15 Minutes";
      else if (m == 30) text = isBn ? "৩০ মিনিট" : "30 Minutes";
      else if (m == 45) text = isBn ? "৪৫ মিনিট" : "45 Minutes";
      else if (m == 60) text = isBn ? "১ ঘণ্টা" : "1 Hour";
      else if (m == 120) text = isBn ? "২ ঘণ্টা" : "2 Hours";
      else if (m == 180) text = isBn ? "৩ ঘণ্টা" : "3 Hours";
      else text = isBn ? (BengaliNumberUtil.toBengali(String.valueOf(m)) + " মিনিট") : (m + " Minutes");
      sheetBinding.tvDurudIntervalDisplay.setText(text);
    };
    updateIntervalDisplay.run();

    // Interval Dialog Picker
    sheetBinding.btnOpenIntervalPicker.setOnClickListener(v -> {
      com.google.android.material.dialog.MaterialAlertDialogBuilder builder =
          new com.google.android.material.dialog.MaterialAlertDialogBuilder(this);
      com.devflux.deenone.databinding.DialogIstigfarIntervalPickerBinding dialogBinding =
          com.devflux.deenone.databinding.DialogIstigfarIntervalPickerBinding.inflate(LayoutInflater.from(this));

      dialogBinding.tvIntervalPickerTitle.setText(isBn ? "স্মরণিকার বিরতি নির্বাচন করুন" : "Select Reminder Interval");
      dialogBinding.tvInterval15.setText(isBn ? "১৫ মিনিট" : "15 Minutes");
      dialogBinding.tvInterval30.setText(isBn ? "৩০ মিনিট" : "30 Minutes");
      dialogBinding.tvInterval45.setText(isBn ? "৪৫ মিনিট" : "45 Minutes");
      dialogBinding.tvInterval60.setText(isBn ? "১ ঘণ্টা" : "1 Hour");
      dialogBinding.tvInterval120.setText(isBn ? "২ ঘণ্টা" : "2 Hours");
      dialogBinding.tvInterval180.setText(isBn ? "৩ ঘণ্টা" : "3 Hours");
      dialogBinding.btnCancelIntervalDialog.setText(isBn ? "বাতিল" : "Cancel");

      androidx.appcompat.app.AlertDialog alertDialog = builder.setView(dialogBinding.getRoot()).create();
      if (alertDialog.getWindow() != null) {
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
      }
      dialogBinding.itemInterval15.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 15; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval30.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 30; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval45.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 45; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval60.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 60; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval120.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 120; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval180.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 180; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.btnCancelIntervalDialog.setOnClickListener(dv -> alertDialog.dismiss());
      alertDialog.show();
    });

    // 3. Audio Selection & Preview
    Runnable stopPreviewAudio = () -> {
      if (previewPlayer[0] != null) {
        try {
          if (previewPlayer[0].isPlaying()) {
            previewPlayer[0].stop();
          }
          previewPlayer[0].reset();
          previewPlayer[0].release();
        } catch (Exception ignored) {}
        previewPlayer[0] = null;
      }
      currentPlayingKey[0] = null;
      sheetBinding.ivPlayIconBangla.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconEnglish.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconArabic1.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconArabic2.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconArabic3.setImageResource(R.drawable.ic_play_arrow);
    };

    Runnable updateAudioSelectionUI = () -> {
      boolean isBnKey = "bn".equalsIgnoreCase(selectedAudioKey[0]);
      boolean isEnKey = "en".equalsIgnoreCase(selectedAudioKey[0]);
      boolean isAr1 = "ar1".equalsIgnoreCase(selectedAudioKey[0]);
      boolean isAr2 = "ar2".equalsIgnoreCase(selectedAudioKey[0]);
      boolean isAr3 = "ar3".equalsIgnoreCase(selectedAudioKey[0]);

      sheetBinding.ivSelectBangla.setImageResource(isBnKey ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectBangla.setColorFilter(isBnKey ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectEnglish.setImageResource(isEnKey ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectEnglish.setColorFilter(isEnKey ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectArabic1.setImageResource(isAr1 ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectArabic1.setColorFilter(isAr1 ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectArabic2.setImageResource(isAr2 ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectArabic2.setColorFilter(isAr2 ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectArabic3.setImageResource(isAr3 ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectArabic3.setColorFilter(isAr3 ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
    };
    updateAudioSelectionUI.run();

    sheetBinding.rowAudioBangla.setOnClickListener(v -> {
      selectedAudioKey[0] = "bn";
      updateAudioSelectionUI.run();
    });

    sheetBinding.rowAudioEnglish.setOnClickListener(v -> {
      selectedAudioKey[0] = "en";
      updateAudioSelectionUI.run();
    });

    sheetBinding.rowAudioArabic1.setOnClickListener(v -> {
      selectedAudioKey[0] = "ar1";
      updateAudioSelectionUI.run();
    });

    sheetBinding.rowAudioArabic2.setOnClickListener(v -> {
      selectedAudioKey[0] = "ar2";
      updateAudioSelectionUI.run();
    });

    sheetBinding.rowAudioArabic3.setOnClickListener(v -> {
      selectedAudioKey[0] = "ar3";
      updateAudioSelectionUI.run();
    });

    // Audio Playback
    sheetBinding.btnPlayBangla.setOnClickListener(v -> {
      if ("bn".equalsIgnoreCase(currentPlayingKey[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_durud_bn);
        if (previewPlayer[0] != null) {
          currentPlayingKey[0] = "bn";
          sheetBinding.ivPlayIconBangla.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    sheetBinding.btnPlayEnglish.setOnClickListener(v -> {
      if ("en".equalsIgnoreCase(currentPlayingKey[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_durud_en);
        if (previewPlayer[0] != null) {
          currentPlayingKey[0] = "en";
          sheetBinding.ivPlayIconEnglish.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    sheetBinding.btnPlayArabic1.setOnClickListener(v -> {
      if ("ar1".equalsIgnoreCase(currentPlayingKey[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_durud_ar1);
        if (previewPlayer[0] != null) {
          currentPlayingKey[0] = "ar1";
          sheetBinding.ivPlayIconArabic1.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    sheetBinding.btnPlayArabic2.setOnClickListener(v -> {
      if ("ar2".equalsIgnoreCase(currentPlayingKey[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_durud_ar2);
        if (previewPlayer[0] != null) {
          currentPlayingKey[0] = "ar2";
          sheetBinding.ivPlayIconArabic2.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    sheetBinding.btnPlayArabic3.setOnClickListener(v -> {
      if ("ar3".equalsIgnoreCase(currentPlayingKey[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_durud_ar3);
        if (previewPlayer[0] != null) {
          currentPlayingKey[0] = "ar3";
          sheetBinding.ivPlayIconArabic3.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    // 4. Silent Hours (Do Not Disturb)
    Runnable updateSilentHoursUI = () -> {
      if (silentHoursEnabled[0]) {
        sheetBinding.btnShowAddSilentHours.setVisibility(View.GONE);
        sheetBinding.layoutActiveSilentHours.setVisibility(View.VISIBLE);

        String startStr = formatTime12H(silentStartHour[0], silentStartMinute[0]);
        String endStr = formatTime12H(silentEndHour[0], silentEndMinute[0]);
        sheetBinding.tvSilentHoursRange.setText(startStr + " - " + endStr);
      } else {
        sheetBinding.btnShowAddSilentHours.setVisibility(View.VISIBLE);
        sheetBinding.layoutActiveSilentHours.setVisibility(View.GONE);
      }
    };
    updateSilentHoursUI.run();

    Runnable openSilentHoursTimePickers = () -> {
      com.google.android.material.timepicker.MaterialTimePicker startPicker =
          new com.google.android.material.timepicker.MaterialTimePicker.Builder()
              .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
              .setHour(silentStartHour[0])
              .setMinute(silentStartMinute[0])
              .setTitleText(isBn ? "শুরুর সময় নির্বাচন করুন" : "Select Start Time")
              .setInputMode(com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK)
              .build();

      startPicker.addOnPositiveButtonClickListener(pv -> {
        int sHour = startPicker.getHour();
        int sMinute = startPicker.getMinute();

        com.google.android.material.timepicker.MaterialTimePicker endPicker =
            new com.google.android.material.timepicker.MaterialTimePicker.Builder()
                .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
                .setHour(silentEndHour[0])
                .setMinute(silentEndMinute[0])
                .setTitleText(isBn ? "শেষের সময় নির্বাচন করুন" : "Select End Time")
                .setInputMode(com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        endPicker.addOnPositiveButtonClickListener(epv -> {
          silentStartHour[0] = sHour;
          silentStartMinute[0] = sMinute;
          silentEndHour[0] = endPicker.getHour();
          silentEndMinute[0] = endPicker.getMinute();
          silentHoursEnabled[0] = true;
          updateSilentHoursUI.run();
        });

        endPicker.show(getSupportFragmentManager(), "durud_silent_end_picker");
      });

      startPicker.show(getSupportFragmentManager(), "durud_silent_start_picker");
    };

    sheetBinding.btnShowAddSilentHours.setOnClickListener(v -> openSilentHoursTimePickers.run());
    sheetBinding.tvSilentHoursRange.setOnClickListener(v -> openSilentHoursTimePickers.run());

    sheetBinding.btnDeleteSilentHours.setOnClickListener(v -> {
      silentHoursEnabled[0] = false;
      updateSilentHoursUI.run();
    });

    // 5. Bottom Actions - Rule 8: Zero unnecessary toasts
    sheetBinding.btnSaveDurud.setOnClickListener(v -> {
      stopPreviewAudio.run();
      boolean masterOn = sheetBinding.switchDurudReminder.isChecked();
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudReminderEnabled(this, masterOn);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudIntervalMinutes(this, selectedIntervalMinutes[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudAudioKey(this, selectedAudioKey[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudSilentHoursEnabled(this, silentHoursEnabled[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudSilentStartHour(this, silentStartHour[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudSilentStartMinute(this, silentStartMinute[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudSilentEndHour(this, silentEndHour[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDurudSilentEndMinute(this, silentEndMinute[0]);

      if (masterOn) {
        if (!com.devflux.deenone.core.permissions.PermissionHelper.hasNotificationPermission(this)) {
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
          }
        }
        com.devflux.deenone.core.notifications.DurudReminderScheduler.scheduleAll(this);
      } else {
        com.devflux.deenone.core.notifications.DurudReminderScheduler.cancelAll(this);
      }
      updateSoundAndNotificationHubSummaries();
      dialog.dismiss();
    });

    sheetBinding.btnCancelDurud.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    sheetBinding.btnBackDurudSheet.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    dialog.setOnDismissListener(d -> stopPreviewAudio.run());
    dialog.show();
  }

  public void showIstigfarReminderSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetIstigfarReminderSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetIstigfarReminderSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Apply strict dual-language localization
    sheetBinding.tvIstigfarSheetTitle.setText(isBn ? "ইস্তিগফার স্মরণিকা" : "Istighfar Reminder");
    sheetBinding.tvIstigfarSheetDesc.setText(isBn
        ? "দিনের বিভিন্ন সময়ে মহান আল্লাহর কাছে ক্ষমা প্রার্থনা ও আত্মশুদ্ধির মৃদু স্মরণিকা।"
        : "Gentle reminders to seek forgiveness and purification from Allah throughout the day.");
    sheetBinding.tvLabelIstigfarMaster.setText(isBn ? "ইস্তিগফার স্মরণিকা" : "Istighfar Reminder");
    sheetBinding.tvIstigfarIntervalLabel.setText(isBn ? "স্মরণিকার বিরতি" : "Reminder Interval");
    sheetBinding.tvTitleIstigfarAudio.setText(isBn ? "রিমাইন্ডার অডিও" : "Reminder Audio");
    sheetBinding.tvAudioArabicLabel.setText(isBn ? "আরবি" : "Arabic");
    sheetBinding.tvAudioBanglaLabel.setText(isBn ? "বাংলা" : "Bengali");
    sheetBinding.tvAudioEnglishLabel.setText(isBn ? "ইংরেজি" : "English");
    sheetBinding.tvTitleIstigfarSilentHours.setText(isBn ? "সাইলেন্ট আওয়ার" : "Silent Hours");
    sheetBinding.tvDescIstigfarSilentHours.setText(isBn
        ? "ঘুম বা কাজের সময় স্মরণিকা নিঃশব্দ রাখতে সাইলেন্ট আওয়ার সেট করুন।"
        : "Set silent hours to mute reminders during sleep or busy hours.");
    sheetBinding.tvAddSilentHoursLabel.setText(isBn ? "+ সাইলেন্ট আওয়ার যোগ করুন" : "+ Add Silent Hours");
    sheetBinding.btnCancelIstigfar.setText(isBn ? "বাতিল" : "Cancel");
    sheetBinding.btnSaveIstigfar.setText(isBn ? "সেভ করুন" : "Save Changes");

    // 1. Initial State from preferences
    boolean isMasterEnabled = com.devflux.deenone.core.notifications.NotificationSettingsManager.isIstigfarReminderEnabled(this);
    final int[] selectedIntervalMinutes = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarIntervalMinutes(this)};
    final String[] selectedAudioLang = new String[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarAudioLanguage(this)};
    final boolean[] silentHoursEnabled = new boolean[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.isIstigfarSilentHoursEnabled(this)};
    final int[] silentStartHour = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarSilentStartHour(this)};
    final int[] silentStartMinute = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarSilentStartMinute(this)};
    final int[] silentEndHour = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarSilentEndHour(this)};
    final int[] silentEndMinute = new int[]{com.devflux.deenone.core.notifications.NotificationSettingsManager.getIstigfarSilentEndMinute(this)};

    final android.media.MediaPlayer[] previewPlayer = new android.media.MediaPlayer[1];
    final String[] currentPlayingLang = new String[]{null};

    // Master Switch state
    sheetBinding.switchIstigfarReminder.setChecked(isMasterEnabled);
    sheetBinding.layoutIstigfarActiveOptions.setVisibility(isMasterEnabled ? View.VISIBLE : View.GONE);
    sheetBinding.switchIstigfarReminder.setOnCheckedChangeListener((btn, isChecked) -> {
      sheetBinding.layoutIstigfarActiveOptions.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      if (isChecked && !com.devflux.deenone.core.permissions.PermissionHelper.hasNotificationPermission(this)) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
          requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }
      }
    });

    // 2. Interval text updater
    Runnable updateIntervalDisplay = () -> {
      int m = selectedIntervalMinutes[0];
      String text;
      if (m == 15) text = isBn ? "১৫ মিনিট" : "15 Minutes";
      else if (m == 30) text = isBn ? "৩০ মিনিট" : "30 Minutes";
      else if (m == 45) text = isBn ? "৪৫ মিনিট" : "45 Minutes";
      else if (m == 60) text = isBn ? "১ ঘণ্টা" : "1 Hour";
      else if (m == 120) text = isBn ? "২ ঘণ্টা" : "2 Hours";
      else if (m == 180) text = isBn ? "৩ ঘণ্টা" : "3 Hours";
      else text = isBn ? (BengaliNumberUtil.toBengali(String.valueOf(m)) + " মিনিট") : (m + " Minutes");
      sheetBinding.tvIstigfarIntervalDisplay.setText(text);
    };
    updateIntervalDisplay.run();

    // Interval Dialog Picker
    sheetBinding.btnOpenIntervalPicker.setOnClickListener(v -> {
      com.google.android.material.dialog.MaterialAlertDialogBuilder builder =
          new com.google.android.material.dialog.MaterialAlertDialogBuilder(this);
      com.devflux.deenone.databinding.DialogIstigfarIntervalPickerBinding dialogBinding =
          com.devflux.deenone.databinding.DialogIstigfarIntervalPickerBinding.inflate(LayoutInflater.from(this));

      dialogBinding.tvIntervalPickerTitle.setText(isBn ? "স্মরণিকার বিরতি নির্বাচন করুন" : "Select Reminder Interval");
      dialogBinding.tvInterval15.setText(isBn ? "১৫ মিনিট" : "15 Minutes");
      dialogBinding.tvInterval30.setText(isBn ? "৩০ মিনিট" : "30 Minutes");
      dialogBinding.tvInterval45.setText(isBn ? "৪৫ মিনিট" : "45 Minutes");
      dialogBinding.tvInterval60.setText(isBn ? "১ ঘণ্টা" : "1 Hour");
      dialogBinding.tvInterval120.setText(isBn ? "২ ঘণ্টা" : "2 Hours");
      dialogBinding.tvInterval180.setText(isBn ? "৩ ঘণ্টা" : "3 Hours");
      dialogBinding.btnCancelIntervalDialog.setText(isBn ? "বাতিল" : "Cancel");

      androidx.appcompat.app.AlertDialog alertDialog = builder.setView(dialogBinding.getRoot()).create();
      if (alertDialog.getWindow() != null) {
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
      }
      dialogBinding.itemInterval15.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 15; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval30.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 30; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval45.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 45; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval60.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 60; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval120.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 120; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.itemInterval180.setOnClickListener(dv -> { selectedIntervalMinutes[0] = 180; updateIntervalDisplay.run(); alertDialog.dismiss(); });
      dialogBinding.btnCancelIntervalDialog.setOnClickListener(dv -> alertDialog.dismiss());
      alertDialog.show();
    });

    // 3. Audio Selection & Preview
    Runnable stopPreviewAudio = () -> {
      if (previewPlayer[0] != null) {
        try {
          if (previewPlayer[0].isPlaying()) {
            previewPlayer[0].stop();
          }
          previewPlayer[0].reset();
          previewPlayer[0].release();
        } catch (Exception ignored) {}
        previewPlayer[0] = null;
      }
      currentPlayingLang[0] = null;
      sheetBinding.ivPlayIconArabic.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconBangla.setImageResource(R.drawable.ic_play_arrow);
      sheetBinding.ivPlayIconEnglish.setImageResource(R.drawable.ic_play_arrow);
    };

    Runnable updateAudioSelectionUI = () -> {
      boolean isArLang = "ar".equalsIgnoreCase(selectedAudioLang[0]);
      boolean isBnLang = "bn".equalsIgnoreCase(selectedAudioLang[0]);
      boolean isEnLang = "en".equalsIgnoreCase(selectedAudioLang[0]);

      sheetBinding.ivSelectArabic.setImageResource(isArLang ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectArabic.setColorFilter(isArLang ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectBangla.setImageResource(isBnLang ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectBangla.setColorFilter(isBnLang ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));

      sheetBinding.ivSelectEnglish.setImageResource(isEnLang ? R.drawable.ic_check_circle : R.drawable.circle_progress_bg);
      sheetBinding.ivSelectEnglish.setColorFilter(isEnLang ? androidx.core.content.ContextCompat.getColor(this, R.color.accent_mint) : androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
    };
    updateAudioSelectionUI.run();

    sheetBinding.rowAudioArabic.setOnClickListener(v -> {
      selectedAudioLang[0] = "ar";
      updateAudioSelectionUI.run();
    });

    sheetBinding.rowAudioBangla.setOnClickListener(v -> {
      selectedAudioLang[0] = "bn";
      updateAudioSelectionUI.run();
    });

    sheetBinding.rowAudioEnglish.setOnClickListener(v -> {
      selectedAudioLang[0] = "en";
      updateAudioSelectionUI.run();
    });

    // Audio Playback
    sheetBinding.btnPlayArabic.setOnClickListener(v -> {
      if ("ar".equalsIgnoreCase(currentPlayingLang[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_istigfar_ar);
        if (previewPlayer[0] != null) {
          currentPlayingLang[0] = "ar";
          sheetBinding.ivPlayIconArabic.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    sheetBinding.btnPlayBangla.setOnClickListener(v -> {
      if ("bn".equalsIgnoreCase(currentPlayingLang[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_istigfar_bn);
        if (previewPlayer[0] != null) {
          currentPlayingLang[0] = "bn";
          sheetBinding.ivPlayIconBangla.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    sheetBinding.btnPlayEnglish.setOnClickListener(v -> {
      if ("en".equalsIgnoreCase(currentPlayingLang[0])) {
        stopPreviewAudio.run();
        return;
      }
      stopPreviewAudio.run();
      try {
        previewPlayer[0] = android.media.MediaPlayer.create(this, R.raw.read_istigfar_en);
        if (previewPlayer[0] != null) {
          currentPlayingLang[0] = "en";
          sheetBinding.ivPlayIconEnglish.setImageResource(R.drawable.ic_pause);
          previewPlayer[0].setOnCompletionListener(mp -> stopPreviewAudio.run());
          previewPlayer[0].start();
        }
      } catch (Exception e) {
        stopPreviewAudio.run();
      }
    });

    // 4. Silent Hours (Do Not Disturb)
    Runnable updateSilentHoursUI = () -> {
      if (silentHoursEnabled[0]) {
        sheetBinding.btnShowAddSilentHours.setVisibility(View.GONE);
        sheetBinding.layoutActiveSilentHours.setVisibility(View.VISIBLE);

        String startStr = formatTime12H(silentStartHour[0], silentStartMinute[0]);
        String endStr = formatTime12H(silentEndHour[0], silentEndMinute[0]);
        sheetBinding.tvSilentHoursRange.setText(startStr + " - " + endStr);
      } else {
        sheetBinding.btnShowAddSilentHours.setVisibility(View.VISIBLE);
        sheetBinding.layoutActiveSilentHours.setVisibility(View.GONE);
      }
    };
    updateSilentHoursUI.run();

    Runnable openSilentHoursTimePickers = () -> {
      com.google.android.material.timepicker.MaterialTimePicker startPicker =
          new com.google.android.material.timepicker.MaterialTimePicker.Builder()
              .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
              .setHour(silentStartHour[0])
              .setMinute(silentStartMinute[0])
              .setTitleText(isBn ? "শুরুর সময় নির্বাচন করুন" : "Select Start Time")
              .setInputMode(com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK)
              .build();

      startPicker.addOnPositiveButtonClickListener(pv -> {
        int sHour = startPicker.getHour();
        int sMinute = startPicker.getMinute();

        com.google.android.material.timepicker.MaterialTimePicker endPicker =
            new com.google.android.material.timepicker.MaterialTimePicker.Builder()
                .setTimeFormat(com.google.android.material.timepicker.TimeFormat.CLOCK_12H)
                .setHour(silentEndHour[0])
                .setMinute(silentEndMinute[0])
                .setTitleText(isBn ? "শেষের সময় নির্বাচন করুন" : "Select End Time")
                .setInputMode(com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();

        endPicker.addOnPositiveButtonClickListener(epv -> {
          silentStartHour[0] = sHour;
          silentStartMinute[0] = sMinute;
          silentEndHour[0] = endPicker.getHour();
          silentEndMinute[0] = endPicker.getMinute();
          silentHoursEnabled[0] = true;
          updateSilentHoursUI.run();
        });

        endPicker.show(getSupportFragmentManager(), "silent_end_picker");
      });

      startPicker.show(getSupportFragmentManager(), "silent_start_picker");
    };

    sheetBinding.btnShowAddSilentHours.setOnClickListener(v -> openSilentHoursTimePickers.run());
    sheetBinding.tvSilentHoursRange.setOnClickListener(v -> openSilentHoursTimePickers.run());

    sheetBinding.btnDeleteSilentHours.setOnClickListener(v -> {
      silentHoursEnabled[0] = false;
      updateSilentHoursUI.run();
    });

    // 5. Bottom Actions - Rule 8: Zero unnecessary toasts
    sheetBinding.btnSaveIstigfar.setOnClickListener(v -> {
      stopPreviewAudio.run();
      boolean masterOn = sheetBinding.switchIstigfarReminder.isChecked();
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarReminderEnabled(this, masterOn);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarIntervalMinutes(this, selectedIntervalMinutes[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarAudioLanguage(this, selectedAudioLang[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarSilentHoursEnabled(this, silentHoursEnabled[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarSilentStartHour(this, silentStartHour[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarSilentStartMinute(this, silentStartMinute[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarSilentEndHour(this, silentEndHour[0]);
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIstigfarSilentEndMinute(this, silentEndMinute[0]);

      if (masterOn) {
        if (!com.devflux.deenone.core.permissions.PermissionHelper.hasNotificationPermission(this)) {
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
          }
        }
        com.devflux.deenone.core.notifications.IstigfarReminderScheduler.scheduleAll(this);
      } else {
        com.devflux.deenone.core.notifications.IstigfarReminderScheduler.cancelAll(this);
      }
      updateSoundAndNotificationHubSummaries();
      dialog.dismiss();
    });

    sheetBinding.btnCancelIstigfar.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    sheetBinding.btnBackIstigfarSheet.setOnClickListener(v -> {
      stopPreviewAudio.run();
      dialog.dismiss();
    });

    dialog.setOnDismissListener(d -> stopPreviewAudio.run());
    dialog.show();
  }

  private String formatTime12H(int hour, int minute) {
    int h = hour % 12;
    if (h == 0) h = 12;
    String amPm = hour >= 12 ? "PM" : "AM";
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    String timeStr = String.format(java.util.Locale.US, "%02d:%02d %s", h, minute, amPm);
    return isBn ? BengaliNumberUtil.toBengali(timeStr) : timeStr;
  }

  public void showNotificationSettingsSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetNotificationSettingsBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetNotificationSettingsBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Apply strict dual-language localization
    sheetBinding.tvNotifSettingsTitle.setText(isBn ? "নোটিফিকেশন সেটিংস" : "Notification Settings");
    sheetBinding.tvNotifSettingsSubtitle.setText(isBn
        ? "দৈনিক আমল, হাদিস, কুইজ ও স্মরণিকা ব্যবস্থাপনা"
        : "Daily Deeds, Hadith, Quiz & Reminders Management");

    sheetBinding.tvSectionDailyReminders.setText(isBn ? "দৈনিক দ্বীনি স্মরণিকা" : "Daily Islamic Reminders");
    sheetBinding.tvLabelDailyAmal.setText(isBn ? "দৈনিক আমল স্মরণিকা - সকাল ৮:৩০" : "Daily Deeds Reminder - 08:30 AM");
    sheetBinding.tvLabelDailyDuaHadith.setText(isBn ? "দৈনিক দোয়া ও হাদিস অ্যালার্ট" : "Daily Dua & Hadith Alert");
    sheetBinding.tvLabelDailyQuiz.setText(isBn ? "ইসলামিক কুইজ রিমাইন্ডার - রাত ৮:৩০" : "Islamic Quiz Reminder - 08:30 PM");
    sheetBinding.tvLabelQuranReminder.setText(isBn ? "দৈনিক কুরআন তিলাওয়াত স্মরণিকা" : "Daily Quran Recitation Reminder");
    sheetBinding.tvLabelDhikrReminder.setText(isBn ? "সকাল ও সন্ধ্যার মাসনূন জিকির" : "Morning & Evening Masnoon Dhikr");
    sheetBinding.tvLabelTahajjudReminder.setText(isBn ? "তাহাজ্জুদ ও শেষ রাতের ডাক" : "Tahajjud & Night Prayer Call");
    sheetBinding.tvLabelSunnahFasting.setText(isBn ? "সুন্নাত ও আইয়ামে বীজের রোজা" : "Sunnah & Ayyam al-Beed Fasting");
    sheetBinding.tvLabelJumuahReminder.setText(isBn ? "জুমার প্রস্তুতি ও সূরা কাহফ" : "Jumuah Preparation & Surah Al-Kahf");
    sheetBinding.tvLabelIslamicEvents.setText(isBn ? "হিজরি ক্যালেন্ডার ও বিশেষ দ্বীনি দিবস" : "Hijri Calendar & Islamic Events");

    sheetBinding.tvSectionSystemSettings.setText(isBn ? "সিস্টেম ও সাধারণ সেটিংস" : "System & General Settings");
    sheetBinding.tvLabelAdminNotifs.setText(isBn ? "এডমিন ও জরুরি সিস্টেম ঘোষণা" : "Admin & Important Announcements");
    sheetBinding.tvLabelNotificationSound.setText(isBn ? "নোটিফিকেশন রিংটোন" : "Notification Ringtone");
    sheetBinding.btnChangeNotificationSound.setText(isBn ? "পরিবর্তন" : "Change");
    updateNotificationSoundSummary(sheetBinding);
    sheetBinding.tvLabelVibration.setText(isBn ? "নোটিফিকেশনে ভাইব্রেশন" : "Vibration on Notifications");

    sheetBinding.tvTestVibrationLabel.setText(isBn ? "ভাইব্রেশন পরীক্ষা করুন" : "Test Notification Vibration");
    sheetBinding.btnTestVibration.setText(isBn ? "পরীক্ষা" : "Test");

    sheetBinding.tvLabelWearOsHub.setText(isBn ? "Wear OS ও স্ক্রিন উইজেট" : "Wear OS & Screen Widgets");
    sheetBinding.tvSubLabelWearOsHub.setText(isBn ? "স্মার্টওয়াচ সিঙ্ক, থিম ও লকস্ক্রিন উইজেট" : "Smartwatch Sync, Themes & Lockscreen Widgets");
    sheetBinding.btnWearOsHubAction.setText(isBn ? "সেটিংস" : "Settings");

    // Initialize Switches from persistent settings
    sheetBinding.switchDailyAmal.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isDailyAmalEnabled(this));
    sheetBinding.switchDailyDuaHadith.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isDailyDuaHadithEnabled(this));
    sheetBinding.switchDailyQuiz.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isDailyQuizEnabled(this));
    sheetBinding.switchAdminNotifs.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isAdminAnnouncementsEnabled(this));
    sheetBinding.switchVibration.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isVibrationEnabled(this));

    // Smart Reminder Category Switches
    sheetBinding.switchQuranReminder.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isQuranReminderEnabled(this));
    sheetBinding.switchDhikrReminder.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isDhikrReminderEnabled(this));
    sheetBinding.switchTahajjudReminder.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isTahajjudReminderEnabled(this));
    sheetBinding.switchSunnahFasting.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isSunnahFastingReminderEnabled(this));
    sheetBinding.switchJumuahReminder.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isJumuahReminderEnabled(this));
    sheetBinding.switchIslamicEvents.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.isIslamicEventsReminderEnabled(this));

    // Switch change listeners
    sheetBinding.switchDailyAmal.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDailyAmalEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchDailyDuaHadith.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDailyDuaHadithEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchDailyQuiz.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDailyQuizEnabled(this, isChecked);
    });

    sheetBinding.switchAdminNotifs.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setAdminAnnouncementsEnabled(this, isChecked);
    });

    sheetBinding.switchVibration.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setVibrationEnabled(this, isChecked);
    });

    sheetBinding.switchQuranReminder.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setQuranReminderEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchDhikrReminder.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setDhikrReminderEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchTahajjudReminder.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setTahajjudReminderEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchSunnahFasting.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setSunnahFastingReminderEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchJumuahReminder.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setJumuahReminderEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.switchIslamicEvents.setOnCheckedChangeListener((btn, isChecked) -> {
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setIslamicEventsReminderEnabled(this, isChecked);
      com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(this);
    });

    sheetBinding.layoutNotificationSound.setOnClickListener(v -> showNotificationSoundSelectionDialog(sheetBinding));
    sheetBinding.btnChangeNotificationSound.setOnClickListener(v -> showNotificationSoundSelectionDialog(sheetBinding));

    // Rule 8: Zero unnecessary toasts
    sheetBinding.layoutTestVibration.setOnClickListener(v -> {
      com.devflux.deenone.core.notifications.IslamicVibrationHelper.triggerVibration(
          this, com.devflux.deenone.core.notifications.IslamicVibrationHelper.PatternType.STRONG_ALARM, true
      );
    });

    sheetBinding.layoutOpenWearOsFromNotifSettings.setOnClickListener(v -> {
      showWearOsWidgetSettingsSheet();
    });

    // Rule 7: Touch animation only on buttons
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnChangeNotificationSound);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnTestVibration);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnWearOsHubAction);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseNotifSettingsSheet);

    sheetBinding.btnCloseNotifSettingsSheet.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  private void updateNotificationSoundSummary(com.devflux.deenone.databinding.BottomSheetNotificationSettingsBinding binding) {
    if (binding == null) return;
    String currentTone = com.devflux.deenone.core.notifications.NotificationSettingsManager.getNotificationTone(this);
    String displayName = com.devflux.deenone.core.notifications.NotificationSettingsManager.getNotificationToneDisplayName(this, currentTone);
    binding.tvCurrentNotificationSound.setText(displayName);
  }

  private android.media.MediaPlayer notifPreviewPlayer = null;

  private void stopNotificationSoundPreview() {
    if (notifPreviewPlayer != null) {
      try {
        if (notifPreviewPlayer.isPlaying()) {
          notifPreviewPlayer.stop();
        }
        notifPreviewPlayer.release();
      } catch (Exception ignored) {}
      notifPreviewPlayer = null;
    }
  }

  private void showNotificationSoundSelectionDialog(com.devflux.deenone.databinding.BottomSheetNotificationSettingsBinding parentBinding) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    com.google.android.material.dialog.MaterialAlertDialogBuilder builder =
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(this);

    com.devflux.deenone.databinding.DialogNotificationSoundPickerBinding pickerBinding =
        com.devflux.deenone.databinding.DialogNotificationSoundPickerBinding.inflate(getLayoutInflater());
    builder.setView(pickerBinding.getRoot());

    androidx.appcompat.app.AlertDialog dialog = builder.create();
    if (dialog.getWindow() != null) {
      dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    pickerBinding.tvDialogTitle.setText(isBn ? "নোটিফিকেশন রিংটোন নির্বাচন" : "Select Notification Ringtone");
    pickerBinding.tvDialogSubtitle.setText(isBn ? "আপনার পছন্দের ইসলামিক বা সিস্টেম সুর বেছে নিন" : "Choose your preferred Islamic or system tone");
    pickerBinding.tvToneDefaultTitle.setText(isBn ? "ডিফল্ট সিস্টেম টিউন" : "Default System Tone");
    pickerBinding.tvToneSubhanallahTitle.setText(isBn ? "সুবহানাল্লাহ সুর" : "Subhanallah Tone");
    pickerBinding.tvToneBismillahTitle.setText(isBn ? "বিসমিল্লাহ সুর" : "Bismillah Tone");
    pickerBinding.tvToneAllahuAkbarTitle.setText(isBn ? "আল্লাহু আকবার সুর" : "Allahu Akbar Tone");
    pickerBinding.tvToneChimeTitle.setText(isBn ? "মৃদু ড্রপ চাইম" : "Gentle Drop Chime");
    pickerBinding.btnCancel.setText(isBn ? "সম্পন্ন" : "Done");

    final String[] selectedTone = {com.devflux.deenone.core.notifications.NotificationSettingsManager.getNotificationTone(this)};

    Runnable updateToneRadioViews = () -> {
      String curr = selectedTone[0];
      pickerBinding.radioToneDefault.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_SYSTEM_DEFAULT.equals(curr));
      pickerBinding.radioToneSubhanallah.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_SUBHANALLAH.equals(curr));
      pickerBinding.radioToneBismillah.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_BISMILLAH.equals(curr));
      pickerBinding.radioToneAllahuAkbar.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_ALLAHU_AKBAR.equals(curr));
      pickerBinding.radioToneChime.setChecked(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_CHIME_DROP.equals(curr));
    };

    updateToneRadioViews.run();

    androidx.core.util.Consumer<String> selectAndPlay = toneKey -> {
      selectedTone[0] = toneKey;
      updateToneRadioViews.run();
      com.devflux.deenone.core.notifications.NotificationSettingsManager.setNotificationTone(this, toneKey);
      updateNotificationSoundSummary(parentBinding);

      // Play audio preview
      stopNotificationSoundPreview();
      android.net.Uri soundUri = com.devflux.deenone.core.notifications.NotificationSettingsManager.getNotificationToneUri(this, toneKey);
      if (soundUri != null) {
        try {
          notifPreviewPlayer = new android.media.MediaPlayer();
          if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            notifPreviewPlayer.setAudioAttributes(
                new android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            );
          }
          notifPreviewPlayer.setDataSource(this, soundUri);
          notifPreviewPlayer.prepare();
          notifPreviewPlayer.start();
        } catch (Exception e) {
          stopNotificationSoundPreview();
        }
      }
    };

    pickerBinding.cardToneDefault.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_SYSTEM_DEFAULT));
    pickerBinding.radioToneDefault.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_SYSTEM_DEFAULT));

    pickerBinding.cardToneSubhanallah.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_SUBHANALLAH));
    pickerBinding.radioToneSubhanallah.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_SUBHANALLAH));

    pickerBinding.cardToneBismillah.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_BISMILLAH));
    pickerBinding.radioToneBismillah.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_BISMILLAH));

    pickerBinding.cardToneAllahuAkbar.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_ALLAHU_AKBAR));
    pickerBinding.radioToneAllahuAkbar.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_ALLAHU_AKBAR));

    pickerBinding.cardToneChime.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_CHIME_DROP));
    pickerBinding.radioToneChime.setOnClickListener(v -> selectAndPlay.accept(com.devflux.deenone.core.notifications.NotificationSettingsManager.TONE_CHIME_DROP));

    pickerBinding.btnCancel.setOnClickListener(v -> {
      stopNotificationSoundPreview();
      dialog.dismiss();
    });
    dialog.setOnDismissListener(d -> stopNotificationSoundPreview());

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(pickerBinding.btnCancel);

    dialog.show();
  }

  public String getAdhanReciterDisplayName(String reciter) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    if ("madinah".equalsIgnoreCase(reciter)) return isBn ? "মদিনা মুনাওয়ারা আযান" : "Madinah Munawwarah Adhan";
    if ("alaqsa".equalsIgnoreCase(reciter)) return isBn ? "মসজিদুল আকসা আযান" : "Masjid Al-Aqsa Adhan";
    if ("abdulbasit".equalsIgnoreCase(reciter)) return isBn ? "কারী আব্দুল বাসেত আযান" : "Qari Abdul Basit Adhan";
    if ("mishary".equalsIgnoreCase(reciter)) return isBn ? "মিশারি রাশিদ আল-আফাসি আযান" : "Mishary Rashid Al-Afasy Adhan";
    return isBn ? "মক্কা মুকাররমা আযান" : "Makkah Mukarramah Adhan";
  }


  // =========================================================================
  // Islamic Audio Hub
  // =========================================================================
  private com.devflux.deenone.features.audio.viewmodel.AudioHubViewModel audioHubViewModel;
  private android.os.Handler audioProgressHandler;
  private Runnable audioProgressRunnable;

  public void showIslamicAudioHub() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.PageIslamicAudioHubBinding sheetBinding =
        com.devflux.deenone.databinding.PageIslamicAudioHubBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
    sheetBinding.tvAudioHubTitle.setText(isBn ? "ইসলামিক অডিও হাব" : "Islamic Audio Hub");
    sheetBinding.tvAudioHubSubtitle.setText(isBn ? "বিশ্ববিখ্যাত স্কলারদের তাফসির ও বয়ান" : "Lectures & Tafsir by World-Renowned Scholars");

    // Initialise ViewModel lazily
    if (audioHubViewModel == null) {
      audioHubViewModel = new androidx.lifecycle.ViewModelProvider(this).get(
          com.devflux.deenone.features.audio.viewmodel.AudioHubViewModel.class);
    }
    audioHubViewModel.loadCategory("all");

    // Adapter
    com.devflux.deenone.features.audio.adapter.AudioListAdapter adapter =
        new com.devflux.deenone.features.audio.adapter.AudioListAdapter(
            new com.devflux.deenone.features.audio.adapter.AudioListAdapter.OnAudioActionListener() {
              @Override
              public void onPlay(com.devflux.deenone.features.audio.model.IslamicAudioItem item) {
                audioHubViewModel.setCurrentItem(item);
                audioHubViewModel.markPlayed(item);
                com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.startPlayback(MainActivity.this, item);
                com.devflux.deenone.features.audio.service.AudioPlayerModalDialog.show(MainActivity.this, item);
                showNowPlayingBar(sheetBinding, item);
              }

              @Override
              public void onFavoriteToggle(com.devflux.deenone.features.audio.model.IslamicAudioItem item) {
                audioHubViewModel.toggleFavorite(item);
              }

              @Override
              public void onDownloadClick(com.devflux.deenone.features.audio.model.IslamicAudioItem item) {
                audioHubViewModel.startDownload(item, new com.devflux.deenone.features.audio.download.AudioDownloadManager.DownloadCallback() {
                  @Override
                  public void onProgress(com.devflux.deenone.features.audio.model.IslamicAudioItem item, int percentage, long downloadedBytes, long totalBytes) {}

                  @Override
                  public void onSuccess(com.devflux.deenone.features.audio.model.IslamicAudioItem item, java.io.File downloadedFile) {}

                  @Override
                  public void onError(com.devflux.deenone.features.audio.model.IslamicAudioItem item, String errorMessage) {}
                });
              }

              @Override
              public boolean isFavorite(String id) {
                return audioHubViewModel.isFavorite(id);
              }

              @Override
              public boolean isDownloaded(String id) {
                return audioHubViewModel.isDownloaded(id);
              }

              @Override
              public boolean isDownloading(String id) {
                return audioHubViewModel.isDownloading(id);
              }
            });

    sheetBinding.rvAudioList.setLayoutManager(new LinearLayoutManager(this));
    sheetBinding.rvAudioList.setAdapter(adapter);

    // Observe audio list
    audioHubViewModel.getAudioList().observe(this, adapter::submitList);

    // Error state
    audioHubViewModel.getError().observe(this, error -> {
      if (error != null && !error.isEmpty()) {
        sheetBinding.layoutAudioError.setVisibility(View.VISIBLE);
        sheetBinding.tvAudioErrorMsg.setText(error);
      } else {
        sheetBinding.layoutAudioError.setVisibility(View.GONE);
      }
    });

    sheetBinding.btnAudioRetry.setOnClickListener(v -> audioHubViewModel.retry());

    // Show current playing item if any
    audioHubViewModel.getCurrentItem().observe(this, item -> {
      if (item != null) showNowPlayingBar(sheetBinding, item);
    });

    dialog.setOnDismissListener(d -> {
      if (audioProgressHandler != null && audioProgressRunnable != null) {
        audioProgressHandler.removeCallbacks(audioProgressRunnable);
      }
    });

    sheetBinding.btnCloseAudioHub.setOnClickListener(v -> dialog.dismiss());
    sheetBinding.btnAudioThemeToggle.setOnClickListener(v -> {
      toggleAppTheme();
      dialog.dismiss();
    });
    sheetBinding.btnAudioNotification.setOnClickListener(v -> showNotificationHistorySheet());
    dialog.show();
  }

  private void showNowPlayingBar(
      com.devflux.deenone.databinding.PageIslamicAudioHubBinding binding,
      com.devflux.deenone.features.audio.model.IslamicAudioItem item) {
    binding.cardNowPlaying.setVisibility(View.VISIBLE);
    binding.tvNowPlayingTitle.setText(item.getTitle().isEmpty() ? "ইসলামিক অডিও" : item.getTitle());
    binding.tvNowPlayingSpeaker.setText(item.getSpeaker().isEmpty() ? "বিশ্ববিখ্যাত স্কলার" : item.getSpeaker());
    audioHubViewModel.setIsPlaying(true);
    updateNowPlayingToggleIcon(binding, true);

    binding.btnNowPlayingToggle.setOnClickListener(v -> {
      boolean playing = Boolean.TRUE.equals(audioHubViewModel.isPlaying().getValue());
      if (playing) {
        com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.pausePlayback(this);
        audioHubViewModel.setIsPlaying(false);
        updateNowPlayingToggleIcon(binding, false);
      } else {
        com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.startPlayback(this, item);
        audioHubViewModel.setIsPlaying(true);
        updateNowPlayingToggleIcon(binding, true);
      }
    });

    // 10s Rewind
    binding.btnNowPlayingPrev.setOnClickListener(v -> {
      Long cur = audioHubViewModel.getPlaybackPosition().getValue();
      long newPos = Math.max(0, (cur != null ? cur : 0L) - 10000);
      seekAudioService(newPos);
    });

    // 10s Fast Forward
    binding.btnNowPlayingNext.setOnClickListener(v -> {
      Long cur = audioHubViewModel.getPlaybackPosition().getValue();
      Long dur = audioHubViewModel.getPlaybackDuration().getValue();
      long maxDur = dur != null && dur > 0 ? dur : Long.MAX_VALUE;
      long newPos = Math.min(maxDur, (cur != null ? cur : 0L) + 10000);
      seekAudioService(newPos);
    });

    binding.btnNowPlayingClose.setOnClickListener(v -> {
      com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.stopPlayback(this);
      audioHubViewModel.setIsPlaying(false);
      binding.cardNowPlaying.setVisibility(View.GONE);
    });

    // SeekBar drag handling
    binding.seekBarAudio.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
          Long duration = audioHubViewModel.getPlaybackDuration().getValue();
          if (duration != null && duration > 0) {
            long targetMs = (long) (progress / 1000.0 * duration);
            binding.tvAudioCurrentTime.setText(formatMs(targetMs));
          }
        }
      }
      @Override
      public void onStartTrackingTouch(android.widget.SeekBar seekBar) {}
      @Override
      public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
        Long duration = audioHubViewModel.getPlaybackDuration().getValue();
        if (duration != null && duration > 0) {
          long targetMs = (long) (seekBar.getProgress() / 1000.0 * duration);
          seekAudioService(targetMs);
        }
      }
    });

    // Live progress tracking runnable
    if (audioProgressHandler == null) {
      audioProgressHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    }
    if (audioProgressRunnable != null) {
      audioProgressHandler.removeCallbacks(audioProgressRunnable);
    }
    audioProgressRunnable = new Runnable() {
      @Override
      public void run() {
        if (Boolean.TRUE.equals(audioHubViewModel.isPlaying().getValue())) {
          Long duration = audioHubViewModel.getPlaybackDuration().getValue();
          Long current = audioHubViewModel.getPlaybackPosition().getValue();
          if (duration != null && duration > 0 && current != null) {
            int prog = (int) ((current * 1000) / duration);
            binding.seekBarAudio.setProgress(prog);
            binding.tvAudioCurrentTime.setText(formatMs(current));
            binding.tvAudioTotalTime.setText(formatMs(duration));
          }
        }
        audioProgressHandler.postDelayed(this, 1000);
      }
    };
    audioProgressHandler.post(audioProgressRunnable);
  }

  private void seekAudioService(long targetMs) {
    Intent seekIntent = new Intent(MainActivity.this, com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.class);
    seekIntent.setAction(com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.ACTION_SEEK);
    seekIntent.putExtra(com.devflux.deenone.features.audio.service.IslamicAudioPlayerService.EXTRA_SEEK_MS, targetMs);
    startService(seekIntent);
    audioHubViewModel.updatePlaybackPosition(targetMs);
  }

  private void updateNowPlayingToggleIcon(
      com.devflux.deenone.databinding.PageIslamicAudioHubBinding binding, boolean isPlaying) {
    binding.btnNowPlayingToggle.setImageResource(
        isPlaying ? R.drawable.ic_pause_circle : R.drawable.ic_play_circle);
  }

  private static String formatMs(long ms) {
    long totalSec = ms / 1000;
    long min = totalSec / 60;
    long sec = totalSec % 60;
    if (min >= 60) {
      long hr = min / 60;
      min = min % 60;
      return String.format(java.util.Locale.US, "%d:%02d:%02d", hr, min, sec);
    }
    return String.format(java.util.Locale.US, "%02d:%02d", min, sec);
  }

  public void showIslamicBooksHubSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetBooksHubBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetBooksHubBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    // Static Header & Localization
    sheetBinding.tvBooksHubMainTitle.setText(isBn ? "ইসলামিক বই" : "Islamic Books");
    sheetBinding.etBooksSearch.setHint(isBn ? "বইয়ের নাম, বিষয় বা লেখক খুঁজুন..." : "Search by book title, author, or topic...");
    sheetBinding.tvBooksEmptyTitle.setText(isBn ? "কোনো বই পাওয়া যায়নি" : "No Books Found");
    sheetBinding.tvBooksEmptySubtitle.setText(isBn ? "অন্য কোনো শব্দ দিয়ে সার্চ করে দেখুন বা ক্যাটাগরি পরিবর্তন করুন।" : "Try searching with other keywords or change category.");
    sheetBinding.tvCurrentCategoryHeading.setText(isBn ? "সকল বই" : "All Books");

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseBooksHub);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnClearBooksSearch);

    com.devflux.deenone.features.books.BooksViewModel booksVm =
        new ViewModelProvider(this).get(com.devflux.deenone.features.books.BooksViewModel.class);

    booksVm.setSearchQuery("");
    booksVm.setCategory("all");

    // Books List Adapter
    final com.devflux.deenone.features.books.adapter.BookListAdapter[] bookAdapter = new com.devflux.deenone.features.books.adapter.BookListAdapter[1];
    bookAdapter[0] = new com.devflux.deenone.features.books.adapter.BookListAdapter(this, new com.devflux.deenone.features.books.adapter.BookListAdapter.OnBookActionListener() {
      @Override
      public void onItemClick(com.devflux.deenone.data.local.entity.IslamicBookEntity book, int position) {
        com.devflux.deenone.features.books.BookDetailsDialog.show(MainActivity.this, book, booksVm);
      }

      @Override
      public void onDownloadClick(com.devflux.deenone.data.local.entity.IslamicBookEntity book, int position) {
        booksVm.downloadBook(book, new com.devflux.deenone.features.books.download.BookDownloadManager.DownloadProgressListener() {
          @Override
          public void onProgress(String bookId, int percent, long bytesRead, long totalBytes) {
            book.setDownloadProgress(percent);
            if (bookAdapter[0] != null) bookAdapter[0].notifyItemChanged(position);
          }

          @Override
          public void onSuccess(String bookId, java.io.File localFile) {
            book.setDownloaded(true);
            book.setLocalFilePath(localFile.getAbsolutePath());
            book.setDownloadProgress(100);
            if (bookAdapter[0] != null) bookAdapter[0].notifyItemChanged(position);
            com.devflux.deenone.features.books.pdf.PdfBookReaderDialog.show(MainActivity.this, book, localFile);
          }

          @Override
          public void onError(String bookId, String errorMessage) {
            if (bookAdapter[0] != null) bookAdapter[0].notifyItemChanged(position);
          }
        });
      }

      @Override
      public void onReadClick(com.devflux.deenone.data.local.entity.IslamicBookEntity book, int position) {
        java.io.File file = booksVm.getLocalBookFile(book);
        com.devflux.deenone.features.books.pdf.PdfBookReaderDialog.show(MainActivity.this, book, file);
      }

      @Override
      public void onFavoriteClick(com.devflux.deenone.data.local.entity.IslamicBookEntity book, int position) {
        booksVm.toggleFavorite(book);
      }
    });

    sheetBinding.rvBooksList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
    sheetBinding.rvBooksList.setAdapter(bookAdapter[0]);

    // Live Search Listener
    sheetBinding.etBooksSearch.addTextChangedListener(new android.text.TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString();
        sheetBinding.btnClearBooksSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
        booksVm.setSearchQueryDebounced(query);
      }

      @Override
      public void afterTextChanged(android.text.Editable s) {}
    });

    sheetBinding.btnClearBooksSearch.setOnClickListener(v -> {
      sheetBinding.etBooksSearch.setText("");
      sheetBinding.btnClearBooksSearch.setVisibility(View.GONE);
      booksVm.setSearchQuery("");
    });

    // Structured Canonical Categories
    final String[][] categories = new String[][]{
        {"all", isBn ? "সব বই" : "All"},
        {"aqeedah", isBn ? "আকীদা" : "Aqeedah"},
        {"salah", isBn ? "সালাত" : "Salah"},
        {"zakat", isBn ? "যাকাত" : "Zakat"},
        {"sawm", isBn ? "সাওম ও রমজান" : "Sawm & Ramadan"},
        {"hajj", isBn ? "হজ ও উমরাহ" : "Hajj & Umrah"},
        {"dua", isBn ? "দো'আ ও যিকির" : "Dua & Zikr"},
        {"fatwa", isBn ? "ফতোয়া ও মাসআলা" : "Fatwa & Masail"},
        {"bidah", isBn ? "শিরক ও বিদআত" : "Shirk & Bid'ah"},
        {"family", isBn ? "পারিবারিক জীবন" : "Family & Life"},
        {"seerah", isBn ? "সীরাত ও জীবনী" : "Seerah & Biography"},
        {"firqa", isBn ? "ফিরকা ও দল" : "Sects & Groups"},
        {"quran_hadith", isBn ? "কুরআন ও হাদিস" : "Quran & Hadith"},
        {"qurbani_eid", isBn ? "কুরবানী ও ঈদ" : "Qurbani & Eid"},
        {"tawhid_waseela", isBn ? "তাওহীদ ও উসীলা" : "Tawhid & Waseela"}
    };

    final List<TextView> chipViews = new ArrayList<>();
    final String[] currentSelectedCat = new String[]{"all"};

    sheetBinding.layoutBookCategoryChips.removeAllViews();
    for (int i = 0; i < categories.length; i++) {
      final String catKey = categories[i][0];
      final String catName = categories[i][1];

      TextView chip = new TextView(this);
      LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
      );
      lp.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
      chip.setLayoutParams(lp);

      int padH = (int) (14 * getResources().getDisplayMetrics().density);
      int padV = (int) (7 * getResources().getDisplayMetrics().density);
      chip.setPadding(padH, padV, padH, padV);
      chip.setText(catName);
      chip.setTextSize(12.5f);
      chip.setClickable(true);
      chip.setFocusable(true);

      com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(chip);

      boolean isSelected = "all".equals(catKey);
      if (isSelected) {
        chip.setBackgroundResource(R.drawable.bg_btn_mint_pill);
        chip.setTextColor(getColor(R.color.bg_main));
        chip.setTypeface(null, android.graphics.Typeface.BOLD);
      } else {
        chip.setBackgroundResource(R.drawable.bg_badge_pill);
        chip.setTextColor(getColor(R.color.text_primary));
        chip.setTypeface(null, android.graphics.Typeface.NORMAL);
      }

      chip.setOnClickListener(v -> {
        currentSelectedCat[0] = catKey;
        booksVm.setCategory(catKey);

        sheetBinding.tvCurrentCategoryHeading.setText(
            "all".equals(catKey)
                ? (isBn ? "সকল বই" : "All Books")
                : (isBn ? ("বিষয়ঃ " + catName) : ("Category: " + catName))
        );

        for (int j = 0; j < chipViews.size(); j++) {
          TextView tv = chipViews.get(j);
          String k = categories[j][0];
          if (k.equals(currentSelectedCat[0])) {
            tv.setBackgroundResource(R.drawable.bg_btn_mint_pill);
            tv.setTextColor(getColor(R.color.bg_main));
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
          } else {
            tv.setBackgroundResource(R.drawable.bg_badge_pill);
            tv.setTextColor(getColor(R.color.text_primary));
            tv.setTypeface(null, android.graphics.Typeface.NORMAL);
          }
        }
      });

      chipViews.add(chip);
      sheetBinding.layoutBookCategoryChips.addView(chip);
    }

    // Observe Books List
    booksVm.getBooks().observe(this, list -> {
      if (list == null || list.isEmpty()) {
        sheetBinding.rvBooksList.setVisibility(View.GONE);
        sheetBinding.layoutBooksEmptyState.setVisibility(View.VISIBLE);
        sheetBinding.tvTopBooksCountBadge.setText(isBn ? "০টি বই" : "0 Books");
        sheetBinding.tvFilteredCountLabel.setText(isBn ? "০টি বই পাওয়া গেছে" : "0 books found");
      } else {
        sheetBinding.rvBooksList.setVisibility(View.VISIBLE);
        sheetBinding.layoutBooksEmptyState.setVisibility(View.GONE);
        String countStr = isBn
            ? (com.devflux.deenone.utils.BengaliNumberUtil.toBengali(list.size()) + "টি বই")
            : (list.size() + " Books");
        sheetBinding.tvTopBooksCountBadge.setText(countStr);
        sheetBinding.tvFilteredCountLabel.setText(
            isBn ? (countStr + " উপলব্ধ") : (list.size() + " books available")
        );
        if (bookAdapter[0] != null) {
          bookAdapter[0].setItems(list);
        }
      }
    });

    // Auto sync from GitHub CDN in background
    booksVm.syncOnlineCatalog(null);

    sheetBinding.btnCloseBooksHub.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
  }

  // =========================================================================
  // =========================================================================
  // Sleep Mode & Sleeping Adhkar Sheet
  // =========================================================================
  private void showSleepModeSheet() {
    boolean isDownloaded = com.devflux.deenone.core.quran.QuranUnifiedDownloadManager.isAllSurahsDownloaded(this);
    boolean hasDismissed = com.devflux.deenone.core.quran.QuranUnifiedDownloadManager.hasUserDismissedPrompt(this);

    if (!isDownloaded && !hasDismissed) {
      com.devflux.deenone.features.quran.QuranUnifiedDownloadDialog.showIfNeeded(
          this,
          this::openSleepModeSheetInternal,
          this::openSleepModeSheetInternal
      );
    } else {
      openSleepModeSheetInternal();
    }
  }

  private void openSleepModeSheetInternal() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetSleepModeBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetSleepModeBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

    sheetBinding.tvSleepModeTopBarTitle.setText(isBn ? "কুরআন স্লিপ মোড" : "Quran Sleep Mode");
    sheetBinding.tvSleepModeMainTitle.setText(isBn ? "ঘুমানোর পূর্বে পছন্দসই সূরা তিলাওয়াত" : "Preferred Surah Recitation Before Sleep");
    sheetBinding.tvSleepModeMainSubtitle.setText(isBn ? "টাইমার শেষ হলে অডিও স্বয়ংক্রিয়ভাবে বন্ধ হয়ে যাবে" : "Audio will automatically turn off when timer ends");
    sheetBinding.tvStartingSurahSectionTitle.setText(isBn ? "পছন্দসই সূরা নির্বাচন করুন:" : "Select Preferred Surah:");
    sheetBinding.tvQueueSectionTitle.setText(isBn ? "বাজানোর ক্রম (Playback Queue):" : "Playback Queue:");

    sheetBinding.btnDuration15.setText(isBn ? "15 মি." : "15 min");
    sheetBinding.btnDuration30.setText(isBn ? "30 মি." : "30 min");
    sheetBinding.btnDuration60.setText(isBn ? "60 মি." : "60 min");

    sheetBinding.chipSurahMulk.setText(isBn ? "❶ সূরা মুলক" : "❶ Al-Mulk");
    sheetBinding.chipSurahRahman.setText(isBn ? "💖 সূরা রহমান" : "💖 Ar-Rahman");
    sheetBinding.chipSurahYaseen.setText(isBn ? "🕊️ সূরা ইয়াসীন" : "🕊️ Ya-Sin");

    com.devflux.deenone.core.quran.QuranSleepManager sleepManager =
        com.devflux.deenone.core.quran.QuranSleepManager.getInstance();
    sleepManager.init(this);

    // Duration Button Styling Helper
    final int[] currentMinutesHolder = {15};
    final Runnable updateDurationUiAction = () -> {
      int minutes = currentMinutesHolder[0];
      int selectedBg = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_gold);
      int selectedText = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.bg_main);
      int unselectedText = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.text_primary);
      int strokeColor = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.border_card);

      com.google.android.material.button.MaterialButton[] durationBtns = {
          sheetBinding.btnDuration15,
          sheetBinding.btnDuration30,
          sheetBinding.btnDuration60
      };
      int[] durationValues = {15, 30, 60};

      for (int i = 0; i < durationBtns.length; i++) {
        boolean isSelected = (durationValues[i] == minutes);
        durationBtns[i].setBackgroundTintList(android.content.res.ColorStateList.valueOf(
            isSelected ? selectedBg : android.graphics.Color.TRANSPARENT
        ));
        durationBtns[i].setTextColor(isSelected ? selectedText : unselectedText);
        durationBtns[i].setStrokeColor(android.content.res.ColorStateList.valueOf(
            isSelected ? selectedBg : strokeColor
        ));
      }
    };

    // Render Playback Queue Items dynamically
    final Runnable renderQueueUiAction = () -> {
      java.util.List<Integer> queue = sleepManager.getSurahQueueLiveData().getValue();
      sheetBinding.layoutQueueList.removeAllViews();

      if (queue == null || queue.isEmpty()) {
        sheetBinding.tvEmptyQueueHint.setVisibility(View.VISIBLE);
      } else {
        sheetBinding.tvEmptyQueueHint.setVisibility(View.GONE);
        for (int i = 0; i < queue.size(); i++) {
          final int surahNum = queue.get(i);
          final int itemIndex = i;
          com.devflux.deenone.data.local.entity.QuranSurahEntity entity = sleepManager.getSurah(surahNum);

          com.devflux.deenone.databinding.ItemSleepQueueChipBinding chipBinding =
              com.devflux.deenone.databinding.ItemSleepQueueChipBinding.inflate(
                  LayoutInflater.from(MainActivity.this), sheetBinding.layoutQueueList, false
              );

          String numStr = isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(itemIndex + 1) : String.valueOf(itemIndex + 1);
          chipBinding.tvQueueItemNumber.setText(numStr);

          String surahName = (entity != null)
              ? (isBn ? ("সূরা " + entity.getNameBengali()) : ("Surah " + entity.getNameEnglish()))
              : (isBn ? ("সূরা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(surahNum)) : ("Surah " + surahNum));
          chipBinding.tvQueueItemName.setText(surahName);

          com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(chipBinding.btnRemoveQueueItem);
          chipBinding.btnRemoveQueueItem.setOnClickListener(v -> {
            sleepManager.removeFromQueue(surahNum);
          });

          sheetBinding.layoutQueueList.addView(chipBinding.getRoot());
        }
      }

      // Update Preset Chips active state
      int activeBg = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_mint);
      int activeText = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.btn_save_text);
      int unselectedText = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.text_primary);
      int strokeColor = androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.border_card);

      boolean hasMulk = (queue != null && queue.contains(67));
      boolean hasRahman = (queue != null && queue.contains(55));
      boolean hasYaseen = (queue != null && queue.contains(36));

      sheetBinding.chipSurahMulk.setBackgroundTintList(android.content.res.ColorStateList.valueOf(hasMulk ? activeBg : android.graphics.Color.TRANSPARENT));
      sheetBinding.chipSurahMulk.setTextColor(hasMulk ? activeText : unselectedText);
      sheetBinding.chipSurahMulk.setStrokeColor(android.content.res.ColorStateList.valueOf(hasMulk ? activeBg : strokeColor));

      sheetBinding.chipSurahRahman.setBackgroundTintList(android.content.res.ColorStateList.valueOf(hasRahman ? activeBg : android.graphics.Color.TRANSPARENT));
      sheetBinding.chipSurahRahman.setTextColor(hasRahman ? activeText : unselectedText);
      sheetBinding.chipSurahRahman.setStrokeColor(android.content.res.ColorStateList.valueOf(hasRahman ? activeBg : strokeColor));

      sheetBinding.chipSurahYaseen.setBackgroundTintList(android.content.res.ColorStateList.valueOf(hasYaseen ? activeBg : android.graphics.Color.TRANSPARENT));
      sheetBinding.chipSurahYaseen.setTextColor(hasYaseen ? activeText : unselectedText);
      sheetBinding.chipSurahYaseen.setStrokeColor(android.content.res.ColorStateList.valueOf(hasYaseen ? activeBg : strokeColor));
    };

    // Observers
    sleepManager.getFormattedTimerLiveData().observe(this, timeStr -> {
      if (timeStr != null) {
        sheetBinding.tvSleepCountdown.setText(isBn ? com.devflux.deenone.utils.BengaliNumberUtil.toBengali(timeStr) : timeStr);
      }
    });

    sleepManager.getTimerMinutesLiveData().observe(this, minutes -> {
      if (minutes != null) {
        currentMinutesHolder[0] = minutes;
        updateDurationUiAction.run();
      }
    });

    sleepManager.getCurrentSurahNumberLiveData().observe(this, surahNum -> {
      if (surahNum != null) {
        com.devflux.deenone.data.local.entity.QuranSurahEntity cur = sleepManager.getSurah(surahNum);
        if (cur != null) {
          String sName = isBn
              ? ("সূরা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(cur.getNumber()) + ". " + cur.getNameBengali())
              : ("Surah " + cur.getNumber() + ". " + cur.getNameEnglish());
          sheetBinding.tvSelectedSurahName.setText(sName);
        }
      }
    });

    sleepManager.getSurahQueueLiveData().observe(this, queue -> {
      renderQueueUiAction.run();
    });

    sleepManager.getStateLiveData().observe(this, state -> {
      if (state == null) return;
      switch (state) {
        case PLAYING:
          sheetBinding.btnToggleSleepPlay.setText(isBn ? "⏸ স্লিপ প্লে বিরতি দিন" : "⏸ Pause Sleep Play");
          sheetBinding.btnToggleSleepPlay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_gold)));
          sheetBinding.btnToggleSleepPlay.setTextColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.bg_main));
          break;
        case PAUSED:
          sheetBinding.btnToggleSleepPlay.setText(isBn ? "▶ পুনরায় চালু করুন" : "▶ Resume Sleep Play");
          sheetBinding.btnToggleSleepPlay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_mint)));
          sheetBinding.btnToggleSleepPlay.setTextColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.btn_save_text));
          break;
        case BUFFERING:
          sheetBinding.btnToggleSleepPlay.setText(isBn ? "● লোড হচ্ছে..." : "● Loading...");
          sheetBinding.btnToggleSleepPlay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_mint)));
          sheetBinding.btnToggleSleepPlay.setTextColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.btn_save_text));
          break;
        case STOPPED:
        case IDLE:
        default:
          sheetBinding.btnToggleSleepPlay.setText(isBn ? "▶ স্লিপ প্লে শুরু করুন" : "▶ Start Sleep Play");
          sheetBinding.btnToggleSleepPlay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.accent_mint)));
          sheetBinding.btnToggleSleepPlay.setTextColor(androidx.core.content.ContextCompat.getColor(MainActivity.this, R.color.btn_save_text));
          break;
      }
    });

    // Duration Button Click Listeners
    sheetBinding.btnDuration15.setOnClickListener(v -> sleepManager.setTimerMinutes(15));
    sheetBinding.btnDuration30.setOnClickListener(v -> sleepManager.setTimerMinutes(30));
    sheetBinding.btnDuration60.setOnClickListener(v -> sleepManager.setTimerMinutes(60));

    // Preset Chips Click Listeners (adds to queue)
    sheetBinding.chipSurahMulk.setOnClickListener(v -> sleepManager.addToQueue(67));
    sheetBinding.chipSurahRahman.setOnClickListener(v -> sleepManager.addToQueue(55));
    sheetBinding.chipSurahYaseen.setOnClickListener(v -> sleepManager.addToQueue(36));

    // Surah Dropdown Selector (114 Surahs)
    sheetBinding.cardSurahDropdown.setOnClickListener(v -> {
      List<com.devflux.deenone.data.local.entity.QuranSurahEntity> surahs = sleepManager.getSurahList();
      String[] items = new String[surahs.size()];
      for (int i = 0; i < surahs.size(); i++) {
        com.devflux.deenone.data.local.entity.QuranSurahEntity s = surahs.get(i);
        if (isBn) {
          items[i] = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(s.getNumber()) + ". " + s.getNameBengali() + " (" + s.getNameArabic() + ") • " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(s.getNumberOfAyahs()) + " আয়াত";
        } else {
          items[i] = s.getNumber() + ". " + s.getNameEnglish() + " (" + s.getNameArabic() + ") • " + s.getNumberOfAyahs() + " Ayahs";
        }
      }
      new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
          .setTitle(isBn ? "পছন্দসই সূরা নির্বাচন করুন" : "Select Preferred Surah")
          .setItems(items, (d, which) -> {
            int selectedSurah = surahs.get(which).getNumber();
            sleepManager.addToQueue(selectedSurah);
          })
          .show();
    });

    // Touch animation strictly on buttons ONLY per Rule 7
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDuration15);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDuration30);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnDuration60);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipSurahMulk);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipSurahRahman);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.chipSurahYaseen);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnToggleSleepPlay);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseSleepMode);

    sheetBinding.btnToggleSleepPlay.setOnClickListener(v -> {
      java.util.List<Integer> q = sleepManager.getSurahQueueLiveData().getValue();
      int firstSurah = (q != null && !q.isEmpty()) ? q.get(0) : 67;
      boolean isCached = com.devflux.deenone.core.quran.QuranAudioCacheManager.isSurahAudioCached(MainActivity.this, firstSurah);
      if (!isCached && !com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(MainActivity.this)) {
        com.devflux.deenone.features.quran.QuranOfflineDownloadDialog.showNoInternetDialog(
            MainActivity.this,
            () -> sleepManager.togglePlayPause()
        );
      } else {
        sleepManager.togglePlayPause();
      }
    });

    sheetBinding.btnCloseSleepMode.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
  }

  private void showSleepModeOfflineDownloadDialog(Runnable onFinished) {
    com.devflux.deenone.features.quran.QuranUnifiedDownloadDialog.showIfNeeded(
        this,
        () -> {
          if (onFinished != null) onFinished.run();
        },
        () -> {
          if (onFinished != null) onFinished.run();
        }
    );
  }

  // =========================================================================
  // Blood Donation & Social Services Sheet (উম্মাহ রক্তদান নেটওয়ার্ক)
  // =========================================================================
  public void showBloodDonationSheet() {
    com.devflux.deenone.features.blood.BloodDonationNetworkDialog.show(this);
  }

  public void showSixKalimaSheet() {
    com.devflux.deenone.features.kalima.SixKalimaPageDialog.show(this);
  }

  // =========================================================================
  // Islamic Travel Mode Sheet (সফর মোড - কসর, জমা ও ৪ মাযহাবের নির্দেশিকা)
  // =========================================================================
  public void showTravelModeSheet() {
    com.devflux.deenone.core.travel.TravelModePageDialog.show(this);
  }

  // =========================================================================
  // Friday Jummah Mode Sheet (পবিত্র জুমু'আ মোড)
  // =========================================================================
  // Friday Jummah Mode Sheet (পবিত্র জুমু'আ মোড)
  // =========================================================================
  private void showJummahModeSheet() {
    com.devflux.deenone.core.jummah.JummahModePageDialog.show(this);
  }

  public void showQuranSurahDirect(int surahNumber) {
    if (quranRepository != null) {
      quranRepository.getSurahByNumber(surahNumber).observe(this, surah -> {
        if (surah != null) {
          showSurahReaderBottomSheet(surah);
        } else {
          showQuranHubBottomSheet();
        }
      });
    } else {
      showQuranHubBottomSheet();
    }
  }

  // =========================================================================
  // Advanced Location-Aware Islamic Eid Mode Sheet (পবিত্র ঈদ মোড)
  // =========================================================================
  private void showEidModeSheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetEidModeBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetEidModeBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseEidMode);
    sheetBinding.btnCloseEidMode.setOnClickListener(v -> dialog.dismiss());

    // Default to active type or Fitr
    final com.devflux.deenone.core.eid.EidModeManager.EidType[] currentType =
        new com.devflux.deenone.core.eid.EidModeManager.EidType[]{
            com.devflux.deenone.core.eid.EidModeManager.getInstance().getActiveEidType(this)
        };

    java.lang.Runnable updateEidView = () -> {
      boolean isFitr = currentType[0] == com.devflux.deenone.core.eid.EidModeManager.EidType.EID_UL_FITR;
      if (isFitr) {
        sheetBinding.tabEidFitr.setTextColor(android.graphics.Color.parseColor("#34D399"));
        sheetBinding.tabEidAdha.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
        sheetBinding.tvEidSubtitle.setText("১ম শাওয়াল (রমজানের সমাপ্তি, ঈদের নামাজ ও ফিতরা আদায়)");

        sheetBinding.tvSpecialRitualHeader.setText("সাদাকাতুল ফিতর সংক্রান্ত গুরুত্বপূর্ণ বিধান");
        sheetBinding.tvSpecialRitualContent.setText("• ফিতরার পরিমাণ: গম বা আটা ১/২ সা' (প্রায় ১ কেজি ৬৫০ গ্রাম) অথবা খেজুর/কিশমিশ/যব ১ সা' (প্রায় ৩ কেজি ৩০০ গ্রাম) বা সমপরিমাণ বাজারমূল্য।\n• কার ওপর ওয়াজিব: ঈদের দিন সুবহে সাদিকের সময় যার নিকট নিজের ও পরিবারের নিত্যপ্রয়োজনীয় খরচের অতিরিক্ত নেসাব পরিমাণ সম্পদ থাকে।\n• আদায়ের সময়: ঈদের নামাজের উদ্দেশ্যে ঈদগাহে যাওয়ার পূর্বেই গরীবদের হাতে পৌঁছে দেওয়া সুন্নাত।");
        sheetBinding.tvSpecialRitualRef.setText("— সহীহ বুখারী: ১৫০৩, সহীহ মুসলিম: ৯৮৪");
      } else {
        sheetBinding.tabEidFitr.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
        sheetBinding.tabEidAdha.setTextColor(android.graphics.Color.parseColor("#34D399"));
        sheetBinding.tvEidSubtitle.setText("১০-১৩ জিলহজ (কুরবানী, তাকবীরে তাশরীক ও ঈদুল আজহা)");

        sheetBinding.tvSpecialRitualHeader.setText("কুরবানীর বিধান ও পশু জবাইয়ের দোয়া");
        sheetBinding.tvSpecialRitualContent.setText("• পশুর বয়স: উট ৫ বছর, গরু/মহিষ ২ বছর, ছাগল/ভেড়া/দুম্বা ১ বছর পূর্ণ হতে হবে।\n• ত্রুটিমুক্ততা: দৃষ্টিহীন, পঙ্গু, দুর্বল বা অতি কৃশকায় পশু দ্বারা কুরবানী শুদ্ধ হবে না।\n• গোশত বণ্টন: ৩ ভাগ করা উত্তম—১ ভাগ নিজের, ১ ভাগ আত্মীয়-স্বজনের ও ১ ভাগ গরীবদের।\n• পশু জবাইয়ের দোয়া: 'বিসমিল্লাহি ওয়াল্লাহু আকবার। হে আল্লাহ! এটি আপনার পক্ষ থেকে এবং আপনারই সন্তুষ্টির জন্য কবুল করুন।'\n\n— সহীহ মুসলিম: ১৯৬৬, সুনানে আবু দাউদ: ২৭৯৫");
        sheetBinding.tvSpecialRitualRef.setText("— সহীহ মুসলিম: ১৯৬৬, সুনানে আবু দাউদ: ২৭৯৫");
      }

      // Populate Sunnahs
      sheetBinding.layoutEidSunnahsContainer.removeAllViews();
      List<com.devflux.deenone.core.eid.EidModeManager.EidSunnahItem> sunnahs =
          com.devflux.deenone.core.eid.EidModeManager.getInstance().getEidSunnahs(currentType[0]);
      for (com.devflux.deenone.core.eid.EidModeManager.EidSunnahItem item : sunnahs) {
        com.devflux.deenone.databinding.ItemEidSunnahCardBinding itemBinding =
            com.devflux.deenone.databinding.ItemEidSunnahCardBinding.inflate(LayoutInflater.from(this), sheetBinding.layoutEidSunnahsContainer, false);
        itemBinding.tvEidSunnahTitle.setText(item.title);
        itemBinding.tvEidSunnahDesc.setText(item.description);
        itemBinding.tvEidSunnahRef.setText("উৎস/দলীল: "+ item.reference);
        sheetBinding.layoutEidSunnahsContainer.addView(itemBinding.getRoot());
      }
    };

    TouchAnimationUtil.attachTouchSpring(sheetBinding.tabEidFitr);
    sheetBinding.tabEidFitr.setOnClickListener(v -> {
      currentType[0] = com.devflux.deenone.core.eid.EidModeManager.EidType.EID_UL_FITR;
      com.devflux.deenone.core.eid.EidModeManager.getInstance().setEidTypeOverride(this, "fitr");
      updateEidView.run();
    });

    TouchAnimationUtil.attachTouchSpring(sheetBinding.tabEidAdha);
    sheetBinding.tabEidAdha.setOnClickListener(v -> {
      currentType[0] = com.devflux.deenone.core.eid.EidModeManager.EidType.EID_UL_ADHA;
      com.devflux.deenone.core.eid.EidModeManager.getInstance().setEidTypeOverride(this, "adha");
      updateEidView.run();
    });

    // 6-Takbeer vs 12-Takbeer method toggle
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMethodHanafi);
    sheetBinding.btnMethodHanafi.setOnClickListener(v -> {
      sheetBinding.btnMethodHanafi.setTextColor(android.graphics.Color.parseColor("#34D399"));
      sheetBinding.btnMethodShafii.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.tvPrayerMethodText.setText("১ম রাকাত:\n• তাকবীরে তাহরীমা বলে হাত বাঁধবেন ও ছানা পড়বেন।\n• এরপর অতিরিক্ত ৩টি তাকবীর বলবেন—প্রথম দুই তাকবীরে হাত কান পর্যন্ত উঠিয়ে ছেড়ে দেবেন এবং ৩য় তাকবীরে হাত বাঁধবেন।\n• এরপর সূরা ফাতিহা ও অন্য সূরা পড়ে রুকু-সিজদা সম্পন্ন করবেন।\n\n২য় রাকাত:\n• দাঁড়িয়ে প্রথমে সূরা ফাতিহা ও অন্য সূরা পড়বেন।\n• রুকুতে যাওয়ার পূর্বে অতিরিক্ত ৩টি তাকবীর বলবেন এবং হাত ছেড়ে দেবেন। ৪র্থ তাকবীর বলে রুকুতে যাবেন।");
      sheetBinding.tvPrayerMethodRef.setText("উৎস/দলীল: মুসান্নাফে ইবনে আবী শায়বাহ: ৫৬৩৯, ফাতাওয়া হিন্দিয়া ১/১৫০");
    });

    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMethodShafii);
    sheetBinding.btnMethodShafii.setOnClickListener(v -> {
      sheetBinding.btnMethodHanafi.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
      sheetBinding.btnMethodShafii.setTextColor(android.graphics.Color.parseColor("#34D399"));
      sheetBinding.tvPrayerMethodText.setText("১ম রাকাত:\n• তাকবীরে তাহরীমার পর ছানা পড়বেন এবং অতিরিক্ত ৭টি তাকবীর বলবেন। প্রতি তাকবীরে হাত উঠাবেন।\n• এরপর সূরা ফাতিহা ও ক্বিরাত পড়ে রুকু-সিজদা করবেন।\n\n২য় রাকাত:\n• সিজদা থেকে দাঁড়িয়ে অতিরিক্ত ৫টি তাকবীর বলবেন।\n• এরপর সূরা ফাতিহা ও ক্বিরাত পড়ে স্বাভাবিকভাবে নামাজ সমাপ্ত করবেন।");
      sheetBinding.tvPrayerMethodRef.setText("উৎস/দলীল: সুনানে আবু দাউদ: ১১৫১, জামে তিরমিযী: ৫৩৬ (সহীহ)");
    });

    // Copy Takbeer
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCopyTakbeer);
    sheetBinding.btnCopyTakbeer.setOnClickListener(v -> {
      String takbeerText = sheetBinding.tvTakbeerArabic.getText().toString() + "\n\n" +
          "“আল্লাহু আকবার, আল্লাহু আকবার, লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার, আল্লাহু আকবার ওয়া লিল্লাহিল হামদ।”\n\n" +
          "— সুনানে দারা কুতনী: ১৭১৭";
      copyToClipboard("ঈদের তাকবীর", takbeerText);
    });

    // Play Takbeer Audio
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlayTakbeer);
    sheetBinding.btnPlayTakbeer.setOnClickListener(v -> {
      try {
        v.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
      } catch (Exception ignored) {}
    });

    // Share Eid Greeting
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnShareGreeting);
    sheetBinding.btnShareGreeting.setOnClickListener(v -> {
      String shareMsg = "ঈদ মুবারক!\n\n" +
          "تَقَبَّلَ اللَّهُ مِنَّا وَمِنْكُمْ (তাকাব্বালাল্লাহু মিন্না ওয়া মিনকুম)\n\n" +
          "\"আল্লাহ আমাদের ও আপনার সকল নেক আমল এবং ইবাদত কবুল করুন। আপনার ও পরিবারের সকলের ঈদ আনন্দময় ও বরকতময় হোক!\"\n\n" +
          "— দ্বীন ওয়ান (DeenOne) ইসলামিক অ্যাপ";
      Intent sendIntent = new Intent(Intent.ACTION_SEND);
      sendIntent.setType("text/plain");
      sendIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
      startActivity(Intent.createChooser(sendIntent, "ঈদের শুভেচ্ছা পাঠান"));
    });

    updateEidView.run();
    dialog.show();
  }

  // =========================================================================
  // Islamic Question & Answer / Fatwa Search Sheet (ইসলামিক প্রশ্নোত্তর)
  // =========================================================================
  private void showIslamicQASheet() {
    FullScreenPageDialog dialog = new FullScreenPageDialog(this);
    com.devflux.deenone.databinding.BottomSheetIslamicQaBinding sheetBinding =
        com.devflux.deenone.databinding.BottomSheetIslamicQaBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(sheetBinding.getRoot());

    sheetBinding.btnCloseQASheet.setOnClickListener(v -> dialog.dismiss());

    final String[] activeCategory = new String[]{"সকল"};

    java.lang.Runnable performSearch = () -> {
      String query = sheetBinding.etQASearch.getText().toString().trim();
      List<com.devflux.deenone.core.qa.IslamicQAManager.QAItem> results =
          com.devflux.deenone.core.qa.IslamicQAManager.getInstance().searchQA(query, activeCategory[0]);

      sheetBinding.layoutQAResultsContainer.removeAllViews();

      if (results.isEmpty()) {
        sheetBinding.layoutQAEmptyState.setVisibility(View.VISIBLE);
        sheetBinding.tvQACountBadge.setText("০টি ফলাফল পাওয়া গেছে");
      } else {
        sheetBinding.layoutQAEmptyState.setVisibility(View.GONE);
        sheetBinding.tvQACountBadge.setText(BengaliNumberUtil.toBengali(results.size()) + "টি যাচাইকৃত উত্তর পাওয়া গেছে");

        for (com.devflux.deenone.core.qa.IslamicQAManager.QAItem item : results) {
          com.devflux.deenone.databinding.ItemIslamicQaCardBinding itemBinding =
              com.devflux.deenone.databinding.ItemIslamicQaCardBinding.inflate(LayoutInflater.from(this), sheetBinding.layoutQAResultsContainer, false);

          itemBinding.tvItemCategory.setText(item.category);
          itemBinding.tvItemQuestion.setText(item.question);
          itemBinding.tvItemAnswer.setText(item.answer);
          itemBinding.tvItemScholar.setText(""+ item.scholarOrInstitution);
          itemBinding.tvItemReference.setText("সূত্র: "+ item.reference);
          itemBinding.tvItemDate.setText("তারিখ: "+ item.date);

          // Copy QA
          itemBinding.btnCopyQA.setOnClickListener(v -> {
            String qaText = "প্রশ্ন: "+ item.question + "\n\n" +
                "উত্তর: "+ item.answer + "\n\n" +
                "ফতোয়া প্রদানকারী: "+ item.scholarOrInstitution + "\n" +
                "রেফারেন্স: "+ item.reference + "\n" +
                "তারিখ: "+ item.date + "\n" +
                "সোর্স: "+ item.sourceUrl + "\n\n" +
                "— দ্বীন ওয়ান (DeenOne) ইসলামিক অ্যাপ";
            copyToClipboard("ইসলামিক প্রশ্নোত্তর", qaText);
            Toast.makeText(this, "প্রশ্নোত্তর কপি করা হয়েছে", Toast.LENGTH_SHORT).show();
          });

          // Open Source URL
          itemBinding.btnOpenSourceUrl.setOnClickListener(v -> {
            if (item.sourceUrl != null && !item.sourceUrl.isEmpty()) {
              try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(item.sourceUrl));
                startActivity(browserIntent);
              } catch (Exception e) {
                Toast.makeText(this, "সোর্স লিংক খোলা যায়নি", Toast.LENGTH_SHORT).show();
              }
            }
          });

          // Share QA
          itemBinding.btnShareQA.setOnClickListener(v -> {
            String shareText = "প্রশ্ন: "+ item.question + "\n\n" +
                "উত্তর: "+ item.answer + "\n\n" +
                "ফতোয়া বোর্ড: "+ item.scholarOrInstitution + "\n" +
                "রেফারেন্স: "+ item.reference + "\n\n" +
                "— দ্বীন ওয়ান (DeenOne)";
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(sendIntent, "প্রশ্নোত্তর শেয়ার করুন"));
          });

          sheetBinding.layoutQAResultsContainer.addView(itemBinding.getRoot());
        }
      }
    };

    // Text Watcher for Search
    sheetBinding.etQASearch.addTextChangedListener(new android.text.TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        sheetBinding.btnClearQASearch.setVisibility(!s.toString().isEmpty() ? View.VISIBLE : View.GONE);
        performSearch.run();
      }

      @Override
      public void afterTextChanged(android.text.Editable s) {}
    });

    sheetBinding.btnClearQASearch.setOnClickListener(v -> {
      sheetBinding.etQASearch.setText("");
      performSearch.run();
    });

    // Category Chips
    TextView[] chips = new TextView[]{
        sheetBinding.chipCatAll,
        sheetBinding.chipCatSalah,
        sheetBinding.chipCatFasting,
        sheetBinding.chipCatZakat,
        sheetBinding.chipCatFinance,
        sheetBinding.chipCatFamily,
        sheetBinding.chipCatModern
    };

    for (TextView chip : chips) {
      chip.setOnClickListener(v -> {
        for (TextView c : chips) {
          c.setTextColor(android.graphics.Color.parseColor("#9CA3AF"));
        }
        chip.setTextColor(android.graphics.Color.parseColor("#34D399"));
        activeCategory[0] = chip.getText().toString();
        performSearch.run();
      });
    }

    performSearch.run();
    dialog.show();
  }

  // =========================================================================
  // User Profile & Spiritual Progress Sheet
  // =========================================================================
  private void showUserProfileSheet() {
    com.devflux.deenone.features.profile.FullProfileDialog.show(this);
  }

  // =========================================================================
  // Notification Detail BottomSheet (Shows Full Message & Actions)
  // =========================================================================
  public void showNotificationDetailSheet(String title, String body, String type, String deepLinkAction, long timestamp) {
    com.google.android.material.bottomsheet.BottomSheetDialog dialog =
        new com.google.android.material.bottomsheet.BottomSheetDialog(this);
    com.devflux.deenone.databinding.BottomSheetNotificationDetailBinding binding =
        com.devflux.deenone.databinding.BottomSheetNotificationDetailBinding.inflate(LayoutInflater.from(this));
    dialog.setContentView(binding.getRoot());

    binding.tvDetailTitle.setText(title != null ? title : "ইসলামিক বার্তা");
    binding.tvDetailBody.setText(body != null ? body : "");

    String categoryName = "দ্বীনি বার্তা ও স্মরণিকা";
    int iconRes = R.drawable.ic_deenone_logo;
    if ("adhan".equalsIgnoreCase(type) || "prayer".equalsIgnoreCase(type) || "tahajjud".equalsIgnoreCase(type)) {
      categoryName = "আযান ও সালাত অ্যালার্ট";
      iconRes = R.drawable.ic_mosque;
    } else if ("daily_dua".equalsIgnoreCase(type) || "dua".equalsIgnoreCase(type)) {
      categoryName = "মাসনূন দোয়া";
      iconRes = R.drawable.ic_feat_dua;
    } else if ("daily_hadith".equalsIgnoreCase(type) || "hadith".equalsIgnoreCase(type)) {
      categoryName = "সহীহ হাদিস";
      iconRes = R.drawable.ic_feat_hadith;
    } else if ("daily_amal".equalsIgnoreCase(type) || "amal".equalsIgnoreCase(type)) {
      categoryName = "দৈনিক আমল";
      iconRes = R.drawable.ic_feat_amal;
    } else if ("sehri".equalsIgnoreCase(type) || "iftar".equalsIgnoreCase(type) || "ramadan".equalsIgnoreCase(type)) {
      categoryName = "সেহরি ও ইফতার";
      iconRes = R.drawable.ic_moon;
    } else if ("admin_announcement".equalsIgnoreCase(type)) {
      categoryName = "জরুরি এডমিন ঘোষণা";
      iconRes = R.drawable.ic_shield_check;
    }
    binding.tvDetailCategoryBadge.setText(categoryName);
    binding.ivDetailIcon.setImageResource(iconRes);

    if (timestamp > 0) {
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd MMM yyyy • hh:mm a", java.util.Locale.getDefault());
      binding.tvDetailTimestamp.setText(sdf.format(new java.util.Date(timestamp)));
    } else {
      binding.tvDetailTimestamp.setText("আজকের বার্তা");
    }

    binding.btnDetailClose.setOnClickListener(v -> dialog.dismiss());

    binding.btnDetailCopy.setOnClickListener(v -> {
      android.content.ClipboardManager cm = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
      if (cm != null) {
        cm.setPrimaryClip(android.content.ClipData.newPlainText("DeenOne Notification", (title != null ? title + "\n\n" : "") + body));
        Toast.makeText(MainActivity.this, "বার্তাটি কপি করা হয়েছে", Toast.LENGTH_SHORT).show();
      }
    });

    if (deepLinkAction != null && !deepLinkAction.isEmpty()) {
      binding.btnDetailAction.setVisibility(View.VISIBLE);
      binding.btnDetailAction.setOnClickListener(v -> {
        dialog.dismiss();
        Intent targetIntent = new Intent();
        targetIntent.putExtra("extra_deep_link", deepLinkAction);
        handleIncomingDeepLink(targetIntent);
      });
    } else {
      binding.btnDetailAction.setVisibility(View.GONE);
    }

    dialog.show();
  }
}
