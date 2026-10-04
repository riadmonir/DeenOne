package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "amal_records")
public class AmalRecordEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String date; // YYYY-MM-DD
    private int streakDays;
    private int completedGoalsCount;
    private int totalGoalsCount;
    private int completedPrayersCount;
    private int pointsEarned;
    private double overallPercentage;

    public AmalRecordEntity(String date, int streakDays, int completedGoalsCount, int totalGoalsCount,
                            int completedPrayersCount, int pointsEarned, double overallPercentage) {
        this.date = date;
        this.streakDays = streakDays;
        this.completedGoalsCount = completedGoalsCount;
        this.totalGoalsCount = totalGoalsCount;
        this.completedPrayersCount = completedPrayersCount;
        this.pointsEarned = pointsEarned;
        this.overallPercentage = overallPercentage;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }

    public int getCompletedGoalsCount() { return completedGoalsCount; }
    public void setCompletedGoalsCount(int completedGoalsCount) { this.completedGoalsCount = completedGoalsCount; }

    public int getTotalGoalsCount() { return totalGoalsCount; }
    public void setTotalGoalsCount(int totalGoalsCount) { this.totalGoalsCount = totalGoalsCount; }

    public int getCompletedPrayersCount() { return completedPrayersCount; }
    public void setCompletedPrayersCount(int completedPrayersCount) { this.completedPrayersCount = completedPrayersCount; }

    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

    public double getOverallPercentage() { return overallPercentage; }
    public void setOverallPercentage(double overallPercentage) { this.overallPercentage = overallPercentage; }
}
