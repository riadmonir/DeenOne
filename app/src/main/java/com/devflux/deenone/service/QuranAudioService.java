package com.devflux.deenone.service;

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
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.app.NotificationCompat.MediaStyle;
import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.PlaybackParameters;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;

import com.devflux.deenone.MainActivity;
import com.devflux.deenone.R;
import com.devflux.deenone.core.audio.GlobalAudioCoordinator;
import com.devflux.deenone.core.quran.QuranCdnAudioHelper;
import com.devflux.deenone.core.quran.QuranSettingsManager;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.File;

/**
 * QuranAudioService — Authoritative Media Service for Ayah-by-Ayah and Full Surah Playback.
 * Features:
 *  - High performance Media3 ExoPlayer with worldwide CDN fast-buffer.
 *  - Auto-advance to Next Ayah on playback finish with full Repeat mode support (Off, Ayah, Surah, All).
 *  - Playback speed control (0.75x, 1.0x, 1.25x, 1.5x, 2.0x).
 *  - Live Seekbar progress tracking & timestamp broadcasting.
 *  - Android MediaStyle Notification Bar & Lock Screen widget with Full Media Controls.
 *  - Single-Stream exclusivity via GlobalAudioCoordinator.
 */
public class QuranAudioService extends Service {

    public static final String NOTIF_CHANNEL_ID = "quran_audio_playback_channel_v2";
    public static final int NOTIF_ID = 9101;

    public static final String ACTION_PLAY_AYAH = "com.devflux.deenone.quran.PLAY_AYAH";
    public static final String ACTION_PLAY_FULL_SURAH = "com.devflux.deenone.quran.PLAY_FULL_SURAH";
    public static final String ACTION_PLAY_BANGLA_TRANSLATION_SURAH = "com.devflux.deenone.quran.PLAY_BANGLA_TRANSLATION_SURAH";
    public static final String ACTION_PAUSE = "com.devflux.deenone.quran.PAUSE";
    public static final String ACTION_RESUME = "com.devflux.deenone.quran.RESUME";
    public static final String ACTION_TOGGLE = "com.devflux.deenone.quran.TOGGLE";
    public static final String ACTION_STOP = "com.devflux.deenone.quran.STOP";
    public static final String ACTION_NEXT_AYAH = "com.devflux.deenone.quran.NEXT_AYAH";
    public static final String ACTION_PREV_AYAH = "com.devflux.deenone.quran.PREV_AYAH";

    public static final String EXTRA_SURAH_NUMBER = "extra_surah_number";
    public static final String EXTRA_AYAH_NUMBER = "extra_ayah_number";
    public static final String EXTRA_TOTAL_AYAHS = "extra_total_ayahs";
    public static final String EXTRA_SURAH_NAME_EN = "extra_surah_name_en";
    public static final String EXTRA_SURAH_NAME_BN = "extra_surah_name_bn";
    public static final String EXTRA_RECITER_INDEX = "extra_reciter_index";

    public interface OnPlaybackEventListener {
        void onAyahChanged(int surahNumber, int ayahNumber, String surahNameBn, boolean isPlaying);
        void onPlaybackStateChanged(boolean isPlaying);
        void onPlaybackStopped();
        void onProgressUpdate(long currentPositionMs, long durationMs);
    }

    private static volatile QuranAudioService singletonInstance;
    private static OnPlaybackEventListener playbackListener;

    private ExoPlayer exoPlayer;
    private final IBinder binder = new QuranAudioBinder();
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private boolean isProgressTrackingActive = false;

    private int currentSurahNumber = 1;
    private int currentAyahNumber = 1;
    private int totalAyahsInSurah = 7;
    private String currentSurahNameEn = "Al-Fatiha";
    private String currentSurahNameBn = "আল ফাতিহা";
    private QuranCdnAudioHelper.Reciter currentReciter = QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
    private boolean isFullSurahMode = false;
    private boolean isBanglaTranslationMode = false;

