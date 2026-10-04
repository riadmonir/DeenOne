package com.devflux.deenone.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncImageLoader {

    private static volatile AsyncImageLoader instance;
    private final LruCache<String, Bitmap> memoryCache;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private File diskCacheDir;

    private AsyncImageLoader(Context context) {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8; // 1/8th of available memory
        this.memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        try {
            diskCacheDir = new File(context.getApplicationContext().getCacheDir(), "halal_img_cache");
            if (!diskCacheDir.exists()) {
                diskCacheDir.mkdirs();
            }
        } catch (Exception ignored) {}
    }

    public static AsyncImageLoader getInstance(Context context) {
        if (instance == null) {
            synchronized (AsyncImageLoader.class) {
                if (instance == null) {
                    instance = new AsyncImageLoader(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public String resolveUrl(Context context, String url) {
        if (url == null || url.trim().isEmpty()) return "";
        url = url.trim();
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://") || url.startsWith("content://")) {
            return url;
        }
        if (url.startsWith("/") && new File(url).exists()) {
            return url;
        }
        String cleanPath = url.startsWith("/") ? url.substring(1) : url;
        String baseUrl = com.devflux.deenone.core.backend.BackendConfigManager.getPhpBaseUrl(context);
        if (baseUrl.endsWith("/")) {
            return baseUrl + cleanPath;
        } else {
            return baseUrl + "/" + cleanPath;
        }
    }

    public void loadImage(ImageView imageView, String imageUrl, int placeholderResId) {
        if (imageView == null) return;

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            if (placeholderResId != 0) {
                imageView.setImageResource(placeholderResId);
            }
            return;
        }

        final String resolvedUrl = resolveUrl(imageView.getContext(), imageUrl);
        imageView.setTag(resolvedUrl);

        // 1. Check Memory Cache (0ms latency)
        Bitmap cached = memoryCache.get(resolvedUrl);
        if (cached != null) {
            imageView.setImageBitmap(cached);
            return;
        }

        // Set placeholder immediately
        if (placeholderResId != 0) {
            imageView.setImageResource(placeholderResId);
        }

        // 2. Load from Disk or CDN Network in Background Thread
        executorService.execute(() -> {
            Bitmap bitmap = loadFromDisk(resolvedUrl);
            if (bitmap == null) {
                if (resolvedUrl.startsWith("http://") || resolvedUrl.startsWith("https://")) {
                    bitmap = downloadFromNetwork(resolvedUrl);
                    if (bitmap != null) {
                        saveToDisk(resolvedUrl, bitmap);
                    }
                } else {
                    File file = new File(resolvedUrl);
                    if (file.exists()) {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    }
                }
            }

            if (bitmap != null) {
                memoryCache.put(resolvedUrl, bitmap);
                final Bitmap finalBitmap = bitmap;
                mainHandler.post(() -> {
                    if (resolvedUrl.equals(imageView.getTag())) {
                        imageView.setImageBitmap(finalBitmap);
                    }
                });
            }
        });
    }

    public void loadCircularImage(ImageView imageView, String imageUrl, int placeholderResId) {
        if (imageView == null) return;

        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            if (placeholderResId != 0) {
                imageView.setImageResource(placeholderResId);
            }
            return;
        }

        final String resolvedUrl = resolveUrl(imageView.getContext(), imageUrl);
        final String cacheKey = resolvedUrl + "_circle";
        imageView.setTag(cacheKey);

        // 1. Check Memory Cache
        Bitmap cached = memoryCache.get(cacheKey);
        if (cached != null) {
            imageView.setImageBitmap(cached);
            return;
        }

        if (placeholderResId != 0) {
            imageView.setImageResource(placeholderResId);
        }

        executorService.execute(() -> {
            Bitmap raw = loadFromDisk(resolvedUrl);
            if (raw == null) {
                if (resolvedUrl.startsWith("http://") || resolvedUrl.startsWith("https://")) {
                    raw = downloadFromNetwork(resolvedUrl);
                    if (raw != null) {
                        saveToDisk(resolvedUrl, raw);
                    }
                } else {
                    File file = new File(resolvedUrl);
                    if (file.exists()) {
                        raw = BitmapFactory.decodeFile(file.getAbsolutePath());
                    }
                }
            }

            if (raw != null) {
                Bitmap circular = getCircularCroppedBitmap(raw);
                if (circular != null) {
                    memoryCache.put(cacheKey, circular);
                    mainHandler.post(() -> {
                        if (cacheKey.equals(imageView.getTag())) {
                            imageView.setImageBitmap(circular);
                        }
                    });
                }
            }
        });
    }

    public static Bitmap getCircularCroppedBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int size = Math.min(width, height);

            int x = (width - size) / 2;
            int y = (height - size) / 2;
            Bitmap squared = Bitmap.createBitmap(bitmap, x, y, size, size);

            Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Rect rect = new Rect(0, 0, size, size);
            RectF rectF = new RectF(rect);

            paint.setColor(0xFF424242);
            canvas.drawOval(rectF, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(squared, rect, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    private String getDiskFileName(String url) {
        return "img_" + Math.abs(url.hashCode()) + ".png";
    }

    private Bitmap loadFromDisk(String url) {
        try {
            if (diskCacheDir == null) return null;
            File file = new File(diskCacheDir, getDiskFileName(url));
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void saveToDisk(String url, Bitmap bitmap) {
        try {
            if (diskCacheDir == null || bitmap == null) return;
            File file = new File(diskCacheDir, getDiskFileName(url));
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (Exception ignored) {}
    }

    private Bitmap downloadFromNetwork(String urlStr) {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setInstanceFollowRedirects(true);
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();
                return BitmapFactory.decodeStream(is);
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (is != null) is.close();
                if (conn != null) conn.disconnect();
            } catch (Exception ignored) {}
        }
        return null;
    }
}
