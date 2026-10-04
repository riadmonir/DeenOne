package com.devflux.deenone.features.salahguide.model;

public class GenericSalahSection {
    private final String title;
    private final String arabic;
    private final String pronunciation;
    private final String translation;
    private final String description;
    private final String reference;

    public GenericSalahSection(String title, String description, String reference) {
        this(title, "", "", "", description, reference);
    }

    public GenericSalahSection(String title, String arabic, String translation, String description, String reference) {
        this(title, arabic, "", translation, description, reference);
    }

    public GenericSalahSection(String title, String arabic, String pronunciation, String translation, String description, String reference) {
        this.title = title != null ? title : "";
        this.arabic = arabic != null ? arabic : "";
        this.pronunciation = pronunciation != null ? pronunciation : "";
        this.translation = translation != null ? translation : "";
        this.description = description != null ? description : "";
        this.reference = reference != null ? reference : "";
    }

    public String getTitle() { return title; }
    public String getArabic() { return arabic; }
    public String getPronunciation() { return pronunciation; }
    public String getTranslation() { return translation; }
    public String getDescription() { return description; }
    public String getReference() { return reference; }
}
