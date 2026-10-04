package com.devflux.deenone.data.model;

import java.io.Serializable;

public class MarriageTopicItem implements Serializable {
    private final int id;
    private final String chapterNumber;
    private final String titleBn;
    private final String titleEn;
    private final String categoryBn;
    private final String categoryEn;
    private final String summaryBn;
    private final String summaryEn;
    private final String contentBn;
    private final String contentEn;
    private final String arabicAyatOrHadith;
    private final String arabicPronunciation;
    private final String arabicMeaning;
    private final String references;
    private final int readTimeMinutes;
    private boolean isBookmarked;
    private final String badgeText;

    public MarriageTopicItem(int id,
                             String chapterNumber,
                             String titleBn,
                             String titleEn,
                             String categoryBn,
                             String categoryEn,
                             String summaryBn,
                             String summaryEn,
                             String contentBn,
                             String contentEn,
                             String arabicAyatOrHadith,
                             String arabicPronunciation,
                             String arabicMeaning,
                             String references,
                             int readTimeMinutes,
                             String badgeText) {
        this.id = id;
        this.chapterNumber = chapterNumber;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.categoryBn = categoryBn;
        this.categoryEn = categoryEn;
        this.summaryBn = summaryBn;
        this.summaryEn = summaryEn;
        this.contentBn = contentBn;
        this.contentEn = contentEn;
        this.arabicAyatOrHadith = arabicAyatOrHadith;
        this.arabicPronunciation = arabicPronunciation;
        this.arabicMeaning = arabicMeaning;
        this.references = references;
        this.readTimeMinutes = readTimeMinutes;
        this.badgeText = badgeText;
        this.isBookmarked = false;
    }

    public int getId() {
        return id;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getCategoryBn() {
        return categoryBn;
    }

    public String getCategoryEn() {
        return categoryEn;
    }

    public String getSummaryBn() {
        return summaryBn;
    }

    public String getSummaryEn() {
        return summaryEn;
    }

    public String getContentBn() {
        return contentBn;
    }

    public String getContentEn() {
        return contentEn;
    }

    public String getArabicAyatOrHadith() {
        return arabicAyatOrHadith;
    }

    public String getArabicPronunciation() {
        return arabicPronunciation;
    }

    public String getArabicMeaning() {
        return arabicMeaning;
    }

    public String getReferences() {
        return references;
    }

    public int getReadTimeMinutes() {
        return readTimeMinutes;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public String getBadgeText() {
        return badgeText;
    }

    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
