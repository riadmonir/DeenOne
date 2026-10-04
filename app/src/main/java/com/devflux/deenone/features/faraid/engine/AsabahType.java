package com.devflux.deenone.features.faraid.engine;

/**
 * Classical Sunni Asabah (Residuary) Classification.
 * Supported per Requirement 16 & Sahih al-Bukhari 6732/6737, 6742; Sahih Muslim 1615a:
 * - Asabah bi-Nafsihi (عصبة بنفسه - Residuary in his own right)
 * - Asabah bil-Ghayr (عصبة بالغير - Residuary by another, 2:1 gender split)
 * - Asabah ma'al-Ghayr (عصبة مع الغير - Residuary with another, sisters with daughters)
 */
public enum AsabahType {
    BI_NAFSIHI("bi_nafsihi", "স্বীয় অধিকারে আসাবা (عصبة بنفسه)"),
    BIL_GHAYR("bil_ghayr", "অন্যের মাধ্যমে আসাবা ২:১ (عصبة بالغير)"),
    MA_AL_GHAYR("ma_al_ghayr", "কন্যাদের উপস্থিতিতে আসাবা (عصبة مع الغير)"),
    NONE("none", "আসাবা নন");

    private final String code;
    private final String displayNameBn;

    AsabahType(String code, String displayNameBn) {
        this.code = code;
        this.displayNameBn = displayNameBn;
    }

    public String getCode() { return code; }
    public String getDisplayNameBn() { return displayNameBn; }
}