package com.devflux.deenone.data.model;

public class AllahNameItem {
    private final int number;
    private final String nameArabic;
    private final String nameBengali;
    private final String nameEnglish;
    private final String meaningBengali;
    private final String meaningEnglish;
    private final String explanationBn;
    private final String fazilatBn;
    private final String audioUrl;
    private final String category;

    public AllahNameItem(int number,
                         String nameArabic,
                         String nameBengali,
                         String nameEnglish,
                         String meaningBengali,
                         String meaningEnglish,
                         String explanationBn,
                         String fazilatBn,
                         String audioUrl,
                         String category) {
        this.number = number;
        this.nameArabic = nameArabic;
        this.nameBengali = nameBengali;
        this.nameEnglish = nameEnglish;
        this.meaningBengali = meaningBengali;
        this.meaningEnglish = meaningEnglish;
        this.explanationBn = explanationBn;
        this.fazilatBn = fazilatBn;
        this.audioUrl = audioUrl;
        this.category = category != null ? category : "ALL";
    }

    public int getNumber() {
        return number;
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public String getNameBengali() {
        return nameBengali;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public String getMeaningBengali() {
        return meaningBengali;
    }

    public String getMeaningEnglish() {
        return meaningEnglish;
    }

    public String getExplanationBn() {
        return explanationBn;
    }

    public String getFazilatBn() {
        return fazilatBn;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getCategory() {
        return category;
    }
}
