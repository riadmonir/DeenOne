package com.devflux.deenone.features.salahguide.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;
import java.util.List;

/**
 * Model representing a book in the Salah Learning Books section (বই সমূহ).
 */
public class SalahBookModel implements Serializable {

    private final String id;
    private final String titleBn;
    private final String titleEn;
    private final String authorBn;
    private final String authorEn;
    @DrawableRes
    private final int coverDrawableRes;
    private final String pdfUrl;
    private final String summaryBn;
    private final String summaryEn;
    private final List<String> chapterTitles;
    private final List<String> chapterContents;

    public SalahBookModel(String id, String titleBn, String titleEn, String authorBn, String authorEn,
                          @DrawableRes int coverDrawableRes, String pdfUrl, String summaryBn, String summaryEn,
                          List<String> chapterTitles, List<String> chapterContents) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.authorBn = authorBn;
        this.authorEn = authorEn;
        this.coverDrawableRes = coverDrawableRes;
        this.pdfUrl = pdfUrl;
        this.summaryBn = summaryBn;
        this.summaryEn = summaryEn;
        this.chapterTitles = chapterTitles;
        this.chapterContents = chapterContents;
    }

    public String getId() {
        return id;
    }

    public String getTitle(boolean isBn) {
        return isBn ? titleBn : titleEn;
    }

    public String getAuthor(boolean isBn) {
        return isBn ? authorBn : authorEn;
    }

    public int getCoverDrawableRes() {
        return coverDrawableRes;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getSummary(boolean isBn) {
        return isBn ? summaryBn : summaryEn;
    }

    public List<String> getChapterTitles() {
        return chapterTitles;
    }

    public List<String> getChapterContents() {
        return chapterContents;
    }
}
