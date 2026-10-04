package com.devflux.deenone.features.salahguide.model;

import java.util.Collections;
import java.util.List;

public class SalahHistoryItem {
    private final String id;
    private final String title;
    private final String badge;
    private final String summary;
    private final List<SalahHistorySection> sections;

    public SalahHistoryItem(String id, String title, String badge, String summary, List<SalahHistorySection> sections) {
        this.id = id != null ? id : "";
        this.title = title != null ? title : "";
        this.badge = badge != null ? badge : "";
        this.summary = summary != null ? summary : "";
        this.sections = sections != null ? sections : Collections.emptyList();
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBadge() {
        return badge;
    }

    public String getSummary() {
        return summary;
    }

    public List<SalahHistorySection> getSections() {
        return sections;
    }
}
