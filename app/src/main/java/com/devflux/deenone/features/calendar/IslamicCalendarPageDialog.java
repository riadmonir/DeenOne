package com.devflux.deenone.features.calendar;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.data.repository.IslamicCalendarRepository;
import com.devflux.deenone.databinding.PageIslamicCalendarBinding;
import com.devflux.deenone.features.calendar.adapter.HijriDaysGridAdapter;
import com.devflux.deenone.features.calendar.adapter.IslamicEventAdapter;
import com.devflux.deenone.features.calendar.adapter.IslamicMonthAdapter;
import com.devflux.deenone.features.calendar.model.HijriDayGridItem;
import com.devflux.deenone.features.calendar.model.IslamicEventItem;
import com.devflux.deenone.features.calendar.model.IslamicMonthItem;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IslamicCalendarPageDialog {

  private static final String PREF_NAME = "deanone_hijri_prefs";
  private static final String KEY_HIJRI_ADJUSTMENT = "hijri_day_adjustment";

  private final Context context;
  private final Dialog dialog;
  private final PageIslamicCalendarBinding binding;

  private int currentAdjustment = 0;
  private int selectedMonthOffset = 0; // 0 = current month, -1 = prev, +1 = next

  private final HijriDaysGridAdapter daysGridAdapter;
  private final IslamicEventAdapter eventsAdapter;
  private final IslamicMonthAdapter monthsAdapter;

  private List<IslamicEventItem> allEventsList = new ArrayList<>();
  private String currentCategoryFilter = "all";
  private String currentSearchQuery = "";

  public static void show(Context context) {
    new IslamicCalendarPageDialog(context).showDialog();
  }

  public IslamicCalendarPageDialog(Context context) {
    this.context = context;
    this.dialog = new com.devflux.deenone.core.ui.FullScreenPageDialog(context);
    this.binding = PageIslamicCalendarBinding.inflate(LayoutInflater.from(context));
    this.dialog.setContentView(binding.getRoot());

    // Load saved moon sighting adjustment from HijriCalendarUtil
    this.currentAdjustment = HijriCalendarUtil.getAdjustmentDays(context);

    // Adapters
    this.daysGridAdapter = new HijriDaysGridAdapter(this::onDayClicked);
    this.eventsAdapter = new IslamicEventAdapter();
    this.monthsAdapter = new IslamicMonthAdapter();

    initViews();
  }

  private void initViews() {
    // 1. Back button
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnBackCalendar);
    binding.btnBackCalendar.setOnClickListener(v -> dialog.dismiss());

    // 2. Adjust button in top bar
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCalendarAdjustMenu);
    binding.btnCalendarAdjustMenu.setOnClickListener(v -> {
      selectTab(1);
      binding.cardDisclaimerNotice.requestFocus();
    });

    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnCalendarNotification);
    binding.btnCalendarNotification.setOnClickListener(v -> {
      if (context instanceof com.devflux.deenone.MainActivity) {
        ((com.devflux.deenone.MainActivity) context).showNotificationHistorySheet();
      }
    });

    // 3. Tab switching
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabHijriDays);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabIslamicEvents);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.tabTwelveMonths);

    binding.tabHijriDays.setOnClickListener(v -> selectTab(1));
    binding.tabIslamicEvents.setOnClickListener(v -> selectTab(2));
    binding.tabTwelveMonths.setOnClickListener(v -> selectTab(3));

    // Dynamic localization
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    if (!isBn) {
      binding.tabHijriDays.setText("Hijri Days & Month");
      binding.tabIslamicEvents.setText("Islamic Events");
      binding.tabTwelveMonths.setText("12 Islamic Months");
      binding.tvMoonAdjustmentLabel.setText("Moon Sighting Adjustment:");
      binding.btnAdjMinus1.setText("-1 Day");
      binding.btnAdjDefault.setText("Default");
      binding.btnAdjPlus1.setText("+1 Day");
      binding.etEventsSearch.setHint("Search Islamic events...");
      binding.chipEventsAll.setText("All Events");
      binding.chipEventsFardhEid.setText("Eid & Fardh");
      binding.chipEventsSunnahFast.setText("Sunnah Fasts");
      binding.chipEventsHolyNights.setText("Sacred Nights");
    } else {
      binding.tabHijriDays.setText("হিজরি মাস ও দিন");
      binding.tabIslamicEvents.setText("ইসলামিক দিবস ও ঘটনাবলী");
      binding.tabTwelveMonths.setText("১২টি মাসের পরিচয়");
      binding.tvMoonAdjustmentLabel.setText("চাঁদ দেখা সমন্বয়:");
      binding.btnAdjMinus1.setText("-১ দিন");
      binding.btnAdjDefault.setText("ডিফল্ট");
      binding.btnAdjPlus1.setText("+১ দিন");
      binding.etEventsSearch.setHint("ইসলামিক দিবস খুঁজুন...");
      binding.chipEventsAll.setText("সব দিবস");
      binding.chipEventsFardhEid.setText("ঈদ ও ফরজ");
      binding.chipEventsSunnahFast.setText("সুন্নাত রোজা");
      binding.chipEventsHolyNights.setText("মর্যাদাপূর্ণ রাত");
    }

    // 4. Tab 1: Setup Days Grid
    binding.rvHijriDaysGrid.setLayoutManager(new GridLayoutManager(context, 5));
    binding.rvHijriDaysGrid.setAdapter(daysGridAdapter);

    // Moon sighting adjustment chips
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnAdjMinus1);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnAdjDefault);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnAdjPlus1);

    binding.btnAdjMinus1.setOnClickListener(v -> setMoonAdjustment(-1));
    binding.btnAdjDefault.setOnClickListener(v -> setMoonAdjustment(0));
    binding.btnAdjPlus1.setOnClickListener(v -> setMoonAdjustment(1));
    updateAdjustmentChipsUI();

    // Month Navigation
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnPrevHijriMonth);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.btnNextHijriMonth);

    binding.btnPrevHijriMonth.setOnClickListener(v -> {
      selectedMonthOffset--;
      refreshDaysGrid();
    });
    binding.btnNextHijriMonth.setOnClickListener(v -> {
      selectedMonthOffset++;
      refreshDaysGrid();
    });

    // 5. Tab 2: Setup Events
    binding.rvIslamicEventsList.setLayoutManager(new LinearLayoutManager(context));
    binding.rvIslamicEventsList.setAdapter(eventsAdapter);

    allEventsList = IslamicCalendarRepository.getInstance().getSignificantIslamicEvents(Calendar.getInstance());
    filterEvents();

    // Category Filter Chips
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipEventsAll);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipEventsFardhEid);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipEventsSunnahFast);
    com.devflux.deenone.utils.TouchAnimationUtil.attachTouchSpring(binding.chipEventsHolyNights);

    binding.chipEventsAll.setOnCheckedChangeListener((b, checked) -> {
      if (checked) { currentCategoryFilter = "all"; filterEvents(); }
    });
    binding.chipEventsFardhEid.setOnCheckedChangeListener((b, checked) -> {
      if (checked) { currentCategoryFilter = "fardh_eid"; filterEvents(); }
    });
    binding.chipEventsSunnahFast.setOnCheckedChangeListener((b, checked) -> {
      if (checked) { currentCategoryFilter = "sunnah_fast"; filterEvents(); }
    });
    binding.chipEventsHolyNights.setOnCheckedChangeListener((b, checked) -> {
      if (checked) { currentCategoryFilter = "holy_night"; filterEvents(); }
    });

    // Search text watcher
    binding.etEventsSearch.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentSearchQuery = s.toString().trim();
        filterEvents();
      }
      @Override public void afterTextChanged(Editable s) {}
    });

    // 6. Tab 3: Setup 12 Months
    binding.rvTwelveMonthsList.setLayoutManager(new LinearLayoutManager(context));
    binding.rvTwelveMonthsList.setAdapter(monthsAdapter);
    List<IslamicMonthItem> monthsList = IslamicCalendarRepository.getInstance().get12IslamicMonths();
    monthsAdapter.setItems(monthsList);

    // Initial Data Load
    refreshDaysGrid();
    selectTab(1);
  }

  private void selectTab(int tabIndex) {
    binding.viewHijriDaysTab.setVisibility(tabIndex == 1 ? View.VISIBLE : View.GONE);
    binding.viewIslamicEventsTab.setVisibility(tabIndex == 2 ? View.VISIBLE : View.GONE);
    binding.viewTwelveMonthsTab.setVisibility(tabIndex == 3 ? View.VISIBLE : View.GONE);

    int activeColor = androidx.core.content.ContextCompat.getColor(context, R.color.accent_mint);
    int inactiveColor = androidx.core.content.ContextCompat.getColor(context, R.color.text_secondary);
    binding.tabHijriDays.setTextColor(tabIndex == 1 ? activeColor : inactiveColor);
    binding.tabIslamicEvents.setTextColor(tabIndex == 2 ? activeColor : inactiveColor);
    binding.tabTwelveMonths.setTextColor(tabIndex == 3 ? activeColor : inactiveColor);
  }

  private void setMoonAdjustment(int adj) {
    this.currentAdjustment = adj;
    HijriCalendarUtil.setAdjustmentDays(context, adj);

    updateAdjustmentChipsUI();
    refreshDaysGrid();
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String adjText = isBn
        ? (adj == 0 ? "স্বাভাবিক (০ দিন)" : (adj > 0 ? "+" + BengaliNumberUtil.toBengali(adj) + " দিন" : BengaliNumberUtil.toBengali(adj) + " দিন"))
        : (adj == 0 ? "Default (0 days)" : (adj > 0 ? "+" + adj + " days" : adj + " days"));
    Toast.makeText(context, (isBn ? "হিজরি ক্যালেন্ডার সমন্বয়: " : "Hijri Calendar Adjusted: ") + adjText, Toast.LENGTH_SHORT).show();
  }

  private void updateAdjustmentChipsUI() {
    setChipStyle(binding.btnAdjMinus1, currentAdjustment == -1);
    setChipStyle(binding.btnAdjDefault, currentAdjustment == 0);
    setChipStyle(binding.btnAdjPlus1, currentAdjustment == 1);
  }

  private void setChipStyle(TextView btn, boolean isSelected) {
    if (isSelected) {
      btn.setBackgroundTintList(ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(context, R.color.accent_mint)));
      btn.setTextColor(Color.WHITE);
    } else {
      btn.setBackgroundTintList(ColorStateList.valueOf(androidx.core.content.ContextCompat.getColor(context, R.color.bg_badge_pill)));
      btn.setTextColor(androidx.core.content.ContextCompat.getColor(context, R.color.text_secondary));
    }
  }

  private void refreshDaysGrid() {
    Calendar cal = Calendar.getInstance();
    if (selectedMonthOffset != 0) {
      cal.add(Calendar.DAY_OF_YEAR, selectedMonthOffset * 29);
    }
    if (currentAdjustment != 0) {
      cal.add(Calendar.DAY_OF_YEAR, currentAdjustment);
    }

    HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getRealHijriDate(cal);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String monthHeader = isBn
        ? ("চলতি হিজরি মাস: " + hijri.monthNameBengali + " • " + BengaliNumberUtil.toBengali(hijri.year) + " হিজরি")
        : ("Current Hijri Month: " + hijri.monthNameEnglish + " • " + hijri.year + " AH");
    binding.tvCurrentHijriMonthHeader.setText(monthHeader);

    // Generate 30 days grid list
    List<HijriDayGridItem> days = new ArrayList<>();
    int todayHijriDay = (selectedMonthOffset == 0) ? hijri.day : -1;

    for (int d = 1; d <= 30; d++) {
      String dayStr = isBn ? BengaliNumberUtil.toBengali(d) : String.valueOf(d);
      boolean isToday = (d == todayHijriDay);
      boolean isAyyamBeed = (d == 13 || d == 14 || d == 15);

      String badgeLabel;
      if (isToday) {
        badgeLabel = isBn ? "আজ" : "Today";
      } else if (isAyyamBeed) {
        badgeLabel = isBn ? "আইয়ামে বীজ" : "Ayyam al-Beed";
      } else {
        badgeLabel = isBn ? "তারিখ" : "Date";
      }

      // Approximate corresponding Gregorian date for that day
      Calendar dayCal = (Calendar) cal.clone();
      dayCal.add(Calendar.DAY_OF_YEAR, (d - hijri.day));
      String gregDate = new SimpleDateFormat("dd MMMM yyyy, EEEE", isBn ? new Locale("bn", "BD") : Locale.ENGLISH).format(dayCal.getTime());

      String sunnahFast = isAyyamBeed
          ? (isBn ? "আজকের দিনে আইয়ামে বীজের নফল রোজা রাখা সুন্নাত ও মহা সওয়াবের কাজ।"
                  : "Observing the Ayyam al-Beed fast on this day is an authentic Sunnah of great reward.")
          : "";

      days.add(new HijriDayGridItem(d, dayStr, badgeLabel, isToday, isAyyamBeed, null, gregDate, sunnahFast));
    }

    daysGridAdapter.setItems(days);
  }

  private void onDayClicked(HijriDayGridItem item) {
    BottomSheetDialog bottomSheet = new BottomSheetDialog(context);
    View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_common_info, null, false);
    bottomSheet.setContentView(view);

    TextView tvTitle = view.findViewById(R.id.tvSheetTitle);
    TextView tvDesc = view.findViewById(R.id.tvSheetDescription);
    View btnClose = view.findViewById(R.id.btnCloseSheet);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    if (tvTitle != null) {
      tvTitle.setText(isBn ? (item.dayNumberBn + " হিজরি তারিখ") : ("Hijri Day " + item.dayNumberBn));
    }
    if (tvDesc != null) {
      StringBuilder sb = new StringBuilder();
      sb.append(isBn ? "ইংরেজি তারিখ: " : "Gregorian Date: ").append(item.gregorianDateStr).append("\n\n");
      if (item.isToday) {
        sb.append(isBn ? ("আজ চলতি হিজরি মাসের " + item.dayNumberBn + " তারিখ।\n\n") : ("Today is Hijri day " + item.dayNumberBn + " of the current month.\n\n"));
      }
      if (item.isAyyamAlBeed) {
        sb.append(isBn
            ? "আইয়ামে বীজ: চাঁদের ১৩, ১৪ ও ১৫ তারিখের রোজা রাখা সারা বছর রোজা রাখার সমান সওয়াব (সহীহ বুখারী: ১৯৮১)।\n\n"
            : "Ayyam al-Beed: Fasting on the 13th, 14th, and 15th of the lunar month carries the reward of fasting the entire year (Sahih al-Bukhari: 1981).\n\n");
      }
      sb.append(isBn
          ? "পবিত্র হিজরি ক্যালেন্ডার অনুযায়ী প্রতিটি দিন আল্লাহর সন্তুষ্টি ও নেক আমলে অতিবাহিত করুন।"
          : "Spend each day of the sacred Hijri calendar in good deeds and obedience to Allah.");
      tvDesc.setText(sb.toString());
    }
    if (btnClose != null) {
      btnClose.setOnClickListener(v -> bottomSheet.dismiss());
    }

    bottomSheet.show();
  }

  private void filterEvents() {
    List<IslamicEventItem> filtered = new ArrayList<>();
    for (IslamicEventItem item : allEventsList) {
      boolean matchesCategory = currentCategoryFilter.equals("all") ||
          (item.getCategory() != null && item.getCategory().equalsIgnoreCase(currentCategoryFilter));

      boolean matchesSearch = currentSearchQuery.isEmpty() ||
          item.getTitle().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
          item.getHijriDateString().toLowerCase().contains(currentSearchQuery.toLowerCase()) ||
          item.getDescription().toLowerCase().contains(currentSearchQuery.toLowerCase());

      if (matchesCategory && matchesSearch) {
        filtered.add(item);
      }
    }
    eventsAdapter.setItems(filtered);
  }

  public void showDialog() {
    if (!dialog.isShowing()) {
      dialog.show();
    }
  }
}
