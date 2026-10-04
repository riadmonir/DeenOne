package com.devflux.deenone.features.battle.data.remote;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleRoom;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Enterprise Zero-Config Real-Time Cloud Relay Engine for Knowledge Battles.
 * Connects multiple independent devices across cellular (4G/5G) and Wi-Fi networks
 * via secure, isolated, and instant cloud pub/sub channels without requiring external database setup.
 */
public class BattleCloudRelayClient {

    private static final String TAG = "BattleCloudRelay";
    private static final String RELAY_BASE_URL = "https://ntfy.sh/deenone_battle_";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    public static final String ACTION_ROOM_CREATE = "ROOM_CREATE";
    public static final String ACTION_PLAYER_JOIN = "PLAYER_JOIN";
    public static final String ACTION_PLAYER_STATUS = "PLAYER_STATUS";
    public static final String ACTION_MATCH_START = "MATCH_START";
    public static final String ACTION_ANSWER_UPDATE = "ANSWER_UPDATE";
    public static final String ACTION_ROOM_CANCEL = "ROOM_CANCEL";

    private static volatile BattleCloudRelayClient instance;
    private final OkHttpClient httpClient;
    private final Handler mainHandler;
    private final ScheduledExecutorService pollerExecutor = Executors.newScheduledThreadPool(2);
    private final Map<String, ScheduledFuture<?>> activePollers = new ConcurrentHashMap<>();
    private final Map<String, RoomSyncListener> activeListeners = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> processedMessageIds = new ConcurrentHashMap<>();

    public interface RelayCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public interface RoomSyncListener {
        void onPlayerJoined(BattlePlayer player);
        void onPlayerStatusChanged(String playerId, BattlePlayer.PlayerStatus status);
        void onMatchStarted();
        void onOpponentAnswer(String playerId, int score, boolean isCorrect, int pointsDelta);
        void onRoomCancelled();
    }

    public static BattleCloudRelayClient getInstance() {
        if (instance == null) {
            synchronized (BattleCloudRelayClient.class) {
                if (instance == null) {
                    instance = new BattleCloudRelayClient();
                }
            }
        }
        return instance;
    }

