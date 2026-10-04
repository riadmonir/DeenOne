package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_amals")
public class DailyAmalEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String amalCode;
    private String title;
    private String description;
    private String howToPerform;
    private String relevantDuaDhikr;
    private String hadithReference;
    private int targetCount;
    private int completedCount;
    private int points;
    private boolean isCompleted;
    private String dateString; // YYYY-MM-DD
    private boolean isExpanded;

    public DailyAmalEntity() {
    }

    @Ignore
    public DailyAmalEntity(String amalCode, String title, String description, String howToPerform,
                           String relevantDuaDhikr, String hadithReference, int targetCount,
                           int completedCount, int points, boolean isCompleted, String dateString) {
        this.amalCode = amalCode;
        this.title = title;
        this.description = description;
        this.howToPerform = howToPerform;
        this.relevantDuaDhikr = relevantDuaDhikr;
        this.hadithReference = hadithReference;
        this.targetCount = targetCount;
        this.completedCount = completedCount;
        this.points = points;
        this.isCompleted = isCompleted;
        this.dateString = dateString;
        this.isExpanded = false;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getAmalCode() { return amalCode; }
    public void setAmalCode(String amalCode) { this.amalCode = amalCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHowToPerform() { return howToPerform; }
    public void setHowToPerform(String howToPerform) { this.howToPerform = howToPerform; }

    public String getRelevantDuaDhikr() { return relevantDuaDhikr; }
    public void setRelevantDuaDhikr(String relevantDuaDhikr) { this.relevantDuaDhikr = relevantDuaDhikr; }

    public String getHadithReference() { return hadithReference; }
    public void setHadithReference(String hadithReference) { this.hadithReference = hadithReference; }

    public int getTargetCount() { return targetCount; }
    public void setTargetCount(int targetCount) { this.targetCount = targetCount; }

    public int getCompletedCount() { return completedCount; }
    public void setCompletedCount(int completedCount) { this.completedCount = completedCount; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    public String getDateString() { return dateString; }
    public void setDateString(String dateString) { this.dateString = dateString; }

    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
