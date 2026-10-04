package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "hadith_chapters_table",
    indices = {@Index(value = {"bookSlug", "chapterNumber"}, unique = true)}
)
public class HadithChapterEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String bookSlug;          // e.g. "bukhari", "muslim"
    private int chapterNumber;        // 1, 2, 3...
    private String chapterNumberBn;   // "১", "২", "৩"...
    private String titleBn;           // "ওহীর সূচনা অধ্যায়", "ঈমান", "ইলম"...
    private String titleEn;           // "Revelation", "Belief", "Knowledge"...
    private String hadithRange;       // "1 - 7", "8 - 58"...
    private String hadithRangeBn;     // "১ - ৭", "৮ - ৫৮"...
    private int startHadith;          // 1
    private int endHadith;            // 7
    private int totalHadith;          // 7
    private int displayOrder;         // 1, 2, 3...
    private boolean isActive;

    public HadithChapterEntity() {
        this.isActive = true;
    }

    @Ignore
    public HadithChapterEntity(String bookSlug, int chapterNumber, String chapterNumberBn, 
                               String titleBn, String titleEn, String hadithRange, 
                               String hadithRangeBn, int startHadith, int endHadith, 
                               int totalHadith, int displayOrder, boolean isActive) {
        this.bookSlug = bookSlug;
        this.chapterNumber = chapterNumber;
        this.chapterNumberBn = chapterNumberBn;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.hadithRange = hadithRange;
        this.hadithRangeBn = hadithRangeBn;
        this.startHadith = startHadith;
        this.endHadith = endHadith;
        this.totalHadith = totalHadith;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookSlug() {
        return bookSlug;
    }

    public void setBookSlug(String bookSlug) {
        this.bookSlug = bookSlug;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterNumberBn() {
        return chapterNumberBn != null && !chapterNumberBn.isEmpty() ? chapterNumberBn : String.valueOf(chapterNumber);
    }

    public void setChapterNumberBn(String chapterNumberBn) {
        this.chapterNumberBn = chapterNumberBn;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public void setTitleBn(String titleBn) {
        this.titleBn = titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getHadithRange() {
        return hadithRange;
    }

    public void setHadithRange(String hadithRange) {
        this.hadithRange = hadithRange;
    }

    public String getHadithRangeBn() {
        return hadithRangeBn;
    }

    public void setHadithRangeBn(String hadithRangeBn) {
        this.hadithRangeBn = hadithRangeBn;
    }

    public int getStartHadith() {
        return startHadith;
    }

    public void setStartHadith(int startHadith) {
        this.startHadith = startHadith;
    }

    public int getEndHadith() {
        return endHadith;
    }

    public void setEndHadith(int endHadith) {
        this.endHadith = endHadith;
    }

    public int getTotalHadith() {
        return totalHadith;
    }

    public void setTotalHadith(int totalHadith) {
        this.totalHadith = totalHadith;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
