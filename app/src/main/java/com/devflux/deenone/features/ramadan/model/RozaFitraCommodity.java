package com.devflux.deenone.features.ramadan.model;

/**
 * Model representing a commodity used for calculating Sadaqatul Fitr.
 */
public class RozaFitraCommodity {
    private final String id;
    private final String nameBn;
    private final String nameEn;
    private final double weightKg;
    private final String weightDescriptionBn;
    private final String weightDescriptionEn;
    private double pricePerKg;

    public RozaFitraCommodity(String id, String nameBn, String nameEn, double weightKg,
                              String weightDescriptionBn, String weightDescriptionEn, double pricePerKg) {
        this.id = id;
        this.nameBn = nameBn;
        this.nameEn = nameEn;
        this.weightKg = weightKg;
        this.weightDescriptionBn = weightDescriptionBn;
        this.weightDescriptionEn = weightDescriptionEn;
        this.pricePerKg = pricePerKg;
    }

    public String getId() {
        return id;
    }

    public String getName(boolean isBn) {
        return isBn ? nameBn : nameEn;
    }

    public String getNameBn() {
        return nameBn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public String getWeightDescription(boolean isBn) {
        return isBn ? weightDescriptionBn : weightDescriptionEn;
    }

    public String getWeightDescriptionBn() {
        return weightDescriptionBn;
    }

    public String getWeightDescriptionEn() {
        return weightDescriptionEn;
    }

    public double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public long calculatePerPersonRate() {
        return Math.round(weightKg * pricePerKg);
    }
}
