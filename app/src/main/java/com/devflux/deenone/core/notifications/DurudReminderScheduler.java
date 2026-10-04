package com.devflux.deenone.core.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;

import com.devflux.deenone.R;

import java.util.Calendar;
import java.util.Random;

public class DurudReminderScheduler extends BroadcastReceiver {

    private static final String TAG = "DurudReminderSched";

    public static final String ACTION_DURUD_REMINDER = "com.devflux.deenone.ACTION_DURUD_REMINDER";
    public static final String ACTION_JUMUAH_DURUD_REMINDER = "com.devflux.deenone.ACTION_JUMUAH_DURUD_REMINDER";

    public static final int REQ_DURUD_PERIODIC = 7001;
    public static final int REQ_DURUD_JUMUAH = 7002;

    private static final String[] DURUD_COLLECTION_TITLES = {
        "দুরুদ শরীফ পাঠের স্মরণিকা",
        "বিশ্বনবী (ﷺ)-এর ওপর দরূদ ও সালাম",
        "দৈনিক দরূদ ও বরকতময় সালাওয়াত",
        "প্রিয় নবীজির প্রতি মহব্বত ও দরূদ",
        "জুমাবার: দরূদ শরীফ পাঠের শ্রেষ্ঠ দিন"
    };

    private static final String[] DURUD_COLLECTION_TEXTS = {
        "রাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি আমার ওপর একবার দরূদ পাঠ করবে, আল্লাহ তার ওপর দশটি রহমত বর্ষণ করবেন।' (সহীহ মুসলিম)\n\nاللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ",
        "সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম (ﷺ)। বেশি বেশি দরূদ পাঠ করুন এবং অন্তরে শান্তি ও রহমত লাভ করুন।",
        "দরূদে ইব্রাহিম: 'আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিঁও ওয়া আলা আলি মুহাম্মাদ, কামা সাল্লাইতা আলা ইবরাহিমা ওয়া আলা আলি ইবরাহিম, ইন্নাকা হামিদুম মাজিদ।' প্রতিদিনের টার্গেট পূরণ করুন।",
        "নবী করীম (ﷺ) বলেছেন: 'কিয়ামতের দিন আমার সবচেয়ে নিকটবর্তী হবে সেই ব্যক্তি, যে আমার ওপর সবচেয়ে বেশি দরূদ পাঠ করে।' (তিরমিযী)",
        "জুমার দিনে ও রাতে রাসূলুল্লাহ (ﷺ)-এর ওপর অধিক পরিমাণে দরূদ পাঠের বিশেষ সুন্নাত পালন করুন।"
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || intent.getAction() == null) return;
        String action = intent.getAction();

