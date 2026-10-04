package com.devflux.deenone.features.faraid.model;

import com.devflux.deenone.features.faraid.engine.HeirEligibilityStatus;

import java.io.Serializable;

/**
 * Represents the detailed Hajb (Blocking) decision for an individual heir category.
 * Requirement 15: Returns one of the 5 statuses with explicit cause and reason.
 */
public class HeirHajbDecision implements Serializable {

    private final String heirKey;
    private final String heirTitleBn;
    private final int count;
    private final HeirEligibilityStatus status;
    private final String blockedByTitleBn;
    private final String reasonEn;
    private final String reasonBn;
    private final String shariahReference;

    public HeirHajbDecision(String heirKey, String heirTitleBn, int count,
                            HeirEligibilityStatus status, String blockedByTitleBn,
                            String reasonEn, String reasonBn, String shariahReference) {
        this.heirKey = heirKey;
        this.heirTitleBn = heirTitleBn;
        this.count = count;
        this.status = status != null ? status : HeirEligibilityStatus.NOT_APPLICABLE;
        this.blockedByTitleBn = blockedByTitleBn != null ? blockedByTitleBn : "";
        this.reasonEn = reasonEn != null ? reasonEn : "";
        this.reasonBn = reasonBn != null ? reasonBn : "";
        this.shariahReference = shariahReference != null ? shariahReference : "";
    }

    public String getHeirKey() { return heirKey; }
    public String getHeirTitleBn() { return heirTitleBn; }
    public int getCount() { return count; }
    public HeirEligibilityStatus getStatus() { return status; }
    public boolean isBlocked() { return status == HeirEligibilityStatus.BLOCKED; }
    public boolean isEligible() {
        return status == HeirEligibilityStatus.ELIGIBLE_FIXED ||
               status == HeirEligibilityStatus.ELIGIBLE_RESIDUARY ||
               status == HeirEligibilityStatus.ELIGIBLE_FIXED_AND_RESIDUARY;
    }
    public String getBlockedByTitleBn() { return blockedByTitleBn; }
    public String getReasonEn() { return reasonEn; }
    public String getReasonBn() { return reasonBn; }
    public String getShariahReference() { return shariahReference; }

    @Override
    public String toString() {
        return heirTitleBn + " (" + heirKey + "): " + status + " - " + (isBlocked() ? reasonEn : "Eligible");
    }
}