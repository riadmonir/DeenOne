package com.devflux.deenone.features.hajj.model;

public class HajjStageItem {

    private int stageNumber;
    private String tabTitle;
    private String heading;
    private String dateAndTiming;
    private String instructions;
    private String whatToDo;
    private String whatNotToDo;
    private String prayersAndRules;
    private String rulingCategory; // রুকন (ফরজ), ওয়াজিব, সুন্নাত
    private String arabicDua;
    private String transliteration;
    private String translation;
    private String authenticReference;

    public HajjStageItem(int stageNumber, String tabTitle, String heading, String dateAndTiming,
                         String instructions, String whatToDo, String whatNotToDo,
                         String prayersAndRules, String rulingCategory, String arabicDua,
                         String transliteration, String translation, String authenticReference) {
        this.stageNumber = stageNumber;
        this.tabTitle = tabTitle;
        this.heading = heading;
        this.dateAndTiming = dateAndTiming;
        this.instructions = instructions;
        this.whatToDo = whatToDo;
        this.whatNotToDo = whatNotToDo;
        this.prayersAndRules = prayersAndRules;
        this.rulingCategory = rulingCategory;
        this.arabicDua = arabicDua;
        this.transliteration = transliteration;
        this.translation = translation;
        this.authenticReference = authenticReference;
    }

    public int getStageNumber() { return stageNumber; }
    public String getTabTitle() { return tabTitle; }
    public String getHeading() { return heading; }
    public String getDateAndTiming() { return dateAndTiming; }
    public String getInstructions() { return instructions; }
    public String getWhatToDo() { return whatToDo; }
    public String getWhatNotToDo() { return whatNotToDo; }
    public String getPrayersAndRules() { return prayersAndRules; }
    public String getRulingCategory() { return rulingCategory; }
    public String getArabicDua() { return arabicDua; }
    public String getTransliteration() { return transliteration; }
    public String getTranslation() { return translation; }
    public String getAuthenticReference() { return authenticReference; }
}
