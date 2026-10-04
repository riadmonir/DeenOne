package com.devflux.deenone.features.privacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.core.theme.ThemeManager;

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
 * DEEN ONE - PRIVACY POLICY OFFLINE CACHE MANAGER
 * Provides instant 0ms offline rendering and auto-syncing of Privacy Policy.
 * ==============================================================================
 */
public class PrivacyCacheManager {

    private static final String TAG = "PrivacyCacheManager";
    private static final String PREF_NAME = "deenone_privacy_cache_pref";
    private static final String KEY_LAST_SYNC = "last_privacy_sync_time";
    public static final String OFFLINE_HTML_FILE = "cached_privacy_page.html";

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
     * Resolves the full URL to the Privacy Policy web page
     */
    public static String getPrivacyPageUrl(Context context) {
        String lang = LocaleManager.isBengali(context) ? "bn" : "en";
        boolean isDark = ThemeManager.getSavedThemeMode(context) == ThemeManager.THEME_DARK;
        String theme = isDark ? "dark" : "light";
        return getPrivacyPageUrl(context, lang, theme);
    }

    public static String getPrivacyPageUrl(Context context, String lang, String theme) {
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
        return root + "privacy/index.php?lang=" + (lang != null ? lang : "bn") + "&theme=" + (theme != null ? theme : "dark");
    }

