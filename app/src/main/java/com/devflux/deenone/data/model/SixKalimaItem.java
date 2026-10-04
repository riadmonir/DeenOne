package com.devflux.deenone.data.model;

public class SixKalimaItem {
    private final int number;
    private final String titleBangla;
    private final String titleEnglish;
    private final String nameArabic;
    private final String arabicText;
    private final String pronunciationBn;
    private final String meaningBn;
    private final String pronunciationEn;
    private final String meaningEn;
    private final String shariahSignificance;
    private final String audioUrl;

    public SixKalimaItem(int number,
                          String titleBangla,
                          String titleEnglish,
                          String nameArabic,
                          String arabicText,
                          String pronunciationBn,
                          String meaningBn,
                          String pronunciationEn,
                          String meaningEn,
                          String shariahSignificance,
                          String audioUrl) {
        this.number = number;
        this.titleBangla = titleBangla;
        this.titleEnglish = titleEnglish;
        this.nameArabic = nameArabic;
        this.arabicText = arabicText;
        this.pronunciationBn = pronunciationBn;
        this.meaningBn = meaningBn;
        this.pronunciationEn = pronunciationEn;
        this.meaningEn = meaningEn;
        this.shariahSignificance = shariahSignificance;
        this.audioUrl = audioUrl;
    }

    public int getNumber() {
        return number;
    }

    public String getTitleBangla() {
        return titleBangla;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public String getArabicText() {
        return arabicText;
    }

    public String getPronunciationBn() {
        return pronunciationBn;
    }

    public String getMeaningBn() {
        return meaningBn;
    }

    public String getPronunciationEn() {
        return pronunciationEn;
    }

    public String getMeaningEn() {
        return meaningEn;
    }

    public String getShariahSignificance() {
        return shariahSignificance;
    }

    public String getAudioUrl() {
        return audioUrl;
    }
}
