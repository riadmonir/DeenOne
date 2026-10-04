package com.devflux.deenone.features.hajj.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class HajjTopicItem implements Serializable {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String categoryBn;
    private final String categoryEn;
    private final @DrawableRes int iconResId;
    private final String overviewBn;
    private final String overviewEn;
    private final String whatToDoBn;
    private final String whatToDoEn;
    private final String whatNotToDoBn;
    private final String whatNotToDoEn;
    private final String arabicDua;
    private final String transliterationBn;
    private final String transliterationEn;
    private final String translationBn;
    private final String translationEn;
    private final String reference;

    public HajjTopicItem(int id,
                         String titleBn,
                         String titleEn,
                         String categoryBn,
                         String categoryEn,
                         @DrawableRes int iconResId,
                         String overviewBn,
                         String overviewEn,
                         String whatToDoBn,
                         String whatToDoEn,
                         String whatNotToDoBn,
                         String whatNotToDoEn,
                         String arabicDua,
                         String transliterationBn,
                         String transliterationEn,
                         String translationBn,
                         String translationEn,
                         String reference) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.categoryBn = categoryBn;
        this.categoryEn = categoryEn;
        this.iconResId = iconResId;
        this.overviewBn = overviewBn;
        this.overviewEn = overviewEn;
        this.whatToDoBn = whatToDoBn;
        this.whatToDoEn = whatToDoEn;
        this.whatNotToDoBn = whatNotToDoBn;
        this.whatNotToDoEn = whatNotToDoEn;
        this.arabicDua = arabicDua;
        this.transliterationBn = transliterationBn;
        this.transliterationEn = transliterationEn;
        this.translationBn = translationBn;
        this.translationEn = translationEn;
        this.reference = reference;
    }

    public int getId() { return id; }
    public String getTitleBn() { return titleBn; }
    public String getTitleEn() { return titleEn; }
    public String getTitle(boolean isBn) { return isBn ? titleBn : titleEn; }
    public String getCategoryBn() { return categoryBn; }
    public String getCategoryEn() { return categoryEn; }
    public String getCategory(boolean isBn) { return isBn ? categoryBn : categoryEn; }
    public @DrawableRes int getIconResId() { return iconResId; }
    public String getOverviewBn() { return overviewBn; }
    public String getOverviewEn() { return overviewEn; }
    public String getOverview(boolean isBn) { return isBn ? overviewBn : overviewEn; }
    public String getWhatToDoBn() { return whatToDoBn; }
    public String getWhatToDoEn() { return whatToDoEn; }
    public String getWhatToDo(boolean isBn) { return isBn ? whatToDoBn : whatToDoEn; }
    public String getWhatNotToDoBn() { return whatNotToDoBn; }
    public String getWhatNotToDoEn() { return whatNotToDoEn; }
    public String getWhatNotToDo(boolean isBn) { return isBn ? whatNotToDoBn : whatNotToDoEn; }
    public String getArabicDua() { return arabicDua; }
    public String getTransliterationBn() { return transliterationBn; }
    public String getTransliterationEn() { return transliterationEn; }
    public String getTransliteration(boolean isBn) { return isBn ? transliterationBn : transliterationEn; }
    public String getTranslationBn() { return translationBn; }
    public String getTranslationEn() { return translationEn; }
    public String getTranslation(boolean isBn) { return isBn ? translationBn : translationEn; }
    public String getReference() { return reference; }
}
