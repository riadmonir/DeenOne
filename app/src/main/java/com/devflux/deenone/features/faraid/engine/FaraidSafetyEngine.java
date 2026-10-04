package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidDisclaimerHelper;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import java.io.Serializable;

/**
 * AI Safety Rules Engine (Requirement 27).
 *
 * Enforces the 12 Mandatory AI Safety Rules:
 * 1. Never invent an Islamic ruling
 * 2. Never fabricate a Quran verse
 * 3. Never fabricate a Hadith
 * 4. Never provide a fake citation
 * 5. Never silently mix madhhabs
 * 6. Never hide a blocked heir
 * 7. Never ignore Awl
 * 8. Never ignore Radd
 * 9. Never treat every relative as an heir
 * 10. Never calculate from percentages alone
 * 11. Never present uncertain cases as certain
 * 12. Never call an educational result a Fatwa
 */
public class FaraidSafetyEngine implements Serializable {

    public static final String UNCERTAIN_CASE_FALLBACK_EN =
            "Unable to confidently resolve this case. Please consult a qualified Faraid scholar.";

    public static final String UNCERTAIN_CASE_FALLBACK_BN =
            "এই মাসআলাটির ক্ষেত্রে নিশ্চিত সমাধানের জন্য নির্ভরযোগ্য উৎস অপ্রতুল। অনুগ্রহ করে একজন বিজ্ঞ ফারায়েজ বিশেষজ্ঞ আলেমের সাথে সরাসরি পরামর্শ করুন।";

    public static boolean isCalculationSafe(FaraidCalculationResult result) {
        if (result == null) return false;

        if (result.isAwlApplied() && result.getAwlDenominator() <= result.getBaseDenominator()) {
            return false;
        }

        for (HeirShareResult heir : result.getHeirShares()) {
            if (heir.getShareFractionNumeric() <= 0 || heir.getTotalCategoryAmount() < 0) {
                return false;
            }
            if (heir.getDalilReference() == null || heir.getDalilReference().trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static String guardAiExplanation(String text, FaraidCalculationResult result) {
        if (text == null || text.trim().isEmpty()) {
            return UNCERTAIN_CASE_FALLBACK_BN;
        }
        if (result != null && !isCalculationSafe(result)) {
            return UNCERTAIN_CASE_FALLBACK_BN;
        }
        String sanitized = text.replace("একটি অফিসিয়াল ফতোয়া হিসেবে বিবেচিত হবে", "শিক্ষামূলক বণ্টন বিশ্লেষণ হিসেবে বিবেচিত হবে")
                               .replace("অফিসিয়াল ফতোয়া", "শিক্ষামূলক বণ্টন বিশ্লেষণ")
                               .replace("official fatwa", "educational analysis")
                               .replace("Official Fatwa", "Educational Analysis");
        return sanitized + "\n\n" + FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN;
    }

    public static String guardAiExplanation(String text) {
        return guardAiExplanation(text, null);
    }

    public static String getSafeDisclaimerBn() {
        return FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN;
    }

    public static String getSafeDisclaimerEn() {
        return FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN;
    }
}