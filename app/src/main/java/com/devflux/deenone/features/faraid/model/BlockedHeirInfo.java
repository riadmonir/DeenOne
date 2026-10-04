package com.devflux.deenone.features.faraid.model;

import java.io.Serializable;

/**
 * Model representing an heir who was legally blocked (Mahjoob)
 * according to Islamic Faraid rules, with the blocking cause and Shariah dalil.
 */
public class BlockedHeirInfo implements Serializable {

    private final String heirTitleBn;
    private final String heirKey;
    private final int count;
    private final String blockedByTitleBn;
    private final String legalReasonBn;
    private final String legalReasonEn;
    private final String shariahReference;

    public BlockedHeirInfo(String heirTitleBn, int count, String blockedByTitleBn,
                           String legalReasonBn, String shariahReference) {
        this(heirTitleBn, "", count, blockedByTitleBn, legalReasonBn, legalReasonBn, shariahReference);
    }

    public BlockedHeirInfo(String heirTitleBn, String heirKey, int count, String blockedByTitleBn,
                           String legalReasonBn, String shariahReference) {
        this(heirTitleBn, heirKey, count, blockedByTitleBn, legalReasonBn, legalReasonBn, shariahReference);
    }

    public BlockedHeirInfo(String heirTitleBn, String heirKey, int count, String blockedByTitleBn,
                           String legalReasonBn, String legalReasonEn, String shariahReference) {
        this.heirTitleBn = heirTitleBn;
        this.heirKey = heirKey;
        this.count = count;
        this.blockedByTitleBn = blockedByTitleBn;
        this.legalReasonBn = legalReasonBn;
        this.legalReasonEn = legalReasonEn != null ? legalReasonEn : legalReasonBn;
        this.shariahReference = shariahReference;
    }

    public String getHeirTitleBn() { return heirTitleBn; }
    public String getLocalizedHeirTitle(android.content.Context context) {
        if (context != null && com.devflux.deenone.core.localization.LocaleManager.isBengali(context)) {
            return heirTitleBn;
        }
        String key = getHeirKey();
        switch (key) {
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
            default: return heirTitleBn;
        }
    }
    public String getHeirKey() { return heirKey != null ? heirKey : ""; }
    public String getRelationKey() { return getHeirKey(); }
    public int getCount() { return count; }
    public String getBlockedByTitleBn() { return blockedByTitleBn; }
    public String getLegalReasonBn() { return legalReasonBn; }
    public String getLegalReasonEn() { return legalReasonEn; }
    public String getLocalizedLegalReason(boolean isBn) {
        return isBn ? legalReasonBn : (legalReasonEn != null && !legalReasonEn.isEmpty() ? legalReasonEn : legalReasonBn);
    }
    public String getShariahReference() { return shariahReference; }

    public String getLocalizedShariahReference(boolean isBn) {
        if (isBn || shariahReference == null) {
            return shariahReference != null ? shariahReference : "";
        }
        String ref = shariahReference;
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
}