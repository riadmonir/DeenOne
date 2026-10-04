package com.devflux.deenone.core.backend.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class AdControlConfig implements Serializable {

    @SerializedName("ads_enabled")
    private boolean adsEnabled;

    @SerializedName("banner_admob_id")
    private String bannerAdmobId;

    @SerializedName("interstitial_admob_id")
    private String interstitialAdmobId;

    @SerializedName("interstitial_interval_clicks")
    private int interstitialIntervalClicks;

    @SerializedName("sponsor_banner_image_url")
    private String sponsorBannerImageUrl;

    @SerializedName("sponsor_banner_target_url")
    private String sponsorBannerTargetUrl;

    public AdControlConfig() {
        this.adsEnabled = false;
        this.bannerAdmobId = "";
        this.interstitialAdmobId = "";
        this.interstitialIntervalClicks = 5;
        this.sponsorBannerImageUrl = "";
        this.sponsorBannerTargetUrl = "";
    }

    public AdControlConfig(boolean adsEnabled, String bannerAdmobId, String interstitialAdmobId,
                           int interstitialIntervalClicks, String sponsorBannerImageUrl, String sponsorBannerTargetUrl) {
        this.adsEnabled = adsEnabled;
        this.bannerAdmobId = bannerAdmobId;
        this.interstitialAdmobId = interstitialAdmobId;
        this.interstitialIntervalClicks = interstitialIntervalClicks;
        this.sponsorBannerImageUrl = sponsorBannerImageUrl;
        this.sponsorBannerTargetUrl = sponsorBannerTargetUrl;
    }

    public boolean isAdsEnabled() {
        return adsEnabled;
    }

    public String getBannerAdmobId() {
        return bannerAdmobId;
    }

    public String getInterstitialAdmobId() {
        return interstitialAdmobId;
    }

    public int getInterstitialIntervalClicks() {
        return interstitialIntervalClicks;
    }

    public String getSponsorBannerImageUrl() {
        return sponsorBannerImageUrl;
    }

    public String getSponsorBannerTargetUrl() {
        return sponsorBannerTargetUrl;
    }
}
