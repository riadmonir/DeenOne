package com.devflux.deenone.features.salahguide;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.pdf.PdfRenderer;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.databinding.ItemSalahVerticalPdfPageBinding;
import com.devflux.deenone.databinding.PageSalahVerticalPdfReaderBinding;
import com.devflux.deenone.features.salahguide.data.SalahBooksRepository;
import com.devflux.deenone.features.salahguide.model.SalahBookModel;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.TouchAnimationUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Super-fast 60 FPS Vertical Continuous Scrolling PDF Reader for Salah Books.
 * Supports smooth vertical scrolling, LRU bitmap caching, night mode, jump-to-page, and sharing.
 */
public class SalahVerticalPdfReaderDialog {

    public static void show(Activity activity, SalahBookModel book) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

        boolean isBn = LocaleManager.isBengali(activity);

        FullScreenPageDialog dialog = new FullScreenPageDialog(activity);
        PageSalahVerticalPdfReaderBinding binding = PageSalahVerticalPdfReaderBinding.inflate(activity.getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        // Header Information
        binding.tvReaderBookTitle.setText(book.getTitle(isBn));
        binding.tvReaderBookAuthor.setText(book.getAuthor(isBn));
        binding.tvScrollDirectionHint.setText(isBn ? "ভার্টিক্যাল স্ক্রল" : "Vertical Scroll");

        // Back Button
        binding.btnBackVerticalReader.setOnClickListener(v -> dialog.dismiss());
        TouchAnimationUtil.attachTouchSpring(binding.btnBackVerticalReader);

        // Share Button
        binding.btnShareBook.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, book.getTitle(isBn));
            String shareText = "📖 " + book.getTitle(isBn) + "\n" +
                    (isBn ? "লেখক: " : "Author: ") + book.getAuthor(isBn) + "\n\n" +
                    book.getSummary(isBn) + "\n\n" +
                    "দ্বীনওয়ান — আপনার প্রতিদিনের ইসলামিক জীবনসঙ্গী";
            intent.putExtra(Intent.EXTRA_TEXT, shareText);
            activity.startActivity(Intent.createChooser(intent, isBn ? "শেয়ার করুন" : "Share Via"));
        });
        TouchAnimationUtil.attachTouchSpring(binding.btnShareBook);

        // Setup Vertical RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.rvVerticalPdfPages.setLayoutManager(layoutManager);

        // Loading indicator
        binding.layoutReaderLoading.setVisibility(View.VISIBLE);
        binding.tvReaderLoadingStatus.setText(isBn ? "বই প্রস্তুত করা হচ্ছে..." : "Loading book...");

        // Load / Prepare PDF in background
        new Thread(() -> {
            File pdfFile = loadOrDownloadPdf(activity, book);

            activity.runOnUiThread(() -> {
                if (activity.isFinishing() || activity.isDestroyed()) return;

                if (pdfFile != null && pdfFile.exists() && pdfFile.length() > 0) {
                    try {
                        ParcelFileDescriptor pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
                        PdfRenderer renderer = new PdfRenderer(pfd);
                        int totalPages = renderer.getPageCount();

                        binding.layoutReaderLoading.setVisibility(View.GONE);
                        binding.scrollRichTextFallback.setVisibility(View.GONE);
                        binding.rvVerticalPdfPages.setVisibility(View.VISIBLE);
                        binding.layoutReaderBottomBar.setVisibility(View.VISIBLE);

                        VerticalPdfAdapter adapter = new VerticalPdfAdapter(activity, renderer);
                        binding.rvVerticalPdfPages.setAdapter(adapter);

                        // Scrubber setup
                        binding.seekBarVerticalProgress.setMax(Math.max(0, totalPages - 1));

                        Runnable updateProgress = () -> {
                            int firstVisible = layoutManager.findFirstVisibleItemPosition();
                            int curPage = Math.max(1, Math.min(firstVisible + 1, totalPages));
                            int pct = (curPage * 100) / totalPages;

                            String progressStr = isBn ?
                                    "পৃষ্ঠা " + BengaliNumberUtil.toBengali(curPage) + " / " + BengaliNumberUtil.toBengali(totalPages) + " • " + BengaliNumberUtil.toBengali(pct) + "% সম্পন্ন" :
                                    "Page " + curPage + " / " + totalPages + " • " + pct + "% Completed";

                            binding.tvPageProgressText.setText(progressStr);
                            binding.seekBarVerticalProgress.setProgress(curPage - 1);
                        };

                        updateProgress.run();

                        // Scroll Listener for smooth progress update
                        binding.rvVerticalPdfPages.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                updateProgress.run();
                            }
                        });

                        // Scrubber listener
                        binding.seekBarVerticalProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if (fromUser) {
                                    binding.rvVerticalPdfPages.scrollToPosition(progress);
                                }
                            }
                            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
                        });

                        // Jump to Page
                        binding.btnJumpPage.setOnClickListener(v -> {
                            showJumpPageDialog(activity, totalPages, binding.rvVerticalPdfPages, isBn);
                        });
                        TouchAnimationUtil.attachTouchSpring(binding.btnJumpPage);

                        // Night Mode Toggle
                        binding.btnNightModeToggle.setOnClickListener(v -> {
                            boolean nextNight = !adapter.isNightMode();
                            adapter.setNightMode(nextNight);
                            binding.layoutVerticalReaderRoot.setBackgroundColor(nextNight ? Color.parseColor("#0F172A") : Color.parseColor("#F8FAFC"));
                            binding.layoutReaderTopBar.setBackgroundColor(nextNight ? Color.parseColor("#1E293B") : Color.parseColor("#FFFFFF"));
                            binding.layoutReaderBottomBar.setBackgroundColor(nextNight ? Color.parseColor("#1E293B") : Color.parseColor("#FFFFFF"));
                            Toast.makeText(activity, isBn ? (nextNight ? "নাইট মোড সক্রিয়" : "স্বাভাবিক রিডিং মোড") : (nextNight ? "Night Mode Active" : "Light Mode Active"), Toast.LENGTH_SHORT).show();
                        });
                        TouchAnimationUtil.attachTouchSpring(binding.btnNightModeToggle);

                        // Cleanup on Dismiss
                        dialog.setOnDismissListener(d -> {
                            try {
                                adapter.close();
                                renderer.close();
                                pfd.close();
                            } catch (Exception ignored) {}
                        });

                    } catch (Exception e) {
                        showFallbackTextMode(binding, book, isBn);
                    }
                } else {
                    showFallbackTextMode(binding, book, isBn);
                }
            });
        }).start();

        dialog.show();
    }

    private static File loadOrDownloadPdf(Context context, SalahBookModel book) {
        File dir = new File(context.getCacheDir(), "salah_books_pdf");
        if (!dir.exists()) dir.mkdirs();

        File localFile = new File(dir, book.getId() + ".pdf");
        if (localFile.exists() && localFile.length() > 500) {
            return localFile;
        }

        // Try downloading online CDN PDF if network available
        try {
            if (book.getPdfUrl() != null && book.getPdfUrl().startsWith("http")) {
                URL url = new URL(book.getPdfUrl());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    FileOutputStream out = new FileOutputStream(localFile);
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    in.close();
                    if (localFile.length() > 1000) {
                        return localFile;
                    }
                }
            }
        } catch (Exception ignored) {}

        // Fallback: Generate authentic local PDF document
        return SalahBooksRepository.getOrCreateLocalPdf(context, book);
    }

    private static void showFallbackTextMode(PageSalahVerticalPdfReaderBinding binding, SalahBookModel book, boolean isBn) {
        binding.layoutReaderLoading.setVisibility(View.GONE);
        binding.rvVerticalPdfPages.setVisibility(View.GONE);
        binding.scrollRichTextFallback.setVisibility(View.VISIBLE);
        binding.layoutReaderBottomBar.setVisibility(View.GONE);

        StringBuilder sb = new StringBuilder();
        List<String> titles = book.getChapterTitles();
        List<String> contents = book.getChapterContents();

        for (int i = 0; i < titles.size(); i++) {
            sb.append("📌 ").append(titles.get(i)).append("\n\n");
            sb.append(contents.get(i)).append("\n\n────────────────────────\n\n");
        }

        binding.tvFallbackChapterHeading.setText(book.getTitle(isBn));
        binding.tvFallbackChapterBody.setText(sb.toString().trim());
    }

    private static void showJumpPageDialog(Activity activity, int totalPages, RecyclerView recyclerView, boolean isBn) {
        EditText input = new EditText(activity);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint(isBn ? "১ থেকে " + BengaliNumberUtil.toBengali(totalPages) : "1 to " + totalPages);
        input.setPadding(48, 32, 48, 32);

        new MaterialAlertDialogBuilder(activity)
                .setTitle(isBn ? "পৃষ্ঠা নম্বরে যান" : "Jump to Page")
                .setView(input)
                .setPositiveButton(isBn ? "যান" : "Go", (dialog, which) -> {
                    try {
                        int page = Integer.parseInt(input.getText().toString().trim());
                        if (page >= 1 && page <= totalPages) {
                            recyclerView.scrollToPosition(page - 1);
                        } else {
                            Toast.makeText(activity, isBn ? "সঠিক পৃষ্ঠা নম্বর দিন" : "Invalid page number", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ignored) {}
                })
                .setNegativeButton(isBn ? "বাতিল" : "Cancel", null)
                .show();
    }

    /**
     * High performance Vertical PDF RecyclerView Adapter with LRU Cache.
     */
    private static class VerticalPdfAdapter extends RecyclerView.Adapter<VerticalPdfAdapter.PageViewHolder> {
        private final Context context;
        private final PdfRenderer pdfRenderer;
        private final int pageCount;
        private final LruCache<Integer, Bitmap> memoryCache;
        private final ExecutorService renderExecutor = Executors.newFixedThreadPool(2);
        private final Handler mainHandler = new Handler(Looper.getMainLooper());
        private boolean isNightMode = false;

        public VerticalPdfAdapter(Context context, @NonNull PdfRenderer pdfRenderer) {
            this.context = context;
            this.pdfRenderer = pdfRenderer;
            this.pageCount = pdfRenderer.getPageCount();

            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 6;
            this.memoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(Integer key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }

        public void setNightMode(boolean nightMode) {
            this.isNightMode = nightMode;
            notifyDataSetChanged();
        }

        public boolean isNightMode() {
            return isNightMode;
        }

        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemSalahVerticalPdfPageBinding binding = ItemSalahVerticalPdfPageBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false
            );
            return new PageViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return pageCount;
        }

        public void close() {
            renderExecutor.shutdown();
            memoryCache.evictAll();
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            final ItemSalahVerticalPdfPageBinding binding;

            public PageViewHolder(@NonNull ItemSalahVerticalPdfPageBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void bind(int pageIndex) {
                binding.progressVerticalPageLoading.setVisibility(View.VISIBLE);
                binding.ivVerticalPdfPage.setImageBitmap(null);

                Bitmap cached = memoryCache.get(pageIndex);
                if (cached != null) {
                    applyBitmap(cached);
                    binding.progressVerticalPageLoading.setVisibility(View.GONE);
                    return;
                }

                renderExecutor.execute(() -> {
                    try {
                        synchronized (pdfRenderer) {
                            if (pageIndex < 0 || pageIndex >= pdfRenderer.getPageCount()) return;
                            PdfRenderer.Page page = pdfRenderer.openPage(pageIndex);

                            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                            int targetWidth = Math.max(1080, screenWidth);
                            float ratio = (float) targetWidth / page.getWidth();
                            int targetHeight = (int) (page.getHeight() * ratio);

                            Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
                            bitmap.eraseColor(Color.WHITE);

                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                            page.close();

                            memoryCache.put(pageIndex, bitmap);

                            mainHandler.post(() -> {
                                if (getBindingAdapterPosition() == pageIndex) {
                                    applyBitmap(bitmap);
                                    binding.progressVerticalPageLoading.setVisibility(View.GONE);
                                }
                            });
                        }
                    } catch (Exception e) {
                        mainHandler.post(() -> binding.progressVerticalPageLoading.setVisibility(View.GONE));
                    }
                });
            }

            private void applyBitmap(Bitmap bitmap) {
                binding.ivVerticalPdfPage.setImageBitmap(bitmap);
                if (isNightMode) {
                    ColorMatrix cm = new ColorMatrix(new float[]{
                            -1f, 0, 0, 0, 255,
                            0, -1f, 0, 0, 255,
                            0, 0, -1f, 0, 255,
                            0, 0, 0, 1f, 0
                    });
                    binding.ivVerticalPdfPage.setColorFilter(new ColorMatrixColorFilter(cm));
                } else {
                    binding.ivVerticalPdfPage.setColorFilter(null);
                }
            }
        }
    }
}
