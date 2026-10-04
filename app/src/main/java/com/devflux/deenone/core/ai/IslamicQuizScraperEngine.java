package com.devflux.deenone.core.ai;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.devflux.deenone.features.battle.engine.IslamicQuestionValidator;
import com.devflux.deenone.features.battle.model.BattleConfig;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Intelligent Real-Time Islamic Question Scraping & Metadata Engine.
 * Features:
 * 1. Live Online HTTP API Scraping via IslamicOnlineScraperClient (Quran Cloud, Hadith CDN, AlAdhan).
 * 2. Massive Authentic Category Pools (100-120+ authentic questions per category, 3,190+ total questions).
 * 3. 100% Unique Question IDs per match.
 * 4. Zero Duplicate Guarantee per match room.
 * 5. Dynamic Option Randomization across positions 0, 1, 2, 3 (ক, খ, গ, ঘ).
 * 6. User Session Unseen History Prioritization.
 */
public class IslamicQuizScraperEngine {

    private static volatile IslamicQuizScraperEngine instance;
    private Handler mainHandler;
    private final Map<String, List<BattleQuestion>> categoryDatabase = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private synchronized Handler getMainHandler() {
        if (mainHandler == null) {
            try {
                Looper looper = Looper.getMainLooper();
                if (looper != null) {
                    mainHandler = new Handler(looper);
                }
            } catch (Throwable ignored) {
                // Safe fallback in headless local unit test execution
            }
        }
        return mainHandler;
    }

    public interface ScraperCallback {
        void onQuestionsScraped(List<BattleQuestion> questions);
        void onError(String errorReason);
    }

    public static IslamicQuizScraperEngine getInstance() {
        if (instance == null) {
            synchronized (IslamicQuizScraperEngine.class) {
                if (instance == null) {
                    instance = new IslamicQuizScraperEngine();
                }
            }
        }
        return instance;
    }

    private IslamicQuizScraperEngine() {
        // Seed initial pool from compiled IslamicQuestionBank
        categoryDatabase.putAll(IslamicQuestionBank.buildCompleteQuestionPool());
    }

    private static Context applicationContext;

    public static void setApplicationContext(Context context) {
        if (context != null && applicationContext == null) {
            applicationContext = context.getApplicationContext();
        }
    }

    /**
     * Ensures the full 100+ authentic question pool is loaded from assets for the given category.
     * Works both in Android runtime (AssetManager) and in headless JVM unit tests (local filesystem).
     */
    public synchronized void ensureCategoryPoolLoaded(Context context, String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) return;
        String targetCat = categoryId.trim().toLowerCase();

        if (context != null && applicationContext == null) {
            applicationContext = context.getApplicationContext();
        }

        List<BattleQuestion> existing = categoryDatabase.get(targetCat);
        if (existing != null && existing.size() >= 50) {
            return; // Already loaded massive pool
        }

        List<BattleQuestion> loadedList = new ArrayList<>();

        // 1. Try loading from Android AssetManager
        Context ctxToUse = context != null ? context : applicationContext;
        if (ctxToUse != null) {
            try {
                String assetPath = "battle/categories/" + targetCat + ".json";
                InputStream is = ctxToUse.getAssets().open(assetPath);
                loadedList = parseQuestionsFromStream(is, targetCat);
            } catch (Exception ignored) {}
        }

        // 2. Headless local unit test filesystem fallback (supporting various working directories)
        if (loadedList.isEmpty()) {
            File[] candidates = new File[] {
                    new File("src/main/assets/battle/categories/" + targetCat + ".json"),
                    new File("app/src/main/assets/battle/categories/" + targetCat + ".json"),
                    new File("../app/src/main/assets/battle/categories/" + targetCat + ".json"),
                    new File("F:/Deanone/app/src/main/assets/battle/categories/" + targetCat + ".json")
            };
            for (File candidate : candidates) {
                if (candidate.exists()) {
                    try (InputStream is = new FileInputStream(candidate)) {
                        loadedList = parseQuestionsFromStream(is, targetCat);
                        if (!loadedList.isEmpty()) break;
                    } catch (Exception ignored) {}
                }
            }
        }

