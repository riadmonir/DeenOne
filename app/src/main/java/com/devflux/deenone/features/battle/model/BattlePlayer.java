package com.devflux.deenone.features.battle.model;

import java.io.Serializable;

public class BattlePlayer implements Serializable {

    public enum PlayerStatus {
        JOINED("যুক্ত হয়েছেন", "Joined"),
        READY("প্রস্তুত", "Ready"),
        NOT_READY("অপেক্ষমাণ...", "Waiting..."),
        DISCONNECTED("সংযোগ বিচ্ছিন্ন", "Disconnected"),
        RECONNECTED("পুনঃসংযুক্ত", "Reconnected");

        private final String labelBn;
        private final String labelEn;
        PlayerStatus(String labelBn, String labelEn) {
            this.labelBn = labelBn;
            this.labelEn = labelEn;
        }
        public String getLabelBn() {
            return labelBn;
        }
        public String getLabelEn() {
            return labelEn;
        }
    }

    private final String id;
    private final String name;
    private final String avatarResource;
    private final boolean isBot;
    private int score;
    private int correctAnswers;
    private int wrongAnswers;
    private int currentSelectedOption = -1;
    private PlayerStatus status;
    private long lastHeartbeatTimestamp;

    public BattlePlayer(String id, String name, String avatarResource, boolean isBot) {
        this.id = id;
        this.name = name;
        this.avatarResource = avatarResource;
        this.isBot = isBot;
        this.score = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
        this.status = PlayerStatus.READY; // Default ready
        this.lastHeartbeatTimestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAvatarResource() { return avatarResource; }
    public boolean isBot() { return isBot; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public void addScore(int points) { this.score += points; }
    public int getCorrectAnswers() { return correctAnswers; }
    public void incrementCorrect() { this.correctAnswers++; }
    public int getWrongAnswers() { return wrongAnswers; }
    public void incrementWrong() { this.wrongAnswers++; }
    public int getCurrentSelectedOption() { return currentSelectedOption; }
    public void setCurrentSelectedOption(int currentSelectedOption) { this.currentSelectedOption = currentSelectedOption; }

    public PlayerStatus getStatus() { return status; }
    public void setStatus(PlayerStatus status) {
        this.status = status;
        this.lastHeartbeatTimestamp = System.currentTimeMillis();
    }

    public boolean isReady() {
        return status == PlayerStatus.READY;
    }

    public boolean isDisconnected() {
        return status == PlayerStatus.DISCONNECTED;
    }

    public long getLastHeartbeatTimestamp() { return lastHeartbeatTimestamp; }
    public void updateHeartbeat() { this.lastHeartbeatTimestamp = System.currentTimeMillis(); }
}
