package com.devflux.deenone.core.quran;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.utils.BengaliNumberUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * High-Performance Quran Daily Goal and Streak Management Engine.
 * Supports:
 *  - 4 Goal Types: AYAH, PAGE, TIME (minutes), SURAH.
 *  - Real-time ayah read and listened tracking.
 *  - Dynamic streak states with emotions:
 *      * ACTIVE_FIRE (🔥 Goal met / active consecutive streak)
 *      * PENDING_SAD (😢 Goal pending or 1 day missed)
 *      * MISSED_ANGRY (😡 2+ consecutive days missed)
 */
public class QuranStreakGoalManager {

    private static final String PREF_NAME = "deanone_quran_streak_goal_prefs";

    private static final String KEY_GOAL_TYPE = "key_goal_type";
    private static final String KEY_GOAL_TARGET = "key_goal_target";
    private static final String KEY_TODAY_DATE = "key_today_date";

    private static final String KEY_TODAY_AYAHS_SET = "key_today_ayahs_set";
    private static final String KEY_TODAY_MINUTES = "key_today_minutes";
    private static final String KEY_TODAY_SECONDS_ACCUMULATOR = "key_today_seconds_acc";
    private static final String KEY_TODAY_PAGES_SET = "key_today_pages_set";
    private static final String KEY_TODAY_SURAHS_SET = "key_today_surahs_set";

    private static final String KEY_STREAK_COUNT = "key_streak_count";
    private static final String KEY_LAST_COMPLETED_DATE = "key_last_completed_date";

    public enum GoalType {
        AYAH("ayah", 5),
        PAGE("page", 1),
        TIME("time", 10),
        SURAH("surah", 1);

        public final String id;
        public final int defaultTarget;

        GoalType(String id, int defaultTarget) {
            this.id = id;
            this.defaultTarget = defaultTarget;
        }

        public static GoalType fromId(String id) {
            for (GoalType type : values()) {
                if (type.id.equalsIgnoreCase(id)) return type;
            }
            return AYAH;
        }
    }

    public enum StreakState {
        ACTIVE_FIRE,   // 🔥 Goal completed today, consecutive streak alive
        PENDING_SAD,   // 😢 Today's goal not yet completed or 1 day missed
        MISSED_ANGRY   // 😡 2 or more consecutive days missed without meeting goal
    }

    public static class GoalProgressInfo {
        public final GoalType goalType;
        public final int currentProgress;
        public final int target;
        public final int percentage;
        public final boolean isGoalMet;
        public final int streakCount;
        public final StreakState streakState;

        public GoalProgressInfo(GoalType goalType, int currentProgress, int target,
                                int percentage, boolean isGoalMet, int streakCount,
                                StreakState streakState) {
            this.goalType = goalType;
            this.currentProgress = currentProgress;
            this.target = target;
            this.percentage = percentage;
            this.isGoalMet = isGoalMet;
            this.streakCount = streakCount;
            this.streakState = streakState;
        }

        public String getFormattedGoalSubtitle(boolean isBn) {
            String currStr = isBn ? BengaliNumberUtil.toBengali(currentProgress) : String.valueOf(currentProgress);
            String targetStr = isBn ? BengaliNumberUtil.toBengali(target) : String.valueOf(target);

            switch (goalType) {
                case AYAH:
                    return isBn
                            ? ("দৈনিক লক্ষ্য: " + currStr + " / " + targetStr + " আয়াত")
                            : ("Daily Goal: " + currStr + " / " + targetStr + " Ayahs");
                case PAGE:
                    return isBn
                            ? ("দৈনিক লক্ষ্য: " + currStr + " / " + targetStr + " পৃষ্ঠা")
                            : ("Daily Goal: " + currStr + " / " + targetStr + " Page");
                case TIME:
                    return isBn
                            ? ("দৈনিক লক্ষ্য: " + currStr + " / " + targetStr + " মিনিট")
                            : ("Daily Goal: " + currStr + " / " + targetStr + " Mins");
                case SURAH:
                    return isBn
                            ? ("দৈনিক লক্ষ্য: " + currStr + " / " + targetStr + " সূরা")
                            : ("Daily Goal: " + currStr + " / " + targetStr + " Surah");
                default:
                    return isBn
                            ? ("দৈনিক লক্ষ্য: " + currStr + " / " + targetStr)
                            : ("Daily Goal: " + currStr + " / " + targetStr);
            }
        }

