package com.devflux.deenone.core.ads;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.devflux.deenone.core.admin.AdminRemoteConfigManager;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Enterprise Production-Grade AdMob AdManager for DeenOne.
 * Fully controlled remotely via PHP Admin Panel.
 *
 * Implements:
 *   1. Master Ads ON/OFF switch (Global Ads = OFF disables ALL requests and displays)
 *   2. Test Ads vs Real Ads with strict mutual exclusivity
 *   3. Dynamic Ad Unit ID replacement without APK/AAB update
 *   4. Independent individual format controls (Banner, Interstitial, Native, Rewarded, App Open)
 *   5. Interstitial frequency, cooldown, and home navigation controls
 *   6. Voluntary Rewarded video playback
 *   7. Zero crash protection & safe defaults
 */
public class AdManager {

    private static final String TAG = "AdManager";
    private static volatile AdManager instance;

    private Context appContext;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final AtomicBoolean isMobileAdsInitialized = new AtomicBoolean(false);
    private final AtomicBoolean isInterstitialLoading = new AtomicBoolean(false);
    private final AtomicBoolean isRewardedLoading = new AtomicBoolean(false);

    // Preloaded Ad Instances
    private InterstitialAd mInterstitialAd;
    private RewardedAd mRewardedAd;
    private AppOpenAd mAppOpenAd;

    // Tracking Counters & Timers
    private int navigationCount = 0;
    private long lastInterstitialShownTimestamp = 0;
    private long lastAppOpenShownTimestamp = 0;

    public interface OnRewardEarnedListener {
        void onRewardEarned(int amount, String type);
    }

    public interface NativeAdCallback {
        void onLoaded(@NonNull NativeAd nativeAd);
        void onFailed(@NonNull String reason);
    }

    private AdManager() {
    }

    public static AdManager getInstance() {
        if (instance == null) {
            synchronized (AdManager.class) {
                if (instance == null) {
                    instance = new AdManager();
                }
            }
        }
        return instance;
    }

    // =========================================================================
    // Initialization & Remote Config Sync
    // =========================================================================

    public void initialize(@NonNull Context context) {
        try {
            this.appContext = context.getApplicationContext();
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(appContext);

            // Rule 1 & 11: Priority Check - If Admin Device or Master Ads is OFF, do not initialize SDK or make ad requests
            if (rc.isAdminDevice() || !rc.isAdsEnabled()) {
                Log.d(TAG, "AdMob Ads globally DISABLED (Admin Device or remote switch OFF). Zero ad requests made.");
                return;
            }

            ensureMobileAdsInitialized();
        } catch (Throwable t) {
            Log.e(TAG, "Error initializing AdManager: " + t.getMessage(), t);
        }
    }

    private void ensureMobileAdsInitialized() {
        try {
            if (appContext == null) return;
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(appContext);
            if (rc.isAdminDevice() || !rc.isAdsEnabled()) return;

            if (isMobileAdsInitialized.compareAndSet(false, true)) {
                // Google AdMob Policy Compliance & Family/Islamic Safety Configuration
                try {
                    com.google.android.gms.ads.RequestConfiguration.Builder reqConfigBuilder =
                            MobileAds.getRequestConfiguration().toBuilder();

                    // 1. Google Policy: Enforce Maximum Ad Content Rating
                    // Defaults strictly to "G" (General Audience) to block all dating, adult, sensitive, gambling ads
                    String rating = rc.getMaxAdContentRating();
                    if ("PG".equalsIgnoreCase(rating)) {
                        reqConfigBuilder.setMaxAdContentRating(com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_PG);
                    } else if ("T".equalsIgnoreCase(rating)) {
                        reqConfigBuilder.setMaxAdContentRating(com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_T);
                    } else {
                        reqConfigBuilder.setMaxAdContentRating(com.google.android.gms.ads.RequestConfiguration.MAX_AD_CONTENT_RATING_G);
                    }

                    // 2. Google Policy: Register Test Devices to prevent Invalid Traffic strikes
                    java.util.List<String> testDevices = new java.util.ArrayList<>();
                    testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
                    String customTestDevice = rc.getTestDeviceId();
                    if (customTestDevice != null && !customTestDevice.trim().isEmpty()) {
                        testDevices.add(customTestDevice.trim());
                    }
                    reqConfigBuilder.setTestDeviceIds(testDevices);

                    MobileAds.setRequestConfiguration(reqConfigBuilder.build());
                } catch (Throwable t) {
                    Log.e(TAG, "Error applying RequestConfiguration: " + t.getMessage());
                }

                MobileAds.initialize(appContext, initializationStatus -> {
                    Log.d(TAG, "MobileAds initialized successfully. Mode: " + rc.getAdMode());
                    // Preload eligible formats
                    if (rc.isInterstitialEnabled()) {
                        preloadInterstitial(appContext);
                    }
                    if (rc.isRewardedEnabled()) {
                        preloadRewarded(appContext);
                    }
                });
            }
        } catch (Throwable t) {
            Log.e(TAG, "ensureMobileAdsInitialized error: " + t.getMessage(), t);
        }
    }

