package com.devflux.deenone.features.battle.engine;

import com.devflux.deenone.features.battle.model.BattleQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mandatory Server-Side Category Integrity Firewall & Validation Layer.
 * Ensures that 100% of questions served in a match strictly and exclusively belong
 * to the user-selected category (e.g. Salat -> only Salat; Ramadan -> only Ramadan; Hajj -> only Hajj).
 * Zero cross-category leakage is mathematically and logically permitted.
 */
public class CategoryIntegrityGuard {

    private static final Map<String, String[]> CATEGORY_EXCLUSIVE_KEYWORDS = new HashMap<>();

    static {
        // Keyword thematic integrity anchors for all major categories
        CATEGORY_EXCLUSIVE_KEYWORDS.put("salat_taharah", new String[]{"সালাত", "নামাজ", "ওজু", "গোসল", "তায়াম্মুম", "রাকাত", "সিজদা", "রুকু", "তাকবীর", "তাহাজ্জুদ", "বিতর", "ফজর", "যোহর", "আসর", "মাগরিব", "ইশা", "ইমাম", "কিবলা", "আযান"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("ramadan_sawm", new String[]{"রোজা", "সাওম", "সিয়াম", "রমজান", "সেহরি", "ইফতার", "লাইলাতুল কদর", "তারাবীহ", "ইতিকাফ", "শাবান", "শাওয়াল"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("hajj_umrah", new String[]{"হজ", "উমরা", "কাবা", "তাওয়াফ", "সাফা", "মারওয়া", "সায়ী", "আরাফাত", "মুজদালিফা", "মিনা", "ইহরাম", "তালবিয়া", "কুরবানি"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("zakat_charity", new String[]{"যাকাত", "সদকা", "নিসাব", "স্বর্ণ", "রৌপ্য", "ফিতরা", "ওশর", "গরিব", "মিসকিন"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("quran_stories", new String[]{"মূসা", "নূহ", "ইউনুস", "ইব্রাহিম", "আসহাবে কাহাফ", "ইউসুফ", "দাউদ", "সুলাইমান", "কুরআন", "পাহাড়", "কিশতী"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("akhira_qiyamah", new String[]{"কিয়ামত", "মীযান", "সিরাত", "পুলসিরাত", "হাশর", "কবর", "মুনকার", "নাকীর", "ইসরাফিল", "শিঙ্গা", "বারযাখ"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("seerat_un_nabi", new String[]{"রাসূলুল্লাহ", "মহানবী", "নবীজি", "বদর", "উহুদ", "খন্দক", "মদিনা", "মক্কা", "হিজরত", "ওহী", "হেরা", "কুরাইশ"});
        CATEGORY_EXCLUSIVE_KEYWORDS.put("hadith_sunnah", new String[]{"হাদিস", "বুখারী", "মুসলিম", "তিরমিযী", "আবু দাউদ", "নাসাঈ", "ইবনে মাজাহ", "রাবী", "সনদ", "সুন্নাহ"});
    }

    public static class IntegrityCheckResult {
        private final boolean isPassed;
        private final String message;

        public IntegrityCheckResult(boolean isPassed, String message) {
            this.isPassed = isPassed;
            this.message = message;
        }

        public boolean isPassed() { return isPassed; }
        public String getMessage() { return message; }
    }

    /**
     * Mandatory Category Validation Rule:
     * Question must have matching categoryId.
     */
    public static IntegrityCheckResult verifyQuestionCategory(BattleQuestion question, String selectedCategoryId) {
        if (question == null) {
            return new IntegrityCheckResult(false, "ত্রুটি: প্রশ্ন অবজেক্ট নাল (Null)");
        }
        if (selectedCategoryId == null || selectedCategoryId.trim().isEmpty()) {
            return new IntegrityCheckResult(false, "ত্রুটি: কোনো নির্দিষ্ট ক্যাটাগরি আইডি প্রদান করা হয়নি");
        }

        String targetCat = selectedCategoryId.trim().toLowerCase();
        String questionCat = question.getCategoryId() != null ? question.getCategoryId().trim().toLowerCase() : "";

        if (!targetCat.equals(questionCat)) {
            return new IntegrityCheckResult(false, "Category Integrity Violation: কাঙ্ক্ষিত ক্যাটাগরি [" + targetCat + "] কিন্তু পাওয়া গেছে [" + questionCat + "]");
        }

        return new IntegrityCheckResult(true, "Category Integrity Verified");
    }

    /**
     * Filters and purges any non-matching questions from a pool, guaranteeing 100% purity.
     */
    public static List<BattleQuestion> enforceCategoryPurity(List<BattleQuestion> rawQuestions, String selectedCategoryId) {
        List<BattleQuestion> pureList = new ArrayList<>();
        if (rawQuestions == null || selectedCategoryId == null) return pureList;

        String targetCat = selectedCategoryId.trim().toLowerCase();

        for (BattleQuestion q : rawQuestions) {
            IntegrityCheckResult check = verifyQuestionCategory(q, targetCat);
            if (check.isPassed()) {
                pureList.add(q);
            }
        }
        return pureList;
    }
}
