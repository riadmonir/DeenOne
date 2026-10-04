package com.devflux.deenone.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "blood_donors")
public class BloodDonorEntity implements Serializable {

    @PrimaryKey
    @NonNull
    private String donorId;

    private String userId;
    private String name;
    private String bloodGroup; // A+, A-, B+, B-, O+, O-, AB+, AB-
    private String phone;
    private String location; // e.g. "মদিনা মুনাওয়ারা, সৌদি আরব"
    private long lastDonationDate;
    private long registrationTimestamp;
    private boolean isAvailable;

    public BloodDonorEntity() {
        this.donorId = "donor_" + System.currentTimeMillis();
        this.isAvailable = true;
    }

    @Ignore
    public BloodDonorEntity(@NonNull String donorId, String userId, String name, String bloodGroup,
                            String phone, String location, long lastDonationDate,
                            long registrationTimestamp, boolean isAvailable) {
        this.donorId = donorId;
        this.userId = userId;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.phone = phone;
        this.location = location;
        this.lastDonationDate = lastDonationDate;
        this.registrationTimestamp = registrationTimestamp;
        this.isAvailable = isAvailable;
    }

    @NonNull
    public String getDonorId() { return donorId; }
    public void setDonorId(@NonNull String donorId) { this.donorId = donorId; }

    public String getUserId() { return userId != null ? userId : ""; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name != null ? name : ""; }
    public void setName(String name) { this.name = name; }

    public String getBloodGroup() { return bloodGroup != null ? bloodGroup : "A+"; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getPhone() { return phone != null ? phone : ""; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location != null ? location : ""; }
    public void setLocation(String location) { this.location = location; }

    public long getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(long lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public long getRegistrationTimestamp() { return registrationTimestamp; }
    public void setRegistrationTimestamp(long registrationTimestamp) { this.registrationTimestamp = registrationTimestamp; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
