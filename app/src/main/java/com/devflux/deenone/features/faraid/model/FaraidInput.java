package com.devflux.deenone.features.faraid.model;

import java.io.Serializable;

/**
 * Complete Input Data Model for Islamic Faraid (Inheritance) Calculator.
 * Supports all Ashab al-Furud, Asabah, and Special Relatives.
 */
public class FaraidInput implements Serializable {

    public enum Gender {
        MALE, FEMALE
    }

    public enum Madhab {
        ALL, HANAFI, SHAFI, MALIKI, HANBALI
    }

    public enum CalculationMethod {
        HANAFI("হানাফি (ইসলামিক ফাউন্ডেশন মানদণ্ড)"),
        GENERAL_SUNNI("সার্বজনীন চার মাযহাব (ইজমা)"),
        BANGLADESH_1961_ACT("বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১");

        private final String displayNameBn;

        CalculationMethod(String displayNameBn) {
            this.displayNameBn = displayNameBn;
        }

        public String getDisplayNameBn() {
            return displayNameBn;
        }
    }

    // Estate Financial Details (অর্থ ও স্থাবর সম্পত্তি)
    private double totalEstate = 0.0;
    private double funeralExpense = 0.0;
    private double debtAmount = 0.0;
    private double wasiyyahAmount = 0.0;
    private boolean wasiyyahToHeir = false;
    private FaraidCurrency currency = FaraidCurrency.BDT;

    // Estate Property / Land Details (স্থাবর সম্পত্তি / জমি)
    private double totalProperty = 0.0;
    private String propertyUnit = "শতক";

    // Deceased Details
    private Gender deceasedGender = Gender.MALE;
    private CalculationMethod calculationMethod = CalculationMethod.HANAFI;
    private Madhab madhab = Madhab.HANAFI;
    private boolean applyBangladeshLaw1961 = false;

    // Primary Heirs (Ashab al-Furud & Primary Asabah)
    private int wifeCount = 0;
    private boolean husbandAlive = false;
    private boolean fatherAlive = false;
    private boolean motherAlive = false;
    private int sonCount = 0;
    private int daughterCount = 0;

    // Grandchildren
    private int grandsonCount = 0;
    private int granddaughterCount = 0;

    // Grandparents
    private boolean paternalGrandfatherAlive = false;
    private boolean paternalGrandmotherAlive = false;
    private boolean maternalGrandmotherAlive = false;

    // Siblings
    private int fullBrotherCount = 0;
    private int fullSisterCount = 0;
    private int consanguineBrotherCount = 0;
    private int consanguineSisterCount = 0;
    private int uterineBrotherCount = 0;
    private int uterineSisterCount = 0;

    // Extended Asabah
    private int fullNephewCount = 0;
    private int consanguineNephewCount = 0;
    private int fullPaternalUncleCount = 0;
    private int consanguinePaternalUncleCount = 0;
    private int fullCousinCount = 0;
    private int consanguineCousinCount = 0;

    public FaraidInput() {}

    // Getters and Setters
    public double getTotalEstate() { return totalEstate; }
    public void setTotalEstate(double totalEstate) { this.totalEstate = totalEstate; }

    public double getFuneralExpense() { return funeralExpense; }
    public void setFuneralExpense(double funeralExpense) { this.funeralExpense = funeralExpense; }

    public double getDebtAmount() { return debtAmount; }
    public void setDebtAmount(double debtAmount) { this.debtAmount = debtAmount; }

    public double getWasiyyahAmount() { return wasiyyahAmount; }
    public void setWasiyyahAmount(double wasiyyahAmount) { this.wasiyyahAmount = wasiyyahAmount; }

    public boolean isWasiyyahToHeir() { return wasiyyahToHeir; }
    public void setWasiyyahToHeir(boolean wasiyyahToHeir) { this.wasiyyahToHeir = wasiyyahToHeir; }

