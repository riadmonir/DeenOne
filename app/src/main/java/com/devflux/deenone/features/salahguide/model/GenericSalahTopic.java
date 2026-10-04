package com.devflux.deenone.features.salahguide.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericSalahTopic {
    private final String id;
    private final String title;
    private final String badge;
    private final String overviewDescription;
    private final List<GenericSalahSection> sections;

    public GenericSalahTopic(String id, String title, String badge, String overviewDescription, List<GenericSalahSection> sections) {
        this.id = id != null ? id : "";
        this.title = title != null ? title : "";
        this.badge = badge != null ? badge : "";
        this.overviewDescription = overviewDescription != null ? overviewDescription : "";
        this.sections = sections != null ? new ArrayList<>(sections) : Collections.emptyList();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getBadge() { return badge; }
    public String getOverviewDescription() { return overviewDescription; }
    public List<GenericSalahSection> getSections() { return Collections.unmodifiableList(sections); }
}
