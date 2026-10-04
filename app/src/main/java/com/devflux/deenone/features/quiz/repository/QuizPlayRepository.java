package com.devflux.deenone.features.quiz.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.ai.IslamicQuestionBank;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.features.battle.model.BattleQuestion;
import com.devflux.deenone.features.quiz.model.QuizPlayQuestion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Islamic Quiz Repository.
 * Dual-tier dynamic data fetching:
 * 1. PHP API & MySQL Backend
 * 2. Instant Local Verified Islamic Question Bank fallback
 * Guarantees exactly 5 questions per category without blank screen or stalls.
 */
public class QuizPlayRepository {

    private static final String PREFS_SEEN_QUESTIONS = "deenone_quiz_seen_questions_prefs";
    private static final String KEY_SEEN_PREFIX = "seen_q_";

    public interface OnQuestionsLoadedListener {
        void onQuestionsLoaded(@NonNull List<QuizPlayQuestion> questions);
    }

    /**
     * Retrieve the set of all previously answered/seen question IDs for a category.
     */
    @NonNull
    public static Set<String> getSeenQuestionIds(@NonNull Context context, String categoryId) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS_SEEN_QUESTIONS, Context.MODE_PRIVATE);
        String catKey = KEY_SEEN_PREFIX + (categoryId != null ? categoryId.trim().toLowerCase() : "default");
        Set<String> catSet = prefs.getStringSet(catKey, new HashSet<>());
        return new HashSet<>(catSet);
    }

    /**
     * Mark question IDs as answered / seen.
     */
    public static void markQuestionsAsSeen(@NonNull Context context, String categoryId, @NonNull List<String> questionIds) {
        if (questionIds.isEmpty()) return;

        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS_SEEN_QUESTIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String catKey = KEY_SEEN_PREFIX + (categoryId != null ? categoryId.trim().toLowerCase() : "default");
        Set<String> currentCatSet = new HashSet<>(prefs.getStringSet(catKey, new HashSet<>()));
        currentCatSet.addAll(questionIds);
        editor.putStringSet(catKey, currentCatSet);
        editor.apply();
    }

    /**
     * Clear seen questions for a specific category when the user has exhausted all questions.
     */
    public static void clearSeenQuestions(@NonNull Context context, String categoryId) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS_SEEN_QUESTIONS, Context.MODE_PRIVATE);
        String catKey = KEY_SEEN_PREFIX + (categoryId != null ? categoryId.trim().toLowerCase() : "default");
        prefs.edit().remove(catKey).apply();
    }

    /**
     * Fetch questions dynamically from MySQL Database / PHP Backend with instant local fallback.
     * Guarantees returning target count (5) questions.
     */
    public static void getQuestions(@NonNull Context context, String categoryId, int count, @NonNull OnQuestionsLoadedListener listener) {
        final int targetCount = count > 0 ? count : 5;
        final String cat = (categoryId != null && !categoryId.trim().isEmpty()) ? categoryId.trim() : "general_knowledge";

        Executors.newSingleThreadExecutor().execute(() -> {
            List<QuizPlayQuestion> resultList = new ArrayList<>();
            Set<String> seenIds = getSeenQuestionIds(context, cat);

            // 1. Attempt remote fetch from PHP API / MySQL Database
            try {
                String baseUrl = BackendConfigManager.getPhpApiBaseUrl(context);
                StringBuilder urlBuilder = new StringBuilder(baseUrl)
                        .append("get_quiz_questions.php?category_id=")
                        .append(URLEncoder.encode(cat, "UTF-8"))
                        .append("&count=")
                        .append(targetCount);

                StringBuilder excludeSb = new StringBuilder();
                for (String id : seenIds) {
                    if (excludeSb.length() > 0) excludeSb.append(",");
                    excludeSb.append(id);
                }
                if (excludeSb.length() > 0) {
                    urlBuilder.append("&exclude_ids=").append(URLEncoder.encode(excludeSb.toString(), "UTF-8"));
                }

                URL url = new URL(urlBuilder.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3500);
                conn.setReadTimeout(3500);

                if (conn.getResponseCode() == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject root = new JSONObject(sb.toString());
                    if (root.optBoolean("success", false)) {
                        JSONArray arr = root.optJSONArray("questions");
                        if (arr != null && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject qObj = arr.getJSONObject(i);
                                String id = qObj.optString("id", "Q_" + cat + "_" + i);
                                String qCat = qObj.optString("category_id", cat);
                                String qBn = qObj.optString("question_text", "");
                                String qEn = qObj.optString("question_text_en", qBn);
                                JSONArray optsBn = qObj.optJSONArray("options");
                                JSONArray optsEn = qObj.optJSONArray("options_en");
                                List<String> bList = new ArrayList<>();
                                List<String> eList = new ArrayList<>();
                                if (optsBn != null) {
                                    for (int j = 0; j < optsBn.length(); j++) bList.add(optsBn.getString(j));
                                }
                                if (optsEn != null) {
                                    for (int j = 0; j < optsEn.length(); j++) eList.add(optsEn.getString(j));
                                } else {
                                    eList.addAll(bList);
                                }
                                int correct = qObj.optInt("correct_option_index", 0);
                                String expBn = qObj.optString("explanation", "");
                                String expEn = qObj.optString("explanation_en", expBn);
                                String refBn = qObj.optString("reference", "");
                                String refEn = qObj.optString("reference_en", refBn);

                                if (!qBn.isEmpty() && bList.size() >= 4) {
                                    resultList.add(new QuizPlayQuestion(id, qCat, qBn, qEn, bList, eList, correct, expBn, expEn, refBn, refEn));
                                }
                            }
                        }
                    }
                }
                conn.disconnect();
            } catch (Exception ignored) {}

            // 2. If result list has fewer than targetCount, supplement from local verified IslamicQuestionBank
            if (resultList.size() < targetCount) {
                List<BattleQuestion> localPool = getLocalPoolForCategory(cat);
                if (!localPool.isEmpty()) {
                    List<BattleQuestion> candidatePool = new ArrayList<>();
                    // Filter unseen first
                    for (BattleQuestion bq : localPool) {
                        if (!seenIds.contains(bq.getId())) {
                            candidatePool.add(bq);
                        }
                    }
                    // If candidate pool is too small, reuse local pool and reset seen IDs
                    if (candidatePool.size() < (targetCount - resultList.size())) {
                        clearSeenQuestions(context, cat);
                        candidatePool.clear();
                        candidatePool.addAll(localPool);
                    }

                    Collections.shuffle(candidatePool);

                    for (BattleQuestion bq : candidatePool) {
                        if (resultList.size() >= targetCount) break;

                        // Check if already in resultList
                        boolean alreadyAdded = false;
                        for (QuizPlayQuestion existing : resultList) {
                            if (existing.getId().equalsIgnoreCase(bq.getId())) {
                                alreadyAdded = true;
                                break;
                            }
                        }
                        if (alreadyAdded) continue;

                        // Create randomized option instance
                        BattleQuestion randomized = bq.createRandomizedInstance("Q_LOCAL_" + cat.toUpperCase() + "_" + bq.getId());
                        List<String> optionsBn = new ArrayList<>(randomized.getOptions());
                        List<String> optionsEn = new ArrayList<>(optionsBn);

                        resultList.add(new QuizPlayQuestion(
                                bq.getId(),
                                cat,
                                randomized.getQuestionText(),
                                randomized.getQuestionText(),
                                optionsBn,
                                optionsEn,
                                randomized.getCorrectOptionIndex(),
                                randomized.getExplanation(),
                                randomized.getExplanation(),
                                randomized.getReference(),
                                randomized.getReference()
                        ));
                    }
                }
            }

            // Trim to exactly targetCount if more
            List<QuizPlayQuestion> finalQuestions = new ArrayList<>();
            for (int i = 0; i < Math.min(targetCount, resultList.size()); i++) {
                finalQuestions.add(resultList.get(i));
            }

            // Post back to main thread
            new Handler(Looper.getMainLooper()).post(() -> {
                listener.onQuestionsLoaded(finalQuestions);
            });
        });
    }

    /**
     * Map category ID to matching questions from IslamicQuestionBank.
     */
    private static List<BattleQuestion> getLocalPoolForCategory(String categoryId) {
        Map<String, List<BattleQuestion>> fullDb = IslamicQuestionBank.buildCompleteQuestionPool();
        String key = categoryId != null ? categoryId.trim().toLowerCase() : "general_knowledge";

        List<BattleQuestion> pool = new ArrayList<>();

        if (fullDb.containsKey(key) && fullDb.get(key) != null) {
            pool.addAll(fullDb.get(key));
        }

        // Comprehensive alias mappings
        if (key.equals("general_knowledge") || key.equals("general")) {
            addIfPresent(fullDb, "quran_knowledge", pool);
            addIfPresent(fullDb, "hadith_sunnah", pool);
            addIfPresent(fullDb, "salat_taharah", pool);
        } else if (key.equals("ibadah") || key.equals("ibadat")) {
            addIfPresent(fullDb, "salat_taharah", pool);
            addIfPresent(fullDb, "hajj_umrah", pool);
            addIfPresent(fullDb, "zakat_charity", pool);
            addIfPresent(fullDb, "ramadan_sawm", pool);
        } else if (key.equals("rabiul_awwal") || key.equals("rabi_al_awwal")) {
            addIfPresent(fullDb, "seerat_un_nabi", pool);
        } else if (key.equals("quran_studies") || key.equals("quran") || key.equals("quran_tajweed")) {
            addIfPresent(fullDb, "quran_knowledge", pool);
            addIfPresent(fullDb, "quran_stories", pool);
        } else if (key.equals("hadith_sunnah") || key.equals("hadith")) {
            addIfPresent(fullDb, "hadith_sunnah", pool);
        } else if (key.equals("prophets_stories") || key.equals("prophets")) {
            addIfPresent(fullDb, "quran_stories", pool);
        } else if (key.equals("seerat_un_nabi") || key.equals("seerah")) {
            addIfPresent(fullDb, "seerat_un_nabi", pool);
        } else if (key.equals("sahaba_life") || key.equals("sahaba")) {
            addIfPresent(fullDb, "sahaba_life", pool);
        } else if (key.equals("jannah_paradise") || key.equals("jannah")) {
            addIfPresent(fullDb, "jannah_paradise", pool);
            addIfPresent(fullDb, "akhira_qiyamah", pool);
        } else if (key.equals("jahannam_hell") || key.equals("jahannam")) {
            addIfPresent(fullDb, "jahannam_hell", pool);
            addIfPresent(fullDb, "akhira_qiyamah", pool);
        } else if (key.equals("jinn_unseen") || key.equals("jinn")) {
            addIfPresent(fullDb, "jinn_unseen", pool);
        } else if (key.equals("islamic_history") || key.equals("history")) {
            addIfPresent(fullDb, "islamic_history", pool);
        } else if (key.equals("islamic_months") || key.equals("months")) {
            addIfPresent(fullDb, "islamic_months", pool);
            addIfPresent(fullDb, "seerat_un_nabi", pool);
        } else if (key.equals("shariah_life") || key.equals("fiqh")) {
            addIfPresent(fullDb, "shariah_life", pool);
            addIfPresent(fullDb, "salat_taharah", pool);
        } else if (key.equals("halal_haram")) {
            addIfPresent(fullDb, "halal_haram", pool);
        } else if (key.equals("muslim_scholars") || key.equals("scholars")) {
            addIfPresent(fullDb, "muslim_scholars", pool);
            addIfPresent(fullDb, "islamic_history", pool);
        } else if (key.equals("quran_nature")) {
            addIfPresent(fullDb, "quran_nature", pool);
            addIfPresent(fullDb, "quran_knowledge", pool);
        } else if (key.equals("islamic_architecture")) {
            addIfPresent(fullDb, "islamic_architecture", pool);
            addIfPresent(fullDb, "holy_mosques", pool);
        } else if (key.equals("quran_vocabulary")) {
            addIfPresent(fullDb, "quran_vocabulary", pool);
            addIfPresent(fullDb, "quran_knowledge", pool);
        } else if (key.equals("islamic_family")) {
            addIfPresent(fullDb, "islamic_family", pool);
            addIfPresent(fullDb, "noble_women", pool);
        } else if (key.equals("masnoon_amal")) {
            addIfPresent(fullDb, "masnoon_amal", pool);
            addIfPresent(fullDb, "dua_azkar", pool);
        } else if (key.equals("islamic_lifestyle")) {
            addIfPresent(fullDb, "islamic_lifestyle", pool);
            addIfPresent(fullDb, "halal_haram", pool);
        } else if (key.equals("ramadan_sawm") || key.equals("ramadan")) {
            addIfPresent(fullDb, "ramadan_sawm", pool);
        } else if (key.equals("hajj_umrah") || key.equals("hajj")) {
            addIfPresent(fullDb, "hajj_umrah", pool);
        } else if (key.equals("zakat_charity") || key.equals("zakat")) {
            addIfPresent(fullDb, "zakat_charity", pool);
        } else if (key.equals("islamic_akhlaq") || key.equals("akhlaq")) {
            addIfPresent(fullDb, "islamic_akhlaq", pool);
        } else if (key.equals("dua_azkar") || key.equals("dua")) {
            addIfPresent(fullDb, "dua_azkar", pool);
        } else if (key.equals("holy_mosques") || key.equals("mosques")) {
            addIfPresent(fullDb, "holy_mosques", pool);
        } else if (key.equals("akhira_qiyamah") || key.equals("akhira")) {
            addIfPresent(fullDb, "akhira_qiyamah", pool);
        } else if (key.equals("noble_women") || key.equals("women")) {
            addIfPresent(fullDb, "noble_women", pool);
        } else if (key.equals("iman") || key.equals("iman_aqeedah") || key.equals("aqeedah")) {
            addIfPresent(fullDb, "iman_aqeedah", pool);
            addIfPresent(fullDb, "quran_knowledge", pool);
        }

        // Final safety fallback: If still empty, collect from all categories
        if (pool.isEmpty()) {
            for (List<BattleQuestion> bqList : fullDb.values()) {
                if (bqList != null) pool.addAll(bqList);
            }
        }

        return pool;
    }

    private static void addIfPresent(Map<String, List<BattleQuestion>> db, String key, List<BattleQuestion> out) {
        if (db.containsKey(key) && db.get(key) != null) {
            for (BattleQuestion bq : db.get(key)) {
                if (!out.contains(bq)) {
                    out.add(bq);
                }
            }
        }
    }
}


