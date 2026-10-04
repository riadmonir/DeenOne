package com.devflux.deenone.core.ads;

/**
 * AdMob Configuration Constants & Safety Policies
 * Compliant with Google AdMob Policies and Google Play Safety Guidelines.
 */
public class AdConfig {

    // =========================================================================
    // Official Google Test Ad Unit IDs (Play Store Safety Policy Compliant)
    // =========================================================================
    public static final String TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713";
    public static final String TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    public static final String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";
    public static final String TEST_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";
    public static final String TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    public static final String TEST_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921";
    public static final String TEST_REWARDED_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/5354046379";

    // =========================================================================
    // DeenOne Default Production Ad Unit IDs (Configured via PHP Admin Panel)
    // =========================================================================
    public static final String DEFAULT_APP_ID = "ca-app-pub-6495900750071718~9706674012";
    public static final String DEFAULT_BANNER_ID = "ca-app-pub-6495900750071718/7835288176";
    public static final String DEFAULT_INTERSTITIAL_ID = "ca-app-pub-6495900750071718/8521769602";
    public static final String DEFAULT_NATIVE_ID = "ca-app-pub-6495900750071718/3896043165";
    public static final String DEFAULT_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    public static final String DEFAULT_APP_OPEN_ID = "ca-app-pub-3940256099942544/9257395921";
    public static final String DEFAULT_REWARDED_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/5354046379";

    // Defaults for behavior
    public static final int DEFAULT_INTERSTITIAL_INTERVAL = 5;
    public static final int DEFAULT_INTERSTITIAL_COOLDOWN_SECONDS = 60;
    public static final boolean DEFAULT_INTERSTITIAL_HOME_TRIGGER = false;
    public static final int DEFAULT_REWARD_AMOUNT = 10;
    public static final int DEFAULT_APP_OPEN_COOLDOWN_SECONDS = 14400; // 4 hours

    // =========================================================================
    // Google AdMob & Play Store Policy Compliance Constants
    // =========================================================================
    // Restricts ads strictly to Family / Islamic safe rating (G = General Audiences)
    public static final String DEFAULT_MAX_AD_CONTENT_RATING = "G";
    // Prevents disruptive ads policy violation even if remote config has 0
    public static final int MIN_SAFE_INTERSTITIAL_COOLDOWN_SECONDS = 30;
    public static final int MIN_SAFE_INTERSTITIAL_INTERVAL = 2;
}
