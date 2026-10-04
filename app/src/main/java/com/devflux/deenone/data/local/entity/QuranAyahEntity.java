package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;

@Entity(
        tableName = "quran_ayahs",
        primaryKeys = {"surahNumber", "ayahNumber"},
        indices = {@Index(value = {"surahNumber", "ayahNumber"}, unique = true)}
)
public class QuranAyahEntity {

    private int surahNumber; // 1 - 114
    private int ayahNumber; // 1 - 286
    private String textArabic; // بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
    private String translationBengali; // পরম করুণাময় অসীম দয়ালু আল্লাহর নামে
    private String translationEnglish; // In the name of Allah, the Entirely Merciful, the Especially Merciful.
    private String transliterationBengali; // বিসমিল্লাহির রাহমানির রাহিম
    private String audioUrl; // Default CDN url
    private int juzNumber;
    private int pageNumber;
    private boolean isBookmarked;
    private boolean isFavorite;

    public QuranAyahEntity(int surahNumber, int ayahNumber, String textArabic,
                           String translationBengali, String translationEnglish,
                           String transliterationBengali, String audioUrl, int juzNumber, int pageNumber) {
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.textArabic = textArabic;
        this.translationBengali = translationBengali;
        this.translationEnglish = translationEnglish;
        this.transliterationBengali = transliterationBengali;
        this.audioUrl = audioUrl;
        this.juzNumber = juzNumber;
        this.pageNumber = pageNumber;
        this.isBookmarked = false;
        this.isFavorite = false;
    }

    public int getSurahNumber() { return surahNumber; }
    public void setSurahNumber(int surahNumber) { this.surahNumber = surahNumber; }

    public int getAyahNumber() { return ayahNumber; }
    public void setAyahNumber(int ayahNumber) { this.ayahNumber = ayahNumber; }

    public String getTextArabic() { return textArabic; }
    public void setTextArabic(String textArabic) { this.textArabic = textArabic; }

    public String getTranslationBengali() { return translationBengali; }
    public void setTranslationBengali(String translationBengali) { this.translationBengali = translationBengali; }

    public String getTranslationEnglish() { return translationEnglish; }
    public void setTranslationEnglish(String translationEnglish) { this.translationEnglish = translationEnglish; }

    public String getTransliterationBengali() { return transliterationBengali; }
    public void setTransliterationBengali(String transliterationBengali) { this.transliterationBengali = transliterationBengali; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public int getJuzNumber() { return juzNumber; }
    public void setJuzNumber(int juzNumber) { this.juzNumber = juzNumber; }

    public int getPageNumber() { return pageNumber; }
    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public boolean isBookmarked() { return isBookmarked; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
