package com.devflux.deenone.core.quran;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.notifications.NotificationHelper;
import com.devflux.deenone.data.repository.QuranRepository;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * QuranUnifiedDownloadManager — Centralized, unified download and offline storage manager
 * for both Quran Section (Quran Majeed) and Quran Sleep Section (Sleep Mode).
 *
 * Guarantees:
 * 1. ONE shared local Quran database/cache and ONE shared offline audio storage directory.
 * 2. 100% synchronized offline status across all Quran sections.
 * 3. Never downloads duplicates — checks existing files first.
 * 4. Background downloading with Notification Bar Pause/Resume and Cancel controls.
 * 5. Reusable across the entire application.
 */
public class QuranUnifiedDownloadManager {

    private static final String TAG = "QuranUnifiedDownload";
    public static final int TOTAL_SURAHS = 114;
    public static final int NOTIFICATION_ID_QURAN_DOWNLOAD = 2500;

    private static final String PREF_NAME = "deenone_prefs";
    public static final String PREF_QURAN_DOWNLOAD_PROMPT_DISMISSED = "pref_quran_download_prompt_dismissed";
    public static final String PREF_LEGACY_SLEEP_PROMPT_SEEN = "pref_quran_sleep_download_prompt_seen";

    public enum DownloadState {
        IDLE,
        DOWNLOADING,
        PAUSED,
        COMPLETED,
        CANCELLED,
        ERROR
    }

    public static class DownloadProgress {
        public final int completedSurahs;
        public final int totalSurahs;
        public final int progressPercent;
        public final String currentSurahBn;
        public final String currentSurahEn;

        public DownloadProgress(int completedSurahs, int totalSurahs, int progressPercent, String currentSurahBn, String currentSurahEn) {
            this.completedSurahs = completedSurahs;
            this.totalSurahs = totalSurahs;
            this.progressPercent = progressPercent;
            this.currentSurahBn = currentSurahBn;
            this.currentSurahEn = currentSurahEn;
        }
    }

    public interface DownloadListener {
        void onProgress(DownloadProgress progress);
        void onSuccess();
        void onError(String errorMessage);
        void onCancelled();
        void onPaused();
        void onResumed();
    }

    private static volatile QuranUnifiedDownloadManager instance;

