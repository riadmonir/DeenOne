package com.devflux.deenone.core.quran;

public class BanglaQuranAyahItem {

    private int surahNumber;
    private int ayahNumber;
    private String textArabic;
    private String translationMuhiuddin;
    private String translationZohurul;
    private int juzNumber;
    private int pageNumber;
    private boolean isBookmarked;
    private boolean isFavorite;

    public BanglaQuranAyahItem(int surahNumber, int ayahNumber, String textArabic,
                               String translationMuhiuddin, String translationZohurul,
                               int juzNumber, int pageNumber) {
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.textArabic = textArabic != null ? textArabic : "";
        this.translationMuhiuddin = translationMuhiuddin != null ? translationMuhiuddin : "";
        this.translationZohurul = translationZohurul != null ? translationZohurul : "";
        this.juzNumber = juzNumber;
        this.pageNumber = pageNumber;
        this.isBookmarked = false;
        this.isFavorite = false;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public void setSurahNumber(int surahNumber) {
        this.surahNumber = surahNumber;
    }

    public int getAyahNumber() {
        return ayahNumber;
    }

    public void setAyahNumber(int ayahNumber) {
        this.ayahNumber = ayahNumber;
    }

    public String getTextArabic() {
        return textArabic;
    }

    public void setTextArabic(String textArabic) {
        this.textArabic = textArabic;
    }

    public String getTranslationMuhiuddin() {
        return translationMuhiuddin;
    }

    public void setTranslationMuhiuddin(String translationMuhiuddin) {
        this.translationMuhiuddin = translationMuhiuddin;
    }

    public String getTranslationZohurul() {
        return translationZohurul;
    }

    public void setTranslationZohurul(String translationZohurul) {
        this.translationZohurul = translationZohurul;
    }

    public String getTranslation(BanglaQuranTranslator translator) {
        if (translator == BanglaQuranTranslator.ZOHURUL_HOQUE) {
            if (translationZohurul != null && !translationZohurul.trim().isEmpty()) {
                return translationZohurul;
            }
            return translationMuhiuddin;
        }
        if (translationMuhiuddin != null && !translationMuhiuddin.trim().isEmpty()) {
            return translationMuhiuddin;
        }
        return translationZohurul;
    }

    public int getJuzNumber() {
        return juzNumber;
    }

    public void setJuzNumber(int juzNumber) {
        this.juzNumber = juzNumber;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
