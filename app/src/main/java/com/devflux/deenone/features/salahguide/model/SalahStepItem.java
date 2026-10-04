package com.devflux.deenone.features.salahguide.model;

import java.io.Serializable;

public class SalahStepItem implements Serializable {
    private final String stepNumber;
    private final String stepTitle;
    private final String instruction;
    private final String arabicText;
    private final String transliteration;
    private final String translation;
    private final String reference;

    public SalahStepItem(String stepNumber, String stepTitle, String instruction,
                         String arabicText, String transliteration, String translation,
                         String reference) {
        this.stepNumber = stepNumber;
        this.stepTitle = stepTitle;
        this.instruction = instruction;
        this.arabicText = arabicText;
        this.transliteration = transliteration;
        this.translation = translation;
        this.reference = reference;
    }

    public String getStepNumber() {
        return stepNumber;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getArabicText() {
        return arabicText;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getTranslation() {
        return translation;
    }

    public String getReference() {
        return reference;
    }

    public boolean hasArabic() {
        return arabicText != null && !arabicText.trim().isEmpty();
    }
}
