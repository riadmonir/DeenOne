package com.devflux.deenone.features.books.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.IslamicBookDao;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IslamicBookRepository {

    private static final String TAG = "IslamicBookRepository";
    public static final String GITHUB_CDN_BOOKS_JSON_URL = "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/islamic_books.json";
    public static final String JSDELIVR_CDN_BOOKS_JSON_URL = "https://cdn.jsdelivr.net/gh/riadmonir/DeenOne@main/database/islamic_books.json";

    private static volatile IslamicBookRepository instance;
    private final IslamicBookDao bookDao;
    private final Context context;
    private final ExecutorService networkExecutor = Executors.newSingleThreadExecutor();
    private static final java.util.Map<String, List<com.devflux.deenone.features.books.model.BookChapter>> CHAPTERS_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

    private IslamicBookRepository(Context context) {
        this.context = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        this.bookDao = db.islamicBookDao();
        seedCanonicalBooksIfEmpty();
        preloadChaptersAsync(this.context);
    }

    public static synchronized IslamicBookRepository getInstance(Context context) {
        if (instance == null) {
            instance = new IslamicBookRepository(context);
        }
        return instance;
    }

    private static void preloadChaptersAsync(Context ctx) {
        if (ctx == null) return;
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                android.content.res.AssetManager am = ctx.getAssets();
                java.io.InputStream is = am.open("books/islamic_books.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JsonArray array = JsonParser.parseString(sb.toString()).getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    JsonObject obj = array.get(i).getAsJsonObject();
                    String curId = obj.has("id") ? obj.get("id").getAsString() : "";
                    if (obj.has("chapters") && obj.get("chapters").isJsonArray()) {
                        JsonArray chArray = obj.getAsJsonArray("chapters");
                        List<com.devflux.deenone.features.books.model.BookChapter> chList = new ArrayList<>();
                        for (int j = 0; j < chArray.size(); j++) {
                            JsonObject chObj = chArray.get(j).getAsJsonObject();
                            String chTitle = chObj.has("title") ? chObj.get("title").getAsString() : ("অধ্যায় " + (j + 1));
                            String chContent = chObj.has("content") ? chObj.get("content").getAsString() : "";
                            chList.add(new com.devflux.deenone.features.books.model.BookChapter(chTitle, chContent));
                        }
                        if (!chList.isEmpty()) {
                            CHAPTERS_CACHE.put(curId, chList);
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Asset JSON chapters preload note: " + e.getMessage());
            }
        });
    }

    public static List<com.devflux.deenone.features.books.model.BookChapter> getChaptersForBook(Context context, IslamicBookEntity book) {
        if (book == null || book.getId() == null) return new ArrayList<>();
        String bookId = book.getId();
        if (CHAPTERS_CACHE.containsKey(bookId) && !CHAPTERS_CACHE.get(bookId).isEmpty()) {
            return CHAPTERS_CACHE.get(bookId);
        }

        // 1. Try high-performance SQLite Database Manager
        if (context != null) {
            try {
                List<com.devflux.deenone.features.books.model.BookChapter> sqliteChapters =
                        IslamicBookDatabaseManager.getInstance(context).getChaptersForBook(bookId);
                if (sqliteChapters != null && !sqliteChapters.isEmpty()) {
                    CHAPTERS_CACHE.put(bookId, sqliteChapters);
                    return sqliteChapters;
                }
            } catch (Exception e) {
                Log.w(TAG, "SQLite chapters lookup: " + e.getMessage());
            }
        }

        // 2. Direct synchronous load from bundled assets JSON if cache not yet populated
        if (context != null) {
            try {
                android.content.res.AssetManager am = context.getAssets();
                java.io.InputStream is = am.open("books/islamic_books.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JsonArray array = JsonParser.parseString(sb.toString()).getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    JsonObject obj = array.get(i).getAsJsonObject();
                    String curId = obj.has("id") ? obj.get("id").getAsString() : "";
                    if (obj.has("chapters") && obj.get("chapters").isJsonArray()) {
                        JsonArray chArray = obj.getAsJsonArray("chapters");
                        List<com.devflux.deenone.features.books.model.BookChapter> chList = new ArrayList<>();
                        for (int j = 0; j < chArray.size(); j++) {
                            JsonObject chObj = chArray.get(j).getAsJsonObject();
                            String chTitle = chObj.has("title") ? chObj.get("title").getAsString() : ("অধ্যায় " + (j + 1));
                            String chContent = chObj.has("content") ? chObj.get("content").getAsString() : "";
                            chList.add(new com.devflux.deenone.features.books.model.BookChapter(chTitle, chContent));
                        }
                        if (!chList.isEmpty()) {
                            CHAPTERS_CACHE.put(curId, chList);
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Sync asset JSON load note: " + e.getMessage());
            }
        }

        if (CHAPTERS_CACHE.containsKey(bookId) && !CHAPTERS_CACHE.get(bookId).isEmpty()) {
            return CHAPTERS_CACHE.get(bookId);
        }

        // Fallback to authentic chapters
        List<com.devflux.deenone.features.books.model.BookChapter> fallbackList = new ArrayList<>();
        List<com.devflux.deenone.features.books.download.BookDownloadManager.BookChapter> rawFallback =
                com.devflux.deenone.features.books.download.BookDownloadManager.getAuthenticChaptersForBook(book);
        for (com.devflux.deenone.features.books.download.BookDownloadManager.BookChapter ch : rawFallback) {
            fallbackList.add(new com.devflux.deenone.features.books.model.BookChapter(ch.title, ch.content));
        }
        if (!fallbackList.isEmpty()) {
            CHAPTERS_CACHE.put(bookId, fallbackList);
            return fallbackList;
        }

        List<com.devflux.deenone.features.books.model.BookChapter> single = new ArrayList<>();
        single.add(new com.devflux.deenone.features.books.model.BookChapter(
                book.getTitle(),
                (book.getDescription() != null ? book.getDescription() : "") + "\n\n(এই প্রামাণ্য কিতাবটি সম্পূর্ণ অফলাইনে পড়ার জন্য প্রস্তুত রয়েছে।)"
        ));
        CHAPTERS_CACHE.put(bookId, single);
        return single;
    }

    public LiveData<List<IslamicBookEntity>> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<List<IslamicBookEntity>> getBooksByCategory(String category) {
        if (category == null || "all".equalsIgnoreCase(category) || "সকল".equalsIgnoreCase(category)) {
            return bookDao.getAllBooks();
        }
        return bookDao.getBooksByCategory(category);
    }

    public LiveData<List<IslamicBookEntity>> getBooksByLanguage(String language) {
        return bookDao.getBooksByLanguage(language);
    }

    public LiveData<List<IslamicBookEntity>> getDownloadedBooks() {
        return bookDao.getDownloadedBooks();
    }

    public LiveData<List<IslamicBookEntity>> getFavoriteBooks() {
        return bookDao.getFavoriteBooks();
    }

    public LiveData<List<IslamicBookEntity>> getContinueReadingBooks() {
        return bookDao.getContinueReadingBooks();
    }

    public LiveData<IslamicBookEntity> getBookById(String id) {
        return bookDao.getBookById(id);
    }

    public LiveData<List<IslamicBookEntity>> searchBooks(String query) {
        return bookDao.searchBooks(query);
    }

    public void toggleFavorite(IslamicBookEntity book) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            bookDao.updateFavorite(book.getId(), !book.isFavorite());
        });
    }

    public void updateReadingProgress(String bookId, int currentPage, int totalPages) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            int percentage = totalPages > 0 ? Math.min(100, (currentPage * 100) / totalPages) : 0;
            String status = percentage >= 100 ? "COMPLETED" : "IN_PROGRESS";
            long timestamp = System.currentTimeMillis();
            bookDao.updateReadingProgressDetailed(bookId, currentPage, percentage, status, timestamp);
        });
    }

    public interface CatalogSyncListener {
        void onSuccess(String message);
        void onOffline(String message);
        void onError(String errorMessage);
    }

    public void syncOnlineCatalog(CatalogSyncListener listener) {
        networkExecutor.execute(() -> {
            boolean isOnline = com.devflux.deenone.core.network.NetworkConnectivityHelper.isOnline(context);
            if (!isOnline) {
                if (listener != null) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            listener.onOffline("অফলাইন মোড: ডিভাইসে সংরক্ষিত কিতাবসমূহ প্রদর্শন করা হচ্ছে।"));
                }
                return;
            }

            try {
                List<IslamicBookEntity> allMergedBooks = new ArrayList<>();

                // 1. Sync SQLite Database from GitHub CDN
                IslamicBookDatabaseManager.getInstance(context).ensureDatabaseAvailable(null);

                // 2. Fetch from GitHub CDN JSON
                List<IslamicBookEntity> cdnBooks = fetchBooksFromGitHubCdn();
                if (cdnBooks != null && !cdnBooks.isEmpty()) {
                    allMergedBooks.addAll(cdnBooks);
                }

                // 3. Ensure Canonical baseline is always included
                List<IslamicBookEntity> canonicalBooks = getCanonicalIslamicBooks();
                allMergedBooks.addAll(canonicalBooks);

                // 3. Batch merge & insert into Room DB
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    try {
                        List<IslamicBookEntity> validBooks = new ArrayList<>();
                        java.util.Set<String> seenIds = new java.util.HashSet<>();

                        for (IslamicBookEntity newBook : allMergedBooks) {
                            if (newBook == null || newBook.getId() == null || !seenIds.add(newBook.getId())) {
                                continue;
                            }

                            IslamicBookEntity existing = bookDao.getBookByIdSync(newBook.getId());
                            if (existing != null) {
                                newBook.setDownloaded(existing.isDownloaded());
                                newBook.setLocalFilePath(existing.getLocalFilePath());
                                newBook.setDownloadProgress(existing.getDownloadProgress());
                                newBook.setLastReadPage(existing.getLastReadPage());
                                newBook.setReadingPercentage(existing.getReadingPercentage());
                                newBook.setReadingStatus(existing.getReadingStatus());
                                newBook.setLastOpenedTimestamp(existing.getLastOpenedTimestamp());
                                newBook.setFavorite(existing.isFavorite());
                            }
                            validBooks.add(newBook);
                        }

                        bookDao.insertAll(validBooks);

                        final int finalCount = validBooks.size();
                        if (listener != null) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    listener.onSuccess("অনলাইন লাইব্রেরি সফলভাবে সিঙ্ক সম্পন্ন হয়েছে (" + BengaliNumberUtil.toBengali(finalCount) + "টি কিতাব প্রস্তুত)"));
                        }
                    } catch (Exception dbEx) {
                        Log.e(TAG, "Database update error: " + dbEx.getMessage());
                        if (listener != null) {
                            new Handler(Looper.getMainLooper()).post(() ->
                                    listener.onError("ডাটাবেজ আপডেটে সমস্যা: " + dbEx.getMessage()));
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Online sync error: " + e.getMessage());
                if (listener != null) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            listener.onError("অনলাইন সিঙ্কে সমস্যা: " + e.getMessage()));
                }
            }
        });
    }

    private List<IslamicBookEntity> fetchBooksFromGitHubCdn() {
        String[] urls = new String[]{GITHUB_CDN_BOOKS_JSON_URL, JSDELIVR_CDN_BOOKS_JSON_URL};
        for (String urlStr : urls) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(20000);
                conn.setRequestProperty("User-Agent", "DeenOne-Android/1.0");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();
                    List<IslamicBookEntity> result = new ArrayList<>();
                    long now = System.currentTimeMillis();

                    for (int i = 0; i < arr.size(); i++) {
                        JsonObject obj = arr.get(i).getAsJsonObject();
                        String id = obj.has("id") ? obj.get("id").getAsString() : ("cdn_book_" + (i + 1));
                        String title = obj.has("title") ? obj.get("title").getAsString() : "";
                        String author = obj.has("author") ? obj.get("author").getAsString() : "";
                        String category = obj.has("category") ? obj.get("category").getAsString() : "general";
                        String language = obj.has("language") ? obj.get("language").getAsString() : "bn";
                        int pages = obj.has("page_count") ? obj.get("page_count").getAsInt() : 100;
                        String size = obj.has("file_size") ? obj.get("file_size").getAsString() : "3.5 MB";
                        String desc = obj.has("description") ? obj.get("description").getAsString() : "";
                        String dlUrl = obj.has("download_url") ? obj.get("download_url").getAsString() : "";
                        String publisher = obj.has("publisher") ? obj.get("publisher").getAsString() : "ইসলামিক প্রকাশনী";
                        String source = obj.has("verified_source") ? obj.get("verified_source").getAsString() : "প্রামাণ্য ইসলামিক উৎস";
                        String format = obj.has("format") ? obj.get("format").getAsString() : "PDF";

                        result.add(new IslamicBookEntity(
                                id, title, author, category, language, pages, size, desc,
                                dlUrl, null, false, 0, 0, 0,
                                "NOT_STARTED", 0, false, null, publisher,
                                source, "পাবলিক ডোমেইন / উন্মুক্ত বিতরণ", format, now - (i * 1000L)
                        ));
                    }
                    return result;
                }
            } catch (Exception e) {
                Log.w(TAG, "Failed fetching from CDN URL: " + urlStr + ": " + e.getMessage());
            } finally {
                try { if (reader != null) reader.close(); } catch (Exception ignored) {}
                if (conn != null) conn.disconnect();
            }
        }
        return null;
    }

    private void seedCanonicalBooksIfEmpty() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (bookDao.getBookCount() < 170) {
                IslamicBookDatabaseManager dbManager = IslamicBookDatabaseManager.getInstance(context);
                if (dbManager.isDatabaseReady()) {
                    List<IslamicBookEntity> canonicalBooks = dbManager.getAllBooks();
                    if (canonicalBooks != null && !canonicalBooks.isEmpty()) {
                        for (IslamicBookEntity newBook : canonicalBooks) {
                            IslamicBookEntity existing = bookDao.getBookByIdSync(newBook.getId());
                            if (existing != null) {
                                newBook.setDownloaded(existing.isDownloaded());
                                newBook.setDownloadProgress(existing.getDownloadProgress());
                                newBook.setFavorite(existing.isFavorite());
                                newBook.setLastReadPage(existing.getLastReadPage());
                                newBook.setReadingPercentage(existing.getReadingPercentage());
                                newBook.setReadingStatus(existing.getReadingStatus());
                                newBook.setLastOpenedTimestamp(existing.getLastOpenedTimestamp());
                            }
                        }
                        bookDao.insertAll(canonicalBooks);
                    }
                } else {
                    dbManager.ensureDatabaseAvailable(success -> {
                        if (success) {
                            seedCanonicalBooksIfEmpty();
                        }
                    });
                }
            }
        });
    }

    public static List<IslamicBookEntity> getCanonicalIslamicBooks() {
        return getCanonicalIslamicBooks(null);
    }

    public static List<IslamicBookEntity> getCanonicalIslamicBooks(Context ctx) {
        List<IslamicBookEntity> list = new ArrayList<>();
        long now = System.currentTimeMillis();

        if (ctx != null) {
            try {
                IslamicBookDatabaseManager dbManager = IslamicBookDatabaseManager.getInstance(ctx);
                if (dbManager.isDatabaseReady()) {
                    List<IslamicBookEntity> dbBooks = dbManager.getAllBooks();
                    if (dbBooks != null && !dbBooks.isEmpty()) {
                        return dbBooks;
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Database manager canonical load fallback: " + e.getMessage());
            }

            try {
                android.content.res.AssetManager am = ctx.getAssets();
                java.io.InputStream is = am.open("books/islamic_books.json");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    String id = obj.has("id") ? obj.get("id").getAsString() : ("book_" + (i + 1));
                    String title = obj.has("title") ? obj.get("title").getAsString() : "";
                    String author = obj.has("author") ? obj.get("author").getAsString() : "";
                    String category = obj.has("category") ? obj.get("category").getAsString() : "daily_life";
                    String language = obj.has("language") ? obj.get("language").getAsString() : "bn";
                    int pages = obj.has("page_count") ? obj.get("page_count").getAsInt() : 100;
                    String size = obj.has("file_size") ? obj.get("file_size").getAsString() : "3.5 MB";
                    String desc = obj.has("description") ? obj.get("description").getAsString() : "";
                    String dlUrl = obj.has("download_url") ? obj.get("download_url").getAsString() : "";
                    String publisher = obj.has("publisher") ? obj.get("publisher").getAsString() : "হাদীসবিডি ও ইসলামহাউজ";
                    String source = obj.has("verified_source") ? obj.get("verified_source").getAsString() : "www.hadithbd.com";
                    String format = obj.has("format") ? obj.get("format").getAsString() : "ডিজিタル কিতাব";

                    int chCount = 1;
                    if (obj.has("chapters") && obj.get("chapters").isJsonArray()) {
                        chCount = obj.getAsJsonArray("chapters").size();
                    }

                    list.add(new IslamicBookEntity(
                            id, title, author, category, language, pages, size, desc,
                            dlUrl, null, false, 0, 0, 0,
                            "NOT_STARTED", 0, false, null, publisher,
                            source, "উন্মুক্ত ইসলামিক বিতরণ", format, now - (i * 1000L),
                            chCount
                    ));
                }
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                Log.w(TAG, "Asset JSON canonical load fallback: " + e.getMessage());
            }
        }

        // Fallback baseline book
        list.add(new IslamicBookEntity(
                "book_proshnottore_sahaj_tawhid",
                "প্রশ্নোত্তরে সহজ তাওহীদ শিক্ষা",
                "আব্দুল আলীম ইবনে কাওসার",
                "aqeedah",
                "bn",
                85,
                "3.2 MB",
                "তাওহীদ সম্পর্কে প্রশ্ন এবং উত্তর",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/sahaj_tawhid.json",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "তাওহীদ পাবলিকেশন্স", "তাওহীদ পাবলিকেশন্স ঢাকা",
                "পাবলিক ডোমেইন", "ডিজিটাল কিতাব", now - 1000, 3
        ));
        return list;
    }
}
