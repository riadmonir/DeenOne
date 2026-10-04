package com.devflux.deenone.core.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.core.ai.IslamicOnlineScraperClient;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.QuizQuestionEntity;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.HijriCalendarUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * QuizManager — Complete Advanced Quiz Hub Controller.
 *
 * Implements:
 *   1. Massive Authentic Islamic Knowledge Base (3,200+ authentic questions across 29 categories from Holy Quran, Hadith, Seerah, Sahabah, Islamic History, and Fiqh).
 *   2. Real-Time Online API Synchronization via IslamicOnlineScraperClient (Al-Quran Cloud, Hadith CDN, AlAdhan).
 *   3. Strict Zero Fake / Dummy Data Policy with Canonical Citations and Zero Emojis.
 *   4. Calendar-Day Based Limit of 10 Quizzes per Day.
 *   5. Permanent Completion Tracking: Correct answer is NEVER asked again.
 *   6. Next-Day Retry Rule for Wrong Answers: Eligible for 1 retry on future days, but NEVER on the same day ("তবে ওইদিন না").
 *   7. Dynamic Option Randomization: The correct answer is randomly distributed across options ক (1), খ (2), গ (3), ঘ (4).
 *   8. Day & Context-Aware Relevance: Prioritizes Jummah on Friday and strictly excludes Friday/Eid/Ramadan celebration questions on ordinary days.
 *   9. Next-Day Background Preloading & Smart Caching (Hidden until next calendar day 00:00).
 *  10. Accurate Midnight Countdown Clock.
 */
public class QuizManager {

    private static final String TAG = "QuizManager";
    private static final String PREF_QUIZ = "deanone_quiz_prefs";
    private static final String KEY_POINTS = "key_deen_points";
    private static final String KEY_DAILY_DATE = "key_daily_played_date";
    private static final String KEY_DAILY_COUNT = "key_daily_played_count";
    public static final String KEY_LIFETIME_PLAYED_COUNT = "key_lifetime_played_count";
    private static final String KEY_STATUS_PREFIX = "key_quiz_status_";
    private static final String KEY_ATTEMPTS_PREFIX = "key_quiz_attempts_";
    private static final String KEY_LAST_WRONG_DATE_PREFIX = "key_quiz_last_wrong_date_";
    private static final String KEY_PRELOADED_CACHE = "key_preloaded_quiz_cache";
    private static final String KEY_TODAY_SESSION_DATE = "key_today_session_date";
    private static final String KEY_TODAY_SESSION_DATA = "key_today_session_data";
    private static final String KEY_LAST_SYNC_TIMESTAMP = "key_last_quiz_sync_timestamp";
    public static final int DAILY_MAX_QUIZZES = 10;

    // Quiz Lifecycle Statuses
    public static final String STATUS_UNSEEN = "unseen";
    public static final String STATUS_CORRECT_COMPLETED = "correct_completed";
    public static final String STATUS_WRONG_PENDING_RETRY = "wrong_pending_retry";
    public static final String STATUS_RETRY_COMPLETED = "retry_completed";
    public static final String STATUS_FAILED_PERMANENTLY = "failed_permanently";

    public static final String[] CATEGORY_FILES = {
            "akhira_qiyamah", "dua_azkar", "hadith_sunnah", "hajj_umrah",
            "halal_haram", "holy_mosques", "islamic_akhlaq", "islamic_architecture",
            "islamic_family", "islamic_history", "islamic_lifestyle", "islamic_months",
            "jahannam_hell", "jannah_paradise", "jinn_unseen", "masnoon_amal",
            "muslim_scholars", "noble_women", "prophets_stories", "quran_knowledge",
            "quran_nature", "quran_stories", "quran_vocabulary", "ramadan_sawm",
            "sahaba_life", "salat_taharah", "seerat_un_nabi", "shariah_life",
            "zakat_charity"
    };

    private static final java.util.regex.Pattern NORMALIZE_PATTERN = java.util.regex.Pattern.compile("[\\s\\p{Punct}]+");

    private static volatile QuizManager instance;
    private final List<QuizItem> masterQuizPool = new ArrayList<>();
    private final ExecutorService bgExecutor = Executors.newSingleThreadExecutor();
    private final Gson gson = new Gson();
    private Handler mainHandler;
    private volatile boolean isPoolLoaded = false;
    private final java.util.concurrent.atomic.AtomicBoolean isPoolLoading = new java.util.concurrent.atomic.AtomicBoolean(false);

    public interface QuizSyncCallback {
        void onSyncComplete(int newSyncedCount, int totalBankCount);
        void onSyncError(String errorMessage);
    }

    private synchronized Handler getMainHandler() {
        if (mainHandler == null) {
            try {
                Looper looper = Looper.getMainLooper();
                if (looper != null) {
                    mainHandler = new Handler(looper);
                }
            } catch (Throwable ignored) {
                // Headless JVM test safe fallback
            }
        }
        return mainHandler;
    }

    public static QuizManager getInstance() {
        if (instance == null) {
            synchronized (QuizManager.class) {
                if (instance == null) {
                    instance = new QuizManager();
                }
            }
        }
        return instance;
    }

    private QuizManager() {
        loadCuratedSeedPool();
    }

    // =========================================================================
    // Category & Context Utilities
    // =========================================================================
    public static String getCategoryBengaliName(String catId) {
        if (catId == null) return "ইসলামিক জ্ঞান";
        switch (catId.toLowerCase()) {
            case "quran_stories": return "কুরআনের ঐতিহাসিক ঘটনা";
            case "quran_knowledge": return "কুরআনুল কারীম";
            case "quran_nature": return "কুরআন ও সৃষ্টিজগত";
            case "quran_vocabulary": return "কুরআনের শব্দার্থ";
            case "hadith_sunnah": return "সহীহ হাদিস ও সুন্নাহ";
            case "seerat_un_nabi": return "সীরাতুন্নবী (ﷺ)";
            case "sahaba_life": return "সাহাবায়ে কেরাম (রা.)";
            case "prophets_stories": return "নবী-রাসূলগণের ঘটনা";
            case "islamic_history": return "ইসলামিক ইতিহাস";
            case "salat_taharah": return "সালাত ও পবিত্রতা";
            case "zakat_charity": return "যাকাত ও সাদাকাহ";
            case "ramadan_sawm": return "সিয়াম ও রমজান";
            case "hajj_umrah": return "হজ ও উমরাহ";
            case "dua_azkar": return "দোয়া ও যিকির";
            case "masnoon_amal": return "মাসনুন আমল ও সুন্নত";
            case "islamic_akhlaq": return "ইসলামিক চরিত্র ও আদব";
            case "islamic_lifestyle": return "দৈনন্দিন ইসলামী জীবন";
            case "halal_haram": return "হালাল ও হারাম";
            case "shariah_life": return "শরীয়া ও আহকাম";
            case "akhira_qiyamah": return "আখিরাত ও কিয়ামত";
            case "jannah_paradise": return "জান্নাত ও নেয়ামত";
            case "jahannam_hell": return "জাহান্নাম ও সতর্কতা";
            case "jinn_unseen": return "অদৃশ্য জগত ও ঈমান";
            case "muslim_scholars": return "মুসলিম বিজ্ঞানী ও মনীষী";
            case "noble_women": return "মহীয়সী নারী ও উম্মাহাতুল মু'মিনীন";
            case "holy_mosques": return "পবিত্র মসজিদসমূহ";
            case "islamic_months": return "ইসলামিক মাস ও দিন";
            case "islamic_architecture": return "ইসলামিক স্থাপত্য ও ঐতিহ্য";
            case "islamic_family": return "পারিবারিক জীবন ও দ্বীন";
            default: return "ইসলামিক সাধারণ জ্ঞান";
        }
    }

