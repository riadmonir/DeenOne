package com.devflux.deenone.core.quran;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * QuranAudioCacheManager — Smart local caching & on-demand downloader for Quran audio.
 * Ensures the app stays ultra-lightweight while enabling offline playback after first download/play.
 */
public class QuranAudioCacheManager {

    private static final String TAG = "QuranAudioCache";
    private static final ExecutorService downloadExecutor = Executors.newFixedThreadPool(3);

    public interface DownloadListener {
        void onProgress(int downloadedAyahs, int totalAyahs, int progressPercent);
        void onComplete(int totalAyahs);
        void onError(String message);
    }

    public static File getAudioBaseDir(Context context) {
        File dir = context.getExternalFilesDir("quran_audio");
        if (dir == null) {
            dir = new File(context.getFilesDir(), "quran_audio");
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getAyahAudioFile(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int ayahNumber) {
        String reciterFolder = reciter != null ? reciter.everyAyahFolder : "Alafasy_128kbps";
        File reciterDir = new File(getAudioBaseDir(context), reciterFolder);
        if (!reciterDir.exists()) {
            reciterDir.mkdirs();
        }
        return new File(reciterDir, surahNumber + "_" + ayahNumber + ".mp3");
    }

    public static File getFullSurahAudioFile(Context context, int surahNumber) {
        File fullSurahDir = new File(getAudioBaseDir(context), "full_surah");
        if (!fullSurahDir.exists()) {
            fullSurahDir.mkdirs();
        }
        return new File(fullSurahDir, "surah_" + surahNumber + ".mp3");
    }

    public static File getBanglaSurahAudioFile(Context context, int surahNumber) {
        File banglaDir = new File(getAudioBaseDir(context), "bangla_translation");
        if (!banglaDir.exists()) {
            banglaDir.mkdirs();
        }
        return new File(banglaDir, "surah_" + surahNumber + ".mp3");
    }

    public static boolean isAyahAudioDownloaded(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int ayahNumber) {
        int targetSurah = surahNumber;
        int targetAyah = ayahNumber;
        if (ayahNumber == 0) {
            targetSurah = 1;
            targetAyah = 1;
        }
        File file = getAyahAudioFile(context, reciter, targetSurah, targetAyah);
        return file.exists() && file.length() > 500;
    }

    public static boolean isBanglaSurahAudioDownloaded(Context context, int surahNumber) {
        File file = getBanglaSurahAudioFile(context, surahNumber);
        return file.exists() && file.length() > 10000;
    }

    public static boolean isSurahAudioCached(Context context, int surahNumber) {
        if (context == null) return false;
        File fullSurah = getFullSurahAudioFile(context, surahNumber);
        if (fullSurah.exists() && fullSurah.length() > 5000) {
            return true;
        }
        File bangla = getBanglaSurahAudioFile(context, surahNumber);
        if (bangla.exists() && bangla.length() > 10000) {
            return true;
        }
        return false;
    }

    public static boolean isSurahAudioDownloaded(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int totalAyahs) {
        if (totalAyahs <= 0) return false;
        File fullSurah = getFullSurahAudioFile(context, surahNumber);
        if (fullSurah.exists() && fullSurah.length() > 5000) {
            return true;
        }
        for (int a = 1; a <= totalAyahs; a++) {
            if (!isAyahAudioDownloaded(context, reciter, surahNumber, a)) {
                return false;
            }
        }
        return true;
    }

    public static Uri getBanglaSurahPlaybackUri(Context context, int surahNumber) {
        File localFile = getBanglaSurahAudioFile(context, surahNumber);
        if (localFile.exists() && localFile.length() > 10000) {
            return Uri.fromFile(localFile);
        }
        return Uri.parse(QuranCdnAudioHelper.getBanglaTranslationSurahAudioUrl(surahNumber));
    }

    public static Uri getAyahPlaybackUri(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int ayahNumber) {
        int targetSurah = surahNumber;
        int targetAyah = ayahNumber;
        if (ayahNumber == 0) {
            targetSurah = 1;
            targetAyah = 1;
        }
        // 1. First check selected reciter's local file
        File localFile = getAyahAudioFile(context, reciter, targetSurah, targetAyah);
        if (localFile.exists() && localFile.length() > 500) {
            return Uri.fromFile(localFile);
        }
        // 2. Check if cached under any other reciter
        for (QuranCdnAudioHelper.Reciter r : QuranCdnAudioHelper.Reciter.values()) {
            if (r != reciter) {
                File otherFile = getAyahAudioFile(context, r, targetSurah, targetAyah);
                if (otherFile.exists() && otherFile.length() > 500) {
                    return Uri.fromFile(otherFile);
                }
            }
        }
        return Uri.parse(QuranCdnAudioHelper.getAyahAudioUrl(reciter, targetSurah, targetAyah));
    }

    public static Uri getFullSurahPlaybackUri(Context context, int surahNumber) {
        File localFile = getFullSurahAudioFile(context, surahNumber);
        if (localFile.exists() && localFile.length() > 5000) {
            return Uri.fromFile(localFile);
        }
        return Uri.parse(QuranCdnAudioHelper.getFullSurahAudioUrl(surahNumber));
    }
    public static void cacheAyahAudioInBackground(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int ayahNumber) {
        downloadExecutor.execute(() -> {
            try {
                int targetSurah = surahNumber;
                int targetAyah = ayahNumber;
                if (ayahNumber == 0) {
                    targetSurah = 1;
                    targetAyah = 1;
                }
                File targetFile = getAyahAudioFile(context, reciter, targetSurah, targetAyah);
                if (targetFile.exists() && targetFile.length() > 500) return;

                String urlStr = QuranCdnAudioHelper.getAyahAudioUrl(reciter, targetSurah, targetAyah);
                downloadUrlToFile(urlStr, targetFile);
            } catch (Exception e) {
                Log.w(TAG, "Background cache failed for Ayah " + surahNumber + ":" + ayahNumber + ": " + e.getMessage());
            }
        });
    }

    public static void downloadSurahAudio(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int totalAyahs, DownloadListener listener) {
        downloadExecutor.execute(() -> {
            int notificationId = 2100 + surahNumber;
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            String surahTitle = isBn ? ("সূরা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(surahNumber) + " অডিও") : ("Surah " + surahNumber + " Audio");

            try {
                // Initial notification
                com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                    context, notificationId, surahTitle, isBn ? "ডাউনলোড শুরু হচ্ছে..." : "Starting download...", 0
                );

                // 1. Ensure Surah text is downloaded and cached in Room DB for offline reading
                try {
                    com.devflux.deenone.data.repository.QuranRepository.getInstance(context).fetchAyahsFromNetworkIfMissing(surahNumber);
                } catch (Exception ignored) {}

                // 2. Download each Ayah MP3 sequentially with real-time percentage progress
                int count = 0;
                for (int a = 1; a <= totalAyahs; a++) {
                    File targetFile = getAyahAudioFile(context, reciter, surahNumber, a);
                    if (!targetFile.exists() || targetFile.length() <= 500) {
                        String urlStr = QuranCdnAudioHelper.getAyahAudioUrl(reciter, surahNumber, a);
                        boolean success = downloadUrlToFile(urlStr, targetFile);
                        if (!success) {
                            String fallback = QuranCdnAudioHelper.getAyahFallbackUrl(reciter, surahNumber, a);
                            downloadUrlToFile(fallback, targetFile);
                        }
                    }
                    count++;
                    int percent = (int) ((count / (float) totalAyahs) * 100);
                    if (listener != null) {
                        listener.onProgress(count, totalAyahs, percent);
                    }

                    // Throttle notification updates (every 2 ayahs or 100%) to keep system smooth
                    if (count % 2 == 0 || count == totalAyahs) {
                        String progressText = isBn
                            ? ("ডাউনলোড হচ্ছে: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(percent) + "% (" + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(count) + "/" + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(totalAyahs) + ")")
                            : ("Downloading: " + percent + "% (" + count + "/" + totalAyahs + ")");
                        com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                            context, notificationId, surahTitle, progressText, percent
                        );
                    }
                }

                // Completion notification
                com.devflux.deenone.core.notifications.NotificationHelper.completeDownloadNotification(
                    context, notificationId, surahTitle, isBn ? "সম্পূর্ণ ডাউনলোড সম্পন্ন হয়েছে" : "Download completed successfully"
                );

                if (listener != null) {
                    listener.onComplete(totalAyahs);
                }

                // 3. Opportunistically cache full surah MP3 in the background without blocking the UI
                downloadExecutor.execute(() -> {
                    try {
                        File fullSurahFile = getFullSurahAudioFile(context, surahNumber);
                        if (!fullSurahFile.exists() || fullSurahFile.length() <= 5000) {
                            String fullSurahUrl = QuranCdnAudioHelper.getFullSurahAudioUrl(surahNumber);
                            downloadUrlToFile(fullSurahUrl, fullSurahFile);
                        }
                    } catch (Exception ignored) {}
                });
            } catch (Exception e) {
                Log.e(TAG, "Surah download error: " + e.getMessage());
                com.devflux.deenone.core.notifications.NotificationHelper.cancelDownloadNotification(context, notificationId);
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public static boolean isFullSurahAudioDownloaded(Context context, int surahNumber) {
        File file = getFullSurahAudioFile(context, surahNumber);
        return file.exists() && file.length() > 5000;
    }

    public static boolean isSleepModeSurahsDownloaded(Context context) {
        if (context == null) return false;
        for (int surahNum = 1; surahNum <= 114; surahNum++) {
            if (!isFullSurahAudioDownloaded(context, surahNum)) {
                return false;
            }
        }
        return true;
    }

    public static int getDownloadedFullSurahCount(Context context) {
        if (context == null) return 0;
        int count = 0;
        for (int surahNum = 1; surahNum <= 114; surahNum++) {
            if (isFullSurahAudioDownloaded(context, surahNum)) {
                count++;
            }
        }
        return count;
    }

    public static void cacheFullSurahAudioInBackground(Context context, int surahNumber) {
        downloadExecutor.execute(() -> {
            try {
                File targetFile = getFullSurahAudioFile(context, surahNumber);
                if (targetFile.exists() && targetFile.length() > 5000) return;
                String urlStr = QuranCdnAudioHelper.getFullSurahAudioUrl(surahNumber);
                boolean success = downloadUrlToFile(urlStr, targetFile);
                if (!success) {
                    String surahPadded = String.format(java.util.Locale.US, "%03d", surahNumber);
                    downloadUrlToFile("https://server8.mp3quran.net/afs/" + surahPadded + ".mp3", targetFile);
                }
            } catch (Exception e) {
                Log.w(TAG, "Background cache failed for full Surah " + surahNumber + ": " + e.getMessage());
            }
        });
    }

    public static void downloadFullSurahAudio(Context context, int surahNumber, DownloadListener listener) {
        downloadExecutor.execute(() -> {
            int notificationId = 2300 + surahNumber;
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            String surahTitle = isBn ? ("সূরা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(surahNumber) + " সম্পূর্ণ অডিও") : ("Surah " + surahNumber + " Full Audio");

            try {
                com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                    context, notificationId, surahTitle, isBn ? "ডাউনলোড শুরু হচ্ছে..." : "Starting download...", 0
                );

                File targetFile = getFullSurahAudioFile(context, surahNumber);
                String urlStr = QuranCdnAudioHelper.getFullSurahAudioUrl(surahNumber);

                boolean success = downloadStreamWithProgress(urlStr, targetFile, (bytesRead, totalBytes, percent) -> {
                    if (listener != null) {
                        listener.onProgress((int) (bytesRead / 1024), (int) (totalBytes / 1024), percent);
                    }
                    String progressText = isBn
                        ? ("ডাউনলোড হচ্ছে: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(percent) + "%")
                        : ("Downloading: " + percent + "%");
                    com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                        context, notificationId, surahTitle, progressText, percent
                    );
                });

                if (!success) {
                    String surahPadded = String.format(java.util.Locale.US, "%03d", surahNumber);
                    String fallbackUrl = "https://server8.mp3quran.net/afs/" + surahPadded + ".mp3";
                    success = downloadStreamWithProgress(fallbackUrl, targetFile, (bytesRead, totalBytes, percent) -> {
                        if (listener != null) {
                            listener.onProgress((int) (bytesRead / 1024), (int) (totalBytes / 1024), percent);
                        }
                    });
                }

                if (success && targetFile.exists() && targetFile.length() > 5000) {
                    com.devflux.deenone.core.notifications.NotificationHelper.completeDownloadNotification(
                        context, notificationId, surahTitle, isBn ? "অডিও ডাউনলোড সম্পন্ন হয়েছে" : "Audio download completed"
                    );
                    if (listener != null) {
                        listener.onComplete(100);
                    }
                } else {
                    throw new Exception("Download failed or file corrupted");
                }
            } catch (Exception e) {
                Log.e(TAG, "Full Surah download error: " + e.getMessage());
                com.devflux.deenone.core.notifications.NotificationHelper.cancelDownloadNotification(context, notificationId);
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public interface SleepDownloadListener {
        void onProgress(int completedCount, int totalCount, int percent, String surahNameBn, String surahNameEn);
        void onComplete(int totalCount);
        void onError(String message);
        void onCancelled();
    }

    private static final java.util.concurrent.atomic.AtomicBoolean isSleepDownloading = new java.util.concurrent.atomic.AtomicBoolean(false);
    private static final java.util.concurrent.atomic.AtomicBoolean isSleepCancelled = new java.util.concurrent.atomic.AtomicBoolean(false);

    public static boolean isSleepModeCurrentlyDownloading() {
        return isSleepDownloading.get();
    }

    public static void cancelSleepModeDownload() {
        isSleepCancelled.set(true);
        isSleepDownloading.set(false);
    }

    public static String getSurahNameBn(int surahNum) {
        java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> list = com.devflux.deenone.data.local.QuranSurahDataSeeder.get114Surahs();
        if (list != null && surahNum >= 1 && surahNum <= list.size()) {
            return list.get(surahNum - 1).getNameBengali();
        }
        return "সূরা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(surahNum);
    }

    public static String getSurahNameEn(int surahNum) {
        java.util.List<com.devflux.deenone.data.local.entity.QuranSurahEntity> list = com.devflux.deenone.data.local.QuranSurahDataSeeder.get114Surahs();
        if (list != null && surahNum >= 1 && surahNum <= list.size()) {
            return list.get(surahNum - 1).getNameEnglish();
        }
        return "Surah " + surahNum;
    }

    public static boolean downloadSurahWithCdnFallbacks(int surahNum, File targetFile) {
        String surah3 = String.format(java.util.Locale.US, "%03d", surahNum);
        String[] cdnUrls = {
            "https://server8.mp3quran.net/afs/" + surah3 + ".mp3",
            "https://download.quranicaudio.com/quran/mishaari_raashid_al_3afaasee/" + surah3 + ".mp3",
            "https://cdn.islamic.network/quran/audio-surah/128/ar.alafasy/" + surahNum + ".mp3"
        };
        for (String url : cdnUrls) {
            boolean success = downloadUrlToFile(url, targetFile);
            if (success && targetFile.exists() && targetFile.length() > 5000) {
                return true;
            }
        }
        return false;
    }

    public static void downloadSleepModeSurahs(Context context, SleepDownloadListener listener) {
        if (context == null) return;
        isSleepDownloading.set(true);
        isSleepCancelled.set(false);

        downloadExecutor.execute(() -> {
            int notificationId = 2400;
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            String title = isBn ? "সম্পূর্ণ কুরআন অফলাইন অডিও" : "Complete Quran Offline Audio";
            android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

            try {
                com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                    context, notificationId, title, isBn ? "ডাউনলোড শুরু হচ্ছে..." : "Starting download...", 0
                );

                int total = 114;
                int completed = 0;

                for (int i = 1; i <= 114; i++) {
                    if (isSleepCancelled.get()) {
                        isSleepDownloading.set(false);
                        com.devflux.deenone.core.notifications.NotificationHelper.cancelDownloadNotification(context, notificationId);
                        mainHandler.post(() -> {
                            if (listener != null) listener.onCancelled();
                        });
                        return;
                    }

                    int surahNum = i;
                    String surahBn = getSurahNameBn(surahNum);
                    String surahEn = getSurahNameEn(surahNum);

                    final int currentIdx = i;
                    int percentBefore = (int) (((i - 1) / (float) total) * 100);
                    mainHandler.post(() -> {
                        if (listener != null) {
                            listener.onProgress(currentIdx, total, percentBefore, surahBn, surahEn);
                        }
                    });

                    File targetFile = getFullSurahAudioFile(context, surahNum);
                    if (!targetFile.exists() || targetFile.length() <= 5000) {
                        downloadSurahWithCdnFallbacks(surahNum, targetFile);
                    }

                    completed++;
                    final int finalCompleted = completed;
                    final int percentAfter = (int) ((completed / (float) total) * 100);
                    mainHandler.post(() -> {
                        if (listener != null) {
                            listener.onProgress(finalCompleted, total, percentAfter, surahBn, surahEn);
                        }
                    });

                    String progressText = isBn
                        ? ("ডাউনলোড হচ্ছে: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(percentAfter) + "% (" + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(completed) + "/" + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(total) + ")")
                        : ("Downloading: " + percentAfter + "% (" + completed + "/" + total + ")");
                    com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                        context, notificationId, title, progressText, percentAfter
                    );
                }

                isSleepDownloading.set(false);
                com.devflux.deenone.core.notifications.NotificationHelper.completeDownloadNotification(
                    context, notificationId, title, isBn ? "সম্পূর্ণ ১১৪টি সূরা অফলাইন ডাউনলোড সম্পন্ন হয়েছে" : "All 114 Surahs offline download complete"
                );
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onComplete(total);
                    }
                });
            } catch (Exception e) {
                isSleepDownloading.set(false);
                Log.e(TAG, "Sleep mode download error: " + e.getMessage());
                com.devflux.deenone.core.notifications.NotificationHelper.cancelDownloadNotification(context, notificationId);
                mainHandler.post(() -> {
                    if (listener != null) {
                        listener.onError(e.getMessage());
                    }
                });
            }
        });
    }

    public static void cacheBanglaSurahAudioInBackground(Context context, int surahNumber) {
        downloadExecutor.execute(() -> {
            try {
                File targetFile = getBanglaSurahAudioFile(context, surahNumber);
                if (targetFile.exists() && targetFile.length() > 10000) return;
                String urlStr = QuranCdnAudioHelper.getBanglaTranslationSurahAudioUrl(surahNumber);
                downloadUrlToFile(urlStr, targetFile);
            } catch (Exception e) {
                Log.w(TAG, "Background cache failed for Bangla Surah " + surahNumber + ": " + e.getMessage());
            }
        });
    }

    public static void downloadBanglaSurahAudio(Context context, int surahNumber, DownloadListener listener) {
        downloadExecutor.execute(() -> {
            int notificationId = 2200 + surahNumber;
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            String surahTitle = isBn ? ("সূরা " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(surahNumber) + " বাংলা অনুবাদ অডিও") : ("Surah " + surahNumber + " Bangla Translation Audio");

            try {
                com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                    context, notificationId, surahTitle, isBn ? "ডাউনলোড শুরু হচ্ছে..." : "Starting download...", 0
                );

                File targetFile = getBanglaSurahAudioFile(context, surahNumber);
                String urlStr = QuranCdnAudioHelper.getBanglaTranslationSurahAudioUrl(surahNumber);

                boolean success = downloadStreamWithProgress(urlStr, targetFile, (bytesRead, totalBytes, percent) -> {
                    if (listener != null) {
                        listener.onProgress((int) (bytesRead / 1024), (int) (totalBytes / 1024), percent);
                    }
                    String progressText = isBn
                        ? ("ডাউনলোড হচ্ছে: " + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(percent) + "%")
                        : ("Downloading: " + percent + "%");
                    com.devflux.deenone.core.notifications.NotificationHelper.updateDownloadProgressNotification(
                        context, notificationId, surahTitle, progressText, percent
                    );
                });

                if (success) {
                    com.devflux.deenone.core.notifications.NotificationHelper.completeDownloadNotification(
                        context, notificationId, surahTitle, isBn ? "বাংলা অডিও ডাউনলোড সম্পন্ন হয়েছে" : "Bangla audio download completed"
                    );
                    if (listener != null) {
                        listener.onComplete(100);
                    }
                } else {
                    throw new Exception("Download failed or file corrupted");
                }
            } catch (Exception e) {
                Log.e(TAG, "Bangla Surah download error: " + e.getMessage());
                com.devflux.deenone.core.notifications.NotificationHelper.cancelDownloadNotification(context, notificationId);
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });
    }

    public static long getBanglaSurahDiskSizeBytes(Context context, int surahNumber) {
        File f = getBanglaSurahAudioFile(context, surahNumber);
        return f.exists() ? f.length() : 0;
    }

    public static void deleteBanglaSurahAudio(Context context, int surahNumber) {
        downloadExecutor.execute(() -> {
            try {
                File f = getBanglaSurahAudioFile(context, surahNumber);
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e) {
                Log.w(TAG, "Delete bangla surah audio error: " + e.getMessage());
            }
        });
    }

    public static long getSurahAudioDiskSizeBytes(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int totalAyahs) {
        long totalBytes = 0;
        File fullSurah = getFullSurahAudioFile(context, surahNumber);
        if (fullSurah.exists()) {
            totalBytes += fullSurah.length();
        }
        for (int a = 1; a <= totalAyahs; a++) {
            File f = getAyahAudioFile(context, reciter, surahNumber, a);
            if (f.exists()) {
                totalBytes += f.length();
            }
        }
        return totalBytes;
    }

    public static void deleteSurahAudio(Context context, QuranCdnAudioHelper.Reciter reciter, int surahNumber, int totalAyahs) {
        downloadExecutor.execute(() -> {
            try {
                File fullSurah = getFullSurahAudioFile(context, surahNumber);
                if (fullSurah.exists()) {
                    fullSurah.delete();
                }
                for (int a = 1; a <= totalAyahs; a++) {
                    File f = getAyahAudioFile(context, reciter, surahNumber, a);
                    if (f.exists()) {
                        f.delete();
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Delete surah audio error: " + e.getMessage());
            }
        });
    }

    public interface StreamProgressListener {
        void onProgressUpdate(long bytesRead, long totalBytes, int percent);
    }

    private static boolean downloadStreamWithProgress(String urlStr, File targetFile, StreamProgressListener listener) {
        HttpURLConnection conn = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOneApp/1.0");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);
            conn.connect();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == 307 || status == 302 || status == 301) {
                String newUrl = conn.getHeaderField("Location");
                if (newUrl != null && !newUrl.isEmpty()) {
                    conn.disconnect();
                    url = new URL(newUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOneApp/1.0");
                    conn.setConnectTimeout(10000);
                    conn.setReadTimeout(20000);
                    conn.connect();
                    status = conn.getResponseCode();
                }
            }

            if (status == 200) {
                long contentLength = conn.getContentLengthLong();
                in = conn.getInputStream();
                File tempFile = new File(targetFile.getAbsolutePath() + ".tmp");
                out = new FileOutputStream(tempFile);

                byte[] buffer = new byte[8192];
                long totalRead = 0;
                int len;
                long lastUpdateTime = 0;
                int lastReportedPercent = -1;

                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    totalRead += len;
                    if (contentLength > 0 && listener != null) {
                        int percent = (int) ((totalRead * 100) / contentLength);
                        long now = System.currentTimeMillis();
                        if (percent != lastReportedPercent && (now - lastUpdateTime > 200 || percent == 100)) {
                            lastReportedPercent = percent;
                            lastUpdateTime = now;
                            listener.onProgressUpdate(totalRead, contentLength, percent);
                        }
                    }
                }
                out.flush();
                out.close();
                out = null;

                if (tempFile.length() > 10000) {
                    if (targetFile.exists()) targetFile.delete();
                    boolean renamed = tempFile.renameTo(targetFile);
                    if (!renamed) {
                        try (InputStream src = new java.io.FileInputStream(tempFile);
                             FileOutputStream dst = new FileOutputStream(targetFile)) {
                            byte[] buf = new byte[8192];
                            int b;
                            while ((b = src.read(buf)) != -1) {
                                dst.write(buf, 0, b);
                            }
                            dst.flush();
                            renamed = targetFile.exists() && targetFile.length() > 10000;
                        } catch (Exception ignored) {}
                        tempFile.delete();
                    }
                    return renamed;
                } else {
                    tempFile.delete();
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Stream download failed for " + urlStr + ": " + e.getMessage());
        } finally {
            try { if (in != null) in.close(); } catch (Exception ignored) {}
            try { if (out != null) out.close(); } catch (Exception ignored) {}
            if (conn != null) conn.disconnect();
        }
        return false;
    }

    private static boolean downloadUrlToFile(String urlStr, File targetFile) {
        HttpURLConnection conn = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOneApp/1.0");
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(12000);
            conn.connect();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == 307) {
                String newUrl = conn.getHeaderField("Location");
                if (newUrl != null && !newUrl.isEmpty()) {
                    conn.disconnect();
                    url = new URL(newUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) DeenOneApp/1.0");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(12000);
                    conn.connect();
                    status = conn.getResponseCode();
                }
            }

            if (status == 200) {
                in = conn.getInputStream();
                File tempFile = new File(targetFile.getAbsolutePath() + ".tmp");
                out = new FileOutputStream(tempFile);

                byte[] buffer = new byte[4096];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                out.close();
                out = null;

                if (tempFile.length() > 500) {
                    if (targetFile.exists()) targetFile.delete();
                    boolean renamed = tempFile.renameTo(targetFile);
                    if (!renamed) {
                        try (InputStream src = new java.io.FileInputStream(tempFile);
                             FileOutputStream dst = new FileOutputStream(targetFile)) {
                            byte[] buf = new byte[4096];
                            int b;
                            while ((b = src.read(buf)) != -1) {
                                dst.write(buf, 0, b);
                            }
                            dst.flush();
                            renamed = targetFile.exists() && targetFile.length() > 500;
                        } catch (Exception ignored) {}
                        tempFile.delete();
                    }
                    return renamed;
                } else {
                    tempFile.delete();
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Download failed for " + urlStr + ": " + e.getMessage());
        } finally {
            try { if (in != null) in.close(); } catch (Exception ignored) {}
            try { if (out != null) out.close(); } catch (Exception ignored) {}
            if (conn != null) conn.disconnect();
        }
        return false;
    }
}