        public String getFormattedStreakText(boolean isBn) {
            switch (streakState) {
                case ACTIVE_FIRE:
                    return isBn
                            ? (BengaliNumberUtil.toBengali(Math.max(1, streakCount)) + " দিন স্ট্রিক")
                            : (Math.max(1, streakCount) + " Day Streak");
                case PENDING_SAD:
                    return isBn ? "০ দিন স্ট্রিক" : "0 Day Streak";
                case MISSED_ANGRY:
                    return isBn ? "স্ট্রিক নষ্ট!" : "Streak Lost!";
                default:
                    return isBn ? "০ দিন স্ট্রিক" : "0 Day Streak";
            }
        }
    }

    private static volatile QuranStreakGoalManager instance;
    private final MutableLiveData<GoalProgressInfo> progressLiveData = new MutableLiveData<>();

    private QuranStreakGoalManager() {}

    public static QuranStreakGoalManager getInstance() {
        if (instance == null) {
            synchronized (QuranStreakGoalManager.class) {
                if (instance == null) {
                    instance = new QuranStreakGoalManager();
                }
            }
        }
        return instance;
    }

    public LiveData<GoalProgressInfo> getProgressLiveData() {
        return progressLiveData;
    }

    private SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private String getTodayDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date());
    }

    private synchronized void checkAndRolloverDay(Context context) {
        SharedPreferences prefs = getPrefs(context);
        String today = getTodayDateString();
        String storedToday = prefs.getString(KEY_TODAY_DATE, "");

        if (!today.equals(storedToday)) {
            // New day arrived: reset today counters
            prefs.edit()
                    .putString(KEY_TODAY_DATE, today)
                    .putStringSet(KEY_TODAY_AYAHS_SET, new HashSet<>())
                    .putInt(KEY_TODAY_MINUTES, 0)
                    .putInt(KEY_TODAY_SECONDS_ACCUMULATOR, 0)
                    .putStringSet(KEY_TODAY_PAGES_SET, new HashSet<>())
                    .putStringSet(KEY_TODAY_SURAHS_SET, new HashSet<>())
                    .apply();
        }
    }

    public synchronized GoalProgressInfo getProgressInfo(Context context) {
        checkAndRolloverDay(context);
        SharedPreferences prefs = getPrefs(context);

        String goalTypeId = prefs.getString(KEY_GOAL_TYPE, GoalType.AYAH.id);
        GoalType type = GoalType.fromId(goalTypeId);
        int target = prefs.getInt(KEY_GOAL_TARGET, type.defaultTarget);
        if (target <= 0) target = type.defaultTarget;

        int current = 0;
        switch (type) {
            case AYAH:
                Set<String> ayahs = prefs.getStringSet(KEY_TODAY_AYAHS_SET, null);
                current = (ayahs != null) ? ayahs.size() : 0;
                break;
            case PAGE:
                Set<String> pages = prefs.getStringSet(KEY_TODAY_PAGES_SET, null);
                current = (pages != null) ? pages.size() : 0;
                break;
            case TIME:
                current = prefs.getInt(KEY_TODAY_MINUTES, 0);
                break;
            case SURAH:
                Set<String> surahs = prefs.getStringSet(KEY_TODAY_SURAHS_SET, null);
                current = (surahs != null) ? surahs.size() : 0;
                break;
        }

        boolean isGoalMet = current >= target;
        int percentage = Math.min(100, (int) Math.round((current * 100.0) / target));

        // Evaluate Streak
        String today = getTodayDateString();
        String lastCompletedDate = prefs.getString(KEY_LAST_COMPLETED_DATE, "");
        int streakCount = prefs.getInt(KEY_STREAK_COUNT, 0);

        if (isGoalMet) {
            if (!today.equals(lastCompletedDate)) {
                // Goal newly completed today!
                int daysDiff = calculateDaysDifference(lastCompletedDate, today);
                if (daysDiff == 1) {
                    // Completed yesterday, so continue streak
                    streakCount += 1;
                } else {
                    // First time or broken streak restored
                    streakCount = 1;
                }
                prefs.edit()
                        .putString(KEY_LAST_COMPLETED_DATE, today)
                        .putInt(KEY_STREAK_COUNT, streakCount)
                        .apply();
            }
        }

        StreakState streakState;
        if (isGoalMet || today.equals(lastCompletedDate)) {
            streakState = StreakState.ACTIVE_FIRE;
        } else {
            int daysSinceCompleted = calculateDaysDifference(lastCompletedDate, today);
            if (daysSinceCompleted <= 1) {
                // Not met today yet, or completed yesterday
                streakState = StreakState.PENDING_SAD;
            } else if (daysSinceCompleted == 2) {
                // Missed 1 full day (yesterday was missed)
                streakState = StreakState.PENDING_SAD;
            } else {
                // Missed 2 or more consecutive days
                streakState = StreakState.MISSED_ANGRY;
                if (streakCount > 0) {
                    streakCount = 0;
                    prefs.edit().putInt(KEY_STREAK_COUNT, 0).apply();
                }
            }
        }

        GoalProgressInfo info = new GoalProgressInfo(type, current, target, percentage, isGoalMet, streakCount, streakState);
        progressLiveData.postValue(info);
        return info;
    }

    public synchronized void setGoal(Context context, GoalType type, int target) {
        checkAndRolloverDay(context);
        getPrefs(context).edit()
                .putString(KEY_GOAL_TYPE, type.id)
                .putInt(KEY_GOAL_TARGET, target > 0 ? target : type.defaultTarget)
                .apply();
        getProgressInfo(context);
    }

    public synchronized void recordAyahReadOrListened(Context context, int surahNumber, int ayahNumber) {
        checkAndRolloverDay(context);
        SharedPreferences prefs = getPrefs(context);

        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_TODAY_AYAHS_SET, new HashSet<>()));
        String key = surahNumber + ":" + ayahNumber;
        if (!set.contains(key)) {
            set.add(key);
            prefs.edit().putStringSet(KEY_TODAY_AYAHS_SET, set).apply();
            getProgressInfo(context);
        }
    }

    public synchronized void recordPageRead(Context context, int pageNumber) {
        if (pageNumber <= 0) return;
        checkAndRolloverDay(context);
        SharedPreferences prefs = getPrefs(context);

        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_TODAY_PAGES_SET, new HashSet<>()));
        String key = String.valueOf(pageNumber);
        if (!set.contains(key)) {
            set.add(key);
            prefs.edit().putStringSet(KEY_TODAY_PAGES_SET, set).apply();
            getProgressInfo(context);
        }
    }

    public synchronized void recordListeningSeconds(Context context, int seconds) {
        if (seconds <= 0) return;
        checkAndRolloverDay(context);
        SharedPreferences prefs = getPrefs(context);

        int accumulated = prefs.getInt(KEY_TODAY_SECONDS_ACCUMULATOR, 0) + seconds;
        int currentMinutes = prefs.getInt(KEY_TODAY_MINUTES, 0);

        if (accumulated >= 60) {
            int addedMinutes = accumulated / 60;
            accumulated = accumulated % 60;
            currentMinutes += addedMinutes;

            prefs.edit()
                    .putInt(KEY_TODAY_MINUTES, currentMinutes)
                    .putInt(KEY_TODAY_SECONDS_ACCUMULATOR, accumulated)
                    .apply();
            getProgressInfo(context);
        } else {
            prefs.edit().putInt(KEY_TODAY_SECONDS_ACCUMULATOR, accumulated).apply();
        }
    }

    public synchronized void recordSurahCompleted(Context context, int surahNumber) {
        checkAndRolloverDay(context);
        SharedPreferences prefs = getPrefs(context);

        Set<String> set = new HashSet<>(prefs.getStringSet(KEY_TODAY_SURAHS_SET, new HashSet<>()));
        String key = String.valueOf(surahNumber);
        if (!set.contains(key)) {
            set.add(key);
            prefs.edit().putStringSet(KEY_TODAY_SURAHS_SET, set).apply();
            getProgressInfo(context);
        }
    }

    private int calculateDaysDifference(String fromDateStr, String toDateStr) {
        if (fromDateStr == null || fromDateStr.isEmpty() || toDateStr == null || toDateStr.isEmpty()) {
            return 999;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date from = sdf.parse(fromDateStr);
            Date to = sdf.parse(toDateStr);
            if (from == null || to == null) return 999;

            long diffMs = to.getTime() - from.getTime();
            return (int) (diffMs / (1000L * 60L * 60L * 24L));
        } catch (Exception e) {
            return 999;
        }
    }
}
