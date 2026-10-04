package com.devflux.deenone.features.privacy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.localization.LocaleManager;
import com.devflux.deenone.core.theme.ThemeManager;
import com.devflux.deenone.core.ui.FullScreenPageDialog;
import com.devflux.deenone.utils.TouchAnimationUtil;

/**
 * ==============================================================================
 * DEEN ONE - PRIVACY POLICY PAGE DIALOG
 * Renders the PHP backend dynamic Privacy Policy page with smart
 * offline caching, dual-language support (pure Bengali / pure English), and theme sync.
 * ==============================================================================
 */
public class PrivacyPolicyPageDialog {

    public static class AndroidPrivacyBridge {
        private final Context context;

        public AndroidPrivacyBridge(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void savePageContent(String html, String lang, String theme) {
            if (html != null && html.contains("<html")) {
                PrivacyCacheManager.saveHtmlToCache(context, html, lang, theme);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void show(@NonNull Context context) {
        FullScreenPageDialog dialog = new FullScreenPageDialog(context);
        boolean isBn = LocaleManager.isBengali(context);
        boolean isDark = ThemeManager.getSavedThemeMode(context) == ThemeManager.THEME_DARK;
        String lang = isBn ? "bn" : "en";
        String theme = isDark ? "dark" : "light";

        // Trigger background pre-cache
        PrivacyCacheManager.preloadPrivacyData(context);

        // Root container
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_main));

        // 1. Top Header Bar
        LinearLayout header = new LinearLayout(context);
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_card));
        int padH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics());
        int padV = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, context.getResources().getDisplayMetrics());
        header.setPadding(padH, padV, padH, padV);

        // Back / Close Button (Rule 7: spring touch animation on buttons)
        FrameLayout btnBack = new FrameLayout(context);
        btnBack.setBackgroundResource(R.drawable.bg_circle_button_soft);
        int btnSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, context.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams btnLp = new LinearLayout.LayoutParams(btnSize, btnSize);
        btnBack.setLayoutParams(btnLp);

        ImageView ivBack = new ImageView(context);
        ivBack.setImageResource(R.drawable.ic_arrow_back);
        ivBack.setColorFilter(ContextCompat.getColor(context, R.color.text_primary));
        int icSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());
        FrameLayout.LayoutParams icLp = new FrameLayout.LayoutParams(icSize, icSize, Gravity.CENTER);
        btnBack.addView(ivBack, icLp);
        TouchAnimationUtil.attachTouchSpring(btnBack);
        btnBack.setOnClickListener(v -> dialog.dismiss());
        header.addView(btnBack);

        // Clean Title: "প্রাইভেসি পলিসি" (BN) / "Privacy Policy" (EN)
        TextView tvTitle = new TextView(context);
        LinearLayout.LayoutParams titleLp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        titleLp.setMarginStart((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, context.getResources().getDisplayMetrics()));
        tvTitle.setLayoutParams(titleLp);
        tvTitle.setText(isBn ? "প্রাইভেসি পলিসি" : "Privacy Policy");
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.5f);
        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        header.addView(tvTitle);

        root.addView(header);

        // Thin Header Divider
        View divider = new View(context);
        divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.border_card));
        root.addView(divider);

        // Progress Bar
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics())));
        progressBar.setMax(100);
        progressBar.setProgress(0);
        progressBar.setProgressTintList(android.content.res.ColorStateList.valueOf(ContextCompat.getColor(context, R.color.accent_mint)));
        root.addView(progressBar);

        // WebView in FrameLayout
        FrameLayout webContainer = new FrameLayout(context);
        webContainer.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));

        WebView webView = new WebView(context);
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setBackgroundColor(ContextCompat.getColor(context, R.color.bg_main));

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setAllowContentAccess(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setBuiltInZoomControls(false);
        ws.setDisplayZoomControls(false);

        boolean online = PrivacyCacheManager.isOnline(context);
        ws.setCacheMode(online ? WebSettings.LOAD_DEFAULT : WebSettings.LOAD_CACHE_ELSE_NETWORK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Bridge for live rendered HTML caching
        webView.addJavascriptInterface(new AndroidPrivacyBridge(context), "AndroidPrivacyBridge");

        final boolean[] isOfflineFallbackLoaded = new boolean[]{false};

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request == null || request.getUrl() == null) return false;
                String url = request.getUrl().toString();
                return handleSpecialUrl(context, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleSpecialUrl(context, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);

                // If online, extract live outerHTML and persist to disk cache
                if (PrivacyCacheManager.isOnline(context)) {
                    view.evaluateJavascript(
                            "(function() { " +
                            "  try { " +
                            "    if (window.AndroidPrivacyBridge && document.documentElement) { " +
                            "      window.AndroidPrivacyBridge.savePageContent(document.documentElement.outerHTML, '" + lang + "', '" + theme + "'); " +
                            "    } " +
                            "  } catch(e) {} " +
                            "})();", null
                    );
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Only fall back if the main frame failed to load
                if (request != null && request.isForMainFrame() && !isOfflineFallbackLoaded[0]) {
                    isOfflineFallbackLoaded[0] = true;
                    loadOfflineFallback(context, view, isBn, isDark);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (!isOfflineFallbackLoaded[0]) {
                    isOfflineFallbackLoaded[0] = true;
                    loadOfflineFallback(context, view, isBn, isDark);
                }
            }
        });

        // Initial Load - Prioritize instant pre-cached HTML for 0ms load time
        String targetUrl = PrivacyCacheManager.getPrivacyPageUrl(context, lang, theme);
        boolean hasCache = PrivacyCacheManager.hasCachedHtml(context, lang, theme);

        if (hasCache) {
            isOfflineFallbackLoaded[0] = true;
            loadOfflineFallback(context, webView, isBn, isDark);
            progressBar.setVisibility(View.GONE);
            if (online) {
                PrivacyCacheManager.preloadPrivacyData(context);
            }
        } else if (online) {
            webView.loadUrl(targetUrl);
        } else {
            isOfflineFallbackLoaded[0] = true;
            loadOfflineFallback(context, webView, isBn, isDark);
        }

        webContainer.addView(webView);
        root.addView(webContainer);

        dialog.setContentView(root);
        dialog.show();
    }

    private static boolean handleSpecialUrl(Context context, String url) {
        if (url == null) return false;

        // Mail
        if (url.startsWith("mailto:")) {
            try {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                context.startActivity(intent);
                return true;
            } catch (Exception ignored) {
                return true;
            }
        }

        // External browser links
        if (url.startsWith("http://") || url.startsWith("https://")) {
            // Check if it's the app's internal privacy link
            if (url.contains("/privacy/index.php") || url.contains("/privacy/")) {
                return false; // let WebView handle internal page
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        }

        return false;
    }

    private static void loadOfflineFallback(Context context, WebView webView, boolean isBn, boolean isDark) {
        if (webView == null || context == null) return;
        try {
            String lang = isBn ? "bn" : "en";
            String theme = isDark ? "dark" : "light";
            String offlineHtml = PrivacyCacheManager.getCachedOfflineHtml(context, lang, theme);

            webView.loadDataWithBaseURL("https://deenone.top/privacy/", offlineHtml, "text/html", "UTF-8", null);
        } catch (Exception ignored) {
        }
    }
}
