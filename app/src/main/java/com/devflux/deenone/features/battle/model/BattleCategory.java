package com.devflux.deenone.features.battle.model;

import java.io.Serializable;

public class BattleCategory implements Serializable {
    private final String id;
    private final String tagBn;
    private final String tagEn;
    private final String titleBn;
    private final String titleEn;
    private final String descriptionBn;
    private final String descriptionEn;
    private final String iconResName;
    private final String colorHex;
    private final String tintHex;
    private int totalQuestions;
    private final int durationMinutes;
    private final int pointsPerQuestion;

    public BattleCategory(String id, String titleBn, String description, String iconResName, int totalQuestions) {
        this(id, "সাধারণ জ্ঞান", "General Knowledge", titleBn, id, description, description, iconResName, "#144234", "#F0C04A", totalQuestions, 10, 10);
    }

    public BattleCategory(String id, String titleBn, String titleEn, String iconResName,
                          String colorHex, String tintHex, int totalQuestions, String description) {
        this(id, "সাধারণ জ্ঞান", "General Knowledge", titleBn, titleEn, description, description, iconResName, colorHex, tintHex, totalQuestions, 10, 10);
    }

    public BattleCategory(String id, String tagBn, String tagEn, String titleBn, String titleEn,
                          String descriptionBn, String descriptionEn, String iconResName,
                          int totalQuestions, int durationMinutes, int pointsPerQuestion) {
        this(id, tagBn, tagEn, titleBn, titleEn, descriptionBn, descriptionEn, iconResName, "#144234", "#F0C04A", totalQuestions, durationMinutes, pointsPerQuestion);
    }

    public BattleCategory(String id, String tagBn, String tagEn, String titleBn, String titleEn,
                          String descriptionBn, String descriptionEn, String iconResName,
                          String colorHex, String tintHex, int totalQuestions, int durationMinutes, int pointsPerQuestion) {
        this.id = id;
        this.tagBn = tagBn != null ? tagBn : "সাধারণ জ্ঞান";
        this.tagEn = tagEn != null ? tagEn : "General Knowledge";
        this.titleBn = titleBn;
        this.titleEn = titleEn != null ? titleEn : titleBn;
        this.descriptionBn = descriptionBn;
        this.descriptionEn = descriptionEn != null ? descriptionEn : descriptionBn;
        this.iconResName = iconResName != null ? iconResName : "ic_feat_book";
        this.colorHex = colorHex != null ? colorHex : "#144234";
        this.tintHex = tintHex != null ? tintHex : "#F0C04A";
        this.totalQuestions = totalQuestions > 0 ? totalQuestions : 5;
        this.durationMinutes = durationMinutes > 0 ? durationMinutes : 10;
        this.pointsPerQuestion = pointsPerQuestion > 0 ? pointsPerQuestion : 10;
    }

    public String getId() { return id; }
    public String getTagBn() { return tagBn; }
    public String getTagEn() { return tagEn; }
    public String getTitleBn() { return titleBn; }
    public String getTitleEn() { return titleEn; }
    public String getDescriptionBn() { return descriptionBn; }
    public String getDescriptionEn() { return descriptionEn; }
    public String getDescription() { return descriptionBn; }
    public String getIconResName() { return iconResName; }
    public String getColorHex() { return colorHex; }
    public String getTintHex() { return tintHex; }
    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = Math.max(1, totalQuestions); }
    public int getDurationMinutes() { return durationMinutes; }
    public int getPointsPerQuestion() { return pointsPerQuestion; }
}
