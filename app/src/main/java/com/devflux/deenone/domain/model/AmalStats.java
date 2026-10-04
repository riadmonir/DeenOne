package com.devflux.deenone.domain.model;

public class AmalStats {
    private final int streakDays;
    private final int completedGoalsCount;
    private final int totalGoalsCount;
    private final int completedPrayersCount;
    private final int pointsEarned;
    private final double overallPercentage;

    public AmalStats(int streakDays, int completedGoalsCount, int totalGoalsCount,
                     int completedPrayersCount, int pointsEarned, double overallPercentage) {
        this.streakDays = streakDays;
        this.completedGoalsCount = completedGoalsCount;
        this.totalGoalsCount = totalGoalsCount;
        this.completedPrayersCount = completedPrayersCount;
        this.pointsEarned = pointsEarned;
        this.overallPercentage = overallPercentage;
    }

    public int getStreakDays() { return streakDays; }
    public int getCompletedGoalsCount() { return completedGoalsCount; }
    public int getTotalGoalsCount() { return totalGoalsCount; }
    public int getCompletedPrayersCount() { return completedPrayersCount; }
    public int getPointsEarned() { return pointsEarned; }
    public double getOverallPercentage() { return overallPercentage; }
}
