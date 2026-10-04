package com.devflux.deenone.features.hajj.model;

import android.content.Context;
import androidx.annotation.DrawableRes;

public class HajjJourneyStage {
    private final int stageNumber;
    private final String stageNumberBn;
    private final String stageNumberEn;
    private final String titleBn;
    private final String titleEn;
    private final String descriptionBn;
    private final String descriptionEn;
    private final String imageUrl;
    private final @DrawableRes int imageResId;
    private final @DrawableRes int lineDrawableResId;
    private final String starColorHex;
    private final String detailsBn;
    private final String detailsEn;
    private final String duaArabic;
    private final String duaPronunciationBn;
    private final String duaPronunciationEn;
    private final String duaMeaningBn;
    private final String duaMeaningEn;
    private final String reference;

    public HajjJourneyStage(
            int stageNumber,
            String stageNumberBn,
            String stageNumberEn,
            String titleBn,
            String titleEn,
            String descriptionBn,
            String descriptionEn,
            String imageUrl,
            @DrawableRes int imageResId,
            @DrawableRes int lineDrawableResId,
            String starColorHex,
            String detailsBn,
            String detailsEn,
            String duaArabic,
            String duaPronunciationBn,
            String duaPronunciationEn,
            String duaMeaningBn,
            String duaMeaningEn,
            String reference
    ) {
        this.stageNumber = stageNumber;
        this.stageNumberBn = stageNumberBn;
        this.stageNumberEn = stageNumberEn;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.descriptionBn = descriptionBn;
        this.descriptionEn = descriptionEn;
        this.imageUrl = imageUrl;
        this.imageResId = imageResId;
        this.lineDrawableResId = lineDrawableResId;
        this.starColorHex = starColorHex;
        this.detailsBn = detailsBn;
        this.detailsEn = detailsEn;
        this.duaArabic = duaArabic;
        this.duaPronunciationBn = duaPronunciationBn;
        this.duaPronunciationEn = duaPronunciationEn;
        this.duaMeaningBn = duaMeaningBn;
        this.duaMeaningEn = duaMeaningEn;
        this.reference = reference;
    }

    public HajjJourneyStage(
            int stageNumber,
            String stageNumberBn,
            String stageNumberEn,
            String titleBn,
            String titleEn,
            String descriptionBn,
            String descriptionEn,
            @DrawableRes int imageResId,
            @DrawableRes int lineDrawableResId,
            String starColorHex,
            String detailsBn,
            String detailsEn,
            String duaArabic,
            String duaPronunciationBn,
            String duaPronunciationEn,
            String duaMeaningBn,
            String duaMeaningEn,
            String reference
    ) {
        this(stageNumber, stageNumberBn, stageNumberEn, titleBn, titleEn, descriptionBn, descriptionEn,
                null, imageResId, lineDrawableResId, starColorHex, detailsBn, detailsEn,
                duaArabic, duaPronunciationBn, duaPronunciationEn, duaMeaningBn, duaMeaningEn, reference);
    }

    public int getStageNumber() {
        return stageNumber;
    }

    public String getStageNumber(boolean isBn) {
        return isBn ? stageNumberBn : stageNumberEn;
    }

    public String getTitle(boolean isBn) {
        return isBn ? titleBn : titleEn;
    }

    public String getDescription(boolean isBn) {
        return isBn ? descriptionBn : descriptionEn;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrl(android.content.Context context) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                return imageUrl;
            }
            String base = com.devflux.deenone.core.backend.BackendConfigManager.getPhpBaseUrl(context);
            if (!base.endsWith("/")) base += "/";
            String rel = imageUrl.startsWith("/") ? imageUrl.substring(1) : imageUrl;
            return base + rel;
        }
        String base = com.devflux.deenone.core.backend.BackendConfigManager.getPhpBaseUrl(context);
        if (!base.endsWith("/")) base += "/";
        return base + "uploads/hajj_journey/img_hajj_journey_stage_" + stageNumber + ".png";
    }

    public int getLineDrawableResId() {
        return lineDrawableResId;
    }

    public String getStarColorHex() {
        return starColorHex;
    }

    public String getDetails(boolean isBn) {
        return isBn ? detailsBn : detailsEn;
    }

    public String getDuaArabic() {
        return duaArabic;
    }

    public String getDuaPronunciation(boolean isBn) {
        return isBn ? duaPronunciationBn : duaPronunciationEn;
    }

    public String getDuaMeaning(boolean isBn) {
        return isBn ? duaMeaningBn : duaMeaningEn;
    }

    public String getReference() {
        return reference;
    }

    public boolean isEven() {
        return stageNumber % 2 == 0;
    }
}
