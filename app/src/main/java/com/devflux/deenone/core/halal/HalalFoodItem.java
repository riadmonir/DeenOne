package com.devflux.deenone.core.halal;

import org.json.JSONObject;

public class HalalFoodItem {

    private String id;
    private String title;
    private String titleEn;
    private String arabicName;
    private String eCode;
    private String category; // SUNNAH_FOOD, HARAM_FOOD, E_CODE, PRINCIPLES, DAILY_FOOD, BEVERAGE, COSMETICS
    private String status;   // HALAL, HARAM, MUSHBOOH
    private String scientificName;
    private String scientificNameEn;
    private String sourceOrigin;
    private String sourceOriginEn;
    private String imageUrl;
    private String nutritionBenefits;
    private String nutritionBenefitsEn;
    private String howToConsume;
    private String howToConsumeEn;
    private String description;
    private String descriptionEn;
    private String reference;
    private String referenceEn;
    private String madhhabNote;
    private String madhhabNoteEn;
    private String halalAlternative;
    private String halalAlternativeEn;

    public HalalFoodItem(String id, String title, String titleEn, String arabicName, String eCode,
                         String category, String status, String scientificName, String scientificNameEn,
                         String sourceOrigin, String sourceOriginEn, String imageUrl,
                         String nutritionBenefits, String nutritionBenefitsEn,
                         String howToConsume, String howToConsumeEn,
                         String description, String descriptionEn,
                         String reference, String referenceEn,
                         String madhhabNote, String madhhabNoteEn,
                         String halalAlternative, String halalAlternativeEn) {
        this.id = id;
        this.title = title;
        this.titleEn = titleEn;
        this.arabicName = arabicName;
        this.eCode = eCode;
        this.category = category;
        this.status = status;
        this.scientificName = scientificName;
        this.scientificNameEn = scientificNameEn;
        this.sourceOrigin = sourceOrigin;
        this.sourceOriginEn = sourceOriginEn;
        this.imageUrl = imageUrl;
        this.nutritionBenefits = nutritionBenefits;
        this.nutritionBenefitsEn = nutritionBenefitsEn;
        this.howToConsume = howToConsume;
        this.howToConsumeEn = howToConsumeEn;
        this.description = description;
        this.descriptionEn = descriptionEn;
        this.reference = reference;
        this.referenceEn = referenceEn;
        this.madhhabNote = madhhabNote;
        this.madhhabNoteEn = madhhabNoteEn;
        this.halalAlternative = halalAlternative;
        this.halalAlternativeEn = halalAlternativeEn;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getTitleEn() { return titleEn; }
    public String getArabicName() { return arabicName; }
    public String getECode() { return eCode; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getScientificName() { return scientificName; }
    public String getScientificNameEn() { return scientificNameEn; }
    public String getSourceOrigin() { return sourceOrigin; }
    public String getSourceOriginEn() { return sourceOriginEn; }
    public String getImageUrl() { return imageUrl; }
    public String getNutritionBenefits() { return nutritionBenefits; }
    public String getNutritionBenefitsEn() { return nutritionBenefitsEn; }
    public String getHowToConsume() { return howToConsume; }
    public String getHowToConsumeEn() { return howToConsumeEn; }
    public String getDescription() { return description; }
    public String getDescriptionEn() { return descriptionEn; }
    public String getReference() { return reference; }
    public String getReferenceEn() { return referenceEn; }
    public String getMadhhabNote() { return madhhabNote; }
    public String getMadhhabNoteEn() { return madhhabNoteEn; }
    public String getHalalAlternative() { return halalAlternative; }
    public String getHalalAlternativeEn() { return halalAlternativeEn; }

    public String getTitle(boolean isBn) {
        if (isBn) {
            if (title != null && !title.trim().isEmpty()) {
                return cleanTitle(title, true);
            }
            return titleEn != null ? titleEn : "";
        } else {
            if (titleEn != null && !titleEn.trim().isEmpty()) {
                return titleEn;
            }
            return cleanTitle(title, false);
        }
    }

    public String getScientificName(boolean isBn) {
        if (isBn) {
            return scientificName != null && !scientificName.trim().isEmpty() ? scientificName : scientificNameEn;
        } else {
            return scientificNameEn != null && !scientificNameEn.trim().isEmpty() ? scientificNameEn : scientificName;
        }
    }

    public String getSourceOrigin(boolean isBn) {
        if (isBn) {
            return sourceOrigin != null && !sourceOrigin.trim().isEmpty() ? sourceOrigin : sourceOriginEn;
        } else {
            return sourceOriginEn != null && !sourceOriginEn.trim().isEmpty() ? sourceOriginEn : sourceOrigin;
        }
    }

    public String getNutritionBenefits(boolean isBn) {
        if (isBn) {
            return nutritionBenefits != null && !nutritionBenefits.trim().isEmpty() ? nutritionBenefits : nutritionBenefitsEn;
        } else {
            return nutritionBenefitsEn != null && !nutritionBenefitsEn.trim().isEmpty() ? nutritionBenefitsEn : nutritionBenefits;
        }
    }

    public String getHowToConsume(boolean isBn) {
        if (isBn) {
            return howToConsume != null && !howToConsume.trim().isEmpty() ? howToConsume : howToConsumeEn;
        } else {
            return howToConsumeEn != null && !howToConsumeEn.trim().isEmpty() ? howToConsumeEn : howToConsume;
        }
    }

    public String getDescription(boolean isBn) {
        if (isBn) {
            return description != null && !description.trim().isEmpty() ? description : descriptionEn;
        } else {
            return descriptionEn != null && !descriptionEn.trim().isEmpty() ? descriptionEn : description;
        }
    }

    public String getReference(boolean isBn) {
        if (isBn) {
            return reference != null && !reference.trim().isEmpty() ? reference : referenceEn;
        } else {
            return referenceEn != null && !referenceEn.trim().isEmpty() ? referenceEn : reference;
        }
    }

    public String getMadhhabNote(boolean isBn) {
        if (isBn) {
            return madhhabNote != null && !madhhabNote.trim().isEmpty() ? madhhabNote : madhhabNoteEn;
        } else {
            return madhhabNoteEn != null && !madhhabNoteEn.trim().isEmpty() ? madhhabNoteEn : madhhabNote;
        }
    }

    public String getHalalAlternative(boolean isBn) {
        if (isBn) {
            return halalAlternative != null && !halalAlternative.trim().isEmpty() ? halalAlternative : halalAlternativeEn;
        } else {
            return halalAlternativeEn != null && !halalAlternativeEn.trim().isEmpty() ? halalAlternativeEn : halalAlternative;
        }
    }

    public static String cleanTitle(String rawTitle, boolean isBn) {
        if (rawTitle == null || rawTitle.trim().isEmpty()) return "";
        if (isBn) {
            String cleaned = rawTitle.replaceAll("\\s*\\([A-Za-z0-9\\s/\\-_,\\.&]+\\)", "").trim();
            return cleaned.isEmpty() ? rawTitle : cleaned;
        } else {
            int openParen = rawTitle.indexOf('(');
            int closeParen = rawTitle.lastIndexOf(')');
            if (openParen != -1 && closeParen > openParen) {
                String candidate = rawTitle.substring(openParen + 1, closeParen).trim();
                if (candidate.matches(".*[a-zA-Z]+.*")) {
                    return candidate;
                }
            }
            return rawTitle;
        }
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("item_key", id);
            obj.put("title", title);
            obj.put("title_en", titleEn);
            obj.put("arabic_name", arabicName);
            obj.put("e_code", eCode);
            obj.put("category", category);
            obj.put("status", status);
            obj.put("scientific_name", scientificName);
            obj.put("scientific_name_en", scientificNameEn);
            obj.put("source_origin", sourceOrigin);
            obj.put("source_origin_en", sourceOriginEn);
            obj.put("image_url", imageUrl);
            obj.put("nutrition_benefits", nutritionBenefits);
            obj.put("nutrition_benefits_en", nutritionBenefitsEn);
            obj.put("usage_instructions", howToConsume);
            obj.put("usage_instructions_en", howToConsumeEn);
            obj.put("description", description);
            obj.put("description_en", descriptionEn);
            obj.put("hadith_ref", reference);
            obj.put("hadith_ref_en", referenceEn);
            obj.put("fiqh_ruling", madhhabNote);
            obj.put("fiqh_ruling_en", madhhabNoteEn);
            obj.put("halal_alternative", halalAlternative);
            obj.put("halal_alternative_en", halalAlternativeEn);
        } catch (Exception ignored) {}
        return obj;
    }

    public static HalalFoodItem fromJson(JSONObject obj) {
        if (obj == null) return null;

        String id = obj.optString("item_key", obj.optString("id", ""));
        String title = obj.optString("title", "");
        String titleEn = obj.optString("title_en", obj.optString("titleEn", ""));
        String arabic = obj.optString("arabic_name", obj.optString("arabicName", ""));
        String eCode = obj.optString("e_code", obj.optString("eCode", ""));
        String cat = obj.optString("category", "DAILY_FOOD");
        String status = obj.optString("status", "HALAL");
        String sci = obj.optString("scientific_name", obj.optString("scientificName", ""));
        String sciEn = obj.optString("scientific_name_en", obj.optString("scientificNameEn", ""));
        String source = obj.optString("source_origin", obj.optString("sourceOrigin", ""));
        String sourceEn = obj.optString("source_origin_en", obj.optString("sourceOriginEn", ""));
        String img = obj.optString("image_url", obj.optString("imageUrl", ""));
        String benefits = obj.optString("nutrition_benefits", obj.optString("nutritionBenefits", ""));
        String benefitsEn = obj.optString("nutrition_benefits_en", obj.optString("nutritionBenefitsEn", ""));
        String usage = obj.optString("usage_instructions", obj.optString("howToConsume", ""));
        String usageEn = obj.optString("usage_instructions_en", obj.optString("howToConsumeEn", ""));
        String desc = obj.optString("description", obj.optString("description", ""));
        String descEn = obj.optString("description_en", obj.optString("descriptionEn", ""));
        String ref = obj.optString("hadith_ref", obj.optString("reference", ""));
        String refEn = obj.optString("hadith_ref_en", obj.optString("referenceEn", ""));
        String madhhab = obj.optString("fiqh_ruling", obj.optString("madhhabNote", ""));
        String madhhabEn = obj.optString("fiqh_ruling_en", obj.optString("madhhabNoteEn", ""));
        String alt = obj.optString("halal_alternative", obj.optString("halalAlternative", ""));
        String altEn = obj.optString("halal_alternative_en", obj.optString("halalAlternativeEn", ""));

        return new HalalFoodItem(
                id, title, titleEn, arabic, eCode, cat, status,
                sci, sciEn, source, sourceEn, img,
                benefits, benefitsEn, usage, usageEn,
                desc, descEn, ref, refEn,
                madhhab, madhhabEn, alt, altEn
        );
    }
}