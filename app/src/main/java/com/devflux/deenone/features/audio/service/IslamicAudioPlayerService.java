package com.devflux.deenone.features.audio.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.audio.GlobalAudioCoordinator;
import com.devflux.deenone.features.audio.model.IslamicAudioItem;

import java.io.File;

/**
 * Foreground media service for fast CDN Islamic audio playback using Media3 ExoPlayer.
 * Features:
 *   - Super-fast CDN buffer configuration
 *   - Offline local file playback
 *   - Live buffering & progress state listener
 *   - Persistent media notification with playback controls
 *   - Playback speed & Sleep timer
 */
public class IslamicAudioPlayerService extends Service {

    private static final String TAG = "IslamicAudioService";
    private static final String CHANNEL_ID = "deanone_audio_channel";
    private static final int NOTIF_ID = 9001;

    public static final String ACTION_PLAY = "com.devflux.deenone.audio.PLAY";
    public static final String ACTION_PAUSE = "com.devflux.deenone.audio.PAUSE";
    public static final String ACTION_STOP = "com.devflux.deenone.audio.STOP";
    public static final String ACTION_SEEK = "com.devflux.deenone.audio.SEEK";
    public static final String EXTRA_STREAM_URL = "extra_stream_url";
    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_SPEAKER = "extra_speaker";
    public static final String EXTRA_SEEK_MS = "extra_seek_ms";

    private static volatile IslamicAudioPlayerService singletonInstance;
    private ExoPlayer player;
    private final IBinder binder = new AudioBinder();
    private String currentTitle = "";
    private String currentSpeaker = "";
    private IslamicAudioItem currentAudioItem;
    private PlaybackStateListener stateListener;
    private Context appContext;
    private CountDownTimer sleepTimer;

    public class AudioBinder extends Binder {
        public IslamicAudioPlayerService getService() {
            return IslamicAudioPlayerService.this;
        }
    }

    public static IslamicAudioPlayerService getInstance() {
        if (singletonInstance == null) {
            synchronized (IslamicAudioPlayerService.class) {
                if (singletonInstance == null) {
                    singletonInstance = new IslamicAudioPlayerService();
                }
            }
        }
        return singletonInstance;
    }

    public static void startPlayback(Context context, IslamicAudioItem item) {
        if (context == null || item == null) return;
        Intent intent = new Intent(context, IslamicAudioPlayerService.class);
        intent.setAction(ACTION_PLAY);
        intent.putExtra(EXTRA_STREAM_URL, item.getEffectivePlayUrl());
        intent.putExtra(EXTRA_TITLE, item.getTitle());
        intent.putExtra(EXTRA_SPEAKER, item.getSpeaker());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        if (singletonInstance != null) {
            singletonInstance.currentAudioItem = item;
            singletonInstance.currentTitle = item.getTitle() != null ? item.getTitle() : "";
            singletonInstance.currentSpeaker = item.getSpeaker() != null ? item.getSpeaker() : "";
            singletonInstance.playUrl(item.getEffectivePlayUrl());
        }
    }

    public static void pausePlayback(Context context) {
        if (singletonInstance != null) {
            singletonInstance.pause();
        } else if (context != null) {
            Intent intent = new Intent(context, IslamicAudioPlayerService.class);
            intent.setAction(ACTION_PAUSE);
            context.startService(intent);
        }
    }

    public static void stopPlayback(Context context) {
        if (singletonInstance != null) {
            singletonInstance.stopPlayback();
        } else if (context != null) {
            Intent intent = new Intent(context, IslamicAudioPlayerService.class);
            intent.setAction(ACTION_STOP);
            context.startService(intent);
        }
    }

    public static void seekToPosition(Context context, long positionMs) {
        if (singletonInstance != null) {
            singletonInstance.seekTo(positionMs);
        } else if (context != null) {
            Intent intent = new Intent(context, IslamicAudioPlayerService.class);
            intent.setAction(ACTION_SEEK);
            intent.putExtra(EXTRA_SEEK_MS, positionMs);
            context.startService(intent);
        }
    }

