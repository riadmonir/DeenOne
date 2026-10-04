package com.devflux.deenone.core.alarms;

import android.content.Context;
import android.util.Log;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.utils.PrayerCalculator;

import java.util.Calendar;

public class AlarmRescheduler {

    private static final String TAG = "AlarmRescheduler";

    public static synchronized void rescheduleAll(Context context) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        Log.d(TAG, "Starting automatic alarm rescheduling pipeline...");

        try {
            // Step 1: Cancel all old alarms
            cancelAllAlarms(appContext);

            // Step 2: Calculate new prayer times
            LocationProvider.Coordinates loc = LocationProvider.getSavedOrCurrentLocation(appContext);
            Calendar cal = Calendar.getInstance();

            PrayerCalculator.PrayerTimesResult todayTimes =
                    PrayerCalculator.calculateForLocationWithContext(appContext, loc.latitude, loc.longitude, loc.timezone, cal);

            Calendar tomorrowCal = Calendar.getInstance();
            tomorrowCal.add(Calendar.DAY_OF_MONTH, 1);
            PrayerCalculator.PrayerTimesResult tomorrowTimes =
                    PrayerCalculator.calculateForLocationWithContext(appContext, loc.latitude, loc.longitude, loc.timezone, tomorrowCal);

            // Step 3: Schedule new alarms (today if in future, otherwise tomorrow)
            long now = System.currentTimeMillis();

            // Fajr
            long fajrTarget = todayTimes.fajrMillis > now ? todayTimes.fajrMillis : tomorrowTimes.fajrMillis;
            scheduleWaqt(appContext, "fajr", "ফজর", fajrTarget, AlarmManagerHelper.REQUEST_BASE_FAJR, AlarmManagerHelper.REQUEST_PRE_FAJR);

            // Sunrise
            long sunriseTarget = todayTimes.sunriseMillis > now ? todayTimes.sunriseMillis : tomorrowTimes.sunriseMillis;
            AlarmManagerHelper.schedulePrayerAlarm(appContext, "sunrise", "সূর্যোদয়", sunriseTarget, AlarmManagerHelper.REQUEST_BASE_SUNRISE, false);

            // Dhuhr / Jummah (জুম্মা on Friday)
            boolean isFridayToday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
            boolean isFridayTomorrow = tomorrowCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

            if (todayTimes.zohrMillis > now) {
                if (isFridayToday) {
                    scheduleWaqt(appContext, "jummah", "জুম্মা", todayTimes.zohrMillis, AlarmManagerHelper.REQUEST_BASE_JUMMAH, AlarmManagerHelper.REQUEST_PRE_JUMMAH);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_BASE_DHUHR);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_PRE_DHUHR);
                } else {
                    scheduleWaqt(appContext, "dhuhr", "যোহর", todayTimes.zohrMillis, AlarmManagerHelper.REQUEST_BASE_DHUHR, AlarmManagerHelper.REQUEST_PRE_DHUHR);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_BASE_JUMMAH);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_PRE_JUMMAH);
                }
            } else {
                if (isFridayTomorrow) {
                    scheduleWaqt(appContext, "jummah", "জুম্মা", tomorrowTimes.zohrMillis, AlarmManagerHelper.REQUEST_BASE_JUMMAH, AlarmManagerHelper.REQUEST_PRE_JUMMAH);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_BASE_DHUHR);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_PRE_DHUHR);
                } else {
                    scheduleWaqt(appContext, "dhuhr", "যোহর", tomorrowTimes.zohrMillis, AlarmManagerHelper.REQUEST_BASE_DHUHR, AlarmManagerHelper.REQUEST_PRE_DHUHR);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_BASE_JUMMAH);
                    AlarmManagerHelper.cancelPrayerAlarm(appContext, AlarmManagerHelper.REQUEST_PRE_JUMMAH);
                }
            }

            // Asr
            long asrTarget = todayTimes.asrMillis > now ? todayTimes.asrMillis : tomorrowTimes.asrMillis;
            scheduleWaqt(appContext, "asr", "আসর", asrTarget, AlarmManagerHelper.REQUEST_BASE_ASR, AlarmManagerHelper.REQUEST_PRE_ASR);

            // Maghrib
            long maghribTarget = todayTimes.maghribMillis > now ? todayTimes.maghribMillis : tomorrowTimes.maghribMillis;
            scheduleWaqt(appContext, "maghrib", "মাগরিব", maghribTarget, AlarmManagerHelper.REQUEST_BASE_MAGHRIB, AlarmManagerHelper.REQUEST_PRE_MAGHRIB);

            // Isha
            long ishaTarget = todayTimes.ishaMillis > now ? todayTimes.ishaMillis : tomorrowTimes.ishaMillis;
            scheduleWaqt(appContext, "isha", "এশা", ishaTarget, AlarmManagerHelper.REQUEST_BASE_ISHA, AlarmManagerHelper.REQUEST_PRE_ISHA);

            // Tahajjud
            long tahajjudTarget = todayTimes.tahajjudMillis > now ? todayTimes.tahajjudMillis : tomorrowTimes.tahajjudMillis;
            scheduleWaqt(appContext, "tahajjud", "তাহাজ্জুদ", tahajjudTarget, AlarmManagerHelper.REQUEST_BASE_TAHAJJUD, AlarmManagerHelper.REQUEST_PRE_TAHAJJUD);

            // Ishraq
            long ishraqTarget = todayTimes.ishraqMillis > now ? todayTimes.ishraqMillis : tomorrowTimes.ishraqMillis;
            scheduleWaqt(appContext, "ishraq", "ইশরাক", ishraqTarget, AlarmManagerHelper.REQUEST_BASE_ISHRAQ, AlarmManagerHelper.REQUEST_PRE_ISHRAQ);

            // Chasht / Duha
            long chashtTarget = todayTimes.chashtMillis > now ? todayTimes.chashtMillis : tomorrowTimes.chashtMillis;
            scheduleWaqt(appContext, "chasht", "চাশত", chashtTarget, AlarmManagerHelper.REQUEST_BASE_CHASHT, AlarmManagerHelper.REQUEST_PRE_CHASHT);

            // Awwabin
            long awwabinTarget = todayTimes.awwabinMillis > now ? todayTimes.awwabinMillis : tomorrowTimes.awwabinMillis;
            scheduleWaqt(appContext, "awwabin", "আওয়াবিন", awwabinTarget, AlarmManagerHelper.REQUEST_BASE_AWWABIN, AlarmManagerHelper.REQUEST_PRE_AWWABIN);

            // Step 4: Schedule Seheri & Iftar Reminders
            com.devflux.deenone.core.notifications.SehriIftarReminderScheduler.scheduleDailySehriIftar(appContext, todayTimes);

            // Step 5: Schedule Daily Islamic Content (Amal, Dua, Hadith, Quiz)
            com.devflux.deenone.core.notifications.DailyIslamicReminderScheduler.scheduleAllDailyReminders(appContext);

            // Step 6: Update Lock Screen Prayer Widget
            com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(appContext);

            // Step 7: Schedule Durud Reminders
            com.devflux.deenone.core.notifications.DurudReminderScheduler.scheduleAll(appContext);

            // Step 8: Schedule Istigfar Reminders
            com.devflux.deenone.core.notifications.IstigfarReminderScheduler.scheduleAll(appContext);

            // Step 9: Schedule Quran Journey Reminder
            com.devflux.deenone.core.quran.QuranJourneyManager.scheduleDailyReminder(appContext);

            Log.d(TAG, "Successfully completed automatic alarm rescheduling pipeline.");
        } catch (Exception e) {
            Log.e(TAG, "Error in alarm rescheduling pipeline: " + e.getMessage(), e);
        }
    }

    private static void cancelAllAlarms(Context context) {
        int[] requestCodes = {
                AlarmManagerHelper.REQUEST_BASE_FAJR, AlarmManagerHelper.REQUEST_PRE_FAJR,
                AlarmManagerHelper.REQUEST_BASE_SUNRISE,
                AlarmManagerHelper.REQUEST_BASE_DHUHR, AlarmManagerHelper.REQUEST_PRE_DHUHR,
                AlarmManagerHelper.REQUEST_BASE_JUMMAH, AlarmManagerHelper.REQUEST_PRE_JUMMAH,
                AlarmManagerHelper.REQUEST_BASE_ASR, AlarmManagerHelper.REQUEST_PRE_ASR,
                AlarmManagerHelper.REQUEST_BASE_MAGHRIB, AlarmManagerHelper.REQUEST_PRE_MAGHRIB,
                AlarmManagerHelper.REQUEST_BASE_ISHA, AlarmManagerHelper.REQUEST_PRE_ISHA,
                AlarmManagerHelper.REQUEST_BASE_TAHAJJUD, AlarmManagerHelper.REQUEST_PRE_TAHAJJUD,
                AlarmManagerHelper.REQUEST_BASE_ISHRAQ, AlarmManagerHelper.REQUEST_PRE_ISHRAQ,
                AlarmManagerHelper.REQUEST_BASE_CHASHT, AlarmManagerHelper.REQUEST_PRE_CHASHT,
                AlarmManagerHelper.REQUEST_BASE_AWWABIN, AlarmManagerHelper.REQUEST_PRE_AWWABIN
        };

        for (int code : requestCodes) {
            AlarmManagerHelper.cancelPrayerAlarm(context, code);
        }
    }

    private static void scheduleWaqt(Context context, String key, String name, long waqtMillis, int baseReqCode, int preReqCode) {
        PrayerAlarmSettingsManager.PrayerAlarmConfig config = PrayerAlarmSettingsManager.getConfig(context, key);

        if (!config.isEnabled) {
            return;
        }

        AlarmManagerHelper.schedulePrayerAlarm(context, key, name, waqtMillis, baseReqCode, false);

        if (config.prePrayerReminderMinutes > 0) {
            long preMillis = waqtMillis - (config.prePrayerReminderMinutes * 60 * 1000L);
            if (preMillis > System.currentTimeMillis()) {
                AlarmManagerHelper.schedulePrayerAlarm(context, key, name, preMillis, preReqCode, true);
            }
        }
    }
}
