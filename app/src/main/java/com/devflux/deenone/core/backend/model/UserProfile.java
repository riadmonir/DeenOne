package com.devflux.deenone.core.backend.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class UserProfile implements Serializable {

    @SerializedName("user_id")
    private String userId;

    @SerializedName("user_name")
    private String userName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("avatar_preset")
    private int avatarPreset;

    @SerializedName("total_points")
    private int totalPoints;

    @SerializedName("joining_timestamp")
    private long joiningTimestamp;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("is_verified")
    private boolean isVerified;

    public UserProfile() {
        this.userId = "usr_main";
        this.userName = "ব্যবহারকারী";
        this.timezone = "Asia/Dhaka";
        this.totalPoints = 0;
        this.joiningTimestamp = System.currentTimeMillis();
        this.isVerified = false;
    }

    public UserProfile(String userId, String userName, String email, String phone,
                       String avatarUrl, int avatarPreset, int totalPoints,
                       long joiningTimestamp, String timezone, boolean isVerified) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.avatarPreset = avatarPreset;
        this.totalPoints = totalPoints;
        this.joiningTimestamp = joiningTimestamp;
        this.timezone = timezone;
        this.isVerified = isVerified;
    }

    public String getUserId() { return userId != null ? userId : "usr_main"; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName != null && !userName.isEmpty() ? userName : "ব্যবহারকারী"; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email != null ? email : ""; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone != null ? phone : ""; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarUrl() { return avatarUrl != null ? avatarUrl : ""; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public int getAvatarPreset() { return avatarPreset; }
    public void setAvatarPreset(int avatarPreset) { this.avatarPreset = avatarPreset; }

    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }

    public long getJoiningTimestamp() { return joiningTimestamp > 0 ? joiningTimestamp : System.currentTimeMillis(); }
    public void setJoiningTimestamp(long joiningTimestamp) { this.joiningTimestamp = joiningTimestamp; }

    public String getTimezone() { return timezone != null && !timezone.isEmpty() ? timezone : "Asia/Dhaka"; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}
