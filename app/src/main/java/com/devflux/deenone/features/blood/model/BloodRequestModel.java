package com.devflux.deenone.features.blood.model;

import java.io.Serializable;

public class BloodRequestModel implements Serializable {
    private int id;
    private String requesterUserId;
    private String patientName;
    private String bloodGroup;
    private int unitsNeeded;
    private String hospitalName;
    private String district;
    private String upazila;
    private String contactPhone;
    private String whatsappNumber;
    private String urgencyLevel;
    private String neededDate;
    private String status;
    private long createdAt;

    public BloodRequestModel() {}

    public BloodRequestModel(int id, String requesterUserId, String patientName, String bloodGroup,
                             int unitsNeeded, String hospitalName, String district, String upazila,
                             String contactPhone, String whatsappNumber, String urgencyLevel,
                             String neededDate, String status, long createdAt) {
        this.id = id;
        this.requesterUserId = requesterUserId;
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.unitsNeeded = unitsNeeded;
        this.hospitalName = hospitalName;
        this.district = district;
        this.upazila = upazila;
        this.contactPhone = contactPhone;
        this.whatsappNumber = whatsappNumber;
        this.urgencyLevel = urgencyLevel;
        this.neededDate = neededDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRequesterUserId() { return requesterUserId; }
    public void setRequesterUserId(String requesterUserId) { this.requesterUserId = requesterUserId; }

    public String getPatientName() { return patientName != null ? patientName : ""; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getBloodGroup() { return bloodGroup != null ? bloodGroup : "AB+"; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getUnitsNeeded() { return unitsNeeded > 0 ? unitsNeeded : 1; }
    public void setUnitsNeeded(int unitsNeeded) { this.unitsNeeded = unitsNeeded; }

    public String getHospitalName() { return hospitalName != null ? hospitalName : ""; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getDistrict() { return district != null ? district : ""; }
    public void setDistrict(String district) { this.district = district; }

    public String getUpazila() { return upazila != null ? upazila : ""; }
    public void setUpazila(String upazila) { this.upazila = upazila; }

    public String getContactPhone() { return contactPhone != null ? contactPhone : ""; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getWhatsappNumber() { return whatsappNumber != null ? whatsappNumber : ""; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }

    public String getUrgencyLevel() { return urgencyLevel != null ? urgencyLevel : "EMERGENCY"; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public String getNeededDate() { return neededDate != null ? neededDate : ""; }
    public void setNeededDate(String neededDate) { this.neededDate = neededDate; }

    public String getStatus() { return status != null ? status : "OPEN"; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
