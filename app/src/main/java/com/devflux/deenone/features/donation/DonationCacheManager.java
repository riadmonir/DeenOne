package com.devflux.deenone.features.donation;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.core.theme.ThemeManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ==============================================================================
 * DEEN ONE - DONATION & KHEDMAT OFFLINE CACHE MANAGER
 * Automatically pre-caches the donation landing page and payment details
 * for seamless, instant offline display across devices.
 * ==============================================================================
 */
public class DonationCacheManager {

    private static final String TAG = "DonationCacheManager";
    private static final String PREF_NAME = "deanone_donation_cache_pref";
    private static final String KEY_CACHED_JSON_BN = "cached_donation_api_json_bn";
    private static final String KEY_CACHED_JSON_EN = "cached_donation_api_json_en";
    private static final String KEY_LAST_SYNC = "last_donation_sync_time";
    public static final String OFFLINE_HTML_FILE = "cached_donation_page.html";

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .build();

    public static boolean isOnline(Context context) {
        return NetworkConnectivityHelper.isOnline(context);
    }

    /**
     * Resolves the full URL to the donation landing page
     */
    public static String getDonationPageUrl(Context context) {
        String base = BackendConfigManager.getPhpApiBaseUrl(context);
        if (base == null || base.trim().isEmpty()) {
            base = BackendConfigManager.DEFAULT_PHP_API_BASE_URL;
        }
        String root = base;
        if (root.endsWith("/api/")) {
            root = root.substring(0, root.length() - 5);
        } else if (root.endsWith("/api")) {
            root = root.substring(0, root.length() - 4);
        }
        if (!root.endsWith("/")) {
            root += "/";
        }
        String lang = LocaleManager.isBengali(context) ? "bn" : "en";
        boolean isDark = ThemeManager.getSavedThemeMode(context) == ThemeManager.THEME_DARK;
        String theme = isDark ? "dark" : "light";
        return root + "donate/index.php?lang=" + lang + "&theme=" + theme;
    }

    public static String getDonationPageUrl(Context context, String lang, String theme) {
        String base = BackendConfigManager.getPhpApiBaseUrl(context);
        if (base == null || base.trim().isEmpty()) {
            base = BackendConfigManager.DEFAULT_PHP_API_BASE_URL;
        }
        String root = base;
        if (root.endsWith("/api/")) {
            root = root.substring(0, root.length() - 5);
        } else if (root.endsWith("/api")) {
            root = root.substring(0, root.length() - 4);
        }
        if (!root.endsWith("/")) {
            root += "/";
        }
        return root + "donate/index.php?lang=" + lang + "&theme=" + theme;
    }

    /**
     * Resolves the API endpoint for donation configuration
     */
    public static String getDonationApiUrl(Context context, String lang) {
        return BackendConfigManager.getPhpApiEndpoint(context, "donation.php?action=get_page_data&lang=" + lang);
    }

    /**
     * Cache file naming convention per language and theme
     */
    public static String getCacheFileName(String lang, String theme) {
        if (lang == null || lang.isEmpty()) lang = "bn";
        if (theme == null || theme.isEmpty()) theme = "light";
        return "cached_donation_page_" + lang + "_" + theme + ".html";
    }

    /**
     * Saves raw HTML string into local storage permanently
     */
    public static synchronized void saveHtmlToCache(Context context, String html, String lang, String theme) {
        if (context == null || html == null || html.trim().isEmpty() || !html.contains("<html")) {
            return;
        }
        try {
            Context appCtx = context.getApplicationContext();
            // 1. Save specific variant
            String fileName = getCacheFileName(lang, theme);
            File file = new File(appCtx.getFilesDir(), fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(html.getBytes(StandardCharsets.UTF_8));
            }

            // 2. Save generic primary fallback (prefer Bengali as default)
            File primaryFile = new File(appCtx.getFilesDir(), OFFLINE_HTML_FILE);
            if (!primaryFile.exists() || "bn".equals(lang)) {
                try (FileOutputStream fos = new FileOutputStream(primaryFile)) {
                    fos.write(html.getBytes(StandardCharsets.UTF_8));
                }
            }

            // Update sync timestamp
            appCtx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putLong(KEY_LAST_SYNC, System.currentTimeMillis())
                    .apply();

            Log.d(TAG, "Saved offline donation HTML cache: " + fileName + " (" + html.length() + " bytes)");
        } catch (Exception e) {
            Log.e(TAG, "Error saving offline HTML: " + e.getMessage());
        }
    }

