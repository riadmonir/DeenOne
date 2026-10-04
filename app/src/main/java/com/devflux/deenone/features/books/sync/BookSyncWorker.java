package com.devflux.deenone.features.books.sync;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.IslamicBookDao;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.features.books.data.IslamicBookRepository;

import java.util.List;

/**
 * Background WorkManager Worker for periodic Islamic Book metadata synchronization.
 * Only syncs metadata, categories, cover URLs, and catalog availability.
 * NEVER downloads full PDF content files during synchronization to save data and storage.
 */
public class BookSyncWorker extends Worker {

    private static final String TAG = "BookSyncWorker";

    public BookSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Log.d(TAG, "Executing periodic Islamic Book metadata synchronization...");

        if (!NetworkConnectivityHelper.isOnline(context)) {
            Log.d(TAG, "Device is offline. Skipping metadata sync until next cycle.");
            return Result.retry();
        }

        try {
            AppDatabase db = AppDatabase.getInstance(context);
            IslamicBookDao bookDao = db.islamicBookDao();

            // Fetch verified canonical metadata catalog
            List<IslamicBookEntity> canonicalCatalog = IslamicBookRepository.getCanonicalIslamicBooks();

            for (IslamicBookEntity newBook : canonicalCatalog) {
                IslamicBookEntity existing = bookDao.getBookByIdSync(newBook.getId());
                if (existing != null) {
                    // Preserve user local reading progress, downloaded files, and favorites
                    newBook.setDownloaded(existing.isDownloaded());
                    newBook.setLocalFilePath(existing.getLocalFilePath());
                    newBook.setDownloadProgress(existing.getDownloadProgress());
                    newBook.setLastReadPage(existing.getLastReadPage());
                    newBook.setReadingPercentage(existing.getReadingPercentage());
                    newBook.setReadingStatus(existing.getReadingStatus());
                    newBook.setLastOpenedTimestamp(existing.getLastOpenedTimestamp());
                    newBook.setFavorite(existing.isFavorite());
                }
                // Update or insert metadata only
                bookDao.insertOrUpdate(newBook);
            }

            Log.d(TAG, "Successfully synchronized metadata for " + canonicalCatalog.size() + " books.");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Failed to sync book metadata: " + e.getMessage(), e);
            return Result.retry();
        }
    }
}
