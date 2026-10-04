package com.devflux.deenone.features.quiz.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devflux.deenone.features.quiz.model.QuizSessionResult;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuizResultStorage {

    private static final String PREF_NAME = "deenone_daily_quiz_results";
    private static final Gson gson = new Gson();

    private static String getTodayKey(String categoryId) {
        String todayStr = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        String cat = (categoryId != null && !categoryId.trim().isEmpty()) ? categoryId.trim() : "default";
        return todayStr + "_" + cat;
    }

    public static boolean isCategoryCompletedToday(@NonNull Context context, String categoryId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(getTodayKey(categoryId));
    }

    public static void saveTodaySessionResult(@NonNull Context context, @NonNull QuizSessionResult result) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(result);
        prefs.edit().putString(getTodayKey(result.getCategoryId()), json).apply();
    }

    @Nullable
    public static QuizSessionResult getTodaySessionResult(@NonNull Context context, String categoryId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(getTodayKey(categoryId), null);
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        try {
            return gson.fromJson(json, QuizSessionResult.class);
        } catch (Exception e) {
            return null;
        }
    }
}
