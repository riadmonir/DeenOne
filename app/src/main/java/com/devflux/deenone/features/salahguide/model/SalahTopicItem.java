package com.devflux.deenone.features.salahguide.model;

import java.io.Serializable;
import java.util.List;

public class SalahTopicItem implements Serializable {
    private final String id;
    private final String title;
    private final String subtitle;
    private final String category; // "purity", "fardh", "sunnah_wajib", "nafl_janazah", "steps"
    private final String rulingType; // "ফরজ আইন", "সুন্নাতে মুয়াক্কাদা", "ওয়াজিব", "নফল / মুস্তাহাব", "ফরজে কিফায়া"
    private final String rakahBreakdown;
    private final String timingInfo;
    private final String mainDescription;
    private final List<SalahStepItem> steps;

    public SalahTopicItem(String id, String title, String subtitle, String category,
                          String rulingType, String rakahBreakdown, String timingInfo,
                          String mainDescription, List<SalahStepItem> steps) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.category = category;
        this.rulingType = rulingType;
        this.rakahBreakdown = rakahBreakdown;
        this.timingInfo = timingInfo;
        this.mainDescription = mainDescription;
        this.steps = steps;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getCategory() {
        return category;
    }

    public String getRulingType() {
        return rulingType;
    }

    public String getRakahBreakdown() {
        return rakahBreakdown;
    }

    public String getTimingInfo() {
        return timingInfo;
    }

    public String getMainDescription() {
        return mainDescription;
    }

    public List<SalahStepItem> getSteps() {
        return steps;
    }
}
