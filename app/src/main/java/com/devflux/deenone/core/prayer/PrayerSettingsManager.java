package com.devflux.deenone.core.prayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devflux.deenone.core.location.LocationProvider;

public class PrayerSettingsManager {

    private static final String TAG = "PrayerSettingsManager";
    private static final String PREF_NAME = "deanone_prayer_settings";

    public enum CalculationMethod {
        KARACHI("University of Islamic Sciences, Karachi", "ইউনিভার্সিটি অব ইসলামিক সায়েন্সেস, করাচি", 18.0, 18.0, 0, 0.0),
        MUSLIM_WORLD_LEAGUE("Muslim World League", "মুসলিম ওয়ার্ল্ড লীগ (MWL)", 18.0, 17.0, 0, 0.0),
        TEHRAN("Institute of Geophysics, University of Tehran", "তেহরান বিশ্ববিদ্যালয় জিওফিজিক্স ইনস্টিটিউট", 17.7, 14.0, 0, 4.5),
        BANGLADESH_ISLAMIC_FOUNDATION("Islamic Foundation Bangladesh", "ইসলামিক ফাউন্ডেশন বাংলাদেশ", 18.0, 18.0, 0, 0.0),
        UMM_AL_QURA("Umm Al-Qura", "উম্ম আল-কুরা বিশ্ববিদ্যালয়, মক্কা", 18.5, 0.0, 90, 0.0), // 90 min after Maghrib
        UMM_AL_QURA_90("Umm Al-Qura (90 Minutes)", "উম্ম আল-কুরা (৯০ মিনিট)", 18.5, 0.0, 90, 0.0),
        UMM_AL_QURA_120("Umm Al-Qura (120 Minutes)", "উম্ম আল-কুরা (১২০ মিনিট - রমজান)", 18.5, 0.0, 120, 0.0),
        EGYPTIAN("Egyptian General Authority of Survey", "মিশরীয় জেনারেল অথরিটি অফ সার্ভে", 19.5, 17.5, 0, 0.0),
        ISNA("Islamic Society of North America", "ইসলামিক সোসাইটি অফ নর্থ আমেরিকা (ISNA)", 15.0, 15.0, 0, 0.0),
        KUWAIT("Calculation Method, Kuwait", "কুয়েত ইসলামিক পদ্ধতি", 18.0, 17.5, 0, 0.0),
        FRANCE_UOIF("Union des Organisations Islamiques de France", "ফ্রান্স ইসলামিক সংস্থা (UOIF)", 12.0, 12.0, 0, 0.0),
        SINGAPORE_MUIS("Majlis Ugama Islam Singapura", "মজলিস উগামা ইসলাম সিঙ্গাপুরা (MUIS)", 20.0, 18.0, 0, 0.0),
        TURKEY_DIYANET("Diyanet İşleri Başkanlığı, Turkey", "দিয়ানেত ইত ও ধর্ম বিষয়ক প্রেসিডেন্সি, তুরস্ক", 18.0, 17.0, 0, 0.0),
        RUSSIA("Spiritual Administration of Muslims of Russia", "রাশিয়ার মুসলিমদের আধ্যাত্মিক প্রশাসন", 16.0, 15.0, 0, 0.0),
        MALAYSIA_JAKIM("Jabatan Kemajuan Islam Malaysia", "জাবাতান কেমাজুয়ান ইসলাম মালয়েশিয়া (JAKIM)", 20.0, 18.0, 0, 0.0),
        MWL_HIGH_LATITUDE("MWL (High Latitude)", "মুসলিম ওয়ার্ল্ড লীগ (উচ্চ অক্ষাংশ)", 18.0, 17.0, 0, 0.0),
        DUBAI("Dubai (UAE General Authority of Islamic Affairs)", "দুবাই / সংযুক্ত আরব আমিরাত (Awqaf)", 18.2, 18.2, 0, 0.0),
        QATAR("Qatar", "কাতার ধর্ম বিষয়ক মন্ত্রণালয়", 18.0, 0.0, 90, 0.0),
        MOONSIGHTING_COMMITTEE("Moonsighting Committee Worldwide", "মুনসাইটিং কমিটি ওয়ার্ল্ডওয়াইড", 18.0, 18.0, 0, 0.0);

        public final String displayName;
        public final String displayNameBn;
        public final double fajrAngle;
        public final double ishaAngle;
        public final int ishaIntervalMinutes;
        public final double maghribAngle;

