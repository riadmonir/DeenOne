package com.devflux.deenone.core.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.data.remote.ApiClient;
import com.devflux.deenone.data.remote.model.PrayerTimeApiResponse;
import com.devflux.deenone.utils.PrayerCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SunriseSunsetSyncManager {

    private static final String TAG = "SunriseSunsetSync";
    private static final String PREF_NAME = "deanone_sunrise_sunset_prefs";
    private static final String KEY_CACHED_SUNRISE = "key_cached_sunrise";
    private static final String KEY_CACHED_SUNSET = "key_cached_sunset";
    private static final String KEY_CACHED_DATE = "key_cached_date";
    private static final String KEY_LAST_SYNC_TS = "key_last_sync_timestamp";

    // 4 hours in milliseconds
    public static final long SYNC_INTERVAL_MILLIS = 4 * 60 * 60 * 1000L;

    public static class SunTimes {
        public final String sunrise;
        public final String sunset;
        public final boolean isFromOnline;

        public SunTimes(String sunrise, String sunset, boolean isFromOnline) {
            this.sunrise = sunrise;
            this.sunset = sunset;
            this.isFromOnline = isFromOnline;
        }
    }

    private static volatile SunriseSunsetSyncManager instance;
    private final MutableLiveData<SunTimes> liveSunTimes = new MutableLiveData<>();
    private final Handler autoSyncHandler = new Handler(Looper.getMainLooper());
    private Runnable autoSyncRunnable;

    public static SunriseSunsetSyncManager getInstance() {
        if (instance == null) {
            synchronized (SunriseSunsetSyncManager.class) {
                if (instance == null) {
                    instance = new SunriseSunsetSyncManager();
                }
            }
        }
        return instance;
    }

    public LiveData<SunTimes> getLiveSunTimes() {
        return liveSunTimes;
    }

    public SunTimes getCachedSunTimes(Context context) {
        if (context == null) return null;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String savedDate = prefs.getString(KEY_CACHED_DATE, "");
        String todayDate = getTodayDateKey();

        if (todayDate.equals(savedDate)) {
            String sunrise = prefs.getString(KEY_CACHED_SUNRISE, null);
            String sunset = prefs.getString(KEY_CACHED_SUNSET, null);
            if (sunrise != null && sunset != null) {
                return new SunTimes(sunrise, sunset, true);
            }
        }
        return null;
    }

    public void saveSunTimes(Context context, String sunrise, String sunset) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_CACHED_DATE, getTodayDateKey())
                .putString(KEY_CACHED_SUNRISE, sunrise)
                .putString(KEY_CACHED_SUNSET, sunset)
                .putLong(KEY_LAST_SYNC_TS, System.currentTimeMillis())
                .apply();

        liveSunTimes.postValue(new SunTimes(sunrise, sunset, true));
    }

    /**
     * Resolves Sun times:
     * 1. Checks online API (Aladhan timings by precise coordinates & date)
     * 2. On success -> saves to cache & notifies LiveData
     * 3. On offline / failure -> uses cached data or precise astronomical NOAA trigonometric formula
     */
    public void syncSunTimes(Context context, double latitude, double longitude, double timezone) {
        if (context == null) return;

        // Provide immediate astronomical fallback while network fetches
        SunTimes cached = getCachedSunTimes(context);
        if (cached != null) {
            liveSunTimes.postValue(cached);
        } else {
            // Astronomical calculation for date & GPS
            PrayerCalculator.PrayerTimesResult astro = PrayerCalculator.calculateForLocationWithContext(
                    context, latitude, longitude, timezone, Calendar.getInstance()
            );
            liveSunTimes.postValue(new SunTimes(astro.sunriseStr, astro.sunsetStr, false));
        }

        if (!NetworkConnectivityHelper.isOnline(context)) {
            Log.d(TAG, "Offline mode. Utilizing cached/astronomical solar data.");
            return;
        }

        int methodCode = getMethodCodeForCountry(latitude, longitude);

        ApiClient.getApiService().getPrayerTimingsByCoordinates(latitude, longitude, methodCode)
                .enqueue(new Callback<PrayerTimeApiResponse>() {
                    @Override
                    public void onResponse(Call<PrayerTimeApiResponse> call, Response<PrayerTimeApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            PrayerTimeApiResponse.Timings timings = response.body().getData().getTimings();
                            String rawSunrise = timings.getSunrise();
                            String rawSunset = timings.getSunset();

                            String formattedSunrise = formatTimeTo12Hour(rawSunrise);
                            String formattedSunset = formatTimeTo12Hour(rawSunset);

                            saveSunTimes(context, formattedSunrise, formattedSunset);
                            Log.d(TAG, "Online astronomical Sunrise: " + formattedSunrise + ", Sunset: " + formattedSunset + " synced successfully.");
                        }
                    }

                    @Override
                    public void onFailure(Call<PrayerTimeApiResponse> call, Throwable t) {
                        Log.w(TAG, "Online SunTimes sync failed: " + t.getMessage());
                    }
                });
    }

    /**
     * Starts 4-hour periodic auto-sync engine and registers network connectivity auto-sync.
     */
    public void start4HourPeriodicSync(Context context) {
        if (context == null) return;
        Context appContext = context.getApplicationContext();

        // 1. Initial Sync
        LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(appContext);
        syncSunTimes(appContext, coords.latitude, coords.longitude, coords.timezone);

        // 2. Schedule 4-hour periodic loop
        stopPeriodicSync();
        autoSyncRunnable = new Runnable() {
            @Override
            public void run() {
                LocationProvider.Coordinates currentCoords = LocationProvider.getSavedOrCurrentLocation(appContext);
                syncSunTimes(appContext, currentCoords.latitude, currentCoords.longitude, currentCoords.timezone);
                autoSyncHandler.postDelayed(this, SYNC_INTERVAL_MILLIS);
            }
        };
        autoSyncHandler.postDelayed(autoSyncRunnable, SYNC_INTERVAL_MILLIS);

        // 3. Listen to network connectivity restoration
        NetworkConnectivityHelper.getNetworkStatusLiveData(appContext).observeForever(isOnline -> {
            if (Boolean.TRUE.equals(isOnline)) {
                SharedPreferences prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                long lastSync = prefs.getLong(KEY_LAST_SYNC_TS, 0);
                long now = System.currentTimeMillis();
                // If more than 4 hours passed since last successful sync, refresh immediately
                if (now - lastSync > SYNC_INTERVAL_MILLIS) {
                    LocationProvider.Coordinates c = LocationProvider.getSavedOrCurrentLocation(appContext);
                    syncSunTimes(appContext, c.latitude, c.longitude, c.timezone);
                }
            }
        });
    }

    public void stopPeriodicSync() {
        if (autoSyncRunnable != null) {
            autoSyncHandler.removeCallbacks(autoSyncRunnable);
        }
    }

    private static String getTodayDateKey() {
        return new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
    }

    private static String formatTimeTo12Hour(String rawTime) {
        if (rawTime == null || rawTime.trim().isEmpty()) return "6:00 am";
        try {
            // Clean any timezone prefix like "05:42 (BDT)"
            String clean = rawTime.split(" ")[0].trim();
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.US);
            SimpleDateFormat sdf12 = new SimpleDateFormat("h:mm a", Locale.US);
            Date date = sdf24.parse(clean);
            if (date != null) {
                return sdf12.format(date).toLowerCase();
            }
        } catch (Exception ignored) {}
        return rawTime.toLowerCase();
    }

    private static int getMethodCodeForCountry(double lat, double lng) {
        if (lat >= 16 && lat <= 32 && lng >= 34 && lng <= 56) {
            return 4; // Umm Al-Qura, Makkah (Saudi Arabia / Gulf)
        } else if (lat >= 22 && lat <= 32 && lng >= 24 && lng <= 37) {
            return 5; // Egyptian General Authority of Survey
        } else if (lat >= 20 && lat <= 37 && lng >= 60 && lng <= 90) {
            return 1; // Karachi / South Asia
        } else if (lat >= 25 && lat <= 49 && lng >= -125 && lng <= -65) {
            return 2; // ISNA (North America)
        } else {
            return 3; // Muslim World League (MWL)
        }
    }
}