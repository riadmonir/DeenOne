package com.devflux.deenone.features.calendar.model;

import java.io.Serializable;

public class IslamicMonthItem implements Serializable {
    private final int monthNumber;
    private final String nameBengali;
    private final String nameArabic;
    private final String nameEnglish;
    private final boolean isSacredMonth; // ৪টি হারাম/সম্মানিত মাস
    private final String statusBadge;
    private final String spiritualSignificance;
    private final String keyEvents;

    public IslamicMonthItem(int monthNumber, String nameBengali, String nameArabic,
                            String nameEnglish, boolean isSacredMonth, String statusBadge,
                            String spiritualSignificance, String keyEvents) {
        this.monthNumber = monthNumber;
        this.nameBengali = nameBengali;
        this.nameArabic = nameArabic;
        this.nameEnglish = nameEnglish;
        this.isSacredMonth = isSacredMonth;
        this.statusBadge = statusBadge;
        this.spiritualSignificance = spiritualSignificance;
        this.keyEvents = keyEvents;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public String getNameBengali() {
        return nameBengali;
    }

    public String getNameArabic() {
        return nameArabic;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public boolean isSacredMonth() {
        return isSacredMonth;
    }

    public String getStatusBadge() {
        return statusBadge;
    }

    public String getSpiritualSignificance() {
        return spiritualSignificance;
    }

    public String getKeyEvents() {
        return keyEvents;
    }
}
