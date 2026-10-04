package com.devflux.deenone.features.faraid.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates full breakdown of classical Sunni Islamic inheritance distribution,
 * estate values (Gross, Funeral, Debts, Valid Wasiyyah, Net Estate), base denominator (Asl),
 * corrected denominator (Tashih), Awl/Radd/Umariyyatan, Hajb blocking,
 * formal classified heir model list (FaraidHeir), and Wasiyyah validation flags.
 */
public class FaraidCalculationResult implements Serializable {

    private final FaraidInput input;
    private final double grossEstate;
    private final double funeralExpense;
    private final double debtAmount;
    private final double enteredWasiyyah;
    private final double validWasiyyah;
    private final double totalDeductions;
    private final double netDistributableEstate;
    private final boolean isWasiyyahExceedingOneThird;
    private final boolean isWasiyyahToHeir;
    private final String wasiyyahValidationNotice;

    private final List<FaraidHeir> allHeirs;
    private final List<HeirShareResult> heirShares;
    private final List<BlockedHeirInfo> blockedHeirs;
    private final long baseDenominator;      // اصل المسألة (Asl al-Mas'alah)
    private final long correctedDenominator; // تصحيح المسألة (Tashih)
    private final boolean isAwlApplied;
    private final long awlDenominator;
    private final boolean isRaddApplied;
    private final boolean isUmariyyatan;
    private final boolean hasMadhabDifference;
    private final String madhabDifferenceNote;
    private final String statusMessageBn;
    private final String stepByStepAuditLog;
    private final String overallQuranicDalilSummary;
    private final FaraidCurrency currency;

    public FaraidCalculationResult(FaraidInput input, double grossEstate,
                                   double funeralExpense, double debtAmount,
                                   double enteredWasiyyah, double validWasiyyah,
                                   double totalDeductions, double netDistributableEstate,
                                   boolean isWasiyyahExceedingOneThird, boolean isWasiyyahToHeir,
                                   String wasiyyahValidationNotice,
                                   List<FaraidHeir> allHeirs,
                                   List<HeirShareResult> heirShares,
                                   List<BlockedHeirInfo> blockedHeirs, long baseDenominator,
                                   long correctedDenominator, boolean isAwlApplied, long awlDenominator,
                                   boolean isRaddApplied, boolean isUmariyyatan,
                                   boolean hasMadhabDifference, String madhabDifferenceNote,
                                   String statusMessageBn, String stepByStepAuditLog,
                                   String overallQuranicDalilSummary) {
        this(input, grossEstate, funeralExpense, debtAmount, enteredWasiyyah, validWasiyyah,
                totalDeductions, netDistributableEstate, isWasiyyahExceedingOneThird, isWasiyyahToHeir,
                wasiyyahValidationNotice, allHeirs, heirShares, blockedHeirs, baseDenominator,
                correctedDenominator, isAwlApplied, awlDenominator, isRaddApplied, isUmariyyatan,
                hasMadhabDifference, madhabDifferenceNote, statusMessageBn, stepByStepAuditLog,
                overallQuranicDalilSummary, (input != null && input.getCurrency() != null) ? input.getCurrency() : FaraidCurrency.BDT);
    }

    public FaraidCalculationResult(FaraidInput input, double grossEstate,
                                   double funeralExpense, double debtAmount,
                                   double enteredWasiyyah, double validWasiyyah,
                                   double totalDeductions, double netDistributableEstate,
                                   boolean isWasiyyahExceedingOneThird, boolean isWasiyyahToHeir,
                                   String wasiyyahValidationNotice,
                                   List<FaraidHeir> allHeirs,
                                   List<HeirShareResult> heirShares,
                                   List<BlockedHeirInfo> blockedHeirs, long baseDenominator,
                                   long correctedDenominator, boolean isAwlApplied, long awlDenominator,
                                   boolean isRaddApplied, boolean isUmariyyatan,
                                   boolean hasMadhabDifference, String madhabDifferenceNote,
                                   String statusMessageBn, String stepByStepAuditLog,
                                   String overallQuranicDalilSummary, FaraidCurrency currency) {
        this.input = input;
        this.grossEstate = grossEstate;
        this.funeralExpense = funeralExpense;
        this.debtAmount = debtAmount;
        this.enteredWasiyyah = enteredWasiyyah;
        this.validWasiyyah = validWasiyyah;
        this.totalDeductions = totalDeductions;
        this.netDistributableEstate = netDistributableEstate;
        this.isWasiyyahExceedingOneThird = isWasiyyahExceedingOneThird;
        this.isWasiyyahToHeir = isWasiyyahToHeir;
        this.wasiyyahValidationNotice = wasiyyahValidationNotice;
        this.allHeirs = (allHeirs != null) ? allHeirs : Collections.emptyList();
        this.heirShares = (heirShares != null) ? heirShares : Collections.emptyList();
        this.blockedHeirs = (blockedHeirs != null) ? blockedHeirs : Collections.emptyList();
        this.baseDenominator = baseDenominator;
        this.correctedDenominator = correctedDenominator;
        this.isAwlApplied = isAwlApplied;
        this.awlDenominator = awlDenominator;
        this.isRaddApplied = isRaddApplied;
        this.isUmariyyatan = isUmariyyatan;
        this.hasMadhabDifference = hasMadhabDifference;
        this.madhabDifferenceNote = madhabDifferenceNote;
        this.statusMessageBn = statusMessageBn;
        this.stepByStepAuditLog = stepByStepAuditLog;
        this.overallQuranicDalilSummary = overallQuranicDalilSummary;
        this.currency = (currency != null) ? currency : (input != null && input.getCurrency() != null ? input.getCurrency() : FaraidCurrency.BDT);
    }

