package com.devflux.deenone.features.prayer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;
import com.devflux.deenone.core.backend.PhpMysqlBackendService;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.prayer.ForbiddenTimesCalculator;
import com.devflux.deenone.core.prayer.QazaCalculatorManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.data.repository.MosqueRepository;
import com.devflux.deenone.databinding.BottomSheetForbiddenTimesBinding;
import com.devflux.deenone.databinding.BottomSheetMissedPrayersTrackerBinding;
import com.devflux.deenone.databinding.BottomSheetMonthlySalatReportBinding;
import com.devflux.deenone.databinding.BottomSheetQazaCalculatorBinding;
import com.devflux.deenone.databinding.DialogIslamicPickerModalBinding;
import com.devflux.deenone.databinding.ItemIslamicSelectionRowBinding;
import com.devflux.deenone.databinding.ItemNaflPrayerCardBinding;
import com.devflux.deenone.databinding.ItemSalahWaqtCardBinding;
import com.devflux.deenone.databinding.PageSalahTrackerBinding;
import com.devflux.deenone.features.mosque.model.MosqueItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.PrayerCalculator;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * SalahTrackerPageDialog — Comprehensive, Real-time Salat Tracker & Prayer Management Engine.
 * Features:
 *  1. Real-time dynamic waqt times, live 1-sec countdown & progress tracking.
 *  2. Time-gating: Future waqts cannot be marked in advance; past/current waqts can be marked.
 *  3. 4-way Status Selector per waqt (Jamat +20 XP, Ekaki +10 XP, Deri +5 XP, Qaza 0 XP).
 *  4. Dynamic Nafl prayers tracking (Tahajjud, Ishraq, Chasht, Awwabin with +15 XP each).
 *  5. Real-time Forbidden times with authentic Sahih Hadith dalil popup.
 *  6. Quick Actions: Missed prayers batch-marker, Lifetime Qaza calculator & 30-Day Analytics report.
 *  7. Full Room SQLite persistence & PHP MySQL backend sync.
 */
public class SalahTrackerPageDialog {

  private static final String TAG = "SalahTrackerDialog";

  public static void show(Context context) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    PageSalahTrackerBinding binding = PageSalahTrackerBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    // Header Back
    TouchAnimationUtil.attachTouchSpring(binding.btnBackSalahTracker);
    binding.btnBackSalahTracker.setOnClickListener(v -> dialog.dismiss());

    // Auto-calculate missed prayers & sync with cloud
    QazaCalculatorManager.autoCalculateMissedPrayers(context);
    QazaCalculatorManager.fetchQazaFromServer(context, null);

