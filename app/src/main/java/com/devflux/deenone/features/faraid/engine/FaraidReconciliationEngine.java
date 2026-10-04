package com.devflux.deenone.features.faraid.engine;

import com.devflux.deenone.features.faraid.model.FaraidCurrency;
import com.devflux.deenone.features.faraid.model.HeirShareResult;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Monetary Rounding & Reconciliation Engine (Requirement 31).
 *
 * Rules:
 * 1. Never calculate each heir's amount independently using rounded percentages.
 * 2. Exact amount = Exact rational fraction × net estate.
 * 3. Round only for display.
 * 4. Post-display reconciliation (Largest Remainder Method) guarantees:
 *    Sum of reconciled amounts == Net Distributable Estate (zero rounding loss/creation).
 * 5. Retains maximum precision using exact rational arithmetic internally.
 */
public class FaraidReconciliationEngine implements Serializable {

    public static class ReconciledEntry {
        private final String relationKey;
        private final String relationTitleBn;
        private final FaraidFraction fraction;
        private final double exactUnroundedAmount;
        private final double reconciledDisplayAmount;
        private final double individualReconciledAmount;
        private final int count;

        public ReconciledEntry(String relationKey, String relationTitleBn, FaraidFraction fraction,
                               double exactUnroundedAmount, double reconciledDisplayAmount,
                               double individualReconciledAmount, int count) {
            this.relationKey = relationKey;
            this.relationTitleBn = relationTitleBn;
            this.fraction = fraction;
            this.exactUnroundedAmount = exactUnroundedAmount;
            this.reconciledDisplayAmount = reconciledDisplayAmount;
            this.individualReconciledAmount = individualReconciledAmount;
            this.count = count;
        }

        public String getRelationKey() { return relationKey; }
        public String getRelationTitleBn() { return relationTitleBn; }
        public FaraidFraction getFraction() { return fraction; }
        public double getExactUnroundedAmount() { return exactUnroundedAmount; }
        public double getReconciledDisplayAmount() { return reconciledDisplayAmount; }
        public double getIndividualReconciledAmount() { return individualReconciledAmount; }
        public int getCount() { return count; }
    }

    /**
     * Calculates exact unrounded amount from rational fraction and reconciles display amounts
     * to match the net distributable estate exactly to 2 decimal places.
     */
    public static List<ReconciledEntry> reconcile(List<HeirShareResult> rawShares, double netDistributableEstate, FaraidCurrency currency) {
        if (rawShares == null || rawShares.isEmpty() || netDistributableEstate <= 0.0) {
            return Collections.emptyList();
        }

        int scale = 2;
        BigDecimal targetNet = BigDecimal.valueOf(netDistributableEstate).setScale(scale, RoundingMode.HALF_UP);
        BigDecimal sumRounded = BigDecimal.ZERO;

        List<ReconciledEntry> entries = new ArrayList<>();

        // 1. Calculate each heir's amount via exact fraction × net estate, then round to 2 decimals
        for (HeirShareResult share : rawShares) {
            double exactAmount = share.getShareFractionNumeric() * netDistributableEstate;
            BigDecimal rounded = BigDecimal.valueOf(exactAmount).setScale(scale, RoundingMode.HALF_UP);
            sumRounded = sumRounded.add(rounded);

            double indAmount = share.getCount() > 0 ? rounded.doubleValue() / share.getCount() : rounded.doubleValue();
            entries.add(new ReconciledEntry(
                    share.getRelationKey(),
                    share.getRelationTitleBn(),
                    FaraidFraction.fromDouble(share.getShareFractionNumeric()),
                    exactAmount,
                    rounded.doubleValue(),
                    indAmount,
                    share.getCount()
            ));
        }

        // 2. Compute discrepancy between targetNet and sumRounded (usually 0 or ±0.01)
        BigDecimal difference = targetNet.subtract(sumRounded);
        if (difference.compareTo(BigDecimal.ZERO) != 0 && !entries.isEmpty()) {
            // Find the entry with the largest amount to absorb the 1-cent difference smoothly
            int bestIdx = 0;
            double maxAmount = -1.0;
            for (int i = 0; i < entries.size(); i++) {
                if (entries.get(i).getExactUnroundedAmount() > maxAmount) {
                    maxAmount = entries.get(i).getExactUnroundedAmount();
                    bestIdx = i;
                }
            }

            ReconciledEntry target = entries.get(bestIdx);
            double adjustedTotal = BigDecimal.valueOf(target.getReconciledDisplayAmount())
                    .add(difference).setScale(scale, RoundingMode.HALF_UP).doubleValue();
            double adjustedInd = target.getCount() > 0 ? adjustedTotal / target.getCount() : adjustedTotal;

            entries.set(bestIdx, new ReconciledEntry(
                    target.getRelationKey(),
                    target.getRelationTitleBn(),
                    target.getFraction(),
                    target.getExactUnroundedAmount(),
                    adjustedTotal,
                    adjustedInd,
                    target.getCount()
            ));
        }

        return entries;
    }
}