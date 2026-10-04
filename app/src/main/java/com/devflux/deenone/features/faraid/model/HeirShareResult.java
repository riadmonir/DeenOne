package com.devflux.deenone.features.faraid.model;

import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Result representation for each qualifying heir in the Faraid calculation.
 */
public class HeirShareResult implements Serializable {

    public enum ShareType {
        FIXED_QURANIC,
        QURANIC_FIXED,
        ASABAH,
        FIXED_AND_ASABAH,
        RADD,
        AWL_REDUCED
    }

    private final String relationTitleBn;
    private final String relationKey;
    private final int count;
    private final ShareType shareType;
    private final String shareFractionLabel;
    private final double shareFractionNumeric;
    private final double individualAmount;
    private final double totalCategoryAmount;
    private final double percentage;

    private final String originalPrescribedShare;
    private final String calculationFormulaBn;
    private final String calculationFormulaEn;
    private final String netEstateFormatted;
    private final String legalReasonBn;
    private final String legalReasonEn;

    private final String arabicDalil;
    private final String dalilPronunciation;
    private final String dalilTranslation;
    private String dalilTranslationEn;
    private final String dalilReference;
    private final String madhabConsensusNote;
    private final String bangladeshLawNote;
    private final String explanation;

    public HeirShareResult(String relationTitleBn, String relationKey, int count,
                           ShareType shareType, String shareFractionLabel, double shareFractionNumeric,
                           double individualAmount, double totalCategoryAmount, double percentage,
                           String originalPrescribedShare, String calculationFormulaBn, String calculationFormulaEn,
                           String netEstateFormatted, String legalReasonBn, String legalReasonEn,
                           String arabicDalil, String dalilPronunciation, String dalilTranslation,
                           String dalilReference, String madhabConsensusNote, String bangladeshLawNote,
                           String explanation) {
        this.relationTitleBn = relationTitleBn;
        this.relationKey = relationKey;
        this.count = count;
        this.shareType = shareType;
        this.shareFractionLabel = shareFractionLabel;
        this.shareFractionNumeric = shareFractionNumeric;
        this.individualAmount = individualAmount;
        this.totalCategoryAmount = totalCategoryAmount;
        this.percentage = percentage;
        this.originalPrescribedShare = originalPrescribedShare;
        this.calculationFormulaBn = calculationFormulaBn;
        this.calculationFormulaEn = calculationFormulaEn;
        this.netEstateFormatted = netEstateFormatted;
        this.legalReasonBn = legalReasonBn;
        this.legalReasonEn = legalReasonEn;
        this.arabicDalil = arabicDalil;
        this.dalilPronunciation = dalilPronunciation;
        this.dalilTranslation = dalilTranslation;
        this.dalilReference = dalilReference;
        this.madhabConsensusNote = madhabConsensusNote;
        this.bangladeshLawNote = bangladeshLawNote;
        this.explanation = explanation;
    }

    public HeirShareResult(String relationTitleBn, String relationKey, int count,
                           ShareType shareType, String shareFractionLabel, double shareFractionNumeric,
                           double individualAmount, double totalCategoryAmount, double percentage,
                           String arabicDalil, String dalilPronunciation, String dalilTranslation,
                           String dalilReference, String madhabConsensusNote, String bangladeshLawNote,
                           String explanation) {
        this(relationTitleBn, relationKey, count, shareType, shareFractionLabel, shareFractionNumeric,
                individualAmount, totalCategoryAmount, percentage,
                shareFractionLabel, shareFractionLabel + " × ত্যাজ্যবিত্ত", shareFractionLabel + " × Estate",
                "", explanation, explanation,
                arabicDalil, dalilPronunciation, dalilTranslation, dalilReference, madhabConsensusNote,
                bangladeshLawNote, explanation);
    }

