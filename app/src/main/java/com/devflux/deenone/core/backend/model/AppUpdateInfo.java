package com.devflux.deenone.core.backend.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AppUpdateInfo implements Serializable {

    @SerializedName("latest_version_code")
    private int latestVersionCode;

    @SerializedName("latest_version_name")
    private String latestVersionName;

    @SerializedName("is_force_update")
    private boolean isForceUpdate;

    @SerializedName("update_title")
    private String updateTitle;

    @SerializedName("update_message")
    private String updateMessage;

    @SerializedName("download_url")
    private String downloadUrl;

    public AppUpdateInfo() {
        this.latestVersionCode = 20100;
        this.latestVersionName = "2.1.0";
        this.isForceUpdate = false;
        this.updateTitle = "দ্বীনওয়ান আপডেট";
        this.updateMessage = "নতুন ফিচার ও উন্নতিসহ অ্যাপটি আপডেট করুন।";
        this.downloadUrl = "https://play.google.com/store/apps/details?id=com.devflux.deenone";
    }

    public AppUpdateInfo(int latestVersionCode, String latestVersionName, boolean isForceUpdate,
                         String updateTitle, String updateMessage, String downloadUrl) {
        this.latestVersionCode = latestVersionCode;
        this.latestVersionName = latestVersionName;
        this.isForceUpdate = isForceUpdate;
        this.updateTitle = updateTitle;
        this.updateMessage = updateMessage;
        this.downloadUrl = downloadUrl;
    }

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public String getLatestVersionName() {
        return latestVersionName;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public String getUpdateTitle() {
        return updateTitle;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
