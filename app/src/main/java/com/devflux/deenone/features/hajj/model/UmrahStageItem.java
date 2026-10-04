package com.devflux.deenone.features.hajj.model;

public class UmrahStageItem {
    private final int stageNumber;
    private final String tabTitle;
    private final String stageTitle;
    private final String description;
    private final String obligatoryInfo;
    private final String sunnahInfo;
    private final String duaArabic;
    private final String duaTransliteration;
    private final String duaMeaning;
    private final String duaReference;
    private final String rulingsAndProhibitions;

    public UmrahStageItem(int stageNumber, String tabTitle, String stageTitle, String description,
                          String obligatoryInfo, String sunnahInfo, String duaArabic,
                          String duaTransliteration, String duaMeaning, String duaReference,
                          String rulingsAndProhibitions) {
        this.stageNumber = stageNumber;
        this.tabTitle = tabTitle;
        this.stageTitle = stageTitle;
        this.description = description;
        this.obligatoryInfo = obligatoryInfo;
        this.sunnahInfo = sunnahInfo;
        this.duaArabic = duaArabic;
        this.duaTransliteration = duaTransliteration;
        this.duaMeaning = duaMeaning;
        this.duaReference = duaReference;
        this.rulingsAndProhibitions = rulingsAndProhibitions;
    }

    public int getStageNumber() { return stageNumber; }
    public String getTabTitle() { return tabTitle; }
    public String getStageTitle() { return stageTitle; }
    public String getDescription() { return description; }
    public String getObligatoryInfo() { return obligatoryInfo; }
    public String getSunnahInfo() { return sunnahInfo; }
    public String getDuaArabic() { return duaArabic; }
    public String getDuaTransliteration() { return duaTransliteration; }
    public String getDuaMeaning() { return duaMeaning; }
    public String getDuaReference() { return duaReference; }
    public String getRulingsAndProhibitions() { return rulingsAndProhibitions; }
}
