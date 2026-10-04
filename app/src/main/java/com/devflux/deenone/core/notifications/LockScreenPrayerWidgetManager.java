package com.devflux.deenone.core.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LockScreenPrayerWidgetManager {

  public static final String CHANNEL_ID_WIDGET = "deanone_lockscreen_widget_channel_v3";
  public static final int NOTIF_ID_WIDGET = 7777;

  public static void updateLockScreenWidget(Context context) {
    if (context == null) return;

    if (!NotificationSettingsManager.isLockScreenWidgetEnabled(context)) {
      cancelWidget(context);
      return;
    }

    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (nm == null) return;

    boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          CHANNEL_ID_WIDGET,
          isBn ? "লকস্ক্রিন সালাত উইজেট" : "Lock Screen Prayer Widget",
          NotificationManager.IMPORTANCE_LOW
      );
      channel.setDescription(isBn ? "লক স্ক্রিনে বর্তমান ও পরবর্তী নামাজের সময় এবং সেহরি-ইফতারের লাইভ স্ট্যাটাস" : "Live prayer times and Suhoor/Iftar countdown on lock screen");
      channel.setShowBadge(false);
      channel.enableVibration(false);
      channel.setSound(null, null);
      channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
      nm.createNotificationChannel(channel);
    }

    LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
    Calendar nowCal = Calendar.getInstance();

    PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocationWithContext(
        context, coords.latitude, coords.longitude, coords.timezone, nowCal
    );

    long now = nowCal.getTimeInMillis();
    SimpleDateFormat timeFmt = new SimpleDateFormat("hh:mm a", Locale.US);

    String currentWaqt = isBn ? "ফজর" : "Fajr";
    String currentRange = res.fajrStr + " - " + res.sunriseStr;
    String nextWaqt = isBn ? "জোহর" : "Dhuhr";
    String nextTime = res.zohrStr;
    long nextTargetMillis = res.zohrMillis;

    if (now < res.fajrMillis) {
      currentWaqt = isBn ? "তাহাজ্জুদ / সেহরি" : "Tahajjud / Suhoor";
      currentRange = res.lastThirdOfNightStr + " - " + res.fajrStr;
      nextWaqt = isBn ? "ফজর" : "Fajr";
      nextTime = res.fajrStr;
      nextTargetMillis = res.fajrMillis;
    } else if (now < res.sunriseMillis) {
      currentWaqt = isBn ? "ফজর" : "Fajr";
      currentRange = res.fajrStr + " - " + res.sunriseStr;
      nextWaqt = isBn ? "ইশরাক / সূর্যোদয়" : "Ishraq / Sunrise";
      nextTime = res.sunriseStr;
      nextTargetMillis = res.sunriseMillis;
    } else if (now < res.zohrMillis) {
      currentWaqt = isBn ? "চাশত / দুহা" : "Duha";
      currentRange = res.sunriseStr + " - " + res.zohrStr;
      nextWaqt = res.isFriday ? (isBn ? "জুম্মা" : "Jummah") : (isBn ? "যোহর" : "Dhuhr");
      nextTime = res.zohrStr;
      nextTargetMillis = res.zohrMillis;
    } else if (now < res.asrMillis) {
      currentWaqt = res.isFriday ? (isBn ? "জুম্মা" : "Jummah") : (isBn ? "যোহর" : "Dhuhr");
      currentRange = res.zohrStr + " - " + res.asrStr;
      nextWaqt = isBn ? "আসর" : "Asr";
      nextTime = res.asrStr;
      nextTargetMillis = res.asrMillis;
    } else if (now < res.maghribMillis) {
      currentWaqt = isBn ? "আসর" : "Asr";
      currentRange = res.asrStr + " - " + res.maghribStr;
      nextWaqt = isBn ? "মাগরিব / ইফতার" : "Maghrib / Iftar";
      nextTime = res.maghribStr;
      nextTargetMillis = res.maghribMillis;
    } else if (now < res.ishaMillis) {
      currentWaqt = isBn ? "মাগরিব" : "Maghrib";
      currentRange = res.maghribStr + " - " + res.ishaStr;
      nextWaqt = isBn ? "এশা" : "Isha";
      nextTime = res.ishaStr;
      nextTargetMillis = res.ishaMillis;
    } else {
      currentWaqt = isBn ? "এশা" : "Isha";
      currentRange = res.ishaStr + " - " + res.fajrStr;
      nextWaqt = isBn ? "ফজর" : "Fajr";
      nextTime = res.fajrStr;
      nextTargetMillis = res.fajrMillis + (24 * 3600 * 1000);
    }

    long diffSec = Math.max(0, (nextTargetMillis - now) / 1000);
    long hrs = diffSec / 3600;
    long mins = (diffSec % 3600) / 60;
    String remainingText = isBn 
        ? ((hrs > 0 ? BengaliNumberUtil.toBengali(hrs) + " ঘণ্টা " : "") + BengaliNumberUtil.toBengali(mins) + " মিনিট")
        : ((hrs > 0 ? hrs + " hr " : "") + mins + " min");

    // Pending Intent for Main Click
    Intent openAppIntent = new Intent(context, MainActivity.class);
    openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    openAppIntent.putExtra("extra_deep_link", "feature_prayer");
    PendingIntent openAppPi = PendingIntent.getActivity(
        context, 7701, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    // Action: Prayer Times
    Intent prayerIntent = new Intent(context, MainActivity.class);
    prayerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    prayerIntent.putExtra("extra_deep_link", "feature_salah");
    PendingIntent prayerPi = PendingIntent.getActivity(
        context, 7702, prayerIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    // Action: Dua
    Intent duaIntent = new Intent(context, MainActivity.class);
    duaIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    duaIntent.putExtra("extra_deep_link", "feature_dua");
    PendingIntent duaPi = PendingIntent.getActivity(
        context, 7703, duaIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    // Action: Quran
    Intent quranIntent = new Intent(context, MainActivity.class);
    quranIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    quranIntent.putExtra("extra_deep_link", "feature_quran");
    PendingIntent quranPi = PendingIntent.getActivity(
        context, 7704, quranIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
    );

    String title = currentWaqt + " (" + currentRange.toLowerCase() + ")";
    String summary = isBn 
        ? ("পরবর্তী: " + nextWaqt + " (" + nextTime.toLowerCase() + ") • বাকি " + remainingText)
        : ("Next: " + nextWaqt + " (" + nextTime.toLowerCase() + ") • in " + remainingText);
    String bigDetails = isBn 
        ? ("বর্তমান ওয়াক্ত: " + currentWaqt + " • " + currentRange.toLowerCase() + "\n"
           + "পরবর্তী ওয়াক্ত: " + nextWaqt + " • " + nextTime.toLowerCase() + " (বাকি " + remainingText + ")\n"
           + "সেহরি শেষ: " + res.sehriStr.toLowerCase() + " | ইফতার: " + res.iftarStr.toLowerCase() + "\n"
           + coords.locationName)
        : ("Current: " + currentWaqt + " • " + currentRange.toLowerCase() + "\n"
           + "Next: " + nextWaqt + " • " + nextTime.toLowerCase() + " (in " + remainingText + ")\n"
           + "Suhoor ends: " + res.sehriStr.toLowerCase() + " | Iftar: " + res.iftarStr.toLowerCase() + "\n"
           + coords.locationName);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_WIDGET)
        .setSmallIcon(R.drawable.ic_notification_deenone)
        .setContentTitle(title)
        .setContentText(summary)
        .setStyle(new NotificationCompat.BigTextStyle().bigText(bigDetails))
        .setContentIntent(openAppPi)
        .setOngoing(true) // Pinned persistent widget
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .addAction(R.drawable.ic_feat_mosque, isBn ? "নামাজ" : "Prayer", prayerPi)
        .addAction(R.drawable.ic_feat_dua, isBn ? "দোয়া" : "Dua", duaPi)
        .addAction(R.drawable.ic_feat_ayah, isBn ? "কুরআন" : "Quran", quranPi);

    try {
      builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
    } catch (Exception ignored) {}

    nm.notify(NOTIF_ID_WIDGET, builder.build());
  }

  public static void cancelWidget(Context context) {
    if (context == null) return;
    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    if (nm != null) {
      nm.cancel(NOTIF_ID_WIDGET);
    }
  }
}