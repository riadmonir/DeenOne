package com.devflux.deenone.features.quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuizPlayQuestion implements Serializable {
    private final String id;
    private final String categoryId;
    private final String questionBn;
    private final String questionEn;
    private final List<String> optionsBn;
    private final List<String> optionsEn;
    private final int correctOptionIndex;
    private final String explanationBn;
    private final String explanationEn;
    private final String referenceBn;
    private final String referenceEn;

    public QuizPlayQuestion(String id, String categoryId, String questionBn, String questionEn,
                            List<String> optionsBn, List<String> optionsEn,
                            int correctOptionIndex, String explanationBn, String explanationEn,
                            String referenceBn, String referenceEn) {
        this.id = id;
        this.categoryId = categoryId;
        this.questionBn = questionBn;
        this.questionEn = questionEn;
        this.optionsBn = optionsBn != null ? optionsBn : new ArrayList<>();
        this.optionsEn = optionsEn != null ? optionsEn : new ArrayList<>();
        this.correctOptionIndex = correctOptionIndex;
        this.explanationBn = explanationBn;
        this.explanationEn = explanationEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
    }

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getQuestionBn() {
        return questionBn;
    }

    public String getQuestionEn() {
        return (questionEn != null && !questionEn.trim().isEmpty()) ? questionEn : questionBn;
    }

    public List<String> getOptionsBn() {
        return optionsBn;
    }

    public List<String> getOptionsEn() {
        return (optionsEn != null && !optionsEn.isEmpty()) ? optionsEn : optionsBn;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public String getExplanationBn() {
        return explanationBn;
    }

    public String getExplanationEn() {
        return explanationEn;
    }

    public String getReferenceBn() {
        return referenceBn;
    }

    public String getReferenceEn() {
        return referenceEn;
    }
}
