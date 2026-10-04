package com.devflux.deenone.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.notifications.NotificationHelper;

public class AudioPlayerService extends Service {

    private static final String TAG = "AudioPlayerService";
    public static final int NOTIFICATION_ID_ADHAN_SERVICE = 9001;

    public static final String ACTION_PLAY_ADHAN = "com.devflux.deenone.ACTION_PLAY_ADHAN";
    public static final String ACTION_STOP_ADHAN = "com.devflux.deenone.ACTION_STOP_ADHAN";

    public static final String EXTRA_AUDIO_URL = "extra_audio_url";
    public static final String EXTRA_PRAYER_NAME = "extra_prayer_name";
    public static final String EXTRA_PRAYER_KEY = "extra_prayer_key";
    public static final String EXTRA_REPEAT_COUNT = "extra_repeat_count";
    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";

    private static volatile AudioPlayerService instance;
    private ExoPlayer player;
    private PowerManager.WakeLock wakeLock;
    private boolean isPlaying = false;
    private int repeatCount = 1;
    private int currentIteration = 0;
    private int currentNotificationId = NOTIFICATION_ID_ADHAN_SERVICE;

    public static void startAdhan(Context context, String audioUrl, String prayerName, String prayerKey) {
        startAdhan(context, audioUrl, prayerName, prayerKey, 1, NOTIFICATION_ID_ADHAN_SERVICE);
    }

    public static void startAdhan(Context context, String audioUrl, String prayerName, String prayerKey, int notifId) {
        startAdhan(context, audioUrl, prayerName, prayerKey, 1, notifId);
    }

