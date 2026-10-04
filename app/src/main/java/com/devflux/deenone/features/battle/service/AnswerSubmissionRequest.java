package com.devflux.deenone.features.battle.service;

import java.io.Serializable;

public class AnswerSubmissionRequest implements Serializable {

    private final String roomCode;
    private final String playerId;
    private final String questionId;
    private final int selectedOptionIndex;
    private final long submissionTimestamp;

    public AnswerSubmissionRequest(String roomCode, String playerId, String questionId, int selectedOptionIndex) {
        this.roomCode = roomCode;
        this.playerId = playerId;
        this.questionId = questionId;
        this.selectedOptionIndex = selectedOptionIndex;
        this.submissionTimestamp = System.currentTimeMillis();
    }

    public String getRoomCode() { return roomCode; }
    public String getPlayerId() { return playerId; }
    public String getQuestionId() { return questionId; }
    public int getSelectedOptionIndex() { return selectedOptionIndex; }
    public long getSubmissionTimestamp() { return submissionTimestamp; }
}
