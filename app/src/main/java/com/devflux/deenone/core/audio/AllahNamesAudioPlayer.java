package com.devflux.deenone.core.audio;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.data.model.AllahNameItem;
import com.devflux.deenone.data.repository.AllahNamesRepository;

import java.util.List;

public class AllahNamesAudioPlayer {

    public interface AudioPlayerCallback {
        void onPlaybackStateChanged(boolean isPlaying, int currentNumber);
        void onProgressUpdate(int currentSec, int totalSec);
        void onError(String message);
    }

    private static AllahNamesAudioPlayer instance;
    private int currentPlayingNumber = -1;
    private boolean isContinuousPlay = false;
    private AudioPlayerCallback callback;
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable;

    public static synchronized AllahNamesAudioPlayer getInstance() {
        if (instance == null) {
            instance = new AllahNamesAudioPlayer();
        }
        return instance;
    }

    public void setCallback(AudioPlayerCallback callback) {
        this.callback = callback;
    }

    public boolean isPlaying() {
        return currentPlayingNumber != -1;
    }

    public int getCurrentPlayingNumber() {
        return currentPlayingNumber;
    }

    public void playName(Context context, AllahNameItem item, boolean continuous) {
        if (item == null) return;
        this.isContinuousPlay = continuous;

        if (currentPlayingNumber == item.getNumber()) {
            stop(context);
            return;
        }

        stop(context);
        currentPlayingNumber = item.getNumber();

        ArabicVoiceAudioEngine engine = ArabicVoiceAudioEngine.getInstance(context);
        engine.playArabic(context, item.getNameArabic(), "allah_name_" + item.getNumber(), new ArabicVoiceAudioEngine.PlaybackCallback() {
            @Override
            public void onStart(String identifier) {
                startProgressUpdates();
                if (callback != null) {
                    callback.onPlaybackStateChanged(true, currentPlayingNumber);
                }
            }

            @Override
            public void onDone(String identifier) {
                stopProgressUpdates();
                if (isContinuousPlay && currentPlayingNumber > 0 && currentPlayingNumber < 99) {
                    playNext(context);
                } else {
                    int prev = currentPlayingNumber;
                    currentPlayingNumber = -1;
                    if (callback != null) {
                        callback.onPlaybackStateChanged(false, -1);
                    }
                }
            }

            @Override
            public void onError(String identifier, String errorReason) {
                stopProgressUpdates();
                currentPlayingNumber = -1;
                if (callback != null) {
                    callback.onPlaybackStateChanged(false, -1);
                    callback.onError(errorReason);
                }
            }
        });
    }

    public void playNext(Context context) {
        int nextNum = currentPlayingNumber + 1;
        if (nextNum > 99) nextNum = 1;
        List<AllahNameItem> all = AllahNamesRepository.getAllNames();
        if (nextNum - 1 < all.size()) {
            playName(context, all.get(nextNum - 1), true);
        }
    }

    public void playPrevious(Context context) {
        int prevNum = currentPlayingNumber - 1;
        if (prevNum < 1) prevNum = 99;
        List<AllahNameItem> all = AllahNamesRepository.getAllNames();
        if (prevNum - 1 < all.size()) {
            playName(context, all.get(prevNum - 1), true);
        }
    }

    public void stop(Context context) {
        stopProgressUpdates();
        if (context != null) {
            ArabicVoiceAudioEngine.getInstance(context).stop();
        }
        currentPlayingNumber = -1;
        if (callback != null) callback.onPlaybackStateChanged(false, -1);
    }

    public void stop() {
        stopProgressUpdates();
        currentPlayingNumber = -1;
        if (callback != null) callback.onPlaybackStateChanged(false, -1);
    }

    private void startProgressUpdates() {
        stopProgressUpdates();
        progressRunnable = new Runnable() {
            int tick = 0;
            @Override
            public void run() {
                if (currentPlayingNumber != -1 && callback != null) {
                    tick++;
                    callback.onProgressUpdate(tick, 3);
                    progressHandler.postDelayed(this, 1000);
                }
            }
        };
        progressHandler.post(progressRunnable);
    }

    private void stopProgressUpdates() {
        if (progressRunnable != null) {
            progressHandler.removeCallbacks(progressRunnable);
            progressRunnable = null;
        }
    }
}
