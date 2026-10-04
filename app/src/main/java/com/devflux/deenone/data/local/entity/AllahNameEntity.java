package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "allah_names")
public class AllahNameEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int serialNumber; // 1 to 99
    private String arabic; // যেমন: الرَّحْمَنُ
    private String pronunciation; // আর-রাহমান
    private String banglaMeaning; // পরম দয়ালু
    private String englishMeaning; // The Most Gracious
    private String fazilat; // এই নামের জিকিরের বিশেষ ফজিলত

    public AllahNameEntity(int serialNumber, String arabic, String pronunciation,
                           String banglaMeaning, String englishMeaning, String fazilat) {
        this.serialNumber = serialNumber;
        this.arabic = arabic;
        this.pronunciation = pronunciation;
        this.banglaMeaning = banglaMeaning;
        this.englishMeaning = englishMeaning;
        this.fazilat = fazilat;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public int getSerialNumber() { return serialNumber; }
    public void setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; }

    public String getArabic() { return arabic; }
    public void setArabic(String arabic) { this.arabic = arabic; }

    public String getPronunciation() { return pronunciation; }
    public void setPronunciation(String pronunciation) { this.pronunciation = pronunciation; }

    public String getBanglaMeaning() { return banglaMeaning; }
    public void setBanglaMeaning(String banglaMeaning) { this.banglaMeaning = banglaMeaning; }

    public String getEnglishMeaning() { return englishMeaning; }
    public void setEnglishMeaning(String englishMeaning) { this.englishMeaning = englishMeaning; }

    public String getFazilat() { return fazilat; }
    public void setFazilat(String fazilat) { this.fazilat = fazilat; }
}