    private final Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (exoPlayer != null && exoPlayer.isPlaying()) {
                long cur = Math.max(0, exoPlayer.getCurrentPosition());
                long dur = Math.max(0, exoPlayer.getDuration());
                if (playbackListener != null) {
                    playbackListener.onProgressUpdate(cur, dur);
                }
                progressHandler.postDelayed(this, 250);
            } else {
                isProgressTrackingActive = false;
            }
        }
    };

    public class QuranAudioBinder extends Binder {
        public QuranAudioService getService() {
            return QuranAudioService.this;
        }
    }

    public static QuranAudioService getInstance() {
        return singletonInstance;
    }

    public static void setPlaybackEventListener(OnPlaybackEventListener listener) {
        playbackListener = listener;
        if (singletonInstance != null && listener != null) {
            listener.onAyahChanged(
                    singletonInstance.currentSurahNumber,
                    singletonInstance.currentAyahNumber,
                    singletonInstance.currentSurahNameBn,
                    singletonInstance.isPlaying()
            );
            if (singletonInstance.exoPlayer != null) {
                listener.onProgressUpdate(
                        Math.max(0, singletonInstance.exoPlayer.getCurrentPosition()),
                        Math.max(0, singletonInstance.exoPlayer.getDuration())
                );
            }
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
        createNotificationChannel();
        buildExoPlayer();
    }

    private void buildExoPlayer() {
        DefaultHttpDataSource.Factory httpDataSourceFactory = new DefaultHttpDataSource.Factory()
                .setUserAgent("DeenOne-QuranAudio/2.0")
                .setConnectTimeoutMs(15000)
                .setReadTimeoutMs(20000)
                .setAllowCrossProtocolRedirects(true);

        DefaultDataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(this, httpDataSourceFactory);
        DefaultMediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(dataSourceFactory);

        DefaultLoadControl loadControl = new DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                        1000,   // Min buffer 1s (super fast startup)
                        15000,  // Max buffer 15s
                        500,    // Buffer for playback start 500ms
                        1000    // Buffer for rebuffer 1s
                )
                .build();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build();

        exoPlayer = new ExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .setLoadControl(loadControl)
                .setAudioAttributes(audioAttributes, true)
                .setHandleAudioBecomingNoisy(true)
                .build();

        applyPlaybackSpeed();

        exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_ENDED) {
                    handlePlaybackEnded();
                }
                updateNotification();
                notifyStateChanged();
                checkProgressTracking();
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                updateNotification();
                notifyStateChanged();
                checkProgressTracking();
            }

            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                android.util.Log.w("QuranAudioService", "ExoPlayer error: " + error.getMessage() + ", code=" + error.errorCode);
                if (!isFullSurahMode) {
                    tryFallbackAyahCdn();
                }
            }
        });
    }

    private void checkProgressTracking() {
        if (isPlaying()) {
            if (!isProgressTrackingActive) {
                isProgressTrackingActive = true;
                progressHandler.removeCallbacks(progressRunnable);
                progressHandler.post(progressRunnable);
            }
        } else {
            isProgressTrackingActive = false;
            progressHandler.removeCallbacks(progressRunnable);
        }
    }

    public void setPlaybackSpeed(float speed) {
        QuranSettingsManager.getInstance(this).setPlaybackSpeed(speed);
        applyPlaybackSpeed();
    }

    public void applyPlaybackSpeed() {
        if (exoPlayer != null) {
            float speed = QuranSettingsManager.getPlaybackSpeed(this);
            exoPlayer.setPlaybackParameters(new PlaybackParameters(speed));
        }
    }

    public void seekTo(long positionMs) {
        if (exoPlayer != null) {
            exoPlayer.seekTo(positionMs);
        }
    }

    public long getCurrentPosition() {
        return exoPlayer != null ? exoPlayer.getCurrentPosition() : 0;
    }

    public long getDuration() {
        return exoPlayer != null ? exoPlayer.getDuration() : 0;
    }

    private void handlePlaybackEnded() {
        if (isBanglaTranslationMode) {
            int repeatMode = QuranSettingsManager.getRepeatMode(this);
            if (repeatMode == QuranSettingsManager.REPEAT_SURAH || repeatMode == QuranSettingsManager.REPEAT_AYAH) {
                playBanglaTranslationSurah(currentSurahNumber, currentSurahNameEn, currentSurahNameBn);
                return;
            } else if (repeatMode == QuranSettingsManager.REPEAT_ALL) {
                int nextSurah = (currentSurahNumber % 114) + 1;
                playBanglaTranslationSurah(nextSurah, getSurahEnName(nextSurah), getSurahBnName(nextSurah));
                return;
            } else {
                stopPlayback();
                return;
            }
        }

        int repeatMode = QuranSettingsManager.getRepeatMode(this);

        if (repeatMode == QuranSettingsManager.REPEAT_AYAH) {
            // Repeat current ayah continuously
            playAyah(currentSurahNumber, currentAyahNumber, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
            return;
        }

        if (repeatMode == QuranSettingsManager.REPEAT_SURAH) {
            // Repeat current Surah in loop (1 to N, then back to 1)
            if (currentAyahNumber < totalAyahsInSurah) {
                playAyah(currentSurahNumber, currentAyahNumber + 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
            } else {
                playAyah(currentSurahNumber, 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
            }
            return;
        }

        if (repeatMode == QuranSettingsManager.REPEAT_ALL) {
            // Continuous loop across Surahs 1..114
            if (currentAyahNumber < totalAyahsInSurah) {
                playAyah(currentSurahNumber, currentAyahNumber + 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
            } else {
                int nextSurah = (currentSurahNumber % 114) + 1;
                QuranSurahEntity nextSurahEntity = QuranSurahDataSeeder.getSurahByNumber(nextSurah);
                int nextTotal = nextSurahEntity != null ? nextSurahEntity.getNumberOfAyahs() : 7;
                playAyah(nextSurah, 1, nextTotal, getSurahEnName(nextSurah), getSurahBnName(nextSurah), currentReciter);
            }
            return;
        }

        // REPEAT_OFF (Default behavior)
        if (!isFullSurahMode) {
            if (currentAyahNumber < totalAyahsInSurah) {
                playAyah(currentSurahNumber, currentAyahNumber + 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
            } else {
                stopPlayback();
            }
        } else {
            stopPlayback();
        }
    }

    private void tryFallbackAyahCdn() {
        if (exoPlayer == null) return;
        File localFile = com.devflux.deenone.core.quran.QuranAudioCacheManager.getAyahAudioFile(this, currentReciter, currentSurahNumber, currentAyahNumber);
        if (localFile.exists() && localFile.length() > 500) {
            MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(localFile));
            exoPlayer.setMediaItem(mediaItem);
            applyPlaybackSpeed();
            exoPlayer.prepare();
            exoPlayer.play();
            return;
        }
        String fallbackUrl = QuranCdnAudioHelper.getAyahFallbackUrl(currentReciter, currentSurahNumber, currentAyahNumber);
        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(fallbackUrl));
        exoPlayer.setMediaItem(mediaItem);
        applyPlaybackSpeed();
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void startForegroundCompat(Notification notification) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(NOTIF_ID, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
            } else {
                startForeground(NOTIF_ID, notification);
            }
        } catch (Exception e) {
            try {
                startForeground(NOTIF_ID, notification);
            } catch (Exception ignored) {}
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;

        String action = intent.getAction();
        if (action == null) return START_NOT_STICKY;

        switch (action) {
            case ACTION_PLAY_AYAH:
                int surah = intent.getIntExtra(EXTRA_SURAH_NUMBER, 1);
                int ayah = intent.getIntExtra(EXTRA_AYAH_NUMBER, 1);
                int total = intent.getIntExtra(EXTRA_TOTAL_AYAHS, 0);
                if (total <= 0) {
                    QuranSurahEntity entity = QuranSurahDataSeeder.getSurahByNumber(surah);
                    total = entity != null ? entity.getNumberOfAyahs() : 7;
                }
                String nameEn = intent.getStringExtra(EXTRA_SURAH_NAME_EN);
                if (nameEn == null || nameEn.trim().isEmpty()) {
                    nameEn = getSurahEnName(surah);
                }
                String nameBn = intent.getStringExtra(EXTRA_SURAH_NAME_BN);
                if (nameBn == null || nameBn.trim().isEmpty()) {
                    nameBn = getSurahBnName(surah);
                }
                int reciterIdx = intent.getIntExtra(EXTRA_RECITER_INDEX, 0);
                QuranCdnAudioHelper.Reciter reciter = QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
                if (reciterIdx >= 0 && reciterIdx < QuranCdnAudioHelper.Reciter.values().length) {
                    reciter = QuranCdnAudioHelper.Reciter.values()[reciterIdx];
                }
                playAyah(surah, ayah, total, nameEn, nameBn, reciter);
                break;

            case ACTION_PLAY_FULL_SURAH:
                int fSurah = intent.getIntExtra(EXTRA_SURAH_NUMBER, 1);
                String fNameEn = intent.getStringExtra(EXTRA_SURAH_NAME_EN);
                if (fNameEn == null || fNameEn.trim().isEmpty()) {
                    fNameEn = getSurahEnName(fSurah);
                }
                String fNameBn = intent.getStringExtra(EXTRA_SURAH_NAME_BN);
                if (fNameBn == null || fNameBn.trim().isEmpty()) {
                    fNameBn = getSurahBnName(fSurah);
                }
                int fReciterIdx = intent.getIntExtra(EXTRA_RECITER_INDEX, 0);
                QuranCdnAudioHelper.Reciter fReciter = QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
                if (fReciterIdx >= 0 && fReciterIdx < QuranCdnAudioHelper.Reciter.values().length) {
                    fReciter = QuranCdnAudioHelper.Reciter.values()[fReciterIdx];
                }
                playFullSurah(fSurah, fNameEn, fNameBn, fReciter);
                break;

            case ACTION_PLAY_BANGLA_TRANSLATION_SURAH:
                int bSurah = intent.getIntExtra(EXTRA_SURAH_NUMBER, 1);
                String bNameEn = intent.getStringExtra(EXTRA_SURAH_NAME_EN);
                if (bNameEn == null || bNameEn.trim().isEmpty()) {
                    bNameEn = getSurahEnName(bSurah);
                }
                String bNameBn = intent.getStringExtra(EXTRA_SURAH_NAME_BN);
                if (bNameBn == null || bNameBn.trim().isEmpty()) {
                    bNameBn = getSurahBnName(bSurah);
                }
                playBanglaTranslationSurah(bSurah, bNameEn, bNameBn);
                break;

            case ACTION_PAUSE:
                pause();
                break;

            case ACTION_RESUME:
                resume();
                break;

            case ACTION_TOGGLE:
                togglePlayPause();
                break;

            case ACTION_NEXT_AYAH:
                playNextAyah();
                break;

            case ACTION_PREV_AYAH:
                playPreviousAyah();
                break;

            case ACTION_STOP:
                stopPlayback();
                break;
        }

        return START_STICKY;
    }

    public void playAyah(int surahNumber, int ayahNumber, int totalAyahs, String surahNameEn, String surahNameBn, QuranCdnAudioHelper.Reciter reciter) {
        this.currentSurahNumber = surahNumber;
        this.currentAyahNumber = ayahNumber;
        this.totalAyahsInSurah = totalAyahs > 0 ? totalAyahs : 7;
        this.currentSurahNameEn = (surahNameEn != null && !surahNameEn.isEmpty()) ? surahNameEn : getSurahEnName(surahNumber);
        this.currentSurahNameBn = (surahNameBn != null && !surahNameBn.isEmpty()) ? surahNameBn : getSurahBnName(surahNumber);
        this.currentReciter = reciter != null ? reciter : QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
        this.isFullSurahMode = false;
        this.isBanglaTranslationMode = false;

        // Request single stream audio exclusivity
        GlobalAudioCoordinator.getInstance().requestPlayback(GlobalAudioCoordinator.AudioSource.QURAN_RECITER);

        if (exoPlayer == null) buildExoPlayer();

        Uri audioUri = com.devflux.deenone.core.quran.QuranAudioCacheManager.getAyahPlaybackUri(this, this.currentReciter, currentSurahNumber, currentAyahNumber);
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        exoPlayer.setMediaItem(mediaItem);
        applyPlaybackSpeed();
        exoPlayer.prepare();
        exoPlayer.play();

        if (!com.devflux.deenone.core.quran.QuranAudioCacheManager.isAyahAudioDownloaded(this, this.currentReciter, currentSurahNumber, currentAyahNumber)) {
            com.devflux.deenone.core.quran.QuranAudioCacheManager.cacheAyahAudioInBackground(this, this.currentReciter, currentSurahNumber, currentAyahNumber);
        }

        com.devflux.deenone.core.quran.QuranStreakGoalManager.getInstance().recordAyahReadOrListened(this, currentSurahNumber, currentAyahNumber);

        startForegroundCompat(buildNotification(true));
        notifyAyahChanged();
        checkProgressTracking();
    }

    public void playFullSurah(int surahNumber, String surahNameEn, String surahNameBn, QuranCdnAudioHelper.Reciter reciter) {
        this.currentSurahNumber = surahNumber;
        this.currentSurahNameEn = (surahNameEn != null && !surahNameEn.isEmpty()) ? surahNameEn : getSurahEnName(surahNumber);
        this.currentSurahNameBn = (surahNameBn != null && !surahNameBn.isEmpty()) ? surahNameBn : getSurahBnName(surahNumber);
        this.currentReciter = reciter != null ? reciter : QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY;
        this.isBanglaTranslationMode = false;

        File localFullSurah = com.devflux.deenone.core.quran.QuranAudioCacheManager.getFullSurahAudioFile(this, surahNumber);
        if (localFullSurah.exists() && localFullSurah.length() > 5000) {
            this.currentAyahNumber = 1;
            this.isFullSurahMode = true;
            GlobalAudioCoordinator.getInstance().requestPlayback(GlobalAudioCoordinator.AudioSource.QURAN_RECITER);
            if (exoPlayer == null) buildExoPlayer();
            MediaItem mediaItem = MediaItem.fromUri(Uri.fromFile(localFullSurah));
            exoPlayer.setMediaItem(mediaItem);
            applyPlaybackSpeed();
            exoPlayer.prepare();
            exoPlayer.play();
            startForegroundCompat(buildNotification(true));
            notifyAyahChanged();
            checkProgressTracking();
            return;
        }

        int totalAyahs = 7;
        QuranSurahEntity entity = QuranSurahDataSeeder.getSurahByNumber(surahNumber);
        if (entity != null && entity.getNumberOfAyahs() > 0) {
            totalAyahs = entity.getNumberOfAyahs();
        }
        playAyah(surahNumber, 1, totalAyahs, this.currentSurahNameEn, this.currentSurahNameBn, this.currentReciter);
    }

    public void playBanglaTranslationSurah(int surahNumber, String surahNameEn, String surahNameBn) {
        this.currentSurahNumber = surahNumber;
        this.currentAyahNumber = 1;
        this.totalAyahsInSurah = 1;
        this.currentSurahNameEn = (surahNameEn != null && !surahNameEn.isEmpty()) ? surahNameEn : getSurahEnName(surahNumber);
        this.currentSurahNameBn = (surahNameBn != null && !surahNameBn.isEmpty()) ? surahNameBn : getSurahBnName(surahNumber);
        this.isFullSurahMode = true;
        this.isBanglaTranslationMode = true;

        GlobalAudioCoordinator.getInstance().requestPlayback(GlobalAudioCoordinator.AudioSource.QURAN_RECITER);

        if (exoPlayer == null) buildExoPlayer();

        Uri audioUri = com.devflux.deenone.core.quran.QuranAudioCacheManager.getBanglaSurahPlaybackUri(this, currentSurahNumber);
        MediaItem mediaItem = MediaItem.fromUri(audioUri);
        exoPlayer.setMediaItem(mediaItem);
        applyPlaybackSpeed();
        exoPlayer.prepare();
        exoPlayer.play();

        if (!com.devflux.deenone.core.quran.QuranAudioCacheManager.isBanglaSurahAudioDownloaded(this, currentSurahNumber)) {
            com.devflux.deenone.core.quran.QuranAudioCacheManager.cacheBanglaSurahAudioInBackground(this, currentSurahNumber);
        }

        startForegroundCompat(buildNotification(true));
        notifyAyahChanged();
        checkProgressTracking();
    }

    public void pause() {
        if (exoPlayer != null) {
            exoPlayer.pause();
            updateNotification();
            notifyStateChanged();
            checkProgressTracking();
        }
    }

    public void resume() {
        if (exoPlayer != null) {
            GlobalAudioCoordinator.getInstance().requestPlayback(GlobalAudioCoordinator.AudioSource.QURAN_RECITER);
            exoPlayer.play();
            updateNotification();
            notifyStateChanged();
            checkProgressTracking();
        }
    }

    public void togglePlayPause() {
        if (isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    public void playNextAyah() {
        if (isBanglaTranslationMode) {
            if (currentSurahNumber < 114) {
                int nextSurah = currentSurahNumber + 1;
                playBanglaTranslationSurah(nextSurah, getSurahEnName(nextSurah), getSurahBnName(nextSurah));
            }
            return;
        }

        if (currentAyahNumber < totalAyahsInSurah) {
            playAyah(currentSurahNumber, currentAyahNumber + 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
        } else {
            if (currentSurahNumber < 114) {
                int nextSurah = currentSurahNumber + 1;
                QuranSurahEntity nextEntity = QuranSurahDataSeeder.getSurahByNumber(nextSurah);
                int nextTotal = nextEntity != null ? nextEntity.getNumberOfAyahs() : 10;
                playAyah(nextSurah, 1, nextTotal, getSurahEnName(nextSurah), getSurahBnName(nextSurah), currentReciter);
            }
        }
    }

    public void playPreviousAyah() {
        if (isBanglaTranslationMode) {
            if (currentSurahNumber > 1) {
                int prevSurah = currentSurahNumber - 1;
                playBanglaTranslationSurah(prevSurah, getSurahEnName(prevSurah), getSurahBnName(prevSurah));
            }
            return;
        }

        if (currentAyahNumber > 1) {
            playAyah(currentSurahNumber, currentAyahNumber - 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
        } else {
            playAyah(currentSurahNumber, 1, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
        }
    }

    public void stopPlayback() {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        isProgressTrackingActive = false;
        progressHandler.removeCallbacks(progressRunnable);
        GlobalAudioCoordinator.getInstance().onSourceStopped(GlobalAudioCoordinator.AudioSource.QURAN_RECITER);
        try {
            stopForeground(STOP_FOREGROUND_REMOVE);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.cancel(NOTIF_ID);
            stopSelf();
        } catch (Exception ignored) {}

        if (playbackListener != null) {
            playbackListener.onPlaybackStopped();
        }
    }

    public boolean isPlaying() {
        return exoPlayer != null && exoPlayer.isPlaying();
    }

    public int getCurrentSurahNumber() {
        return currentSurahNumber;
    }

    public int getCurrentAyahNumber() {
        return currentAyahNumber;
    }

    public String getCurrentSurahNameBn() {
        return currentSurahNameBn;
    }

    public QuranCdnAudioHelper.Reciter getCurrentReciter() {
        return currentReciter;
    }

    public void setReciter(QuranCdnAudioHelper.Reciter reciter) {
        this.currentReciter = reciter;
        if (isPlaying()) {
            playAyah(currentSurahNumber, currentAyahNumber, totalAyahsInSurah, currentSurahNameEn, currentSurahNameBn, currentReciter);
        }
    }

    private void notifyAyahChanged() {
        try {
            android.content.SharedPreferences quranPrefs = getSharedPreferences("quran_prefs", Context.MODE_PRIVATE);
            quranPrefs.edit()
                    .putInt("last_surah_number", currentSurahNumber)
                    .putString("last_surah_name_bn", currentSurahNameBn)
                    .putString("last_surah_name_en", currentSurahNameEn)
                    .putInt("last_ayah_number", currentAyahNumber)
                    .putInt("last_total_ayahs", totalAyahsInSurah)
                    .putLong("last_read_timestamp", System.currentTimeMillis())
                    .apply();

            com.devflux.deenone.data.local.AppDatabase.databaseWriteExecutor.execute(() -> {
                com.devflux.deenone.data.local.AppDatabase.getInstance(this)
                        .surahDao()
                        .updateLastReadPosition(currentSurahNumber, currentAyahNumber, System.currentTimeMillis());
            });
        } catch (Exception ignored) {}

        if (playbackListener != null) {
            playbackListener.onAyahChanged(currentSurahNumber, currentAyahNumber, currentSurahNameBn, isPlaying());
        }
    }

    private void notifyStateChanged() {
        if (playbackListener != null) {
            playbackListener.onPlaybackStateChanged(isPlaying());
        }
    }

    private void updateNotification() {
        try {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.notify(NOTIF_ID, buildNotification(isPlaying()));
            }
        } catch (Exception ignored) {}
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    "কুরআন তিলাওয়াত ব্যাকগ্রাউন্ড অডিও",
                    NotificationManager.IMPORTANCE_LOW
            );
            ch.setDescription("কুরআন তিলাওয়াত নোটিফিকেশন বার ও লকস্ক্রিন কন্ট্রোল");
            ch.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.createNotificationChannel(ch);
        }
    }

    private Notification buildNotification(boolean playing) {
        Intent stopIntent = new Intent(this, QuranAudioService.class).setAction(ACTION_STOP);
        PendingIntent stopPi = PendingIntent.getService(this, 101, stopIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent prevIntent = new Intent(this, QuranAudioService.class).setAction(ACTION_PREV_AYAH);
        PendingIntent prevPi = PendingIntent.getService(this, 102, prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent toggleIntent = new Intent(this, QuranAudioService.class).setAction(ACTION_TOGGLE);
        PendingIntent togglePi = PendingIntent.getService(this, 103, toggleIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, QuranAudioService.class).setAction(ACTION_NEXT_AYAH);
        PendingIntent nextPi = PendingIntent.getService(this, 104, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent contentIntent = new Intent(this, MainActivity.class);
        contentIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        contentIntent.putExtra("extra_deep_link", "feature_quran_surah");
        contentIntent.putExtra("extra_surah_number", currentSurahNumber);
        contentIntent.putExtra("extra_ayah_number", currentAyahNumber);
        PendingIntent contentPi = PendingIntent.getActivity(this, 105, contentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(this);
        String titleText;
        String reciterName;
        if (isBanglaTranslationMode) {
            titleText = isBn
                    ? (currentSurahNameBn + " — তিলাওয়াত ও বাংলা অনুবাদ")
                    : (currentSurahNameEn + " — Recitation & Bangla Translation");
            reciterName = isBn
                    ? "মিশারী রশিদ আল-আফাসী • ইসলামিক ফাউন্ডেশন"
                    : "Mishary Alafasy & Islamic Foundation";
        } else if (isFullSurahMode) {
            titleText = isBn
                    ? (currentSurahNameBn + " — পূর্ণাঙ্গ তিলাওয়াত")
                    : (currentSurahNameEn + " — Full Recitation");
            reciterName = currentReciter != null ? (isBn ? currentReciter.displayNameBn : currentReciter.displayNameEn) : "Mishary Rashid Alafasy";
        } else {
            String ayahNumStr = isBn ? BengaliNumberUtil.toBengali(currentAyahNumber) : String.valueOf(currentAyahNumber);
            titleText = isBn
                    ? (currentSurahNameBn + " — আয়াত " + ayahNumStr)
                    : (currentSurahNameEn + " — Ayah " + currentAyahNumber);
            reciterName = currentReciter != null ? (isBn ? currentReciter.displayNameBn : currentReciter.displayNameEn) : "Mishary Rashid Alafasy";
        }

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_deenone)
                .setContentTitle(titleText)
                .setContentText(reciterName)
                .setContentIntent(contentPi)
                .setOngoing(playing)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        try {
            nb.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        } catch (Exception ignored) {}

        nb.addAction(R.drawable.ic_stop, "Stop", stopPi);
        nb.addAction(R.drawable.ic_skip_previous, "Previous", prevPi);
        nb.addAction(playing ? R.drawable.ic_pause : R.drawable.ic_play_arrow, playing ? "Pause" : "Play", togglePi);
        nb.addAction(R.drawable.ic_skip_next, "Next", nextPi);

        nb.setStyle(new MediaStyle()
                .setShowActionsInCompactView(1, 2, 3)
                .setShowCancelButton(true)
                .setCancelButtonIntent(stopPi));

        return nb.build();
    }

    private String getSurahEnName(int surahNumber) {
        switch (surahNumber) {
            case 1: return "Al-Fatiha";
            case 2: return "Al-Baqara";
            case 3: return "Ali 'Imran";
            case 4: return "An-Nisa";
            case 5: return "Al-Ma'idah";
            case 18: return "Al-Kahf";
            case 36: return "Ya-Sin";
            case 55: return "Ar-Rahman";
            case 56: return "Al-Waqi'ah";
            case 67: return "Al-Mulk";
            case 112: return "Al-Ikhlas";
            case 113: return "Al-Falaq";
            case 114: return "An-Nas";
            default: return "Surah " + surahNumber;
        }
    }

    private String getSurahBnName(int surahNumber) {
        switch (surahNumber) {
            case 1: return "আল ফাতিহা";
            case 2: return "আল-বাকারা";
            case 3: return "আলে ইমরান";
            case 4: return "আন-নিসা";
            case 5: return "আল-মায়েদা";
            case 18: return "আল-কাহফ";
            case 36: return "ইয়াসীন";
            case 55: return "আর-রহমান";
            case 56: return "আল-ওয়াকিয়াহ";
            case 67: return "আল-মুলক";
            case 112: return "আল-ইখলাস";
            case 113: return "আল-ফালাক";
            case 114: return "আন-নাস";
            default: return "সূরা " + BengaliNumberUtil.toBengali(surahNumber);
        }
    }

    @Override
    public void onDestroy() {
        isProgressTrackingActive = false;
        progressHandler.removeCallbacks(progressRunnable);
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
        singletonInstance = null;
        super.onDestroy();
    }
}
