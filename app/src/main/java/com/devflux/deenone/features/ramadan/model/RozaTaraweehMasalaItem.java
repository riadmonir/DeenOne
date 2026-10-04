package com.devflux.deenone.features.ramadan.model;

/**
 * Model class representing a single Masala item under "তারাবীহ নিয়ে মাসআলা-মাসায়েল".
 * Exactly supports verbatim texts, previews, references, and dual-language mode.
 */
public class RozaTaraweehMasalaItem {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String previewBn;
    private final String previewEn;
    private final String contentBn;
    private final String contentEn;
    private final String referenceBn;
    private final String referenceEn;
    private boolean isExpanded = false;

    public RozaTaraweehMasalaItem(
            int id,
            String titleBn,
            String titleEn,
            String previewBn,
            String previewEn,
            String contentBn,
            String contentEn,
            String referenceBn,
            String referenceEn
    ) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.previewBn = previewBn;
        this.previewEn = previewEn;
        this.contentBn = contentBn;
        this.contentEn = contentEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
    }

    public int getId() {
        return id;
    }

    public String getTitle(boolean isBn) {
        return isBn ? titleBn : titleEn;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getPreview(boolean isBn) {
        return isBn ? previewBn : previewEn;
    }

    public String getContent(boolean isBn) {
        return isBn ? contentBn : contentEn;
    }

    public String getReference(boolean isBn) {
        return isBn ? referenceBn : referenceEn;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
    }
}
