package com.devflux.deenone.core.verification;

import java.io.Serializable;

public class IslamicSourceMetadata implements Serializable {

    private final String id;
    private final String sourceName;
    private final String sourceUrl;
    private final String collectionName;
    private final String referenceNumber;
    private final String verifiedBy;
    private final VerificationStatus verificationStatus;
    private final long lastUpdatedTimestamp;
    private final String methodologyNotes;

    public IslamicSourceMetadata(String id, String sourceName, String sourceUrl,
                                 String collectionName, String referenceNumber,
                                 String verifiedBy, VerificationStatus verificationStatus,
                                 long lastUpdatedTimestamp, String methodologyNotes) {
        this.id = id;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.collectionName = collectionName;
        this.referenceNumber = referenceNumber;
        this.verifiedBy = verifiedBy;
        this.verificationStatus = verificationStatus != null ? verificationStatus : VerificationStatus.ISLAMIC_FOUNDATION_APPROVED;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp > 0 ? lastUpdatedTimestamp : System.currentTimeMillis();
        this.methodologyNotes = methodologyNotes;
    }

    public String getId() {
        return id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public String getMethodologyNotes() {
        return methodologyNotes;
    }
}
