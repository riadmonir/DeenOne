package com.devflux.deenone.core.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AutoLocationUpdateManager {

  private static final String TAG = "AutoLocationManager";
  private static volatile AutoLocationUpdateManager instance;

  private final MutableLiveData<LocationProvider.Coordinates> liveCoordinates = new MutableLiveData<>();
  private FusedLocationProviderClient fusedClient;
  private LocationCallback locationCallback;
  private boolean isListening = false;

  public static AutoLocationUpdateManager getInstance() {
    if (instance == null) {
      synchronized (AutoLocationUpdateManager.class) {
        if (instance == null) {
          instance = new AutoLocationUpdateManager();
        }
      }
    }
    return instance;
  }

  public LiveData<LocationProvider.Coordinates> getLiveCoordinates() {
    return liveCoordinates;
  }

  @SuppressLint("MissingPermission")
  public void startAutoLocationUpdates(Context context) {
    if (context == null || isListening) return;

    String currentMode = LocationProvider.getLocationMode(context);
    if ("MANUAL".equals(currentMode) || "TIMEZONE_ONLY".equals(currentMode)) {
      Log.d(TAG, "Location mode is "+ currentMode + ". Using saved location.");
      liveCoordinates.postValue(LocationProvider.getSavedOrCurrentLocation(context));
      return;
    }

    boolean hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    boolean hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    if (!hasFine && !hasCoarse) {
      Log.d(TAG, "Location permission not granted. Using saved location.");
      liveCoordinates.postValue(LocationProvider.getSavedOrCurrentLocation(context));
      return;
    }

    try {
      fusedClient = LocationServices.getFusedLocationProviderClient(context);

      LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5 * 60 * 1000)
          .setMinUpdateIntervalMillis(60 * 1000)
          .setMinUpdateDistanceMeters(1000) // 1 KM displacement threshold for automatic travel detection
          .build();

      locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
          if (locationResult == null || locationResult.getLastLocation() == null) return;
          Location location = locationResult.getLastLocation();
          processNewLocation(context, location);
        }
      };

      fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
      isListening = true;

      // Also trigger immediate GPS fix
      LocationProvider.requestCurrentGpsLocation(context, new LocationProvider.LocationCallback() {
        @Override
        public void onLocationResolved(LocationProvider.Coordinates coordinates) {
          liveCoordinates.postValue(coordinates);
        }

        @Override
        public void onLocationFailed(String errorReason, LocationProvider.Coordinates fallbackCoordinates) {
          liveCoordinates.postValue(fallbackCoordinates);
        }
      });

    } catch (Exception e) {
      Log.e(TAG, "Error starting location updates: "+ e.getMessage());
    }
  }

  public void stopAutoLocationUpdates() {
    if (fusedClient != null && locationCallback != null) {
      try {
        fusedClient.removeLocationUpdates(locationCallback);
        isListening = false;
      } catch (Exception ignored) {}
    }
  }

  public void processNewLocation(Context context, Location location) {
    if (context == null || location == null) return;

    String currentMode = LocationProvider.getLocationMode(context);
    if ("MANUAL".equals(currentMode) || "TIMEZONE_ONLY".equals(currentMode)) {
      Log.d(TAG, "Skipping GPS location update because mode is "+ currentMode);
      return;
    }

    double lat = location.getLatitude();
    double lng = location.getLongitude();
    double tz = (TimeZone.getDefault().getRawOffset()) / 3600000.0;
    String resolvedName = "বর্তমান জিপিএস লোকেশন";
    String countryCode = "";

    try {
      Geocoder geocoder = new Geocoder(context, new Locale("bn", "BD"));
      List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
      if (addresses != null && !addresses.isEmpty()) {
        Address addr = addresses.get(0);
        countryCode = addr.getCountryCode() != null ? addr.getCountryCode().toUpperCase() : "";
        String locality = addr.getLocality() != null ? addr.getLocality() : addr.getSubAdminArea();
        String country = addr.getCountryName() != null ? addr.getCountryName() : "";
        if (locality != null && !locality.isEmpty()) {
          resolvedName = locality;
        } else if (addr.getSubAdminArea() != null && !addr.getSubAdminArea().isEmpty()) {
          resolvedName = addr.getSubAdminArea();
        } else if (addr.getAdminArea() != null && !addr.getAdminArea().isEmpty()) {
          resolvedName = addr.getAdminArea();
        } else if (!country.isEmpty()) {
          resolvedName = country;
        }
      }
    } catch (Exception ignored) {}

    // Auto-configure calculation method and juristic school based on country & coords
    if ("SA".equals(countryCode) || "AE".equals(countryCode) || "QA".equals(countryCode) || "KW".equals(countryCode) || "BH".equals(countryCode) || "OM".equals(countryCode) || (lat >= 16 && lat <= 32 && lng >= 34 && lng <= 56)) {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.UMM_AL_QURA);
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.SHAFI);
    } else if ("EG".equals(countryCode) || (lat >= 22 && lat <= 32 && lng >= 24 && lng <= 37)) {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.EGYPTIAN);
    } else if ("PK".equals(countryCode) || "IN".equals(countryCode) || "AF".equals(countryCode)) {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.KARACHI);
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI);
    } else if ("BD".equals(countryCode)) {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION);
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI);
    } else if ("US".equals(countryCode) || "CA".equals(countryCode)) {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.ISNA);
    } else {
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.MUSLIM_WORLD_LEAGUE);
    }

    LocationProvider.Coordinates coords = new LocationProvider.Coordinates(""+ resolvedName, lat, lng, tz);
    LocationProvider.saveLocation(context, coords);
    liveCoordinates.postValue(coords);

    // Reschedule all local prayer alarms, Adhans, and lockscreen reminders
    com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(context);
    com.devflux.deenone.core.notifications.DailyIslamicReminderScheduler.scheduleAllDailyReminders(context);
    com.devflux.deenone.core.notifications.SmartIslamicReminderEngine.scheduleAllSmartReminders(context);
  }
}