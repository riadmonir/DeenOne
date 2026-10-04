package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz_results")
public class QuizResultEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int score;
    private int totalQuestions;
    private String category;
    private int difficulty; // 1=Beginner, 2=Intermediate, 3=Advanced
    private long completedTimestamp;
    private String dateString;
    private boolean isDailyQuiz;

    public QuizResultEntity() {
    }

    @Ignore
    public QuizResultEntity(int score, int totalQuestions, String category, int difficulty,
                            long completedTimestamp, String dateString, boolean isDailyQuiz) {
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.category = category;
        this.difficulty = difficulty;
        this.completedTimestamp = completedTimestamp;
        this.dateString = dateString;
        this.isDailyQuiz = isDailyQuiz;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public long getCompletedTimestamp() {
        return completedTimestamp;
    }

    public void setCompletedTimestamp(long completedTimestamp) {
        this.completedTimestamp = completedTimestamp;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public boolean isDailyQuiz() {
        return isDailyQuiz;
    }

    public void setDailyQuiz(boolean dailyQuiz) {
        isDailyQuiz = dailyQuiz;
    }
}
