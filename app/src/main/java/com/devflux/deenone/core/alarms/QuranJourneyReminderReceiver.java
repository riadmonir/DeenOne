package com.devflux.deenone.core.alarms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.quran.QuranJourneyManager;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.data.repository.NotificationRepository;

public class QuranJourneyReminderReceiver extends BroadcastReceiver {

    private static final String TAG = "QuranJourneyReceiver";
    public static final String ACTION_JOURNEY_REMINDER = "com.devflux.deenone.ACTION_QURAN_JOURNEY_REMINDER";
    public static final String CHANNEL_ID = "deanone_quran_journey_reminders";
    private static final int NOTIF_ID = 88210;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;

        String action = intent.getAction();
        Log.d(TAG, "onReceive called with action: " + action);

        // Handle reboot / time change to reschedule
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || "android.intent.action.TIME_SET".equals(action)
                || "android.intent.action.TIMEZONE_CHANGED".equals(action)) {
            QuranJourneyManager.scheduleDailyReminder(context);
            return;
        }

        if (ACTION_JOURNEY_REMINDER.equals(action) || action == null) {
            boolean isBn = LocaleManager.isBengali(context);

            String title = isBn ? "কুরআন যাত্রা: আজকের পাঠ ও আমল" : "Quran Journey: Today's Lesson & Deeds";
            String message = isBn
                    ? "রাত ৯:০০ টা বেজেছে! আজকের কুরআন শিক্ষা ও আমল সম্পন্ন করে নূর পয়েন্ট অর্জন করুন।"
                    : "It is 9:00 PM! Complete today's Quran lesson and deeds to earn Noor Points.";

            showNotification(context, title, message, isBn);

            // Persist to notification history
            try {
                NotificationRepository repo = new NotificationRepository(context);
                repo.saveNotification(new NotificationMessageEntity(
                        title, message, "quran_journey", System.currentTimeMillis(),
                        false, "quran_mode", "high", "system"
                ));
            } catch (Exception e) {
                Log.w(TAG, "Error saving journey notification: " + e.getMessage());
            }

            // Reschedule for next day 9:00 PM
            QuranJourneyManager.scheduleDailyReminder(context);
        }
    }

    private void showNotification(Context context, String title, String message, boolean isBn) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    isBn ? "কুরআন যাত্রা দৈনিক রিমাইন্ডার" : "Quran Journey Daily Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(isBn ? "প্রতিদিন রাত ৯:০০ টায় কুরআন শিক্ষা ও আমল রিমাইন্ডার" : "Daily 9:00 PM Quran lesson and deeds reminder");
            channel.enableLights(true);
            channel.setLightColor(Color.parseColor("#10B981"));
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 300, 200, 300});
            nm.createNotificationChannel(channel);
        }

        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openIntent.putExtra("DEEP_LINK_FEATURE", "QURAN_JOURNEY");
        openIntent.putExtra("extra_deep_link", "feature_quran_journey");

        PendingIntent pi = PendingIntent.getActivity(
                context,
                NOTIF_ID,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_deenone)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentIntent(pi);

        try {
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        } catch (Exception ignored) {}

        nm.notify(NOTIF_ID, builder.build());
    }
}
