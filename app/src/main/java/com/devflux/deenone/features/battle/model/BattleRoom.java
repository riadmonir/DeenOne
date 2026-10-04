package com.devflux.deenone.features.battle.model;

import com.devflux.deenone.features.battle.engine.CategoryIntegrityGuard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BattleRoom implements Serializable {

    // Maintained for backward compatibility and mapped directly to BattleLifecycleState
    public enum RoomState {
        LOBBY_WAITING,
        ALL_PLAYERS_READY,
        STARTING_COUNTDOWN,
        IN_BATTLE,
        PODIUM_FINISHED,
        CANCELLED
    }

    private final String roomCode;
    private final BattleConfig config;
    private final BattlePlayer hostPlayer;
    private final List<BattlePlayer> players = new ArrayList<>();
    private final List<BattleQuestion> questions = new ArrayList<>();
    private final Set<String> usedQuestionIds = new HashSet<>();
    private BattleLifecycleState lifecycleState;
    private RoomState state;
    private int currentQuestionIndex = 0;
    private final long createdAtTimestamp;

    public BattleRoom(String roomCode, BattleConfig config, BattlePlayer hostPlayer) {
        this.roomCode = roomCode;
        this.config = config;
        this.hostPlayer = hostPlayer;
        this.players.add(hostPlayer);
        this.lifecycleState = BattleLifecycleState.CREATED;
        this.state = RoomState.LOBBY_WAITING;
        this.createdAtTimestamp = System.currentTimeMillis();

        if (config.getTotalPlayers() > 1) {
            this.lifecycleState = BattleLifecycleState.WAITING_FOR_PLAYERS;
        } else {
            this.lifecycleState = BattleLifecycleState.PLAYERS_JOINED;
        }
    }

    public synchronized boolean addPlayer(BattlePlayer player) {
        if (player == null) return false;
        if (players.size() >= config.getTotalPlayers()) {
            return false;
        }
        for (BattlePlayer p : players) {
            if (p.getId().equals(player.getId())) {
                return true;
            }
        }
        players.add(player);

        if (players.size() >= config.getTotalPlayers()) {
            this.lifecycleState = BattleLifecycleState.PLAYERS_JOINED;
            if (isAllPlayersReady()) {
                this.lifecycleState = BattleLifecycleState.READY_CHECK;
                this.state = RoomState.ALL_PLAYERS_READY;
            }
        } else {
            this.lifecycleState = BattleLifecycleState.WAITING_FOR_PLAYERS;
        }

        return true;
    }

    public boolean hasPlayer(String playerId) {
        if (playerId == null) return false;
        for (BattlePlayer p : players) {
            if (p.getId().equalsIgnoreCase(playerId)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void updatePlayerStatus(String playerId, BattlePlayer.PlayerStatus newStatus) {
        for (BattlePlayer p : players) {
            if (p.getId().equals(playerId)) {
                p.setStatus(newStatus);
                break;
            }
        }

        if (players.size() >= config.getTotalPlayers()) {
            if (isAllPlayersReady()) {
                this.lifecycleState = BattleLifecycleState.READY_CHECK;
                this.state = RoomState.ALL_PLAYERS_READY;
            } else {
                this.lifecycleState = BattleLifecycleState.PLAYERS_JOINED;
                this.state = RoomState.LOBBY_WAITING;
            }
        } else {
            this.lifecycleState = BattleLifecycleState.WAITING_FOR_PLAYERS;
            this.state = RoomState.LOBBY_WAITING;
        }
    }

    public boolean isAllPlayersReady() {
        if (players.size() < config.getTotalPlayers()) {
            return false;
        }
        for (BattlePlayer p : players) {
            if (p.getStatus() != BattlePlayer.PlayerStatus.READY) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        return players.size() >= config.getTotalPlayers();
    }

    public String getRoomCode() { return roomCode; }
    public BattleConfig getConfig() { return config; }
    public BattlePlayer getHostPlayer() { return hostPlayer; }
    public List<BattlePlayer> getPlayers() { return Collections.unmodifiableList(players); }
    public int getPlayerCount() { return players.size(); }
    
    public List<BattleQuestion> getQuestions() { return Collections.unmodifiableList(questions); }

    public synchronized void setQuestions(List<BattleQuestion> qList) {
        this.questions.clear();
        if (qList != null) {
            List<BattleQuestion> pureList = CategoryIntegrityGuard.enforceCategoryPurity(qList, config.getCategoryId());
            Set<String> seenSignatures = new HashSet<>();
            for (BattleQuestion q : pureList) {
                if (q == null) continue;
                String signature = q.getNormalizedQuestionText();
                if (signature.isEmpty()) {
                    signature = q.getQuestionText() != null ? q.getQuestionText().trim().toLowerCase() : q.getId();
                }
                if (!seenSignatures.contains(signature)) {
                    seenSignatures.add(signature);
                    this.questions.add(q);
                    this.usedQuestionIds.add(q.getId());
                }
            }
        }
    }

    public synchronized void recordUsedQuestionId(String qId) {
        if (qId != null) {
            this.usedQuestionIds.add(qId);
        }
    }

    public synchronized boolean isQuestionUsedInBattle(String qId) {
        return qId != null && usedQuestionIds.contains(qId);
    }

    public synchronized Set<String> getUsedQuestionIds() {
        return Collections.unmodifiableSet(new HashSet<>(usedQuestionIds));
    }

    public BattleLifecycleState getLifecycleState() { return lifecycleState; }
    public void setLifecycleState(BattleLifecycleState lifecycleState) {
        this.lifecycleState = lifecycleState;
        if (lifecycleState == BattleLifecycleState.COUNTDOWN) {
            this.state = RoomState.STARTING_COUNTDOWN;
        } else if (lifecycleState == BattleLifecycleState.LIVE) {
            this.state = RoomState.IN_BATTLE;
        } else if (lifecycleState == BattleLifecycleState.RESULT || lifecycleState == BattleLifecycleState.COMPLETED) {
            this.state = RoomState.PODIUM_FINISHED;
        }
    }

    public RoomState getState() { return state; }
    public void setState(RoomState state) {
        this.state = state;
        if (state == RoomState.STARTING_COUNTDOWN) {
            this.lifecycleState = BattleLifecycleState.COUNTDOWN;
        } else if (state == RoomState.IN_BATTLE) {
            this.lifecycleState = BattleLifecycleState.LIVE;
        } else if (state == RoomState.PODIUM_FINISHED) {
            this.lifecycleState = BattleLifecycleState.RESULT;
        }
    }

    public boolean isFinished() {
        return state == RoomState.PODIUM_FINISHED || state == RoomState.CANCELLED ||
               lifecycleState == BattleLifecycleState.COMPLETED || lifecycleState == BattleLifecycleState.RESULT;
    }

    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public void setCurrentQuestionIndex(int currentQuestionIndex) { this.currentQuestionIndex = currentQuestionIndex; }
    public long getCreatedAtTimestamp() { return createdAtTimestamp; }
}