    /**
     * Called dynamically when PHP Admin Remote Config is fetched.
     * Re-applies changes immediately without requiring APK/AAB update (Rule 4 & 17).
     */
    public void onConfigurationUpdated(@NonNull Context context) {
        try {
            this.appContext = context.getApplicationContext();
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(appContext);

            if (rc.isAdminDevice() || !rc.isAdsEnabled()) {
                Log.d(TAG, "Ads remotely turned OFF or Admin Device detected. Clearing all preloaded ads.");
                mInterstitialAd = null;
                mRewardedAd = null;
                mAppOpenAd = null;
                return;
            }

            ensureMobileAdsInitialized();

            // Refresh preloaded ads with new unit IDs
            if (rc.isInterstitialEnabled() && mInterstitialAd == null) {
                preloadInterstitial(appContext);
            }
            if (rc.isRewardedEnabled() && mRewardedAd == null) {
                preloadRewarded(appContext);
            }
        } catch (Throwable t) {
            Log.e(TAG, "onConfigurationUpdated error: " + t.getMessage(), t);
        }
    }

    public boolean isAdEnabled() {
        try {
            if (appContext == null) return false;
            return AdminRemoteConfigManager.getInstance(appContext).isAdsEnabled();
        } catch (Throwable t) {
            return false;
        }
    }

    // =========================================================================
    // 1. BANNER AD CONTROLLER (Rule 7)
    // =========================================================================

