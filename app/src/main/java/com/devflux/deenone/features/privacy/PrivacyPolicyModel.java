package com.devflux.deenone.features.privacy;

import org.json.JSONObject;

public class PrivacyPolicyModel {
    public String titleBn;
    public String titleEn;
    public String updatedAtBn;
    public String updatedAtEn;
    public String contentBn;
    public String contentEn;
    public String contactEmail;
    public String organization;
    public String privacyUrl;
    public String dataDeletionInfo;

    public PrivacyPolicyModel() {
        this.titleBn = "প্রাইভেসি পলিসি";
        this.titleEn = "PRIVACY POLICY";
        this.updatedAtBn = "সর্বশেষ হালনাগাদ: ২০২৬";
        this.updatedAtEn = "Last update: September 2026";
        this.contactEmail = "privacy@deenone.top";
        this.organization = "DeenOne Technologies & Foundation";
        this.privacyUrl = "https://deenone.top/privacy";
        this.dataDeletionInfo = "ব্যবহারকারী অ্যাপের সেটিংস থেকে অথবা privacy@deenone.top এ ইমেইল পাঠিয়ে যেকোনো সময় তাদের অ্যাকাউন্ট ও ক্লাউডে সংরক্ষিত সমস্ত তথ্য স্থায়ীভাবে মুছে ফেলার আবেদন করতে পারেন।";
    }

    public static PrivacyPolicyModel fromJson(JSONObject json) {
        if (json == null) return null;
        PrivacyPolicyModel model = new PrivacyPolicyModel();
        model.titleBn = json.optString("title_bn", "প্রাইভেসি পলিসি");
        model.titleEn = json.optString("title_en", "PRIVACY POLICY");
        model.updatedAtBn = json.optString("updated_at_bn", "সর্বশেষ হালনাগাদ: ২০২৬");
        model.updatedAtEn = json.optString("updated_at_en", "Last update: September 2026");
        model.contentBn = json.optString("content_bn", json.optString("privacy_policy_bn", ""));
        model.contentEn = json.optString("content_en", json.optString("privacy_policy_en", ""));
        model.contactEmail = json.optString("contact_email", "privacy@deenone.top");
        model.organization = json.optString("organization", "DeenOne Technologies & Foundation");
        model.privacyUrl = json.optString("privacy_url", "https://deenone.top/privacy");
        model.dataDeletionInfo = json.optString("data_deletion_info", "");
        return model;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("title_bn", titleBn);
            json.put("title_en", titleEn);
            json.put("updated_at_bn", updatedAtBn);
            json.put("updated_at_en", updatedAtEn);
            json.put("content_bn", contentBn);
            json.put("content_en", contentEn);
            json.put("contact_email", contactEmail);
            json.put("organization", organization);
            json.put("privacy_url", privacyUrl);
            json.put("data_deletion_info", dataDeletionInfo);
        } catch (Exception ignored) {}
        return json;
    }
}
