package com.devflux.deenone.features.battle.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleSyncState implements Serializable {

    private final String roomCode;
    private final BattleRoom.RoomState battleStatus;
    private final BattleLifecycleState lifecycleState;
    private final int currentQuestionIndex;
    private final int totalQuestions;
    private final int remainingTimeSec;
    private final long serverTimestampEpochMillis;
    private final List<PlayerLiveState> playersState;

    public BattleSyncState(String roomCode, BattleRoom.RoomState battleStatus,
                           int currentQuestionIndex, int totalQuestions,
                           int remainingTimeSec, long serverTimestampEpochMillis,
                           List<PlayerLiveState> playersState) {
        this(roomCode, battleStatus, BattleLifecycleState.LIVE,
                currentQuestionIndex, totalQuestions, remainingTimeSec,
                serverTimestampEpochMillis, playersState);
    }

    public BattleSyncState(String roomCode, BattleRoom.RoomState battleStatus,
                           BattleLifecycleState lifecycleState,
                           int currentQuestionIndex, int totalQuestions,
                           int remainingTimeSec, long serverTimestampEpochMillis,
                           List<PlayerLiveState> playersState) {
        this.roomCode = roomCode;
        this.battleStatus = battleStatus;
        this.lifecycleState = lifecycleState != null ? lifecycleState : BattleLifecycleState.LIVE;
        this.currentQuestionIndex = currentQuestionIndex;
        this.totalQuestions = totalQuestions;
        this.remainingTimeSec = remainingTimeSec;
        this.serverTimestampEpochMillis = serverTimestampEpochMillis;
        this.playersState = playersState != null ? new ArrayList<>(playersState) : new ArrayList<>();
    }

    public String getRoomCode() { return roomCode; }
    public BattleRoom.RoomState getBattleStatus() { return battleStatus; }
    public BattleLifecycleState getLifecycleState() { return lifecycleState; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getRemainingTimeSec() { return remainingTimeSec; }
    public long getServerTimestampEpochMillis() { return serverTimestampEpochMillis; }
    public List<PlayerLiveState> getPlayersState() { return Collections.unmodifiableList(playersState); }

    public PlayerLiveState getPlayerState(String playerId) {
        if (playerId == null) return null;
        for (PlayerLiveState state : playersState) {
            if (state.getPlayerId().equals(playerId)) {
                return state;
            }
        }
        return null;
    }
}
