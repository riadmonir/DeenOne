package com.devflux.deenone.features.faraid.rules;

import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry of classical Islamic Inheritance Rules (Requirement 29).
 */
public class FaraidRuleRegistry {

    private static final List<FaraidRule> RULES = new ArrayList<>();

    static {
        // 1. Father with male descendants (1/6)
        RULES.add(new FaraidRule(
                "RULE_FATHER_MALE_CHILD",
                "father",
                "মৃতের ছেলে বা নাতি উপস্থিত থাকলে",
                Collections.emptyList(),
                FaraidFraction.ONE_SIXTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.singletonList("Sahih Bukhari: 6732 (https://sunnah.com/bukhari:6732)"),
                "মৃত ব্যক্তির ছেলে বা নাতি জীবিত থাকলে পিতা কোরআনিক নির্ধারিত অংশীদার (আসহাবুল ফুরুজ) হিসেবে ১/৬ অংশ পাবেন।",
                "Father receives 1/6 fixed Quranic share when male descendants are alive."
        ));

        // 2. Father with female descendants only (1/6 + Asabah)
        RULES.add(new FaraidRule(
                "RULE_FATHER_FEMALE_CHILD_ONLY",
                "father",
                "মৃতের কেবল কন্যা/নাতনি উপস্থিত থাকলে",
                Collections.emptyList(),
                FaraidFraction.ONE_SIXTH,
                FaraidRule.ShareType.FIXED_AND_RESIDUE,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.singletonList("Sahih Bukhari: 6732 (https://sunnah.com/bukhari:6732)"),
                "মৃত ব্যক্তির কেবল কন্যা বা নাতনি থাকলে পিতা ১/৬ নির্ধারিত অংশ এবং অন্যান্যদের অংশ দেওয়ার পর অবশিষ্ট সম্পদ আসাবা হিসেবে পাবেন।",
                "Father receives 1/6 fixed share plus remaining residue as Asabah when only female descendants exist."
        ));

        // 3. Father without descendants (Asabah only)
        RULES.add(new FaraidRule(
                "RULE_FATHER_NO_CHILDREN",
                "father",
                "মৃতের কোনো সন্তান বা নাতি-নাতনি না থাকলে",
                Collections.emptyList(),
                FaraidFraction.ZERO,
                FaraidRule.ShareType.RESIDUE,
                FaraidRule.Madhhab.ALL,
                3,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.singletonList("Sahih Bukhari: 6732 (https://sunnah.com/bukhari:6732)"),
                "সন্তানহীন অবস্থায় পিতা নিকটতম পুরুষ আসাবা (bi-nafsihi) হিসেবে সর্বাবশিষ্ট সম্পত্তির একক বা প্রধান অধিকারী হবেন।",
                "Father inherits entire remaining residue as primary male Asabah when there are no descendants."
        ));

        // 4. Mother with children or multiple siblings (1/6)
        RULES.add(new FaraidRule(
                "RULE_MOTHER_WITH_CHILD_OR_SIBLINGS",
                "mother",
                "মৃতের সন্তান থাকলে অথবা একাধিক (২ বা ততোধিক) ভাই-বোন থাকলে",
                Collections.emptyList(),
                FaraidFraction.ONE_SIXTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.emptyList(),
                "মৃতের সন্তান থাকলে বা একাধিক ভাই-বোন জীবিত থাকলে মা ১/৬ অংশ পাবেন।",
                "Mother receives 1/6 fixed Quranic share when deceased leaves children or 2+ siblings."
        ));

        // 5. Mother without children and <2 siblings (1/3)
        RULES.add(new FaraidRule(
                "RULE_MOTHER_NO_CHILD_NO_SIBLINGS",
                "mother",
                "মৃতের সন্তান না থাকলে এবং ভাই-বোন ২ জনের কম হলে",
                Collections.emptyList(),
                FaraidFraction.ONE_THIRD,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.emptyList(),
                "সন্তান ও একাধিক ভাই-বোনের অনুপস্থিতিতে মা পূর্ণ সম্পত্তির ১/৩ অংশ পাবেন।",
                "Mother receives 1/3 of the total estate in the absence of children and multiple siblings."
        ));

        // 6. Husband without children (1/2)
        RULES.add(new FaraidRule(
                "RULE_HUSBAND_NO_CHILDREN",
                "husband",
                "মৃত স্ত্রীর কোনো সন্তান না থাকলে",
                Collections.emptyList(),
                FaraidFraction.HALF,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:12 (https://quran.com/4/12)"),
                Collections.emptyList(),
                "মৃত স্ত্রীর কোনো সন্তান বা নাতি-নাতনি না থাকলে স্বামী ১/২ অংশ পাবেন।",
                "Husband receives 1/2 fixed share when deceased wife has no children."
        ));

        // 7. Husband with children (1/4)
        RULES.add(new FaraidRule(
                "RULE_HUSBAND_WITH_CHILDREN",
                "husband",
                "মৃত স্ত্রীর সন্তান থাকলে",
                Collections.emptyList(),
                FaraidFraction.ONE_FOURTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:12 (https://quran.com/4/12)"),
                Collections.emptyList(),
                "মৃত স্ত্রীর সন্তান বা নাতি-নাতনি থাকলে স্বামী ১/৪ অংশ পাবেন।",
                "Husband receives 1/4 fixed share when deceased wife leaves children."
        ));

        // 8. Wife without children (1/4)
        RULES.add(new FaraidRule(
                "RULE_WIFE_NO_CHILDREN",
                "wife",
                "মৃত স্বামীর কোনো সন্তান না থাকলে",
                Collections.emptyList(),
                FaraidFraction.ONE_FOURTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:12 (https://quran.com/4/12)"),
                Collections.emptyList(),
                "মৃত স্বামীর সন্তান না থাকলে স্ত্রী/স্ত্রীগণ যৌথভাবে ১/৪ অংশ পাবেন।",
                "Wives collectively receive 1/4 fixed share when deceased husband has no children."
        ));

        // 9. Wife with children (1/8)
        RULES.add(new FaraidRule(
                "RULE_WIFE_WITH_CHILDREN",
                "wife",
                "মৃত স্বামীর সন্তান থাকলে",
                Collections.emptyList(),
                FaraidFraction.ONE_EIGHTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:12 (https://quran.com/4/12)"),
                Collections.emptyList(),
                "মৃত স্বামীর সন্তান বা নাতি-নাতনি থাকলে স্ত্রী/স্ত্রীগণ যৌথভাবে ১/৮ অংশ পাবেন।",
                "Wives collectively receive 1/8 fixed share when deceased husband leaves children."
        ));

        // 10. Single Daughter without sons (1/2)
        RULES.add(new FaraidRule(
                "RULE_DAUGHTER_SINGLE",
                "daughter",
                "মৃতের পুত্র না থাকলে এবং একমাত্র কন্যা থাকলে",
                Collections.singletonList("son"),
                FaraidFraction.HALF,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.emptyList(),
                "পুত্র সন্তান না থাকলে একমাত্র কন্যা সম্পত্তির ১/২ অংশ পাবেন।",
                "Single daughter receives 1/2 fixed share when there are no sons."
        ));

        // 11. Multiple Daughters without sons (2/3)
        RULES.add(new FaraidRule(
                "RULE_DAUGHTERS_MULTIPLE",
                "daughter",
                "মৃতের পুত্র না থাকলে এবং ২ বা ততোধিক কন্যা থাকলে",
                Collections.singletonList("son"),
                FaraidFraction.TWO_THIRDS,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.emptyList(),
                "পুত্র সন্তান না থাকলে ২ বা ততোধিক কন্যা যৌথভাবে সমান ভাগে ২/৩ অংশ পাবেন।",
                "Two or more daughters collectively receive 2/3 fixed share when there are no sons."
        ));

        // 12. Sons and Daughters together (Asabah bil-Ghayr 2:1)
        RULES.add(new FaraidRule(
                "RULE_SONS_AND_DAUGHTERS_ASABAH",
                "son",
                "পুত্র ও কন্যা একত্রে জীবিত থাকলে",
                Collections.emptyList(),
                FaraidFraction.ZERO,
                FaraidRule.ShareType.RESIDUE,
                FaraidRule.Madhhab.ALL,
                3,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.emptyList(),
                "পুত্র ও কন্যা একত্রে থাকলে কন্যা আসাবা বিল-গাইর হবে এবং ২:১ অনুপাতে অবশিষ্ট সম্পত্তি বণ্টন হবে।",
                "Daughters become Asabah bi-ghayrihi with sons, sharing residue in a 2:1 ratio."
        ));

        // 13. Granddaughter Takmilat al-Thuluthayn (1/6)
        RULES.add(new FaraidRule(
                "RULE_GRANDDAUGHTER_TAKMILAT",
                "granddaughter",
                "এক কন্যা উপস্থিত থাকলে নাতনির ২/৩ পূর্ণ করার অংশ",
                Collections.singletonList("son"),
                FaraidFraction.ONE_SIXTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:11 (https://quran.com/4/11)"),
                Collections.singletonList("Sahih Bukhari: 6737 (https://sunnah.com/bukhari:6737)"),
                "এক কন্যার ১/২ অংশের সাথে ২/৩ পূর্ণ করতে নাতনি ১/৬ (তাকমিলাতুস সুলুসাইন) পাবে।",
                "Granddaughter receives 1/6 to complete the 2/3 share (Takmilat al-Thuluthayn) with one daughter."
        ));

        // 14. Full Sister single in Kalalah (1/2)
        RULES.add(new FaraidRule(
                "RULE_FULL_SISTER_SINGLE",
                "full_sister",
                "কালালাহ অবস্থায় একক বোন",
                Collections.singletonList("father"),
                FaraidFraction.HALF,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:176 (https://quran.com/4/176)"),
                Collections.emptyList(),
                "কালালাহ (পিতা ও সন্তানহীন) অবস্থায় একক সহোদর বোন ১/২ অংশ পাবে।",
                "Single full sister receives 1/2 fixed share in Kalalah state."
        ));

        // 15. Full Sisters multiple in Kalalah (2/3)
        RULES.add(new FaraidRule(
                "RULE_FULL_SISTERS_MULTIPLE",
                "full_sister",
                "কালালাহ অবস্থায় একাধিক বোন",
                Collections.singletonList("father"),
                FaraidFraction.TWO_THIRDS,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:176 (https://quran.com/4/176)"),
                Collections.emptyList(),
                "কালালাহ অবস্থায় একাধিক সহোদর বোন সম্মিলিতভাবে ২/৩ অংশ সমান ভাগে পাবে।",
                "Multiple full sisters collectively receive 2/3 fixed share in Kalalah state."
        ));

        // 16. Uterine Brother/Sister single (1/6)
        RULES.add(new FaraidRule(
                "RULE_UTERINE_SIBLING_SINGLE",
                "uterine_brother",
                "কালালাহ অবস্থায় একক বৈপিত্রীয় ভাই বা বোন",
                Collections.singletonList("father"),
                FaraidFraction.ONE_SIXTH,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                1,
                Collections.singletonList("Quran 4:12 (https://quran.com/4/12)"),
                Collections.emptyList(),
                "কালালাহ অবস্থায় একক বৈপিত্রীয় ভাই বা বোন ১/৬ অংশ পাবে।",
                "Single uterine brother or sister receives 1/6 fixed share in Kalalah."
        ));

        // 17. Uterine Siblings multiple (1/3)
        RULES.add(new FaraidRule(
                "RULE_UTERINE_SIBLINGS_MULTIPLE",
                "uterine_brother",
                "কালালাহ অবস্থায় একাধিক বৈপিত্রীয় ভাই-বোন",
                Collections.singletonList("father"),
                FaraidFraction.ONE_THIRD,
                FaraidRule.ShareType.FIXED,
                FaraidRule.Madhhab.ALL,
                2,
                Collections.singletonList("Quran 4:12 (https://quran.com/4/12)"),
                Collections.emptyList(),
                "কালালাহ অবস্থায় একাধিক বৈপিত্রীয় ভাই-বোন সমান ভাগে ১/৩ অংশ ভাগ করে নেবে (নারী-পুরুষ ১:১)।",
                "Multiple uterine siblings share 1/3 equally regardless of gender (1:1 ratio) in Kalalah."
        ));
    }

    public static List<FaraidRule> getAllRules() {
        return Collections.unmodifiableList(RULES);
    }

    public static List<FaraidRule> getRulesForHeir(String heirType) {
        return getRulesForHeir(heirType, null);
    }

    public static List<FaraidRule> getRulesForHeir(String heirType, FaraidInput.Madhab madhab) {
        if (heirType == null) return Collections.emptyList();
        List<FaraidRule> matching = new ArrayList<>();
        for (FaraidRule r : RULES) {
            if (r.getHeirType().equalsIgnoreCase(heirType)) {
                if (madhab == null || r.appliesToMadhab(madhab)) {
                    matching.add(r);
                }
            }
        }
        return matching;
    }

    public static FaraidRule getRuleById(String ruleId) {
        if (ruleId == null) return null;
        for (FaraidRule r : RULES) {
            if (r.getRuleId().equalsIgnoreCase(ruleId)) {
                return r;
            }
        }
        // Aliases mapping
        if ("RULE_FATHER_ONE_SIXTH_WITH_MALE_CHILD".equalsIgnoreCase(ruleId)) return getRuleById("RULE_FATHER_MALE_CHILD");
        if ("RULE_WIFE_ONE_EIGHTH_WITH_CHILDREN".equalsIgnoreCase(ruleId)) return getRuleById("RULE_WIFE_WITH_CHILDREN");
        if ("RULE_FULL_SISTER_SINGLE_HALF".equalsIgnoreCase(ruleId)) return getRuleById("RULE_FULL_SISTER_SINGLE");
        if ("RULE_GRANDDAUGHTER_TAKMILAT_AL_THULUTHAYN".equalsIgnoreCase(ruleId)) return getRuleById("RULE_GRANDDAUGHTER_TAKMILAT");
        return null;
    }
}