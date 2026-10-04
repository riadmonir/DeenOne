package com.devflux.deenone.features.hadith.model;

import com.devflux.deenone.utils.BengaliNumberUtil;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class HadithBookCategory implements Serializable {

    @SerializedName("slug")
    private String slug;

    @SerializedName("name_bn")
    private String nameBn;

    @SerializedName("name_en")
    private String nameEn;

    @SerializedName("author_bn")
    private String authorBn;

    @SerializedName("author_en")
    private String authorEn;

    @SerializedName("initials")
    private String initials;

    @SerializedName("color_hex")
    private String colorHex;

    @SerializedName("total_hadith")
    private int totalHadith;

    @SerializedName("display_order")
    private int displayOrder;

    public HadithBookCategory() {}

    public HadithBookCategory(String slug, String nameBn, String nameEn, String authorBn, String authorEn,
                              String initials, String colorHex, int totalHadith, int displayOrder) {
        this.slug = slug;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.authorBn = authorBn;
        this.authorEn = authorEn;
        this.initials = initials;
        this.colorHex = colorHex;
        this.totalHadith = totalHadith;
        this.displayOrder = displayOrder;
    }

    public String getSlug() {
        return slug != null ? slug : "";
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNameBn() {
        return nameBn != null ? nameBn : "";
    }

    public void setNameBn(String nameBn) {
        this.nameBn = nameBn;
    }

    public String getNameEn() {
        return nameEn != null ? nameEn : "";
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAuthorBn() {
        return authorBn != null ? authorBn : "";
    }

    public void setAuthorBn(String authorBn) {
        this.authorBn = authorBn;
    }

    public String getAuthorEn() {
        return authorEn != null ? authorEn : "";
    }

    public void setAuthorEn(String authorEn) {
        this.authorEn = authorEn;
    }

    public String getInitials() {
        return initials != null && !initials.trim().isEmpty() ? initials : "H";
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getColorHex() {
        return colorHex != null && !colorHex.trim().isEmpty() ? colorHex : "#10B981";
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public int getTotalHadith() {
        return totalHadith;
    }

    public void setTotalHadith(int totalHadith) {
        this.totalHadith = totalHadith;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDisplayName(boolean isBn) {
        if (isBn) {
            return nameBn != null && !nameBn.isEmpty() ? nameBn : (nameEn != null ? nameEn : "হাদিস গ্রন্থ");
        } else {
            return nameEn != null && !nameEn.isEmpty() ? nameEn : (nameBn != null ? nameBn : "Hadith Book");
        }
    }

    public String getDisplayAuthor(boolean isBn) {
        if (isBn) {
            return authorBn != null && !authorBn.isEmpty() ? authorBn : (authorEn != null ? authorEn : "");
        } else {
            return authorEn != null && !authorEn.isEmpty() ? authorEn : (authorBn != null ? authorBn : "");
        }
    }

    public String getFormattedCount(boolean isBn) {
        if (isBn) {
            return BengaliNumberUtil.toBengali(String.valueOf(totalHadith));
        } else {
            return String.valueOf(totalHadith);
        }
    }
}