    public String getRelationTitleBn() { return relationTitleBn; }
    public String getLocalizedRelationTitle(android.content.Context context) {
        if (context != null && com.devflux.deenone.core.localization.LocaleManager.isBengali(context)) {
            return relationTitleBn;
        }
        String key = relationKey != null ? relationKey : "";
        switch (key) {
            case "wife": return "Wife";
            case "husband": return "Husband";
            case "father": return "Father";
            case "mother": return "Mother";
            case "son": return "Son";
            case "daughter": return "Daughter";
            case "paternal_grandfather": return "Paternal Grandfather";
            case "paternal_grandmother": return "Paternal Grandmother";
            case "maternal_grandmother": return "Maternal Grandmother";
            case "grandson": return "Grandson";
            case "granddaughter": return "Granddaughter";
            case "full_brother": return "Full Brother";
            case "full_sister": return "Full Sister";
            case "consanguine_brother": return "Consanguine Brother";
            case "consanguine_sister": return "Consanguine Sister";
            case "uterine_brother": return "Uterine Brother";
            case "uterine_sister": return "Uterine Sister";
            case "nephew": return "Nephew";
            case "uncle": return "Uncle";
            case "cousin": return "Cousin";
            case "grandmother": return "Grandmother";
            default: return relationTitleBn;
        }
    }
    public String getHeirTitleBn() { return relationTitleBn; }
    public String getRelationKey() { return relationKey; }
    public int getCount() { return count; }
    public ShareType getShareType() { return shareType; }
    public String getShareFractionLabel() { return shareFractionLabel; }
    public String getShareFractionLabel(boolean isBn) {
        if (isBn) {
            return shareFractionLabel;
        }
        String eng = BengaliNumberUtil.toEnglish(shareFractionLabel);
        return eng.replace("অংশ", "share").replace("অবশিষ্ট", "Residue").trim();
    }
    public String getShareFractionStr() { return shareFractionLabel; }
    public double getShareFractionNumeric() { return shareFractionNumeric; }
    public double getIndividualAmount() { return individualAmount; }
    public double getTotalCategoryAmount() { return totalCategoryAmount; }
    public double getPercentage() { return percentage; }

    public String getOriginalPrescribedShare() { return originalPrescribedShare; }
    public String getOriginalPrescribedShare(boolean isBn) {
        if (isBn || originalPrescribedShare == null) return originalPrescribedShare;
        return BengaliNumberUtil.toEnglish(originalPrescribedShare).replace("অংশ", "share").replace("অবশিষ্ট", "Residue");
    }
    public String getCalculationFormulaBn() { return calculationFormulaBn; }
    public String getCalculationFormulaEn() { return calculationFormulaEn; }
    public String getCalculationFormula(boolean isBn) {
        if (isBn) return calculationFormulaBn;
        return (calculationFormulaEn != null && !calculationFormulaEn.isEmpty()) ? calculationFormulaEn : calculationFormulaBn;
    }
    public String getNetEstateFormatted() { return netEstateFormatted; }
    public String getLegalReasonBn() { return legalReasonBn; }
    public String getLegalReasonEn() { return legalReasonEn; }
    public String getLegalReason(boolean isBn) {
        if (isBn) return legalReasonBn;
        return (legalReasonEn != null && !legalReasonEn.isEmpty()) ? legalReasonEn : legalReasonBn;
    }

    public String getArabicDalil() { return arabicDalil; }
    public String getDalilPronunciation() { return dalilPronunciation; }
    private String dalilPronunciationEn;
    public String getDalilPronunciationEn() { return dalilPronunciationEn; }
    public void setDalilPronunciationEn(String val) { this.dalilPronunciationEn = val; }
    public String getDalilPronunciation(boolean isBn) {
        if (isBn) return dalilPronunciation;
        return (dalilPronunciationEn != null && !dalilPronunciationEn.isEmpty()) ? dalilPronunciationEn : dalilPronunciation;
    }

