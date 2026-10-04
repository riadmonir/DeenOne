package com.devflux.deenone.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.devflux.deenone.data.local.dao.AllahNameDao;
import com.devflux.deenone.data.local.dao.AmalRecordDao;
import com.devflux.deenone.data.local.dao.AyahDao;
import com.devflux.deenone.data.local.dao.AzkarDao;
import com.devflux.deenone.data.local.dao.BloodDonorDao;
import com.devflux.deenone.data.local.dao.BookmarkDao;
import com.devflux.deenone.data.local.dao.DailyAmalDao;
import com.devflux.deenone.data.local.dao.DailyContentDao;
import com.devflux.deenone.data.local.dao.DuaDao;
import com.devflux.deenone.data.local.dao.DuaLogDao;
import com.devflux.deenone.data.local.dao.FeatureWidgetDao;
import com.devflux.deenone.data.local.dao.HadithChapterDao;
import com.devflux.deenone.data.local.dao.HadithDao;
import com.devflux.deenone.data.local.dao.HalalDao;
import com.devflux.deenone.data.local.dao.IslamicBookDao;
import com.devflux.deenone.data.local.dao.KalemaDao;
import com.devflux.deenone.data.local.dao.NotificationDao;
import com.devflux.deenone.data.local.dao.PrayerLogDao;
import com.devflux.deenone.data.local.dao.PrayerScheduleDao;
import com.devflux.deenone.data.local.dao.QuizDao;
import com.devflux.deenone.data.local.dao.SurahDao;
import com.devflux.deenone.data.local.dao.TasbihDao;
import com.devflux.deenone.data.local.dao.UserProfileDao;
import com.devflux.deenone.data.local.entity.AllahNameEntity;
import com.devflux.deenone.data.local.entity.AmalRecordEntity;
import com.devflux.deenone.data.local.entity.AzkarEntity;
import com.devflux.deenone.data.local.entity.BloodDonorEntity;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;
import com.devflux.deenone.data.local.entity.DailyContentEntity;
import com.devflux.deenone.data.local.entity.DuaEntity;
import com.devflux.deenone.data.local.entity.DuaLogEntity;
import com.devflux.deenone.data.local.entity.FeatureWidgetEntity;
import com.devflux.deenone.data.local.entity.HadithChapterEntity;
import com.devflux.deenone.data.local.entity.HadithEntity;
import com.devflux.deenone.data.local.entity.HalalFoodEntity;
import com.devflux.deenone.data.local.entity.IslamicBookEntity;
import com.devflux.deenone.data.local.entity.KalemaEntity;
import com.devflux.deenone.data.local.entity.NotificationMessageEntity;
import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranBookmarkEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;
import com.devflux.deenone.data.local.entity.QuizQuestionEntity;
import com.devflux.deenone.data.local.entity.QuizResultEntity;
import com.devflux.deenone.data.local.entity.TasbihEntity;
import com.devflux.deenone.data.local.entity.UserProfileEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.devflux.deenone.data.local.dao.BattleHistoryDao;
import com.devflux.deenone.data.local.entity.BattleHistoryEntity;

