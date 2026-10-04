package com.devflux.deenone.features.blood.model;

public class BloodOrganizationModel {
  private final int id;
  private final String nameBn;
  private final String nameEn;
  private final String category;
  private final String district;
  private final String address;
  private final String hotlinePhone;
  private final String altPhone;
  private final String website;
  private final boolean isVerified;

  public BloodOrganizationModel(int id, String nameBn, String nameEn, String category, String district, String address, String hotlinePhone, String altPhone, String website, boolean isVerified) {
    this.id = id;
    this.nameBn = nameBn;
    this.nameEn = nameEn;
    this.category = category;
    this.district = district;
    this.address = address;
    this.hotlinePhone = hotlinePhone;
    this.altPhone = altPhone;
    this.website = website;
    this.isVerified = isVerified;
  }

  public int getId() { return id; }
  public String getNameBn() { return nameBn; }
  public String getNameEn() { return nameEn; }
  public String getCategory() { return category; }
  public String getDistrict() { return district; }
  public String getAddress() { return address; }
  public String getHotlinePhone() { return hotlinePhone; }
  public String getAltPhone() { return altPhone; }
  public String getWebsite() { return website; }
  public boolean isVerified() { return isVerified; }
}
