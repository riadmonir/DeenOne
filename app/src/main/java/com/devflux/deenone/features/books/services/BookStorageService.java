package com.devflux.deenone.features.books.services;

import android.content.Context;

import androidx.annotation.NonNull;

import com.devflux.deenone.data.local.entity.IslamicBookEntity;

import java.io.File;
import java.util.Locale;

/**
 * Service managing local file storage, offline directories, file integrity, and storage calculations.
 */
public class BookStorageService {

    private static volatile BookStorageService instance;
    private final Context context;
    private final File booksDirectory;

    private BookStorageService(Context context) {
        this.context = context.getApplicationContext();
        this.booksDirectory = new File(this.context.getFilesDir(), "islamic_books");
        if (!booksDirectory.exists()) {
            booksDirectory.mkdirs();
        }
    }

    public static synchronized BookStorageService getInstance(Context context) {
        if (instance == null) {
            instance = new BookStorageService(context);
        }
        return instance;
    }

    public File getBooksDirectory() {
        return booksDirectory;
    }

    public File getBookFile(@NonNull IslamicBookEntity book) {
        if (book.getLocalFilePath() != null) {
            File f = new File(book.getLocalFilePath());
            if (f.exists() && f.length() > 0) return f;
        }
        return new File(booksDirectory, "book_" + book.getId() + ".pdf");
    }

    public boolean isBookFilePresent(@NonNull IslamicBookEntity book) {
        File file = getBookFile(book);
        return file.exists() && file.length() > 0;
    }

    public boolean deleteBookFile(@NonNull IslamicBookEntity book) {
        File file = getBookFile(book);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public long getTotalOfflineStorageBytes() {
        if (!booksDirectory.exists() || !booksDirectory.isDirectory()) return 0;
        long total = 0;
        File[] files = booksDirectory.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile()) {
                    total += f.length();
                }
            }
        }
        return total;
    }

    public String getFormattedStorageSize() {
        long bytes = getTotalOfflineStorageBytes();
        if (bytes <= 0) return "০ MB";
        double mb = bytes / (1024.0 * 1024.0);
        return String.format(Locale.US, "%.1f MB", mb);
    }
}