    public String getDalilTranslation() { return dalilTranslation; }
    public String getDalilTranslationEn() { return dalilTranslationEn; }
    public void setDalilTranslationEn(String dalilTranslationEn) { this.dalilTranslationEn = dalilTranslationEn; }
    public String getDalilTranslation(boolean isBn) {
        if (isBn) return dalilTranslation;
        return (dalilTranslationEn != null && !dalilTranslationEn.isEmpty()) ? dalilTranslationEn : dalilTranslation;
    }

    public String getDalilReference() { return dalilReference; }
    private String dalilReferenceEn;
    public String getDalilReferenceEn() { return dalilReferenceEn; }
    public void setDalilReferenceEn(String val) { this.dalilReferenceEn = val; }
    public String getDalilReference(boolean isBn) {
        if (isBn) return dalilReference;
        if (dalilReferenceEn != null && !dalilReferenceEn.isEmpty()) return dalilReferenceEn;
        if (dalilReference == null) return "";
        String ref = dalilReference;
        ref = ref.replace("সূরা আন-নিসা: ১১", "Surah An-Nisa: 11")
                 .replace("সূরা আন-নিসা: ১২", "Surah An-Nisa: 12")
                 .replace("সূরা আন-নিসা: ১৭৬", "Surah An-Nisa: 176")
                 .replace("সূরা আন-নিসা", "Surah An-Nisa")
                 .replace("সহীহ বুখারী: ৬৭৩২", "Sahih al-Bukhari: 6732")
                 .replace("সহীহ বুখারী: ৬৭৩৬", "Sahih al-Bukhari: 6736")
                 .replace("সহীহ বুখারী: ৬৭৪২", "Sahih al-Bukhari: 6742")
                 .replace("সহীহ বুখারী", "Sahih al-Bukhari")
                 .replace("সহীহ মুসলিম: ১৬১৫", "Sahih Muslim: 1615")
                 .replace("সহীহ মুসলিম", "Sahih Muslim")
                 .replace("সুনান আবু দাউদ: ২৮৯৪", "Sunan Abi Dawud: 2894")
                 .replace("সুনান আবু দাউদ", "Sunan Abi Dawud")
                 .replace("আল-হিদায়াহ ও হানাফি ফিকহ", "Al-Hidayah & Hanafi Fiqh")
                 .replace("বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ ধারা ৪", "Muslim Family Laws Ordinance 1961 Section 4")
                 .replace("ও ইজমা", "& Consensus")
                 .replace("ইজমা", "Consensus");
        return com.devflux.deenone.utils.BengaliNumberUtil.toEnglish(ref);
    }

    public String getMadhabConsensusNote() { return madhabConsensusNote; }
    private String madhabConsensusNoteEn;
    public String getMadhabConsensusNoteEn() { return madhabConsensusNoteEn; }
    public void setMadhabConsensusNoteEn(String val) { this.madhabConsensusNoteEn = val; }
    public String getMadhabConsensusNote(boolean isBn) {
        if (isBn) return madhabConsensusNote;
        if (madhabConsensusNoteEn != null && !madhabConsensusNoteEn.isEmpty()) return madhabConsensusNoteEn;
        if (madhabConsensusNote == null) return "";
        String note = madhabConsensusNote;
        note = note.replace("চার মাযহাবের সর্বসম্মত ফতোয়া", "Consensus of the Four Madhabs")
                   .replace("হানাফি মাযহাব", "Hanafi Madhhab")
                   .replace("শাফেয়ী মাযহাব", "Shafi'i Madhhab")
                   .replace("মালিকী মাযহাব", "Maliki Madhhab")
                   .replace("হাম্বলী মাযহাব", "Hanbali Madhhab")
                   .replace("সর্বসম্মত ঐক্যমত্য (ইজমা)", "Unanimous Consensus")
                   .replace("ইজমা", "Consensus");
        return note;
    }

