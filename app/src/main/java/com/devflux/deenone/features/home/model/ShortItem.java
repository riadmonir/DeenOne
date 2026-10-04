package com.devflux.deenone.features.home.model;

public class ShortItem {
    private final String title;
    private final String views;
    private final String videoUrl;

    public ShortItem(String title, String views, String videoUrl) {
        this.title = title;
        this.views = views;
        this.videoUrl = videoUrl;
    }

    public String getTitle() { return title; }
    public String getViews() { return views; }
    public String getVideoUrl() { return videoUrl; }
}