        Log.d(TAG, "Durud BroadcastReceiver triggered with action: " + action);
        // Acquire WakeLock to guarantee CPU stays active during notification processing
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DeenOne:DurudWakeLock");
            wakeLock.acquire(15000); // 15s max
        }

        try {
            String audioKey = NotificationSettingsManager.getDurudAudioKey(context);
            if (ACTION_DURUD_REMINDER.equals(action)) {
                if (NotificationSettingsManager.isDurudReminderEnabled(context)) {
                    if (!NotificationSettingsManager.isWithinDurudSilentHours(context)) {
                        int index = new Random().nextInt(DURUD_COLLECTION_TEXTS.length);
                        String title = DURUD_COLLECTION_TITLES[index % DURUD_COLLECTION_TITLES.length];
                        String text = DURUD_COLLECTION_TEXTS[index];
                        NotificationHelper.sendDurudNotification(context, title, text, audioKey);
                        Log.d(TAG, "Successfully sent Durud periodic notification with audioKey: " + audioKey);
                    } else {
                        Log.d(TAG, "Suppressed Durud reminder audio/notif due to user-configured active Silent Hours (Do Not Disturb)");
                    }
                } else {
                    Log.d(TAG, "Durud reminder skipped because master switch is disabled.");
                }
                // Reschedule next cycle
                scheduleDurudReminders(context);
            } else if (ACTION_JUMUAH_DURUD_REMINDER.equals(action)) {
                if (NotificationSettingsManager.isDurudReminderEnabled(context) && NotificationSettingsManager.isDurudJumuahSpecialEnabled(context)) {
                    if (!NotificationSettingsManager.isWithinDurudSilentHours(context)) {
                        Calendar cal = Calendar.getInstance();
                        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                            NotificationHelper.sendDurudNotification(
                                context,
                                "পবিত্র জুমু'আ: দরূদ শরীফ পাঠের বিশেষ সময়",
                                "রাসূলুল্লাহ (ﷺ) বলেছেন: 'তোমরা জুমার দিন আমার ওপর বেশি বেশি দরূদ পাঠ করো, কারণ তোমাদের দরূদ আমার নিকট পেশ করা হয়।' (আবু দাউদ)\n\nاللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّনَا مُحَمَّদٍ",
                                audioKey
                            );
                            Log.d(TAG, "Successfully sent Friday Jumuah Durud notification with audioKey: " + audioKey);
                        }
                    }
                }
                scheduleJumuahDurudReminder(context);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in Durud onReceive: " + e.getMessage(), e);
        } finally {
            releaseWakeLockSafe(wakeLock);
        }
    }

    public static void playDurudAudio(Context context) {
        playDurudAudio(context, null);
    }

    public static void playDurudAudio(Context context, PowerManager.WakeLock wakeLock) {
        if (context == null) {
            releaseWakeLockSafe(wakeLock);
            return;
        }
        String audioKey = NotificationSettingsManager.getDurudAudioKey(context);
        int soundResId = R.raw.read_durud_ar1; // default Arabic - 1
        if ("ar2".equalsIgnoreCase(audioKey)) {
            soundResId = R.raw.read_durud_ar2;
        } else if ("ar3".equalsIgnoreCase(audioKey)) {
            soundResId = R.raw.read_durud_ar3;
        } else if ("bn".equalsIgnoreCase(audioKey)) {
            soundResId = R.raw.read_durud_bn;
        } else if ("en".equalsIgnoreCase(audioKey)) {
            soundResId = R.raw.read_durud_en;
        }

        try {
            AudioAttributes audioAttributes = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build();
            }

            MediaPlayer player = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && audioAttributes != null) {
                player = MediaPlayer.create(context.getApplicationContext(), soundResId, audioAttributes, 0);
            } else {
                player = MediaPlayer.create(context.getApplicationContext(), soundResId);
            }

            if (player != null) {
                if (wakeLock != null) {
                    player.setWakeMode(context.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                }
                final MediaPlayer finalPlayer = player;
                final PowerManager.WakeLock finalWakeLock = wakeLock;
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        try {
                            if (finalPlayer != null) {
                                finalPlayer.reset();
                                finalPlayer.release();
                            }
                        } catch (Exception ignored) {}
                        releaseWakeLockSafe(finalWakeLock);
                    }
                });
                player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        try {
                            if (finalPlayer != null) {
                                finalPlayer.reset();
                                finalPlayer.release();
                            }
                        } catch (Exception ignored) {}
                        releaseWakeLockSafe(finalWakeLock);
                        return true;
                    }
                });
                player.start();
                Log.d(TAG, "Durud voice audio playback started successfully (Key: " + audioKey + ")");
            } else {
                Log.w(TAG, "MediaPlayer.create returned null for soundResId: " + soundResId);
                releaseWakeLockSafe(wakeLock);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to play Durud audio: " + e.getMessage(), e);
            releaseWakeLockSafe(wakeLock);
        }
    }

    private static void releaseWakeLockSafe(PowerManager.WakeLock wakeLock) {
        if (wakeLock != null && wakeLock.isHeld()) {
            try {
                wakeLock.release();
            } catch (Exception ignored) {}
        }
    }

    public static void scheduleAll(Context context) {
        if (context == null) return;
        scheduleDurudReminders(context);
        scheduleJumuahDurudReminder(context);
    }

    public static void cancelAll(Context context) {
        if (context == null) return;
        cancelAlarm(context, REQ_DURUD_PERIODIC);
        cancelAlarm(context, REQ_DURUD_JUMUAH);
    }

    public static void updateAllSchedules(Context context) {
        scheduleAll(context);
    }

    public static void scheduleDurudReminders(Context context) {
        if (context == null) return;
        if (!NotificationSettingsManager.isDurudReminderEnabled(context)) {
            cancelAlarm(context, REQ_DURUD_PERIODIC);
            Log.d(TAG, "Durud periodic reminder cancelled (disabled).");
            return;
        }

        int intervalMinutes = NotificationSettingsManager.getDurudIntervalMinutes(context);
        int intervalHours = NotificationSettingsManager.getDurudIntervalHours(context);
        long delayMillis = (intervalHours * 60L + intervalMinutes) * 60L * 1000L;
        if (delayMillis < 60_000L) {
            delayMillis = 15 * 60_000L; // Safety floor: minimum 15 mins
        }

        long triggerAtMillis = System.currentTimeMillis() + delayMillis;
        scheduleExactAlarm(context, triggerAtMillis, ACTION_DURUD_REMINDER, REQ_DURUD_PERIODIC);
        Log.d(TAG, "Scheduled next Durud periodic reminder in " + (delayMillis / 60000) + " mins");
    }

    public static void scheduleJumuahDurudReminder(Context context) {
        if (context == null) return;
        if (!NotificationSettingsManager.isDurudReminderEnabled(context) ||
            !NotificationSettingsManager.isDurudJumuahSpecialEnabled(context)) {
            cancelAlarm(context, REQ_DURUD_JUMUAH);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

        scheduleExactAlarm(context, cal.getTimeInMillis(), ACTION_JUMUAH_DURUD_REMINDER, REQ_DURUD_JUMUAH);
        Log.d(TAG, "Scheduled next Friday Jumuah Durud reminder for: " + cal.getTime().toString());
    }

    public static void sendTestReminder(Context context) {
        if (context == null) return;
        String audioKey = NotificationSettingsManager.getDurudAudioKey(context);
        NotificationHelper.sendDurudNotification(
            context,
            "দুরুদ শরীফ স্মরণিকা (টেস্ট)",
            "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ\n\nরাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি আমার ওপর একবার দরূদ পাঠ করবে, আল্লাহ তার ওপর দশটি রহমত বর্ষণ করবেন।' (সহীহ মুসলিম)",
            audioKey
        );
    }

    private static void scheduleExactAlarm(Context context, long triggerAtMillis, String action, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, DurudReminderScheduler.class);
        intent.setAction(action);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                        Log.d(TAG, "Scheduled exact alarm (API 31+) for action: " + action);
                    } else {
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                        Log.w(TAG, "Exact alarms permission missing; fallback to setAndAllowWhileIdle for: " + action);
                    }
                } else {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    Log.d(TAG, "Scheduled exact alarm (API 23+) for action: " + action);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                Log.d(TAG, "Scheduled exact alarm (API 19+) for action: " + action);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        } catch (SecurityException se) {
            Log.e(TAG, "SecurityException while scheduling exact alarm: " + se.getMessage());
            try {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } catch (Exception e) {
                Log.e(TAG, "Fallback alarm scheduling also failed: " + e.getMessage());
            }
        }
    }

    public static void cancelAlarm(Context context, int requestCode) {
        if (context == null) return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, DurudReminderScheduler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
