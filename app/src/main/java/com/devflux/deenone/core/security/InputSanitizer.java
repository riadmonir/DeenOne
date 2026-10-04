package com.devflux.deenone.core.security;

import java.util.regex.Pattern;

public class InputSanitizer {

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile("(?i)(--|;|'|\"|/\\*|\\*/|xp_)");

    public static String sanitizeText(String input, int maxLength) {
        if (input == null) return "";
        String clean = input.trim();

        // 1. Remove dangerous script and HTML tags
        clean = SCRIPT_PATTERN.matcher(clean).replaceAll("");
        clean = HTML_TAG_PATTERN.matcher(clean).replaceAll("");

        // 2. Limit length
        if (maxLength > 0 && clean.length() > maxLength) {
            clean = clean.substring(0, maxLength);
        }

        return clean;
    }

    public static String sanitizeSearchQuery(String query) {
        if (query == null) return "";
        String clean = query.trim();
        // Remove raw SQL comment / injection characters
        clean = SQL_INJECTION_PATTERN.matcher(clean).replaceAll("");
        return sanitizeText(clean, 100);
    }
}
