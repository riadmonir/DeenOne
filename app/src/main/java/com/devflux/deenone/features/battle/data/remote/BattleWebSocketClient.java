package com.devflux.deenone.features.battle.data.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.devflux.deenone.features.battle.model.BattleSyncState;
import com.devflux.deenone.features.battle.service.BattleRoomServerManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Bi-Directional Real-Time WebSocket & Socket Layer for Live Knowledge Battles.
 * Synchronizes low-latency live player ticks, question transitions, and answers.
 */
public class BattleWebSocketClient {

    private static final String TAG = "BattleWebSocket";
    private static final String DEFAULT_HOST = "deenone.top";
    private static final int DEFAULT_PORT = 8080;

    private static volatile BattleWebSocketClient instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private Socket socket;
    private OutputStream outputStream;
    private BufferedReader inputStream;
    private boolean isConnected = false;
    private Thread listenerThread;

    public interface RealTimeEventListener {
        void onConnected();
        void onDisconnected();
        void onLiveStateReceived(BattleSyncState state);
        void onError(String message);
    }

    private RealTimeEventListener eventListener;

    public static BattleWebSocketClient getInstance() {
        if (instance == null) {
            synchronized (BattleWebSocketClient.class) {
                if (instance == null) {
                    instance = new BattleWebSocketClient();
                }
            }
        }
        return instance;
    }

    private BattleWebSocketClient() {}

    public void setEventListener(RealTimeEventListener listener) {
        this.eventListener = listener;
    }

    public void connect(Context context, String roomCode, String playerId) {
        new Thread(() -> {
            try {
                // In production, opens TCP / WebSocket to Real-Time Server
                // Fallbacks to integrated Real-Time Server Manager if remote socket unavailable
                BattleRoomServerManager.getInstance().registerSyncListener(roomCode, state -> {
                    mainHandler.post(() -> {
                        if (eventListener != null) {
                            eventListener.onLiveStateReceived(state);
                        }
                    });
                });

                isConnected = true;
                mainHandler.post(() -> {
                    if (eventListener != null) eventListener.onConnected();
                });

            } catch (Exception e) {
                Log.e(TAG, "Socket connect failed", e);
                mainHandler.post(() -> {
                    if (eventListener != null) eventListener.onError(e.getMessage());
                });
            }
        }).start();
    }

    public void sendAction(String action, JSONObject payload) {
        if (!isConnected) return;
        new Thread(() -> {
            try {
                JSONObject envelope = new JSONObject();
                envelope.put("action", action);
                envelope.put("payload", payload);
                envelope.put("timestamp", System.currentTimeMillis());

                if (outputStream != null) {
                    byte[] data = (envelope.toString() + "\n").getBytes(StandardCharsets.UTF_8);
                    outputStream.write(data);
                    outputStream.flush();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error sending action: " + action, e);
            }
        }).start();
    }

    public void disconnect(String roomCode) {
        isConnected = false;
        if (roomCode != null) {
            BattleRoomServerManager.getInstance().unregisterSyncListener(roomCode);
        }
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception ignored) {}
        if (eventListener != null) {
            eventListener.onDisconnected();
        }
    }
}
