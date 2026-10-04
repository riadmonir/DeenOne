package com.devflux.deenone.core.calendar;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * Production-ready settings manager for Calendar Settings:
 * 1. Hijri Date Source (Muslims Day / Deanone Managed, Umm Al-Qura, Islamic Foundation, etc.)
 * 2. Hijri Date Change Timing (At Maghrib / Sunset vs At 12:00 AM / Midnight)
 * 3. Bengali Calendar (বঙ্গাব্দ) (On / Off)
 */
public class CalendarSettingsManager {

    private static final String PREF_NAME = "deanone_calendar_settings";
    private static final String KEY_HIJRI_SOURCE = "key_hijri_date_source";
    private static final String KEY_HIJRI_CHANGE_TIMING = "key_hijri_change_timing";
    private static final String KEY_BENGALI_CALENDAR_ENABLED = "key_bengali_calendar_enabled";

    public enum HijriDateSource {
        DEENONE_MANAGED(
                "Managed by DeenOne",
                "Managed by DeenOne",
                "DeenOne Managed"
        ),
        @Deprecated
        DEANONE_MANAGED(
                "Managed by DeenOne",
                "Managed by DeenOne",
                "DeenOne Managed"
        ),
        ESTIMATED_MOON_CALCULATION(
                "Estimated Moon Position",
                "Estimated Moon Position",
                "Estimated Moon"
        ),
        UMM_AL_QURA(
                "Umm Al-Qura",
                "Umm Al-Qura Calendar",
                "Umm Al-Qura"
        ),
        ISLAMIC_FOUNDATION_BD(
                "Islamic Foundation Bangladesh",
                "Islamic Foundation Bangladesh Calendar",
                "Islamic Foundation BD"
        ),
        MOONSIGHTING_HILAL(
                "Moon Sighting Committee",
                "Moon Sighting Committee",
                "Moon Sighting Committee"
        ),
        ASTRONOMICAL(
                "Estimated Moon Position",
                "Estimated Moon Position",
                "Astronomical"
        );

        private final String displayName;
        private final String description;
        private final String shortName;

        HijriDateSource(String displayName, String description, String shortName) {
            this.displayName = displayName;
            this.description = description;
            this.shortName = shortName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDisplayName(Context context) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            switch (this) {
                case DEENONE_MANAGED:
                case DEANONE_MANAGED:
                    return isBn ? "দীনওয়ান নিয়ন্ত্রিত" : "Managed by DeenOne";
                case ESTIMATED_MOON_CALCULATION:
                case ASTRONOMICAL:
                    return isBn ? "চাঁদের আনুমানিক অবস্থান" : "Estimated Moon Position";
                case UMM_AL_QURA:
                    return isBn ? "উম্মুল কুরা" : "Umm Al-Qura";
                case ISLAMIC_FOUNDATION_BD:
                    return isBn ? "ইসলামিক ফাউন্ডেশন" : "Islamic Foundation BD";
                case MOONSIGHTING_HILAL:
                    return isBn ? "চাঁদ দেখা কমিটি" : "Moon Sighting Committee";
                default:
                    return displayName;
            }
        }

        public String getDescription() {
            return description;
        }

        public String getShortName() {
            return shortName;
        }
    }

    public enum HijriChangeTiming {
        MIDNIGHT_12AM(
                "At Midnight",
                "Advances at 00:00 local time"
        ),
        MAGHRIB_SUNSET(
                "After Sunset",
                "Advances at local Maghrib"
        );

        private final String displayName;
        private final String description;

        HijriChangeTiming(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDisplayName(Context context) {
            boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
            if (this == MIDNIGHT_12AM) {
                return isBn ? "মধ্যরাতে" : "At Midnight";
            } else {
                return isBn ? "সূর্যাস্তের পর" : "After Sunset";
            }
        }

        public String getDescription() {
            return description;
        }
    }

    private static SharedPreferences getPrefs(@NonNull Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // --- Hijri Date Source ---
    public static HijriDateSource getHijriDateSource(@NonNull Context context) {
        String name = getPrefs(context).getString(KEY_HIJRI_SOURCE, HijriDateSource.DEENONE_MANAGED.name());
        if ("DEANONE_MANAGED".equals(name)) {
            return HijriDateSource.DEENONE_MANAGED;
        }
        try {
            return HijriDateSource.valueOf(name);
        } catch (Exception e) {
            return HijriDateSource.DEENONE_MANAGED;
        }
    }

    public static void setHijriDateSource(@NonNull Context context, @NonNull HijriDateSource source) {
        getPrefs(context).edit().putString(KEY_HIJRI_SOURCE, source.name()).apply();
    }

    public static String getHijriSourceSummary(@NonNull Context context) {
        return getHijriDateSource(context).getDisplayName(context);
    }

    // --- Hijri Change Timing ---
    public static HijriChangeTiming getHijriChangeTiming(@NonNull Context context) {
        String name = getPrefs(context).getString(KEY_HIJRI_CHANGE_TIMING, HijriChangeTiming.MIDNIGHT_12AM.name());
        try {
            return HijriChangeTiming.valueOf(name);
        } catch (Exception e) {
            return HijriChangeTiming.MIDNIGHT_12AM;
        }
    }

    public static void setHijriChangeTiming(@NonNull Context context, @NonNull HijriChangeTiming timing) {
        getPrefs(context).edit().putString(KEY_HIJRI_CHANGE_TIMING, timing.name()).apply();
    }

    public static String getHijriChangeTimingSummary(@NonNull Context context) {
        return getHijriChangeTiming(context).getDisplayName(context);
    }

    private static final String KEY_BENGALI_CALC_METHOD = "key_bengali_calc_method";

    // --- Bengali Calendar (বঙ্গাব্দ) ---
    public static boolean isBengaliCalendarEnabled(@NonNull Context context) {
        return getPrefs(context).getBoolean(KEY_BENGALI_CALENDAR_ENABLED, false);
    }

    public static void setBengaliCalendarEnabled(@NonNull Context context, boolean enabled) {
        getPrefs(context).edit().putBoolean(KEY_BENGALI_CALENDAR_ENABLED, enabled).apply();
    }

    public static com.devflux.deenone.utils.BengaliCalendarUtil.CalculationMethod getBengaliCalculationMethod(@NonNull Context context) {
        String name = getPrefs(context).getString(
                KEY_BENGALI_CALC_METHOD,
                com.devflux.deenone.utils.BengaliCalendarUtil.CalculationMethod.BANGLA_ACADEMY.name()
        );
        try {
            return com.devflux.deenone.utils.BengaliCalendarUtil.CalculationMethod.valueOf(name);
        } catch (Exception e) {
            return com.devflux.deenone.utils.BengaliCalendarUtil.CalculationMethod.BANGLA_ACADEMY;
        }
    }

    public static void setBengaliCalculationMethod(
            @NonNull Context context,
            @NonNull com.devflux.deenone.utils.BengaliCalendarUtil.CalculationMethod method
    ) {
        getPrefs(context).edit().putString(KEY_BENGALI_CALC_METHOD, method.name()).apply();
    }

    public static String getBengaliCalendarSummary(@NonNull Context context) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        if (!isBengaliCalendarEnabled(context)) {
            return isBn ? "বন্ধ" : "Off";
        }
        com.devflux.deenone.utils.BengaliCalendarUtil.CalculationMethod method = getBengaliCalculationMethod(context);
        return method.getDisplayName(isBn);
    }
}
