package com.devflux.deenone.features.books.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.IslamicBookDao;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;

/**
 * Service managing reading positions, page tracking, percentage completion, and reading history.
 */
public class ReadingProgressService {

    private static volatile ReadingProgressService instance;
    private final IslamicBookDao bookDao;

    private ReadingProgressService(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.bookDao = db.islamicBookDao();
    }

    public static synchronized ReadingProgressService getInstance(Context context) {
        if (instance == null) {
            instance = new ReadingProgressService(context);
        }
        return instance;
    }

    /**
     * Records an update to the current book's reading state.
     */
    public void recordReadingProgress(@NonNull String bookId, int currentPage, int totalPages) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int percentage = totalPages > 0 ? Math.min(100, (currentPage * 100) / totalPages) : 0;
            String status = percentage >= 100 ? "COMPLETED" : "IN_PROGRESS";
            long timestamp = System.currentTimeMillis();
            bookDao.updateReadingProgressDetailed(bookId, currentPage, percentage, status, timestamp);
        });
    }

    /**
     * Resets reading progress for a specific book.
     */
    public void resetProgress(@NonNull String bookId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.updateReadingProgressDetailed(bookId, 0, 0, "NOT_STARTED", 0);
        });
    }
}
