package com.devflux.deenone.core.quran;

public enum BanglaQuranTranslator {
    MUHIUDDIN_KHAN(
            "muhiuddin",
            "মাওলানা মুহিউদ্দীন খান",
            "Maulana Muhiuddin Khan",
            "bn.bengali",
            "মুহিউদ্দীন খান",
            "Muhiuddin Khan",
            "Tanzil Project (tanzil.net) • সর্বাধিক জনপ্রিয় ও প্রামাণ্য বাংলা অনুবাদ",
            "Tanzil Project (tanzil.net) • Most widely read authentic Bengali translation"
    ),
    ZOHURUL_HOQUE(
            "zohurul",
            "ড. জহুরুল হক",
            "Dr. Zohurul Hoque",
            "bn.hoque",
            "জহুরুল হক",
            "Zohurul Hoque",
            "Tanzil Project (tanzil.net) • প্রাঞ্জল ও বিশদ ব্যাখ্যাসমৃদ্ধ বাংলা অনুবাদ",
            "Tanzil Project (tanzil.net) • Fluent and detailed contextual Bengali translation"
    );

    public final String id;
    public final String fullNameBn;
    public final String fullNameEn;
    public final String editionCode;
    public final String shortNameBn;
    public final String shortNameEn;
    public final String descriptionBn;
    public final String descriptionEn;

    BanglaQuranTranslator(String id, String fullNameBn, String fullNameEn, String editionCode,
                          String shortNameBn, String shortNameEn, String descriptionBn, String descriptionEn) {
        this.id = id;
        this.fullNameBn = fullNameBn;
        this.fullNameEn = fullNameEn;
        this.editionCode = editionCode;
        this.shortNameBn = shortNameBn;
        this.shortNameEn = shortNameEn;
        this.descriptionBn = descriptionBn;
        this.descriptionEn = descriptionEn;
    }

    public static BanglaQuranTranslator fromId(String id) {
        if (id != null) {
            for (BanglaQuranTranslator t : values()) {
                if (t.id.equalsIgnoreCase(id) || t.editionCode.equalsIgnoreCase(id)) {
                    return t;
                }
            }
        }
        return MUHIUDDIN_KHAN;
    }

    public String getDisplayName(boolean isBn) {
        return isBn ? fullNameBn : fullNameEn;
    }

    public String getShortName(boolean isBn) {
        return isBn ? shortNameBn : shortNameEn;
    }

    public String getDescription(boolean isBn) {
        return isBn ? descriptionBn : descriptionEn;
    }
}
