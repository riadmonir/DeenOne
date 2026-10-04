package com.devflux.deenone.core.jummah;

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

import java.util.Calendar;

public class JummahNotificationReceiver extends BroadcastReceiver {

  private static final String TAG = "JummahNotifReceiver";

  public static final String ACTION_JUMMAH_REMINDER = "com.devflux.deenone.ACTION_JUMMAH_REMINDER";
  public static final String EXTRA_PHASE_KEY = "extra_phase_key";
  public static final String EXTRA_TITLE = "extra_title";
  public static final String EXTRA_MESSAGE = "extra_message";

  public static final String CHANNEL_ID = "deanone_jummah_reminders";
  public static final String CHANNEL_NAME = "পবিত্র জুমু'আ রিমাইন্ডার";

  @Override
  public void onReceive(Context context, Intent intent) {
    if (context == null || intent == null) return;

    String action = intent.getAction();
    if (action == null) return;

    // Handle Boot / Timezone / Time change: Re-register alarms without alerting
    if (Intent.ACTION_BOOT_COMPLETED.equals(action)
        || "android.intent.action.TIME_SET".equals(action)
        || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
      Log.d(TAG, "Device booted or time changed. Rescheduling Jummah reminders.");
      JummahNotificationScheduler.scheduleAllJummahReminders(context);
      return;
    }

    if (!ACTION_JUMMAH_REMINDER.equals(action)) return;

    SharedPreferences prefs = context.getSharedPreferences("deanone_jummah_mode_prefs", Context.MODE_PRIVATE);
    boolean enabled = prefs.getBoolean("key_jummah_notif_enabled", true);
    if (!enabled) return;

    int phaseCode = intent.getIntExtra(EXTRA_PHASE_KEY, 102);
    Calendar cal = Calendar.getInstance();
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

    // Strict Day Validation: Prevent firing on incorrect days
    if (phaseCode == 101) {
      // 101 = Thursday night 8:00 PM
      if (dayOfWeek != Calendar.THURSDAY) {
        Log.w(TAG, "Suppressed Jummah Thursday night reminder because today is not Thursday: " + dayOfWeek);
        return;
      }
    } else {
      // 102 (Fri 8:00 AM), 103 (Fri 11:30 AM), 104 (Fri 4:30 PM) -> MUST BE FRIDAY
      if (dayOfWeek != Calendar.FRIDAY) {
        Log.w(TAG, "Suppressed Jummah reminder because today is not Friday: " + dayOfWeek);
        return;
      }
    }

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    String title;
    String message;

    switch (phaseCode) {
      case 101:
        title = isBn ? "জুমার রাত: দরূদ শরীফ পাঠের আহ্বান" : "Friday Eve: Call to Recite Salawat";
        message = isBn 
            ? "রাসূলুল্লাহ (ﷺ) বলেছেন: জুমার রাত ও দিনে আমার ওপর অধিক পরিমাণে দরূদ পাঠ করো। (বায়হাকী)"
            : "The Prophet (ﷺ) said: 'Increase your supplications for me on Friday eve and day.' (Bayhaqi)";
        break;
      case 102:
        title = isBn ? "জুমার সকাল: সূরা আল-কাহাফ ও গোসল প্রস্তুতি" : "Friday Morning: Surah Al-Kahf & Ghusl";
        message = isBn 
            ? "আজ জুমার দিন: সূরা আল-কাহাফ তিলাওয়াত করুন এবং জুমার গোসল ও উত্তম পরিচ্ছন্নতার প্রস্তুতি নিন।"
            : "Today is Friday: Recite Surah Al-Kahf and prepare with Ghusl and clean clothes for Jummah.";
        break;
      case 103:
        title = isBn ? "মসজিদে দ্রুত গমন ও খুতবার প্রস্তুতি" : "Early Departure to Mosque & Khutbah";
        message = isBn 
            ? "আগে আগে মসজিদে রওনা দিন, তাহিয়্যাতুল মসজিদ আদায় করুন এবং নিরব থেকে মনোযোগ সহকারে খুতবা শুনুন।"
            : "Proceed early to the mosque, offer Tahiyyatul Masjid, and listen attentively to the Khutbah.";
        break;
      case 104:
        title = isBn ? "সা'আতুল ইজাবাহ: দোয়া কবুলের বিশেষ মুহূর্ত" : "Sa'at al-Ijabah: The Hour of Acceptance";
        message = isBn 
            ? "আসরের শেষ প্রহরে দোয়া কবুলের বিশেষ সময়। আপনার জীবনের সকল প্রয়োজন আল্লাহর দরবারে পেশ করুন।"
            : "The final hour after Asr is a special time for accepted prayers. Present your supplications to Allah.";
        break;
      default:
        title = isBn ? "পবিত্র জুমু'আ রিমাইন্ডার" : "Holy Jummah Reminder";
        message = isBn ? "আজ জুমার দিন: সূরা কাহাফ ও অধিক দরূদ পাঠের প্রস্তুতি নিন।" : "Today is Friday: Recite Surah Al-Kahf and send abundant Salawat upon the Prophet (ﷺ).";
        break;
    }

    showJummahNotification(context, title, message, isBn);

    // Persist to In-App Notification History Database
    try {
      NotificationRepository repo = new NotificationRepository(context);
      repo.saveNotification(new NotificationMessageEntity(
          title, message, "jumuah_kahf", System.currentTimeMillis(),
          false, "jummah_mode", "high", "system"
      ));
    } catch (Exception e) {
      Log.w(TAG, "Error persisting Jummah notification history: " + e.getMessage());
    }
  }

  private void showJummahNotification(Context context, String title, String message, boolean isBn) {
    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (nm == null) return;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          CHANNEL_ID,
          isBn ? "পবিত্র জুমু'আ রিমাইন্ডার" : "Holy Jummah Reminders",
          NotificationManager.IMPORTANCE_HIGH
      );
      channel.setDescription(isBn ? "জুমার দিনের বিশেষ আমল, সূরা কাহাফ ও সা'আতুল ইজাবাহ রিমাইন্ডার" : "Special Friday deeds, Surah Al-Kahf and Sa'at al-Ijabah reminders");
      channel.enableLights(true);
      channel.setLightColor(Color.parseColor("#34D399"));
      channel.enableVibration(true);
      channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      nm.createNotificationChannel(channel);
    }

    Intent openIntent = new Intent(context, MainActivity.class);
    openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    openIntent.putExtra("extra_deep_link", "jummah_mode");
    openIntent.putExtra("navigate_to", "jummah_mode");
    openIntent.putExtra("extra_notification_title", title);
    openIntent.putExtra("extra_notification_body", message);
    openIntent.putExtra("extra_notification_type", "jumuah_kahf");

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

    nm.notify(7700 + (int) (System.currentTimeMillis() % 100), builder.build());
  }
}