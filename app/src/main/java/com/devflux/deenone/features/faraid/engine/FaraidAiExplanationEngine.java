package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidDisclaimerHelper;
import com.devflux.deenone.features.faraid.model.HeirShareResult;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;

/**
 * AI Shariah Explanation Engine (Requirement 26).
 *
 * Provides clear, verified, pure Shariah explanations for already calculated Faraid results.
 * It DOES NOT calculate inheritance shares from natural language; it only explains deterministic engine outputs.
 */
public class FaraidAiExplanationEngine implements Serializable {

    public static String explainCalculation(FaraidCalculationResult result) {
        return explainCalculationInBangla(result);
    }

    public static String explainCalculation(FaraidCalculationResult result, boolean isBn) {
        return isBn ? explainCalculationInBangla(result) : explainCalculationInEnglish(result);
    }

    public static String explainCalculationInBangla(FaraidCalculationResult result) {
        if (result == null) {
            return "কোনো গণনার ফলাফল পাওয়া যায়নি।";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ফারায়েজ বণ্টনের সারসংক্ষেপ ও শরয়ী ব্যাখ্যা:\n\n");

        sb.append("১. ত্যাজ্যবিত্তের হিসাব:\n");
        sb.append("   • মোট ত্যাজ্য সম্পত্তি: ").append(result.getGrossEstateFormatted()).append("\n");
        sb.append("   • কাফন-দাফন ও আনুষঙ্গিক ব্যয়: ").append(result.getFuneralExpenseFormatted()).append("\n");
        sb.append("   • ঋণ ও দেনা-মোহরানা পরিশোধ: ").append(result.getDebtAmountFormatted()).append("\n");
        sb.append("   • কার্যকরী অসিয়ত (সর্বোচ্চ ১/৩ সীমা): ").append(result.getValidWasiyyahFormatted()).append("\n");
        sb.append("   • ওয়ারিশদের মাঝে বণ্টনযোগ্য অবশিষ্ট সম্পত্তি: ").append(result.getNetDistributableEstateFormatted()).append("\n\n");

        if (result.hasBlockedHeirs()) {
            sb.append("২. বঞ্চিত (মাহজুব) আত্মীয়স্বজন:\n");
            for (BlockedHeirInfo bh : result.getBlockedHeirs()) {
                sb.append("   • ").append(bh.getHeirTitleBn()).append(": ")
                        .append(bh.getLegalReasonBn()).append(" (উৎস: ").append(bh.getShariahReference()).append(")\n");
            }
            sb.append("\n");
        }

        sb.append("৩. জীবিত ওয়ারিশদের নির্ধারিত অংশ ও প্রাপ্য:\n");
        for (HeirShareResult share : result.getHeirShares()) {
            sb.append("   • ").append(share.getRelationTitleBn()).append(" (").append(share.getCount()).append(" জন)")
                    .append(" (হিস্যা: ").append(share.getShareFractionLabel())
                    .append(" বা ").append(BengaliNumberUtil.toBengali(String.format("%.2f", share.getPercentage()))).append("%)\n")
                    .append("     মোট প্রাপ্য: ").append(share.getTotalCategoryAmountFormatted(true)).append("\n")
                    .append("     কারণ: ").append(share.getLegalReasonBn()).append("\n")
                    .append("     দলিল: ").append(share.getDalilReference()).append("\n");
        }
        sb.append("\n");

        if (result.isAwlApplied()) {
            sb.append("৪. আওল (العول) প্রয়োগ:\n");
            sb.append("   নির্ধারিত হিস্যাসমূহের সমষ্টি ১ অতিক্রম করায় আনুপাতিক হারে মূল হর বৃদ্ধি করে হিস্যা সমন্বয় করা হয়েছে (হর: ")
                    .append(BengaliNumberUtil.toBengali(String.valueOf(result.getBaseDenominator()))).append(" থেকে ")
                    .append(BengaliNumberUtil.toBengali(String.valueOf(result.getAwlDenominator()))).append(" এ উন্নীত)।\n\n");
        } else if (result.isRaddApplied()) {
            sb.append("৪. রাদ্দ (الرد) প্রয়োগ:\n");
            sb.append("   আসাবা না থাকায় নির্ধারিত অংশীদারদের হিস্যা দেওয়ার পর উদ্বৃত্ত সম্পত্তি তাদের মূল অনুপাত অনুযায়ী পুনর্বণ্টন করা হয়েছে।\n\n");
        }

        sb.append("৫. সতর্কবার্তা ও ঘোষণা:\n");
        sb.append(FaraidDisclaimerHelper.WARNING_DISCLAIMER_BN).append("\n\n");
        sb.append(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_BN);

        return sb.toString();
    }

    public static String explainCalculationInEnglish(FaraidCalculationResult result) {
        if (result == null) {
            return "No calculation result found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Islamic Faraid Distribution Summary & Shariah Analysis:\n\n");

        sb.append("1. Estate Account:\n");
        sb.append("   • Gross Estate: ").append(result.getGrossEstateFormatted()).append("\n");
        sb.append("   • Funeral & Burial Expenses: ").append(result.getFuneralExpenseFormatted()).append("\n");
        sb.append("   • Debts & Liabilities Settled: ").append(result.getDebtAmountFormatted()).append("\n");
        sb.append("   • Valid Bequest (Max 1/3): ").append(result.getValidWasiyyahFormatted()).append("\n");
        sb.append("   • Net Distributable Estate: ").append(result.getNetDistributableEstateFormatted()).append("\n\n");

        if (result.hasBlockedHeirs()) {
            sb.append("2. Excluded Relatives:\n");
            for (BlockedHeirInfo bh : result.getBlockedHeirs()) {
                String heirTitle = FaraidAuditTrailEngine.getEnglishRelationTitle(bh.getHeirKey());
                sb.append("   • ").append(heirTitle).append(": ")
                        .append(bh.getLocalizedLegalReason(false)).append(" (Source: ").append(bh.getLocalizedShariahReference(false)).append(")\n");
            }
            sb.append("\n");
        }

        sb.append("3. Surviving Legal Heirs & Prescribed Shares:\n");
        for (HeirShareResult share : result.getHeirShares()) {
            String heirTitle = FaraidAuditTrailEngine.getEnglishRelationTitle(share.getRelationKey());
            sb.append("   • ").append(heirTitle)
                    .append(" (").append(share.getCount()).append(share.getCount() > 1 ? " persons)" : " person)")
                    .append(" (Share: ").append(share.getShareFractionLabel(false))
                    .append(" or ").append(String.format("%.2f", share.getPercentage())).append("%)\n")
                    .append("     Total Amount: ").append(share.getTotalCategoryAmountFormatted(false)).append("\n")
                    .append("     Legal Basis: ").append(share.getLegalReason(false)).append("\n")
                    .append("     Proof: ").append(share.getDalilReference(false)).append("\n");
        }
        sb.append("\n");

        if (result.isAwlApplied()) {
            sb.append("4. Awl (Proportional Reduction) Applied:\n");
            sb.append("   The sum of prescribed Quranic shares exceeded 1, so all shares were proportionately reduced by raising the base denominator from ")
                    .append(result.getBaseDenominator()).append(" to ").append(result.getAwlDenominator()).append(".\n\n");
        } else if (result.isRaddApplied()) {
            sb.append("4. Radd (Surplus Return) Applied:\n");
            sb.append("   Since no Asabah was present, the remaining surplus was redistributed among eligible Quranic heirs in proportion to their basic shares.\n\n");
        }

        sb.append("5. Disclaimer & Notice:\n");
        sb.append(FaraidDisclaimerHelper.WARNING_DISCLAIMER_EN).append("\n\n");
        sb.append(FaraidDisclaimerHelper.GENERAL_DISCLAIMER_EN);

        return sb.toString();
    }
}