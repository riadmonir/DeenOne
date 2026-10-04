package com.devflux.deenone.features.battle.service;

import java.io.Serializable;

public class AnswerValidationResponse implements Serializable {

    public enum Status {
        VALIDATED,
        INVALID_ROOM,
        INVALID_PLAYER,
        INVALID_QUESTION,
        DUPLICATE_SUBMISSION,
        SERVER_ERROR
    }

    private final Status status;
    private final boolean isCorrect;
    private final int correctOptionIndex;
    private final int pointsDelta;
    private final int updatedPlayerScore;
    private final String explanation;
    private final String reference;
    private final boolean isMatchFinished;

    public AnswerValidationResponse(Status status, boolean isCorrect, int correctOptionIndex,
                                    int pointsDelta, int updatedPlayerScore,
                                    String explanation, String reference, boolean isMatchFinished) {
        this.status = status;
        this.isCorrect = isCorrect;
        this.correctOptionIndex = correctOptionIndex;
        this.pointsDelta = pointsDelta;
        this.updatedPlayerScore = updatedPlayerScore;
        this.explanation = explanation;
        this.reference = reference;
        this.isMatchFinished = isMatchFinished;
    }

    public static AnswerValidationResponse error(Status status) {
        return new AnswerValidationResponse(status, false, -1, 0, 0, null, null, false);
    }

    public Status getStatus() { return status; }
    public boolean isSuccess() { return status == Status.VALIDATED; }
    public boolean isCorrect() { return isCorrect; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public int getPointsDelta() { return pointsDelta; }
    public int getUpdatedPlayerScore() { return updatedPlayerScore; }
    public String getExplanation() { return explanation; }
    public String getReference() { return reference; }
    public boolean isMatchFinished() { return isMatchFinished; }
}
