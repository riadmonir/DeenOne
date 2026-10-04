package com.devflux.deenone.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "battle_history")
public class BattleHistoryEntity {

    @PrimaryKey
    @NonNull
    private String battleId;
    private String battleCode;
    private String categoryId;
    private String categoryTitle;
    private int score;
    private int correctAnswers;
    private int wrongAnswers;
    private int totalQuestions;
    private int rank;
    private boolean isWinner;
    private int deenXpEarned;
    private long durationSeconds;
    private long timestamp;

    public BattleHistoryEntity(@NonNull String battleId, String battleCode, String categoryId,
                               String categoryTitle, int score, int correctAnswers, int wrongAnswers,
                               int totalQuestions, int rank, boolean isWinner, int deenXpEarned,
                               long durationSeconds, long timestamp) {
        this.battleId = battleId;
        this.battleCode = battleCode;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.score = score;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.totalQuestions = totalQuestions;
        this.rank = rank;
        this.isWinner = isWinner;
        this.deenXpEarned = deenXpEarned;
        this.durationSeconds = durationSeconds;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getBattleId() { return battleId; }
    public void setBattleId(@NonNull String battleId) { this.battleId = battleId; }

    public String getBattleCode() { return battleCode; }
    public void setBattleCode(String battleCode) { this.battleCode = battleCode; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryTitle() { return categoryTitle; }
    public void setCategoryTitle(String categoryTitle) { this.categoryTitle = categoryTitle; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(int wrongAnswers) { this.wrongAnswers = wrongAnswers; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public boolean isWinner() { return isWinner; }
    public void setWinner(boolean winner) { isWinner = winner; }

    public int getDeenXpEarned() { return deenXpEarned; }
    public void setDeenXpEarned(int deenXpEarned) { this.deenXpEarned = deenXpEarned; }

    public long getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(long durationSeconds) { this.durationSeconds = durationSeconds; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
