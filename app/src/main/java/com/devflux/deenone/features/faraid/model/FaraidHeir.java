package com.devflux.deenone.features.faraid.model;

import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.utils.BengaliNumberUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Formal Heir Model for Classical Sunni Islamic Inheritance (Faraid) Engine.
 * Represents all attributes of a potential or actual heir, with strictly exact
 * rational arithmetic (FaraidFraction) to prevent floating-point inaccuracies.
 */
public class FaraidHeir implements Serializable {

    public enum ShareType {
        FIXED("fixed", "আসহাবুল ফুরুজ"),
        RESIDUE("residue", "আসাবা"),
        FIXED_AND_RESIDUE("fixed_and_residue", "নির্দিষ্ট অংশ ও আসাবা উভয়টি"),
        BLOCKED("blocked", "মাহজুব"),
        ZAWIL_FURUD("fixed", "আসহাবুল ফুরুজ"),
        ASABAH("residue", "আসাবা");

        private final String code;
        private final String displayNameBn;

        ShareType(String code, String displayNameBn) {
            this.code = code;
            this.displayNameBn = displayNameBn;
        }

        public String getCode() { return code; }
        public String getDisplayNameBn() { return displayNameBn; }
    }

    private final String id;
    private final String relationship;
    private final String relationshipBn;
    private final FaraidInput.Gender gender;
    private final int quantity;
    private final boolean alive;
    private final boolean eligible;
    private final boolean blocked;
    private final String blockingReason;
    private final ShareType shareType;
    private final FaraidFraction fixedFraction;
    private final boolean residueEligible;
    private final FaraidFraction finalFraction;
    private final double finalAmount;
    private final double individualAmount;
    private final String explanation;
    private final List<String> evidenceReferences;
    private final String arabicDalil;
    private final String banglaPronunciation;
    private final String banglaTranslation;
    private final String referenceSource;
    private final String madhabConsensusBn;
    private final String bangladeshLawNoteBn;

    public FaraidHeir(String id, String relationship, String relationshipBn,
                      FaraidInput.Gender gender, int quantity, boolean alive,
                      boolean eligible, boolean blocked, String blockingReason,
                      ShareType shareType, FaraidFraction fixedFraction,
                      boolean residueEligible, FaraidFraction finalFraction,
                      double finalAmount, double individualAmount,
                      String explanation,
                      List<String> evidenceReferences, String arabicDalil,
                      String banglaPronunciation,
                      String banglaTranslation, String referenceSource,
                      String madhabConsensusBn, String bangladeshLawNoteBn) {
        this.id = id;
        this.relationship = relationship;
        this.relationshipBn = relationshipBn;
        this.gender = gender;
        this.quantity = quantity;
        this.alive = alive;
        this.eligible = eligible;
        this.blocked = blocked;
        this.blockingReason = (blockingReason != null) ? blockingReason : "";
        this.shareType = (shareType != null) ? shareType : ShareType.BLOCKED;
        this.fixedFraction = (fixedFraction != null) ? fixedFraction : FaraidFraction.ZERO;
        this.residueEligible = residueEligible;
        this.finalFraction = (finalFraction != null) ? finalFraction : FaraidFraction.ZERO;
        this.finalAmount = Math.max(0.0, finalAmount);
        this.individualAmount = Math.max(0.0, individualAmount);
        this.explanation = (explanation != null) ? explanation : "";
        this.evidenceReferences = (evidenceReferences != null) ? evidenceReferences : Collections.emptyList();
        this.arabicDalil = (arabicDalil != null) ? arabicDalil : "";
        this.banglaPronunciation = (banglaPronunciation != null) ? banglaPronunciation : "";
        this.banglaTranslation = (banglaTranslation != null) ? banglaTranslation : "";
        this.referenceSource = (referenceSource != null) ? referenceSource : "";
        this.madhabConsensusBn = (madhabConsensusBn != null) ? madhabConsensusBn : "";
        this.bangladeshLawNoteBn = (bangladeshLawNoteBn != null) ? bangladeshLawNoteBn : "";
    }

    public FaraidHeir(String id, String relationship, String relationshipBn,
                      FaraidInput.Gender gender, int quantity, boolean alive,
                      boolean eligible, boolean blocked, String blockingReason,
                      ShareType shareType, FaraidFraction fixedFraction,
                      boolean residueEligible, FaraidFraction finalFraction,
                      double finalAmount, double individualAmount,
                      String explanation,
                      List<String> evidenceReferences, String arabicDalil,
                      String banglaTranslation, String referenceSource,
                      String madhabConsensusBn, String bangladeshLawNoteBn) {
        this(id, relationship, relationshipBn, gender, quantity, alive, eligible, blocked, blockingReason,
                shareType, fixedFraction, residueEligible, finalFraction, finalAmount, individualAmount,
                explanation, evidenceReferences, arabicDalil, "", banglaTranslation, referenceSource,
                madhabConsensusBn, bangladeshLawNoteBn);
    }

    public String getId() { return id; }
    public String getRelationship() { return relationship; }
    public String getRelationshipBn() { return relationshipBn; }
    public FaraidInput.Gender getGender() { return gender; }
    public int getQuantity() { return quantity; }
    public boolean isAlive() { return alive; }
    public boolean isEligible() { return eligible; }
    public boolean isBlocked() { return blocked; }
    public String getBlockingReason() { return blockingReason; }
    public ShareType getShareType() { return shareType; }
    public FaraidFraction getFixedFraction() { return fixedFraction; }
    public boolean isResidueEligible() { return residueEligible; }
    public FaraidFraction getFinalFraction() { return finalFraction; }
    public double getFinalAmount() { return finalAmount; }
    public double getIndividualAmount() { return individualAmount; }
    public String getExplanation() { return explanation; }
    public String getJustification() { return explanation; }
    public List<String> getEvidenceReferences() { return evidenceReferences; }
    public String getArabicDalil() { return arabicDalil; }
    public String getBanglaPronunciation() { return banglaPronunciation; }
    public String getBanglaTranslation() { return banglaTranslation; }
    public String getReferenceSource() { return referenceSource; }
    public String getMadhabConsensusBn() { return madhabConsensusBn; }
    public String getBangladeshLawNoteBn() { return bangladeshLawNoteBn; }

    public double getPercentage() {
        return finalFraction.toDouble() * 100.0;
    }

    public String getFixedFractionString() {
        return fixedFraction.toString();
    }

    public String getFinalFractionString() {
        return finalFraction.toString();
    }

    public String getFinalFractionBengaliString() {
        return finalFraction.toBengaliString();
    }

    public FaraidFraction getIndividualFraction() {
        return finalFraction.divide(Math.max(1, quantity));
    }

    public String getIndividualFractionString() {
        return getIndividualFraction().toString();
    }

    public String getIndividualFractionBengaliString() {
        return getIndividualFraction().toBengaliString();
    }

    @Override
    public String toString() {
        return "FaraidHeir{" +
                "id='" + id + '\'' +
                ", relationship='" + relationship + '\'' +
                ", eligible=" + eligible +
                ", blocked=" + blocked +
                ", shareType=" + shareType.getCode() +
                ", fixedFraction=" + fixedFraction +
                ", finalFraction=" + finalFraction +
                ", finalAmount=" + finalAmount +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