    public void init(Context context) {
        this.appContext = context.getApplicationContext();
        if (player == null) {
            buildFastPlayer();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singletonInstance = this;
        this.appContext = getApplicationContext();
        createNotificationChannel();
        buildFastPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;
        String action = intent.getAction();
        if (ACTION_PLAY.equals(action)) {
            String url = intent.getStringExtra(EXTRA_STREAM_URL);
            currentTitle = intent.getStringExtra(EXTRA_TITLE) != null ? intent.getStringExtra(EXTRA_TITLE) : "";
            currentSpeaker = intent.getStringExtra(EXTRA_SPEAKER) != null ? intent.getStringExtra(EXTRA_SPEAKER) : "";
            if (currentAudioItem == null || url == null || !url.equals(currentAudioItem.getEffectivePlayUrl())) {
                IslamicAudioItem item = new IslamicAudioItem();
                item.setId("track_" + System.currentTimeMillis());
                item.setTitle(currentTitle);
                item.setSpeaker(currentSpeaker);
                item.setStreamUrl(url != null ? url : "");
                currentAudioItem = item;
            }
            if (url != null && !url.isEmpty()) {
                playUrl(url);
            }
        } else if (ACTION_PAUSE.equals(action)) {
            pause();
        } else if (ACTION_STOP.equals(action)) {
            stopPlayback();
        } else if (ACTION_SEEK.equals(action)) {
            long seekMs = intent.getLongExtra(EXTRA_SEEK_MS, 0L);
            seekTo(seekMs);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelSleepTimer();
        if (player != null) {
            player.release();
            player = null;
        }
        if (singletonInstance == this) {
            singletonInstance = null;
        }
    }

    private void buildFastPlayer() {
        if (player != null) return;
        Context ctx = appContext != null ? appContext : this;
        DefaultLoadControl loadControl = new DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                        2000,
                        30000,
                        500,
                        1000
                )
                .setPrioritizeTimeOverSizeThresholds(true)
                .build();

        DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)
                .setConnectTimeoutMs(8000)
                .setReadTimeoutMs(15000)
                .setUserAgent("DeenOne-AudioEngine/2.0");

        androidx.media3.datasource.DefaultDataSource.Factory dataSourceFactory =
                new androidx.media3.datasource.DefaultDataSource.Factory(ctx, httpDataSourceFactory);

        player = new ExoPlayer.Builder(ctx)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(dataSourceFactory))
                .setLoadControl(loadControl)
                .build();