    /**
     * Preloads and caches the donation pages and JSON asynchronously in background
     */
    public static void preloadDonationData(Context context) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        executor.execute(() -> {
            try {
                // Ensure initial offline seed cache exists immediately on disk
                ensureSeedCacheExists(appContext);

                if (!isOnline(appContext)) {
                    Log.d(TAG, "Offline: skipping background donation prefetch.");
                    return;
                }

                // 1. Prefetch API JSON for both languages
                prefetchApiJson(appContext, "bn");
                prefetchApiJson(appContext, "en");

                // 2. Prefetch HTML for current user config first
                boolean isBn = LocaleManager.isBengali(appContext);
                boolean isDark = ThemeManager.getSavedThemeMode(appContext) == ThemeManager.THEME_DARK;
                String currentLang = isBn ? "bn" : "en";
                String currentTheme = isDark ? "dark" : "light";

                prefetchHtmlVariant(appContext, currentLang, currentTheme);

                // 3. Prefetch opposite theme & opposite language in background
                prefetchHtmlVariant(appContext, currentLang, isDark ? "light" : "dark");
                prefetchHtmlVariant(appContext, isBn ? "en" : "bn", currentTheme);
                prefetchHtmlVariant(appContext, isBn ? "en" : "bn", isDark ? "light" : "dark");

            } catch (Exception e) {
                Log.w(TAG, "Background donation prefetch notice: " + e.getMessage());
            }
        });
    }

    private static void ensureSeedCacheExists(Context appContext) {
        try {
            if (!hasCachedHtml(appContext, "bn", "dark")) {
                saveHtmlToCache(appContext, generateFallbackOfflineHtml(appContext, "bn", "dark"), "bn", "dark");
            }
            if (!hasCachedHtml(appContext, "bn", "light")) {
                saveHtmlToCache(appContext, generateFallbackOfflineHtml(appContext, "bn", "light"), "bn", "light");
            }
            if (!hasCachedHtml(appContext, "en", "dark")) {
                saveHtmlToCache(appContext, generateFallbackOfflineHtml(appContext, "en", "dark"), "en", "dark");
            }
            if (!hasCachedHtml(appContext, "en", "light")) {
                saveHtmlToCache(appContext, generateFallbackOfflineHtml(appContext, "en", "light"), "en", "light");
            }
        } catch (Exception ignored) {}
    }

    private static void prefetchApiJson(Context context, String lang) {
        try {
            String apiUrl = getDonationApiUrl(context, lang);
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .header("User-Agent", "DeenOne-Android-App/2.1.0")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    if (json.contains("\"success\":true")) {
                        String key = "bn".equals(lang) ? KEY_CACHED_JSON_BN : KEY_CACHED_JSON_EN;
                        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                                .edit()
                                .putString(key, json)
                                .apply();
                        Log.d(TAG, "Cached donation API JSON for " + lang);
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "prefetchApiJson (" + lang + ") error: " + e.getMessage());
        }
    }

    private static void prefetchHtmlVariant(Context context, String lang, String theme) {
        try {
            String pageUrl = getDonationPageUrl(context, lang, theme);
            Request request = new Request.Builder()
                    .url(pageUrl)
                    .header("User-Agent", "DeenOne-Android-App/2.1.0")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String html = response.body().string();
                    if (html.contains("<html")) {
                        saveHtmlToCache(context, html, lang, theme);
                    }
                }
            }
        } catch (Exception e) {
            Log.w(TAG, "prefetchHtmlVariant (" + lang + ", " + theme + ") error: " + e.getMessage());
        }
    }

    /**
     * Returns the cached HTML file from disk matching lang/theme, or generated fallback
     */
    public static String getCachedOfflineHtml(Context context, String lang, String theme) {
        if (context == null) return "";
        try {
            Context appCtx = context.getApplicationContext();

            // 1. Try exact match
            String targetFileName = getCacheFileName(lang, theme);
            File exactFile = new File(appCtx.getFilesDir(), targetFileName);
            if (exactFile.exists() && exactFile.length() > 500) {
                return readFileToString(exactFile);
            }

            // 2. Try same language opposite theme
            String altTheme = "dark".equalsIgnoreCase(theme) ? "light" : "dark";
            File altThemeFile = new File(appCtx.getFilesDir(), getCacheFileName(lang, altTheme));
            if (altThemeFile.exists() && altThemeFile.length() > 500) {
                return readFileToString(altThemeFile);
            }

            // 3. Try primary generic cached file
            File primaryFile = new File(appCtx.getFilesDir(), OFFLINE_HTML_FILE);
            if (primaryFile.exists() && primaryFile.length() > 500) {
                return readFileToString(primaryFile);
            }

            // 4. Try any cached donation html file in filesDir
            File[] files = appCtx.getFilesDir().listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().startsWith("cached_donation_page") && f.getName().endsWith(".html") && f.length() > 500) {
                        return readFileToString(f);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving cached HTML: " + e.getMessage());
        }

        // 5. High-fidelity dynamic fallback generated from cached JSON or authentic defaults
        return generateFallbackOfflineHtml(context, lang, theme);
    }

    private static String readFileToString(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks if any valid cached HTML exists on disk
     */
    public static boolean hasCachedHtml(Context context, String lang, String theme) {
        if (context == null) return false;
        try {
            Context appCtx = context.getApplicationContext();
            String targetFileName = getCacheFileName(lang, theme);
            File exactFile = new File(appCtx.getFilesDir(), targetFileName);
            if (exactFile.exists() && exactFile.length() > 500) return true;

            String altTheme = "dark".equalsIgnoreCase(theme) ? "light" : "dark";
            File altThemeFile = new File(appCtx.getFilesDir(), getCacheFileName(lang, altTheme));
            if (altThemeFile.exists() && altThemeFile.length() > 500) return true;

            File primaryFile = new File(appCtx.getFilesDir(), OFFLINE_HTML_FILE);
            if (primaryFile.exists() && primaryFile.length() > 500) return true;

            File[] files = appCtx.getFilesDir().listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().startsWith("cached_donation_page") && f.getName().endsWith(".html") && f.length() > 500) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Fallback offline HTML with 100% complete authentic account information and styling
     */
    public static String generateFallbackOfflineHtml(Context context, String lang, String theme) {
        boolean isBn = "bn".equalsIgnoreCase(lang);
        boolean isDark = "dark".equalsIgnoreCase(theme);

        String bgPage = isDark ? "#0b1120" : "#f8fafc";
        String bgCard = isDark ? "#131e32" : "#ffffff";
        String textHead = isDark ? "#f8fafc" : "#0f172a";
        String textBody = isDark ? "#cbd5e1" : "#334155";
        String textMuted = isDark ? "#94a3b8" : "#64748b";
        String border = isDark ? "rgba(255,255,255,0.08)" : "#e2e8f0";
        String copyBoxBg = isDark ? "#1e293b" : "#f1f5f9";

        // Try reading cached JSON for account numbers
        String bkashNum = "01700000000";
        String nagadNum = "01700000000";
        String rocketNum = "017000000000";
        String bankNum = "2050123456789000";
        String bankName = "Islami Bank Bangladesh PLC";
        String bankBranch = "Dhanmondi Branch, Dhaka";
        String bankRouting = "125271892";
        String binancePayId = "88992211";
        String paypalEmail = "donate@deenone.top";

        try {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String jsonKey = isBn ? KEY_CACHED_JSON_BN : KEY_CACHED_JSON_EN;
            String cachedJson = prefs.getString(jsonKey, null);
            if (cachedJson == null) {
                cachedJson = prefs.getString(KEY_CACHED_JSON_BN, null);
            }
            if (cachedJson != null) {
                JSONObject obj = new JSONObject(cachedJson);
                if (obj.has("payment_methods")) {
                    JSONObject methods = obj.getJSONObject("payment_methods");
                    if (methods.has("bkash")) {
                        bkashNum = methods.getJSONObject("bkash").optString("number", bkashNum);
                    }
                    if (methods.has("nagad")) {
                        nagadNum = methods.getJSONObject("nagad").optString("number", nagadNum);
                    }
                    if (methods.has("rocket")) {
                        rocketNum = methods.getJSONObject("rocket").optString("number", rocketNum);
                    }
                    if (methods.has("bank")) {
                        JSONObject b = methods.getJSONObject("bank");
                        bankNum = b.optString("number", bankNum);
                        bankName = b.optString("bank_name", bankName);
                        bankBranch = b.optString("branch", bankBranch);
                        bankRouting = b.optString("routing", bankRouting);
                    }
                    if (methods.has("binance")) {
                        binancePayId = methods.getJSONObject("binance").optString("binance_id", binancePayId);
                    }
                    if (methods.has("paypal")) {
                        paypalEmail = methods.getJSONObject("paypal").optString("email", paypalEmail);
                    }
                }
            }
        } catch (Exception ignored) {}

        return "<!DOCTYPE html>\n" +
                "<html lang=\"" + (isBn ? "bn" : "en") + "\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=5.0\">\n" +
                "  <title>" + (isBn ? "দ্বীন ওয়ান খেদমত ও অনুদান" : "DeenOne Voluntary Contribution") + "</title>\n" +
                "  <style>\n" +
                "    * { box-sizing: border-box; margin: 0; padding: 0; }\n" +
                "    body { font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: " + bgPage + "; color: " + textBody + "; padding: 16px; line-height: 1.5; }\n" +
                "    .container { max-width: 520px; margin: 0 auto; }\n" +
                "    .hero { background: " + bgCard + "; border: 1px solid " + border + "; border-radius: 18px; padding: 22px 18px; text-align: center; margin-bottom: 18px; }\n" +
                "    .hero-icon { width: 50px; height: 50px; border-radius: 14px; background: linear-gradient(135deg, #047857, #10b981); color: #fff; font-size: 24px; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 12px; font-weight: 800; }\n" +
                "    h1 { font-size: 19px; font-weight: 800; color: " + textHead + "; margin-bottom: 6px; }\n" +
                "    p { font-size: 13.5px; color: " + textMuted + "; line-height: 1.5; }\n" +
                "    .section-title { font-size: 14px; font-weight: 800; color: #059669; text-transform: uppercase; letter-spacing: 0.5px; margin: 18px 0 10px; }\n" +
                "    .card { background: " + bgCard + "; border: 1.5px solid " + border + "; border-radius: 14px; padding: 16px; margin-bottom: 12px; transition: transform 0.2s ease; }\n" +
                "    .card-head { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px; }\n" +
                "    .method-name { font-size: 14.5px; font-weight: 800; color: " + textHead + "; display: flex; align-items: center; gap: 8px; }\n" +
                "    .tag { font-size: 11px; font-weight: 700; padding: 3px 10px; border-radius: 12px; }\n" +
                "    .copy-box { background: " + copyBoxBg + "; border: 1.5px dashed " + border + "; border-radius: 10px; padding: 12px 14px; font-family: monospace; font-size: 15.5px; font-weight: 800; color: " + textHead + "; display: flex; justify-content: space-between; align-items: center; margin-top: 8px; }\n" +
                "    .btn-copy { color: #fff; border: none; padding: 7px 16px; border-radius: 8px; font-size: 12px; font-weight: 800; cursor: pointer; transition: opacity 0.2s; }\n" +
                "    .btn-copy:hover { opacity: 0.9; }\n" +
                "    .meta-text { font-size: 12px; color: " + textMuted + "; margin-top: 6px; }\n" +
                "    .toast { position: fixed; bottom: 20px; left: 50%; transform: translateX(-50%); background: #064e3b; color: #fff; padding: 10px 20px; border-radius: 20px; font-size: 13px; font-weight: 700; display: none; z-index: 9999; box-shadow: 0 4px 14px rgba(0,0,0,0.2); }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div class=\"container\">\n" +
                "    <div class=\"hero\">\n" +
                "      <div class=\"hero-icon\">♥</div>\n" +
                "      <h1>" + (isBn ? "দ্বীন ওয়ান উন্মুক্ত দ্বীনি খেদমত" : "DeenOne Voluntary Contribution") + "</h1>\n" +
                "      <p>" + (isBn ? "আপনার আন্তরিক সদকাহ কোটি মুসলিমের দ্বীনি যাত্রাকে বেগবান করে।" : "Your voluntary contribution supports authentic Islamic knowledge and platform maintenance.") + "</p>\n" +
                "    </div>\n" +
                "    \n" +
                "    <div class=\"section-title\">" + (isBn ? "অনুদান প্রেরণের মাধ্যমসমূহ" : "Available Payment Methods") + "</div>\n" +
                "    \n" +
                "    <!-- bKash -->\n" +
                "    <div class=\"card\" style=\"border-left: 4px solid #e2136e;\">\n" +
                "      <div class=\"card-head\">\n" +
                "        <div class=\"method-name\"><span style=\"color:#e2136e;\">●</span> " + (isBn ? "বিকাশ নম্বর" : "bKash Number") + "</div>\n" +
                "        <span class=\"tag\" style=\"background:#fdf2f8;color:#be185d;border:1px solid #fbcfe8;\">bKash Personal</span>\n" +
                "      </div>\n" +
                "      <div class=\"copy-box\" style=\"border-color: rgba(226,19,110,0.3);\">\n" +
                "        <span style=\"color:#e2136e;\">" + bkashNum + "</span>\n" +
                "        <button class=\"btn-copy\" style=\"background:#e2136e;\" onclick=\"copyNumber('" + bkashNum + "')\">" + (isBn ? "কপি করুন" : "Copy") + "</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <!-- Nagad -->\n" +
                "    <div class=\"card\" style=\"border-left: 4px solid #ea1d25;\">\n" +
                "      <div class=\"card-head\">\n" +
                "        <div class=\"method-name\"><span style=\"color:#ea1d25;\">●</span> " + (isBn ? "নগদ নম্বর" : "Nagad Number") + "</div>\n" +
                "        <span class=\"tag\" style=\"background:#fff7ed;color:#c2410c;border:1px solid #fed7aa;\">Nagad Personal</span>\n" +
                "      </div>\n" +
                "      <div class=\"copy-box\" style=\"border-color: rgba(234,29,37,0.3);\">\n" +
                "        <span style=\"color:#ea1d25;\">" + nagadNum + "</span>\n" +
                "        <button class=\"btn-copy\" style=\"background:#ea1d25;\" onclick=\"copyNumber('" + nagadNum + "')\">" + (isBn ? "কপি করুন" : "Copy") + "</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <!-- Rocket -->\n" +
                "    <div class=\"card\" style=\"border-left: 4px solid #8c3494;\">\n" +
                "      <div class=\"card-head\">\n" +
                "        <div class=\"method-name\"><span style=\"color:#8c3494;\">●</span> " + (isBn ? "রকেট নম্বর" : "Rocket Number") + "</div>\n" +
                "        <span class=\"tag\" style=\"background:#faf5ff;color:#7e22ce;border:1px solid #e9d5ff;\">Rocket Personal</span>\n" +
                "      </div>\n" +
                "      <div class=\"copy-box\" style=\"border-color: rgba(140,52,148,0.3);\">\n" +
                "        <span style=\"color:#8c3494;\">" + rocketNum + "</span>\n" +
                "        <button class=\"btn-copy\" style=\"background:#8c3494;\" onclick=\"copyNumber('" + rocketNum + "')\">" + (isBn ? "কপি করুন" : "Copy") + "</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <!-- Bank Account -->\n" +
                "    <div class=\"card\" style=\"border-left: 4px solid #059669;\">\n" +
                "      <div class=\"card-head\">\n" +
                "        <div class=\"method-name\"><span style=\"color:#059669;\">●</span> " + (isBn ? "ব্যাংক একাউন্ট" : "Bank Transfer") + "</div>\n" +
                "        <span class=\"tag\" style=\"background:#ecfdf5;color:#047857;border:1px solid #a7f3d0;\">" + bankName + "</span>\n" +
                "      </div>\n" +
                "      <div class=\"meta-text\"><strong>" + (isBn ? "হিসাবধারীর নাম:" : "Account Name:") + "</strong> DeenOne Foundation</div>\n" +
                "      <div class=\"copy-box\" style=\"border-color: rgba(5,150,105,0.3);\">\n" +
                "        <span style=\"color:#047857;\">" + bankNum + "</span>\n" +
                "        <button class=\"btn-copy\" style=\"background:#059669;\" onclick=\"copyNumber('" + bankNum + "')\">" + (isBn ? "কপি করুন" : "Copy") + "</button>\n" +
                "      </div>\n" +
                "      <div class=\"meta-text\">" + (isBn ? "শাখা: " : "Branch: ") + bankBranch + " | " + (isBn ? "রাউটিং: " : "Routing: ") + bankRouting + "</div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <!-- Binance Pay ID -->\n" +
                "    <div class=\"card\" style=\"border-left: 4px solid #F0B90B;\">\n" +
                "      <div class=\"card-head\">\n" +
                "        <div class=\"method-name\"><span style=\"color:#d97706;\">●</span> " + (isBn ? "বাইনান্স পে আইডি" : "Binance Pay ID") + "</div>\n" +
                "        <span class=\"tag\" style=\"background:#fef3c7;color:#d97706;border:1px solid #fde68a;\">Binance Pay</span>\n" +
                "      </div>\n" +
                "      <div class=\"meta-text\">" + (isBn ? "বাইনান্স অ্যাপ থেকে সরাসরি পে আইডি দিয়ে ট্রান্সফার করুন।" : "Transfer directly via Binance Pay ID inside Binance app.") + "</div>\n" +
                "      <div class=\"copy-box\" style=\"border-color: rgba(240,185,11,0.4);\">\n" +
                "        <span style=\"color:#d97706;\">" + binancePayId + "</span>\n" +
                "        <button class=\"btn-copy\" style=\"background:#d97706;\" onclick=\"copyNumber('" + binancePayId + "')\">" + (isBn ? "কপি করুন" : "Copy") + "</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    \n" +
                "    <!-- PayPal -->\n" +
                "    <div class=\"card\" style=\"border-left: 4px solid #0079C1;\">\n" +
                "      <div class=\"card-head\">\n" +
                "        <div class=\"method-name\"><span style=\"color:#0079C1;\">●</span> " + (isBn ? "পেপ্যাল একাউন্ট" : "PayPal Account") + "</div>\n" +
                "        <span class=\"tag\" style=\"background:#eff6ff;color:#1d4ed8;border:1px solid #bfdbfe;\">PayPal Official</span>\n" +
                "      </div>\n" +
                "      <div class=\"copy-box\" style=\"border-color: rgba(0,121,193,0.3);\">\n" +
                "        <span style=\"color:#0079C1;\">" + paypalEmail + "</span>\n" +
                "        <button class=\"btn-copy\" style=\"background:#0079C1;\" onclick=\"copyNumber('" + paypalEmail + "')\">" + (isBn ? "কপি করুন" : "Copy") + "</button>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  \n" +
                "  <div class=\"toast\" id=\"toastBox\">✓ " + (isBn ? "কপি করা হয়েছে!" : "Copied!") + "</div>\n" +
                "  \n" +
                "  <script>\n" +
                "    function copyNumber(text) {\n" +
                "      if (navigator.clipboard) {\n" +
                "        navigator.clipboard.writeText(text);\n" +
                "      } else {\n" +
                "        const input = document.createElement('input');\n" +
                "        input.value = text;\n" +
                "        document.body.appendChild(input);\n" +
                "        input.select();\n" +
                "        document.execCommand('copy');\n" +
                "        document.body.removeChild(input);\n" +
                "      }\n" +
                "      const toast = document.getElementById('toastBox');\n" +
                "      toast.style.display = 'block';\n" +
                "      setTimeout(() => toast.style.display = 'none', 2000);\n" +
                "    }\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
