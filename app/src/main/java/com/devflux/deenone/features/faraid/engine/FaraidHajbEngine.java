package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirHajbDecision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Dedicated Blocking / Hajb Engine (Requirement 15).
 * Evaluates every entered heir category and returns one of:
 * - ELIGIBLE_FIXED
 * - ELIGIBLE_RESIDUARY
 * - ELIGIBLE_FIXED_AND_RESIDUARY
 * - BLOCKED
 * - NOT_APPLICABLE
 *
 * Implements strict classical Hajb Hirman (total blocking) and Hajb Nuqsan (partial reduction)
 * across all Sunni Madhabs, using natural Bangladeshi terminology (e.g. নাতি, নাতনি, সৎ ভাই, সৎ বোন).
 */
public class FaraidHajbEngine {

    public static Map<String, HeirHajbDecision> evaluateAll(FaraidInput input) {
        if (input == null) return Collections.emptyMap();

        Map<String, HeirHajbDecision> decisions = new LinkedHashMap<>();

        int sons = input.getSonCount();
        int daughters = input.getDaughterCount();
        int grandsons = input.getGrandsonCount();
        int granddaughters = input.getGranddaughterCount();

        boolean hasMaleDescendant = (sons > 0 || grandsons > 0);
        boolean hasOnlyFemaleDescendants = (!hasMaleDescendant && (daughters > 0 || granddaughters > 0));
        boolean hasDescendants = (hasMaleDescendant || hasOnlyFemaleDescendants);

        int fullBrothers = input.getFullBrotherCount();
        int fullSisters = input.getFullSisterCount();
        int consBrothers = input.getConsanguineBrotherCount();
        int consSisters = input.getConsanguineSisterCount();
        int uBrothers = input.getUterineBrotherCount();
        int uSisters = input.getUterineSisterCount();

        boolean grandfatherAlive = input.isPaternalGrandfatherAlive();
        FaraidInput.Madhab madhab = input.getMadhab();
        boolean applyBdLaw = input.isApplyBangladeshLaw1961();

        // 1. Husband
        if (input.getDeceasedGender() == FaraidInput.Gender.FEMALE) {
            if (input.isHusbandAlive()) {
                decisions.put("husband", new HeirHajbDecision(
                        "husband", "স্বামী", 1,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "স্বামী কখনো মাহজুব (বঞ্চিত) হন না (সূরা আন-নিসা: ১২)।",
                        "সূরা আন-নিসা: ১২ ও ইজমা"
                ));
            } else {
                decisions.put("husband", new HeirHajbDecision("husband", "স্বামী", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
            }
        }

        // 2. Wife
        if (input.getDeceasedGender() == FaraidInput.Gender.MALE) {
            int wives = input.getWifeCount();
            if (wives > 0) {
                decisions.put("wife", new HeirHajbDecision(
                        "wife", wives == 1 ? "স্ত্রী" : "স্ত্রীগণ (" + wives + " জন)", wives,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "স্ত্রী কখনো মাহজুব (বঞ্চিত) হন না (সূরা আন-নিসা: ১২)।",
                        "সূরা আন-নিসা: ১২ ও ইজমা"
                ));
            } else {
                decisions.put("wife", new HeirHajbDecision("wife", "স্ত্রী", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
            }
        }

        // 3. Father
        if (input.isFatherAlive()) {
            HeirEligibilityStatus fStatus = hasMaleDescendant ? HeirEligibilityStatus.ELIGIBLE_FIXED :
                    (hasOnlyFemaleDescendants ? HeirEligibilityStatus.ELIGIBLE_FIXED_AND_RESIDUARY : HeirEligibilityStatus.ELIGIBLE_RESIDUARY);
            decisions.put("father", new HeirHajbDecision(
                    "father", "পিতা", 1,
                    fStatus, "",
                    "Eligible", "পিতা কখনো উত্তরাধিকার থেকে বঞ্চিত হন না (সূরা আন-নিসা: ১১)।",
                    "সূরা আন-নিসা: ১১ ও সহীহ বুখারী: ৬৭৩২"
            ));
        } else {
            decisions.put("father", new HeirHajbDecision("father", "পিতা", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 4. Mother
        if (input.isMotherAlive()) {
            decisions.put("mother", new HeirHajbDecision(
                    "mother", "মাতা", 1,
                    HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                    "Eligible", "মাতা কখনো উত্তরাধিকার থেকে বঞ্চিত হন না (সূরা আন-নিসা: ১১)।",
                    "সূরা আন-নিসা: ১১ ও ইজমা"
            ));
        } else {
            decisions.put("mother", new HeirHajbDecision("mother", "মাতা", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 5. Son
        if (sons > 0) {
            decisions.put("son", new HeirHajbDecision(
                    "son", sons == 1 ? "পুত্র" : "পুত্রগণ (" + sons + " জন)", sons,
                    HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                    "Eligible", "ছেলে আসাবা হিসেবে অগ্রাধিকার পান এবং কখনো বঞ্চিত হন না।",
                    "সূরা আন-নিসা: ১১ ও সহীহ বুখারী: ৬৭৩২"
            ));
        } else {
            decisions.put("son", new HeirHajbDecision("son", "পুত্র", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 6. Daughter
        if (daughters > 0) {
            HeirEligibilityStatus dStatus = (sons > 0) ? HeirEligibilityStatus.ELIGIBLE_RESIDUARY : HeirEligibilityStatus.ELIGIBLE_FIXED;
            decisions.put("daughter", new HeirHajbDecision(
                    "daughter", daughters == 1 ? "কন্যা" : "কন্যাগণ (" + daughters + " জন)", daughters,
                    dStatus, "",
                    "Eligible", "কন্যা কখনো উত্তরাধিকার থেকে বঞ্চিত হন না।",
                    "সূরা আন-নিসা: ১১"
            ));
        } else {
            decisions.put("daughter", new HeirHajbDecision("daughter", "কন্যা", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 7. Grandson (পুত্রের ছেলে / নাতি)
        if (grandsons > 0) {
            if (sons > 0 && !applyBdLaw) {
                decisions.put("grandson", new HeirHajbDecision(
                        "grandson", grandsons == 1 ? "নাতি" : "নাতিগণ (" + grandsons + " জন)", grandsons,
                        HeirEligibilityStatus.BLOCKED, "পুত্র",
                        "Son's son is excluded because a qualifying son of the deceased is present.",
                        "মৃতের প্রত্যক্ষ পুত্র জীবিত থাকায় নাতি মাহজুব (বঞ্চিত) হন।",
                        "সহীহ বুখারী: ৬৭৩২ ও ইজমা"
                ));
            } else {
                decisions.put("grandson", new HeirHajbDecision(
                        "grandson", grandsons == 1 ? "নাতি" : "নাতিগণ (" + grandsons + " জন)", grandsons,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "ছেলের অবর্তমানে নাতি আসাবা হিসেবে উত্তরাধিকার লাভ করেন।",
                        applyBdLaw ? "বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ ধারা ৪" : "সহীহ বুখারী: ৬৭৩২"
                ));
            }
        } else {
            decisions.put("grandson", new HeirHajbDecision("grandson", "নাতি", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 8. Granddaughter (পুত্রের মেয়ে / নাতনি)
        if (granddaughters > 0) {
            if (sons > 0 && !applyBdLaw) {
                decisions.put("granddaughter", new HeirHajbDecision(
                        "granddaughter", granddaughters == 1 ? "নাতনি" : "নাতনিগণ (" + granddaughters + " জন)", granddaughters,
                        HeirEligibilityStatus.BLOCKED, "পুত্র",
                        "Son's daughter is excluded because a qualifying son of the deceased is present.",
                        "মৃতের প্রত্যক্ষ পুত্র জীবিত থাকায় নাতনি মাহজুব (বঞ্চিত) হন।",
                        "সহীহ বুখারী: ৬৭৩২ ও ইজমা"
                ));
            } else if (sons == 0 && daughters >= 2 && grandsons == 0 && !applyBdLaw) {
                decisions.put("granddaughter", new HeirHajbDecision(
                        "granddaughter", granddaughters == 1 ? "নাতনি" : "নাতনিগণ (" + granddaughters + " জন)", granddaughters,
                        HeirEligibilityStatus.BLOCKED, "২ বা ততোধিক কন্যা",
                        "Son's daughter is excluded because two or more direct daughters exhaust the two-thirds quota.",
                        "২ বা ততোধিক কন্যা কর্তৃক সর্বোচ্চ ২/৩ অংশ পূর্ণ হয়ে যাওয়ায় এবং কোনো নাতি না থাকায় নাতনি মাহজুব হন।",
                        "সূরা আন-নিসা: ১১ ও ইজমা"
                ));
            } else if (grandsons > 0) {
                decisions.put("granddaughter", new HeirHajbDecision(
                        "granddaughter", granddaughters == 1 ? "নাতনি" : "নাতনিগণ (" + granddaughters + " জন)", granddaughters,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "নাতির সাথে নাতনি আসাবা বিল গাইর (২:১) হিসেবে অংশীদার হন।",
                        "সহীহ বুখারী: ৬৭৩৬"
                ));
            } else {
                decisions.put("granddaughter", new HeirHajbDecision(
                        "granddaughter", granddaughters == 1 ? "নাতনি" : "নাতনিগণ (" + granddaughters + " জন)", granddaughters,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", daughters == 1 ? "এক কন্যার সাথে নাতনি ২/৩ পূর্ণ করার নিমিত্তে ১/৬ অংশ পাবেন।" : "কন্যা না থাকায় নাতনি নির্ধারিত অংশ পাবেন।",
                        "সহীহ বুখারী: ৬৭৩৬"
                ));
            }
        } else {
            decisions.put("granddaughter", new HeirHajbDecision("granddaughter", "নাতনি", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 9. Paternal Grandfather (দাদা)
        if (grandfatherAlive) {
            if (input.isFatherAlive()) {
                decisions.put("paternal_grandfather", new HeirHajbDecision(
                        "paternal_grandfather", "দাদা", 1,
                        HeirEligibilityStatus.BLOCKED, "পিতা",
                        "Paternal grandfather is excluded because father is present.",
                        "পিতার উপস্থিতিতে দাদা সম্পূর্ণ মাহজুব (বঞ্চিত) হন।",
                        "ইজমা ও সহীহ বুখারী: ৬৭৩২"
                ));
            } else {
                HeirEligibilityStatus gfStatus = hasMaleDescendant ? HeirEligibilityStatus.ELIGIBLE_FIXED :
                        (hasOnlyFemaleDescendants ? HeirEligibilityStatus.ELIGIBLE_FIXED_AND_RESIDUARY : HeirEligibilityStatus.ELIGIBLE_RESIDUARY);
                decisions.put("paternal_grandfather", new HeirHajbDecision(
                        "paternal_grandfather", "দাদা", 1,
                        gfStatus, "",
                        "Eligible", "বাবার অবর্তমানে দাদা উত্তরাধিকার লাভ করেন।",
                        "সহীহ বুখারী ও ইজমা"
                ));
            }
        } else {
            decisions.put("paternal_grandfather", new HeirHajbDecision("paternal_grandfather", "দাদা", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 10. Paternal Grandmother (দাদী)
        boolean pGrandmother = input.isPaternalGrandmotherAlive();
        if (pGrandmother) {
            if (input.isMotherAlive()) {
                decisions.put("paternal_grandmother", new HeirHajbDecision(
                        "paternal_grandmother", "দাদী", 1,
                        HeirEligibilityStatus.BLOCKED, "মাতা",
                        "Paternal grandmother is excluded because mother is present.",
                        "মায়ের উপস্থিতিতে দাদী সম্পূর্ণ মাহজুব হন।",
                        "সুনান আবু দাউদ: ২৮৯৪ ও ইজমা"
                ));
            } else if (input.isFatherAlive() && (madhab == FaraidInput.Madhab.HANAFI || madhab == FaraidInput.Madhab.HANBALI)) {
                decisions.put("paternal_grandmother", new HeirHajbDecision(
                        "paternal_grandmother", "দাদী", 1,
                        HeirEligibilityStatus.BLOCKED, "পিতা",
                        "Paternal grandmother is excluded because father is present in Hanafi and Hanbali schools.",
                        "বাবার উপস্থিতির কারণে হানাফি ও হাম্বলী মাযহাবে দাদী মাহজুব হন।",
                        "আল-হিদায়াহ ও হানাফি ফিকহ"
                ));
            } else {
                decisions.put("paternal_grandmother", new HeirHajbDecision(
                        "paternal_grandmother", "দাদী", 1,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "মায়ের অবর্তমানে দাদী ১/৬ অংশ লাভ করেন।",
                        "সুনান আবু দাউদ: ২৮৯৪"
                ));
            }
        } else {
            decisions.put("paternal_grandmother", new HeirHajbDecision("paternal_grandmother", "দাদী", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 11. Maternal Grandmother (নানী)
        boolean mGrandmother = input.isMaternalGrandmotherAlive();
        if (mGrandmother) {
            if (input.isMotherAlive()) {
                decisions.put("maternal_grandmother", new HeirHajbDecision(
                        "maternal_grandmother", "নানী", 1,
                        HeirEligibilityStatus.BLOCKED, "মাতা",
                        "Maternal grandmother is excluded because mother is present.",
                        "মায়ের উপস্থিতিতে নানী সম্পূর্ণ মাহজুব হন।",
                        "সুনান আবু দাউদ: ২৮৯৪ ও ইজমা"
                ));
            } else {
                decisions.put("maternal_grandmother", new HeirHajbDecision(
                        "maternal_grandmother", "নানী", 1,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "মায়ের অবর্তমানে নানী ১/৬ অংশ লাভ করেন।",
                        "সুনান আবু দাউদ: ২৮৯৪"
                ));
            }
        } else {
            decisions.put("maternal_grandmother", new HeirHajbDecision("maternal_grandmother", "নানী", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // Sibling blocking context
        boolean siblingsBlockedByAscOrDesc = (hasMaleDescendant || input.isFatherAlive() ||
                (grandfatherAlive && !input.isFatherAlive() && madhab == FaraidInput.Madhab.HANAFI));
        String siblingBlocker = input.isFatherAlive() ? "পিতা" : (sons > 0 ? "পুত্র" : (grandsons > 0 ? "নাতি" : "দাদা (হানাফি মাযহাব)"));
        String siblingBlockerEn = input.isFatherAlive() ? "father" : (sons > 0 ? "son" : (grandsons > 0 ? "grandson" : "paternal grandfather under Hanafi school"));

        boolean fullSisterIsAsabahMaAlGhayr = (fullBrothers == 0 && fullSisters > 0 && hasOnlyFemaleDescendants && !siblingsBlockedByAscOrDesc);

        // 12. Full Brother (সহোদর ভাই / আপন ভাই)
        if (fullBrothers > 0) {
            if (siblingsBlockedByAscOrDesc) {
                decisions.put("full_brother", new HeirHajbDecision(
                        "full_brother", fullBrothers == 1 ? "সহোদর ভাই" : "সহোদর ভাইগণ (" + fullBrothers + " জন)", fullBrothers,
                        HeirEligibilityStatus.BLOCKED, siblingBlocker,
                        "Full brother is excluded because " + siblingBlockerEn + " is present.",
                        siblingBlocker + "-এর উপস্থিতিতে সহোদর ভাই মাহজুব (বঞ্চিত) হন।",
                        "সূরা আন-নিসা: ১৭৬ ও সহীহ বুখারী: ৬৭৩২"
                ));
            } else {
                decisions.put("full_brother", new HeirHajbDecision(
                        "full_brother", fullBrothers == 1 ? "সহোদর ভাই" : "সহোদর ভাইগণ (" + fullBrothers + " জন)", fullBrothers,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "কালালাহ অবস্থায় সহোদর ভাই আসাবা হিসেবে অংশ পাবেন।",
                        "সূরা আন-নিসা: ১৭৬"
                ));
            }
        } else {
            decisions.put("full_brother", new HeirHajbDecision("full_brother", "সহোদর ভাই", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 13. Full Sister (সহোদর বোন / আপন বোন)
        if (fullSisters > 0) {
            if (siblingsBlockedByAscOrDesc) {
                decisions.put("full_sister", new HeirHajbDecision(
                        "full_sister", fullSisters == 1 ? "সহোদর বোন" : "সহোদর বোনগণ (" + fullSisters + " জন)", fullSisters,
                        HeirEligibilityStatus.BLOCKED, siblingBlocker,
                        "Full sister is excluded because " + siblingBlockerEn + " is present.",
                        siblingBlocker + "-এর উপস্থিতিতে সহোদর বোন মাহজুব হন।",
                        "সূরা আন-নিসা: ১৭৬ ও সহীহ বুখারী: ৬৭৩২"
                ));
            } else if (fullBrothers > 0) {
                decisions.put("full_sister", new HeirHajbDecision(
                        "full_sister", fullSisters == 1 ? "সহোদর বোন" : "সহোদর বোনগণ (" + fullSisters + " জন)", fullSisters,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "সহোদর ভাইয়ের সাথে বোন আসাবা বিল গাইর (২:১) হিসেবে অংশ পাবেন।",
                        "সূরা আন-নিসা: ১৭৬"
                ));
            } else if (hasOnlyFemaleDescendants) {
                decisions.put("full_sister", new HeirHajbDecision(
                        "full_sister", fullSisters == 1 ? "সহোদর বোন" : "সহোদর বোনগণ (" + fullSisters + " জন)", fullSisters,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "কন্যার উপস্থিতিতে সহোদর বোন আসাবা মা'আল গাইর হিসেবে অবশিষ্টাংশ পাবেন।",
                        "সহীহ বুখারী: ৬৭৪২"
                ));
            } else {
                decisions.put("full_sister", new HeirHajbDecision(
                        "full_sister", fullSisters == 1 ? "সহোদর বোন" : "সহোদর বোনগণ (" + fullSisters + " জন)", fullSisters,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "পিতা, সন্তান ও ভাই না থাকায় বোন আসহাবুল ফুরুজ হিসেবে অংশ পাবেন।",
                        "সূরা আন-নিসা: ১৭৬"
                ));
            }
        } else {
            decisions.put("full_sister", new HeirHajbDecision("full_sister", "সহোদর বোন", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 14. Paternal Half-Brother (বৈমাত্রেয় ভাই / সৎ ভাই - বাবার দিক)
        if (consBrothers > 0) {
            boolean consBroBlocked = siblingsBlockedByAscOrDesc || (fullBrothers > 0) || fullSisterIsAsabahMaAlGhayr;
            if (consBroBlocked) {
                String cBlocker = siblingsBlockedByAscOrDesc ? siblingBlocker : (fullBrothers > 0 ? "সহোদর ভাই" : "আসাবা সহোদর বোন");
                String cBlockerEn = siblingsBlockedByAscOrDesc ? siblingBlockerEn : (fullBrothers > 0 ? "full brother" : "full sister inheriting as residuary");
                decisions.put("consanguine_brother", new HeirHajbDecision(
                        "consanguine_brother", consBrothers == 1 ? "সৎ ভাই (বাবার দিক)" : "সৎ ভাইগণ (বাবার দিক - " + consBrothers + " জন)", consBrothers,
                        HeirEligibilityStatus.BLOCKED, cBlocker,
                        "Paternal half-brother is excluded because " + cBlockerEn + " is present.",
                        cBlocker + "-এর উপস্থিতিতে বৈমাত্রেয় ভাই মাহজুব হন।",
                        "সূরা আন-নিসা: ১৭৬ ও ইজমা"
                ));
            } else {
                decisions.put("consanguine_brother", new HeirHajbDecision(
                        "consanguine_brother", consBrothers == 1 ? "সৎ ভাই (বাবার দিক)" : "সৎ ভাইগণ (বাবার দিক - " + consBrothers + " জন)", consBrothers,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "সহোদর ভাইয়ের অবর্তমানে বৈমাত্রেয় ভাই আসাবা হিসেবে অংশ পাবেন।",
                        "সূরা আন-নিসা: ১৭৬"
                ));
            }
        } else {
            decisions.put("consanguine_brother", new HeirHajbDecision("consanguine_brother", "সৎ ভাই (বাবার দিক)", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 15. Paternal Half-Sister (বৈমাত্রেয় বোন / সৎ বোন - বাবার দিক)
        if (consSisters > 0) {
            boolean consSisBlocked = siblingsBlockedByAscOrDesc || (fullBrothers > 0) || fullSisterIsAsabahMaAlGhayr ||
                    (fullSisters >= 2 && consBrothers == 0);
            if (consSisBlocked) {
                String csBlocker = siblingsBlockedByAscOrDesc ? siblingBlocker : (fullBrothers > 0 ? "সহোদর ভাই" : (fullSisterIsAsabahMaAlGhayr ? "আসাবা সহোদর বোন" : "২ বা ততোধিক সহোদর বোন"));
                String csBlockerEn = siblingsBlockedByAscOrDesc ? siblingBlockerEn : (fullBrothers > 0 ? "full brother" : (fullSisterIsAsabahMaAlGhayr ? "full sister inheriting as residuary" : "two or more full sisters"));
                decisions.put("consanguine_sister", new HeirHajbDecision(
                        "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)", consSisters,
                        HeirEligibilityStatus.BLOCKED, csBlocker,
                        "Paternal half-sister is excluded because " + csBlockerEn + " is present.",
                        csBlocker + "-এর উপস্থিতিতে বৈমাত্রেয় বোন মাহজুব হন।",
                        "সূরা আন-নিসা: ১৭৬ ও ইজমা"
                ));
            } else if (consBrothers > 0) {
                decisions.put("consanguine_sister", new HeirHajbDecision(
                        "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)", consSisters,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "বৈমাত্রেয় ভাইয়ের সাথে বোন আসাবা বিল গাইর (২:১) হবেন।",
                        "সূরা আন-নিসা: ১৭৬"
                ));
            } else if (hasOnlyFemaleDescendants && fullSisters == 0) {
                decisions.put("consanguine_sister", new HeirHajbDecision(
                        "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)", consSisters,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "কন্যার উপস্থিতিতে বৈমাত্রেয় বোন আসাবা মা'আল গাইর হবেন।",
                        "সহীহ বুখারী: ৬৭৪২"
                ));
            } else {
                decisions.put("consanguine_sister", new HeirHajbDecision(
                        "consanguine_sister", consSisters == 1 ? "সৎ বোন (বাবার দিক)" : "সৎ বোনগণ (বাবার দিক - " + consSisters + " জন)", consSisters,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", fullSisters == 1 ? "এক সহোদর বোনের সাথে বৈমাত্রেয় বোন ১/৬ অংশ পাবেন।" : "বৈমাত্রেয় বোন নির্ধারিত অংশ পাবেন।",
                        "সহীহ বুখারী ও ইজমা"
                ));
            }
        } else {
            decisions.put("consanguine_sister", new HeirHajbDecision("consanguine_sister", "সৎ বোন (বাবার দিক)", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 16. Maternal Half-Brother & Sister (বৈপিত্রীয় ভাই ও বোন / সৎ ভাই-বোন - মায়ের দিক)
        boolean uBlocked = hasDescendants || input.isFatherAlive() || grandfatherAlive;
        String uBlocker = input.isFatherAlive() ? "পিতা" : (hasDescendants ? "সন্তান বা বংশধর" : "দাদা");
        String uBlockerEn = input.isFatherAlive() ? "father" : (hasDescendants ? "descendant" : "grandfather");

        if (uBrothers > 0) {
            if (uBlocked) {
                decisions.put("uterine_brother", new HeirHajbDecision(
                        "uterine_brother", uBrothers == 1 ? "বৈপিত্রীয় ভাই" : "বৈপিত্রীয় ভাইগণ (" + uBrothers + " জন)", uBrothers,
                        HeirEligibilityStatus.BLOCKED, uBlocker,
                        "Maternal brother is excluded because " + uBlockerEn + " is present.",
                        uBlocker + "-এর উপস্থিতিতে বৈপিত্রীয় ভাই সম্পূর্ণ মাহজুব হন।",
                        "সূরা আন-নিসা: ১২ ও ইজমা"
                ));
            } else {
                decisions.put("uterine_brother", new HeirHajbDecision(
                        "uterine_brother", uBrothers == 1 ? "বৈপিত্রীয় ভাই" : "বৈপিত্রীয় ভাইগণ (" + uBrothers + " জন)", uBrothers,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "কালালাহ অবস্থায় বৈপিত্রীয় ভাই আসহাবুল ফুরুজ হিসেবে অংশ পাবেন (নারী-পুরুষ ১:১)।",
                        "সূরা আন-নিসা: ১২"
                ));
            }
        } else {
            decisions.put("uterine_brother", new HeirHajbDecision("uterine_brother", "বৈপিত্রীয় ভাই", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        if (uSisters > 0) {
            if (uBlocked) {
                decisions.put("uterine_sister", new HeirHajbDecision(
                        "uterine_sister", uSisters == 1 ? "বৈপিত্রীয় বোন" : "বৈপিত্রীয় বোনগণ (" + uSisters + " জন)", uSisters,
                        HeirEligibilityStatus.BLOCKED, uBlocker,
                        "Maternal sister is excluded because " + uBlockerEn + " is present.",
                        uBlocker + "-এর উপস্থিতিতে বৈপিত্রীয় বোন সম্পূর্ণ মাহজুব হন।",
                        "সূরা আন-নিসা: ১২ ও ইজমা"
                ));
            } else {
                decisions.put("uterine_sister", new HeirHajbDecision(
                        "uterine_sister", uSisters == 1 ? "বৈপিত্রীয় বোন" : "বৈপিত্রীয় বোনগণ (" + uSisters + " জন)", uSisters,
                        HeirEligibilityStatus.ELIGIBLE_FIXED, "",
                        "Eligible", "কালালাহ অবস্থায় বৈপিত্রীয় বোন আসহাবুল ফুরুজ হিসেবে অংশ পাবেন (নারী-পুরুষ ১:১)।",
                        "সূরা আন-নিসা: ১২"
                ));
            }
        } else {
            decisions.put("uterine_sister", new HeirHajbDecision("uterine_sister", "বৈপিত্রীয় বোন", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 17. Nephew (ভাতিজা)
        int nephews = input.getNephewCount();
        if (nephews > 0) {
            boolean nephewBlocked = hasMaleDescendant || input.isFatherAlive() || fullBrothers > 0 ||
                    consBrothers > 0 || fullSisterIsAsabahMaAlGhayr ||
                    (grandfatherAlive && madhab == FaraidInput.Madhab.HANAFI);
            if (nephewBlocked) {
                String nBlocker = input.isFatherAlive() ? "পিতা" : (sons > 0 ? "পুত্র" : (fullBrothers > 0 ? "সহোদর ভাই" : "নিকটবর্তী আসাবা"));
                String nBlockerEn = input.isFatherAlive() ? "father" : (sons > 0 ? "son" : (fullBrothers > 0 ? "full brother" : "closer residuary heir"));
                decisions.put("nephew", new HeirHajbDecision(
                        "nephew", nephews == 1 ? "ভাতিজা" : "ভাতিজাগণ (" + nephews + " জন)", nephews,
                        HeirEligibilityStatus.BLOCKED, nBlocker,
                        "Nephew is excluded because " + nBlockerEn + " is present.",
                        nBlocker + "-এর উপস্থিতিতে ভাতিজা মাহজুব হন।",
                        "সহীহ বুখারী: ৬৭৩২"
                ));
            } else {
                decisions.put("nephew", new HeirHajbDecision(
                        "nephew", nephews == 1 ? "ভাতিজা" : "ভাতিজাগণ (" + nephews + " জন)", nephews,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "নিকটবর্তী কোনো আসাবা না থাকায় ভাতিজা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                        "সহীহ বুখারী: ৬৭৩২"
                ));
            }
        } else {
            decisions.put("nephew", new HeirHajbDecision("nephew", "ভাতিজা", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        // 18. Paternal Uncle (চাচা)
        int uncles = input.getPaternalUncleCount();
        if (uncles > 0) {
            boolean uncleBlocked = hasMaleDescendant || input.isFatherAlive() || fullBrothers > 0 ||
                    consBrothers > 0 || fullSisterIsAsabahMaAlGhayr || nephews > 0 ||
                    (grandfatherAlive && madhab == FaraidInput.Madhab.HANAFI);
            if (uncleBlocked) {
                String uBlockerName = (nephews > 0 && !hasMaleDescendant && !input.isFatherAlive() && fullBrothers == 0) ? "ভাতিজা" :
                        (input.isFatherAlive() ? "পিতা" : (sons > 0 ? "পুত্র" : "নিকটবর্তী আসাবা"));
                String uBlockerNameEn = (nephews > 0 && !hasMaleDescendant && !input.isFatherAlive() && fullBrothers == 0) ? "nephew" :
                        (input.isFatherAlive() ? "father" : (sons > 0 ? "son" : "closer residuary heir"));
                decisions.put("uncle", new HeirHajbDecision(
                        "uncle", uncles == 1 ? "চাচা" : "চাচাগণ (" + uncles + " জন)", uncles,
                        HeirEligibilityStatus.BLOCKED, uBlockerName,
                        "Paternal uncle is excluded because " + uBlockerNameEn + " is present.",
                        uBlockerName + "-এর উপস্থিতিতে চাচা মাহজুব হন।",
                        "সহীহ বুখারী: ৬৭৩২ ও সহীহ মুসলিম: ১৬১৫"
                ));
            } else {
                decisions.put("uncle", new HeirHajbDecision(
                        "uncle", uncles == 1 ? "চাচা" : "চাচাগণ (" + uncles + " জন)", uncles,
                        HeirEligibilityStatus.ELIGIBLE_RESIDUARY, "",
                        "Eligible", "নিকটবর্তী কোনো আসাবা না থাকায় চাচা আসাবা হিসেবে অবশিষ্টাংশ পাবেন।",
                        "সহীহ বুখারী: ৬৭৩২"
                ));
            }
        } else {
            decisions.put("uncle", new HeirHajbDecision("uncle", "চাচা", 0, HeirEligibilityStatus.NOT_APPLICABLE, "", "", "", ""));
        }

        return decisions;
    }

    public static List<BlockedHeirInfo> extractBlockedHeirs(Map<String, HeirHajbDecision> decisions) {
        List<BlockedHeirInfo> list = new ArrayList<>();
        if (decisions == null) return list;
        for (HeirHajbDecision d : decisions.values()) {
            if (d.isBlocked() && d.getCount() > 0) {
                list.add(new BlockedHeirInfo(
                        d.getHeirTitleBn(),
                        d.getHeirKey(),
                        d.getCount(),
                        d.getBlockedByTitleBn(),
                        d.getReasonBn(),
                        d.getReasonEn(),
                        d.getShariahReference()
                ));
            }
        }
        return list;
    }
}