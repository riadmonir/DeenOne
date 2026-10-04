package com.devflux.deenone.features.ramadan.model;

import java.util.Calendar;

public class RozaCalendarDay {
    public int dayNumber;
    public int month; // 0-based Calendar.MONTH
    public int year;
    public boolean isCurrentMonth;
    public boolean isFriday;
    public boolean isToday;
    public boolean isSelected;
    public int fastingStatus; // 0=Unmarked (Yellow), 1=Fasted (Green), 2=Not Fasted (Red)
    public boolean isFasting;
    public String dateKey; // yyyy_MM_dd
    public Calendar calendar;

    public RozaCalendarDay(int dayNumber, int month, int year, boolean isCurrentMonth,
                           boolean isFriday, boolean isToday, boolean isSelected,
                           boolean isFasting, String dateKey, Calendar calendar) {
        this(dayNumber, month, year, isCurrentMonth, isFriday, isToday, isSelected,
                isFasting ? 1 : 0, dateKey, calendar);
    }

    public RozaCalendarDay(int dayNumber, int month, int year, boolean isCurrentMonth,
                           boolean isFriday, boolean isToday, boolean isSelected,
                           int fastingStatus, String dateKey, Calendar calendar) {
        this.dayNumber = dayNumber;
        this.month = month;
        this.year = year;
        this.isCurrentMonth = isCurrentMonth;
        this.isFriday = isFriday;
        this.isToday = isToday;
        this.isSelected = isSelected;
        this.fastingStatus = fastingStatus;
        this.isFasting = (fastingStatus == 1);
        this.dateKey = dateKey;
        this.calendar = calendar;
    }
}
