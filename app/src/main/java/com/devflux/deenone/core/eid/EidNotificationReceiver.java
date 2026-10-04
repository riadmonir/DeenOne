package com.devflux.deenone.core.eid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.data.repository.NotificationRepository;

public class EidNotificationReceiver extends BroadcastReceiver {

  private static final String TAG = "EidNotifReceiver";

  public static final String ACTION_EID_REMINDER = "com.devflux.deenone.ACTION_EID_REMINDER";
  public static final String EXTRA_TITLE = "extra_eid_title";
  public static final String EXTRA_MESSAGE = "extra_eid_message";

  public static final String CHANNEL_ID = "deanone_eid_reminders";
  public static final String CHANNEL_NAME = "পবিত্র ঈদ ও তাকবীর রিমাইন্ডার";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (context == null || intent == null) return;

    String action = intent.getAction();
    if (action == null) return;

    // Handle Boot / Timezone / Time change: Re-register alarms without alerting
    if (Intent.ACTION_BOOT_COMPLETED.equals(action)
        || "android.intent.action.TIME_SET".equals(action)
        || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
      Log.d(TAG, "Device booted or time changed. Rescheduling Eid reminders.");
      EidNotificationScheduler.scheduleEidReminders(context);
      return;
    }

    if (!ACTION_EID_REMINDER.equals(action)) return;

    SharedPreferences prefs = context.getSharedPreferences("deanone_eid_mode_prefs", Context.MODE_PRIVATE);
    boolean enabled = prefs.getBoolean("key_eid_notif_enabled", true);
    if (!enabled) return;

    // Strict Eid Day Verification: Prevent spurious triggers on normal days
    if (!EidModeManager.getInstance().isEidActiveToday(context)) {
      Log.w(TAG, "Suppressed Eid notification because today is not an authentic Islamic Eid day or preview mode is off.");
      return;
    }

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    String title = intent.getStringExtra(EXTRA_TITLE);
    String message = intent.getStringExtra(EXTRA_MESSAGE);
    if (title == null || title.isEmpty()) {
      title = isBn ? "ঈদ মুবারক! তাকাব্বালাল্লাহু মিন্না ওয়া মিনকুম" : "Eid Mubarak! Taqabbalallahu Minna wa Minkum";
    }
    if (message == null || message.isEmpty()) {
      message = isBn 
          ? "ঈদের সকালে গোসল, সুগন্ধি, তাকবীর পাঠ ও ঈদের নামাজের প্রস্তুতি নিন।" 
          : "Prepare for Eid with Ghusl, perfume, Takbeer recitation, and Eid Prayer.";
    }

    showEidNotification(context, title, message, isBn);

    // Persist to In-App Notification History Database
    try {
      NotificationRepository repo = new NotificationRepository(context);
      repo.saveNotification(new NotificationMessageEntity(
          title, message, "eid", System.currentTimeMillis(),
          false, "eid_mode", "high", "system"
      ));
    } catch (Exception e) {
      Log.w(TAG, "Error persisting Eid notification history: " + e.getMessage());
    }
  }

  private void showEidNotification(Context context, String title, String message, boolean isBn) {
    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (nm == null) return;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          CHANNEL_ID,
          isBn ? "পবিত্র ঈদ ও তাকবীর রিমাইন্ডার" : "Holy Eid & Takbeer Reminders",
          NotificationManager.IMPORTANCE_HIGH
      );
      channel.setDescription(isBn ? "ঈদুল ফিতর, ঈদুল আজহা ও তাকবীরে তাশরীক রিমাইন্ডার" : "Eid-ul-Fitr, Eid-ul-Adha and Takbeer-e-Tashreeq reminders");
      channel.enableLights(true);
      channel.setLightColor(Color.parseColor("#34D399"));
      channel.enableVibration(true);
      channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      nm.createNotificationChannel(channel);
    }

    Intent openIntent = new Intent(context, MainActivity.class);
    openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    openIntent.putExtra("extra_deep_link", "eid_mode");
    openIntent.putExtra("navigate_to", "eid_mode");
    openIntent.putExtra("extra_notification_title", title);
    openIntent.putExtra("extra_notification_body", message);
    openIntent.putExtra("extra_notification_type", "eid");

    PendingIntent pi = PendingIntent.getActivity(
        context,
        (int) System.currentTimeMillis(),
        openIntent,
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            : PendingIntent.FLAG_UPDATE_CURRENT
    );

    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_notification_deenone)
        .setContentTitle(title)
        .setContentText(message)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
        .setAutoCancel(true)
        .setSound(defaultSoundUri)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentIntent(pi);

    try {
      builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
    } catch (Exception ignored) {}

    nm.notify(8800 + (int) (System.currentTimeMillis() % 100), builder.build());
  }
}