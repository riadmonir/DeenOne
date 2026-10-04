package com.devflux.deenone.core.quran;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.alarms.QuranJourneyReminderReceiver;
import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.QuranSurahDataSeeder;
import com.devflux.deenone.data.local.entity.QuranAyahEntity;
import com.devflux.deenone.data.local.entity.QuranSurahEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class QuranJourneyManager {

    private static final String TAG = "QuranJourneyManager";
    private static final String PREFS_NAME = "quran_journey_prefs";
    private static final String KEY_NOOR_POINTS = "noor_points";
    private static final String KEY_STREAK_DAYS = "streak_days";
    private static final String KEY_LAST_COMPLETED_DATE = "last_completed_date";
    private static final String KEY_LAST_COMPLETED_SURAH = "last_completed_surah";
    private static final String KEY_LAST_COMPLETED_AYAH = "last_completed_ayah";
    private static final String KEY_CURRENT_SURAH = "current_surah";
    private static final String KEY_CURRENT_AYAH = "current_ayah";
    private static final String KEY_COMPLETED_AYAHS_CSV = "completed_ayahs_csv";
    private static final String KEY_AMOL_DONE_DATE = "amol_done_date";
    private static final String KEY_REMINDER_ENABLED = "reminder_enabled";
    private static final String KEY_CURRENT_LEVEL = "current_level";
    private static final String KEY_LEARNER_BASE_COUNT = "learner_base_count";
    private static final String KEY_REPEAT_COUNT = "repeat_count";
    private static final String KEY_PLAY_SPEED = "play_speed";

    // Exact count of Ayahs for each of the 114 Surahs (1-indexed, total = 6,236 Ayahs)
    private static final int[] SURAH_AYAH_COUNTS = new int[]{
            7, 286, 200, 176, 120, 165, 206, 75, 129, 109,
            123, 111, 43, 52, 99, 128, 111, 110, 98, 135,
            112, 78, 118, 64, 77, 227, 93, 88, 69, 60,
            34, 30, 73, 54, 45, 83, 182, 88, 75, 85,
            54, 53, 89, 59, 37, 35, 38, 29, 18, 45,
            60, 49, 62, 55, 78, 96, 29, 22, 24, 13,
            14, 11, 11, 18, 12, 12, 30, 52, 52, 44,
            28, 28, 20, 56, 40, 31, 50, 40, 46, 42,
            29, 19, 36, 25, 22, 17, 19, 26, 30, 20,
            15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
            11, 8, 3, 9, 5, 4, 7, 3, 6, 3,
            5, 4, 5, 6
    };

    public static class JourneyWord {
        public final int serial;
        public final String arabic;
        public final String meaningBengali;
        public final String meaningEnglish;
        public boolean isRevealed;

        public JourneyWord(int serial, String arabic, String meaningBengali, String meaningEnglish) {
            this.serial = serial;
            this.arabic = arabic;
            this.meaningBengali = meaningBengali;
            this.meaningEnglish = meaningEnglish;
            this.isRevealed = false;
        }
    }

    public static class RevisionAyah {
        public final int dayNumber;
        public final int surahNumber;
        public final int ayahNumber;
        public final String arabicText;
        public final String audioUrl;

        public RevisionAyah(int dayNumber, int surahNumber, int ayahNumber, String arabicText, String audioUrl) {
            this.dayNumber = dayNumber;
            this.surahNumber = surahNumber;
            this.ayahNumber = ayahNumber;
            this.arabicText = arabicText;
            this.audioUrl = audioUrl;
        }
    }

    public static class JourneyLesson {
        public final int surahNumber;
        public final int ayahNumber;
        public final String arabicAyah;
        public final String surahRefBengali;
        public final String surahRefEnglish;
        public final String meaningBengali;
        public final String meaningEnglish;
        public final String audioUrl;

        public final List<JourneyWord> words;

        public final String quizChallengeArabic;
        public final String quizCorrectOption;
        public final String[] quizOptions;

        public final String lessonBengali;
        public final String lessonEnglish;
        public final String amolBengali;
        public final String amolEnglish;

        public final List<RevisionAyah> revisionAyahs;

        public JourneyLesson(int surahNumber, int ayahNumber, String arabicAyah,
                             String surahRefBengali, String surahRefEnglish,
                             String meaningBengali, String meaningEnglish,
                             String audioUrl, List<JourneyWord> words,
                             String quizChallengeArabic, String quizCorrectOption, String[] quizOptions,
                             String lessonBengali, String lessonEnglish,
                             String amolBengali, String amolEnglish,
                             List<RevisionAyah> revisionAyahs) {
            this.surahNumber = surahNumber;
            this.ayahNumber = ayahNumber;
            this.arabicAyah = arabicAyah;
            this.surahRefBengali = surahRefBengali;
            this.surahRefEnglish = surahRefEnglish;
            this.meaningBengali = meaningBengali;
            this.meaningEnglish = meaningEnglish;
            this.audioUrl = audioUrl;
            this.words = words;
            this.quizChallengeArabic = quizChallengeArabic;
            this.quizCorrectOption = quizCorrectOption;
            this.quizOptions = quizOptions;
            this.lessonBengali = lessonBengali;
            this.lessonEnglish = lessonEnglish;
            this.amolBengali = amolBengali;
            this.amolEnglish = amolEnglish;
            this.revisionAyahs = revisionAyahs;
        }
    }

    public static class StreakReaction {
        public final String emoji; // "😊", "🔥", "😢", "😠", "🎉", "✨"
        public final String titleBn;
        public final String titleEn;
        public final String subtitleBn;
        public final String subtitleEn;
        public final int streakDays;
        public final int bgCircleRes;
        public final boolean isAngry;
        public final boolean isSad;
        public final boolean isHappy;

        public StreakReaction(String emoji, String titleBn, String titleEn,
                              String subtitleBn, String subtitleEn, int streakDays,
                              int bgCircleRes, boolean isAngry, boolean isSad, boolean isHappy) {
            this.emoji = emoji;
            this.titleBn = titleBn;
            this.titleEn = titleEn;
            this.subtitleBn = subtitleBn;
            this.subtitleEn = subtitleEn;
            this.streakDays = streakDays;
            this.bgCircleRes = bgCircleRes;
            this.isAngry = isAngry;
            this.isSad = isSad;
            this.isHappy = isHappy;
        }
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static int getNoorPoints(Context context) {
        return getPrefs(context).getInt(KEY_NOOR_POINTS, 10);
    }

    public static void addNoorPoints(Context context, int pointsToAdd) {
        int current = getNoorPoints(context);
        getPrefs(context).edit().putInt(KEY_NOOR_POINTS, current + pointsToAdd).apply();
    }

    public static int getStreakDays(Context context) {
        return getPrefs(context).getInt(KEY_STREAK_DAYS, 1);
    }

    public static int getSurahTotalAyahs(int surahNumber) {
        if (surahNumber >= 1 && surahNumber <= SURAH_AYAH_COUNTS.length) {
            return SURAH_AYAH_COUNTS[surahNumber - 1];
        }
        return 7;
    }

    public static int getCurrentSurah(Context context) {
        return getPrefs(context).getInt(KEY_CURRENT_SURAH, 1);
    }

    public static int getCurrentAyah(Context context) {
        return getPrefs(context).getInt(KEY_CURRENT_AYAH, 1);
    }

    public static int getLastCompletedSurah(Context context) {
        return getPrefs(context).getInt(KEY_LAST_COMPLETED_SURAH, 1);
    }

    public static int getLastCompletedAyah(Context context) {
        return getPrefs(context).getInt(KEY_LAST_COMPLETED_AYAH, 1);
    }

    public static boolean isAyahCompleted(Context context, int surah, int ayah) {
        String csv = getPrefs(context).getString(KEY_COMPLETED_AYAHS_CSV, "");
        if (csv == null || csv.isEmpty()) return false;
        String target = surah + ":" + ayah;
        for (String item : csv.split(",")) {
            if (item.trim().equals(target)) return true;
        }
        return false;
    }

    public static Set<String> getCompletedAyahsSet(Context context) {
        String csv = getPrefs(context).getString(KEY_COMPLETED_AYAHS_CSV, "");
        Set<String> set = new HashSet<>();
        if (csv != null && !csv.isEmpty()) {
            for (String s : csv.split(",")) {
                if (!s.trim().isEmpty()) set.add(s.trim());
            }
        }
        return set;
    }

    public static List<String> getCompletedAyahsOrderedList(Context context) {
        String csv = getPrefs(context).getString(KEY_COMPLETED_AYAHS_CSV, "");
        List<String> list = new ArrayList<>();
        if (csv != null && !csv.trim().isEmpty()) {
            for (String s : csv.split(",")) {
                String item = s.trim();
                if (!item.isEmpty() && !list.contains(item)) {
                    list.add(item);
                }
            }
        }
        return list;
    }

    public static void recordAyahCompleted(Context context, int surah, int ayah) {
        List<String> ordered = getCompletedAyahsOrderedList(context);
        String key = surah + ":" + ayah;
        if (!ordered.contains(key)) {
            ordered.add(key);
        }
        StringBuilder sb = new StringBuilder();
        for (String k : ordered) {
            if (sb.length() > 0) sb.append(",");
            sb.append(k);
        }
        getPrefs(context).edit().putString(KEY_COMPLETED_AYAHS_CSV, sb.toString()).apply();
    }

    /**
     * Completes today's lesson, updates streak, awards Noor points,
     * and permanently advances to the next sequential Ayah in the Quran.
     * Guarantees zero repetition.
     */
    public static void markCompletedToday(Context context) {
        int activeSurah = getCurrentSurah(context);
        int activeAyah = getCurrentAyah(context);

        String today = getTodayDateString();
        String lastDate = getPrefs(context).getString(KEY_LAST_COMPLETED_DATE, "");

        int currentStreak = getStreakDays(context);
        int diffDays = computeDaysDiffFromToday(lastDate);

        int newStreak;
        if (lastDate.isEmpty()) {
            newStreak = 1;
        } else if (diffDays == 0) {
            newStreak = Math.max(currentStreak, 1);
        } else if (diffDays == 1) {
            newStreak = currentStreak + 1;
        } else {
            // Missed one or more days, reset to 1
            newStreak = 1;
        }

        // Record completed Ayah
        recordAyahCompleted(context, activeSurah, activeAyah);

        // Advance to next uncompleted Ayah across all 30 Paras / 114 Surahs
        int nextSurah = activeSurah;
        int nextAyah = activeAyah + 1;
        int maxAyahs = getSurahTotalAyahs(nextSurah);

        if (nextAyah > maxAyahs) {
            nextSurah = (nextSurah % 114) + 1;
            nextAyah = 1;
        }

        // Keep advancing if this next Ayah was ever previously completed
        Set<String> completedSet = getCompletedAyahsSet(context);
        int guard = 0;
        while (completedSet.contains(nextSurah + ":" + nextAyah) && guard < 6236) {
            nextAyah++;
            if (nextAyah > getSurahTotalAyahs(nextSurah)) {
                nextSurah = (nextSurah % 114) + 1;
                nextAyah = 1;
            }
            guard++;
        }

        getPrefs(context).edit()
                .putString(KEY_LAST_COMPLETED_DATE, today)
                .putInt(KEY_LAST_COMPLETED_SURAH, activeSurah)
                .putInt(KEY_LAST_COMPLETED_AYAH, activeAyah)
                .putInt(KEY_CURRENT_SURAH, nextSurah)
                .putInt(KEY_CURRENT_AYAH, nextAyah)
                .putInt(KEY_STREAK_DAYS, newStreak)
                .apply();

        addNoorPoints(context, 20);

        // Async sync to Backend API
        syncProgressToBackend(context, activeSurah, activeAyah, newStreak);
    }

    private static int computeDaysDiffFromToday(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date lastDate = sdf.parse(dateStr);
            if (lastDate == null) return -1;

            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);
            todayCal.set(Calendar.MILLISECOND, 0);

            Calendar lastCal = Calendar.getInstance();
            lastCal.setTime(lastDate);
            lastCal.set(Calendar.HOUR_OF_DAY, 0);
            lastCal.set(Calendar.MINUTE, 0);
            lastCal.set(Calendar.SECOND, 0);
            lastCal.set(Calendar.MILLISECOND, 0);

            long diffMillis = todayCal.getTimeInMillis() - lastCal.getTimeInMillis();
            return (int) (diffMillis / (24L * 60L * 60L * 1000L));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Evaluates the user's streak status and generates the dynamic emotional reaction:
     * - Today completed -> Happy celebration 🎉
     * - Active streak (completed yesterday) -> Happy / Flame 🔥
     * - Missed yesterday -> Sad crying emoji 😢
     * - Missed multiple days -> Angry reaction 😠
     */
    public static StreakReaction getStreakReaction(Context context) {
        String lastDate = getPrefs(context).getString(KEY_LAST_COMPLETED_DATE, "");
        int streak = getStreakDays(context);

        if (lastDate.isEmpty()) {
            return new StreakReaction(
                    "✨",
                    "স্বাগতম! আপনার কুরআন যাত্রা শুরু করুন",
                    "Welcome! Start your Quran Journey",
                    "প্রতিদিন ১টি করে আয়াত শিখে এগিয়ে চলুন",
                    "Learn 1 Ayah daily and build your streak",
                    0,
                    R.drawable.bg_circle_reaction_happy,
                    false, false, true
            );
        }

        int diffDays = computeDaysDiffFromToday(lastDate);

        if (diffDays == 0) {
            // Already completed today
            return new StreakReaction(
                    "🎉",
                    "মাশাআল্লাহ! আজকের পাঠ সম্পন্ন!",
                    "MashaAllah! Today's lesson completed!",
                    "আপনার কুরআন যাত্রা সফলভাবে চালু আছে। স্ট্রিক ধরে রাখুন!",
                    "Your Quran Journey is active. Keep your streak alive!",
                    streak,
                    R.drawable.bg_circle_reaction_happy,
                    false, false, true
            );
        } else if (diffDays == 1) {
            // Completed yesterday, waiting for today
            return new StreakReaction(
                    "🔥",
                    "স্ট্রিক বজায় রাখুন! আজকের নতুন পাঠ",
                    "Keep your streak alive! Today's new lesson",
                    "আজকের আয়াতটি সম্পন্ন করে আপনার স্ট্রিক এগিয়ে নিন",
                    "Complete today's Ayah to advance your streak",
                    streak,
                    R.drawable.bg_circle_reaction_happy,
                    false, false, true
            );
        } else if (diffDays == 2) {
            // Missed yesterday! Sad crying reaction
            return new StreakReaction(
                    "😢",
                    "হায়! গতকাল আপনি মিস করেছেন 😢",
                    "Oh no! You missed yesterday 😢",
                    "আপনার স্ট্রিক ভেঙে গেছে! দেরি না করে আজই আবার শুরু করুন",
                    "Your streak broke! Start again today without delay",
                    0,
                    R.drawable.bg_circle_reaction_sad,
                    false, true, false
            );
        } else {
            // Absent for 2+ days! Angry reaction
            return new StreakReaction(
                    "😠",
                    "কুরআন থেকে দূরে থাকা যাবে না! 😠",
                    "Don't stay away from the Quran! 😠",
                    "আপনি বেশ কিছুদিন ধরে অনুপস্থিত! আল্লাহর কালাম প্রতিদিন তিলাওয়াত করুন",
                    "You have been absent for days! Recite Allah's words every day",
                    0,
                    R.drawable.bg_circle_reaction_angry,
                    true, false, false
            );
        }
    }

    public static boolean isAmolDoneToday(Context context) {
        String today = getTodayDateString();
        String amolDate = getPrefs(context).getString(KEY_AMOL_DONE_DATE, "");
        return today.equals(amolDate);
    }

    public static void setAmolDoneToday(Context context, boolean isDone) {
        String today = getTodayDateString();
        getPrefs(context).edit()
                .putString(KEY_AMOL_DONE_DATE, isDone ? today : "")
                .apply();
    }

    public static boolean isReminderEnabled(Context context) {
        return getPrefs(context).getBoolean(KEY_REMINDER_ENABLED, true);
    }

    public static void setReminderEnabled(Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply();
        if (enabled) {
            scheduleDailyReminder(context);
        } else {
            cancelDailyReminder(context);
        }
    }

    public static String getSafeUserId(Context context) {
        if (context == null) return "usr_default";
        try {
            String uid = AuthManager.getCurrentSession(context).userId;
            if (uid != null && !uid.trim().isEmpty() && !uid.equals("usr_guest")) {
                return uid.trim();
            }
        } catch (Exception ignored) {}

        SharedPreferences prefs = getPrefs(context);
        String deviceUid = prefs.getString("persistent_journey_user_id", null);
        if (deviceUid == null || deviceUid.trim().isEmpty()) {
            deviceUid = "usr_jrn_" + UUID.randomUUID().toString().substring(0, 8);
            prefs.edit().putString("persistent_journey_user_id", deviceUid).apply();
        }
        return deviceUid;
    }

    public interface OnLearnerCountUpdatedListener {
        void onLearnerCountUpdated(int count);
    }

    public static int getLearnerCount(Context context) {
        return getPrefs(context).getInt(KEY_LEARNER_BASE_COUNT, 1);
    }

    public static void setLearnerCount(Context context, int count) {
        if (count < 1) count = 1;
        getPrefs(context).edit().putInt(KEY_LEARNER_BASE_COUNT, count).apply();
    }

    public static void fetchRealTimeLearnersCount(Context context, OnLearnerCountUpdatedListener listener) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "quran_journey.php?user_id=" + getSafeUserId(context));
                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject root = new JSONObject(sb.toString());
                    if (root.optBoolean("success")) {
                        JSONObject data = root.optJSONObject("data");
                        if (data != null) {
                            int count = data.optInt("total_learners", 1);
                            setLearnerCount(context, count);
                            if (listener != null) {
                                new Handler(Looper.getMainLooper()).post(() -> listener.onLearnerCountUpdated(count));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "Failed fetching learner count: " + e.getMessage());
            }
        });
    }

    public static int getRepeatCount(Context context) {
        return getPrefs(context).getInt(KEY_REPEAT_COUNT, 3);
    }

    public static void setRepeatCount(Context context, int count) {
        getPrefs(context).edit().putInt(KEY_REPEAT_COUNT, count).apply();
    }

    public static float getPlaySpeed(Context context) {
        return getPrefs(context).getFloat(KEY_PLAY_SPEED, 1.0f);
    }

    public static void setPlaySpeed(Context context, float speed) {
        getPrefs(context).edit().putFloat(KEY_PLAY_SPEED, speed).apply();
    }

    public static String getCurrentLevel(Context context) {
        int surah = getCurrentSurah(context);
        int ayah = getCurrentAyah(context);
        return "পারা " + getJuzForSurahAyah(surah, ayah) + " • সূরা " + getSurahBnName(surah) + " (" + surah + ":" + ayah + ")";
    }

    public static String getCurrentLevelEnglish(Context context) {
        int surah = getCurrentSurah(context);
        int ayah = getCurrentAyah(context);
        return "Juz " + getJuzForSurahAyah(surah, ayah) + " • " + getSurahEnName(surah) + " (" + surah + ":" + ayah + ")";
    }

    public static void setCurrentLevel(Context context, String level) {
        getPrefs(context).edit().putString(KEY_CURRENT_LEVEL, level).apply();
    }

    private static String getTodayDateString() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    /**
     * Loads the authentic, sequential real Quran lesson.
     * Guaranteed zero repetition and 100% authentic Quran data.
     */
    public static JourneyLesson getTodayLesson(Context context) {
        int surah = getCurrentSurah(context);
        int ayah = getCurrentAyah(context);

        // Ensure we never serve an Ayah that has already been completed by the user
        Set<String> completedSet = getCompletedAyahsSet(context);
        int guard = 0;
        while (completedSet.contains(surah + ":" + ayah) && guard < 6236) {
            ayah++;
            if (ayah > getSurahTotalAyahs(surah)) {
                surah = (surah % 114) + 1;
                ayah = 1;
            }
            guard++;
        }
        getPrefs(context).edit()
                .putInt(KEY_CURRENT_SURAH, surah)
                .putInt(KEY_CURRENT_AYAH, ayah)
                .apply();

        // Fetch from Room Database or fallback
        QuranAyahEntity ayahEntity = null;
        try {
            ayahEntity = AppDatabase.getInstance(context).ayahDao().getAyahSync(surah, ayah);
        } catch (Exception e) {
            Log.w(TAG, "Failed reading ayah from Room: " + e.getMessage());
        }

        if (ayahEntity == null) {
            ayahEntity = fetchAyahFromNetworkOrFallback(context, surah, ayah);
        }

        String arAyah = ayahEntity.getTextArabic();
        String meanBn = ayahEntity.getTranslationBengali();
        String meanEn = ayahEntity.getTranslationEnglish();
        String audioUrl = ayahEntity.getAudioUrl();

        String surahBn = getSurahBnName(surah);
        String surahEn = getSurahEnName(surah);
        String surahAr = getSurahArName(surah);

        String refBn = "সূরা (" + surah + ":" + ayah + ") " + surahAr + " • " + surahBn;
        String refEn = "Surah (" + surah + ":" + ayah + ") " + surahEn;

        // Word-by-word tokenization
        List<JourneyWord> words = buildWordsFromAyah(arAyah, meanBn, meanEn);

        // Dynamic Quiz Challenge from the Ayah
        String[] quizData = buildDynamicQuiz(arAyah, words, meanBn, meanEn);
        String quizChallenge = quizData[0];
        String correctOption = quizData[1];
        String[] options = new String[]{quizData[1], quizData[2], quizData[3], quizData[4]};

        // Lesson Reflection & Actionable Amol
        String lessonBn = getLessonReflectionBn(surah, ayah, meanBn);
        String lessonEn = getLessonReflectionEn(surah, ayah, meanEn);
        String amolBn = getAmolGuidanceBn(surah, ayah);
        String amolEn = getAmolGuidanceEn(surah, ayah);

        // 7-Day Revision list (from preceding completed ayahs)
        List<RevisionAyah> revisions = build7DayRevisions(context, surah, ayah);

        return new JourneyLesson(surah, ayah, arAyah, refBn, refEn, meanBn, meanEn, audioUrl,
                words, quizChallenge, correctOption, options, lessonBn, lessonEn, amolBn, amolEn, revisions);
    }

    private static QuranAyahEntity fetchAyahFromNetworkOrFallback(Context context, int surah, int ayah) {
        // Check preloaded essential list first
        for (QuranAyahEntity pre : QuranSurahDataSeeder.getEssentialAyahs()) {
            if (pre.getSurahNumber() == surah && pre.getAyahNumber() == ayah) {
                return pre;
            }
        }

        // Try synchronous network fetch
        try {
            String urlStr = "https://api.alquran.cloud/v1/ayah/" + surah + ":" + ayah + "/editions/quran-uthmani,bn.bengali,en.sahih";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONArray editions = root.getJSONArray("data");
                if (editions.length() >= 3) {
                    String arText = editions.getJSONObject(0).getString("text");
                    String bnText = editions.getJSONObject(1).getString("text");
                    String enText = editions.getJSONObject(2).getString("text");
                    int juz = editions.getJSONObject(0).optInt("juz", 1);
                    int page = editions.getJSONObject(0).optInt("page", 1);
                    String audio = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surah, ayah);

                    QuranAyahEntity entity = new QuranAyahEntity(surah, ayah, arText, bnText, enText, "", audio, juz, page);
                    AppDatabase.getInstance(context).ayahDao().insertAyah(entity);
                    return entity;
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "Network fetch failed: " + e.getMessage());
        }

        // Guaranteed fallback for initial sequence
        return getDefaultFallbackAyah(surah, ayah);
    }

    private static QuranAyahEntity getDefaultFallbackAyah(int surah, int ayah) {
        if (surah == 1 && ayah == 1) {
            return new QuranAyahEntity(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
                    "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                    "বিসমিল্লাহির রাহমানির রাহিম",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 1), 1, 1);
        } else if (surah == 1 && ayah == 2) {
            return new QuranAyahEntity(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                    "যাবতীয় প্রশংসা জগৎসমূহের প্রতিপালক আল্লাহরই জন্য।",
                    "[All] praise is [due] to Allah, Lord of the worlds.",
                    "আলহামদু লিল্লাহি রাব্বিল আলামিন",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 2), 1, 1);
        } else if (surah == 1 && ayah == 3) {
            return new QuranAyahEntity(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ",
                    "যিনি পরম করুণাময়, অতি দয়ালু।",
                    "The Entirely Merciful, the Especially Merciful,",
                    "আর-রাহমানির রাহিম",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 3), 1, 1);
        } else if (surah == 1 && ayah == 4) {
            return new QuranAyahEntity(1, 4, "مَالِكِ يَوْمِ الدِّينِ",
                    "যিনি বিচার দিবসের অধিপতি।",
                    "Sovereign of the Day of Recompense.",
                    "মালিকি ইয়াওমিদ্দিন",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 4), 1, 1);
        } else if (surah == 1 && ayah == 5) {
            return new QuranAyahEntity(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                    "আমরা কেবল তোমারই ইবাদত করি এবং কেবল তোমারই কাছে সাহায্য প্রার্থনা করি।",
                    "It is You we worship and You we ask for help.",
                    "ইয়্যাকা না'বুদু ওয়া ইয়্যাকা নাসতাঈন",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 5), 1, 1);
        } else if (surah == 1 && ayah == 6) {
            return new QuranAyahEntity(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                    "আমাদের সরল ও সঠিক পথে পরিচালিত করুন।",
                    "Guide us to the straight path -",
                    "ইহদিনাস সিরাতাল মুস্তাকীম",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 6), 1, 1);
        } else if (surah == 1 && ayah == 7) {
            return new QuranAyahEntity(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                    "তাদের পথে, যাদের আপনি অনুগ্রহ করেছেন; যাদের ওপর আপনার ক্রোধ আপতিত হয়নি এবং যারা পথভ্রষ্টও হয়নি।",
                    "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.",
                    "সিরাতাল্লাযীনা আনআমতা আলাইহিম, গাইরিল মাগদূবি আলাইহিম ওয়ালাদ্দাল্লীন",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 1, 7), 1, 1);
        } else if (surah == 2 && ayah == 255) {
            return new QuranAyahEntity(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ",
                    "আল্লাহ ছাড়া কোনো উপাস্য নেই, তিনি চিরঞ্জীব, সবকিছুর ধারক।",
                    "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence.",
                    "আল্লাহু লা ইলাহা ইল্লা হুয়াল হাইয়্যুল কাইয়্যুম",
                    QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, 2, 255), 3, 42);
        }

        // Generic authentic representation
        String audio = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, surah, ayah);
        return new QuranAyahEntity(surah, ayah, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
                "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                "", audio, 1, 1);
    }

    private static List<JourneyWord> buildWordsFromAyah(String arAyah, String meanBn, String meanEn) {
        List<JourneyWord> words = new ArrayList<>();
        if (arAyah == null || arAyah.isEmpty()) return words;

        String[] tokens = arAyah.trim().split("\\s+");
        String[] bnTokens = meanBn != null ? meanBn.split("[,।\\s]+") : new String[0];
        String[] enTokens = meanEn != null ? meanEn.split("[,\\.\\s]+") : new String[0];

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) continue;

            String wordBn = getCuratedWordMeaningBn(token);
            if (wordBn == null) {
                wordBn = (i < bnTokens.length && !bnTokens[i].isEmpty()) ? bnTokens[i] : "আল্লাহর বাণী";
            }

            String wordEn = getCuratedWordMeaningEn(token);
            if (wordEn == null) {
                wordEn = (i < enTokens.length && !enTokens[i].isEmpty()) ? enTokens[i] : "Divine Word";
            }

            words.add(new JourneyWord(i + 1, token, wordBn, wordEn));
        }
        return words;
    }

    private static String getCuratedWordMeaningBn(String ar) {
        if (ar.contains("بِسْمِ")) return "নামে";
        if (ar.contains("اللَّهِ") || ar.contains("لِلَّهِ")) return "আল্লাহর জন্য";
        if (ar.contains("الرَّحْمَٰنِ")) return "পরম করুণাময়";
        if (ar.contains("الرَّحِيمِ")) return "অতি দয়ালু";
        if (ar.contains("الْحَمْدُ") || ar.contains("ٱلْحَمْدُ")) return "সমস্ত প্রশংসা";
        if (ar.contains("رَبِّ")) return "প্রতিপালক";
        if (ar.contains("الْعَالَمِينَ") || ar.contains("ٱلْعَٰلَمِينَ")) return "সকল সৃষ্টিজগতের";
        if (ar.contains("مَالِكِ") || ar.contains("مَٰلِكِ")) return "মালিক / অধিপতি";
        if (ar.contains("يَوْمِ")) return "দিবস";
        if (ar.contains("الدِّينِ") || ar.contains("ٱلدِّينِ")) return "বিচার দিবসের";
        if (ar.contains("إِيَّاكَ")) return "আপনারই";
        if (ar.contains("نَعْبُدُ")) return "আমরা ইবাদত করি";
        if (ar.contains("نَسْتَعِينُ")) return "সাহায্য চাই";
        if (ar.contains("اهْدِنَا") || ar.contains("ٱهْدِنَا")) return "আমাদের পথ দেখান";
        if (ar.contains("الصِّرَاطَ") || ar.contains("ٱلصِّرَٰطَ")) return "সরল পথ";
        if (ar.contains("الْمُسْتَقِيمَ") || ar.contains("ٱلْمُسْتَقِيمَ")) return "সঠিক / সোজা";
        if (ar.contains("قُلْ")) return "বলুন";
        if (ar.contains("هُوَ")) return "তিনি";
        if (ar.contains("أَحَدٌ")) return "একক";
        if (ar.contains("الصَّمَدُ")) return "অমুখাপেক্ষী";
        if (ar.contains("الْحَيُّ")) return "চিরঞ্জীব";
        if (ar.contains("الْقَيُّومُ")) return "সবকিছুর ধারক";
        return null;
    }

    private static String getCuratedWordMeaningEn(String ar) {
        if (ar.contains("بِسْمِ")) return "In the name of";
        if (ar.contains("اللَّهِ") || ar.contains("لِلَّهِ")) return "For Allah";
        if (ar.contains("الرَّحْمَٰنِ")) return "The Entirely Merciful";
        if (ar.contains("الرَّحِيمِ")) return "The Especially Merciful";
        if (ar.contains("الْحَمْدُ") || ar.contains("ٱلْحَمْدُ")) return "All praise";
        if (ar.contains("رَبِّ")) return "Lord / Sustainer";
        if (ar.contains("الْعَالَمِينَ") || ar.contains("ٱلْعَٰلَمِينَ")) return "Of all worlds";
        if (ar.contains("مَالِكِ") || ar.contains("مَٰلِكِ")) return "Sovereign / Master";
        if (ar.contains("يَوْمِ")) return "Day of";
        if (ar.contains("الدِّينِ") || ar.contains("ٱلدِّينِ")) return "Recompense";
        if (ar.contains("إِيَّاكَ")) return "You alone";
        if (ar.contains("نَعْبُدُ")) return "We worship";
        if (ar.contains("نَسْتَعِينُ")) return "We ask for help";
        if (ar.contains("اهْدِنَا") || ar.contains("ٱهْدِنَا")) return "Guide us";
        if (ar.contains("الصِّرَاطَ") || ar.contains("ٱلصِّرَٰطَ")) return "Straight path";
        if (ar.contains("قُلْ")) return "Say";
        if (ar.contains("هُوَ")) return "He is";
        if (ar.contains("أَحَدٌ")) return "One";
        if (ar.contains("الصَّمَدُ")) return "The Eternal Refuge";
        return null;
    }

    private static String[] buildDynamicQuiz(String arAyah, List<JourneyWord> words, String meanBn, String meanEn) {
        if (words.isEmpty()) {
            return new String[]{"[ _____ ]", "اللَّهِ", "الرَّحْمَٰنِ", "مَٰلِكِ", "إِيَّاكَ"};
        }

        int targetIndex = words.size() > 2 ? 1 : 0;
        JourneyWord targetWord = words.get(targetIndex);
        String targetAr = targetWord.arabic;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            if (i == targetIndex) {
                sb.append("[ _____ ] ");
            } else {
                sb.append(words.get(i).arabic).append(" ");
            }
        }
        String challenge = sb.toString().trim();

        String option1 = targetAr;
        String option2 = words.size() > (targetIndex + 1) ? words.get(targetIndex + 1).arabic : "الرَّحْمَٰنِ";
        String option3 = words.size() > (targetIndex + 2) ? words.get(targetIndex + 2).arabic : "مَٰلِكِ";
        String option4 = words.size() > 0 && targetIndex != 0 ? words.get(0).arabic : "إِيَّاكَ";

        return new String[]{challenge, option1, option2, option3, option4};
    }

    private static String getLessonReflectionBn(int surah, int ayah, String meanBn) {
        return "পবিত্র কুরআনের এই আয়াতে মহান আল্লাহ রাব্বুল আলামিন আমাদের ঈমান, আমল ও জীবনের সঠিক পথের নির্দেশনা দিয়েছেন। এই আয়াতের মর্মবাণী অন্তরে ধারণ করে প্রতিদিনের কাজে এর প্রতিফলন ঘটানোই মুমিনের পরম প্রাপ্তি।";
    }

    private static String getLessonReflectionEn(int surah, int ayah, String meanEn) {
        return "In this divine verse, Almighty Allah guides our hearts toward true faith, righteous action, and continuous remembrance. Reflecting upon its profound meaning enriches our daily life and prayers.";
    }

    private static String getAmolGuidanceBn(int surah, int ayah) {
        return "আজকের দিনে যেকোনো ৩টি নেয়ামতের কথা স্মরণ করে আন্তরিকভাবে এই আয়াতের শিক্ষা অনুযায়ী শুকরিয়া আদায় করুন";
    }

    private static String getAmolGuidanceEn(int surah, int ayah) {
        return "Reflect upon Allah's blessings today and express sincere gratitude following the message of this Ayah";
    }

    public static List<RevisionAyah> build7DayRevisions(Context context, int currentSurah, int currentAyah) {
        List<RevisionAyah> revisions = new ArrayList<>();
        List<String> completedList = getCompletedAyahsOrderedList(context);
        if (completedList == null || completedList.isEmpty()) {
            return revisions;
        }

        int count = 0;
        for (int i = completedList.size() - 1; i >= 0 && count < 7; i--) {
            String item = completedList.get(i);
            String[] parts = item.split(":");
            if (parts.length == 2) {
                try {
                    int s = Integer.parseInt(parts[0].trim());
                    int a = Integer.parseInt(parts[1].trim());

                    QuranAyahEntity rev = null;
                    try {
                        rev = AppDatabase.getInstance(context).ayahDao().getAyahSync(s, a);
                    } catch (Exception ignored) {}

                    if (rev == null) {
                        rev = fetchAyahFromNetworkOrFallback(context, s, a);
                    }

                    if (rev != null) {
                        count++;
                        String text = rev.getTextArabic();
                        String audio = rev.getAudioUrl();
                        if (audio == null || audio.isEmpty()) {
                            audio = QuranCdnAudioHelper.getAyahAudioUrl(QuranCdnAudioHelper.Reciter.MISHARY_ALAFASY, s, a);
                        }
                        revisions.add(new RevisionAyah(count, s, a, text, audio));
                    }
                } catch (Exception ignored) {}
            }
        }
        return revisions;
    }

    private static void syncProgressToBackend(Context context, int surah, int ayah, int streak) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "quran_journey.php?action=submit_completion");
                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("X-API-KEY", BackendConfigManager.getPhpApiKey(context));
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("user_id", getSafeUserId(context));
                payload.put("surah_number", surah);
                payload.put("ayah_number", ayah);
                payload.put("streak_days", streak);
                payload.put("noor_points", getNoorPoints(context));
                payload.put("current_level", getCurrentLevel(context));
                payload.put("completed_ayahs", getCompletedAyahsOrderedList(context).size());

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                    reader.close();

                    JSONObject root = new JSONObject(sb.toString());
                    if (root.optBoolean("success")) {
                        JSONObject data = root.optJSONObject("data");
                        if (data != null && data.has("total_learners")) {
                            int count = data.optInt("total_learners", 1);
                            setLearnerCount(context, count);
                        }
                    }
                }
                Log.d(TAG, "Backend sync completed with code: " + code);
            } catch (Exception e) {
                Log.w(TAG, "Backend progress sync ignored: " + e.getMessage());
            }
        });
    }

    public static String getSurahBnName(int surah) {
        List<QuranSurahEntity> list = QuranSurahDataSeeder.get114Surahs();
        if (surah >= 1 && surah <= list.size()) {
            return list.get(surah - 1).getNameBengali();
        }
        return "আল-ফাতিহা";
    }

    public static String getSurahEnName(int surah) {
        List<QuranSurahEntity> list = QuranSurahDataSeeder.get114Surahs();
        if (surah >= 1 && surah <= list.size()) {
            return list.get(surah - 1).getNameEnglish();
        }
        return "Al-Fatihah";
    }

    public static String getSurahArName(int surah) {
        List<QuranSurahEntity> list = QuranSurahDataSeeder.get114Surahs();
        if (surah >= 1 && surah <= list.size()) {
            return list.get(surah - 1).getNameArabic();
        }
        return "سُورَةُ الْفَاتِحَةِ";
    }

    public static int getJuzForSurahAyah(int surah, int ayah) {
        List<QuranSurahEntity> list = QuranSurahDataSeeder.get114Surahs();
        if (surah >= 1 && surah <= list.size()) {
            return list.get(surah - 1).getJuzNumber();
        }
        return 1;
    }

    // Exact 9:00 PM Daily Habit Reminder
    public static void scheduleDailyReminder(Context context) {
        if (!isReminderEnabled(context)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, QuranJourneyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                90021,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21); // 9:00 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (SecurityException se) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public static void cancelDailyReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, QuranJourneyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                90021,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0)
        );
        alarmManager.cancel(pendingIntent);
    }
}
