package com.devflux.deenone.features.salahguide.model;

public class SalahHistorySection {
    private final String heading;
    private final String arabicText;
    private final String bengaliText;
    private final String reference;

    public SalahHistorySection(String heading, String bengaliText, String reference) {
        this(heading, "", bengaliText, reference);
    }

    public SalahHistorySection(String heading, String arabicText, String bengaliText, String reference) {
        this.heading = heading != null ? heading : "";
        this.arabicText = arabicText != null ? arabicText : "";
        this.bengaliText = bengaliText != null ? bengaliText : "";
        this.reference = reference != null ? reference : "";
    }

    public String getHeading() {
        return heading;
    }

    public String getArabicText() {
        return arabicText;
    }

    public String getBengaliText() {
        return bengaliText;
    }

    public String getReference() {
        return reference;
    }
}
