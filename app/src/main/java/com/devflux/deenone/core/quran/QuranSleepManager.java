package com.devflux.deenone.core.quran;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.audio.GlobalAudioCoordinator;
import com.devflux.deenone.core.quran.service.QuranSleepService;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QuranSleepManager — High-Performance Superfast Sleep Mode Quran Audio Controller.
 * Supports dynamic Playback Queue (sequential autoplay of selected Surahs with auto-looping until timer finishes).
 */
public class QuranSleepManager {

    private static final String TAG = "QuranSleepManager";

    public enum PlaybackState {
        IDLE,
        BUFFERING,
        PLAYING,
        PAUSED,
        STOPPED
    }

    private static volatile QuranSleepManager instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<QuranSurahEntity> surahList;

    // State LiveData
    private final MutableLiveData<PlaybackState> stateLiveData = new MutableLiveData<>(PlaybackState.IDLE);
    private final MutableLiveData<List<Integer>> surahQueueLiveData = new MutableLiveData<>(new ArrayList<>(Collections.singletonList(67))); // Default: Surah Mulk (67)
    private final MutableLiveData<Integer> currentSurahNumberLiveData = new MutableLiveData<>(67);
    private final MutableLiveData<Integer> currentQueueIndexLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> timerMinutesLiveData = new MutableLiveData<>(15);      // Default: 15 Mins
    private final MutableLiveData<Long> remainingSecondsLiveData = new MutableLiveData<>(15 * 60L);
    private final MutableLiveData<String> formattedTimerLiveData = new MutableLiveData<>("15:00");
    private final MutableLiveData<String> statusMessageLiveData = new MutableLiveData<>("● টাইমার প্রস্তুত");

    private Context appContext;

    public static QuranSleepManager getInstance() {
        if (instance == null) {
            synchronized (QuranSleepManager.class) {
                if (instance == null) {
                    instance = new QuranSleepManager();
                }
            }
        }
        return instance;
    }

    private QuranSleepManager() {
        this.surahList = QuranSurahDataSeeder.get114Surahs();
    }

    public void init(Context context) {
        this.appContext = context.getApplicationContext();
    }

    // =========================================================================
    // LiveData Getters
    // =========================================================================
    public LiveData<PlaybackState> getStateLiveData() { return stateLiveData; }
    public LiveData<List<Integer>> getSurahQueueLiveData() { return surahQueueLiveData; }
    public LiveData<Integer> getCurrentSurahNumberLiveData() { return currentSurahNumberLiveData; }
    public LiveData<Integer> getCurrentQueueIndexLiveData() { return currentQueueIndexLiveData; }
    public LiveData<Integer> getTimerMinutesLiveData() { return timerMinutesLiveData; }
    public LiveData<Long> getRemainingSecondsLiveData() { return remainingSecondsLiveData; }
    public LiveData<String> getFormattedTimerLiveData() { return formattedTimerLiveData; }
    public LiveData<String> getStatusMessageLiveData() { return statusMessageLiveData; }

    public List<QuranSurahEntity> getSurahList() { return surahList; }

    public QuranSurahEntity getSurah(int surahNumber) {
        if (surahList != null) {
            for (QuranSurahEntity s : surahList) {
                if (s.getNumber() == surahNumber) return s;
            }
            if (!surahList.isEmpty()) return surahList.get(0);
        }
        return new QuranSurahEntity(1, "الفاتحة", "আল-ফাতিহা", "Al-Fatihah", "সূচনা", "The Opening", 7, "মাক্কী", 1);
    }

    // =========================================================================
    // Queue Management
    // =========================================================================
    public void addToQueue(int surahNumber) {
        if (surahNumber < 1 || surahNumber > 114) return;
        List<Integer> currentQueue = surahQueueLiveData.getValue();
        List<Integer> updated = new ArrayList<>(currentQueue != null ? currentQueue : Collections.emptyList());
        
        if (!updated.contains(surahNumber)) {
            updated.add(surahNumber);
        }
        surahQueueLiveData.setValue(updated);
        currentSurahNumberLiveData.setValue(surahNumber);

        if (stateLiveData.getValue() == PlaybackState.PLAYING && appContext != null) {
            QuranSleepService.updateQueueDirect(appContext, new ArrayList<>(updated));
        }
    }

    public void removeFromQueue(int surahNumber) {
        List<Integer> currentQueue = surahQueueLiveData.getValue();
        if (currentQueue == null || currentQueue.isEmpty()) return;

        List<Integer> updated = new ArrayList<>(currentQueue);
        updated.remove(Integer.valueOf(surahNumber));
        surahQueueLiveData.setValue(updated);

        if (updated.isEmpty()) {
            stopPlayback();
            currentSurahNumberLiveData.setValue(1);
        } else {
            Integer cur = currentSurahNumberLiveData.getValue();
            if (cur != null && cur == surahNumber) {
                currentSurahNumberLiveData.setValue(updated.get(0));
            }
            if (stateLiveData.getValue() == PlaybackState.PLAYING && appContext != null) {
                QuranSleepService.updateQueueDirect(appContext, new ArrayList<>(updated));
            }
        }
    }

    public void setSelectedSurahNumber(int surahNumber) {
        if (surahNumber < 1 || surahNumber > 114) surahNumber = 1;
        addToQueue(surahNumber);
    }

