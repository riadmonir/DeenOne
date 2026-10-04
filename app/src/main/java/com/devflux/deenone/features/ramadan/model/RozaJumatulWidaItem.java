package com.devflux.deenone.features.ramadan.model;

/**
 * Data Model representing a topic in the Roza Jumatul Wida section (জুমাতুল বিদা).
 * Supports 100% verbatim text, dual-language mode, and authentic Islamic content.
 */
public class RozaJumatulWidaItem {
    private final int id;
    private final String slug;
    private final String titleBn;
    private final String titleEn;
    private final String categoryBn;
    private final String categoryEn;
    private final String previewBn;
    private final String previewEn;
    private final String detailsBn;
    private final String detailsEn;
    private final String referenceBn;
    private final String referenceEn;
    private boolean isExpanded = false;

    public RozaJumatulWidaItem(
            int id,
            String slug,
            String titleBn,
            String titleEn,
            String categoryBn,
            String categoryEn,
            String previewBn,
            String previewEn,
            String detailsBn,
            String detailsEn,
            String referenceBn,
            String referenceEn
    ) {
        this.id = id;
        this.slug = slug;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.categoryBn = categoryBn;
        this.categoryEn = categoryEn;
        this.previewBn = previewBn;
        this.previewEn = previewEn;
        this.detailsBn = detailsBn;
        this.detailsEn = detailsEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
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

    public String getCategory(boolean isBn) {
        return isBn ? categoryBn : categoryEn;
    }

    public String getCategoryBn() {
        return categoryBn;
    }

    public String getCategoryEn() {
        return categoryEn;
    }

    public String getPreview(boolean isBn) {
        if (isBn) {
            if (previewBn != null && !previewBn.isEmpty()) return previewBn;
            return detailsBn != null && detailsBn.length() > 160 ? detailsBn.substring(0, 160) + "..." : detailsBn;
        } else {
            if (previewEn != null && !previewEn.isEmpty()) return previewEn;
            return detailsEn != null && detailsEn.length() > 160 ? detailsEn.substring(0, 160) + "..." : detailsEn;
        }
    }

    public String getPreviewBn() {
        return previewBn;
    }

    public String getPreviewEn() {
        return previewEn;
    }

    public String getDetails(boolean isBn) {
        return isBn ? detailsBn : detailsEn;
    }

    public String getDetailsBn() {
        return detailsBn;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public String getReference(boolean isBn) {
        return isBn ? referenceBn : referenceEn;
    }

    public String getReferenceBn() {
        return referenceBn;
    }

    public String getReferenceEn() {
        return referenceEn;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
