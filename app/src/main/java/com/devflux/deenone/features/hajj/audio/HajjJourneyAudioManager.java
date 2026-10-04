package com.devflux.deenone.features.hajj.audio;

import android.animation.ValueAnimator;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.audio.GlobalAudioCoordinator;
import com.devflux.deenone.service.QuranAudioService;

import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * HajjJourneyAudioManager — Dedicated singleton audio manager for Labbaik playback
 * Features:
 *   - Mutual audio exclusivity (won't interrupt Quran recitation or Azan)
 *   - Smooth 400ms volume Fade In on Start and Fade Out on Stop/Pause
 *   - Single instance guarantee (no audio overlap)
 *   - Autoplay once on entry
 *   - UI listener for Play/Pause state and animated wave indicator
 */
public class HajjJourneyAudioManager {

    private static final String TAG = "HajjJourneyAudio";
    private static volatile HajjJourneyAudioManager instance;

    public interface PlaybackStateListener {
        void onPlaybackStateChanged(boolean isPlaying);
    }

    private MediaPlayer mediaPlayer;
    private ValueAnimator fadeAnimator;
    private boolean isAutoplayTriggered = false;
    private float currentVolume = 0.0f;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final CopyOnWriteArrayList<WeakReference<PlaybackStateListener>> listeners = new CopyOnWriteArrayList<>();

    public static HajjJourneyAudioManager getInstance() {
        if (instance == null) {
            synchronized (HajjJourneyAudioManager.class) {
                if (instance == null) {
                    instance = new HajjJourneyAudioManager();
                }
            }
        }
        return instance;
    }

    private HajjJourneyAudioManager() {}

    public void addListener(PlaybackStateListener listener) {
        if (listener != null) {
            listeners.add(new WeakReference<>(listener));
            listener.onPlaybackStateChanged(isPlaying());
        }
    }

    public void removeListener(PlaybackStateListener listener) {
        if (listener == null) return;
        listeners.removeIf(ref -> {
            PlaybackStateListener l = ref.get();
            return l == null || l == listener;
        });
    }

    private void notifyStateChanged(boolean isPlaying) {
        mainHandler.post(() -> {
            for (WeakReference<PlaybackStateListener> ref : listeners) {
                PlaybackStateListener l = ref.get();
                if (l != null) {
                    l.onPlaybackStateChanged(isPlaying);
                }
            }
        });
    }

    /**
     * Autoplay once on entry into Hajj Journey section, provided no other audio is playing.
     */
    public synchronized void triggerAutoplayOnEntry(@NonNull Context context) {
        if (isAutoplayTriggered) {
            return;
        }
        isAutoplayTriggered = true;

        if (isCompetingAudioActive(context)) {
            Log.d(TAG, "Competing audio is active; skipping autoplay to avoid interruption.");
            return;
        }

        playWithFadeIn(context);
    }

    /**
     * Checks if higher-priority audio (Quran, Azan, etc.) is currently playing.
     */
    public boolean isCompetingAudioActive(@NonNull Context context) {
        try {
            // Check DeenOne global coordinator
            GlobalAudioCoordinator coordinator = GlobalAudioCoordinator.getInstance();
            if (coordinator.getCurrentActiveSource() != GlobalAudioCoordinator.AudioSource.NONE) {
                return true;
            }

            // Check QuranAudioService
            QuranAudioService quranService = QuranAudioService.getInstance();
            if (quranService != null && quranService.isPlaying()) {
                return true;
            }

            // Check system AudioManager music stream
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (am != null && am.isMusicActive()) {
                return true;
            }
        } catch (Throwable t) {
            Log.w(TAG, "Error checking competing audio: " + t.getMessage());
        }
        return false;
    }

    public synchronized boolean isPlaying() {
        try {
            return mediaPlayer != null && mediaPlayer.isPlaying();
        } catch (Throwable ignored) {
            return false;
        }
    }

    public synchronized void togglePlayPause(@NonNull Context context) {
        if (isPlaying()) {
            pauseWithFadeOut();
        } else {
            playWithFadeIn(context);
        }
    }

    /**
     * Starts playback with a smooth 400ms volume fade-in.
     */
    public synchronized void playWithFadeIn(@NonNull Context context) {
        cancelFadeAnimator();

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.labbaik);
                if (mediaPlayer == null) {
                    Log.e(TAG, "Failed to create MediaPlayer for labbaik.mp3");
                    return;
                }
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnCompletionListener(mp -> {
                    currentVolume = 0.0f;
                    notifyStateChanged(false);
                });
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
                    releasePlayer();
                    notifyStateChanged(false);
                    return true;
                });
            }

            if (!mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration() - 500) {
                mediaPlayer.seekTo(0);
            }

            currentVolume = 0.0f;
            mediaPlayer.setVolume(0.0f, 0.0f);
            mediaPlayer.start();
            notifyStateChanged(true);

            fadeAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            fadeAnimator.setDuration(400);
            fadeAnimator.addUpdateListener(anim -> {
                float vol = (float) anim.getAnimatedValue();
                currentVolume = vol;
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.setVolume(vol, vol);
                    } catch (Throwable ignored) {}
                }
            });
            fadeAnimator.start();

        } catch (Throwable t) {
            Log.e(TAG, "Error playing labbaik: " + t.getMessage());
        }
    }

    /**
     * Pauses playback with a smooth 400ms volume fade-out.
     */
    public synchronized void pauseWithFadeOut() {
        if (mediaPlayer == null || !isPlaying()) {
            notifyStateChanged(false);
            return;
        }

        cancelFadeAnimator();

        final float startVol = currentVolume > 0.0f ? currentVolume : 1.0f;
        fadeAnimator = ValueAnimator.ofFloat(startVol, 0.0f);
        fadeAnimator.setDuration(400);
        fadeAnimator.addUpdateListener(anim -> {
            float vol = (float) anim.getAnimatedValue();
            currentVolume = vol;
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.setVolume(vol, vol);
                } catch (Throwable ignored) {}
            }
        });
        fadeAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                synchronized (HajjJourneyAudioManager.this) {
                    if (mediaPlayer != null && isPlaying()) {
                        try {
                            mediaPlayer.pause();
                        } catch (Throwable ignored) {}
                    }
                    notifyStateChanged(false);
                }
            }
        });
        fadeAnimator.start();
    }

    /**
     * Stops and releases playback gracefully with 400ms fade-out when leaving Hajj Journey section.
     */
    public synchronized void stopAndReleaseWithFadeOut() {
        cancelFadeAnimator();

        if (mediaPlayer == null) {
            notifyStateChanged(false);
            return;
        }

        if (!isPlaying()) {
            releasePlayer();
            notifyStateChanged(false);
            return;
        }

        final float startVol = currentVolume > 0.0f ? currentVolume : 1.0f;
        fadeAnimator = ValueAnimator.ofFloat(startVol, 0.0f);
        fadeAnimator.setDuration(400);
        fadeAnimator.addUpdateListener(anim -> {
            float vol = (float) anim.getAnimatedValue();
            currentVolume = vol;
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.setVolume(vol, vol);
                } catch (Throwable ignored) {}
            }
        });
        fadeAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                releasePlayer();
                notifyStateChanged(false);
            }
        });
        fadeAnimator.start();
    }

    private synchronized void releasePlayer() {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.release();
            } catch (Throwable ignored) {}
            mediaPlayer = null;
        }
        currentVolume = 0.0f;
        isAutoplayTriggered = false;
    }

    private void cancelFadeAnimator() {
        if (fadeAnimator != null && fadeAnimator.isRunning()) {
            fadeAnimator.cancel();
            fadeAnimator = null;
        }
    }
}
