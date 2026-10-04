package com.devflux.deenone.core.ramadan;

import android.content.Context;

import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.util.Calendar;
import java.util.Locale;

public class RamadanCalculator {

  public static class RamadanStatus {
    public boolean isCurrentlyRamadan;
    public int currentRamadanDay;
    public int remainingRamadanDays;
    public String ramadanStartDateBengali;
    public String sehriTimeStr;
    public String fajrTimeStr;
    public String maghribTimeStr;
    public String iftarTimeStr;
    public String countdownTitle;
    public String countdownValue;
    public int progressPercent;
    public boolean isFastingActiveNow;
  }

  public static RamadanStatus calculateStatus(Context context, double latitude, double longitude, double timezone) {
    RamadanStatus status = new RamadanStatus();
    Calendar cal = Calendar.getInstance();

    // 1. Calculate Hijri Date using Kuwaiti Algorithm
    HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getRealHijriDate(cal);
    status.isCurrentlyRamadan = (hijri.monthIndex == 8); // 8 is Ramadan (0-based)
    if (status.isCurrentlyRamadan) {
      status.currentRamadanDay = Math.max(1, Math.min(30, hijri.day));
      status.remainingRamadanDays = Math.max(0, 30 - status.currentRamadanDay);
      status.ramadanStartDateBengali = "বর্তমানে পবিত্র রমজান মাস চলমান ("+ BengaliNumberUtil.toBengali(status.currentRamadanDay) + "ই রমজান)";
    } else {
      status.currentRamadanDay = 0;
      // Calculate approximate days until next Ramadan
      int monthsUntil = (8 - hijri.monthIndex + 12) % 12;
      if (monthsUntil == 0) monthsUntil = 12;
      int daysApprox = (monthsUntil - 1) * 30 + (30 - hijri.day) + 1;
      status.remainingRamadanDays = daysApprox;
      status.ramadanStartDateBengali = "পরবর্তী রমজান শুরু হতে আনুমানিক " + BengaliNumberUtil.toBengali(daysApprox) + " দিন বাকি";
    }

    // 2. Real-time Location-based Sehri and Iftar via PrayerCalculator
    PrayerCalculator.PrayerTimesResult pt = PrayerCalculator.calculateForLocationWithContext(
        context, latitude, longitude, timezone, cal
    );

    status.sehriTimeStr = pt.sehriStr;
    status.fajrTimeStr = pt.fajrStr;
    status.maghribTimeStr = pt.maghribStr;
    status.iftarTimeStr = pt.iftarStr;

    long now = System.currentTimeMillis();

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    if (now >= pt.fajrMillis && now < pt.maghribMillis) {
      // Currently Fasting (Daytime)
      status.isFastingActiveNow = true;
      status.countdownTitle = isBn ? "ইফতারের বাকি" : "Time to Iftar";
      long diffSec = (pt.maghribMillis - now) / 1000;
      long hours = diffSec / 3600;
      long mins = (diffSec % 3600) / 60;
      status.countdownValue = String.format(Locale.getDefault(), "%02d:%02d", hours, mins);

      long totalDayMillis = pt.maghribMillis - pt.fajrMillis;
      long elapsedMillis = now - pt.fajrMillis;
      if (totalDayMillis > 0) {
        status.progressPercent = (int) Math.min(100, Math.max(0, (elapsedMillis * 100) / totalDayMillis));
      }
    } else {
      // Nighttime (Between Iftar and next Sehri)
      status.isFastingActiveNow = false;
      status.countdownTitle = isBn ? "সেহরির শেষ সময় বাকি" : "Time to Suhoor";

      long targetSehri = pt.sehriMillis;
      if (now >= pt.maghribMillis) {
        // Next day's Sehri
        targetSehri += 24 * 60 * 60 * 1000;
      }

      long diffSec = Math.max(0, (targetSehri - now) / 1000);
      long hours = diffSec / 3600;
      long mins = (diffSec % 3600) / 60;
      status.countdownValue = String.format(Locale.getDefault(), "%02d:%02d", hours, mins);
      status.progressPercent = 100;
    }

    return status;
  }
}
