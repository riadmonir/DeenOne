package com.devflux.deenone.features.ramadan.model;

/**
 * Data Model representing an item in the Roza Taraweeh section (রোজা:- তারাবীহ).
 * Supports 100% verbatim text, dual-language mode, and authentic Islamic content.
 */
public class RozaTaraweehItem {
    private final int id;
    private final String slug;
    private final String titleBn;
    private final String titleEn;
    private final String categoryBn;
    private final String categoryEn;
    private final String arabicText;
    private final String transliterationBn;
    private final String transliterationEn;
    private final String translationBn;
    private final String translationEn;
    private final String previewBn;
    private final String previewEn;
    private final String detailsBn;
    private final String detailsEn;
    private final String referenceBn;
    private final String referenceEn;
    private boolean isExpanded = false;

    public RozaTaraweehItem(
            int id,
            String slug,
            String titleBn,
            String titleEn,
            String categoryBn,
            String categoryEn,
            String arabicText,
            String transliterationBn,
            String transliterationEn,
            String translationBn,
            String translationEn,
            String detailsBn,
            String detailsEn,
            String referenceBn,
            String referenceEn
    ) {
        this(id, slug, titleBn, titleEn, categoryBn, categoryEn, arabicText,
                transliterationBn, transliterationEn, translationBn, translationEn,
                null, null, detailsBn, detailsEn, referenceBn, referenceEn);
    }

    public RozaTaraweehItem(
            int id,
            String slug,
            String titleBn,
            String titleEn,
            String categoryBn,
            String categoryEn,
            String arabicText,
            String transliterationBn,
            String transliterationEn,
            String translationBn,
            String translationEn,
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
        this.arabicText = arabicText;
        this.transliterationBn = transliterationBn;
        this.transliterationEn = transliterationEn;
        this.translationBn = translationBn;
        this.translationEn = translationEn;
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

    public String getArabicText() {
        return arabicText;
    }

    public String getTransliteration(boolean isBn) {
        return isBn ? transliterationBn : transliterationEn;
    }

    public String getTransliterationBn() {
        return transliterationBn;
    }

    public String getTransliterationEn() {
        return transliterationEn;
    }

    public String getTranslation(boolean isBn) {
        return isBn ? translationBn : translationEn;
    }

    public String getTranslationBn() {
        return translationBn;
    }

    public String getTranslationEn() {
        return translationEn;
    }

    public String getPreview(boolean isBn) {
        String p = isBn ? previewBn : previewEn;
        if (p != null && !p.trim().isEmpty()) {
            return p;
        }
        String full = getDetails(isBn);
        if (full != null && full.length() > 140) {
            return full.substring(0, 140) + "...";
        }
        return full != null ? full : "";
    }

    public String getPreviewBn() {
        return previewBn;
    }

    public String getPreviewEn() {
        return previewEn;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.isExpanded = expanded;
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
