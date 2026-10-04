package com.devflux.deenone.features.prophets.model;

import java.io.Serializable;

public class ProphetOverviewTopicItem implements Serializable {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String contentBn;
    private final String contentEn;
    private boolean isExpanded;

    public ProphetOverviewTopicItem(int id, String titleBn, String titleEn, String contentBn, String contentEn) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.contentBn = contentBn;
        this.contentEn = contentEn;
        this.isExpanded = false;
    }

    public int getId() { return id; }
    public String getTitle(boolean isBn) { return isBn ? titleBn : titleEn; }
    public String getContent(boolean isBn) { return isBn ? contentBn : contentEn; }
    public boolean isExpanded() { return isExpanded; }
    public void setExpanded(boolean expanded) { isExpanded = expanded; }
}
