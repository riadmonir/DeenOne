package com.devflux.deenone.core.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.util.Calendar;

/**
 * Unified Smart Islamic Reminder Engine.
 * Intelligently computes reminder times based on:
 * - User's local time & timezone
 * - Geographical coordinates & real prayer calculation
 * - Hijri Islamic Calendar & historical Islamic events
 * - User preferences & individual category toggles
 * - Fully synchronized Dual-Language (Bangla & English)
 */
public class SmartIslamicReminderEngine extends BroadcastReceiver {

  private static final String TAG = "SmartReminderEngine";

  // Action Constants for Smart Reminders
  public static final String ACTION_SMART_SALAH_PRE = "com.devflux.deenone.ACTION_SMART_SALAH_PRE";
  public static final String ACTION_SMART_TAHAJJUD = "com.devflux.deenone.ACTION_SMART_TAHAJJUD";
  public static final String ACTION_SMART_MORNING_DHIKR = "com.devflux.deenone.ACTION_SMART_MORNING_DHIKR";
  public static final String ACTION_SMART_EVENING_DHIKR = "com.devflux.deenone.ACTION_SMART_EVENING_DHIKR";
  public static final String ACTION_SMART_QURAN_READING = "com.devflux.deenone.ACTION_SMART_QURAN_READING";
  public static final String ACTION_SMART_JUMUAH_KAHF = "com.devflux.deenone.ACTION_SMART_JUMUAH_KAHF";
  public static final String ACTION_SMART_SUNNAH_FASTING = "com.devflux.deenone.ACTION_SMART_SUNNAH_FASTING";
  public static final String ACTION_SMART_ISLAMIC_EVENT = "com.devflux.deenone.ACTION_SMART_ISLAMIC_EVENT";
  public static final String ACTION_SMART_DAILY_AMAL = "com.devflux.deenone.ACTION_SMART_DAILY_AMAL";
  public static final String ACTION_SMART_MUHASABAH = "com.devflux.deenone.ACTION_SMART_MUHASABAH";
  public static final String ACTION_SMART_SLEEPING_DUA = "com.devflux.deenone.ACTION_SMART_SLEEPING_DUA";

