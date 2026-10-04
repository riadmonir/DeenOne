package com.devflux.deenone.features.ramadan.model;

import java.io.Serializable;

/**
 * Data Model for Laylatul Qadr Topics (section: লাইলাতুল কদর).
 * 100% matches the screenshot items:
 * 1. লাইলাতুল কদর
 * 2. লাইলাতুল কদরের বৈশিষ্ট্য
 * 3. লাইলাতুল কদর খুঁজে পাওয়ার গুরুত্ব
 * 4. লাইলাতুল কদরের ইবাদত
 * 5. লাইলাতুল কদরের ফজিলত
 * 6. কদরের রাতকে ফলপ্রসূ করার উপায়
 * 7. লাইলাতুল কদরের সাথে সম্পর্কিত ইসলামিক বিষয়
 */
public class RozaQadrTopicItem implements Serializable {
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

    public RozaQadrTopicItem(
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

    public RozaQadrTopicItem(
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
