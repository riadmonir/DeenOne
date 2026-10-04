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

public class IstigfarReminderScheduler extends BroadcastReceiver {

    private static final String TAG = "IstigfarReminderSched";

    public static final String ACTION_ISTIGFAR_REMINDER = "com.devflux.deenone.ACTION_ISTIGFAR_REMINDER";
    public static final String ACTION_SAYYIDUL_ISTIGFAR_MORNING = "com.devflux.deenone.ACTION_SAYYIDUL_ISTIGFAR_MORNING";
    public static final String ACTION_SAYYIDUL_ISTIGFAR_EVENING = "com.devflux.deenone.ACTION_SAYYIDUL_ISTIGFAR_EVENING";
    public static final String ACTION_BEDTIME_ISTIGFAR = "com.devflux.deenone.ACTION_BEDTIME_ISTIGFAR";

    public static final int REQ_ISTIGFAR_PERIODIC = 7101;
    public static final int REQ_SAYYIDUL_MORNING = 7102;
    public static final int REQ_SAYYIDUL_EVENING = 7103;
    public static final int REQ_BEDTIME_ISTIGFAR = 7104;

    private static final String[] ISTIGFAR_TITLES = {
        "ইস্তিগফার ও ক্ষমা প্রার্থনার ডাক",
        "আল্লাহর দরবারে তওবা ও মাগফিরাত",
        "দৈনিক ইস্তিগফার ও অন্তরের প্রশান্তি",
        "সাইয়্যিদুল ইস্তিগফার পাঠের শ্রেষ্ঠ সময়"
    };

    private static final String[] ISTIGFAR_TEXTS = {
        "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ\n'আস্তাগফিরুল্লাহ ওয়া আতূবু ইলাইহি' — আমি আল্লাহর কাছে ক্ষমা প্রার্থনা করছি এবং তাঁরই দিকে প্রত্যাবর্তন করছি। প্রতিদিন অন্তত ১০০ বার পাঠের সুন্নাত পালন করুন।",
        "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ الَّذِي لَا إِلَهَ إِلَّا هُوَ الْحَيَّ الْقَيُّومَ وَأَتُوبُ إِلَيْهِ\n'আস্তাগফিরুল্লাহাল আজীমাল্লাযী লা ইলাহা ইল্লা হুয়াল হাইয়্যুল ক্বাইয়্যূমু ওয়া আতূবু ইলাইহি'। ইস্তিগফারের মাধ্যমে রিযিক ও রহমতের দ্বার উন্মুক্ত হয়।",
        "রাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি নিয়মিত ইস্তিগফার করে, আল্লাহ তাকে সকল সংকট থেকে উত্তরণের পথ করে দেন এবং সকল দুশ্চিন্তা দূর করে দেন।' (আবু দাউদ)"
    };

    public static final String SAYYIDUL_ISTIGFAR_TITLE = "সাইয়্যিদুল ইস্তিগফার (তওবার শ্রেষ্ঠ দোয়া)";
    public static final String SAYYIDUL_ISTIGFAR_TEXT = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ...\n'আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা, খালাক্বতানী ওয়া আনা আবদুকা...' যে ব্যক্তি সকালে বা সন্ধ্যায় দৃঢ় বিশ্বাসের সাথে এটি পড়বে সে জান্নাতবাসী হবে। (সহীহ বুখারী)";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || intent.getAction() == null) return;
        String action = intent.getAction();
        Log.d(TAG, "Istigfar BroadcastReceiver triggered with action: " + action);