        CalculationMethod(String displayName, String displayNameBn, double fajrAngle, double ishaAngle, int ishaIntervalMinutes, double maghribAngle) {
            this.displayName = displayName;
            this.displayNameBn = displayNameBn;
            this.fajrAngle = fajrAngle;
            this.ishaAngle = ishaAngle;
            this.ishaIntervalMinutes = ishaIntervalMinutes;
            this.maghribAngle = maghribAngle;
        }

        public String getLocalizedName(Context context) {
            if (context != null && com.devflux.deenone.core.localization.LocaleManager.isBengali(context)) {
                return displayNameBn != null ? displayNameBn : displayName;
            }
            return displayName;
        }
    }

    public enum JuristicMethod {
        HANAFI("হানাফি - দ্বিগুণ ছায়া", "Hanafi - Double Shadow", 2),
        SHAFI("শাফেয়ী, মালেকী ও হাম্বলী - একগুণ ছায়া", "Shafi'i, Maliki, Hanbali - Single Shadow", 1);

        public final String displayName;
        public final String displayNameEn;
        public final int shadowFactor;

        JuristicMethod(String displayName, String displayNameEn, int shadowFactor) {
            this.displayName = displayName;
            this.displayNameEn = displayNameEn;
            this.shadowFactor = shadowFactor;
        }

        public String getLocalizedName(Context context) {
            if (context != null && com.devflux.deenone.core.localization.LocaleManager.isBengali(context)) {
                return displayName;
            }
            return displayNameEn != null ? displayNameEn : displayName;
        }
    }

    public static class AutoRecommendation {
        public final String countryName;
        public final String countryNameBn;
        public final CalculationMethod recommendedMethod;

        public AutoRecommendation(String countryName, String countryNameBn, CalculationMethod recommendedMethod) {
            this.countryName = countryName;
            this.countryNameBn = countryNameBn;
            this.recommendedMethod = recommendedMethod;
        }
    }

    private static final String KEY_METHOD = "key_calc_method";
    private static final String KEY_IS_AUTO_METHOD = "key_is_auto_method";
    private static final String KEY_JURISTIC = "key_juristic_method";
    public static final String KEY_OFFSET_FAJR = "key_offset_fajr";
    public static final String KEY_OFFSET_SUNRISE = "key_offset_sunrise";
    public static final String KEY_OFFSET_DHUHR = "key_offset_dhuhr";
    public static final String KEY_OFFSET_JUMMAH = "key_offset_jummah";
    public static final String KEY_OFFSET_ASR = "key_offset_asr";
    public static final String KEY_OFFSET_MAGHRIB = "key_offset_maghrib";
    public static final String KEY_OFFSET_ISHA = "key_offset_isha";

