package com.devflux.deenone.features.battle.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.BattleHistoryEntity;
import com.devflux.deenone.features.battle.model.BattleFinalSummary;
import com.devflux.deenone.features.battle.model.PlayerBattleResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.devflux.deenone.features.battle.model.BattleQuestion;

/**
 * Local Storage & Cache Manager for Knowledge Battles.
 * Manages Room Database match history records, SharedPreferences user credentials,
 * and session state for instant offline access and reconnection.
 */
public class BattleSessionStorage {

    private static final String PREF_NAME = "deenone_battle_prefs";
    private static final String KEY_USER_ID = "pref_battle_user_id";
    private static final String KEY_USER_NAME = "pref_battle_user_name";
    private static final String KEY_LAST_ROOM_CODE = "pref_last_room_code";
    private static final String KEY_SESSION_TOKEN = "pref_session_token";

    private static volatile BattleSessionStorage instance;
    private final SharedPreferences prefs;
    private final AppDatabase db;

    public static BattleSessionStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (BattleSessionStorage.class) {
                if (instance == null) {
                    instance = new BattleSessionStorage(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private BattleSessionStorage(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.db = AppDatabase.getInstance(context);
    }

    public void saveUserCredentials(String userId, String userName) {
        prefs.edit()
                .putString(KEY_USER_ID, userId)
                .putString(KEY_USER_NAME, userName)
                .apply();
    }

    public String getUserId() {
        String id = prefs.getString(KEY_USER_ID, null);
        if (id == null) {
            id = "user_" + System.currentTimeMillis();
            saveUserCredentials(id, "Anonymous");
        }
        return id;
    }

    public String getUserName() {
        String name = prefs.getString(KEY_USER_NAME, "Anonymous");
        if (name != null) {
            name = name.replace("(আপনি)", "").replace("(You)", "").trim();
        }
        return name;
    }

    public void saveActiveSession(String roomCode, String sessionToken) {
        prefs.edit()
                .putString(KEY_LAST_ROOM_CODE, roomCode)
                .putString(KEY_SESSION_TOKEN, sessionToken)
                .apply();
    }

    public void saveCreatedRoomCode(String roomCode) {
        prefs.edit()
                .putString("pref_last_created_room_code", roomCode)
                .putString(KEY_LAST_ROOM_CODE, roomCode)
                .putLong("pref_last_created_room_time", System.currentTimeMillis())
                .apply();
    }

    public String getLastCreatedRoomCode() {
        long time = prefs.getLong("pref_last_created_room_time", 0);
        // Valid for 30 minutes
        if (System.currentTimeMillis() - time < 30 * 60 * 1000L) {
            return prefs.getString("pref_last_created_room_code", null);
        }
        return null;
    }

    public boolean isRoomCreator(String roomCode) {
        if (roomCode == null) return false;
        String created = prefs.getString("pref_last_created_room_code", null);
        return roomCode.trim().equalsIgnoreCase(created);
    }

    public String getLastRoomCode() {
        return prefs.getString(KEY_LAST_ROOM_CODE, null);
    }

    public String getSessionToken() {
        return prefs.getString(KEY_SESSION_TOKEN, null);
    }

    public void clearActiveSession() {
        prefs.edit()
                .remove(KEY_LAST_ROOM_CODE)
                .remove(KEY_SESSION_TOKEN)
                .remove("pref_last_created_room_code")
                .remove("pref_last_created_room_time")
                .apply();
    }

    /**
     * Local Room Database Persistence for Battle History.
     */
    public void recordCompletedBattle(BattleFinalSummary summary, String currentUserId) {
        if (summary == null) return;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            PlayerBattleResult myResult = null;
            for (PlayerBattleResult r : summary.getRankedResults()) {
                if (r.getPlayerId().equalsIgnoreCase(currentUserId)) {
                    myResult = r;
                    break;
                }
            }

            if (myResult == null && !summary.getRankedResults().isEmpty()) {
                myResult = summary.getRankedResults().get(0);
            }

            if (myResult != null) {
                BattleHistoryEntity entity = new BattleHistoryEntity(
                        "HIST_" + summary.getRoomCode() + "_" + System.currentTimeMillis(),
                        summary.getRoomCode(),
                        "category",
                        summary.getCategoryTitleBn(),
                        myResult.getFinalScore(),
                        myResult.getCorrectAnswers(),
                        myResult.getWrongAnswers(),
                        summary.getTotalQuestions(),
                        myResult.getRank(),
                        myResult.isWinner(),
                        myResult.getDeenXpEarned(),
                        summary.getBattleDurationSeconds(),
                        System.currentTimeMillis()
                );
                db.battleHistoryDao().insertBattleHistory(entity);
            }
        });
    }

    public LiveData<List<BattleHistoryEntity>> getAllBattleHistoryLive() {
        return db.battleHistoryDao().getAllBattleHistoryLive();
    }

    public Set<String> getSeenQuestionSignatures(String categoryId) {
        String key = "pref_seen_q_" + (categoryId != null ? categoryId.trim().toLowerCase() : "general");
        return new HashSet<>(prefs.getStringSet(key, new HashSet<>()));
    }

    public void recordSeenQuestionSignatures(String categoryId, List<BattleQuestion> questions) {
        if (questions == null || questions.isEmpty()) return;
        String key = "pref_seen_q_" + (categoryId != null ? categoryId.trim().toLowerCase() : "general");
        Set<String> current = new HashSet<>(prefs.getStringSet(key, new HashSet<>()));
        for (BattleQuestion q : questions) {
            if (q != null) {
                String sig = q.getNormalizedQuestionText();
                if (sig.isEmpty() && q.getQuestionText() != null) {
                    sig = q.getQuestionText().trim().toLowerCase();
                }
                if (!sig.isEmpty()) {
                    current.add(sig);
                }
            }
        }
        if (current.size() > 250) {
            current.clear();
        }
        prefs.edit().putStringSet(key, current).apply();
    }
}