    public void setTimerMinutes(int minutes) {
        timerMinutesLiveData.setValue(minutes);
        remainingSecondsLiveData.setValue((long) minutes * 60L);
        formattedTimerLiveData.setValue(String.format(java.util.Locale.US, "%02d:00", minutes));

        if (appContext != null) {
            if (stateLiveData.getValue() == PlaybackState.PLAYING) {
                List<Integer> queue = surahQueueLiveData.getValue();
                if (queue != null && !queue.isEmpty()) {
                    QuranSleepService.startPlaybackQueue(appContext, new ArrayList<>(queue), minutes);
                }
            } else {
                QuranSleepService.setTimerDuration(appContext, minutes);
            }
        }
    }

    // =========================================================================
    // Playback Control Flow
    // =========================================================================
    public void togglePlayPause() {
        PlaybackState current = stateLiveData.getValue();
        if (current == PlaybackState.PLAYING) {
            pausePlayback();
        } else if (current == PlaybackState.PAUSED) {
            resumePlayback();
        } else {
            startSleepSession();
        }
    }

    public void startSleepSession() {
        List<Integer> queue = surahQueueLiveData.getValue();
        if (queue == null || queue.isEmpty()) {
            queue = new ArrayList<>(Collections.singletonList(67));
            surahQueueLiveData.setValue(queue);
        }
        int minutes = timerMinutesLiveData.getValue() != null ? timerMinutesLiveData.getValue() : 15;

        stateLiveData.setValue(PlaybackState.BUFFERING);
        statusMessageLiveData.setValue("● লোড হচ্ছে...");
        currentSurahNumberLiveData.setValue(queue.get(0));

        if (appContext != null) {
            QuranSleepService.startPlaybackQueue(appContext, new ArrayList<>(queue), minutes);
        }
    }

    public void pausePlayback() {
        stateLiveData.setValue(PlaybackState.PAUSED);
        statusMessageLiveData.setValue("● তিলাওয়াত স্থগিত");
        if (appContext != null) {
            QuranSleepService.pausePlayback(appContext);
        }
    }

    public void resumePlayback() {
        stateLiveData.setValue(PlaybackState.PLAYING);
        statusMessageLiveData.setValue("● তিলাওয়াত চলছে");
        if (appContext != null) {
            QuranSleepService.resumePlayback(appContext);
        }
    }

    public void stopPlayback() {
        stateLiveData.setValue(PlaybackState.STOPPED);
        statusMessageLiveData.setValue("● টাইমার প্রস্তুত");
        int minutes = timerMinutesLiveData.getValue() != null ? timerMinutesLiveData.getValue() : 15;
        formattedTimerLiveData.setValue(String.format(java.util.Locale.US, "%02d:00", minutes));
        remainingSecondsLiveData.setValue((long) minutes * 60L);

        if (appContext != null) {
            QuranSleepService.stopPlayback(appContext);
        }
    }

    // =========================================================================
    // Callbacks from QuranSleepService
    // =========================================================================
    public void notifyPlayingState(boolean isPlaying) {
        mainHandler.post(() -> {
            if (isPlaying) {
                stateLiveData.setValue(PlaybackState.PLAYING);
                statusMessageLiveData.setValue("● তিলাওয়াত চলছে");
            } else {
                stateLiveData.setValue(PlaybackState.PAUSED);
                statusMessageLiveData.setValue("● তিলাওয়াত স্থগিত");
            }
        });
    }

    public void notifyBuffering(boolean isBuffering) {
        mainHandler.post(() -> {
            if (isBuffering) {
                stateLiveData.setValue(PlaybackState.BUFFERING);
                statusMessageLiveData.setValue("● লোড হচ্ছে...");
            } else if (stateLiveData.getValue() == PlaybackState.BUFFERING) {
                stateLiveData.setValue(PlaybackState.PLAYING);
                statusMessageLiveData.setValue("● তিলাওয়াত চলছে");
            }
        });
    }

    public void notifySurahChanged(int currentSurahNumber, int queueIndex, int minutes) {
        mainHandler.post(() -> {
            currentSurahNumberLiveData.setValue(currentSurahNumber);
            currentQueueIndexLiveData.setValue(queueIndex);
            timerMinutesLiveData.setValue(minutes);
            stateLiveData.setValue(PlaybackState.PLAYING);
            statusMessageLiveData.setValue("● তিলাওয়াত চলছে");
        });
    }

    public void notifyTimerTick(long totalSeconds, String formatted) {
        mainHandler.post(() -> {
            remainingSecondsLiveData.setValue(totalSeconds);
            formattedTimerLiveData.setValue(formatted);
        });
    }

    public void notifyStopped() {
        mainHandler.post(() -> {
            stateLiveData.setValue(PlaybackState.STOPPED);
            statusMessageLiveData.setValue("● টাইমার প্রস্তুত");
            int minutes = timerMinutesLiveData.getValue() != null ? timerMinutesLiveData.getValue() : 15;
            formattedTimerLiveData.setValue(String.format(java.util.Locale.US, "%02d:00", minutes));
            remainingSecondsLiveData.setValue((long) minutes * 60L);
        });
    }
}