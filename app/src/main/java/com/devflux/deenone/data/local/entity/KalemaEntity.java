package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "kalemas")
public class KalemaEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int orderNumber; // 1 to 6
    private String nameBengali; // যেমন: কালেমা তাইয়্যেবা
    private String nameEnglish; // e.g. Kalima Tayyibah
    private String arabic;
    private String transliteration;
    private String bengaliMeaning;
    private String fazilat;

    public KalemaEntity(int orderNumber, String nameBengali, String nameEnglish, String arabic,
                        String transliteration, String bengaliMeaning, String fazilat) {
        this.orderNumber = orderNumber;
        this.nameBengali = nameBengali;
        this.nameEnglish = nameEnglish;
        this.arabic = arabic;
        this.transliteration = transliteration;
        this.bengaliMeaning = bengaliMeaning;
        this.fazilat = fazilat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getOrderNumber() { return orderNumber; }
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    public String getNameBengali() { return nameBengali; }
    public void setNameBengali(String nameBengali) { this.nameBengali = nameBengali; }

    public String getNameEnglish() { return nameEnglish; }
    public void setNameEnglish(String nameEnglish) { this.nameEnglish = nameEnglish; }

    public String getArabic() { return arabic; }
    public void setArabic(String arabic) { this.arabic = arabic; }

    public String getTransliteration() { return transliteration; }
    public void setTransliteration(String transliteration) { this.transliteration = transliteration; }

    public String getBengaliMeaning() { return bengaliMeaning; }
    public void setBengaliMeaning(String bengaliMeaning) { this.bengaliMeaning = bengaliMeaning; }

    public String getFazilat() { return fazilat; }
    public void setFazilat(String fazilat) { this.fazilat = fazilat; }
}
