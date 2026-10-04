package com.devflux.deenone.features.faraid.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dedicated Radd Engine (Requirement 18).
 *
 * Implements classical Sunni Radd (الرد) surplus redistribution:
 * If: Sum(Fixed Shares) < 1 AND No Qualifying Residuary (Asabah) Heir exists:
 * - Return the surplus estate proportionally to eligible fixed-share blood heirs.
 * - Madhab Compliance: Spouses do NOT take Radd in the presence of other Quranic heirs (Hanafi/Hanbali consensus).
 * - Displays "Radd applied" and explains the exact redistribution mechanism.
 */
public class FaraidRaddEngine {

    public static class RaddItem {
        private final String heirKey;
        private final String heirTitleBn;
        private final int count;
        private final FaraidFraction originalFraction;
        private final FaraidFraction raddFinalFraction;
        private final double totalAmount;
        private final double individualAmount;
        private final String explanation;

        public RaddItem(String heirKey, String heirTitleBn, int count,
                        FaraidFraction originalFraction, FaraidFraction raddFinalFraction,
                        double totalAmount, double individualAmount, String explanation) {
            this.heirKey = heirKey;
            this.heirTitleBn = heirTitleBn;
            this.count = count;
            this.originalFraction = originalFraction;
            this.raddFinalFraction = raddFinalFraction;
            this.totalAmount = totalAmount;
            this.individualAmount = individualAmount;
            this.explanation = explanation;
        }

        public String getHeirKey() { return heirKey; }
        public String getHeirTitleBn() { return heirTitleBn; }
        public int getCount() { return count; }
        public FaraidFraction getOriginalFraction() { return originalFraction; }
        public FaraidFraction getRaddFinalFraction() { return raddFinalFraction; }
        public double getTotalAmount() { return totalAmount; }
        public double getIndividualAmount() { return individualAmount; }
        public String getExplanation() { return explanation; }
    }

    public static class RaddResult {
        private final boolean isRaddApplied;
        private final FaraidFraction surplusFraction;
        private final List<RaddItem> items;
        private final String redistributionExplanation;

        public RaddResult(boolean isRaddApplied, FaraidFraction surplusFraction,
                          List<RaddItem> items, String redistributionExplanation) {
            this.isRaddApplied = isRaddApplied;
            this.surplusFraction = surplusFraction;
            this.items = items;
            this.redistributionExplanation = redistributionExplanation;
        }

        public boolean isRaddApplied() { return isRaddApplied; }
        public FaraidFraction getSurplusFraction() { return surplusFraction; }
        public List<RaddItem> getItems() { return items; }
        public String getRedistributionExplanation() { return redistributionExplanation; }
    }

    public static RaddResult evaluateAndApplyRadd(List<FaraidCalculatorEngine.WorkingShare> fixedShares,
                                                  boolean hasQualifyingAsabah,
                                                  double netDistributableEstate) {
        if (hasQualifyingAsabah || fixedShares == null || fixedShares.isEmpty()) {
            return new RaddResult(false, FaraidFraction.ZERO, Collections.emptyList(), "রাদ্দ প্রযোজ্য নয় (আসাবা বিদ্যমান বা ওয়ারিশ অনুপস্থিত)।");
        }

        FaraidFraction sum = FaraidFraction.ZERO;
        for (FaraidCalculatorEngine.WorkingShare ws : fixedShares) {
            sum = sum.add(ws.fixedFraction);
        }

        if (sum.compareTo(FaraidFraction.ONE) >= 0 || sum.compareTo(FaraidFraction.ZERO) <= 0) {
            return new RaddResult(false, FaraidFraction.ZERO, Collections.emptyList(), "রাদ্দ প্রযোজ্য নয়।");
        }

        FaraidFraction surplus = FaraidFraction.ONE.subtract(sum);
        List<RaddItem> resultItems = new ArrayList<>();

        FaraidCalculatorEngine.WorkingShare spouseShare = null;
        FaraidFraction nonSpouseSum = FaraidFraction.ZERO;

        for (FaraidCalculatorEngine.WorkingShare ws : fixedShares) {
            if ("wife".equals(ws.id) || "husband".equals(ws.id)) {
                spouseShare = ws;
            } else {
                nonSpouseSum = nonSpouseSum.add(ws.fixedFraction);
            }
        }

        StringBuilder expBuilder = new StringBuilder();
        expBuilder.append("রাদ্দ প্রযোজ্য (Radd applied): কোনো আসাবা না থাকায় উদ্বৃত্ত ")
                  .append(surplus.toBengaliString())
                  .append(" অংশ কোরআনিক ওয়ারিশদের মাঝে ফিরিয়ে দেওয়া হয়েছে। ");

        if (spouseShare != null && nonSpouseSum.compareTo(FaraidFraction.ZERO) > 0) {
            // Spouse excluded from Radd; remainder given to blood heirs
            expBuilder.append("চার মাযহাবের সিদ্ধান্ত অনুযায়ী স্বামী/স্ত্রী রাদ্দের অংশ পান না; তাদের নির্ধারিত অংশ দেওয়ার পর অবশিষ্ট পূর্ণ অংশ রক্তসম্পর্কীয় অংশীদারদের মাঝে আনুপাতিক হারে বণ্টিত হয়েছে।\n");
            FaraidFraction remainderAfterSpouse = FaraidFraction.ONE.subtract(spouseShare.fixedFraction);

            for (FaraidCalculatorEngine.WorkingShare ws : fixedShares) {
                FaraidFraction finalFrac;
                String exp;
                if ("wife".equals(ws.id) || "husband".equals(ws.id)) {
                    finalFrac = ws.fixedFraction;
                    exp = ws.explanation;
                } else {
                    finalFrac = ws.fixedFraction.divide(nonSpouseSum).multiply(remainderAfterSpouse);
                    exp = ws.explanation + " [রাদ্দ নীতিতে অংশ বৃদ্ধি করা হয়েছে: " + finalFrac.toBengaliString() + "]";
                }

                double totalAmt = netDistributableEstate * finalFrac.toDouble();
                double indAmt = totalAmt / Math.max(1, ws.count);
                resultItems.add(new RaddItem(ws.id, ws.relationshipBn, ws.count, ws.fixedFraction, finalFrac, totalAmt, indAmt, exp));
            }
        } else {
            // No spouse or sole heir: full estate returned proportionally
            expBuilder.append("সম্পূর্ণ উদ্বৃত্ত অংশ বিদ্যমান কোরআনিক ওয়ারিশদের মাঝে আনুপাতিক হারে ফিরিয়ে দেওয়া হয়েছে।\n");
            for (FaraidCalculatorEngine.WorkingShare ws : fixedShares) {
                FaraidFraction scaled = ws.fixedFraction.divide(sum);
                double totalAmt = netDistributableEstate * scaled.toDouble();
                double indAmt = totalAmt / Math.max(1, ws.count);
                String exp = ws.explanation + " [রাদ্দ নীতিতে অংশ বৃদ্ধি করা হয়েছে: " + scaled.toBengaliString() + "]";
                resultItems.add(new RaddItem(ws.id, ws.relationshipBn, ws.count, ws.fixedFraction, scaled, totalAmt, indAmt, exp));
            }
        }

        return new RaddResult(true, surplus, resultItems, expBuilder.toString());
    }
}