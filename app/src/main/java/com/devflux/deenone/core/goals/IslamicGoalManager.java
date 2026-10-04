package com.devflux.deenone.core.goals;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class IslamicGoalManager {

    private static final String PREF_NAME = "deanone_islamic_goals_prefs";
    private static final String KEY_GOALS_JSON = "key_goals_json_array";
    private static final String KEY_LAST_ACTIVE_DATE = "key_last_active_date";

    private static volatile IslamicGoalManager instance;

    public static IslamicGoalManager getInstance() {
        if (instance == null) {
            synchronized (IslamicGoalManager.class) {
                if (instance == null) {
                    instance = new IslamicGoalManager();
                }
            }
        }
        return instance;
    }

    public static String getTodayDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
    }

    public synchronized List<IslamicGoalItem> getGoals(Context context) {
        if (context == null) return new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String jsonStr = prefs.getString(KEY_GOALS_JSON, "");
        List<IslamicGoalItem> list = new ArrayList<>();

        if (jsonStr.isEmpty()) {
            list = getDefaultPresetGoals();
            saveGoals(context, list);
            return list;
        }

        try {
            JSONArray arr = new JSONArray(jsonStr);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                IslamicGoalItem item = IslamicGoalItem.fromJson(obj);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (Exception ignored) {}

        checkAndResetDailyProgress(context, list);
        return list;
    }

    public synchronized void saveGoals(Context context, List<IslamicGoalItem> list) {
        if (context == null || list == null) return;
        JSONArray arr = new JSONArray();
        for (IslamicGoalItem item : list) {
            arr.put(item.toJson());
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_GOALS_JSON, arr.toString()).apply();
    }

    public void addGoal(Context context, IslamicGoalItem.Category category, String title, String unit, int dailyTarget, boolean reminder, String reminderTime) {
        List<IslamicGoalItem> list = getGoals(context);
        String id = UUID.randomUUID().toString();
        int weeklyTarget = dailyTarget * 7;
        IslamicGoalItem item = new IslamicGoalItem(id, category, title, unit, dailyTarget, weeklyTarget, 0, 0, false, reminder, reminderTime, "", System.currentTimeMillis());
        list.add(0, item);
        saveGoals(context, list);
    }

    public void updateGoal(Context context, String goalId, String title, String unit, int dailyTarget, boolean reminder, String reminderTime) {
        List<IslamicGoalItem> list = getGoals(context);
        for (IslamicGoalItem item : list) {
            if (item.id.equals(goalId)) {
                item.title = title;
                item.targetUnit = unit;
                item.dailyTarget = dailyTarget;
                item.weeklyTarget = dailyTarget * 7;
                item.isReminderEnabled = reminder;
                item.reminderTime = reminderTime;
                break;
            }
        }
        saveGoals(context, list);
    }

    public void deleteGoal(Context context, String goalId) {
        List<IslamicGoalItem> list = getGoals(context);
        list.removeIf(item -> item.id.equals(goalId));
        saveGoals(context, list);
    }

    public void incrementProgress(Context context, String goalId, int amount) {
        List<IslamicGoalItem> list = getGoals(context);
        String today = getTodayDateStr();
        for (IslamicGoalItem item : list) {
            if (item.id.equals(goalId)) {
                item.currentProgress += amount;
                if (item.currentProgress >= item.dailyTarget && !item.isCompletedToday) {
                    item.isCompletedToday = true;
                    item.streakDays++;
                    item.lastCompletedDate = today;
                }
                break;
            }
        }
        saveGoals(context, list);
    }

    private void checkAndResetDailyProgress(Context context, List<IslamicGoalItem> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedDate = prefs.getString(KEY_LAST_ACTIVE_DATE, "");
        String today = getTodayDateStr();

        if (!today.equals(savedDate)) {
            for (IslamicGoalItem item : list) {
                item.currentProgress = 0;
                item.isCompletedToday = false;
            }
            prefs.edit().putString(KEY_LAST_ACTIVE_DATE, today).apply();
            saveGoals(context, list);
        }
    }

    private List<IslamicGoalItem> getDefaultPresetGoals() {
        List<IslamicGoalItem> presets = new ArrayList<>();
        presets.add(new IslamicGoalItem(
                "preset_quran",
                IslamicGoalItem.Category.QURAN,
                "দৈনিক ২০ মিনিট কুরআন তিলাওয়াত",
                "মিনিট",
                20, 140, 0, 1, false, true, "06:00 AM", "", System.currentTimeMillis()
        ));
        presets.add(new IslamicGoalItem(
                "preset_dhikr",
                IslamicGoalItem.Category.DHIKR,
                "দৈনিক ১০০ বার ইস্তিগফার পাঠ",
                "বার",
                100, 700, 0, 3, false, true, "08:00 AM", "", System.currentTimeMillis()
        ));
        presets.add(new IslamicGoalItem(
                "preset_salah",
                IslamicGoalItem.Category.SALAH,
                "৫ ওয়াক্ত সালাত জামাতের সাথে আদায়",
                "ওয়াক্ত",
                5, 35, 0, 5, false, true, "04:30 AM", "", System.currentTimeMillis()
        ));
        presets.add(new IslamicGoalItem(
                "preset_hadith",
                IslamicGoalItem.Category.HADITH,
                "প্রতিদিন ১টি সহীহ হাদিস অধ্যয়ন",
                "টি",
                1, 7, 0, 2, false, true, "09:00 PM", "", System.currentTimeMillis()
        ));
        return presets;
    }
}