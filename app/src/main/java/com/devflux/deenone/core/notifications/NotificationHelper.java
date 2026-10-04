package com.devflux.deenone.core.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.quiz.QuizManager;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.data.repository.NotificationRepository;
import com.devflux.deenone.service.AdhanAlarmReceiver;

public class NotificationHelper {

  private static final String TAG = "NotificationHelper";
  private static String lastDispatchedSignature = "";
  private static long lastDispatchedTimestamp = 0;

  // 8 Specialized Channels for DeenOne (High Alert Priority - Never Muted)
  public static final String CHANNEL_ID_ADHAN = "deanone_adhan_channel_v6";
  public static final String CHANNEL_ID_PRAYER = "deanone_prayer_channel_v6";
  public static final String CHANNEL_ID_REMINDER = "deanone_reminder_channel_v6";
  public static final String CHANNEL_ID_RAMADAN = "deanone_ramadan_channel_v5";
  public static final String CHANNEL_ID_DAILY = "deanone_daily_content_channel_v5";
  public static final String CHANNEL_ID_QUIZ = "deanone_quiz_channel_v5";
  public static final String CHANNEL_ID_ADMIN = "deanone_admin_channel_v5";
  public static final String CHANNEL_ID_CRITICAL = "deanone_critical_channel_v5";
  public static final String CHANNEL_ID_DOWNLOAD = "deanone_download_channel_v1";
  public static final String CHANNEL_ID_DURUD_PREFIX = "deanone_durud_channel_v6_";
  public static final String CHANNEL_ID_ISTIGFAR_PREFIX = "deanone_istigfar_channel_v6_";

  public static String getDurudChannelId(Context context) {
    if (context == null) return CHANNEL_ID_DURUD_PREFIX + "ar1";
    String key = NotificationSettingsManager.getDurudAudioKey(context);
    if (key == null || key.trim().isEmpty()) key = "ar1";
    return CHANNEL_ID_DURUD_PREFIX + key.trim().toLowerCase();
  }

