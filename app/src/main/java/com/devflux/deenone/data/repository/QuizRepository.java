package com.devflux.deenone.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.QuizDao;
import com.devflux.deenone.data.local.entity.QuizQuestionEntity;
import com.devflux.deenone.data.local.entity.QuizResultEntity;

import java.util.List;

public class QuizRepository {

    private final QuizDao quizDao;

    public QuizRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.quizDao = db.quizDao();
    }

    public LiveData<List<QuizQuestionEntity>> getRandomQuestions(int limit) {
        return quizDao.getRandomQuestions(limit);
    }

    public LiveData<List<QuizQuestionEntity>> getQuestionsByCategory(String category, int limit) {
        if ("all".equalsIgnoreCase(category) || "সকল বিষয়".equalsIgnoreCase(category)) {
            return quizDao.getRandomQuestions(limit);
        }
        return quizDao.getQuestionsByCategory(category, limit);
    }

    public LiveData<List<QuizQuestionEntity>> getQuestionsByDifficulty(int difficulty, int limit) {
        if (difficulty <= 0) {
            return quizDao.getRandomQuestions(limit);
        }
        return quizDao.getQuestionsByDifficulty(difficulty, limit);
    }

    public LiveData<List<QuizQuestionEntity>> getDailyQuiz() {
        return quizDao.getDailyQuiz();
    }

    public void saveQuizResult(QuizResultEntity result) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            quizDao.insertResult(result);
        });
    }

    public LiveData<List<QuizResultEntity>> getQuizHistory() {
        return quizDao.getQuizHistory();
    }

    public LiveData<Integer> getTotalScore() {
        return quizDao.getTotalScore();
    }

    public void insertQuestions(List<QuizQuestionEntity> questions) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            quizDao.insertAll(questions);
        });
    }
}