    /**
     * Cache file naming convention per language and theme
     */
    public static String getCacheFileName(String lang, String theme) {
        if (lang == null || lang.isEmpty()) lang = "bn";
        if (theme == null || theme.isEmpty()) theme = "light";
        return "cached_privacy_page_" + lang + "_" + theme + ".html";
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

            Log.d(TAG, "Saved offline privacy HTML cache: " + fileName + " (" + html.length() + " bytes)");
        } catch (Exception e) {
            Log.e(TAG, "Error saving offline HTML: " + e.getMessage());
        }
    }

    /**
     * Preloads and caches the privacy policy pages asynchronously in background
     */
    public static void preloadPrivacyData(Context context) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        executor.execute(() -> {
            try {
                // Ensure initial offline seed cache exists immediately on disk
                ensureSeedCacheExists(appContext);

                if (!isOnline(appContext)) {
                    Log.d(TAG, "Offline: skipping background privacy prefetch.");
                    return;
                }

                boolean isBn = LocaleManager.isBengali(appContext);
                boolean isDark = ThemeManager.getSavedThemeMode(appContext) == ThemeManager.THEME_DARK;
                String currentLang = isBn ? "bn" : "en";
                String currentTheme = isDark ? "dark" : "light";

                // 1. Prefetch HTML for current user config first
                prefetchHtmlVariant(appContext, currentLang, currentTheme);

                // 2. Prefetch opposite theme & opposite language in background
                prefetchHtmlVariant(appContext, currentLang, isDark ? "light" : "dark");
                prefetchHtmlVariant(appContext, isBn ? "en" : "bn", currentTheme);
                prefetchHtmlVariant(appContext, isBn ? "en" : "bn", isDark ? "light" : "dark");

            } catch (Exception e) {
                Log.w(TAG, "Background privacy prefetch notice: " + e.getMessage());
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

    private static void prefetchHtmlVariant(Context context, String lang, String theme) {
        try {
            String pageUrl = getPrivacyPageUrl(context, lang, theme);
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

            // 4. Try any cached privacy html file in filesDir
            File[] files = appCtx.getFilesDir().listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().startsWith("cached_privacy_page") && f.getName().endsWith(".html") && f.length() > 500) {
                        return readFileToString(f);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving cached HTML: " + e.getMessage());
        }

        // 5. Authentic fallback
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
                    if (f.getName().startsWith("cached_privacy_page") && f.getName().endsWith(".html") && f.length() > 500) {
                        return true;
                    }
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    /**
     * Fallback offline HTML with 100% authentic DeenOne Privacy Policy
     */
    public static String generateFallbackOfflineHtml(Context context, String lang, String theme) {
        boolean isBn = "bn".equalsIgnoreCase(lang);
        boolean isDark = "dark".equalsIgnoreCase(theme);

        String bgPage = isDark ? "#0b1120" : "#ffffff";
        String textHead = isDark ? "#f8fafc" : "#0f172a";
        String textBody = isDark ? "#cbd5e1" : "#334155";
        String textMuted = isDark ? "#94a3b8" : "#64748b";
        String slashColor = isDark ? "#10b981" : "#059669";
        String linkColor = isDark ? "#38bdf8" : "#0284c7";
        String codeBg = isDark ? "#1e293b" : "#f1f5f9";

        String title = isBn ? "প্রাইভেসি পলিসি" : "PRIVACY POLICY";
        String updated = isBn ? "সর্বশেষ হালনাগাদ: ২০২৬" : "Last update: September 2026";

        String contentBn = "<p>এই <strong>Privacy Policy</strong> ব্যাখ্যা করে কীভাবে DeenOne (\"<strong>DeenOne</strong>\", \"<strong>আমরা</strong>\" বা \"<strong>আমাদের</strong>\") আপনার ব্যক্তিগত তথ্য সংগ্রহ, ব্যবহার এবং সুরক্ষিত রাখে, যা আপনি আমাদের ওয়েবসাইট <a href=\"https://deenone.top\">https://deenone.top</a> এবং সংশ্লিষ্ট ডিজিটাল সেবা ও মোবাইল অ্যাপ্লিকেশন (একত্রে \"<strong>Services</strong>\") ব্যবহারের সময় প্রদান করেন।</p>\n" +
                "<p>এই Privacy Policy <strong>নিম্নোক্ত ক্ষেত্রসমূহে প্রযোজ্য নয় :</strong></p>\n" +
                "<ul>\n" +
                "  <li><strong>অফলাইনে সংগৃহীত তথ্য;</strong> অথবা কোনো অননুমোদিত তৃতীয় পক্ষের অ্যাপ্লিকেশন বা কন্টেন্টের (বিজ্ঞাপন সহ) মাধ্যমে সংগৃহীত তথ্য যা আমাদের প্ল্যাটফর্মের সাথে লিংক করা থাকতে পারে।</li>\n" +
                "  <li><strong>তৃতীয় পক্ষের পেমেন্ট প্রসেসর দ্বারা প্রক্রিয়াকৃত তথ্য।</strong> কিছু সেবার জন্য ব্যবহারকারীকে সরাসরি তৃতীয় পক্ষের বিশ্বস্ত পেমেন্ট প্রসেসরের মাধ্যমে পেমেন্ট তথ্য প্রদান করতে হতে পারে।</li>\n" +
                "  <li><strong>সোশ্যাল মিডিয়া ইন্টিগ্রেশন।</strong> ব্যবহারকারী চাইলে তাদের সোশ্যাল মিডিয়া অ্যাকাউন্ট যুক্ত করতে পারেন। অ্যাকাউন্ট যুক্ত থাকলে সংশ্লিষ্ট প্ল্যাটফর্মের গোপনীয়তা নীতি অনুযায়ী তথ্য শেয়ার হতে পারে।</li>\n" +
                "</ul>\n" +
                "<h3>১. আমরা কী কী তথ্য সংগ্রহ করি</h3>\n" +
                "<p>সঠিক নামাজের সময়সূচি, কিবলা দিক এবং বিশুদ্ধ ইসলামিক লাইফস্টাইল ট্র্যাকিং নিশ্চিত করতে আমরা শুধুমাত্র প্রয়োজনীয় তথ্য সংগ্রহ করি:</p>\n" +
                "<ul>\n" +
                "  <li><strong>প্রোফাইল তথ্য:</strong> নাম, ইমেইল বা ফোন নম্বর (অ্যাকাউন্ট ব্যাকআপ ও ডেটা সিঙ্কের জন্য)।</li>\n" +
                "  <li><strong>লোকেশন ডাটা:</strong> শুধুমাত্র সালাতের ওয়াক্ত ও কিবলা কম্পাসের দিক নির্ধারণে ডিভাইসে ব্যবহৃত হয়। এটি সার্ভারে স্থায়ীভাবে জমা বা ট্র্যাক করা হয় না।</li>\n" +
                "  <li><strong>ইবাদত ও সালাত ট্র্যাকিং:</strong> দৈনিক সালাত, কুরআন তিলাওয়াত ও তাসবীহ জিকিরের পরিসংখ্যান (যা ১০০% ব্যক্তিগত ও সুরক্ষিত)।</li>\n" +
                "  <li><strong>রক্তদান নেটওয়ার্ক:</strong> স্বেচ্ছায় নিবন্ধিত রক্তদাতাদের রক্তের গ্রুপ, জেলা ও যোগাযোগের নম্বর শুধুমাত্র জরুরি প্রয়োজনে প্রদর্শিত হয়।</li>\n" +
                "</ul>\n" +
                "<h3>২. তথ্যের নিরাপত্তা ও সুরক্ষা</h3>\n" +
                "<p>আপনার সমস্ত তথ্য আধুনিক SSL/TLS এনক্রিপশন প্রোটোকলের মাধ্যমে সুরক্ষিত থাকে। বাণিজ্যিক বিজ্ঞাপনদাতার কাছে তথ্য বিক্রি বা হস্তান্তর সম্পূর্ণ নিষিদ্ধ।</p>\n" +
                "<h3>৩. অ্যাকাউন্ট ও ডেটা স্থায়ীভাবে মুছে ফেলা (Data Deletion)</h3>\n" +
                "<p>ব্যবহারকারী যেকোনো সময় অ্যাপের প্রোফাইল সেটিংস থেকে অথবা <code>privacy@deenone.top</code> এ যোগাযোগ করে তার সমস্ত সংরক্ষিত ডেটা স্থায়ীভাবে মুছে ফেলতে পারেন।</p>";

        String contentEn = "<p>This <strong>Privacy Policy</strong> explains how DeenOne (\"<strong>DeenOne</strong>\", \"<strong>us</strong>\" or \"<strong>we</strong>\"), collects, uses and protects your personal information, which we may collect from you or that you may provide when you visit any of our websites, including <a href=\"https://deenone.top\">https://deenone.top</a> and other digital properties that link to this Privacy Policy (our \"<strong>Website(s)</strong>\") or otherwise use our Website(s) or applications (collectively referred to as services the \"<strong>Services</strong>\").</p>\n" +
                "<p>This Privacy Policy <strong>does not apply to :</strong></p>\n" +
                "<ul>\n" +
                "  <li><strong>Information we may collect from you offline;</strong> or information collected by any third-party, including through any application or content (including advertising) that may link to or be accessible from or through the Websites.</li>\n" +
                "  <li><strong>Payment Information Processed by third-parties.</strong> Certain Services may require users to provide payment information directly to third-party payment processors.</li>\n" +
                "  <li><strong>Social Media Integration.</strong> Users may link their account with third-party social media platforms. When accounts are linked, the social media platform may share certain data with DeenOne as per its privacy policy.</li>\n" +
                "</ul>\n" +
                "<h3>1. Information We Collect</h3>\n" +
                "<p>To deliver an authentic, distraction-free Islamic lifestyle experience and accurate prayer timings, DeenOne collects minimal necessary information:</p>\n" +
                "<ul>\n" +
                "  <li><strong>Account & Profile:</strong> Name, phone number or email for account authentication, streak progress, and cloud backup across devices.</li>\n" +
                "  <li><strong>Location Data:</strong> Accessed on-device strictly to calculate accurate prayer times, sunrise/sunset, and Qibla compass direction. Location coordinates are never sold or permanently stored on our servers.</li>\n" +
                "  <li><strong>Worship & Habits Activity:</strong> Daily Salah logs, Quran reading progress, and Tasbih counts stored with end-to-end security.</li>\n" +
                "  <li><strong>Blood Donor Registry:</strong> Blood group, district, and contact numbers shared voluntarily for emergency blood requests.</li>\n" +
                "</ul>\n" +
                "<h3>2. Data Security & Encryption</h3>\n" +
                "<p>All sensitive transmissions are secured using SSL/TLS 256-bit encryption. We never sell, rent, or trade user personal information to third-party commercial advertisers.</p>\n" +
                "<h3>3. Data Retention & Account Deletion</h3>\n" +
                "<p>You have full ownership of your data. You may request permanent deletion of your account and all associated data at any time directly through the app settings or by contacting <code>privacy@deenone.top</code>.</p>";

        String content = isBn ? contentBn : contentEn;

        return "<!DOCTYPE html>\n" +
                "<html lang=\"" + (isBn ? "bn" : "en") + "\" data-theme=\"" + (isDark ? "dark" : "light") + "\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=5.0\">\n" +
                "  <title>" + title + " - DeenOne</title>\n" +
                "  <style>\n" +
                "    * { box-sizing: border-box; margin: 0; padding: 0; }\n" +
                "    body { font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; background: " + bgPage + "; color: " + textBody + "; padding: 24px 20px 60px; line-height: 1.65; }\n" +
                "    .container { max-width: 900px; margin: 0 auto; }\n" +
                "    .header { margin-bottom: 24px; }\n" +
                "    .title { font-size: 24px; font-weight: 800; color: " + textHead + "; margin-bottom: 8px; text-transform: uppercase; }\n" +
                "    .slash { color: " + slashColor + "; font-weight: 900; margin-right: 4px; }\n" +
                "    .updated { font-size: 14px; color: " + textMuted + "; font-weight: 500; margin-bottom: 20px; }\n" +
                "    p { margin-bottom: 20px; font-size: 15px; }\n" +
                "    strong, b { font-weight: 700; color: " + textHead + "; }\n" +
                "    a { color: " + linkColor + "; text-decoration: underline; text-underline-offset: 2px; }\n" +
                "    ul, ol { margin: 16px 0 24px 20px; }\n" +
                "    li { margin-bottom: 14px; padding-left: 6px; }\n" +
                "    h2, h3, h4 { color: " + textHead + "; font-weight: 800; margin-top: 32px; margin-bottom: 14px; }\n" +
                "    h3 { font-size: 17px; }\n" +
                "    code { font-family: monospace; background: " + codeBg + "; padding: 2px 6px; border-radius: 4px; color: " + textHead + "; }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <main class=\"container\">\n" +
                "    <header class=\"header\">\n" +
                "      <h1 class=\"title\"><span class=\"slash\">/</span>" + title + "</h1>\n" +
                "      <p class=\"updated\">" + updated + "</p>\n" +
                "    </header>\n" +
                "    <article class=\"content\">\n" +
                content +
                "    </article>\n" +
                "  </main>\n" +
                "</body>\n" +
                "</html>";
    }
}
