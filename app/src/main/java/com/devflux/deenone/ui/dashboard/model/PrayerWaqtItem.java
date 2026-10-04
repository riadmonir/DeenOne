package com.devflux.deenone.ui.dashboard.model;

public class PrayerWaqtItem {

    private final String name;
    private final String timeStr;
    private final int iconResId;
    private boolean isActive;
    private boolean isPassed;
    private boolean isAlarmEnabled;

    public PrayerWaqtItem(String name, String timeStr, int iconResId, boolean isActive, boolean isPassed, boolean isAlarmEnabled) {
        this.name = name;
        this.timeStr = timeStr;
        this.iconResId = iconResId;
        this.isActive = isActive;
        this.isPassed = isPassed;
        this.isAlarmEnabled = isAlarmEnabled;
    }

    public String getName() { return name; }
    public String getTimeStr() { return timeStr; }
    public int getIconResId() { return iconResId; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public boolean isPassed() { return isPassed; }
    public void setPassed(boolean passed) { isPassed = passed; }
    public boolean isAlarmEnabled() { return isAlarmEnabled; }
    public void setAlarmEnabled(boolean alarmEnabled) { isAlarmEnabled = alarmEnabled; }
}