    // Bottom Navigation Tabs & Touch Feedback
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabHome);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabSalat);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabAmal);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabRank);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabCommunity);
    binding.tabHome.setOnClickListener(v -> dialog.dismiss());
    binding.tabSalat.setOnClickListener(v -> binding.scrollSalahContent.smoothScrollTo(0, 0));
    binding.tabAmal.setOnClickListener(v -> {
      dialog.dismiss();
      if (context instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) context).showAmalTrackerSheet();
      }
    });
    binding.tabRank.setOnClickListener(v -> {
      dialog.dismiss();
      if (context instanceof android.app.Activity) {
        com.devflux.deenone.features.leaderboard.LeaderboardRankPageDialog.show((android.app.Activity) context);
      }
    });
    binding.tabCommunity.setOnClickListener(v -> {
      dialog.dismiss();
      com.devflux.deenone.features.community.CommunityFeedDialog.show(context);
    });

    // Bilingual & Theme Status
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    boolean isDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(context) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;

    // Set Section Titles bilingually
    binding.pillSalahTrackerTitle.setText(isBn ? "• সালাত ট্র্যাকার" : "• Salat Tracker");
    binding.tvCurrentWaqtSubtitle.setText(isBn ? "বর্তমান ওয়াক্ত" : "Current Waqt");
    binding.tvCurrentWaqtRemainingSub.setText(isBn ? "ওয়াক্ত শেষ হতে বাকি" : "Time remaining in this waqt");
    binding.tvTodayScheduleTitle.setText(isBn ? "আজকের নামাজের সময়সূচি" : "Today's Prayer Schedule");
    binding.tvMasjidJamatModeTitle.setText(isBn ? "মসজিদ জামাত মোড" : "Masjid Jamat Mode");
    binding.tvChangeMasjidBtnText.setText(isBn ? "জামাতের জন্য মসজিদ পরিবর্তন করুন" : "Change Masjid for Jamat");
    binding.tvNaflSectionTitle.setText(isBn ? "নফল সালাত" : "Nafl Prayers");
    binding.tvForbiddenSectionTitle.setText(isBn ? "নামাজের নিষিদ্ধ সময়" : "Forbidden Prayer Times");
    binding.tvForbiddenDalilBtnText.setText(isBn ? "হুকুম ও দলিল" : "Rulings & Evidence");
    binding.tvMissedPrayerTitle.setText(isBn ? "ছুটে যাওয়া নামাজ" : "Missed Prayers");
    binding.tvMissedPrayerSubtitle.setText(isBn ? "আজকের না পড়া ওয়াক্ত কাজা হিসেবে মার্ক করুন" : "Mark unperformed waqts as qaza");
    binding.tvQazaTrackerTitle.setText(isBn ? "কাজা নামাজ" : "Qaza Prayers");
    binding.tvQazaTrackerSubtitle.setText(isBn ? "জীবনের কাজা নামাজের মোট হিসাব ও ক্যালকুলেটর" : "Lifetime qaza calculation & tracker");
    binding.tvMonthlyReportTitle.setText(isBn ? "মাসিক সালাত রিপোর্ট" : "Monthly Salat Report");
    binding.tvMonthlyReportSubtitle.setText(isBn ? "গত ৩০ দিনের সালাত পরিসংখ্যান ও চার্ট" : "30-day prayer statistics & charts");

    // Date Setup
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat dayMonthYearFmt = new SimpleDateFormat("dd MMMM, yyyy", isBn ? new Locale("bn", "BD") : Locale.ENGLISH);
    SimpleDateFormat isoDateFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    SimpleDateFormat slashDateFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    String todayIso = isoDateFmt.format(cal.getTime());
    String todayFormatted = isBn ? BengaliNumberUtil.toBengali(dayMonthYearFmt.format(cal.getTime())) : dayMonthYearFmt.format(cal.getTime());
    String todaySlash = isBn ? BengaliNumberUtil.toBengali(slashDateFmt.format(cal.getTime())) : slashDateFmt.format(cal.getTime());

    binding.tvSalahTrackerDate.setText(todayFormatted);
    binding.tvHeroDateFormatted.setText((isBn ? "তারিখ: " : "Date: ") + todaySlash);

    // Location & Prayer Time Calculation
    LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
    double lat = coords != null ? coords.latitude : 23.8103;
    double lng = coords != null ? coords.longitude : 90.4125;
    double timezone = coords != null ? coords.timezone : 6.0;

    PrayerCalculator.PrayerTimesResult pt = PrayerCalculator.calculateForLocationWithContext(
        context, lat, lng, timezone, cal);

    // Waqt Cards Wiring
    boolean isFriday = (pt != null && pt.isFriday) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);

    ItemSalahWaqtCardBinding fajrBinding = binding.cardFajr;
    ItemSalahWaqtCardBinding activeNoonBinding;
    if (isFriday) {
      // জুম্মার দিন যোহরের নামাজ হাইড হয়ে গিয়ে জুম্মার নামাজ শো করবে
      binding.cardDhuhr.getRoot().setVisibility(View.GONE);
      binding.cardJummah.getRoot().setVisibility(View.VISIBLE);
      activeNoonBinding = binding.cardJummah;
    } else {
      // বাকি দিনগুলোতে যোহরের নামাজ শো করবে
      binding.cardDhuhr.getRoot().setVisibility(View.VISIBLE);
      binding.cardJummah.getRoot().setVisibility(View.GONE);
      activeNoonBinding = binding.cardDhuhr;
    }
    ItemSalahWaqtCardBinding asrBinding = binding.cardAsr;
    ItemSalahWaqtCardBinding maghribBinding = binding.cardMaghrib;
    ItemSalahWaqtCardBinding ishaBinding = binding.cardIsha;

    // Waqt metadata helper maps
    Map<String, ItemSalahWaqtCardBinding> waqtBindingMap = new HashMap<>();
    waqtBindingMap.put("Fajr", fajrBinding);
    waqtBindingMap.put("Dhuhr", activeNoonBinding);
    waqtBindingMap.put("Asr", asrBinding);
    waqtBindingMap.put("Maghrib", maghribBinding);
    waqtBindingMap.put("Isha", ishaBinding);

    // Setup Waqt visual themes with 100% accurate circular artwork from user screenshot
    setupWaqtCardAppearance(fajrBinding, isBn ? "ফজর" : "Fajr", pt != null ? pt.fajrStr : "", R.drawable.ic_waqt_fajr, isBn);
    if (isFriday) {
      String jummahTime = (pt != null && pt.jummahStr != null && !pt.jummahStr.isEmpty()) ? pt.jummahStr : (pt != null ? pt.zohrStr : "");
      setupWaqtCardAppearance(activeNoonBinding, isBn ? "জুম'আ" : "Jummah", jummahTime, R.drawable.ic_waqt_jummah, isBn);
    } else {
      setupWaqtCardAppearance(activeNoonBinding, isBn ? "যোহর" : "Dhuhr", pt != null ? pt.zohrStr : "", R.drawable.ic_waqt_dhuhr, isBn);
    }
    setupWaqtCardAppearance(asrBinding, isBn ? "আসর" : "Asr", pt != null ? pt.asrStr : "", R.drawable.ic_waqt_asr, isBn);
    setupWaqtCardAppearance(maghribBinding, isBn ? "মাগরিব" : "Maghrib", pt != null ? pt.maghribStr : "", R.drawable.ic_waqt_maghrib, isBn);
    setupWaqtCardAppearance(ishaBinding, isBn ? "এশা" : "Isha", pt != null ? pt.ishaStr : "", R.drawable.ic_waqt_isha, isBn);

    // Real-time Countdown & Waqt Status Handler
    Handler timerHandler = new Handler(Looper.getMainLooper());
    Runnable timerRunnable = new Runnable() {
      @Override
      public void run() {
        long now = System.currentTimeMillis();
        updateRealtimeWaqtStatus(context, binding, pt, waqtBindingMap, now);
        timerHandler.postDelayed(this, 1000);
      }
    };
    timerHandler.post(timerRunnable);
    dialog.setOnDismissListener(d -> timerHandler.removeCallbacks(timerRunnable));

    // Load persisted prayer statuses from Room SQLite
    loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, binding);

    // Wire Status Click Handlers with Time-Gating Guard
    wireWaqtClickListeners(context, "Fajr", isBn ? "ফজর" : "Fajr", pt != null ? pt.fajrMillis : 0, pt != null ? pt.sunriseMillis : 0, fajrBinding, todayIso, binding, waqtBindingMap);
    wireWaqtClickListeners(context, "Dhuhr", isFriday ? (isBn ? "জুম'আ" : "Jummah") : (isBn ? "যোহর" : "Dhuhr"), pt != null ? pt.zohrMillis : 0, pt != null ? pt.asrMillis : 0, activeNoonBinding, todayIso, binding, waqtBindingMap);
    wireWaqtClickListeners(context, "Asr", isBn ? "আসর" : "Asr", pt != null ? pt.asrMillis : 0, pt != null ? pt.maghribMillis : 0, asrBinding, todayIso, binding, waqtBindingMap);
    wireWaqtClickListeners(context, "Maghrib", isBn ? "মাগরিব" : "Maghrib", pt != null ? pt.maghribMillis : 0, pt != null ? pt.ishaMillis : 0, maghribBinding, todayIso, binding, waqtBindingMap);
    wireWaqtClickListeners(context, "Isha", isBn ? "এশা" : "Isha", pt != null ? pt.ishaMillis : 0, (pt != null ? pt.fajrMillis : 0) + (24 * 3600 * 1000L), ishaBinding, todayIso, binding, waqtBindingMap);

    // Masjid Jamat Mode switch & Selected Mosque state
    SharedPreferences salahPrefs = context.getSharedPreferences("deanone_salah_prefs", Context.MODE_PRIVATE);
    boolean isJamatEnabled = salahPrefs.getBoolean("key_masjid_jamat_enabled", true);
    binding.switchMasjidJamat.setChecked(isJamatEnabled);
    binding.tvJamatModeBadge.setText(isJamatEnabled ? (isBn ? "সক্রিয়" : "Active") : (isBn ? "বন্ধ" : "Off"));
    binding.tvJamatModeBadge.setTextColor(Color.parseColor(isJamatEnabled ? (isDark ? "#34D399" : "#16A34A") : (isDark ? "#9CA3AF" : "#6B7280")));
    binding.tvJamatModeBadge.getBackground().setTint(Color.parseColor(isJamatEnabled ? (isDark ? "#0E3B27" : "#E6F5ED") : (isDark ? "#1F2937" : "#E5ECE8")));

    String savedMosqueName = salahPrefs.getString("key_selected_masjid_name", null);
    String savedMosqueDistance = salahPrefs.getString("key_selected_masjid_dist", "");
    if (savedMosqueName != null && !savedMosqueName.isEmpty()) {
      binding.tvSelectedMasjidName.setText(savedMosqueName + (!savedMosqueDistance.isEmpty() ? " • " + savedMosqueDistance : ""));
    } else {
      binding.tvSelectedMasjidName.setText(isBn ? "নিকটবর্তী মসজিদ নির্বাচন করতে ট্যাপ করুন" : "Tap to select nearby mosque");
      // Automatically resolve closest real mosque in background
      if (coords != null) {
        MosqueRepository.getInstance().fetchLiveNearbyMosquesAsync(
            coords.latitude, coords.longitude, "all", "", mosques -> {
              if (mosques != null && !mosques.isEmpty()) {
                MosqueItem closest = mosques.get(0);
                String dist = closest.getDistanceFormatted() != null ? closest.getDistanceFormatted() : "";
                binding.tvSelectedMasjidName.setText(closest.getName() + (!dist.isEmpty() ? " • " + dist : ""));
              }
            }
        );
      }
    }

    binding.switchMasjidJamat.setOnCheckedChangeListener((btn, isChecked) -> {
      salahPrefs.edit().putBoolean("key_masjid_jamat_enabled", isChecked).apply();
      binding.tvJamatModeBadge.setText(isChecked ? (isBn ? "সক্রিয়" : "Active") : (isBn ? "বন্ধ" : "Off"));
      binding.tvJamatModeBadge.setTextColor(Color.parseColor(isChecked ? (isDark ? "#34D399" : "#16A34A") : (isDark ? "#9CA3AF" : "#6B7280")));
      binding.tvJamatModeBadge.getBackground().setTint(Color.parseColor(isChecked ? (isDark ? "#0E3B27" : "#E6F5ED") : (isDark ? "#1F2937" : "#E5ECE8")));
    });

    TouchAnimationUtil.attachTouchSpring(binding.btnChangeMasjid);
    binding.btnChangeMasjid.setOnClickListener(v -> {
      showMosqueSelectionDialog(context, binding);
    });

    // 2. Setup Nafl Prayers
    setupNaflPrayers(context, binding, pt, todayIso);

    // 3. Setup Forbidden Prayer Times
    setupForbiddenTimes(context, binding, pt);

    // 4. Setup Management & Analytics Cards
    setupActionCards(context, binding, todayIso, waqtBindingMap, pt);

    dialog.show();
  }

  private static void setupWaqtCardAppearance(ItemSalahWaqtCardBinding wb, String waqtName, String timeStr, int iconRes, boolean isBn) {
    wb.tvWaqtName.setText(waqtName);
    wb.tvWaqtTimePill.setText((isBn ? "ওয়াক্ত: " : "Waqt: ") + (isBn ? BengaliNumberUtil.toBengali(timeStr) : timeStr));
    wb.tvJamatTimePill.setText(isBn ? "ⓘ জামাত সময় অপ্রাপ্ত" : "ⓘ Jamat time unavailable");
    wb.tvStatusHeaderLabel.setText(isBn ? "স্ট্যাটাস" : "Status");
    wb.tvStatusJamatText.setText(isBn ? "জামাআত\n+২০" : "Jamat\n+20");
    wb.tvStatusEkakiText.setText(isBn ? "একাকী\n+১২" : "Alone\n+12");
    wb.tvStatusDeriText.setText(isBn ? "দেরী\n+১০" : "Delayed\n+10");
    wb.tvStatusQazaText.setText(isBn ? "কাযা\n-২০" : "Qaza\n-20");
    wb.ivWaqtIcon.setImageResource(iconRes);
    wb.ivWaqtIcon.setColorFilter(null);
    wb.ivWaqtIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    if (wb.flWaqtIconBox.getBackground() != null) {
      wb.flWaqtIconBox.getBackground().setTint(Color.TRANSPARENT);
    }
  }

  private static void updateRealtimeWaqtStatus(Context context, PageSalahTrackerBinding binding,
                        PrayerCalculator.PrayerTimesResult pt,
                        Map<String, ItemSalahWaqtCardBinding> map,
                        long now) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    // Determine current Waqt & remaining seconds
    boolean isFriday = (pt != null && pt.isFriday) || (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);
    String currentWaqtName = isBn ? (isFriday ? "জুম'আ" : "যোহর") : (isFriday ? "Jummah" : "Dhuhr");
    long currentWaqtEndMillis = pt != null ? pt.asrMillis : 0;

    if (pt != null) {
      if (now >= pt.fajrMillis && now < pt.sunriseMillis) {
        currentWaqtName = isBn ? "ফজর" : "Fajr";
        currentWaqtEndMillis = pt.sunriseMillis;
      } else if (now >= pt.zohrMillis && now < pt.asrMillis) {
        currentWaqtName = isBn ? (isFriday ? "জুম'আ" : "যোহর") : (isFriday ? "Jummah" : "Dhuhr");
        currentWaqtEndMillis = pt.asrMillis;
      } else if (now >= pt.asrMillis && now < pt.maghribMillis) {
        currentWaqtName = isBn ? "আসর" : "Asr";
        currentWaqtEndMillis = pt.maghribMillis;
      } else if (now >= pt.maghribMillis && now < pt.ishaMillis) {
        currentWaqtName = isBn ? "মাগরিব" : "Maghrib";
        currentWaqtEndMillis = pt.ishaMillis;
      } else if (now >= pt.ishaMillis || now < pt.fajrMillis) {
        currentWaqtName = isBn ? "এশা" : "Isha";
        currentWaqtEndMillis = now < pt.fajrMillis ? pt.fajrMillis : (pt.fajrMillis + 24 * 3600 * 1000L);
      }
    }

    binding.tvCurrentWaqtTitle.setText(isBn ? (currentWaqtName + " ওয়াক্ত") : (currentWaqtName + " Waqt"));

    long diffSeconds = Math.max(0, (currentWaqtEndMillis - now) / 1000);
    long hours = diffSeconds / 3600;
    long mins = (diffSeconds % 3600) / 60;
    long secs = diffSeconds % 60;

    String countdownText;
    if (diffSeconds <= 0) {
      countdownText = isBn ? "ওয়াক্ত সমাপ্ত" : "Waqt Ended";
    } else if (hours > 0) {
      if (isBn) {
        countdownText = BengaliNumberUtil.toBengali(String.format(Locale.US, "%d ঘণ্টা %d মিনিট %d সেকেন্ড", hours, mins, secs));
      } else {
        countdownText = String.format(Locale.US, "%dh %dm %ds", hours, mins, secs);
      }
    } else if (mins > 0) {
      if (isBn) {
        countdownText = BengaliNumberUtil.toBengali(String.format(Locale.US, "%d মিনিট %d সেকেন্ড", mins, secs));
      } else {
        countdownText = String.format(Locale.US, "%dm %ds", mins, secs);
      }
    } else {
      if (isBn) {
        countdownText = BengaliNumberUtil.toBengali(String.format(Locale.US, "%d সেকেন্ড", secs));
      } else {
        countdownText = String.format(Locale.US, "%ds", secs);
      }
    }
    binding.tvCurrentWaqtCountdown.setText(countdownText);

    // Update badges for each waqt
    updateBadge(map.get("Fajr"), pt.fajrMillis, pt.sunriseMillis, now, isBn);
    updateBadge(map.get("Dhuhr"), pt.zohrMillis, pt.asrMillis, now, isBn);
    updateBadge(map.get("Asr"), pt.asrMillis, pt.maghribMillis, now, isBn);
    updateBadge(map.get("Maghrib"), pt.maghribMillis, pt.ishaMillis, now, isBn);
    updateBadge(map.get("Isha"), pt.ishaMillis, pt.fajrMillis + 24 * 3600 * 1000L, now, isBn);
  }

  private static void updateBadge(ItemSalahWaqtCardBinding wb, long startMillis, long endMillis, long now, boolean isBn) {
    if (wb == null) return;

    // If this waqt is already marked with a status, do not overwrite the marked status badge
    Object tag = wb.getRoot().getTag();
    if (tag instanceof String && !((String) tag).isEmpty()) {
      return;
    }

    float density = wb.getRoot().getResources().getDisplayMetrics().density;
    Context context = wb.getRoot().getContext();
    boolean isDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(context) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;

    if (now >= startMillis && now < endMillis) {
      wb.tvWaqtBadge.setText(isBn ? "বর্তমান ওয়াক্ত" : "Current Waqt");
      wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#34D399" : "#16A34A"));
      wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#0E3B27" : "#E6F5ED"));
      wb.cardWaqtContainer.setStrokeColor(Color.parseColor(isDark ? "#34D399" : "#16A34A"));
      wb.cardWaqtContainer.setStrokeWidth((int) (1.5f * density));
      wb.cardWaqtContainer.setCardElevation(0);
    } else if (now >= endMillis) {
      wb.tvWaqtBadge.setText(isBn ? "পূর্বের ওয়াক্ত" : "Past Waqt");
      wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#C084FC" : "#9333EA"));
      wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#291238" : "#F3E8FF"));
      wb.cardWaqtContainer.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
      wb.cardWaqtContainer.setStrokeWidth((int) (1.0f * density));
      wb.cardWaqtContainer.setCardElevation(0);
    } else {
      wb.cardWaqtContainer.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
      wb.cardWaqtContainer.setStrokeWidth((int) (1.0f * density));
      wb.cardWaqtContainer.setCardElevation(0);
      // Check if it's the very next waqt
      if (now < startMillis && (startMillis - now) <= 4 * 3600 * 1000L) {
        wb.tvWaqtBadge.setText(isBn ? "পরবর্তী ওয়াক্ত" : "Next Waqt");
        wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#FBBF24" : "#D97706"));
        wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#38230E" : "#FEF3C7"));
      } else {
        wb.tvWaqtBadge.setText(isBn ? "অপেক্ষমাণ" : "Upcoming");
        wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#94A3B8" : "#64748B"));
        wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#152D32" : "#F1F5F9"));
      }
    }
  }

  private static void wireWaqtClickListeners(Context context, String prayerKey, String prayerBn,
                        long startMillis, long endMillis,
                        ItemSalahWaqtCardBinding wb,
                        String todayIso,
                        PageSalahTrackerBinding rootBinding,
                        Map<String, ItemSalahWaqtCardBinding> allBindings) {
    TouchAnimationUtil.attachTouchSpring(wb.btnStatusJamat);
    TouchAnimationUtil.attachTouchSpring(wb.btnStatusEkaki);
    TouchAnimationUtil.attachTouchSpring(wb.btnStatusDeri);
    TouchAnimationUtil.attachTouchSpring(wb.btnStatusQaza);

    wb.btnStatusJamat.setOnClickListener(v -> handleStatusSelection(context, prayerKey, prayerBn, "JAMAAT", startMillis, wb, todayIso, rootBinding, allBindings));
    wb.btnStatusEkaki.setOnClickListener(v -> handleStatusSelection(context, prayerKey, prayerBn, "EKAKI", startMillis, wb, todayIso, rootBinding, allBindings));
    wb.btnStatusDeri.setOnClickListener(v -> handleStatusSelection(context, prayerKey, prayerBn, "DERI", startMillis, wb, todayIso, rootBinding, allBindings));
    wb.btnStatusQaza.setOnClickListener(v -> handleStatusSelection(context, prayerKey, prayerBn, "QAZA", startMillis, wb, todayIso, rootBinding, allBindings));
  }

  private static void handleStatusSelection(Context context, String prayerKey, String prayerBn,
                       String status, long startMillis,
                       ItemSalahWaqtCardBinding wb,
                       String todayIso,
                       PageSalahTrackerBinding rootBinding,
                       Map<String, ItemSalahWaqtCardBinding> allBindings) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    long now = System.currentTimeMillis();

    // 1. Time-Gating Guard: Cannot mark future waqts in advance!
    if (now < startMillis) {
      String waitMsg = isBn ? (prayerBn + " ওয়াক্তের সময় এখনও শুরু হয়নি। অগ্রিম সালাত ট্র্যাক করা যাবে না।")
                            : (prayerKey + " waqt has not started yet. Advance logging is not permitted.");
      Toast.makeText(context, waitMsg, Toast.LENGTH_SHORT).show();
      return;
    }

    AppDatabase.databaseWriteExecutor.execute(() -> {
      AppDatabase db = AppDatabase.getInstance(context);
      PrayerLogEntity existing = db.prayerLogDao().getLog(todayIso, prayerKey);

      // 2. Lock Guard: Once selected, status cannot be changed or switched again!
      if (existing != null && existing.getStatus() != null && !existing.getStatus().isEmpty()) {
        String prevStatus = existing.getStatus();
        String prevLabel = prevStatus.equals("JAMAAT") ? (isBn ? "জামাত" : "Jamat") : (prevStatus.equals("EKAKI") ? (isBn ? "একাকী" : "Alone") : (prevStatus.equals("DERI") ? (isBn ? "দেরী" : "Late") : (isBn ? "কাযা" : "Qaza")));
        new Handler(Looper.getMainLooper()).post(() -> {
          Toast.makeText(context, (isBn ? (prayerBn + " সালাত ইতিমধ্যে '" + prevLabel + "' হিসেবে সংরক্ষিত হয়েছে — এটি পরিবর্তনযোগ্য নয়") : (prayerKey + " prayer already logged as '" + prevLabel + "' — cannot be changed")), Toast.LENGTH_SHORT).show();
        });
        return;
      }

      // 3. Determine points & status details
      int points = 0;
      String statusLabel = "";
      switch (status) {
        case "JAMAAT":
          points = 20; // 100% Base points (+20)
          statusLabel = isBn ? "জামাতে আদায়: +২০ পয়েন্ট" : "Prayed in Jamat: +20 XP";
          break;
        case "EKAKI":
          points = 12; // 60% of base points (60% of 20 = +12)
          statusLabel = isBn ? "একাকী আদায়: +১২ পয়েন্ট" : "Prayed Alone (+12 XP)";
          break;
        case "DERI":
          points = 10; // Delayed prayer (+10)
          statusLabel = isBn ? "দেরীতে আদায়: +১০ পয়েন্ট" : "Prayed Late (+10 XP)";
          break;
        case "QAZA":
          points = -20; // Qaza Missed penalty (-20 points deduction)
          statusLabel = isBn ? "কাযা হিসেবে চিহ্নিত: -২০ পয়েন্ট" : "Marked as Qaza: -20 XP";
          break;
      }

      final int finalPoints = points;
      final String finalLabel = statusLabel;

      PrayerLogEntity existingLog = db.prayerLogDao().getLog(todayIso, prayerKey);
      boolean wasQaza = (existingLog != null && ("QAZA".equalsIgnoreCase(existingLog.getStatus()) || existingLog.isQaza()));

      boolean isPrayed = !status.equals("QAZA");
      boolean isJamat = status.equals("JAMAAT");
      boolean isQaza = status.equals("QAZA");

      PrayerLogEntity log = new PrayerLogEntity(todayIso, prayerKey, isPrayed, isJamat, isQaza, status, finalPoints, System.currentTimeMillis());
      db.prayerLogDao().insertOrUpdate(log);

      try {
        com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context)
            .syncPrayerLog(prayerKey, todayIso, isPrayed, isJamat, isQaza, status, finalPoints);
      } catch (Exception ignored) {}

      String qKey = QazaCalculatorManager.waqtNameToKey(prayerKey);
      if (qKey != null) {
        if (isQaza && !wasQaza) {
          QazaCalculatorManager.incrementQaza(context, qKey);
          QazaCalculatorManager.markWaqtEvaluated(context, todayIso, prayerKey);
        } else if (!isQaza && wasQaza) {
          QazaCalculatorManager.decrementQaza(context, qKey);
          QazaCalculatorManager.markWaqtEvaluated(context, todayIso, prayerKey);
        } else if (isPrayed) {
          QazaCalculatorManager.markWaqtEvaluated(context, todayIso, prayerKey);
        }
      }

      GamificationManager.addXP(context, finalPoints);

      // Sync with central AmalRepository for dashboard consistency
      new com.devflux.deenone.data.repository.AmalRepository(context).recalculateAndSyncTodayRecord();

      int rawPrayed = db.prayerLogDao().getPrayedCountForDateSync(todayIso);
      final int prayedCount = Math.min(5, Math.max(0, rawPrayed)); // Strictly capped at 5/5

      new Handler(Looper.getMainLooper()).post(() -> {
        applySelectedStatusVisual(wb, status);
        rootBinding.tvPrayedProgressCount.setText(isBn ? ("আদায় সম্পন্ন: " + BengaliNumberUtil.toBengali(prayedCount + "/৫")) : ("Prayed: " + prayedCount + "/5"));
        rootBinding.pbPrayedCircular.setMax(5);
        rootBinding.pbPrayedCircular.setProgress(prayedCount);
        Toast.makeText(context, (isBn ? prayerBn : prayerKey) + ": " + finalLabel, Toast.LENGTH_SHORT).show();
      });
    });
  }

  private static void applySelectedStatusVisual(ItemSalahWaqtCardBinding wb, String status) {
    // Tag card with marked status
    wb.getRoot().setTag(status);
    Context context = wb.getRoot().getContext();
    boolean isDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(context) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // Reset all 4 buttons to unselected theme colors
    resetButtonVisual(context, wb.btnStatusJamat, wb.ivStatusJamatIcon, wb.tvStatusJamatText);
    resetButtonVisual(context, wb.btnStatusEkaki, wb.ivStatusEkakiIcon, wb.tvStatusEkakiText);
    resetButtonVisual(context, wb.btnStatusDeri, wb.ivStatusDeriIcon, wb.tvStatusDeriText);
    resetButtonVisual(context, wb.btnStatusQaza, wb.ivStatusQazaIcon, wb.tvStatusQazaText);

    // Highlight selected button & update card border & badge
    switch (status) {
      case "JAMAAT":
        // Green for Jamat
        highlightButtonVisual(wb.btnStatusJamat, wb.ivStatusJamatIcon, wb.tvStatusJamatText,
            "#16A34A", isDark ? "#0E382C" : "#E6F5ED", isDark ? "#34D399" : "#16A34A");
        wb.cardWaqtContainer.setStrokeColor(Color.parseColor("#16A34A"));
        wb.tvWaqtBadge.setText(isBn ? "জামাতে আদায় সম্পন্ন" : "Prayed in Jamat");
        wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#34D399" : "#16A34A"));
        wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#0E3B27" : "#E6F5ED"));
        break;
      case "EKAKI":
        // Blue for Ekaki
        highlightButtonVisual(wb.btnStatusEkaki, wb.ivStatusEkakiIcon, wb.tvStatusEkakiText,
            "#0284C7", isDark ? "#0B2538" : "#E0F2FE", isDark ? "#38BDF8" : "#0284C7");
        wb.cardWaqtContainer.setStrokeColor(Color.parseColor("#0284C7"));
        wb.tvWaqtBadge.setText(isBn ? "একাকী আদায় সম্পন্ন" : "Prayed Alone");
        wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#38BDF8" : "#0284C7"));
        wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#0B2538" : "#E0F2FE"));
        break;
      case "DERI":
        // Yellow / Amber for Deri
        highlightButtonVisual(wb.btnStatusDeri, wb.ivStatusDeriIcon, wb.tvStatusDeriText,
            "#D97706", isDark ? "#38230E" : "#FEF3C7", isDark ? "#FBBF24" : "#D97706");
        wb.cardWaqtContainer.setStrokeColor(Color.parseColor("#D97706"));
        wb.tvWaqtBadge.setText(isBn ? "দেরীতে আদায় সম্পন্ন" : "Prayed Late");
        wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#FBBF24" : "#D97706"));
        wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#38230E" : "#FEF3C7"));
        break;
      case "QAZA":
        // Red for Qaza
        highlightButtonVisual(wb.btnStatusQaza, wb.ivStatusQazaIcon, wb.tvStatusQazaText,
            "#DC2626", isDark ? "#3C1215" : "#FEE2E2", isDark ? "#F87171" : "#DC2626");
        wb.cardWaqtContainer.setStrokeColor(Color.parseColor("#DC2626"));
        wb.tvWaqtBadge.setText(isBn ? "কাযা হয়েছে" : "Qaza");
        wb.tvWaqtBadge.setTextColor(Color.parseColor(isDark ? "#F87171" : "#DC2626"));
        wb.tvWaqtBadge.getBackground().setTint(Color.parseColor(isDark ? "#3C1215" : "#FEE2E2"));
        break;
    }
  }

  private static void resetButtonVisual(Context context, MaterialCardView card, ImageView icon, TextView text) {
    card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.bg_card_secondary));
    card.setStrokeColor(ContextCompat.getColor(context, R.color.border_card));
    card.setStrokeWidth(1);
    card.setCardElevation(0);
    icon.setColorFilter(ContextCompat.getColor(context, R.color.text_secondary));
    text.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
  }

  private static void highlightButtonVisual(MaterialCardView card, ImageView icon, TextView text,
                       String strokeHex, String bgHex, String contentHex) {
    card.setCardBackgroundColor(Color.parseColor(bgHex));
    card.setStrokeColor(Color.parseColor(strokeHex));
    card.setStrokeWidth(2);
    card.setCardElevation(0);
    icon.setColorFilter(Color.parseColor(contentHex));
    text.setTextColor(Color.parseColor(contentHex));
  }

  private static void loadTodayPrayerStatuses(Context context, String todayIso,
                        Map<String, ItemSalahWaqtCardBinding> map,
                        PageSalahTrackerBinding rootBinding) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      AppDatabase db = AppDatabase.getInstance(context);
      List<PrayerLogEntity> logs = db.prayerLogDao().getPrayerLogsForDateSync(todayIso);
      int rawPrayed = db.prayerLogDao().getPrayedCountForDateSync(todayIso);
      final int prayedCount = Math.min(5, Math.max(0, rawPrayed)); // Strictly capped at 5/5

      new Handler(Looper.getMainLooper()).post(() -> {
        boolean isBnLocale = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        rootBinding.tvPrayedProgressCount.setText(isBnLocale ? ("আদায় সম্পন্ন: " + BengaliNumberUtil.toBengali(prayedCount + "/৫")) : ("Prayed: " + prayedCount + "/5"));
        rootBinding.pbPrayedCircular.setMax(5);
        rootBinding.pbPrayedCircular.setProgress(prayedCount);

        if (logs != null) {
          for (PrayerLogEntity l : logs) {
            ItemSalahWaqtCardBinding wb = map.get(l.getPrayerName());
            if (wb != null && l.getStatus() != null) {
              applySelectedStatusVisual(wb, l.getStatus());
            }
          }
        }
      });
    });
  }

  private static void setupNaflPrayers(Context context, PageSalahTrackerBinding binding,
                    PrayerCalculator.PrayerTimesResult pt, String todayIso) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

    // 1. Tahajjud (রাতের অত্যন্ত ফজিলতপূর্ণ নফল সালাত - +১৫ পয়েন্ট)
    ItemNaflPrayerCardBinding tahajjudBinding = ItemNaflPrayerCardBinding.bind(binding.cardNaflTahajjud.getRoot());
    tahajjudBinding.tvNaflTitle.setText(isBn ? "তাহাজ্জুদ" : "Tahajjud");
    tahajjudBinding.ivNaflIcon.setImageResource(R.drawable.ic_nafl_tahajjud);
    tahajjudBinding.ivNaflIcon.setColorFilter(null);
    tahajjudBinding.ivNaflIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    if (tahajjudBinding.flNaflIconBox.getBackground() != null) {
      tahajjudBinding.flNaflIconBox.getBackground().setTint(Color.TRANSPARENT);
    }
    String tahajjudRange = "11:30 PM - " + (pt != null && pt.fajrStr != null ? pt.fajrStr : "04:26 AM");
    tahajjudBinding.tvNaflTimeRange.setText(isBn ? BengaliNumberUtil.toBengali(tahajjudRange) : tahajjudRange);
    TouchAnimationUtil.attachTouchSpring(tahajjudBinding.btnTrackNafl);
    wireNaflButton(context, tahajjudBinding, "Tahajjud", isBn ? "তাহাজ্জুদ" : "Tahajjud", 15, todayIso, pt);

    // 2. Ishraq (সূর্যোদয়ের পরের সুন্নাত/নফল - +১০ পয়েন্ট)
    ItemNaflPrayerCardBinding ishraqBinding = ItemNaflPrayerCardBinding.bind(binding.cardNaflIshraq.getRoot());
    ishraqBinding.tvNaflTitle.setText(isBn ? "ইশরাক" : "Ishraq");
    ishraqBinding.ivNaflIcon.setImageResource(R.drawable.ic_nafl_ishraq);
    ishraqBinding.ivNaflIcon.setColorFilter(null);
    ishraqBinding.ivNaflIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    if (ishraqBinding.flNaflIconBox.getBackground() != null) {
      ishraqBinding.flNaflIconBox.getBackground().setTint(Color.TRANSPARENT);
    }
    long ishraqStart = (pt != null ? pt.sunriseMillis : 0) + (15 * 60 * 1000L);
    long ishraqEnd = (pt != null ? pt.sunriseMillis : 0) + (55 * 60 * 1000L);
    String ishraqRange = timeFmt.format(new Date(ishraqStart)) + " - " + timeFmt.format(new Date(ishraqEnd));
    ishraqBinding.tvNaflTimeRange.setText(isBn ? BengaliNumberUtil.toBengali(ishraqRange) : ishraqRange);
    TouchAnimationUtil.attachTouchSpring(ishraqBinding.btnTrackNafl);
    wireNaflButton(context, ishraqBinding, "Ishraq", isBn ? "ইশরাক" : "Ishraq", 10, todayIso, pt);

    // 3. Chasht (Duha) (দ্বিপ্রহরের পূর্বের চাশত সালাত - +১০ পয়েন্ট)
    ItemNaflPrayerCardBinding chashtBinding = ItemNaflPrayerCardBinding.bind(binding.cardNaflChasht.getRoot());
    chashtBinding.tvNaflTitle.setText(isBn ? "চাশত (যুহা)" : "Chasht (Duha)");
    chashtBinding.ivNaflIcon.setImageResource(R.drawable.ic_nafl_chasht);
    chashtBinding.ivNaflIcon.setColorFilter(null);
    chashtBinding.ivNaflIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    if (chashtBinding.flNaflIconBox.getBackground() != null) {
      chashtBinding.flNaflIconBox.getBackground().setTint(Color.TRANSPARENT);
    }
    long chashtStart = (pt != null ? pt.sunriseMillis : 0) + (55 * 60 * 1000L);
    long chashtEnd = (pt != null ? pt.zohrMillis : 0) - (15 * 60 * 1000L);
    String chashtRange = timeFmt.format(new Date(chashtStart)) + " - " + timeFmt.format(new Date(chashtEnd));
    chashtBinding.tvNaflTimeRange.setText(isBn ? BengaliNumberUtil.toBengali(chashtRange) : chashtRange);
    TouchAnimationUtil.attachTouchSpring(chashtBinding.btnTrackNafl);
    wireNaflButton(context, chashtBinding, "Chasht", isBn ? "চাশত (যুহা)" : "Chasht (Duha)", 10, todayIso, pt);

    // 4. Awwabin (মাগরিবের পরবর্তী ৬ রাকাত সালাত - +১০ পয়েন্ট)
    ItemNaflPrayerCardBinding awwabinBinding = ItemNaflPrayerCardBinding.bind(binding.cardNaflAwwabin.getRoot());
    awwabinBinding.tvNaflTitle.setText(isBn ? "আউওয়াবিন" : "Awwabin");
    awwabinBinding.ivNaflIcon.setImageResource(R.drawable.ic_nafl_awwabin);
    awwabinBinding.ivNaflIcon.setColorFilter(null);
    awwabinBinding.ivNaflIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
    if (awwabinBinding.flNaflIconBox.getBackground() != null) {
      awwabinBinding.flNaflIconBox.getBackground().setTint(Color.TRANSPARENT);
    }
    long awwabinStart = (pt != null ? pt.maghribMillis : 0) + (10 * 60 * 1000L);
    long awwabinEnd = (pt != null ? pt.ishaMillis : 0) - (5 * 60 * 1000L);
    String awwabinRange = timeFmt.format(new Date(awwabinStart)) + " - " + timeFmt.format(new Date(awwabinEnd));
    awwabinBinding.tvNaflTimeRange.setText(isBn ? BengaliNumberUtil.toBengali(awwabinRange) : awwabinRange);
    TouchAnimationUtil.attachTouchSpring(awwabinBinding.btnTrackNafl);
    wireNaflButton(context, awwabinBinding, "Awwabin", isBn ? "আউওয়াবিন" : "Awwabin", 10, todayIso, pt);
  }

  private static boolean isNaflTimeStarted(long now, String naflKey, PrayerCalculator.PrayerTimesResult pt) {
    if (pt == null) return true;
    switch (naflKey) {
      case "Tahajjud":
        // Nighttime: before Fajr or after Isha/night
        return (now < pt.fajrMillis || now >= pt.ishaMillis);
      case "Ishraq":
        return (now >= (pt.sunriseMillis + 15 * 60 * 1000L));
      case "Chasht":
        return (now >= (pt.sunriseMillis + 55 * 60 * 1000L));
      case "Awwabin":
        return (now >= (pt.maghribMillis + 10 * 60 * 1000L));
      default:
        return true;
    }
  }

  private static void applyNaflButtonStyle(Context context, ItemNaflPrayerCardBinding nb,
                                           boolean isTracked, boolean timeStarted,
                                           int bonusPoints, boolean isBn, boolean isDark) {
    if (context == null || nb == null || nb.btnTrackNafl == null) return;

    com.google.android.material.button.MaterialButton btn = nb.btnTrackNafl;

    // 1. Zero elevation & clear state list animator to completely eliminate dirty gray shadow in Light Mode
    btn.setElevation(0f);
    btn.setStateListAnimator(null);

    // 2. Uniform Dimensions: Exactly 104dp width & 36dp height
    float density = context.getResources().getDisplayMetrics().density;
    int widthPx = (int) (104 * density + 0.5f);
    int heightPx = (int) (36 * density + 0.5f);
    int strokeWidthPx = (int) (1.2f * density + 0.5f);
    int cornerRadiusPx = (int) (18 * density + 0.5f);

    ViewGroup.LayoutParams lp = btn.getLayoutParams();
    if (lp != null) {
      if (lp.width != widthPx || lp.height != heightPx) {
        lp.width = widthPx;
        lp.height = heightPx;
        btn.setLayoutParams(lp);
      }
    }
    btn.setInsetTop(0);
    btn.setInsetBottom(0);
    btn.setPadding(0, 0, 0, 0);
    btn.setGravity(Gravity.CENTER);
    btn.setCornerRadius(cornerRadiusPx);

    if (isTracked) {
      // State 3: Prayed / Tracked ("আদায় (+১০)")
      btn.setText(isBn ? ("আদায় (+" + BengaliNumberUtil.toBengali(bonusPoints) + ")")
                       : ("Prayed (+" + bonusPoints + ")"));
      int bgColor = Color.parseColor(isDark ? "#0E382C" : "#E6F5ED");
      int strokeColor = Color.parseColor(isDark ? "#34D399" : "#16A34A");
      int textColor = Color.parseColor(isDark ? "#34D399" : "#16A34A");

      btn.setBackgroundTintList(ColorStateList.valueOf(bgColor));
      btn.setStrokeColor(ColorStateList.valueOf(strokeColor));
      btn.setStrokeWidth(strokeWidthPx);
      btn.setTextColor(textColor);
    } else if (!timeStarted) {
      // State 1: Upcoming / Waiting ("অপেক্ষমাণ")
      btn.setText(isBn ? "অপেক্ষমাণ" : "Upcoming");
      int bgColor = Color.parseColor(isDark ? "#0E2B23" : "#F1F5F3");
      int strokeColor = Color.parseColor(isDark ? "#144A3C" : "#D8E3DC");
      int textColor = Color.parseColor(isDark ? "#A8B7B2" : "#475569");

      btn.setBackgroundTintList(ColorStateList.valueOf(bgColor));
      btn.setStrokeColor(ColorStateList.valueOf(strokeColor));
      btn.setStrokeWidth(strokeWidthPx);
      btn.setTextColor(textColor);
    } else {
      // State 2: Time Active / Ready to Track ("ট্র্যাক (+১০)")
      btn.setText(isBn ? ("ট্র্যাক (+" + BengaliNumberUtil.toBengali(bonusPoints) + ")")
                       : ("Track (+" + bonusPoints + ")"));
      int bgColor = Color.parseColor(isDark ? "#0B2B20" : "#E6F5ED");
      int strokeColor = Color.parseColor(isDark ? "#34D399" : "#16A34A");
      int textColor = Color.parseColor(isDark ? "#34D399" : "#16A34A");

      btn.setBackgroundTintList(ColorStateList.valueOf(bgColor));
      btn.setStrokeColor(ColorStateList.valueOf(strokeColor));
      btn.setStrokeWidth(strokeWidthPx);
      btn.setTextColor(textColor);
    }

    TouchAnimationUtil.attachTouchSpring(btn);
  }

  private static void wireNaflButton(Context context, ItemNaflPrayerCardBinding nb,
                    String naflKey, String naflBn, int bonusPoints, String todayIso,
                    PrayerCalculator.PrayerTimesResult pt) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    boolean isDark = com.devflux.deenone.core.theme.ThemeManager.getSavedThemeMode(context) == com.devflux.deenone.core.theme.ThemeManager.THEME_DARK;
    long now = System.currentTimeMillis();
    boolean timeStarted = isNaflTimeStarted(now, naflKey, pt);

    // Apply clean initial styling right away
    applyNaflButtonStyle(context, nb, false, timeStarted, bonusPoints, isBn, isDark);

    // Check persisted state from Room SQLite
    AppDatabase.databaseWriteExecutor.execute(() -> {
      AppDatabase db = AppDatabase.getInstance(context);
      PrayerLogEntity log = db.prayerLogDao().getLog(todayIso, naflKey);
      boolean isTracked = (log != null && log.isPrayed());
      new Handler(Looper.getMainLooper()).post(() -> {
        applyNaflButtonStyle(context, nb, isTracked, timeStarted, bonusPoints, isBn, isDark);
      });
    });

    nb.btnTrackNafl.setOnClickListener(v -> {
      long currentNow = System.currentTimeMillis();
      if (!isNaflTimeStarted(currentNow, naflKey, pt)) {
        String msg;
        switch (naflKey) {
          case "Tahajjud":
            msg = isBn ? "তাহাজ্জুদ সালাতের সময় শুরু হওয়ার পূর্বে আদায় মার্ক করা যাবে না (ওয়াক্ত: এশার পর রাত থেকে ফজর পর্যন্ত)"
                       : "Cannot log Tahajjud before its time (window: after Isha until Fajr)";
            break;
          case "Ishraq":
            msg = isBn ? "ইশরাক সালাতের সময় শুরু হওয়ার পূর্বে আদায় মার্ক করা যাবে না (সময়: সূর্যোদয়ের ১৫ মিনিট পর থেকে)"
                       : "Cannot log Ishraq before its time (window: 15 mins after sunrise)";
            break;
          case "Chasht":
            msg = isBn ? "চাশত (যুহা) সালাতের সময় শুরু হওয়ার পূর্বে আদায় মার্ক করা যাবে না (সময়: সকাল থেকে দ্বিপ্রহরের পূর্ব পর্যন্ত)"
                       : "Cannot log Chasht before its time (window: mid-morning to zawal)";
            break;
          case "Awwabin":
            msg = isBn ? "আউওয়াবিন সালাতের সময় শুরু হওয়ার পূর্বে আদায় মার্ক করা যাবে না (সময়: মাগরিবের পর থেকে এশা পর্যন্ত)"
                       : "Cannot log Awwabin before its time (window: after Maghrib until Isha)";
            break;
          default:
            msg = isBn ? (naflBn + " সালাতের সময় শুরু হওয়ার পূর্বে আদায় মার্ক করা যাবে না")
                       : ("Cannot log " + naflKey + " before its time window");
            break;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        return;
      }

      AppDatabase.databaseWriteExecutor.execute(() -> {
        AppDatabase db = AppDatabase.getInstance(context);
        PrayerLogEntity existing = db.prayerLogDao().getLog(todayIso, naflKey);
        if (existing != null && existing.isPrayed()) {
          // Lock: Once tracked, it cannot be duplicated or spammed
          new Handler(Looper.getMainLooper()).post(() -> {
            Toast.makeText(context, isBn ? (naflBn + " সালাত ইতিমধ্যে আদায় সম্পন্ন ও সংরক্ষিত হয়েছে") : (naflKey + " already tracked for today"), Toast.LENGTH_SHORT).show();
          });
        } else {
          PrayerLogEntity log = new PrayerLogEntity(todayIso, naflKey, true, false, false, "NAFL", bonusPoints, System.currentTimeMillis());
          db.prayerLogDao().insertOrUpdate(log);
          try {
            com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context)
                .syncPrayerLog(naflKey, todayIso, true, false, false, "NAFL", bonusPoints);
          } catch (Exception ignored) {}
          GamificationManager.addXP(context, bonusPoints);
          new Handler(Looper.getMainLooper()).post(() -> {
            applyNaflButtonStyle(context, nb, true, true, bonusPoints, isBn, isDark);
            Toast.makeText(context, isBn ? (naflBn + " আদায় সম্পন্ন (+" + BengaliNumberUtil.toBengali(bonusPoints) + " বোনাস পয়েন্ট অর্জিত)") : (naflKey + " logged (+" + bonusPoints + " XP earned)"), Toast.LENGTH_SHORT).show();
          });
        }
      });
    });
  }

  private static void setupForbiddenTimes(Context context, PageSalahTrackerBinding binding,
                      PrayerCalculator.PrayerTimesResult pt) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

    // 1. Sunrise Forbidden: Sunrise to Sunrise + 15m
    String sunriseStr = timeFmt.format(new Date(pt.sunriseMillis)) + " - " + timeFmt.format(new Date(pt.sunriseMillis + 15 * 60 * 1000L));
    binding.tvForbiddenSunriseTime.setText(isBn ? BengaliNumberUtil.toBengali(sunriseStr) : sunriseStr);

    // 2. Zawal Forbidden: Zohr - 15m to Zohr
    String zawalStr = timeFmt.format(new Date(pt.zohrMillis - 15 * 60 * 1000L)) + " - " + timeFmt.format(new Date(pt.zohrMillis));
    binding.tvForbiddenZawalTime.setText(isBn ? BengaliNumberUtil.toBengali(zawalStr) : zawalStr);

    // 3. Sunset Forbidden: Maghrib - 15m to Maghrib
    String sunsetStr = timeFmt.format(new Date(pt.maghribMillis - 15 * 60 * 1000L)) + " - " + timeFmt.format(new Date(pt.maghribMillis));
    binding.tvForbiddenSunsetTime.setText(isBn ? BengaliNumberUtil.toBengali(sunsetStr) : sunsetStr);

    binding.tvForbiddenSunriseTitle.setText(isBn ? "• সূর্যোদয়ের নিষিদ্ধ সময়" : "• Sunrise Forbidden Time");
    binding.tvForbiddenZawalTitle.setText(isBn ? "• ঠিক দ্বিপ্রহরের নিষিদ্ধ সময়" : "• Midday Forbidden Time");
    binding.tvForbiddenSunsetTitle.setText(isBn ? "• সূর্যাস্তের নিষিদ্ধ সময়" : "• Sunset Forbidden Time");
    binding.tvForbiddenDalilBtnText.setText(isBn ? "হুকুম ও দলিল" : "Rulings & Evidence");

    // Click handlers to open the 4-section Forbidden & Makrooh Times Bottom Sheet
    TouchAnimationUtil.attachTouchSpring(binding.btnForbiddenDalil);
    binding.tvForbiddenSunriseTime.setOnClickListener(v -> showForbiddenTimesBottomSheet(context, pt));
    binding.tvForbiddenZawalTime.setOnClickListener(v -> showForbiddenTimesBottomSheet(context, pt));
    binding.tvForbiddenSunsetTime.setOnClickListener(v -> showForbiddenTimesBottomSheet(context, pt));
    binding.btnForbiddenDalil.setOnClickListener(v -> showForbiddenTimesBottomSheet(context, pt));
  }

  /**
   * Forbidden & Makrooh Prayer Times Bottom Sheet — 100% Visual & Semantic match to user screenshots.
   */
  public static void showForbiddenTimesBottomSheet(Context context, PrayerCalculator.PrayerTimesResult pt) {
    if (context == null) return;
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    BottomSheetForbiddenTimesBinding sheetBinding =
        BottomSheetForbiddenTimesBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(sheetBinding.getRoot());

    if (dialog.getWindow() != null) {
      View bottomSheetInternal = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (bottomSheetInternal != null) {
        bottomSheetInternal.setBackgroundResource(android.R.color.transparent);
      }
    }

    if (pt == null) {
      Calendar cal = Calendar.getInstance();
      LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
      double lat = coords != null ? coords.latitude : 23.8103;
      double lng = coords != null ? coords.longitude : 90.4125;
      double timezone = coords != null ? coords.timezone : 6.0;
      pt = PrayerCalculator.calculateForLocationWithContext(context, lat, lng, timezone, cal);
    }

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    SimpleDateFormat timeFmtEn = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    SimpleDateFormat timeFmtBn = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

    sheetBinding.tvForbiddenSheetTitle.setText(isBn ? "নামাজের নিষিদ্ধ ও মাকরূহ সময়" : "Forbidden & Makrooh Prayer Times");
    sheetBinding.tvForbiddenSheetSubtitle.setText(isBn
        ? "বিশুদ্ধ হাদিস ও ইসলামী শরীয়াহ অনুযায়ী যেসকল সময়ে সালাত আদায় নিষিদ্ধ বা অপছন্দনীয়:"
        : "Times during which offering prayer is prohibited or disliked according to authentic Hadith & Islamic Shariah:");

    // Section 1: Hadith Card
    sheetBinding.tvForbiddenHadithTitle.setText(isBn
        ? "বিশুদ্ধ মূল হাদিস: সহীহ মুসলিম ৮৩১"
        : "Authentic Hadith: Sahih Muslim 831");
    sheetBinding.tvForbiddenHadithContent.setText(isBn
        ? "হযরত উকবাহ ইবনে আমির (রাঃ) বর্ণনা করেন: রাসুলুল্লাহ (সাঃ) আমাদেরকে তিনটি এমন সময়ে সালাত আদায় করতে অথবা আমাদের মৃতদের দাফন ও জানাযা করতে নিষেধ করেছেন:\n১. সূর্যোদয়ের সময় যতক্ষণ না তা স্পষ্ট উপরে ওঠে (১৫-১৮ মিনিট)।\n২. ঠিক দ্বিপ্রহরের সময় যতক্ষণ না সূর্য পশ্চিমাকাশে ঢলে পড়ে (যাওয়াল)।\n৩. সূর্যাস্তের উপক্রম হওয়ার সময় যতক্ষণ না সূর্য পুরোপুরি ডুবে যায়।"
        : "Uqbah ibn Amir (RA) narrated: The Messenger of Allah (peace be upon him) forbade us from praying or burying our dead at three specific times:\n1. When the sun begins to rise until it is fully risen (15-18 minutes).\n2. At midday when the sun is at its meridian until it declines (Zawal).\n3. When the sun begins to set until it has completely set.");

    // Section 2: 3 Strictly Forbidden Times
    sheetBinding.tvSectionForbiddenStrictTitle.setText(isBn
        ? "৩টি সম্পূর্ণ নিষিদ্ধ সময়: মাকরূহে তাহরীমী"
        : "3 Strictly Forbidden Times: Makrooh Tahrimi");

    // 1. Sunrise (Sunrise to Sunrise + 18m)
    long sunriseStart = pt.sunriseMillis;
    long sunriseEnd = pt.sunriseMillis + (18 * 60 * 1000L);
    String sunriseRange = isBn
        ? (BengaliNumberUtil.toBengali(timeFmtBn.format(new Date(sunriseStart))) + " - " + BengaliNumberUtil.toBengali(timeFmtBn.format(new Date(sunriseEnd))) + " (" + BengaliNumberUtil.toBengali(18) + " মিনিট)")
        : (timeFmtEn.format(new Date(sunriseStart)) + " - " + timeFmtEn.format(new Date(sunriseEnd)) + " (18 mins)");
    sheetBinding.tvForbiddenSunriseTitle.setText(isBn ? "১. সূর্যোদয়ের সময়" : "1. Sunrise Time");
    sheetBinding.tvForbiddenSunriseRange.setText(sunriseRange);
    sheetBinding.tvForbiddenSunriseTypes.setText(isBn
        ? "নিষিদ্ধ সালাত: ফরজ, নফল, কাযা, জানাযা বা সিজদায়ে তিলাওয়াত"
        : "Prohibited Prayers: Fard, Nafl, Qaza, Janaza, or Sajdah Tilawat");
    sheetBinding.tvForbiddenSunriseReason.setText(isBn
        ? "কারণ: এ সময় সূর্য শয়তানের দুই শিংয়ের মধ্য দিয়ে উদিত হয় এবং সূর্যপূজকরা সূর্যের পূজা করে।"
        : "Reason: At this time the sun rises between the two horns of Satan and sun-worshipers prostrate to it.");
    sheetBinding.tvForbiddenSunriseRef.setText(isBn
        ? "রেফারেন্স: সহীহ মুসলিম ৮৩১, সহীহ বুখারী ৫৮২"
        : "Reference: Sahih Muslim 831, Sahih Bukhari 582");

    // 2. Zawal (Zohr - 15m to Zohr)
    long zawalStart = pt.zohrMillis - (15 * 60 * 1000L);
    long zawalEnd = pt.zohrMillis;
    String zawalRange = isBn
        ? (BengaliNumberUtil.toBengali(timeFmtBn.format(new Date(zawalStart))) + " - " + BengaliNumberUtil.toBengali(timeFmtBn.format(new Date(zawalEnd))) + " (" + BengaliNumberUtil.toBengali(15) + " মিনিট)")
        : (timeFmtEn.format(new Date(zawalStart)) + " - " + timeFmtEn.format(new Date(zawalEnd)) + " (15 mins)");
    sheetBinding.tvForbiddenZawalTitle.setText(isBn ? "২. ঠিক দ্বিপ্রহরের সময়" : "2. Midday / Solar Noon");
    sheetBinding.tvForbiddenZawalRange.setText(zawalRange);
    sheetBinding.tvForbiddenZawalTypes.setText(isBn
        ? "নিষিদ্ধ সালাত: জুমার দিন ব্যতীত যেকোনো ফরজ, নফল, কাযা বা জানাযা"
        : "Prohibited Prayers: Any Fard, Nafl, Qaza, or Janaza (except on Friday)");
    sheetBinding.tvForbiddenZawalReason.setText(isBn
        ? "সূর্য ঠিক মাথার ওপর থাকা থেকে সামান্য পশ্চিম আকাশে ঢলে পড়া পর্যন্ত সময়। কারণ: এ সময় জাহান্নামের আগুনকে চরমভাবে উত্তপ্ত ও প্রজ্বলিত করা হয়।"
        : "From when the sun is at zenith until it declines westward. Reason: At this hour Hellfire is fiercely stoked.");
    sheetBinding.tvForbiddenZawalRef.setText(isBn
        ? "রেফারেন্স: সহীহ মুসলিম ৮৩১, সুনানে আবু দাউদ ১২৭৭"
        : "Reference: Sahih Muslim 831, Sunan Abi Dawud 1277");

    // 3. Sunset (Maghrib - 16m to Maghrib)
    long sunsetStart = pt.maghribMillis - (16 * 60 * 1000L);
    long sunsetEnd = pt.maghribMillis;
    String sunsetRange = isBn
        ? (BengaliNumberUtil.toBengali(timeFmtBn.format(new Date(sunsetStart))) + " - " + BengaliNumberUtil.toBengali(timeFmtBn.format(new Date(sunsetEnd))) + " (" + BengaliNumberUtil.toBengali(16) + " মিনিট)")
        : (timeFmtEn.format(new Date(sunsetStart)) + " - " + timeFmtEn.format(new Date(sunsetEnd)) + " (16 mins)");
    sheetBinding.tvForbiddenSunsetTitle.setText(isBn ? "৩. সূর্যাস্তের সময়" : "3. Sunset Time");
    sheetBinding.tvForbiddenSunsetRange.setText(sunsetRange);
    sheetBinding.tvForbiddenSunsetTypes.setText(isBn
        ? "নিষিদ্ধ সালাত: নফল সালাত ও জানাযা"
        : "Prohibited Prayers: Nafl Prayers and Janaza");
    sheetBinding.tvForbiddenSunsetReason.setText(isBn
        ? "কারণ: সূর্য অস্ত যাওয়ার সময় মুশরিকরা পূজা করে। তবে ওই দিনের আসর যদি কারো ভুলবশত না পড়া হয়ে থাকে, তবে তা এ সময় পড়ে নিতে হবে (সহীহ বুখারী ৫৫৩)।"
        : "Reason: Polytheists worship during sunset. However, if that day's Asr has not yet been performed due to an excuse, it must still be prayed then (Sahih Bukhari 553).");
    sheetBinding.tvForbiddenSunsetRef.setText(isBn
        ? "রেফারেন্স: সহীহ বুখারী ৫৫৩, সহীহ মুসলিম ৮৩১"
        : "Reference: Sahih Bukhari 553, Sahih Muslim 831");

    // Section 3: 2 Disliked Times for Nafl Prayers
    sheetBinding.tvSectionMakroohTitle.setText(isBn
        ? "২টি নফল সালাত আদায়ের মাকরূহ সময়"
        : "2 Disliked Times for Voluntary (Nafl) Prayers");

    // Card 4: Post-Fajr
    sheetBinding.tvMakroohFajrTitle.setText(isBn
        ? "১. ফজর সালাত আদায়ের পর হতে সূর্যোদয় পর্যন্ত"
        : "1. After Fajr Prayer until Sunrise");
    sheetBinding.tvMakroohFajrTypes.setText(isBn
        ? "মাকরূহ সালাত: অতিরিক্ত সাধারণ নফল সালাত"
        : "Disliked Prayers: Extra voluntary (Nafl) prayers");
    sheetBinding.tvMakroohFajrReason.setText(isBn
        ? "কারণ: রাসুলুল্লাহ (সাঃ) ফজরের পর সূর্যোদয় পর্যন্ত নফল পড়তে নিষেধ করেছেন।"
        : "Reason: The Messenger of Allah (peace be upon him) forbade praying voluntary prayers after Fajr until the sun has risen.");
    sheetBinding.tvMakroohFajrRef.setText(isBn
        ? "রেফারেন্স: সহীহ বুখারী ৫৮৬, সহীহ মুসলিম ৮২৭"
        : "Reference: Sahih Bukhari 586, Sahih Muslim 827");

    // Card 5: Post-Asr
    sheetBinding.tvMakroohAsrTitle.setText(isBn
        ? "২. আসর সালাত আদায়ের পর হতে সূর্যাস্ত শুরু হওয়া পর্যন্ত"
        : "2. After Asr Prayer until Sunset begins");
    sheetBinding.tvMakroohAsrTypes.setText(isBn
        ? "মাকরূহ সালাত: অতিরিক্ত সাধারণ নফল সালাত"
        : "Disliked Prayers: Extra voluntary (Nafl) prayers");
    sheetBinding.tvMakroohAsrReason.setText(isBn
        ? "কারণ: আসরের পর কেবল পূর্বের কাযা নামাজ পড়া জায়েয, কিন্তু সাধারণ নফল পড়া মাকরূহ।"
        : "Reason: Only missed (Qaza) prayers are permissible after Asr, while ordinary voluntary prayers are disliked.");
    sheetBinding.tvMakroohAsrRef.setText(isBn
        ? "রেফারেন্স: সহীহ বুখারী ৫৮৬, সহীহ মুসলিম ৮২৫"
        : "Reference: Sahih Bukhari 586, Sahih Muslim 825");

    // Section 4: Fiqh Perspectives
    sheetBinding.tvSectionFiqhTitle.setText(isBn
        ? "ফিকহ ও মাযহাবগত বিশুদ্ধ অবস্থান"
        : "Fiqh & Madhhab Perspectives");
    sheetBinding.tvFiqhHanafiTitle.setText(isBn
        ? "• হানাফী মাযহাব:"
        : "• Hanafi Madhhab:");
    sheetBinding.tvFiqhHanafiContent.setText(isBn
        ? "এই ৩টি সময়ে যেকোনো সালাত (নফল, কাযা, জানাযা, সিজদায়ে তিলাওয়াত) আদায় করা মাকরূহে তাহরীমী (অবৈধ)। কেবল সেদিনের আসর বাকি থাকলে তা সূর্যাস্তের সময় পড়ে নেওয়া যাবে।"
        : "Performing any prayer (Nafl, Qaza, Janaza, Sajdah Tilawat) during the 3 prohibited times is strictly disliked (Makrooh Tahrimi). Only that day's delayed Asr may be performed before sunset.");
    sheetBinding.tvFiqhShafiTitle.setText(isBn
        ? "• শাফিয়ী ও হাম্বলী মাযহাব:"
        : "• Shafi'i & Hanbali Madhhabs:");
    sheetBinding.tvFiqhShafiContent.setText(isBn
        ? "কারণহীন সাধারণ নফল পড়া নিষিদ্ধ। কিন্তু কারণযুক্ত সালাত (যেমন: কাযা সালাত, জানাযার সালাত, তাহিয়্যাতুল মসজিদ, তাওয়াফের দুই রাকাত) আদায় করা বৈধ।"
        : "Ordinary voluntary prayers without a specific cause are prohibited. However, prayers with a specific reason (such as Qaza, Janaza, Tahiyyatul Masjid, or Tawaf two rakats) are permissible.");

    // Spring touch animations

    dialog.show();
  }

  private static void setupActionCards(Context context, PageSalahTrackerBinding binding,
                    String todayIso,
                    Map<String, ItemSalahWaqtCardBinding> waqtBindingMap,
                    PrayerCalculator.PrayerTimesResult pt) {
    // 1. ছুটে যাওয়া নামাজ -> Open Bottom Sheet Missed Prayers Tracker
    binding.cardMissedPrayerAction.setOnClickListener(v -> {
      showMissedPrayersTrackerBottomSheet(context, todayIso, waqtBindingMap, binding, pt);
    });

    // 2. কাজা নামাজ -> Qaza Lifetime Calculator bottom sheet dialog
    binding.cardQazaTrackerAction.setOnClickListener(v -> {
      showQazaCalculatorDialog(context, todayIso, waqtBindingMap, binding);
    });

    // 3. মাসিক সালাত রিপোর্ট -> Monthly 30-day analytics report
    binding.cardMonthlyReportAction.setOnClickListener(v -> {
      showMonthlySalatReportDialog(context);
    });
  }

  private static void showQazaCalculatorDialog(Context context,
                         String todayIso,
                         Map<String, ItemSalahWaqtCardBinding> waqtBindingMap,
                         PageSalahTrackerBinding rootBinding) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    BottomSheetQazaCalculatorBinding sheetBinding =
        BottomSheetQazaCalculatorBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(sheetBinding.getRoot());

    if (dialog.getWindow() != null) {
      View bottomSheetInternal = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (bottomSheetInternal != null) {
        bottomSheetInternal.setBackgroundResource(android.R.color.transparent);
      }
    }

    // Dual-language header and labels
    sheetBinding.tvQazaCalcTitle.setText(isBn ? "কাজা নামাজ ক্যালকুলেটর" : "Qaza Prayer Calculator");
    sheetBinding.tvNameQazaFajr.setText(isBn ? "ফজর" : "Fajr");
    sheetBinding.tvNameQazaDhuhr.setText(isBn ? "যোহর" : "Dhuhr");
    sheetBinding.tvNameQazaAsr.setText(isBn ? "আসর" : "Asr");
    sheetBinding.tvNameQazaMaghrib.setText(isBn ? "মাগরিব" : "Maghrib");
    sheetBinding.tvNameQazaIsha.setText(isBn ? "এশা" : "Isha");
    sheetBinding.tvNameQazaWitr.setText(isBn ? "বিতর" : "Witr");

    // Helper to refresh total qaza badge
    Runnable updateTotalBadge = () -> {
      int total = QazaCalculatorManager.getTotalQazaCount(context);
      sheetBinding.tvTotalQazaBadge.setText(isBn ? ("মোট কাজা: " + BengaliNumberUtil.toBengali(total) + " ওয়াক্ত")
                                                 : ("Total Qaza: " + total + " Waqts"));
    };

    updateTotalBadge.run();

    // Attach touch springs
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlusQazaFajr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMinusQazaFajr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlusQazaDhuhr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMinusQazaDhuhr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlusQazaAsr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMinusQazaAsr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlusQazaMaghrib);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMinusQazaMaghrib);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlusQazaIsha);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMinusQazaIsha);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnPlusQazaWitr);
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnMinusQazaWitr);

    // 1. Fajr
    sheetBinding.tvCountQazaFajr.setText(isBn ? BengaliNumberUtil.toBengali(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_FAJR)) : String.valueOf(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_FAJR)));
    sheetBinding.btnPlusQazaFajr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.incrementQaza(context, QazaCalculatorManager.KEY_FAJR);
      sheetBinding.tvCountQazaFajr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
    });
    sheetBinding.btnMinusQazaFajr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.decrementQaza(context, QazaCalculatorManager.KEY_FAJR);
      sheetBinding.tvCountQazaFajr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
      loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, rootBinding);
      Toast.makeText(context, isBn ? "মাশাআল্লাহ! ১ ওয়াক্ত ফজর কাজা আদায় সম্পন্ন" : "MashaAllah! 1 Fajr Qaza completed", Toast.LENGTH_SHORT).show();
    });

    // 2. Dhuhr
    sheetBinding.tvCountQazaDhuhr.setText(isBn ? BengaliNumberUtil.toBengali(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_DHUHR)) : String.valueOf(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_DHUHR)));
    sheetBinding.btnPlusQazaDhuhr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.incrementQaza(context, QazaCalculatorManager.KEY_DHUHR);
      sheetBinding.tvCountQazaDhuhr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
    });
    sheetBinding.btnMinusQazaDhuhr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.decrementQaza(context, QazaCalculatorManager.KEY_DHUHR);
      sheetBinding.tvCountQazaDhuhr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
      loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, rootBinding);
      Toast.makeText(context, isBn ? "মাশাআল্লাহ! ১ ওয়াক্ত যোহর কাজা আদায় সম্পন্ন" : "MashaAllah! 1 Dhuhr Qaza completed", Toast.LENGTH_SHORT).show();
    });

    // 3. Asr
    sheetBinding.tvCountQazaAsr.setText(isBn ? BengaliNumberUtil.toBengali(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_ASR)) : String.valueOf(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_ASR)));
    sheetBinding.btnPlusQazaAsr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.incrementQaza(context, QazaCalculatorManager.KEY_ASR);
      sheetBinding.tvCountQazaAsr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
    });
    sheetBinding.btnMinusQazaAsr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.decrementQaza(context, QazaCalculatorManager.KEY_ASR);
      sheetBinding.tvCountQazaAsr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
      loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, rootBinding);
      Toast.makeText(context, isBn ? "মাশাআল্লাহ! ১ ওয়াক্ত আসর কাজা আদায় সম্পন্ন" : "MashaAllah! 1 Asr Qaza completed", Toast.LENGTH_SHORT).show();
    });

    // 4. Maghrib
    sheetBinding.tvCountQazaMaghrib.setText(isBn ? BengaliNumberUtil.toBengali(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_MAGHRIB)) : String.valueOf(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_MAGHRIB)));
    sheetBinding.btnPlusQazaMaghrib.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.incrementQaza(context, QazaCalculatorManager.KEY_MAGHRIB);
      sheetBinding.tvCountQazaMaghrib.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
    });
    sheetBinding.btnMinusQazaMaghrib.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.decrementQaza(context, QazaCalculatorManager.KEY_MAGHRIB);
      sheetBinding.tvCountQazaMaghrib.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
      loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, rootBinding);
      Toast.makeText(context, isBn ? "মাশাআল্লাহ! ১ ওয়াক্ত মাগরিব কাজা আদায় সম্পন্ন" : "MashaAllah! 1 Maghrib Qaza completed", Toast.LENGTH_SHORT).show();
    });

    // 5. Isha
    sheetBinding.tvCountQazaIsha.setText(isBn ? BengaliNumberUtil.toBengali(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_ISHA)) : String.valueOf(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_ISHA)));
    sheetBinding.btnPlusQazaIsha.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.incrementQaza(context, QazaCalculatorManager.KEY_ISHA);
      sheetBinding.tvCountQazaIsha.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
    });
    sheetBinding.btnMinusQazaIsha.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.decrementQaza(context, QazaCalculatorManager.KEY_ISHA);
      sheetBinding.tvCountQazaIsha.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
      loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, rootBinding);
      Toast.makeText(context, isBn ? "মাশাআল্লাহ! ১ ওয়াক্ত এশা কাজা আদায় সম্পন্ন" : "MashaAllah! 1 Isha Qaza completed", Toast.LENGTH_SHORT).show();
    });

    // 6. Witr
    sheetBinding.tvCountQazaWitr.setText(isBn ? BengaliNumberUtil.toBengali(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_WITR)) : String.valueOf(QazaCalculatorManager.getQazaCount(context, QazaCalculatorManager.KEY_WITR)));
    sheetBinding.btnPlusQazaWitr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.incrementQaza(context, QazaCalculatorManager.KEY_WITR);
      sheetBinding.tvCountQazaWitr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
    });
    sheetBinding.btnMinusQazaWitr.setOnClickListener(v -> {
      int newCount = QazaCalculatorManager.decrementQaza(context, QazaCalculatorManager.KEY_WITR);
      sheetBinding.tvCountQazaWitr.setText(isBn ? BengaliNumberUtil.toBengali(newCount) : String.valueOf(newCount));
      updateTotalBadge.run();
      Toast.makeText(context, isBn ? "মাশাআল্লাহ! ১ ওয়াক্ত বিতর কাজা আদায় সম্পন্ন" : "MashaAllah! 1 Witr Qaza completed", Toast.LENGTH_SHORT).show();
    });

    dialog.show();
  }

  private static void showMonthlySalatReportDialog(Context context) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    BottomSheetMonthlySalatReportBinding sheetBinding =
        BottomSheetMonthlySalatReportBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(sheetBinding.getRoot());

    if (dialog.getWindow() != null) {
      View bottomSheetInternal = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (bottomSheetInternal != null) {
        bottomSheetInternal.setBackgroundResource(android.R.color.transparent);
      }
    }

    MonthlySalatReportAdapter adapter = new MonthlySalatReportAdapter();
    sheetBinding.rvMonthlySalatRows.setLayoutManager(new LinearLayoutManager(context));
    sheetBinding.rvMonthlySalatRows.setAdapter(adapter);

    // Dual-language titles, table headers, and quotes
    sheetBinding.tvMonthlyReportTitle.setText(isBn ? "মাসিক নামাজের রিপোর্ট" : "Monthly Salat Report");
    sheetBinding.tvColDate.setText(isBn ? "তারিখ" : "Date");
    sheetBinding.tvColFajr.setText(isBn ? "ফজর" : "Fajr");
    sheetBinding.tvColDhuhr.setText(isBn ? "জোহর" : "Dhuhr");
    sheetBinding.tvColAsr.setText(isBn ? "আসর" : "Asr");
    sheetBinding.tvColMaghrib.setText(isBn ? "মাগরিব" : "Maghrib");
    sheetBinding.tvColIsha.setText(isBn ? "এশা" : "Isha");
    sheetBinding.tvColRate.setText(isBn ? "হার" : "Rate");

    sheetBinding.tvReportQuranQuote.setText(isBn
        ? "“নিশ্চয়ই নামাজ মানুষকে অশ্লীল ও খারাপ কাজ থেকে বিরত রাখে।”"
        : "“Indeed, prayer prohibits immorality and wrongdoing.”");
    sheetBinding.tvReportQuranRef.setText(isBn
        ? "— সূরা আল-আনকাবুত: ৪৫"
        : "— Surah Al-Ankabut: 45");

    sheetBinding.tvReportHadithQuote.setText(isBn
        ? "“সালাত হলো মুমিনের মিরাজ।”"
        : "“Prayer is the ascension of the believer.”");
    sheetBinding.tvReportHadithRef.setText(isBn
        ? "— আল-হাদিস"
        : "— Al-Hadith");

    AppDatabase db = AppDatabase.getInstance(context);

    final String[] bnMonthFull = {
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    };
    final String[] enMonthFull = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };

    final String[] bnDayNames = {
        "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি"
    };
    final String[] enDayNames = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    final long installTimeMillis = QazaCalculatorManager.getFirstInstallTimeMillis(context);
    Calendar installCal = Calendar.getInstance();
    installCal.setTimeInMillis(installTimeMillis);
    final int installYear = installCal.get(Calendar.YEAR);
    final int installMonth = installCal.get(Calendar.MONTH);
    final int installDay = installCal.get(Calendar.DAY_OF_MONTH);

    Calendar currentCal = Calendar.getInstance();
    final int[] selectedYear = {currentCal.get(Calendar.YEAR)};
    final int[] selectedMonth = {currentCal.get(Calendar.MONTH)}; // 0-indexed

    Runnable loadMonthReport = new Runnable() {
      @Override
      public void run() {
        int year = selectedYear[0];
        int month = selectedMonth[0];

        String monthPrefix = String.format(Locale.US, "%04d-%02d", year, month + 1);
        String selectedTitle = isBn ? (bnMonthFull[month] + " " + BengaliNumberUtil.toBengali(year))
                                    : (enMonthFull[month] + " " + year);
        sheetBinding.tvSelectedMonthYear.setText(selectedTitle);

        AppDatabase.databaseWriteExecutor.execute(() -> {
          SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
          Calendar todayCal = Calendar.getInstance();
          String todayIso = isoFormat.format(todayCal.getTime());
          int todayYear = todayCal.get(Calendar.YEAR);
          int todayMonth = todayCal.get(Calendar.MONTH);
          int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);

          int effInstallYear = installYear;
          int effInstallMonth = installMonth;
          int effInstallDay = installDay;
          if (effInstallYear > todayYear || (effInstallYear == todayYear && effInstallMonth > todayMonth)
              || (effInstallYear == todayYear && effInstallMonth == todayMonth && effInstallDay > todayDay)) {
            effInstallYear = todayYear;
            effInstallMonth = todayMonth;
            effInstallDay = todayDay;
          }

          Calendar iterCal = Calendar.getInstance();
          iterCal.set(Calendar.YEAR, year);
          iterCal.set(Calendar.MONTH, month);
          iterCal.set(Calendar.DAY_OF_MONTH, 1);
          int maxDays = iterCal.getActualMaximum(Calendar.DAY_OF_MONTH);

          List<PrayerLogEntity> monthLogs = db.prayerLogDao().getLogsForMonth(monthPrefix);
          Map<String, Map<String, PrayerLogEntity>> datePrayerMap = new HashMap<String, Map<String, PrayerLogEntity>>();
          int earliestLogDayInMonth = -1;
          if (monthLogs != null) {
            for (PrayerLogEntity log : monthLogs) {
              if (log != null && log.getDate() != null && log.getPrayerName() != null) {
                if (!datePrayerMap.containsKey(log.getDate())) {
                  datePrayerMap.put(log.getDate(), new HashMap<String, PrayerLogEntity>());
                }
                datePrayerMap.get(log.getDate()).put(log.getPrayerName(), log);
                if (log.getDate().length() >= 10) {
                  try {
                    int dNum = Integer.parseInt(log.getDate().substring(8, 10));
                    if (earliestLogDayInMonth == -1 || dNum < earliestLogDayInMonth) {
                      earliestLogDayInMonth = dNum;
                    }
                  } catch (Exception ignored) {}
                }
              }
            }
          }

          boolean isCurrentMonth = (year == todayYear && month == todayMonth);
          boolean isFutureMonth = (year > todayYear || (year == todayYear && month > todayMonth));
          boolean isInstallMonth = (year == effInstallYear && month == effInstallMonth);
          boolean isBeforeInstallMonth = (year < effInstallYear || (year == effInstallYear && month < effInstallMonth));

          int startDay;
          int endDay;

          if (isFutureMonth) {
            startDay = 0;
            endDay = 1;
          } else if (isBeforeInstallMonth) {
            if (earliestLogDayInMonth > 0) {
              startDay = maxDays;
              endDay = earliestLogDayInMonth;
            } else {
              startDay = 0;
              endDay = 1;
            }
          } else {
            // Latest day to display (starts at top of descending list)
            if (isCurrentMonth) {
              startDay = todayDay; // Today is at the top! (e.g. 9)
            } else {
              startDay = maxDays; // Last day of that past month
            }

            // Earliest day to display (ends at bottom of descending list)
            if (isInstallMonth) {
              endDay = effInstallDay;
              if (earliestLogDayInMonth > 0 && earliestLogDayInMonth < effInstallDay) {
                endDay = earliestLogDayInMonth;
              }
            } else {
              endDay = 1;
            }
          }

          List<MonthlySalatReportAdapter.DaySalatRecord> dayRecords = new ArrayList<>();
          int totalPrayedOnTime = 0;
          int totalQaza = 0;
          int totalMissed = 0;
          int totalWaqtsEvaluated = 0;

          // Reverse chronological loop: Today at the top, down to install day
          for (int d = startDay; d >= endDay; d--) {
            iterCal.set(Calendar.DAY_OF_MONTH, d);
            Date dayDate = iterCal.getTime();
            String dateIso = isoFormat.format(dayDate);
            int dayOfWeek = iterCal.get(Calendar.DAY_OF_WEEK); // 1 = Sunday, 7 = Saturday
            String dayName = isBn ? bnDayNames[dayOfWeek - 1] : enDayNames[dayOfWeek - 1];
            String dayNumBn = isBn ? BengaliNumberUtil.toBengali(d) : String.valueOf(d);

            Map<String, PrayerLogEntity> dayLogs = datePrayerMap.get(dateIso);
            boolean isToday = dateIso.equals(todayIso);

            MonthlySalatReportAdapter.SalatStatus fajrStatus = getSalatStatus(dayLogs, "Fajr", isToday, false);
            MonthlySalatReportAdapter.SalatStatus dhuhrStatus = getSalatStatus(dayLogs, "Dhuhr", isToday, false);
            MonthlySalatReportAdapter.SalatStatus asrStatus = getSalatStatus(dayLogs, "Asr", isToday, false);
            MonthlySalatReportAdapter.SalatStatus maghribStatus = getSalatStatus(dayLogs, "Maghrib", isToday, false);
            MonthlySalatReportAdapter.SalatStatus ishaStatus = getSalatStatus(dayLogs, "Isha", isToday, false);

            int dayPrayedCount = 0;
            MonthlySalatReportAdapter.SalatStatus[] dayStatuses = {fajrStatus, dhuhrStatus, asrStatus, maghribStatus, ishaStatus};
            for (MonthlySalatReportAdapter.SalatStatus st : dayStatuses) {
              if (st == MonthlySalatReportAdapter.SalatStatus.PRAYED) {
                dayPrayedCount++;
                totalPrayedOnTime++;
                totalWaqtsEvaluated++;
              } else if (st == MonthlySalatReportAdapter.SalatStatus.QAZA) {
                totalQaza++;
                totalWaqtsEvaluated++;
              } else if (st == MonthlySalatReportAdapter.SalatStatus.MISSED) {
                totalMissed++;
                totalWaqtsEvaluated++;
              }
            }

            int dayPercentage = Math.min(100, Math.max(0, (dayPrayedCount * 100) / 5));

            dayRecords.add(new MonthlySalatReportAdapter.DaySalatRecord(
                dateIso, dayNumBn, dayName,
                fajrStatus, dhuhrStatus, asrStatus, maghribStatus, ishaStatus,
                dayPercentage, false
            ));
          }

          int monthlyPercentage = totalWaqtsEvaluated > 0 ? (totalPrayedOnTime * 100) / totalWaqtsEvaluated : 0;

          new Handler(Looper.getMainLooper()).post(() -> {
            adapter.setRecords(dayRecords);
            if (sheetBinding.tvEmptyReport != null) {
              sheetBinding.tvEmptyReport.setVisibility(dayRecords.isEmpty() ? View.VISIBLE : View.GONE);
              sheetBinding.tvEmptyReport.setText(isBn ? "এই মাসে কোনো সালাত রেকর্ড পাওয়া যায়নি" : "No salat records found for this month");
            }
          });
        });
      }
    };

    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnSelectMonthYear);
    sheetBinding.btnSelectMonthYear.setOnClickListener(v -> {
      List<String> monthOptions = new ArrayList<>();
      List<int[]> monthYearValues = new ArrayList<>();

      Calendar mCal = Calendar.getInstance();
      for (int i = 0; i < 12; i++) {
        int y = mCal.get(Calendar.YEAR);
        int m = mCal.get(Calendar.MONTH);
        monthOptions.add(isBn ? (bnMonthFull[m] + " " + BengaliNumberUtil.toBengali(y))
                              : (enMonthFull[m] + " " + y));
        monthYearValues.add(new int[]{y, m});
        if (y < installYear || (y == installYear && m <= installMonth)) {
          break; // Stop at install month
        }
        mCal.add(Calendar.MONTH, -1);
      }

      if (monthOptions.isEmpty()) {
        int y = currentCal.get(Calendar.YEAR);
        int m = currentCal.get(Calendar.MONTH);
        monthOptions.add(isBn ? (bnMonthFull[m] + " " + BengaliNumberUtil.toBengali(y))
                              : (enMonthFull[m] + " " + y));
        monthYearValues.add(new int[]{y, m});
      }

      new AlertDialog.Builder(context)
          .setTitle(isBn ? "মাস নির্বাচন করুন" : "Select Month")
          .setItems(monthOptions.toArray(new String[0]), (dialogInterface, which) -> {
            int[] selected = monthYearValues.get(which);
            selectedYear[0] = selected[0];
            selectedMonth[0] = selected[1];
            loadMonthReport.run();
          })
          .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
          .show();
    });

    loadMonthReport.run();

    dialog.show();
  }

  private static MonthlySalatReportAdapter.SalatStatus getSalatStatus(
      Map<String, PrayerLogEntity> dayLogs, String waqt, boolean isToday, boolean isFutureDate) {
    if (isFutureDate) {
      return MonthlySalatReportAdapter.SalatStatus.NONE;
    }
    if (dayLogs != null) {
      PrayerLogEntity log = dayLogs.get(waqt);
      if (log != null) {
        if (log.isQaza() || "QAZA".equalsIgnoreCase(log.getStatus())) {
          return MonthlySalatReportAdapter.SalatStatus.QAZA;
        }
        if (log.isPrayed()) {
          return MonthlySalatReportAdapter.SalatStatus.PRAYED;
        }
        return MonthlySalatReportAdapter.SalatStatus.MISSED;
      }
    }
    if (isToday) {
      return MonthlySalatReportAdapter.SalatStatus.NONE;
    }
    return MonthlySalatReportAdapter.SalatStatus.MISSED;
  }

  /**
   * Missed Prayers Tracker Bottom Sheet — 100% Visual & Semantic match to user screenshot.
   */
  private static void showMissedPrayersTrackerBottomSheet(Context context,
                              String todayIso,
                              Map<String, ItemSalahWaqtCardBinding> waqtBindingMap,
                              PageSalahTrackerBinding rootBinding,
                              PrayerCalculator.PrayerTimesResult pt) {
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    BottomSheetDialog dialog = new BottomSheetDialog(context);
    BottomSheetMissedPrayersTrackerBinding sheetBinding =
        BottomSheetMissedPrayersTrackerBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(sheetBinding.getRoot());

    if (dialog.getWindow() != null) {
      View bottomSheetInternal = dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (bottomSheetInternal != null) {
        bottomSheetInternal.setBackgroundResource(android.R.color.transparent);
      }
    }

    QazaCalculatorManager.autoCalculateMissedPrayers(context);

    sheetBinding.tvMissedPrayersTitle.setText(isBn ? "ছুটে যাওয়া নামাজ ট্র্যাকার" : "Missed Prayers Tracker");
    sheetBinding.tvEmptyMissedPrayers.setText(isBn ? "আলহামদুলিল্লাহ! আজকের কোনো সালাত ছুটে যায়নি।" : "Alhamdulillah! No prayers missed today.");
    sheetBinding.tvEmptyMissedPrayersDesc.setText(isBn ? "আপনার আজকের সকল ওয়াক্ত সময়মতো আদায় হয়েছে।" : "All your prayers for today have been completed on time.");
    sheetBinding.tvNameFajr.setText(isBn ? "ফজর ওয়াক্ত" : "Fajr Waqt");
    sheetBinding.tvNameDhuhr.setText(isBn ? "যোহর ওয়াক্ত" : "Dhuhr Waqt");
    sheetBinding.tvNameAsr.setText(isBn ? "আসর ওয়াক্ত" : "Asr Waqt");
    sheetBinding.tvNameMaghrib.setText(isBn ? "মাগরিব ওয়াক্ত" : "Maghrib Waqt");
    sheetBinding.tvNameIsha.setText(isBn ? "এশা ওয়াক্ত" : "Isha Waqt");

    sheetBinding.tvStatusFajr.setText(isBn ? "সময় অতিক্রান্ত • কাজা হয়েছে" : "Time expired • Missed");
    sheetBinding.tvStatusDhuhr.setText(isBn ? "সময় অতিক্রান্ত • কাজা হয়েছে" : "Time expired • Missed");
    sheetBinding.tvStatusAsr.setText(isBn ? "সময় অতিক্রান্ত • কাজা হয়েছে" : "Time expired • Missed");
    sheetBinding.tvStatusMaghrib.setText(isBn ? "সময় অতিক্রান্ত • কাজা হয়েছে" : "Time expired • Missed");
    sheetBinding.tvStatusIsha.setText(isBn ? "সময় অতিক্রান্ত • কাজা হয়েছে" : "Time expired • Missed");

    Runnable updateTotalMissedBadge = () -> {
      int totalQaza = QazaCalculatorManager.getTotalQazaCount(context);
      sheetBinding.tvTotalMissedBadge.setText(isBn ? ("মোট কাজা: " + BengaliNumberUtil.toBengali(totalQaza) + " ওয়াক্ত")
                                                  : ("Total Qaza: " + totalQaza + " Waqts"));
    };
    updateTotalMissedBadge.run();

    sheetBinding.tvOpenLifetimeQazaTitle.setText(isBn ? "কাজা নামাজ ক্যালকুলেটর" : "Qaza Prayer Calculator");
    sheetBinding.tvOpenLifetimeQazaSubtitle.setText(isBn ? "জীবনের মোট কাজা নামাজের হিসাব ও পরিচালনা" : "Manage your lifetime missed prayers ledger");
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnOpenLifetimeQaza);
    sheetBinding.btnOpenLifetimeQaza.setOnClickListener(v -> {
      dialog.dismiss();
      showQazaCalculatorDialog(context, todayIso, waqtBindingMap, rootBinding);
    });

    AppDatabase db = AppDatabase.getInstance(context);

    String[] waqts = {"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"};
    View[] rows = {
        sheetBinding.layoutRowFajr,
        sheetBinding.layoutRowDhuhr,
        sheetBinding.layoutRowAsr,
        sheetBinding.layoutRowMaghrib,
        sheetBinding.layoutRowIsha
    };
    ImageView[] indicators = {
        sheetBinding.ivIndicatorFajr,
        sheetBinding.ivIndicatorDhuhr,
        sheetBinding.ivIndicatorAsr,
        sheetBinding.ivIndicatorMaghrib,
        sheetBinding.ivIndicatorIsha
    };
    TextView[] buttons = {
        sheetBinding.btnToggleFajr,
        sheetBinding.btnToggleDhuhr,
        sheetBinding.btnToggleAsr,
        sheetBinding.btnToggleMaghrib,
        sheetBinding.btnToggleIsha
    };

    long[] startTimes = (pt != null) ? new long[]{
        pt.fajrMillis,
        pt.zohrMillis,
        pt.asrMillis,
        pt.maghribMillis,
        pt.ishaMillis
    } : new long[]{0, 0, 0, 0, 0};

    long[] endTimes = (pt != null) ? new long[]{
        pt.sunriseMillis,
        pt.asrMillis,
        pt.maghribMillis,
        pt.ishaMillis,
        pt.fajrMillis + (24 * 3600 * 1000L)
    } : new long[]{0, 0, 0, 0, 0};

    long now = System.currentTimeMillis();

    // Load current prayer log statuses from Room DB asynchronously
    AppDatabase.databaseWriteExecutor.execute(() -> {
      List<PrayerLogEntity> logs = db.prayerLogDao().getPrayerLogsForDateSync(todayIso);
      Map<String, PrayerLogEntity> logMap = new HashMap<>();
      if (logs != null) {
        for (PrayerLogEntity log : logs) {
          if (log != null && log.getPrayerName() != null) {
            logMap.put(log.getPrayerName(), log);
          }
        }
      }

      new Handler(Looper.getMainLooper()).post(() -> {
        int missedCount = 0;
        final int[] activeMissedCounter = new int[1];

        for (int i = 0; i < waqts.length; i++) {
          final int idx = i;
          final String waqt = waqts[idx];
          final long start = startTimes[idx];
          final long end = endTimes[idx];
          PrayerLogEntity log = logMap.get(waqt);

          boolean isPrayedOnTime = (log != null && log.isPrayed() && !"QAZA".equalsIgnoreCase(log.getStatus()));
          boolean isMarkedQaza = (log != null && "QAZA".equalsIgnoreCase(log.getStatus()));
          boolean isExpiredUnprayed = (now >= end && (log == null || !log.isPrayed()));
          boolean isMissed = isMarkedQaza || isExpiredUnprayed;

          if (isMissed) {
            missedCount++;
            rows[idx].setVisibility(View.VISIBLE);
            indicators[idx].setImageResource(R.drawable.ic_cancel_circle);
            indicators[idx].setColorFilter(ContextCompat.getColor(context, R.color.accent_red));
            buttons[idx].setBackgroundResource(R.drawable.bg_btn_pill_green);
            buttons[idx].setBackgroundTintList(null);
            buttons[idx].setText(isBn ? "আদায় করেছি" : "Prayed");
            buttons[idx].setTextColor(Color.WHITE);
            buttons[idx].setEnabled(true);
            TouchAnimationUtil.attachTouchSpring(buttons[idx]);
            buttons[idx].setOnClickListener(v -> {
              // Perform Qaza recovery
              AppDatabase.databaseWriteExecutor.execute(() -> {
                PrayerLogEntity qazaRecoveredLog = new PrayerLogEntity(
                    todayIso, waqt, true, false, false, "EKAKI", 10, System.currentTimeMillis()
                );
                db.prayerLogDao().insertOrUpdate(qazaRecoveredLog);
                GamificationManager.addXP(context, 10);
                new com.devflux.deenone.data.repository.AmalRepository(context).recalculateAndSyncTodayRecord();

                String qKey = QazaCalculatorManager.waqtNameToKey(waqt);
                if (qKey != null) {
                  QazaCalculatorManager.decrementQaza(context, qKey);
                  QazaCalculatorManager.markWaqtEvaluated(context, todayIso, waqt);
                }

                new Handler(Looper.getMainLooper()).post(() -> {
                  rows[idx].setVisibility(View.GONE);
                  activeMissedCounter[0]--;
                  if (activeMissedCounter[0] <= 0) {
                    sheetBinding.layoutEmptyMissedPrayers.setVisibility(View.VISIBLE);
                    sheetBinding.tvMissedPrayersSubtitle.setText(isBn ? "আজকের কোনো সালাত ছুটে যায়নি, আলহামদুলিল্লাহ!" : "No prayers missed today, Alhamdulillah!");
                  }
                  updateTotalMissedBadge.run();
                  loadTodayPrayerStatuses(context, todayIso, waqtBindingMap, rootBinding);
                });
              });
            });
          } else {
            // Not missed (either prayed or future) -> hidden from missed prayers list
            rows[idx].setVisibility(View.GONE);
          }
        }

        activeMissedCounter[0] = missedCount;

        if (missedCount == 0) {
          sheetBinding.layoutEmptyMissedPrayers.setVisibility(View.VISIBLE);
          sheetBinding.tvMissedPrayersSubtitle.setText(isBn ? "আজকের কোনো সালাত ছুটে যায়নি, আলহামদুলিল্লাহ!" : "No prayers missed today, Alhamdulillah!");
        } else {
          sheetBinding.layoutEmptyMissedPrayers.setVisibility(View.GONE);
          sheetBinding.tvMissedPrayersSubtitle.setText(isBn ? "আজকের যেসকল ওয়াক্ত পড়তে পারেননি, সেগুলো কাযা হিসেবে আদায় করে মার্ক করুন:" : "Mark your unperformed prayers for today once prayed as Qaza:");
        }
      });
    });

    dialog.show();
  }

  private static String waqtToBengaliName(String waqt) {
    boolean isFriday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
    switch (waqt) {
      case "Fajr": return "ফজর";
      case "Dhuhr": return isFriday ? "জুম'আ" : "যোহর";
      case "Asr": return "আসর";
      case "Maghrib": return "মাগরিব";
      case "Isha": return "এশা";
      default: return waqt;
    }
  }

  /**
   * Real-time Worldwide Nearby Mosque Selection Bottom Sheet matching design aesthetics.
   */
  private static void showMosqueSelectionDialog(Context context, PageSalahTrackerBinding trackerBinding) {
    BottomSheetDialog bottomSheet = new BottomSheetDialog(context);
    DialogIslamicPickerModalBinding sheetBinding = DialogIslamicPickerModalBinding.inflate(LayoutInflater.from(context));
    bottomSheet.setContentView(sheetBinding.getRoot());

    try {
      View sheetView = bottomSheet.findViewById(com.google.android.material.R.id.design_bottom_sheet);
      if (sheetView != null) {
        sheetView.setBackgroundResource(android.R.color.transparent);
      }
    } catch (Exception ignored) {}

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    sheetBinding.tvPickerTitle.setText(isBn ? "জামাতের জন্য মসজিদ নির্বাচন করুন" : "Select Masjid for Jamat");
    sheetBinding.etPickerSearch.setHint(isBn ? "মসজিদের নাম দিয়ে খুঁজুন..." : "Search masjid by name...");
    TouchAnimationUtil.attachTouchSpring(sheetBinding.btnClosePicker);
    sheetBinding.btnClosePicker.setOnClickListener(v -> bottomSheet.dismiss());

    SharedPreferences salahPrefs = context.getSharedPreferences("deanone_salah_prefs", Context.MODE_PRIVATE);
    String currentSavedMosqueName = salahPrefs.getString("key_selected_masjid_name", "");

    List<MosqueItem> mosqueList = new ArrayList<>();
    MosquePickerAdapter adapter = new MosquePickerAdapter(mosqueList, currentSavedMosqueName, selected -> {
      salahPrefs.edit()
          .putString("key_selected_masjid_name", selected.getName())
          .putString("key_selected_masjid_address", selected.getAddress())
          .putString("key_selected_masjid_dist", selected.getDistanceFormatted())
          .putString("key_selected_masjid_walk", selected.getWalkingTimeFormatted())
          .putLong("key_selected_masjid_lat", Double.doubleToLongBits(selected.getLatitude()))
          .putLong("key_selected_masjid_lng", Double.doubleToLongBits(selected.getLongitude()))
          .apply();

      String dist = selected.getDistanceFormatted() != null ? " • " + selected.getDistanceFormatted() : "";
      trackerBinding.tvSelectedMasjidName.setText(selected.getName() + dist);

      Toast.makeText(context, selected.getName() + (isBn ? " জামাতের জন্য নির্বাচিত হয়েছে" : " selected for Jamat"), Toast.LENGTH_SHORT).show();
      bottomSheet.dismiss();
    });

    sheetBinding.rvPickerItems.setLayoutManager(new LinearLayoutManager(context));
    sheetBinding.rvPickerItems.setAdapter(adapter);

    LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
    double userLat = coords != null ? coords.latitude : 23.8103;
    double userLng = coords != null ? coords.longitude : 90.4125;

    // Fetch real-time worldwide & nearby mosques using current GPS / saved coordinates
    MosqueRepository.getInstance().fetchLiveNearbyMosquesAsync(
        userLat, userLng, "all", "", liveMosques -> {
          mosqueList.clear();
          if (liveMosques != null && !liveMosques.isEmpty()) {
            mosqueList.addAll(liveMosques);
          }
          adapter.notifyDataSetChanged();
        }
    );

    sheetBinding.etPickerSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        String query = s.toString().trim();
        MosqueRepository.getInstance().fetchLiveNearbyMosquesAsync(
            userLat, userLng, "all", query, filtered -> {
              mosqueList.clear();
              if (filtered != null && !filtered.isEmpty()) {
                mosqueList.addAll(filtered);
              }
              adapter.notifyDataSetChanged();
            }
        );
      }

      @Override
      public void afterTextChanged(Editable s) {}
    });

    bottomSheet.show();
  }

  private static class MosquePickerAdapter extends RecyclerView.Adapter<MosquePickerAdapter.ViewHolder> {
    private final List<MosqueItem> items;
    private final String currentSelectedName;
    private final java.util.function.Consumer<MosqueItem> onSelect;

    public MosquePickerAdapter(List<MosqueItem> items,
                  String currentSelectedName,
                  java.util.function.Consumer<MosqueItem> onSelect) {
      this.items = items;
      this.currentSelectedName = currentSelectedName;
      this.onSelect = onSelect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      ItemIslamicSelectionRowBinding binding =
          ItemIslamicSelectionRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
      return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      MosqueItem item = items.get(position);
      holder.binding.tvItemTitle.setText(item.getName());
      holder.binding.tvItemSubtitle.setVisibility(View.VISIBLE);

      String distStr = item.getDistanceFormatted() != null ? item.getDistanceFormatted() : "";
      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(holder.itemView.getContext());
      String walkStr = item.getWalkingTimeFormatted() != null ? ((isBn ? "• হাঁটার পথ: " : "• Walk: ") + item.getWalkingTimeFormatted()) : "";
      String addrStr = item.getAddress() != null && !item.getAddress().isEmpty() ? "• "+ item.getAddress() : "";
      holder.binding.tvItemSubtitle.setText(distStr + walkStr + addrStr);

      boolean isSelected = currentSelectedName != null && currentSelectedName.equals(item.getName());
      holder.binding.ivItemCheck.setVisibility(isSelected ? View.VISIBLE : View.GONE);
      holder.binding.layoutSelectionItemRoot.setBackgroundColor(isSelected ? 0x1A34D399 : 0x00000000);

      holder.itemView.setOnClickListener(v -> onSelect.accept(item));
    }

    @Override
    public int getItemCount() {
      return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
      final ItemIslamicSelectionRowBinding binding;

      ViewHolder(ItemIslamicSelectionRowBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
      }
    }
  }
}
