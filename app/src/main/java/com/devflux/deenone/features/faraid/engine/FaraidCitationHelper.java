package com.devflux.deenone.features.faraid.engine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Verified Islamic Source Citation URLs (Requirement 28).
 *
 * Direct verified URLs:
 * - Qur'an 4:11: https://quran.com/4/11
 * - Qur'an 4:12: https://quran.com/4/12
 * - Qur'an 4:176: https://quran.com/4/176
 * - Sahih al-Bukhari 6732: https://sunnah.com/bukhari:6732
 * - Sahih al-Bukhari 6737: https://sunnah.com/bukhari:6737
 * - Sahih Muslim 1615a: https://sunnah.com/muslim:1615a
 */
public class FaraidCitationHelper implements Serializable {

    public static final String URL_QURAN_4_11 = "https://quran.com/4/11";
    public static final String URL_QURAN_4_12 = "https://quran.com/4/12";
    public static final String URL_QURAN_4_176 = "https://quran.com/4/176";
    public static final String URL_BUKHARI_6732 = "https://sunnah.com/bukhari:6732";
    public static final String URL_BUKHARI_6737 = "https://sunnah.com/bukhari:6737";
    public static final String URL_MUSLIM_1615A = "https://sunnah.com/muslim:1615a";

    private static final Map<String, String> CITATION_URL_MAP = new HashMap<>();

    static {
        CITATION_URL_MAP.put("QURAN_4_11", URL_QURAN_4_11);
        CITATION_URL_MAP.put("QURAN_4_12", URL_QURAN_4_12);
        CITATION_URL_MAP.put("QURAN_4_176", URL_QURAN_4_176);
        CITATION_URL_MAP.put("BUKHARI_6732", URL_BUKHARI_6732);
        CITATION_URL_MAP.put("BUKHARI_6737", URL_BUKHARI_6737);
        CITATION_URL_MAP.put("MUSLIM_1615A", URL_MUSLIM_1615A);
    }

    /**
     * Resolves the verified source URL from a reference string.
     */
    public static String getSourceUrl(String referenceKeyOrText) {
        if (referenceKeyOrText == null) return URL_QURAN_4_11;
        String lower = referenceKeyOrText.toLowerCase();

        if (lower.contains("4:11") || lower.contains("১১")) {
            return URL_QURAN_4_11;
        } else if (lower.contains("4:12") || lower.contains("১২")) {
            return URL_QURAN_4_12;
        } else if (lower.contains("4:176") || lower.contains("১৭৬")) {
            return URL_QURAN_4_176;
        } else if (lower.contains("6732") || lower.contains("৬৭৩২")) {
            return URL_BUKHARI_6732;
        } else if (lower.contains("6737") || lower.contains("৬৭৩৭")) {
            return URL_BUKHARI_6737;
        } else if (lower.contains("1615") || lower.contains("১৬১৫")) {
            return URL_MUSLIM_1615A;
        }
        return URL_QURAN_4_11;
    }
}