    public static void startAdhan(Context context, String audioUrl, String prayerName, String prayerKey, int repeatCount, int notifId) {
        if (context == null || audioUrl == null || audioUrl.isEmpty()) return;
        Intent intent = new Intent(context, AudioPlayerService.class);
        intent.setAction(ACTION_PLAY_ADHAN);
        intent.putExtra(EXTRA_AUDIO_URL, audioUrl);
        intent.putExtra(EXTRA_PRAYER_NAME, prayerName);
        intent.putExtra(EXTRA_PRAYER_KEY, prayerKey);
        intent.putExtra(EXTRA_REPEAT_COUNT, Math.max(1, Math.min(5, repeatCount)));
        intent.putExtra(EXTRA_NOTIFICATION_ID, notifId);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, intent);
            } else {
                context.startService(intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to start AudioPlayerService: " + e.getMessage(), e);
            // Fallback: direct player execution
            getInstance(context).playAudioUrl(audioUrl);
        }
    }

    public static void stopAdhan(Context context) {
        if (context == null) return;
        Intent intent = new Intent(context, AudioPlayerService.class);
        intent.setAction(ACTION_STOP_ADHAN);
        try {
            context.startService(intent);
        } catch (Exception ignored) {
            if (instance != null) {
                instance.stopAudio();
            }
        }
    }

    public static AudioPlayerService getInstance(Context context) {
        if (instance == null) {
            synchronized (AudioPlayerService.class) {
                if (instance == null) {
                    AudioPlayerService dummy = new AudioPlayerService();
                    dummy.contextFallback = context.getApplicationContext();
                    instance = dummy;
                }
            }
        }
        return instance;
    }

    private Context contextFallback;

    private Context getEffectiveContext() {
        return (getBaseContext() != null) ? this : (contextFallback != null ? contextFallback : this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        acquireWakeLock();
        initializePlayer();
    }

    private void acquireWakeLock() {
        try {
            if (wakeLock == null || !wakeLock.isHeld()) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (pm != null) {
                    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DeenOne:AdhanPlayerWakeLock");
                    wakeLock.acquire(5 * 60 * 1000L); // 5 minutes max safety timeout
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Could not acquire WakeLock: " + e.getMessage());
        }
    }

    private void releaseWakeLock() {
        try {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
                wakeLock = null;
            }
        } catch (Exception ignored) {}
    }

    private void initializePlayer() {
        if (player == null) {
            Context ctx = getEffectiveContext();
            player = new ExoPlayer.Builder(ctx).build();

            // Set USAGE_ALARM with FLAG_AUDIBILITY_ENFORCED so Azan plays loud and clear via loudspeaker
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(C.USAGE_ALARM)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                    .setFlags(C.FLAG_AUDIBILITY_ENFORCED)
                    .build();
            player.setAudioAttributes(audioAttributes, true);
            player.setVolume(1.0f);
            player.setWakeMode(C.WAKE_MODE_LOCAL);

            player.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean playing) {
                    isPlaying = playing;
                }

                @Override
                public void onPlaybackStateChanged(int playbackState) {
                    if (playbackState == Player.STATE_ENDED) {
                        currentIteration++;
                        if (currentIteration < repeatCount && player != null) {
                            player.seekTo(0);
                            player.play();
                        } else {
                            stopForegroundAndSelf();
                        }
                    } else if (playbackState == Player.STATE_IDLE && currentIteration >= repeatCount) {
                        stopForegroundAndSelf();
                    }
                }

                @Override
                public void onPlayerError(PlaybackException error) {
                    Log.e(TAG, "Adhan player error: " + error.getMessage());
                    stopForegroundAndSelf();
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;
        acquireWakeLock();

        if (intent == null || ACTION_STOP_ADHAN.equals(intent.getAction())) {
            stopForegroundAndSelf();
            return START_NOT_STICKY;
        }

        this.repeatCount = Math.max(1, Math.min(5, intent.getIntExtra(EXTRA_REPEAT_COUNT, 1)));
        this.currentIteration = 0;
        this.currentNotificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_ID_ADHAN_SERVICE);

        String audioUrl = intent.getStringExtra(EXTRA_AUDIO_URL);
        String prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME);
        String prayerKey = intent.getStringExtra(EXTRA_PRAYER_KEY);

        if (prayerName == null) prayerName = "সালাত";
        if (prayerKey == null) prayerKey = "fajr";

        // 1. Build and post Foreground Notification
        Notification notification = buildAdhanForegroundNotification(prayerName, prayerKey, this.currentNotificationId);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ServiceCompat.startForeground(
                        this,
                        this.currentNotificationId,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                );
            } else {
                startForeground(this.currentNotificationId, notification);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error starting foreground service: " + e.getMessage(), e);
        }

        // 2. Start Audio Playback with loudspeaker enforcement
        if (audioUrl != null && !audioUrl.isEmpty()) {
            playAudioUrl(audioUrl);
        }

        return START_NOT_STICKY;
    }

    private Notification buildAdhanForegroundNotification(String prayerName, String prayerKey, int notifId) {
        NotificationHelper.createNotificationChannels(this);

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);

        Intent appIntent = new Intent(this, MainActivity.class);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        appIntent.putExtra("extra_deep_link", "feature_prayer");
        PendingIntent openPendingIntent = PendingIntent.getActivity(
                this, notifId, appIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Intent stopIntent = new Intent(this, AdhanAlarmReceiver.class);
        stopIntent.setAction(AdhanAlarmReceiver.ACTION_STOP_ADHAN);
        stopIntent.putExtra(AdhanAlarmReceiver.EXTRA_PRAYER_KEY, prayerKey);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                this, notifId + 10, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        boolean isSehri = "sehri".equalsIgnoreCase(prayerKey) || (prayerName != null && prayerName.contains("সেহরি"));
        boolean isIftar = "iftar".equalsIgnoreCase(prayerKey) || (prayerName != null && prayerName.contains("ইফতার"));
        boolean isJummah = "jummah".equalsIgnoreCase(prayerKey);

        String title;
        String contentText;
        String actionBtnText;
        String channelId;

        if (isSehri) {
            title = isBn ? "সেহরি অ্যালার্ম চলছে" : "Sehri Alarm is Playing";
            contentText = isBn ? "সেহরির শেষ সময় আসন্ন। সেহরি সমাপ্ত করুন ও রোযার নিয়ত করুন।" : "Sehri time is ending soon. Finish Sehri and make intention for fasting.";
            actionBtnText = isBn ? "সেহরি বন্ধ করুন" : "Stop Sehri Alarm";
            channelId = NotificationHelper.CHANNEL_ID_RAMADAN;
        } else if (isIftar) {
            title = isBn ? "ইফতারের সময় হয়েছে" : "Time for Iftar";
            contentText = isBn ? "বিসমিল্লাহ বলে ইফতার করুন: 'আল্লাহুম্মা লাকা সুমতু ওয়া আলা রিজক্বিকা আফতারতু'। " : "Break your fast with Bismillah: 'Allahumma laka sumtu wa ala rizqika aftartu'.";
            actionBtnText = isBn ? "ইফতার বন্ধ করুন" : "Dismiss Iftar";
            channelId = NotificationHelper.CHANNEL_ID_RAMADAN;
        } else if (isJummah) {
            title = isBn ? "পবিত্র জুমু'আর আযান চলছে" : "Holy Jummah Adhan is Playing";
            contentText = isBn ? "আজ পবিত্র জুমু'আ। দ্রুত মসজিদে গমন করুন এবং জামায়াতে নামাজ আদায় করুন।" : "Today is Holy Jummah. Please proceed to the mosque for congregational prayer.";
            actionBtnText = isBn ? "আযান বন্ধ করুন" : "Stop Adhan";
            channelId = NotificationHelper.CHANNEL_ID_ADHAN;
        } else {
            title = isBn ? (prayerName + " এর আযান চলছে") : (prayerName + " Adhan is Playing");
            contentText = isBn ? "সালাত কায়েম করুন, জামায়াতে নামাজ আদায় করুন।" : "Establish prayer, join the congregation at the mosque.";
            actionBtnText = isBn ? "আযান বন্ধ করুন" : "Stop Adhan";
            channelId = NotificationHelper.CHANNEL_ID_ADHAN;
        }

        Bitmap largeAppIcon = null;
        try {
            largeAppIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        } catch (Exception ignored) {}

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_deenone)
                .setContentTitle(title)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setColor(ContextCompat.getColor(this, R.color.accent_mint))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(openPendingIntent)
                .setDeleteIntent(stopPendingIntent)
                .addAction(R.drawable.ic_stop, actionBtnText, stopPendingIntent)
                .addAction(R.drawable.ic_mosque, isBn ? "নামাজের সময়সূচী" : "Prayer Times", openPendingIntent)
                .setOngoing(true);

        if (largeAppIcon != null) {
            builder.setLargeIcon(largeAppIcon);
        }

        return builder.build();
    }

    private void boostAlarmVolumeAndRouteLoudspeaker() {
        try {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (am != null) {
                // Ensure audio mode is normal so audio plays through external loudspeaker
                am.setMode(AudioManager.MODE_NORMAL);
                try {
                    am.setSpeakerphoneOn(true);
                } catch (Exception ignored) {}

                // Set device STREAM_ALARM volume to 100% maximum for clear loud Azan
                int maxAlarmVol = am.getStreamMaxVolume(AudioManager.STREAM_ALARM);
                am.setStreamVolume(AudioManager.STREAM_ALARM, maxAlarmVol, 0);

                // Also boost STREAM_MUSIC volume if low
                int maxMusicVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                if (am.getStreamVolume(AudioManager.STREAM_MUSIC) < (int)(maxMusicVol * 0.85f)) {
                    am.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusicVol, 0);
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Could not set max alarm volume: " + e.getMessage());
        }
    }

    public void playAudioUrl(String url) {
        acquireWakeLock();
        initializePlayer();
        boostAlarmVolumeAndRouteLoudspeaker();
        if (player != null) {
            player.setVolume(1.0f);
        }
        try {
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(url));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.play();
        } catch (Exception e) {
            Log.e(TAG, "Error playing audio: " + e.getMessage(), e);
            stopForegroundAndSelf();
        }
    }

    public void togglePlayPause() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.play();
            }
        }
    }

    public void stopAudio() {
        if (player != null) {
            try {
                player.stop();
            } catch (Exception ignored) {}
        }
        stopForegroundAndSelf();
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    private void stopForegroundAndSelf() {
        releaseWakeLock();
        if (player != null) {
            try {
                player.stop();
            } catch (Exception ignored) {}
        }
        try {
            stopForeground(true);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.cancel(this.currentNotificationId);
                nm.cancel(NOTIFICATION_ID_ADHAN_SERVICE);
            }
        } catch (Exception ignored) {}
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseWakeLock();
        if (player != null) {
            try {
                player.release();
            } catch (Exception ignored) {}
            player = null;
        }
        if (instance == this) {
            instance = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
