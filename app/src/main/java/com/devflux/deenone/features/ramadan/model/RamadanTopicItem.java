package com.devflux.deenone.features.ramadan.model;

public class RamadanTopicItem {
    private final int id;
    private final String tabTitle;
    private final String title;
    private final String categoryBadge;
    private final String summary;
    private final String detailedInstructions;
    private final String allowedOrProhibited;
    private final String arabicDua;
    private final String transliteration;
    private final String translation;
    private final String authenticReference;

    public RamadanTopicItem(int id, String tabTitle, String title, String categoryBadge,
                            String summary, String detailedInstructions, String allowedOrProhibited,
                            String arabicDua, String transliteration, String translation,
                            String authenticReference) {
        this.id = id;
        this.tabTitle = tabTitle;
        this.title = title;
        this.categoryBadge = categoryBadge;
        this.summary = summary;
        this.detailedInstructions = detailedInstructions;
        this.allowedOrProhibited = allowedOrProhibited;
        this.arabicDua = arabicDua;
        this.transliteration = transliteration;
        this.translation = translation;
        this.authenticReference = authenticReference;
    }

    public int getId() { return id; }
    public String getTabTitle() { return tabTitle; }
    public String getTitle() { return title; }
    public String getCategoryBadge() { return categoryBadge; }
    public String getSummary() { return summary; }
    public String getDetailedInstructions() { return detailedInstructions; }
    public String getAllowedOrProhibited() { return allowedOrProhibited; }
    public String getArabicDua() { return arabicDua; }
    public String getTransliteration() { return transliteration; }
    public String getTranslation() { return translation; }
    public String getAuthenticReference() { return authenticReference; }
}
