package com.devflux.deenone.features.hadith.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.HadithChapterEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for Hadith Chapters with direct SQLite & local Room database architecture:
 * 1. Instant SQLite / Room Database LiveData / local retrieval (0ms, 60 FPS, lag-free).
 * 2. Background GitHub CDN sync via HadithDatabaseManager.
 * 3. 100% PHP decoupled, offline reliable.
 */
public class HadithChapterRepository {

    private static final String TAG = "HadithChapterRepo";
    private static volatile HadithChapterRepository instance;

    private final ExecutorService diskExecutor = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface ChaptersCallback {
        void onLoaded(List<HadithChapterEntity> chapters);
    }

    private HadithChapterRepository() {}

    public static HadithChapterRepository getInstance() {
        if (instance == null) {
            synchronized (HadithChapterRepository.class) {
                if (instance == null) {
                    instance = new HadithChapterRepository();
                }
            }
        }
        return instance;
    }

    /**
     * Get LiveData of chapters for a given book from local Room database.
     */
    public LiveData<List<HadithChapterEntity>> getChaptersLiveData(Context context, String bookSlug) {
        return AppDatabase.getInstance(context).hadithChapterDao().getChaptersByBook(bookSlug);
    }

    /**
     * Load chapters with immediate local callback from HadithDatabaseManager / Room database.
     * 100% decoupled from PHP server; purely offline and GitHub CDN SQLite synced.
     */
    public void loadChapters(Context context, String bookSlug, ChaptersCallback callback) {
        final Context appContext = context.getApplicationContext();
        final String safeSlug = (bookSlug != null ? bookSlug.toLowerCase().trim() : "bukhari");

        diskExecutor.execute(() -> {
            // 1. Check direct SQLite engine first (0ms instant)
            HadithDatabaseManager dbMgr = HadithDatabaseManager.getInstance(appContext);
            if (dbMgr.isDatabaseReady()) {
                List<HadithChapterEntity> sqliteChapters = dbMgr.getChaptersForBook(safeSlug);
                if (sqliteChapters != null && !sqliteChapters.isEmpty()) {
                    AppDatabase.getInstance(appContext).hadithChapterDao().insertAll(sqliteChapters);
                    mainHandler.post(() -> {
                        if (callback != null) callback.onLoaded(sqliteChapters);
                    });
                    return;
                }
            }

            // 2. Check local Room Database
            AppDatabase db = AppDatabase.getInstance(appContext);
            List<HadithChapterEntity> localList = db.hadithChapterDao().getChaptersByBookSync(safeSlug);
            if (localList != null && !localList.isEmpty()) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onLoaded(localList);
                });
            } else if ("bukhari".equalsIgnoreCase(safeSlug)) {
                List<HadithChapterEntity> defaults = getDefaultBukhariChapters();
                db.hadithChapterDao().insertAll(defaults);
                mainHandler.post(() -> {
                    if (callback != null) callback.onLoaded(defaults);
                });
            }