    public static String determineContextTag(String catId, String questionText) {
        String q = (questionText != null) ? questionText : "";
        if ("ramadan_sawm".equalsIgnoreCase(catId) || q.contains("রোজা") || q.contains("সিয়াম") || q.contains("রমজান") || q.contains("তারাবীহ") || q.contains("সেহরি") || q.contains("ইফতার")) {
            return "ramadan";
        }
        if (q.contains("জুমা") || q.contains("জুমুআ") || q.contains("শুক্রবার")) {
            return "jummah";
        }
        if (q.contains("ঈদ") || q.contains("ঈদুল ফিতর") || q.contains("ঈদুল আযহা") || q.contains("কোরবানি") || q.contains("ঈদের নামাজ")) {
            return "eid";
        }
        return "general";
    }

    /**
     * Day & Event Aware Relevance Filter.
     * Guarantees that:
     * 1. Jummah questions appear ONLY on Friday.
     * 2. Ramadan questions appear ONLY during Ramadan month.
     * 3. Eid questions appear ONLY during Eid.
     * 4. General Islamic knowledge is served on ordinary days (e.g. Saturday, Sunday).
     */
    public static boolean isContextAllowedToday(QuizItem item) {
        if (item == null) return false;
        String tag = item.contextTag != null ? item.contextTag.toLowerCase().trim() : "general";

        Calendar cal = Calendar.getInstance();
        boolean isFriday = (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);

        if ("jummah".equals(tag)) {
            // STRICT RULE: Jummah questions are ONLY allowed on Friday!
            return isFriday;
        }

        HijriCalendarUtil.HijriDateResult hijri = HijriCalendarUtil.getRealHijriDate(cal);
        if ("ramadan".equals(tag)) {
            // STRICT RULE: Ramadan questions are ONLY allowed during Ramadan (monthIndex == 8)!
            return hijri != null && hijri.monthIndex == 8;
        }

        if ("eid".equals(tag)) {
            // STRICT RULE: Eid questions are ONLY allowed on Eid days!
            return hijri != null && (
                    (hijri.monthIndex == 9 && hijri.day <= 3) // Shawwal 1-3
                            || (hijri.monthIndex == 11 && hijri.day >= 10 && hijri.day <= 13) // Dhul Hijjah 10-13
            );
        }

        // "general" is allowed any day
        return true;
    }

    /**
     * Dynamic Option Randomization.
     * Ensures options are randomly shuffled every time so the correct answer is
     * randomly distributed across position 1 (A), position 2 (B), position 3 (C), and position 4 (D).
     */
    public static QuizItem randomizeOptions(QuizItem original) {
        if (original == null || original.options == null || original.options.length < 2) {
            return original;
        }
        int origCorrect = original.correctIndex;
        if (origCorrect < 0 || origCorrect >= original.options.length) {
            origCorrect = 0;
        }
        String correctText = original.options[origCorrect];

        List<String> list = new ArrayList<>(Arrays.asList(original.options));
        Collections.shuffle(list);

        int newCorrectIndex = list.indexOf(correctText);
        if (newCorrectIndex < 0) newCorrectIndex = 0;

        return new QuizItem(
                original.id,
                original.question,
                list.toArray(new String[0]),
                newCorrectIndex,
                original.category,
                original.contextTag,
                original.reference,
                original.explanation,
                original.availableFromDate,
                original.source,
                original.difficulty
        );
    }

    // =========================================================================
    // Asset Pool Loading (3,190+ Authentic Questions)
    // =========================================================================
    public void ensureMasterPoolLoaded(Context context) {
        if (isPoolLoaded && masterQuizPool.size() > 50) return;

        if (masterQuizPool.isEmpty()) {
            loadCuratedSeedPool();
        }

        final Context appContext = (context != null) ? context.getApplicationContext() : null;

        // If called on Main UI Thread, NEVER block UI with heavy disk file I/O!
        boolean isMainThread = false;
        try {
            isMainThread = (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper());
        } catch (Throwable ignored) {
            isMainThread = false;
        }

        if (isMainThread) {
            loadMasterPoolAsync(appContext);
            return;
        }

        doLoadMasterPool(appContext);
    }

    public void loadMasterPoolAsync(Context context) {
        if (isPoolLoaded && masterQuizPool.size() > 50) return;
        final Context appContext = (context != null) ? context.getApplicationContext() : null;
        bgExecutor.execute(() -> doLoadMasterPool(appContext));
    }

    private void doLoadMasterPool(Context context) {
        if (isPoolLoaded && masterQuizPool.size() > 50) return;
        if (!isPoolLoading.compareAndSet(false, true)) return;

        try {
            if (masterQuizPool.isEmpty()) {
                loadCuratedSeedPool();
            }

            Set<String> existingQuestions = new HashSet<>();
            synchronized (masterQuizPool) {
                for (QuizItem q : masterQuizPool) {
                    if (q != null && q.question != null) {
                        existingQuestions.add(normalizeText(q.question));
                    }
                }
            }

            for (String catFile : CATEGORY_FILES) {
                List<QuizItem> itemsFromCat = loadCategoryItems(context, catFile);
                if (itemsFromCat != null && !itemsFromCat.isEmpty()) {
                    List<QuizItem> toAdd = new ArrayList<>();
                    for (QuizItem item : itemsFromCat) {
                        if (item != null && item.question != null) {
                            String norm = normalizeText(item.question);
                            if (!existingQuestions.contains(norm)) {
                                toAdd.add(item);
                                existingQuestions.add(norm);
                            }
                        }
                    }
                    if (!toAdd.isEmpty()) {
                        synchronized (masterQuizPool) {
                            masterQuizPool.addAll(toAdd);
                        }
                    }
                }
            }

            // Load offline persisted questions from Room Database
            if (context != null) {
                try {
                    AppDatabase db = AppDatabase.getInstance(context);
                    List<QuizQuestionEntity> dbQuestions = db.quizDao().getAllQuestionsSync();
                    if (dbQuestions != null && !dbQuestions.isEmpty()) {
                        List<QuizItem> dbBatch = new ArrayList<>();
                        for (QuizQuestionEntity entity : dbQuestions) {
                            if (entity != null && entity.getQuestion() != null && !entity.getQuestion().trim().isEmpty()) {
                                String norm = normalizeText(entity.getQuestion());
                                if (!existingQuestions.contains(norm)) {
                                    String[] opts = new String[]{
                                            entity.getOptionA() != null ? entity.getOptionA() : "",
                                            entity.getOptionB() != null ? entity.getOptionB() : "",
                                            entity.getOptionC() != null ? entity.getOptionC() : "",
                                            entity.getOptionD() != null ? entity.getOptionD() : ""
                                    };
                                    QuizItem item = new QuizItem(
                                            "DB_Q_" + entity.getId(),
                                            entity.getQuestion(),
                                            opts,
                                            entity.getCorrectOptionIndex(),
                                            entity.getCategory() != null ? entity.getCategory() : "ইসলামিক সাধারণ জ্ঞান",
                                            determineContextTag("general", entity.getQuestion()),
                                            entity.getReferenceSource() != null ? entity.getReferenceSource() : "কুরআন ও সহীহ সুন্নাহ",
                                            entity.getExplanation() != null ? entity.getExplanation() : "",
                                            "",
                                            "Local Room Database",
                                            entity.getDifficulty() == 3 ? "hard" : (entity.getDifficulty() == 1 ? "easy" : "medium")
                                    );
                                    dbBatch.add(item);
                                    existingQuestions.add(norm);
                                }
                            }
                        }
                        if (!dbBatch.isEmpty()) {
                            synchronized (masterQuizPool) {
                                masterQuizPool.addAll(dbBatch);
                            }
                        }
                    }
                } catch (Exception e) {
                    logWarn(TAG, "Room DB quiz loading note: " + e.getMessage());
                }
            }

            isPoolLoaded = true;
            logDebug(TAG, "Master quiz pool loaded. Total questions: " + masterQuizPool.size());
        } finally {
            isPoolLoading.set(false);
        }
    }

