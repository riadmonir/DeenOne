package com.devflux.deenone.features.books;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.features.books.data.IslamicBookRepository;
import com.devflux.deenone.features.books.download.BookDownloadManager;

import java.util.List;

public class BooksViewModel extends AndroidViewModel {

    private final IslamicBookRepository repository;
    private final BookDownloadManager downloadManager;

    private final MutableLiveData<String> selectedCategory = new MutableLiveData<>("all");
    private final MutableLiveData<String> selectedLanguage = new MutableLiveData<>("all");
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    private final MediatorLiveData<List<IslamicBookEntity>> filteredBooks = new MediatorLiveData<>();
    private LiveData<List<IslamicBookEntity>> currentSource = null;

    public BooksViewModel(@NonNull Application application) {
        super(application);
        this.repository = IslamicBookRepository.getInstance(application);
        this.downloadManager = BookDownloadManager.getInstance(application);

        filteredBooks.addSource(selectedCategory, cat -> updateBooksSource());
        filteredBooks.addSource(selectedLanguage, lang -> updateBooksSource());
        filteredBooks.addSource(searchQuery, q -> updateBooksSource());

        updateBooksSource();
    }

    private void updateBooksSource() {
        if (currentSource != null) {
            filteredBooks.removeSource(currentSource);
        }

        String query = searchQuery.getValue();
        String cat = selectedCategory.getValue();

        if (query != null && !query.trim().isEmpty()) {
            currentSource = repository.searchBooks(query.trim());
        } else if (cat == null || "all".equalsIgnoreCase(cat)) {
            currentSource = repository.getAllBooks();
        } else if ("downloaded".equalsIgnoreCase(cat)) {
            currentSource = repository.getDownloadedBooks();
        } else if ("favorites".equalsIgnoreCase(cat)) {
            currentSource = repository.getFavoriteBooks();
        } else {
            currentSource = repository.getBooksByCategory(cat);
        }

        filteredBooks.addSource(currentSource, list -> {
            String lang = selectedLanguage.getValue();
            if (list == null) {
                filteredBooks.setValue(new java.util.ArrayList<>());
                return;
            }

            // Duplicate-result prevention & language filtering
            java.util.Set<String> seenIds = new java.util.HashSet<>();
            List<IslamicBookEntity> distinctList = new java.util.ArrayList<>();

            for (IslamicBookEntity b : list) {
                if (b != null && seenIds.add(b.getId())) {
                    if (lang == null || "all".equalsIgnoreCase(lang) || lang.equalsIgnoreCase(b.getLanguage())) {
                        distinctList.add(b);
                    }
                }
            }
            filteredBooks.setValue(distinctList);
        });
    }

    private final android.os.Handler searchDebounceHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private Runnable searchDebounceRunnable;

    public void setSearchQueryDebounced(String query) {
        if (searchDebounceRunnable != null) {
            searchDebounceHandler.removeCallbacks(searchDebounceRunnable);
        }
        searchDebounceRunnable = () -> setSearchQuery(query);
        searchDebounceHandler.postDelayed(searchDebounceRunnable, 300);
    }

    public LiveData<List<IslamicBookEntity>> getBooks() {
        return filteredBooks;
    }

    public LiveData<List<IslamicBookEntity>> getDownloadedBooks() {
        return repository.getDownloadedBooks();
    }

    public LiveData<List<IslamicBookEntity>> getContinueReadingBooks() {
        return repository.getContinueReadingBooks();
    }

    public void setCategory(String categoryKey) {
        if (searchDebounceRunnable != null) {
            searchDebounceHandler.removeCallbacks(searchDebounceRunnable);
        }
        selectedCategory.setValue(categoryKey);
    }

    public void setLanguage(String lang) {
        if (searchDebounceRunnable != null) {
            searchDebounceHandler.removeCallbacks(searchDebounceRunnable);
        }
        selectedLanguage.setValue(lang);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query != null ? query.trim() : "");
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (searchDebounceRunnable != null) {
            searchDebounceHandler.removeCallbacks(searchDebounceRunnable);
        }
    }

    public void toggleFavorite(IslamicBookEntity book) {
        repository.toggleFavorite(book);
    }

    public void downloadBook(IslamicBookEntity book, BookDownloadManager.DownloadProgressListener listener) {
        downloadManager.downloadBook(book, listener);
    }

    public boolean isBookLocallyAvailable(IslamicBookEntity book) {
        return downloadManager.isBookLocallyAvailable(book);
    }

    public java.io.File getLocalBookFile(IslamicBookEntity book) {
        return downloadManager.getLocalBookFile(book);
    }

    public boolean deleteDownloadedBook(IslamicBookEntity book) {
        return downloadManager.deleteDownloadedBook(book);
    }

    public String getOfflineStorageSizeFormatted() {
        long bytes = downloadManager.getTotalOfflineStorageSizeBytes();
        if (bytes <= 0) return "০ MB";
        double mb = bytes / (1024.0 * 1024.0);
        return String.format(java.util.Locale.US, "%.1f MB", mb);
    }

    public void syncOnlineCatalog(IslamicBookRepository.CatalogSyncListener listener) {
        repository.syncOnlineCatalog(listener);
    }

    public LiveData<Boolean> getNetworkStatus() {
        return com.devflux.deenone.core.network.NetworkConnectivityHelper.getNetworkStatusLiveData(getApplication());
    }
}
