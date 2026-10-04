package com.devflux.deenone.features.books;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.databinding.BottomSheetRichBookReaderBinding;
import com.devflux.deenone.features.books.data.IslamicBookRepository;
import com.devflux.deenone.features.books.model.BookChapter;
import com.devflux.deenone.utils.TouchAnimationUtil;

import java.util.List;

public class RichBookReaderDialog {

    private static float currentFontSize = 16.5f;

    public static void show(@NonNull Context context, @NonNull IslamicBookEntity book, int initialChapterIndex) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        BottomSheetRichBookReaderBinding binding =
                BottomSheetRichBookReaderBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        boolean isBn = LocaleManager.isBengali(context);

        // Load chapters for this book
        List<BookChapter> chapters = IslamicBookRepository.getChaptersForBook(context, book);
        if (chapters.isEmpty()) {
            chapters.add(new BookChapter(
                    book.getTitle(),
                    (book.getDescription() != null ? book.getDescription() : "") + "\n\n(এই প্রামাণ্য কিতাবটি সম্পূর্ণ অফলাইনে পড়ার জন্য প্রস্তুত রয়েছে।)"
            ));
        }

        final int[] currentIdx = {Math.max(0, Math.min(initialChapterIndex, chapters.size() - 1))};

        // Attach touch animations strictly to buttons (Rule 7)
        TouchAnimationUtil.attachTouchSpring(binding.btnCloseReader);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderSync);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderSearch);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderToc);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderSettings);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderFav);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderBookmark);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderTag);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderTheme);
        TouchAnimationUtil.attachTouchSpring(binding.btnReaderMore);
        TouchAnimationUtil.attachTouchSpring(binding.fabReaderQuickAction);
        TouchAnimationUtil.attachTouchSpring(binding.fabReaderFontAdjust);

        // Title and Subtitle
        binding.tvReaderTopTitle.setText(book.getTitle() != null ? book.getTitle() : "");
        String sub = book.getDescription() != null && !book.getDescription().isEmpty()
                ? book.getDescription()
                : (isBn ? "প্রশ্ন এবং তাঁর উত্তরসমূহ" : "Questions & Answers");
        binding.tvReaderSubtitle.setText(sub);

        // Render current chapter content
        Runnable renderChapter = () -> {
            if (currentIdx[0] >= 0 && currentIdx[0] < chapters.size()) {
                BookChapter chapter = chapters.get(currentIdx[0]);
                binding.tvChapterHeading.setText(chapter.getTitle());
                binding.tvChapterBodyText.setText(chapter.getContent());
                binding.tvChapterBodyText.setTextSize(currentFontSize);
                binding.scrollReaderContent.scrollTo(0, 0);

                // Update Reading Progress in DB
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    AppDatabase.getInstance(context).islamicBookDao()
                            .updateReadingProgressDetailed(
                                    book.getId(),
                                    currentIdx[0] + 1,
                                    (int) (((currentIdx[0] + 1) * 100.0f) / chapters.size()),
                                    currentIdx[0] + 1 >= chapters.size() ? "COMPLETED" : "IN_PROGRESS",
                                    System.currentTimeMillis()
                            );
                });
            }
        };

        renderChapter.run();

        // Close
        binding.btnCloseReader.setOnClickListener(v -> dialog.dismiss());

        // Next / Previous Chapter Toggle (⇄)
        binding.btnReaderSync.setOnClickListener(v -> {
            if (currentIdx[0] + 1 < chapters.size()) {
                currentIdx[0]++;
            } else {
                currentIdx[0] = 0;
            }
            renderChapter.run();
        });

        // Open Table of Contents / Chapters
        binding.btnReaderToc.setOnClickListener(v -> {
            BookChapterIndexDialog.show(context, book);
        });

        // Font Adjust & Appearance
        Runnable toggleFont = () -> {
            currentFontSize = (currentFontSize >= 22.0f) ? 15.0f : (currentFontSize + 2.0f);
            binding.tvChapterBodyText.setTextSize(currentFontSize);
        };
        binding.btnReaderSettings.setOnClickListener(v -> toggleFont.run());
        binding.fabReaderFontAdjust.setOnClickListener(v -> toggleFont.run());

        // Favorite Toggle (♡)
        final boolean[] isFav = {book.isFavorite()};
        binding.btnReaderFav.setImageResource(isFav[0] ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        binding.btnReaderFav.setOnClickListener(v -> {
            isFav[0] = !isFav[0];
            book.setFavorite(isFav[0]);
            binding.btnReaderFav.setImageResource(isFav[0] ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
            AppDatabase.databaseWriteExecutor.execute(() -> {
                AppDatabase.getInstance(context).islamicBookDao()
                        .updateFavorite(book.getId(), isFav[0]);
            });
        });

        // Share & More
        binding.btnReaderMore.setOnClickListener(v -> {
            if (currentIdx[0] < chapters.size()) {
                BookChapter ch = chapters.get(currentIdx[0]);
                android.content.Intent shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, book.getTitle());
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, book.getTitle() + "\n\n" + ch.getTitle() + "\n\n" + ch.getContent());
                context.startActivity(android.content.Intent.createChooser(shareIntent, isBn ? "কিতাবের বিষয় শেয়ার করুন" : "Share Chapter"));
            }
        });

        binding.fabReaderQuickAction.setOnClickListener(v -> {
            BookChapterIndexDialog.show(context, book);
        });

        dialog.show();
    }
}