        // Store authentic asset pool directly into categoryDatabase
        if (!loadedList.isEmpty()) {
            categoryDatabase.put(targetCat, loadedList);
        }
    }

    private List<BattleQuestion> parseQuestionsFromStream(InputStream is, String targetCategoryId) {
        List<BattleQuestion> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            JsonElement root = JsonParser.parseReader(reader);
            if (root != null && root.isJsonArray()) {
                JsonArray arr = root.getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    JsonElement el = arr.get(i);
                    if (!el.isJsonObject()) continue;
                    JsonObject obj = el.getAsJsonObject();
                    String id = obj.has("id") ? obj.get("id").getAsString() : "KB_Q_" + targetCategoryId.toUpperCase() + "_" + i;
                    String catId = obj.has("categoryId") ? obj.get("categoryId").getAsString() : targetCategoryId;
                    String qText = obj.has("questionText") ? obj.get("questionText").getAsString() : "";
                    List<String> options = new ArrayList<>();
                    if (obj.has("options") && obj.get("options").isJsonArray()) {
                        JsonArray optArr = obj.getAsJsonArray("options");
                        for (int j = 0; j < optArr.size(); j++) {
                            options.add(optArr.get(j).getAsString());
                        }
                    }
                    int correctIdx = obj.has("correctOptionIndex") ? obj.get("correctOptionIndex").getAsInt() : 0;
                    String explanation = obj.has("explanation") ? obj.get("explanation").getAsString() : "";
                    String reference = obj.has("reference") ? obj.get("reference").getAsString() : "কুরআন ও সহীহ সুন্নাহ";

                    if (!qText.trim().isEmpty() && options.size() == 4) {
                        BattleQuestion q = new BattleQuestion(
                                id, catId, qText, options, correctIdx, explanation, reference
                        );
                        list.add(q);
                    }
                }
            }
        } catch (Exception ignored) {}
        return list;
    }

    public int getCategoryPoolSize(String categoryId) {
        List<BattleQuestion> pool = categoryDatabase.get(categoryId);
        return pool != null ? pool.size() : 0;
    }

    public synchronized List<BattleQuestion> getQuestionsByCategory(Context context, String categoryId, int count) {
        ensureCategoryPoolLoaded(context, categoryId);
        List<BattleQuestion> pool = categoryDatabase.get(categoryId);
        if (pool == null || pool.isEmpty()) {
            return Collections.emptyList();
        }
        int take = Math.min(count, pool.size());
        return new ArrayList<>(pool.subList(0, take));
    }

    /**
     * Real-time Online Scraping & Question Fetching:
     * 1. Loads local authentic seed pool (100+ questions) immediately.
     * 2. Concurrently calls IslamicOnlineScraperClient for real-time live scraping over HTTP.
     * 3. Merges and validates freshly scraped questions in real time.
     * 4. Shuffles answer choices and enforces zero duplicate match guarantee.
     */
    public void scrapeQuestionsForCategory(Context context, String categoryId, int count, ScraperCallback callback) {
        new Thread(() -> {
            try {
                // Ensure 100+ authentic offline questions are loaded
                ensureCategoryPoolLoaded(context, categoryId);

                // Concurrently scrape live online endpoints via OkHttp
                IslamicOnlineScraperClient.getInstance().fetchLiveQuestions(categoryId, count, new IslamicOnlineScraperClient.OnlineScrapeCallback() {
                    @Override
                    public void onSuccess(List<BattleQuestion> scrapedQuestions) {
                        if (scrapedQuestions != null && !scrapedQuestions.isEmpty()) {
                            mergeScrapedQuestions(categoryId, scrapedQuestions);
                        }
                        dispatchScrapedResult(categoryId, count, callback);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Seamless network fallback to our 100+ authentic pool
                        dispatchScrapedResult(categoryId, count, callback);
                    }
                });
            } catch (Exception e) {
                dispatchScrapedResult(categoryId, count, callback);
            }
        }).start();
    }

    public synchronized void mergeScrapedQuestions(String categoryId, List<BattleQuestion> scrapedList) {
        if (categoryId == null || scrapedList == null || scrapedList.isEmpty()) return;
        List<BattleQuestion> pool = categoryDatabase.get(categoryId);
        if (pool == null) {
            pool = new ArrayList<>();
            categoryDatabase.put(categoryId, pool);
        }
        Set<String> signatures = new HashSet<>();
        for (BattleQuestion q : pool) {
            signatures.add(q.getNormalizedQuestionText());
        }
        for (BattleQuestion q : scrapedList) {
            IslamicQuestionValidator.ValidationResult val = IslamicQuestionValidator.validateAndDeduplicate(q, categoryId);
            if (val.isValid() && !signatures.contains(q.getNormalizedQuestionText())) {
                pool.add(q);
                signatures.add(q.getNormalizedQuestionText());
            }
        }
    }

    private void dispatchScrapedResult(String categoryId, int count, ScraperCallback callback) {
        List<BattleQuestion> generatedList = generateDynamicQuestions(categoryId, count);
        Handler handler = getMainHandler();
        if (handler != null) {
            handler.post(() -> {
                if (callback != null) callback.onQuestionsScraped(generatedList);
            });
        } else {
            if (callback != null) callback.onQuestionsScraped(generatedList);
        }
    }

    public List<BattleQuestion> generateDynamicQuestions(String categoryId, int count) {
        return generateDynamicQuestions(categoryId, count, null, null);
    }

    /**
     * Strict Non-Repeating, Option-Randomized Question Generator:
     * 1. 100% Unique Question IDs for every question in every match.
     * 2. Zero Duplicates in the same match room (guaranteed by normalized signature deduplication).
     * 3. Dynamic Option Shuffling: Answer choices are randomized across positions 0, 1, 2, 3 (ক, খ, গ, ঘ)
     *    so Option 1 is NOT always the correct answer.
     * 4. Session History Prioritization: Questions not recently seen by the user are prioritized.
     */
    public BattleQuestion getQuestionById(Context context, String categoryId, String questionId) {
        if (questionId == null || questionId.trim().isEmpty()) return null;
        String cleanId = questionId.trim().toUpperCase();
        String targetCat = (categoryId != null && !categoryId.trim().isEmpty()) ? categoryId.trim().toLowerCase() : null;

        if (targetCat != null) {
            ensureCategoryPoolLoaded(context, targetCat);
            List<BattleQuestion> pool = categoryDatabase.get(targetCat);
            if (pool != null) {
                for (BattleQuestion q : pool) {
                    if (isMatchingQuestionId(q.getId(), cleanId)) {
                        return q;
                    }
                }
            }
        }

        for (List<BattleQuestion> pool : categoryDatabase.values()) {
            for (BattleQuestion q : pool) {
                if (isMatchingQuestionId(q.getId(), cleanId)) {
                    return q;
                }
            }
        }
        return null;
    }

    private boolean isMatchingQuestionId(String candidateId, String cleanId) {
        if (candidateId == null || cleanId == null) return false;
        if (candidateId.equalsIgnoreCase(cleanId)) return true;
        String candStripped = stripInstanceSuffix(candidateId);
        String cleanStripped = stripInstanceSuffix(cleanId);
        return candStripped.equalsIgnoreCase(cleanStripped);
    }

    private String stripInstanceSuffix(String id) {
        if (id == null) return "";
        String s = id.trim().toUpperCase();
        if (s.startsWith("KB_Q_")) s = s.substring(5);
        int lastUnder = s.lastIndexOf('_');
        if (lastUnder > 0 && s.length() - lastUnder - 1 == 6) {
            boolean isHex = true;
            for (int i = lastUnder + 1; i < s.length(); i++) {
                char c = s.charAt(i);
                if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F'))) {
                    isHex = false;
                    break;
                }
            }
            if (isHex) {
                return s.substring(0, lastUnder);
            }
        }
        return s;
    }

    private String generateRandomHex(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int v = random.nextInt(16);
            sb.append(Integer.toHexString(v).toUpperCase());
        }
        return sb.toString();
    }

    public List<BattleQuestion> generateDynamicQuestions(String categoryId, int count,
                                                         Set<String> excludedQuestionIds,
                                                         Set<String> excludedSignatures) {
        String targetCat = (categoryId != null && !categoryId.trim().isEmpty()) ? categoryId.trim().toLowerCase() : "quran_stories";
        ensureCategoryPoolLoaded(null, targetCat);
        List<BattleQuestion> pool = categoryDatabase.get(targetCat);
        if (pool == null || pool.isEmpty()) {
            ensureCategoryPoolLoaded(null, "quran_stories");
            pool = categoryDatabase.get("quran_stories");
            targetCat = "quran_stories";
        }
        if (pool == null || pool.isEmpty()) {
            return new ArrayList<>();
        }

        List<BattleQuestion> candidatePool = new ArrayList<>(pool);
        Collections.shuffle(candidatePool, random);

        // Partition into unseen vs seen questions if excludedSignatures provided
        List<BattleQuestion> unseenList = new ArrayList<>();
        List<BattleQuestion> seenList = new ArrayList<>();

        for (BattleQuestion base : candidatePool) {
            String sig = base.getNormalizedQuestionText();
            if (excludedSignatures != null && excludedSignatures.contains(sig)) {
                seenList.add(base);
            } else {
                unseenList.add(base);
            }
        }

        List<BattleQuestion> prioritizedCandidates = new ArrayList<>();
        prioritizedCandidates.addAll(unseenList);
        prioritizedCandidates.addAll(seenList);

        List<BattleQuestion> result = new ArrayList<>();
        Set<String> addedSignaturesInMatch = new HashSet<>();

        for (BattleQuestion base : prioritizedCandidates) {
            if (result.size() >= count) break;

            String sig = base.getNormalizedQuestionText();
            // Strict match-level deduplication: NEVER add the same question text twice in the same match
            if (addedSignaturesInMatch.contains(sig)) {
                continue;
            }

            if (excludedQuestionIds != null && excludedQuestionIds.contains(base.getId()) && prioritizedCandidates.size() > count) {
                continue;
            }

            // Generate globally unique instance ID always prefixed with KB_Q_
            String rawBase = (base.getId() != null && !base.getId().trim().isEmpty())
                    ? base.getId().trim()
                    : (targetCat.toUpperCase() + "_" + (result.size() + 1));
            String baseId = rawBase.startsWith("KB_Q_") ? rawBase : ("KB_Q_" + rawBase);
            String instanceId = baseId + "_" + generateRandomHex(6);

            // createRandomizedInstance dynamically shuffles the 4 options, records permutation, and updates correctOptionIndex accurately
            BattleQuestion randomized = base.createRandomizedInstance(instanceId);
            result.add(randomized);
            addedSignaturesInMatch.add(sig);
        }

        return result;
    }
}