    public String getBangladeshLawNote() { return bangladeshLawNote; }
    private String bangladeshLawNoteEn;
    public String getBangladeshLawNoteEn() { return bangladeshLawNoteEn; }
    public void setBangladeshLawNoteEn(String val) { this.bangladeshLawNoteEn = val; }
    public String getBangladeshLawNote(boolean isBn) {
        if (isBn) return bangladeshLawNote;
        if (bangladeshLawNoteEn != null && !bangladeshLawNoteEn.isEmpty()) return bangladeshLawNoteEn;
        if (bangladeshLawNote == null) return "";
        String note = bangladeshLawNote;
        note = note.replace("বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১ (ধারা ৪)", "Muslim Family Laws Ordinance 1961 Section 4")
                   .replace("বাংলাদেশ মুসলিম পারিবারিক আইন ১৯৬১", "Muslim Family Laws Ordinance 1961")
                   .replace("ধারা ৪", "Section 4");
        return note;
    }

    public String getExplanation() { return explanation; }

    public String getTotalCategoryAmountFormatted() {
        return getTotalCategoryAmountFormatted(true);
    }

    public String getTotalCategoryAmountFormatted(boolean isBn) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formatted = df.format(totalCategoryAmount);
        return isBn ? ("৳ " + BengaliNumberUtil.toBengali(formatted)) : ("BDT " + formatted);
    }

    public String getIndividualAmountFormatted() {
        return getIndividualAmountFormatted(true);
    }

    public String getIndividualAmountFormatted(boolean isBn) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formatted = df.format(individualAmount);
        return isBn ? ("৳ " + BengaliNumberUtil.toBengali(formatted)) : ("BDT " + formatted);
    }

    // -------------------------------------------------------------------------
    // LAND / REAL ESTATE PROPERTY (স্থাবর সম্পত্তি / জমি বণ্টন)
    // -------------------------------------------------------------------------
    private double totalPropertyAmount = 0.0;
    private double individualPropertyAmount = 0.0;
    private String propertyUnit = "শতক";

    public double getTotalPropertyAmount() { return totalPropertyAmount; }
    public void setTotalPropertyAmount(double totalPropertyAmount) { this.totalPropertyAmount = totalPropertyAmount; }

    public double getIndividualPropertyAmount() { return individualPropertyAmount; }
    public void setIndividualPropertyAmount(double individualPropertyAmount) { this.individualPropertyAmount = individualPropertyAmount; }

    public String getPropertyUnit() { return propertyUnit != null ? propertyUnit : "শতক"; }
    public void setPropertyUnit(String propertyUnit) { this.propertyUnit = propertyUnit; }

    public String getLocalizedPropertyUnit(boolean isBn) {
        String u = getPropertyUnit();
        if (isBn) return u;
        if (u.contains("শতক") || u.contains("ডেসিমাল")) return "Decimal";
        if (u.contains("কাঠা")) return "Katha";
        if (u.contains("বিঘা")) return "Bigha";
        if (u.contains("একর")) return "Acre";
        if (u.contains("বর্গফুট")) return "Sq Ft";
        return u;
    }

    public String getTotalPropertyFormatted() {
        return getTotalPropertyFormatted(true);
    }

    public String getTotalPropertyFormatted(boolean isBn) {
        String unit = getLocalizedPropertyUnit(isBn);
        if (totalPropertyAmount <= 0) return (isBn ? "০ " : "0 ") + unit;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formatted = df.format(totalPropertyAmount);
        return (isBn ? BengaliNumberUtil.toBengali(formatted) : formatted) + " " + unit;
    }

    public String getIndividualPropertyFormatted() {
        return getIndividualPropertyFormatted(true);
    }

    public String getIndividualPropertyFormatted(boolean isBn) {
        String unit = getLocalizedPropertyUnit(isBn);
        if (individualPropertyAmount <= 0) return (isBn ? "০ " : "0 ") + unit;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String formatted = df.format(individualPropertyAmount);
        return (isBn ? BengaliNumberUtil.toBengali(formatted) : formatted) + " " + unit;
    }
}