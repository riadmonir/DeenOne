package com.devflux.deenone.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.alarms.PrayerAlarmSettingsManager;
import com.devflux.deenone.core.notifications.IslamicVibrationHelper;
import com.devflux.deenone.core.notifications.NotificationHelper;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.data.repository.NotificationRepository;

public class AdhanAlarmReceiver extends BroadcastReceiver {

  public static final String EXTRA_PRAYER_KEY = "extra_prayer_key";
  public static final String EXTRA_PRAYER_NAME = "extra_prayer_name";
  public static final String EXTRA_IS_PRE_REMINDER = "extra_is_pre_reminder";

  public static final String ACTION_STOP_ADHAN = "com.devflux.deenone.ACTION_STOP_ADHAN";
  public static final String ACTION_DISMISS = "com.devflux.deenone.ACTION_DISMISS";
  public static final String ACTION_DISABLE_NOTIF = "com.devflux.deenone.ACTION_DISABLE_NOTIF";
  public static final String ACTION_OPEN_PRAYER = "com.devflux.deenone.ACTION_OPEN_PRAYER";

  public static final String CHANNEL_ID_ADHAN = NotificationHelper.CHANNEL_ID_ADHAN;
  public static final String CHANNEL_ID_REMINDER = NotificationHelper.CHANNEL_ID_REMINDER;

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent == null || context == null) return;

    String action = intent.getAction();
    String prayerKey = intent.getStringExtra(EXTRA_PRAYER_KEY);
    if (prayerKey == null) prayerKey = "fajr";
    int notifId = getNotificationIdForWaqt(prayerKey);

    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // Handle Action: Stop Adhan
    if (ACTION_STOP_ADHAN.equals(action)) {
      AudioPlayerService.getInstance(context).stopAudio();
      IslamicVibrationHelper.cancelVibration(context);
      if (notificationManager != null) {
        notificationManager.cancel(notifId);
      }
      Toast.makeText(context, isBn ? "আযান বন্ধ করা হয়েছে" : "Adhan stopped", Toast.LENGTH_SHORT).show();
      return;
    }

    // Handle Action: Dismiss
    if (ACTION_DISMISS.equals(action)) {
      AudioPlayerService.getInstance(context).stopAudio();
      IslamicVibrationHelper.cancelVibration(context);
      if (notificationManager != null) {
        notificationManager.cancel(notifId);
      }
      return;
    }

    // Handle Action: Turn Notification Off for this prayer
    if (ACTION_DISABLE_NOTIF.equals(action)) {
      AudioPlayerService.getInstance(context).stopAudio();
      IslamicVibrationHelper.cancelVibration(context);
      PrayerAlarmSettingsManager.PrayerAlarmConfig config =
          PrayerAlarmSettingsManager.getConfig(context, prayerKey);
      config.isEnabled = false;
      config.isNotificationEnabled = false;
      config.isAzanEnabled = false;
      PrayerAlarmSettingsManager.saveConfig(context, config);

      if (notificationManager != null) {
        notificationManager.cancel(notifId);
      }
      Toast.makeText(context, isBn ? "এই ওয়াক্তের স্মরণিকা বন্ধ করা হয়েছে" : "Reminder disabled for this prayer", Toast.LENGTH_SHORT).show();
      return;
    }

    // Handle Action: Open Prayer
    if (ACTION_OPEN_PRAYER.equals(action)) {
      AudioPlayerService.getInstance(context).stopAudio();
      IslamicVibrationHelper.cancelVibration(context);
      if (notificationManager != null) {
        notificationManager.cancel(notifId);
      }
      Intent appIntent = new Intent(context, MainActivity.class);
      appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
      context.startActivity(appIntent);
      return;
    }

    // Triggering Alarm/Adhan
    String localizedPrayerName = com.devflux.deenone.utils.PrayerCalculator.getWaqtName(prayerKey, isBn);
    boolean isPreReminder = intent.getBooleanExtra(EXTRA_IS_PRE_REMINDER, false);

    PrayerAlarmSettingsManager.PrayerAlarmConfig config =
        PrayerAlarmSettingsManager.getConfig(context, prayerKey);

    // If waqt reminder is disabled by user, skip
    if (!config.isEnabled) return;

    if (notificationManager == null) return;
    NotificationHelper.createNotificationChannels(context);

    boolean isJummah = "jummah".equalsIgnoreCase(prayerKey);
    String appName = "DeenOne";
    String timeStr = isBn ? "এইমাত্র" : "Just now";

    // Open App Direct Activity Intent
    Intent appIntent = new Intent(context, MainActivity.class);
    appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    appIntent.putExtra("extra_deep_link", "feature_prayer");
    appIntent.putExtra("extra_notification_title", isBn ? (localizedPrayerName + " এর ওয়াক্ত") : (localizedPrayerName + " Prayer"));
    appIntent.putExtra("extra_notification_body", isPreReminder 
        ? (isBn ? (localizedPrayerName + " এর ওয়াক্ত শুরু হতে " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(config.prePrayerReminderMinutes) + " মিনিট বাকি।") : (localizedPrayerName + " starts in " + config.prePrayerReminderMinutes + " minutes."))
        : (isBn ? "সালাত কায়েম করুন, জামাতের সাথে নামাজ আদায় করুন।" : "Establish prayer with the congregation."));
    appIntent.putExtra("extra_notification_type", "adhan");
    PendingIntent openPendingIntent = PendingIntent.getActivity(
        context, notifId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    if (isPreReminder) {
      // Pre-prayer reminder trigger (5/10/15/20/30 min before)
      int preNotifId = notifId + 1000;
      String preTitle = isBn ? (localizedPrayerName + " এর ওয়াক্ত আসন্ন") : (localizedPrayerName + " Prayer Approaching");
      String preBody = isBn 
          ? (localizedPrayerName + " এর ওয়াক্ত শুরু হতে " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(config.prePrayerReminderMinutes) + " মিনিট বাকি। অজু ও প্রস্তুতি নিন।")
          : (localizedPrayerName + " prayer starts in " + config.prePrayerReminderMinutes + " minutes. Prepare for Wudu and prayer.");

      NotificationCompat.Builder reminderBuilder = new NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
          .setSmallIcon(R.drawable.ic_notification_deenone)
          .setContentTitle(preTitle)
          .setContentText(preBody)
          .setStyle(new NotificationCompat.BigTextStyle().bigText(preBody))
          .setColor(ContextCompat.getColor(context, R.color.accent_mint))
          .setAutoCancel(true)
          .setPriority(NotificationCompat.PRIORITY_HIGH)
          .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
          .setContentIntent(openPendingIntent)
          .addAction(R.drawable.ic_mosque, isBn ? "নামাজের সময়সূচী" : "Prayer Times", openPendingIntent);

      try {
        reminderBuilder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
      } catch (Exception ignored) {}

      notificationManager.notify(preNotifId, reminderBuilder.build());

      if (config.isVibrationEnabled && !PrayerAlarmSettingsManager.ALERT_MODE_SILENT.equals(config.alertMode)) {
        IslamicVibrationHelper.triggerVibration(context, IslamicVibrationHelper.PatternType.DOUBLE_PULSE, false);
      }
      return;
    }

    // Main Waqt Trigger
    // Action: Stop Adhan
    Intent stopIntent = new Intent(context, AdhanAlarmReceiver.class);
    stopIntent.setAction(ACTION_STOP_ADHAN);
    stopIntent.putExtra(EXTRA_PRAYER_KEY, prayerKey);
    PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
        context, notifId + 10, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    String displayTitle = isJummah 
        ? (isBn ? "পবিত্র জুমু'আর আযান ও নামাজের সময় হয়েছে" : "Time for Holy Jummah Prayer")
        : (isBn ? (localizedPrayerName + " এর নামাজের সময় হয়েছে") : ("Time for " + localizedPrayerName + " Prayer"));
    String displayBody = isJummah 
        ? (isBn ? "আজ পবিত্র জুমু'আ। দ্রুত মসজিদে গমন করুন এবং জামায়াতে নামাজ আদায় করুন।" : "Today is Holy Jummah. Please proceed to the mosque and join the congregation.")
        : (isBn ? "নামাজের সময় হয়েছে, জামায়াতে নামাজ আদায় করুন।" : "It is time for prayer, join the congregation at the mosque.");

    // Determine if audio will play via AudioPlayerService
    boolean isSoundEnabled = config.isSoundEnabled && !PrayerAlarmSettingsManager.SOUND_SILENT.equalsIgnoreCase(config.soundType);
    String soundUrl = isSoundEnabled ? PrayerAlarmSettingsManager.getAzanAudioUrl(context, prayerKey, config.soundType) : null;
    boolean willPlayAudio = isSoundEnabled && soundUrl != null && !soundUrl.isEmpty();

    // If audio is NOT playing (silent or vibration-only mode), post single native NotificationCompat with official logo
    if (!willPlayAudio) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_ADHAN)
          .setSmallIcon(R.drawable.ic_notification_deenone)
          .setContentTitle(displayTitle)
          .setContentText(displayBody)
          .setStyle(new NotificationCompat.BigTextStyle().bigText(displayBody))
          .setColor(ContextCompat.getColor(context, R.color.accent_mint))
          .setAutoCancel(true)
          .setPriority(NotificationCompat.PRIORITY_MAX)
          .setCategory(NotificationCompat.CATEGORY_ALARM)
          .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
          .setContentIntent(openPendingIntent)
          .setDeleteIntent(stopPendingIntent)
          .setFullScreenIntent(openPendingIntent, false)
          .addAction(R.drawable.ic_mosque, isBn ? "নামাজের সময়সূচী" : "Prayer Times", openPendingIntent)
          .addAction(R.drawable.ic_stop, isBn ? "বন্ধ করুন" : "Dismiss", stopPendingIntent);

      try {
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
      } catch (Exception ignored) {}

      notificationManager.notify(notifId, builder.build());
    }

    // Save to Notification History Database
    try {
      NotificationRepository repo = new NotificationRepository(context);
      repo.saveNotification(new NotificationMessageEntity(
          isBn ? (localizedPrayerName + " এর ওয়াক্ত শুরু হয়েছে") : (localizedPrayerName + " prayer time started"),
          isBn ? "সালাত কায়েম করুন, জামাতের সাথে নামাজ আদায় করুন।" : "Establish prayer with the congregation.",
          "adhan",
          System.currentTimeMillis(),
          false,
          "feature_salah",
          "high",
          "scheduled"
      ));
    } catch (Exception ignored) {}

    // Live update Lock Screen & Status Bar Prayer Widget
    try {
      com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(context);
    } catch (Exception ignored) {}

    android.os.PowerManager.WakeLock receiverWakeLock = null;
    try {
      android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService(Context.POWER_SERVICE);
      if (pm != null) {
        receiverWakeLock = pm.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, "DeenOne:AdhanReceiverWakeLock");
        receiverWakeLock.acquire(15000L); // 15 seconds safety timeout
      }
    } catch (Exception ignored) {}

    try {
      // Vibration Trigger
      if (config.isVibrationEnabled && !PrayerAlarmSettingsManager.SOUND_SILENT.equalsIgnoreCase(config.soundType)) {
        IslamicVibrationHelper.triggerVibration(
            context, IslamicVibrationHelper.PatternType.STRONG_ALARM, true
        );
      }

      // Single Notification Audio Playback via Foreground AudioPlayerService
      if (willPlayAudio) {
        AudioPlayerService.startAdhan(context, soundUrl, localizedPrayerName, prayerKey, notifId);
      }
    } finally {
      if (receiverWakeLock != null && receiverWakeLock.isHeld()) {
        try {
          receiverWakeLock.release();
        } catch (Exception ignored) {}
      }
    }
  }

  private int getNotificationIdForWaqt(String prayerKey) {
    return switch (prayerKey.toLowerCase().trim()) {
      case "fajr" -> 1001;
      case "sunrise" -> 1002;
      case "dhuhr" -> 1003;
      case "asr" -> 1004;
      case "maghrib" -> 1005;
      case "isha" -> 1006;
      case "tahajjud" -> 1007;
      case "ishraq" -> 1008;
      case "chasht" -> 1009;
      case "awwabin" -> 1010;
      case "jummah" -> 1011;
      default -> 1000;
    };
  }
}
