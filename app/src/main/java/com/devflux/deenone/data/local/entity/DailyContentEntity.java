package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_contents")
public class DailyContentEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String date; // YYYY-MM-DD
    private String ayahArabic;
    private String ayahBengali;
    private String ayahReference;
    private String hadithBengali;
    private String hadithReference;
    private String duaTitle;
    private String duaArabic;
    private String duaBengali;
    private String duaReference;
    private boolean isDuaRead;
    private String barakahFoodTitle;
    private String barakahFoodSubtitle;

    public DailyContentEntity(String date, String ayahArabic, String ayahBengali, String ayahReference,
                              String hadithBengali, String hadithReference, String duaTitle,
                              String duaArabic, String duaBengali, String duaReference,
                              boolean isDuaRead, String barakahFoodTitle, String barakahFoodSubtitle) {
        this.date = date;
        this.ayahArabic = ayahArabic;
        this.ayahBengali = ayahBengali;
        this.ayahReference = ayahReference;
        this.hadithBengali = hadithBengali;
        this.hadithReference = hadithReference;
        this.duaTitle = duaTitle;
        this.duaArabic = duaArabic;
        this.duaBengali = duaBengali;
        this.duaReference = duaReference;
        this.isDuaRead = isDuaRead;
        this.barakahFoodTitle = barakahFoodTitle;
        this.barakahFoodSubtitle = barakahFoodSubtitle;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getAyahArabic() { return ayahArabic; }
    public void setAyahArabic(String ayahArabic) { this.ayahArabic = ayahArabic; }

    public String getAyahBengali() { return ayahBengali; }
    public void setAyahBengali(String ayahBengali) { this.ayahBengali = ayahBengali; }

    public String getAyahReference() { return ayahReference; }
    public void setAyahReference(String ayahReference) { this.ayahReference = ayahReference; }

    public String getHadithBengali() { return hadithBengali; }
    public void setHadithBengali(String hadithBengali) { this.hadithBengali = hadithBengali; }

    public String getHadithReference() { return hadithReference; }
    public void setHadithReference(String hadithReference) { this.hadithReference = hadithReference; }

    public String getDuaTitle() { return duaTitle; }
    public void setDuaTitle(String duaTitle) { this.duaTitle = duaTitle; }

    public String getDuaArabic() { return duaArabic; }
    public void setDuaArabic(String duaArabic) { this.duaArabic = duaArabic; }

    public String getDuaBengali() { return duaBengali; }
    public void setDuaBengali(String duaBengali) { this.duaBengali = duaBengali; }

    public String getDuaReference() { return duaReference; }
    public void setDuaReference(String duaReference) { this.duaReference = duaReference; }

    public boolean isDuaRead() { return isDuaRead; }
    public void setDuaRead(boolean duaRead) { isDuaRead = duaRead; }

    public String getBarakahFoodTitle() { return barakahFoodTitle; }
    public void setBarakahFoodTitle(String barakahFoodTitle) { this.barakahFoodTitle = barakahFoodTitle; }

    public String getBarakahFoodSubtitle() { return barakahFoodSubtitle; }
    public void setBarakahFoodSubtitle(String barakahFoodSubtitle) { this.barakahFoodSubtitle = barakahFoodSubtitle; }
}
