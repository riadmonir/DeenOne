package com.devflux.deenone.features.prophets.model;

import java.io.Serializable;

public class ProphetMenuItem implements Serializable {
    public enum Type {
        OVERVIEW,
        PROPHET
    }

    private final int id;
    private final String titleBn;
    private final String titleEn;
    private final Type type;
    private final ProphetStoryItem prophetStoryItem;

    public ProphetMenuItem(int id, String titleBn, String titleEn, Type type, ProphetStoryItem prophetStoryItem) {
        this.id = id;
        this.titleBn = titleBn;
        this.titleEn = titleEn;
        this.type = type;
        this.prophetStoryItem = prophetStoryItem;
    }

    public int getId() { return id; }
    public String getTitle(boolean isBn) { return isBn ? titleBn : titleEn; }
    public String getTitleBn() { return titleBn; }
    public String getTitleEn() { return titleEn; }
    public Type getType() { return type; }
    public ProphetStoryItem getProphetStoryItem() { return prophetStoryItem; }
}
