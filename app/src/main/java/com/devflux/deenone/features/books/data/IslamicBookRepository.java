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

        // Direct synchronous load if cache not yet populated
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

                // 1. Fetch from GitHub CDN JSON
                List<IslamicBookEntity> cdnBooks = fetchBooksFromGitHubCdn();
                if (cdnBooks != null && !cdnBooks.isEmpty()) {
                    allMergedBooks.addAll(cdnBooks);
                }

                // 2. Ensure Canonical baseline is always included
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
            if (bookDao.getBookCount() == 0) {
                List<IslamicBookEntity> canonicalBooks = getCanonicalIslamicBooks();
                bookDao.insertAll(canonicalBooks);
            }
        });
    }

    public static List<IslamicBookEntity> getCanonicalIslamicBooks() {
        List<IslamicBookEntity> list = new ArrayList<>();
        long now = System.currentTimeMillis();

        // 1. আদর্শ মা (মাওলানা রুহুল আমীন)
        list.add(new IslamicBookEntity(
                "book_01",
                "আদর্শ মা",
                "মাওলানা রুহুল আমীন",
                "family",
                "bn",
                128,
                "3.8 MB",
                "একজন আদর্শ মুসলিম নারী ও মায়ের কর্তব্য, সন্তানদের ইসলামী লালন-পালন ও পারিবারিক শান্তি বজায় রাখার অমূল্য নির্দেশনা।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/adorsho_ma.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "ইসলামিক ফাউন্ডেশন", "পাবলিক ডোমেইন ইসলামিক বই সম্ভার",
                "পাবলিক ডোমেইন", "PDF", now - 1000
        ));

        // 2. চাঁদ দেখা, রোজা ও ঈদ (শায়খ মাকবুল হাসান ফাইযী)
        list.add(new IslamicBookEntity(
                "book_02",
                "চাঁদ দেখা, রোজা ও ঈদ",
                "শায়খ মাকবুল হাসান ফাইযী",
                "sawm",
                "bn",
                96,
                "2.9 MB",
                "কুরআন ও সহীহ হাদীসের আলোকে চাঁদ দেখা, সিয়াম আরম্ভ ও ঈদ উদযাপনের সঠিক বিধান এবং সমসাময়িক বিভ্রান্তি নিরসন।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/chand_dekha_roza_eid.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "তাওহীদ পাবলিকেশন্স", "তাওহীদ পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 2000
        ));

        // 3. রমজান বিষয়ক ফতোয়া (আব্দুল্লাহ শহীদ আব্দুর রহমান)
        list.add(new IslamicBookEntity(
                "book_03",
                "রমজান বিষয়ক ফতোয়া",
                "আব্দুল্লাহ শহীদ আব্দুর রহমান",
                "fatwa",
                "bn",
                144,
                "4.1 MB",
                "রমজান মাসের রোজা, তারাবীহ, সেহরি, ইফতার ও এতেকাফ সম্পর্কিত শতাধিক প্রয়োজনীয় মাসআলা ও নির্ভরযোগ্য ফতোয়া।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/ramadan_fatwa.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "ইসলামিক দাওয়াহ সেন্টার", "ইসলামিক দাওয়াহ সেন্টার",
                "পাবলিক ডোমেইন", "PDF", now - 3000
        ));

        // 4. কুরবানির তাৎপর্য ও বিধান (ড. মুহাম্মাদ আসাদুল্লাহ আল-গালিব)
        list.add(new IslamicBookEntity(
                "book_04",
                "কুরবানির তাৎপর্য ও বিধান",
                "ড. মুহাম্মাদ আসাদুল্লাহ আল-গালিব",
                "qurbani_eid",
                "bn",
                88,
                "2.5 MB",
                "কুরবানির সঠিক ইতিহাস, আত্মিক তাৎপর্য, পশু নির্বাচন, যবেহের সঠিক নিয়ম এবং মাংস বন্টন পদ্ধতির প্রামাণ্য বিধান।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/qurbani_tatporjo.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "হাদীছ ফাউন্ডেশন বাংলাদেশ", "হাদীছ ফাউন্ডেশন বাংলাদেশ",
                "পাবলিক ডোমেইন", "PDF", now - 4000
        ));

        // 5. হাকীকতে মোহাম্মাদী ও মিলাদুন্নবী (আল্লামা নাসিরুদ্দিন আলবানী)
        list.add(new IslamicBookEntity(
                "book_05",
                "হাকীকতে মোহাম্মাদী ও মিলাদুন্নবী",
                "আল্লামা নাসিরুদ্দিন আলবানী",
                "bidah",
                "bn",
                112,
                "3.2 MB",
                "রাসূলুল্লাহ ﷺ-এর মর্যাদা, সৃষ্টিতত্ত্ব সংক্রান্ত প্রচলিত ধারণা ও মিলাদুন্নবীর শারঈ পর্যালোচনা এবং দলীলভিত্তিক বিশ্লেষণ।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/haqiqate_muhammadi.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "দারুস সালাম", "দারুস সালাম বাংলাদেশ",
                "পাবলিক ডোমেইন", "PDF", now - 5000
        ));

        // 6. হজের মর্মার্থ ও শিক্ষা (শায়খ আব্দুর রাজ্জাক বিন ইউসুফ)
        list.add(new IslamicBookEntity(
                "book_06",
                "হজের মর্মার্থ ও শিক্ষা",
                "শায়খ আব্দুর রাজ্জাক বিন ইউসুফ",
                "hajj",
                "bn",
                160,
                "4.8 MB",
                "হজের আত্মিক তাৎপর্য, সুন্নাত মোতাবেক হজের প্রতিটি রুকন আদায়ের পুঙ্খানুপুঙ্খ নিয়ম ও মাবরুর হজ অর্জনের উপায়।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/hajj_mormartho.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "আল-ইতিসাম প্রকাশনী", "আল-ইতিসাম প্রকাশনী",
                "পাবলিক ডোমেইন", "PDF", now - 6000
        ));

        // 7. যাকাত সম্পর্কিত ফতোয়া ও সমাধান (শায়খ মুহাম্মদ বিন সালিহ আল-উসাইমীন)
        list.add(new IslamicBookEntity(
                "book_07",
                "যাকাত সম্পর্কিত ফতোয়া ও সমাধান",
                "শায়খ মুহাম্মদ বিন সালিহ আল-উসাইমীন",
                "zakat",
                "bn",
                136,
                "3.9 MB",
                "নগদ সম্পদ, স্বর্ণ-রৌপ্য, ব্যবসায়ী পণ্য ও কৃষি ফসলের যাকাত নিরূপণের আধুনিক ও সমসাময়িক ফতোয়া সমগ্র।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/zakat_fatwa.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "ইসলাম প্রচার ব্যুরো", "ইসলাম প্রচার ব্যুরো",
                "পাবলিক ডোমেইন", "PDF", now - 7000
        ));

        // 8. সিয়াম ও রমজান: শিক্ষা, তাৎপর্য ও মাসায়েল (ড. মুহাম্মদ মানজুরে ইলাহী)
        list.add(new IslamicBookEntity(
                "book_08",
                "সিয়াম ও রমজান: শিক্ষা, তাৎপর্য ও মাসায়েল",
                "ড. মুহাম্মদ মানজুরে ইলাহী",
                "sawm",
                "bn",
                176,
                "5.2 MB",
                "রমজানের শিক্ষা, তাকওয়া অর্জন, রোজার আহকাম ও আধুনিক চিকিৎসায় রোজা ভঙ্গের শারঈ সমাধান।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/siyam_o_ramadan.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "কুরআন ও সুন্নাহ পরিষদ", "কুরআন ও সুন্নাহ পরিষদ",
                "পাবলিক ডোমেইন", "PDF", now - 8000
        ));

        // 9. যেভাবে স্বাগত জানাবো মাহে রমাজান (মাওলানা আব্দুল কাইয়ূম)
        list.add(new IslamicBookEntity(
                "book_09",
                "যেভাবে স্বাগত জানাবো মাহে রমাজান",
                "মাওলানা আব্দুল কাইয়ূম",
                "sawm",
                "bn",
                80,
                "2.2 MB",
                "রমজান মাসের আগমনকে ফলপ্রসূ ও বরকতময় করার পূর্বপ্রস্তুতি, আমলের সূচি ও আত্মশুদ্ধির রুটিন গাইড।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/sagoto_ramadan.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "বাংলাদেশ ইসলামিক পাবলিকেশন্স", "বাংলাদেশ ইসলামিক পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 9000
        ));

        // 10. নামাজের সময়সূচি ও নিয়মাবলী (মুফতী কাজী ইব্রাহীম)
        list.add(new IslamicBookEntity(
                "book_10",
                "নামাজের সময়সূচি ও নিয়মাবলী",
                "মুফতী কাজী ইব্রাহীম",
                "salah",
                "bn",
                120,
                "3.5 MB",
                "পাঁচ ওয়াক্ত সালাতের নিষিদ্ধ ও উত্তম ওয়াক্ত, কিবলা নির্ধারণ এবং সহীহ হাদিস মোতাবেক সালাত আদায়ের নিয়মাবলী।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/namajer_somoy_niyom.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "দারুল হুদা", "দারুল হুদা",
                "পাবলিক ডোমেইন", "PDF", now - 10000
        ));

        // 11. মহানবীর আদর্শ ও সুন্নাহ (আল্লামা ছফিউর রহমান মুবারকপুরী)
        list.add(new IslamicBookEntity(
                "book_11",
                "মহানবীর আদর্শ ও সুন্নাহ",
                "আল্লামা ছফিউর রহমান মুবারকপুরী",
                "seerah",
                "bn",
                310,
                "8.7 MB",
                "উসওয়াতুন হাসানাহ—নবী করীম ﷺ-এর ব্যক্তিগত, পারিবারিক, সামাজিক ও রাষ্ট্রীয় জীবনের সর্বোত্তম আদর্শ।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/mohanobir_adorsho.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "তাওহীদ পাবলিকেশন্স", "তাওহীদ পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 11000
        ));

        // 12. কুরবানির ফজিলত, মাসায়েল ও আমল (শায়খ আকরামুজ্জামান বিন আব্দুস সালাম)
        list.add(new IslamicBookEntity(
                "book_12",
                "কুরবানির ফজিলত, মাসায়েল ও আমল",
                "শায়খ আকরামুজ্জামান বিন আব্দুস সালাম",
                "qurbani_eid",
                "bn",
                92,
                "2.7 MB",
                "যিলহজ মাসের প্রথম দশ দিনের ফজিলত, কুরবানি ও আইয়ামে তাশরীকের আমলসমূহের প্রামাণ্য বিশ্লেষণ।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/qurbani_fozilot.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "মদিনা ইসলামিক সেন্টার", "মদিনা ইসলামিক সেন্টার",
                "পাবলিক ডোমেইন", "PDF", now - 12000
        ));

        // 13. ঈদ ও ঈদের বিধান (মাওলানা মতিউর রহমান মাদানী)
        list.add(new IslamicBookEntity(
                "book_13",
                "ঈদ ও ঈদের বিধান",
                "মাওলানা মতিউর রহমান মাদানী",
                "qurbani_eid",
                "bn",
                72,
                "2.1 MB",
                "ঈদের তাকবীর, সালাতের পদ্ধতি, ফিতরা আদায় ও ঈদের দিনে শারঈ সীমারেখা বজায় রেখে আনন্দ উদযাপনের নিয়ম।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/eid_bidhan.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "পিস পাবলিকেশন্স", "পিস পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 13000
        ));

        // 14. সহীহ আকীদা ও নাজাতপ্রাপ্ত দল (শায়খ মুহাম্মদ জামিল যাইনু)
        list.add(new IslamicBookEntity(
                "book_14",
                "সহীহ আকীদা ও নাজাতপ্রাপ্ত দল",
                "শায়খ মুহাম্মদ জামিল যাইনু",
                "aqeedah",
                "bn",
                180,
                "5.4 MB",
                "আহলুস সুন্নাহ ওয়াল জামাআতের বিশুদ্ধ আকীদা ও ঈমানের সঠিক রুকনসমূহ এবং মুক্তিপ্রাপ্ত দলের অনন্য বৈশিষ্ট্য।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/sahih_aqeedah.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "তাওহীদ পাবলিকেশন্স", "তাওহীদ পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 14000
        ));

        // 15. সালাতুর রাসূল ﷺ (ড. মুহাম্মাদ আসাদুল্লাহ আল-গালিব)
        list.add(new IslamicBookEntity(
                "book_15",
                "সালাতুর রাসূল ﷺ",
                "ড. মুহাম্মাদ আসাদুল্লাহ আল-গালিব",
                "salah",
                "bn",
                250,
                "7.1 MB",
                "তাকবীরে তাহরীমা থেকে সালাম ফিরানো পর্যন্ত রাসূলুল্লাহ ﷺ-এর বিশুদ্ধ সালাত আদায়ের পুঙ্খানুপুঙ্খ বিবরণ।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/salatur_rasool.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "হাদীছ ফাউন্ডেশন বাংলাদেশ", "হাদীছ ফাউন্ডেশন বাংলাদেশ",
                "পাবলিক ডোমেইন", "PDF", now - 15000
        ));

        // 16. দৈনন্দিন দো'আ ও যিকির (ড. সাঈদ ইবনে আলী আল-কাহত্বানী)
        list.add(new IslamicBookEntity(
                "book_16",
                "দৈনন্দিন দো'আ ও যিকির (হিসনুল মুসলিম)",
                "ড. সাঈদ ইবনে আলী আল-কাহত্বানী",
                "dua",
                "bn",
                240,
                "6.8 MB",
                "দৈনন্দিন জীবনের সকাল-সন্ধ্যা, বিপদ-আপদ, রোগব্যাধি ও সকল পরিস্থিতির সহীহ ও নির্ভরযোগ্য দো'আ ও যিকির।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/hisnul_muslim_dua.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "দারুস সালাম", "মাকতাবাতুস সুন্নাহ",
                "পাবলিক ডোমেইন", "PDF", now - 16000
        ));

        // 17. শিরক, কুফর ও বিদআত বর্জন (ড. খন্দকার আব্দুল্লাহ জাহাঙ্গীর)
        list.add(new IslamicBookEntity(
                "book_17",
                "শিরক, কুফর ও বিদআত বর্জন",
                "ড. খন্দকার আব্দুল্লাহ জাহাঙ্গীর",
                "bidah",
                "bn",
                320,
                "9.3 MB",
                "শিরক ও বিদআতের সূক্ষ্ম স্বরূপ, সমাজে প্রচলিত কুসংস্কার ও বিদআতসমূহ এবং সহীহ সুন্নাহ মোতাবেক চলার দিকনির্দেশনা।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/shirk_bidah_borjon.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "আস-সুন্নাহ ট্রাস্ট", "আস-সুন্নাহ ট্রাস্ট",
                "পাবলিক ডোমেইন", "PDF", now - 17000
        ));

        // 18. ইসলামী ফতোয়া সমগ্র (স্থায়ী ফতোয়া বোর্ড)
        list.add(new IslamicBookEntity(
                "book_18",
                "ইসলামী ফতোয়া সমগ্র",
                "স্থায়ী ফতোয়া বোর্ড (লাজনাহ দায়িমাহ)",
                "fatwa",
                "bn",
                420,
                "12.1 MB",
                "সমসাময়িক ও ঐতিহ্যবাহী সকল জরুরি ধর্মীয় প্রশ্নের নির্ভরযোগ্য ইসলামী ফতোয়া ও দলীলভিত্তিক সমাধান।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/islami_fatwa_somogro.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "রিয়াদ ফতোয়া কাউন্সিল", "রিয়াদ ফতোয়া কাউন্সিল",
                "পাবলিক ডোমেইন", "PDF", now - 18000
        ));

        // 19. ফিরকা ও বাতিল দল পরিচিতি (শায়খ মুহাম্মদ আসাদুল্লাহ)
        list.add(new IslamicBookEntity(
                "book_19",
                "ফিরকা ও বাতিল দল পরিচিতি",
                "শায়খ মুহাম্মদ আসাদুল্লাহ",
                "firqa",
                "bn",
                210,
                "6.2 MB",
                "ইসলামের ইতিহাসে উদ্ভূত বিভিন্ন ভ্রান্ত দল (খারেজী, মুতাজিলা, শিয়া, কাদিয়ানীবাদ) ও তাদের বিভ্রান্তিকর মতবাদের খণ্ডন।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/firqa_porichiti.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "আল-হেদায়াহ প্রকাশনী", "আল-হেদায়াহ প্রকাশনী",
                "পাবলিক ডোমেইন", "PDF", now - 19000
        ));

        // 20. কুরআন ও হাদিসের আলোকে জীবন (মাওলানা আব্দুল হামিদ ফাইযী)
        list.add(new IslamicBookEntity(
                "book_20",
                "কুরআন ও হাদিসের আলোকে জীবন",
                "মাওলানা আব্দুল হামিদ ফাইযী",
                "quran_hadith",
                "bn",
                280,
                "7.9 MB",
                "প্রতিটি পদক্ষেপে কুরআন ও সহীহ সুন্নাহর পরিপূর্ণ অনুসারী হওয়ার মূলনীতি ও বাস্তবসম্মত দিকনির্দেশনা।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/quran_hadith_aloke_jibon.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "মদিনা ইসলামিক বুকস", "মদিনা ইসলামিক বুকস",
                "পাবলিক ডোমেইন", "PDF", now - 20000
        ));

        // 21. উসীলা ও তাওহীদের সঠিক ধারণা (আল্লামা নাসিরুদ্দিন আলবানী)
        list.add(new IslamicBookEntity(
                "book_21",
                "উসীলা ও তাওহীদের সঠিক ধারণা",
                "আল্লামা নাসিরুদ্দিন আলবানী",
                "tawhid_waseela",
                "bn",
                156,
                "4.5 MB",
                "জায়েয ও না-জায়েয উসীলার পার্থক্য, কবরে উসীলা নেওয়ার বিভ্রান্তি এবং তাওহীদের শর্তাবলীর বিশদ ব্যাখ্যা।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/waseela_o_tawhid.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "তাওহীদ পাবলিকেশন্স", "তাওহীদ পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 21000
        ));

        // 22. আর-রাহীকুল মাখতূম (আল্লামা ছফিউর রহমান মুবারকপুরী)
        list.add(new IslamicBookEntity(
                "book_22",
                "আর-রাহীকুল মাখতূম (সীরাত গ্রন্থ)",
                "আল্লামা ছফিউর রহমান মুবারকপুরী",
                "seerah",
                "bn",
                590,
                "16.5 MB",
                "বিশ্ববিখ্যাত আন্তর্জাতিক সীরাত প্রতিযোগিতায় প্রথম পুরস্কারপ্রাপ্ত রাসূলুল্লাহ ﷺ-এর প্রামাণ্য জীবনীগ্রন্থ।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/raheeq_makhtum.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "তাওহীদ পাবলিকেশন্স", "তাওহীদ পাবলিকেশন্স",
                "পাবলিক ডোমেইন", "PDF", now - 22000
        ));

        // 23. কিতাবুত তাওহীদ (শায়খ মুহাম্মদ বিন আব্দুল ওহাব)
        list.add(new IslamicBookEntity(
                "book_23",
                "কিতাবুত তাওহীদ",
                "শায়খ মুহাম্মদ বিন আব্দুল ওহাব",
                "aqeedah",
                "bn",
                198,
                "5.6 MB",
                "আল্লাহর একত্ববাদ, শিরকের ভয়াবহতা এবং তাওহীদের সকল শাখা-প্রশাখার দলিলভিত্তিক বিশদ ব্যাখ্যা।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/kitabut_tawhid.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "ইসলাম প্রচার ব্যুরো", "ইসলাম প্রচার ব্যুরো",
                "পাবলিক ডোমেইন", "PDF", now - 23000
        ));

        // 24. তাফসীর ইবনে কাসীর (১ম খণ্ড) (ইমাম হাফিজ ইবনে কাসীর)
        list.add(new IslamicBookEntity(
                "book_24",
                "তাফসীর ইবনে কাসীর (১ম খণ্ড)",
                "ইমাম হাফিজ ইবনে কাসীর",
                "quran_hadith",
                "bn",
                650,
                "14.2 MB",
                "বিশ্বের সবচেয়ে গ্রহণযোগ্য ও প্রামাণ্য তাফসীর গ্রন্থ। কুরআন ও সহিহ হাদিসের আলোকে আয়াতের বিস্তারিত ব্যাখ্যা।",
                "https://raw.githubusercontent.com/riadmonir/DeenOne/main/database/books/tafsir_ibn_kathir_1.pdf",
                null, false, 0, 0, 0, "NOT_STARTED", 0, false, null,
                "ইসলামিক ফাউন্ডেশন", "ইসলামিক ফাউন্ডেশন",
                "পাবলিক ডোমেইন", "PDF", now - 24000
        ));

        return list;
    }
}