        // Acquire WakeLock so CPU doesn't sleep while notification & audio are processing
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = null;
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DeenOne:IstigfarWakeLock");
            wakeLock.acquire(15000); // 15 seconds max
        }

        try {
            String lang = NotificationSettingsManager.getIstigfarAudioLanguage(context);
            if (ACTION_ISTIGFAR_REMINDER.equals(action)) {
                if (NotificationSettingsManager.isIstigfarReminderEnabled(context)) {
                    if (!NotificationSettingsManager.isWithinIstigfarSilentHours(context)) {
                        int index = new Random().nextInt(ISTIGFAR_TEXTS.length);
                        String title = ISTIGFAR_TITLES[index % ISTIGFAR_TITLES.length];
                        String text = ISTIGFAR_TEXTS[index];
                        NotificationHelper.sendIstigfarNotification(context, title, text, lang);
                        Log.d(TAG, "Successfully sent Istigfar periodic notification with lang: " + lang);
                    } else {
                        Log.d(TAG, "Istigfar reminder skipped due to user-configured active Silent Hours (Do Not Disturb).");
                    }
                } else {
                    Log.d(TAG, "Istigfar reminder skipped because master switch is disabled.");
                }
                // Reschedule next periodic alarm cycle
                scheduleIstigfarReminders(context);
            } else if (ACTION_SAYYIDUL_ISTIGFAR_MORNING.equals(action) || ACTION_SAYYIDUL_ISTIGFAR_EVENING.equals(action)) {
                if (NotificationSettingsManager.isIstigfarReminderEnabled(context) && NotificationSettingsManager.isIstigfarSayyidulEnabled(context)) {
                    if (!NotificationSettingsManager.isWithinIstigfarSilentHours(context)) {
                        NotificationHelper.sendIstigfarNotification(context, SAYYIDUL_ISTIGFAR_TITLE, SAYYIDUL_ISTIGFAR_TEXT, lang);
                        Log.d(TAG, "Successfully sent Sayyidul Istigfar notification with lang: " + lang);
                    }
                }
                scheduleSayyidulIstigfar(context);
            } else if (ACTION_BEDTIME_ISTIGFAR.equals(action)) {
                if (NotificationSettingsManager.isIstigfarReminderEnabled(context) && NotificationSettingsManager.isIstigfarBedtimeEnabled(context)) {
                    if (!NotificationSettingsManager.isWithinIstigfarSilentHours(context)) {
                        NotificationHelper.sendIstigfarNotification(
                            context,
                            "ঘুমানোর পূর্বে তওবা ও ইস্তিগফার",
                            "আজকের দিনের ভুলত্রুটির জন্য অন্তর থেকে আল্লাহর দরবারে ক্ষমা প্রার্থনা করুন এবং পবিত্র মনে ঘুমান। 'আস্তাগফিরুল্লাহাল্লাযী লা ইলাহা ইল্লা হুয়াল হাইয়্যুল ক্বাইয়্যুম ওয়া আতূবু ইলাইহি' ৩ বার পাঠ করুন।",
                            lang
                        );
                        Log.d(TAG, "Successfully sent Bedtime Istigfar notification with lang: " + lang);
                    }
                }
                scheduleBedtimeIstigfar(context);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in Istigfar onReceive: " + e.getMessage(), e);
        } finally {
            releaseWakeLockSafe(wakeLock);
        }
    }

    public static void playIstigfarAudio(Context context) {
        playIstigfarAudio(context, null);
    }

    public static void playIstigfarAudio(Context context, PowerManager.WakeLock wakeLock) {
        if (context == null) {
            releaseWakeLockSafe(wakeLock);
            return;
        }
        try {
            String lang = NotificationSettingsManager.getIstigfarAudioLanguage(context);
            int rawRes = R.raw.read_istigfar_ar;
            if ("bn".equalsIgnoreCase(lang)) {
                rawRes = R.raw.read_istigfar_bn;
            } else if ("en".equalsIgnoreCase(lang)) {
                rawRes = R.raw.read_istigfar_en;
            }

            AudioAttributes audioAttributes = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .build();
            }

            MediaPlayer mp = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && audioAttributes != null) {
                mp = MediaPlayer.create(context.getApplicationContext(), rawRes, audioAttributes, 0);
            }
            if (mp == null) {
                mp = MediaPlayer.create(context.getApplicationContext(), rawRes);
            }

            if (mp != null) {
                mp.setWakeMode(context.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mp.setOnCompletionListener(player -> {
                    try {
                        player.release();
                    } catch (Exception ignored) {}
                    releaseWakeLockSafe(wakeLock);
                });
                mp.setOnErrorListener((player, what, extra) -> {
                    try {
                        player.release();
                    } catch (Exception ignored) {}
                    releaseWakeLockSafe(wakeLock);
                    return true;
                });
                mp.start();
                Log.d(TAG, "Successfully started Istigfar audio for language: " + lang);
            } else {
                Log.w(TAG, "MediaPlayer.create returned null for rawRes: " + rawRes);
                releaseWakeLockSafe(wakeLock);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to play istigfar audio: " + e.getMessage(), e);
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
        scheduleIstigfarReminders(context);
        scheduleSayyidulIstigfar(context);
        scheduleBedtimeIstigfar(context);
    }

    public static void scheduleIstigfarReminders(Context context) {
        if (context == null) return;
        if (!NotificationSettingsManager.isIstigfarReminderEnabled(context)) {
            cancelPeriodic(context);
            return;
        }

        int intervalMinutes = NotificationSettingsManager.getIstigfarIntervalMinutes(context);
        if (intervalMinutes <= 0) intervalMinutes = 15;

        long triggerAtMillis = System.currentTimeMillis() + (intervalMinutes * 60 * 1000L);
        scheduleExactAlarm(context, triggerAtMillis, ACTION_ISTIGFAR_REMINDER, REQ_ISTIGFAR_PERIODIC);
        Log.d(TAG, "Istigfar periodic reminder scheduled in " + intervalMinutes + " minutes (time: " + triggerAtMillis + ")");
    }

    public static void scheduleSayyidulIstigfar(Context context) {
        if (context == null) return;
        if (!NotificationSettingsManager.isIstigfarReminderEnabled(context) || !NotificationSettingsManager.isIstigfarSayyidulEnabled(context)) {
            cancelSayyidul(context);
            return;
        }

        // Morning Sayyidul: 06:45 AM
        scheduleDailyAlarm(context, 6, 45, ACTION_SAYYIDUL_ISTIGFAR_MORNING, REQ_SAYYIDUL_MORNING);

        // Evening Sayyidul: 05:45 PM
        scheduleDailyAlarm(context, 17, 45, ACTION_SAYYIDUL_ISTIGFAR_EVENING, REQ_SAYYIDUL_EVENING);
    }

    public static void scheduleBedtimeIstigfar(Context context) {
        if (context == null) return;
        if (!NotificationSettingsManager.isIstigfarReminderEnabled(context) || !NotificationSettingsManager.isIstigfarBedtimeEnabled(context)) {
            cancelBedtime(context);
            return;
        }

        // Bedtime: 10:15 PM
        scheduleDailyAlarm(context, 22, 15, ACTION_BEDTIME_ISTIGFAR, REQ_BEDTIME_ISTIGFAR);
    }

    public static void sendTestReminder(Context context) {
        if (context == null) return;
        String lang = NotificationSettingsManager.getIstigfarAudioLanguage(context);
        NotificationHelper.sendIstigfarNotification(
            context,
            "ইস্তিগফার স্মরণিকা (টেস্ট)",
            "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ — 'আমি আল্লাহর কাছে ক্ষমা প্রার্থনা করছি এবং তাঁরই দিকে প্রত্যাবর্তন করছি।'",
            "test"
        );
    }

    private static void scheduleDailyAlarm(Context context, int hour, int minute, String action, int requestCode) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        scheduleExactAlarm(context, cal.getTimeInMillis(), action, requestCode);
    }

    private static void scheduleExactAlarm(Context context, long triggerAtMillis, String action, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, IstigfarReminderScheduler.class);
        intent.setAction(action);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    Log.d(TAG, "Scheduled setExactAndAllowWhileIdle for " + action + " at " + triggerAtMillis);
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                    Log.w(TAG, "Exact alarms not allowed. Fallback to setAndAllowWhileIdle for " + action + " at " + triggerAtMillis);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                Log.d(TAG, "Scheduled setExactAndAllowWhileIdle for " + action + " at " + triggerAtMillis);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
                Log.d(TAG, "Scheduled setExact for " + action + " at " + triggerAtMillis);
            }
        } catch (SecurityException se) {
            Log.w(TAG, "SecurityException on exact alarm scheduling: " + se.getMessage() + ", using fallback");
            try {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } catch (Exception e) {
                Log.e(TAG, "Failed fallback scheduling for " + action + ": " + e.getMessage(), e);
            }
        } catch (Exception e) {
            Log.e(TAG, "Critical error scheduling alarm for " + action + ": " + e.getMessage(), e);
        }
    }

    public static void cancelPeriodic(Context context) {
        cancelAlarm(context, ACTION_ISTIGFAR_REMINDER, REQ_ISTIGFAR_PERIODIC);
    }

    public static void cancelSayyidul(Context context) {
        cancelAlarm(context, ACTION_SAYYIDUL_ISTIGFAR_MORNING, REQ_SAYYIDUL_MORNING);
        cancelAlarm(context, ACTION_SAYYIDUL_ISTIGFAR_EVENING, REQ_SAYYIDUL_EVENING);
    }

    public static void cancelBedtime(Context context) {
        cancelAlarm(context, ACTION_BEDTIME_ISTIGFAR, REQ_BEDTIME_ISTIGFAR);
    }

    private static void cancelAlarm(Context context, String action, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, IstigfarReminderScheduler.class);
        intent.setAction(action);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pi != null) {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static void cancelAll(Context context) {
        cancelPeriodic(context);
        cancelSayyidul(context);
        cancelBedtime(context);
    }
}
