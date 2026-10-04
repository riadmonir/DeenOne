package com.devflux.deenone.features.audio.download;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.features.audio.model.IslamicAudioItem;
import com.devflux.deenone.features.audio.repository.AudioLocalRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Handles offline downloading of Islamic audio files with live progress,
 * retry resilience, and offline file tracking.
 */
public class AudioDownloadManager {

    private static final String TAG = "AudioDownloadManager";
    private static final String DIR_AUDIO = "islamic_audio";

    public interface DownloadCallback {
        void onProgress(IslamicAudioItem item, int percentage, long downloadedBytes, long totalBytes);
        void onSuccess(IslamicAudioItem item, File downloadedFile);
        void onError(IslamicAudioItem item, String errorMessage);
    }

    private static volatile AudioDownloadManager instance;

    private final Context appContext;
    private final OkHttpClient httpClient;
    private final AudioLocalRepository localRepo;
    private final ExecutorService downloadExecutor;
    private final Handler mainHandler;
    private final ConcurrentHashMap<String, Boolean> activeDownloads = new ConcurrentHashMap<>();

    private AudioDownloadManager(Context context) {
        this.appContext = context.getApplicationContext();
        this.httpClient = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        this.localRepo = new AudioLocalRepository(appContext);
        this.downloadExecutor = Executors.newFixedThreadPool(2);
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public static AudioDownloadManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AudioDownloadManager.class) {
                if (instance == null) {
                    instance = new AudioDownloadManager(context);
                }
            }
        }
        return instance;
    }

    public boolean isDownloading(String audioId) {
        return Boolean.TRUE.equals(activeDownloads.get(audioId));
    }

    public boolean isDownloaded(String audioId) {
        File f = getLocalAudioFile(audioId);
        return f != null && f.exists() && f.length() > 1024;
    }

    public File getLocalAudioFile(String audioId) {
        File dir = getAudioStorageDir();
        if (dir == null) return null;
        String safeName = sanitizeFilename(audioId) + ".mp3";
        return new File(dir, safeName);
    }

    public File getAudioStorageDir() {
        File dir = appContext.getExternalFilesDir(DIR_AUDIO);
        if (dir == null) {
            dir = new File(appContext.getFilesDir(), DIR_AUDIO);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public void downloadAudio(IslamicAudioItem item, DownloadCallback callback) {
        if (item == null || item.getStreamUrl() == null || item.getStreamUrl().isEmpty()) {
            if (callback != null) callback.onError(item, "অনলাইন স্ট্রিম লিংক পাওয়া যায়নি");
            return;
        }

        String audioId = item.getId();
        if (isDownloaded(audioId)) {
            File local = getLocalAudioFile(audioId);
            item.setDownloaded(true);
            item.setLocalFilePath(local.getAbsolutePath());
            if (callback != null) callback.onSuccess(item, local);
            return;
        }

        if (Boolean.TRUE.equals(activeDownloads.put(audioId, true))) {
            return; // Already downloading
        }

        item.setDownloading(true);
        item.setDownloadProgress(0);

        downloadExecutor.execute(() -> {
            File targetFile = getLocalAudioFile(audioId);
            File tempFile = new File(targetFile.getAbsolutePath() + ".tmp");

            try {
                Request request = new Request.Builder()
                        .url(item.getStreamUrl())
                        .addHeader("User-Agent", "DeenOne-AudioDownloader/1.0")
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        throw new Exception("HTTP error code: " + response.code());
                    }

                    ResponseBody body = response.body();
                    if (body == null) throw new Exception("Empty response body");

                    long totalBytes = body.contentLength();
                    try (InputStream is = body.byteStream();
                         FileOutputStream fos = new FileOutputStream(tempFile)) {

                        byte[] buffer = new byte[8192];
                        long downloaded = 0;
                        int read;
                        int lastReportedPercent = 0;

                        while ((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                            downloaded += read;

                            if (totalBytes > 0) {
                                int percent = (int) ((downloaded * 100) / totalBytes);
                                if (percent != lastReportedPercent) {
                                    lastReportedPercent = percent;
                                    long finalDownloaded = downloaded;
                                    mainHandler.post(() -> {
                                        item.setDownloadProgress(percent);
                                        if (callback != null) {
                                            callback.onProgress(item, percent, finalDownloaded, totalBytes);
                                        }
                                    });
                                }
                            }
                        }
                        fos.flush();
                    }

                    if (tempFile.renameTo(targetFile)) {
                        item.setDownloaded(true);
                        item.setDownloading(false);
                        item.setDownloadProgress(100);
                        item.setLocalFilePath(targetFile.getAbsolutePath());
                        item.setFileSizeBytes(targetFile.length());

                        // Save downloaded state in local repo
                        localRepo.saveDownloadedItem(item);

                        mainHandler.post(() -> {
                            if (callback != null) callback.onSuccess(item, targetFile);
                        });
                    } else {
                        throw new Exception("Failed to rename temporary file");
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Download failed for " + audioId + ": " + e.getMessage());
                if (tempFile.exists()) tempFile.delete();
                item.setDownloading(false);
                mainHandler.post(() -> {
                    if (callback != null) callback.onError(item, "ডাউনলোড ব্যর্থ হয়েছে: " + e.getMessage());
                });
            } finally {
                activeDownloads.remove(audioId);
            }
        });
    }

    public void deleteDownload(String audioId) {
        File f = getLocalAudioFile(audioId);
        if (f != null && f.exists()) {
            f.delete();
        }
        localRepo.removeDownloadedItem(audioId);
    }

    private String sanitizeFilename(String name) {
        if (name == null) return "audio_" + System.currentTimeMillis();
        return name.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}