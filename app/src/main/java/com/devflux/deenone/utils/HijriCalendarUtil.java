package com.devflux.deenone.utils;

import java.util.Calendar;
import java.util.Date;

public class HijriCalendarUtil {

    private static final String[] HIJRI_MONTHS_BENGALI = {
            "মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
            "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
            "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
    };

    private static final String[] HIJRI_MONTHS_ENGLISH = {
            "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
            "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
            "Ramadan", "Shawwal", "Dhu al-Qadah", "Dhu al-Hijjah"
    };

    public static class HijriDateResult {
        public int day;
        public int monthIndex; // 0-based
        public String monthNameBengali;
        public String monthNameEnglish;
        public int year;

        public String getFormattedBengali() {
            return BengaliNumberUtil.toBengali(day) + " " + monthNameBengali + " " + BengaliNumberUtil.toBengali(year) + " হিজরী ↗";
        }

        public String getFormattedEnglish() {
            return day + " " + monthNameEnglish + " " + year + " AH ↗";
        }

        public String getFormatted(boolean isBn) {
            return isBn ? getFormattedBengali() : getFormattedEnglish();
        }
    }

    public static HijriDateResult getRealHijriDate(Calendar calendar) {
        if (calendar == null) calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 1-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Kuwaiti Algorithm for accurate Hijri conversion
        if (year < 1900) return getFallbackHijri();

        int m = month;
        int y = year;
        if (m < 3) {
            y -= 1;
            m += 12;
        }

        double a = Math.floor(y / 100.0);
        double b = 2 - a + Math.floor(a / 4.0);
        if (y < 1583) b = 0;
        if (y == 1582) {
            if (m > 10) b = -10;
            if (m == 10) {
                b = 0;
                if (day > 4) b = -10;
            }
        }

        double jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day + b - 1524;

        b = 0;
        if (jd > 2299160) {
            a = Math.floor((jd - 1867216.25) / 36524.25);
            b = 1 + a - Math.floor(a / 4.0);
        }
        double bb = jd + b + 1524;
        double cc = Math.floor((bb - 122.1) / 365.25);
        double dd = Math.floor(365.25 * cc);
        double ee = Math.floor((bb - dd) / 30.6001);
        day = (int) (bb - dd - Math.floor(30.6001 * ee));
        month = (int) (ee - 1);
        if (ee > 13) {
            cc += 1;
            month = (int) (ee - 13);
        }
        year = (int) (cc - 4716);

        double wd = (jd + 1) % 7;
        double iyear = 10631.0 / 30.0;
        double epochastro = 1948084;
        double shift1 = 8.01 / 60.0;

        double z = jd - epochastro;
        double cyc = Math.floor(z / 10631.0);
        z = z - 10631 * cyc;
        double j = Math.floor((z - shift1) / iyear);
        double iy = 30 * cyc + j;
        z = z - Math.floor(j * iyear + shift1);
        double im = Math.floor((z + 28.5001) / 29.5);
        if (im == 13) im = 12;
        double id = z - Math.floor(29.5001 * im - 29);

        HijriDateResult result = new HijriDateResult();
        result.day = (int) id;
        result.monthIndex = Math.max(0, Math.min(11, (int) im - 1));
        result.monthNameBengali = HIJRI_MONTHS_BENGALI[result.monthIndex];
        result.monthNameEnglish = HIJRI_MONTHS_ENGLISH[result.monthIndex];
        result.year = (int) iy;

        if (result.day <= 0) result.day = 1;

        return result;
    }

    private static HijriDateResult getFallbackHijri() {
        HijriDateResult r = new HijriDateResult();
        r.day = 4;
        r.monthIndex = 2; // রবিউল আউয়াল
        r.monthNameBengali = HIJRI_MONTHS_BENGALI[2];
        r.monthNameEnglish = HIJRI_MONTHS_ENGLISH[2];
        r.year = 1448;
        return r;
    }