@Database(entities = {
        PrayerScheduleEntity.class,
        DailyContentEntity.class,
        AmalRecordEntity.class,
        DuaEntity.class,
        AzkarEntity.class,
        TasbihEntity.class,
        PrayerLogEntity.class,
        QuizQuestionEntity.class,
        HalalFoodEntity.class,
        KalemaEntity.class,
        AllahNameEntity.class,
        QuranSurahEntity.class,
        QuranAyahEntity.class,
        QuranBookmarkEntity.class,
        HadithEntity.class,
        HadithChapterEntity.class,
        DuaLogEntity.class,
        QuizResultEntity.class,
        DailyAmalEntity.class,
        NotificationMessageEntity.class,
        IslamicBookEntity.class,
        UserProfileEntity.class,
        BloodDonorEntity.class,
        FeatureWidgetEntity.class,
        BattleHistoryEntity.class
}, version = 18, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "deanone_database.db";
    private static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract PrayerScheduleDao prayerScheduleDao();
    public abstract DailyContentDao dailyContentDao();
    public abstract AmalRecordDao amalRecordDao();
    public abstract DailyAmalDao dailyAmalDao();
    public abstract DuaDao duaDao();
    public abstract DuaLogDao duaLogDao();
    public abstract AzkarDao azkarDao();
    public abstract TasbihDao tasbihDao();
    public abstract PrayerLogDao prayerLogDao();
    public abstract QuizDao quizDao();
    public abstract HalalDao halalDao();
    public abstract KalemaDao kalemaDao();
    public abstract AllahNameDao allahNameDao();
    public abstract SurahDao surahDao();
    public abstract AyahDao ayahDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract HadithDao hadithDao();
    public abstract HadithChapterDao hadithChapterDao();
    public abstract NotificationDao notificationDao();
    public abstract IslamicBookDao islamicBookDao();
    public abstract UserProfileDao userProfileDao();
    public abstract BloodDonorDao bloodDonorDao();
    public abstract FeatureWidgetDao featureWidgetDao();
    public abstract BattleHistoryDao battleHistoryDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME
            )
            .fallbackToDestructiveMigration()
            .addCallback(new Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriteExecutor.execute(() -> populateInitialData(instance));
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    databaseWriteExecutor.execute(() -> checkAndSeedData(instance));
                }
            })
            .build();
        }
        return instance;
    }

    private static void checkAndSeedData(AppDatabase db) {
        if (db.kalemaDao().getCount() == 0) {
            populateInitialData(db);
        }
        // Seed Tasbih if not present
        if (db.tasbihDao().getDhikrCount() == 0) {
            seedDefaultTasbih(db);
        }
        // Seed Azkar if not present
        if (db.azkarDao().getAzkarCount() == 0) {
            seedDefaultAzkar(db);
        }
        // Seed User Profile if not present
        if (db.userProfileDao().getProfileCount() == 0) {
            seedDefaultUserProfile(db);
        }
        // Purge any legacy dummy blood donors
        db.bloodDonorDao().deleteDummyDonors();
        // Seed Feature Widgets (27 items in screenshot order) if not present
        if (db.featureWidgetDao().getFeatureCount() == 0) {
            seedDefaultFeatureWidgets(db);
        }
        // Seed Hadith Chapters (e.g. Sahih Bukhari) if not present or incomplete
        if (db.hadithChapterDao().getChapterCount("bukhari") < 41) {
            seedDefaultHadithChapters(db);
        }
        // Seed 114 Surahs if not present
        if (db.surahDao().getSurahCount() < 114) {
            db.surahDao().insertSurahs(QuranSurahDataSeeder.get114Surahs());
        }
        // Seed Essential Ayahs if not present
        if (db.ayahDao().getTotalAyahCount() == 0) {
            db.ayahDao().insertAyahs(QuranSurahDataSeeder.getEssentialAyahs());
        }
    }

    private static void seedDefaultHadithChapters(AppDatabase db) {
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
        db.hadithChapterDao().insertAll(list);
    }

    private static void seedDefaultFeatureWidgets(AppDatabase db) {
        List<com.devflux.deenone.data.local.entity.FeatureWidgetEntity> list = new ArrayList<>();
        // Row 1
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_1", "সালাত", "ic_feat_salat", "#38230E", "#F59E0B", 1, true, "action_salat"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_2", "নামাজ শিক্ষা", "ic_feat_namaz_shikha", "#0E3827", "#34D399", 2, true, "action_namaz_shikha"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_3", "কুরআন মাজিদ", "ic_feat_quran", "#0E3827", "#10B981", 3, true, "action_quran"));

        // Row 2
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_4", "ইসলামিক অডিও হাব", "ic_feat_audio", "#0F3A2C", "#34D399", 4, true, "action_audio"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_5", "ইসলামিক বই", "ic_feat_book", "#103833", "#2DD4BF", 5, true, "action_books"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_6", "হাদিস শরিফ", "ic_feat_hadith", "#102838", "#38BDF8", 6, true, "action_hadith"));

        // Row 3
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_7", "দোয়া ভাণ্ডার", "ic_feat_dua", "#381028", "#F472B6", 7, true, "action_dua"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_8", "আমল ট্র্যাকার", "ic_feat_amal", "#102738", "#60A5FA", 8, true, "action_amal"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_9", "কুইজ হাব", "ic_feat_quiz", "#291238", "#C084FC", 9, true, "action_quiz"));

        // Row 4
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_10", "মসজিদ সন্ধান", "ic_feat_mosque", "#103338", "#22D3EE", 10, true, "action_mosque"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_11", "সফর মোড", "ic_feat_safar", "#102B38", "#38BDF8", 11, true, "action_safar"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_12", "জুম্মা মোড", "ic_feat_jumma", "#0E3827", "#34D399", 12, true, "action_jummah"));

        // Row 5
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_13", "ঈদ মোড", "ic_feat_eid", "#38320E", "#FBBF24", 13, true, "action_eid"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_14", "জানাযা গাইড", "ic_feat_janaza", "#103538", "#2DD4BF", 14, true, "action_janaza"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_15", "রোজা ও রমজান", "ic_feat_ramadan", "#38230E", "#FB923C", 15, true, "action_roza_ramadan"));

        // Row 6
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_16", "খতম প্ল্যানার", "ic_feat_khatam", "#0E3827", "#34D399", 16, true, "action_khatm"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_17", "আজকের আয়াত", "ic_feat_ayah", "#103834", "#2DD4BF", 17, true, "action_daily_ayah"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_18", "হজ ও উমরাহ", "ic_feat_hajj", "#382B10", "#FBBF24", 18, true, "action_hajj"));

        // Row 7
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_19", "নবীদের জীবনী", "ic_feat_prophets", "#103833", "#2DD4BF", 19, true, "action_prophets"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_20", "হিজরি ক্যালেন্ডার", "ic_feat_calendar", "#102B38", "#818CF8", 20, true, "action_calendar"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_21", "যাকাত ক্যালকুলেটর", "ic_feat_zakat", "#281238", "#C084FC", 21, true, "action_zakat"));

        // Row 8
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_22", "তাসবিহ কাউন্টার", "ic_feat_tasbih", "#0F3A30", "#34D399", 22, true, "action_tasbih"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_23", "দৈনিক আজকার", "ic_feat_azkar", "#381F0A", "#FB923C", 23, true, "action_azkar"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_24", "কিবলা কম্পাস", "ic_feat_qibla", "#152D32", "#2DD4BF", 24, true, "action_qibla"));

        // Row 9
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_25", "মুসলিম বিবাহ", "ic_feat_marriage", "#0E3B27", "#34D399", 25, true, "action_marriage"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_26", "উত্তরাধিকার বণ্টন", "ic_feat_faraid", "#122E2B", "#35D99B", 26, true, "action_faraid"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_27", "রক্তদান সেকশন", "ic_feat_blood", "#3C1215", "#EF4444", 27, true, "action_blood"));

        // Row 10
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_28", "আল্লাহর ৯৯ নাম", "ic_feat_allah_names", "#0E3827", "#34D399", 28, true, "action_allah_names"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_29", "৬ কালিমা", "ic_feat_kalima", "#382810", "#FBBF24", 29, true, "action_six_kalima"));
        list.add(new com.devflux.deenone.data.local.entity.FeatureWidgetEntity("feat_30", "নলেজ ব্যাটেল", "ic_feat_battle", "#281238", "#C084FC", 30, true, "action_knowledge_battle"));

        db.featureWidgetDao().insertAll(list);
    }


    private static void seedDefaultUserProfile(AppDatabase db) {
        com.devflux.deenone.data.local.entity.UserProfileEntity defaultProfile =
                new com.devflux.deenone.data.local.entity.UserProfileEntity(
                        "usr_main",
                        "ব্যবহারকারী",
                        "",
                        "",
                        "Asia/Dhaka",
                        System.currentTimeMillis(),
                        0,
                        1,
                        1,
                        false,
                        "",
                        0,
                        0,
                        0,
                        0,
                        System.currentTimeMillis()
                );
        db.userProfileDao().insertOrUpdateProfile(defaultProfile);
    }

    private static void populateInitialData(AppDatabase db) {
        // 1. Seed 6 Kalimas
        List<KalemaEntity> kalemas = new ArrayList<>();
        kalemas.add(new KalemaEntity(
                1,
                "১. কালেমা তাইয়্যেবা",
                "Kalima Tayyibah",
                "لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ",
                "লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ।",
                "আল্লাহ ছাড়া কোনো উপাস্য নেই, মুহাম্মদ (সা.) আল্লাহর প্রেরিত রসূল।",
                "ঈমানের মূল ভিত্তি ও জান্নাতের চাবিকাঠি।"
        ));
        kalemas.add(new KalemaEntity(
                2,
                "২. কালেমা শাহাদাত",
                "Kalima Shahadat",
                "أَشْهَدُ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ",
                "আশহাদু আল লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, ওয়া আশহাদু আন্না মুহাম্মাদান আবদুহু ওয়া রাসুলুহু।",
                "আমি সাক্ষ্য দিচ্ছি যে, আল্লাহ ছাড়া কোনো উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। এবং আমি আরও সাক্ষ্য দিচ্ছি যে, মুহাম্মদ (সা.) তাঁর বান্দা ও রাসুল।",
                "অজু করার পর পাঠ করলে জান্নাতের আটটি দরজাই উন্মুক্ত হয়ে যায়।"
        ));
        kalemas.add(new KalemaEntity(
                3,
                "৩. কালেমা তামজীদ",
                "Kalima Tamjeed",
                "سُبْحَانَ اللهِ وَالْحَمْدُ لِلَّهِ وَلَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ",
                "সুবহানাল্লাহি ওয়াল হামদুলিল্লাহি ওয়া লা ইলাহা ইল্লাল্লাহু ওয়াল্লাহু আকবার, ওয়া লা হাওলা ওয়া লা কুওয়াতা ইল্লা বিল্লাহিল আলিয়্যিল আজিম।",
                "আল্লাহ মহাপবিত্র, সকল প্রশংসা আল্লাহর, আল্লাহ ছাড়া কোনো উপাস্য নেই এবং আল্লাহ সর্বশ্রেষ্ঠ। মহান ও সর্বশক্তিমান আল্লাহর সাহায্য ছাড়া পাপ থেকে বাঁচার বা পুণ্য করার কোনো শক্তি নেই।",
                "এই জিকির জান্নাতের রোপণ করা বৃক্ষস্বরূপ।"
        ));
        kalemas.add(new KalemaEntity(
                4,
                "৪. কালেমা তাওহীদ",
                "Kalima Tawheed",
                "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ يُحْيِي وَيُمِيتُ وَهُوَ حَيٌّ لَا يَمُوتُ أَبَدًا أَبَدًا، ذُو الْجَلَالِ وَالإِكْرَامِ، بِيَدِهِ الْخَيْرُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু ইউহই ওয়া ইউমিতু ওয়া হুওয়া হাইয়ুল লা ইয়ামূতু আবাদান আবাদা, যুল জালালি ওয়াল ইকরাম, বিয়াদিহিল খাইরু ওয়া হুওয়া আলা কুল্লি শাইয়িন ক্বদীর।",
                "আল্লাহ ছাড়া কোনো উপাস্য নেই, তিনি এক ও তাঁর কোনো শরিক নেই। সার্বভৌম রাজত্ব ও সমস্ত প্রশংসা একমাত্র তাঁরই। তিনিই জীবন দান করেন ও মৃত্যু ঘটান। তিনি চিরঞ্জীব, তাঁর কখনো মৃত্যু হবে না। তিনি মহিমান্বিত ও মর্যাদাবান। সমস্ত কল্যাণ তাঁর হাতেই এবং তিনি সবকিছুর ওপর সর্বশক্তিমান।",
                "বাজারে বা কর্মক্ষেত্রে প্রবেশের সময় পাঠ করলে ১০ লক্ষ নেকি লাভ হয়।"
        ));
        kalemas.add(new KalemaEntity(
                5,
                "৫. কালেমা রদ্দে কুফর",
                "Kalima Radde Kufr",
                "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ أَنْ أُشْرِكَ بِكَ شَيْئًا وَأَنَا أَعْلَمُ، وَأَسْتَغْفِرُكَ لِمَا لَا أَعْلَمُ، تُبْتُ عَنْهُ وَتَبَرَّأْتُ مِنَ الْكُفْرِ وَالشِّرْكِ وَالْكِذْبِ وَالْغِيبَةِ وَالْبِدْعَةِ وَالنَّمِيمَةِ وَالْفَوَاحِشِ وَالْبُهْتَانِ وَالْمَعَاصِي كُلِّهَا، وَأَسْلَمْتُ وَأَقُولُ: لَا إِلٰهَ إِلَّا اللهُ مُحَمَّدٌ رَسُولُ اللهِ",
                "আল্লাহুম্মা ইন্নি আউযুবিকা মিন আন উশরিকা বিকা শাইআও ওয়া আনা আ'লামু, ওয়াস্তাগফিরুকা লিমা লা আ'লামু, তুবতু আনহু ওয়া তাবাররা'তু মিনাল কুফরি ওয়াশ শিরকি ওয়াল কিযবি ওয়াল গীবতি ওয়াল বিদআতি ওয়ান নামীমাতি ওয়াল ফাওয়াহিশি ওয়াল বুহতানি ওয়াল মাআসি কুল্লিহা, ওয়া আসলামতু ওয়া আকূলু: লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ।",
                "হে আল্লাহ! নিশ্চয়ই আমি জেনে-শুনে আপনার সাথে কাউকে শরিক করা থেকে আপনার কাছে আশ্রয় চাই এবং যা আমার অজ্ঞাত রয়েছে তা থেকেও ক্ষমা প্রার্থনা করছি। আমি কুফর, শিরক, মিথ্যা, গিবত, বিদআত, চোগলখোরি, অশ্লীলতা, অপবাদ এবং যাবতীয় পাপাচার থেকে তওবা করলাম ও মুক্ত হলাম। আমি আত্মসমর্পণ করলাম ও ঘোষণা করছি: আল্লাহ ছাড়া কোনো উপাস্য নেই, মুহাম্মদ (সা.) আল্লাহর রাসুল।",
                "ঈমান তাজা রাখতে ও শিরক থেকে বাঁচতে বিশেষ দোআ।"
        ));
        kalemas.add(new KalemaEntity(
                6,
                "৬. কালেমা ইস্তিগফার",
                "Kalima Astaghfar",
                "أَسْتَغْفِرُ اللهَ رَبِّي مِنْ كُلِّ ذَنْبٍ أَذْنَبْتُهُ عَمْدًا أَوْ خَطَأً سِرًّا أَوْ عَلَانِيَةً، وَأَتُوبُ إِلَيْهِ مِنَ الذَّنْبِ الَّذِي أَعْلَمُ وَمِنَ الذَّنْبِ الَّذِي لَا أَعْلَمُ، إِنَّكَ أَنْتَ عَلَّامُ الْغُيُوبِ وَسَتَّارُ الْعُيُوبِ وَغَفَّارُ الذُّنُوبِ، وَلَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ الْعَلِيِّ الْعَظِيمِ",
                "আস্তাগফিরুল্লাহা রব্বি মিন কুল্লি যাম্বিন আযনাবতুহু আমাদান আও খাতায়ান সিররান আও আলানিয়াতান, ওয়া আতূবু ইলাইহি মিনায যামবিল্লাজি আ'লামু ওয়া মিনায যামবিল্লাজি লা আ'লামু, ইন্নাকা আনতা আল্লামুল গুয়ুবী ওয়া সাত্তারুল উয়ূবী ওয়া গাফফারুয যুনূব, ওয়া লা হাওলা ওয়া লা কুওয়াতা ইল্লা বিল্লাহিল আলিয়্যিল আজিম।",
                "আমি আমার প্রভু আল্লাহর নিকট ক্ষমা প্রার্থনা করছি এমন প্রতিটি পাপের জন্য যা আমি জেনে বা ভুলে, গোপনে বা প্রকাশ্যে করেছি। এবং আমি তাঁর কাছে সেই পাপ থেকেও তওবা করছি যা আমি জানি এবং যা জানি না। নিশ্চয়ই আপনি অদৃশ্য বিষয়ে সম্পূর্ণ জ্ঞাত, সমস্ত ত্রুটি গোপনকারী ও যাবতীয় পাপ ক্ষমাকারী। মহান ও সর্বশক্তিমান আল্লাহর শক্তি ব্যতীত পাপ থেকে নিষ্কৃতি পাওয়া অসম্ভব।",
                "সকল গুনাহ মাফ ও মানসিক প্রশান্তির মহা ঔষধ।"
        ));
        db.kalemaDao().insertAll(kalemas);

        // 2. Seed 99 Names of Allah (Asmaul Husna)
        List<AllahNameEntity> names = new ArrayList<>();
        names.add(new AllahNameEntity(1, "الرَّحْمٰنُ", "আর-রাহমান", "পরম দয়ালু", "The Most Gracious", "প্রতিদিন ১০০ বার পাঠ করলে হৃদয় থেকে কঠোরতা দূর হয়ে করুণা সৃষ্টি হয়।"));
        names.add(new AllahNameEntity(2, "الرَّحِيمُ", "আর-রাহীম", "অতিশয় মেহেরবান", "The Most Merciful", "প্রতি ওয়াক্তের পর পাঠ করলে দুনিয়া ও আখিরাতের বিপদ থেকে মুক্তি মেলে।"));
        names.add(new AllahNameEntity(3, "الْمَلِكُ", "আল-মালিক", "সার্বভৌম ক্ষমতার অধিকারী / রাজা", "The Absolute Ruler", "নিয়মিত পাঠে আত্মসম্মান ও মর্যাদা বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(4, "الْقُدُّوسُ", "আল-কুদ্দুস", "মহা পবিত্র / নিষ্কলঙ্ক", "The Most Sacred", "মানসিক অশান্তি ও হৃদয়ের পাপমোচনে কার্যকর।"));
        names.add(new AllahNameEntity(5, "السَّلَامُ", "আস-সালাম", "শান্তি দানকারী / নিরাপত্তাদাতা", "The Giver of Peace", "অসুস্থ ব্যক্তির জন্য পাঠ করলে রোগমুক্তি ও প্রশান্তি লাভ হয়।"));
        names.add(new AllahNameEntity(6, "الْمُؤْمِنُ", "আল-মুমিন", "নিরাপত্তা ও ঈমান দানকারী", "The Granter of Security", "ভয় ও শঙ্কা দূর করতে এই নামের জিকির অত্যন্ত ফলদায়ক।"));
        names.add(new AllahNameEntity(7, "الْمُهَيْمِنُ", "আল-মুহাইমিন", "রক্ষক ও তত্ত্বাবধায়ক", "The Guardian", "অন্তর পরিশুদ্ধ হয় এবং অন্তর্দৃষ্টি লাভ হয়।"));
        names.add(new AllahNameEntity(8, "الْعَزِيزُ", "আল-আজিজ", "মহাপরাক্রমশালী ও অপ্রতিরোধ্য", "The All-Mighty", "নিয়মিত জিকিরে সম্মান, ইজ্জত ও সফলতা অর্জিত হয়।"));
        names.add(new AllahNameEntity(9, "الْجَبَّارُ", "আল-জাব্বার", "মহাপ্রতাপশালী / সংশোধনকারী", "The Restorer", "জালেমের অত্যাচার থেকে হেফাজত থাকতে সহায়তা করে।"));
        names.add(new AllahNameEntity(10, "الْمُتَكَبِّرُ", "আল-মুতাকাব্বির", "সর্বশ্রেষ্ঠ মর্যাদাবান ও অহংকারের একমাত্র অধিকারী", "The Supreme", "সৎ কাজে বরকত ও প্রভাব অর্জনে উপকারী।"));
        names.add(new AllahNameEntity(11, "الْخَالِقُ", "আল-খালিক", "সৃষ্টিকর্তা", "The Creator", "আল্লাহর সৃষ্টির মহিমা অনুধাবন ও অন্তরে নূর সৃষ্টি হয়।"));
        names.add(new AllahNameEntity(12, "الْبَارِئُ", "আল-বারি", "উদ্ভাদক / প্রাণদাতা", "The Originator", "কঠিন সমস্যা সমাধানে এই নামের জিকির সহায়ক।"));
        names.add(new AllahNameEntity(13, "الْمُصَوِّرُ", "আল-মুসাওয়ির", "আকৃতি দানকারী", "The Shaper of Beauty", "সন্তানহীনদের জন্য ইস্তিগফারের সাথে পাঠে কল্যাণকর।"));
        names.add(new AllahNameEntity(14, "الْغَفَّارُ", "আল-গাফফার", "মহাক্ষমাশীল", "The Forgiving", "অসংখ্য পাপ মার্জনা এবং তাওবা কবুল হয়।"));
        names.add(new AllahNameEntity(15, "الْقَهَّارُ", "আল-কাহ্হার", "মহাদমনকারী", "The All-Subduer", "শত্রুর ষড়যন্ত্র ও কুপ্রবৃত্তি দমনে কার্যকর।"));
        names.add(new AllahNameEntity(16, "الْوَهَّابُ", "আল-ওয়াহহাব", "সীমাহীন দানশীল", "The Bestower", "রিজিক ও অভাব দূর করতে সেজদায় এই নাম পাঠ করা হয়।"));
        names.add(new AllahNameEntity(17, "الرَّزَّاقُ", "আর-রাজ্জাক", "রিজিকদাতা", "The Provider", "সকালে ফজরের পর পাঠ করলে জীবিকায় প্রভূত বরকত আসে।"));
        names.add(new AllahNameEntity(18, "الْفَتَّاحُ", "আল-ফাত্তাহ", "বিজয়দানকারী / দ্বার উন্মোচনকারী", "The Opener", "বন্ধ ভাগ্য ও সফলতার পথ উন্মুক্ত হতে সহায়তা করে।"));
        names.add(new AllahNameEntity(19, "الْعَلِيمُ", "আল-আলিম", "সর্বজ্ঞাত", "The All-Knowing", "মেধা, প্রজ্ঞা ও ইলম বৃদ্ধিতে সহায়ক।"));
        names.add(new AllahNameEntity(20, "الْقَابِضُ", "আল-কাবিদ", "সংকোচনকারী", "The Withholder", "ভয় ও হতাশা কাটিয়ে আল্লাহর ওপর তাওয়াক্কুল বাড়ে।"));
        names.add(new AllahNameEntity(21, "الْبَاسِطُ", "আল-বাসিত", "সম্প্রসারণকারী", "The Expander", "চাশতের নামাজের পর পাঠ করলে দারিদ্র্য দূর হয়।"));
        names.add(new AllahNameEntity(22, "الْخَافِضُ", "আল-খাফিদ", "অবনমিতকারী", "The Abaser", "অহংকার চূর্ণ ও জালিমের পতন ঘটে।"));
        names.add(new AllahNameEntity(23, "الرَّافِعُ", "আর-রাফি", "উন্নতকারী / মর্যাদাদানকারী", "The Exalter", "মর্যাদা ও সম্মান বৃদ্ধিতে সাহায্য করে।"));
        names.add(new AllahNameEntity(24, "الْمُعِزُّ", "আল-মুইজ্জ", "সম্মানদানকারী", "The Bestower of Honor", "মানুষের অন্তরে প্রিয় ও সম্মানিত হতে সহায়ক।"));
        names.add(new AllahNameEntity(25, "الْمُذِلُّ", "আল-মুজিল্ল", "অপমানকারী", "The Dishonorer", "শত্রুর ক্ষতি ও অপবাদ থেকে হেফাজত মেলে।"));
        names.add(new AllahNameEntity(26, "السَّمِيعُ", "আস-সামি", "সর্বশ্রোতা", "The All-Hearing", "দোয়া কবুল হওয়ার জন্য অত্যন্ত কার্যকর নাম।"));
        names.add(new AllahNameEntity(27, "الْبَصِيرُ", "আল-বাসির", "সর্বদ্রষ্টা", "The All-Seeing", "দৃষ্টিশক্তি ও অন্তর্দৃষ্টির জ্যোতি বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(28, "الْحَكَمُ", "আল-হাকাম", "চূড়ান্ত বিচারক", "The Impartial Judge", "সঠিক সিদ্ধান্ত গ্রহণে প্রজ্ঞা লাভ হয়।"));
        names.add(new AllahNameEntity(29, "الْعَدْلُ", "আল-আদল", "পরম ন্যায়পরায়ণ", "The Utterly Just", "ন্যায্য অধিকার ও ইনসাফ প্রতিষ্ঠায় সহায়ক।"));
        names.add(new AllahNameEntity(30, "اللَّطِيفُ", "আল-লতিফ", "পরম স্নেহশীল ও সূক্ষ্মদর্শী", "The Subtle One", "কঠিন বিপদ ও অভাব মুহূর্তে পাঠে আল্লাহর অদৃশ্য সাহায্য আসে।"));
        names.add(new AllahNameEntity(31, "الْخَبِيرُ", "আল-খাবির", "সর্বজ্ঞাত ও সম্যক অবগত", "The All-Aware", "সঠিক পথ নির্দেশনা মেলে।"));
        names.add(new AllahNameEntity(32, "الْحَلِيمُ", "আল-হালিম", "পরম ধৈর্যশীল ও সহনশীল", "The Most Forbearing", "রাগ ও ক্রোধ নিয়ন্ত্রণে সাহায্য করে।"));
        names.add(new AllahNameEntity(33, "الْعَظِيمُ", "আল-আজিম", "মর্যাদাময় ও সুমহান", "The Magnificent", "রোগব্যাধি থেকে আরোগ্য ও মানসিক শক্তি লাভ হয়।"));
        names.add(new AllahNameEntity(34, "الْغَفُورُ", "আল-গাফুর", "মহাক্ষমাশীল", "The Great Forgiver", "অন্তর শান্ত হয় ও পাপরাশি মাফ হয়।"));
        names.add(new AllahNameEntity(35, "الشَّكُورُ", "আশ-শাকুর", "কৃতজ্ঞতা গ্রহণকারী ও বহুগুণ প্রতিদানকারী", "The Most Appreciative", "স্বল্প আমলে বিপুল সওয়াব মেলে।"));
        names.add(new AllahNameEntity(36, "الْعَلِيُّ", "আল-আলি", "সর্বোচ্চ ও সুউচ্চ", "The Most High", "মর্যাদা ও আধ্যাত্মিক উন্নতি অর্জিত হয়।"));
        names.add(new AllahNameEntity(37, "الْكَبِيرُ", "আল-কবীর", "সবচেয়ে মহান ও সর্বশ্রেষ্ঠ", "The Most Great", "অহংকার দূর হয় ও ঈমান দৃঢ় হয়।"));
        names.add(new AllahNameEntity(38, "الْحَفِيظُ", "আল-হাফিজ", "মহারক্ষক", "The Preserver", "যাবতীয় বিপদাপদ ও ক্ষতি থেকে আল্লাহর সুরক্ষা লাভ হয়।"));
        names.add(new AllahNameEntity(39, "الْمُقِيتُ", "আল-মুকিত", "জীবনোপকরণ ও শক্তিদাতা", "The Sustainer", "দুর্বলতা দূর হয় ও দৈহিক শক্তি লাভ হয়।"));
        names.add(new AllahNameEntity(40, "الْحَسِيبُ", "আল-হাসিব", "হিসাব গ্রহণকারী ও যথেষ্ট সত্তা", "The Reckoner", "আল্লাহই যথেষ্ট এই তাওয়াক্কুল অন্তরে প্রতিষ্ঠিত হয়।"));
        names.add(new AllahNameEntity(41, "الْجَلِيلُ", "আল-জালিল", "মহিমাময় ও প্রতাপশালী", "The Majestic", "সম্মান ও মর্যাদা বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(42, "الْكَرِيمُ", "আল-কারিম", "সীমাহীন দয়ালু ও মহানুভব", "The Most Generous", "দয়া ও দানশীলতার সওয়াব অর্জিত হয়।"));
        names.add(new AllahNameEntity(43, "الرَّقِيبُ", "আর-রাকিব", "সর্বদা নজরদারীকারী / পর্যবেক্ষক", "The Watchful", "গোপন পাপ থেকে বিরত থাকতে সহায়তা করে।"));
        names.add(new AllahNameEntity(44, "الْمُجِيبُ", "আল-মুজিব", "দোয়া কবুলকারী", "The Responsive One", "দোয়া কবুলের বিশেষ মাধ্যম।"));
        names.add(new AllahNameEntity(45, "الْوَاسِعُ", "আল-ওয়াসি", "সর্বব্যাপী ও প্রাচুর্যময়", "The All-Encompassing", "রিজিক ও জ্ঞানে প্রশস্ততা আসে।"));
        names.add(new AllahNameEntity(46, "الْحَكِيمُ", "আল-হাকিম", "পরম প্রজ্ঞাময়", "The All-Wise", "প্রজ্ঞা ও উত্তম বিচারশক্তি লাভ হয়।"));
        names.add(new AllahNameEntity(47, "الْوَدُودُ", "আল-ওয়াদুদ", "প্রেমময় ও ভালোবাসার আধার", "The Loving One", "পারিবারিক কলহ দূর ও ভালোবাসা বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(48, "الْمَجِيدُ", "আল-মাজিদ", "মর্যাদাবান ও গৌরবময়", "The Most Glorious", "অন্তরে নূর ও পবিত্রতা বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(49, "الْبَاعِثُ", "আল-বাইস", "পুনরুত্থানকারী", "The Resurrector", "আখিরাতের ভয় ও নেক আমলের স্পৃহা জাগে।"));
        names.add(new AllahNameEntity(50, "الشَّهِيدُ", "আশ-শাহীদ", "সর্বদা প্রত্যক্ষকারী ও সাক্ষী", "The All-Witnessing", "অবাধ্য সন্তান ও অন্তরকে সত্যের পথে আনে।"));
        names.add(new AllahNameEntity(51, "الْحَقُّ", "আল-হাক্ক", "চিরন্তন সত্য", "The Absolute Truth", "হারানো জিনিস ফিরে পেতে সহায়ক।"));
        names.add(new AllahNameEntity(52, "الْوَكِيلُ", "আল-ওয়াকিল", "উত্তম কর্মবিধায়ক ও নির্ভরস্থল", "The Trustee", "যেকোনো ভয় ও সংকটে তাওয়াক্কুলের শ্রেষ্ঠ ভিত্তি।"));
        names.add(new AllahNameEntity(53, "الْقَوِيُّ", "আল-কাউয়ী", "মহা শক্তিশালী", "The All-Strong", "শারীরিক ও ঈমানি বল বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(54, "الْمَتِينُ", "আল-মাতীন", "সুদৃঢ় ও পরাক্রমশালী", "The Firm", "কঠিন বিপদ থেকে সুরক্ষা মেলে।"));
        names.add(new AllahNameEntity(55, "الْوَلِيُّ", "আল-ওয়ালি", "অভিভাবক ও বন্ধু", "The Protecting Friend", "আল্লাহর নৈকট্য ও অভিভাবকত্ব লাভ হয়।"));
        names.add(new AllahNameEntity(56, "الْحَمِيدُ", "আল-হামিদ", "সকল প্রশংসার একমাত্র যোগ্য", "The Praiseworthy", "হৃদয় আল্লাহর প্রশংসায় মুখরিত থাকে।"));
        names.add(new AllahNameEntity(57, "الْمُحْصِي", "আল-মুহসী", "সবকিছুর পুঙ্খানুপুঙ্খ হিসাবকারী", "The Appraiser", "হিসাবের দিনের ভয় ও নেক আমলের আগ্রহ বাড়ে।"));
        names.add(new AllahNameEntity(58, "الْمُبْدِئُ", "আল-মুবদি", "প্রথম সৃষ্টিকারী", "The Originator", "নতুন কাজে সফলতা ও বরকত আনে।"));
        names.add(new AllahNameEntity(59, "الْمُعِيدُ", "আল-মুঈদ", "পুনর্বার সৃষ্টিকারী", "The Restorer", "হারানো সম্পদ ফিরে পাওয়ার সম্ভাবনা বাড়ে।"));
        names.add(new AllahNameEntity(60, "الْمُحْيِي", "আল-মুহইয়ী", "জীবনদানকারী", "The Giver of Life", "অসুস্থতা দূর ও আধ্যাত্মিক পুনরুজ্জীবন ঘটে।"));
        names.add(new AllahNameEntity(61, "الْمُمِيتُ", "আল-মুমীত", "মৃত্যুদানকারী", "The Bringer of Death", "কুপ্রবৃত্তি ও নফসের দাসত্ব থেকে মুক্তি মেলে।"));
        names.add(new AllahNameEntity(62, "الْحَيُّ", "আল-হাইয়্যু", "চিরঞ্জীব", "The Ever-Living", "ইয়া হাইয়্যু ইয়া কাইয়্যুম পাঠে সমস্ত মুসিবত দূর হয়।"));
        names.add(new AllahNameEntity(63, "الْقَيُّومُ", "আল-কাইয়্যুম", "সবকিছুর ধারক ও রক্ষক", "The Self-Subsisting", "ক্লান্তি ও অবসাদ দূর হয়।"));
        names.add(new AllahNameEntity(64, "الْوَاجِدُ", "আল-ওয়াজিদ", "অভাবহীন ও স্বয়ংসম্পূর্ণ", "The Finder", "অন্তরের তৃপ্তি ও ধনী ভাব সৃষ্টি হয়।"));
        names.add(new AllahNameEntity(65, "الْمَاجِدُ", "আল-মাজিদ", "মহিমান্বিত ও মর্যাদাবান", "The Noble", "সম্মান ও মর্যাদা বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(66, "الْوَاحِدُ", "আল-ওয়াহিদ", "এক ও অদ্বিতীয়", "The Unique One", "শিরক থেকে অন্তরের মুক্তি মেলে।"));
        names.add(new AllahNameEntity(67, "الْأَحَدُ", "আল-আহাদ", "একক সত্ত্বা", "The Only One", "তাওহীদের গভীর বিশ্বাস প্রোথিত হয়।"));
        names.add(new AllahNameEntity(68, "الصَّمَدُ", "আস-সামাদ", "অমুখাপেক্ষী", "The Eternal Refuge", "সকল সৃষ্টির প্রয়োজন মেটানোর একমাত্র আশ্রয়।"));
        names.add(new AllahNameEntity(69, "الْقَادِرُ", "আল-কাদির", "সর্বশক্তিমান", "The Capable", "অসম্ভব কাজ সহজ হতে আল্লাহর সাহায্য আসে।"));
        names.add(new AllahNameEntity(70, "الْمُقْتَدِرُ", "আল-মুকতাদির", "প্রভাবশালী ক্ষমতার অধিকারী", "The Omnipotent", "কঠিন পরিস্থিতিতে শক্তি সঞ্চার হয়।"));
        names.add(new AllahNameEntity(71, "الْمُقَدِّمُ", "আল-মুকাদ্দিম", "অগ্রগামীকারী", "The Expediter", "উন্নতি ও অগ্রযাত্রায় সহায়ক।"));
        names.add(new AllahNameEntity(72, "الْمُؤَخِّرُ", "আল-মুআখ্খির", "পশ্চাদপসরণকারী", "The Delayer", "ক্ষতিকর বিষয় বিলম্বিত বা দূর করতে সহায়ক।"));
        names.add(new AllahNameEntity(73, "الْأَوَّلُ", "আল-আউয়াল", "সর্বপ্রথম (অনাদি)", "The Very First", "সন্তান লাভ ও কাজে বরকত আসে।"));
        names.add(new AllahNameEntity(74, "الْآخِرُ", "আল-আখির", "সর্বশেষ (অনন্ত)", "The Very Last", "সুন্দর সমাপ্তি ও ঈমানের সাথে মৃত্যুর দোয়া।"));
        names.add(new AllahNameEntity(75, "الظَّاهِرُ", "আজ-জাহির", "প্রকাশ্য", "The Manifest", "সৃষ্টির মাঝে আল্লাহর নির্দশন স্পষ্ট হয়।"));
        names.add(new AllahNameEntity(76, "الْبَاطِنُ", "আল-বাতিন", "গোপন ও অদৃশ্য", "The Hidden", "হৃদয়ে নূর ও আধ্যাত্মিক প্রশান্তি লাভ হয়।"));
        names.add(new AllahNameEntity(77, "الْوَالِي", "আল-ওয়ালী", "একমাত্র অভিভাবক ও শাসক", "The Governing Lord", "ঘরের ও জানমালের নিরাপত্তা মেলে।"));
        names.add(new AllahNameEntity(78, "الْمُتَعَالِي", "আল-মুতাআলী", "সর্বোচ্চ মর্যাদাবান", "The Supreme Exalted", "কঠিন সমস্যা সমাধানে কল্যাণকর।"));
        names.add(new AllahNameEntity(79, "الْبَرُّ", "আল-বার্", "পরম অনুগ্রহকারী ও কল্যাণদাতা", "The Source of Goodness", "পাপ থেকে বিরত থাকতে ও নেককার হতে সহায়ক।"));
        names.add(new AllahNameEntity(80, "التَّوَّابُ", "আত-তাওয়াব", "তওবা কবুলকারী", "The Ever-Pardoning", "বারংবার তওবা কবুল হওয়ার বিশেষ নাম।"));
        names.add(new AllahNameEntity(81, "الْمُنْتَقِمُ", "আল-মুনতাকিম", "অন্যায়কারীদের শাস্তিদাতা", "The Retaliator", "জালেমের হাত থেকে পরিত্রাণ মেলে।"));
        names.add(new AllahNameEntity(82, "الْعَفُوُّ", "আল-আফুউ", "পরম মার্জনাশীল", "The Pardoner", "শবে কদরের বিশেষ দোয়া: আল্লাহুম্মা ইন্নাকা আফুউউন।"));
        names.add(new AllahNameEntity(83, "الرَّؤُوفُ", "আর-রাউফ", "পরম স্নেহশীল ও দয়াবান", "The Most Kind", "মানুষের মধ্যে স্নেহ ও দয়া বৃদ্ধি পায়।"));
        names.add(new AllahNameEntity(84, "مَالِكُ الْمُلْكِ", "মালিকুল মুলক", "সার্বভৌম রাজত্বের একমাত্র মালিক", "Master of Dominion", "অভাব ও পরমুখাপেক্ষিতা দূর হয়।"));
        names.add(new AllahNameEntity(85, "ذُو الْجَلَالِ وَالْإِكْرَامِ", "যুল জালালি ওয়াল ইকরাম", "মহিমাময় ও পরম শ্রদ্ধার অধিকারী", "Lord of Glory and Honor", "দোয়া কবুলের ইসমে আজম হিসেবে গণ্য।"));
        names.add(new AllahNameEntity(86, "الْمُقْسِطُ", "আল-মুকসিত", "ন্যায়পরায়ণ ও সমতাবিধানকারী", "The Equitable", "শয়তানের ওয়াসওয়াসা থেকে সুরক্ষা মেলে।"));
        names.add(new AllahNameEntity(87, "الْجَامِعُ", "আল-জামি", "একত্রকারী / সমবেতকারী", "The Gatherer", "বিচ্ছিন্ন পরিবার বা হারানো জিনিস মেলাতে সহায়ক।"));
        names.add(new AllahNameEntity(88, "الْغَنِيُّ", "আল-গানি", "স্বয়ংসম্পূর্ণ ও প্রাচুর্যময়", "The Self-Sufficient", "দারিদ্র্য দূর ও অন্তরের প্রাচুর্য আসে।"));
        names.add(new AllahNameEntity(89, "الْمُغْنِي", "আল-মুগনি", "অন্যকে সমৃদ্ধকারী", "The Enricher", "আর্থিক সংকট থেকে উত্তরণ ঘটে।"));
        names.add(new AllahNameEntity(90, "الْمَانِعُ", "আল-মানি", "ক্ষতি প্রতিরোধকারী", "The Preventer", "বিপদাপদ ও শত্রুতার হাত থেকে বাধা প্রদান করে।"));
        names.add(new AllahNameEntity(91, "الضَّارُّ", "আদ-দার", "ক্ষতিসাধনকারী (পরীক্ষাস্বরূপ)", "The Distressor", "বিপদে ধৈর্যধারণের শক্তি যোগায়।"));
        names.add(new AllahNameEntity(92, "النَّافِعُ", "আন-নাফি", "উপকারকারী ও কল্যাণদাতা", "The Propitious", "ব্যবসায় ও জীবনে কল্যাণ লাভ হয়।"));
        names.add(new AllahNameEntity(93, "النُّورُ", "আন-নূর", "পরম জ্যোতি ও আলোর উৎস", "The Light", "চেহারায় ও অন্তরে ঈমানের নূর প্রস্ফুটিত হয়।"));
        names.add(new AllahNameEntity(94, "الْهَادِي", "আল-হাদী", "সঠিক পথপ্রদর্শক", "The Guide", "হিদায়েত ও দ্বীনের ওপর অবিচল থাকার জন্য ফলদায়ক।"));
        names.add(new AllahNameEntity(95, "الْبَدِيعُ", "আল-বাদী", "অনুপম ও অপূর্ব সৃষ্টিকর্তা", "The Incomparable", "কঠিন সংকট ও বিষণ্ণতা দূর করতে সাহায্য করে।"));
        names.add(new AllahNameEntity(96, "الْبَاقِي", "আল-বাকী", "চিরস্থায়ী ও অবিনশ্বর", "The Everlasting", "আল্লাহর স্থায়িত্বে বিশ্বাস দৃঢ় হয়।"));
        names.add(new AllahNameEntity(97, "الْوَارِثُ", "আল-ওয়ারিস", "সবকিছুর চূড়ান্ত উত্তরাধিকারী", "The Ultimate Inheritor", "দীর্ঘায়ু ও বরকতময় জীবনের দোয়া।"));
        names.add(new AllahNameEntity(98, "الرَّشِيدُ", "আর-রাশীদ", "সঠিক পথনির্দেশক ও পরম প্রজ্ঞাবান", "The Guide to the Right Path", "ভুল সিদ্ধান্ত থেকে রক্ষা পেতে সহায়ক।"));
        names.add(new AllahNameEntity(99, "الصَّبُورُ", "আস-সাবুর", "পরম ধৈর্যশীল", "The Most Patient", "কষ্ট ও বিপদে পরম ধৈর্য অর্জিত হয়।"));
        db.allahNameDao().insertAll(names);

        // 3. Seed Duas (All 19 Authentic Categories)
        List<DuaEntity> duas = new ArrayList<>();

        // 1. Morning Dua (সকালের দোয়া)
        duas.add(new DuaEntity(
                "Morning Dua", "সকালের আশ্রয় ও তাওহীদের দোয়া",
                "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                "আসবাহনা ওয়া আসবাহাল মুলকু লিল্লাহ, ওয়ালহামদু লিল্লাহ, লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু...",
                "আমরা সকালে উপনীত হলাম এবং সমস্ত রাজত্ব আল্লাহরই রইল। সমস্ত প্রশংসা আল্লাহর জন্য, আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই...",
                "We have reached the morning and the kingdom belongs to Allah; praise is due to Allah...",
                "ہم نے صبح کی اور اللہ کے سارے جہاں نے صبح کی...",
                "ফজরের নামাজের পর থেকে সূর্যোদয়ের পূর্ব পর্যন্ত",
                "সারাদিনের যাবতীয় অনিষ্ট থেকে নিরাপত্তা ও আল্লাহর রহমত লাভ",
                "সহীহ মুসলিম: ২৭২৩, সুনান আবু দাউদ: ৫০৬৮", true
        ));

        // 2. Evening Dua (সন্ধ্যার দোয়া)
        duas.add(new DuaEntity(
                "Evening Dua", "সন্ধ্যার নিরাপত্তা ও আশ্রয়ের দোয়া",
                "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ...",
                "আমসাইনা ওয়া আমসাল মুলকু লিল্লাহ, ওয়ালহামদু লিল্লাহ...",
                "আমরা সন্ধ্যায় উপনীত হলাম এবং রাজত্ব আল্লাহরই রইল। সমস্ত প্রশংসা আল্লাহর...",
                "We have reached the evening and the dominion belongs to Allah...",
                "ہم نے شام کی اور اللہ کے سارے جہاں نے شام کی...",
                "আসর সালাতের পর থেকে মাগরিব/এশার পূর্ব পর্যন্ত",
                "রাত্রিকালীন যাবতীয় বিপদাপদ থেকে সুরক্ষা",
                "সহীহ মুসলিম: ২৭২৩", true
        ));

        // 3. Before Sleeping (ঘুমানোর পূর্বে)
        duas.add(new DuaEntity(
                "Before Sleeping", "ঘুমানোর পূর্বের সুন্নাহ দোয়া",
                "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                "বিসমিকাল্লাহুম্মা আমূতু ওয়া আহ্ইয়া",
                "হে আল্লাহ! আপনার নামেই মৃত্যুবরণ (ঘুমাই) করছি এবং আপনার নামেই জীবিত হবো।",
                "In Your Name, O Allah, I die and I live.",
                "اے اللہ! تیرے نام کے ساتھ میں مرتا ہوں اور جیتا ہوں۔",
                "বিছানায় শোওয়ার পর ডান কাতে শুয়ে",
                "ঘুমকে ইবাদতে রূপান্তর ও সুরক্ষার সুন্নাহ",
                "সহীহ বুখারী: ৬৩২৪, সহীহ মুসলিম: ২৭১১", true
        ));

        // 4. After Waking (ঘুম থেকে ওঠার পর)
        duas.add(new DuaEntity(
                "After Waking", "ঘুম থেকে জাগ্রত হওয়ার দোয়া",
                "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                "আলহামদু লিল্লাহিল্লাজি আহ্ইয়ানা বা'দা মা আমাতানা ওয়া ইলাইহিন নুশূর",
                "সমস্ত প্রশংসা আল্লাহর যিনি আমাদের মৃত্যুর পর পুনর্জীবিত করলেন এবং তাঁর কাছেই সবার প্রত্যাবর্তন।",
                "All praise is for Allah who gave us life after having taken it from us and unto Him is the resurrection.",
                "تمام تعریفیں اللہ کے لیے ہیں جس نے ہمیں مارنے کے بعد زندہ کیا...",
                "ঘুম থেকে চোখ মেলার সাথে সাথে",
                "নতুন দিনের জীবনের জন্য রবের কৃতজ্ঞতা প্রকাশ",
                "সহীহ বুখারী: ৬৩১২, সহীহ মুসলিম: ২৭১১", true
        ));

        // 5. Before Eating (খাবার পূর্বে)
        duas.add(new DuaEntity(
                "Before Eating", "খাবার শুরুর মাসনুন দোয়া",
                "بِسْمِ اللَّهِ (ভুলে গেলে: بِسْمِ اللَّهِ أَوَّلَهُ وَآخِرَهُ)",
                "বিসমিল্লাহ (ভুলে গেলে: বিসমিল্লাহি আউওয়ালাহু ওয়া আখিরাহু)",
                "আল্লাহর নামে শুরু করছি। (শুরুতে ভুলে গেলে: শুরুতে ও শেষে আল্লাহর নামে)।",
                "In the Name of Allah. (If forgotten: In the Name of Allah at its beginning and end).",
                "اللہ کے نام کے ساتھ۔",
                "খাবারে হাত দেওয়ার পূর্বে",
                "খাবারে বরকত ও শয়তানের অংশগ্রহণ প্রতিহত করতে",
                "সুনান আবু দাউদ: ৩৭৬৭, জামে তিরমিযী: ১৮৫৮", true
        ));

        // 6. After Eating (খাবার শেষে)
        duas.add(new DuaEntity(
                "After Eating", "খাবার সমাপ্তির শুকরিয়া দোয়া",
                "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ",
                "আলহামদু লিল্লাহিল্লাজি আত'আমানা ওয়া সাকানা ওয়া জা'আলানা মুসলিমিন",
                "সমস্ত প্রশংসা আল্লাহর যিনি আমাদের আহার করিয়েছেন, পান করিয়েছেন এবং মুসলিম বানিয়েছেন।",
                "Praise be to Allah Who has fed us and given us drink and made us Muslims.",
                "تمام تعریفیں اللہ کے لیے ہیں جس نے ہمیں کھلایا اور پلایا اور مسلمان بنایا۔",
                "খাবার আহার সমাপ্ত করার পর",
                "রিজিকের অশেষ নিয়ামতের কৃতজ্ঞতা আদায়",
                "সুনান আবু দাউদ: ৩৮৫০, জামে তিরমিযী: ৩৪৫৭", false
        ));

        // 7. Travel (সফর ও ভ্রমণ)
        duas.add(new DuaEntity(
                "Travel", "বাহনে আরোহণ ও সফরের দোয়া",
                "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنْقَلِبُونَ",
                "সুবহানাল্লাজি সাখখারা লানা হাজা ওয়া মা কুন্না লাহু মুকরিনিন, ওয়া ইন্না ইলা রব্বিনা লামুনকালিবুন",
                "পবিত্র সেই সত্তা যিনি এটিকে আমাদের অধীন করে দিয়েছেন, অথচ আমরা এটিকে বশীভূত করতে সক্ষম ছিলাম না। নিশ্চয়ই আমরা আমাদের রবের কাছে ফিরে যাব।",
                "Glory to Him who has brought this into subjection for us though we were unable to subdue it ourselves...",
                "پاک ہے وہ ذات جس نے اس کو ہمارے بس میں کر دیا...",
                "গাড়ি, বিমান বা যেকোনো বাহনে আরোহণের পর",
                "নিরাপদ ভ্রমণ ও পথচলার সুরক্ষা লাভ",
                "সূরা আল-জু Ruf: ১৩-১৪, সহীহ মুসলিম: ১৩৪২", true
        ));

        // 8. Protection (বিপদ ও হেফাজত)
        duas.add(new DuaEntity(
                "Protection", "সর্ববিধ ক্ষতি থেকে সুরক্ষার দোয়া",
                "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
                "বিসমিল্লাহিল্লাজি লা ইয়াদুররু মা'আসমিহি শাইউন ফিল আরদি ওয়া লা ফিস সামা-ই, ওয়া হুওয়াস সামিউল আলিম",
                "আল্লাহর নামে শুরু করছি, যাঁর নামের বরকতে আসমান ও জমিনের কোনো বস্তুই কোনো ক্ষতি করতে পারে না। তিনি সর্বশ্রোতা, সর্বজ্ঞ।",
                "In the Name of Allah with Whose Name nothing can cause harm on the earth or in the heaven...",
                "اللہ کے نام کے ساتھ جس کے نام کی برکت سے زمین اور آسمان میں کوئی چیز نقصان نہیں پہنچا سکتی...",
                "প্রতিদিন সকাল ও সন্ধ্যায় ৩ বার",
                "বিষাক্ত প্রাণী, আকস্মিক বিপদ, জাদু ও অশুভ প্রভাব থেকে হেফাজত",
                "সুনান আবু দাউদ: ৫০৮৮, জামে তিরমিযী: ৩৩৮৮", true
        ));

        // 9. Anxiety/Worry (দুশ্চিন্তা ও মানসিক চাপ)
        duas.add(new DuaEntity(
                "Anxiety/Worry", "কঠিন পেরেশানি ও ঋণ থেকে মুক্তির দোয়া",
                "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ وَغَلَبَةِ الرِّجَالِ",
                "আল্লাহুম্মা ইন্নি আউযুবিকা মিনাল হামমি ওয়াল হাযান, ওয়াল আজযি ওয়াল কাসাল, ওয়াল বুখলি ওয়াল জুবন, ওয়া দালাইদ দাইনি ওয়া গালাবাতির রিজাল",
                "হে আল্লাহ! নিশ্চয়ই আমি আপনার কাছে আশ্রয় চাই দুশ্চিন্তা ও দুঃখ-বেদনা থেকে, অক্ষমতা ও অলসতা থেকে, কৃপণতা ও কাপুরুষতা থেকে, ঋণের বোঝা ও মানুষের প্রাধান্য থেকে।",
                "O Allah, I seek refuge in You from grief and sadness, from weakness and laziness, from miserliness and cowardice, from debt and oppression.",
                "اے اللہ! میں تیری پناہ مانگتا ہوں فکر اور غم سے، عاجزی اور سستی سے...",
                "যেকোনো দুশ্চিন্তা, বিষণ্ণতা বা ঋণগ্রস্ত অবস্থায়",
                "মানসিক প্রশান্তি ও অচিন্তনীয় সাহায্য লাভ",
                "সহীহ বুখারী: ২৮৯৩", true
        ));

        // 10. Forgiveness (ক্ষমা ও তওবা)
        duas.add(new DuaEntity(
                "Forgiveness", "সাইয়্যিদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমা প্রার্থনা)",
                "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
                "আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা, খালাকতানী ওয়া আনা আবদুকা...",
                "হে আল্লাহ! আপনিই আমার প্রতিপালক, আপনি ছাড়া কোনো সত্য উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা...",
                "O Allah, You are my Lord, there is no true god but You. You created me and I am Your slave...",
                "اے اللہ! تو میرا رب ہے، تیرے سوا کوئی معبود نہیں...",
                "প্রতিদিন সকালে ও সন্ধ্যায় ১ বার",
                "যে ব্যক্তি দৃঢ় বিশ্বাসের সাথে পড়বে এবং মৃত্যুবরণ করবে, সে জান্নাতি হবে",
                "সহীহ বুখারী: ৬৩০৬", true
        ));

        // 11. Parents (পিতামাতা)
        duas.add(new DuaEntity(
                "Parents", "পিতামাতার মাগফিরাত ও করুণার দোয়া",
                "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                "রব্বির হামহুমা কামা রব্বায়ানী সাগীরা",
                "হে আমার প্রতিপালক! তাদের দুজনের (পিতা-মাতার) প্রতি দয়া করুন, যেভাবে তারা শৈশবে আমাকে স্নেহ-মমতায় লালন-পালন করেছেন।",
                "My Lord, have mercy upon them as they brought me up when I was small.",
                "اے میرے رب! ان دونوں پر رحم فرما جس طرح انہوں نے مجھے بچپن میں پالا۔",
                "প্রত্যেক নামাজের পর বা যেকোনো দোয়ায়",
                "পিতামাতার হক আদায় ও সন্তানের জন্য উত্তম সাদাকায়ে জারিয়া",
                "সূরা আল-ইসরা: ২৪", true
        ));

        // 12. Rizq (রিজিক ও বরকত)
        duas.add(new DuaEntity(
                "Rizq", "হালাল ও প্রশস্ত রিজিকের দোয়া",
                "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلًا مُتَقَبَّلًا",
                "আল্লাহুম্মা ইন্নি আসআলুকা ইলমান নাফিআও ওয়া রিজকান তায়্যিবাও ওয়া আমালান মুতাকাব্বালা",
                "হে আল্লাহ! নিশ্চয়ই আমি আপনার কাছে কল্যাণকর জ্ঞান, পবিত্র ও হালাল রিজিক এবং কবুলযোগ্য আমল প্রার্থনা করছি।",
                "O Allah, I ask You for beneficial knowledge, good (halal) provision, and acceptable deeds.",
                "اے اللہ! میں تجھ سے نفع بخش علم، پاکیزہ رزق اور قبول ہونے والے عمل کا سوال کرتا ہوں۔",
                "ফজরের নামাজের সালাম ফেরানোর পর",
                "সারাদিনের উপার্জন ও আমলে বরকত লাভ",
                "সুনান ইবনে মাজাহ: ৯২৫", true
        ));

        // 13. Health (রোগমুক্তি ও সুস্থতা)
        duas.add(new DuaEntity(
                "Health", "রোগমুক্তি ও শারীরিক সুস্থতার দোয়া",
                "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لَا شِفَاءَ إِلَّا شِفَاؤُكَ، شِفَاءً لَا يُغَادِرُ سَقَمًا",
                "আল্লাহুম্মা রব্বান নাসি আজহিবিল বা'স, ইশফিহি ওয়া আনতাশ শাফী, লা শিফা-আ ইল্লা শিফাউকা, শিফা-আল লা ইউগাদিরু সাক্বামা",
                "হে মানুষের প্রতিপালক আল্লাহ! কষ্ট দূর করে দিন এবং আরোগ্য দান করুন। আপনিই প্রকৃত আরোগ্যকারী। আপনার আরোগ্য ছাড়া কোনো আরোগ্য নেই...",
                "O Allah, Lord of mankind, remove the disease and heal him; You are the Healer, there is no healing except Your healing...",
                "اے اللہ! لوگوں کے پروردگار! تکلیف کو دور فرما دے اور شفا عطا فرما...",
                "অসুস্থ ব্যক্তি নিজে বা রোগীর শরীরে হাত রেখে",
                "দ্রুত রোগমুক্তি ও সুন্নাহ তরিকায় আরোগ্য লাভ",
                "সহীহ বুখারী: ৫৬৭৫, সহীহ মুসলিম: ২১৯১", true
        ));

        // 14. Guidance (হিদায়েত ও দ্বীনের ওপর অবিচলতা)
        duas.add(new DuaEntity(
                "Guidance", "দ্বীনের ওপর হৃদয় অটল রাখার দোয়া",
                "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَىٰ دِينِكَ",
                "ইয়া মুকাল্লিবাল কুলূবি ছাব্বিত কাল্বী আলা দীনিক",
                "হে অন্তরসমূহের পরিবর্তনকারী! আমার অন্তরকে আপনার দ্বীনের ওপর অটল ও দৃঢ় রাখুন।",
                "O Turner of the hearts, keep my heart firm upon Your religion.",
                "اے دلوں کے پھیرنے والے! میرے دل کو اپنے دین پر ثابت قدم رکھ۔",
                "সেজদায়, সালাতের শেষ বৈঠকে এবং যেকোনো দোয়ায়",
                "রাসূলুল্লাহ (সা.) এই দোয়া সর্বাধিক বেশি পড়তেন",
                "জামে তিরমিযী: ২১৪০", true
        ));

        // 15. Ramadan (রমজান ও রোজা)
        duas.add(new DuaEntity(
                "Ramadan", "ইফতারের সময় তৃষ্ণা নিবারণ ও সওয়াবের দোয়া",
                "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ، وَثَبَتَ الْأَجْرُ إِنْ شَاءَ اللَّهُ",
                "জাহাবাজ জামাউ ওয়াবতাল্লাতিল উরূকু, ওয়া ছাবাতাল আজরু ইনশাআল্লাহ",
                "পিপাসা দূর হলো, শিরা-উপশিরা সিক্ত হলো এবং ইনশাআল্লাহ প্রতিদান নিশ্চিত হলো।",
                "The thirst has gone, the arteries are moist, and the reward is confirmed, if Allah wills.",
                "پیاس بجھ گئی اور رگیں تر ہو گئیں اور اجر ثابت ہو گیا اگر اللہ نے چاہا۔",
                "ইফতার মুখে দেওয়ার সাথে সাথে",
                "রোজাদারের ইফতার মুহূর্তের নিশ্চিত দোয়া কবুল হওয়া",
                "সুনান আবু দাউদ: ২৩৫৭", true
        ));

        // 16. Hajj (হজ)
        duas.add(new DuaEntity(
                "Hajj", "আরাফাত দিবসের শ্রেষ্ঠ দোয়া",
                "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                "লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারীকা লাহু, লাহুল মুলকু ওয়া লাহুল হামদু ওয়া হুওয়া আলা কুল্লি শাইয়িন ক্বদীর",
                "আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। রাজত্ব একমাত্র তাঁরই এবং সমস্ত প্রশংসাও তাঁরই...",
                "There is no deity except Allah alone without partner, to Him belongs dominion and to Him belongs praise...",
                "اللہ کے سوا کوئی معبود نہیں وہ اکیلا ہے اس کا کوئی شریک نہیں...",
                "হজের আরাফাত ময়দানে এবং জিলহজের দিনগুলোতে",
                "রাসূলুল্লাহ (সা.) বলেছেন: আরাফাত দিবসের দোয়াই সর্বশ্রেষ্ঠ দোয়া",
                "জামে তিরমিযী: ৩৫৮৫", true
        ));

        // 17. Umrah (উমরাহ)
        duas.add(new DuaEntity(
                "Umrah", "তাওয়াফে রুকনে ইয়ামানী ও হাজরে আসওয়াদের মধ্যবর্তী দোয়া",
                "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                "রব্বানা আতিনা ফিদ দুনয়া হাসানাতাও ওয়া ফিল আখিরাতি হাসানাতাও ওয়া কিনা আজাবান নার",
                "হে আমাদের প্রতিপালক! আমাদের দুনিয়াতে কল্যাণ দিন, আখিরাতেও কল্যাণ দিন এবং জাহান্নামের আজাব থেকে রক্ষা করুন।",
                "Our Lord, give us in this world that which is good and in the Hereafter that which is good and protect us from the Fire.",
                "اے ہمارے رب! ہمیں دنیا میں بھی بھلائی دے اور آخرت میں بھی بھلائی دے...",
                "কাবা শরীফের প্রতিটি তাওয়াফের চক্করে রুকনে ইয়ামানী থেকে হাজরে আসওয়াদ পর্যন্ত",
                "উভয় জাহানের পূর্ণাঙ্গ মুক্তি ও শান্তির দোয়া",
                "সুনান আবু দাউদ: ১৮৯২, সহীহ বুখারী: ৪৫২২", true
        ));

        // 18. Salah-related Duas (সালাতের দোয়া)
        duas.add(new DuaEntity(
                "Salah-related Duas", "সালাতের সেজদায় পাঠের দোয়া",
                "اللَّهُمَّ اغْفِرْ لِي ذَنْبِي كُلَّهُ: دِقَّهُ وَجِلَّهُ، وَأَوَّلَهُ وَآخِرَهُ، وَعَلَانِيَتَهُ وَسِرَّهُ",
                "আল্লাহুম্মাগফির লী যাম্বী কুল্লাহু: দিক্কাহু ওয়া জিল্লাহু, ওয়া আউওয়ালাহু ওয়া আখিরাহু, ওয়া আলানিয়্যাতাহু ওয়া সিররাহু",
                "হে আল্লাহ! আপনি আমার সকল গুনাহ ক্ষমা করে দিন—ছোট ও বড়, পূর্বের ও পরের, প্রকাশ্য ও অপ্রকাশ্য।",
                "O Allah, forgive all my sins, small and great, first and last, open and secret.",
                "اے اللہ! میرے تمام گناہ معاف فرما دے، چھوٹے اور بڑے...",
                "ফরজ বা নফল নামাজের সেজদারত অবস্থায়",
                "সেজদায় বান্দা আল্লাহর সবচেয়ে বেশি নিকটবর্তী হয়",
                "সহীহ মুসলিম: ৪৮৩", true
        ));

        // 19. Daily Life (দৈনন্দিন জীবন)
        duas.add(new DuaEntity(
                "Daily Life", "ঘর থেকে বের হওয়ার সময় তাওয়াক্কুলের দোয়া",
                "بِسْمِ اللَّهِ، تَوَكَّلْتُ عَلَى اللَّهِ، لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
                "বিসমিল্লাহি তাওয়াক্কালতু আলাল্লাহ, লা হাওলা ওয়া লা কুওয়াতা ইল্লা বিল্লাহ",
                "আল্লাহর নামে বের হচ্ছি, আল্লাহর ওপরই ভরসা করলাম। আল্লাহর সাহায্য ছাড়া কোনো অনিষ্ট থেকে বাঁচার বা পুণ্য করার শক্তি নেই।",
                "In the Name of Allah, I place my trust in Allah; there is no power and no strength except with Allah.",
                "اللہ کے نام کے ساتھ، میں نے اللہ پر بھروسہ کیا...",
                "বাড়ি, অফিস বা যেকোনো স্থান থেকে বাইরে পা দেওয়ার সময়",
                "ফেরেশতারা সুরক্ষা প্রদান করেন এবং শয়তান দূরে সরে যায়",
                "সুনান আবু দাউদ: ৫০৯৫, জামে তিরমিযী: ৩৪২৬", true
        ));

        db.duaDao().insertAll(duas);

        // 4. Seed Quiz Questions (All 11 Authentic Categories with Explanations & References)
        List<QuizQuestionEntity> questions = new ArrayList<>();

        // Quran
        questions.add(new QuizQuestionEntity(
                "পবিত্র কুরআনের সর্ববৃহৎ সূরা কোনটি?",
                "সূরা আল-ইমরান", "সূরা আল-বাকারা", "সূরা আন-নিসা", "সূরা আল-মায়েদা", 1,
                "সূরা আল-বাকারায় মোট ২৮৬টি আয়াত রয়েছে যা কুরআনের দীর্ঘতম সূরা।", "সূরা আল-বাকারা: ২৮৬", "Quran", 1
        ));
        questions.add(new QuizQuestionEntity(
                "কুরআনের কোন সূরায় 'বিসমিল্লাহ' দুইবার উল্লেখ আছে?",
                "সূরা আন-নামল", "সূরা আত-তাওবা", "সূরা আল-কাহফ", "সূরা ইয়াসিন", 0,
                "সূরা আন-নামলের শুরুতে এবং ৩০ নং আয়াতে সুলাইমান (আ.)-এর চিঠির বিবরণে বিসমিল্লাহ এসেছে।", "সূরা আন-নামল: ৩০", "Quran", 2
        ));
        questions.add(new QuizQuestionEntity(
                "কুরআনের সবচেয়ে ছোট সূরা কোনটি?",
                "সূরা আল-ইখলাস", "সূরা আল-কাওসার", "সূরা আল-ফালাক", "সূরা আন-নাস", 1,
                "সূরা আল-কাওসার কুরআনের সবচেয়ে ছোট সূরা, এতে মাত্র ৩টি আয়াত রয়েছে।", "সূরা আল-কাওসার: ১-৩", "Quran", 1
        ));

        // Hadith
        questions.add(new QuizQuestionEntity(
                "হাদিস শাস্ত্রের সবচেয়ে বিশুদ্ধতম দুটি গ্রন্থের যৌথ উপাধি কী?",
                "সুনানাইন", "সহীহাই", "মুসান্নাফাইন", "মুয়াত্তাইন", 1,
                "সহীহ আল-বুখারী ও সহীহ মুসলিমকে যৌথভাবে 'সহীহাই' বা 'সহীহাইন' বলা হয়।", "মুকাদ্দিমা ইবনুস সালাহ", "Hadith", 1
        ));
        questions.add(new QuizQuestionEntity(
                "'সকল আমল নিয়তের ওপর নির্ভরশীল'—হাদিসটি কোন সাহাবী বর্ণনা করেছেন?",
                "হযরত আবু হুরায়রা (রা.)", "হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত আলী (রা.)", "হযরত আব্দুল্লাহ ইবনে উমর (রা.)", 1,
                "হযরত উমর (রা.) থেকে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: নিশ্চয়ই সকল আমলের ফলাফল নিয়তের ওপর নির্ভরশীল।", "সহীহ বুখারী: ১", "Hadith", 2
        ));

        // Salah
        questions.add(new QuizQuestionEntity(
                "প্রতিদিন ৫ ওয়াক্তে মোট কত রাকাত সালাত ফরজ?",
                "১৫ রাকাত", "১৭ রাকাত", "২০ রাকাত", "১২ রাকাত", 1,
                "ফজর ২, জোহর ৪, আসর ৪, মাগরিব ৩ এবং এশা ৪—মোট ১৭ রাকাত ফরজ।", "ফিকহুস সুন্নাহ", "Salah", 1
        ));
        questions.add(new QuizQuestionEntity(
                "সূর্যোদয়ের সময় নফল সালাত আদায়ের হুকুম কী?",
                "মুস্তাহাব", "মাকরূহে তাহরিমি / নিষিদ্ধ", "জায়েজ", "সুন্নাত", 1,
                "সূর্য উদিত হওয়ার সময় সূর্য পুরোপুরি উপরে না ওঠা পর্যন্ত সালাত আদায় করা নিষেধ।", "সহীহ মুসলিম: ৮৩১", "Salah", 2
        ));

        // Prophets
        questions.add(new QuizQuestionEntity(
                "কোন নবীকে 'খলিলুল্লাহ' (আল্লাহর বন্ধু) উপাধি দেওয়া হয়েছিল?",
                "মুসা (আ.)", "ইব্রাহিম (আ.)", "নূহ (আ.)", "ঈসা (আ.)", 1,
                "আল্লাহ তাআলা সূরা নিসার ১২৫ নং আয়াতে ইব্রাহিম (আ.) কে বন্ধু হিসেবে গ্রহণ করার কথা বলেছেন।", "সূরা আন-নিসা: ১২৫", "Prophets", 1
        ));
        questions.add(new QuizQuestionEntity(
                "মাছের পেটে কোন নবী আল্লাহর তাসবীহ পাঠ করেছিলেন?",
                "হযরত ইউনুস (আ.)", "হযরত ইয়াহইয়া (আ.)", "হযরত ইলিয়াস (আ.)", "হযরত জাকারিয়া (আ.)", 0,
                "হযরত ইউনুস (আ.) মাছের পেটে 'লা ইলাহা ইল্লা আনতা সুবহানাকা...' পাঠ করেছিলেন।", "সূরা আল-আম্বিয়া: ৮৭", "Prophets", 1
        ));

        // Seerah
        questions.add(new QuizQuestionEntity(
                "রাসূলুল্লাহ (সা.) কত বছর বয়সে নবুওয়াত লাভ করেন?",
                "২৫ বছর", "৪০ বছর", "৬৩ বছর", "৫০ বছর", 1,
                "হেরা গুহায় ধ্যানমগ্ন থাকা অবস্থায় ৪০ বছর বয়সে জিবরীল (আ.)-এর মাধ্যমে প্রথম ওহী নাযিল হয়।", "আর-রাহীকুল মাখতূম", "Seerah", 1
        ));
        questions.add(new QuizQuestionEntity(
                "ঐতিহাসিক বদর যুদ্ধ কত হিজরিতে সংঘটিত হয়েছিল?",
                "১ম হিজরি", "২য় হিজরি", "৩য় হিজরি", "৫ম হিজরি", 1,
                "২য় হিজরির ১৭ই রমজান মুসলমানদের সাথে কুরাইশদের প্রথম ঐতিহাসিক বদর যুদ্ধ সংঘটিত হয়।", "সহীহ বুখারী: ৩৯৫১", "Seerah", 2
        ));

        // Islamic History
        questions.add(new QuizQuestionEntity(
                "ইসলামের প্রথম খলিফা কে ছিলেন?",
                "হযরত উমর (রা.)", "হযরত আবু বকর সিদ্দিক (রা.)", "হযরত উসমান (রা.)", "হযরত আলী (রা.)", 1,
                "রাসূলুল্লাহ (সা.)-এর ওফাতের পর সাহাবায়ে কেরামের সর্বসম্মত সিদ্ধান্তে হযরত আবু বকর (রা.) প্রথম খলিফা নির্বাচিত হন।", "তারিখে তাবারী", "Islamic History", 1
        ));
        questions.add(new QuizQuestionEntity(
                "কার খিলাফতকালে সর্বপ্রথম হিজরি সন গণনা প্রবর্তন করা হয়?",
                "হযরত আবু বকর (রা.)", "হযরত উমর (রা.)", "হযরত উসমান (রা.)", "হযরত মুআবিয়া (রা.)", 1,
                "হযরত উমর (রা.)-এর শাসনামলে ১৬ বা ১৭ হিজরিতে হিজরতের বছরকে ভিত্তি ধরে হিজরি বর্ষ গণনা শুরু হয়।", "আল-বিদায়া ওয়ান নিহায়া", "Islamic History", 2
        ));

        // Ramadan
        questions.add(new QuizQuestionEntity(
                "পবিত্র কুরআনে কোন সূরায় লাইলাতুল কদরের মাহাত্ম্য বর্ণনা করা হয়েছে?",
                "সূরা আল-ফাতিহা", "সূরা আল-ক্বদর", "সূরা আল-দুহা", "সূরা আল-ইনশিরাহ", 1,
                "সূরা আল-ক্বদরে বলা হয়েছে কদরের রাত হাজার মাসের চেয়েও শ্রেষ্ঠ।", "সূরা আল-ক্বদর: ১-৫", "Ramadan", 1
        ));
        questions.add(new QuizQuestionEntity(
                "সাদকাতুল ফিতর আদায়ের সর্বোত্তম সময় কোনটি?",
                "রমজানের ১ম দিনে", "ঈদের সালাতে যাওয়ার পূর্বে", "ঈদের সালাতের পরদিন", "শাবান মাসে", 1,
                "রাসূলুল্লাহ (সা.) ঈদের সালাতে বের হওয়ার পূর্বেই ফিতরা আদায়ের নির্দেশ দিয়েছেন।", "সহীহ বুখারী: ১৫০৯", "Ramadan", 2
        ));

        // Hajj
        questions.add(new QuizQuestionEntity(
                "হজের প্রধান ও সবচেয়ে গুরুত্বপূর্ণ রুকন কোনটি?",
                "তাওয়াফ করা", "৯ই জিলহজ আরাফাতের ময়দানে অবস্থান", "সাঈ করা", "কঙ্কর মারা", 1,
                "রাসূলুল্লাহ (সা.) বলেছেন: 'আল-হাজ্জু আরাফাহ' অর্থাৎ আরাফাতে অবস্থানই হলো হজ।", "জামে তিরমিযী: ৮৮৯", "Hajj", 1
        ));
        questions.add(new QuizQuestionEntity(
                "হজের সময় শয়তানকে কঙ্কর নিক্ষেপের স্থানটিকে কী বলা হয়?",
                "সাফা", "জামারাত (মিনা)", "মুজদালিফা", "মাকামে ইব্রাহিম", 1,
                "মিনায় তিনটি জামারায় (ছোট, মধ্যম ও বড়) পাথর নিক্ষেপ করা ওয়াজিব।", "সহীহ মুসলিম: ১২৯৭", "Hajj", 1
        ));

        // Umrah
        questions.add(new QuizQuestionEntity(
                "উমরাহর রুকন মোট কয়টি?",
                "২টি", "৩টি", "৫টি", "৭টি", 1,
                "উমরাহর প্রধান রুকন ৩টি: ইহরাম বাঁধা, কাবা তাওয়াফ করা এবং সাফা-মারওয়া সাঈ করা।", "আল-ফিকহুল ইসলামী", "Umrah", 2
        ));
        questions.add(new QuizQuestionEntity(
                "সাফা ও মারওয়া পাহাড়ের মধ্যবর্তী স্থানে দ্রুত পদচারণা করাকে কী বলা হয়?",
                "তাওয়াফ", "সাঈ", "ইজতিবা", "রমল", 1,
                "সাফা ও মারওয়ার মাঝে সাত চক্কর সম্পন্ন করাকে সাঈ বলা হয়।", "সহীহ বুখারী: ১৬৪৩", "Umrah", 1
        ));

        // Fiqh
        questions.add(new QuizQuestionEntity(
                "অজুর ফরজ কাজ কয়টি?",
                "৩টি", "৪টি", "৫টি", "৬টি", 1,
                "অজুর ফরজ ৪টি: মুখ ধোয়া, উভয় হাত কনুইসহ ধোয়া, মাথা মাসেহ করা এবং পা টাখনুসহ ধোয়া।", "সূরা আল-মায়িদা: ৬", "Fiqh", 1
        ));
        questions.add(new QuizQuestionEntity(
                "শরিয়তসম্মত কারণে পানি না পাওয়া গেলে পবিত্রতা অর্জনের পদ্ধতি কোনটি?",
                "গোসল ত্যাগ", "তায়াম্মুম", "শুধু কাপড় বদল", "নফল সালাত", 1,
                "পবিত্র মাটি দিয়ে চেহারা ও হাত মাসেহ করে তায়াম্মুমের মাধ্যমে পবিত্রতা অর্জন করা যায়।", "সূরা আন-নিসা: ৪৩", "Fiqh", 1
        ));

        // General Islamic Knowledge
        questions.add(new QuizQuestionEntity(
                "ইসলামের স্তম্ভ কয়টি?",
                "৩টি", "৪টি", "৫টি", "৬টি", 2,
                "ইসলামের স্তম্ভ ৫টি: কালেমা, সালাত, যাকাত, সাওম ও হজ।", "সহীহ বুখারী: ৮", "General Islamic Knowledge", 1
        ));
        questions.add(new QuizQuestionEntity(
                "আল্লাহ তায়ালার গুণবাচক সুন্দর নাম (আসমাউল হুসনা) কয়টি?",
                "৬৩টি", "৯৯টি", "১১৪টি", "৩৬০টি", 1,
                "রাসূলুল্লাহ (সা.) বলেছেন: নিশ্চয়ই আল্লাহর ৯৯টি নাম রয়েছে, যে ব্যক্তি এগুলো মুখস্থ/হিফজ করবে সে জান্নাতে প্রবেশ করবে।", "সহীহ বুখারী: ২৭৩৬", "General Islamic Knowledge", 1
        ));

        db.quizDao().insertAll(questions);

        // 5. Seed Halal Ingredients
        List<HalalFoodEntity> ingredients = new ArrayList<>();
        ingredients.add(new HalalFoodEntity("E100", "Curcumin (হলুদ রঙ)", "কালারিং", "HALAL", "প্রাকৃতিক হলুদ গাছ থেকে প্রাপ্ত সম্পূর্ণ হালাল খাদ্য উপাদান।", "উদ্ভিদজ"));
        ingredients.add(new HalalFoodEntity("E120", "Carmine / Cochineal", "কালারিং", "HARAM", "কীটপতঙ্গ থেকে সংগৃহীত রক্তবর্ণ রঞ্জক, যা অধিকাংশ ফিকহ অনুযায়ী হারাম।", "কীটপতঙ্গ"));
        ingredients.add(new HalalFoodEntity("E441", "Gelatin (জিলেটিন)", "জেলিং এজেন্ট", "MUSHBOOH", "অ-জবাইকৃত বা শূকরের উৎস হলে হারাম, ১০০% হালাল জবাইকৃত গরু বা মাছের হলে হালাল।", "প্রাণিজ"));
        ingredients.add(new HalalFoodEntity("E471", "Mono- and Diglycerides", "ইমালসিফায়ার", "MUSHBOOH", "উদ্ভিজ্জ তেলের উৎস থেকে হলে হালাল, অন্যথায় প্রাণিজ চর্বি হতে পারে।", "উদ্ভিদ/প্রাণিজ"));
        ingredients.add(new HalalFoodEntity("E322", "Lecithin (লেসিথিন)", "ইমালসিফায়ার", "HALAL", "সয়াবিন বা সূর্যমুখী বীজ থেকে প্রাপ্ত হালাল উপাদান।", "উদ্ভিদজ"));
        db.halalDao().insertAll(ingredients);

        // 6. Seed 114 Surahs & Essential Verified Ayahs
        db.surahDao().insertSurahs(QuranSurahDataSeeder.get114Surahs());
        db.ayahDao().insertAyahs(QuranSurahDataSeeder.getEssentialAyahs());

        // 9. Seed Default Tasbih Dhikr
        seedDefaultTasbih(db);

        // 10. Seed Default Azkar
        seedDefaultAzkar(db);

        // 11. Seed Verified Authentic Hadiths
        seedDefaultHadiths(db);
    }

    private static void seedDefaultTasbih(AppDatabase db) {
        List<TasbihEntity> list = new ArrayList<>();
        list.add(new TasbihEntity(
                "سُبْحَانَ اللَّهِ", "SubhanAllah",
                "সুবহানাল্লাহ", "SubhanAllah",
                0, 33, 0, 0, true, false, 1));
        list.add(new TasbihEntity(
                "الْحَمْدُ لِلَّهِ", "Alhamdulillah",
                "আলহামদুলিল্লাহ", "Alhamdulillah",
                0, 33, 0, 0, true, false, 2));
        list.add(new TasbihEntity(
                "اللَّهُ أَكْبَرُ", "Allahu Akbar",
                "আল্লাহু আকবার", "Allah is Greatest",
                0, 33, 0, 0, true, false, 3));
        list.add(new TasbihEntity(
                "أَسْتَغْفِرُ اللَّهَ", "Astaghfirullah",
                "আস্তাগফিরুল্লাহ", "I seek forgiveness from Allah",
                0, 100, 0, 0, true, false, 4));
        list.add(new TasbihEntity(
                "لَا إِلٰهَ إِلَّا اللَّهُ", "La ilaha illallah",
                "লা ইলাহা ইল্লাল্লাহ", "There is no god but Allah",
                0, 100, 0, 0, true, false, 5));
        list.add(new TasbihEntity(
                "صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ", "Sallallahu Alayhi Wasallam",
                "দরুদ শরিফ", "Blessings upon the Prophet",
                0, 100, 0, 0, true, false, 6));
        db.tasbihDao().insertAll(list);
    }

    private static void seedDefaultAzkar(AppDatabase db) {
        List<AzkarEntity> list = new ArrayList<>();
        // Morning Azkar (Hisnul Muslim verified)
        list.add(new AzkarEntity(
                "morning",
                "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                "Asbahna wa asbahal mulku lillah, walhamdu lillah, la ilaha illallahu wahdahu la sharika lah, lahul mulku walahul hamdu wahuwa 'ala kulli shay'in qadir",
                "আমরা সকালে উঠলাম এবং সকল রাজত্ব আল্লাহর। সমস্ত প্রশংসা আল্লাহর। তিনি ছাড়া কোনো উপাস্য নেই, তিনি একক, তাঁর কোনো অংশীদার নেই। সার্বভৌম রাজত্ব ও সকল প্রশংসা তাঁরই এবং তিনি সবকিছুর ওপর শক্তিমান।",
                "We have entered the morning and at this very time unto Allah belongs all sovereignty...",
                "আবু দাউদ ৫০৭৮",
                "সকালে পাঠ করলে সারাদিনের জন্য যথেষ্ট হয়।",
                1, 0, null, 1));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "morning",
                "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ",
                "Allahumma bika asbahna, wa bika amsayna, wa bika nahya, wa bika namutu, wa ilaykan-nushur",
                "হে আল্লাহ! তোমার অনুগ্রহেই আমরা সকালে উঠলাম, তোমার অনুগ্রহেই সন্ধ্যায় পৌঁছলাম, তোমার ইচ্ছাতেই বাঁচি ও মরি এবং তোমার কাছেই প্রত্যাবর্তন।",
                "O Allah, by You we have entered the morning and by You we have entered the evening...",
                "আবু দাউদ ৫০৬৮, তিরমিযি ৩৩৯১",
                "সকাল ও সন্ধ্যার আমল।",
                1, 0, null, 2));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "morning",
                "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ: اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ...",
                "A'udhu billahi minash-shaytanir-rajim: Allahu la ilaha illa huwal hayyul qayyum... (Ayatul Kursi)",
                "আয়াতুল কুরসি — মন্দ শয়তান থেকে আল্লাহর কাছে আশ্রয় চাই। আল্লাহ — তিনি ব্যতীত কোনো উপাস্য নেই...",
                "Ayat Al-Kursi — Allah — there is no deity except Him, the Ever-Living, the Sustainer...",
                "সূরা আল-বাকারা ২:২৫৫ | আবু দাউদ ১৫০৩",
                "যে ব্যক্তি সকাল-সন্ধ্যায় আয়াতুল কুরসি পাঠ করবে সে জান্নাতে প্রবেশ করবে।",
                1, 0, null, 3));

        // Evening Azkar
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "evening",
                "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
                "Amsayna wa amsal-mulku lillah, walhamdu lillah, la ilaha illallahu wahdahu la sharika lah",
                "আমরা সন্ধ্যায় পৌঁছলাম এবং সকল রাজত্ব আল্লাহর। সমস্ত প্রশংসা আল্লাহর। তিনি ছাড়া কোনো উপাস্য নেই, তিনি একক।",
                "We have entered the evening and at this very time unto Allah belongs all sovereignty...",
                "আবু দাউদ ৫০৭৮",
                "সন্ধ্যার নির্দিষ্ট আমল।",
                1, 0, null, 1));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "evening",
                "اللَّهُمَّ بِكَ أَمْسَيْنَا وَبِكَ أَصْبَحْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوتُ وَإِلَيْكَ الْمَصِيرُ",
                "Allahumma bika amsayna wa bika asbahna wa bika nahya wa bika namutu wa ilaykal-masir",
                "হে আল্লাহ! তোমার অনুগ্রহেই সন্ধ্যায় পৌঁছলাম, তোমার অনুগ্রহেই সকালে উঠলাম, তোমার ইচ্ছাতেই বাঁচি ও মরি এবং তোমার কাছেই ফিরে যাব।",
                "O Allah, by You we have entered the evening and by You we have entered the morning...",
                "আবু দাউদ ৫০৬৮",
                "সন্ধ্যার আমল।",
                1, 0, null, 2));

        // After Salah
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "after_salah",
                "أَسْتَغْفِرُ اللَّهَ (ثَلَاثًا) اللَّهُمَّ أَنْتَ السَّلَامُ، وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
                "Astaghfirullah (3x) Allahumma antas-salam, wa minkas-salam, tabarakta ya dhal-jalali wal-ikram",
                "আমি আল্লাহর নিকট ক্ষমা প্রার্থনা করছি (৩ বার)। হে আল্লাহ! তুমিই শান্তি, তোমার থেকেই শান্তি আসে। তুমি মহান হে মহিমাময় ও মর্যাদাবান।",
                "I seek forgiveness from Allah (3x). O Allah, You are As-Salam...",
                "মুসলিম ৫৯১",
                "প্রতিটি ফরজ নামাজের পর পাঠযোগ্য।",
                3, 0, null, 1));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "after_salah",
                "سُبْحَانَ اللَّهِ (ثَلَاثًا وَثَلَاثِينَ) وَالْحَمْدُ لِلَّهِ (ثَلَاثًا وَثَلَاثِينَ) وَاللَّهُ أَكْبَرُ (ثَلَاثًا وَثَلَاثِينَ) لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيكَ لَهُ...",
                "SubhanAllah (33x), Alhamdulillah (33x), Allahu Akbar (33x), La ilaha illallahu wahdahu la sharika lah...",
                "সুবহানাল্লাহ (৩৩ বার), আলহামদুলিল্লাহ (৩৩ বার), আল্লাহু আকবার (৩৩ বার) এবং ১ বার লা ইলাহা ইল্লাল্লাহু ওয়াহদাহু লা শারিকা লাহু...",
                "SubhanAllah (33x), Alhamdulillah (33x), Allahu Akbar (33x) and once La ilaha illallah...",
                "মুসলিম ৫৯৭",
                "নামাজের পর এই তাসবিহ পাঠে সমুদ্রের ফেনা পরিমাণ গুনাহ মাফ হয়।",
                33, 0, null, 2));

        // Before Sleep
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "before_sleep",
                "بِسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                "Bismika Allahumma amutu wa ahya",
                "হে আল্লাহ! তোমার নামে মরি এবং জীবিত হই।",
                "In Your name, O Allah, I die and I live.",
                "বুখারি ৬৩২৪",
                "ঘুমানোর আগে এই দুআ পাঠ করা সুন্নত।",
                1, 0, null, 1));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "before_sleep",
                "اللَّهُمَّ قِنِي عَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ",
                "Allahumma qini 'adhabaka yawma tab'athu 'ibadak",
                "হে আল্লাহ! যেদিন তুমি তোমার বান্দাদের উঠাবে সেদিন তোমার আজাব থেকে আমাকে রক্ষা করো।",
                "O Allah, protect me from Your punishment on the day You resurrect Your servants.",
                "আবু দাউদ ৫০৪৫",
                "শোওয়ার আগে ৩ বার পাঠ করা।",
                3, 0, null, 2));

        // Upon Waking
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "upon_waking",
                "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
                "Alhamdu lillahil-ladhi ahyana ba'da ma amatana wa ilayhin-nushur",
                "সকল প্রশংসা আল্লাহর যিনি মৃত্যুর পর আমাদের জীবন দিলেন এবং তাঁর কাছেই ফিরে যেতে হবে।",
                "Praise be to Allah Who has given us life after causing us to die and unto Him is the resurrection.",
                "বুখারি ৬৩২৫",
                "ঘুম থেকে উঠলে সর্বপ্রথম এই দুআ পাঠ করা সুন্নত।",
                1, 0, null, 1));

        // Protection
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "protection",
                "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
                "Bismillahil-ladhi la yadurru ma'asmihi shay'un fil-ardi wa la fis-sama'i wa huwas-sami'ul-'alim",
                "আল্লাহর নামে, যার নামের সাথে থাকলে আসমান ও জমিনে কোনো কিছু ক্ষতি করতে পারে না। তিনি সর্বশ্রোতা, সর্বজ্ঞ।",
                "In the Name of Allah with Whose name nothing can harm on earth or in heaven...",
                "আবু দাউদ ৫০৮৮, তিরমিযি ৩৩৮৮",
                "সকাল-সন্ধ্যায় ৩ বার পাঠ করলে কোনো বিপদ স্পর্শ করবে না।",
                3, 0, null, 1));

        // Rizq (Sustenance)
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "rizq",
                "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا وَرِزْقًا طَيِّبًا وَعَمَلًا مُتَقَبَّلًا",
                "Allahumma inni as'aluka 'ilman nafi'an wa rizqan tayyiban wa 'amalan mutaqabbala",
                "হে আল্লাহ! আমি তোমার কাছে উপকারী ইলম, হালাল রিযক এবং কবুল আমল প্রার্থনা করছি।",
                "O Allah, I ask You for beneficial knowledge, good provision and accepted deeds.",
                "ইবনে মাজাহ ৯২৫",
                "ফজরের পর পাঠ করা।",
                1, 0, null, 1));

        // Forgiveness
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "forgiveness",
                "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَٰهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَىٰ عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ...",
                "Allahumma anta rabbi la ilaha illa ant, khalaqtani wa ana 'abduk, wa ana 'ala 'ahdika wa wa'dika mastata't... (Sayyidul Istighfar)",
                "সাইয়্যিদুল ইস্তিগফার — হে আল্লাহ! তুমিই আমার রব, তুমি ছাড়া কোনো উপাস্য নেই। তুমি আমাকে সৃষ্টি করেছ এবং আমি তোমার বান্দা...",
                "Sayyidul Istighfar — O Allah, You are my Lord, none has the right to be worshipped but You...",
                "বুখারি ৬৩০৬",
                "এই দুআকে ইস্তিগফারের সেরা দুআ বলা হয়েছে। সকাল-সন্ধ্যায় পাঠ করলে জান্নাত নিশ্চিত।",
                1, 0, null, 1));

        // Travel
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "travel",
                "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنْقَلِبُونَ",
                "Allahu Akbar (3x), Subhana-alladhi sakhkhara lana hadha wa ma kunna lahu muqrinin wa inna ila rabbina lamunqalibun",
                "আল্লাহু আকবার (৩ বার)। পবিত্র সেই সত্তা যিনি এটিকে আমাদের অধীন করে দিয়েছেন, আমরা এটিকে নিয়ন্ত্রণ করার যোগ্য ছিলাম না। নিশ্চয়ই আমরা আমাদের রবের কাছে ফিরে যাব।",
                "Allah is the Greatest (3x). Glory is to Him Who has provided this for us...",
                "আবু দাউদ ২৬০২, তিরমিযি ৩৪৪৬",
                "সফরে বাহনে আরোহণকালে পাঠ করা।",
                1, 0, null, 1));

        // Parents
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "parents",
                "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                "Rabbir-hamhuma kama rabbayani saghira",
                "হে আমার রব! তাদের প্রতি দয়া করুন, যেভাবে তারা আমাকে ছোটকালে লালন করেছিলেন।",
                "My Lord, have mercy upon them as they brought me up [when I was] small.",
                "সূরা আল-ইসরা ১৭:২৪",
                "পিতামাতার জন্য সেরা দুআ।",
                1, 0, null, 1));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "parents",
                "اللَّهُمَّ اغْفِرْ لِي وَلِوَالِدَيَّ وَارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                "Allahumma-ghfir li wa liwalidayya war-hamhuma kama rabbayani saghira",
                "হে আল্লাহ! আমাকে এবং আমার পিতামাতাকে ক্ষমা করুন এবং তাদের প্রতি দয়া করুন যেমন তারা আমাকে শৈশবে লালন করেছিলেন।",
                "O Allah, forgive me and my parents and have mercy on them as they raised me when I was young.",
                "সূরা নূহ ৭১:২৮ | হিসনুল মুসলিম",
                "প্রতিদিন পিতামাতার জন্য দুআ করা সন্তানের দায়িত্ব।",
                1, 0, null, 2));

        // Ramadan
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "ramadan",
                "اللَّهُمَّ إِنَّكَ عَفُوٌّ كَرِيمٌ تُحِبُّ الْعَفْوَ فَاعْفُ عَنِّي",
                "Allahumma innaka 'afuwwun karimun tuhibbul-'afwa fa'fu 'anni",
                "হে আল্লাহ! তুমি অবশ্যই ক্ষমাশীল ও দানশীল, তুমি ক্ষমা করতে ভালোবাসো, তাই আমাকে ক্ষমা করে দাও।",
                "O Allah, You are Forgiving and Generous, You love to forgive, so forgive me.",
                "তিরমিযি ৩৫১৩",
                "লাইলাতুল ক্বদরে এই দুআ বেশি বেশি পড়তে বলা হয়েছে।",
                1, 0, null, 1));
        list.add(new com.devflux.deenone.data.local.entity.AzkarEntity(
                "ramadan",
                "اللَّهُمَّ بَارِكْ لَنَا فِي رَجَبَ وَشَعْبَانَ وَبَلِّغْنَا رَمَضَانَ",
                "Allahumma barik lana fi Rajab wa Sha'ban wa ballighna Ramadan",
                "হে আল্লাহ! রজব ও শাবানে আমাদের বরকত দাও এবং আমাদের রমজান পর্যন্ত পৌঁছে দাও।",
                "O Allah, bless us in Rajab and Sha'ban and bring us to Ramadan.",
                "আহমদ ২৩৪৬ | বায়হাকি",
                "রমজানের আগে ও রমজানে পাঠ করার দুআ।",
                1, 0, null, 2));
        db.azkarDao().insertAll(list);
    }

    private static void seedDefaultHadiths(AppDatabase db) {
        List<HadithEntity> list = new ArrayList<>();

        // 1. Sahih al-Bukhari 1
        list.add(new HadithEntity(
                "bukhari",
                "সহীহ আল-বুখারী (Sahih al-Bukhari)",
                1,
                "কিতাবুল ওহী (Book of Revelation)",
                "কাজের ফলাফল নিয়তের ওপর নির্ভরশীল",
                "হযরত উমর ইবনুল খাত্তাব (রা.)",
                "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ",
                "নিশ্চয়ই সকল কাজ নিয়তের ওপর নির্ভরশীল। প্রত্যেক ব্যক্তি তাই পাবে যার সে নিয়ত করেছে। যার হিজরত আল্লাহ ও তাঁর রাসুলের জন্য হবে, তার হিজরত আল্লাহ ও তাঁর রাসুলের জন্যই গণ্য হবে। আর যার হিজরত কোনো পার্থিব সম্পদ অর্জনের জন্য কিংবা কোনো নারীকে বিয়ে করার উদ্দেশ্যে হবে, তার হিজরত সেই উদ্দেশ্যের জন্যই গণ্য হবে।",
                "The reward of deeds depends upon the intentions and every person will get the reward according to what he has intended. So whoever emigrated for worldly benefits or for a woman to marry, his emigration was for what he emigrated for.",
                "اعمال کا دارومدار نیتوں پر ہے اور ہر انسان کے لیے وہی ہے جس کی اس نے نیت کی...",
                "সহীহ (Sahih - মুত্তাফাকুন আলাইহি)",
                "সহীহ বুখারী: ১, সহীহ মুসলিম: ১৯০৭",
                false,
                "নিয়ত ও ইখলাস"
        ));

        // 2. Sahih Muslim 8 (Hadith Jibril)
        list.add(new HadithEntity(
                "muslim",
                "সহীহ মুসলিম (Sahih Muslim)",
                8,
                "কিতাবুল ঈমান (Book of Faith)",
                "ঈমান, ইসলাম ও ইহসানের পরিচয় (হাদিসে জিবরীল)",
                "হযরত উমর ইবনুল খাত্তাব (রা.)",
                "قَالَ: يَا مُحَمَّدُ، أَخْبِرْنِي عَنِ الْإِسْلَامِ، فَقَالَ رَسُولُ اللهِ صَلَّى اللهُ عَلَيْهِ وَسَلَّمَ: الْإِسْلَامُ أَنْ تَشْهَدَ أَنْ لَا إِلٰهَ إِلَّا اللهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللهِ، وَتُقِيمَ الصَّلَاةَ، وَتُؤْتِيَ الزَّكَاةَ، وَتَصُومَ رَمَضَانَ، وَتَحُجَّ الْبَيْتَ إِنِ اسْتَطَعْتَ إِلَيْهِ سَبِيلًا",
                "জিবরীল (আ.) বললেন: হে মুহাম্মদ! আমাকে ইসলাম সম্পর্কে বলুন। রাসূলুল্লাহ (সা.) বললেন: ইসলাম হলো আপনি সাক্ষ্য দেবেন যে আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই এবং মুহাম্মদ (সা.) আল্লাহর রাসুল, সালাত কায়েম করবেন, যাকাত আদায় করবেন, রমজানের রোজা রাখবেন এবং সামর্থ্য থাকলে বায়তুল্লাহর হজ পালন করবেন।",
                "Jibril said: O Muhammad, tell me about Islam. The Messenger of Allah (ﷺ) said: Islam is to testify that there is no god but Allah and that Muhammad is the Messenger of Allah, to establish prayer, give zakah, fast Ramadan, and perform pilgrimage to the House if you are able.",
                "حضرت جبریل علیہ السلام نے پوچھا: اے محمد! مجھے اسلام کے بارے میں بتائیے...",
                "সহীহ (Sahih)",
                "সহীহ মুসলিম: ৮, সহীহ বুখারী: ৫০",
                false,
                "ঈমান ও ইসলাম"
        ));

        // 3. Sahih al-Bukhari 13
        list.add(new HadithEntity(
                "bukhari",
                "সহীহ আল-বুখারী (Sahih al-Bukhari)",
                13,
                "কিতাবুল ঈমান (Book of Faith)",
                "অন্য মুসলিমের জন্য কল্যাণ কামনা করা",
                "হযরত আনাস ইবনে মালিক (রা.)",
                "لَا يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ",
                "তোমাদের কেউ প্রকৃত মুমিন হতে পারবে না যতক্ষণ না সে তার মুসলিম ভাইয়ের জন্য তা-ই পছন্দ করে যা সে নিজের জন্য পছন্দ করে।",
                "None of you truly believes until he loves for his brother what he loves for himself.",
                "تم میں سے کوئی شخص اس وقت تک مومن نہیں ہو سکتا جب تک کہ وہ اپنے بھائی کے لیے وہی پسند نہ کرے جو اپنے لیے پسند کرتا ہے۔",
                "সহীহ (Sahih - মুত্তাফাকুন আলাইহি)",
                "সহীহ বুখারী: ১৩, সহীহ মুসলিম: ৪৫",
                false,
                "ভ্রাতৃত্ব ও আখলাক"
        ));

        // 4. Jami' at-Tirmidhi 1956
        list.add(new HadithEntity(
                "tirmidhi",
                "জামে আত-তিরমিযী (Jami' at-Tirmidhi)",
                1956,
                "কিতাবুল বিররি ওয়াস সিলাহ",
                "হাসিমুখে সাক্ষাৎ করা সাদাকাস্বরূপ",
                "হযরত আবু জর আল-গিফারী (রা.)",
                "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
                "তোমার মুসলিম ভাইয়ের মুখের দিকে তাকিয়ে মুচকি হাসা তোমার জন্য একটি সাদাকা (দান)।",
                "Your smiling in the face of your brother is charity for you.",
                "اپنے بھائی کے چہرے پر دیکھ کر مسکرانا تمہارے لیے صدقہ ہے۔",
                "সহীহ (Sahih / হাসান)",
                "জামে তিরমিযী: ১৯৫৬, সহীহ ইবনে হিব্বান: ৪৭৪",
                false,
                "সাদাকা ও সদাচার"
        ));

        // 5. Sunan Abu Dawud 4941
        list.add(new HadithEntity(
                "abudawud",
                "সুনান আবু দাউদ (Sunan Abu Dawud)",
                4941,
                "কিতাবুল আদব (Book of Manners)",
                "দয়াশীলদের প্রতি আল্লাহর রহমত",
                "হযরত আব্দুল্লাহ ইবনে আমর (রা.)",
                "الرَّاحِمُونَ يَرْحَمُهُمُ الرَّحْمٰنُ، ارْحَمُوا مَنْ فِي الْأَرْضِ يَرْحَمْكُمْ مَنْ فِي السَّمَاءِ",
                "দয়াশীলদের প্রতি পরম দয়াময় আল্লাহ দয়া করেন। তোমরা জমিনবাসীদের প্রতি দয়া করো, তাহলে যিনি আকাশে আছেন (আল্লাহ) তিনি তোমাদের প্রতি দয়া করবেন।",
                "The merciful are shown mercy by the All-Merciful. Be merciful to those on the earth and the One in the heavens will be merciful to you.",
                "رحم کرنے والوں پر رحمن رحم فرماتا ہے۔ تم زمین والوں پر رحم کرو، آسمان والا تم پر رحم کرے گا۔",
                "সহীহ (Sahih)",
                "সুনান আবু দাউদ: ৪৯৪১, জামে তিরমিযী: ১৯২৪",
                false,
                "দয়া ও মানবিকতা"
        ));

        // 6. Sunan an-Nasa'i 3104
        list.add(new HadithEntity(
                "nasai",
                "সুনান আন-নাসায়ী (Sunan an-Nasa'i)",
                3104,
                "কিতাবুল জিহাদ",
                "মায়ের সেবা ও জান্নাতের নৈকট্য",
                "হযরত মুআবিয়া ইবনে জাহেমা আস-সুলামী (রা.)",
                "الْزَمْ رِجْلَهَا فَثَمَّ الْجَنَّةُ",
                "তোমার মায়ের পা জড়িয়ে ধরে সেবা করতে থাকো, কেননা সেখানেই জান্নাত রয়েছে।",
                "Stay at her (your mother's) feet, for Paradise is there.",
                "اپنی ماں کے پاؤں لازم پکڑ لو کیونکہ جنت وہیں ہے۔",
                "হাসান সহীহ (Hasan Sahih)",
                "সুনান আন-নাসায়ী: ৩১০৪, মুসনাদ আহমদ: ১৫৫৭৭",
                false,
                "পিতামাতার মর্যাদা"
        ));

        // 7. Sunan Ibn Majah 224
        list.add(new HadithEntity(
                "ibnmajah",
                "সুনান ইবনে মাজাহ (Sunan Ibn Majah)",
                224,
                "মুকাদ্দিমা (Introduction)",
                "দ্বীনি ইলম অর্জনের আবশ্যকতা",
                "হযরত আনাস ইবনে মালিক (রা.)",
                "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَىٰ كُلِّ مُسْلِمٍ",
                "জ্ঞান (দ্বীনি ইলম) অন্বেষণ করা প্রত্যেক মুসলমানের ওপর ফরজ (অবশ্য কর্তব্য)।",
                "Seeking knowledge is an obligation upon every Muslim.",
                "علم حاصل کرنا ہر مسلمان پر فرض ہے۔",
                "সহীহ / হাসান (Hasan Sahih)",
                "সুনান ইবনে মাজাহ: ২২৪, সিলসিলাতুস সাহীহাহ: ৪১৬",
                false,
                "ইলম ও জ্ঞানার্জন"
        ));

        // 8. Sahih al-Bukhari 5027
        list.add(new HadithEntity(
                "bukhari",
                "সহীহ আল-বুখারী (Sahih al-Bukhari)",
                5027,
                "ফাযায়িলুল কুরআন",
                "কুরআন শিক্ষা ও শিক্ষাদানের শ্রেষ্ঠত্ব",
                "হযরত উসমান ইবনে আফফান (রা.)",
                "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ",
                "তোমাদের মধ্যে সর্বোত্তম সেই ব্যক্তি, যে নিজে কুরআন শিখে এবং অন্যকে তা শেখায়।",
                "The best among you are those who learn the Quran and teach it.",
                "تم میں سے بہترین شخص وہ ہے جو قرآن سیکھے اور سکھائے۔",
                "সহীহ (Sahih)",
                "সহীহ বুখারী: ৫০২৭, জামে তিরমিযী: ২৯০৭",
                false,
                "আল-কুরআনের মর্যাদা"
        ));

        db.hadithDao().insertAll(list);
    }
}
