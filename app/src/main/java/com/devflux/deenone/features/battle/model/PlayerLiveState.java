package com.devflux.deenone.features.battle.model;

import java.io.Serializable;

public class PlayerLiveState implements Serializable {

    public enum AnswerStatus {
        UNANSWERED,
        ANSWERED,
        TIMEOUT
    }

    private final String playerId;
    private final String playerName;
    private final int score;
    private final int correctCount;
    private final int wrongCount;
    private final AnswerStatus answerStatus;
    private final BattlePlayer.PlayerStatus connectionStatus;
    private final int currentQuestionNumber;
    private final int totalQuestions;

    public PlayerLiveState(String playerId, String playerName, int score,
                           int correctCount, int wrongCount,
                           AnswerStatus answerStatus,
                           BattlePlayer.PlayerStatus connectionStatus,
                           int currentQuestionNumber, int totalQuestions) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.correctCount = correctCount;
        this.wrongCount = wrongCount;
        this.answerStatus = answerStatus;
        this.connectionStatus = connectionStatus;
        this.currentQuestionNumber = currentQuestionNumber;
        this.totalQuestions = totalQuestions;
    }

    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }
    public int getCorrectCount() { return correctCount; }
    public int getWrongCount() { return wrongCount; }
    public AnswerStatus getAnswerStatus() { return answerStatus; }
    public BattlePlayer.PlayerStatus getConnectionStatus() { return connectionStatus; }
    public int getCurrentQuestionNumber() { return currentQuestionNumber; }
    public int getTotalQuestions() { return totalQuestions; }

    public int getProgressPercentage() {
        if (totalQuestions <= 0) return 0;
        return (int) (((float) currentQuestionNumber / totalQuestions) * 100);
    }
}
