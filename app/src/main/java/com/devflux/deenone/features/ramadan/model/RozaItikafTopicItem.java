package com.devflux.deenone.features.ramadan.model;

import java.io.Serializable;

/**
 * Data Model for Itikaf Topics (section: ইতিকাফ).
 * 100% matches the screenshot items:
 * 1. ইতিকাফ কি?
 * 2. ইতিকাফের গুরুত্ব ও ফজিলত
 * 3. ইতিকাফের ধরন
 * 4. ইতিকাফের নিয়ম ও শর্তাবলি
 * 5. ইতিকাফের সময়ে করণীয় কাজ
 * 6. ইতিকাফ এবং মহিলারা
 * 7. ইতিকাফ এবং আধুনিক যুগ
 * 8. ইতিকাফের ভুল ও সতর্কতা
 * 9. ইতিকাফের ইতিহাস ও উদাহরণ
 * 10. ইতিকাফের প্রস্তুতি
 * 11. বাচ্চাদের জন্য ইতিকাফ শেখানো
 */
public class RozaItikafTopicItem implements Serializable {
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

    public RozaItikafTopicItem(
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

    public int getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getCardTitleBn() {
        return cardTitleBn;
    }

    public String getCardTitleEn() {
        return cardTitleEn;
    }

    public String getPreviewBn() {
        return previewBn;
    }

    public String getPreviewEn() {
        return previewEn;
    }

    public String getDetailsBn() {
        return detailsBn;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public String getReferenceBn() {
        return referenceBn;
    }

    public String getReferenceEn() {
        return referenceEn;
    }

    public String getTitle(boolean isBn) {
        return isBn ? (titleBn != null ? titleBn : "") : (titleEn != null && !titleEn.isEmpty() ? titleEn : titleBn);
    }

    public String getCardTitle(boolean isBn) {
        return isBn ? (cardTitleBn != null ? cardTitleBn : "") : (cardTitleEn != null && !cardTitleEn.isEmpty() ? cardTitleEn : cardTitleBn);
    }

    public String getPreview(boolean isBn) {
        return isBn ? (previewBn != null ? previewBn : "") : (previewEn != null && !previewEn.isEmpty() ? previewEn : previewBn);
    }

    public String getDetails(boolean isBn) {
        return isBn ? (detailsBn != null ? detailsBn : "") : (detailsEn != null && !detailsEn.isEmpty() ? detailsEn : detailsBn);
    }

    public String getReference(boolean isBn) {
        return isBn ? (referenceBn != null ? referenceBn : "") : (referenceEn != null && !referenceEn.isEmpty() ? referenceEn : referenceBn);
    }
}
