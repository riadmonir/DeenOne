package com.devflux.deenone.features.battle.service;

import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.model.BattleSyncState;

import java.io.Serializable;

public class ReconnectResult implements Serializable {

    public enum Status {
        SUCCESS,
        MATCH_ALREADY_FINISHED,
        SESSION_EXPIRED,
        INVALID_CREDENTIALS,
        NETWORK_ERROR
    }

    private final Status status;
    private final BattleRoom room;
    private final BattleSyncState syncState;
    private final String message;

    public ReconnectResult(Status status, BattleRoom room, BattleSyncState syncState, String message) {
        this.status = status;
        this.room = room;
        this.syncState = syncState;
        this.message = message;
    }

    public static ReconnectResult success(BattleRoom room, BattleSyncState syncState) {
        return new ReconnectResult(Status.SUCCESS, room, syncState, "সফলভাবে পুনরায় সংযুক্ত হয়েছে");
    }

    public static ReconnectResult failure(Status status, String message) {
        return new ReconnectResult(status, null, null, message);
    }

    public Status getStatus() { return status; }
    public boolean isSuccess() { return status == Status.SUCCESS; }
    public BattleRoom getRoom() { return room; }
    public BattleSyncState getSyncState() { return syncState; }
    public String getMessage() { return message; }
}