    private final ExecutorService downloadExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final AtomicBoolean isDownloading = new AtomicBoolean(false);
    private final AtomicBoolean isPaused = new AtomicBoolean(false);
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);
    private final Object pauseLock = new Object();

    private final MutableLiveData<DownloadState> downloadStateLiveData = new MutableLiveData<>(DownloadState.IDLE);
    private final MutableLiveData<DownloadProgress> downloadProgressLiveData = new MutableLiveData<>();
    private final List<DownloadListener> listeners = new CopyOnWriteArrayList<>();

    private int lastCompletedCount = 0;
    private int lastPercent = 0;
    private String lastSurahBn = "";
    private String lastSurahEn = "";

    public static QuranUnifiedDownloadManager getInstance() {
        if (instance == null) {
            synchronized (QuranUnifiedDownloadManager.class) {
                if (instance == null) {
                    instance = new QuranUnifiedDownloadManager();
                }
            }
        }
        return instance;
    }

    private QuranUnifiedDownloadManager() {}

    // =========================================================================
    // Shared Global Storage & File Existence Verification (Rules 3, 6, 7, 8, 12, 13)
    // =========================================================================

    /**
     * Shared audio directory used globally across both sections.
     */
    public static File getAudioBaseDir(Context context) {
        return QuranAudioCacheManager.getAudioBaseDir(context);
    }

    /**
     * Shared full surah audio file reference used by both Quran Section and Quran Sleep Section.
     */
    public static File getFullSurahAudioFile(Context context, int surahNumber) {
        return QuranAudioCacheManager.getFullSurahAudioFile(context, surahNumber);
    }

    /**
     * Shared Ayah audio file reference used across all readers.
     */
    public static File getAyahAudioFile(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int ayahNumber) {
        return QuranAudioCacheManager.getAyahAudioFile(context, reciter, surahNumber, ayahNumber);
    }

    /**
     * Verifies if a specific Surah is downloaded and valid (> 5000 bytes) on disk.
     */
    public static boolean isSurahDownloaded(Context context, int surahNumber) {
        if (context == null || surahNumber < 1 || surahNumber > TOTAL_SURAHS) return false;
        File file = getFullSurahAudioFile(context, surahNumber);
        return file != null && file.exists() && file.length() > 5000;
    }

    /**
     * Returns total count of downloaded full Surahs (0 to 114).
     */
    public static int getDownloadedSurahCount(Context context) {
        if (context == null) return 0;
        int count = 0;
        for (int i = 1; i <= TOTAL_SURAHS; i++) {
            if (isSurahDownloaded(context, i)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Globally checks whether all 114 Surahs are fully downloaded and ready for offline playback.
     */
    public static boolean isAllSurahsDownloaded(Context context) {
        if (context == null) return false;
        for (int i = 1; i <= TOTAL_SURAHS; i++) {
            if (!isSurahDownloaded(context, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates total disk size in bytes used by downloaded Quran audios.
     */
    public static long getTotalOfflineAudioSizeBytes(Context context) {
        if (context == null) return 0L;
        long totalBytes = 0L;
        File fullSurahDir = new File(getAudioBaseDir(context), "full_surah");
        if (fullSurahDir.exists() && fullSurahDir.isDirectory()) {
            File[] files = fullSurahDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f != null && f.isFile()) {
                        totalBytes += f.length();
                    }
                }
            }
        }
        return totalBytes;
    }

    /**
     * Deletes all offline Quran audio files and immediately updates the global offline status for both sections (Rule 13).
     */
    public static void deleteOfflineAudio(Context context) {
        if (context == null) return;
        File fullSurahDir = new File(getAudioBaseDir(context), "full_surah");
        if (fullSurahDir.exists() && fullSurahDir.isDirectory()) {
            File[] files = fullSurahDir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f != null && f.isFile()) {
                        f.delete();
                    }
                }
            }
        }
        getInstance().downloadStateLiveData.postValue(DownloadState.IDLE);
        getInstance().downloadProgressLiveData.postValue(new DownloadProgress(0, TOTAL_SURAHS, 0, "", ""));
    }

    // =========================================================================
    // First-Time Prompt Preferences (Persistent & Shared between Sections)
    // =========================================================================

    /**
     * Checks if the user has dismissed/cancelled the first-time download popup from either section.
     */
    public static boolean hasUserDismissedPrompt(Context context) {
        if (context == null) return false;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(PREF_QURAN_DOWNLOAD_PROMPT_DISMISSED, false)
                || prefs.getBoolean(PREF_LEGACY_SLEEP_PROMPT_SEEN, false);
    }

    /**
     * Marks that the user has responded to the prompt (Download or Cancel), ensuring it is never shown again on subsequent visits.
     */
    public static void markPromptDismissed(Context context) {
        if (context == null) return;
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(PREF_QURAN_DOWNLOAD_PROMPT_DISMISSED, true)
                .putBoolean(PREF_LEGACY_SLEEP_PROMPT_SEEN, true)
                .apply();
    }

    // =========================================================================
    // Centralized Download Execution, Pause, Resume, and Cancel (Rule 4, 14, 15)
    // =========================================================================

    public boolean isCurrentlyDownloading() {
        return isDownloading.get();
    }

    public boolean isCurrentlyPaused() {
        return isPaused.get();
    }

    public LiveData<DownloadState> getDownloadStateLiveData() {
        return downloadStateLiveData;
    }

    public LiveData<DownloadProgress> getDownloadProgressLiveData() {
        return downloadProgressLiveData;
    }

    public void addListener(DownloadListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(DownloadListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Starts or attaches to the centralized full Quran audio download.
     * If a download is already in progress, reuses it without duplicating network operations (Rule 15).
     */
    public synchronized void startFullDownload(Context context, DownloadListener optionalListener) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        if (optionalListener != null) {
            addListener(optionalListener);
        }

        if (isDownloading.get()) {
            if (isPaused.get()) {
                resumeDownload(appContext);
            }
            Log.d(TAG, "Download already active. Reusing current download instance.");
            return;
        }

        isDownloading.set(true);
        isPaused.set(false);
        isCancelled.set(false);
        downloadStateLiveData.postValue(DownloadState.DOWNLOADING);

        downloadExecutor.execute(() -> {
            boolean isBn = LocaleManager.isBengali(appContext);
            String title = isBn ? "আল-কুরআন অফলাইন অডিও" : "Complete Quran Offline Audio";

            try {
                // Initial notification with Pause/Resume and Cancel actions
                NotificationHelper.updateQuranDownloadNotificationWithActions(
                        appContext,
                        NOTIFICATION_ID_QURAN_DOWNLOAD,
                        title,
                        isBn ? "ডাউনলোড শুরু হচ্ছে..." : "Starting download...",
                        0,
                        false
                );

                int completed = getDownloadedSurahCount(appContext);
                lastCompletedCount = completed;

                for (int i = 1; i <= TOTAL_SURAHS; i++) {
                    // Check cancellation
                    if (isCancelled.get()) {
                        handleCancelled(appContext);
                        return;
                    }

                    // Check pause
                    synchronized (pauseLock) {
                        while (isPaused.get() && !isCancelled.get()) {
                            try {
                                pauseLock.wait();
                            } catch (InterruptedException ignored) {
                                break;
                            }
                        }
                    }

                    if (isCancelled.get()) {
                        handleCancelled(appContext);
                        return;
                    }

                    int surahNum = i;
                    String surahBn = QuranAudioCacheManager.getSurahNameBn(surahNum);
                    String surahEn = QuranAudioCacheManager.getSurahNameEn(surahNum);
                    lastSurahBn = surahBn;
                    lastSurahEn = surahEn;

                    File targetFile = getFullSurahAudioFile(appContext, surahNum);

                    // Requirement 7 & 8: Check if file already exists locally. If so, NEVER download a duplicate!
                    if (targetFile.exists() && targetFile.length() > 5000) {
                        int percent = (int) ((completed / (float) TOTAL_SURAHS) * 100);
                        postProgress(completed, TOTAL_SURAHS, percent, surahBn, surahEn);
                        continue;
                    }

                    // File is missing: notify progress before download begins
                    int percentBefore = (int) ((completed / (float) TOTAL_SURAHS) * 100);
                    postProgress(completed, TOTAL_SURAHS, percentBefore, surahBn, surahEn);

                    String notifText = isBn
                            ? ("সূরা " + BengaliNumberUtil.toBengali(surahNum) + ": " + surahBn + " ডাউনলোড হচ্ছে... (" + BengaliNumberUtil.toBengali(percentBefore) + "%)")
                            : ("Surah " + surahNum + ": " + surahEn + " downloading... (" + percentBefore + "%)");

                    NotificationHelper.updateQuranDownloadNotificationWithActions(
                            appContext,
                            NOTIFICATION_ID_QURAN_DOWNLOAD,
                            title,
                            notifText,
                            percentBefore,
                            false
                    );

                    // Download using high-speed CDN with multiple fallbacks
                    boolean downloaded = QuranAudioCacheManager.downloadSurahWithCdnFallbacks(surahNum, targetFile);

                    if (isCancelled.get()) {
                        if (targetFile.exists() && targetFile.length() <= 5000) {
                            targetFile.delete();
                        }
                        handleCancelled(appContext);
                        return;
                    }

                    if (downloaded && targetFile.exists() && targetFile.length() > 5000) {
                        completed++;
                        lastCompletedCount = completed;

                        // Also fetch Ayahs text for offline reading if not already cached in Room DB
                        try {
                            QuranRepository.getInstance(appContext).fetchAyahsFromNetworkIfMissing(surahNum);
                        } catch (Exception ignored) {}
                    }

                    int percentAfter = (int) ((completed / (float) TOTAL_SURAHS) * 100);
                    lastPercent = percentAfter;
                    postProgress(completed, TOTAL_SURAHS, percentAfter, surahBn, surahEn);

                    String updatedNotifText = isBn
                            ? ("ডাউনলোড হচ্ছে: " + BengaliNumberUtil.toBengali(percentAfter) + "% (" + BengaliNumberUtil.toBengali(completed) + "/" + BengaliNumberUtil.toBengali(TOTAL_SURAHS) + ")")
                            : ("Downloading: " + percentAfter + "% (" + completed + "/" + TOTAL_SURAHS + ")");

                    NotificationHelper.updateQuranDownloadNotificationWithActions(
                            appContext,
                            NOTIFICATION_ID_QURAN_DOWNLOAD,
                            title,
                            updatedNotifText,
                            percentAfter,
                            false
                    );
                }

                // Completion
                isDownloading.set(false);
                isPaused.set(false);
                downloadStateLiveData.postValue(DownloadState.COMPLETED);

                NotificationHelper.completeDownloadNotification(
                        appContext,
                        NOTIFICATION_ID_QURAN_DOWNLOAD,
                        title,
                        isBn ? "সম্পূর্ণ ১১৪টি সূরা অফলাইন ডাউনলোড সম্পন্ন হয়েছে" : "All 114 Surahs offline download completed"
                );

                mainHandler.post(() -> {
                    for (DownloadListener l : listeners) {
                        l.onSuccess();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Full Quran download failed: " + e.getMessage(), e);
                isDownloading.set(false);
                isPaused.set(false);
                downloadStateLiveData.postValue(DownloadState.ERROR);
                NotificationHelper.cancelDownloadNotification(appContext, NOTIFICATION_ID_QURAN_DOWNLOAD);

                mainHandler.post(() -> {
                    for (DownloadListener l : listeners) {
                        l.onError(e.getMessage());
                    }
                });
            }
        });
    }

    public void pauseDownload(Context context) {
        if (!isDownloading.get() || isPaused.get()) return;
        isPaused.set(true);
        downloadStateLiveData.postValue(DownloadState.PAUSED);

        Context appContext = context != null ? context.getApplicationContext() : null;
        if (appContext != null) {
            boolean isBn = LocaleManager.isBengali(appContext);
            String title = isBn ? "আল-কুরআন অফলাইন অডিও" : "Complete Quran Offline Audio";
            String pausedText = isBn
                    ? ("ডাউনলোড স্থগিত আছে (" + BengaliNumberUtil.toBengali(lastPercent) + "%) — চালু করতে ক্লিক করুন")
                    : ("Download paused (" + lastPercent + "%) — Tap to resume");

            NotificationHelper.updateQuranDownloadNotificationWithActions(
                    appContext,
                    NOTIFICATION_ID_QURAN_DOWNLOAD,
                    title,
                    pausedText,
                    lastPercent,
                    true
            );
        }

        mainHandler.post(() -> {
            for (DownloadListener l : listeners) {
                l.onPaused();
            }
        });
    }

    public void resumeDownload(Context context) {
        if (!isDownloading.get() || !isPaused.get()) return;
        isPaused.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
        downloadStateLiveData.postValue(DownloadState.DOWNLOADING);

        Context appContext = context != null ? context.getApplicationContext() : null;
        if (appContext != null) {
            boolean isBn = LocaleManager.isBengali(appContext);
            String title = isBn ? "আল-কুরআন অফলাইন অডিও" : "Complete Quran Offline Audio";
            String resumeText = isBn
                    ? ("ডাউনলোড চলছে (" + BengaliNumberUtil.toBengali(lastPercent) + "%)")
                    : ("Downloading (" + lastPercent + "%)");

            NotificationHelper.updateQuranDownloadNotificationWithActions(
                    appContext,
                    NOTIFICATION_ID_QURAN_DOWNLOAD,
                    title,
                    resumeText,
                    lastPercent,
                    false
            );
        }

        mainHandler.post(() -> {
            for (DownloadListener l : listeners) {
                l.onResumed();
            }
        });
    }

    public void cancelDownload(Context context) {
        isCancelled.set(true);
        isDownloading.set(false);
        isPaused.set(false);
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
        downloadStateLiveData.postValue(DownloadState.CANCELLED);

        Context appContext = context != null ? context.getApplicationContext() : null;
        if (appContext != null) {
            NotificationHelper.cancelDownloadNotification(appContext, NOTIFICATION_ID_QURAN_DOWNLOAD);
        }

        mainHandler.post(() -> {
            for (DownloadListener l : listeners) {
                l.onCancelled();
            }
        });
    }

    private void handleCancelled(Context context) {
        isDownloading.set(false);
        isPaused.set(false);
        downloadStateLiveData.postValue(DownloadState.CANCELLED);
        NotificationHelper.cancelDownloadNotification(context, NOTIFICATION_ID_QURAN_DOWNLOAD);

        mainHandler.post(() -> {
            for (DownloadListener l : listeners) {
                l.onCancelled();
            }
        });
    }

    private void postProgress(int completed, int total, int percent, String surahBn, String surahEn) {
        DownloadProgress p = new DownloadProgress(completed, total, percent, surahBn, surahEn);
        downloadProgressLiveData.postValue(p);
        mainHandler.post(() -> {
            for (DownloadListener l : listeners) {
                l.onProgress(p);
            }
        });
    }
}
