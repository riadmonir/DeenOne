package com.devflux.deenone.features.books.pdf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PdfPageAdapter extends RecyclerView.Adapter<PdfPageAdapter.PageViewHolder> {

    private final PdfRenderer pdfRenderer;
    private final int pageCount;
    private final LruCache<Integer, Bitmap> memoryCache;
    private final ExecutorService renderExecutor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private volatile boolean isClosed = false;

    private int targetWidth = 720;
    private int targetHeight = 1018;

    public PdfPageAdapter(@NonNull PdfRenderer pdfRenderer) {
        this(null, pdfRenderer);
    }

    public PdfPageAdapter(Context context, @NonNull PdfRenderer pdfRenderer) {
        this.pdfRenderer = pdfRenderer;
        this.pageCount = pdfRenderer.getPageCount();

        if (context != null) {
            try {
                int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                int renderW = Math.min(800, Math.max(480, screenWidth));
                synchronized (pdfRenderer) {
                    if (pageCount > 0) {
                        PdfRenderer.Page samplePage = pdfRenderer.openPage(0);
                        int origW = Math.max(1, samplePage.getWidth());
                        int origH = Math.max(1, samplePage.getHeight());
                        samplePage.close();
                        float ratio = (float) origH / origW;
                        this.targetWidth = renderW;
                        this.targetHeight = (int) (renderW * ratio);
                    }
                }
            } catch (Exception ignored) {}
        }

        // Allocate 25% of available JVM heap to keep 20-30 pages resident in memory for 60 FPS scrolling
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = Math.max(2048, maxMemory / 4);
        this.memoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null && targetHeight > 0) {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }

    public void prefetchNearby(int currentPos) {
        if (isClosed) return;
        int[] nearby = new int[]{currentPos + 1, currentPos + 2, currentPos - 1};
        for (int p : nearby) {
            if (p >= 0 && p < pageCount && memoryCache.get(p) == null) {
                final int pageIndex = p;
                renderExecutor.execute(() -> renderPageToCache(pageIndex));
            }
        }
    }

    private Bitmap renderPageToCache(int pageIndex) {
        if (isClosed || pageIndex < 0 || pageIndex >= pageCount) return null;
        Bitmap cached = memoryCache.get(pageIndex);
        if (cached != null && !cached.isRecycled()) return cached;

        PdfRenderer.Page page = null;
        try {
            synchronized (pdfRenderer) {
                if (isClosed || pageIndex >= pdfRenderer.getPageCount()) return null;
                page = pdfRenderer.openPage(pageIndex);

                int pWidth = Math.max(1, page.getWidth());
                int pHeight = Math.max(1, page.getHeight());
                float ratio = (float) pHeight / pWidth;
                int renderH = (int) (targetWidth * ratio);

                Bitmap bitmap = Bitmap.createBitmap(targetWidth, renderH, Bitmap.Config.ARGB_8888);
                bitmap.eraseColor(Color.WHITE);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                memoryCache.put(pageIndex, bitmap);
                return bitmap;
            }
        } catch (Throwable t) {
            return null;
        } finally {
            if (page != null) {
                try { page.close(); } catch (Exception ignored) {}
            }
        }
    }

    public void close() {
        isClosed = true;
        renderExecutor.shutdownNow();
        memoryCache.evictAll();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPage;
        private final ProgressBar progressBar;
        private int bindingPageIndex = -1;
        private long currentTaskId = 0;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPage = itemView.findViewById(R.id.ivPdfPage);
            progressBar = itemView.findViewById(R.id.progressPageLoading);
        }

        public void bind(int pageIndex) {
            this.bindingPageIndex = pageIndex;
            final long taskId = ++currentTaskId;

            Bitmap cached = memoryCache.get(pageIndex);
            if (cached != null && !cached.isRecycled()) {
                ivPage.setImageBitmap(cached);
                progressBar.setVisibility(View.GONE);
                prefetchNearby(pageIndex);
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            renderExecutor.execute(() -> {
                if (isClosed || taskId != currentTaskId || bindingPageIndex != pageIndex) return;

                Bitmap bitmap = renderPageToCache(pageIndex);
                if (bitmap != null) {
                    mainHandler.post(() -> {
                        if (!isClosed && taskId == currentTaskId && bindingPageIndex == pageIndex) {
                            ivPage.setImageBitmap(bitmap);
                            progressBar.setVisibility(View.GONE);
                            prefetchNearby(pageIndex);
                        }
                    });
                } else {
                    mainHandler.post(() -> {
                        if (taskId == currentTaskId && bindingPageIndex == pageIndex) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }
    }
}
