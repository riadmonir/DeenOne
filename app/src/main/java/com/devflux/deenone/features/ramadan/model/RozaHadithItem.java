package com.devflux.deenone.features.ramadan.model;

/**
 * Model class for Roza Hadith Card.
 * 100% compliant with the verbatim hadith text and dual-language mode.
 */
public class RozaHadithItem {

    private final int id;
    private final int hadithNumber;
    private final String hadithNumberBn;
    private final String hadithNumberEn;
    private final String arabicText;
    private final String banglaTranslation;
    private final String englishTranslation;
    private boolean isFavorite;

    public RozaHadithItem(
            int id,
            int hadithNumber,
            String hadithNumberBn,
            String hadithNumberEn,
            String arabicText,
            String banglaTranslation,
            String englishTranslation,
            boolean isFavorite
    ) {
        this.id = id;
        this.hadithNumber = hadithNumber;
        this.hadithNumberBn = hadithNumberBn;
        this.hadithNumberEn = hadithNumberEn;
        this.arabicText = arabicText;
        this.banglaTranslation = banglaTranslation;
        this.englishTranslation = englishTranslation;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public int getHadithNumber() {
        return hadithNumber;
    }

    public String getHadithNumberBn() {
        return hadithNumberBn;
    }

    public String getHadithNumberEn() {
        return hadithNumberEn;
    }

    public String getBadgeText(boolean isBn) {
        return isBn ? ("হাদিস- " + hadithNumberBn) : ("Hadith- " + hadithNumberEn);
    }

    public String getArabicText() {
        return arabicText;
    }

    public String getBanglaTranslation() {
        return banglaTranslation;
    }

    public String getEnglishTranslation() {
        return englishTranslation;
    }

    public String getTranslation(boolean isBn) {
        if (isBn) {
            return (banglaTranslation != null && !banglaTranslation.isEmpty()) ? banglaTranslation : englishTranslation;
        } else {
            return (englishTranslation != null && !englishTranslation.isEmpty()) ? englishTranslation : banglaTranslation;
        }
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