    public static String getRealGregorianDateBengali(Calendar calendar) {
        if (calendar == null) calendar = Calendar.getInstance();

        String dayOfWeek = BengaliNumberUtil.getBengaliDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        String dayOfMonth = BengaliNumberUtil.toBengali(calendar.get(Calendar.DAY_OF_MONTH));
        String month = BengaliNumberUtil.getBengaliMonthName(calendar.get(Calendar.MONTH));
        String year = BengaliNumberUtil.toBengali(calendar.get(Calendar.YEAR));

        return dayOfWeek + ", " + dayOfMonth + " " + month + " " + year;
    }

    public static String getRealGregorianDateEnglish(Calendar calendar) {
        if (calendar == null) calendar = Calendar.getInstance();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", java.util.Locale.ENGLISH);
        return sdf.format(calendar.getTime());
    }

    public static String getRealGregorianDate(Calendar calendar, boolean isBn) {
        return isBn ? getRealGregorianDateBengali(calendar) : getRealGregorianDateEnglish(calendar);
    }

    private static final String PREF_NAME = "deanone_hijri_prefs";
    private static final String KEY_HIJRI_ADJUSTMENT = "key_hijri_adjustment_days";

    public static int getHijriAdjustment(android.content.Context context) {
        if (context == null) return 0;
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE);
        return prefs.getInt(KEY_HIJRI_ADJUSTMENT, 0);
    }

    public static int getAdjustmentDays(android.content.Context context) {
        return getHijriAdjustment(context);
    }

    public static void setHijriAdjustment(android.content.Context context, int adjustmentDays) {
        if (context == null) return;
        android.content.SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, android.content.Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_HIJRI_ADJUSTMENT, adjustmentDays).apply();
    }

    public static void setAdjustmentDays(android.content.Context context, int adjustmentDays) {
        setHijriAdjustment(context, adjustmentDays);
    }

    public static HijriDateResult getAdjustedHijriDate(android.content.Context context, Calendar calendar) {
        if (calendar == null) calendar = Calendar.getInstance();
        if (context == null) return getRealHijriDate(calendar);

        int manualAdjustment = getHijriAdjustment(context);
        Calendar workCal = (Calendar) calendar.clone();

        // 1. Hijri Date Change Timing (Maghrib vs Midnight)
        com.devflux.deenone.core.calendar.CalendarSettingsManager.HijriChangeTiming timing =
                com.devflux.deenone.core.calendar.CalendarSettingsManager.getHijriChangeTiming(context);

        if (timing == com.devflux.deenone.core.calendar.CalendarSettingsManager.HijriChangeTiming.MAGHRIB_SUNSET) {
            long nowTime = calendar.getTimeInMillis();
            boolean isAfterMaghrib = false;
            try {
                com.devflux.deenone.core.location.LocationProvider.Coordinates coords =
                        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(context);
                if (coords != null) {
                    PrayerCalculator.PrayerTimesResult pt = PrayerCalculator.calculateForLocationWithContext(
                            context, coords.latitude, coords.longitude, coords.timezone, calendar
                    );
                    if (pt != null && pt.maghribMillis > 0) {
                        isAfterMaghrib = (nowTime >= pt.maghribMillis);
                    }
                }
            } catch (Exception ignored) {
            }

            if (!isAfterMaghrib) {
                // Fallback check if coordinates not resolved: approx 18:15 (Sunset)
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int currentMinutes = hour * 60 + minute;
                if (currentMinutes >= (18 * 60 + 15)) {
                    isAfterMaghrib = true;
                }
            }

            if (isAfterMaghrib) {
                workCal.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        if (manualAdjustment != 0) {
            workCal.add(Calendar.DAY_OF_YEAR, manualAdjustment);
        }

        return getRealHijriDate(workCal);
    }

    public static HijriDateResult getRealHijriDate(Calendar calendar, int adjustmentDays) {
        if (calendar == null) calendar = Calendar.getInstance();
        if (adjustmentDays == 0) {
            return getRealHijriDate(calendar);
        }
        Calendar adjustedCal = (Calendar) calendar.clone();
        adjustedCal.add(Calendar.DAY_OF_YEAR, adjustmentDays);
        return getRealHijriDate(adjustedCal);
    }
}
