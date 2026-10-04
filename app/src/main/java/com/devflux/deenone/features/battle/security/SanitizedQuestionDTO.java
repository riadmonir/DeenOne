package com.devflux.deenone.features.battle.security;

import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sanitized Question Data Transfer Object for Client Transmission.
 * STRIPS correct answer completely so that no client APK inspection, RAM dump,
 * or network sniffing can reveal the correct option before server-side validation!
 */
public class SanitizedQuestionDTO implements Serializable {

    private final String questionId;
    private final String categoryId;
    private final String questionText;
    private final List<String> options;
    private final BattleConfig.Difficulty difficulty;
    private final String language;

    public SanitizedQuestionDTO(BattleQuestion fullQuestion) {
        this.questionId = fullQuestion.getId();
        this.categoryId = fullQuestion.getCategoryId();
        this.questionText = fullQuestion.getQuestionText();
        this.options = fullQuestion.getOptions() != null ? new ArrayList<>(fullQuestion.getOptions()) : new ArrayList<>();
        this.difficulty = fullQuestion.getDifficulty();
        this.language = fullQuestion.getLanguage();
    }

    public String getQuestionId() { return questionId; }
    public String getCategoryId() { return categoryId; }
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return Collections.unmodifiableList(options); }
    public BattleConfig.Difficulty getDifficulty() { return difficulty; }
    public String getLanguage() { return language; }
}