    private BattleCloudRelayClient() {
        Handler h = null;
        try {
            if (Looper.getMainLooper() != null) {
                h = new Handler(Looper.getMainLooper());
            }
        } catch (Throwable ignored) {
            h = null;
        }
        this.mainHandler = h;

        this.httpClient = new OkHttpClient.Builder()
                .dns(hostname -> {
                    try {
                        java.util.List<java.net.InetAddress> addresses = okhttp3.Dns.SYSTEM.lookup(hostname);
                        java.util.List<java.net.InetAddress> ipv4 = new java.util.ArrayList<>();
                        java.util.List<java.net.InetAddress> ipv6 = new java.util.ArrayList<>();
                        for (java.net.InetAddress addr : addresses) {
                            if (addr instanceof java.net.Inet4Address) {
                                ipv4.add(addr);
                            } else {
                                ipv6.add(addr);
                            }
                        }
                        java.util.List<java.net.InetAddress> result = new java.util.ArrayList<>();
                        result.addAll(ipv4);
                        result.addAll(ipv6);
                        return result.isEmpty() ? addresses : result;
                    } catch (Exception e) {
                        return okhttp3.Dns.SYSTEM.lookup(hostname);
                    }
                })
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    private void postToMain(Runnable r) {
        if (r == null) return;
        if (mainHandler != null && isAndroidRuntime()) {
            mainHandler.post(r);
        } else {
            r.run();
        }
    }

    private static boolean isAndroidRuntime() {
        try {
            String vendor = System.getProperty("java.vendor", "");
            return vendor.contains("Android") || "The Android Project".equals(vendor);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private String getTopicUrl(String roomCode) {
        return RELAY_BASE_URL + roomCode.trim().toLowerCase();
    }

    public boolean publishRoomSync(BattleRoom room) {
        if (room == null) return false;
        try {
            JSONObject payload = new JSONObject();
            payload.put("action", ACTION_ROOM_CREATE);
            payload.put("room_code", room.getRoomCode());
            payload.put("timestamp", System.currentTimeMillis());
            payload.put("room_data", BattleRoomJsonConverter.serializeRoom(room));

            return sendCloudMessage(room.getRoomCode(), "ROOM_CREATED", payload.toString());
        } catch (Exception e) {
            Log.e(TAG, "Error publishing room synchronously", e);
            return false;
        }
    }

    /**
     * Publishes a newly created BattleRoom to the online cloud relay.
     */
    public void publishRoom(BattleRoom room, RelayCallback<Boolean> callback) {
        if (room == null) {
            if (callback != null) callback.onError("Room is null");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                boolean success = publishRoomSync(room);
                postToMain(() -> {
                    if (callback != null) {
                        if (success) callback.onSuccess(true);
                        else callback.onError("ক্লাউড রিলেতে রুম পোস্ট করা যায়নি!");
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error publishing room", e);
                postToMain(() -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
            }
        });
    }

    public void fetchRoomOnline(String roomCode, RelayCallback<BattleRoom> callback) {
        fetchRoomOnline(null, roomCode, callback);
    }

    /**
     * Queries the cloud relay to find an active room by code.
     * Uses since=all to immediately retrieve all cached events.
     * Retries up to 8 times with progressive backoff (~9s window) to ensure seamless joining across variable mobile latencies.
     */
    public void fetchRoomOnline(android.content.Context context, String roomCode, RelayCallback<BattleRoom> callback) {
        if (roomCode == null || roomCode.trim().length() < 4) {
            if (callback != null) callback.onSuccess(null);
            return;
        }

        final String cleanCode = roomCode.trim().toUpperCase();

        Executors.newSingleThreadExecutor().execute(() -> {
            BattleRoom hydratedRoom = null;
            int maxAttempts = 8;

            for (int attempt = 1; attempt <= maxAttempts && hydratedRoom == null; attempt++) {
                try {
                    String pollUrl = getTopicUrl(cleanCode) + "/json?poll=1&since=all";
                    Request req = new Request.Builder()
                            .url(pollUrl)
                            .get()
                            .build();

                    try (Response response = httpClient.newCall(req).execute()) {
                        if (response.isSuccessful() && response.body() != null) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));
                            String line;
                            Set<String> processedMsgIds = processedMessageIds.computeIfAbsent(cleanCode, k -> Collections.synchronizedSet(new HashSet<>()));

                            while ((line = reader.readLine()) != null) {
                                if (line.trim().isEmpty()) continue;
                                try {
                                    JSONObject eventObj = new JSONObject(line);
                                    String msgId = eventObj.optString("id", "");
                                    if (!msgId.isEmpty()) {
                                        processedMsgIds.add(msgId);
                                    }

                                    String messageBody = eventObj.optString("message", "");

                                    // Resolve attachment if message was bundled as a file
                                    if (eventObj.has("attachment")) {
                                        JSONObject att = eventObj.optJSONObject("attachment");
                                        if (att != null) {
                                            String attUrl = att.optString("url", "");
                                            if (!attUrl.isEmpty()) {
                                                try {
                                                    Request attReq = new Request.Builder().url(attUrl).get().build();
                                                    try (Response attRes = httpClient.newCall(attReq).execute()) {
                                                        if (attRes.isSuccessful() && attRes.body() != null) {
                                                            messageBody = attRes.body().string();
                                                        }
                                                    }
                                                } catch (Exception ignored) {}
                                            }
                                        }
                                    }

                                    if (messageBody.isEmpty() || messageBody.startsWith("You received a file:")) continue;

                                    JSONObject actionObj = new JSONObject(messageBody);
                                    String action = actionObj.optString("action", "");

                                    if (ACTION_ROOM_CREATE.equals(action)) {
                                        JSONObject roomData = actionObj.optJSONObject("room_data");
                                        if (roomData != null) {
                                            hydratedRoom = BattleRoomJsonConverter.deserializeRoom(roomData, context);
                                        }
                                    } else if (ACTION_PLAYER_JOIN.equals(action) && hydratedRoom != null) {
                                        JSONObject playerObj = actionObj.optJSONObject("player");
                                        if (playerObj != null) {
                                            BattlePlayer joinedPlayer = BattleRoomJsonConverter.deserializePlayer(playerObj);
                                            if (joinedPlayer != null) {
                                                hydratedRoom.addPlayer(joinedPlayer);
                                            }
                                        }
                                    } else if (ACTION_PLAYER_STATUS.equals(action) && hydratedRoom != null) {
                                        String pId = actionObj.optString("player_id", "");
                                        String statusStr = actionObj.optString("status", "");
                                        if (!pId.isEmpty() && !statusStr.isEmpty()) {
                                            try {
                                                hydratedRoom.updatePlayerStatus(pId, BattlePlayer.PlayerStatus.valueOf(statusStr));
                                            } catch (Exception ignored) {}
                                        }
                                    } else if (ACTION_ROOM_CANCEL.equals(action)) {
                                        hydratedRoom = null; // Room was cancelled
                                    }
                                } catch (Exception lineEx) {
                                    Log.w(TAG, "Error parsing event line: " + lineEx.getMessage(), lineEx);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Attempt " + attempt + " querying room online failed: " + e.getMessage(), e);
                }

                if (hydratedRoom == null && attempt < maxAttempts) {
                    try {
                        long delay = Math.min(2000, 300 + attempt * 250);
                        Thread.sleep(delay);
                    } catch (InterruptedException ignored) {}
                }
            }

            final BattleRoom finalRoom = hydratedRoom;
            postToMain(() -> {
                if (callback != null) callback.onSuccess(finalRoom);
            });
        });
    }

    /**
     * Publishes a Guest player joining the room.
     */
    public void publishPlayerJoined(String roomCode, BattlePlayer guest, RelayCallback<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("action", ACTION_PLAYER_JOIN);
                payload.put("room_code", roomCode);
                payload.put("timestamp", System.currentTimeMillis());
                payload.put("player", BattleRoomJsonConverter.serializePlayer(guest));

                boolean ok = sendCloudMessage(roomCode, "PLAYER_JOINED", payload.toString());
                postToMain(() -> {
                    if (callback != null) callback.onSuccess(ok);
                });
            } catch (Exception e) {
                postToMain(() -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
            }
        });
    }

    /**
     * Publishes player ready status update.
     */
    public void publishPlayerStatus(String roomCode, String playerId, BattlePlayer.PlayerStatus status) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("action", ACTION_PLAYER_STATUS);
                payload.put("room_code", roomCode);
                payload.put("player_id", playerId);
                payload.put("status", status != null ? status.name() : "READY");
                payload.put("timestamp", System.currentTimeMillis());

                sendCloudMessage(roomCode, "STATUS_UPDATE", payload.toString());
            } catch (Exception ignored) {}
        });
    }