    public static boolean isAutoMethod(Context context) {
        if (context == null) return true;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_AUTO_METHOD, true);
    }

    public static void setAutoMethod(Context context, boolean isAuto) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_IS_AUTO_METHOD, isAuto).apply();
        notifyPrayerSettingsChanged(context);
    }

    /**
     * Resolves the recommended calculation method & country metadata from active location.
     */
    public static AutoRecommendation getRecommendedMethodForCurrentLocation(Context context) {
        LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
        String tzId = LocationProvider.getSavedTimezoneId(context);
        String locName = coords.locationName != null ? coords.locationName.toLowerCase() : "";
        double lat = coords.latitude;
        double lng = coords.longitude;

        // 1. Saudi Arabia & Makkah
        if (locName.contains("saudi") || locName.contains("সৌদি") || locName.contains("tabuk") ||
            locName.contains("makkah") || locName.contains("মক্কা") || locName.contains("madinah") ||
            locName.contains("মদিনা") || locName.contains("riyadh") || locName.contains("রিয়াদ") ||
            "Asia/Riyadh".equalsIgnoreCase(tzId) || (lat >= 16.0 && lat <= 32.5 && lng >= 34.5 && lng <= 55.7)) {
            return new AutoRecommendation("Saudi Arabia", "সৌদি আরব", CalculationMethod.UMM_AL_QURA);
        }

        // 2. Kuwait
        if (locName.contains("kuwait") || locName.contains("কুয়েত") || "Asia/Kuwait".equalsIgnoreCase(tzId) ||
            (lat >= 28.5 && lat <= 30.2 && lng >= 46.5 && lng <= 48.6)) {
            return new AutoRecommendation("Kuwait", "কুয়েত", CalculationMethod.KUWAIT);
        }

        // 3. Qatar
        if (locName.contains("qatar") || locName.contains("কাতার") || locName.contains("doha") ||
            "Asia/Qatar".equalsIgnoreCase(tzId) || (lat >= 24.5 && lat <= 26.2 && lng >= 50.7 && lng <= 51.7)) {
            return new AutoRecommendation("Qatar", "কাতার", CalculationMethod.QATAR);
        }

        // 4. UAE / Dubai
        if (locName.contains("emirates") || locName.contains("dubai") || locName.contains("দুবাই") ||
            locName.contains("abu dhabi") || "Asia/Dubai".equalsIgnoreCase(tzId) ||
            (lat >= 22.5 && lat <= 26.1 && lng >= 51.5 && lng <= 56.4)) {
            return new AutoRecommendation("United Arab Emirates", "সংযুক্ত আরব আমিরাত", CalculationMethod.DUBAI);
        }

        // 5. Malaysia
        if (locName.contains("malaysia") || locName.contains("মালয়েশিয়া") || locName.contains("kuala lumpur") ||
            "Asia/Kuala_Lumpur".equalsIgnoreCase(tzId) || "Asia/Kuching".equalsIgnoreCase(tzId) ||
            (lat >= 1.0 && lat <= 7.5 && lng >= 99.5 && lng <= 119.5)) {
            return new AutoRecommendation("Malaysia", "মালয়েশিয়া", CalculationMethod.MALAYSIA_JAKIM);
        }

        // 6. Singapore
        if (locName.contains("singapore") || locName.contains("সিঙ্গাপুর") || "Asia/Singapore".equalsIgnoreCase(tzId) ||
            (lat >= 1.15 && lat <= 1.48 && lng >= 103.6 && lng <= 104.1)) {
            return new AutoRecommendation("Singapore", "সিঙ্গাপুর", CalculationMethod.SINGAPORE_MUIS);
        }

        // 7. Turkey
        if (locName.contains("turkey") || locName.contains("তুরস্ক") || locName.contains("istanbul") ||
            locName.contains("ankara") || "Europe/Istanbul".equalsIgnoreCase(tzId) || "Asia/Istanbul".equalsIgnoreCase(tzId) ||
            (lat >= 35.8 && lat <= 42.2 && lng >= 25.5 && lng <= 44.9)) {
            return new AutoRecommendation("Turkey", "তুরস্ক", CalculationMethod.TURKEY_DIYANET);
        }

        // 8. Egypt & North Africa
        if (locName.contains("egypt") || locName.contains("মিশর") || locName.contains("cairo") ||
            "Africa/Cairo".equalsIgnoreCase(tzId) || (lat >= 22.0 && lat <= 31.7 && lng >= 24.7 && lng <= 36.9)) {
            return new AutoRecommendation("Egypt", "মিশর", CalculationMethod.EGYPTIAN);
        }

        // 9. France
        if (locName.contains("france") || locName.contains("ফ্রান্স") || locName.contains("paris") ||
            "Europe/Paris".equalsIgnoreCase(tzId) || (lat >= 41.3 && lat <= 51.1 && lng >= -5.2 && lng <= 9.6)) {
            return new AutoRecommendation("France", "ফ্রান্স", CalculationMethod.FRANCE_UOIF);
        }

        // 10. Russia
        if (locName.contains("russia") || locName.contains("রাশিয়া") || locName.contains("moscow") ||
            locName.contains("chechnya") || locName.contains("dagestan") || tzId.startsWith("Europe/Moscow") ||
            (lat >= 41.0 && lat <= 82.0 && lng >= 19.0 && lng <= 180.0)) {
            return new AutoRecommendation("Russia", "রাশিয়া", CalculationMethod.RUSSIA);
        }

        // 11. Iran
        if (locName.contains("iran") || locName.contains("ইরান") || locName.contains("tehran") ||
            "Asia/Tehran".equalsIgnoreCase(tzId) || (lat >= 25.0 && lat <= 39.8 && lng >= 44.0 && lng <= 63.4)) {
            return new AutoRecommendation("Iran", "ইরান", CalculationMethod.TEHRAN);
        }

        // 12. Pakistan & India & Afghanistan
        if (locName.contains("pakistan") || locName.contains("পাকিস্তান") || locName.contains("karachi") ||
            locName.contains("india") || locName.contains("ভারত") || locName.contains("delhi") ||
            locName.contains("afghanistan") || "Asia/Karachi".equalsIgnoreCase(tzId) ||
            "Asia/Kolkata".equalsIgnoreCase(tzId) || "Asia/Calcutta".equalsIgnoreCase(tzId) || "Asia/Kabul".equalsIgnoreCase(tzId)) {
            return new AutoRecommendation("Pakistan / India", "পাকিস্তান / ভারত", CalculationMethod.KARACHI);
        }

        // 13. Bangladesh
        if (locName.contains("bangladesh") || locName.contains("বাংলাদেশ") || locName.contains("dhaka") ||
            locName.contains("ঢাকা") || locName.contains("chittagong") || locName.contains("চট্টগ্রাম") ||
            locName.contains("sylhet") || locName.contains("সিলেট") || "Asia/Dhaka".equalsIgnoreCase(tzId) ||
            (lat >= 20.5 && lat <= 26.7 && lng >= 88.0 && lng <= 92.7)) {
            return new AutoRecommendation("Bangladesh", "বাংলাদেশ", CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION);
        }

        // 14. North America (USA & Canada)
        if (locName.contains("usa") || locName.contains("united states") || locName.contains("america") ||
            locName.contains("canada") || tzId.startsWith("America/")) {
            return new AutoRecommendation("United States", "যুক্তরাষ্ট্র", CalculationMethod.ISNA);
        }

        // 15. High Latitude Regions (e.g. Norway, Sweden, UK North > 48°)
        if (Math.abs(lat) >= 48.0) {
            return new AutoRecommendation("High Latitude Region", "উচ্চ অক্ষাংশ অঞ্চল", CalculationMethod.MWL_HIGH_LATITUDE);
        }

        // Default World Fallback: Muslim World League
        return new AutoRecommendation("International", "আন্তর্জাতিক", CalculationMethod.MUSLIM_WORLD_LEAGUE);
    }

    /**
     * Gets the active calculation method. If Auto is enabled, resolves the recommended method dynamically.
     */
    public static CalculationMethod getCalculationMethod(Context context) {
        if (context == null) return CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION;
        if (isAutoMethod(context)) {
            AutoRecommendation rec = getRecommendedMethodForCurrentLocation(context);
            if (rec != null && rec.recommendedMethod != null) {
                return rec.recommendedMethod;
            }
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_METHOD, CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION.name());
        try {
            return CalculationMethod.valueOf(name);
        } catch (Exception e) {
            return CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION;
        }
    }

    /**
     * Gets raw saved manual method preference without auto resolution.
     */
    public static CalculationMethod getRawSavedCalculationMethod(Context context) {
        if (context == null) return CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_METHOD, CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION.name());
        try {
            return CalculationMethod.valueOf(name);
        } catch (Exception e) {
            return CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION;
        }
    }

    public static void setCalculationMethod(Context context, CalculationMethod method) {
        if (context == null || method == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
            .putString(KEY_METHOD, method.name())
            .putBoolean(KEY_IS_AUTO_METHOD, false)
            .apply();
        notifyPrayerSettingsChanged(context);
    }

    public static JuristicMethod getJuristicMethod(Context context) {
        if (context == null) return JuristicMethod.HANAFI;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(KEY_JURISTIC, JuristicMethod.HANAFI.name());
        try {
            return JuristicMethod.valueOf(name);
        } catch (Exception e) {
            return JuristicMethod.HANAFI;
        }
    }

    public static void setJuristicMethod(Context context, JuristicMethod method) {
        if (context == null || method == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_JURISTIC, method.name()).apply();
        notifyPrayerSettingsChanged(context);
    }

    public static int getOffsetMinutes(Context context, String waqtKey) {
        if (context == null || waqtKey == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt("key_offset_" + waqtKey.toLowerCase(), 0);
    }

    public static void setOffsetMinutes(Context context, String waqtKey, int offsetMinutes) {
        if (context == null || waqtKey == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt("key_offset_" + waqtKey.toLowerCase(), offsetMinutes).apply();
        notifyPrayerSettingsChanged(context);
    }

    public static void setAllOffsets(Context context, int fajr, int sunrise, int dhuhr, int asr, int maghrib, int isha) {
        setAllOffsets(context, fajr, sunrise, dhuhr, getOffsetMinutes(context, "jummah"), asr, maghrib, isha);
    }

    public static void setAllOffsets(Context context, int fajr, int sunrise, int dhuhr, int jummah, int asr, int maghrib, int isha) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(KEY_OFFSET_FAJR, fajr)
                .putInt(KEY_OFFSET_SUNRISE, sunrise)
                .putInt(KEY_OFFSET_DHUHR, dhuhr)
                .putInt(KEY_OFFSET_JUMMAH, jummah)
                .putInt(KEY_OFFSET_ASR, asr)
                .putInt(KEY_OFFSET_MAGHRIB, maghrib)
                .putInt(KEY_OFFSET_ISHA, isha)
                .apply();
        notifyPrayerSettingsChanged(context);
    }

    public static void resetAllOffsets(Context context) {
        setAllOffsets(context, 0, 0, 0, 0, 0, 0, 0);
    }

    public static boolean hasAnyOffset(Context context) {
        return getOffsetMinutes(context, "fajr") != 0
                || getOffsetMinutes(context, "sunrise") != 0
                || getOffsetMinutes(context, "dhuhr") != 0
                || getOffsetMinutes(context, "jummah") != 0
                || getOffsetMinutes(context, "asr") != 0
                || getOffsetMinutes(context, "maghrib") != 0
                || getOffsetMinutes(context, "isha") != 0;
    }

    public static String getFormattedOffsetSummary(Context context) {
        if (context == null) return "Default - No adjustment";
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        int f = getOffsetMinutes(context, "fajr");
        int sun = getOffsetMinutes(context, "sunrise");
        int d = getOffsetMinutes(context, "dhuhr");
        int j = getOffsetMinutes(context, "jummah");
        int a = getOffsetMinutes(context, "asr");
        int m = getOffsetMinutes(context, "maghrib");
        int i = getOffsetMinutes(context, "isha");

        if (f == 0 && sun == 0 && d == 0 && j == 0 && a == 0 && m == 0 && i == 0) {
            return isBn ? "ডিফল্ট - কোনো পরিবর্তন নেই" : "Default - No adjustment";
        }

        StringBuilder sb = new StringBuilder();
        if (isBn) {
            if (f != 0) sb.append("ফজর ").append(f > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(f)).append(" মি, ");
            if (sun != 0) sb.append("সূর্যোদয় ").append(sun > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(sun)).append(" মি, ");
            if (d != 0) sb.append("যোহর ").append(d > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(d)).append(" মি, ");
            if (j != 0) sb.append("জুমুআ ").append(j > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(j)).append(" মি, ");
            if (a != 0) sb.append("আসর ").append(a > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(a)).append(" মি, ");
            if (m != 0) sb.append("মাগরিব ").append(m > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(m)).append(" মি, ");
            if (i != 0) sb.append("ইশা ").append(i > 0 ? "+" : "").append(com.devflux.deenone.utils.BengaliNumberUtil.toBengali(i)).append(" মি, ");
        } else {
            if (f != 0) sb.append("Fajr ").append(f > 0 ? "+" : "").append(f).append("m, ");
            if (sun != 0) sb.append("Sunrise ").append(sun > 0 ? "+" : "").append(sun).append("m, ");
            if (d != 0) sb.append("Dhuhr ").append(d > 0 ? "+" : "").append(d).append("m, ");
            if (j != 0) sb.append("Jummah ").append(j > 0 ? "+" : "").append(j).append("m, ");
            if (a != 0) sb.append("Asr ").append(a > 0 ? "+" : "").append(a).append("m, ");
            if (m != 0) sb.append("Maghrib ").append(m > 0 ? "+" : "").append(m).append("m, ");
            if (i != 0) sb.append("Isha ").append(i > 0 ? "+" : "").append(i).append("m, ");
        }

        String str = sb.toString().trim();
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static String getMazhabSubtitle(Context context) {
        boolean isBn = com.devflux.deenone.core.localization.LocaleManager.isBengali(context);
        JuristicMethod juristic = getJuristicMethod(context);
        if (juristic == JuristicMethod.HANAFI) {
            return isBn ? "২ গুণ ছায়া পদ্ধতি - হানাফী" : "2x shadow system - Hanafi";
        } else {
            return isBn ? "১ গুণ ছায়া পদ্ধতি - শাফেয়ী, মালেকী ও হাম্বলী" : "1x shadow system - Shafi'i, Maliki & Hanbali";
        }
    }

    /**
     * Broadcasts and updates all background schedulers, Room DB, widgets, lockscreen, and wearables.
     */
    public static void notifyPrayerSettingsChanged(Context context) {
        if (context == null) return;
        try {
            // 1. Reschedule alarms
            com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(context);

            // 2. Refresh Room DB Realtime Schedule
            new com.devflux.deenone.data.repository.PrayerRepository(context).refreshRealtimeSchedule();

            // 3. Update all Home Screen Widgets
            com.devflux.deenone.widget.WidgetDataHelper.updateAllWidgets(context);

            // 4. Update Lock Screen Dynamic Widget
            com.devflux.deenone.core.notifications.LockScreenPrayerWidgetManager.updateLockScreenWidget(context);

            // 5. Sync with Wear OS
            com.devflux.deenone.core.wearable.WearOsSyncManager.syncToWearable(context);
        } catch (Exception e) {
            Log.e(TAG, "Failed to broadcast prayer settings update: " + e.getMessage());
        }
    }
}
