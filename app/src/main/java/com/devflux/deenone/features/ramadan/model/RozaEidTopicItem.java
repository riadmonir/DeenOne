package com.devflux.deenone.features.ramadan.model;

import java.io.Serializable;

/**
 * Data Model for Eid Topics (section: ঈদ).
 * 100% matches the screenshot items:
 * 1. ঈদ
 * 2. ঈদুল ফিতর
 * 3. ঈদের নামাজ
 * 4. ঈদ প্রস্তুতি
 * 5. ঈদ উদযাপন
 * 6. ঈদ সংক্রান্ত সুন্নাত, নফল, ফরজ ও ওয়াজিব বিষয়
 * 7. ঈদ সম্পর্কিত মাসআলা-মাসায়েল
 * 8. ঈদ সংক্রান্ত ইসলামিক বিষয়
 */
public class RozaEidTopicItem implements Serializable {
    private final int id;
    private final String slug;
    private final int orderIndex;
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

    public RozaEidTopicItem(
            int id,
            String slug,
            int orderIndex,
            String titleBn,
            String titleEn,
            String cardTitleBn,
            String cardTitleEn,
            String previewBn,
            String previewEn,
            String detailsBn,
            String detailsEn,
            String referenceBn,
            String referenceEn
    ) {
        this.id = id;
        this.slug = slug;
        this.orderIndex = orderIndex;
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

    public RozaEidTopicItem(
            int id,
            String slug,
            int orderIndex,
            String titleBn,
            String titleEn,
            String previewBn,
            String previewEn,
            String detailsBn,
            String detailsEn,
            String referenceBn,
            String referenceEn
    ) {
        this(id, slug, orderIndex, titleBn, titleEn, titleBn, titleEn, previewBn, previewEn, detailsBn, detailsEn, referenceBn, referenceEn);
    }

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getTitle(boolean isBn) {
        return (isBn || titleEn == null || titleEn.trim().isEmpty()) ? titleBn : titleEn;
    }

    public String getCardTitle(boolean isBn) {
        if (isBn) {
            return (cardTitleBn != null && !cardTitleBn.trim().isEmpty()) ? cardTitleBn : titleBn;
        } else {
            return (cardTitleEn != null && !cardTitleEn.trim().isEmpty()) ? cardTitleEn : getTitle(false);
        }
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

    public String getPreviewBn() {
        return previewBn;
    }

    public String getPreviewEn() {
        return previewEn;
    }

    public String getDetails(boolean isBn) {
        return (isBn || detailsEn == null || detailsEn.trim().isEmpty()) ? detailsBn : detailsEn;
    }

    public String getDetailsBn() {
        return detailsBn;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public String getReference(boolean isBn) {
        return (isBn || referenceEn == null || referenceEn.trim().isEmpty()) ? referenceBn : referenceEn;
    }

    public String getReferenceBn() {
        return referenceBn;
    }

    public String getReferenceEn() {
        return referenceEn;
    }
}
