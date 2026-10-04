package com.devflux.deenone.features.ramadan.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.devflux.deenone.R;
import com.devflux.deenone.core.fasting.FastingTrackerManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.BottomSheetFastingTrackerBinding;
import com.devflux.deenone.databinding.PageUnifiedRamadanRozaBinding;
import com.devflux.deenone.features.location.LocationSelectorPageDialog;
import com.devflux.deenone.features.ramadan.adapter.RozaScheduleAdapter;
import com.devflux.deenone.features.ramadan.model.RozaScheduleItem;
import com.devflux.deenone.utils.BengaliCalendarUtil;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.PrayerCalculator;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UnifiedRamadanRozaPageDialog {

    public static void show(@NonNull AppCompatActivity activity, LocationProvider.Coordinates initialCoordinates) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageUnifiedRamadanRozaBinding binding = PageUnifiedRamadanRozaBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(activity);

        // Header & Feature Cards Localization (Dual-Language Clean without brackets)
        binding.tvUnifiedRozaTitle.setText(isBn ? "রোজা" : "Fasting");
        binding.tvLabelSehri.setText(isBn ? "সাহরী" : "Sehri");
        binding.tvLabelIftar.setText(isBn ? "ইফতার" : "Iftar");
        binding.tvAreYouFastingTitle.setText(isBn ? "আজ রোজা আছেন?" : "Are you fasting today?");
        binding.tvRozaTrackingLink.setText(isBn ? "রোজা ট্র্যাকিং  >" : "Roza Tracking  >");
        binding.tvSectionScheduleHeader.setText(isBn ? "ইফতার ও সাহরীর সময়" : "Iftar and Sehri Time");
        binding.tvHeaderDate.setText(isBn ? "তারিখ" : "Date");
        binding.tvHeaderSehri.setText(isBn ? "সাহরী শেষ" : "Sehri Ends");
        binding.tvHeaderIftar.setText(isBn ? "ইফতার" : "Iftar");

        // 14 Feature Cards Dual-Language Clean Labels (No Brackets)
        binding.tvCardRozaDua.setText(isBn ? "রোজার দোয়া" : "Fasting Dua");
        binding.tvCardRozaBayan.setText(isBn ? "রোজার বয়ান ভিডিও" : "Fasting Lectures");
        binding.tvCardRozaBooks.setText(isBn ? "রোজার ইসলামিক বই" : "Islamic Books on Fasting");
        binding.tvCardRozaKhatm.setText(isBn ? "খতমে কুরআন" : "Khatm Quran");
        binding.tvCardRozaInfo.setText(isBn ? "রোজা নিয়ে তথ্য" : "Fasting Information");
        binding.tvCardRozaHadith.setText(isBn ? "রোজার হাদিস" : "Fasting Hadith");
        binding.tvCardRozaTaraweeh.setText(isBn ? "তারাবীহ" : "Taraweeh");
        binding.tvCardRozaItikaf.setText(isBn ? "ই'তিকাফ" : "Itikaf");
        binding.tvCardRozaQadr.setText(isBn ? "লাইলাতুল কদর" : "Laylatul Qadr");
        binding.tvCardRozaFitra.setText(isBn ? "ফিতরা" : "Fitra");
        binding.tvCardRozaEid.setText(isBn ? "ঈদ" : "Eid");
        binding.tvCardRozaMasayel.setText(isBn ? "ফাযায়েল মাসায়েল" : "Virtues and Rules");
        binding.tvCardRozaHealth.setText(isBn ? "রোজায় স্বাস্থ্য পরামর্শ" : "Health Tips for Fasting");
        binding.tvCardRozaJumatulWida.setText(isBn ? "জুমাতুল বিদা" : "Jumatul Wida");

        // Schedule RecyclerView Setup
        RozaScheduleAdapter scheduleAdapter = new RozaScheduleAdapter();
        binding.rvRozaSchedule.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvRozaSchedule.setAdapter(scheduleAdapter);

        // Coordinates resolution
        final LocationProvider.Coordinates[] currentCoords = new LocationProvider.Coordinates[1];
        if (initialCoordinates != null) {
            currentCoords[0] = initialCoordinates;
        } else {
            currentCoords[0] = LocationProvider.getSavedOrCurrentLocation(activity);
        }

        // Real-Time Data Loader Function
        Runnable refreshAllData = new Runnable() {
            @Override
            public void run() {
                LocationProvider.Coordinates coords = currentCoords[0];
                if (coords == null) {
                    coords = LocationProvider.getSavedOrCurrentLocation(activity);
                    currentCoords[0] = coords;
                }

                // 1. Location Bar (Pure City Name without Bracket Pollution)
                String formattedCity = LocationProvider.formatCityNameOnly(coords.locationName, isBn);
                binding.tvCurrentLocationName.setText(formattedCity);

                // 2. Dates Calculation (Today)
                Calendar today = Calendar.getInstance();

                // Gregorian Date String
                String gregorianStr;
                if (isBn) {
                    String dayOfWeekBn = BengaliNumberUtil.getBengaliDayOfWeek(today.get(Calendar.DAY_OF_WEEK));
                    String dayBn = BengaliNumberUtil.toBengali(today.get(Calendar.DAY_OF_MONTH));
                    String monthBn = BengaliNumberUtil.getBengaliMonthName(today.get(Calendar.MONTH));
                    String yearBn = BengaliNumberUtil.toBengali(today.get(Calendar.YEAR));
                    gregorianStr = dayOfWeekBn + ", " + dayBn + " " + monthBn + " " + yearBn;
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.ENGLISH);
                    gregorianStr = sdf.format(today.getTime());
                }

                // Bengali Calendar Date
                BengaliCalendarUtil.BengaliDateResult bnDate = BengaliCalendarUtil.getBengaliDate(activity, today);
                String bnDateStr = (bnDate != null)
                        ? (BengaliNumberUtil.toBengali(bnDate.day) + " " + bnDate.monthName + ", " + BengaliNumberUtil.toBengali(bnDate.year))
                        : "";

                String subDateLine = isBn ? (gregorianStr + " • " + bnDateStr) : gregorianStr;
                binding.tvHeroSubDate.setText(subDateLine);

                // Hijri Date
                HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getRealHijriDate(today);
                if (hijri != null) {
                    if (isBn) {
                        binding.tvHeroHijriDate.setText(
                                BengaliNumberUtil.toBengali(hijri.day) + " " + hijri.monthNameBengali + ", " + BengaliNumberUtil.toBengali(hijri.year) + " হিজরী"
                        );
                    } else {
                        binding.tvHeroHijriDate.setText(
                                hijri.day + " " + hijri.monthNameEnglish + ", " + hijri.year + " AH"
                        );
                    }
                }

                // 3. Real-Time Sehri & Iftar Calculation (Astronomical Engine)
                PrayerCalculator.PrayerTimesResult todayTimes = PrayerCalculator.calculateForLocationWithContext(
                        activity, coords.latitude, coords.longitude, coords.timezone, today
                );

                if (todayTimes != null) {
                    SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
                    String sehriStr = timeFormatter.format(new Date(todayTimes.sehriMillis));
                    String iftarStr = timeFormatter.format(new Date(todayTimes.iftarMillis));

                    if (isBn) {
                        binding.tvHeroSehriTime.setText(BengaliNumberUtil.toBengali(sehriStr));
                        binding.tvHeroIftarTime.setText(BengaliNumberUtil.toBengali(iftarStr));
                    } else {
                        binding.tvHeroSehriTime.setText(sehriStr);
                        binding.tvHeroIftarTime.setText(iftarStr);
                    }

                    // Dynamic Live Countdown Banner ("ইফতার হতে বাকি:..." / "সাহরী শেষ হতে বাকি:...")
                    long nowMillis = System.currentTimeMillis();
                    if (nowMillis < todayTimes.sehriMillis) {
                        long diff = todayTimes.sehriMillis - nowMillis;
                        long hours = diff / (1000 * 60 * 60);
                        long mins = (diff / (1000 * 60)) % 60;
                        String countdownText = isBn
                                ? ("সাহরী শেষ হতে বাকি: " + BengaliNumberUtil.toBengali(hours) + " ঘণ্টা " + BengaliNumberUtil.toBengali(mins) + " মিনিট")
                                : ("Sehri ends in: " + hours + "h " + mins + "m");
                        binding.tvRozaCountdown.setText(countdownText);
                    } else if (nowMillis < todayTimes.iftarMillis) {
                        long diff = todayTimes.iftarMillis - nowMillis;
                        long hours = diff / (1000 * 60 * 60);
                        long mins = (diff / (1000 * 60)) % 60;
                        String countdownText = isBn
                                ? ("ইফতার হতে বাকি: " + BengaliNumberUtil.toBengali(hours) + " ঘণ্টা " + BengaliNumberUtil.toBengali(mins) + " মিনিট")
                                : ("Iftar in: " + hours + "h " + mins + "m");
                        binding.tvRozaCountdown.setText(countdownText);
                    } else {
                        String countdownText = isBn
                                ? "আজকের ইফতার সম্পন্ন (আলহামদুলিল্লাহ)"
                                : "Today's Iftar completed (Alhamdulillah)";
                        binding.tvRozaCountdown.setText(countdownText);
                    }
                }

                // 4. Real-Time Fasting Status & Fraction Counter (০/৩০)
                String todayKey = String.format(Locale.US, "%04d_%02d_%02d",
                        today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH));
                int todayStatus = FastingTrackerManager.getInstance().getFastingStatus(activity, todayKey);

                if (todayStatus == FastingTrackerManager.STATUS_FASTED) {
                    binding.ivFastingCheckCircle.setImageResource(R.drawable.ic_check_circle);
                    binding.ivFastingCheckCircle.setColorFilter(0xFF10B981); // Emerald Green
                } else if (todayStatus == FastingTrackerManager.STATUS_NOT_FASTED) {
                    binding.ivFastingCheckCircle.setImageResource(R.drawable.ic_close);
                    binding.ivFastingCheckCircle.setColorFilter(0xFFEF4444); // Red
                } else {
                    binding.ivFastingCheckCircle.setImageResource(R.drawable.ic_radio_circle_empty);
                    binding.ivFastingCheckCircle.setColorFilter(0xFFF59E0B); // Amber Yellow
                }

                int completedCount = FastingTrackerManager.getInstance().getCompletedRamadanDaysCount(activity);
                if (completedCount <= 0) {
                    completedCount = FastingTrackerManager.getInstance().getMonthlyCompletedCount(activity);
                }
                String fraction = (isBn ? BengaliNumberUtil.toBengali(completedCount) : String.valueOf(completedCount))
                        + "/" + (isBn ? "৩০" : "30");
                binding.tvFastingProgressFraction.setText(fraction);

                // 5. Build Real-Time 10-Day Consecutive Schedule Table
                List<RozaScheduleItem> scheduleItems = new ArrayList<>();
                SimpleDateFormat sdfRowGregorian = new SimpleDateFormat("EEEE d MMMM", isBn ? new Locale("bn") : Locale.ENGLISH);
                SimpleDateFormat sdfRowTime = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

                for (int i = 0; i < 10; i++) {
                    Calendar rowCal = (Calendar) today.clone();
                    rowCal.add(Calendar.DAY_OF_YEAR, i);

                    HijriCalendarUtil.HijriDateResult rowHijri = HijriCalendarUtil.getRealHijriDate(rowCal);
                    String rowHijriStr = "";
                    if (rowHijri != null) {
                        rowHijriStr = isBn
                                ? (BengaliNumberUtil.toBengali(rowHijri.day) + " " + rowHijri.monthNameBengali)
                                : (rowHijri.day + " " + rowHijri.monthNameEnglish);
                    }

                    String rowGregorianStr;
                    if (isBn) {
                        String dayOfWeek = BengaliNumberUtil.getBengaliDayOfWeek(rowCal.get(Calendar.DAY_OF_WEEK));
                        String dayNum = BengaliNumberUtil.toBengali(rowCal.get(Calendar.DAY_OF_MONTH));
                        String monthName = BengaliNumberUtil.getBengaliMonthName(rowCal.get(Calendar.MONTH));
                        rowGregorianStr = dayOfWeek + " " + dayNum + " " + monthName;
                    } else {
                        rowGregorianStr = sdfRowGregorian.format(rowCal.getTime());
                    }

                    PrayerCalculator.PrayerTimesResult rowTimes = PrayerCalculator.calculateForLocationWithContext(
                            activity, coords.latitude, coords.longitude, coords.timezone, rowCal
                    );

                    String rowSehri = sdfRowTime.format(new Date(rowTimes.sehriMillis));
                    String rowIftar = sdfRowTime.format(new Date(rowTimes.iftarMillis));

                    if (isBn) {
                        rowSehri = BengaliNumberUtil.toBengali(rowSehri);
                        rowIftar = BengaliNumberUtil.toBengali(rowIftar);
                    }

                    scheduleItems.add(new RozaScheduleItem(
                            rowHijriStr,
                            rowGregorianStr,
                            rowSehri,
                            rowIftar,
                            i == 0
                    ));
                }

                scheduleAdapter.setItems(scheduleItems);
            }
        };

        // Initial Load
        refreshAllData.run();

        // Attach Spring Touch Effects (Buttons ONLY)
        TouchAnimationUtil.attachTouchSpring(binding.btnBackUnifiedRoza);
        TouchAnimationUtil.attachTouchSpring(binding.btnSelectLocation);
        TouchAnimationUtil.attachTouchSpring(binding.btnOpenRozaTracking);

        // Top Bar Back Button
        binding.btnBackUnifiedRoza.setOnClickListener(v -> dialog.dismiss());

        // Location Selector Click
        binding.btnSelectLocation.setOnClickListener(v -> {
            LocationSelectorPageDialog.show(activity, () -> {
                currentCoords[0] = LocationProvider.getSavedOrCurrentLocation(activity);
                refreshAllData.run();
            });
        });

        // Open Roza Tracking Page Dialog when tapping Fasting Row or Tracking Link
        // (Prevents blind fake toggle directly on hero card, routes to verified calendar tracking)
        View.OnClickListener openTrackingListener = v -> RozaTrackingPageDialog.show(activity, refreshAllData);
        binding.layoutTodayFastingRow.setOnClickListener(openTrackingListener);
        binding.btnToggleFastingToday.setOnClickListener(openTrackingListener);
        binding.btnOpenRozaTracking.setOnClickListener(openTrackingListener);
        binding.tvRozaTrackingLink.setOnClickListener(openTrackingListener);

        // Click Actions for All 14 Feature Cards (Screenshot 2) - Excluded from touch animation
        if (binding.cardRozaDua != null) {
            binding.cardRozaDua.setOnClickListener(v -> RozaDuaPageDialog.show(activity));
        }
        setupFeatureCard(binding.cardRozaBayan, "roza_bayan", activity);
        setupFeatureCard(binding.cardRozaBooks, "roza_books", activity);
        setupFeatureCard(binding.cardRozaKhatm, "roza_khatm", activity);
        if (binding.cardRozaInfo != null) {
            binding.cardRozaInfo.setOnClickListener(v -> RozaInfoPageDialog.show(activity));
        }
        if (binding.cardRozaHadith != null) {
            binding.cardRozaHadith.setOnClickListener(v -> RozaHadithPageDialog.show(activity));
        }
        if (binding.cardRozaTaraweeh != null) {
            binding.cardRozaTaraweeh.setOnClickListener(v -> RozaTaraweehPageDialog.show(activity));
        }
        if (binding.cardRozaItikaf != null) {
            binding.cardRozaItikaf.setOnClickListener(v -> RozaItikafPageDialog.show(activity));
        }
        if (binding.cardRozaQadr != null) {
            binding.cardRozaQadr.setOnClickListener(v -> RozaQadrPageDialog.show(activity));
        }
        if (binding.cardRozaFitra != null) {
            binding.cardRozaFitra.setOnClickListener(v -> RozaFitraPageDialog.show(activity));
        }
        if (binding.cardRozaEid != null) {
            binding.cardRozaEid.setOnClickListener(v -> RozaEidPageDialog.show(activity));
        }
        if (binding.cardRozaMasayel != null) {
            binding.cardRozaMasayel.setOnClickListener(v -> RozaFazayelMasayelPageDialog.show(activity));
        }
        if (binding.cardRozaHealth != null) {
            binding.cardRozaHealth.setOnClickListener(v -> RozaHealthAdvicePageDialog.show(activity));
        }
        if (binding.cardRozaJumatulWida != null) {
            binding.cardRozaJumatulWida.setOnClickListener(v -> RozaJumatulWidaPageDialog.show(activity));
        }

        dialog.show();
    }

    private static void setupFeatureCard(View cardView, String topicId, AppCompatActivity activity) {
        if (cardView == null) return;
        cardView.setOnClickListener(v -> RozaTopicDetailDialog.show(activity, topicId));
    }

    private static void showRozaTrackingManagerSheet(AppCompatActivity activity, Runnable onDataChanged) {
        try {
            FullScreenPageDialog sheetDialog = new FullScreenPageDialog(activity);
            BottomSheetFastingTrackerBinding sheetBinding =
                    BottomSheetFastingTrackerBinding.inflate(LayoutInflater.from(activity));
            sheetDialog.setContentView(sheetBinding.getRoot());

            TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCloseFastingSheet);
            TouchAnimationUtil.attachTouchSpring(sheetBinding.btnAddQaza);
            TouchAnimationUtil.attachTouchSpring(sheetBinding.btnCompleteQaza);

            sheetBinding.btnCloseFastingSheet.setOnClickListener(v -> sheetDialog.dismiss());

            Runnable updateStats = () -> {
                boolean isBn = LocaleManager.isBengali(activity);
                boolean isFasting = FastingTrackerManager.getInstance().isFastingToday(activity);
                sheetBinding.cbTodayFasting.setChecked(isFasting);
                sheetBinding.tvTodayFastingStatus.setText(
                        isFasting
                                ? (isBn ? "আলহামদুলিল্লাহ! আজ আপনি রোজা রেখেছেন" : "Alhamdulillah! You are fasting today")
                                : (isBn ? "আজকে রোজা রাখলে টিক দিন" : "Check if you are fasting today")
                );

                int monthly = FastingTrackerManager.getInstance().getMonthlyCompletedCount(activity);
                int yearly = FastingTrackerManager.getInstance().getYearlyCompletedCount(activity);
                int qaza = FastingTrackerManager.getInstance().getQazaRemaining(activity);

                sheetBinding.tvMonthlyFastsCount.setText(isBn ? (BengaliNumberUtil.toBengali(monthly) + "টি") : String.valueOf(monthly));
                sheetBinding.tvYearlyFastsCount.setText(isBn ? (BengaliNumberUtil.toBengali(yearly) + "টি") : String.valueOf(yearly));
                sheetBinding.tvQazaRemaining.setText(isBn ? (BengaliNumberUtil.toBengali(qaza) + "টি বাকি") : (qaza + " remaining"));
            };

            sheetBinding.cbTodayFasting.setOnCheckedChangeListener((btn, isChecked) -> {
                FastingTrackerManager.getInstance().setFastingToday(activity, isChecked);
                updateStats.run();
                if (onDataChanged != null) onDataChanged.run();
            });

            sheetBinding.btnAddQaza.setOnClickListener(v -> {
                FastingTrackerManager.getInstance().addQazaFast(activity, 1);
                updateStats.run();
                if (onDataChanged != null) onDataChanged.run();
            });

            sheetBinding.btnCompleteQaza.setOnClickListener(v -> {
                FastingTrackerManager.getInstance().completeOneQazaFast(activity);
                updateStats.run();
                if (onDataChanged != null) onDataChanged.run();
            });

            updateStats.run();
            sheetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
