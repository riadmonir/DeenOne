package com.devflux.deenone.features.battle.model;

import java.io.Serializable;

public class BattleConfig implements Serializable {

    public enum BattleMode {
        ONE_VS_ONE("1 vs 1", 2),
        ONE_VS_TWO("1 vs 2", 3),
        ONE_VS_THREE("1 vs 3", 4),
        ONE_VS_FOUR("1 vs 4", 5);

        private final String label;
        private final int playerCapacity;

        BattleMode(String label, int playerCapacity) {
            this.label = label;
            this.playerCapacity = playerCapacity;
        }

        public String getLabel() { return label; }
        public int getPlayerCapacity() { return playerCapacity; }
    }

    public enum Difficulty {
        EASY("সহজ"),
        MEDIUM("মাঝারি"),
        HARD("কঠিন"),
        MIXED("মিশ্র");

        private final String labelBn;
        Difficulty(String labelBn) { this.labelBn = labelBn; }
        public String getLabelBn() { return labelBn; }
    }

    private BattleMode mode;
    private int totalPlayers = 2;
    private int totalQuestions = 10;
    private int totalDurationMinutes = 5;
    private int timePerQuestionSec = 15;
    private String categoryId = "quran_stories";
    private String categoryTitleBn = "কুরআনের ঘটনা ও শিক্ষা";
    private Difficulty difficulty = Difficulty.MEDIUM;
    private boolean negativeMarkingEnabled = false;

    public BattleConfig(int totalPlayers, int totalQuestions, int totalDurationMinutes,
                        String categoryId, String categoryTitleBn) {
        this.totalPlayers = totalPlayers >= 2 ? totalPlayers : 2;
        this.totalQuestions = totalQuestions > 0 ? totalQuestions : 10;
        this.totalDurationMinutes = totalDurationMinutes > 0 ? totalDurationMinutes : 5;
        this.categoryId = categoryId != null ? categoryId : "quran_stories";
        this.categoryTitleBn = categoryTitleBn != null ? categoryTitleBn : "কুরআনের ঘটনা ও শিক্ষা";
        this.difficulty = Difficulty.MEDIUM;
        this.negativeMarkingEnabled = false;

        // Auto-calculate per question time
        this.timePerQuestionSec = Math.max(10, (this.totalDurationMinutes * 60) / this.totalQuestions);

        if (this.totalPlayers == 2) this.mode = BattleMode.ONE_VS_ONE;
        else if (this.totalPlayers == 3) this.mode = BattleMode.ONE_VS_TWO;
        else if (this.totalPlayers == 4) this.mode = BattleMode.ONE_VS_THREE;
        else this.mode = BattleMode.ONE_VS_FOUR;
    }

    public BattleConfig(BattleMode mode, int totalQuestions, int timePerQuestionSec,
                        String categoryId, String categoryTitleBn, Difficulty difficulty,
                        boolean negativeMarkingEnabled) {
        this.mode = mode != null ? mode : BattleMode.ONE_VS_ONE;
        this.totalPlayers = this.mode.getPlayerCapacity();
        this.totalQuestions = totalQuestions > 0 ? totalQuestions : 10;
        this.timePerQuestionSec = timePerQuestionSec > 0 ? timePerQuestionSec : 15;
        this.totalDurationMinutes = Math.max(1, (this.totalQuestions * this.timePerQuestionSec) / 60);
        this.categoryId = categoryId != null ? categoryId : "quran_stories";
        this.categoryTitleBn = categoryTitleBn != null ? categoryTitleBn : "কুরআনের ঘটনা ও শিক্ষা";
        this.difficulty = difficulty != null ? difficulty : Difficulty.MEDIUM;
        this.negativeMarkingEnabled = negativeMarkingEnabled;
    }

    public BattleMode getMode() { return mode; }
    public void setMode(BattleMode mode) {
        this.mode = mode;
        this.totalPlayers = mode.getPlayerCapacity();
    }
    public int getTotalPlayers() { return totalPlayers; }
    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
        if (totalPlayers == 2) this.mode = BattleMode.ONE_VS_ONE;
        else if (totalPlayers == 3) this.mode = BattleMode.ONE_VS_TWO;
        else if (totalPlayers == 4) this.mode = BattleMode.ONE_VS_THREE;
        else this.mode = BattleMode.ONE_VS_FOUR;
    }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
        this.timePerQuestionSec = Math.max(10, (this.totalDurationMinutes * 60) / Math.max(1, this.totalQuestions));
    }
    public int getTotalDurationMinutes() { return totalDurationMinutes; }
    public void setTotalDurationMinutes(int totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
        this.timePerQuestionSec = Math.max(10, (this.totalDurationMinutes * 60) / Math.max(1, this.totalQuestions));
    }
    public int getTimePerQuestionSec() { return timePerQuestionSec; }
    public void setTimePerQuestionSec(int timePerQuestionSec) { this.timePerQuestionSec = timePerQuestionSec; }
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryTitleBn() { return categoryTitleBn; }
    public void setCategoryTitleBn(String categoryTitleBn) { this.categoryTitleBn = categoryTitleBn; }
    public String getCategoryTitleEn() {
        if (categoryId != null) {
            com.devflux.deenone.features.battle.model.BattleCategory cat =
                com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository.getCategoryById(categoryId);
            if (cat != null && cat.getTitleEn() != null) return cat.getTitleEn();
        }
        return categoryTitleBn != null ? categoryTitleBn : "General Islamic";
    }
    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public boolean isNegativeMarkingEnabled() { return negativeMarkingEnabled; }
    public void setNegativeMarkingEnabled(boolean negativeMarkingEnabled) { this.negativeMarkingEnabled = negativeMarkingEnabled; }
}
