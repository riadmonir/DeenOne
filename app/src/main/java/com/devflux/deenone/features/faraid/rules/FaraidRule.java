package com.devflux.deenone.features.faraid.rules;

import com.devflux.deenone.features.faraid.engine.FaraidFraction;
import com.devflux.deenone.features.faraid.model.FaraidHeir;
import com.devflux.deenone.features.faraid.model.FaraidInput;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Structured Inheritance Rule Entity (Requirement 29).
 */
public class FaraidRule implements Serializable {

    public enum MadhabScope {
        ALL,
        HANAFI,
        SHAFI,
        MALIKI,
        HANBALI
    }

    public enum Madhhab {
        ALL,
        HANAFI,
        SHAFI,
        MALIKI,
        HANBALI
    }

    public enum ShareType {
        FIXED,
        RESIDUE,
        FIXED_AND_RESIDUE,
        BLOCKED
    }

    private final String ruleId;
    private final String heirType;
    private final String conditionDescription;
    private final List<String> blockedBy;
    private final FaraidFraction share;
    private final FaraidHeir.ShareType shareType;
    private final MadhabScope madhhab;
    private final int priority;
    private final List<String> quranReferences;
    private final List<String> hadithReferences;
    private final String explanationBn;
    private final String explanationEn;

    public FaraidRule(String ruleId, String heirType, String conditionDescription,
                      List<String> blockedBy, FaraidFraction share,
                      ShareType shareType, Madhhab madhhab, int priority,
                      List<String> quranReferences, List<String> hadithReferences,
                      String explanationBn, String explanationEn) {
        this.ruleId = ruleId;
        this.heirType = heirType;
        this.conditionDescription = conditionDescription;
        this.blockedBy = blockedBy != null ? Collections.unmodifiableList(new ArrayList<>(blockedBy)) : Collections.emptyList();
        this.share = share != null ? share : FaraidFraction.ZERO;
        this.shareType = (shareType == ShareType.RESIDUE) ? FaraidHeir.ShareType.ASABAH : FaraidHeir.ShareType.ZAWIL_FURUD;
        this.madhhab = madhhab != null ? MadhabScope.valueOf(madhhab.name()) : MadhabScope.ALL;
        this.priority = priority;
        this.quranReferences = quranReferences != null ? Collections.unmodifiableList(new ArrayList<>(quranReferences)) : Collections.emptyList();
        this.hadithReferences = hadithReferences != null ? Collections.unmodifiableList(new ArrayList<>(hadithReferences)) : Collections.emptyList();
        this.explanationBn = explanationBn;
        this.explanationEn = explanationEn;
    }

    public FaraidRule(String ruleId, String heirType, String conditionDescription,
                      List<String> blockedBy, FaraidFraction share,
                      FaraidHeir.ShareType shareType, MadhabScope madhhab, int priority,
                      List<String> quranReferences, List<String> hadithReferences,
                      String explanationBn, String explanationEn) {
        this.ruleId = ruleId;
        this.heirType = heirType;
        this.conditionDescription = conditionDescription;
        this.blockedBy = blockedBy != null ? Collections.unmodifiableList(new ArrayList<>(blockedBy)) : Collections.emptyList();
        this.share = share != null ? share : FaraidFraction.ZERO;
        this.shareType = shareType;
        this.madhhab = madhhab != null ? madhhab : MadhabScope.ALL;
        this.priority = priority;
        this.quranReferences = quranReferences != null ? Collections.unmodifiableList(new ArrayList<>(quranReferences)) : Collections.emptyList();
        this.hadithReferences = hadithReferences != null ? Collections.unmodifiableList(new ArrayList<>(hadithReferences)) : Collections.emptyList();
        this.explanationBn = explanationBn;
        this.explanationEn = explanationEn;
    }

    public String getRuleId() { return ruleId; }
    public String getHeirType() { return heirType; }
    public String getConditionDescription() { return conditionDescription; }
    public List<String> getBlockedBy() { return blockedBy; }
    public FaraidFraction getShare() { return share; }
    public FaraidHeir.ShareType getShareType() { return shareType; }
    public MadhabScope getMadhhab() { return madhhab; }
    public int getPriority() { return priority; }
    public List<String> getQuranReferences() { return quranReferences; }
    public List<String> getHadithReferences() { return hadithReferences; }
    public String getExplanationBn() { return explanationBn; }
    public String getExplanationEn() { return explanationEn; }

    public boolean appliesToMadhab(FaraidInput.Madhab targetMadhab) {
        if (this.madhhab == MadhabScope.ALL) return true;
        if (targetMadhab == null) return true;
        return this.madhhab.name().equalsIgnoreCase(targetMadhab.name());
    }
}