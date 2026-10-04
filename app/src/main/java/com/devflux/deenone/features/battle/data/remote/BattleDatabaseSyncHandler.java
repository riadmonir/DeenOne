package com.devflux.deenone.features.battle.data.remote;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattlePlayer;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Android Client-to-MySQL Backend Network Interface.
 * Interfaces with PHP Backend and MySQL Database for persistent room creation,
 * join validation, answer submission, and reconnect sessions.
 */
public class BattleDatabaseSyncHandler {

    private static volatile BattleDatabaseSyncHandler instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface ApiCallback {
        void onSuccess(JSONObject jsonResponse);
        void onError(String errorMessage);
    }

    public static BattleDatabaseSyncHandler getInstance() {
        if (instance == null) {
            synchronized (BattleDatabaseSyncHandler.class) {
                if (instance == null) {
                    instance = new BattleDatabaseSyncHandler();
                }
            }
        }
        return instance;
    }

    private BattleDatabaseSyncHandler() {}

    public void createBattleOnServer(Context context, String roomCode, BattleConfig config, BattlePlayer host, ApiCallback callback) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                if (roomCode != null && !roomCode.trim().isEmpty()) {
                    payload.put("room_code", roomCode.trim().toUpperCase());
                    payload.put("battle_code", roomCode.trim().toUpperCase());
                }
                payload.put("user_id", host.getId());
                payload.put("username", host.getName());
                payload.put("avatar", host.getAvatarResource());

                JSONObject hostObj = new JSONObject();
                hostObj.put("id", host.getId());
                hostObj.put("name", host.getName());
                hostObj.put("avatar_resource", host.getAvatarResource() != null ? host.getAvatarResource() : "avatar_1");
                payload.put("host_player", hostObj);

                payload.put("mode", config.getMode().getLabel());
                payload.put("total_players", config.getTotalPlayers());
                payload.put("max_players", config.getTotalPlayers());
                payload.put("total_questions", config.getTotalQuestions());
                payload.put("question_count", config.getTotalQuestions());
                payload.put("time_per_question_sec", config.getTimePerQuestionSec());
                payload.put("question_time", config.getTimePerQuestionSec());
                payload.put("category_id", config.getCategoryId());
                payload.put("category_title_bn", config.getCategoryTitleBn());
                payload.put("difficulty", config.getDifficulty().name());
                payload.put("negative_marking", config.isNegativeMarkingEnabled() ? 1 : 0);

                String endpoint = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(context, "create_room.php");
                JSONObject res = postJson(endpoint, payload);
                mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(res);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
            }
        }).start();
    }

    public void leaveBattleOnServer(Context context, String roomCode, String playerId, ApiCallback callback) {
        if (roomCode == null || playerId == null) return;
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("room_code", roomCode.trim().toUpperCase());
                payload.put("player_id", playerId.trim());

                String endpoint = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(context, "leave_room.php");
                JSONObject res = postJson(endpoint, payload);
                mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(res);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
            }
        }).start();
    }

    public void joinBattleOnServer(Context context, String battleCode, BattlePlayer guest, ApiCallback callback) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("room_code", battleCode);
                payload.put("battle_code", battleCode);
                payload.put("user_id", guest.getId());
                payload.put("username", guest.getName());
                payload.put("avatar", guest.getAvatarResource());

                JSONObject playerObj = new JSONObject();
                playerObj.put("id", guest.getId());
                playerObj.put("name", guest.getName());
                playerObj.put("avatar_resource", guest.getAvatarResource() != null ? guest.getAvatarResource() : "avatar_1");
                payload.put("player", playerObj);

                String endpoint = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(context, "join_room.php");
                JSONObject res = postJson(endpoint, payload);
                mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(res);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
            }
        }).start();
    }

    public void updateRoomStateOnServer(Context context, String roomCode, String lifecycleState, String winnerPlayerId, String winnerName, String scoresJson, ApiCallback callback) {
        if (roomCode == null || roomCode.trim().isEmpty()) return;
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("room_code", roomCode.trim().toUpperCase());
                payload.put("lifecycle_state", lifecycleState);
                if (winnerPlayerId != null) payload.put("winner_player_id", winnerPlayerId);
                if (winnerName != null) payload.put("winner_name", winnerName);
                if (scoresJson != null) payload.put("final_scores_json", scoresJson);

                String endpoint = com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(context, "update_room.php");
                JSONObject res = postJson(endpoint, payload);
                mainHandler.post(() -> {
                    if (callback != null) callback.onSuccess(res);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onError(e.getMessage());
                });
            }
        }).start();
    }

    private JSONObject postJson(String endpoint, JSONObject jsonBody) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = conn.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line.trim());
        }
        return new JSONObject(sb.toString());
    }
}
