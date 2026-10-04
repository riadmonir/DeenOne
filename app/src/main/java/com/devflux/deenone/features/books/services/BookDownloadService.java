package com.devflux.deenone.features.books.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.features.books.download.BookDownloadManager;

import java.io.File;

/**
 * Service managing background streaming PDF downloads and progress listeners.
 */
public class BookDownloadService {

    private static volatile BookDownloadService instance;
    private final BookDownloadManager downloadManager;

    private BookDownloadService(Context context) {
        this.downloadManager = BookDownloadManager.getInstance(context);
    }

    public static synchronized BookDownloadService getInstance(Context context) {
        if (instance == null) {
            instance = new BookDownloadService(context);
        }
        return instance;
    }

    public void downloadBook(@NonNull IslamicBookEntity book, BookDownloadManager.DownloadProgressListener listener) {
        downloadManager.downloadBook(book, listener);
    }

    public boolean isDownloading(@NonNull String bookId) {
        return downloadManager.isDownloading(bookId);
    }

    public boolean isBookLocallyAvailable(@NonNull IslamicBookEntity book) {
        return downloadManager.isBookLocallyAvailable(book);
    }

    public File getLocalBookFile(@NonNull IslamicBookEntity book) {
        return downloadManager.getLocalBookFile(book);
    }

    public boolean deleteDownloadedBook(@NonNull IslamicBookEntity book) {
        return downloadManager.deleteDownloadedBook(book);
    }
}
