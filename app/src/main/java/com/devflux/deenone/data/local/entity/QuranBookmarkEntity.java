package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quran_bookmarks")
public class QuranBookmarkEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int surahNumber;
    private int ayahNumber;
    private String surahNameBengali;
    private String ayahTextSnippet;
    private long createdAt;

    public QuranBookmarkEntity(int surahNumber, int ayahNumber, String surahNameBengali, String ayahTextSnippet, long createdAt) {
        this.surahNumber = surahNumber;
        this.ayahNumber = ayahNumber;
        this.surahNameBengali = surahNameBengali;
        this.ayahTextSnippet = ayahTextSnippet;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSurahNumber() { return surahNumber; }
    public void setSurahNumber(int surahNumber) { this.surahNumber = surahNumber; }

    public int getAyahNumber() { return ayahNumber; }
    public void setAyahNumber(int ayahNumber) { this.ayahNumber = ayahNumber; }

    public String getSurahNameBengali() { return surahNameBengali; }
    public void setSurahNameBengali(String surahNameBengali) { this.surahNameBengali = surahNameBengali; }

    public String getAyahTextSnippet() { return ayahTextSnippet; }
    public void setAyahTextSnippet(String ayahTextSnippet) { this.ayahTextSnippet = ayahTextSnippet; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
