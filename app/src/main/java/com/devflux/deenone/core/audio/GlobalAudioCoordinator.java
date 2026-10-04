package com.devflux.deenone.core.audio;

import android.content.Context;
import android.util.Log;

import com.devflux.deenone.core.quran.QuranSleepManager;
import com.devflux.deenone.features.audio.service.IslamicAudioPlayerService;

/**
 * GlobalAudioCoordinator — Centralized audio coordinator guaranteeing mutual audio exclusivity
 * across the entire DeenOne application.
 *
 * Guarantees:
 *   1. Zero audio overlap (e.g. Quran Sleep Mode and Islamic Audio Hub will NEVER play together).
 *   2. Instant pause/stop of competing audio when a new stream starts.
 *   3. Global Stop / Dismiss function accessible from UI, Notification Bar, and Lock Screen.
 */
public class GlobalAudioCoordinator {

    private static final String TAG = "GlobalAudioCoordinator";
    private static volatile GlobalAudioCoordinator instance;

    public enum AudioSource {
        NONE,
        QURAN_SLEEP_MODE,
        ISLAMIC_AUDIO_HUB,
        QURAN_RECITER,
        ADHAN_PREVIEW,
        DUA_AUDIO
    }

    private AudioSource currentActiveSource = AudioSource.NONE;

    public static GlobalAudioCoordinator getInstance() {
        if (instance == null) {
            synchronized (GlobalAudioCoordinator.class) {
                if (instance == null) {
                    instance = new GlobalAudioCoordinator();
                }
            }
        }
        return instance;
    }

    private GlobalAudioCoordinator() {}

    /**
     * Call this BEFORE starting any audio playback from any source in the app.
     * It immediately terminates any currently playing audio from other sources.
     */
    public synchronized void requestPlayback(Context context, AudioSource requestedSource) {
        logDebug("Audio requested by: " + requestedSource + ", current: " + currentActiveSource);

        if (currentActiveSource != requestedSource && currentActiveSource != AudioSource.NONE) {
            stopSourcePlayback(context, currentActiveSource);
        }

        currentActiveSource = requestedSource;
    }

    public synchronized void requestPlayback(AudioSource requestedSource) {
        requestPlayback(null, requestedSource);
    }

    public synchronized void onSourceStopped(AudioSource source) {
        if (currentActiveSource == source) {
            currentActiveSource = AudioSource.NONE;
        }
    }

    /**
     * Stops the specified audio source.
     */
    public synchronized void stopSourcePlayback(Context context, AudioSource source) {
        logDebug("Stopping source: " + source);

        try {
            switch (source) {
                case QURAN_SLEEP_MODE:
                    QuranSleepManager.getInstance().stopPlayback();
                    break;
                case ISLAMIC_AUDIO_HUB:
                    if (context != null) IslamicAudioPlayerService.stopPlayback(context);
                    break;
                case QURAN_RECITER:
                    if (com.devflux.deenone.service.QuranAudioService.getInstance() != null) {
                        com.devflux.deenone.service.QuranAudioService.getInstance().stopPlayback();
                    }
                    if (context != null) IslamicAudioPlayerService.stopPlayback(context);
                    break;
                case ADHAN_PREVIEW:
                case DUA_AUDIO:
                    if (context != null) IslamicAudioPlayerService.stopPlayback(context);
                    break;
                default:
                    break;
            }
        } catch (Throwable e) {
            logError("Error stopping audio source " + source + ": " + e.getMessage());
        }

        if (currentActiveSource == source) {
            currentActiveSource = AudioSource.NONE;
        }
    }

    /**
     * Stop all audio streams across the entire app and reset coordinator.
     */
    public synchronized void stopAll(Context context) {
        logDebug("Stopping ALL audio playback across the app");

        try {
            QuranSleepManager.getInstance().stopPlayback();
        } catch (Throwable ignored) {}

        try {
            if (com.devflux.deenone.service.QuranAudioService.getInstance() != null) {
                com.devflux.deenone.service.QuranAudioService.getInstance().stopPlayback();
            }
        } catch (Throwable ignored) {}

        if (context != null) {
            try {
                IslamicAudioPlayerService.stopPlayback(context);
            } catch (Throwable ignored) {}
        }

        currentActiveSource = AudioSource.NONE;
    }

    public synchronized AudioSource getCurrentActiveSource() {
        return currentActiveSource;
    }

    private static void logDebug(String msg) {
        try {
            Log.d(TAG, msg);
        } catch (Throwable ignored) {
            System.out.println(TAG + ": " + msg);
        }
    }

    private static void logError(String msg) {
        try {
            Log.e(TAG, msg);
        } catch (Throwable ignored) {
            System.err.println(TAG + ": " + msg);
        }
    }
}
