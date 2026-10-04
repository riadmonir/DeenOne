package com.devflux.deenone.core.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class DailyIslamicReminderScheduler extends BroadcastReceiver {

  private static final String TAG = "IslamicReminderSched";

  public static final String ACTION_DAILY_AMAL_REMINDER = "com.devflux.deenone.ACTION_DAILY_AMAL_REMINDER";
  public static final String ACTION_DAILY_HADITH_REMINDER = "com.devflux.deenone.ACTION_DAILY_HADITH_REMINDER";
  public static final String ACTION_DAILY_DUA_REMINDER = "com.devflux.deenone.ACTION_DAILY_DUA_REMINDER";
  public static final String ACTION_DAILY_QUIZ_REMINDER = "com.devflux.deenone.ACTION_DAILY_QUIZ_REMINDER";
  public static final String ACTION_DAILY_MIDNIGHT_RECAP = "com.devflux.deenone.ACTION_DAILY_MIDNIGHT_RECAP";

  public static final int REQ_AMAL = 5001;
  public static final int REQ_HADITH = 5002;
  public static final int REQ_DUA = 5003;
  public static final int REQ_QUIZ = 5004;
  public static final int REQ_MIDNIGHT_RECAP = 5005;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent == null || intent.getAction() == null) return;
    String action = intent.getAction();

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    switch (action) {
      case ACTION_DAILY_AMAL_REMINDER:
        if (NotificationSettingsManager.isDailyAmalEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "আজকের দৈনিক আমল ও লক্ষ্যমাত্রা" : "Daily Deeds & Goals",
              isBn ? "আজকের আমলসমূহ সম্পন্ন করে আল্লাহর নৈকট্য অর্জন করুন এবং নেকি পয়েন্ট বাড়ান।" : "Complete today's deeds to gain closeness to Allah and increase your reward points.",
              "daily_amal",
              "feature_amal",
              "normal"
          );
        }
        scheduleDailyAlarm(context, 8, 30, ACTION_DAILY_AMAL_REMINDER, REQ_AMAL);
        break;

      case ACTION_DAILY_HADITH_REMINDER:
        if (NotificationSettingsManager.isDailyDuaHadithEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "আজকের নির্বাচিত সহীহ হাদিস" : "Today's Sahih Hadith",
              isBn ? "রাসূলুল্লাহ (ﷺ) বলেছেন: 'উত্তম চরিত্রই হলো সকল পুণ্য ও নেক কাজের মূল।' (সহীহ মুসলিম)" : "The Prophet (ﷺ) said: 'Righteousness is good character.' (Sahih Muslim)",
              "daily_hadith",
              "feature_hadith",
              "normal"
          );
        }
        scheduleDailyAlarm(context, 13, 0, ACTION_DAILY_HADITH_REMINDER, REQ_HADITH);
        break;

      case ACTION_DAILY_DUA_REMINDER:
        if (NotificationSettingsManager.isDailyDuaHadithEnabled(context)) {
          NotificationHelper.sendDynamicNotification(
              context,
              isBn ? "সন্ধ্যার মাসনূন দোয়া ও জিকির" : "Evening Masnoon Dua & Dhikr",
              isBn ? "আল্লাহর দরবারে সন্ধ্যার মাসনূন দোয়া ও ইস্তেগফার পাঠ করে দ্বীনি প্রশান্তি লাভ করুন।" : "Recite the evening supplications and seek forgiveness for peace of heart.",
              "daily_dua",
              "feature_dua",
              "normal"
          );
        }
        scheduleDailyAlarm(context, 17, 30, ACTION_DAILY_DUA_REMINDER, REQ_DUA);
        break;

      case ACTION_DAILY_QUIZ_REMINDER:
        if (NotificationSettingsManager.isDailyQuizEnabled(context)) {
          boolean alreadyPlayedToday = com.devflux.deenone.core.quiz.QuizManager.getInstance().hasPlayedQuizToday(context);
          if (!alreadyPlayedToday) {
            NotificationHelper.sendDynamicNotification(
                context,
                isBn ? "আজকের ইসলামিক কুইজ চ্যালেঞ্জ" : "Today's Islamic Quiz Challenge",
                isBn ? "কুরআন, হাদিস ও সীরাহ বিষয়ক নতুন কুইজে অংশগ্রহণ করে নিজের জ্ঞান পরীক্ষা করুন।" : "Participate in today's quiz on Quran, Hadith, and Seerah to test your knowledge.",
                "quiz",
                "feature_quiz",
                "normal"
            );
          } else {
            Log.d(TAG, "User has already played today's quiz. Skipping quiz reminder notification.");
          }
        }
        scheduleDailyAlarm(context, 20, 30, ACTION_DAILY_QUIZ_REMINDER, REQ_QUIZ);
        break;

      case ACTION_DAILY_MIDNIGHT_RECAP:
        // Midnight 12:00 AM Daily Progress & Points Recap Notification
        com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
          try {
            com.devflux.deenone.data.local.AppDatabase db = com.devflux.deenone.data.local.AppDatabase.getInstance(context);
            String todayDate = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(new java.util.Date());

            int prayedToday = db.prayerLogDao().getPrayedCountForDateSync(todayDate);
            int amalsCompleted = db.dailyAmalDao().getCountByDate(todayDate);
            int quizPlayed = db.quizDao().getTotalQuizzesPlayedCountSync();
            if (quizPlayed == 0) {
              quizPlayed = com.devflux.deenone.core.quiz.QuizManager.getInstance().getDailyQuizzesPlayedCount(context);
            }

            int totalPoints = com.devflux.deenone.core.gamification.GamificationManager.getTotalXP(context);
            int totalPossibleItems = 11; // 5 prayers + 5 amals + 1 quiz
            int completedItems = prayedToday + amalsCompleted + (quizPlayed > 0 ? 1 : 0);
            int percentage = (int) Math.min(100.0, Math.round(((double) completedItems / totalPossibleItems) * 100.0));

            String title = isBn ? "আজকের আমল ও পয়েন্ট সারাংশ" : "Daily Deeds & Points Summary";
            String body = isBn 
                ? ("মাশাআল্লাহ! আজ আপনি মোট " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(totalPoints) + " পয়েন্ট অর্জন করেছেন এবং আপনার দৈনিক অগ্রগতি " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(percentage) + "% সম্পন্ন হয়েছে।")
                : ("MashaAllah! Today you earned " + totalPoints + " points and completed " + percentage + "% of your daily goals.");

            NotificationHelper.sendDynamicNotification(
                context,
                title,
                body,
                "daily_recap",
                "feature_profile",
                "high"
            );
          } catch (Exception e) {
            Log.e(TAG, "Error in midnight recap: "+ e.getMessage());
          }
        });
        scheduleDailyAlarm(context, 0, 0, ACTION_DAILY_MIDNIGHT_RECAP, REQ_MIDNIGHT_RECAP);
        break;

      default:
        break;
    }
  }

  public static void scheduleAllDailyReminders(Context context) {
    if (context == null) return;
    Context appContext = context.getApplicationContext();

    scheduleDailyAlarm(appContext, 8, 30, ACTION_DAILY_AMAL_REMINDER, REQ_AMAL);
    scheduleDailyAlarm(appContext, 13, 0, ACTION_DAILY_HADITH_REMINDER, REQ_HADITH);
    scheduleDailyAlarm(appContext, 17, 30, ACTION_DAILY_DUA_REMINDER, REQ_DUA);
    scheduleDailyAlarm(appContext, 20, 30, ACTION_DAILY_QUIZ_REMINDER, REQ_QUIZ);
    scheduleDailyAlarm(appContext, 0, 0, ACTION_DAILY_MIDNIGHT_RECAP, REQ_MIDNIGHT_RECAP);

    Log.d(TAG, "All daily Islamic reminders scheduled successfully.");
  }

  private static void scheduleDailyAlarm(Context context, int hour, int minute, String action, int requestCode) {
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    if (alarmManager == null) return;

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    if (cal.getTimeInMillis() <= System.currentTimeMillis()) {
      cal.add(Calendar.DAY_OF_MONTH, 1);
    }

    Intent intent = new Intent(context, DailyIslamicReminderScheduler.class);
    intent.setAction(action);

    PendingIntent pendingIntent = PendingIntent.getBroadcast(
        context, requestCode, intent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
          alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        } else {
          alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
      } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
      }
    } catch (Exception e) {
      Log.w(TAG, "Error scheduling reminder "+ action + ": "+ e.getMessage());
    }
  }
}
