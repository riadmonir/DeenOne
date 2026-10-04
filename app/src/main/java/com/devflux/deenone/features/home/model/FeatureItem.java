package com.devflux.deenone.features.home.model;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

public class FeatureItem {

    private final int id;
    private final String title;
    @DrawableRes private final int iconResId;
    @ColorRes private final int circleBgColorResId;
    private final int circleBgColorInt;
    private final int iconTintInt;
    private final String actionKey;

    public FeatureItem(int id, String title, @DrawableRes int iconResId, @ColorRes int circleBgColorResId) {
        this.id = id;
        this.title = title;
        this.iconResId = iconResId;
        this.circleBgColorResId = circleBgColorResId;
        this.circleBgColorInt = 0;
        this.iconTintInt = 0;
        this.actionKey = "";
    }

    public FeatureItem(int id, String title, @DrawableRes int iconResId, int circleBgColorInt, int iconTintInt, String actionKey) {
        this.id = id;
        this.title = title;
        this.iconResId = iconResId;
        this.circleBgColorResId = 0;
        this.circleBgColorInt = circleBgColorInt;
        this.iconTintInt = iconTintInt;
        this.actionKey = actionKey;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
    public int getCircleBgColorResId() { return circleBgColorResId; }
    public int getCircleBgColorInt() { return circleBgColorInt; }
    public int getIconTintInt() { return iconTintInt; }
    public String getActionKey() { return actionKey != null ? actionKey : ""; }
}
