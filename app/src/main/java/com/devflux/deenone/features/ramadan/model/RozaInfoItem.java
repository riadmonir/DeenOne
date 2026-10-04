package com.devflux.deenone.features.ramadan.model;

public class RozaInfoItem {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String previewTextBn;
    private final String previewTextEn;
    private final String fullContentBn;
    private final String fullContentEn;
    private final String referenceBn;
    private final String referenceEn;
    private boolean isExpanded;

    public RozaInfoItem(int id, String titleBn, String titleEn,
                        String previewTextBn, String previewTextEn,
                        String fullContentBn, String fullContentEn,
                        String referenceBn, String referenceEn) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.previewTextBn = previewTextBn;
        this.previewTextEn = previewTextEn;
        this.fullContentBn = fullContentBn;
        this.fullContentEn = fullContentEn;
        this.referenceBn = referenceBn != null ? referenceBn : "";
        this.referenceEn = referenceEn != null ? referenceEn : "";
        this.isExpanded = false;
    }

    public int getId() { return id; }
    public String getTitle(boolean isBn) { return isBn ? titleBn : titleEn; }
    public String getPreviewText(boolean isBn) { return isBn ? previewTextBn : previewTextEn; }
    public String getFullContent(boolean isBn) { return isBn ? fullContentBn : fullContentEn; }
    public String getReference(boolean isBn) { return isBn ? referenceBn : referenceEn; }

    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
