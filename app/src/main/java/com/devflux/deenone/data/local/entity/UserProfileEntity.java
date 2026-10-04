package com.devflux.deenone.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profiles")
public class UserProfileEntity {

    @PrimaryKey
    @NonNull
    private String userId;

    private String fullName;
    private String email;
    private String phone;
    private String timezone;
    private long joiningDateTimestamp;
    private int points;
    private int level;
    private int streakDays;
    private boolean isVerified;
    private String avatarUri;
    private int avatarPreset;
    private int salahCompletedTotal;
    private int amalCompletedTotal;
    private int quizCompletedTotal;
    private long lastUpdatedTimestamp;

    public UserProfileEntity() {
        this.userId = "usr_main";
        this.fullName = "ব্যবহারকারী";
        this.email = "";
        this.phone = "";
        this.timezone = "Asia/Dhaka";
        this.joiningDateTimestamp = System.currentTimeMillis();
        this.points = 0;
        this.level = 1;
        this.streakDays = 1;
        this.isVerified = false;
        this.avatarUri = "";
        this.avatarPreset = 0;
        this.salahCompletedTotal = 0;
        this.amalCompletedTotal = 0;
        this.quizCompletedTotal = 0;
        this.lastUpdatedTimestamp = System.currentTimeMillis();
    }

    @Ignore
    public UserProfileEntity(@NonNull String userId, String fullName, String email, String phone,
                             String timezone, long joiningDateTimestamp, int points, int level,
                             int streakDays, boolean isVerified, String avatarUri, int avatarPreset,
                             int salahCompletedTotal, int amalCompletedTotal, int quizCompletedTotal,
                             long lastUpdatedTimestamp) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.timezone = timezone;
        this.joiningDateTimestamp = joiningDateTimestamp;
        this.points = points;
        this.level = level;
        this.streakDays = streakDays;
        this.isVerified = isVerified;
        this.avatarUri = avatarUri;
        this.avatarPreset = avatarPreset;
        this.salahCompletedTotal = salahCompletedTotal;
        this.amalCompletedTotal = amalCompletedTotal;
        this.quizCompletedTotal = quizCompletedTotal;
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }

    public String getFullName() { return (fullName != null && !fullName.isEmpty()) ? fullName : "দ্বীনওয়ান ব্যবহারকারী"; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getTimezone() { return timezone != null ? timezone : "Asia/Dhaka"; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public long getJoiningDateTimestamp() { return joiningDateTimestamp > 0 ? joiningDateTimestamp : System.currentTimeMillis(); }
    public void setJoiningDateTimestamp(long joiningDateTimestamp) { this.joiningDateTimestamp = joiningDateTimestamp; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getStreakDays() { return streakDays; }
    public void setStreakDays(int streakDays) { this.streakDays = streakDays; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getAvatarUri() { return avatarUri; }
    public void setAvatarUri(String avatarUri) { this.avatarUri = avatarUri; }

    public int getAvatarPreset() { return avatarPreset; }
    public void setAvatarPreset(int avatarPreset) { this.avatarPreset = avatarPreset; }

    public int getSalahCompletedTotal() { return salahCompletedTotal; }
    public void setSalahCompletedTotal(int salahCompletedTotal) { this.salahCompletedTotal = salahCompletedTotal; }

    public int getAmalCompletedTotal() { return amalCompletedTotal; }
    public void setAmalCompletedTotal(int amalCompletedTotal) { this.amalCompletedTotal = amalCompletedTotal; }

    public int getQuizCompletedTotal() { return quizCompletedTotal; }
    public void setQuizCompletedTotal(int quizCompletedTotal) { this.quizCompletedTotal = quizCompletedTotal; }

    public long getLastUpdatedTimestamp() { return lastUpdatedTimestamp; }
    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) { this.lastUpdatedTimestamp = lastUpdatedTimestamp; }
}