    public FaraidCurrency getCurrency() { return currency != null ? currency : FaraidCurrency.BDT; }
    public void setCurrency(FaraidCurrency currency) { this.currency = currency; }

    public Gender getDeceasedGender() { return deceasedGender; }
    public void setDeceasedGender(Gender deceasedGender) { this.deceasedGender = deceasedGender; }

    public CalculationMethod getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(CalculationMethod calculationMethod) { this.calculationMethod = calculationMethod; }

    public Madhab getMadhab() { return madhab != null ? madhab : Madhab.HANAFI; }
    public void setMadhab(Madhab madhab) { this.madhab = madhab; }

    public boolean isApplyBangladeshLaw1961() {
        return applyBangladeshLaw1961 || calculationMethod == CalculationMethod.BANGLADESH_1961_ACT;
    }
    public void setApplyBangladeshLaw1961(boolean applyBangladeshLaw1961) { this.applyBangladeshLaw1961 = applyBangladeshLaw1961; }

    public int getWifeCount() { return wifeCount; }
    public void setWifeCount(int wifeCount) { this.wifeCount = wifeCount; }

    public boolean isHusbandAlive() { return husbandAlive; }
    public void setHusbandAlive(boolean husbandAlive) { this.husbandAlive = husbandAlive; }

    public boolean isFatherAlive() { return fatherAlive; }
    public void setFatherAlive(boolean fatherAlive) { this.fatherAlive = fatherAlive; }

    public boolean isMotherAlive() { return motherAlive; }
    public void setMotherAlive(boolean motherAlive) { this.motherAlive = motherAlive; }

    public int getSonCount() { return sonCount; }
    public void setSonCount(int sonCount) { this.sonCount = sonCount; }

    public int getDaughterCount() { return daughterCount; }
    public void setDaughterCount(int daughterCount) { this.daughterCount = daughterCount; }

    public int getGrandsonCount() { return grandsonCount; }
    public void setGrandsonCount(int grandsonCount) { this.grandsonCount = grandsonCount; }

    public int getGranddaughterCount() { return granddaughterCount; }
    public void setGranddaughterCount(int granddaughterCount) { this.granddaughterCount = granddaughterCount; }

    public boolean isPaternalGrandfatherAlive() { return paternalGrandfatherAlive; }
    public void setPaternalGrandfatherAlive(boolean paternalGrandfatherAlive) { this.paternalGrandfatherAlive = paternalGrandfatherAlive; }

    public boolean isPaternalGrandmotherAlive() { return paternalGrandmotherAlive; }
    public void setPaternalGrandmotherAlive(boolean paternalGrandmotherAlive) { this.paternalGrandmotherAlive = paternalGrandmotherAlive; }

    public boolean isMaternalGrandmotherAlive() { return maternalGrandmotherAlive; }
    public void setMaternalGrandmotherAlive(boolean maternalGrandmotherAlive) { this.maternalGrandmotherAlive = maternalGrandmotherAlive; }

    public int getFullBrotherCount() { return fullBrotherCount; }
    public void setFullBrotherCount(int fullBrotherCount) { this.fullBrotherCount = fullBrotherCount; }

    public int getFullSisterCount() { return fullSisterCount; }
    public void setFullSisterCount(int fullSisterCount) { this.fullSisterCount = fullSisterCount; }

    public int getConsanguineBrotherCount() { return consanguineBrotherCount; }
    public void setConsanguineBrotherCount(int consanguineBrotherCount) { this.consanguineBrotherCount = consanguineBrotherCount; }

    public int getConsanguineSisterCount() { return consanguineSisterCount; }
    public void setConsanguineSisterCount(int consanguineSisterCount) { this.consanguineSisterCount = consanguineSisterCount; }

    public int getUterineBrotherCount() { return uterineBrotherCount; }
    public void setUterineBrotherCount(int uterineBrotherCount) { this.uterineBrotherCount = uterineBrotherCount; }

