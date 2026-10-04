package com.devflux.deenone.features.audio.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.features.audio.download.AudioDownloadManager;
import com.devflux.deenone.features.audio.model.IslamicAudioItem;
import com.devflux.deenone.features.audio.repository.AudioLocalRepository;
import com.devflux.deenone.features.audio.repository.IslamicAudioApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class AudioHubViewModel extends AndroidViewModel {

    private static final String TAG = "AudioHubViewModel";

    private final MutableLiveData<List<IslamicAudioItem>> audioListLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<IslamicAudioItem>> favoritesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<IslamicAudioItem>> recentlyPlayedLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<IslamicAudioItem>> downloadedLiveData = new MutableLiveData<>();
    private final MutableLiveData<IslamicAudioItem> currentItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isBufferingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Long> playbackPositionLiveData = new MutableLiveData<>(0L);
    private final MutableLiveData<Long> playbackDurationLiveData = new MutableLiveData<>(0L);
    private final MutableLiveData<Long> bufferedPositionLiveData = new MutableLiveData<>(0L);
    private final MutableLiveData<Boolean> isPlayingLiveData = new MutableLiveData<>(false);

    private final AudioLocalRepository localRepository;
    private final IslamicAudioApiService apiService;
    private final AudioDownloadManager downloadManager;
    private final Executor bgExecutor;

    private String currentCategory = "all";
    private String currentSearchQuery = "";

    // Debounce for live search
    private final Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;
    private ConnectivityManager.NetworkCallback networkCallback;

    public AudioHubViewModel(@NonNull Application application) {
        super(application);
        this.localRepository = new AudioLocalRepository(application);
        this.downloadManager = AudioDownloadManager.getInstance(application);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
        this.apiService = new IslamicAudioApiService(client);
        this.bgExecutor = Executors.newSingleThreadExecutor();

        loadCategory("all");
        refreshFavorites();
        refreshRecentlyPlayed();
        refreshDownloads();
        registerNetworkObserver();
    }

    private void registerNetworkObserver() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                networkCallback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        Log.d(TAG, "Network restored: auto-syncing audio catalog");
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (currentSearchQuery.isEmpty()
                                    && !"favorites".equals(currentCategory)
                                    && !"recent".equals(currentCategory)
                                    && !"downloaded".equals(currentCategory)) {
                                syncCategoryFromApi(currentCategory);
                            }
                        });
                    }
                };
                cm.registerDefaultNetworkCallback(networkCallback);
            }
        } catch (Exception e) {
            Log.w(TAG, "registerNetworkObserver note: " + e.getMessage());
        }
    }

    // =========================================================================
    // LiveData Getters
    // =========================================================================
    public LiveData<List<IslamicAudioItem>> getAudioList() { return audioListLiveData; }
    public LiveData<List<IslamicAudioItem>> getFavorites() { return favoritesLiveData; }
    public LiveData<List<IslamicAudioItem>> getRecentlyPlayed() { return recentlyPlayedLiveData; }
    public LiveData<List<IslamicAudioItem>> getDownloadedList() { return downloadedLiveData; }
    public LiveData<IslamicAudioItem> getCurrentItem() { return currentItemLiveData; }
    public LiveData<Boolean> isLoading() { return isLoadingLiveData; }
    public LiveData<Boolean> isBuffering() { return isBufferingLiveData; }
    public LiveData<String> getError() { return errorLiveData; }
    public LiveData<Long> getPlaybackPosition() { return playbackPositionLiveData; }
    public LiveData<Long> getPlaybackDuration() { return playbackDurationLiveData; }
    public LiveData<Long> getBufferedPosition() { return bufferedPositionLiveData; }
    public LiveData<Boolean> isPlaying() { return isPlayingLiveData; }

    // =========================================================================
    // Data Loading & Category Switching with Offline-First & Online Auto-Sync
    // =========================================================================
    public void loadCategory(String category) {
        this.currentCategory = category;
        this.currentSearchQuery = "";
        errorLiveData.setValue(null);

        bgExecutor.execute(() -> {
            try {
                List<IslamicAudioItem> items;
                switch (category) {
                    case "favorites":
                        items = localRepository.getFavorites();
                        break;
                    case "recent":
                        items = localRepository.getRecentlyPlayed();
                        break;
                    case "downloaded":
                        items = localRepository.getDownloadedItems();
                        break;
                    default:
                        // 1. First retrieve local cached items for instant 0ms display
                        items = localRepository.getCachedCatalog(category);
                        if (items.isEmpty()) {
                            // Fallback to base curated items
                            items = apiService.fetchCategoryAudio(category);
                            localRepository.saveCachedCatalog(category, items);
                        }
                        break;
                }

                // Check download status for all items
                decorateDownloadStatus(items);
                audioListLiveData.postValue(items);

                // 2. If online and not a local collection, silently fetch fresh items in background
                if (AudioLocalRepository.isOnline(getApplication())
                        && !"favorites".equals(category)
                        && !"recent".equals(category)
                        && !"downloaded".equals(category)) {
                    syncCategoryFromApi(category);
                }

            } catch (Exception e) {
                Log.e(TAG, "loadCategory error: " + e.getMessage());
            }
        });
    }

    private void syncCategoryFromApi(String category) {
        bgExecutor.execute(() -> {
            try {
                List<IslamicAudioItem> remoteItems = apiService.fetchCategoryAudio(category);
                if (remoteItems != null && !remoteItems.isEmpty()) {
                    localRepository.saveCachedCatalog(category, remoteItems);
                    List<IslamicAudioItem> updated = localRepository.getCachedCatalog(category);
                    decorateDownloadStatus(updated);
                    audioListLiveData.postValue(updated);
                }
            } catch (Exception e) {
                Log.w(TAG, "syncCategoryFromApi error: " + e.getMessage());
            }
        });
    }

    private void decorateDownloadStatus(List<IslamicAudioItem> items) {
        if (items == null) return;
        for (IslamicAudioItem item : items) {
            if (downloadManager.isDownloaded(item.getId())) {
                item.setDownloaded(true);
                File f = downloadManager.getLocalAudioFile(item.getId());
                if (f != null) item.setLocalFilePath(f.getAbsolutePath());
            }
            if (downloadManager.isDownloading(item.getId())) {
                item.setDownloading(true);
            }
        }
    }

    public void searchWithDebounce(String query) {
        if (debounceRunnable != null) debounceHandler.removeCallbacks(debounceRunnable);
        debounceRunnable = () -> {
            currentSearchQuery = query != null ? query.trim() : "";
            if (currentSearchQuery.isEmpty()) {
                loadCategory(currentCategory);
            } else {
                performSearch(currentSearchQuery);
            }
        };
        debounceHandler.postDelayed(debounceRunnable, 350);
    }

    private void performSearch(String query) {
        isLoadingLiveData.setValue(true);
        errorLiveData.setValue(null);

        bgExecutor.execute(() -> {
            try {
                List<IslamicAudioItem> results = apiService.searchOnlineAudio(query);
                decorateDownloadStatus(results);
                audioListLiveData.postValue(results);
            } catch (Exception e) {
                Log.e(TAG, "performSearch error: " + e.getMessage());
                errorLiveData.postValue("অনুসন্ধানে সমস্যা হয়েছে।");
            } finally {
                isLoadingLiveData.postValue(false);
            }
        });
    }

    public void retry() {
        if (!currentSearchQuery.isEmpty()) {
            performSearch(currentSearchQuery);
        } else {
            loadCategory(currentCategory);
        }
    }

    // =========================================================================
    // Downloading & Offline Storage
    // =========================================================================
    public void startDownload(IslamicAudioItem item, AudioDownloadManager.DownloadCallback callback) {
        downloadManager.downloadAudio(item, new AudioDownloadManager.DownloadCallback() {
            @Override
            public void onProgress(IslamicAudioItem item, int percentage, long downloadedBytes, long totalBytes) {
                if (callback != null) callback.onProgress(item, percentage, downloadedBytes, totalBytes);
            }

            @Override
            public void onSuccess(IslamicAudioItem item, File downloadedFile) {
                refreshDownloads();
                if (callback != null) callback.onSuccess(item, downloadedFile);
            }

            @Override
            public void onError(IslamicAudioItem item, String errorMessage) {
                if (callback != null) callback.onError(item, errorMessage);
            }
        });
    }

    public boolean isDownloaded(String audioId) {
        return downloadManager.isDownloaded(audioId);
    }

    public boolean isDownloading(String audioId) {
        return downloadManager.isDownloading(audioId);
    }

    public File getLocalAudioFile(String audioId) {
        return downloadManager.getLocalAudioFile(audioId);
    }

    public void deleteDownload(String audioId) {
        File f = downloadManager.getLocalAudioFile(audioId);
        if (f != null && f.exists()) f.delete();
        localRepository.removeDownloadedItem(audioId);
        refreshDownloads();
    }

    // =========================================================================
    // Favorites & History
    // =========================================================================
    public void toggleFavorite(IslamicAudioItem item) {
        if (localRepository.isFavorite(item.getId())) {
            localRepository.removeFavorite(item.getId());
        } else {
            localRepository.addFavorite(item);
        }
        refreshFavorites();
    }

    public boolean isFavorite(String audioId) {
        return localRepository.isFavorite(audioId);
    }

    public void markPlayed(IslamicAudioItem item) {
        localRepository.markPlayed(item);
        refreshRecentlyPlayed();
    }

    public void refreshFavorites() {
        bgExecutor.execute(() -> favoritesLiveData.postValue(localRepository.getFavorites()));
    }

    public void refreshRecentlyPlayed() {
        bgExecutor.execute(() -> recentlyPlayedLiveData.postValue(localRepository.getRecentlyPlayed()));
    }

    public void refreshDownloads() {
        bgExecutor.execute(() -> downloadedLiveData.postValue(localRepository.getDownloadedItems()));
    }

    // =========================================================================
    // Playback State Management
    // =========================================================================
    public void setCurrentItem(IslamicAudioItem item) {
        currentItemLiveData.setValue(item);
    }

    public void setIsPlaying(boolean playing) {
        isPlayingLiveData.setValue(playing);
    }

    public void setIsBuffering(boolean buffering) {
        isBufferingLiveData.setValue(buffering);
    }

    public void updatePlaybackPosition(long positionMs) {
        playbackPositionLiveData.setValue(positionMs);
        IslamicAudioItem current = currentItemLiveData.getValue();
        if (current != null) {
            localRepository.savePlaybackPosition(current.getId(), positionMs);
        }
    }

    public void updatePlaybackDuration(long durationMs) {
        playbackDurationLiveData.setValue(durationMs);
    }

    public void updateBufferedPosition(long bufferedMs) {
        bufferedPositionLiveData.setValue(bufferedMs);
    }

    public long getSavedPosition(String audioId) {
        return localRepository.getSavedPlaybackPosition(audioId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (debounceRunnable != null) debounceHandler.removeCallbacks(debounceRunnable);
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null && networkCallback != null) {
                cm.unregisterNetworkCallback(networkCallback);
            }
        } catch (Exception ignored) {}
    }
}