    /**
     * Publishes Match Start signal to synchronize countdown and arena entrance on all devices.
     */
    public void publishMatchStart(String roomCode) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("action", ACTION_MATCH_START);
                payload.put("room_code", roomCode);
                payload.put("timestamp", System.currentTimeMillis());

                sendCloudMessage(roomCode, "MATCH_START", payload.toString());
            } catch (Exception ignored) {}
        });
    }

    /**
     * Publishes real-time score updates during live match.
     */
    public void publishAnswer(String roomCode, String playerId, int newScore, boolean isCorrect, int pointsDelta) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("action", ACTION_ANSWER_UPDATE);
                payload.put("room_code", roomCode);
                payload.put("player_id", playerId);
                payload.put("score", newScore);
                payload.put("is_correct", isCorrect);
                payload.put("points_delta", pointsDelta);
                payload.put("timestamp", System.currentTimeMillis());

                sendCloudMessage(roomCode, "ANSWER_SUBMITTED", payload.toString());
            } catch (Exception ignored) {}
        });
    }

    /**
     * Publishes Room Cancellation event.
     */
    public void publishRoomCancel(String roomCode) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("action", ACTION_ROOM_CANCEL);
                payload.put("room_code", roomCode);
                payload.put("timestamp", System.currentTimeMillis());

                sendCloudMessage(roomCode, "ROOM_CANCELLED", payload.toString());
            } catch (Exception ignored) {}
        });
    }

    /**
     * Starts lightweight lobby poller (every 1.5 seconds) to receive live join, status, and match start events.
     */
    public void startLobbySync(String roomCode, RoomSyncListener listener) {
        if (roomCode == null || listener == null) return;
        final String cleanCode = roomCode.trim().toUpperCase();

        stopLobbySync(cleanCode);
        activeListeners.put(cleanCode, listener);

        Set<String> seenIds = processedMessageIds.computeIfAbsent(cleanCode, k -> Collections.synchronizedSet(new HashSet<>()));

        ScheduledFuture<?> future = pollerExecutor.scheduleWithFixedDelay(() -> {
            try {
                String pollUrl = getTopicUrl(cleanCode) + "/json?poll=1&since=all";
                Request req = new Request.Builder().url(pollUrl).get().build();

                try (Response response = httpClient.newCall(req).execute()) {
                    if (!response.isSuccessful() || response.body() == null) return;

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        try {
                            JSONObject eventObj = new JSONObject(line);
                            String msgId = eventObj.optString("id", "");
                            if (!msgId.isEmpty() && seenIds.contains(msgId)) {
                                continue; // Already processed
                            }
                            if (!msgId.isEmpty()) {
                                seenIds.add(msgId);
                            }

                            String messageBody = eventObj.optString("message", "");
                            if (messageBody.isEmpty()) continue;

                            JSONObject actionObj = new JSONObject(messageBody);
                            String action = actionObj.optString("action", "");

                            RoomSyncListener l = activeListeners.get(cleanCode);
                            if (l == null) continue;

                            if (ACTION_PLAYER_JOIN.equals(action)) {
                                JSONObject pObj = actionObj.optJSONObject("player");
                                if (pObj != null) {
                                    BattlePlayer joinedPlayer = BattleRoomJsonConverter.deserializePlayer(pObj);
                                    if (joinedPlayer != null) {
                                        postToMain(() -> l.onPlayerJoined(joinedPlayer));
                                    }
                                }
                            } else if (ACTION_PLAYER_STATUS.equals(action)) {
                                String pId = actionObj.optString("player_id", "");
                                String statusStr = actionObj.optString("status", "READY");
                                try {
                                    BattlePlayer.PlayerStatus st = BattlePlayer.PlayerStatus.valueOf(statusStr);
                                    postToMain(() -> l.onPlayerStatusChanged(pId, st));
                                } catch (Exception ignored) {}
                            } else if (ACTION_MATCH_START.equals(action)) {
                                postToMain(l::onMatchStarted);
                            } else if (ACTION_ANSWER_UPDATE.equals(action)) {
                                String pId = actionObj.optString("player_id", "");
                                int score = actionObj.optInt("score", 0);
                                boolean isCorrect = actionObj.optBoolean("is_correct", false);
                                int delta = actionObj.optInt("points_delta", 0);
                                postToMain(() -> l.onOpponentAnswer(pId, score, isCorrect, delta));
                            } else if (ACTION_ROOM_CANCEL.equals(action)) {
                                postToMain(l::onRoomCancelled);
                            }
                        } catch (Exception ignored) {}
                    }
                }
            } catch (Exception ignored) {}
        }, 500, 1500, TimeUnit.MILLISECONDS);

        activePollers.put(cleanCode, future);
    }

    /**
     * Stops the active lobby poller and clears listeners.
     */
    public void stopLobbySync(String roomCode) {
        if (roomCode == null) return;
        String cleanCode = roomCode.trim().toUpperCase();

        ScheduledFuture<?> future = activePollers.remove(cleanCode);
        if (future != null) {
            future.cancel(true);
        }
        activeListeners.remove(cleanCode);
    }

    private boolean sendCloudMessage(String roomCode, String title, String body) {
        String topicUrl = getTopicUrl(roomCode);
        RequestBody requestBody = RequestBody.create(body, JSON_MEDIA_TYPE);
        Request req = new Request.Builder()
                .url(topicUrl)
                .addHeader("Title", title)
                .post(requestBody)
                .build();

        for (int attempt = 1; attempt <= 3; attempt++) {
            try (Response res = httpClient.newCall(req).execute()) {
                if (res.isSuccessful()) {
                    return true;
                }
            } catch (Exception e) {
                Log.w(TAG, "Attempt " + attempt + " sending cloud message to " + roomCode + " failed: " + e.getMessage());
            }
            if (attempt < 3) {
                try { Thread.sleep(400); } catch (InterruptedException ignored) {}
            }
        }
        return false;
    }
}
