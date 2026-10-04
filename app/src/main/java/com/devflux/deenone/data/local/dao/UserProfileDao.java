package com.devflux.deenone.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.devflux.deenone.data.local.entity.UserProfileEntity;

@Dao
public interface UserProfileDao {

    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    LiveData<UserProfileEntity> getUserProfile(String userId);

    @Query("SELECT * FROM user_profiles WHERE userId = :userId LIMIT 1")
    UserProfileEntity getUserProfileSync(String userId);

    @Query("SELECT * FROM user_profiles LIMIT 1")
    LiveData<UserProfileEntity> getActiveProfile();

    @Query("SELECT * FROM user_profiles LIMIT 1")
    UserProfileEntity getActiveProfileSync();

    @Query("SELECT COUNT(*) FROM user_profiles")
    int getProfileCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateProfile(UserProfileEntity profile);

    @Update
    void updateProfile(UserProfileEntity profile);

    @Query("UPDATE user_profiles SET points = :points, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
    void updatePoints(String userId, int points, long timestamp);

    @Query("UPDATE user_profiles SET fullName = :fullName, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
    void updateFullName(String userId, String fullName, long timestamp);

    @Query("UPDATE user_profiles SET timezone = :timezone, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
    void updateTimezone(String userId, String timezone, long timestamp);

    @Query("UPDATE user_profiles SET avatarUri = :avatarUri, avatarPreset = :preset, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
    void updateAvatar(String userId, String avatarUri, int preset, long timestamp);

    @Query("UPDATE user_profiles SET isVerified = :isVerified, lastUpdatedTimestamp = :timestamp WHERE userId = :userId")
    void updateVerificationStatus(String userId, boolean isVerified, long timestamp);

    @Query("DELETE FROM user_profiles")
    void deleteAllProfilesSync();
}
