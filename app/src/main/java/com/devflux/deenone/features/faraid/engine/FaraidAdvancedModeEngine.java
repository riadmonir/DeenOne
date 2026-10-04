package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.BlockedHeirInfo;
import com.devflux.deenone.features.faraid.model.FaraidCalculationResult;
import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.FaraidInput;
import com.devflux.deenone.features.faraid.model.HeirShareResult;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Advanced Faraid Mode Engine (Requirement 34).
 *
 * Provides dedicated juristic and mathematical diagnostics intended for
 * Islamic scholars, muftis, students of sacred law (Talibul Ilm), and advanced users:
 * - Ashab al-Furud (أصحاب الفروض)
 * - Asabah (العصبة: بالنفس، بالغير، مع الغير)
 * - Hajb (الحجب: حجب حرمان، حجب نقصان)
 * - Awl (العول) & Radd (الرد)
 * - Kalalah (الكلالة) status
 * - Original vs Adjusted Fractions
 * - Base Number (اصل المسألة - Asl al-Mas'alah) & Tashih
 * - Share Units (السهام - Sahm) breakdown
 * - Sequential Calculation Trace
 */
public class FaraidAdvancedModeEngine implements Serializable {

    public static class AdvancedSahmUnit implements Serializable {
        private final String heirTitleBn;
        private final String arabicTitle;
        private final String classificationBn;
        private final String originalFraction;
        private final String adjustedFraction;
        private final long sahmUnits;
        private final long totalDenominator;
        private final double percentage;
        private final double amount;
        private final String reference;

        public AdvancedSahmUnit(String heirTitleBn, String arabicTitle, String classificationBn,
                                 String originalFraction, String adjustedFraction, long sahmUnits,
                                 long totalDenominator, double percentage, double amount, String reference) {
            this.heirTitleBn = heirTitleBn;
            this.arabicTitle = arabicTitle;
            this.classificationBn = classificationBn;
            this.originalFraction = originalFraction;
            this.adjustedFraction = adjustedFraction;
            this.sahmUnits = sahmUnits;
            this.totalDenominator = totalDenominator;
            this.percentage = percentage;
            this.amount = amount;
            this.reference = reference;
        }

        public String getHeirTitleBn() { return heirTitleBn; }
        public String getArabicTitle() { return arabicTitle; }
        public String getClassificationBn() { return classificationBn; }
        public String getOriginalFraction() { return originalFraction; }
        public String getAdjustedFraction() { return adjustedFraction; }
        public long getSahmUnits() { return sahmUnits; }
        public long getTotalDenominator() { return totalDenominator; }
        public double getPercentage() { return percentage; }
        public double getAmount() { return amount; }
        public String getReference() { return reference; }
    }

    public static class AdvancedFaraidAnalysis implements Serializable {
        private final boolean isKalalah;
        private final String kalalahExplanationBn;
        private final long aslAlMasalah;
        private final long correctedDenominator;
        private final boolean isAwlApplied;
        private final long awlDenominator;
        private final boolean isRaddApplied;
        private final String awlRaddDiagnosticsBn;
        private final List<AdvancedSahmUnit> sahmUnits;
        private final List<BlockedHeirInfo> hajbHirmanList;
        private final String calculationTraceBn;

        public AdvancedFaraidAnalysis(boolean isKalalah, String kalalahExplanationBn,
                                      long aslAlMasalah, long correctedDenominator,
                                      boolean isAwlApplied, long awlDenominator,
                                      boolean isRaddApplied, String awlRaddDiagnosticsBn,
                                      List<AdvancedSahmUnit> sahmUnits,
                                      List<BlockedHeirInfo> hajbHirmanList,
                                      String calculationTraceBn) {
            this.isKalalah = isKalalah;
            this.kalalahExplanationBn = kalalahExplanationBn;
            this.aslAlMasalah = aslAlMasalah;
            this.correctedDenominator = correctedDenominator;
            this.isAwlApplied = isAwlApplied;
            this.awlDenominator = awlDenominator;
            this.isRaddApplied = isRaddApplied;
            this.awlRaddDiagnosticsBn = awlRaddDiagnosticsBn;
            this.sahmUnits = sahmUnits != null ? sahmUnits : Collections.emptyList();
            this.hajbHirmanList = hajbHirmanList != null ? hajbHirmanList : Collections.emptyList();
            this.calculationTraceBn = calculationTraceBn;
        }

        public boolean isKalalah() { return isKalalah; }
        public String getKalalahExplanationBn() { return kalalahExplanationBn; }
        public long getAslAlMasalah() { return aslAlMasalah; }
        public long getCorrectedDenominator() { return correctedDenominator; }
        public boolean isAwlApplied() { return isAwlApplied; }
        public long getAwlDenominator() { return awlDenominator; }
        public boolean isRaddApplied() { return isRaddApplied; }
        public String getAwlRaddDiagnosticsBn() { return awlRaddDiagnosticsBn; }
        public List<AdvancedSahmUnit> getSahmUnits() { return sahmUnits; }
        public List<BlockedHeirInfo> getHajbHirmanList() { return hajbHirmanList; }
        public String getCalculationTraceBn() { return calculationTraceBn; }
    }

    public static AdvancedFaraidAnalysis analyze(FaraidCalculationResult result) {
        if (result == null) {
            return new AdvancedFaraidAnalysis(false, "", 1, 1, false, 1, false, "", Collections.emptyList(), Collections.emptyList(), "");
        }

        FaraidInput input = result.getInput();
        boolean hasDescendants = (input != null && (input.getSonCount() > 0 || input.getDaughterCount() > 0 ||
                input.getGrandsonCount() > 0 || input.getGranddaughterCount() > 0));
        boolean hasAscendants = (input != null && (input.isFatherAlive() || input.isPaternalGrandfatherAlive()));
        boolean isKalalah = (!hasDescendants && !hasAscendants);
        String kalalahBn = isKalalah ?
                "মৃত ব্যক্তির কোনো পিতা বা দাদা (পুরুষ উর্ধ্বতন) এবং কোনো সন্তান বা নাতি-নাতনি (অধস্তন) জীবিত না থাকায় এই বণ্টনটি কোরআনিক কালালাহ (الكلالة) নীতির অন্তর্ভুক্ত (সূরা আন-নিসা: ১২ ও ১৭৬)।" :
                "মৃতের প্রত্যক্ষ সন্তান বা পিতা বিদ্যমান থাকায় এটি কালালাহ বণ্টন নয়।";

        long asl = result.getBaseDenominator() > 0 ? result.getBaseDenominator() : 1;
        long finalDen = result.isAwlApplied() ? result.getAwlDenominator() : (result.getCorrectedDenominator() > 0 ? result.getCorrectedDenominator() : asl);

        String awlRaddNote = "";
        if (result.isAwlApplied()) {
            awlRaddNote = "আওল (العول): নির্ধারিত অংশীদারদের হিস্যার সমষ্টি ১ অতিক্রম করায় মূল মাসআলা " +
                    BengaliNumberUtil.toBengali(String.valueOf(asl)) + " থেকে বাড়িয়ে " +
                    BengaliNumberUtil.toBengali(String.valueOf(result.getAwlDenominator())) +
                    " এ উন্নীত করে সকলের অংশ আনুপাতিক হারে হ্রাস করা হয়েছে।";
        } else if (result.isRaddApplied()) {
            awlRaddNote = "রাদ্দ (الرد): আসাবার অবর্তমানে নির্ধারিত অংশীদারদের দেওয়ার পর উদ্বৃত্ত সম্পত্তি কোরআনিক অংশীদারদের মাঝে তাদের মূল হিস্যার অনুপাতে পুনর্বণ্টন করা হয়েছে।";
        } else {
            awlRaddNote = "আদল (العدل): কোনো হ্রাস বা বৃদ্ধি ছাড়াই মূল মাসআলা " + BengaliNumberUtil.toBengali(String.valueOf(asl)) + " এর মধ্যে পূর্ণ বণ্টন সম্পন্ন হয়েছে।";
        }

        List<AdvancedSahmUnit> units = new ArrayList<>();
        for (HeirShareResult heir : result.getHeirShares()) {
            String origFrac = heir.getOriginalPrescribedShare() != null ? heir.getOriginalPrescribedShare() : heir.getShareFractionLabel();
            String adjFrac = heir.getShareFractionLabel();
            long sahm = Math.round(heir.getShareFractionNumeric() * finalDen);

            String classBn = (heir.getShareType() == HeirShareResult.ShareType.ASABAH) ? "আসাবা (العصبة)" : "আসহাবুল ফুরুজ (أصحاب الفروض)";
            String arTitle = getArabicHeirTitle(heir.getRelationKey());

            units.add(new AdvancedSahmUnit(
                    heir.getRelationTitleBn(),
                    arTitle,
                    classBn,
                    origFrac,
                    adjFrac,
                    sahm,
                    finalDen,
                    heir.getPercentage(),
                    heir.getTotalCategoryAmount(),
                    heir.getDalilReference()
            ));
        }

        String trace = FaraidAuditTrailEngine.formatFullAuditTrailBn(result);

        return new AdvancedFaraidAnalysis(
                isKalalah,
                kalalahBn,
                asl,
                finalDen,
                result.isAwlApplied(),
                result.getAwlDenominator(),
                result.isRaddApplied(),
                awlRaddNote,
                units,
                result.getBlockedHeirs(),
                trace
        );
    }

    private static String getArabicHeirTitle(String key) {
        if (key == null) return "";
        switch (key.toLowerCase()) {
            case "husband": return "الزوج (Az-Zawj)";
            case "wife": return "الزوجة (Az-Zawjah)";
            case "father": return "الأب (Al-Ab)";
            case "mother": return "الأم (Al-Umm)";
            case "son": return "الابن (Al-Ibn)";
            case "daughter": return "البنت (Al-Bint)";
            case "grandson": return "ابن الابن (Ibn al-Ibn)";
            case "granddaughter": return "بنت الابن (Bint al-Ibn)";
            case "full_brother": return "الأخ الشقيق (Al-Akh ash-Shaqiq)";
            case "full_sister": return "الأخت الشقيقة (Al-Ukht ash-Shaqiqah)";
            case "consanguine_brother": return "الأخ لأب (Al-Akh li-Ab)";
            case "consanguine_sister": return "الأخت لأب (Al-Ukht li-Ab)";
            case "uterine_brother": return "الأخ لأم (Al-Akh li-Umm)";
            case "uterine_sister": return "الأخت لأم (Al-Ukht li-Umm)";
            case "paternal_grandfather": return "الجد الصحيح (Al-Jadd as-Sahih)";
            case "paternal_grandmother": return "الجدة لأب (Al-Jaddah li-Ab)";
            case "maternal_grandmother": return "الجدة لأم (Al-Jaddah li-Umm)";
            case "nephew": return "ابن الأخ الشقيق (Ibn al-Akh)";
            case "paternal_uncle": return "العم الشقيق (Al-Amm ash-Shaqiq)";
            default: return "";
        }
    }
}