package com.devflux.deenone.features.ramadan.model;

import java.io.Serializable;

/**
 * Data Model for Roza Health Advice (রোজায় স্বাস্থ্য পরামর্শ) items.
 * Supports dual-language (bn/en) and verbatim screenshot rendering.
 */
public class RozaHealthAdviceItem implements Serializable {
    private int id;
    private String slug;
    private String headerTitleBn;
    private String headerTitleEn;
    private String cardTitleBn;
    private String cardTitleEn;
    private String categoryBn;
    private String categoryEn;
    private String previewBn;
    private String previewEn;
    private String detailsBn;
    private String detailsEn;
    private String referenceBn;
    private String referenceEn;
    private int displayOrder;
    private boolean isExpanded;

    public RozaHealthAdviceItem(int id, String slug, String headerTitleBn, String headerTitleEn,
                                String cardTitleBn, String cardTitleEn,
                                String categoryBn, String categoryEn,
                                String previewBn, String previewEn,
                                String detailsBn, String detailsEn,
                                String referenceBn, String referenceEn,
                                int displayOrder) {
        this.id = id;
        this.slug = slug;
        this.headerTitleBn = headerTitleBn;
        this.headerTitleEn = headerTitleEn;
        this.cardTitleBn = cardTitleBn;
        this.cardTitleEn = cardTitleEn;
        this.categoryBn = categoryBn;
        this.categoryEn = categoryEn;
        this.previewBn = previewBn;
        this.previewEn = previewEn;
        this.detailsBn = detailsBn;
        this.detailsEn = detailsEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
        this.displayOrder = displayOrder;
        this.isExpanded = false;
    }

    public int getId() { return id; }
    public String getSlug() { return slug; }

    public String getHeaderTitle(boolean isBn) {
        return (isBn || headerTitleEn == null || headerTitleEn.trim().isEmpty()) ? headerTitleBn : headerTitleEn;
    }

    public String getCardTitle(boolean isBn) {
        return (isBn || cardTitleEn == null || cardTitleEn.trim().isEmpty()) ? cardTitleBn : cardTitleEn;
    }

    public String getCategory(boolean isBn) {
        return (isBn || categoryEn == null || categoryEn.trim().isEmpty()) ? categoryBn : categoryEn;
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

    public int getDisplayOrder() { return displayOrder; }

    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