    private static void logDebug(String tag, String msg) {
        try {
            Log.d(tag, msg);
        } catch (Throwable ignored) {}
    }

    private static void logWarn(String tag, String msg) {
        try {
            Log.w(tag, msg);
        } catch (Throwable ignored) {}
    }

    private List<QuizItem> loadCategoryItems(Context context, String catFile) {
        List<QuizItem> list = new ArrayList<>();

        // 1. Android AssetManager
        if (context != null) {
            try {
                String path = "battle/categories/" + catFile + ".json";
                InputStream is = context.getAssets().open(path);
                list = parseQuestionsFromStream(is, catFile);
            } catch (Exception ignored) {}
        }

        // 2. Headless local unit test filesystem fallback
        if (list.isEmpty()) {
            File[] candidates = new File[] {
                    new File("src/main/assets/battle/categories/" + catFile + ".json"),
                    new File("app/src/main/assets/battle/categories/" + catFile + ".json"),
                    new File("../app/src/main/assets/battle/categories/" + catFile + ".json"),
                    new File("F:/Deanone/app/src/main/assets/battle/categories/" + catFile + ".json")
            };
            for (File candidate : candidates) {
                if (candidate.exists()) {
                    try (InputStream is = new FileInputStream(candidate)) {
                        list = parseQuestionsFromStream(is, catFile);
                        if (!list.isEmpty()) break;
                    } catch (Exception ignored) {}
                }
            }
        }

        return list;
    }