  public static Uri getDurudSoundUri(Context context, String audioKey) {
    if (context == null) return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    if (audioKey == null || audioKey.trim().isEmpty()) audioKey = "ar1";
    int soundResId = R.raw.read_durud_ar1;
    if ("ar2".equalsIgnoreCase(audioKey)) soundResId = R.raw.read_durud_ar2;
    else if ("ar3".equalsIgnoreCase(audioKey)) soundResId = R.raw.read_durud_ar3;
    else if ("bn".equalsIgnoreCase(audioKey)) soundResId = R.raw.read_durud_bn;
    else if ("en".equalsIgnoreCase(audioKey)) soundResId = R.raw.read_durud_en;

    try {
      return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + soundResId);
    } catch (Exception e) {
      return getDefaultNotificationSoundUri(context);
    }
  }

  public static String getIstigfarChannelId(Context context) {
    if (context == null) return CHANNEL_ID_ISTIGFAR_PREFIX + "ar";
    String lang = NotificationSettingsManager.getIstigfarAudioLanguage(context);
    if (lang == null || lang.trim().isEmpty()) lang = "ar";
    return CHANNEL_ID_ISTIGFAR_PREFIX + lang.trim().toLowerCase();
  }

  public static Uri getIstigfarSoundUri(Context context, String lang) {
    if (context == null) return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    if (lang == null || lang.trim().isEmpty()) lang = "ar";
    int soundResId = R.raw.read_istigfar_ar;
    if ("bn".equalsIgnoreCase(lang)) soundResId = R.raw.read_istigfar_bn;
    else if ("en".equalsIgnoreCase(lang)) soundResId = R.raw.read_istigfar_en;

    try {
      return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + soundResId);
    } catch (Exception e) {
      return getDefaultNotificationSoundUri(context);
    }
  }

  public static Uri getDefaultNotificationSoundUri(Context context) {
    if (context == null) return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    try {
      String soundName = NotificationSettingsManager.getCustomNotificationSoundName(context);
      if ("SILENT".equalsIgnoreCase(soundName) || "NONE".equalsIgnoreCase(soundName)) {
        return null;
      }
      String customUri = NotificationSettingsManager.getCustomNotificationSoundUri(context);
      if (customUri != null && !customUri.trim().isEmpty() && !"SILENT".equalsIgnoreCase(customUri)) {
        return Uri.parse(customUri.trim());
      }
    } catch (Exception ignored) {}
    try {
      return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.universfield_new_notification);
    } catch (Exception e) {
      return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }
  }

  public static Uri getAdhanSoundUri(Context context) {
    try {
      return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.read_azan);
    } catch (Exception e) {
      return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }
  }

  public static void createNotificationChannels(Context context) {
    if (context == null) return;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (manager == null) return;

      // Delete legacy/muted channels to reset OS priority and sound cache
      String[] legacyChannels = {
          "deanone_adhan_channel", "deanone_adhan_channel_v2", "deanone_adhan_channel_v3", "deanone_adhan_channel_v4", "deanone_adhan_channel_v5",
          "deanone_prayer_channel", "deanone_prayer_channel_v2", "deanone_prayer_channel_v3", "deanone_prayer_channel_v4", "deanone_prayer_channel_v5",
          "deanone_reminder_channel", "deanone_reminder_channel_v2", "deanone_reminder_channel_v3", "deanone_reminder_channel_v4", "deanone_reminder_channel_v5",
          "deanone_ramadan_channel", "deanone_ramadan_channel_v2", "deanone_ramadan_channel_v3", "deanone_ramadan_channel_v4",
          "deanone_daily_channel", "deanone_daily_content_channel_v2", "deanone_daily_content_channel_v3", "deanone_daily_content_channel_v4",
          "deanone_quiz_channel", "deanone_quiz_channel_v2", "deanone_quiz_channel_v3", "deanone_quiz_channel_v4",
          "deanone_admin_channel", "deanone_admin_channel_v2", "deanone_admin_channel_v3", "deanone_admin_channel_v4",
          "deanone_critical_channel", "deanone_critical_channel_v2", "deanone_critical_channel_v3", "deanone_critical_channel_v4",
          "deanone_durud_channel", "deanone_durud_channel_v2", "deanone_durud_channel_v3", "deanone_durud_channel_v4", "deanone_durud_channel_v5",
          "deanone_durud_channel_v5_ar1", "deanone_durud_channel_v5_ar2", "deanone_durud_channel_v5_ar3", "deanone_durud_channel_v5_bn", "deanone_durud_channel_v5_en",
          "deanone_istigfar_channel", "deanone_istigfar_channel_v2", "deanone_istigfar_channel_v3", "deanone_istigfar_channel_v4", "deanone_istigfar_channel_v5",
          "deanone_istigfar_channel_v5_ar", "deanone_istigfar_channel_v5_bn", "deanone_istigfar_channel_v5_en"
      };
      for (String chId : legacyChannels) {
        try {
          manager.deleteNotificationChannel(chId);
        } catch (Exception ignored) {}
      }

      Uri defaultSoundUri = getDefaultNotificationSoundUri(context);

      AudioAttributes notificationAudioAttributes = new AudioAttributes.Builder()
          .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
          .setUsage(AudioAttributes.USAGE_NOTIFICATION)
          .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
          .build();

      AudioAttributes speechAudioAttributes = new AudioAttributes.Builder()
          .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
          .setUsage(AudioAttributes.USAGE_NOTIFICATION)
          .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
          .build();

      AudioAttributes alarmAudioAttributes = new AudioAttributes.Builder()
          .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
          .setUsage(AudioAttributes.USAGE_ALARM)
          .build();

      // 1. Adhan Channel (High Importance, Lock Screen Public, sound handled via AudioPlayerService)
      NotificationChannel adhanChannel = new NotificationChannel(
          CHANNEL_ID_ADHAN,
          "আযান ও সালাত অ্যালার্ট (Adhan & Prayer Times)",
          NotificationManager.IMPORTANCE_HIGH
      );
      adhanChannel.setDescription("৫ ওয়াক্ত নামাজের সঠিক সময়ের আযান, ফুলস্ক্রিন অ্যালার্ট ও ওয়াক্ত নোটিফিকেশন");
      adhanChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      adhanChannel.enableVibration(true);
      adhanChannel.enableLights(true);
      adhanChannel.setLightColor(Color.parseColor("#10B981"));
      adhanChannel.setVibrationPattern(new long[]{0, 500, 300, 500, 300, 500});
      adhanChannel.setSound(null, null); // Dedicated Adhan audio is exclusively controlled by AudioPlayerService
      adhanChannel.setBypassDnd(true);
      manager.createNotificationChannel(adhanChannel);

      // 2. Prayer Times Channel
      NotificationChannel prayerChannel = new NotificationChannel(
          CHANNEL_ID_PRAYER,
          "নামাজের সময়সূচী (Prayer Times)",
          NotificationManager.IMPORTANCE_HIGH
      );
      prayerChannel.setDescription("নামাজের ওয়াক্ত শুরুর নোটিফিকেশন");
      prayerChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      prayerChannel.enableVibration(true);
      prayerChannel.enableLights(true);
      prayerChannel.setLightColor(Color.parseColor("#10B981"));
      prayerChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(prayerChannel);

      // 3. Upcoming Prayer Pre-reminder Channel
      NotificationChannel reminderChannel = new NotificationChannel(
          CHANNEL_ID_REMINDER,
          "আসন্ন নামাজের স্মরণিকা (Upcoming Prayer Reminder)",
          NotificationManager.IMPORTANCE_HIGH
      );
      reminderChannel.setDescription("নামাজের ওয়াক্ত শুরুর ৫-১৫ মিনিট পূর্বের প্রস্তুতি নোটিফিকেশন");
      reminderChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      reminderChannel.enableVibration(true);
      reminderChannel.enableLights(true);
      reminderChannel.setLightColor(Color.parseColor("#10B981"));
      reminderChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(reminderChannel);

      // 4. Seheri & Iftar Channel
      NotificationChannel ramadanChannel = new NotificationChannel(
          CHANNEL_ID_RAMADAN,
          "সেহরি ও ইফতার অ্যালার্ট (Seheri & Iftar Alert)",
          NotificationManager.IMPORTANCE_HIGH
      );
      ramadanChannel.setDescription("রমজান ও নফল রোজার সেহরি শেষ ও ইফতার শুরুর নোটিফিকেশন");
      ramadanChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      ramadanChannel.enableVibration(true);
      ramadanChannel.enableLights(true);
      ramadanChannel.setLightColor(Color.parseColor("#10B981"));
      ramadanChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(ramadanChannel);

      // 5. Daily Islamic Content (Amal, Dua, Hadith)
      NotificationChannel dailyChannel = new NotificationChannel(
          CHANNEL_ID_DAILY,
          "দৈনিক আমল, দোয়া ও হাদিস (Daily Islamic Content)",
          NotificationManager.IMPORTANCE_HIGH
      );
      dailyChannel.setDescription("দৈনিক আমল, নির্বাচিত সহীহ হাদিস, সকাল-সন্ধ্যার মাসনূন দোয়া");
      dailyChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      dailyChannel.enableVibration(true);
      dailyChannel.enableLights(true);
      dailyChannel.setLightColor(Color.parseColor("#10B981"));
      dailyChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(dailyChannel);

      // 6. Daily Quiz Challenge Channel
      NotificationChannel quizChannel = new NotificationChannel(
          CHANNEL_ID_QUIZ,
          "ইসলামিক কুইজ চ্যালেঞ্জ (Daily Quiz)",
          NotificationManager.IMPORTANCE_HIGH
      );
      quizChannel.setDescription("দৈনিক নতুন ইসলামিক কুইজ নোটিফিকেশন");
      quizChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      quizChannel.enableVibration(true);
      quizChannel.enableLights(true);
      quizChannel.setLightColor(Color.parseColor("#10B981"));
      quizChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(quizChannel);

      // 7. Admin Announcements & Community
      NotificationChannel adminChannel = new NotificationChannel(
          CHANNEL_ID_ADMIN,
          "গুরুত্বপূর্ণ ঘোষণা ও বার্তা (Admin Announcements)",
          NotificationManager.IMPORTANCE_HIGH
      );
      adminChannel.setDescription("এডমিন ও সার্ভার থেকে পাঠানো বিশেষ ইসলামিক ঘোষণা, কমিউনিটি ও নোটিশ");
      adminChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      adminChannel.enableVibration(true);
      adminChannel.enableLights(true);
      adminChannel.setLightColor(Color.parseColor("#10B981"));
      adminChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(adminChannel);

      // 8. Critical & System Updates
      NotificationChannel criticalChannel = new NotificationChannel(
          CHANNEL_ID_CRITICAL,
          "জরুরি ও সিস্টেম নোটিফিকেশন (Critical / System)",
          NotificationManager.IMPORTANCE_HIGH
      );
      criticalChannel.setDescription("বাধ্যতামূলক অ্যাপ আপডেট ও জরুরি সিস্টেম বার্তা");
      criticalChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      criticalChannel.enableVibration(true);
      criticalChannel.enableLights(true);
      criticalChannel.setLightColor(Color.parseColor("#10B981"));
      criticalChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(criticalChannel);

      // 9. Jummah Special Channel
      NotificationChannel jummahChannel = new NotificationChannel(
          "deanone_jummah_reminders",
          "পবিত্র জুমু'আ রিমাইন্ডার (Jummah Reminders)",
          NotificationManager.IMPORTANCE_HIGH
      );
      jummahChannel.setDescription("জুমার দিনের বিশেষ আমল, সূরা কাহাফ ও সা'আতুল ইজাবাহ রিমাইন্ডার");
      jummahChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      jummahChannel.enableVibration(true);
      jummahChannel.enableLights(true);
      jummahChannel.setLightColor(Color.parseColor("#34D399"));
      jummahChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(jummahChannel);

      // 10. Eid Special Channel
      NotificationChannel eidChannel = new NotificationChannel(
          "deanone_eid_reminders",
          "পবিত্র ঈদ ও তাকবীর রিমাইন্ডার (Eid Reminders)",
          NotificationManager.IMPORTANCE_HIGH
      );
      eidChannel.setDescription("ঈদুল ফিতর, ঈদুল আজহা ও তাকবীরে তাশরীক রিমাইন্ডার");
      eidChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      eidChannel.enableVibration(true);
      eidChannel.enableLights(true);
      eidChannel.setLightColor(Color.parseColor("#34D399"));
      eidChannel.setSound(defaultSoundUri, notificationAudioAttributes);
      manager.createNotificationChannel(eidChannel);

      // 11. Durud Channels (Voice & Language Specific Channels)
      String[][] durudConfigs = {
          {"ar1", "দুরুদ শরীফ (আরবি ক্বারী ১)"},
          {"ar2", "দুরুদ শরীফ (আরবি ক্বারী ২)"},
          {"ar3", "দুরুদ শরীফ (আরবি ক্বারী ৩)"},
          {"bn", "দুরুদ শরীফ (বাংলা তিলাওয়াত)"},
          {"en", "দুরুদ শরীফ (ইংরেজি অনুবাদ)"}
      };
      for (String[] config : durudConfigs) {
        String chId = CHANNEL_ID_DURUD_PREFIX + config[0];
        NotificationChannel durudCh = new NotificationChannel(
            chId,
            config[1],
            NotificationManager.IMPORTANCE_HIGH
        );
        durudCh.setDescription("বিশ্বনবী (ﷺ)-এর ওপর নিয়মিত দরূদ ও সালাওয়াত পাঠের স্মরণিকা");
        durudCh.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        durudCh.enableVibration(true);
        durudCh.enableLights(true);
        durudCh.setLightColor(Color.parseColor("#10B981"));
        durudCh.setSound(getDurudSoundUri(context, config[0]), speechAudioAttributes);
        manager.createNotificationChannel(durudCh);
      }

      // 12. Istigfar Channels (Language Specific Channels)
      String[][] istigfarConfigs = {
          {"ar", "ইস্তিগফার ও তওবা (আরবি পাঠ)"},
          {"bn", "ইস্তিগফার ও তওবা (বাংলা অনুবাদ)"},
          {"en", "ইস্তিগফার ও তওবা (ইংরেজি অনুবাদ)"}
      };
      for (String[] config : istigfarConfigs) {
        String chId = CHANNEL_ID_ISTIGFAR_PREFIX + config[0];
        NotificationChannel istigfarCh = new NotificationChannel(
            chId,
            config[1],
            NotificationManager.IMPORTANCE_HIGH
        );
        istigfarCh.setDescription("সাইয়্যিদুল ইস্তিগফার, ক্ষমা প্রার্থনা ও আত্মসমালোচনা স্মরণিকা");
        istigfarCh.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        istigfarCh.enableVibration(true);
        istigfarCh.enableLights(true);
        istigfarCh.setLightColor(Color.parseColor("#10B981"));
        istigfarCh.setSound(getIstigfarSoundUri(context, config[0]), speechAudioAttributes);
        manager.createNotificationChannel(istigfarCh);
      }

      // 13. Offline Download Progress Channel (Low Importance, Silent & Smooth)
      NotificationChannel downloadChannel = new NotificationChannel(
          CHANNEL_ID_DOWNLOAD,
          "অফলাইন ডাউনলোড (Offline Downloads)",
          NotificationManager.IMPORTANCE_LOW
      );
      downloadChannel.setDescription("কুরআন অডিও ও অফলাইন ফাইল ডাউনলোড প্রগ্রেস নোটিফিকেশন");
      downloadChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      downloadChannel.enableVibration(false);
      downloadChannel.enableLights(false);
      downloadChannel.setSound(null, null);
      manager.createNotificationChannel(downloadChannel);

      Log.d(TAG, "All DeenOne Notification Channels created successfully with HIGH importance, voice audio URIs and PUBLIC lock screen visibility.");
    }
  }

  public static void updateDownloadProgressNotification(Context context, int notificationId, String title, String text, int percent) {
    if (context == null) return;
    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (manager == null) return;
    createNotificationChannels(context);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_DOWNLOAD)
        .setSmallIcon(R.drawable.ic_download)
        .setContentTitle(title)
        .setContentText(text)
        .setProgress(100, Math.max(0, Math.min(100, percent)), false)
        .setOngoing(percent < 100)
        .setOnlyAlertOnce(true)
        .setPriority(NotificationCompat.PRIORITY_LOW);

    manager.notify(notificationId, builder.build());
  }

  public static void updateQuranDownloadNotificationWithActions(Context context, int notificationId, String title, String text, int percent, boolean isPaused) {
    if (context == null) return;
    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (manager == null) return;
    createNotificationChannels(context);

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    // Intent for Pause / Resume
    Intent toggleIntent = new Intent(context, com.devflux.deenone.core.quran.QuranDownloadActionReceiver.class);
    toggleIntent.setAction(isPaused
        ? com.devflux.deenone.core.quran.QuranDownloadActionReceiver.ACTION_RESUME
        : com.devflux.deenone.core.quran.QuranDownloadActionReceiver.ACTION_PAUSE);
    PendingIntent togglePendingIntent = PendingIntent.getBroadcast(
        context,
        101,
        toggleIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
    );

    // Intent for Cancel
    Intent cancelIntent = new Intent(context, com.devflux.deenone.core.quran.QuranDownloadActionReceiver.class);
    cancelIntent.setAction(com.devflux.deenone.core.quran.QuranDownloadActionReceiver.ACTION_CANCEL);
    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(
        context,
        102,
        cancelIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
    );

    int toggleIcon = isPaused ? R.drawable.ic_play_arrow : R.drawable.ic_pause;
    String toggleLabel = isPaused
        ? (isBn ? "চালু করুন" : "Resume")
        : (isBn ? "বিরতি" : "Pause");
    String cancelLabel = isBn ? "বাতিল" : "Cancel";

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_DOWNLOAD)
        .setSmallIcon(R.drawable.ic_download)
        .setContentTitle(title)
        .setContentText(text)
        .setProgress(100, Math.max(0, Math.min(100, percent)), false)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .addAction(toggleIcon, toggleLabel, togglePendingIntent)
        .addAction(R.drawable.ic_close, cancelLabel, cancelPendingIntent);

    manager.notify(notificationId, builder.build());
  }

  public static void completeDownloadNotification(Context context, int notificationId, String title, String completionText) {
    if (context == null) return;
    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (manager == null) return;
    createNotificationChannels(context);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_DOWNLOAD)
        .setSmallIcon(R.drawable.ic_check)
        .setContentTitle(title)
        .setContentText(completionText)
        .setProgress(0, 0, false)
        .setOngoing(false)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_LOW);

    manager.notify(notificationId, builder.build());

    // Auto-dismiss after 4 seconds
    new Handler(Looper.getMainLooper()).postDelayed(() -> {
      try {
        manager.cancel(notificationId);
      } catch (Exception ignored) {}
    }, 4000);
  }

  public static void cancelDownloadNotification(Context context, int notificationId) {
    if (context == null) return;
    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (manager != null) {
      try {
        manager.cancel(notificationId);
      } catch (Exception ignored) {}
    }
  }

  public static void sendDynamicNotification(Context context, String title, String body,
                       String type, String deepLinkAction, String priority) {
    if (context == null) return;

    // Strict 10-second debounce to prevent duplicate/multiple rapid notification alerts
    String signature = (type != null ? type : "") + "::" + (title != null ? title : "") + "::" + (body != null ? body : "");
    long now = System.currentTimeMillis();
    synchronized (NotificationHelper.class) {
      if (signature.equals(lastDispatchedSignature) && (now - lastDispatchedTimestamp) < 10000L) {
        Log.d(TAG, "Suppressing duplicate dynamic notification within 10s window: " + title);
        return;
      }
      lastDispatchedSignature = signature;
      lastDispatchedTimestamp = now;
    }

    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (manager == null) return;

    createNotificationChannels(context);

    String channelId = CHANNEL_ID_DAILY;
    int importance = NotificationCompat.PRIORITY_HIGH;
    final int iconRes = R.drawable.ic_notification_deenone;

    boolean isAdhan = "adhan".equalsIgnoreCase(type);
    boolean isPrayer = "prayer".equalsIgnoreCase(type)
        || "fajr".equalsIgnoreCase(type) || "dhuhr".equalsIgnoreCase(type) || "asr".equalsIgnoreCase(type)
        || "maghrib".equalsIgnoreCase(type) || "isha".equalsIgnoreCase(type);

    if (isAdhan) {
      channelId = CHANNEL_ID_ADHAN;
      importance = NotificationCompat.PRIORITY_MAX;
    } else if (isPrayer) {
      channelId = CHANNEL_ID_PRAYER;
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("reminder".equalsIgnoreCase(type)) {
      channelId = CHANNEL_ID_REMINDER;
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("sehri".equalsIgnoreCase(type) || "iftar".equalsIgnoreCase(type) || "ramadan".equalsIgnoreCase(type)) {
      channelId = CHANNEL_ID_RAMADAN;
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("quiz".equalsIgnoreCase(type) || (deepLinkAction != null && deepLinkAction.contains("quiz")) || (title != null && title.contains("কুইজ"))) {
      if (QuizManager.getInstance().hasPlayedQuizToday(context)) {
        Log.d(TAG, "User has already played/completed today's quiz. Suppressing quiz notification.");
        return;
      }
      channelId = CHANNEL_ID_QUIZ;
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("admin_announcement".equalsIgnoreCase(type) || "announcement".equalsIgnoreCase(type)
        || "community_like".equalsIgnoreCase(type) || "community_comment".equalsIgnoreCase(type) || "community_reply".equalsIgnoreCase(type)) {
      channelId = CHANNEL_ID_ADMIN;
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("jumuah_kahf".equalsIgnoreCase(type) || "jumuah".equalsIgnoreCase(type) || "jummah".equalsIgnoreCase(type)) {
      channelId = "deanone_jummah_reminders";
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("eid".equalsIgnoreCase(type)) {
      channelId = "deanone_eid_reminders";
      importance = NotificationCompat.PRIORITY_HIGH;
    } else if ("durud".equalsIgnoreCase(type) || "salawat".equalsIgnoreCase(type)) {
      channelId = getDurudChannelId(context);
      importance = NotificationCompat.PRIORITY_MAX;
    } else if ("istigfar".equalsIgnoreCase(type) || "istighfar".equalsIgnoreCase(type) || "tawbah".equalsIgnoreCase(type)) {
      channelId = getIstigfarChannelId(context);
      importance = NotificationCompat.PRIORITY_MAX;
    } else if ("critical_update".equalsIgnoreCase(type) || "force_update".equalsIgnoreCase(type)) {
      channelId = CHANNEL_ID_CRITICAL;
      importance = NotificationCompat.PRIORITY_MAX;
    }

    int notifId = getDeterministicNotificationId(type, title);

    Intent intent = new Intent(context, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.putExtra("extra_notification_title", title);
    intent.putExtra("extra_notification_body", body);
    intent.putExtra("extra_notification_type", type);
    intent.putExtra("extra_timestamp", System.currentTimeMillis());
    if (deepLinkAction != null) {
      intent.putExtra("extra_deep_link", deepLinkAction);
      intent.putExtra("navigate_to", deepLinkAction);
    }

    PendingIntent pendingIntent = PendingIntent.getActivity(
        context, notifId, intent,
        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    Uri soundUri = getDefaultNotificationSoundUri(context);
    if ("durud".equalsIgnoreCase(type) || "salawat".equalsIgnoreCase(type)) {
      soundUri = getDurudSoundUri(context, NotificationSettingsManager.getDurudAudioKey(context));
    } else if ("istigfar".equalsIgnoreCase(type) || "istighfar".equalsIgnoreCase(type) || "tawbah".equalsIgnoreCase(type)) {
      soundUri = getIstigfarSoundUri(context, NotificationSettingsManager.getIstigfarAudioLanguage(context));
    } else if (isAdhan) {
      soundUri = null; // Full Adhan audio playback is exclusively managed by AudioPlayerService
    } else if (isPrayer) {
      soundUri = getDefaultNotificationSoundUri(context);
    }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
        .setSmallIcon(iconRes)
        .setContentTitle(title)
        .setContentText(body)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
        .setPriority(importance)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
        .setCategory(isAdhan ? NotificationCompat.CATEGORY_ALARM : NotificationCompat.CATEGORY_REMINDER)
        .setContentIntent(pendingIntent);

    try {
      builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
    } catch (Exception ignored) {}

    if (soundUri != null) {
      builder.setSound(soundUri);
    }

    if (isAdhan) {
      Intent stopIntent = new Intent(context, AdhanAlarmReceiver.class);
      stopIntent.setAction(AdhanAlarmReceiver.ACTION_STOP_ADHAN);
      stopIntent.putExtra(AdhanAlarmReceiver.EXTRA_PRAYER_KEY, type);
      PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
          context, notifId + 10, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
      );

      boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
      builder.addAction(R.drawable.ic_stop, isBn ? "আযান বন্ধ করুন" : "Stop Adhan", stopPendingIntent);
      builder.addAction(R.drawable.ic_mosque, isBn ? "নামাজের সময়সূচী" : "Prayer Times", pendingIntent);
      builder.setDeleteIntent(stopPendingIntent);
      builder.setFullScreenIntent(pendingIntent, false);
    }

    if (NotificationSettingsManager.isVibrationEnabled(context)) {
      builder.setVibrate(new long[]{0, 350, 150, 350});
      // Direct haptic feedback to ensure immediate vibration
      try {
        IslamicVibrationHelper.triggerVibration(context, IslamicVibrationHelper.PatternType.DOUBLE_PULSE, false);
      } catch (Exception ignored) {}
    } else {
      builder.setVibrate(new long[]{0});
    }

    try {
      manager.notify(notifId, builder.build());
    } catch (SecurityException se) {
      Log.e(TAG, "Notification permission missing or security exception: " + se.getMessage());
    } catch (Exception e) {
      Log.e(TAG, "Error posting notification ID " + notifId + ": " + e.getMessage(), e);
    }

    // Save to Notification History Database (Strict duplicate prevention)
    try {
      NotificationRepository repo = NotificationRepository.getInstance(context);
      com.devflux.deenone.data.local.AppDatabase db = com.devflux.deenone.data.local.AppDatabase.getInstance(context);
      if (db.notificationDao().countByTitleAndMessage(title, body) == 0) {
        repo.saveNotification(new NotificationMessageEntity(
            title, body, type, System.currentTimeMillis(),
            false, deepLinkAction, priority, "system"
        ));
      }
    } catch (Exception e) {
      Log.w(TAG, "Could not save notification to history: "+ e.getMessage());
    }
  }

  public static void sendSehriIftarNotification(Context context, boolean isSehri, String timeStr, String message) {
    if (context == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String title = isBn
        ? (isSehri ? "সেহরির শেষ সময় আসন্ন ("+ timeStr + ")" : "ইফতারের সময় হয়েছে ("+ timeStr + ")")
        : (isSehri ? "Sehri ending time approaching ("+ timeStr + ")" : "Iftar time has arrived ("+ timeStr + ")");
    sendDynamicNotification(context, title, message, isSehri ? "sehri" : "iftar", "feature_ramadan", "high");
  }

  public static void sendCommunityReplyNotification(Context context, String replyAuthorName, String targetAuthorName, String commentText, String postId) {
    if (context == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String title = isBn ? "💬 আপনার মন্তব্যে নতুন উত্তর!" : "💬 New reply to your comment!";
    String preview = commentText != null && commentText.length() > 50 ? commentText.substring(0, 47) + "..." : (commentText != null ? commentText : "");
    String body = isBn
        ? (replyAuthorName + " আপনার মন্তব্যে উত্তর দিয়েছেন: \"" + preview + "\"")
        : (replyAuthorName + " replied to your comment: \"" + preview + "\"");
    String deepLink = (postId != null && !postId.isEmpty()) ? ("feature_community:post_id:" + postId) : "feature_community";
    sendDynamicNotification(context, title, body, "community_reply", deepLink, "high");
  }

  public static void sendCommunityReplyNotification(Context context, String replyAuthorName, String targetAuthorName, String commentText) {
    sendCommunityReplyNotification(context, replyAuthorName, targetAuthorName, commentText, null);
  }

  public static void sendCommunityLikeNotification(Context context, String likerName, String postTitle, String postId) {
    if (context == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String title = isBn ? "❤️ আপনার পোস্টে লাইক এসেছে!" : "❤️ New like on your post!";
    String preview = postTitle != null && postTitle.length() > 40 ? postTitle.substring(0, 37) + "..." : (postTitle != null ? postTitle : "");
    String body = isBn
        ? (likerName + " আপনার \"" + preview + "\" পোস্টে লাইক করেছেন।")
        : (likerName + " liked your post: \"" + preview + "\"");
    String deepLink = (postId != null && !postId.isEmpty()) ? ("feature_community:post_id:" + postId) : "feature_community";
    sendDynamicNotification(context, title, body, "community_like", deepLink, "normal");
  }

  public static void sendCommunityLikeNotification(Context context, String likerName, String postTitle) {
    sendCommunityLikeNotification(context, likerName, postTitle, null);
  }

  public static void sendCommunityCommentNotification(Context context, String commenterName, String postTitle, String commentText, String postId) {
    if (context == null) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String title = isBn ? "💬 আপনার পোস্টে নতুন মন্তব্য!" : "💬 New comment on your post!";
    String preview = commentText != null && commentText.length() > 50 ? commentText.substring(0, 47) + "..." : (commentText != null ? commentText : "");
    String body = isBn
        ? (commenterName + " আপনার পোস্টে মন্তব্য করেছেন: \"" + preview + "\"")
        : (commenterName + " commented on your post: \"" + preview + "\"");
    String deepLink = (postId != null && !postId.isEmpty()) ? ("feature_community:post_id:" + postId) : "feature_community";
    sendDynamicNotification(context, title, body, "community_comment", deepLink, "high");
  }

  public static void sendCommunityCommentNotification(Context context, String commenterName, String postTitle, String commentText) {
    sendCommunityCommentNotification(context, commenterName, postTitle, commentText, null);
  }

  public static void sendForceCriticalNotification(Context context, String title, String body, String deepLink) {
    sendDynamicNotification(context, "" + title, body, "critical_update", deepLink != null ? deepLink : "feature_update", "critical");
  }

  public static void cancelQuizNotification(Context context) {
    if (context == null) return;
    try {
      NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (manager != null) {
        manager.cancel(5004);
      }
    } catch (Exception e) {
      Log.w(TAG, "Error cancelling quiz notification: "+ e.getMessage());
    }
  }

  public static void sendDurudNotification(Context context, String title, String message, String durudType) {
    if (context == null) return;
    if (!"test".equalsIgnoreCase(durudType) && !NotificationSettingsManager.isDurudReminderEnabled(context)) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String defaultTitle = isBn ? "দুরুদ শরীফ স্মরণিকা" : "Durood Sharif Reminder";
    String deepLink = "feature_tasbih";
    sendDynamicNotification(context, title != null ? title : defaultTitle, message, "durud", deepLink, "high");
  }

  public static void sendIstigfarNotification(Context context, String title, String message, String istigfarType) {
    if (context == null) return;
    if (!"test".equalsIgnoreCase(istigfarType) && !NotificationSettingsManager.isIstigfarReminderEnabled(context)) return;
    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
    String defaultTitle = isBn ? "ইস্তিগফার ও তওবা স্মরণিকা" : "Istighfar & Tawbah Reminder";
    String deepLink = "feature_dua";
    sendDynamicNotification(context, title != null ? title : defaultTitle, message, "istigfar", deepLink, "high");
  }

  private static int getDeterministicNotificationId(String type, String title) {
    if ("fajr".equalsIgnoreCase(type)) return 1001;
    if ("dhuhr".equalsIgnoreCase(type)) return 1003;
    if ("asr".equalsIgnoreCase(type)) return 1004;
    if ("maghrib".equalsIgnoreCase(type)) return 1005;
    if ("isha".equalsIgnoreCase(type)) return 1006;
    if ("sehri".equalsIgnoreCase(type)) return 3001;
    if ("iftar".equalsIgnoreCase(type)) return 3002;
    if ("daily_amal".equalsIgnoreCase(type)) return 5001;
    if ("daily_hadith".equalsIgnoreCase(type)) return 5002;
    if ("daily_dua".equalsIgnoreCase(type)) return 5003;
    if ("quiz".equalsIgnoreCase(type)) return 5004;
    if ("durud".equalsIgnoreCase(type)) return 7001;
    if ("istigfar".equalsIgnoreCase(type)) return 7002;
    String seed = (type != null ? type : "") + (title != null ? title : "");
    return Math.abs(seed.hashCode() % 900000) + 10000;
  }
}
