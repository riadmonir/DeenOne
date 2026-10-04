package com.devflux.deenone.features.ramadan.model;

public class RozaScheduleItem {
    private final String hijriDayAndMonth;
    private final String gregorianDayAndDate;
    private final String sehriTime;
    private final String iftarTime;
    private final boolean isToday;

    public RozaScheduleItem(String hijriDayAndMonth, String gregorianDayAndDate, String sehriTime, String iftarTime, boolean isToday) {
        this.hijriDayAndMonth = hijriDayAndMonth;
        this.gregorianDayAndDate = gregorianDayAndDate;
        this.sehriTime = sehriTime;
        this.iftarTime = iftarTime;
        this.isToday = isToday;
    }

    public String getHijriDayAndMonth() {
        return hijriDayAndMonth;
    }

    public String getGregorianDayAndDate() {
        return gregorianDayAndDate;
    }

    public String getSehriTime() {
        return sehriTime;
    }

    public String getIftarTime() {
        return iftarTime;
    }

    public boolean isToday() {
        return isToday;
    }
}
