package com.devflux.deenone.core.amal;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserLocationTimezoneHelper {

    private static final String PREFS_TIMEZONE = "user_location_tz_prefs";
    private static final String KEY_SERVER_TIME_OFFSET = "server_time_offset_ms";
    private static final String KEY_LAST_CALCULATED_DATE = "last_calculated_date_key";

    public static TimeZone getUserCurrentTimeZone(Context context) {
        if (context == null) return TimeZone.getDefault();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIMEZONE, Context.MODE_PRIVATE);
        String customTzId = prefs.getString("custom_timezone_id", null);
        if (customTzId != null && !customTzId.isEmpty()) {
            return TimeZone.getTimeZone(customTzId);
        }
        return TimeZone.getDefault();
    }

    public static void setCustomTimeZone(Context context, String timeZoneId) {
        if (context == null || timeZoneId == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIMEZONE, Context.MODE_PRIVATE);
        prefs.edit().putString("custom_timezone_id", timeZoneId).apply();
    }

    public static void updateServerTimeOffset(Context context, long serverUtcTimestampMs) {
        if (context == null) return;
        long localUtcTimestampMs = System.currentTimeMillis();
        long drift = serverUtcTimestampMs - localUtcTimestampMs;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIMEZONE, Context.MODE_PRIVATE);
        prefs.edit().putLong(KEY_SERVER_TIME_OFFSET, drift).apply();
    }

    public static long getAuthoritativeCurrentTimeMillis(Context context) {
        if (context == null) return System.currentTimeMillis();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIMEZONE, Context.MODE_PRIVATE);
        long drift = prefs.getLong(KEY_SERVER_TIME_OFFSET, 0L);
        return System.currentTimeMillis() + drift;
    }

    public static String getTodayLocationDateString(Context context) {
        TimeZone tz = getUserCurrentTimeZone(context);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setTimeZone(tz);
        Date authoritativeDate = new Date(getAuthoritativeCurrentTimeMillis(context));
        return sdf.format(authoritativeDate);
    }

    public static String getTodayMonthYearBangla(Context context) {
        return getTodayMonthYearFormatted(context, true);
    }

    public static String getTodayMonthYearFormatted(Context context, boolean isBn) {
        TimeZone tz = getUserCurrentTimeZone(context);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", isBn ? new Locale("bn", "BD") : Locale.US);
        sdf.setTimeZone(tz);
        Date authoritativeDate = new Date(getAuthoritativeCurrentTimeMillis(context));
        return sdf.format(authoritativeDate);
    }


    public static long getMillisUntilLocationMidnight(Context context) {
        TimeZone tz = getUserCurrentTimeZone(context);
        Calendar now = Calendar.getInstance(tz);
        now.setTimeInMillis(getAuthoritativeCurrentTimeMillis(context));

        Calendar midnight = Calendar.getInstance(tz);
        midnight.setTimeInMillis(now.getTimeInMillis());
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DAY_OF_YEAR, 1);

        return Math.max(0, midnight.getTimeInMillis() - now.getTimeInMillis());
    }

    public static boolean hasDateChangedSinceLastCheck(Context context) {
        String today = getTodayLocationDateString(context);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_TIMEZONE, Context.MODE_PRIVATE);
        String lastDate = prefs.getString(KEY_LAST_CALCULATED_DATE, "");
        if (!today.equals(lastDate)) {
            prefs.edit().putString(KEY_LAST_CALCULATED_DATE, today).apply();
            return true;
        }
        return false;
    }
}
