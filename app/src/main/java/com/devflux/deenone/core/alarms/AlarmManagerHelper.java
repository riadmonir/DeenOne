package com.devflux.deenone.core.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.devflux.deenone.service.AdhanAlarmReceiver;
import com.devflux.deenone.utils.PrayerCalculator;

public class AlarmManagerHelper {

    private static final String TAG = "AlarmManagerHelper";

    private AlarmManagerHelper() {
        // Utility class
    }

    public static final int REQUEST_BASE_FAJR = 1001;
    public static final int REQUEST_BASE_SUNRISE = 1002;
    public static final int REQUEST_BASE_DHUHR = 1003;
    public static final int REQUEST_BASE_ASR = 1004;
    public static final int REQUEST_BASE_MAGHRIB = 1005;
    public static final int REQUEST_BASE_ISHA = 1006;
    public static final int REQUEST_BASE_TAHAJJUD = 1007;
    public static final int REQUEST_BASE_ISHRAQ = 1008;
    public static final int REQUEST_BASE_CHASHT = 1009;
    public static final int REQUEST_BASE_AWWABIN = 1010;
    public static final int REQUEST_BASE_JUMMAH = 1011;

    public static final int REQUEST_PRE_FAJR = 2001;
    public static final int REQUEST_PRE_DHUHR = 2003;
    public static final int REQUEST_PRE_ASR = 2004;
    public static final int REQUEST_PRE_MAGHRIB = 2005;
    public static final int REQUEST_PRE_ISHA = 2006;
    public static final int REQUEST_PRE_TAHAJJUD = 2007;
    public static final int REQUEST_PRE_ISHRAQ = 2008;
    public static final int REQUEST_PRE_CHASHT = 2009;
    public static final int REQUEST_PRE_AWWABIN = 2010;
    public static final int REQUEST_PRE_JUMMAH = 2011;

    public static void schedulePrayerAlarm(Context context, String prayerKey, String prayerDisplayName, long triggerAtMillis, int requestCode, boolean isPreReminder) {
        if (triggerAtMillis <= System.currentTimeMillis()) {
            return; // Past time, do not schedule
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, AdhanAlarmReceiver.class);
        intent.putExtra(AdhanAlarmReceiver.EXTRA_PRAYER_KEY, prayerKey);
        intent.putExtra(AdhanAlarmReceiver.EXTRA_PRAYER_NAME, prayerDisplayName);
        intent.putExtra(AdhanAlarmReceiver.EXTRA_IS_PRE_REMINDER, isPreReminder);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                } else {
                    // Fall back to standard inexact alarm on Android 12+ if exact alarm permission is not granted
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
            Log.d(TAG, "Scheduled alarm for " + prayerDisplayName + " at " + triggerAtMillis);
        } catch (SecurityException se) {
            Log.w(TAG, "SecurityException scheduling exact alarm: " + se.getMessage());
            // Fallback for strict devices
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }

    public static void cancelPrayerAlarm(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, AdhanAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE
        );

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    public static void scheduleAllDailyAlarms(Context context, PrayerCalculator.PrayerTimesResult times) {
        if (times == null) return;

        // 1. Farj Prayers (ফরজ নামাজ)
        scheduleWaqtAndPreReminder(context, "fajr", "ফজর", times.fajrMillis, REQUEST_BASE_FAJR, REQUEST_PRE_FAJR);
        schedulePrayerAlarm(context, "sunrise", "সূর্যোদয়", times.sunriseMillis, REQUEST_BASE_SUNRISE, false);
        scheduleWaqtAndPreReminder(context, "dhuhr", "যোহর", times.zohrMillis, REQUEST_BASE_DHUHR, REQUEST_PRE_DHUHR);
        scheduleWaqtAndPreReminder(context, "asr", "আসর", times.asrMillis, REQUEST_BASE_ASR, REQUEST_PRE_ASR);
        scheduleWaqtAndPreReminder(context, "maghrib", "মাগরিব", times.maghribMillis, REQUEST_BASE_MAGHRIB, REQUEST_PRE_MAGHRIB);
        scheduleWaqtAndPreReminder(context, "isha", "এশা", times.ishaMillis, REQUEST_BASE_ISHA, REQUEST_PRE_ISHA);

        // 2. Nafl Prayers (নফল নামাজ)
        if (times.tahajjudMillis > 0) {
            scheduleWaqtAndPreReminder(context, "tahajjud", "তাহাজ্জুদ", times.tahajjudMillis, REQUEST_BASE_TAHAJJUD, REQUEST_PRE_TAHAJJUD);
        }
        if (times.ishraqMillis > 0) {
            scheduleWaqtAndPreReminder(context, "ishraq", "ইশরাক", times.ishraqMillis, REQUEST_BASE_ISHRAQ, REQUEST_PRE_ISHRAQ);
        }
        if (times.chashtMillis > 0) {
            scheduleWaqtAndPreReminder(context, "chasht", "চাশত (দুহা)", times.chashtMillis, REQUEST_BASE_CHASHT, REQUEST_PRE_CHASHT);
        }
        if (times.awwabinMillis > 0) {
            scheduleWaqtAndPreReminder(context, "awwabin", "আওয়াবিন", times.awwabinMillis, REQUEST_BASE_AWWABIN, REQUEST_PRE_AWWABIN);
        }
    }

    private static void scheduleWaqtAndPreReminder(Context context, String key, String name, long waqtMillis, int baseReqCode, int preReqCode) {
        PrayerAlarmSettingsManager.PrayerAlarmConfig config = PrayerAlarmSettingsManager.getConfig(context, key);

        if (config.isEnabled && (config.isNotificationEnabled || config.isAlarmEnabled || config.isAzanEnabled)) {
            schedulePrayerAlarm(context, key, name, waqtMillis, baseReqCode, false);
        } else {
            cancelPrayerAlarm(context, baseReqCode);
        }

        if (config.isEnabled && config.prePrayerReminderMinutes > 0) {
            long preMillis = waqtMillis - (config.prePrayerReminderMinutes * 60 * 1000L);
            schedulePrayerAlarm(context, key, name, preMillis, preReqCode, true);
        } else {
            cancelPrayerAlarm(context, preReqCode);
        }
    }
}
