package com.devflux.deenone.core.amal;

import android.content.Context;
import android.content.SharedPreferences;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.DailyAmalDao;
import com.devflux.deenone.data.local.entity.DailyAmalEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DailyAmalRotationEngine {

    private static final String PREFS_NAME = "daily_amal_rotation_prefs";
    private static final String KEY_SHOWN_CODES = "shown_amal_codes_history";
    private static final String KEY_LAST_ROTATION_DATE = "last_rotation_date";
    private static final String KEY_SELECTED_DHIKR_INDEX = "selected_dhikr_index_";
    private static final String KEY_TASBIH_COUNT_PREFIX = "tasbih_count_";
    private static final String KEY_TASBIH_COMPLETED_PREFIX = "tasbih_completed_times_";

    public static final int TOTAL_DHIKR_COUNT = 14;

    public static class TasbihChallengeInfo {
        public final int id;
        public final String duaTitle;
        public final String duaMeaning;
        public final int targetCount;
        public final int rewardPoints;

        public TasbihChallengeInfo(int id, String duaTitle, String duaMeaning, int targetCount, int rewardPoints) {
            this.id = id;
            this.duaTitle = duaTitle;
            this.duaMeaning = duaMeaning;
            this.targetCount = targetCount;
            this.rewardPoints = rewardPoints;
        }

        public TasbihChallengeInfo(String duaTitle, String duaMeaning, int targetCount, int rewardPoints) {
            this(0, duaTitle, duaMeaning, targetCount, rewardPoints);
        }
    }

    public static String getTodayDateString(Context context) {
        return UserLocationTimezoneHelper.getTodayLocationDateString(context);
    }

    public static String getTodayMonthYearBangla(Context context) {
        return UserLocationTimezoneHelper.getTodayMonthYearBangla(context);
    }

    public static String getTodayMonthYearFormatted(Context context, boolean isBn) {
        return UserLocationTimezoneHelper.getTodayMonthYearFormatted(context, isBn);
    }

    public static void ensureDailyRotation(Context context) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String today = getTodayDateString(context);
            String lastDate = prefs.getString(KEY_LAST_ROTATION_DATE, "");

            AppDatabase db = AppDatabase.getInstance(context);
            DailyAmalDao dao = db.dailyAmalDao();
            int countInDb = dao.getCountByDate(today);

            if (countInDb == 0 || !today.equals(lastDate)) {
                List<DailyAmalEntity> chosenAmals = selectAmalsForDate(context, today);
                dao.insertAll(chosenAmals);
                prefs.edit().putString(KEY_LAST_ROTATION_DATE, today).apply();
            }
        });
    }

    /**
     * Canonical Daily Amal Selection:
     * - Returns the full suite of daily authentic deeds.
     * - Only includes Surah Al-Kahf on Friday (জুম্মার দিন) as per canonical Islamic practice.
     */
    public static List<DailyAmalEntity> selectAmalsForDate(Context context, String dateString) {
        List<DailyAmalEntity> masterPool = IslamicContentVerificationPipeline.getMasterAuthenticAmalPool(dateString);
        List<DailyAmalEntity> selected = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        boolean isFriday = (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);

        for (DailyAmalEntity amal : masterPool) {
            if ("AMAL_SURAH_KAHF".equals(amal.getAmalCode())) {
                if (isFriday) {
                    selected.add(amal);
                }
            } else {
                selected.add(amal);
            }
        }

        return selected;
    }

    public static List<TasbihChallengeInfo> getAllTasbihChallenges(Context context) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        List<TasbihChallengeInfo> list = new ArrayList<>();
        if (isBn) {
            list.add(new TasbihChallengeInfo(0, "রব্বি ইন্নি লিমা আনযালতা ইলাইয়া মিন খাইরিন ফাকির", "“হে আমার রব, নিশ্চয় আপনি আমার প্রতি যে অনুগ্রহই নাযিল করবেন, আমি তার মুখাপেক্ষী” — সূরা আল-ক্বাসাস: ২৪", 100, 50));
            list.add(new TasbihChallengeInfo(1, "ইয়া মুক্বাল্লিবাল কুলূব, সাব্বিত ক্বালবি 'আলা দ্বীনিক", "“হে অন্তরসমূহের পরিবর্তনকারী! আমার অন্তরকে আপনার দ্বীনের ওপর অবিচল রাখুন” — জামে তিরমিযী: ২১৪০", 100, 50));
            list.add(new TasbihChallengeInfo(2, "লা ইলাহা ইল্লাল্লাহু মুহাম্মাদুর রাসুলুল্লাহ", "“আল্লাহ ব্যতীত কোনো উপাস্য নেই, মুহাম্মদ (সা.) আল্লাহর রাসুল” — সহীহ বুখারী: ৮", 100, 50));
            list.add(new TasbihChallengeInfo(3, "সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম", "“আল্লাহ তাঁর ওপর শান্তি ও বরকত বর্ষণ করুন” — সহীহ মুসলিম: ৪০৮", 100, 50));
            list.add(new TasbihChallengeInfo(4, "হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকিল", "“আল্লাহই আমাদের জন্য যথেষ্ট, তিনিই উত্তম অভিভাবক” — সূরা আলে-ইমরান: ১৭৩", 100, 50));
            list.add(new TasbihChallengeInfo(5, "সুবহানাল্লাহিল আযীম", "“মহিমান্বিত মহান আল্লাহ অতীব পবিত্র” — সহীহ বুখারী: ৬৪০৬", 100, 50));
            list.add(new TasbihChallengeInfo(6, "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ", "“হে আল্লাহ! আপনি মুহাম্মদ (সা.)-এর ওপর সালাত ও রহমত বর্ষণ করুন” — সহীহ বুখারী: ৩৩৭০", 100, 50));
            list.add(new TasbihChallengeInfo(7, "লা হাওলা ওয়া লা কুওয়াতা ইল্লা বিল্লাহ", "“আল্লাহর সাহায্য ব্যতীত কোনো শক্তি নেই, কোনো পরাক্রম নেই” — সহীহ বুখারী: ৪২০৫", 100, 50));
            list.add(new TasbihChallengeInfo(8, "সুবহানাল্লাহি ওয়া বিহামদিহি", "“আল্লাহর প্রশংসাসহ তাঁর পবিত্রতা ঘোষণা করছি” — সহীহ বুখারী: ৬৪০৫", 100, 50));
            list.add(new TasbihChallengeInfo(9, "লা ইলাহা ইল্লাল্লাহ", "“আল্লাহ ব্যতীত সত্য কোনো উপাস্য নেই” — সহীহ মুসলিম: ২৬", 100, 50));
            list.add(new TasbihChallengeInfo(10, "আস্তাগফিরুল্লাহ", "“আমি আল্লাহর নিকট ক্ষমা প্রার্থনা করছি” — সহীহ মুসলিম: ৭০৩৪", 100, 50));
            list.add(new TasbihChallengeInfo(11, "আল্লাহু আকবার", "“আল্লাহ মহান” — সহীহ বুখারী: ৮৪২", 100, 50));
            list.add(new TasbihChallengeInfo(12, "সুবহানাল্লাহ", "“আল্লাহ অতীব পবিত্র” — সহীহ মুসলিম: ৫৯১", 100, 50));
            list.add(new TasbihChallengeInfo(13, "আলহামদুলিল্লাহ", "“সকল প্রশংসা আল্লাহর জন্য” — সহীহ মুসলিম: ২২৩", 100, 50));
        } else {
            list.add(new TasbihChallengeInfo(0, "Rabbi inni lima anzalta ilayya min khayrin faqir", "“My Lord, indeed I am in need of whatever good You would send down to me” — Surah Al-Qasas: 24", 100, 50));
            list.add(new TasbihChallengeInfo(1, "Ya Muqallib al-qulub, thabbit qalbi 'ala dinik", "“O Controller of the hearts, make my heart steadfast upon Your religion” — Jami' at-Tirmidhi: 2140", 100, 50));
            list.add(new TasbihChallengeInfo(2, "La ilaha illallah, Muhammadur Rasulullah", "“There is no deity except Allah, and Muhammad is the Messenger of Allah” — Sahih Bukhari: 8", 100, 50));
            list.add(new TasbihChallengeInfo(3, "Sallallahu 'alayhi wa sallam", "“May Allah bless him and grant him peace” — Sahih Muslim: 408", 100, 50));
            list.add(new TasbihChallengeInfo(4, "Hasbunallahu wa ni'mal wakeel", "“Allah is sufficient for us, and He is the best disposer of affairs” — Surah Ali 'Imran: 173", 100, 50));
            list.add(new TasbihChallengeInfo(5, "SubhanAllahil Azeem", "“Glory be to Allah, the Supreme” — Sahih Bukhari: 6406", 100, 50));
            list.add(new TasbihChallengeInfo(6, "Allahumma salli 'ala Muhammad", "“O Allah, send blessings upon Muhammad” — Sahih Bukhari: 3370", 100, 50));
            list.add(new TasbihChallengeInfo(7, "La hawla wa la quwwata illa billah", "“There is no power and no strength except with Allah” — Sahih Bukhari: 4205", 100, 50));
            list.add(new TasbihChallengeInfo(8, "SubhanAllahi wa bihamdihi", "“Glory be to Allah and His is the praise” — Sahih Bukhari: 6405", 100, 50));
            list.add(new TasbihChallengeInfo(9, "La ilaha illallah", "“There is no deity except Allah” — Sahih Muslim: 26", 100, 50));
            list.add(new TasbihChallengeInfo(10, "Astaghfirullah", "“I seek forgiveness from Allah” — Sahih Muslim: 7034", 100, 50));
            list.add(new TasbihChallengeInfo(11, "Allahu Akbar", "“Allah is the Greatest” — Sahih Bukhari: 842", 100, 50));
            list.add(new TasbihChallengeInfo(12, "SubhanAllah", "“Glory be to Allah” — Sahih Muslim: 591", 100, 50));
            list.add(new TasbihChallengeInfo(13, "Alhamdulillah", "“All praise is due to Allah” — Sahih Muslim: 223", 100, 50));
        }
        return list;
    }

    public static TasbihChallengeInfo getTasbihChallengeByIndex(Context context, int index) {
        List<TasbihChallengeInfo> list = getAllTasbihChallenges(context);
        int safeIndex = Math.max(0, Math.min(index, list.size() - 1));
        return list.get(safeIndex);
    }

    public static int getSelectedDhikrIndex(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getTodayDateString(context);
        int defaultIndex = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % TOTAL_DHIKR_COUNT;
        return prefs.getInt(KEY_SELECTED_DHIKR_INDEX + today, defaultIndex);
    }

    public static void setSelectedDhikrIndex(Context context, int index) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getTodayDateString(context);
        prefs.edit().putInt(KEY_SELECTED_DHIKR_INDEX + today, index).apply();
    }

    public static TasbihChallengeInfo getTodayTasbihChallenge(Context context) {
        int selectedIndex = getSelectedDhikrIndex(context);
        return getTasbihChallengeByIndex(context, selectedIndex);
    }

    public static int getTodayDhikrCount(Context context, int dhikrIndex) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getTodayDateString(context);
        return prefs.getInt(KEY_TASBIH_COUNT_PREFIX + today + "_" + dhikrIndex, 0);
    }

    public static void saveTodayDhikrCount(Context context, int dhikrIndex, int count) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getTodayDateString(context);
        prefs.edit().putInt(KEY_TASBIH_COUNT_PREFIX + today + "_" + dhikrIndex, count).apply();
    }

    public static int getTodayDhikrCompletedTimes(Context context, int dhikrIndex) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getTodayDateString(context);
        return prefs.getInt(KEY_TASBIH_COMPLETED_PREFIX + today + "_" + dhikrIndex, 0);
    }

    public static void incrementTodayDhikrCompletedTimes(Context context, int dhikrIndex) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String today = getTodayDateString(context);
        int current = getTodayDhikrCompletedTimes(context, dhikrIndex);
        prefs.edit().putInt(KEY_TASBIH_COMPLETED_PREFIX + today + "_" + dhikrIndex, current + 1).apply();
    }

    public static int getTodayTotalTasbihCount(Context context) {
        int total = 0;
        for (int i = 0; i < TOTAL_DHIKR_COUNT; i++) {
            total += getTodayDhikrCount(context, i) + (getTodayDhikrCompletedTimes(context, i) * 100);
        }
        return total;
    }

    // Backwards-compatible convenience methods
    public static int getTodayTasbihCount(Context context) {
        return getTodayDhikrCount(context, getSelectedDhikrIndex(context));
    }

    public static void saveTodayTasbihCount(Context context, int count) {
        saveTodayDhikrCount(context, getSelectedDhikrIndex(context), count);
    }

    public static int getTodayTasbihCompletedTimes(Context context) {
        return getTodayDhikrCompletedTimes(context, getSelectedDhikrIndex(context));
    }

    public static void incrementTodayTasbihCompletedTimes(Context context) {
        incrementTodayDhikrCompletedTimes(context, getSelectedDhikrIndex(context));
    }
}
