package com.devflux.deenone.features.amal.model;

public class AmalGoalItem {
    private final int id;
    private final String title;
    private final String target;
    private final int points;
    private int completedCount;
    private final int iconResId;
    private final String description;
    private boolean isCompleted;
    private boolean isExpanded;

    public AmalGoalItem(int id, String title, String target, int points, int completedCount, int iconResId, String description) {
        this.id = id;
        this.title = title;
        this.target = target;
        this.points = points;
        this.completedCount = completedCount;
        this.iconResId = iconResId;
        this.description = description;
        this.isCompleted = false;
        this.isExpanded = false;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getTarget() { return target; }
    public int getPoints() { return points; }
    public int getCompletedCount() { return completedCount; }
    public void setCompletedCount(int completedCount) { this.completedCount = completedCount; }
    public int getIconResId() { return iconResId; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
