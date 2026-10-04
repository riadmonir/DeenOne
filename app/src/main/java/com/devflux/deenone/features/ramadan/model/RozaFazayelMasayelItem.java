package com.devflux.deenone.features.ramadan.model;

import java.io.Serializable;

/**
 * Data Model for Roza Fazayel & Masayel (ফাযায়েল মাসায়েল).
 * 100% matches screenshot verbatim text, serial numbers, dual language, and authentic Fiqh rulings.
 */
public class RozaFazayelMasayelItem implements Serializable {
    private final int id;
    private final String slug;
    private final int serialNumber;
    private final String titleBn;
    private final String titleEn;
    private final String previewBn;
    private final String previewEn;
    private final String detailsBn;
    private final String detailsEn;
    private final String referenceBn;
    private final String referenceEn;
    private boolean isExpanded;

    public RozaFazayelMasayelItem(
            int id,
            String slug,
            int serialNumber,
            String titleBn,
            String titleEn,
            String previewBn,
            String previewEn,
            String detailsBn,
            String detailsEn,
            String referenceBn,
            String referenceEn
    ) {
        this.id = id;
        this.slug = slug;
        this.serialNumber = serialNumber;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.previewBn = previewBn;
        this.previewEn = previewEn;
        this.detailsBn = detailsBn;
        this.detailsEn = detailsEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
        this.isExpanded = false;
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public String getFormattedSerialNumber(boolean isBn) {
        if (!isBn) {
            return String.valueOf(serialNumber);
        }
        return convertToBengaliDigits(serialNumber);
    }

    private static String convertToBengaliDigits(int number) {
        String s = String.valueOf(number);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                sb.append((char) (c - '0' + '০'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String getTitle(boolean isBn) {
        return (isBn || titleEn == null || titleEn.trim().isEmpty()) ? titleBn : titleEn;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getPreview(boolean isBn) {
        return (isBn || previewEn == null || previewEn.trim().isEmpty()) ? previewBn : previewEn;
    }

    public String getDetails(boolean isBn) {
        return (isBn || detailsEn == null || detailsEn.trim().isEmpty()) ? detailsBn : detailsEn;
    }

    public String getReference(boolean isBn) {
        return (isBn || referenceEn == null || referenceEn.trim().isEmpty()) ? referenceBn : referenceEn;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
