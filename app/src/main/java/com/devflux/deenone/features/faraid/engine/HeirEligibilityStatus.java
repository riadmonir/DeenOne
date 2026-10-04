package com.devflux.deenone.features.faraid.engine;

/**
 * Standardized 5-State Heir Eligibility Status for Classical Sunni Faraid Engine.
 * Requirement 15: For every entered heir, return one of:
 * - ELIGIBLE_FIXED
 * - ELIGIBLE_RESIDUARY
 * - ELIGIBLE_FIXED_AND_RESIDUARY
 * - BLOCKED
 * - NOT_APPLICABLE
 */
public enum HeirEligibilityStatus {
    ELIGIBLE_FIXED("eligible_fixed", "নির্দিষ্ট অংশীদার (আসহাবুল ফুরুজ)"),
    ELIGIBLE_RESIDUARY("eligible_residuary", "অবশিষ্টাংশভোগী (আসাবা)"),
    ELIGIBLE_FIXED_AND_RESIDUARY("eligible_fixed_and_residuary", "নির্দিষ্ট অংশ ও অবশিষ্টাংশ উভয়টি"),
    BLOCKED("blocked", "বঞ্চিত / বাদ পড়েছেন (মাহজুব)"),
    NOT_APPLICABLE("not_applicable", "প্রযোজ্য নয় / অন্তর্ভুক্ত নন");

    private final String code;
    private final String displayNameBn;

    HeirEligibilityStatus(String code, String displayNameBn) {
        this.code = code;
        this.displayNameBn = displayNameBn;
    }

    public String getCode() { return code; }
    public String getDisplayNameBn() { return displayNameBn; }
}