    public int getUterineSisterCount() { return uterineSisterCount; }
    public void setUterineSisterCount(int uterineSisterCount) { this.uterineSisterCount = uterineSisterCount; }

    public int getFullNephewCount() { return fullNephewCount; }
    public void setFullNephewCount(int fullNephewCount) { this.fullNephewCount = fullNephewCount; }

    public int getConsanguineNephewCount() { return consanguineNephewCount; }
    public void setConsanguineNephewCount(int consanguineNephewCount) { this.consanguineNephewCount = consanguineNephewCount; }

    public int getNephewCount() { return fullNephewCount + consanguineNephewCount; }
    public void setNephewCount(int count) { this.fullNephewCount = count; }

    public int getFullPaternalUncleCount() { return fullPaternalUncleCount; }
    public void setFullPaternalUncleCount(int fullPaternalUncleCount) { this.fullPaternalUncleCount = fullPaternalUncleCount; }

    public int getConsanguinePaternalUncleCount() { return consanguinePaternalUncleCount; }
    public void setConsanguinePaternalUncleCount(int consanguinePaternalUncleCount) { this.consanguinePaternalUncleCount = consanguinePaternalUncleCount; }

    public int getPaternalUncleCount() { return fullPaternalUncleCount + consanguinePaternalUncleCount; }
    public void setPaternalUncleCount(int count) { this.fullPaternalUncleCount = count; }

    public int getFullCousinCount() { return fullCousinCount; }
    public void setFullCousinCount(int fullCousinCount) { this.fullCousinCount = fullCousinCount; }

    public int getConsanguineCousinCount() { return consanguineCousinCount; }
    public void setConsanguineCousinCount(int consanguineCousinCount) { this.consanguineCousinCount = consanguineCousinCount; }

    public int getCousinCount() { return fullCousinCount + consanguineCousinCount; }

    // Convenient Aliases
    public double getGrossEstate() { return totalEstate; }
    public void setGrossEstate(double val) { this.totalEstate = val; }
    public void setFuneralExpenses(double val) { this.funeralExpense = val; }
    public void setDebts(double val) { this.debtAmount = val; }
    public void setWasiyyah(double val) { this.wasiyyahAmount = val; }
    public void setPaternalGrandfather(boolean val) { this.paternalGrandfatherAlive = val; }
    public void setPaternalGrandmother(boolean val) { this.paternalGrandmotherAlive = val; }
    public void setMaternalGrandmother(boolean val) { this.maternalGrandmotherAlive = val; }
    public void setFullBrothers(int val) { this.fullBrotherCount = val; }
    public void setFullSisters(int val) { this.fullSisterCount = val; }
    public void setPaternalBrothers(int val) { this.consanguineBrotherCount = val; }
    public void setPaternalSisters(int val) { this.consanguineSisterCount = val; }
    public void setMaternalBrothers(int val) { this.uterineBrotherCount = val; }
    public void setMaternalSisters(int val) { this.uterineSisterCount = val; }
    public void setSons(int val) { this.sonCount = val; }
    public void setDaughters(int val) { this.daughterCount = val; }
    public void setGrandsons(int val) { this.grandsonCount = val; }
    public void setGranddaughters(int val) { this.granddaughterCount = val; }
    public void setWives(int val) { this.wifeCount = val; }
    public void setHusband(boolean val) { this.husbandAlive = val; }
    public void setFather(boolean val) { this.fatherAlive = val; }
    public void setMother(boolean val) { this.motherAlive = val; }

    public double getTotalProperty() { return totalProperty; }
    public void setTotalProperty(double totalProperty) { this.totalProperty = totalProperty; }
    public String getPropertyUnit() { return propertyUnit != null && !propertyUnit.isEmpty() ? propertyUnit : "শতক"; }
    public void setPropertyUnit(String propertyUnit) { this.propertyUnit = propertyUnit; }
}