            // 3. Ensure SQLite database is downloaded/verified from GitHub CDN in background
            dbMgr.ensureDatabaseAvailable(success -> {
                if (success) {
                    diskExecutor.execute(() -> {
                        List<HadithChapterEntity> loaded = dbMgr.getChaptersForBook(safeSlug);
                        if (loaded != null && !loaded.isEmpty()) {
                            AppDatabase.getInstance(appContext).hadithChapterDao().insertAll(loaded);
                            mainHandler.post(() -> {
                                if (callback != null) callback.onLoaded(loaded);
                            });
                        }
                    });
                }
            });
        });
    }

    public static List<HadithChapterEntity> getDefaultBukhariChapters() {
        List<HadithChapterEntity> list = new ArrayList<>();
        list.add(new HadithChapterEntity("bukhari", 1, "১", "ওহীর সূচনা অধ্যায়", "Revelation", "1 - 7", "১ - ৭", 1, 7, 7, 1, true));
        list.add(new HadithChapterEntity("bukhari", 2, "২", "ঈমান", "Belief", "8 - 58", "৮ - ৫৮", 8, 58, 51, 2, true));
        list.add(new HadithChapterEntity("bukhari", 3, "৩", "ইলম", "Knowledge", "59 - 134", "৫৯ - ১৩৪", 59, 134, 76, 3, true));
        list.add(new HadithChapterEntity("bukhari", 4, "৪", "ওযু", "Ablution", "135 - 247", "১৩৫ - ২৪৭", 135, 247, 113, 4, true));
        list.add(new HadithChapterEntity("bukhari", 5, "৫", "গোসল", "Bathing", "248 - 293", "২৪৮ - ২৯৩", 248, 293, 46, 5, true));
        list.add(new HadithChapterEntity("bukhari", 6, "৬", "হায়েজ", "Menses", "294 - 333", "২৯৪ - ৩৩৩", 294, 333, 40, 6, true));
        list.add(new HadithChapterEntity("bukhari", 7, "৭", "তায়াম্মুম", "Tayammum", "334 - 348", "৩৩৪ - ৩৪৮", 334, 348, 15, 7, true));
        list.add(new HadithChapterEntity("bukhari", 8, "৮", "সালাত", "Prayers", "349 - 520", "৩৪৯ - ৫২০", 349, 520, 172, 8, true));
        list.add(new HadithChapterEntity("bukhari", 9, "৯", "সালাতের ওয়াক্তসমূহ", "Times of the Prayers", "521 - 602", "৫২১ - ৬০২", 521, 602, 82, 9, true));
        list.add(new HadithChapterEntity("bukhari", 10, "১০", "আজান", "Call to Prayer", "603 - 875", "৬০৩ - ৮৭৫", 603, 875, 273, 10, true));
        list.add(new HadithChapterEntity("bukhari", 11, "১১", "জুমা", "Friday Prayer", "876 - 941", "৮৭৬ - ৯৪১", 876, 941, 66, 11, true));
        list.add(new HadithChapterEntity("bukhari", 12, "১২", "খাওফ (ভয় ভীতির সালাত)", "Fear Prayer", "942 - 947", "৯৪২ - ৯৪৭", 942, 947, 6, 12, true));
        list.add(new HadithChapterEntity("bukhari", 13, "১৩", "দুই ঈদ", "The Two Festivals (Eids)", "948 - 989", "৯৪৮ - ৯৮৯", 948, 989, 42, 13, true));
        list.add(new HadithChapterEntity("bukhari", 14, "১৪", "বিতর", "Witr", "990 - 1004", "৯৯০ - ১০০৪", 990, 1004, 15, 14, true));
        list.add(new HadithChapterEntity("bukhari", 15, "১৫", "বৃষ্টির জন্য দোয়া", "Invoking Allah for Rain (Istisqa)", "1005 - 1039", "১০০৫ - ১০৩৯", 1005, 1039, 35, 15, true));
        list.add(new HadithChapterEntity("bukhari", 16, "১৬", "সূর্যগ্রহণ", "Eclipses", "1040 - 1066", "১০৪০ - ১০৬৬", 1040, 1066, 27, 16, true));
        list.add(new HadithChapterEntity("bukhari", 17, "১৭", "কুরআন তিলাওয়াতের সিজদা", "Prostration During Quran Recital", "1067 - 1079", "১০৬৭ - ১০৭৯", 1067, 1079, 13, 17, true));
        list.add(new HadithChapterEntity("bukhari", 18, "১৮", "সালাতে কসর করা", "Shortening the Prayers", "1080 - 1119", "১০৮০ - ১১১৯", 1080, 1119, 40, 18, true));
        list.add(new HadithChapterEntity("bukhari", 19, "১৯", "তাহাজ্জুদ", "Tahajjud", "1120 - 1187", "১১২০ - ১১৮৭", 1120, 1187, 68, 19, true));
        list.add(new HadithChapterEntity("bukhari", 20, "২০", "মক্কা ও মদীনার মসজিদে সালাতের মর্যাদা", "Virtues of Prayer in Makkah & Madinah", "1188 - 1197", "১১৮৮ - ১১৯৭", 1188, 1197, 10, 20, true));
        list.add(new HadithChapterEntity("bukhari", 21, "২১", "সালাতের সাথে সংশ্লিষ্ট কাজ", "Actions while Praying", "1198 - 1223", "১১৯৮ - ১২২৩", 1198, 1223, 26, 21, true));
        list.add(new HadithChapterEntity("bukhari", 22, "২২", "সাহু", "Forgetfulness in Prayer (Sahu)", "1224 - 1236", "১২২৪ - ১২৩৬", 1224, 1236, 13, 22, true));
        list.add(new HadithChapterEntity("bukhari", 23, "২৩", "জানাজা", "Funerals (Janaza)", "1237 - 1394", "১২৩৭ - ১৩৯৪", 1237, 1394, 158, 23, true));
        list.add(new HadithChapterEntity("bukhari", 24, "২৪", "যাকাত", "Zakat", "1395 - 1512", "১৩৯৫ - ১৫১২", 1395, 1512, 118, 24, true));
        list.add(new HadithChapterEntity("bukhari", 25, "২৫", "হজ্জ", "Hajj", "1513 - 1772", "১৫১৩ - ১৭৭২", 1513, 1772, 260, 25, true));
        list.add(new HadithChapterEntity("bukhari", 26, "২৬", "উমরাহ", "Umrah", "1773 - 1805", "১৭৭৩ - ১৮০৫", 1773, 1805, 33, 26, true));
        list.add(new HadithChapterEntity("bukhari", 27, "২৭", "পথে আটকে পড়া ও ইহরাম অবস্থায় শিকারকারীর বিধান", "Muhsar & Hunting Penalty", "1806 - 1820", "১৮০৬ - ১৮২০", 1806, 1820, 15, 27, true));
        list.add(new HadithChapterEntity("bukhari", 28, "২৮", "ইহরাম অবস্থায় শিকার ও অনুরূপ কিছুর বদলা", "Penalty of Hunting in Ihram", "1821 - 1866", "১৮২১ - ১৮৬৬", 1821, 1866, 46, 28, true));
        list.add(new HadithChapterEntity("bukhari", 29, "২৯", "মদীনার ফজিলত", "Virtues of Madinah", "1867 - 1890", "১৮৬৭ - ১৮৯০", 1867, 1890, 24, 29, true));
        list.add(new HadithChapterEntity("bukhari", 30, "৩০", "সাওম", "Fasting (Sawm)", "1891 - 2007", "১৮৯১ - ২০০৭", 1891, 2007, 117, 30, true));
        list.add(new HadithChapterEntity("bukhari", 31, "৩১", "তারাবীহর সালাত", "Tarawih Prayer", "2008 - 2013", "২০০৮ - ২০১৩", 2008, 2013, 6, 31, true));
        list.add(new HadithChapterEntity("bukhari", 32, "৩২", "লাইলাতুল কদর এর ফজিলত", "Virtues of Laylat al-Qadr", "2014 - 2024", "২০১৪ - ২০২৪", 2014, 2024, 11, 32, true));
        list.add(new HadithChapterEntity("bukhari", 33, "৩৩", "ইতিকাফ", "Itikaf", "2025 - 2046", "২০২৫ - ২০৪৬", 2025, 2046, 22, 33, true));
        list.add(new HadithChapterEntity("bukhari", 34, "৩৪", "ক্রয়-বিক্রয়", "Sales and Trade", "2047 - 2238", "২০৪৭ - ২২৩৮", 2047, 2238, 192, 34, true));
        list.add(new HadithChapterEntity("bukhari", 35, "৩৫", "সলম (অগ্রিম ক্রয়-বিক্রয়)", "Salam (Advance Purchase)", "2239 - 2256", "২২৩৯ - ২২৫৬", 2239, 2256, 18, 35, true));
        list.add(new HadithChapterEntity("bukhari", 36, "৩৬", "শুফআ", "Pre-emption (Shuf'ah)", "2257 - 2259", "২২৫৭ - ২২৫৯", 2257, 2259, 3, 36, true));
        list.add(new HadithChapterEntity("bukhari", 37, "৩৭", "ইজারা", "Hiring and Leasing (Ijarah)", "2260 - 2286", "২২৬০ - ২২৮৬", 2260, 2286, 27, 37, true));
        list.add(new HadithChapterEntity("bukhari", 38, "৩৮", "হাওয়ালাত", "Transfer of Debt (Hawalah)", "2287 - 2289", "২২৮৭ - ২২৮৯", 2287, 2289, 3, 38, true));
        list.add(new HadithChapterEntity("bukhari", 39, "৩৯", "যামিন হওয়া", "Suretyship (Kafalah)", "2290 - 2298", "২২৯০ - ২২৯৮", 2290, 2298, 9, 39, true));
        list.add(new HadithChapterEntity("bukhari", 40, "৪০", "ওয়াকালাহ (প্রতিনিধিত্ব)", "Representation (Wakalah)", "2299 - 2319", "২২৯৯ - ২৩১৯", 2299, 2319, 21, 40, true));
        list.add(new HadithChapterEntity("bukhari", 41, "৪১", "চাষাবাদ", "Agriculture (Muzara'ah)", "2320 - 2350", "২৩২০ - ২৩৫০", 2320, 2350, 31, 41, true));
        return list;
    }
}
