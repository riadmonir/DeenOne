package com.devflux.deenone.ui.dashboard.model;

public class FeatureItem {

    private final int id;
    private final String title;
    private final int iconResId;
    private final int circleBgColor;

    public FeatureItem(int id, String title, int iconResId, int circleBgColor) {
        this.id = id;
        this.title = title;
        this.iconResId = iconResId;
        this.circleBgColor = circleBgColor;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getIconResId() { return iconResId; }
    public int getCircleBgColor() { return circleBgColor; }
}
