package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quran_surahs")
public class QuranSurahEntity {

    @PrimaryKey
    private int number; // 1 - 114

    private String nameArabic; // الفاتحة
    private String nameBengali; // আল-ফাতিহা
    private String nameEnglish; // Al-Fatihah
    private String meaningBengali; // সূচনা
    private String meaningEnglish; // The Opening
    private int numberOfAyahs; // 7
    private String revelationType; // মাক্কী / মাদানী
    private int juzNumber; // 1
    private int readingProgressAyah; // 1 - 7
    private boolean isFavorite;
    private long lastReadTimestamp;

    public QuranSurahEntity(int number, String nameArabic, String nameBengali, String nameEnglish,
                            String meaningBengali, String meaningEnglish, int numberOfAyahs,
                            String revelationType, int juzNumber) {
        this.number = number;
        this.nameArabic = nameArabic;
        this.nameBengali = nameBengali;
        this.nameEnglish = nameEnglish;
        this.meaningBengali = meaningBengali;
        this.meaningEnglish = meaningEnglish;
        this.numberOfAyahs = numberOfAyahs;
        this.revelationType = revelationType;
        this.juzNumber = juzNumber;
        this.readingProgressAyah = 0;
        this.isFavorite = false;
        this.lastReadTimestamp = 0;
    }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public String getNameArabic() { return nameArabic; }
    public void setNameArabic(String nameArabic) { this.nameArabic = nameArabic; }

    public String getNameBengali() { return nameBengali; }
    public void setNameBengali(String nameBengali) { this.nameBengali = nameBengali; }

    public String getNameEnglish() { return nameEnglish; }
    public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }

    public String getMeaningBengali() { return meaningBengali; }
    public void setMeaningBengali(String meaningBengali) { this.meaningBengali = meaningBengali; }

    public String getMeaningEnglish() { return meaningEnglish; }
    public void setMeaningEnglish(String meaningEnglish) { this.meaningEnglish = meaningEnglish; }

    public int getNumberOfAyahs() { return numberOfAyahs; }
    public void setNumberOfAyahs(int numberOfAyahs) { this.numberOfAyahs = numberOfAyahs; }

    public String getRevelationType() { return revelationType; }
    public void setRevelationType(String revelationType) { this.revelationType = revelationType; }

    public String getRevelationTypeBengali() {
        return "Meccan".equalsIgnoreCase(revelationType) ? "মাক্কী" : "মাদানী";
    }

    public String getRevelationTypeEnglish() {
        return "Meccan".equalsIgnoreCase(revelationType) ? "Makki" : "Madani";
    }

    public int getJuzNumber() { return juzNumber; }
    public void setJuzNumber(int juzNumber) { this.juzNumber = juzNumber; }

    public int getReadingProgressAyah() { return readingProgressAyah; }
    public void setReadingProgressAyah(int readingProgressAyah) { this.readingProgressAyah = readingProgressAyah; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public long getLastReadTimestamp() { return lastReadTimestamp; }
    public void setLastReadTimestamp(long lastReadTimestamp) { this.lastReadTimestamp = lastReadTimestamp; }
}
