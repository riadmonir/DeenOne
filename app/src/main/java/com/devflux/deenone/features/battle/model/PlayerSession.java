package com.devflux.deenone.features.battle.model;

import java.io.Serializable;
import java.util.UUID;

public class PlayerSession implements Serializable {

    private final String sessionToken;
    private final String roomCode;
    private final String playerId;
    private final String playerName;
    private final long sessionCreatedAt;
    private long lastActiveTimestamp;
    private long disconnectedTimestamp;
    private boolean isConnected;
    private boolean isSealed;

    public PlayerSession(String roomCode, String playerId, String playerName) {
        this.sessionToken = "SES_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        this.roomCode = roomCode;
        this.playerId = playerId;
        this.playerName = playerName;
        this.sessionCreatedAt = System.currentTimeMillis();
        this.lastActiveTimestamp = System.currentTimeMillis();
        this.isConnected = true;
        this.isSealed = false;
    }

    public String getSessionToken() { return sessionToken; }
    public String getRoomCode() { return roomCode; }
    public String getPlayerId() { return playerId; }
    public String getPlayerName() { return playerName; }
    public long getSessionCreatedAt() { return sessionCreatedAt; }
    public long getLastActiveTimestamp() { return lastActiveTimestamp; }
    public long getDisconnectedTimestamp() { return disconnectedTimestamp; }

    public boolean isConnected() { return isConnected; }
    public void setConnected(boolean connected) {
        this.isConnected = connected;
        this.lastActiveTimestamp = System.currentTimeMillis();
        if (!connected) {
            this.disconnectedTimestamp = System.currentTimeMillis();
        }
    }

    public boolean isSealed() { return isSealed; }
    public void sealSession() { this.isSealed = true; }
}
