package com.devflux.deenone.core.quran.service;

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
import androidx.media.app.NotificationCompat.MediaStyle;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.audio.GlobalAudioCoordinator;
import com.devflux.deenone.core.quran.QuranSleepManager;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * QuranSleepService — Foreground Media Service for Background Quran Sleep Mode Playback.
 * Runs continuously when screen is locked or app is closed.
 * Plays sequential playlist queue with automatic looping until countdown timer completes.
 */
public class QuranSleepService extends Service {

  private static final String TAG = "QuranSleepService";
  public static final String NOTIF_CHANNEL_ID = "channel_quran_sleep_mode";
  public static final int NOTIF_ID = 9902;

  public static final String ACTION_PLAY_QUEUE = "com.devflux.deenone.quran.sleep.PLAY_QUEUE";
  public static final String ACTION_UPDATE_QUEUE = "com.devflux.deenone.quran.sleep.UPDATE_QUEUE";
  public static final String ACTION_PLAY_SURAH = "com.devflux.deenone.quran.sleep.PLAY_SURAH";
  public static final String ACTION_PAUSE = "com.devflux.deenone.quran.sleep.PAUSE";
  public static final String ACTION_RESUME = "com.devflux.deenone.quran.sleep.RESUME";
  public static final String ACTION_STOP = "com.devflux.deenone.quran.sleep.STOP";
  public static final String ACTION_NEXT = "com.devflux.deenone.quran.sleep.NEXT";
  public static final String ACTION_PREV = "com.devflux.deenone.quran.sleep.PREV";
  public static final String ACTION_CHANGE_SURAH = "com.devflux.deenone.quran.sleep.CHANGE_SURAH";
  public static final String ACTION_SET_TIMER = "com.devflux.deenone.quran.sleep.SET_TIMER";

  public static final String EXTRA_SURAH_QUEUE = "extra_surah_queue";
  public static final String EXTRA_SURAH_NUMBER = "extra_surah_number";
  public static final String EXTRA_TIMER_MINUTES = "extra_timer_minutes";

  private static volatile QuranSleepService singletonInstance;
  private final IBinder binder = new SleepBinder();

  private ExoPlayer exoPlayer;
  private CountDownTimer countDownTimer;
  private ArrayList<Integer> surahQueue = new ArrayList<>();
  private int currentQueueIndex = 0;
  private int timerMinutes = 15;
  private long remainingMillis = 15 * 60 * 1000L;
  private boolean isTimerRunning = false;
  private List<QuranSurahEntity> surahList;

  public class SleepBinder extends Binder {
    public QuranSleepService getService() {
      return QuranSleepService.this;
    }
  }

  public static QuranSleepService getInstance() {
    return singletonInstance;
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
    surahList = QuranSurahDataSeeder.get114Surahs();
    createNotificationChannel();
    buildExoPlayer();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent == null) return START_NOT_STICKY;

    String action = intent.getAction();
    if (action != null) {
      switch (action) {
        case ACTION_PLAY_QUEUE:
          ArrayList<Integer> queue = intent.getIntegerArrayListExtra(EXTRA_SURAH_QUEUE);
          int minutes = intent.getIntExtra(EXTRA_TIMER_MINUTES, timerMinutes);
          startPlayQueue(queue, minutes);
          break;
        case ACTION_UPDATE_QUEUE:
          ArrayList<Integer> updatedQueue = intent.getIntegerArrayListExtra(EXTRA_SURAH_QUEUE);
          if (updatedQueue != null && !updatedQueue.isEmpty()) {
            this.surahQueue = updatedQueue;
            if (currentQueueIndex >= surahQueue.size()) {
              currentQueueIndex = 0;
            }
            updateNotification();
          }
          break;
        case ACTION_PLAY_SURAH:
          int surahNum = intent.getIntExtra(EXTRA_SURAH_NUMBER, 67);
          int mins = intent.getIntExtra(EXTRA_TIMER_MINUTES, timerMinutes);
          ArrayList<Integer> singleQueue = new ArrayList<>();
          singleQueue.add(surahNum);
          startPlayQueue(singleQueue, mins);
          break;
        case ACTION_PAUSE:
          pausePlayback();
          break;
        case ACTION_RESUME:
          resumePlayback();
          break;
        case ACTION_STOP:
          stopPlaybackAndClose();
          break;
        case ACTION_NEXT:
          playNextInQueue();
          break;
        case ACTION_PREV:
          playPrevInQueue();
          break;
        case ACTION_CHANGE_SURAH:
          int targetSurah = intent.getIntExtra(EXTRA_SURAH_NUMBER, 67);
          int idx = surahQueue.indexOf(targetSurah);
          if (idx != -1) {
            currentQueueIndex = idx;
          } else {
            surahQueue.add(targetSurah);
            currentQueueIndex = surahQueue.size() - 1;
          }
          playCurrentQueueIndex();
          break;
        case ACTION_SET_TIMER:
          int newMins = intent.getIntExtra(EXTRA_TIMER_MINUTES, timerMinutes);
          updateTimerDuration(newMins);
          break;
      }
    }

