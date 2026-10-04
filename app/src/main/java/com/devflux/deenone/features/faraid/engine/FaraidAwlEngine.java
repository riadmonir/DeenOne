package com.devflux.deenone.features.faraid.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dedicated Awl Engine (Requirement 17).
 *
 * Implements classical Sunni Awl (العول) normalization established by Umar ibn al-Khattab (r.a.) and the Sahaba:
 * When Sum(Fixed Shares) > 1:
 * - Proportional reduction across all Quranic heirs.
 * - Never discards an heir, never caps independently, never creates negative remainder.
 * - Displays "Awl applied" and tracks:
 *   1. Original share
 *   2. Adjusted share
 *   3. Adjusted amount
 *   4. Explanation
 */
public class FaraidAwlEngine {

    public static class AwlItem {
        private final String heirKey;
        private final String heirTitleBn;
        private final int count;
        private final FaraidFraction originalFraction;
        private final FaraidFraction adjustedFraction;
        private final double adjustedTotalAmount;
        private final double adjustedIndividualAmount;
        private final String explanation;

        public AwlItem(String heirKey, String heirTitleBn, int count,
                       FaraidFraction originalFraction, FaraidFraction adjustedFraction,
                       double adjustedTotalAmount, double adjustedIndividualAmount,
                       String explanation) {
            this.heirKey = heirKey;
            this.heirTitleBn = heirTitleBn;
            this.count = count;
            this.originalFraction = originalFraction;
            this.adjustedFraction = adjustedFraction;
            this.adjustedTotalAmount = adjustedTotalAmount;
            this.adjustedIndividualAmount = adjustedIndividualAmount;
            this.explanation = explanation;
        }

        public String getHeirKey() { return heirKey; }
        public String getHeirTitleBn() { return heirTitleBn; }
        public int getCount() { return count; }
        public FaraidFraction getOriginalFraction() { return originalFraction; }
        public FaraidFraction getAdjustedFraction() { return adjustedFraction; }
        public double getAdjustedTotalAmount() { return adjustedTotalAmount; }
        public double getAdjustedIndividualAmount() { return adjustedIndividualAmount; }
        public String getExplanation() { return explanation; }
    }

    public static class AwlResult {
        private final boolean isAwlApplied;
        private final long baseDenominator;
        private final long awlDenominator;
        private final FaraidFraction sumFixedShares;
        private final List<AwlItem> adjustedItems;
        private final String auditSummary;

        public AwlResult(boolean isAwlApplied, long baseDenominator, long awlDenominator,
                         FaraidFraction sumFixedShares, List<AwlItem> adjustedItems,
                         String auditSummary) {
            this.isAwlApplied = isAwlApplied;
            this.baseDenominator = baseDenominator;
            this.awlDenominator = awlDenominator;
            this.sumFixedShares = sumFixedShares;
            this.adjustedItems = adjustedItems;
            this.auditSummary = auditSummary;
        }

        public boolean isAwlApplied() { return isAwlApplied; }
        public long getBaseDenominator() { return baseDenominator; }
        public long getAwlDenominator() { return awlDenominator; }
        public FaraidFraction getSumFixedShares() { return sumFixedShares; }
        public List<AwlItem> getAdjustedItems() { return adjustedItems; }
        public String getAuditSummary() { return auditSummary; }
    }

    public static AwlResult evaluateAndApplyAwl(List<FaraidCalculatorEngine.WorkingShare> fixedShares, double netDistributableEstate) {
        FaraidFraction sum = FaraidFraction.ZERO;
        long baseDenom = 1;

        for (FaraidCalculatorEngine.WorkingShare ws : fixedShares) {
            if (ws.fixedFraction != null && ws.fixedFraction.compareTo(FaraidFraction.ZERO) > 0) {
                sum = sum.add(ws.fixedFraction);
                baseDenom = FaraidFraction.lcm(baseDenom, ws.fixedFraction.getDenominator());
            }
        }

        if (baseDenom < 1) baseDenom = 1;

        if (sum.compareTo(FaraidFraction.ONE) <= 0) {
            return new AwlResult(false, baseDenom, baseDenom, sum, Collections.emptyList(), "আওল প্রযোজ্য নয়।");
        }

        // Awl is applied
        long awlDenom = (baseDenom * sum.getNumerator()) / sum.getDenominator();
        List<AwlItem> items = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append("আওল সমন্বয় প্রযোজ্য (Awl applied): নির্ধারিত অংশের যোগফল (")
          .append(sum.toBengaliString())
          .append(") ১-এর বেশি হওয়ায় মূল মাসআলা ")
          .append(baseDenom)
          .append(" থেকে বৃদ্ধি পেয়ে ")
          .append(awlDenom)
          .append(" এ সমন্বিত করা হয়েছে। সকল অংশীদারের প্রাপ্য আনুপাতিক হারে পুনর্নির্ধারিত হয়েছে।\n");

        for (FaraidCalculatorEngine.WorkingShare ws : fixedShares) {
            FaraidFraction scaled = ws.fixedFraction.divide(sum);
            double totalAmt = netDistributableEstate * scaled.toDouble();
            double indAmt = totalAmt / Math.max(1, ws.count);

            String exp = ws.explanation + " [আওল নীতিতে মূল অংশ " + ws.fixedFraction.toBengaliString() + " থেকে সমন্বিত হয়ে " + scaled.toBengaliString() + " অংশ হয়েছে]";
            items.add(new AwlItem(ws.id, ws.relationshipBn, ws.count, ws.fixedFraction, scaled, totalAmt, indAmt, exp));
        }

        return new AwlResult(true, baseDenom, awlDenom, sum, items, sb.toString());
    }
}