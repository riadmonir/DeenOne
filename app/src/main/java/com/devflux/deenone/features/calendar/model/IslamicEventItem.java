package com.devflux.deenone.features.calendar.model;

import java.io.Serializable;

public class IslamicEventItem implements Serializable {
    private final String id;
    private final String title;
    private final String arabicName;
    private final String hijriDateString;
    private final String gregorianDateString;
    private final String category; // "fardh_eid", "ramadan_hajj", "sunnah_fast", "holy_night"
    private final String importanceBadge; // "ফরজ সিয়াম", "ওয়াজিব ঈদ", "হজের দিন", "সুন্নাত রোজা", "মহিমান্বিত রাত"
    private final String countdownText;
    private final String description;
    private final String reference;

    public IslamicEventItem(String id, String title, String arabicName, String hijriDateString,
                            String gregorianDateString, String category, String importanceBadge,
                            String countdownText, String description, String reference) {
        this.id = id;
        this.title = title;
        this.arabicName = arabicName;
        this.hijriDateString = hijriDateString;
        this.gregorianDateString = gregorianDateString;
        this.category = category;
        this.importanceBadge = importanceBadge;
        this.countdownText = countdownText;
        this.description = description;
        this.reference = reference;
    }

    public IslamicEventItem(String id, String title, String arabicName, String hijriDateString,
                            String gregorianDateString, String category, String importanceBadge,
                            String description, String reference) {
        this(id, title, arabicName, hijriDateString, gregorianDateString, category, importanceBadge, "", description, reference);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArabicName() {
        return arabicName != null ? arabicName : "";
    }

    public String getHijriDateString() {
        return hijriDateString;
    }

    public String getGregorianDateString() {
        return gregorianDateString;
    }

    public String getCategory() {
        return category;
    }

    public String getImportanceBadge() {
        return importanceBadge;
    }

    public String getCountdownText() {
        return countdownText;
    }

    public String getDescription() {
        return description;
    }

    public String getReference() {
        return reference;
    }
}
