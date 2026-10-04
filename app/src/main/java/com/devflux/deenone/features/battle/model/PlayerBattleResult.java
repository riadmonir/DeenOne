package com.devflux.deenone.features.battle.model;

import java.io.Serializable;

public class PlayerBattleResult implements Serializable {

    private final String playerId;
    private final String playerName;
    private final String avatarResource;
    private final int rank;
    private final int finalScore;
    private final int correctAnswers;
    private final int wrongAnswers;
    private final int totalQuestions;
    private final long avgResponseTimeMs;
    private final String completionStatus;
    private final boolean isWinner;
    private final int deenXpEarned;

    public PlayerBattleResult(String playerId, String playerName, String avatarResource,
                              int rank, int finalScore, int correctAnswers, int wrongAnswers,
                              int totalQuestions, long avgResponseTimeMs,
                              String completionStatus, boolean isWinner, int deenXpEarned) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.avatarResource = avatarResource;
        this.rank = rank;
        this.finalScore = finalScore;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.totalQuestions = totalQuestions;
        this.avgResponseTimeMs = avgResponseTimeMs;
        this.completionStatus = completionStatus;
        this.isWinner = isWinner;
        this.deenXpEarned = deenXpEarned;
    }

    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public String getAvatarResource() { return avatarResource; }
    public int getRank() { return rank; }
    public int getFinalScore() { return finalScore; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getWrongAnswers() { return wrongAnswers; }
    public int getTotalQuestions() { return totalQuestions; }
    public long getAvgResponseTimeMs() { return avgResponseTimeMs; }
    public String getCompletionStatus() { return completionStatus; }
    public boolean isWinner() { return isWinner; }
    public int getDeenXpEarned() { return deenXpEarned; }

    public double getAvgResponseTimeSec() {
        return Math.round((avgResponseTimeMs / 1000.0) * 10.0) / 10.0;
    }
}
