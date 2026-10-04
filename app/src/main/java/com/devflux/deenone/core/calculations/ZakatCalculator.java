package com.devflux.deenone.core.calculations;

public class ZakatCalculator {

    // Nisab thresholds in grams
    public static final double GOLD_NISAB_GRAMS = 87.48; // 7.5 Tola
    public static final double SILVER_NISAB_GRAMS = 612.36; // 52.5 Tola

    // Default reference prices per gram in BDT (customizable in UI)
    public static final double DEFAULT_GOLD_PRICE_PER_GRAM = 11500.0;
    public static final double DEFAULT_SILVER_PRICE_PER_GRAM = 180.0;

    public static class ZakatResult {
        public double totalAssets;
        public double totalLiabilities;
        public double netZakatableAmount;
        public double silverNisabThreshold;
        public boolean isEligibleForZakat;
        public double zakatPayable;

        public ZakatResult(double totalAssets, double totalLiabilities, double netZakatableAmount,
                           double silverNisabThreshold, boolean isEligibleForZakat, double zakatPayable) {
            this.totalAssets = totalAssets;
            this.totalLiabilities = totalLiabilities;
            this.netZakatableAmount = netZakatableAmount;
            this.silverNisabThreshold = silverNisabThreshold;
            this.isEligibleForZakat = isEligibleForZakat;
            this.zakatPayable = zakatPayable;
        }
    }

    public static ZakatResult calculateZakat(
            double cashInHandBank,
            double goldValue,
            double silverValue,
            double businessStockValue,
            double investmentReceivables,
            double debtsAndLiabilities,
            double customSilverPricePerGram
    ) {
        double effectiveSilverPrice = customSilverPricePerGram > 0 ? customSilverPricePerGram : DEFAULT_SILVER_PRICE_PER_GRAM;
        double silverNisabThreshold = SILVER_NISAB_GRAMS * effectiveSilverPrice;

        double totalAssets = cashInHandBank + goldValue + silverValue + businessStockValue + investmentReceivables;
        double netZakatableAmount = Math.max(0, totalAssets - debtsAndLiabilities);

        boolean isEligible = netZakatableAmount >= silverNisabThreshold;
        double zakatPayable = isEligible ? netZakatableAmount * 0.025 : 0.0;

        return new ZakatResult(totalAssets, debtsAndLiabilities, netZakatableAmount, silverNisabThreshold, isEligible, zakatPayable);
    }
}
