package com.devflux.deenone.features.ramadan.model;

/**
 * Model representing an authentic topic item for section: ফিতরা.
 * Supports distinct page title (top bar) and card title inside the expandable view.
 */
public class RozaFitraTopicItem {
    private final int id;
    private final String slug;
    private final String titleBn;
    private final String titleEn;
    private final String cardTitleBn;
    private final String cardTitleEn;
    private final String previewBn;
    private final String previewEn;
    private final String detailsBn;
    private final String detailsEn;
    private final String referenceBn;
    private final String referenceEn;

    public RozaFitraTopicItem(int id, String slug, String titleBn, String titleEn,
                              String cardTitleBn, String cardTitleEn,
                              String previewBn, String previewEn,
                              String detailsBn, String detailsEn,
                              String referenceBn, String referenceEn) {
        this.id = id;
        this.slug = slug;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.cardTitleBn = cardTitleBn;
        this.cardTitleEn = cardTitleEn;
        this.previewBn = previewBn;
        this.previewEn = previewEn;
        this.detailsBn = detailsBn;
        this.detailsEn = detailsEn;
        this.referenceBn = referenceBn;
        this.referenceEn = referenceEn;
    }

    public RozaFitraTopicItem(int id, String slug, String titleBn, String titleEn,
                              String previewBn, String previewEn,
                              String detailsBn, String detailsEn,
                              String referenceBn, String referenceEn) {
        this(id, slug, titleBn, titleEn, titleBn, titleEn, previewBn, previewEn, detailsBn, detailsEn, referenceBn, referenceEn);
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

    public String getCardTitle(boolean isBn) {
        if (isBn) {
            return (cardTitleBn != null && !cardTitleBn.trim().isEmpty()) ? cardTitleBn : titleBn;
        } else {
            return (cardTitleEn != null && !cardTitleEn.trim().isEmpty()) ? cardTitleEn : titleEn;
        }
    }

    public String getCardTitleBn() {
        return cardTitleBn;
    }

    public String getCardTitleEn() {
        return cardTitleEn;
    }

    public String getPreview(boolean isBn) {
        return isBn ? previewBn : previewEn;
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
}
