package com.devflux.deenone.features.books.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.devflux.deenone.features.books.data.IslamicBookRepository;
import com.devflux.deenone.features.books.sync.BookSyncScheduler;

/**
 * Service orchestrating catalog metadata synchronization across foreground and background workflows.
 */
public class BookSyncService {

    private static volatile BookSyncService instance;
    private final Context context;
    private final IslamicBookRepository repository;

    private BookSyncService(Context context) {
        this.context = context.getApplicationContext();
        this.repository = IslamicBookRepository.getInstance(context);
    }

    public static synchronized BookSyncService getInstance(Context context) {
        if (instance == null) {
            instance = new BookSyncService(context);
        }
        return instance;
    }

    /**
     * Executes manual on-demand foreground metadata sync.
     */
    public void syncNow(IslamicBookRepository.CatalogSyncListener listener) {
        repository.syncOnlineCatalog(listener);
    }

    /**
     * Enqueues background sync via WorkManager scheduler.
     */
    public void scheduleBackgroundSync() {
        BookSyncScheduler.schedulePeriodicSync(context);
    }
}
