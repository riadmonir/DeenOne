package com.devflux.deenone.features.books.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.IslamicBookDao;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;

import java.util.List;

/**
 * Service managing book searching, multi-criteria filtering, and keyword normalization.
 */
public class BookSearchService {

    private static volatile BookSearchService instance;
    private final IslamicBookDao bookDao;

    private BookSearchService(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.bookDao = db.islamicBookDao();
    }

    public static synchronized BookSearchService getInstance(Context context) {
        if (instance == null) {
            instance = new BookSearchService(context);
        }
        return instance;
    }

    public LiveData<List<IslamicBookEntity>> searchBooks(@NonNull String query) {
        String sanitized = query.trim();
        return bookDao.searchBooks(sanitized);
    }
}