    public FaraidInput getInput() { return input; }
    public double getGrossEstate() { return grossEstate; }
    public double getFuneralExpense() { return funeralExpense; }
    public double getDebtAmount() { return debtAmount; }
    public double getEnteredWasiyyah() { return enteredWasiyyah; }
    public double getValidWasiyyah() { return validWasiyyah; }
    public double getTotalDeductions() { return totalDeductions; }
    public double getNetDistributableEstate() { return netDistributableEstate; }
    public boolean isWasiyyahExceedingOneThird() { return isWasiyyahExceedingOneThird; }
    public boolean isWasiyyahExceedingLimit() { return isWasiyyahExceedingOneThird; }
    public boolean isWasiyyahToHeir() { return isWasiyyahToHeir; }
    public String getWasiyyahValidationNotice() { return wasiyyahValidationNotice; }
    public List<FaraidHeir> getAllHeirs() { return allHeirs; }
    public List<HeirShareResult> getHeirShares() { return heirShares; }
    public List<BlockedHeirInfo> getBlockedHeirs() { return blockedHeirs; }
    public boolean hasBlockedHeirs() { return blockedHeirs != null && !blockedHeirs.isEmpty(); }
    public long getBaseDenominator() { return baseDenominator; }
    public long getCorrectedDenominator() { return correctedDenominator; }
    public boolean isAwlApplied() { return isAwlApplied; }
    public long getAwlDenominator() { return awlDenominator; }
    public boolean isRaddApplied() { return isRaddApplied; }
    public boolean isUmariyyatan() { return isUmariyyatan; }
    public boolean hasMadhabDifference() { return hasMadhabDifference; }
    public String getMadhabDifferenceNote() { return madhabDifferenceNote; }
    private String madhabDifferenceNoteEn;
    public void setMadhabDifferenceNoteEn(String val) { this.madhabDifferenceNoteEn = val; }
    public String getMadhabDifferenceNote(boolean isBn) {
        if (isBn) return madhabDifferenceNote;
        if (madhabDifferenceNoteEn != null && !madhabDifferenceNoteEn.isEmpty()) return madhabDifferenceNoteEn;
        if (madhabDifferenceNote == null) return "";
        return madhabDifferenceNote;
    }

    public String getStatusMessageBn() { return statusMessageBn; }
    public String getStepByStepAuditLog() { return stepByStepAuditLog; }
    public String getStepByStepAuditLog(boolean isBn) {
        return isBn ? com.devflux.deenone.features.faraid.engine.FaraidAuditTrailEngine.formatFullAuditTrailBn(this)
                    : com.devflux.deenone.features.faraid.engine.FaraidAuditTrailEngine.formatFullAuditTrailEn(this);
    }

    public String getOverallQuranicDalilSummary() { return overallQuranicDalilSummary; }
    private String overallQuranicDalilSummaryEn;
    public void setOverallQuranicDalilSummaryEn(String val) { this.overallQuranicDalilSummaryEn = val; }
    public String getOverallQuranicDalilSummary(boolean isBn) {
        if (isBn) return overallQuranicDalilSummary;
        if (overallQuranicDalilSummaryEn != null && !overallQuranicDalilSummaryEn.isEmpty()) return overallQuranicDalilSummaryEn;
        return "This Faraid distribution is calculated strictly based on Surah An-Nisa verses 11, 12, and 176 of the Holy Quran, authentic Hadiths from Sahih al-Bukhari and Sahih Muslim, the rulings of the four Sunni Madhabs, and Muslim Family Laws Ordinance 1961.";
    }

    public FaraidCurrency getCurrency() {
        return currency != null ? currency : FaraidCurrency.BDT;
    }

    public String getGrossEstateFormatted() {
        return getCurrency().format(grossEstate, true);
    }

    public String getFuneralExpenseFormatted() {
        return getCurrency().format(funeralExpense, true);
    }

    public String getDebtAmountFormatted() {
        return getCurrency().format(debtAmount, true);
    }

    public String getValidWasiyyahFormatted() {
        return getCurrency().format(validWasiyyah, true);
    }

    public String getNetDistributableEstateFormatted() {
        return getCurrency().format(netDistributableEstate, true);
    }

    public HeirShareResult getShareFor(String relationKey) {
        if (relationKey == null || heirShares == null) return null;
        for (HeirShareResult share : heirShares) {
            if (relationKey.equalsIgnoreCase(share.getRelationKey())) {
                return share;
            }
        }
        return null;
    }

    private double grossProperty = 0.0;
    private double netDistributableProperty = 0.0;
    private String propertyUnit = "শতক";

    public double getGrossProperty() { return grossProperty; }
    public void setGrossProperty(double grossProperty) { this.grossProperty = grossProperty; }

    public double getNetDistributableProperty() { return netDistributableProperty; }
    public void setNetDistributableProperty(double netDistributableProperty) { this.netDistributableProperty = netDistributableProperty; }

    public String getPropertyUnit() { return propertyUnit != null ? propertyUnit : "শতক"; }
    public void setPropertyUnit(String propertyUnit) { this.propertyUnit = propertyUnit; }
}
