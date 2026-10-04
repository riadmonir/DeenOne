package com.devflux.deenone.features.hajj.model;

import java.io.Serializable;

public class HajjArticleCardItem implements Serializable {
    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final String contentBn;
    private final String contentEn;

    public HajjArticleCardItem(int id, String titleBn, String titleEn, String contentBn, String contentEn) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.contentBn = contentBn;
        this.contentEn = contentEn;
    }

    public int getId() {
        return id;
    }

    public String getTitleBn() {
        return titleBn;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public String getTitle(boolean isBn) {
        return isBn ? titleBn : titleEn;
    }

    public String getContentBn() {
        return contentBn;
    }

    public String getContentEn() {
        return contentEn;
    }

    public String getContent(boolean isBn) {
        return isBn ? contentBn : contentEn;
    }
}
