package com.devflux.deenone.core.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devflux.deenone.core.ads.AdConfig;
import com.devflux.deenone.core.notifications.NotificationHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Universal Admin Remote Configuration Manager.
 * Built for dual-architecture readiness:
 *   - Custom PHP/MySQL REST API (e.g. https://your-domain.com/api/get_config.php)
 *   - Local SharedPreferences fallback cache with instant 0-lag responsiveness
 *
 * Remotely manages:
 *   1. Dynamic AdMob Master & Individual Formats ON/OFF switch
 *   2. AdMob Ad Unit IDs (Test vs Real Ads mode with safety guarantees)
 *   3. Ad frequency, cooldown, and navigation eligibility
 *   4. Server broadcast announcements & push notifications
 *   5. Maintenance mode & Force update flags
 *   6. Daily Islamic content overrides
 */
public class AdminRemoteConfigManager {

    private static final String TAG = "AdminRemoteConfig";
    private static final String PREF_NAME = "deanone_admin_remote_config";

    // General App Keys
    public static final String KEY_ADS_ENABLED = "admin_ads_enabled";
    public static final String KEY_MAINTENANCE_MODE = "admin_maintenance_mode";
    public static final String KEY_FORCE_UPDATE_REQUIRED = "admin_force_update";
    public static final String KEY_MIN_VERSION_CODE = "admin_min_version_code";
    public static final String KEY_LATEST_VERSION_CODE = "admin_latest_version_code";
    public static final String KEY_LATEST_VERSION_NAME = "admin_latest_version_name";
    public static final String KEY_UPDATE_URL = "admin_update_url";
    public static final String KEY_UPDATE_TITLE = "admin_update_title";
    public static final String KEY_UPDATE_MESSAGE = "admin_update_message";
    public static final String KEY_ANNOUNCEMENT_ID = "admin_last_announcement_id";
    public static final String KEY_ANNOUNCEMENT_TEXT = "admin_announcement_text";
    public static final String KEY_SERVER_ENDPOINT = "admin_server_endpoint";
    public static final String KEY_DAILY_AYAH = "admin_daily_ayah";
    public static final String KEY_DAILY_HADITH = "admin_daily_hadith";

    // AdMob Mode & Unit Keys
    public static final String KEY_IS_ADMIN_DEVICE = "admin_is_this_admin_device";
    public static final String KEY_AD_MODE = "admin_ad_mode"; // "production" or "test"
    public static final String KEY_ADMOB_APP_ID = "admin_admob_app_id";
    public static final String KEY_ADMOB_BANNER = "admin_admob_banner_id";
    public static final String KEY_ADMOB_INTERSTITIAL = "admin_admob_interstitial_id";
    public static final String KEY_ADMOB_NATIVE = "admin_admob_native_id";
    public static final String KEY_ADMOB_REWARDED = "admin_admob_rewarded_id";
    public static final String KEY_ADMOB_APP_OPEN = "admin_admob_app_open_id";
    public static final String KEY_ADMOB_REWARDED_INTERSTITIAL = "admin_admob_rewarded_interstitial_id";

    // AdMob Individual Format Toggles
    public static final String KEY_AD_BANNER_ENABLED = "admin_ad_banner_enabled";
    public static final String KEY_AD_INTERSTITIAL_ENABLED = "admin_ad_interstitial_enabled";
    public static final String KEY_AD_NATIVE_ENABLED = "admin_ad_native_enabled";
    public static final String KEY_AD_REWARDED_ENABLED = "admin_ad_rewarded_enabled";
    public static final String KEY_AD_APP_OPEN_ENABLED = "admin_ad_app_open_enabled";
    public static final String KEY_AD_REWARDED_INTERSTITIAL_ENABLED = "admin_ad_rewarded_interstitial_enabled";

    // AdMob Behavioral Controls
    public static final String KEY_AD_FREQUENCY = "admin_ad_frequency_interval";
    public static final String KEY_AD_INTERSTITIAL_COOLDOWN = "admin_ad_interstitial_cooldown";
    public static final String KEY_AD_INTERSTITIAL_HOME_ENABLED = "admin_ad_interstitial_home_enabled";
    public static final String KEY_AD_BANNER_PLACEMENT = "admin_ad_banner_placement";
    public static final String KEY_AD_REWARD_AMOUNT = "admin_ad_reward_amount";
    public static final String KEY_AD_APP_OPEN_COOLDOWN = "admin_ad_app_open_cooldown";

    // Google AdMob Policy Compliance & Family Protection Keys
    public static final String KEY_AD_CONTENT_RATING = "admin_ad_content_rating";
    public static final String KEY_AD_TEST_DEVICE_ID = "admin_ad_test_device_id";

    private static volatile AdminRemoteConfigManager instance;

    private final Context appContext;
    private final SharedPreferences prefs;
    private final OkHttpClient httpClient;
    private final ExecutorService executor;
    private final Gson gson;

    private AdminRemoteConfigManager(Context context) {
        this.appContext = context.getApplicationContext();
        this.prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        this.executor = Executors.newSingleThreadExecutor();
        this.gson = new Gson();
    }

    public static AdminRemoteConfigManager getInstance(Context context) {
        if (instance == null) {
            synchronized (AdminRemoteConfigManager.class) {
                if (instance == null) {
                    instance = new AdminRemoteConfigManager(context);
                }
            }
        }
        return instance;
    }

    // =========================================================================
    // Master & Mode Configuration
    // =========================================================================

    public boolean isAdminDevice() {
        return prefs.getBoolean(KEY_IS_ADMIN_DEVICE, false);
    }

    public void setAdminDevice(boolean isAdmin) {
        prefs.edit().putBoolean(KEY_IS_ADMIN_DEVICE, isAdmin).apply();
    }

    public boolean isAdsEnabled() {
        // Admin Device Ad Suppression: If device belongs to admin, never serve ads
        if (isAdminDevice()) {
            return false;
        }
        return prefs.getBoolean(KEY_ADS_ENABLED, true);
    }

    public void setAdsEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_ADS_ENABLED, enabled).apply();
    }

    public String getAdMode() {
        return prefs.getString(KEY_AD_MODE, "production");
    }

    public boolean isTestAdsMode() {
        return "test".equalsIgnoreCase(getAdMode());
    }

    public String getAdmobAppId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_APP_ID;
        }
        return prefs.getString(KEY_ADMOB_APP_ID, AdConfig.DEFAULT_APP_ID);
    }

    // =========================================================================
    // Individual Format Toggles
    // =========================================================================

    public boolean isBannerEnabled() {
        return prefs.getBoolean(KEY_AD_BANNER_ENABLED, true);
    }

    public boolean isInterstitialEnabled() {
        return prefs.getBoolean(KEY_AD_INTERSTITIAL_ENABLED, true);
    }

    public boolean isNativeEnabled() {
        return prefs.getBoolean(KEY_AD_NATIVE_ENABLED, true);
    }

    public boolean isRewardedEnabled() {
        return prefs.getBoolean(KEY_AD_REWARDED_ENABLED, true);
    }

    public boolean isAppOpenEnabled() {
        return prefs.getBoolean(KEY_AD_APP_OPEN_ENABLED, false);
    }

    public boolean isRewardedInterstitialEnabled() {
        return prefs.getBoolean(KEY_AD_REWARDED_INTERSTITIAL_ENABLED, false);
    }

    // =========================================================================
    // Ad Unit IDs (Enforcing Safety Rule 2 & 16: Test vs Production)
    // =========================================================================

    public String getBannerAdUnitId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_BANNER_ID;
        }
        return prefs.getString(KEY_ADMOB_BANNER, AdConfig.DEFAULT_BANNER_ID);
    }

    public String getInterstitialAdUnitId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_INTERSTITIAL_ID;
        }
        return prefs.getString(KEY_ADMOB_INTERSTITIAL, AdConfig.DEFAULT_INTERSTITIAL_ID);
    }

    public String getNativeAdUnitId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_NATIVE_ID;
        }
        return prefs.getString(KEY_ADMOB_NATIVE, AdConfig.DEFAULT_NATIVE_ID);
    }

    public String getRewardedAdUnitId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_REWARDED_ID;
        }
        return prefs.getString(KEY_ADMOB_REWARDED, AdConfig.DEFAULT_REWARDED_ID);
    }

    public String getAppOpenAdUnitId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_APP_OPEN_ID;
        }
        return prefs.getString(KEY_ADMOB_APP_OPEN, AdConfig.DEFAULT_APP_OPEN_ID);
    }

    public String getRewardedInterstitialAdUnitId() {
        if (isTestAdsMode()) {
            return AdConfig.TEST_REWARDED_INTERSTITIAL_ID;
        }
        return prefs.getString(KEY_ADMOB_REWARDED_INTERSTITIAL, AdConfig.DEFAULT_REWARDED_INTERSTITIAL_ID);
    }

    // =========================================================================
    // Behavioral & Policy Guardrail Settings
    // =========================================================================

    public int getAdFrequencyInterval() {
        int interval = prefs.getInt(KEY_AD_FREQUENCY, AdConfig.DEFAULT_INTERSTITIAL_INTERVAL);
        // Google AdMob Policy Guardrail: Never allow negative or 1 to prevent disruptive ad spam
        return Math.max(AdConfig.MIN_SAFE_INTERSTITIAL_INTERVAL, interval);
    }

    public int getInterstitialCooldownSeconds() {
        int cooldown = prefs.getInt(KEY_AD_INTERSTITIAL_COOLDOWN, AdConfig.DEFAULT_INTERSTITIAL_COOLDOWN_SECONDS);
        // Google AdMob Policy Guardrail: Enforce at least 30s cooldown between interstitials
        return Math.max(AdConfig.MIN_SAFE_INTERSTITIAL_COOLDOWN_SECONDS, cooldown);
    }

    public boolean isInterstitialHomeTriggerEnabled() {
        return prefs.getBoolean(KEY_AD_INTERSTITIAL_HOME_ENABLED, AdConfig.DEFAULT_INTERSTITIAL_HOME_TRIGGER);
    }

    public String getBannerPlacement() {
        return prefs.getString(KEY_AD_BANNER_PLACEMENT, "bottom");
    }

    public int getRewardAmount() {
        return prefs.getInt(KEY_AD_REWARD_AMOUNT, AdConfig.DEFAULT_REWARD_AMOUNT);
    }

    public int getAppOpenCooldownSeconds() {
        return prefs.getInt(KEY_AD_APP_OPEN_COOLDOWN, AdConfig.DEFAULT_APP_OPEN_COOLDOWN_SECONDS);
    }

    public String getMaxAdContentRating() {
        return prefs.getString(KEY_AD_CONTENT_RATING, AdConfig.DEFAULT_MAX_AD_CONTENT_RATING);
    }

    public String getTestDeviceId() {
        return prefs.getString(KEY_AD_TEST_DEVICE_ID, "");
    }

    // =========================================================================
    // General App Settings
    // =========================================================================

    public boolean isMaintenanceMode() {
        return prefs.getBoolean(KEY_MAINTENANCE_MODE, false);
    }

    public boolean isForceUpdateRequired(int currentVersionCode) {
        int minVersion = prefs.getInt(KEY_MIN_VERSION_CODE, 1);
        return prefs.getBoolean(KEY_FORCE_UPDATE_REQUIRED, false) && currentVersionCode < minVersion;
    }

    public boolean isUpdateAvailable(int currentVersionCode) {
        int latestVersion = prefs.getInt(KEY_LATEST_VERSION_CODE, 1);
        return currentVersionCode < latestVersion;
    }

    public int getMinVersionCode() {
        return prefs.getInt(KEY_MIN_VERSION_CODE, 1);
    }

    public int getLatestVersionCode() {
        return prefs.getInt(KEY_LATEST_VERSION_CODE, 1);
    }

    public String getLatestVersionName() {
        return prefs.getString(KEY_LATEST_VERSION_NAME, "1.1.0");
    }

    public String getUpdateTitle() {
        return prefs.getString(KEY_UPDATE_TITLE, "নতুন সংস্করণ আপডেট");
    }

    public String getUpdateMessage() {
        return prefs.getString(KEY_UPDATE_MESSAGE, "দীন ওয়ান অ্যাপের নতুন সংস্করণ উপলব্ধ রয়েছে। উন্নত পারফরম্যান্স ও নতুন ফিচারের জন্য অনুগ্রহ করে এখনই আপডেট করুন।");
    }

    public String getUpdateUrl() {
        return prefs.getString(KEY_UPDATE_URL, "https://play.google.com/store/apps/details?id=com.devflux.deenone");
    }

    public void setCustomServerEndpoint(String url) {
        prefs.edit().putString(KEY_SERVER_ENDPOINT, url).apply();
    }

    public String getServerEndpoint() {
        String saved = prefs.getString(KEY_SERVER_ENDPOINT, "");
        if (saved != null && !saved.trim().isEmpty()) {
            return saved;
        }
        return com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiEndpoint(appContext, "get_config.php");
    }

    public String getAnnouncementText() {
        return prefs.getString(KEY_ANNOUNCEMENT_TEXT, "দীন ওয়ান ইসলামিক নলেজ ব্যাটেলে আপনাকে স্বাগতম!");
    }

    public String getDailyAyahText() {
        return prefs.getString(KEY_DAILY_AYAH, "বলুন, আমার প্রতিপালক! আমার জ্ঞান বৃদ্ধি করুন। (সূরা ত্বহা: ১১৪)");
    }

    public String getDailyHadithText() {
        return prefs.getString(KEY_DAILY_HADITH, "যে ব্যক্তি ইলম অন্বেষণে কোনো পথ অবলম্বন করে, আল্লাহ তার জন্য জান্নাতের পথ সহজ করে দেন। (সহীহ মুসলিম)");
    }

    // =========================================================================
    // Async Server Sync (PHP/MySQL)
    // =========================================================================
    public void fetchRemoteConfigAsync(OnConfigFetchedCallback callback) {
        executor.execute(() -> {
            String endpoint = getServerEndpoint();
            if (endpoint == null || (!endpoint.startsWith("https://") && !endpoint.startsWith("http://"))) {
                if (callback != null) callback.onComplete(false);
                return;
            }

            try {
                Request request = new Request.Builder()
                        .url(endpoint)
                        .addHeader("User-Agent", "DeenOne-App/1.0")
                        .addHeader("X-API-KEY", com.devflux.deenone.core.backend.BackendConfigManager.getPhpApiKey(appContext))
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        String json = response.body().string();
                        parseAndApplyConfig(json);
                        if (callback != null) callback.onComplete(true);
                        return;
                    }
                }
            } catch (Exception e) {
                Log.w(TAG, "fetchRemoteConfig failed (using cached values): " + e.getMessage());
            }

            if (callback != null) callback.onComplete(false);
        });
    }

    private void parseAndApplyConfig(String jsonStr) {
        try {
            JsonObject obj = gson.fromJson(jsonStr, JsonObject.class);
            if (obj == null) return;

            SharedPreferences.Editor editor = prefs.edit();

            if (obj.has("is_admin_device")) {
                boolean isAdmin = obj.get("is_admin_device").getAsBoolean();
                editor.putBoolean(KEY_IS_ADMIN_DEVICE, isAdmin);
                if (isAdmin) {
                    editor.putBoolean(KEY_ADS_ENABLED, false);
                }
            }

            if (obj.has("ads_enabled")) {
                boolean ads = obj.get("ads_enabled").getAsBoolean();
                editor.putBoolean(KEY_ADS_ENABLED, ads);
            }
            if (obj.has("maintenance_mode")) {
                editor.putBoolean(KEY_MAINTENANCE_MODE, obj.get("maintenance_mode").getAsBoolean());
            }
            if (obj.has("force_update")) {
                editor.putBoolean(KEY_FORCE_UPDATE_REQUIRED, obj.get("force_update").getAsBoolean());
            }
            if (obj.has("min_version_code")) {
                editor.putInt(KEY_MIN_VERSION_CODE, obj.get("min_version_code").getAsInt());
            }
            if (obj.has("latest_version_code")) {
                editor.putInt(KEY_LATEST_VERSION_CODE, obj.get("latest_version_code").getAsInt());
            }
            if (obj.has("latest_version_name")) {
                editor.putString(KEY_LATEST_VERSION_NAME, obj.get("latest_version_name").getAsString());
            }
            if (obj.has("update_url")) {
                editor.putString(KEY_UPDATE_URL, obj.get("update_url").getAsString());
            }
            if (obj.has("update_title")) {
                editor.putString(KEY_UPDATE_TITLE, obj.get("update_title").getAsString());
            }
            if (obj.has("update_message")) {
                editor.putString(KEY_UPDATE_MESSAGE, obj.get("update_message").getAsString());
            }

            // Comprehensive AdMob Configuration
            if (obj.has("admob")) {
                JsonObject admob = obj.getAsJsonObject("admob");
                if (admob.has("is_admin_device")) {
                    boolean isAdmin = admob.get("is_admin_device").getAsBoolean();
                    editor.putBoolean(KEY_IS_ADMIN_DEVICE, isAdmin);
                    if (isAdmin) {
                        editor.putBoolean(KEY_ADS_ENABLED, false);
                    }
                }
                if (admob.has("enabled")) editor.putBoolean(KEY_ADS_ENABLED, admob.get("enabled").getAsBoolean());
                if (admob.has("mode")) editor.putString(KEY_AD_MODE, admob.get("mode").getAsString());
                if (admob.has("app_id")) editor.putString(KEY_ADMOB_APP_ID, admob.get("app_id").getAsString());

                // Format Toggles
                if (admob.has("banner_enabled")) editor.putBoolean(KEY_AD_BANNER_ENABLED, admob.get("banner_enabled").getAsBoolean());
                if (admob.has("interstitial_enabled")) editor.putBoolean(KEY_AD_INTERSTITIAL_ENABLED, admob.get("interstitial_enabled").getAsBoolean());
                if (admob.has("native_enabled")) editor.putBoolean(KEY_AD_NATIVE_ENABLED, admob.get("native_enabled").getAsBoolean());
                if (admob.has("rewarded_enabled")) editor.putBoolean(KEY_AD_REWARDED_ENABLED, admob.get("rewarded_enabled").getAsBoolean());
                if (admob.has("app_open_enabled")) editor.putBoolean(KEY_AD_APP_OPEN_ENABLED, admob.get("app_open_enabled").getAsBoolean());
                if (admob.has("rewarded_interstitial_enabled")) editor.putBoolean(KEY_AD_REWARDED_INTERSTITIAL_ENABLED, admob.get("rewarded_interstitial_enabled").getAsBoolean());

                // Active Ad Unit IDs
                if (admob.has("banner_id")) editor.putString(KEY_ADMOB_BANNER, admob.get("banner_id").getAsString());
                if (admob.has("interstitial_id")) editor.putString(KEY_ADMOB_INTERSTITIAL, admob.get("interstitial_id").getAsString());
                if (admob.has("native_id")) editor.putString(KEY_ADMOB_NATIVE, admob.get("native_id").getAsString());
                if (admob.has("rewarded_id")) editor.putString(KEY_ADMOB_REWARDED, admob.get("rewarded_id").getAsString());
                if (admob.has("app_open_id")) editor.putString(KEY_ADMOB_APP_OPEN, admob.get("app_open_id").getAsString());
                if (admob.has("rewarded_interstitial_id")) editor.putString(KEY_ADMOB_REWARDED_INTERSTITIAL, admob.get("rewarded_interstitial_id").getAsString());

                // Behavioral Controls
                if (admob.has("frequency_interval")) editor.putInt(KEY_AD_FREQUENCY, admob.get("frequency_interval").getAsInt());
                if (admob.has("interstitial_cooldown")) editor.putInt(KEY_AD_INTERSTITIAL_COOLDOWN, admob.get("interstitial_cooldown").getAsInt());
                if (admob.has("interstitial_home_enabled")) editor.putBoolean(KEY_AD_INTERSTITIAL_HOME_ENABLED, admob.get("interstitial_home_enabled").getAsBoolean());
                if (admob.has("banner_placement")) editor.putString(KEY_AD_BANNER_PLACEMENT, admob.get("banner_placement").getAsString());
                if (admob.has("reward_amount")) editor.putInt(KEY_AD_REWARD_AMOUNT, admob.get("reward_amount").getAsInt());
                if (admob.has("app_open_cooldown")) editor.putInt(KEY_AD_APP_OPEN_COOLDOWN, admob.get("app_open_cooldown").getAsInt());

                // Policy Compliance & Family Protection
                if (admob.has("max_rating")) editor.putString(KEY_AD_CONTENT_RATING, admob.get("max_rating").getAsString());
                if (admob.has("test_device_id")) editor.putString(KEY_AD_TEST_DEVICE_ID, admob.get("test_device_id").getAsString());
            }

            // Daily content overrides
            if (obj.has("daily_content")) {
                JsonObject dc = obj.getAsJsonObject("daily_content");
                if (dc.has("ayah")) editor.putString(KEY_DAILY_AYAH, dc.get("ayah").getAsString());
                if (dc.has("hadith")) editor.putString(KEY_DAILY_HADITH, dc.get("hadith").getAsString());
            }

            // Broadcast announcements
            if (obj.has("announcement")) {
                JsonObject ann = obj.getAsJsonObject("announcement");
                boolean isActive = !ann.has("active") || ann.get("active").getAsBoolean();
                String body = ann.has("body") ? ann.get("body").getAsString() : "";
                if (!body.isEmpty()) {
                    editor.putString(KEY_ANNOUNCEMENT_TEXT, body);
                }

                if (isActive) {
                    String annId = ann.has("id") ? ann.get("id").getAsString() : "";
                    String lastId = prefs.getString(KEY_ANNOUNCEMENT_ID, "");

                    if (!annId.isEmpty() && !annId.equals(lastId)) {
                        String title = ann.has("title") ? ann.get("title").getAsString() : "ঘোষণা";
                        String deepLink = ann.has("deep_link") ? ann.get("deep_link").getAsString() : "feature_history";

                        editor.putString(KEY_ANNOUNCEMENT_ID, annId);

                        NotificationHelper.sendDynamicNotification(
                                appContext,
                                title,
                                body,
                                "admin_announcement",
                                deepLink,
                                "high"
                        );
                    }
                }
            }

            editor.apply();
            Log.d(TAG, "Applied remote config successfully.");

            // Dynamic live update to AdManager without APK/AAB update
            com.devflux.deenone.core.ads.AdManager.getInstance().onConfigurationUpdated(appContext);
        } catch (Exception e) {
            Log.e(TAG, "parseAndApplyConfig error: " + e.getMessage());
        }
    }

    public interface OnConfigFetchedCallback {
        void onComplete(boolean success);
    }
}