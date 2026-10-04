package com.devflux.deenone.domain.model;

public class DailyContent {
    private final long id;
    private final String ayahBengali;
    private final String ayahReference;
    private final String hadithBengali;
    private final String hadithReference;
    private final String duaTitle;
    private final String duaArabic;
    private final String duaBengali;
    private final String duaReference;
    private final boolean isDuaRead;
    private final String barakahFoodTitle;
    private final String barakahFoodSubtitle;

    public DailyContent(long id, String ayahBengali, String ayahReference, String hadithBengali,
                        String hadithReference, String duaTitle, String duaArabic,
                        String duaBengali, String duaReference, boolean isDuaRead,
                        String barakahFoodTitle, String barakahFoodSubtitle) {
        this.id = id;
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
    public String getAyahBengali() { return ayahBengali; }
    public String getAyahReference() { return ayahReference; }
    public String getHadithBengali() { return hadithBengali; }
    public String getHadithReference() { return hadithReference; }
    public String getDuaTitle() { return duaTitle; }
    public String getDuaArabic() { return duaArabic; }
    public String getDuaBengali() { return duaBengali; }
    public String getDuaReference() { return duaReference; }
    public boolean isDuaRead() { return isDuaRead; }
    public String getBarakahFoodTitle() { return barakahFoodTitle; }
    public String getBarakahFoodSubtitle() { return barakahFoodSubtitle; }
}
