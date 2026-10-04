package com.devflux.deenone.features.amal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.amal.AuthoritativePointsLedgerManager;
import com.devflux.deenone.core.amal.DailyAmalRotationEngine;
import com.devflux.deenone.core.amal.UserLocationTimezoneHelper;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.DailyAmalDao;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.databinding.ItemAmalActivityLogBinding;
import com.devflux.deenone.databinding.ItemIbadahStatRowBinding;
import com.devflux.deenone.databinding.ItemZikirStatRowBinding;
import com.devflux.deenone.databinding.PageAmalAnalyticsBinding;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AmalAnalyticsPageDialog {

  public static void show(@NonNull Context context) {
    FullScreenPageDialog dialog = new FullScreenPageDialog(context);
    PageAmalAnalyticsBinding binding = PageAmalAnalyticsBinding.inflate(LayoutInflater.from(context));
    dialog.setContentView(binding.getRoot());

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // 1. Dual-Language Headers and Static Labels (Rule 5)
    binding.tvAnalyticsHeaderTitle.setText(isBn ? "আমল ও জিকির ড্যাশবোর্ড" : "Amal & Dhikr Dashboard");
    binding.tvAnalyticsBadge.setText(isBn ? "বিস্তারিত অ্যানালিটিক্স" : "Detailed Analytics");
    binding.tvAnalyticsTitle.setText(isBn ? "আমল ও জিকির ড্যাশবোর্ড" : "Amal & Dhikr Dashboard");
    binding.tvAnalyticsSubtitle.setText(isBn ? "আপনার প্রতিদিনের জিকির ও নেক আমলের সার্বিক অগ্রগতি" : "Overall progress of your daily Dhikr & good deeds");

    binding.tvZikirReportHeader.setText(isBn ? "জিকির রিপোর্ট" : "Dhikr Report");
    binding.tvTotalZikirLabel.setText(isBn ? "মোট জিকির" : "Total Dhikr");
    binding.tvAvgZikirLabel.setText(isBn ? "দৈনিক গড় জিকির" : "Daily Average Dhikr");
    binding.tvZikirBreakdownLabel.setText(isBn ? "জিকির টাইপ অনুযায়ী পরিসংখ্যান:" : "Statistics by Dhikr type:");

    binding.tvIbadahReportHeader.setText(isBn ? "অন্যান্য ইবাদত রিপোর্ট" : "Other Ibadah Report");
    binding.tvTotalIbadahLabel.setText(isBn ? "মোট ইবাদত টি" : "Total Ibadah");
    binding.tvCompletedCategoryLabel.setText(isBn ? "সম্পন্ন ক্যাটাগরি" : "Completed Categories");
    binding.tvIbadahBreakdownLabel.setText(isBn ? "ইবাদতের নাম অনুযায়ী পরিসংখ্যান:" : "Statistics by Ibadah name:");

    binding.tvWeeklyTrendHeader.setText(isBn ? "সাপ্তাহিক ট্রেন্ড চিত্র" : "Weekly Trend Chart");
    binding.tvWeeklyTrendSubtitle.setText(isBn ? "গত ৭ দিনের আমল ও জিকির ট্রেন্ড" : "Past 7 days Amal & Dhikr trend");

    binding.tvActivityLogHeader.setText(isBn ? "ইবাদত ও জিকির অ্যাক্টিভিটি লগ" : "Amal & Dhikr Activity Log");

    // 2. Authoritative Points
    int totalPoints = AuthoritativePointsLedgerManager.getLifetimePoints(context);
    binding.tvAnalyticsPointsPill.setText(isBn ? (BengaliNumberUtil.toBengali(totalPoints) + " পয়েন্ট") : (totalPoints + " pts"));

    // Set Filter Tabs Localization
    binding.tabFilterToday.setText(isBn ? "আজ" : "Today");
    binding.tabFilterMonth.setText(isBn ? "মাস" : "Month");
    binding.tabFilterYear.setText(isBn ? "বছর" : "Year");
    binding.tabFilterCustom.setText(isBn ? "কাস্টম" : "Custom");

    // 3. Dynamic Filter Handler with Real Data Loading
    java.util.function.Consumer<Integer> updateFilterData = (mode) -> {
      AppDatabase.databaseWriteExecutor.execute(() -> {
        String today = UserLocationTimezoneHelper.getTodayLocationDateString(context);
        DailyAmalDao dao = AppDatabase.getInstance(context).dailyAmalDao();
        List<DailyAmalEntity> todayEntities = dao.getAmalsByDateSync(today);

        int totalTodayTasbih = DailyAmalRotationEngine.getTodayTotalTasbihCount(context);

        int factor = 1;
        double avgDays = 1.0;
        if (mode == 1) { // Month
          factor = 1;
          avgDays = 30.0;
        } else if (mode == 2) { // Year
          factor = 1;
          avgDays = 365.0;
        } else if (mode == 3) { // Custom
          factor = 1;
          avgDays = 7.0;
        }

        int currentZikir = totalTodayTasbih * factor;
        double avgZikir = currentZikir > 0 ? (currentZikir / avgDays) : 0.0;

        // Count actual completed deeds
        int completedCount = 0;
        Map<String, Boolean> completedMap = new HashMap<>();
        if (todayEntities != null) {
          for (DailyAmalEntity entity : todayEntities) {
            if (entity.isCompleted()) {
              completedCount++;
              completedMap.put(entity.getTitle(), true);
              completedMap.put(entity.getAmalCode(), true);
            }
          }
        }

        final int finalZikir = currentZikir;
        final double finalAvgZikir = avgZikir;
        final int finalCompletedCount = completedCount;
        final Map<String, Boolean> finalCompletedMap = completedMap;
        final int finalFactor = factor;

        binding.getRoot().post(() -> {
          binding.tvTotalZikirCount.setText(isBn ? (BengaliNumberUtil.toBengali(finalZikir) + " বার") : (finalZikir + " times"));
          binding.tvAvgZikirCount.setText(isBn ? (BengaliNumberUtil.toBengali(String.format(Locale.US, "%.1f", finalAvgZikir)) + " / দিন") : (String.format(Locale.US, "%.1f", finalAvgZikir) + " / day"));
          binding.tvTotalIbadahCount.setText(isBn ? (BengaliNumberUtil.toBengali(finalCompletedCount) + " টি") : (finalCompletedCount + " items"));
          binding.tvCompletedCategoryCount.setText(isBn ? (BengaliNumberUtil.toBengali(finalCompletedCount) + " টি") : (finalCompletedCount + " categories"));

          populateZikirStats(context, binding.layoutZikirStatsList, finalFactor, isBn);
          populateIbadahStats(context, binding.layoutIbadahStatsList, finalCompletedMap, isBn);
        });
      });
    };

    // Initial Load
    updateFilterData.accept(0);

    // 4. Time Filter Tab Switching
    View.OnClickListener tabClickListener = v -> {
      int inactiveBg = ContextCompat.getColor(context, R.color.bg_card_secondary);
      int inactiveText = ContextCompat.getColor(context, R.color.text_secondary);
      int activeBg = ContextCompat.getColor(context, R.color.primary_green);
      int activeText = ContextCompat.getColor(context, R.color.text_primary);

      // Reset all tabs
      binding.tabFilterToday.setBackgroundTintList(ColorStateList.valueOf(inactiveBg));
      binding.tabFilterToday.setTextColor(inactiveText);
      binding.tabFilterMonth.setBackgroundTintList(ColorStateList.valueOf(inactiveBg));
      binding.tabFilterMonth.setTextColor(inactiveText);
      binding.tabFilterYear.setBackgroundTintList(ColorStateList.valueOf(inactiveBg));
      binding.tabFilterYear.setTextColor(inactiveText);
      binding.tabFilterCustom.setBackgroundTintList(ColorStateList.valueOf(inactiveBg));
      binding.tabFilterCustom.setTextColor(inactiveText);

      // Set active tab
      TextView activeTab = (TextView) v;
      activeTab.setBackgroundTintList(ColorStateList.valueOf(activeBg));
      activeTab.setTextColor(activeText);

      int mode = 0;
      if (v == binding.tabFilterMonth) mode = 1;
      else if (v == binding.tabFilterYear) mode = 2;
      else if (v == binding.tabFilterCustom) mode = 3;

      updateFilterData.accept(mode);
    };

    TouchAnimationUtil.attachTouchSpring(binding.tabFilterToday);
    TouchAnimationUtil.attachTouchSpring(binding.tabFilterMonth);
    TouchAnimationUtil.attachTouchSpring(binding.tabFilterYear);
    TouchAnimationUtil.attachTouchSpring(binding.tabFilterCustom);

    binding.tabFilterToday.setOnClickListener(tabClickListener);
    binding.tabFilterMonth.setOnClickListener(tabClickListener);
    binding.tabFilterYear.setOnClickListener(tabClickListener);
    binding.tabFilterCustom.setOnClickListener(tabClickListener);

    // 5. Section 3: Weekly Trend Chart
    setupWeeklyTrendChart(context, binding, isBn);

    // 6. Section 4: Activity Log
    populateActivityLogs(context, binding, isBn);

    // 7. Right Back Button (Rule 13)
    TouchAnimationUtil.attachTouchSpring(binding.btnCloseAnalytics);
    binding.btnCloseAnalytics.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
  }

  private static void populateZikirStats(Context context, LinearLayout container, int factor, boolean isBn) {
    container.removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(context);

    List<DailyAmalRotationEngine.TasbihChallengeInfo> allChallenges = DailyAmalRotationEngine.getAllTasbihChallenges(context);

    for (int i = 0; i < allChallenges.size(); i++) {
      ItemZikirStatRowBinding rowBinding = ItemZikirStatRowBinding.inflate(inflater, container, false);
      DailyAmalRotationEngine.TasbihChallengeInfo challengeInfo = allChallenges.get(i);
      rowBinding.tvZikirTitle.setText(challengeInfo.duaTitle);

      int todayCount = DailyAmalRotationEngine.getTodayDhikrCount(context, i);
      int todayDoneRounds = DailyAmalRotationEngine.getTodayDhikrCompletedTimes(context, i);
      int count = (todayCount + (todayDoneRounds * 100)) * factor;

      rowBinding.tvZikirCount.setText(isBn ? (BengaliNumberUtil.toBengali(count) + " বার") : (count + " times"));
      if (count > 0) {
        rowBinding.tvZikirCount.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
      } else {
        rowBinding.tvZikirCount.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
      }

      container.addView(rowBinding.getRoot());
    }
  }

  private static void populateIbadahStats(Context context, LinearLayout container, Map<String, Boolean> completedMap, boolean isBn) {
    container.removeAllViews();
    LayoutInflater inflater = LayoutInflater.from(context);

    String[] ibadahListBn = new String[]{
        "ইসলামিক বই পড়া",
        "প্রত্যেক ফরয সালাতের পর নিম্নোক্ত জিকির করা",
        "১০০০ বার ইস্তেগফার পাঠ",
        "আজানের জবাব দেওয়া",
        "হারাম গান শোনা থেকে বিরত থেকেছি",
        "আজ জবানের হেফাজত করেছি",
        "আজ নজরের হেফাজত করেছি",
        "মাগরিবের পর সূরা ওয়াকিয়া",
        "ফজরের পর সূরা ইয়াসিন",
        "এশার পর এবং রাতে ঘুমানোর আগে সূরা আল-মুলক তিলাওয়াত বা শ্রবণ",
        "অন্তত একজন মুসলিমকে নামাজের কথা বলা",
        "অন্তত একজন মুসলিমকে সালাম দেওয়া",
        "অন্তত ১০টা দান করা",
        "অন্তত ১০০বার সুবহানাল্লাহ পাঠ করুন",
        "৫ ওয়াক্ত সালাত আদায় করেছি",
        "১২ রাকাআত সুন্নাতে মু'আক্কাদাহ",
        "আয়াতুল কুরসি পাঠ",
        "মিসওয়াক করা",
        "বেশি দূরূদ পাঠ করুন",
        "পিতামাতার জন্য দোয়া",
        "কুরআন তিলাওয়াত",
        "জুমার দিনে সূরা কাহফ",
        "তাহাজ্জুদ নামাজ",
        "২ রাকাত নফল নামাজ"
    };

    String[] ibadahListEn = new String[]{
        "Islamic Book Reading",
        "Dhikr after every obligatory prayer",
        "Recited Istighfar 1000 times",
        "Answered the Azan",
        "Refrained from prohibited music",
        "Guarded the tongue today",
        "Guarded the gaze today",
        "Surah Al-Waqiah after Maghrib",
        "Surah Yasin after Fajr",
        "Surah Al-Mulk before sleeping",
        "Reminded a fellow Muslim of Salah",
        "Greeted a fellow Muslim with Salam",
        "Gave at least 10 in charity",
        "Recited SubhanAllah at least 100 times",
        "Performed 5 daily obligatory prayers",
        "12 Rak'ah Sunnah Mu'akkadah",
        "Recited Ayat al-Kursi",
        "Used Miswak",
        "Sent abundant blessings upon Prophet Muhammad",
        "Dua for parents",
        "Quran recitation",
        "Surah Al-Kahf on Friday",
        "Tahajjud prayer",
        "2 Rak'ah Nafl prayer"
    };

    String[] ibadahList = isBn ? ibadahListBn : ibadahListEn;

    for (int i = 0; i < ibadahList.length; i++) {
      String title = ibadahList[i];
      ItemIbadahStatRowBinding rowBinding = ItemIbadahStatRowBinding.inflate(inflater, container, false);
      rowBinding.tvIbadahTitle.setText(title);

      boolean isCompleted = (completedMap != null) && (
          Boolean.TRUE.equals(completedMap.get(title)) ||
          Boolean.TRUE.equals(completedMap.get(ibadahListBn[i]))
      );
      int count = isCompleted ? 1 : 0;

      rowBinding.tvIbadahCount.setText(isBn ? (BengaliNumberUtil.toBengali(count) + " টি") : (count + " done"));
      if (count > 0) {
        rowBinding.tvIbadahCount.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg_card_active)));
        rowBinding.tvIbadahCount.setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
      } else {
        rowBinding.tvIbadahCount.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg_card_secondary)));
        rowBinding.tvIbadahCount.setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
      }

      container.addView(rowBinding.getRoot());
    }
  }

  private static void setupWeeklyTrendChart(Context context, PageAmalAnalyticsBinding binding, boolean isBn) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      String[] daysBn = new String[]{"রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি"};
      String[] daysEn = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
      String[] days = isBn ? daysBn : daysEn;

      TextView[] tvVals = new TextView[]{
          binding.tvBarVal0, binding.tvBarVal1, binding.tvBarVal2,
          binding.tvBarVal3, binding.tvBarVal4, binding.tvBarVal5, binding.tvBarVal6
      };
      View[] viewBars = new View[]{
          binding.viewBar0, binding.viewBar1, binding.viewBar2,
          binding.viewBar3, binding.viewBar4, binding.viewBar5, binding.viewBar6
      };
      TextView[] tvLabels = new TextView[]{
          binding.tvBarLabel0, binding.tvBarLabel1, binding.tvBarLabel2,
          binding.tvBarLabel3, binding.tvBarLabel4, binding.tvBarLabel5, binding.tvBarLabel6
      };

      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
      DailyAmalDao dao = AppDatabase.getInstance(context).dailyAmalDao();

      int[] vals = new int[7];
      int[] dates = new int[7];
      String[] dayNames = new String[7];

      for (int i = 6; i >= 0; i--) {
        Calendar dayCal = (Calendar) cal.clone();
        dayCal.add(Calendar.DAY_OF_YEAR, -(6 - i));
        String dateStr = sdf.format(dayCal.getTime());
        int completedAmals = dao.getCompletedCountByDateSync(dateStr);

        int dayOfWeek = dayCal.get(Calendar.DAY_OF_WEEK); // 1 = Sunday
        int dayIndex = (dayOfWeek - 1) % 7;
        dayNames[i] = days[dayIndex];
        dates[i] = dayCal.get(Calendar.DAY_OF_MONTH);
        vals[i] = completedAmals;
      }

      binding.getRoot().post(() -> {
        for (int i = 0; i < 7; i++) {
          tvVals[i].setText(isBn ? BengaliNumberUtil.toBengali(vals[i]) : String.valueOf(vals[i]));
          tvLabels[i].setText(dayNames[i] + "\n" + (isBn ? BengaliNumberUtil.toBengali(dates[i]) : String.valueOf(dates[i])));

          if (i == 6) { // Current day highlighted
            tvLabels[i].setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
          } else {
            tvLabels[i].setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
          }

          if (vals[i] > 0) {
            tvVals[i].setTextColor(ContextCompat.getColor(context, R.color.accent_mint));
            viewBars[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
            int heightDp = Math.min(110, Math.max(25, vals[i] * 35));
            int heightPx = (int) (heightDp * context.getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewBars[i].getLayoutParams();
            lp.height = heightPx;
            viewBars[i].setLayoutParams(lp);
          } else {
            tvVals[i].setTextColor(ContextCompat.getColor(context, R.color.text_secondary));
            viewBars[i].setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.bg_card_secondary)));
            int heightPx = (int) (8 * context.getResources().getDisplayMetrics().density);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewBars[i].getLayoutParams();
            lp.height = heightPx;
            viewBars[i].setLayoutParams(lp);
          }
        }
      });
    });
  }

  private static void populateActivityLogs(Context context, PageAmalAnalyticsBinding binding, boolean isBn) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      DailyAmalDao dao = AppDatabase.getInstance(context).dailyAmalDao();
      String today = UserLocationTimezoneHelper.getTodayLocationDateString(context);
      List<DailyAmalEntity> completedList = dao.getAmalsByDateSync(today);

      class ActivityItem {
        final String title;
        final String time;
        final int points;

        ActivityItem(String title, String time, int points) {
          this.title = title;
          this.time = time;
          this.points = points;
        }
      }

      List<ActivityItem> list = new ArrayList<>();
      if (completedList != null) {
        for (DailyAmalEntity amal : completedList) {
          if (amal.isCompleted()) {
            list.add(new ActivityItem(amal.getTitle(), today + " " + (isBn ? "আজকের আমল" : "Today's Deed"), amal.getPoints()));
          }
        }
      }

      int tasbihDoneTimes = DailyAmalRotationEngine.getTodayTasbihCompletedTimes(context);
      if (tasbihDoneTimes > 0) {
        DailyAmalRotationEngine.TasbihChallengeInfo challenge = DailyAmalRotationEngine.getTodayTasbihChallenge(context);
        list.add(new ActivityItem(challenge.duaTitle, today + " " + (isBn ? "তাসবীহ পূর্ণ" : "Tasbih Goal"), challenge.rewardPoints * tasbihDoneTimes));
      }

      binding.getRoot().post(() -> {
        binding.layoutActivityLogList.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(context);

        if (list.isEmpty()) {
          binding.tvActivityRecordCountBadge.setText(isBn ? "০ টি রেকর্ড" : "0 records");
          binding.layoutActivityLogEmpty.setVisibility(View.VISIBLE);
          binding.layoutActivityLogList.setVisibility(View.GONE);
          binding.tvActivityLogEmptyMessage.setText(isBn
              ? "নির্বাচন করা সময়ের মধ্যে কোনো আমল রেকর্ড করা হয়নি।"
              : "No deeds recorded within the selected timeframe.");
        } else {
          binding.tvActivityRecordCountBadge.setText(isBn
              ? (BengaliNumberUtil.toBengali(list.size()) + " টি রেকর্ড")
              : (list.size() + " records"));
          binding.layoutActivityLogEmpty.setVisibility(View.GONE);
          binding.layoutActivityLogList.setVisibility(View.VISIBLE);

          for (ActivityItem item : list) {
            ItemAmalActivityLogBinding rowBinding = ItemAmalActivityLogBinding.inflate(inflater, binding.layoutActivityLogList, false);
            rowBinding.tvActivityTitle.setText(item.title);
            rowBinding.tvActivityTimestamp.setText(item.time);
            rowBinding.tvActivityCount.setText(isBn ? "১ টি" : "1 done");
            rowBinding.tvActivityPoints.setText("+" + (isBn ? BengaliNumberUtil.toBengali(item.points) : String.valueOf(item.points)) + " pts");
            binding.layoutActivityLogList.addView(rowBinding.getRoot());
          }
        }
      });
    });
  }
}
