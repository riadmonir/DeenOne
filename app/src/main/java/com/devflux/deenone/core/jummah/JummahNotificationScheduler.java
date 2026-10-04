package com.devflux.deenone.core.jummah;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class JummahNotificationScheduler {

  private static final String PREF_JUMMAH = "deanone_jummah_mode_prefs";
  private static final String KEY_NOTIF_ENABLED = "key_jummah_notif_enabled";

  public static boolean isJummahNotificationEnabled(Context context) {
    if (context == null) return true;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    return prefs.getBoolean(KEY_NOTIF_ENABLED, true);
  }

  public static void setJummahNotificationEnabled(Context context, boolean enabled) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_JUMMAH, Context.MODE_PRIVATE);
    prefs.edit().putBoolean(KEY_NOTIF_ENABLED, enabled).apply();
    if (enabled) {
      scheduleAllJummahReminders(context);
    } else {
      cancelAllJummahReminders(context);
    }
  }

  public static void scheduleAllJummahReminders(Context context) {
    if (context == null || !isJummahNotificationEnabled(context)) return;

    // 1. Thursday Eve 8:00 PM - দরূদ পাঠের সূচনা
    scheduleWeeklyReminder(context, 101, Calendar.THURSDAY, 20, 0,
        "জুমার রাত: দরূদ শরীফ পাঠের আহ্বান",
        "রাসূলুল্লাহ (ﷺ) বলেছেন: জুমার রাত ও দিনে আমার ওপর অধিক পরিমাণে দরূদ পাঠ করো। (বায়হাকী)");

    // 2. Friday Morning 8:00 AM - সূরা আল-কাহাফ ও গোসল
    scheduleWeeklyReminder(context, 102, Calendar.FRIDAY, 8, 0,
        "জুমার সকাল: সূরা আল-কাহাফ ও গোসল প্রস্তুতি",
        "আজ জুমার দিন: সূরা আল-কাহাফ তিলাওয়াত করুন এবং জুমার গোসল ও উত্তম পরিচ্ছন্নতার প্রস্তুতি নিন।");

    // 3. Friday Pre-Jummah 11:30 AM - দ্রুত মসজিদে যাওয়া
    scheduleWeeklyReminder(context, 103, Calendar.FRIDAY, 11, 30,
        "মসজিদে দ্রুত গমন ও খুতবার প্রস্তুতি",
        "আগে আগে মসজিদে রওনা দিন, তাহিয়্যাতুল মসজিদ আদায় করুন এবং নিরব থেকে মনোযোগ সহকারে খুতবা শুনুন।");

    // 4. Friday Asr 4:30 PM - সা'আতুল ইজাবাহ (দোয়া কবুলের সময়)
    scheduleWeeklyReminder(context, 104, Calendar.FRIDAY, 16, 30,
        "সা'আতুল ইজাবাহ: দোয়া কবুলের বিশেষ মুহূর্ত",
        "আসরের শেষ প্রহরে দোয়া কবুলের বিশেষ সময়। আপনার জীবনের সকল প্রয়োজন আল্লাহর দরবারে পেশ করুন।");
  }

  private static void scheduleWeeklyReminder(Context context, int requestCode, int dayOfWeek, int hour, int minute, String title, String message) {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (am == null) return;

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    // If time already passed this week, advance by 7 days
    if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
      cal.add(Calendar.DAY_OF_YEAR, 7);
    }

    Intent intent = new Intent(context, JummahNotificationReceiver.class);
    intent.setAction(JummahNotificationReceiver.ACTION_JUMMAH_REMINDER);
    intent.putExtra(JummahNotificationReceiver.EXTRA_TITLE, title);
    intent.putExtra(JummahNotificationReceiver.EXTRA_MESSAGE, message);

    PendingIntent pi = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            : PendingIntent.FLAG_UPDATE_CURRENT
    );

    long intervalMillis = 7 * 24 * 60 * 60 * 1000L; // Weekly
    am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), intervalMillis, pi);
  }

  public static void cancelAllJummahReminders(Context context) {
    if (context == null) return;
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (am == null) return;

    int[] requestCodes = {101, 102, 103, 104};
    for (int code : requestCodes) {
      Intent intent = new Intent(context, JummahNotificationReceiver.class);
      intent.setAction(JummahNotificationReceiver.ACTION_JUMMAH_REMINDER);
      PendingIntent pi = PendingIntent.getBroadcast(
          context,
          code,
          intent,
          Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
              ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
              : PendingIntent.FLAG_UPDATE_CURRENT
      );
      am.cancel(pi);
    }
  }
}