package com.devflux.deenone.core.eid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

public class EidNotificationScheduler {

  public static void scheduleEidReminders(Context context) {
    if (context == null || !EidModeManager.getInstance().isEidNotificationEnabled(context)) return;

    // Schedule morning Eid reminder
    scheduleDailyReminder(context, 201, 6, 30,
        "ঈদ মুবারক! তাকাব্বালাল্লাহু মিন্না ওয়া মিনকুম",
        "আজকের পবিত্র দিনে গোসল, উত্তম পোশাক, সুগন্ধি, তাকবীর পাঠ ও ঈদগাহে যাওয়ার সুন্নাত পালন করুন।");
  }

  private static void scheduleDailyReminder(Context context, int requestCode, int hour, int minute, String title, String message) {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (am == null) return;

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
      cal.add(Calendar.DAY_OF_YEAR, 1);
    }

    Intent intent = new Intent(context, EidNotificationReceiver.class);
    intent.setAction(EidNotificationReceiver.ACTION_EID_REMINDER);
    intent.putExtra(EidNotificationReceiver.EXTRA_TITLE, title);
    intent.putExtra(EidNotificationReceiver.EXTRA_MESSAGE, message);

    PendingIntent pi = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            : PendingIntent.FLAG_UPDATE_CURRENT
    );

    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
  }

  public static void cancelEidReminders(Context context) {
    if (context == null) return;
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (am == null) return;

    Intent intent = new Intent(context, EidNotificationReceiver.class);
    intent.setAction(EidNotificationReceiver.ACTION_EID_REMINDER);
    PendingIntent pi = PendingIntent.getBroadcast(
        context,
        201,
        intent,
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            : PendingIntent.FLAG_UPDATE_CURRENT
    );
    am.cancel(pi);
  }
}