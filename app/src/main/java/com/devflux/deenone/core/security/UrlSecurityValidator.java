package com.devflux.deenone.core.security;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Validates external URLs for strict HTTPS compliance, malicious scheme prevention,
 * and SSRF/local loopback protection.
 */
public class UrlSecurityValidator {

    private static final String TAG = "UrlSecurityValidator";

    // Set of forbidden local loopback / private subnet hosts
    private static final Set<String> FORBIDDEN_HOSTS = new HashSet<>(Arrays.asList(
            "localhost",
            "127.0.0.1",
            "0.0.0.0",
            "::1",
            "10.0.2.2"
    ));

    /**
     * Validates that the provided URL is well-formed, uses secure HTTPS scheme,
     * and does not attempt loopback/private network traversal.
     */
    public static boolean isSecureHttpsUrl(@Nullable String urlString) {
        if (urlString == null || urlString.trim().isEmpty()) {
            return false;
        }

        try {
            Uri uri = Uri.parse(urlString.trim());
            String scheme = uri.getScheme();

            // 1. Strict HTTPS Check
            if (scheme == null || !"https".equalsIgnoreCase(scheme)) {
                Log.w(TAG, "Insecure URL scheme rejected: " + scheme);
                return false;
            }

            // 2. Host Validation
            String host = uri.getHost();
            if (host == null || host.trim().isEmpty()) {
                Log.w(TAG, "Empty or missing host in URL: " + urlString);
                return false;
            }

            String lowerHost = host.toLowerCase(Locale.ROOT);

            // 3. Localhost & Loopback Protection
            if (FORBIDDEN_HOSTS.contains(lowerHost)) {
                Log.w(TAG, "Local loopback host rejected: " + lowerHost);
                return false;
            }

            // 4. Private Subnet Protection
            if (lowerHost.startsWith("192.168.") || lowerHost.startsWith("10.") || lowerHost.startsWith("172.")) {
                Log.w(TAG, "Private IP range rejected: " + lowerHost);
                return false;
            }

            // 5. Valid RFC 2396 URI check
            new URI(urlString.trim());
            return true;

        } catch (Exception e) {
            Log.w(TAG, "URL validation failed for: " + urlString + " (" + e.getMessage() + ")");
            return false;
        }
    }

    /**
     * Sanitizes and returns a safe HTTPS URL or null if invalid.
     */
    @Nullable
    public static String getSanitizedSecureUrl(@Nullable String urlString) {
        if (isSecureHttpsUrl(urlString)) {
            return urlString != null ? urlString.trim() : null;
        }
        return null;
    }
}
