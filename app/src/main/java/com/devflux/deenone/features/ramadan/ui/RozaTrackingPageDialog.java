package com.devflux.deenone.features.ramadan.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.fasting.FastingTrackerManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.databinding.PageRozaTrackingBinding;
import com.devflux.deenone.features.ramadan.adapter.RozaCalendarAdapter;
import com.devflux.deenone.features.ramadan.model.RozaCalendarDay;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.utils.BengaliCalendarUtil;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RozaTrackingPageDialog {

    public static void show(@NonNull AppCompatActivity activity, @Nullable Runnable onDataChanged) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageRozaTrackingBinding binding = PageRozaTrackingBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header Localization
        binding.tvRozaTrackingHeaderTitle.setText(isBn ? "রোজা ট্র্যাকিং" : "Roza Tracking");

        // Weekday Headers Localization
        if (isBn) {
            binding.tvDaySun.setText("রবি");
            binding.tvDayMon.setText("সোম");
            binding.tvDayTue.setText("মঙ্গল");
            binding.tvDayWed.setText("বুধ");
            binding.tvDayThu.setText("বৃহস্পতি");
            binding.tvDayFri.setText("শুক্র");
            binding.tvDaySat.setText("শনি");
        } else {
            binding.tvDaySun.setText("Sun");
            binding.tvDayMon.setText("Mon");
            binding.tvDayTue.setText("Tue");
            binding.tvDayWed.setText("Wed");
            binding.tvDayThu.setText("Thu");
            binding.tvDayFri.setText("Fri");
            binding.tvDaySat.setText("Sat");
        }
        binding.tvDayFri.setTextColor(ContextCompat.getColor(activity, R.color.accent_red));

        // State variables
        final Calendar currentCal = Calendar.getInstance();
        final int[] displayedYear = new int[]{currentCal.get(Calendar.YEAR)};
        final int[] displayedMonth = new int[]{currentCal.get(Calendar.MONTH)}; // 0-based

        final RozaCalendarDay[] selectedDay = new RozaCalendarDay[1];

        // RecyclerView setup with 7 columns
        binding.rvRozaCalendarGrid.setLayoutManager(new GridLayoutManager(activity, 7));
        final RozaCalendarAdapter[] adapterHolder = new RozaCalendarAdapter[1];

        // Attach Spring Touch Effects
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseRozaTracking);
        TouchAnimationUtil.attachTouchSpring(binding.btnPrevMonth);
        TouchAnimationUtil.attachTouchSpring(binding.btnNextMonth);
        TouchAnimationUtil.attachTouchSpring(binding.btnYesFasting);
        TouchAnimationUtil.attachTouchSpring(binding.btnNotYetFasting);

        // Close Button
        binding.btnCloseRozaTracking.setOnClickListener(v -> dialog.dismiss());

        // Refresh Function
        Runnable refreshCalendarAndCard = new Runnable() {
            @Override
            public void run() {
                // 1. Month & Year Title
                Calendar calDisplay = Calendar.getInstance();
                calDisplay.set(Calendar.YEAR, displayedYear[0]);
                calDisplay.set(Calendar.MONTH, displayedMonth[0]);
                calDisplay.set(Calendar.DAY_OF_MONTH, 1);

                if (isBn) {
                    String monthName = BengaliNumberUtil.getBengaliMonthName(displayedMonth[0]);
                    String yearStr = BengaliNumberUtil.toBengali(displayedYear[0]);
                    binding.tvCurrentMonthYear.setText(monthName + " " + yearStr);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
                    binding.tvCurrentMonthYear.setText(sdf.format(calDisplay.getTime()));
                }

                // 2. Subtitle: Hijri Date Range for this month
                Calendar calStart = (Calendar) calDisplay.clone();
                calStart.set(Calendar.DAY_OF_MONTH, 1);

                Calendar calEnd = (Calendar) calDisplay.clone();
                int maxDaysInMonth = calDisplay.getActualMaximum(Calendar.DAY_OF_MONTH);
                calEnd.set(Calendar.DAY_OF_MONTH, maxDaysInMonth);

                HijriCalendarUtil.HijriDateResult hStart = HijriCalendarUtil.getAdjustedHijriDate(activity, calStart);
                HijriCalendarUtil.HijriDateResult hEnd = HijriCalendarUtil.getAdjustedHijriDate(activity, calEnd);

                if (hStart != null && hEnd != null) {
                    if (isBn) {
                        String startStr = BengaliNumberUtil.toBengali(hStart.day) + " " + hStart.monthNameBengali + ", " + BengaliNumberUtil.toBengali(hStart.year) + " হিজরী";
                        String endStr = BengaliNumberUtil.toBengali(hEnd.day) + " " + hEnd.monthNameBengali + ", " + BengaliNumberUtil.toBengali(hEnd.year) + " হিজরী";
                        binding.tvMonthHijriRange.setText(startStr + " থেকে " + endStr);
                    } else {
                        String startStr = hStart.day + " " + hStart.monthNameEnglish + ", " + hStart.year + " AH";
                        String endStr = hEnd.day + " " + hEnd.monthNameEnglish + ", " + hEnd.year + " AH";
                        binding.tvMonthHijriRange.setText(startStr + " to " + endStr);
                    }
                }

                // 3. Build 7-column calendar day items
                List<RozaCalendarDay> dayList = new ArrayList<>();
                int firstDayOfWeek = calStart.get(Calendar.DAY_OF_WEEK); // Sunday = 1, Monday = 2...
                int leadingDays = firstDayOfWeek - Calendar.SUNDAY;

                // A. Previous Month Days
                Calendar calPrev = (Calendar) calStart.clone();
                calPrev.add(Calendar.MONTH, -1);
                int prevMaxDays = calPrev.getActualMaximum(Calendar.DAY_OF_MONTH);

                for (int i = leadingDays - 1; i >= 0; i--) {
                    int dayNum = prevMaxDays - i;
                    Calendar c = (Calendar) calPrev.clone();
                    c.set(Calendar.DAY_OF_MONTH, dayNum);
                    String dateKey = FastingTrackerManager.getDateKey(c);
                    int status = FastingTrackerManager.getInstance().getFastingStatus(activity, dateKey);
                    dayList.add(new RozaCalendarDay(
                            dayNum, calPrev.get(Calendar.MONTH), calPrev.get(Calendar.YEAR),
                            false, c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY,
                            false, false, status, dateKey, c
                    ));
                }

                // B. Current Month Days
                Calendar todayCal = Calendar.getInstance();
                boolean isCurrentRealMonth = (todayCal.get(Calendar.YEAR) == displayedYear[0] &&
                        todayCal.get(Calendar.MONTH) == displayedMonth[0]);
                int todayDayNum = isCurrentRealMonth ? todayCal.get(Calendar.DAY_OF_MONTH) : -1;

                RozaCalendarDay candidateSelected = null;

                for (int d = 1; d <= maxDaysInMonth; d++) {
                    Calendar c = (Calendar) calStart.clone();
                    c.set(Calendar.DAY_OF_MONTH, d);
                    String dateKey = FastingTrackerManager.getDateKey(c);
                    int status = FastingTrackerManager.getInstance().getFastingStatus(activity, dateKey);
                    boolean isToday = (d == todayDayNum);

                    boolean isSelected = false;
                    if (selectedDay[0] != null) {
                        isSelected = dateKey.equals(selectedDay[0].dateKey);
                    } else if (isToday) {
                        isSelected = true;
                    } else if (selectedDay[0] == null && candidateSelected == null && d == 1) {
                        isSelected = true;
                    }

                    RozaCalendarDay dayObj = new RozaCalendarDay(
                            d, displayedMonth[0], displayedYear[0],
                            true, c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY,
                            isToday, isSelected, status, dateKey, c
                    );

                    if (isSelected) {
                        candidateSelected = dayObj;
                    }

                    dayList.add(dayObj);
                }

                // C. Trailing Days from Next Month to complete rows
                int totalSoFar = dayList.size();
                int trailingDays = (7 - (totalSoFar % 7)) % 7;
                Calendar calNext = (Calendar) calStart.clone();
                calNext.add(Calendar.MONTH, 1);

                for (int d = 1; d <= trailingDays; d++) {
                    Calendar c = (Calendar) calNext.clone();
                    c.set(Calendar.DAY_OF_MONTH, d);
                    String dateKey = FastingTrackerManager.getDateKey(c);
                    int status = FastingTrackerManager.getInstance().getFastingStatus(activity, dateKey);
                    dayList.add(new RozaCalendarDay(
                            d, calNext.get(Calendar.MONTH), calNext.get(Calendar.YEAR),
                            false, c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY,
                            false, false, status, dateKey, c
                    ));
                }

                if (candidateSelected != null) {
                    selectedDay[0] = candidateSelected;
                } else if (!dayList.isEmpty() && selectedDay[0] == null) {
                    selectedDay[0] = dayList.get(Math.min(leadingDays, dayList.size() - 1));
                    selectedDay[0].isSelected = true;
                }

                // Update adapter
                adapterHolder[0].setDays(dayList);

                // 4. Update Bottom Action Card
                updateBottomCard(activity, binding, isBn, selectedDay[0]);
            }
        };

        // Day click listener
        adapterHolder[0] = new RozaCalendarAdapter(day -> {
            selectedDay[0] = day;
            refreshCalendarAndCard.run();
        });
        binding.rvRozaCalendarGrid.setAdapter(adapterHolder[0]);

        // Month Navigation Clicks
        binding.btnPrevMonth.setOnClickListener(v -> {
            displayedMonth[0]--;
            if (displayedMonth[0] < 0) {
                displayedMonth[0] = 11;
                displayedYear[0]--;
            }
            selectedDay[0] = null;
            refreshCalendarAndCard.run();
        });

        binding.btnNextMonth.setOnClickListener(v -> {
            displayedMonth[0]++;
            if (displayedMonth[0] > 11) {
                displayedMonth[0] = 0;
                displayedYear[0]++;
            }
            selectedDay[0] = null;
            refreshCalendarAndCard.run();
        });

        // Fasting Confirmation and Toggle Buttons
        binding.btnYesFasting.setOnClickListener(v -> {
            if (selectedDay[0] != null) {
                int currentStatus = FastingTrackerManager.getInstance().getFastingStatus(activity, selectedDay[0].dateKey);
                if (currentStatus == FastingTrackerManager.STATUS_FASTED) {
                    return; // Already confirmed as fasted
                }
                showConfirmationDialog(activity, isBn, selectedDay[0], true, () -> {
                    FastingTrackerManager.getInstance().setFastingStatus(activity, selectedDay[0].dateKey, FastingTrackerManager.STATUS_FASTED);
                    selectedDay[0].fastingStatus = FastingTrackerManager.STATUS_FASTED;
                    selectedDay[0].isFasting = true;
                    refreshCalendarAndCard.run();
                    if (onDataChanged != null) onDataChanged.run();
                });
            }
        });

        binding.btnNotYetFasting.setOnClickListener(v -> {
            if (selectedDay[0] != null) {
                int currentStatus = FastingTrackerManager.getInstance().getFastingStatus(activity, selectedDay[0].dateKey);
                if (currentStatus == FastingTrackerManager.STATUS_NOT_FASTED) {
                    return; // Already marked as not fasted
                }
                showConfirmationDialog(activity, isBn, selectedDay[0], false, () -> {
                    FastingTrackerManager.getInstance().setFastingStatus(activity, selectedDay[0].dateKey, FastingTrackerManager.STATUS_NOT_FASTED);
                    selectedDay[0].fastingStatus = FastingTrackerManager.STATUS_NOT_FASTED;
                    selectedDay[0].isFasting = false;
                    refreshCalendarAndCard.run();
                    if (onDataChanged != null) onDataChanged.run();
                });
            }
        });

        // Initial Load
        refreshCalendarAndCard.run();

        dialog.show();
    }

    private static void showConfirmationDialog(AppCompatActivity activity, boolean isBn,
                                               RozaCalendarDay day, boolean isFastingAction,
                                               Runnable onConfirmed) {
        if (activity == null || activity.isFinishing()) return;

        String dateStr;
        if (day.calendar != null) {
            if (isBn) {
                String dayBn = BengaliNumberUtil.toBengali(day.dayNumber);
                String monthBn = BengaliNumberUtil.getBengaliMonthName(day.month);
                dateStr = dayBn + " " + monthBn;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM", Locale.ENGLISH);
                dateStr = sdf.format(day.calendar.getTime());
            }
        } else {
            dateStr = isBn ? BengaliNumberUtil.toBengali(day.dayNumber) : String.valueOf(day.dayNumber);
        }

        int currentStatus = FastingTrackerManager.getInstance().getFastingStatus(activity, day.dateKey);

        String title;
        String message;
        if (isFastingAction) {
            title = isBn ? "রোজা নিশ্চিতকরণ" : "Confirm Fasting";
            message = isBn
                    ? (dateStr + " তারিখের রোজা রেখেছেন বলে নিশ্চিত করতে চান?")
                    : ("Do you want to confirm fasting for " + dateStr + "?");
        } else {
            if (currentStatus == FastingTrackerManager.STATUS_FASTED) {
                title = isBn ? "রোজা পরিবর্তন" : "Change Fasting Status";
                message = isBn
                        ? (dateStr + " তারিখের রোজা ইতিমধ্যে নিশ্চিত করা হয়েছে। আপনি কি এটি পরিবর্তন করে 'রোজা রাখেননি' হিসেবে চিহ্নিত করতে চান?")
                        : ("You have already confirmed fasting for " + dateStr + ". Do you want to change it to 'Not Fasted'?");
            } else {
                title = isBn ? "রোজা স্থিতি নির্ধারণ" : "Mark as Not Fasted";
                message = isBn
                        ? (dateStr + " তারিখের রোজা রাখেননি হিসেবে চিহ্নিত করতে চান?")
                        : ("Do you want to mark " + dateStr + " as not fasted?");
            }
        }

        String confirmText = isBn ? "হ্যাঁ, নিশ্চিত" : "Confirm";
        String cancelText = isBn ? "বাতিল" : "Cancel";

        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirmText, (d, which) -> {
                    if (onConfirmed != null) onConfirmed.run();
                })
                .setNegativeButton(cancelText, null)
                .show();
    }

    private static void updateBottomCard(Context context, PageRozaTrackingBinding binding,
                                         boolean isBn, @Nullable RozaCalendarDay day) {
        if (day == null || day.calendar == null) return;

        // 1. Gregorian Date String
        SimpleDateFormat sdfGregorian = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
        String gregorianStr = sdfGregorian.format(day.calendar.getTime());

        // 2. Bengali Calendar Date
        BengaliCalendarUtil.BengaliDateResult bnDate = BengaliCalendarUtil.getBengaliDate(context, day.calendar);
        String bnDateStr = (bnDate != null)
                ? (BengaliNumberUtil.toBengali(bnDate.day) + " " + bnDate.monthName + ", " + BengaliNumberUtil.toBengali(bnDate.year))
                : "";

        String subDateLine = isBn ? (gregorianStr + " • " + bnDateStr) : gregorianStr;
        binding.tvSelectedDayGregorianBengali.setText(subDateLine);

        // 3. Golden Hijri Date
        HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getAdjustedHijriDate(context, day.calendar);
        if (hijri != null) {
            if (isBn) {
                binding.tvSelectedDayHijri.setText(
                        BengaliNumberUtil.toBengali(hijri.day) + " " + hijri.monthNameBengali + ", " + BengaliNumberUtil.toBengali(hijri.year) + " হিজরী"
                );
            } else {
                binding.tvSelectedDayHijri.setText(
                        hijri.day + " " + hijri.monthNameEnglish + ", " + hijri.year + " AH"
                );
            }
        }

        // 4. Question: "আজ রোজা আছেন?"
        Calendar todayCal = Calendar.getInstance();
        boolean isToday = (todayCal.get(Calendar.YEAR) == day.calendar.get(Calendar.YEAR) &&
                todayCal.get(Calendar.DAY_OF_YEAR) == day.calendar.get(Calendar.DAY_OF_YEAR));

        if (isToday) {
            binding.tvAreYouFastingTitle.setText(isBn ? "আজ রোজা আছেন?" : "Are you fasting today?");
        } else {
            binding.tvAreYouFastingTitle.setText(isBn ? "এই দিনে রোজা ছিলেন?" : "Did you fast on this day?");
        }

        // 5. Total Completed Fasting Counter (০/৬০)
        int totalCompleted = FastingTrackerManager.getInstance().getTotalCompletedCount(context);
        String fractionStr = (isBn ? BengaliNumberUtil.toBengali(totalCompleted) : String.valueOf(totalCompleted))
                + "/" + (isBn ? "৬০" : "60");
        binding.tvFastingCounterFraction.setText(fractionStr);

        binding.progressFastingLine.setMax(60);
        binding.progressFastingLine.setProgress(Math.min(60, totalCompleted));

        // 6. Toggle Buttons State (3-State: Fasted, Not Fasted, Unmarked)
        int status = FastingTrackerManager.getInstance().getFastingStatus(context, day.dateKey);
        int badgePillBg = ContextCompat.getColor(context, R.color.bg_badge_pill);
        int textPrimary = ContextCompat.getColor(context, R.color.text_primary);
        int textSecondary = ContextCompat.getColor(context, R.color.text_secondary);

        if (status == FastingTrackerManager.STATUS_FASTED) {
            // YES FASTING IS CONFIRMED (GREEN)
            binding.btnYesFasting.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnYesFasting.getBackground().setTint(Color.parseColor("#2510B981"));
            binding.ivYesFastingRadio.setImageResource(R.drawable.ic_check_circle);
            binding.ivYesFastingRadio.setImageTintList(ColorStateList.valueOf(Color.parseColor("#10B981")));
            binding.tvYesFastingText.setText(isBn ? "হ্যাঁ, রোজা আছি" : "Yes, Fasting");
            binding.tvYesFastingText.setTextColor(Color.parseColor("#10B981"));

            binding.btnNotYetFasting.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnNotYetFasting.getBackground().setTint(badgePillBg);
            binding.ivNotYetRadio.setImageResource(R.drawable.ic_radio_circle_empty);
            binding.ivNotYetRadio.setImageTintList(ColorStateList.valueOf(textSecondary));
            binding.tvNotYetText.setText(isBn ? "এখনো না" : "Not Yet");
            binding.tvNotYetText.setTextColor(textSecondary);
        } else if (status == FastingTrackerManager.STATUS_NOT_FASTED) {
            // NOT FASTING IS SELECTED (RED)
            binding.btnYesFasting.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnYesFasting.getBackground().setTint(badgePillBg);
            binding.ivYesFastingRadio.setImageResource(R.drawable.ic_radio_circle_empty);
            binding.ivYesFastingRadio.setImageTintList(ColorStateList.valueOf(textSecondary));
            binding.tvYesFastingText.setText(isBn ? "হ্যাঁ, রোজা আছি" : "Yes, Fasting");
            binding.tvYesFastingText.setTextColor(textSecondary);

            binding.btnNotYetFasting.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnNotYetFasting.getBackground().setTint(Color.parseColor("#25EF4444"));
            binding.ivNotYetRadio.setImageResource(R.drawable.ic_check_circle_red);
            binding.ivNotYetRadio.setImageTintList(null);
            binding.tvNotYetText.setText(isBn ? "এখনো না" : "Not Yet");
            binding.tvNotYetText.setTextColor(ContextCompat.getColor(context, R.color.accent_red));
        } else {
            // UNMARKED / PENDING (NEUTRAL)
            binding.btnYesFasting.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnYesFasting.getBackground().setTint(badgePillBg);
            binding.ivYesFastingRadio.setImageResource(R.drawable.ic_radio_circle_empty);
            binding.ivYesFastingRadio.setImageTintList(ColorStateList.valueOf(textSecondary));
            binding.tvYesFastingText.setText(isBn ? "হ্যাঁ, রোজা আছি" : "Yes, Fasting");
            binding.tvYesFastingText.setTextColor(textPrimary);

            binding.btnNotYetFasting.setBackgroundResource(R.drawable.bg_badge_pill);
            binding.btnNotYetFasting.getBackground().setTint(badgePillBg);
            binding.ivNotYetRadio.setImageResource(R.drawable.ic_radio_circle_empty);
            binding.ivNotYetRadio.setImageTintList(ColorStateList.valueOf(textSecondary));
            binding.tvNotYetText.setText(isBn ? "এখনো না" : "Not Yet");
            binding.tvNotYetText.setTextColor(textPrimary);
        }
    }
}
