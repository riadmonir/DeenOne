package com.devflux.deenone.core.backend.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class LeaderboardUser implements Serializable {

  @SerializedName("rank")
  private int rank;

  @SerializedName("user_id")
  private String userId;

  @SerializedName("user_name")
  private String userName;

  @SerializedName("avatar_url")
  private String avatarUrl;

  @SerializedName("location")
  private String location;

  @SerializedName("amal_points")
  private int amalPoints;

  @SerializedName("streak_days")
  private int streakDays;

  @SerializedName("badge_title")
  private String badgeTitle;

  public LeaderboardUser() {}

  public LeaderboardUser(int rank, String userId, String userName, String avatarUrl,
              int amalPoints, int streakDays, String badgeTitle) {
    this(rank, userId, userName, avatarUrl, "ঢাকা", amalPoints, streakDays, badgeTitle);
  }

  public LeaderboardUser(int rank, String userId, String userName, String avatarUrl,
              String location, int amalPoints, int streakDays, String badgeTitle) {
    this.rank = rank;
    this.userId = userId;
    this.userName = userName;
    this.avatarUrl = avatarUrl;
    this.location = location;
    this.amalPoints = amalPoints;
    this.streakDays = streakDays;
    this.badgeTitle = badgeTitle;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public String getLocation() {
    if (location == null || location.trim().isEmpty()) {
      return "ঢাকা";
    }
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public int getAmalPoints() {
    return amalPoints;
  }

  public void setAmalPoints(int amalPoints) {
    this.amalPoints = amalPoints;
  }

  public int getPoints() {
    return amalPoints;
  }

  public int getStreakDays() {
    return streakDays;
  }

  public String getBadgeTitle() {
    if (badgeTitle == null || badgeTitle.trim().isEmpty()) {
      return "নবাগত";
    }
    return badgeTitle;
  }

  public void setBadgeTitle(String badgeTitle) {
    this.badgeTitle = badgeTitle;
  }
}

