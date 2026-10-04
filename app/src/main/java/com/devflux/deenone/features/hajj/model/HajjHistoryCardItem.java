package com.devflux.deenone.features.hajj.model;

import java.io.Serializable;

public class HajjHistoryCardItem implements Serializable {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String previewBn;
    private final String previewEn;
    private final String fullContentBn;
    private final String fullContentEn;
    private boolean isExpanded;

    public HajjHistoryCardItem(int id, String titleBn, String titleEn, String previewBn, String previewEn, String fullContentBn, String fullContentEn) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.previewBn = previewBn;
        this.previewEn = previewEn;
        this.fullContentBn = fullContentBn;
        this.fullContentEn = fullContentEn;
        this.isExpanded = false;
    }

    public int getId() {
        return id;
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

    public String getPreview(boolean isBn) {
        return isBn ? previewBn : previewEn;
    }

    public String getPreviewBn() {
        return previewBn;
    }

    public String getPreviewEn() {
        return previewEn;
    }

    public String getFullContent(boolean isBn) {
        return isBn ? fullContentBn : fullContentEn;
    }

    public String getFullContentBn() {
        return fullContentBn;
    }

    public String getFullContentEn() {
        return fullContentEn;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
