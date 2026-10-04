package com.devflux.deenone.core.quran;

public class QuranWordItem {
    private final int surahNumber;
    private final int ayahNumber;
    private final int position;
    private final String textArabic;
    private final String meaningBengali;
    private final String meaningEnglish;

    public QuranWordItem(int surahNumber, int ayahNumber, int position, String textArabic, String meaningBengali, String meaningEnglish) {
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.position = position;
        this.textArabic = textArabic;
        this.meaningBengali = meaningBengali;
        this.meaningEnglish = meaningEnglish;
    }

    public int getSurahNumber() {
        return surahNumber;
    }

    public int getAyahNumber() {
        return ayahNumber;
    }

    public int getPosition() {
        return position;
    }

    public String getTextArabic() {
        return textArabic;
    }

    public String getMeaningBengali() {
        return meaningBengali;
    }

    public String getMeaningEnglish() {
        return meaningEnglish;
    }

    public String getMeaning(boolean isBengali) {
        if (isBengali) {
            return (meaningBengali != null && !meaningBengali.trim().isEmpty()) ? meaningBengali : meaningEnglish;
        } else {
            return (meaningEnglish != null && !meaningEnglish.trim().isEmpty()) ? meaningEnglish : meaningBengali;
        }
    }
}