    return START_NOT_STICKY;
  }

  private void buildExoPlayer() {
    if (exoPlayer != null) return;

    AudioAttributes audioAttributes = new AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build();

    DefaultLoadControl loadControl = new DefaultLoadControl.Builder()
        .setBufferDurationsMs(2000, 30000, 500, 1000)
        .setPrioritizeTimeOverSizeThresholds(true)
        .build();

    DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
        .setAllowCrossProtocolRedirects(true)
        .setConnectTimeoutMs(8000)
        .setReadTimeoutMs(15000)
        .setUserAgent("DeenOne-QuranSleep/2.0");

    androidx.media3.datasource.DefaultDataSource.Factory dataSourceFactory =
        new androidx.media3.datasource.DefaultDataSource.Factory(this, httpDataSourceFactory);

    DefaultMediaSourceFactory mediaSourceFactory =
        new DefaultMediaSourceFactory(dataSourceFactory);

    exoPlayer = new ExoPlayer.Builder(this)
        .setAudioAttributes(audioAttributes, true)
        .setLoadControl(loadControl)
        .setMediaSourceFactory(mediaSourceFactory)
        .setHandleAudioBecomingNoisy(true)
        .setWakeMode(C.WAKE_MODE_NETWORK)
        .build();

    exoPlayer.addListener(new Player.Listener() {
      @Override
      public void onIsPlayingChanged(boolean isPlaying) {
        if (isPlaying) {
          if (!isTimerRunning && remainingMillis > 0) {
            startCountdownTimer();
          }
        } else {
          if (isTimerRunning) {
            stopTimer();
          }
        }
        QuranSleepManager.getInstance().notifyPlayingState(isPlaying);
        updateNotification();
      }

      @Override
      public void onPlaybackStateChanged(int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
          QuranSleepManager.getInstance().notifyBuffering(true);
        } else if (playbackState == Player.STATE_READY) {
          QuranSleepManager.getInstance().notifyBuffering(false);
        } else if (playbackState == Player.STATE_ENDED) {
          // Auto advance to next surah in queue seamlessly!
          playNextInQueue();
        }
        updateNotification();
      }

      @Override
      public void onPlayerError(PlaybackException error) {
        int currentSurah = getCurrentSurah();
        Log.w(TAG, "Primary stream error, falling back to secondary CDN for surah " + currentSurah + ": " + error.getMessage());
        playFallbackCdn(currentSurah);
      }
    });
  }

  public void startPlayQueue(ArrayList<Integer> queue, int minutes) {
    GlobalAudioCoordinator.getInstance().requestPlayback(this, GlobalAudioCoordinator.AudioSource.QURAN_SLEEP_MODE);

    if (queue == null || queue.isEmpty()) {
      this.surahQueue = new ArrayList<>();
      this.surahQueue.add(67); // Default Surah Mulk
    } else {
      this.surahQueue = new ArrayList<>(queue);
    }
    this.currentQueueIndex = 0;
    this.timerMinutes = minutes;
    this.remainingMillis = minutes * 60 * 1000L;

    if (exoPlayer == null) buildExoPlayer();

    playCurrentQueueIndex();
    startForeground(NOTIF_ID, buildNotification(true));
    startCountdownTimer();
  }

  private int getCurrentSurah() {
    if (surahQueue != null && !surahQueue.isEmpty() && currentQueueIndex < surahQueue.size()) {
      return surahQueue.get(currentQueueIndex);
    }
    return 67;
  }

  private void playCurrentQueueIndex() {
    int surahNumber = getCurrentSurah();
    if (exoPlayer == null) buildExoPlayer();

    Uri playbackUri = com.devflux.deenone.core.quran.QuranAudioCacheManager.getFullSurahPlaybackUri(this, surahNumber);
    com.devflux.deenone.core.quran.QuranAudioCacheManager.cacheFullSurahAudioInBackground(this, surahNumber);

    try {
      exoPlayer.setMediaItem(MediaItem.fromUri(playbackUri));
      exoPlayer.prepare();
      exoPlayer.setPlayWhenReady(true);
      updateNotification();

      QuranSleepManager.getInstance().notifySurahChanged(surahNumber, currentQueueIndex, timerMinutes);
    } catch (Exception e) {
      Log.e(TAG, "Error starting playback: " + e.getMessage());
    }
  }

  private void playFallbackCdn(int surahNumber) {
    if (exoPlayer == null) return;
    String surahPadded = String.format(Locale.US, "%03d", surahNumber);
    String secondaryCdn = "https://server8.mp3quran.net/afs/" + surahPadded + ".mp3";
    try {
      exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(secondaryCdn)));
      exoPlayer.prepare();
      exoPlayer.setPlayWhenReady(true);
    } catch (Exception ex) {
      Log.e(TAG, "Fallback CDN error: " + ex.getMessage());
    }
  }

  public void pausePlayback() {
    stopTimer();
    if (exoPlayer != null) {
      exoPlayer.setPlayWhenReady(false);
      updateNotification();
      QuranSleepManager.getInstance().notifyPlayingState(false);
    }
  }

  public void resumePlayback() {
    if (exoPlayer != null) {
      GlobalAudioCoordinator.getInstance().requestPlayback(this, GlobalAudioCoordinator.AudioSource.QURAN_SLEEP_MODE);
      exoPlayer.setPlayWhenReady(true);
      if (remainingMillis > 0 && !isTimerRunning) {
        startCountdownTimer();
      }
      updateNotification();
      QuranSleepManager.getInstance().notifyPlayingState(true);
    }
  }

  public void playNextInQueue() {
    if (surahQueue == null || surahQueue.isEmpty()) return;
    currentQueueIndex = (currentQueueIndex + 1) % surahQueue.size();
    playCurrentQueueIndex();
  }

  public void playPrevInQueue() {
    if (surahQueue == null || surahQueue.isEmpty()) return;
    currentQueueIndex = (currentQueueIndex - 1 + surahQueue.size()) % surahQueue.size();
    playCurrentQueueIndex();
  }

  public void updateTimerDuration(int minutes) {
    this.timerMinutes = minutes;
    this.remainingMillis = minutes * 60 * 1000L;
    if (isTimerRunning) {
      startCountdownTimer();
    } else {
      stopTimer();
      long totalSecs = (long) minutes * 60L;
      String formatted = String.format(Locale.US, "%02d:00", minutes);
      QuranSleepManager.getInstance().notifyTimerTick(totalSecs, formatted);
    }
    updateNotification();
  }

  public void stopPlaybackAndClose() {
    Log.d(TAG, "Stopping Quran Sleep Playback completely");
    stopTimer();

    if (exoPlayer != null) {
      exoPlayer.stop();
      exoPlayer.release();
      exoPlayer = null;
    }

    QuranSleepManager.getInstance().notifyStopped();
    stopForeground(true);
    stopSelf();
  }

  private void startCountdownTimer() {
    stopTimer();
    isTimerRunning = true;
    countDownTimer = new CountDownTimer(remainingMillis, 1000) {
      @Override
      public void onTick(long millisUntilFinished) {
        remainingMillis = millisUntilFinished;
        long totalSecs = millisUntilFinished / 1000;
        long mins = totalSecs / 60;
        long secs = totalSecs % 60;
        String formatted = String.format(Locale.US, "%02d:%02d", mins, secs);

        QuranSleepManager.getInstance().notifyTimerTick(totalSecs, formatted);
        if (totalSecs % 30 == 0) {
          updateNotification();
        }
      }

      @Override
      public void onFinish() {
        remainingMillis = 0;
        isTimerRunning = false;
        QuranSleepManager.getInstance().notifyTimerTick(0, "00:00");
        stopPlaybackAndClose();
      }
    }.start();
  }

  private void stopTimer() {
    if (countDownTimer != null) {
      countDownTimer.cancel();
      countDownTimer = null;
    }
    isTimerRunning = false;
  }

  private boolean isPlaying() {
    return exoPlayer != null && exoPlayer.isPlaying();
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
          NOTIF_CHANNEL_ID,
          "Quran Sleep Mode",
          NotificationManager.IMPORTANCE_LOW
      );
      channel.setDescription("Shows background playing Quran Sleep Mode with countdown timer and controls");
      channel.setShowBadge(false);
      channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

      NotificationManager manager = getSystemService(NotificationManager.class);
      if (manager != null) {
        manager.createNotificationChannel(channel);
      }
    }
  }

  private Notification buildNotification(boolean isPlaying) {
    int curSurahNum = getCurrentSurah();
    QuranSurahEntity cur = getSurah(curSurahNum);

    String timerFormatted = QuranSleepManager.getInstance().getFormattedTimerLiveData().getValue();
    if (timerFormatted == null) {
      long totalSecs = remainingMillis / 1000;
      timerFormatted = String.format(Locale.US, "%02d:%02d", totalSecs / 60, totalSecs % 60);
    }

    String title = "🌙 স্লিপ মোড: সূরা " + cur.getNameBengali() + " (" + cur.getNameArabic() + ")";
    String contentText = "টাইমার বাকি: " + timerFormatted;

    Intent openIntent = new Intent(this, MainActivity.class);
    openIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent openPi = PendingIntent.getActivity(
        this, 0, openIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
    );

    Intent prevIntent = new Intent(this, QuranSleepService.class).setAction(ACTION_PREV);
    PendingIntent prevPi = PendingIntent.getService(this, 1, prevIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

    Intent toggleIntent = new Intent(this, QuranSleepService.class).setAction(isPlaying ? ACTION_PAUSE : ACTION_RESUME);
    PendingIntent togglePi = PendingIntent.getService(this, 2, toggleIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

    Intent nextIntent = new Intent(this, QuranSleepService.class).setAction(ACTION_NEXT);
    PendingIntent nextPi = PendingIntent.getService(this, 3, nextIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

    Intent stopIntent = new Intent(this, QuranSleepService.class).setAction(ACTION_STOP);
    PendingIntent stopPi = PendingIntent.getService(this, 4, stopIntent,
        PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0));

    int playPauseIcon = isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
    String playPauseTitle = isPlaying ? "Pause" : "Play";

    return new NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_moon)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_moon))
        .setContentTitle(title)
        .setContentText(contentText)
        .setSubText("অবশিষ্ট " + timerFormatted)
        .setContentIntent(openPi)
        .setOngoing(isPlaying)
        .setSilent(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .addAction(android.R.drawable.ic_media_previous, "Previous", prevPi)
        .addAction(playPauseIcon, playPauseTitle, togglePi)
        .addAction(android.R.drawable.ic_media_next, "Next", nextPi)
        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Close", stopPi)
        .setStyle(new MediaStyle()
            .setShowActionsInCompactView(0, 1, 2)
            .setShowCancelButton(true)
            .setCancelButtonIntent(stopPi))
        .build();
  }

  private void updateNotification() {
    NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    if (nm != null) {
      nm.notify(NOTIF_ID, buildNotification(isPlaying()));
    }
  }

  private QuranSurahEntity getSurah(int surahNumber) {
    if (surahList != null) {
      for (QuranSurahEntity s : surahList) {
        if (s.getNumber() == surahNumber) return s;
      }
      if (!surahList.isEmpty()) return surahList.get(0);
    }
    return new QuranSurahEntity(1, "الفاتحة", "আল-ফাতিহা", "Al-Fatihah", "সূচনা", "The Opening", 7, "মাক্কী", 1);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    stopTimer();
    if (exoPlayer != null) {
      exoPlayer.release();
      exoPlayer = null;
    }
    if (singletonInstance == this) {
      singletonInstance = null;
    }
  }

  public static void startPlaybackQueue(Context context, ArrayList<Integer> queue, int timerMinutes) {
    if (context == null) return;
    Intent intent = new Intent(context, QuranSleepService.class);
    intent.setAction(ACTION_PLAY_QUEUE);
    intent.putIntegerArrayListExtra(EXTRA_SURAH_QUEUE, queue);
    intent.putExtra(EXTRA_TIMER_MINUTES, timerMinutes);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(intent);
    } else {
      context.startService(intent);
    }
  }

  public static void updateQueueDirect(Context context, ArrayList<Integer> queue) {
    if (context == null) return;
    Intent intent = new Intent(context, QuranSleepService.class);
    intent.setAction(ACTION_UPDATE_QUEUE);
    intent.putIntegerArrayListExtra(EXTRA_SURAH_QUEUE, queue);
    context.startService(intent);
  }

  public static void setTimerDuration(Context context, int timerMinutes) {
    if (context == null) return;
    Intent intent = new Intent(context, QuranSleepService.class);
    intent.setAction(ACTION_SET_TIMER);
    intent.putExtra(EXTRA_TIMER_MINUTES, timerMinutes);
    context.startService(intent);
  }

  public static void pausePlayback(Context context) {
    if (context == null) return;
    Intent intent = new Intent(context, QuranSleepService.class);
    intent.setAction(ACTION_PAUSE);
    context.startService(intent);
  }

  public static void resumePlayback(Context context) {
    if (context == null) return;
    Intent intent = new Intent(context, QuranSleepService.class);
    intent.setAction(ACTION_RESUME);
    context.startService(intent);
  }

  public static void stopPlayback(Context context) {
    if (context == null) return;
    Intent intent = new Intent(context, QuranSleepService.class);
    intent.setAction(ACTION_STOP);
    context.startService(intent);
  }
}
