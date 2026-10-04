package com.devflux.deenone.core.backend;

import android.content.Context;
import android.content.SharedPreferences;

public class BackendConfigManager {

    private static final String PREF_NAME = "deanone_backend_config";
    private static final String KEY_PROVIDER_TYPE = "backend_provider_type";
    private static final String KEY_PHP_BASE_URL = "php_api_base_url";
    private static final String KEY_PHP_API_KEY = "php_api_key";
    private static final String KEY_FIREBASE_PROJECT_ID = "firebase_project_id";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static BackendProviderType getActiveProvider(Context context) {
        String typeStr = getPrefs(context).getString(KEY_PROVIDER_TYPE, BackendProviderType.HYBRID_STANDALONE.name());
        try {
            return BackendProviderType.valueOf(typeStr);
        } catch (Exception e) {
            return BackendProviderType.HYBRID_STANDALONE;
        }
    }

    public static void setActiveProvider(Context context, BackendProviderType type) {
        getPrefs(context).edit().putString(KEY_PROVIDER_TYPE, type.name()).apply();
    }

    public static final String DEFAULT_PHP_API_BASE_URL = "https://deenone.top/api/";
    public static final String LOCAL_EMULATOR_PHP_API_BASE_URL = "http://10.0.2.2:8000/api/";
    public static final String LOCAL_WIFI_PHP_API_BASE_URL = "http://192.168.100.251:8000/api/";

    private static final String KEY_USE_OFFLINE_PHP = "use_offline_local_php";

    public static boolean isOfflinePhpMode(Context context) {
        // Defaults to true when user activated offline PHP mode
        return context == null || getPrefs(context).getBoolean(KEY_USE_OFFLINE_PHP, true);
    }

    public static void setOfflinePhpMode(Context context, boolean enabled) {
        if (context != null) {
            getPrefs(context).edit().putBoolean(KEY_USE_OFFLINE_PHP, enabled).apply();
        }
    }

    public static boolean isEmulator() {
        return (android.os.Build.FINGERPRINT.startsWith("generic")
                || android.os.Build.FINGERPRINT.startsWith("unknown")
                || android.os.Build.MODEL.contains("google_sdk")
                || android.os.Build.MODEL.contains("Emulator")
                || android.os.Build.MODEL.contains("Android SDK built for x86")
                || android.os.Build.MANUFACTURER.contains("Genymotion")
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(android.os.Build.PRODUCT));
    }

    public static String getPhpApiBaseUrl(Context context) {
        if (isOfflinePhpMode(context)) {
            String saved = (context != null) ? getPrefs(context).getString(KEY_PHP_BASE_URL, null) : null;
            if (saved != null && !saved.trim().isEmpty() && !saved.contains("deenone.top")) {
                return saved;
            }
            return isEmulator() ? LOCAL_EMULATOR_PHP_API_BASE_URL : LOCAL_WIFI_PHP_API_BASE_URL;
        }

        String saved = (context != null) ? getPrefs(context).getString(KEY_PHP_BASE_URL, DEFAULT_PHP_API_BASE_URL) : DEFAULT_PHP_API_BASE_URL;
        if (saved == null || saved.trim().isEmpty() 
                || saved.contains("deanone.com") 
                || saved.contains("deenone.com") 
                || saved.contains("/server_backend/api")) {
            if (context != null) {
                getPrefs(context).edit().putString(KEY_PHP_BASE_URL, DEFAULT_PHP_API_BASE_URL).apply();
            }
            return DEFAULT_PHP_API_BASE_URL;
        }
        return saved;
    }

    public static String getPhpBaseUrl(Context context) {
        String apiBase = getPhpApiBaseUrl(context);
        if (apiBase != null && apiBase.endsWith("/api/")) {
            return apiBase.substring(0, apiBase.length() - 4);
        }
        return apiBase != null ? apiBase : (isOfflinePhpMode(context) ? (isEmulator() ? "http://10.0.2.2:8000/" : "http://192.168.100.251:8000/") : "https://deenone.top/");
    }

    public static void setPhpApiBaseUrl(Context context, String url) {
        getPrefs(context).edit().putString(KEY_PHP_BASE_URL, url).apply();
    }

    public static String getPhpApiEndpoint(Context context, String endpoint) {
        String base = getPhpApiBaseUrl(context);
        if (base == null || base.trim().isEmpty() || base.contains("deanone.com") || base.contains("deenone.com")) {
            base = DEFAULT_PHP_API_BASE_URL;
        }
        if (!base.endsWith("/")) {
            base += "/";
        }
        if (endpoint != null && endpoint.startsWith("/")) {
            endpoint = endpoint.substring(1);
        }
        return base + (endpoint != null ? endpoint : "");
    }

    public static String getPhpApiKey(Context context) {
        return getPrefs(context).getString(KEY_PHP_API_KEY, "deenone_secure_secret_key");
    }

    public static void setPhpApiKey(Context context, String apiKey) {
        getPrefs(context).edit().putString(KEY_PHP_API_KEY, apiKey).apply();
    }

    public static String getFirebaseProjectId(Context context) {
        return getPrefs(context).getString(KEY_FIREBASE_PROJECT_ID, "deanone-islamic-app");
    }

    public static void setFirebaseProjectId(Context context, String projectId) {
        getPrefs(context).edit().putString(KEY_FIREBASE_PROJECT_ID, projectId).apply();
    }
}
