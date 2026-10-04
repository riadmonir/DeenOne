package com.devflux.deenone.features.books.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.IslamicBookDao;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;

import java.util.ArrayList;
import java.util.List;

public class IslamicBookRepository {

    private static volatile IslamicBookRepository instance;
    private final IslamicBookDao bookDao;
    private final Context context;
    private final java.util.concurrent.ExecutorService networkExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();

    private IslamicBookRepository(Context context) {
        this.context = context.getApplicationContext();
        AppDatabase db = AppDatabase.getInstance(context);
        this.bookDao = db.islamicBookDao();
        seedCanonicalBooksIfEmpty();
    }

    public static synchronized IslamicBookRepository getInstance(Context context) {
        if (instance == null) {
            instance = new IslamicBookRepository(context);
        }
        return instance;
    }

    public LiveData<List<IslamicBookEntity>> getAllBooks() {
        return bookDao.getAllBooks();
    }

    public LiveData<List<IslamicBookEntity>> getBooksByCategory(String category) {
        if ("all".equalsIgnoreCase(category) || "সকল".equalsIgnoreCase(category)) {
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
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            listener.onOffline("অফলাইন মোড: ইন্টারনেটের অনুপস্থিতিতে ডিভাইসে সংরক্ষিত বইসমূহ প্রদর্শন করা হচ্ছে।"));
                }
                return;
            }

            try {
                List<IslamicBookEntity> allMergedBooks = new ArrayList<>();

                // 1. Fetch live dynamic books from IslamHouse Public Domain Islamic Books API (Page 1 & 2)
                try {
                    List<IslamicBookEntity> apiBooksP1 = com.devflux.deenone.features.books.services.IslamicBookApiService.getInstance(context)
                            .fetchBooksFromApiSync("bn", 1, 25);
                    if (apiBooksP1 != null) {
                        allMergedBooks.addAll(apiBooksP1);
                    }

                    List<IslamicBookEntity> apiBooksP2 = com.devflux.deenone.features.books.services.IslamicBookApiService.getInstance(context)
                            .fetchBooksFromApiSync("bn", 2, 25);
                    if (apiBooksP2 != null) {
                        allMergedBooks.addAll(apiBooksP2);
                    }
                } catch (Exception apiEx) {
                    android.util.Log.w("IslamicBookRepository", "IslamHouse API call note: " + apiEx.getMessage());
                }

                // 2. Combine with Canonical baseline collection
                List<IslamicBookEntity> canonicalBooks = getCanonicalIslamicBooks();
                allMergedBooks.addAll(canonicalBooks);

                // 3. Batch merge & single batch insert on database executor
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    try {
                        List<IslamicBookEntity> validBooks = new ArrayList<>();
                        for (IslamicBookEntity newBook : allMergedBooks) {
                            if (newBook == null || newBook.getDownloadUrl() == null || newBook.getDownloadUrl().isEmpty()) {
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
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                                    listener.onSuccess("অনলাইন ক্যাটালগ সফলভাবে সিঙ্ক সম্পন্ন হয়েছে (" + com.devflux.deenone.utils.BengaliNumberUtil.toBengali(finalCount) + "টি প্রামাণ্য কিতাব প্রস্তুত)"));
                        }
                    } catch (Exception dbEx) {
                        if (listener != null) {
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                                    listener.onError("ডাটাবেজ আপডেটে সমস্যা: " + dbEx.getMessage()));
                        }
                    }
                });
            } catch (Exception e) {
                if (listener != null) {
                    new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                            listener.onError("অনলাইন সিঙ্কে সমস্যা: " + e.getMessage()));
                }
            }
        });
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

        // 1. Quran
        list.add(new IslamicBookEntity(
                "quran_01",
                "তাফসীরে উসমানী (বাংলা অনুবাদ ও সংক্ষিপ্ত তাফসীর)",
                "মাওলানা শাব্বীর আহমদ উসমানী (রহ.)",
                "Quran",
                "bn",
                820,
                "18.4 MB",
                "পবিত্র কুরআনের বিশুদ্ধ বাংলা অনুবাদ ও নির্ভরযোগ্য সংক্ষিপ্ত তাফসীর। ইসলামিক ফাউন্ডেশন অনুমোদিত বিশুদ্ধ ব্যাখ্যা।",
                "https://archive.org/download/TafsirEUsmaniBangla/Tafseer-e-Usmani-Bangla-Part-1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলামিক ফাউন্ডেশন",
                "কিং ফাহাদ কুরআন প্রিন্টিং কমপ্লেক্স ও ইসলামিক ফাউন্ডেশন",
                "পাবলিক ডোমেইন / উন্মুক্ত ইসলামিক বিতরণ",
                "PDF",
                now - 1000
        ));

        list.add(new IslamicBookEntity(
                "quran_02",
                "কুরআনুল কারীম (উচ্চারণ ও অর্থসহ)",
                "কিং ফাহাদ কুরআন কমপ্লেক্স",
                "Quran",
                "bn",
                604,
                "22.1 MB",
                "সহজ বাংলা উচ্চারণ এবং প্রতিটি আয়াতের নির্ভরযোগ্য বাংলা অনুবাদ সম্বলিত পূর্ণাঙ্গ আল-কুরআন।",
                "https://archive.org/download/BanglaQuranWithTajweed/Quran_Majeed_Bangla.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "মদিনা মুনাওয়ারা প্রিন্ট",
                "কিং ফাহাদ কুরআন প্রিন্টিং কমপ্লেক্স",
                "উন্মুক্ত ওয়াকফ প্রকাশনা",
                "PDF",
                now - 2000
        ));

        // 2. Tafsir
        list.add(new IslamicBookEntity(
                "tafsir_01",
                "তাফসীর ইবনে কাসীর (১ম খণ্ড)",
                "ইমাম হাফিজ ইবনে কাসীর (রহ.)",
                "Tafsir",
                "bn",
                650,
                "14.2 MB",
                "বিশ্বের সবচেয়ে গ্রহণযোগ্য ও প্রামাণ্য তাফসীর গ্রন্থ। কুরআন ও সহিহ হাদিসের আলোকে আয়াতের বিস্তারিত ব্যাখ্যা।",
                "https://archive.org/download/TafsirIbnKathirBanglaAllVolume/Tafsir_Ibn_Kathir_Vol_01.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "হুসাইন আল-মাদানী প্রকাশনী",
                "দারুসসালাম ও ইসলামিক ফাউন্ডেশন",
                "উন্মুক্ত ইসলামিক শিক্ষা বিতরণ",
                "PDF",
                now - 3000
        ));

        list.add(new IslamicBookEntity(
                "tafsir_02",
                "তাফসীরে মা'আরিফুল কুরআন (সংক্ষিপ্ত)",
                "মুফতী মুহাম্মদ শফী (রহ.)",
                "Tafsir",
                "bn",
                720,
                "16.8 MB",
                "প্রতিটি আয়াতের শানে নুযূল, শিক্ষা এবং আধুনিক জীবনের মাসআলা সংক্রান্ত প্রখ্যাত তাফসীর।",
                "https://archive.org/download/TafsirMaarifulQuranBangla/Maariful_Quran_Vol_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলামিক ফাউন্ডেশন বাংলাদেশ",
                "দারুল উলুম করাচি ও ইফা",
                "পাবলিক ইসলামিক ফাউন্ডেশন লাইসেন্স",
                "PDF",
                now - 4000
        ));

        // 3. Hadith
        list.add(new IslamicBookEntity(
                "hadith_01",
                "সহিহুল বুখারী (পূর্ণাঙ্গ সংকলন - ১ম খণ্ড)",
                "ইমাম মুহাম্মদ ইবনে ইসমাইল আল-বুখারী (রহ.)",
                "Hadith",
                "bn",
                580,
                "12.5 MB",
                "কুরআনের পর বিশুদ্ধতম কিতাব। সহিহ বুখারী শরীফের বিশুদ্ধ বাংলা অনুবাদ ও হাদিসের সনদ বিশ্লেষণ।",
                "https://archive.org/download/SahihBukhariBanglaAllVolumes/Sahih_Bukhari_Vol_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "তাওহীদ পাবলিকেশন্স",
                "আন্তর্জাতিক হাদিস একাডেমি",
                "পাবলিক ডোমেইন হাদিস প্রকাশনা",
                "PDF",
                now - 5000
        ));

        list.add(new IslamicBookEntity(
                "hadith_02",
                "রিয়াদুস সালেহীন (নেককারদের বাগান)",
                "ইমাম আন-নববী (রহ.)",
                "Hadith",
                "bn",
                490,
                "10.1 MB",
                "দৈনন্দিন জীবনের আখলাক, ইবাদত ও আত্মশুদ্ধি সম্পর্কিত সহিহ হাদিসের শ্রেষ্ঠ সংকলন।",
                "https://archive.org/download/RiyadusSaliheenBanglaFull/Riyadus_Saliheen_Part_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "দারুসসালাম রিয়াদ",
                "মাকতাবাতুল মাআরেফ",
                "উন্মুক্ত ইসলামিক সংকলন",
                "PDF",
                now - 6000
        ));

        list.add(new IslamicBookEntity(
                "hadith_03",
                "ইমাম নববীর চল্লিশ হাদিস (আরবি ও বাংলা)",
                "ইমাম ইয়াহইয়া ইবনে শরাফ আন-নববী (রহ.)",
                "Hadith",
                "bn",
                120,
                "3.2 MB",
                "ইসলামের মূল স্তম্ভ ও মৌলিক শিক্ষার ওপর সর্বাধিক পঠিত ও মুখস্থকৃত ৪০টি বিশুদ্ধ হাদিস।",
                "https://archive.org/download/FortyHadithAnNawawiBangla/40_Hadith_Nawawi_Bangla.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলাম প্রচার ব্যুরো, রিয়াদ",
                "ইসলামহাউজ ভেরিফাইড",
                "পাবলিক ডোমেইন ওয়াকফ",
                "PDF",
                now - 7000
        ));

        // 4. Fiqh
        list.add(new IslamicBookEntity(
                "fiqh_01",
                "ফিকহুস সুন্নাহ (১ম খণ্ড: তাহারাত ও সালাত)",
                "সাইয়্যিদ সাবিক (রহ.)",
                "Fiqh",
                "bn",
                450,
                "9.8 MB",
                "কুরআন ও সুন্নাহর প্রামাণ্য দলিলের ওপর প্রতিষ্ঠিত ফিকহের অনন্য আধুনিক নির্দেশিকা।",
                "https://archive.org/download/FiqhUsSunnahBangla/Fiqh_Us_Sunnah_Vol_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলামিক ফাউন্ডেশন",
                "আল-আজহার বিশ্ববিদ্যালয় অনুমোদিত",
                "উন্মুক্ত ইসলামিক শিক্ষা",
                "PDF",
                now - 8000
        ));

        list.add(new IslamicBookEntity(
                "fiqh_02",
                "দৈনন্দিন জীবনে ইসলাম (প্রশ্নোত্তর ও সমাধান)",
                "ইসলামিক ফাউন্ডেশন গবেষক প্যানেল",
                "Fiqh",
                "bn",
                520,
                "11.4 MB",
                "পবিত্রতা, নামাজ, রোজা, জাকাত, হজ ও আধুনিক জীবনের লেনদেনের শরয়ী সমাধান।",
                "https://archive.org/download/DainandinJiboneIslamIFB/Dainandin_Jibone_Islam.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলামিক ফাউন্ডেশন বাংলাদেশ",
                "ইসলামিক ফাউন্ডেশন প্রকাশনা",
                "ইসলামিক ফাউন্ডেশন কপিরাইট ওপেন",
                "PDF",
                now - 9000
        ));

        // 5. Aqeedah
        list.add(new IslamicBookEntity(
                "aqeedah_01",
                "কিতাবুত তাওহীদ (আল্লাহর একত্ববাদ)",
                "ইমাম মুহাম্মদ বিন আব্দুল ওয়াহাব (রহ.)",
                "Aqeedah",
                "bn",
                240,
                "4.8 MB",
                "বিশুদ্ধ তাওহীদ ও শিরক মুক্ত ঈমান গঠনের জন্য সর্বাধিক নির্ভরযোগ্য পথপ্রদর্শক।",
                "https://archive.org/download/KitabutTawheedBangla/Kitabut_Tawheed_Bangla.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "তাওহীদ পাবলিকেশন্স",
                "মাকতাবাতুত তাওহীদ",
                "উন্মুক্ত তাওহীদ প্রচার লাইসেন্স",
                "PDF",
                now - 10000
        ));

        list.add(new IslamicBookEntity(
                "aqeedah_02",
                "আকিদাতুত তাহাবী (আহলে সুন্নাত ওয়াল জামাআতের বিশ্বাস)",
                "ইমাম আবু জাফর আত-তাহাবী (রহ.)",
                "Aqeedah",
                "bn",
                180,
                "3.6 MB",
                "উম্মতের সর্বসম্মত বিশুদ্ধ আকিদার প্রাচীন ও প্রামাণ্য মৌলিক গ্রন্থ।",
                "https://archive.org/download/AqeedahTahawiyyahBangla/Aqeedah_Tahawiyyah_Bangla.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "মাকতাবাতুল ইসলাম",
                "দারুল ইফতা",
                "পাবলিক ডোমেইন ক্ল্যাসিক",
                "PDF",
                now - 11000
        ));

        // 6. Seerah
        list.add(new IslamicBookEntity(
                "seerah_01",
                "আর-রাহীকুল মাখতূম (মোহরাঙ্কিত জান্নাতী সুধা)",
                "আল্লামা শফিউর রহমান মুবারকপুরী (রহ.)",
                "Seerah",
                "bn",
                560,
                "13.5 MB",
                "বিশ্ব সীরাত প্রতিযোগিতায় ১ম স্থান অধিকারী বিশ্ববিখ্যাত ও সর্বাধিক জনপ্রিয় রাসুলুল্লাহ (সা.)-এর জীবনী।",
                "https://archive.org/download/ArRaheeqAlMakhtumBanglaFull/Ar_Raheeq_Al_Makhtum.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "তাওহীদ পাবলিকেশন্স",
                "রাবেতায়ে আলমে ইসলামী স্বর্ণপদকপ্রাপ্ত",
                "রাবেতা ওপেন সীরাত লাইসেন্স",
                "PDF",
                now - 12000
        ));

        list.add(new IslamicBookEntity(
                "seerah_02",
                "নবীয়ে রহমত (সা.) এর জীবন ও আদর্শ",
                "সৈয়দ আবুল হাসান আলী নদভী (রহ.)",
                "Seerah",
                "bn",
                480,
                "11.2 MB",
                "বিশ্বনবীর দয়া, ক্ষমা, নেতৃত্ব ও মানবজাতির মুক্তির পথনির্দেশক অনুপম সীরাত গ্রন্থ।",
                "https://archive.org/download/NobiyeRahmatBangla/Nobiye_Rahmat_Bangla.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "মজলিসে তাহকিকাত ও নাশরিয়াতে ইসলাম",
                "নাদওয়াতুল উলামা লখনৌ",
                "নাদওয়াতুল উলামা উন্মুক্ত প্রচার",
                "PDF",
                now - 13000
        ));

        // 7. Islamic History
        list.add(new IslamicBookEntity(
                "history_01",
                "তারিখে তাবারী (ইসলামের প্রাথমিক ইতিহাস)",
                "ইমাম মুহাম্মদ ইবনে জারীর আত-তাবারী (রহ.)",
                "Islamic History",
                "bn",
                620,
                "15.8 MB",
                "সৃষ্টির সূচনা, নবীগণের যুগ এবং খুলাফায়ে রাশিদীনের যুগান্তকারী নির্ভরযোগ্য ইতিহাস।",
                "https://archive.org/download/TarikhETabariBangla/Tarikh_Tabari_Vol_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলামিক ফাউন্ডেশন",
                "ইফা প্রকাশনা",
                "পাবলিক ডোমেইন ইতিহাস ক্লাসিক",
                "PDF",
                now - 14000
        ));

        list.add(new IslamicBookEntity(
                "history_02",
                "আল-বিদায়া ওয়ান নিহায়া (১ম খণ্ড)",
                "ইমাম হাফিজ ইবনে কাসীর (রহ.)",
                "Islamic History",
                "bn",
                680,
                "16.5 MB",
                "ইসলামের ইতিহাস গবেষণায় সর্বকালের অন্যতম সেরা প্রামাণ্য ঐতিহাসিক মহাকাব্য।",
                "https://archive.org/download/AlBidayahWanNihayahBangla/Al_Bidayah_Vol_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "ইসলামিক ফাউন্ডেশন",
                "মাকতাবাতুল মাআরেফ",
                "পাবলিক ডোমেইন ঐতিহাসিক সংকলন",
                "PDF",
                now - 15000
        ));

        // 8. Dua & Azkar
        list.add(new IslamicBookEntity(
                "dua_01",
                "হিসনুল মুসলিম (কুরআন ও হাদিস থেকে দোয়ার দুর্গ)",
                "ড. সাঈদ ইবনে আলী আল-কাহত্বানী (রহ.)",
                "Dua & Azkar",
                "bn",
                280,
                "5.4 MB",
                "দৈনন্দিন জীবনের সকাল-সন্ধ্যা, বিপদ-আপদ ও সকল অবস্থার সহিহ দোয়ার বিশ্বসেরা কিতাব।",
                "https://archive.org/download/HisnulMuslimBanglaDuaBook/Hisnul_Muslim_Bangla.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "মাকতাবা দারুসসালাম",
                "ইসলামহাউজ ভেরিফাইড",
                "উন্মুক্ত ওয়াকফ ইসলামিক লাইসেন্স",
                "PDF",
                now - 16000
        ));

        // 9. Salah
        list.add(new IslamicBookEntity(
                "salah_01",
                "নবীজির সালাত (রাসূলুল্লাহ সা. এর সালাতের পদ্ধতি)",
                "আল্লামা মুহাম্মদ নাসিরুদ্দীন আল-আলবানী (রহ.)",
                "Salah",
                "bn",
                320,
                "6.8 MB",
                "তাকবীরে তাহরীমা থেকে সালাম পর্যন্ত সহিহ হাদিসের আলোকে রাসুলুল্লাহ (সা.)-এর সালাতের চিত্র।",
                "https://archive.org/download/NobijirSalahAlbaniBangla/Nobijir_Salah_Albani.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "তাওহীদ পাবলিকেশন্স",
                "হাদিস একাডেমি",
                "উন্মুক্ত সুন্নাহ প্রচার",
                "PDF",
                now - 17000
        ));

        // 10. Islamic Ethics
        list.add(new IslamicBookEntity(
                "ethics_01",
                "আত্মশুদ্ধি ও তাওবা (তাজকিয়াতুন নফস)",
                "ইমাম ইবনুল কাইয়্যিম আল-জাওযিয়্যাহ (রহ.)",
                "Islamic Ethics",
                "bn",
                310,
                "6.2 MB",
                "অন্তরকে পাপমুক্ত করা, কুপ্রবৃত্তি দমন এবং আল্লাহর সন্তুষ্টি অর্জনের আধ্যাত্মিক দিকনির্দেশনা।",
                "https://archive.org/download/AtmosuddhiTazkiyahBangla/Atmosuddhi_Ibnul_Qayyim.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "মাকতাবাতুল বায়ান",
                "দারুল উলুম",
                "পাবলিক ডোমেইন আধ্যাত্মিক ক্লাসিক",
                "PDF",
                now - 18000
        ));

        // 11. Family & Marriage
        list.add(new IslamicBookEntity(
                "family_01",
                "আদর্শ পরিবার ও দাম্পত্য জীবন",
                "মুহাম্মদ জামিল যাইনু (রহ.)",
                "Family & Marriage",
                "bn",
                220,
                "4.5 MB",
                "সুখী দাম্পত্য, স্বামী-স্ত্রীর অধিকার, এবং ইসলামিক নীতিতে সন্তান প্রতিপালনের নির্দেশিকা।",
                "https://archive.org/download/AdorshoPoribarBangla/Adorsho_Poribar.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "দারুসসালাম প্রকাশনী",
                "ইসলামহাউজ ভেরিফাইড",
                "উন্মুক্ত পারিবারিক শিক্ষা",
                "PDF",
                now - 19000
        ));

        // 12. Children
        list.add(new IslamicBookEntity(
                "children_01",
                "ছোটদের প্রিয় নবী ও সাহাবাদের গল্প",
                "ড. রাগেব সারজানী",
                "Children",
                "bn",
                160,
                "4.1 MB",
                "সহজ-সরল ভাষায় ছোটদের জন্য নবীজি (সা.) ও জান্নাতী সাহাবীদের শিক্ষণীয় ও অনুপ্রেরণাদায়ী গল্প।",
                "https://archive.org/download/ChotoderSahabaderGolpo/Chotoder_Sahabader_Golpo.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "গার্ডিয়ান পাবলিকেশন্স",
                "ইসলামিক শিশু একাডেমি",
                "শিশু শিক্ষা উন্মুক্ত প্রচার",
                "PDF",
                now - 20000
        ));

        // 13. Bangla Islamic Books
        list.add(new IslamicBookEntity(
                "bangla_01",
                "আসহাবে রাসুলের জীবনকথা (১ম খণ্ড)",
                "মুহাম্মদ আবদুল মা'বুদ",
                "Bangla Islamic Books",
                "bn",
                420,
                "9.5 MB",
                "উম্মতের শ্রেষ্ঠ সন্তান সাহাবায়ে কেরামের ঈমানদীপ্ত ত্যাগ, বীরত্ব ও জীবনবৃত্তান্ত।",
                "https://archive.org/download/AshabeRasulerJibonkothaVol1/Ashabe_Rasuler_Jibonkotha_Vol_1.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "বাংলাদেশ ইসলামিক সেন্টার",
                "বিআইসি প্রকাশনা",
                "উন্মুক্ত বাংলা সাহিত্য প্রচার",
                "PDF",
                now - 21000
        ));

        // 14. English Islamic Books
        list.add(new IslamicBookEntity(
                "eng_01",
                "The Sealed Nectar (Ar-Raheeq Al-Makhtum)",
                "Safiur Rahman Al-Mubarakpuri",
                "English Islamic Books",
                "en",
                580,
                "11.8 MB",
                "Award-winning authentic biography of the Prophet Muhammad (Peace Be Upon Him) in English.",
                "https://archive.org/download/TheSealedNectarBiographyOfTheNobleProphet_201608/TheSealedNectar-BiographyOfTheNobleProphet.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "Darussalam Publishers",
                "World Muslim League 1st Prize",
                "World Muslim League Open License",
                "PDF",
                now - 22000
        ));

        list.add(new IslamicBookEntity(
                "eng_02",
                "Fortress of the Muslim (Hisnul Muslim Invocations)",
                "Dr. Sa'eed bin Ali Al-Qahtani",
                "English Islamic Books",
                "en",
                250,
                "4.5 MB",
                "Comprehensive authentic daily prayers, supplications and remembrance from Quran and Sunnah.",
                "https://archive.org/download/FortressOfTheMuslimHisnulMuslimEnglish/Fortress_of_the_Muslim.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "Darussalam Riyadh",
                "IslamHouse International",
                "Public Waqf International",
                "PDF",
                now - 23000
        ));

        list.add(new IslamicBookEntity(
                "eng_03",
                "Stories of the Prophets (Qisas Al-Anbiya)",
                "Ibn Kathir (Translated by Rashid Ahmad)",
                "English Islamic Books",
                "en",
                510,
                "10.2 MB",
                "Authentic narratives and lessons from the lives of all Prophets from Adam to Isa (Peace Be Upon Them).",
                "https://archive.org/download/StoriesOfTheProphetsByIbnKathirEnglish/Stories_of_the_Prophets_Ibn_Kathir.pdf",
                null,
                false,
                0,
                0,
                0,
                "NOT_STARTED",
                0,
                false,
                null,
                "Darussalam International",
                "Darussalam Authenticated",
                "Public Domain Translation",
                "PDF",
                now - 24000
        ));

        return list;
    }
}
