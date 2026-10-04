package com.devflux.deenone.features.calendar.model;

public class HijriDayGridItem {
    public int dayNumber;
    public String dayNumberBn;
    public String badgeLabel;
    public boolean isToday;
    public boolean isAyyamAlBeed;
    public String specialEvent;
    public String gregorianDateStr;
    public String sunnahFastInfo;

    public HijriDayGridItem(int dayNumber, String dayNumberBn, String badgeLabel, boolean isToday,
                            boolean isAyyamAlBeed, String specialEvent, String gregorianDateStr, String sunnahFastInfo) {
        this.dayNumber = dayNumber;
        this.dayNumberBn = dayNumberBn;
        this.badgeLabel = badgeLabel;
        this.isToday = isToday;
        this.isAyyamAlBeed = isAyyamAlBeed;
        this.specialEvent = specialEvent;
        this.gregorianDateStr = gregorianDateStr;
        this.sunnahFastInfo = sunnahFastInfo;
    }
}