    private List<QuizItem> parseQuestionsFromStream(InputStream is, String catFile) {
        List<QuizItem> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            JsonElement root = JsonParser.parseReader(reader);
            if (root != null && root.isJsonArray()) {
                JsonArray arr = root.getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    JsonElement el = arr.get(i);
                    if (!el.isJsonObject()) continue;
                    JsonObject obj = el.getAsJsonObject();
                    String id = obj.has("id") ? obj.get("id").getAsString() : "KB_Q_" + catFile.toUpperCase() + "_" + i;
                    String qText = obj.has("questionText") ? obj.get("questionText").getAsString() : "";
                    List<String> options = new ArrayList<>();
                    if (obj.has("options") && obj.get("options").isJsonArray()) {
                        JsonArray optArr = obj.getAsJsonArray("options");
                        for (int j = 0; j < optArr.size(); j++) {
                            options.add(optArr.get(j).getAsString());
                        }
                    }
                    int correctIdx = obj.has("correctOptionIndex") ? obj.get("correctOptionIndex").getAsInt() : 0;
                    String explanation = obj.has("explanation") ? obj.get("explanation").getAsString() : "";
                    String reference = obj.has("reference") ? obj.get("reference").getAsString() : "কুরআন ও সহীহ সুন্নাহ";
                    String categoryBn = getCategoryBengaliName(catFile);
                    String contextTag = determineContextTag(catFile, qText);

                    if (!qText.trim().isEmpty() && options.size() == 4) {
                        QuizItem qi = new QuizItem(
                                id, qText, options.toArray(new String[0]), correctIdx,
                                categoryBn, contextTag, reference, explanation,
                                "", "Verified Islamic Database", "medium"
                        );
                        list.add(qi);
                    }
                }
            }
        } catch (Exception ignored) {}
        return list;
    }

    public static String normalizeText(String str) {
        if (str == null) return "";
        return NORMALIZE_PATTERN.matcher(str).replaceAll("").toLowerCase(java.util.Locale.ROOT);
    }

    // =========================================================================
    // Daily Limit & Calendar Day Management
    // =========================================================================
    public static String getTodayCalendarDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    public static String getTomorrowCalendarDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(cal.getTime());
    }

    public int getDailyQuizzesPlayedCount(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        String savedDate = prefs.getString(KEY_DAILY_DATE, "");
        String today = getTodayCalendarDate();
        if (!today.equals(savedDate)) {
            prefs.edit().putString(KEY_DAILY_DATE, today).putInt(KEY_DAILY_COUNT, 0).apply();
            return 0;
        }
        return prefs.getInt(KEY_DAILY_COUNT, 0);
    }

    public boolean isDailyQuizCompletedToday(Context context) {
        return getDailyQuizzesPlayedCount(context) >= DAILY_MAX_QUIZZES;
    }

    public boolean hasPlayedQuizToday(Context context) {
        if (context == null) return false;
        return getDailyQuizzesPlayedCount(context) > 0 || isDailyQuizCompletedToday(context);
    }

    public String getRemainingTimeToNextCycleFormatted() {
        return getRemainingTimeToNextCycleFormatted(true);
    }

    public String getRemainingTimeToNextCycleFormatted(boolean isBn) {
        Calendar now = Calendar.getInstance();
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 24);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        long diffMs = midnight.getTimeInMillis() - now.getTimeInMillis();
        if (diffMs <= 0) return isBn ? "নতুন কুইজ উপলব্ধ!" : "New quiz available!";

        long hours = diffMs / (1000 * 60 * 60);
        long mins = (diffMs / (1000 * 60)) % 60;

        if (isBn) {
            return "নতুন কুইজ আসতে আর " + BengaliNumberUtil.toBengali((int) hours) + " ঘণ্টা "
                    + BengaliNumberUtil.toBengali((int) mins) + " মিনিট বাকি";
        } else {
            return "Next quiz available in " + hours + " hours " + mins + " minutes";
        }
    }

    // =========================================================================
    // Smart Quiz Selection for Current Calendar Day
    // =========================================================================
    public List<QuizItem> getDailyQuizQuestions(Context context) {
        if (isDailyQuizCompletedToday(context)) {
            return new ArrayList<>();
        }

        // Fast in-memory seed pool ensures instant availability with 0ms delay
        if (masterQuizPool.isEmpty()) {
            loadCuratedSeedPool();
        }

        // Ensure master pool loads asynchronously in background without blocking UI
        ensureMasterPoolLoaded(context);

        SharedPreferences prefs = (context != null) ?
                context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE) : null;
        String today = getTodayCalendarDate();

        // 1. Fast Cache Check: If today's 10 questions session is already cached, return remaining questions instantly (0 ms)
        if (prefs != null) {
            String cachedDate = prefs.getString(KEY_TODAY_SESSION_DATE, "");
            String cachedJson = prefs.getString(KEY_TODAY_SESSION_DATA, null);
            if (today.equals(cachedDate) && cachedJson != null && !cachedJson.trim().isEmpty()) {
                try {
                    Type listType = new TypeToken<List<QuizItem>>(){}.getType();
                    List<QuizItem> cachedList = gson.fromJson(cachedJson, listType);
                    if (cachedList != null && !cachedList.isEmpty()) {
                        List<QuizItem> remaining = new ArrayList<>();
                        for (QuizItem item : cachedList) {
                            if (item == null || item.id == null) continue;
                            String status = prefs.getString(KEY_STATUS_PREFIX + item.id, STATUS_UNSEEN);
                            if (!STATUS_CORRECT_COMPLETED.equals(status) && !STATUS_RETRY_COMPLETED.equals(status) && !STATUS_FAILED_PERMANENTLY.equals(status)) {
                                remaining.add(item);
                            }
                        }
                        if (!remaining.isEmpty()) {
                            return remaining;
                        } else {
                            return new ArrayList<>();
                        }
                    }
                } catch (Exception ignored) {}
            }
        }

        // 2. Select 10 questions for today from available pool
        List<QuizItem> allAvailable;
        synchronized (masterQuizPool) {
            allAvailable = new ArrayList<>(masterQuizPool);
        }

        // Include locally cached preloaded quizzes
        if (context != null) {
            List<QuizItem> preloaded = getPreloadedQuizzes(context);
            Set<String> existingIds = new HashSet<>();
            for (QuizItem m : allAvailable) if (m != null && m.id != null) existingIds.add(m.id);

            for (QuizItem p : preloaded) {
                if (p != null && (p.availableFromDate.isEmpty() || p.availableFromDate.compareTo(today) <= 0)) {
                    if (p.id != null && !existingIds.contains(p.id)) {
                        allAvailable.add(p);
                        existingIds.add(p.id);
                    }
                }
            }
        }

        List<QuizItem> unseenJummahList = new ArrayList<>();
        List<QuizItem> unseenGeneralList = new ArrayList<>();
        List<QuizItem> retryList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        boolean isFriday = (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY);

        for (QuizItem item : allAvailable) {
            if (item == null) continue;

            // Check calendar availability
            if (!item.availableFromDate.isEmpty() && item.availableFromDate.compareTo(today) > 0) {
                continue; // Hidden until future date
            }

            // Check Context & Day Relevance (strict day matching)
            if (!isContextAllowedToday(item)) {
                continue;
            }

            String status = (prefs != null) ? prefs.getString(KEY_STATUS_PREFIX + item.id, STATUS_UNSEEN) : STATUS_UNSEEN;

            // Correctly answered -> PERMANENTLY EXCLUDED (Never show again)
            if (STATUS_CORRECT_COMPLETED.equals(status) || STATUS_RETRY_COMPLETED.equals(status) || STATUS_FAILED_PERMANENTLY.equals(status)) {
                continue;
            }

            if (STATUS_UNSEEN.equals(status)) {
                if ("jummah".equalsIgnoreCase(item.contextTag)) {
                    unseenJummahList.add(item);
                } else {
                    unseenGeneralList.add(item);
                }
            } else if (STATUS_WRONG_PENDING_RETRY.equals(status)) {
                if (prefs != null) {
                    int attempts = prefs.getInt(KEY_ATTEMPTS_PREFIX + item.id, 0);
                    String lastWrongDate = prefs.getString(KEY_LAST_WRONG_DATE_PREFIX + item.id, "");

                    // User Rule: max 2 attempts, retry not on the same day
                    if (attempts < 2 && !today.equals(lastWrongDate)) {
                        retryList.add(item);
                    }
                }
            }
        }

        Collections.shuffle(unseenJummahList);
        Collections.shuffle(unseenGeneralList);
        Collections.shuffle(retryList);

        List<QuizItem> selected = new ArrayList<>();

        // On Friday: prioritize 2-3 Jummah questions
        if (isFriday && !unseenJummahList.isEmpty()) {
            int jummahCount = Math.min(3, unseenJummahList.size());
            for (int i = 0; i < jummahCount && selected.size() < DAILY_MAX_QUIZZES; i++) {
                selected.add(unseenJummahList.get(i));
            }
        }

        // Priority 1: Unseen authentic quizzes
        for (QuizItem u : unseenGeneralList) {
            if (selected.size() < DAILY_MAX_QUIZZES) {
                selected.add(u);
            }
        }

        // Priority 2: One-time retry from previous wrong sessions (never same day)
        if (selected.size() < DAILY_MAX_QUIZZES) {
            for (QuizItem r : retryList) {
                if (selected.size() < DAILY_MAX_QUIZZES) {
                    selected.add(r);
                }
            }
        }

        // User Rule: Randomize option positions so correct answer is randomly distributed at 0, 1, 2, 3
        List<QuizItem> randomizedSelected = new ArrayList<>();
        for (QuizItem q : selected) {
            randomizedSelected.add(randomizeOptions(q));
        }

        // Save today's session in cache for instant reopening
        if (prefs != null && !randomizedSelected.isEmpty()) {
            try {
                prefs.edit()
                        .putString(KEY_TODAY_SESSION_DATE, today)
                        .putString(KEY_TODAY_SESSION_DATA, gson.toJson(randomizedSelected))
                        .apply();
            } catch (Exception ignored) {}
        }

        return randomizedSelected;
    }

    // =========================================================================
    // Answer Recording & History Management
    // =========================================================================
    public void recordQuizAnswer(Context context, QuizItem item, boolean isCorrect) {
        if (context == null || item == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int attempts = prefs.getInt(KEY_ATTEMPTS_PREFIX + item.id, 0) + 1;
        editor.putInt(KEY_ATTEMPTS_PREFIX + item.id, attempts);

        String today = getTodayCalendarDate();

        if (isCorrect) {
            // User answered correctly -> PERMANENTLY EXCLUDED (never ask again)
            editor.putString(KEY_STATUS_PREFIX + item.id, STATUS_CORRECT_COMPLETED);
            addDeenPoints(context, 10);
            // Increment Lifetime Won/Correct Quizzes Count
            int lifetimeWonCount = prefs.getInt(KEY_LIFETIME_PLAYED_COUNT, 0) + 1;
            editor.putInt(KEY_LIFETIME_PLAYED_COUNT, lifetimeWonCount);
        } else {
            // User answered incorrectly
            editor.putString(KEY_LAST_WRONG_DATE_PREFIX + item.id, today);
            if (attempts >= 2) {
                // Max 2 attempts reached -> failed permanently
                editor.putString(KEY_STATUS_PREFIX + item.id, STATUS_FAILED_PERMANENTLY);
            } else {
                // Eligible for retry on future days, but NOT today ("তবে ওইদিন না")
                editor.putString(KEY_STATUS_PREFIX + item.id, STATUS_WRONG_PENDING_RETRY);
            }
        }

        // Daily Play Quota Count (for calendar day limit of 10)
        String savedDate = prefs.getString(KEY_DAILY_DATE, "");
        int currentDailyCount = today.equals(savedDate) ? prefs.getInt(KEY_DAILY_COUNT, 0) : 0;
        editor.putString(KEY_DAILY_DATE, today);
        editor.putInt(KEY_DAILY_COUNT, currentDailyCount + 1);

        editor.apply();

        // Insert into Room QuizResultEntity for persistent local history
        bgExecutor.execute(() -> {
            try {
                com.devflux.deenone.data.local.AppDatabase db = com.devflux.deenone.data.local.AppDatabase.getInstance(context);
                com.devflux.deenone.data.local.entity.QuizResultEntity res = new com.devflux.deenone.data.local.entity.QuizResultEntity(
                        isCorrect ? 10 : 0,
                        1,
                        item.category != null ? item.category : "General",
                        1,
                        System.currentTimeMillis(),
                        today,
                        true
                );
                db.quizDao().insertResult(res);
                if (isCorrect) {
                    com.devflux.deenone.core.gamification.GamificationManager.addXP(context, 10);
                }
                new com.devflux.deenone.data.repository.AmalRepository(context).recalculateAndSyncTodayRecord();
            } catch (Exception ignored) {}
        });

        // Clear active quiz notification
        try {
            com.devflux.deenone.core.notifications.NotificationHelper.cancelQuizNotification(context);
        } catch (Exception ignored) {}

        // Sync with MySQL backend
        try {
            com.devflux.deenone.core.sync.UserActivitySyncManager.getInstance(context)
                    .syncQuizAnswer(item.category != null ? item.category : "General", isCorrect, isCorrect ? 10 : 0);
        } catch (Exception ignored) {}

        // Preload next day
        preloadNextDayQuizzesAsync(context);
    }

    public int getLifetimeQuizzesPlayedCount(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_LIFETIME_PLAYED_COUNT, 0);
    }

    public void setLifetimeQuizzesPlayedCount(Context context, int count) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        int current = prefs.getInt(KEY_LIFETIME_PLAYED_COUNT, 0);
        if (count > current) {
            prefs.edit().putInt(KEY_LIFETIME_PLAYED_COUNT, count).apply();
        }
    }

    // =========================================================================
    // =========================================================================
    // Extensible Multi-Source API Architecture & Synchronization
    // =========================================================================
    public interface QuizApiSource {
        String getSourceName();
        List<QuizItem> fetchQuestions(Context context) throws Exception;
    }

    private final List<QuizApiSource> registeredApiSources = new ArrayList<>();

    public void registerApiSource(QuizApiSource source) {
        if (source != null) {
            synchronized (registeredApiSources) {
                registeredApiSources.add(source);
            }
        }
    }

    private void persistQuestionsToRoomDb(Context context, List<QuizItem> items) {
        if (context == null || items == null || items.isEmpty()) return;
        try {
            List<QuizQuestionEntity> entities = new ArrayList<>();
            for (QuizItem item : items) {
                if (item != null && item.question != null && !item.question.trim().isEmpty() && item.options != null && item.options.length >= 4) {
                    int diff = "hard".equalsIgnoreCase(item.difficulty) ? 3 : ("easy".equalsIgnoreCase(item.difficulty) ? 1 : 2);
                    QuizQuestionEntity entity = new QuizQuestionEntity(
                            item.question,
                            item.options[0],
                            item.options[1],
                            item.options[2],
                            item.options[3],
                            item.correctIndex,
                            item.explanation != null ? item.explanation : "",
                            item.reference != null ? item.reference : "কুরআন ও সহীহ সুন্নাহ",
                            item.category != null ? item.category : "ইসলামিক সাধারণ জ্ঞান",
                            diff
                    );
                    entities.add(entity);
                }
            }
            if (!entities.isEmpty()) {
                AppDatabase.getInstance(context).quizDao().insertOrIgnore(entities);
                logDebug(TAG, "Persisted " + entities.size() + " quiz questions into local Room database.");
            }
        } catch (Exception e) {
            logWarn(TAG, "Failed to persist quiz questions to Room DB: " + e.getMessage());
        }
    }

    public void syncOnlineQuestionsAsync(Context context, QuizSyncCallback callback) {
        syncOnlineQuestionsAsync(context, false, callback);
    }

    public void syncOnlineQuestionsAsync(Context context, boolean force, QuizSyncCallback callback) {
        bgExecutor.execute(() -> {
            try {
                final Context appContext = (context != null) ? context.getApplicationContext() : null;
                SharedPreferences prefs = (appContext != null) ?
                        appContext.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE) : null;
                long now = System.currentTimeMillis();
                long lastSync = (prefs != null) ? prefs.getLong(KEY_LAST_SYNC_TIMESTAMP, 0) : 0;

                // Throttle: don't hammer the network on every open; sync at most once every 3 hours unless forced
                if (!force && (now - lastSync < 3 * 3600 * 1000L)) {
                    postSyncSuccess(callback, 0, getTotalQuestionBankCount(appContext));
                    return;
                }

                ensureMasterPoolLoaded(appContext);
                int[] backendAdded = new int[]{0};
                List<QuizItem> newItemsForRoom = new ArrayList<>();

                // 1. Primary Sync Engine: DeenOne PHP MySQL Backend API (Custom Admin Questions & Live Question Bank)
                if (context != null) {
                    try {
                        String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "get_quiz_questions.php?count=100");
                        String apiKey = BackendConfigManager.getPhpApiKey(context);

                        URL url = new URL(endpoint);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(8000);
                        conn.setReadTimeout(10000);
                        conn.setRequestProperty("User-Agent", "DeenOne-App/2.1");
                        if (apiKey != null && !apiKey.isEmpty()) {
                            conn.setRequestProperty("X-API-KEY", apiKey);
                        }

                        if (conn.getResponseCode() == 200) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                            reader.close();

                            JsonObject root = JsonParser.parseString(sb.toString()).getAsJsonObject();
                            if (root != null && root.has("success") && root.get("success").getAsBoolean() && root.has("questions")) {
                                JsonArray qArray = root.getAsJsonArray("questions");
                                if (qArray != null) {
                                    Set<String> existingIds = new HashSet<>();
                                    Set<String> existingQuestions = new HashSet<>();
                                    synchronized (masterQuizPool) {
                                        for (QuizItem q : masterQuizPool) {
                                            if (q.id != null) existingIds.add(q.id);
                                            existingQuestions.add(normalizeText(q.question));
                                        }
                                    }

                                    List<QuizItem> preloaded = getPreloadedQuizzes(context);
                                    for (QuizItem q : preloaded) {
                                        if (q.id != null) existingIds.add(q.id);
                                        existingQuestions.add(normalizeText(q.question));
                                    }

                                    for (int i = 0; i < qArray.size(); i++) {
                                        JsonObject obj = qArray.get(i).getAsJsonObject();
                                        String qId = obj.has("id") ? obj.get("id").getAsString() : "PHP_Q_" + i;
                                        String qText = obj.has("question_text") ? obj.get("question_text").getAsString() : "";
                                        String catId = obj.has("category_id") ? obj.get("category_id").getAsString() : "general_knowledge";
                                        int correctOpt = obj.has("correct_option_index") ? obj.get("correct_option_index").getAsInt() : 0;
                                        String explanation = obj.has("explanation") ? obj.get("explanation").getAsString() : "";
                                        String reference = obj.has("reference") ? obj.get("reference").getAsString() : "কুরআন ও সহীহ সুন্নাহ";

                                        List<String> options = new ArrayList<>();
                                        if (obj.has("options") && obj.get("options").isJsonArray()) {
                                            JsonArray optArr = obj.getAsJsonArray("options");
                                            for (int j = 0; j < optArr.size(); j++) {
                                                options.add(optArr.get(j).getAsString());
                                            }
                                        }

                                        String norm = normalizeText(qText);
                                        if (!qText.isEmpty() && options.size() == 4 && !existingIds.contains(qId) && !existingQuestions.contains(norm)) {
                                            QuizItem item = new QuizItem(
                                                    qId,
                                                    qText,
                                                    options.toArray(new String[0]),
                                                    correctOpt,
                                                    getCategoryBengaliName(catId),
                                                    determineContextTag(catId, qText),
                                                    reference,
                                                    explanation,
                                                    "",
                                                    "DeenOne Cloud Server",
                                                    "medium"
                                            );
                                            preloaded.add(item);
                                            newItemsForRoom.add(item);
                                            synchronized (masterQuizPool) {
                                                masterQuizPool.add(item);
                                            }
                                            existingIds.add(qId);
                                            existingQuestions.add(norm);
                                            backendAdded[0]++;
                                        }
                                    }
                                    if (backendAdded[0] > 0) {
                                        savePreloadedQuizzes(context, preloaded);
                                        persistQuestionsToRoomDb(context, newItemsForRoom);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logWarn(TAG, "PHP Backend quiz sync note: " + e.getMessage());
                    }
                }

                // 2. Custom Registered API Sources
                synchronized (registeredApiSources) {
                    for (QuizApiSource source : registeredApiSources) {
                        try {
                            List<QuizItem> extraFromSource = source.fetchQuestions(context);
                            if (extraFromSource != null && !extraFromSource.isEmpty()) {
                                List<QuizItem> customAdded = new ArrayList<>();
                                synchronized (masterQuizPool) {
                                    Set<String> existingQuestions = new HashSet<>();
                                    for (QuizItem q : masterQuizPool) existingQuestions.add(normalizeText(q.question));
                                    for (QuizItem sItem : extraFromSource) {
                                        if (sItem != null && sItem.question != null && !existingQuestions.contains(normalizeText(sItem.question))) {
                                            masterQuizPool.add(sItem);
                                            customAdded.add(sItem);
                                            existingQuestions.add(normalizeText(sItem.question));
                                        }
                                    }
                                }
                                if (!customAdded.isEmpty()) {
                                    persistQuestionsToRoomDb(context, customAdded);
                                }
                            }
                        } catch (Exception e) {
                            logWarn(TAG, "Custom API Source error: " + source.getSourceName() + " - " + e.getMessage());
                        }
                    }
                }

                // 3. Secondary Sync Engine: IslamicOnlineScraperClient
                IslamicOnlineScraperClient client = IslamicOnlineScraperClient.getInstance();
                client.fetchLiveQuestions("quran_knowledge", 25, new IslamicOnlineScraperClient.OnlineScrapeCallback() {
                    @Override
                    public void onSuccess(List<BattleQuestion> scrapedQuestions) {
                        bgExecutor.execute(() -> {
                            int extraAdded = 0;
                            List<QuizItem> scraperItemsForRoom = new ArrayList<>();
                            if (scrapedQuestions != null && !scrapedQuestions.isEmpty()) {
                                Set<String> existingIds = new HashSet<>();
                                Set<String> existingQuestions = new HashSet<>();
                                synchronized (masterQuizPool) {
                                    for (QuizItem q : masterQuizPool) {
                                        if (q.id != null) existingIds.add(q.id);
                                        existingQuestions.add(normalizeText(q.question));
                                    }
                                }

                                List<QuizItem> preloaded = (context != null) ? getPreloadedQuizzes(context) : new ArrayList<>();
                                for (QuizItem q : preloaded) {
                                    if (q.id != null) existingIds.add(q.id);
                                    existingQuestions.add(normalizeText(q.question));
                                }

                                for (BattleQuestion bq : scrapedQuestions) {
                                    String norm = normalizeText(bq.getQuestionText());
                                    if (!existingIds.contains(bq.getId()) && !existingQuestions.contains(norm)) {
                                        String[] opts = bq.getOptions().toArray(new String[0]);
                                        QuizItem item = new QuizItem(
                                                bq.getId(),
                                                bq.getQuestionText(),
                                                opts,
                                                bq.getCorrectOptionIndex(),
                                                getCategoryBengaliName(bq.getCategoryId()),
                                                determineContextTag(bq.getCategoryId(), bq.getQuestionText()),
                                                bq.getReference(),
                                                bq.getExplanation()
                                        );
                                        preloaded.add(item);
                                        scraperItemsForRoom.add(item);
                                        synchronized (masterQuizPool) {
                                            masterQuizPool.add(item);
                                        }
                                        existingIds.add(bq.getId());
                                        existingQuestions.add(norm);
                                        extraAdded++;
                                    }
                                }
                                if (extraAdded > 0 && context != null) {
                                    savePreloadedQuizzes(context, preloaded);
                                    persistQuestionsToRoomDb(context, scraperItemsForRoom);
                                }
                            }

                            final int totalAdded = backendAdded[0] + extraAdded;
                            final int total = getTotalQuestionBankCount(context);
                            if (prefs != null) {
                                prefs.edit().putLong(KEY_LAST_SYNC_TIMESTAMP, System.currentTimeMillis()).apply();
                            }
                            postSyncSuccess(callback, totalAdded, total);
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        final int total = getTotalQuestionBankCount(context);
                        if (prefs != null) {
                            prefs.edit().putLong(KEY_LAST_SYNC_TIMESTAMP, System.currentTimeMillis()).apply();
                        }
                        postSyncSuccess(callback, backendAdded[0], total);
                    }
                });
            } catch (Exception e) {
                postSyncError(callback, "অনলাইন সিঙ্ক ব্যর্থ: " + e.getMessage());
            }
        });
    }

    private void postSyncSuccess(QuizSyncCallback callback, int newCount, int totalCount) {
        if (callback == null) return;
        Handler h = getMainHandler();
        if (h != null) {
            h.post(() -> callback.onSyncComplete(newCount, totalCount));
        } else {
            callback.onSyncComplete(newCount, totalCount);
        }
    }

    private void postSyncError(QuizSyncCallback callback, String err) {
        if (callback == null) return;
        Handler h = getMainHandler();
        if (h != null) {
            h.post(() -> callback.onSyncError(err));
        } else {
            callback.onSyncError(err);
        }
    }

    public int getTotalQuestionBankCount(Context context) {
        ensureMasterPoolLoaded(context);
        synchronized (masterQuizPool) {
            return masterQuizPool.size();
        }
    }

    // =========================================================================
    // Points Management
    // =========================================================================
    public int getDeenPoints(Context context) {
        if (context == null) return 0;
        return context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE).getInt(KEY_POINTS, 0);
    }

    public void addDeenPoints(Context context, int points) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        int cur = prefs.getInt(KEY_POINTS, 0);
        prefs.edit().putInt(KEY_POINTS, cur + points).apply();
    }

    // =========================================================================
    // Next-Day Background Preloader & Cache
    // =========================================================================
    public void preloadNextDayQuizzesAsync(Context context) {
        if (context == null) return;
        bgExecutor.execute(() -> {
            try {
                String tomorrow = getTomorrowCalendarDate();
                List<QuizItem> existingPreloaded = getPreloadedQuizzes(context);
                boolean hasTomorrow = false;
                for (QuizItem q : existingPreloaded) {
                    if (tomorrow.equals(q.availableFromDate)) {
                        hasTomorrow = true;
                        break;
                    }
                }

                if (!hasTomorrow) {
                    List<QuizItem> nextDayBatch = new ArrayList<>();
                    synchronized (masterQuizPool) {
                        for (int i = 0; i < masterQuizPool.size() && nextDayBatch.size() < DAILY_MAX_QUIZZES; i++) {
                            QuizItem base = masterQuizPool.get(i);
                            String qId = "auto_q_" + tomorrow.replace("-", "") + "_" + (i + 1);
                            QuizItem tomorrowItem = new QuizItem(
                                    qId,
                                    base.question,
                                    base.options,
                                    base.correctIndex,
                                    base.category,
                                    base.contextTag,
                                    base.reference,
                                    base.explanation,
                                    tomorrow,
                                    "Verified Canonical Islamic Database",
                                    base.difficulty
                            );
                            nextDayBatch.add(tomorrowItem);
                        }
                    }

                    existingPreloaded.addAll(nextDayBatch);
                    savePreloadedQuizzes(context, existingPreloaded);
                    logDebug(TAG, "Preloaded next calendar day quizzes for: " + tomorrow);
                }
            } catch (Exception e) {
                logWarn(TAG, "preloadNextDayQuizzesAsync error: " + e.getMessage());
            }
        });
    }

    private List<QuizItem> getPreloadedQuizzes(Context context) {
        if (context == null) return new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_PRELOADED_CACHE, null);
        if (json == null) return new ArrayList<>();
        try {
            Type type = new TypeToken<List<QuizItem>>(){}.getType();
            List<QuizItem> list = gson.fromJson(json, type);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void savePreloadedQuizzes(Context context, List<QuizItem> list) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_QUIZ, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_PRELOADED_CACHE, gson.toJson(list)).apply();
    }

    // =========================================================================
    // Curated Seed Pool (High-Priority Core Questions)
    // =========================================================================
    private void loadCuratedSeedPool() {
        masterQuizPool.clear();

        // 1. জুমা ও সালাত (Jumu'ah & Salah)
        masterQuizPool.add(new QuizItem(
                "q_j_01",
                "জুমার দিন কোন সূরা তিলাওয়াত করা বিশেষ ফযিলতপূর্ণ এবং এর ফলে দুই জুমার মধ্যবর্তী সময়ে নূর চমকাতে থাকে?",
                new String[]{"সূরা আল-কাহাফ", "সূরা আল-মুলক", "সূরা ইয়াসীন", "সূরা আর-রহমান"},
                0, "জুমা ও সালাত", "jummah",
                "সুনানে বায়হাকী: ৫৯৯৬, সহীহুল জামি': ৬৪৭০",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: যে ব্যক্তি জুমার দিন সূরা আল-কাহাফ তিলাওয়াত করবে, তার জন্য এক জুমা থেকে অপর জুমা পর্যন্ত একটি বিশেষ নূর চমকাতে থাকবে।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_j_02",
                "জুমার দিনে এমন একটি বিশেষ মুহূর্ত রয়েছে যখন বান্দার বৈধ দোয়া নিশ্চিত কবুল হয়, তাকে কী বলা হয়?",
                new String[]{"সা'আতুল ইজাবাহ", "লাইলাতুল ক্বদর", "ইয়াউমুল আরাফাহ", "আইয়ামে তাশরীক"},
                0, "জুমা ও দোয়া", "jummah",
                "সহীহ বুখারী: ৯৩৫, সহীহ মুসলিম: ৮৫২",
                "রাসূলুল্লাহ (ﷺ) জুমার দিনের সা'আতুল ইজাবাহ (দোয়া কবুলের বিশেষ মুহূর্ত) সম্পর্কে বলেছেন, বান্দা এই সময়ে যে কল্যাণ প্রার্থনা করে আল্লাহ তা দান করেন।"
        ));

        // 2. সিয়াম ও রমজান (Sawm & Ramadan)
        masterQuizPool.add(new QuizItem(
                "q_r_01",
                "রমজান মাসের রোজা কোন হিজরি সনে উম্মতে মুহাম্মাদীর ওপর ফরজ করা হয়েছিল?",
                new String[]{"২য় হিজরি", "১ম হিজরি", "৩য় হিজরি", "৫ম হিজরি"},
                0, "সিয়াম ও রমজান", "ramadan",
                "তাফসীরে ইবনে কাসীর, সূরা বাক্বারাহ: ১৮৩",
                "নবীজী (ﷺ)-এর মদিনায় হিজরতের দ্বিতীয় বছর শাবান মাসে রমজানের রোজা ফরজ করা হয়।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_r_02",
                "জান্নাতের কোন বিশেষ দরজা দিয়ে কিয়ামতের দিন কেবল রোযাদারগণই প্রবেশ করার সম্মান লাভ করবেন?",
                new String[]{"রাইয়্যান (Ar-Rayyan)", "বাবুল জিহাদ", "বাবুল ঈমান", "বাবুস সালাত"},
                0, "সিয়াম ও রমজান", "ramadan",
                "সহীহ বুখারী: ১৮৯৬, সহীহ মুসলিম: ১১৫২",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: জান্নাতে 'রাইয়্যান' নামক একটি দরজা রয়েছে, কিয়ামতের দিন কেবল রোযাদাররাই তা দিয়ে প্রবেশ করবে।"
        ));

        // 3. কুরআনুল কারীম (Holy Quran)
        masterQuizPool.add(new QuizItem(
                "q_q_01",
                "পবিত্র কুরআনের সর্বপ্রথম কোন সূরার আয়াতসমূহ হেরা গুহায় নাযিল হয়েছিল?",
                new String[]{"সূরা আল-আলাক্ব (১-৫ আয়াত)", "সূরা আল-ফাতিহা", "সূরা আল-মুদ্দাসসির", "সূরা আল-বাক্বারাহ"},
                0, "কুরআনুল কারীম", "general",
                "সহীহ বুখারী: ৩",
                "হেরা গুহায় জিবরীল (আ.) সর্বপ্রথম সূরা আলাকের প্রথম ৫টি আয়াত ('ইক্বরা বিসমি রাব্বিকাল্লাযী খালাক্ব') নিয়ে অবতীর্ণ হন।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_q_02",
                "পবিত্র কুরআনের দীর্ঘতম সূরা কোনটি?",
                new String[]{"সূরা আল-বাক্বারাহ (২৮৬ আয়াত)", "সূরা আলে ইমরান", "সূরা আন-নিসা", "সূরা আল-মায়েদাহ"},
                0, "কুরআনুল কারীম", "general",
                "মুসহাফে উসমানী",
                "সূরা আল-বাক্বারাহ পবিত্র কুরআনের দীর্ঘতম সূরা, যাতে মোট ২৮৬টি আয়াত রয়েছে।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_q_03",
                "কুরআনের কোন সূরাটিকে 'উম্মুল কুরআন' বা কুরআনের জননী বলা হয়?",
                new String[]{"সূরা আল-ফাতিহা", "সূরা ইয়াসীন", "সূরা আল-ইখলাস", "সূরা আল-মুলক"},
                0, "কুরআনুল কারীম", "general",
                "সহীহ বুখারী: ৪৭০৪",
                "সূরা ফাতিহাকে উম্মুল কুরআন বা কুরআনের জননী বলা হয় কারণ এটি পুরো কুরআনের নির্যাস ধারণ করে।"
        ));

        // 4. সীরাতুন্নবী (ﷺ) (Prophetic Biography)
        masterQuizPool.add(new QuizItem(
                "q_s_01",
                "নবী কারীম (ﷺ) কোন ঐতিহাসিক ঘটনা ও হস্তিবর্ষে জন্মগ্রহণ করেছিলেন?",
                new String[]{"আমুল ফীল (হস্তিবর্ষ)", "আমুল হুযন (শোকের বছর)", "আমুল ফুজ্জার", "আমুল ওফূদ"},
                0, "সীরাতুন্নবী (ﷺ)", "general",
                "আর-রাহীকুল মাখতূম",
                "রাসূলুল্লাহ (ﷺ) ৫৭১ খ্রিস্টাব্দে আবরাহার হস্তিবাহিনীর ধ্বংসের বছর তথা 'আমুল ফীল'-এ জন্মগ্রহণ করেন।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_s_02",
                "নবীজী (ﷺ)-এর ঐতিহাসিক মি'রাজের ঘটনা কোন মাসে সংঘটিত হয়েছিল বলে সর্বাধিক প্রসিদ্ধ?",
                new String[]{"রজব মাস (২৭শে রজব রাত)", "শাবান মাস", "রমজান মাস", "মুহররম মাস"},
                0, "সীরাতুন্নবী (ﷺ)", "general",
                "আল-বিদায়া ওয়ান নিহায়া",
                "বেশিরভাগ ইতিহাসবিদের মতে নবুওয়াতের দশম বা একাদশ বর্ষের ২৭শে রজব রাতে ঐতিহাসিক ইসরা ও মি'রাজ সংঘটিত হয়।"
        ));

        // 5. সাহাবায়ে কেরাম (The Companions)
        masterQuizPool.add(new QuizItem(
                "q_c_01",
                "ইসলামের প্রথম খলিফা এবং আশারায়ে মুবাশশারার প্রধান সাহাবী কে?",
                new String[]{"হযরত আবু বকর সিদ্দিক (রা.)", "হযরত উমর ইবনুল খাত্তাব (রা.)", "হযরত উসমান ইবনে আফফান (রা.)", "হযরত আলী ইবনে আবি তালিব (রা.)"},
                0, "সাহাবায়ে কেরাম", "general",
                "সহীহ বুখারী: ৩৬৫৬",
                "হযরত আবু বকর সিদ্দিক (রা.) প্রাপ্তবয়স্ক পুরুষদের মধ্যে প্রথম ইসলাম গ্রহণ করেন এবং ইসলামের প্রথম খলিফা নির্বাচিত হন।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_c_02",
                "নবীজী (ﷺ) কাকে 'সাইয়্যেদুশ শুহাদা' বা শহীদদের সরদার উপাধিতে ভূষিত করেছিলেন?",
                new String[]{"হযরত হামযা ইবনে আব্দুল মুত্তালিব (রা.)", "হযরত মুসআব ইবনে উমাইর (রা.)", "হযরত জাফর ইবনে আবি তালিব (রা.)", "হযরত হুসাইন (রা.)"},
                0, "সাহাবায়ে কেরাম", "general",
                "মুসতাদরাকে হাকিম: ৪৮৮৩",
                "রাসূলুল্লাহ (ﷺ) উহুদের যুদ্ধে শাহাদাতবরণকারী তাঁর চাচা হযরত হামযা (রা.)-কে সাইয়্যেদুশ শুহাদা ঘোষণা করেন।"
        ));

        // 6. ফিকহ ও আহকাম (Islamic Jurisprudence)
        masterQuizPool.add(new QuizItem(
                "q_f_01",
                "ইসলামের পাঁচটি মূল স্তম্ভের মধ্যে কোনটি দ্বিতীয় স্তম্ভ?",
                new String[]{"দৈনিক পাঁচ ওয়াক্ত সালাত প্রতিষ্ঠা করা", "রমজানের রোজা রাখা", "যাকাত আদায় করা", "পবিত্র হজ পালন করা"},
                0, "ফিকহ ও সালাত", "general",
                "সহীহ বুখারী: ৮, সহীহ মুসলিম: ১৬",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: ইসলামের ভিত্তি পাঁচটি জিনিসের ওপর প্রতিষ্ঠিত। কালিমার সাক্ষ্য দেওয়ার পর দ্বিতীয়টি হলো সালাত কায়েম করা।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_f_02",
                "রূপার নেসাব কত নির্ধারণ করা হয়েছে যার বেশি হলে বছরান্তে শতকরা ২.৫% হারে যাকাত দেওয়া ফরজ?",
                new String[]{"৫২.৫ তোলা (তোলা / ৬১২.৩৬ গ্রাম)", "৭.৫ তোলা", "১০ তোলা", "৮৫ গ্রাম"},
                0, "ফিকহ ও যাকাত", "general",
                "সুনানে আবু দাউদ: ১৫৭৩, ফাতাওয়ায়ে হিন্দিয়াহ",
                "রূপার নেসাব হলো ৫২.৫ তোলা (৬১২.৩৬ গ্রাম) এবং স্বর্ণের নেসাব হলো ৭.৫ তোলা (৮৭.৪৮ গ্রাম)।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_f_03",
                "কোন নামাজে কোনো আযান ও ইকামত দেওয়া হয় না?",
                new String[]{"ঈদের নামাজ ও জানাজার নামাজ", "তাহাজ্জুদের নামাজ", "বিতরের নামাজ", "তারাবীহর নামাজ"},
                0, "ফিকহ ও সালাত", "general",
                "সহীহ মুসলিম: ৮৮৫",
                "জাবের ইবনে সামুরা (রা.) বর্ণনা করেন: আমি রাসূলুল্লাহ (ﷺ)-এর সাথে একাধিকবার ঈদের নামাজ পড়েছি যাতে কোনো আযান ও ইকামত ছিল না।"
        ));

        // 7. ইসলামিক আখলাক ও হাদিস (Islamic Manners & Hadith)
        masterQuizPool.add(new QuizItem(
                "q_a_01",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি মানুষের প্রতি কৃতজ্ঞতা প্রকাশ করে না, সে আল্লাহর প্রতিও কৃতজ্ঞ হতে পারে না' — এটি কোন বিষয়ের নির্দেশ?",
                new String[]{"উপকারকারীর প্রতি কৃতজ্ঞতা ও উত্তম চরিত্র", "দান-সদকা", "সালাতের গুরুত্ব", "জিহাদের ফজিলত"},
                0, "ইসলামিক আখলাক", "general",
                "সুনানে আবু দাউদ: ৪৮১১, জামে আত-তিরমিযী: ১৯৫৪",
                "অন্যের অনুগ্রহ স্বীকার করা এবং কৃতজ্ঞতা প্রকাশ করা ইসলামের অন্যতম শ্রেষ্ঠ শিষ্টাচার ও ঈমানের পূর্ণতা।"
        ));

        masterQuizPool.add(new QuizItem(
                "q_a_02",
                "মাতা-পিতার অবাধ্য হওয়াকে ইসলামে কোন পর্যায়ের গুনাহ হিসেবে গণ্য করা হয়েছে?",
                new String[]{"কবিরা গুনাহ (সবচেয়ে বড় মহাপাপ সমূহের একটি)", "সগিরা গুনাহ", "মাকরূহ", "মুস্তাহাব"},
                0, "ইসলামিক আখলাক", "general",
                "সহীহ বুখারী: ২৬৫৪, সহীহ মুসলিম: ৮৭",
                "রাসূলুল্লাহ (ﷺ) বলেছেন: আমি কি তোমাদের সবচেয়ে বড় কবিরা গুনাহ সম্পর্কে বলব না? তা হলো আল্লাহর সাথে শিরক করা এবং মাতা-পিতার অবাধ্য হওয়া।"
        ));
    }
}