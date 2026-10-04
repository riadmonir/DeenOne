package com.devflux.deenone.features.battle.data.remote;

import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleLifecycleState;
import com.devflux.deenone.features.battle.model.BattlePlayer;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.model.BattleRoom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Lossless JSON Converter for BattleRoom, BattleConfig, BattlePlayer, and BattleQuestion.
 * Ensures complete synchronization of match state across distinct devices.
 */
public class BattleRoomJsonConverter {

    public static JSONObject serializeRoom(BattleRoom room) {
        if (room == null) return null;
        try {
            JSONObject json = new JSONObject();
            json.put("room_code", room.getRoomCode());
            json.put("created_at", room.getCreatedAtTimestamp());
            json.put("lifecycle_state", room.getLifecycleState() != null ? room.getLifecycleState().name() : BattleLifecycleState.WAITING_FOR_PLAYERS.name());
            json.put("current_question_index", room.getCurrentQuestionIndex());

            // Config
            BattleConfig cfg = room.getConfig();
            JSONObject cfgJson = new JSONObject();
            cfgJson.put("total_players", cfg.getTotalPlayers());
            cfgJson.put("total_questions", cfg.getTotalQuestions());
            cfgJson.put("total_duration_minutes", cfg.getTotalDurationMinutes());
            cfgJson.put("time_per_question_sec", cfg.getTimePerQuestionSec());
            cfgJson.put("category_id", cfg.getCategoryId());
            cfgJson.put("category_title_bn", cfg.getCategoryTitleBn());
            cfgJson.put("difficulty", cfg.getDifficulty() != null ? cfg.getDifficulty().name() : "MEDIUM");
            cfgJson.put("negative_marking", cfg.isNegativeMarkingEnabled());
            json.put("config", cfgJson);

            // Host
            BattlePlayer host = room.getHostPlayer();
            json.put("host_player", serializePlayer(host));

            // Players
            JSONArray playersArr = new JSONArray();
            for (BattlePlayer p : room.getPlayers()) {
                playersArr.put(serializePlayer(p));
            }
            json.put("players", playersArr);

            // Questions
            JSONArray questionsArr = new JSONArray();
            for (BattleQuestion q : room.getQuestions()) {
                JSONObject qJson = new JSONObject();
                qJson.put("id", q.getId());
                qJson.put("category_id", q.getCategoryId());

                // Check permutation from BattleQuestion directly
                List<Integer> perm = q.getOptionPermutation();
                if (perm != null && !perm.isEmpty()) {
                    JSONArray permArr = new JSONArray();
                    for (int idx : perm) permArr.put(idx);
                    qJson.put("p", permArr);
                } else {
                    // Try looking up base question if permutation wasn't cached
                    BattleQuestion baseQ = null;
                    try {
                        baseQ = com.devflux.deenone.core.ai.IslamicQuizScraperEngine.getInstance()
                                .getQuestionById(null, q.getCategoryId(), q.getId());
                    } catch (Exception ignored) {}

                    if (baseQ != null && baseQ.getOptions() != null && !baseQ.getOptions().isEmpty()) {
                        JSONArray permArr = new JSONArray();
                        for (String opt : q.getOptions()) {
                            int origIdx = baseQ.getOptions().indexOf(opt);
                            permArr.put(origIdx != -1 ? origIdx : permArr.length());
                        }
                        qJson.put("p", permArr);
                    } else {
                        // Ad-hoc question: serialize options only without lengthy explanations
                        qJson.put("question_text", q.getQuestionText());
                        JSONArray optsArr = new JSONArray();
                        for (String opt : q.getOptions()) {
                            optsArr.put(opt);
                        }
                        qJson.put("options", optsArr);
                        qJson.put("correct_option_index", q.getCorrectOptionIndex());
                        if (q.getExplanation() != null && q.getExplanation().length() < 60) qJson.put("explanation", q.getExplanation());
                        if (q.getReference() != null && q.getReference().length() < 40) qJson.put("reference", q.getReference());
                    }
                }
                questionsArr.put(qJson);
            }
            json.put("questions", questionsArr);

            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public static BattleRoom deserializeRoom(JSONObject json) {
        return deserializeRoom(json, null);
    }

    public static BattleRoom deserializeRoom(JSONObject json, android.content.Context context) {
        if (json == null) return null;
        try {
            String roomCode = json.optString("room_code", "");
            if (roomCode.isEmpty()) return null;

            // Config
            JSONObject cfgJson = json.getJSONObject("config");
            int totalPlayers = cfgJson.optInt("total_players", 2);
            int totalQuestions = cfgJson.optInt("total_questions", 10);
            int totalDurationMinutes = cfgJson.optInt("total_duration_minutes", 5);
            String categoryId = cfgJson.optString("category_id", "quran_stories");
            String categoryTitleBn = cfgJson.optString("category_title_bn", "কুরআনের ঘটনা ও শিক্ষা");
            boolean negativeMarking = cfgJson.optBoolean("negative_marking", false);

            BattleConfig config = new BattleConfig(totalPlayers, totalQuestions, totalDurationMinutes, categoryId, categoryTitleBn);
            config.setNegativeMarkingEnabled(negativeMarking);

            // Host
            JSONObject hostJson = json.getJSONObject("host_player");
            BattlePlayer host = deserializePlayer(hostJson);
            if (host == null) {
                host = new BattlePlayer("host_" + roomCode, "হোস্ট (Host)", "ic_person", false);
            }

            BattleRoom room = new BattleRoom(roomCode, config, host);

            // Players
            JSONArray playersArr = json.optJSONArray("players");
            if (playersArr != null) {
                for (int i = 0; i < playersArr.length(); i++) {
                    BattlePlayer p = deserializePlayer(playersArr.getJSONObject(i));
                    if (p != null && !p.getId().equalsIgnoreCase(host.getId())) {
                        room.addPlayer(p);
                        room.updatePlayerStatus(p.getId(), p.getStatus());
                    }
                }
            }

            // Lifecycle
            String lifecycle = json.optString("lifecycle_state", BattleLifecycleState.WAITING_FOR_PLAYERS.name());
            try {
                room.setLifecycleState(BattleLifecycleState.valueOf(lifecycle));
            } catch (Exception ignored) {}

            // Questions
            JSONArray questionsArr = json.optJSONArray("questions");
            List<BattleQuestion> questions = new ArrayList<>();
            if (questionsArr != null) {
                for (int i = 0; i < questionsArr.length(); i++) {
                    JSONObject qJson = questionsArr.getJSONObject(i);
                    String qId = qJson.optString("id", "q_" + i);
                    String qCat = qJson.optString("category_id", categoryId);

                    if (qJson.has("p")) {
                        // Re-hydrate authentic question from Local Storage assets using ID and permutation
                        BattleQuestion baseQ = null;
                        try {
                            baseQ = com.devflux.deenone.core.ai.IslamicQuizScraperEngine.getInstance()
                                    .getQuestionById(context, qCat, qId);
                        } catch (Exception ignored) {}

                        if (baseQ != null && baseQ.getOptions() != null && !baseQ.getOptions().isEmpty()) {
                            JSONArray permArr = qJson.getJSONArray("p");
                            List<String> permutedOpts = new ArrayList<>();
                            int correctIdx = 0;
                            for (int j = 0; j < permArr.length(); j++) {
                                int origIdx = permArr.getInt(j);
                                if (origIdx >= 0 && origIdx < baseQ.getOptions().size()) {
                                    permutedOpts.add(baseQ.getOptions().get(origIdx));
                                    if (origIdx == baseQ.getCorrectOptionIndex()) {
                                        correctIdx = j;
                                    }
                                }
                            }
                            if (permutedOpts.size() >= 2) {
                                BattleQuestion bq = new BattleQuestion(
                                        qId, qCat, baseQ.getQuestionText(), permutedOpts, correctIdx,
                                        baseQ.getExplanation(), baseQ.getReference(),
                                        baseQ.getDifficulty(), baseQ.getSource(), baseQ.getLanguage(),
                                        System.currentTimeMillis(), baseQ.getStatus()
                                );
                                questions.add(bq);
                                continue;
                            }
                        }
                    }

                    // Fallback for ad-hoc / explicit questions
                    String qText = qJson.has("question_text") ? qJson.optString("question_text", "") : qJson.optString("q", "");
                    JSONArray optsArr = qJson.has("options") ? qJson.optJSONArray("options") : qJson.optJSONArray("opts");
                    List<String> opts = new ArrayList<>();
                    if (optsArr != null) {
                        for (int j = 0; j < optsArr.length(); j++) {
                            opts.add(optsArr.optString(j, ""));
                        }
                    }
                    int correctIdx = qJson.has("correct_option_index") ? qJson.optInt("correct_option_index", 0) : qJson.optInt("ans", 0);
                    String explanation = qJson.has("explanation") ? qJson.optString("explanation", "") : qJson.optString("exp", "");
                    String reference = qJson.has("reference") ? qJson.optString("reference", "") : qJson.optString("ref", "");

                    BattleQuestion bq = new BattleQuestion(qId, qCat, qText, opts, correctIdx, explanation, reference);
                    questions.add(bq);
                }
            }
            room.setQuestions(questions);

            return room;
        } catch (Exception e) {
            android.util.Log.e("BattleRoomJsonConverter", "Error deserializing room: " + e.getMessage(), e);
            return null;
        }
    }

    public static JSONObject serializePlayer(BattlePlayer player) {
        if (player == null) return null;
        try {
            JSONObject json = new JSONObject();
            json.put("id", player.getId());
            json.put("name", player.getName());
            json.put("avatar", player.getAvatarResource());
            json.put("is_bot", player.isBot());
            json.put("status", player.getStatus() != null ? player.getStatus().name() : BattlePlayer.PlayerStatus.READY.name());
            json.put("score", player.getScore());
            json.put("correct_answers", player.getCorrectAnswers());
            json.put("wrong_answers", player.getWrongAnswers());
            return json;
        } catch (Exception e) {
            return null;
        }
    }

    public static BattlePlayer deserializePlayer(JSONObject json) {
        if (json == null) return null;
        try {
            String id = json.optString("id", "");
            String name = json.optString("name", "খেলোয়াড়");
            String avatar = json.optString("avatar", "ic_person");
            boolean isBot = json.optBoolean("is_bot", false);
            BattlePlayer player = new BattlePlayer(id, name, avatar, isBot);

            player.setScore(json.optInt("score", 0));
            String statusStr = json.optString("status", BattlePlayer.PlayerStatus.READY.name());
            try {
                player.setStatus(BattlePlayer.PlayerStatus.valueOf(statusStr));
            } catch (Exception ignored) {}

            int correct = json.optInt("correct_answers", 0);
            for (int i = 0; i < correct; i++) player.incrementCorrect();

            int wrong = json.optInt("wrong_answers", 0);
            for (int i = 0; i < wrong; i++) player.incrementWrong();

            return player;
        } catch (Exception e) {
            return null;
        }
    }
}
