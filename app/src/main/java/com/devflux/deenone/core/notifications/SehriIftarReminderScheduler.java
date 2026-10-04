package com.devflux.deenone.core.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.service.AudioPlayerService;
import com.devflux.deenone.utils.PrayerCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SehriIftarReminderScheduler extends BroadcastReceiver {

    private static final String TAG = "SehriIftarScheduler";

    public static final String ACTION_SEHRI_REMINDER = "com.devflux.deenone.ACTION_SEHRI_REMINDER";
    public static final String ACTION_SEHRI_10MIN_WARNING = "com.devflux.deenone.ACTION_SEHRI_10MIN_WARNING";
    public static final String ACTION_IFTAR_REMINDER = "com.devflux.deenone.ACTION_IFTAR_REMINDER";

    public static final int REQ_SEHRI = 3001;
    public static final int REQ_IFTAR = 3002;
    public static final int REQ_SEHRI_10MIN = 3003;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        String action = intent.getAction();

        switch (action) {
            case ACTION_SEHRI_REMINDER:
                if (NotificationSettingsManager.isRamadanSehriEnabled(context)) {
                    String timeStr = intent.getStringExtra("extra_time_str");
                    if (timeStr == null) timeStr = "";
                    String bnFajr = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(timeStr);

                    // 1. Play Sehri Ringtone Audio in background via Foreground AudioPlayerService
                    try {
                        String soundKey = NotificationSettingsManager.getRamadanSehriSoundType(context);
                        int resId = NotificationSettingsManager.getRamadanSehriSoundResId(soundKey);
                        int repeatCount = NotificationSettingsManager.getRamadanSehriRepeatCount(context);
                        String soundUri = "android.resource://" + context.getPackageName() + "/" + resId;
                        AudioPlayerService.startAdhan(context, soundUri, "সেহরি অ্যালার্ম", "sehri", repeatCount);
                    } catch (Exception e) {
                        Log.e(TAG, "Failed to start Sehri audio alarm: " + e.getMessage());
                    }

                    // 2. Trigger vibration if enabled
                    if (NotificationSettingsManager.isRamadanSehriVibrationEnabled(context)) {
                        triggerVibration(context);
                    }

                    // 3. Send high-priority Heads-up Notification
                    NotificationHelper.sendSehriIftarNotification(
                            context,
                            true,
                            bnFajr,
                            "সেহরির শেষ সময় আসন্ন: " + bnFajr + "। সেহরি সমাপ্ত করুন এবং রোযার নিয়ত করুন: 'নাওয়াইতু আন আসুমা গাদাম মিন শাহরি রামাদান'।"
                    );
                }
                rescheduleNextSehri(context);
                break;

            case ACTION_SEHRI_10MIN_WARNING:
                if (NotificationSettingsManager.isRamadanSehriEnabled(context)) {
                    String timeStr = intent.getStringExtra("extra_time_str");
                    if (timeStr == null) timeStr = "";
                    String bnFajr = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(timeStr);

                    NotificationHelper.sendSehriIftarNotification(
                            context,
                            true,
                            bnFajr,
                            "সেহরির শেষ সময় আর মাত্র ১০ মিনিট বাকি (" + bnFajr + ")। দ্রুত সেহরি সমাপ্ত করুন এবং রোযার নিয়ত করুন।"
                    );
                }
                break;

            case ACTION_IFTAR_REMINDER:
                if (NotificationSettingsManager.isRamadanIftarEnabled(context)) {
                    String timeStr = intent.getStringExtra("extra_time_str");
                    if (timeStr == null) timeStr = "";
                    String bnMaghrib = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(timeStr);

                    // Trigger vibration if enabled
                    if (NotificationSettingsManager.isRamadanIftarVibrationEnabled(context)) {
                        triggerVibration(context);
                    }

                    NotificationHelper.sendSehriIftarNotification(
                            context,
                            false,
                            bnMaghrib,
                            "ইফতারের সময় হয়েছে (" + bnMaghrib + ")। বিসমিল্লাহ বলে ইফতার করুন: 'আল্লাহুম্মা লাকা সুমতু ওয়া আলা রিজক্বিকা আফতারতু'।"
                    );
                }
                rescheduleNextIftar(context);
                break;

            default:
                break;
        }
    }

    public static void scheduleDailySehriIftar(Context context, PrayerCalculator.PrayerTimesResult times) {
        if (context == null || times == null) return;
        if (!NotificationSettingsManager.isSehriIftarEnabled(context)) {
            cancelAll(context);
            return;
        }

        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

        // Schedule Sehri reminder
        if (NotificationSettingsManager.isRamadanSehriEnabled(context)) {
            long sehriTarget = computeNextSehriTriggerMillis(context, times, now);
            String fajrTimeStr = sdf.format(new Date(times.fajrMillis));
            if (sehriTarget > now) {
                scheduleAlarm(context, sehriTarget, ACTION_SEHRI_REMINDER, REQ_SEHRI, fajrTimeStr);
            }

            // 10-Minute warning before Sehri ends
            long warningTarget = times.fajrMillis - (10 * 60 * 1000L);
            if (warningTarget > now) {
                scheduleAlarm(context, warningTarget, ACTION_SEHRI_10MIN_WARNING, REQ_SEHRI_10MIN, fajrTimeStr);
            }
        }

        // Schedule Iftar reminder
        if (NotificationSettingsManager.isRamadanIftarEnabled(context)) {
            int iftarOffset = NotificationSettingsManager.getRamadanIftarOffsetMinutes(context);
            long iftarTarget = times.maghribMillis - (iftarOffset * 60 * 1000L);
            if (iftarTarget > now) {
                String maghribTimeStr = sdf.format(new Date(times.maghribMillis));
                scheduleAlarm(context, iftarTarget, ACTION_IFTAR_REMINDER, REQ_IFTAR, maghribTimeStr);
            }
        }

        Log.d(TAG, "Sehri and Iftar reminders scheduled successfully.");
    }

    public static long computeNextSehriTriggerMillis(Context context, PrayerCalculator.PrayerTimesResult todayTimes, long now) {
        if (NotificationSettingsManager.isRamadanSehriCustomTime(context)) {
            // User set custom clock time
            int hour = NotificationSettingsManager.getRamadanSehriAlarmHour(context);
            int minute = NotificationSettingsManager.getRamadanSehriAlarmMinute(context);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            long target = cal.getTimeInMillis();
            if (target <= now) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                target = cal.getTimeInMillis();
            }
            return target;
        } else {
            // Default 1 hour before Fajr
            long target = todayTimes.fajrMillis - (60 * 60 * 1000L);
            if (target <= now) {
                // If today's 1-hr-before Fajr already passed, calculate for tomorrow
                try {
                    LocationProvider.Coordinates loc = LocationProvider.getSavedOrCurrentLocation(context);
                    Calendar tomCal = Calendar.getInstance();
                    tomCal.add(Calendar.DAY_OF_MONTH, 1);
                    PrayerCalculator.PrayerTimesResult tomTimes =
                            PrayerCalculator.calculateForLocationWithContext(context, loc.latitude, loc.longitude, loc.timezone, tomCal);
                    target = tomTimes.fajrMillis - (60 * 60 * 1000L);
                } catch (Exception ignored) {
                    target = todayTimes.fajrMillis + (24 * 60 * 60 * 1000L) - (60 * 60 * 1000L);
                }
            }
            return target;
        }
    }

    private static void rescheduleNextSehri(Context context) {
        try {
            if (!NotificationSettingsManager.isRamadanSehriEnabled(context)) return;
            LocationProvider.Coordinates loc = LocationProvider.getSavedOrCurrentLocation(context);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            PrayerCalculator.PrayerTimesResult tomorrowTimes =
                    PrayerCalculator.calculateForLocationWithContext(context, loc.latitude, loc.longitude, loc.timezone, cal);

            long now = System.currentTimeMillis();
            long nextSehriTarget = computeNextSehriTriggerMillis(context, tomorrowTimes, now);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String timeStr = sdf.format(new Date(tomorrowTimes.fajrMillis));
            scheduleAlarm(context, nextSehriTarget, ACTION_SEHRI_REMINDER, REQ_SEHRI, timeStr);

            long warningTarget = tomorrowTimes.fajrMillis - (10 * 60 * 1000L);
            if (warningTarget > now) {
                scheduleAlarm(context, warningTarget, ACTION_SEHRI_10MIN_WARNING, REQ_SEHRI_10MIN, timeStr);
            }
        } catch (Exception e) {
            Log.w(TAG, "Error rescheduling next Sehri: " + e.getMessage());
        }
    }

    public static void sendTestSehriNotification(Context context) {
        if (context == null) return;

        String fajrTimeStr = "০৪:৪৭ AM";
        try {
            LocationProvider.Coordinates loc = LocationProvider.getSavedOrCurrentLocation(context);
            Calendar cal = Calendar.getInstance();
            PrayerCalculator.PrayerTimesResult times = PrayerCalculator.calculateForLocationWithContext(context, loc.latitude, loc.longitude, loc.timezone, cal);
            if (times != null && times.fajrMillis > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                fajrTimeStr = com.devflux.deenone.utils.BengaliNumberUtil.toBengali(sdf.format(new Date(times.fajrMillis)));
            }
        } catch (Exception ignored) {}

        // Play selected sound with user's configured repeat count
        try {
            String soundKey = NotificationSettingsManager.getRamadanSehriSoundType(context);
            int resId = NotificationSettingsManager.getRamadanSehriSoundResId(soundKey);
            int repeatCount = NotificationSettingsManager.getRamadanSehriRepeatCount(context);
            String soundUri = "android.resource://" + context.getPackageName() + "/" + resId;
            AudioPlayerService.startAdhan(context, soundUri, "সেহরি অ্যালার্ম", "sehri", repeatCount);
        } catch (Exception e) {
            Log.e(TAG, "Failed to start Sehri test audio: " + e.getMessage());
        }

        if (NotificationSettingsManager.isRamadanSehriVibrationEnabled(context)) {
            triggerVibration(context);
        }

        NotificationHelper.sendSehriIftarNotification(
            context,
            true,
            fajrTimeStr,
            "সেহরির শেষ সময় আসন্ন: " + fajrTimeStr + "। দ্রুত সেহরি সমাপ্ত করুন এবং রোযার নিয়ত করুন: 'নাওয়াইতু আন আসুমা গাদাম মিন শাহরি রামাদান'।"
        );
    }

    public static void sendTestIftarNotification(Context context) {
        if (context == null) return;
        if (NotificationSettingsManager.isRamadanIftarVibrationEnabled(context)) {
            triggerVibration(context);
        }
        NotificationHelper.sendSehriIftarNotification(
            context,
            false,
            "০৬:১৮ PM",
            "ইফতারের সময় হয়েছে। বিসমিল্লাহ বলে ইফতার করুন: 'আল্লাহুম্মা লাকা সুমতু ওয়া আলা রিজক্বিকা আফতারতু'।"
        );
    }

    private static void rescheduleNextIftar(Context context) {
        try {
            if (!NotificationSettingsManager.isRamadanIftarEnabled(context)) return;
            LocationProvider.Coordinates loc = LocationProvider.getSavedOrCurrentLocation(context);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            PrayerCalculator.PrayerTimesResult tomorrowTimes =
                    PrayerCalculator.calculateForLocationWithContext(context, loc.latitude, loc.longitude, loc.timezone, cal);

            int iftarOffset = NotificationSettingsManager.getRamadanIftarOffsetMinutes(context);
            long nextIftarTarget = tomorrowTimes.maghribMillis - (iftarOffset * 60 * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String timeStr = sdf.format(new Date(tomorrowTimes.maghribMillis));
            scheduleAlarm(context, nextIftarTarget, ACTION_IFTAR_REMINDER, REQ_IFTAR, timeStr);
        } catch (Exception e) {
            Log.w(TAG, "Error rescheduling next Iftar: " + e.getMessage());
        }
    }

    public static void triggerVibration(Context context) {
        try {
            long[] pattern = new long[]{0, 800, 400, 800, 400, 800};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                VibratorManager vibratorManager = (VibratorManager) context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                if (vibratorManager != null) {
                    Vibrator vibrator = vibratorManager.getDefaultVibrator();
                    if (vibrator != null && vibrator.hasVibrator()) {
                        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    }
                }
            } else {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null && vibrator.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    } else {
                        vibrator.vibrate(pattern, -1);
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Vibration execution failed: " + e.getMessage());
        }
    }

    private static void scheduleAlarm(Context context, long triggerAtMillis, String action, int requestCode, String timeStr) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, SehriIftarReminderScheduler.class);
        intent.setAction(action);
        intent.putExtra("extra_time_str", timeStr);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
            Log.d(TAG, "Alarm scheduled successfully for action: " + action + " at millis: " + triggerAtMillis);
        } catch (SecurityException se) {
            Log.e(TAG, "SecurityException scheduling alarm (exact alarm permission missing): " + se.getMessage());
            try {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } catch (Exception ex) {
                Log.e(TAG, "Fallback alarm scheduling failed: " + ex.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to schedule alarm for " + action + ": " + e.getMessage());
        }
    }

    public static void cancelAll(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager == null) return;

            Intent sehriIntent = new Intent(context, SehriIftarReminderScheduler.class);
            sehriIntent.setAction(ACTION_SEHRI_REMINDER);
            PendingIntent piSehri = PendingIntent.getBroadcast(
                    context, REQ_SEHRI, sehriIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(piSehri);

            Intent iftarIntent = new Intent(context, SehriIftarReminderScheduler.class);
            iftarIntent.setAction(ACTION_IFTAR_REMINDER);
            PendingIntent piIftar = PendingIntent.getBroadcast(
                    context, REQ_IFTAR, iftarIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
            alarmManager.cancel(piIftar);

            Log.d(TAG, "All Sehri & Iftar reminders cancelled.");
        } catch (Exception e) {
            Log.w(TAG, "Error cancelling Sehri/Iftar alarms: " + e.getMessage());
        }
    }
}
