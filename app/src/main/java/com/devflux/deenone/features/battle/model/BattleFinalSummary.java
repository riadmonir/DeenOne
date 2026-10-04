package com.devflux.deenone.features.battle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleFinalSummary implements Serializable {

    private final String roomCode;
    private final String categoryTitleBn;
    private final String battleMode;
    private final int totalQuestions;
    private final long battleDurationSeconds;
    private final List<PlayerBattleResult> rankedResults;

    public BattleFinalSummary(String roomCode, String categoryTitleBn, String battleMode,
                              int totalQuestions, long battleDurationSeconds,
                              List<PlayerBattleResult> rankedResults) {
        this.roomCode = roomCode;
        this.categoryTitleBn = categoryTitleBn;
        this.battleMode = battleMode;
        this.totalQuestions = totalQuestions;
        this.battleDurationSeconds = battleDurationSeconds;
        this.rankedResults = rankedResults != null ? new ArrayList<>(rankedResults) : new ArrayList<>();
    }

    public String getRoomCode() { return roomCode; }
    public String getCategoryTitleBn() { return categoryTitleBn; }
    public String getBattleMode() { return battleMode; }
    public int getTotalQuestions() { return totalQuestions; }
    public long getBattleDurationSeconds() { return battleDurationSeconds; }
    public List<PlayerBattleResult> getRankedResults() { return Collections.unmodifiableList(rankedResults); }

    public PlayerBattleResult getWinner() {
        return rankedResults.isEmpty() ? null : rankedResults.get(0);
    }
}
