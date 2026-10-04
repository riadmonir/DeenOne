package com.devflux.deenone.features.hajj.model;

import java.io.Serializable;

public class MinistryHajjGuideItem implements Serializable {
    private final int id;
    private final String title;
    private final String preview;
    private final String fullContent;
    private boolean isExpanded;

    public MinistryHajjGuideItem(int id, String title, String preview, String fullContent) {
        this.id = id;
        this.title = title;
        this.preview = preview;
        this.fullContent = fullContent;
        this.isExpanded = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public String getFullContent() {
        return fullContent;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
