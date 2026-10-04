package com.devflux.deenone.features.quiz.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class QuizRankUser implements Serializable {

    @SerializedName("rank")
    private int rank;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("district")
    private String district;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("quiz_points")
    private int quizPoints;

    public QuizRankUser() {}

    public QuizRankUser(int rank, String userId, String name, String district, String avatar, int quizPoints) {
        this.rank = rank;
        this.userId = userId;
        this.name = name;
        this.district = district;
        this.avatar = avatar;
        this.quizPoints = quizPoints;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserId() {
        return userId != null ? userId : "";
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name != null && !name.trim().isEmpty() ? name : "ব্যবহারকারী";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district != null && !district.trim().isEmpty() ? district : "বাংলাদেশ";
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAvatar() {
        return avatar != null ? avatar : "";
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getQuizPoints() {
        return quizPoints;
    }

    public void setQuizPoints(int quizPoints) {
        this.quizPoints = quizPoints;
    }

    public String getInitials() {
        if (avatar != null && !avatar.trim().isEmpty() && avatar.length() <= 3 && !avatar.startsWith("avatar_")) {
            return avatar;
        }
        String cleanName = getName().trim();
        if (cleanName.isEmpty()) return "U";
        String[] parts = cleanName.split("\\s+");
        if (parts.length >= 2) {
            String f = parts[0].substring(0, Math.min(1, parts[0].length()));
            String s = parts[1].substring(0, Math.min(1, parts[1].length()));
            return (f + s).toUpperCase();
        } else {
            return cleanName.substring(0, Math.min(2, cleanName.length())).toUpperCase();
        }
    }
}