    public void loadBanner(@NonNull Activity activity, ViewGroup container) {
        if (container == null) return;

        try {
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(activity);

            // Rule 1 & 5: Check master toggle and banner format toggle
            if (!rc.isAdsEnabled() || !rc.isBannerEnabled()) {
                container.setVisibility(View.GONE);
                container.removeAllViews();
                return;
            }

            ensureMobileAdsInitialized();

            String bannerUnitId = rc.getBannerAdUnitId();
            if (bannerUnitId == null || bannerUnitId.trim().isEmpty()) {
                container.setVisibility(View.GONE);
                return;
            }

            AdView adView = new AdView(activity);
            adView.setAdSize(AdSize.BANNER);
            adView.setAdUnitId(bannerUnitId);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    try {
                        container.removeAllViews();
                        container.addView(adView);
                        container.setVisibility(View.VISIBLE);
                        Log.d(TAG, "Banner ad loaded successfully into container.");
                    } catch (Throwable t) {
                        Log.e(TAG, "Error displaying banner ad view: " + t.getMessage());
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    Log.w(TAG, "Banner ad failed to load: " + loadAdError.getMessage());
                    container.setVisibility(View.GONE);
                }
            });

            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        } catch (Throwable t) {
            Log.e(TAG, "loadBanner safe fallback: " + t.getMessage(), t);
            container.setVisibility(View.GONE);
        }
    }

    // =========================================================================
    // 2. INTERSTITIAL AD CONTROLLER (Rule 6)
    // =========================================================================

    public void preloadInterstitial(@NonNull Context context) {
        try {
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(context);
            if (!rc.isAdsEnabled() || !rc.isInterstitialEnabled()) {
                return;
            }

            if (mInterstitialAd != null || isInterstitialLoading.get()) {
                return;
            }

            isInterstitialLoading.set(true);
            String unitId = rc.getInterstitialAdUnitId();

            InterstitialAd.load(
                    context,
                    unitId,
                    new AdRequest.Builder().build(),
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            mInterstitialAd = interstitialAd;
                            isInterstitialLoading.set(false);
                            Log.d(TAG, "Interstitial ad preloaded successfully.");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mInterstitialAd = null;
                            isInterstitialLoading.set(false);
                            Log.w(TAG, "Interstitial ad failed to preload: " + loadAdError.getMessage());
                        }
                    }
            );
        } catch (Throwable t) {
            isInterstitialLoading.set(false);
            Log.e(TAG, "preloadInterstitial safe fallback: " + t.getMessage(), t);
        }
    }

    /**
     * Records an app navigation event and shows Interstitial Ad only if all remote criteria are satisfied:
     *   - Master Ads ON & Interstitial Format ON
     *   - Homepage trigger condition respected
     *   - Minimum navigation interval reached
     *   - Cooldown duration satisfied
     */
    public void recordNavigationAndShowInterstitial(@NonNull Activity activity, boolean isHomepage, Runnable onFinished) {
        Runnable safeCallback = () -> {
            if (onFinished != null) {
                mainHandler.post(onFinished);
            }
        };

        try {
            if (activity.isFinishing() || activity.isDestroyed()) {
                safeCallback.run();
                return;
            }

            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(activity);

            // Priority Check
            if (!rc.isAdsEnabled() || !rc.isInterstitialEnabled()) {
                safeCallback.run();
                return;
            }

            // Homepage navigation rule
            if (isHomepage && !rc.isInterstitialHomeTriggerEnabled()) {
                safeCallback.run();
                return;
            }

            navigationCount++;
            int interval = Math.max(AdConfig.MIN_SAFE_INTERSTITIAL_INTERVAL, rc.getAdFrequencyInterval());
            int cooldownSec = Math.max(AdConfig.MIN_SAFE_INTERSTITIAL_COOLDOWN_SECONDS, rc.getInterstitialCooldownSeconds());
            long now = System.currentTimeMillis();
            boolean intervalMet = (navigationCount % interval == 0);
            boolean cooldownMet = (now - lastInterstitialShownTimestamp) >= (cooldownSec * 1000L);

            if (!intervalMet || !cooldownMet) {
                safeCallback.run();
                return;
            }

            if (mInterstitialAd != null) {
                InterstitialAd ad = mInterstitialAd;
                mInterstitialAd = null; // Consume instance

                ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        lastInterstitialShownTimestamp = System.currentTimeMillis();
                        safeCallback.run();
                        preloadInterstitial(activity);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.w(TAG, "Interstitial failed to show: " + adError.getMessage());
                        safeCallback.run();
                        preloadInterstitial(activity);
                    }
                });

                ad.show(activity);
            } else {
                // Not ready, proceed smoothly without blocking user
                safeCallback.run();
                preloadInterstitial(activity);
            }
        } catch (Throwable t) {
            Log.e(TAG, "recordNavigationAndShowInterstitial safe fallback: " + t.getMessage(), t);
            safeCallback.run();
        }
    }

    // =========================================================================
    // 3. REWARDED VIDEO AD CONTROLLER (Rule 8)
    // =========================================================================

    public void preloadRewarded(@NonNull Context context) {
        try {
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(context);
            if (!rc.isAdsEnabled() || !rc.isRewardedEnabled()) return;

            if (mRewardedAd != null || isRewardedLoading.get()) return;

            isRewardedLoading.set(true);
            String unitId = rc.getRewardedAdUnitId();

            RewardedAd.load(
                    context,
                    unitId,
                    new AdRequest.Builder().build(),
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            isRewardedLoading.set(false);
                            Log.d(TAG, "Rewarded ad preloaded successfully.");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mRewardedAd = null;
                            isRewardedLoading.set(false);
                            Log.w(TAG, "Rewarded ad failed to preload: " + loadAdError.getMessage());
                        }
                    }
            );
        } catch (Throwable t) {
            isRewardedLoading.set(false);
            Log.e(TAG, "preloadRewarded safe fallback: " + t.getMessage(), t);
        }
    }

    /**
     * Shows voluntary Rewarded Video Ad. User earns reward ONLY on full completion.
     */
    public void showRewardedAd(@NonNull Activity activity, OnRewardEarnedListener rewardListener, Runnable onDismissed) {
        Runnable safeDismiss = () -> {
            if (onDismissed != null) mainHandler.post(onDismissed);
        };

        try {
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(activity);
            if (!rc.isAdsEnabled() || !rc.isRewardedEnabled()) {
                safeDismiss.run();
                return;
            }

            if (mRewardedAd != null) {
                RewardedAd ad = mRewardedAd;
                mRewardedAd = null;

                ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        safeDismiss.run();
                        preloadRewarded(activity);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        Log.w(TAG, "Rewarded ad failed to show: " + adError.getMessage());
                        safeDismiss.run();
                        preloadRewarded(activity);
                    }
                });

                ad.show(activity, rewardItem -> {
                    if (rewardListener != null) {
                        int configuredReward = rc.getRewardAmount();
                        rewardListener.onRewardEarned(configuredReward, rewardItem.getType());
                    }
                });
            } else {
                safeDismiss.run();
                preloadRewarded(activity);
            }
        } catch (Throwable t) {
            Log.e(TAG, "showRewardedAd safe fallback: " + t.getMessage(), t);
            safeDismiss.run();
        }
    }

    // =========================================================================
    // 4. NATIVE ADVANCED AD CONTROLLER (Rule 9)
    // =========================================================================

    public void loadNativeAd(@NonNull Context context, @NonNull NativeAdCallback callback) {
        try {
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(context);
            if (!rc.isAdsEnabled() || !rc.isNativeEnabled()) {
                callback.onFailed("Native ads disabled remotely.");
                return;
            }

            ensureMobileAdsInitialized();

            AdLoader adLoader = new AdLoader.Builder(context, rc.getNativeAdUnitId())
                    .forNativeAd(nativeAd -> mainHandler.post(() -> callback.onLoaded(nativeAd)))
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mainHandler.post(() -> callback.onFailed(loadAdError.getMessage()));
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } catch (Throwable t) {
            Log.e(TAG, "loadNativeAd safe fallback: " + t.getMessage(), t);
            callback.onFailed(t.getMessage());
        }
    }

    // =========================================================================
    // 5. APP OPEN AD CONTROLLER (Rule 10)
    // =========================================================================

    public void showAppOpenAdIfEligible(@NonNull Activity activity) {
        try {
            AdminRemoteConfigManager rc = AdminRemoteConfigManager.getInstance(activity);
            if (!rc.isAdsEnabled() || !rc.isAppOpenEnabled()) return;

            long now = System.currentTimeMillis();
            long cooldownMs = rc.getAppOpenCooldownSeconds() * 1000L;
            if ((now - lastAppOpenShownTimestamp) < cooldownMs) return;

            ensureMobileAdsInitialized();

            AppOpenAd.load(
                    activity,
                    rc.getAppOpenAdUnitId(),
                    new AdRequest.Builder().build(),
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                            mAppOpenAd = appOpenAd;
                            mAppOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    lastAppOpenShownTimestamp = System.currentTimeMillis();
                                    mAppOpenAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    mAppOpenAd = null;
                                }
                            });
                            mAppOpenAd.show(activity);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            mAppOpenAd = null;
                        }
                    }
            );
        } catch (Throwable t) {
            Log.e(TAG, "showAppOpenAdIfEligible safe fallback: " + t.getMessage(), t);
        }
    }
}
