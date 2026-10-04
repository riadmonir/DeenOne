package com.devflux.deenone.features.ramadan.model;

public class RozaDuaItem {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String shortSummaryBn;
    private final String shortSummaryEn;
    private final String arabicText;
    private final String transliterationBn;
    private final String transliterationEn;
    private final String fullTranslationBn;
    private final String fullTranslationEn;
    private final String referenceBn;
    private final String referenceEn;
    private final String fazilatBn;
    private final String fazilatEn;
    private boolean isExpanded;

    public RozaDuaItem(int id, String titleBn, String titleEn,
                       String shortSummaryBn, String shortSummaryEn,
                       String arabicText,
                       String transliterationBn, String transliterationEn,
                       String fullTranslationBn, String fullTranslationEn,
                       String referenceBn, String referenceEn) {
        this(id, titleBn, titleEn, shortSummaryBn, shortSummaryEn, arabicText,
                transliterationBn, transliterationEn, fullTranslationBn, fullTranslationEn,
                referenceBn, referenceEn, "", "");
    }

    public RozaDuaItem(int id, String titleBn, String titleEn,
                       String shortSummaryBn, String shortSummaryEn,
                       String arabicText,
                       String transliterationBn, String transliterationEn,
                       String fullTranslationBn, String fullTranslationEn,
                       String referenceBn, String referenceEn,
                       String fazilatBn, String fazilatEn) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.shortSummaryBn = shortSummaryBn;
        this.shortSummaryEn = shortSummaryEn;
        this.arabicText = arabicText;
        this.transliterationBn = transliterationBn;
        this.transliterationEn = transliterationEn;
        this.fullTranslationBn = fullTranslationBn;
        this.fullTranslationEn = fullTranslationEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
        this.fazilatBn = fazilatBn != null ? fazilatBn : "";
        this.fazilatEn = fazilatEn != null ? fazilatEn : "";
        this.isExpanded = false;
    }

    public int getId() { return id; }
    public String getTitle(boolean isBn) { return isBn ? titleBn : titleEn; }
    public String getShortSummary(boolean isBn) { return isBn ? shortSummaryBn : shortSummaryEn; }
    public String getArabicText() { return arabicText; }
    public String getTransliteration(boolean isBn) { return isBn ? transliterationBn : transliterationEn; }
    public String getFullTranslation(boolean isBn) { return isBn ? fullTranslationBn : fullTranslationEn; }
    public String getReference(boolean isBn) { return isBn ? referenceBn : referenceEn; }
    public String getFazilat(boolean isBn) { return isBn ? fazilatBn : fazilatEn; }

    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
    public void toggleExpanded() { isExpanded = !isExpanded; }
}
