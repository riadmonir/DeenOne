package com.devflux.deenone.utils;

import android.content.Context;

import com.devflux.deenone.core.prayer.PrayerSettingsManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PrayerCalculator {

    public static class PrayerTimesResult {
        public long fajrMillis;
        public long sunriseMillis;
        public long ishraqMillis;
        public long chashtMillis;
        public long zohrMillis;
        public long asrMillis;
        public long sunsetMillis;
        public long maghribMillis;
        public long awwabinMillis;
        public long ishaMillis;
        public long midnightMillis;
        public long lastThirdOfNightMillis;
        public long tahajjudMillis;
        public long sehriMillis;
        public long iftarMillis;

        public String fajrStr;
        public String sunriseStr;
        public String ishraqStr;
        public String chashtStr;
        public String chashtRangeStr;
        public String zohrStr;
        public String asrStr;
        public String sunsetStr;
        public String maghribStr;
        public String awwabinStr;
        public String ishaStr;
        public String midnightStr;
        public String lastThirdOfNightStr;
        public String tahajjudStr;
        public String sehriStr;
        public String iftarStr;
        public String jummahStr;
        public long jummahMillis;
        public boolean isFriday;

        public String currentWaqtName;
        public String nextPrayerName;
        public String nextPrayerTimeStr;
        public long nextPrayerRemainingSeconds;
        public long iftarRemainingSeconds;
        public long currentWaqtRemainingSeconds;
        public int currentWaqtProgressPercent;

        public boolean isFastingDaytime;
        public String fastingCountdownTitle;
        public String fastingCountdownTimeStr;
        public long fastingRemainingSeconds;
    }

    public static PrayerTimesResult calculateForLocation(double latitude, double longitude, double timezone, Calendar calendar) {
        return calculateForLocationWithSettings(
                latitude,
                longitude,
                timezone,
                calendar,
                PrayerSettingsManager.CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION,
                PrayerSettingsManager.JuristicMethod.HANAFI,
                0, 0, 0, 0, 0
        );
    }

    public static PrayerTimesResult calculateForLocation(
            double latitude,
            double longitude,
            double timezone,
            Calendar calendar,
            PrayerSettingsManager.JuristicMethod juristic,
            PrayerSettingsManager.CalculationMethod method
    ) {
        return calculateForLocationWithSettings(
                latitude,
                longitude,
                timezone,
                calendar,
                method != null ? method : PrayerSettingsManager.CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION,
                juristic != null ? juristic : PrayerSettingsManager.JuristicMethod.HANAFI,
                0, 0, 0, 0, 0
        );
    }

    public static PrayerTimesResult calculateForLocationWithContext(Context context, double latitude, double longitude, double timezone, Calendar calendar) {
        PrayerSettingsManager.CalculationMethod method = PrayerSettingsManager.getCalculationMethod(context);
        PrayerSettingsManager.JuristicMethod juristic = PrayerSettingsManager.getJuristicMethod(context);
        int fajrOffset = PrayerSettingsManager.getOffsetMinutes(context, "fajr");
        int sunriseOffset = PrayerSettingsManager.getOffsetMinutes(context, "sunrise");
        int dhuhrOffset = PrayerSettingsManager.getOffsetMinutes(context, "dhuhr");
        int jummahOffset = PrayerSettingsManager.getOffsetMinutes(context, "jummah");
        int asrOffset = PrayerSettingsManager.getOffsetMinutes(context, "asr");
        int maghribOffset = PrayerSettingsManager.getOffsetMinutes(context, "maghrib");
        int ishaOffset = PrayerSettingsManager.getOffsetMinutes(context, "isha");

        return calculateForLocationWithSettings(
                latitude, longitude, timezone, calendar, method, juristic,
                fajrOffset, sunriseOffset, dhuhrOffset, jummahOffset, asrOffset, maghribOffset, ishaOffset
        );
    }

    public static PrayerTimesResult calculateForLocationWithSettings(
            double latitude,
            double longitude,
            double timezone,
            Calendar calendar,
            PrayerSettingsManager.CalculationMethod calcMethod,
            PrayerSettingsManager.JuristicMethod juristicMethod,
            int fajrOffsetMin,
            int dhuhrOffsetMin,
            int asrOffsetMin,
            int maghribOffsetMin,
            int ishaOffsetMin
    ) {
        return calculateForLocationWithSettings(
                latitude, longitude, timezone, calendar, calcMethod, juristicMethod,
                fajrOffsetMin, 0, dhuhrOffsetMin, dhuhrOffsetMin, asrOffsetMin, maghribOffsetMin, ishaOffsetMin
        );
    }

    public static PrayerTimesResult calculateForLocationWithSettings(
            double latitude,
            double longitude,
            double timezone,
            Calendar calendar,
            PrayerSettingsManager.CalculationMethod calcMethod,
            PrayerSettingsManager.JuristicMethod juristicMethod,
            int fajrOffsetMin,
            int sunriseOffsetMin,
            int dhuhrOffsetMin,
            int asrOffsetMin,
            int maghribOffsetMin,
            int ishaOffsetMin
    ) {
        return calculateForLocationWithSettings(
                latitude, longitude, timezone, calendar, calcMethod, juristicMethod,
                fajrOffsetMin, sunriseOffsetMin, dhuhrOffsetMin, dhuhrOffsetMin, asrOffsetMin, maghribOffsetMin, ishaOffsetMin
        );
    }

    public static PrayerTimesResult calculateForLocationWithSettings(
            double latitude,
            double longitude,
            double timezone,
            Calendar calendar,
            PrayerSettingsManager.CalculationMethod calcMethod,
            PrayerSettingsManager.JuristicMethod juristicMethod,
            int fajrOffsetMin,
            int sunriseOffsetMin,
            int dhuhrOffsetMin,
            int jummahOffsetMin,
            int asrOffsetMin,
            int maghribOffsetMin,
            int ishaOffsetMin
    ) {
        if (calendar == null) calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Julian Day calculation
        double jd = getJulianDay(year, month, day);
        double d = jd - 2451545.0;

        double g = fixAngle(357.529 + 0.98560028 * d);
        double q = fixAngle(280.459 + 0.98564736 * d);
        double l = fixAngle(q + 1.915 * Math.sin(Math.toRadians(g)) + 0.020 * Math.sin(Math.toRadians(2 * g)));

        double e = 23.439 - 0.00000036 * d;
        double ra = fixAngle(Math.toDegrees(Math.atan2(Math.cos(Math.toRadians(e)) * Math.sin(Math.toRadians(l)), Math.cos(Math.toRadians(l))))) / 15.0;

        double eqt = q / 15.0 - ra;
        double decl = Math.toDegrees(Math.asin(Math.sin(Math.toRadians(e)) * Math.sin(Math.toRadians(l))));

        // Solar Noon (Dhuhr)
        double noon = fixHour(12.0 + timezone - longitude / 15.0 - eqt);

        // Calculation angles
        double fajrAngle = calcMethod.fajrAngle;
        double ishaAngle = calcMethod.ishaAngle;

        double sunriseHA = getHourAngle(0.833, latitude, decl);
        if (Double.isNaN(sunriseHA)) {
            sunriseHA = 6.0; // Polar fallback
        }
        double sunriseHour = noon - sunriseHA;
        double sunsetHour = noon + sunriseHA;

        double maghribHour;
        if (calcMethod.maghribAngle > 0) {
            double magHA = getHourAngle(calcMethod.maghribAngle, latitude, decl);
            maghribHour = Double.isNaN(magHA) ? sunsetHour : (noon + magHA);
        } else {
            maghribHour = sunsetHour;
        }

        double chashtHour = sunriseHour + 0.35; // ~20 min after sunrise
        double asrHour = noon + getAsrHourAngle(juristicMethod.shadowFactor, latitude, decl);

        // Calculate Night Duration
        double nextSunriseHour = sunriseHour + 24.0;
        double nightDurationHours = nextSunriseHour - sunsetHour;

        double fajrHour;
        double fajrHA = getHourAngle(fajrAngle, latitude, decl);
        if (Double.isNaN(fajrHA) || calcMethod == PrayerSettingsManager.CalculationMethod.MWL_HIGH_LATITUDE) {
            // Angle based high latitude rule: proportional fraction of night
            double portion = Math.min(nightDurationHours / 2.0, nightDurationHours * (fajrAngle / 60.0));
            fajrHour = sunriseHour - portion;
        } else {
            fajrHour = noon - fajrHA;
        }

        double ishaHour;
        if (calcMethod.ishaIntervalMinutes > 0) {
            ishaHour = maghribHour + (calcMethod.ishaIntervalMinutes / 60.0);
        } else {
            double ishaHA = getHourAngle(ishaAngle, latitude, decl);
            if (Double.isNaN(ishaHA) || calcMethod == PrayerSettingsManager.CalculationMethod.MWL_HIGH_LATITUDE) {
                double portion = Math.min(nightDurationHours / 2.0, nightDurationHours * (ishaAngle / 60.0));
                ishaHour = maghribHour + portion;
            } else {
                ishaHour = noon + ishaHA;
            }
        }

        // Base calendar start of day
        Calendar base = (Calendar) calendar.clone();
        base.set(Calendar.HOUR_OF_DAY, 0);
        base.set(Calendar.MINUTE, 0);
        base.set(Calendar.SECOND, 0);
        base.set(Calendar.MILLISECOND, 0);
        long startOfDay = base.getTimeInMillis();

        PrayerTimesResult res = new PrayerTimesResult();
        res.fajrMillis = startOfDay + (long) (fajrHour * 3600 * 1000) + (fajrOffsetMin * 60 * 1000);
        res.sunriseMillis = startOfDay + (long) (sunriseHour * 3600 * 1000) + (sunriseOffsetMin * 60 * 1000);
        res.ishraqMillis = res.sunriseMillis + (15 * 60 * 1000);
        res.zohrMillis = startOfDay + (long) (noon * 3600 * 1000) + (dhuhrOffsetMin * 60 * 1000);
        res.chashtMillis = res.sunriseMillis + ((res.zohrMillis - res.sunriseMillis) / 2);
        res.asrMillis = startOfDay + (long) (asrHour * 3600 * 1000) + (asrOffsetMin * 60 * 1000);
        res.sunsetMillis = startOfDay + (long) (sunsetHour * 3600 * 1000);
        res.maghribMillis = startOfDay + (long) (maghribHour * 3600 * 1000) + (maghribOffsetMin * 60 * 1000);
        res.awwabinMillis = res.maghribMillis + (15 * 60 * 1000);
        res.ishaMillis = startOfDay + (long) (ishaHour * 3600 * 1000) + (ishaOffsetMin * 60 * 1000);

        // Next Day Sunrise for Night Calculations (Midnight & Last Third)
        long nextSunriseMillis = startOfDay + (long) (nextSunriseHour * 3600 * 1000);
        long nightDuration = nextSunriseMillis - res.sunsetMillis;

        res.midnightMillis = res.sunsetMillis + (nightDuration / 2);
        res.lastThirdOfNightMillis = res.sunsetMillis + (2 * nightDuration / 3);
        res.tahajjudMillis = res.lastThirdOfNightMillis;

        res.sehriMillis = res.fajrMillis; // Subhe Sadiq / Fajr start time
        res.iftarMillis = res.maghribMillis; // Sunset / Maghrib start time

        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        res.fajrStr = timeFormat.format(new Date(res.fajrMillis)).toLowerCase();
        res.sunriseStr = timeFormat.format(new Date(res.sunriseMillis)).toLowerCase();
        res.ishraqStr = timeFormat.format(new Date(res.ishraqMillis)).toLowerCase();
        res.chashtStr = timeFormat.format(new Date(res.chashtMillis)).toLowerCase();
        res.zohrStr = timeFormat.format(new Date(res.zohrMillis)).toLowerCase();
        res.asrStr = timeFormat.format(new Date(res.asrMillis)).toLowerCase();
        res.sunsetStr = timeFormat.format(new Date(res.sunsetMillis)).toLowerCase();
        res.maghribStr = timeFormat.format(new Date(res.maghribMillis)).toLowerCase();
        res.awwabinStr = timeFormat.format(new Date(res.awwabinMillis)).toLowerCase();
        res.ishaStr = timeFormat.format(new Date(res.ishaMillis)).toLowerCase();
        res.midnightStr = timeFormat.format(new Date(res.midnightMillis)).toLowerCase();
        res.lastThirdOfNightStr = timeFormat.format(new Date(res.lastThirdOfNightMillis)).toLowerCase();
        res.tahajjudStr = timeFormat.format(new Date(res.tahajjudMillis)).toLowerCase();
        res.sehriStr = timeFormat.format(new Date(res.sehriMillis)).toLowerCase();
        res.iftarStr = timeFormat.format(new Date(res.iftarMillis)).toLowerCase();
        res.jummahMillis = startOfDay + (long) (noon * 3600 * 1000) + ((long) jummahOffsetMin * 60 * 1000);
        res.jummahStr = timeFormat.format(new Date(res.jummahMillis)).toLowerCase();
        res.isFriday = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;
        if (res.isFriday) {
            res.zohrMillis = res.jummahMillis;
            res.zohrStr = res.jummahStr;
        }

        res.chashtRangeStr = res.sunriseStr + " - " + res.zohrStr;

        // Determine current waqt & countdown
        long now = calendar.getTimeInMillis();
        computeCurrentWaqtAndNextPrayer(now, res);

        return res;
    }

    private static void computeCurrentWaqtAndNextPrayer(long now, PrayerTimesResult res) {
        if (now < res.fajrMillis) {
            res.currentWaqtName = "তাহাজ্জুদ / সেহরি";
            res.nextPrayerName = "ফজর";
            res.nextPrayerTimeStr = res.fajrStr;
            res.nextPrayerRemainingSeconds = Math.max(0, (res.fajrMillis - now) / 1000);
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = 85;
        } else if (now < res.sunriseMillis) {
            res.currentWaqtName = "ফজর";
            res.nextPrayerName = "সূর্যোদয় / ইশরাক";
            res.nextPrayerTimeStr = res.sunriseStr;
            res.nextPrayerRemainingSeconds = Math.max(0, (res.sunriseMillis - now) / 1000);
            long total = res.sunriseMillis - res.fajrMillis;
            long elapsed = now - res.fajrMillis;
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else if (now < res.zohrMillis) {
            res.currentWaqtName = "চাশত / ইশরাক";
            res.nextPrayerName = res.isFriday ? "জুম্মা" : "যোহর";
            res.nextPrayerTimeStr = res.zohrStr;
            res.nextPrayerRemainingSeconds = Math.max(0, (res.zohrMillis - now) / 1000);
            long total = res.zohrMillis - res.sunriseMillis;
            long elapsed = now - res.sunriseMillis;
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else if (now < res.asrMillis) {
            res.currentWaqtName = res.isFriday ? "জুম্মা" : "যোহর";
            res.nextPrayerName = "আসর";
            res.nextPrayerTimeStr = res.asrStr;
            res.nextPrayerRemainingSeconds = Math.max(0, (res.asrMillis - now) / 1000);
            long total = res.asrMillis - res.zohrMillis;
            long elapsed = now - res.zohrMillis;
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else if (now < res.maghribMillis) {
            res.currentWaqtName = "আসর";
            res.nextPrayerName = "মাগরিব";
            res.nextPrayerTimeStr = res.maghribStr;
            res.nextPrayerRemainingSeconds = Math.max(0, (res.maghribMillis - now) / 1000);
            long total = res.maghribMillis - res.asrMillis;
            long elapsed = now - res.asrMillis;
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else if (now < res.ishaMillis) {
            res.currentWaqtName = "মাগরিব";
            res.nextPrayerName = "এশা";
            res.nextPrayerTimeStr = res.ishaStr;
            res.nextPrayerRemainingSeconds = Math.max(0, (res.ishaMillis - now) / 1000);
            long total = res.ishaMillis - res.maghribMillis;
            long elapsed = now - res.maghribMillis;
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        } else {
            res.currentWaqtName = "এশা";
            res.nextPrayerName = "ফজর";
            res.nextPrayerTimeStr = res.fajrStr;
            long nextFajrMillis = res.fajrMillis + (24 * 3600 * 1000);
            res.nextPrayerRemainingSeconds = Math.max(0, (nextFajrMillis - now) / 1000);
            long total = nextFajrMillis - res.ishaMillis;
            long elapsed = now - res.ishaMillis;
            res.currentWaqtRemainingSeconds = res.nextPrayerRemainingSeconds;
            res.currentWaqtProgressPercent = (int) Math.min(100, Math.max(0, (elapsed * 100) / total));
        }

        // Fasting / Sehri & Iftar Countdown
        if (now >= res.sehriMillis && now < res.iftarMillis) {
            // Daytime: After Sehri ends until Iftar starts
            res.isFastingDaytime = true;
            res.fastingCountdownTitle = "ইফতার শুরু হতে বাকি";
            res.fastingRemainingSeconds = Math.max(0, (res.iftarMillis - now) / 1000);
            res.iftarRemainingSeconds = res.fastingRemainingSeconds;
        } else {
            // Nighttime: After Iftar until next morning's Sehri ends
            res.isFastingDaytime = false;
            res.fastingCountdownTitle = "সেহরি শেষ হতে বাকি";
            long targetSehri = res.sehriMillis;
            if (now >= res.iftarMillis) {
                // Next day's Sehri
                targetSehri += 24 * 60 * 60 * 1000L;
            }
            res.fastingRemainingSeconds = Math.max(0, (targetSehri - now) / 1000);
            res.iftarRemainingSeconds = res.fastingRemainingSeconds;
        }

        long fHours = res.fastingRemainingSeconds / 3600;
        long fMinutes = (res.fastingRemainingSeconds % 3600) / 60;
        long fSecs = res.fastingRemainingSeconds % 60;
        String rawFastingCountdown = String.format(Locale.ENGLISH, "%02d : %02d : %02d", fHours, fMinutes, fSecs);
        res.fastingCountdownTimeStr = BengaliNumberUtil.toBengali(rawFastingCountdown);
    }

    private static double getJulianDay(int year, int month, int day) {
        if (month <= 2) {
            year -= 1;
            month += 12;
        }
        double a = Math.floor((double) year / 100.0);
        double b = 2.0 - a + Math.floor(a / 4.0);
        return Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5;
    }

    private static double getHourAngle(double alpha, double lat, double decl) {
        double dLat = Math.toRadians(lat);
        double dDecl = Math.toRadians(decl);
        double dAlpha = Math.toRadians(alpha);

        double cosHA = (-Math.sin(dAlpha) - Math.sin(dLat) * Math.sin(dDecl)) / (Math.cos(dLat) * Math.cos(dDecl));
        cosHA = Math.max(-1.0, Math.min(1.0, cosHA));
        return Math.toDegrees(Math.acos(cosHA)) / 15.0;
    }

    private static double getAsrHourAngle(int shadowFactor, double lat, double decl) {
        double dLat = Math.toRadians(lat);
        double dDecl = Math.toRadians(decl);

        double tanAlpha = 1.0 / (shadowFactor + Math.tan(Math.abs(dLat - dDecl)));
        double alpha = Math.toDegrees(Math.atan(tanAlpha));
        return getHourAngle(-alpha, lat, decl);
    }

    private static double fixAngle(double angle) {
        angle = angle - 360.0 * Math.floor(angle / 360.0);
        return angle < 0 ? angle + 360.0 : angle;
    }

    private static double fixHour(double hour) {
        hour = hour - 24.0 * Math.floor(hour / 24.0);
        return hour < 0 ? hour + 24.0 : hour;
    }

    public static String getWaqtName(String waqtKey, boolean isBn) {
        if (waqtKey == null) return isBn ? "ওয়াক্ত" : "Waqt";
        String lower = waqtKey.toLowerCase().trim();
        if (lower.contains("fajr") || lower.contains("ফজর")) {
            return isBn ? "ফজর" : "Fajr";
        } else if (lower.contains("sunrise") || lower.contains("সূর্যোদয়") || lower.contains("সূর্যোদয়")) {
            return isBn ? "সূর্যোদয়" : "Sunrise";
        } else if (lower.contains("chasht") || lower.contains("duha") || lower.contains("চাশত")) {
            return isBn ? "চাশত / দুহা" : "Chasht / Duha";
        } else if (lower.contains("jummah") || lower.contains("জুম্মা") || lower.contains("জুমুআ")) {
            return isBn ? "জুম্মা" : "Jummah";
        } else if (lower.contains("zohr") || lower.contains("dhuhr") || lower.contains("যোহর") || lower.contains("জোহর")) {
            return isBn ? "জোহর" : "Dhuhr";
        } else if (lower.contains("asr") || lower.contains("আসর")) {
            return isBn ? "আসর" : "Asr";
        } else if (lower.contains("maghrib") || lower.contains("মাগরিব")) {
            return isBn ? "মাগরিব" : "Maghrib";
        } else if (lower.contains("isha") || lower.contains("এশা")) {
            return isBn ? "এশা" : "Isha";
        } else if (lower.contains("tahajjud") || lower.contains("তাহাজ্জুদ")) {
            return isBn ? "তাহাজ্জুদ / সেহরি" : "Tahajjud / Sehri";
        }
        return waqtKey;
    }

    public static String getFastingCountdownTitle(boolean isFastingDaytime, boolean isBn) {
        if (isFastingDaytime) {
            return isBn ? "ইফতার শুরু হতে বাকি" : "Time until Iftar";
        } else {
            return isBn ? "সেহরি শেষ হতে বাকি" : "Time until Sehri ends";
        }
    }
}
