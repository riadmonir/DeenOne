package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * AzkarEntity — Represents a single Azkar/Dua item stored in Room DB.
 *
 * Data sourced from Hisnul Muslim / Sunnah.com verified Islamic sources.
 * Reference field always includes Book + Hadith/Ayah number.
 */
@Entity(tableName = "azkar")
public class AzkarEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    /** Category: morning, evening, after_salah, before_sleep, upon_waking,
     *  protection, rizq, forgiveness, travel, parents, ramadan */
    private String category;

    /** Arabic text (UTF-8, fully verified) */
    private String arabic;

    /** Transliteration in English letters */
    private String transliteration;

    /** Bengali translation */
    private String bengaliMeaning;

    /** English translation */
    private String englishMeaning;

    /** Reference: e.g. "বুখারী ৬২৩৬" / "Bukhari 6236" */
    private String reference;

    /** Benefit/virtue of reading this Azkar */
    private String benefit;

    /** How many times to repeat (e.g. 1, 3, 7, 33, 100) */
    private int targetCount;

    /** Current session repeat count */
    private int currentCount;

    /** Online audio URL (if available from Islamic CDNs) */
    private String audioUrl;

    /** Sort order within category */
    private int sortOrder;

    public AzkarEntity() {}

    @Ignore
    public AzkarEntity(String category, String arabic, String transliteration,
                       String bengaliMeaning, String englishMeaning,
                       String reference, String benefit,
                       int targetCount, int currentCount,
                       String audioUrl, int sortOrder) {
        this.category = category;
        this.arabic = arabic;
        this.transliteration = transliteration;
        this.bengaliMeaning = bengaliMeaning;
        this.englishMeaning = englishMeaning;
        this.reference = reference;
        this.benefit = benefit;
        this.targetCount = targetCount;
        this.currentCount = currentCount;
        this.audioUrl = audioUrl;
        this.sortOrder = sortOrder;
    }

    // --- Getters & Setters ---

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getArabic() { return arabic; }
    public void setArabic(String arabic) { this.arabic = arabic; }

    public String getTransliteration() { return transliteration; }
    public void setTransliteration(String transliteration) { this.transliteration = transliteration; }

    public String getBengaliMeaning() { return bengaliMeaning; }
    public void setBengaliMeaning(String bengaliMeaning) { this.bengaliMeaning = bengaliMeaning; }

    public String getEnglishMeaning() { return englishMeaning; }
    public void setEnglishMeaning(String englishMeaning) { this.englishMeaning = englishMeaning; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public String getBenefit() { return benefit; }
    public void setBenefit(String benefit) { this.benefit = benefit; }

    public int getTargetCount() { return targetCount; }
    public void setTargetCount(int targetCount) { this.targetCount = targetCount; }

    public int getCurrentCount() { return currentCount; }
    public void setCurrentCount(int currentCount) { this.currentCount = currentCount; }

    public String getAudioUrl() { return audioUrl; }
    public void setAudioUrl(String audioUrl) { this.audioUrl = audioUrl; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