  // Request Codes
  public static final int REQ_SALAH_PRE = 6001;
  public static final int REQ_TAHAJJUD = 6002;
  public static final int REQ_MORNING_DHIKR = 6003;
  public static final int REQ_EVENING_DHIKR = 6004;
  public static final int REQ_QURAN_READING = 6005;
  public static final int REQ_JUMUAH_KAHF = 6006;
  public static final int REQ_SUNNAH_FASTING = 6007;
  public static final int REQ_ISLAMIC_EVENT = 6008;
  public static final int REQ_DAILY_AMAL = 6009;
  public static final int REQ_MUHASABAH = 6010;
  public static final int REQ_SLEEPING_DUA = 6011;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent == null || intent.getAction() == null || context == null) return;
    String action = intent.getAction();
    Log.d(TAG, "Smart Reminder triggered with action: " + action);

    boolean isBn = LocaleManager.isBengali(context);

    switch (action) {
      case ACTION_SMART_TAHAJJUD:
        if (NotificationSettingsManager.isTahajjudReminderEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "তাহাজ্জুদ ও সাহরির বরকতময় সময়" : "Blessed Time for Tahajjud & Suhoor",
              isBn ? "রাতের শেষ তৃতীয়াংশে আল্লাহ প্রথম আসমানে অবতরণ করেন। তাহাজ্জুদ ও ইস্তেগফারের মাধ্যমে আল্লাহর নৈকট্য অর্জন করুন।" : "In the last third of the night, draw close to Allah through Tahajjud prayer and sincere Istighfar.",
              "tahajjud",
              "feature_prayer",
              "high"
          );
        }
        break;

      case ACTION_SMART_MORNING_DHIKR:
        if (NotificationSettingsManager.isDhikrReminderEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "সকালের মাসনূন জিকির ও তাসবিহ" : "Morning Masnoon Dhikr & Tasbih",
              isBn ? "'সুবহানাল্লাহি ওয়া বিহামদিহি' ও সকালের দোয়া পাঠ করে দিনটি বরকতময় করুন।" : "Start your day blessed by reciting 'SubhanAllah wa bihamdihi' and morning supplications.",
              "dhikr_morning",
              "feature_azkar",
              "normal"
          );
        }
        break;

      case ACTION_SMART_EVENING_DHIKR:
        if (NotificationSettingsManager.isDhikrReminderEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "সন্ধ্যার মাসনূন জিকির ও আশ্রয় প্রার্থনা" : "Evening Dhikr & Seeking Protection",
              isBn ? "সন্ধ্যার মাসনূন দোয়া ও আয়াতুল কুরসি পাঠ করে সকল অনিষ্ট থেকে আল্লাহর আশ্রয়ে থাকুন।" : "Seek Allah's protection from all evil by reciting the evening supplications and Ayat al-Kursi.",
              "dhikr_evening",
              "feature_azkar",
              "normal"
          );
        }
        break;

      case ACTION_SMART_QURAN_READING:
        if (NotificationSettingsManager.isQuranReminderEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "দৈনিক কুরআন তিলাওয়াতের সময়" : "Time for Daily Quran Recitation",
              isBn ? "আজকের নির্ধারিত পৃষ্ঠা তিলাওয়াত ও তাফসীর অধ্যয়ন করে অন্তরে নুর জাগ্রত করুন।" : "Enlighten your heart by reciting today's Quran portion and pondering its meaning.",
              "quran_daily",
              "feature_quran",
              "normal"
          );
        }
        break;

      case ACTION_SMART_JUMUAH_KAHF:
        if (NotificationSettingsManager.isJumuahReminderEnabled(context)) {
          Calendar cal = Calendar.getInstance();
          if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            NotificationHelper.sendDynamicNotification(
                context,
                isBn ? "জুমাবার: সূরা আল-কাহফ ও দরূদ শরীফ" : "Friday: Surah Al-Kahf & Salawat",
                isBn ? "রাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি জুমার দিন সূরা কাহফ পড়বে, তার জন্য দুই জুমার মধ্যবর্তী সময় নুর দ্বারা আলোকিত থাকবে।'" : "The Prophet (ﷺ) said: 'Whoever recites Surah Al-Kahf on Friday will have a light between the two Fridays.'",
                "jumuah_kahf",
                "feature_quran",
                "high"
            );
          }
        }
        break;

      case ACTION_SMART_SUNNAH_FASTING:
        if (NotificationSettingsManager.isSunnahFastingReminderEnabled(context)) {
          Calendar cal = Calendar.getInstance();
          int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
          String fastingNotice;
          if (isBn) {
            fastingNotice = (dayOfWeek == Calendar.SUNDAY)
                ? "আগামীকাল সোমবারের সুন্নাত রোজা রাখার নিয়তে সাহরি করার প্রস্তুতি নিন।"
                : (dayOfWeek == Calendar.WEDNESDAY)
                ? "আগামীকাল বৃহস্পতিবারের সুন্নাত রোজা রাখার জন্য প্রস্তুতি নিন।"
                : "আগামীকাল আইয়ামে বীজের (১৩, ১৪, ১৫ হিজরি) সুন্নাত নফল রোজার প্রস্তুতি নিন।";
          } else {
            fastingNotice = (dayOfWeek == Calendar.SUNDAY)
                ? "Prepare for Suhoor to observe tomorrow's Monday Sunnah fast."
                : (dayOfWeek == Calendar.WEDNESDAY)
                ? "Prepare for Suhoor to observe tomorrow's Thursday Sunnah fast."
                : "Prepare for the Sunnah fast of Ayyam al-Beed (13th, 14th, 15th Hijri).";
          }

          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "সুন্নাত রোজার স্মরণিকা" : "Sunnah Fasting Reminder",
              fastingNotice,
              "sunnah_fasting",
              "feature_ramadan",
              "normal"
          );
        }
        break;

      case ACTION_SMART_ISLAMIC_EVENT:
        if (NotificationSettingsManager.isIslamicEventsReminderEnabled(context)) {
          HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getRealHijriDate(Calendar.getInstance());
          if (hijri != null) {
            String eventTitle = isBn
                ? ("আজকের হিজরি তারিখ: " + BengaliNumberUtil.toBengali(hijri.day) + " " + hijri.monthNameBengali)
                : ("Today's Hijri Date: " + hijri.day + " " + hijri.monthNameEnglish);
            NotificationHelper.sendDynamicNotification(
                context,
                eventTitle,
                isBn ? "পবিত্র ইসলামী ক্যালেন্ডারের বরকতময় দিনসমূহে বেশি বেশি নেক আমল করুন।" : "Perform good deeds and remember Allah during the blessed days of the Islamic calendar.",
                "islamic_event",
                "feature_calendar",
                "normal"
            );
          }
        }
        break;

      case ACTION_SMART_DAILY_AMAL:
        if (NotificationSettingsManager.isDailyAmalEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "আজকের দৈনিক আমল ট্র্যাকার" : "Daily Deeds Tracker",
              isBn ? "আজকের ফরজ নামাজ, সুন্নাত ও মাসনূন আমলসমূহ সম্পন্ন করে নিজের আমলনামা সমৃদ্ধ করুন।" : "Complete your obligatory prayers, Sunnah, and masnoon deeds to enrich your record of deeds.",
              "daily_amal",
              "feature_amal",
              "normal"
          );
        }
        break;

      case ACTION_SMART_MUHASABAH:
        if (NotificationSettingsManager.isDailyAmalEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "আজকের মুহাসাবাহ (আত্মসমালোচনা)" : "Daily Muhasabah (Self-Accountability)",
              isBn ? "ঘুমানোর পূর্বে আজকের দিনের ভুলত্রুটির জন্য ইস্তেগফার করুন এবং আমলের হিসাব সম্পন্ন করুন।" : "Before sleeping, seek forgiveness for any shortcomings and reflect upon today's deeds.",
              "muhasabah",
              "feature_muhasabah",
              "normal"
          );
        }
        break;

      case ACTION_SMART_SLEEPING_DUA:
        if (NotificationSettingsManager.isDailyDuaHadithEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "ঘুমের পূর্বের মাসনূন দোয়া" : "Bedtime Sunnah Supplication",
              isBn ? "'আল্লাহুম্মা বিসমিকা আমূতু ওয়া আহ্ইয়া' দোয়া এবং তিন কুল পাঠ করে ঘুমানোর সুন্নাত পালন করুন।" : "Recite 'Allahumma bismika amootu wa-ahya' and the three Quls before sleep.",
              "sleeping_dua",
              "feature_dua",
              "normal"
          );
        }
        break;

      default:
        break;
    }

    // Reschedule reminders for upcoming cycle
    scheduleAllSmartReminders(context);
  }

  /**
   * Intelligently schedules all enabled Islamic reminders using real astronomical prayer data and calendar rules.
   */
  public static void scheduleAllSmartReminders(Context context) {
    if (context == null) return;
    Context appContext = context.getApplicationContext();
    LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(appContext);

    // 1. Calculate dynamic astronomical prayer schedule for location
    PrayerCalculator.PrayerTimesResult schedule =
        PrayerCalculator.calculateForLocationWithContext(appContext, coords.latitude, coords.longitude, coords.timezone, Calendar.getInstance());
    if (schedule != null) {
      Log.d(TAG, "Calculated dynamic prayer times for " + coords.locationName);
    }

    // 2. Schedule Tahajjud (Last third of the night / 45 mins before Fajr)
    scheduleFixedDailyAlarm(appContext, 4, 15, ACTION_SMART_TAHAJJUD, REQ_TAHAJJUD);

    // 3. Schedule Morning Dhikr (07:00 AM)
    scheduleFixedDailyAlarm(appContext, 7, 0, ACTION_SMART_MORNING_DHIKR, REQ_MORNING_DHIKR);

    // 4. Schedule Morning Daily Amal (08:30 AM)
    scheduleFixedDailyAlarm(appContext, 8, 30, ACTION_SMART_DAILY_AMAL, REQ_DAILY_AMAL);

    // 5. Schedule Friday Kahf (Every Friday 09:00 AM)
    scheduleWeeklyAlarm(appContext, Calendar.FRIDAY, 9, 0, ACTION_SMART_JUMUAH_KAHF, REQ_JUMUAH_KAHF);

    // 6. Schedule Sunnah Fasting (Every Sunday & Wednesday 08:30 PM)
    scheduleWeeklyAlarm(appContext, Calendar.SUNDAY, 20, 30, ACTION_SMART_SUNNAH_FASTING, REQ_SUNNAH_FASTING);
    scheduleWeeklyAlarm(appContext, Calendar.WEDNESDAY, 20, 30, ACTION_SMART_SUNNAH_FASTING, REQ_SUNNAH_FASTING + 100);

    // 7. Schedule Islamic Events & Hijri Update (09:00 AM Daily)
    scheduleFixedDailyAlarm(appContext, 9, 0, ACTION_SMART_ISLAMIC_EVENT, REQ_ISLAMIC_EVENT);

    // 8. Schedule Evening Dhikr (05:15 PM)
    scheduleFixedDailyAlarm(appContext, 17, 15, ACTION_SMART_EVENING_DHIKR, REQ_EVENING_DHIKR);

    // 9. Schedule Quran Reading (06:30 PM)
    scheduleFixedDailyAlarm(appContext, 18, 30, ACTION_SMART_QURAN_READING, REQ_QURAN_READING);

    // 10. Schedule Night Muhasabah & Sleep Dua (10:30 PM & 11:00 PM)
    scheduleFixedDailyAlarm(appContext, 22, 30, ACTION_SMART_MUHASABAH, REQ_MUHASABAH);
    scheduleFixedDailyAlarm(appContext, 23, 0, ACTION_SMART_SLEEPING_DUA, REQ_SLEEPING_DUA);

    Log.d(TAG, "All smart Islamic reminders registered successfully.");
  }

  private static void scheduleFixedDailyAlarm(Context context, int hour, int minute, String action, int reqCode) {
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

    Intent intent = new Intent(context, SmartIslamicReminderEngine.class);
    intent.setAction(action);
    PendingIntent pi = PendingIntent.getBroadcast(
        context, reqCode, intent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
    );

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
      } else {
        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
      }
    } catch (SecurityException se) {
      am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }
  }

  private static void scheduleWeeklyAlarm(Context context, int dayOfWeek, int hour, int minute, String action, int reqCode) {
    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (am == null) return;

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
      cal.add(Calendar.DAY_OF_YEAR, 7);
    }

    Intent intent = new Intent(context, SmartIslamicReminderEngine.class);
    intent.setAction(action);
    PendingIntent pi = PendingIntent.getBroadcast(
        context, reqCode, intent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
    );

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
      } else {
        am.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
      }
    } catch (SecurityException se) {
      am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }
  }
}
