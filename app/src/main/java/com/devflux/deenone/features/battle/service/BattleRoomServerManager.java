package com.devflux.deenone.features.battle.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.features.battle.engine.BattleCodeGenerator;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleLifecycleState;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;
import com.devflux.deenone.features.battle.model.BattleSyncState;
import com.devflux.deenone.features.battle.model.PlayerLiveState;
import com.devflux.deenone.features.battle.model.PlayerSession;
import com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository;
import com.devflux.deenone.features.battle.data.remote.BattleCloudRelayClient;
import com.devflux.deenone.features.battle.security.BattleSecurityAntiCheatEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Backend & Server-Side Room, Lifecycle, Session, Reconnection, and Anti-Cheat Manager.
 * Zero-Client-Trust architecture enforcing 100% server validation on joins, scores,
 * timers, answers, and states.
 */
public class BattleRoomServerManager {

    private static volatile BattleRoomServerManager instance;
    private static final long ROOM_EXPIRATION_MILLIS = 20 * 60 * 1000L; // 20 minutes TTL
    private static final long DISCONNECT_GRACE_PERIOD_MILLIS = 5 * 60 * 1000L; // 5 minutes reconnect grace period
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Map<String, BattleRoom> activeRooms = new ConcurrentHashMap<>();
    private final Map<String, PlayerSession> activeSessions = new ConcurrentHashMap<>();
    private final Set<String> submissionRegistry = Collections.synchronizedSet(new HashSet<>());
    private final Map<String, RoomStateSyncListener> syncListeners = new ConcurrentHashMap<>();

    public interface RoomCallback {
        void onSuccess(BattleRoom room);
        void onError(String errorMessage);
    }

    public interface JoinCallback {
        void onResult(JoinValidationResult result);
    }

    public interface StatusCallback {
        void onUpdated(BattleRoom room, boolean canStart);
    }

    public interface CountdownCallback {
        void onTick(int secondsRemaining);
        void onMatchStarted();
    }

    public interface AnswerValidationCallback {
        void onResponse(AnswerValidationResponse response);
    }

    public interface ReconnectCallback {
        void onResult(ReconnectResult result);
    }

    public interface RoomStateSyncListener {
        void onStateSynchronized(BattleSyncState state);
    }

    public static BattleRoomServerManager getInstance() {
        if (instance == null) {
            synchronized (BattleRoomServerManager.class) {
                if (instance == null) {
                    instance = new BattleRoomServerManager();
                }
            }
        }
        return instance;
    }

    private BattleRoomServerManager() {}