        player.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (stateListener != null) stateListener.onPlayingChanged(isPlaying);
                updateNotification();
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                boolean isBuffering = (playbackState == Player.STATE_BUFFERING);
                if (stateListener != null) {
                    stateListener.onBufferingChanged(isBuffering);
                    stateListener.onStateChanged(playbackState);
                }
                if (playbackState == Player.STATE_ENDED) {
                    updateNotification();
                }
            }

            @Override
            public void onPlayerError(androidx.media3.common.PlaybackException error) {
                Log.e(TAG, "ExoPlayer playback error: " + error.getMessage(), error);
                if (stateListener != null) {
                    stateListener.onBufferingChanged(false);
                    stateListener.onPlayingChanged(false);
                }
            }
        });
    }

    public void playItem(IslamicAudioItem item) {
        this.currentAudioItem = item;
        this.currentTitle = item.getTitle();
        this.currentSpeaker = item.getSpeaker();
        playUrl(item.getEffectivePlayUrl());
    }

    public void playUrl(String url) {
        Context ctx = appContext != null ? appContext : this;
        GlobalAudioCoordinator.getInstance().requestPlayback(ctx, GlobalAudioCoordinator.AudioSource.ISLAMIC_AUDIO_HUB);

        if (player == null) buildFastPlayer();
        Uri uri;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            uri = Uri.parse(url);
        } else {
            uri = Uri.fromFile(new File(url));
        }

        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();
        player.setPlayWhenReady(true);
        try {
            startForeground(NOTIF_ID, buildNotification(true));
        } catch (Exception ignored) {}
    }

    public void pause() {
        if (player != null) {
            player.setPlayWhenReady(false);
            updateNotification();
        }
    }

    public void resume() {
        if (player != null) {
            Context ctx = appContext != null ? appContext : this;
            GlobalAudioCoordinator.getInstance().requestPlayback(ctx, GlobalAudioCoordinator.AudioSource.ISLAMIC_AUDIO_HUB);
            player.setPlayWhenReady(true);
            updateNotification();
        }
    }

    public void seekTo(int seconds) {
        if (player != null) player.seekTo((long) seconds * 1000L);
    }

    public void seekTo(long positionMs) {
        if (player != null) player.seekTo(positionMs);
    }

    public void setPlaybackSpeed(float speed) {
        if (player != null) {
            player.setPlaybackParameters(new PlaybackParameters(speed));
        }
    }

    public void setSleepTimer(int minutes) {
        cancelSleepTimer();
        if (minutes <= 0) return;
        sleepTimer = new CountDownTimer((long) minutes * 60 * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                pause();
            }
        }.start();
    }

    public void cancelSleepTimer() {
        if (sleepTimer != null) {
            sleepTimer.cancel();
            sleepTimer = null;
        }
    }

    public void stopPlayback() {
        cancelSleepTimer();
        if (player != null) {
            player.stop();
        }
        GlobalAudioCoordinator.getInstance().onSourceStopped(GlobalAudioCoordinator.AudioSource.ISLAMIC_AUDIO_HUB);
        try {
            stopForeground(STOP_FOREGROUND_REMOVE);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.cancel(NOTIF_ID);
            stopSelf();
        } catch (Exception ignored) {}
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public boolean isBuffering() {
        return player != null && player.getPlaybackState() == Player.STATE_BUFFERING;
    }

    public boolean isLoading() {
        return player != null && player.getPlaybackState() == Player.STATE_BUFFERING;
    }

    public int getCurrentPositionSeconds() {
        return player != null ? (int) (player.getCurrentPosition() / 1000) : 0;
    }

    public int getDurationSeconds() {
        return player != null && player.getDuration() > 0 ? (int) (player.getDuration() / 1000) : (currentAudioItem != null ? currentAudioItem.getDurationSeconds() : 0);
    }

    public IslamicAudioItem getCurrentAudioItem() {
        return currentAudioItem;
    }

    public void setPlaybackStateListener(PlaybackStateListener listener) {
        this.stateListener = listener;
    }

    // =========================================================================
    // Foreground Notification
    // =========================================================================
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "ইসলামিক অডিও হাব", NotificationManager.IMPORTANCE_LOW
            );
            ch.setDescription("ইসলামিক অডিও ব্যাকগ্রাউন্ড প্লেয়ার");
            Context ctx = appContext != null ? appContext : this;
            NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.createNotificationChannel(ch);
        }
    }

    private Notification buildNotification(boolean playing) {
        Context ctx = appContext != null ? appContext : this;
        Intent stopIntent = new Intent(ctx, IslamicAudioPlayerService.class).setAction(ACTION_STOP);
        PendingIntent stopPi = PendingIntent.getService(ctx, 0, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

        Intent pauseIntent = new Intent(ctx, IslamicAudioPlayerService.class).setAction(ACTION_PAUSE);
        PendingIntent pausePi = PendingIntent.getService(ctx, 1, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

        Intent playIntent = new Intent(ctx, IslamicAudioPlayerService.class).setAction(ACTION_PLAY);
        PendingIntent playPi = PendingIntent.getService(ctx, 2, playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

        Intent mainIntent = new Intent(ctx, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.putExtra("extra_deep_link", "feature_audio_hub");
        PendingIntent contentPi = PendingIntent.getActivity(ctx, 3, mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

        NotificationCompat.Builder nb = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_deenone)
                .setContentTitle(currentTitle.isEmpty() ? "ইসলামিক অডিও হাব" : currentTitle)
                .setContentText(currentSpeaker.isEmpty() ? "DeenOne Islamic Audio" : currentSpeaker)
                .setOngoing(playing)
                .setContentIntent(contentPi)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        try {
            nb.setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.ic_launcher));
        } catch (Exception ignored) {}

        nb.addAction(R.drawable.ic_stop, "Stop", stopPi);
        nb.addAction(playing ? R.drawable.ic_pause : R.drawable.ic_play_arrow, playing ? "Pause" : "Play", playing ? pausePi : playPi);
        nb.addAction(R.drawable.ic_close, "Close", stopPi);

        nb.setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
                .setShowCancelButton(true)
                .setCancelButtonIntent(stopPi));

        return nb.build();
    }

    private void updateNotification() {
        Context ctx = appContext != null ? appContext : this;
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) nm.notify(NOTIF_ID, buildNotification(isPlaying()));
    }

    public interface PlaybackStateListener {
        void onPlayingChanged(boolean playing);
        void onBufferingChanged(boolean isBuffering);
        void onStateChanged(int state);
    }
}