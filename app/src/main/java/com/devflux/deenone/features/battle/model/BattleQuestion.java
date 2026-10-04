package com.devflux.deenone.features.battle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BattleQuestion implements Serializable {

    public enum QuestionStatus {
        ACTIVE,
        VERIFIED,
        ARCHIVED
    }

    private final String id;
    private final String categoryId;
    private final String questionText;
    private final List<String> options;
    private final int correctOptionIndex;
    private final String explanation;
    private final String reference;
    private final BattleConfig.Difficulty difficulty;
    private final String source;
    private final String language;
    private final long createdTimestamp;
    private final QuestionStatus status;
    private List<Integer> optionPermutation;

    public BattleQuestion(String id, String categoryId, String questionText,
                          String[] optionsArray, int correctOptionIndex,
                          String explanation, String reference) {
        this(id, categoryId, questionText,
                optionsArray != null ? Arrays.asList(optionsArray) : new ArrayList<>(),
                correctOptionIndex, explanation, reference,
                BattleConfig.Difficulty.MEDIUM, "Islamic Authentic Foundation", "bn",
                System.currentTimeMillis(), QuestionStatus.VERIFIED);
    }

    public BattleQuestion(String id, String categoryId, String questionText,
                          List<String> options, int correctOptionIndex,
                          String explanation, String reference) {
        this(id, categoryId, questionText,
                options != null ? options : new ArrayList<>(),
                correctOptionIndex, explanation, reference,
                BattleConfig.Difficulty.MEDIUM, "Islamic Authentic Foundation", "bn",
                System.currentTimeMillis(), QuestionStatus.VERIFIED);
    }

    public BattleQuestion(String id, String categoryId, String questionText,
                          List<String> options, int correctOptionIndex,
                          String explanation, String reference,
                          BattleConfig.Difficulty difficulty, String source,
                          String language, long createdTimestamp, QuestionStatus status) {
        this.id = id;
        this.categoryId = categoryId;
        this.questionText = questionText;
        this.options = options != null ? new ArrayList<>(options) : new ArrayList<>();
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.reference = reference;
        this.difficulty = difficulty != null ? difficulty : BattleConfig.Difficulty.MEDIUM;
        this.source = source != null ? source : "Authentic Islamic Source";
        this.language = language != null ? language : "bn";
        this.createdTimestamp = createdTimestamp > 0 ? createdTimestamp : System.currentTimeMillis();
        this.status = status != null ? status : QuestionStatus.VERIFIED;
    }

    public String getId() { return id; }
    public String getCategoryId() { return categoryId; }
    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public String getExplanation() { return explanation; }
    public String getReference() { return reference; }
    public BattleConfig.Difficulty getDifficulty() { return difficulty; }
    public String getSource() { return source; }
    public String getLanguage() { return language; }
    public long getCreatedTimestamp() { return createdTimestamp; }
    public QuestionStatus getStatus() { return status; }
    public List<Integer> getOptionPermutation() { return optionPermutation; }
    public void setOptionPermutation(List<Integer> optionPermutation) { this.optionPermutation = optionPermutation; }

    /**
     * Creates a fresh copy of this question with dynamically randomized/shuffled option positions
     * and an updated correctOptionIndex matching the new position of the correct answer,
     * along with a unique question ID.
     */
    public BattleQuestion createRandomizedInstance(String uniqueId) {
        if (options == null || options.size() < 2 || correctOptionIndex < 0 || correctOptionIndex >= options.size()) {
            return this;
        }

        String correctText = options.get(correctOptionIndex);
        List<String> shuffled = new ArrayList<>(options);
        java.util.Collections.shuffle(shuffled);

        int newCorrectIndex = shuffled.indexOf(correctText);
        if (newCorrectIndex == -1) {
            newCorrectIndex = 0;
        }

        List<Integer> permutation = new ArrayList<>();
        for (String opt : shuffled) {
            permutation.add(this.options.indexOf(opt));
        }

        BattleQuestion randomized = new BattleQuestion(
                uniqueId != null ? uniqueId : this.id,
                this.categoryId,
                this.questionText,
                shuffled,
                newCorrectIndex,
                this.explanation,
                this.reference,
                this.difficulty,
                this.source,
                this.language,
                System.currentTimeMillis(),
                this.status
        );
        randomized.setOptionPermutation(permutation);
        return randomized;
    }

    public String getNormalizedQuestionText() {
        return questionText != null ? questionText.trim().toLowerCase().replaceAll("[\\s\\p{Punct}]", "") : "";
    }
}