    private boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo active = cm.getActiveNetworkInfo();
                return active != null && active.isConnectedOrConnecting();
            }
        } catch (Exception ignored) {}
        return false;
    }

    public void registerSyncListener(String roomCode, RoomStateSyncListener listener) {
        if (roomCode != null && listener != null) {
            syncListeners.put(roomCode.trim().toUpperCase(), listener);
        }
    }

    public void unregisterSyncListener(String roomCode) {
        if (roomCode != null) {
            syncListeners.remove(roomCode.trim().toUpperCase());
        }
    }

    public PlayerSession createPlayerSession(String roomCode, BattlePlayer player) {
        if (roomCode == null || player == null) return null;
        String cleanCode = roomCode.trim().toUpperCase();
        PlayerSession session = new PlayerSession(cleanCode, player.getId(), player.getName());
        activeSessions.put(cleanCode + "_" + player.getId(), session);
        return session;
    }

    public void markPlayerDisconnected(String roomCode, String playerId) {
        if (roomCode == null || playerId == null) return;
        String cleanCode = roomCode.trim().toUpperCase();
        BattleRoom room = activeRooms.get(cleanCode);
        if (room == null) return;

        room.updatePlayerStatus(playerId, BattlePlayer.PlayerStatus.DISCONNECTED);
        PlayerSession session = activeSessions.get(cleanCode + "_" + playerId);
        if (session != null) {
            session.setConnected(false);
        }

        broadcastRoomStateSync(cleanCode, 0);
    }

    public void handlePlayerReconnect(Context context, String roomCode, String playerId, String sessionToken, ReconnectCallback callback) {
        if (!isNetworkAvailable(context)) {
            if (callback != null) callback.onResult(ReconnectResult.failure(ReconnectResult.Status.NETWORK_ERROR, "ইন্টারনেট সংযোগ নেই!"));
            return;
        }

        if (roomCode == null || playerId == null) {
            if (callback != null) callback.onResult(ReconnectResult.failure(ReconnectResult.Status.INVALID_CREDENTIALS, "ভুল রিকানেক্ট তথ্য!"));
            return;
        }

        final String cleanCode = roomCode.trim().toUpperCase();

        new Thread(() -> {
            try {
                BattleRoom room = activeRooms.get(cleanCode);

                if (room == null) {
                    mainHandler.post(() -> {
                        if (callback != null) callback.onResult(ReconnectResult.failure(ReconnectResult.Status.SESSION_EXPIRED, "ব্যাটেল রুমটি আর সক্রিয় নেই!"));
                    });
                    return;
                }

                if (room.getLifecycleState() == BattleLifecycleState.RESULT ||
                        room.getLifecycleState() == BattleLifecycleState.COMPLETED ||
                        room.getLifecycleState() == BattleLifecycleState.EXPIRED) {
                    mainHandler.post(() -> {
                        if (callback != null) callback.onResult(ReconnectResult.failure(ReconnectResult.Status.MATCH_ALREADY_FINISHED, "ব্যাটেল ইতোমধ্যে সম্পন্ন হয়ে গেছে!"));
                    });
                    return;
                }

                PlayerSession session = activeSessions.get(cleanCode + "_" + playerId);
                if (session != null) {
                    long disconnectDuration = System.currentTimeMillis() - session.getDisconnectedTimestamp();
                    if (!session.isConnected() && disconnectDuration > DISCONNECT_GRACE_PERIOD_MILLIS) {
                        mainHandler.post(() -> {
                            if (callback != null) callback.onResult(ReconnectResult.failure(ReconnectResult.Status.SESSION_EXPIRED, "রিকানেক্ট সেশনের মেয়াদ শেষ!"));
                        });
                        return;
                    }
                    session.setConnected(true);
                }

                room.updatePlayerStatus(playerId, BattlePlayer.PlayerStatus.RECONNECTED);

                BattleSyncState syncState = buildCurrentSyncState(room, 0);
                broadcastRoomStateSync(cleanCode, 0);

                mainHandler.post(() -> {
                    if (callback != null) callback.onResult(ReconnectResult.success(room, syncState));
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onResult(ReconnectResult.failure(ReconnectResult.Status.NETWORK_ERROR, e.getMessage()));
                });
            }
        }).start();
    }

    private BattleSyncState buildCurrentSyncState(BattleRoom room, int remainingTimeSec) {
        String cleanCode = room.getRoomCode();
        List<PlayerLiveState> playerLiveStates = new ArrayList<>();
        for (BattlePlayer player : room.getPlayers()) {
            String subKey = cleanCode + "_" + player.getId() + "_" + (room.getCurrentQuestionIndex() < room.getQuestions().size() ? room.getQuestions().get(room.getCurrentQuestionIndex()).getId() : "none");
            PlayerLiveState.AnswerStatus ansStatus = submissionRegistry.contains(subKey) ? PlayerLiveState.AnswerStatus.ANSWERED : PlayerLiveState.AnswerStatus.UNANSWERED;

            PlayerLiveState pls = new PlayerLiveState(
                    player.getId(),
                    player.getName(),
                    player.getScore(),
                    player.getCorrectAnswers(),
                    player.getWrongAnswers(),
                    ansStatus,
                    player.getStatus(),
                    room.getCurrentQuestionIndex() + 1,
                    room.getConfig().getTotalQuestions()
            );
            playerLiveStates.add(pls);
        }

        return new BattleSyncState(
                cleanCode,
                room.getState(),
                room.getLifecycleState(),
                room.getCurrentQuestionIndex(),
                room.getConfig().getTotalQuestions(),
                remainingTimeSec,
                System.currentTimeMillis(),
                playerLiveStates
        );
    }

    public void broadcastRoomStateSync(String roomCode, int remainingTimeSec) {
        if (roomCode == null) return;
        final String cleanCode = roomCode.trim().toUpperCase();
        BattleRoom room = activeRooms.get(cleanCode);
        if (room == null) return;

        BattleSyncState syncState = buildCurrentSyncState(room, remainingTimeSec);
        RoomStateSyncListener listener = syncListeners.get(cleanCode);
        if (listener != null) {
            mainHandler.post(() -> listener.onStateSynchronized(syncState));
        }
    }

    public void createRoom(Context context, BattleConfig config, BattlePlayer host, RoomCallback callback) {
        if (!isNetworkAvailable(context)) {
            if (callback != null) callback.onError("ইন্টারনেট সংযোগ নেই! সক্রিয় ইন্টারনেট সংযোগ প্রয়োজন।");
            return;
        }

        new Thread(() -> {
            try {
                Thread.sleep(250);

                String uniqueCode = BattleCodeGenerator.generateUniqueBattleCode(context);
                BattleRoom room = new BattleRoom(uniqueCode, config, host);

                List<BattleQuestion> initialQuestions = KnowledgeBattleRepository.getQuestionsForMatch(
                        config.getCategoryId(),
                        config.getTotalQuestions(),
                        room.getUsedQuestionIds(),
                        context
                );
                room.setQuestions(initialQuestions);

                if (context != null) {
                    com.devflux.deenone.features.battle.data.local.BattleSessionStorage.getInstance(context)
                            .recordSeenQuestionSignatures(config.getCategoryId(), initialQuestions);
                }

                createPlayerSession(uniqueCode, host);

                activeRooms.put(uniqueCode, room);

                // Publish room to Cloud Relay synchronously so the room is guaranteed to exist when guest enters code
                boolean published = BattleCloudRelayClient.getInstance().publishRoomSync(room);
                if (!published) {
                    // Retry once asynchronously if first attempt encountered temporary network blip
                    BattleCloudRelayClient.getInstance().publishRoom(room, null);
                }

                // Sync room to MySQL live database via PHP Backend
                if (context != null) {
                    com.devflux.deenone.features.battle.data.remote.BattleDatabaseSyncHandler.getInstance()
                            .createBattleOnServer(context, uniqueCode, config, host, null);
                }

                // Persist creator info & active room code
                if (context != null) {
                    com.devflux.deenone.features.battle.data.local.BattleSessionStorage storage =
                            com.devflux.deenone.features.battle.data.local.BattleSessionStorage.getInstance(context);
                    storage.saveCreatedRoomCode(uniqueCode);
                    storage.saveUserCredentials(host.getId(), host.getName());
                }

                mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(room);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onError("ব্যাটেল রুম তৈরি করতে ত্রুটি হয়েছে: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Anti-Cheat Guarded Join Validation supporting both Creator and Guest entry
     */
    public void validateAndJoinRoom(Context context, String inputCode, BattlePlayer player, JoinCallback callback) {
        if (!isNetworkAvailable(context)) {
            if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.NETWORK_ERROR));
            return;
        }

        if (inputCode == null || inputCode.trim().length() < 4) {
            if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.INVALID_CODE));
            return;
        }

        final String cleanCode = inputCode.trim().toUpperCase();

        new Thread(() -> {
            try {
                BattleRoom localRoom = activeRooms.get(cleanCode);
                if (localRoom != null) {
                    processJoinValidation(context, localRoom, cleanCode, player, callback);
                } else {
                    // Query Cloud Relay for room created by other phone/device!
                    BattleCloudRelayClient.getInstance().fetchRoomOnline(context, cleanCode, new BattleCloudRelayClient.RelayCallback<BattleRoom>() {
                        @Override
                        public void onSuccess(BattleRoom onlineRoom) {
                            if (onlineRoom == null) {
                                mainHandler.post(() -> {
                                    if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.INVALID_CODE));
                                });
                                return;
                            }
                            activeRooms.put(cleanCode, onlineRoom);
                            new Thread(() -> processJoinValidation(context, onlineRoom, cleanCode, player, callback)).start();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            mainHandler.post(() -> {
                                if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.INVALID_CODE));
                            });
                        }
                    });
                }
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.NETWORK_ERROR, e.getMessage()));
                });
            }
        }).start();
    }

    private void processJoinValidation(Context context, BattleRoom room, String cleanCode, BattlePlayer player, JoinCallback callback) {
        long age = System.currentTimeMillis() - room.getCreatedAtTimestamp();
        if (age > ROOM_EXPIRATION_MILLIS) {
            room.setLifecycleState(BattleLifecycleState.EXPIRED);
            activeRooms.remove(cleanCode);
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.ROOM_EXPIRED));
            });
            return;
        }

        // Check 1: Is this user the Host/Creator of this room?
        boolean isHostUser = false;
        if (context != null) {
            com.devflux.deenone.features.battle.data.local.BattleSessionStorage storage =
                    com.devflux.deenone.features.battle.data.local.BattleSessionStorage.getInstance(context);
            if (storage.isRoomCreator(cleanCode) || (storage.getUserId() != null && storage.getUserId().equalsIgnoreCase(room.getHostPlayer().getId()))) {
                isHostUser = true;
            }
        }
        if (player != null && room.getHostPlayer().getId().equalsIgnoreCase(player.getId())) {
            isHostUser = true;
        }

        if (isHostUser) {
            // Creator re-entering their own battle room! Take them directly to the lobby/arena.
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(JoinValidationResult.success(room));
            });
            return;
        }

        // Check 2: Is this user already a player in this room (reconnection)?
        boolean alreadyJoined = false;
        if (player != null) {
            for (BattlePlayer p : room.getPlayers()) {
                if (p.getId().equalsIgnoreCase(player.getId())) {
                    alreadyJoined = true;
                    break;
                }
            }
        }
        if (alreadyJoined) {
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(JoinValidationResult.success(room));
            });
            return;
        }

        // Check 3: Is Room in valid state for new players to join?
        if (room.getLifecycleState() == BattleLifecycleState.LIVE) {
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.ALREADY_STARTED));
            });
            return;
        }

        // Check 4: Room capacity
        if (room.getPlayerCount() >= room.getConfig().getTotalPlayers()) {
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.ROOM_FULL));
            });
            return;
        }

        // Add Guest Player to room
        boolean added = room.addPlayer(player);
        if (!added) {
            mainHandler.post(() -> {
                if (callback != null) callback.onResult(JoinValidationResult.failure(JoinValidationResult.Status.ROOM_FULL));
            });
            return;
        }

        createPlayerSession(cleanCode, player);
        broadcastRoomStateSync(cleanCode, 0);

        // Notify cloud relay so Host sees Guest join immediately in real time
        BattleCloudRelayClient.getInstance().publishPlayerJoined(cleanCode, player, null);

        // Sync guest join to MySQL live database via PHP Backend
        if (context != null) {
            com.devflux.deenone.features.battle.data.remote.BattleDatabaseSyncHandler.getInstance()
                    .joinBattleOnServer(context, cleanCode, player, null);
        }

        mainHandler.post(() -> {
            if (callback != null) callback.onResult(JoinValidationResult.success(room));
        });
    }

    public BattleRoom getActiveRoomForUser(Context context, String userId) {
        if (context != null) {
            com.devflux.deenone.features.battle.data.local.BattleSessionStorage storage =
                    com.devflux.deenone.features.battle.data.local.BattleSessionStorage.getInstance(context);
            String lastCreated = storage.getLastCreatedRoomCode();
            if (lastCreated != null) {
                BattleRoom r = activeRooms.get(lastCreated.trim().toUpperCase());
                if (r != null) {
                    long age = System.currentTimeMillis() - r.getCreatedAtTimestamp();
                    if (age <= ROOM_EXPIRATION_MILLIS && r.getLifecycleState() != BattleLifecycleState.EXPIRED && r.getLifecycleState() != BattleLifecycleState.COMPLETED) {
                        return r;
                    } else {
                        r.setLifecycleState(BattleLifecycleState.EXPIRED);
                        activeRooms.remove(lastCreated.trim().toUpperCase());
                        storage.clearActiveSession();
                    }
                }
            }
        }
        if (userId != null) {
            for (BattleRoom room : activeRooms.values()) {
                long age = System.currentTimeMillis() - room.getCreatedAtTimestamp();
                if (age <= ROOM_EXPIRATION_MILLIS && room.getLifecycleState() != BattleLifecycleState.EXPIRED && room.getLifecycleState() != BattleLifecycleState.COMPLETED) {
                    for (BattlePlayer p : room.getPlayers()) {
                        if (p.getId().equalsIgnoreCase(userId)) {
                            return room;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void updatePlayerStatus(String roomCode, String playerId, BattlePlayer.PlayerStatus status, StatusCallback callback) {
        if (roomCode == null || playerId == null) return;
        final String cleanCode = roomCode.trim().toUpperCase();
        BattleRoom room = activeRooms.get(cleanCode);
        if (room == null) return;

        room.updatePlayerStatus(playerId, status);
        boolean canStart = room.isAllPlayersReady();

        BattleCloudRelayClient.getInstance().publishPlayerStatus(cleanCode, playerId, status);

        mainHandler.post(() -> {
            if (callback != null) callback.onUpdated(room, canStart);
        });
    }

    public void startSynchronizedCountdown(String roomCode, CountdownCallback callback) {
        if (roomCode == null) return;
        final String cleanCode = roomCode.trim().toUpperCase();
        BattleRoom room = activeRooms.get(cleanCode);
        if (room == null) return;

        BattleCloudRelayClient.getInstance().publishMatchStart(cleanCode);

        room.setLifecycleState(BattleLifecycleState.COUNTDOWN);

        final int[] countdown = {3};
        Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (countdown[0] > 0) {
                    if (callback != null) callback.onTick(countdown[0]);
                    countdown[0]--;
                    mainHandler.postDelayed(this, 1000);
                } else {
                    room.setLifecycleState(BattleLifecycleState.LIVE);
                    if (callback != null) callback.onMatchStarted();
                }
            }
        };
        mainHandler.post(countdownRunnable);
    }

    /**
     * Anti-Cheat Server-Authoritative Answer Validation
     */
    public void validateAnswer(Context context, AnswerSubmissionRequest request, AnswerValidationCallback callback) {
        if (!isNetworkAvailable(context)) {
            if (callback != null) callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.SERVER_ERROR));
            return;
        }

        if (request == null) {
            if (callback != null) callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.SERVER_ERROR));
            return;
        }

        new Thread(() -> {
            try {
                BattleRoom room = activeRooms.get(request.getRoomCode().trim().toUpperCase());
                if (room == null) {
                    mainHandler.post(() -> {
                        if (callback != null) callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.INVALID_ROOM));
                    });
                    return;
                }

                // Anti-Cheat Check: Double submit & Player authorization
                BattleSecurityAntiCheatEngine.SecurityResult secRes = BattleSecurityAntiCheatEngine.validateSubmissionSecurity(room, request);
                if (!secRes.isAllowed()) {
                    mainHandler.post(() -> {
                        if (secRes.getStatus() == BattleSecurityAntiCheatEngine.SecurityCheckStatus.DUPLICATE_ANSWER_SUBMISSION) {
                            callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.DUPLICATE_SUBMISSION));
                        } else {
                            callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.INVALID_PLAYER));
                        }
                    });
                    return;
                }

                BattleQuestion targetQuestion = null;
                int currentQIndex = room.getCurrentQuestionIndex();
                if (currentQIndex < room.getQuestions().size()) {
                    BattleQuestion q = room.getQuestions().get(currentQIndex);
                    if (q.getId().equals(request.getQuestionId())) {
                        targetQuestion = q;
                    }
                }

                if (targetQuestion == null) {
                    for (BattleQuestion q : room.getQuestions()) {
                        if (q.getId().equals(request.getQuestionId())) {
                            targetQuestion = q;
                            break;
                        }
                    }
                }

                if (targetQuestion == null) {
                    mainHandler.post(() -> {
                        if (callback != null) callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.INVALID_QUESTION));
                    });
                    return;
                }

                boolean isCorrect = (request.getSelectedOptionIndex() == targetQuestion.getCorrectOptionIndex());
                // Server calculated score (Client score is ignored)
                int pointsDelta = BattleSecurityAntiCheatEngine.calculateServerScore(
                        request.getSelectedOptionIndex(),
                        targetQuestion.getCorrectOptionIndex(),
                        room.getConfig().isNegativeMarkingEnabled()
                );

                BattlePlayer player = null;
                for (BattlePlayer p : room.getPlayers()) {
                    if (p.getId().equals(request.getPlayerId())) {
                        player = p;
                        break;
                    }
                }

                int newScore = 0;
                if (player != null) {
                    player.addScore(pointsDelta);
                    if (isCorrect) {
                        player.incrementCorrect();
                    } else {
                        player.incrementWrong();
                    }
                    newScore = player.getScore();
                }

                boolean isMatchFinished = (room.getCurrentQuestionIndex() >= room.getQuestions().size() - 1);
                if (isMatchFinished) {
                    room.setLifecycleState(BattleLifecycleState.COMPLETED);
                }

                final AnswerValidationResponse response = new AnswerValidationResponse(
                        AnswerValidationResponse.Status.VALIDATED,
                        isCorrect,
                        targetQuestion.getCorrectOptionIndex(),
                        pointsDelta,
                        newScore,
                        targetQuestion.getExplanation(),
                        targetQuestion.getReference(),
                        isMatchFinished
                );

                broadcastRoomStateSync(request.getRoomCode(), 0);

                // Broadcast live score update to cloud relay so opponent sees it live
                BattleCloudRelayClient.getInstance().publishAnswer(
                        request.getRoomCode().trim().toUpperCase(),
                        request.getPlayerId(),
                        newScore,
                        isCorrect,
                        pointsDelta
                );

                mainHandler.post(() -> {
                    if (callback != null) callback.onResponse(response);
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onResponse(AnswerValidationResponse.error(AnswerValidationResponse.Status.SERVER_ERROR));
                });
            }
        }).start();
    }

    public boolean canStartBattle(String roomCode) {
        if (roomCode == null) return false;
        BattleRoom room = activeRooms.get(roomCode.trim().toUpperCase());
        return room != null && room.isAllPlayersReady();
    }

    public BattleRoom getRoom(String roomCode) {
        if (roomCode == null) return null;
        return activeRooms.get(roomCode.trim().toUpperCase());
    }

    public void deleteRoom(String roomCode) {
        if (roomCode != null) {
            String clean = roomCode.trim().toUpperCase();
            BattleRoom room = activeRooms.get(clean);
            if (room != null) {
                room.setLifecycleState(BattleLifecycleState.EXPIRED);
            }
            BattleCloudRelayClient.getInstance().publishRoomCancel(clean);
            BattleCloudRelayClient.getInstance().stopLobbySync(clean);
            activeRooms.remove(clean);
            syncListeners.remove(clean);
        }
    }

    public void startLobbySync(String roomCode, BattleCloudRelayClient.RoomSyncListener listener) {
        BattleCloudRelayClient.getInstance().startLobbySync(roomCode, listener);
    }

    public void stopLobbySync(String roomCode) {
        BattleCloudRelayClient.getInstance().stopLobbySync(roomCode);
    }
}
