package com.devflux.deenone.features.battle.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.features.battle.engine.IslamicQuestionValidator;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.battle.repository.KnowledgeBattleRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enterprise Question Pool Ingestion & Online Sync Manager.
 * Ingests external API payloads, parses Islamic questions, passes every item through
 * the 7-step IslamicQuestionValidator (Category check, Duplicate detection, Quality check),
 * and maintains expansive verified question pools for the battle arena.
 */
public class IslamicQuestionSyncManager {

    private static volatile IslamicQuestionSyncManager instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Map<String, List<BattleQuestion>> verifiedPoolMap = new ConcurrentHashMap<>();

    public interface SyncCallback {
        void onSyncCompleted(int importedCount, int duplicateRejectedCount);
        void onSyncFailed(String reason);
    }

    public static IslamicQuestionSyncManager getInstance() {
        if (instance == null) {
            synchronized (IslamicQuestionSyncManager.class) {
                if (instance == null) {
                    instance = new IslamicQuestionSyncManager();
                }
            }
        }
        return instance;
    }

    private IslamicQuestionSyncManager() {}

    /**
     * Parse raw external JSON stream and import into verified pool.
     */
    public void importQuestionsFromJson(String rawJsonArrayString, String targetCategoryId, SyncCallback callback) {
        new Thread(() -> {
            int imported = 0;
            int rejected = 0;

            try {
                JSONArray arr = new JSONArray(rawJsonArrayString);
                List<BattleQuestion> validList = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    String categoryId = obj.optString("categoryId", targetCategoryId);
                    String questionText = obj.optString("questionText");
                    JSONArray optsJson = obj.optJSONArray("options");
                    List<String> options = new ArrayList<>();
                    if (optsJson != null) {
                        for (int j = 0; j < optsJson.length(); j++) {
                            options.add(optsJson.getString(j));
                        }
                    }

                    int correctIdx = obj.optInt("correctOptionIndex", 0);
                    String explanation = obj.optString("explanation", "");
                    String reference = obj.optString("reference", "সহীহ ইসলামী তথ্যসূত্র");
                    String source = obj.optString("source", "Islamic Knowledge Cloud");

                    String uniqueId = "Q_SYNC_" + categoryId.toUpperCase() + "_" + System.currentTimeMillis() + "_" + i;

                    BattleQuestion rawQuestion = new BattleQuestion(
                            uniqueId,
                            categoryId,
                            questionText,
                            options,
                            correctIdx,
                            explanation,
                            reference,
                            BattleConfig.Difficulty.MEDIUM,
                            source,
                            "bn",
                            System.currentTimeMillis(),
                            BattleQuestion.QuestionStatus.ACTIVE
                    );

                    // Execute 7-step enterprise validation pipeline
                    IslamicQuestionValidator.ValidationResult result = IslamicQuestionValidator.validateAndDeduplicate(rawQuestion, targetCategoryId);
                    if (result.isValid()) {
                        validList.add(rawQuestion);
                        imported++;
                    } else {
                        rejected++;
                    }
                }

                List<BattleQuestion> existing = verifiedPoolMap.get(targetCategoryId);
                if (existing == null) {
                    existing = new ArrayList<>();
                    verifiedPoolMap.put(targetCategoryId, existing);
                }
                existing.addAll(validList);

                final int finalImported = imported;
                final int finalRejected = rejected;
                mainHandler.post(() -> {
                    if (callback != null) callback.onSyncCompleted(finalImported, finalRejected);
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onSyncFailed("ইমপোর্ট পার্সিং ত্রুটি: " + e.getMessage());
                });
            }
        }).start();
    }

    /**
     * Trigger background synchronization for an active Battle category.
     */
    public void syncCategoryPool(Context context, String categoryId, SyncCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(300); // Network simulation

                // Retrieve validated questions for category
                List<BattleQuestion> basePool = KnowledgeBattleRepository.getQuestionsForMatch(categoryId, 20);
                List<BattleQuestion> validatedPool = new ArrayList<>();
                int imported = 0;
                int rejected = 0;

                IslamicQuestionValidator.resetRegisteredHashes();
                for (BattleQuestion q : basePool) {
                    IslamicQuestionValidator.ValidationResult res = IslamicQuestionValidator.validateAndDeduplicate(q, categoryId);
                    if (res.isValid()) {
                        validatedPool.add(q);
                        imported++;
                    } else {
                        rejected++;
                    }
                }

                verifiedPoolMap.put(categoryId, validatedPool);

                final int finalImported = imported;
                final int finalRejected = rejected;
                mainHandler.post(() -> {
                    if (callback != null) callback.onSyncCompleted(finalImported, finalRejected);
                });

            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (callback != null) callback.onSyncFailed(e.getMessage());
                });
            }
        }).start();
    }

    public List<BattleQuestion> getVerifiedPool(String categoryId) {
        return verifiedPoolMap.get(categoryId);
    }
}
