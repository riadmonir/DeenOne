package com.devflux.deenone.widget;

public class HomeScreenWidgetItem {
    public final String id;
    public final String titleBengali;
    public final String titleEnglish;
    public final String descriptionBn;
    public final String descriptionEn;
    public final String sizeBadgeBn;
    public final String sizeBadgeEn;
    public final String category; // all, salat, saom, content, tools
    public final int layoutResId;
    public final Class<?> providerClass;

    public HomeScreenWidgetItem(String id,
                                String titleBengali, String titleEnglish,
                                String descriptionBn, String descriptionEn,
                                String sizeBadgeBn, String sizeBadgeEn,
                                String category, int layoutResId, Class<?> providerClass) {
        this.id = id;
        this.titleBengali = titleBengali;
        this.titleEnglish = titleEnglish;
        this.descriptionBn = descriptionBn;
        this.descriptionEn = descriptionEn;
        this.sizeBadgeBn = sizeBadgeBn;
        this.sizeBadgeEn = sizeBadgeEn;
        this.category = category;
        this.layoutResId = layoutResId;
        this.providerClass = providerClass;
    }

    public String getTitle(boolean isBn) {
        return isBn ? titleBengali : titleEnglish;
    }

    public String getDescription(boolean isBn) {
        return isBn ? descriptionBn : descriptionEn;
    }

    public String getSizeBadge(boolean isBn) {
        return isBn ? sizeBadgeBn : sizeBadgeEn;
    }
}
