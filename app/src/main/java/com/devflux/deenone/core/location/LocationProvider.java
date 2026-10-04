package com.devflux.deenone.core.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.devflux.deenone.core.constants.AppConstants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.List;
import java.util.Locale;

public class LocationProvider {

  public static final String PREF_NAME = "deanone_location_prefs";
  public static final String KEY_LOC_NAME = "key_location_name";
  public static final String KEY_LATITUDE = "key_latitude";
  public static final String KEY_LONGITUDE = "key_longitude";
  public static final String KEY_TIMEZONE = "key_timezone";
  public static final String KEY_MODE = "key_location_mode";
  public static final String KEY_TIMEZONE_ID = "key_timezone_id";

  public static String getLocationMode(Context context) {
    if (context == null) return "AUTO_GPS";
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getString(KEY_MODE, "AUTO_GPS");
  }

  public static void setLocationMode(Context context, String mode) {
    if (context == null || mode == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    prefs.edit().putString(KEY_MODE, mode).apply();
  }

  public static String getSavedTimezoneId(Context context) {
    if (context == null) return "Asia/Dhaka";
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String defaultTzId = java.util.TimeZone.getDefault().getID();
    return prefs.getString(KEY_TIMEZONE_ID, defaultTzId != null && !defaultTzId.isEmpty() ? defaultTzId : "Asia/Dhaka");
  }

  public static void setSavedTimezoneId(Context context, String timezoneId) {
    if (context == null || timezoneId == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    prefs.edit().putString(KEY_TIMEZONE_ID, timezoneId).apply();
  }

  public interface LocationCallback {
    void onLocationResolved(Coordinates coordinates);
    void onLocationFailed(String errorReason, Coordinates fallbackCoordinates);
  }

  public static String formatCityNameOnly(String rawLocation) {
    return formatCityNameOnly(rawLocation, true);
  }

  public static String formatCityNameOnly(String rawLocation, boolean isBn) {
    if (rawLocation == null || rawLocation.trim().isEmpty()) {
      return isBn ? "ঢাকা" : "Dhaka";
    }
    String clean = rawLocation.trim();
    // Strip any bracket pollution like "(ঢাকা)" or "(Dhaka)"
    if (clean.contains("(")) {
      int idx = clean.indexOf('(');
      clean = clean.substring(0, idx).trim();
    }
    if (clean.contains(",")) {
      String[] parts = clean.split(",");
      if (parts.length > 0 && !parts[0].trim().isEmpty()) {
        clean = parts[0].trim();
      }
    }

    // Comprehensive Dual-Language City Mapping
    String lower = clean.toLowerCase(Locale.ENGLISH);
    if (lower.equals("tabuk") || clean.equals("তবুক") || clean.equals("তাবুক")) {
      return isBn ? "তাবুক" : "Tabuk";
    } else if (lower.equals("dhaka") || clean.equals("ঢাকা")) {
      return isBn ? "ঢাকা" : "Dhaka";
    } else if (lower.equals("makkah") || lower.equals("mecca") || clean.equals("মক্কা")) {
      return isBn ? "মক্কা" : "Makkah";
    } else if (lower.equals("madinah") || lower.equals("medina") || clean.equals("মদিনা")) {
      return isBn ? "মদিনা" : "Madinah";
    } else if (lower.equals("riyadh") || clean.equals("রিয়াদ") || clean.equals("রিয়াদ")) {
      return isBn ? "রিয়াদ" : "Riyadh";
    } else if (lower.equals("jeddah") || clean.equals("জেদ্দা")) {
      return isBn ? "জেদ্দা" : "Jeddah";
    } else if (lower.equals("chittagong") || lower.equals("chattogram") || clean.equals("চট্টগ্রাম")) {
      return isBn ? "চট্টগ্রাম" : "Chattogram";
    } else if (lower.equals("sylhet") || clean.equals("সিলেট")) {
      return isBn ? "সিলেট" : "Sylhet";
    } else if (lower.equals("rajshahi") || clean.equals("রাজশাহী")) {
      return isBn ? "রাজশাহী" : "Rajshahi";
    } else if (lower.equals("khulna") || clean.equals("খুলনা")) {
      return isBn ? "খুলনা" : "Khulna";
    } else if (lower.equals("barishal") || clean.equals("বরিশাল")) {
      return isBn ? "বরিশাল" : "Barishal";
    } else if (lower.equals("rangpur") || clean.equals("রংপুর")) {
      return isBn ? "রংপুর" : "Rangpur";
    } else if (lower.equals("mymensingh") || clean.equals("ময়মনসিংহ")) {
      return isBn ? "ময়মনসিংহ" : "Mymensingh";
    } else if (lower.equals("cumilla") || lower.equals("comilla") || clean.equals("কুমিল্লা")) {
      return isBn ? "কুমিল্লা" : "Cumilla";
    }
    return clean;
  }

  public static class Coordinates {
    public final String locationName;
    public final double latitude;
    public final double longitude;
    public final double timezone;

    public Coordinates(String locationName, double latitude, double longitude, double timezone) {
      this.locationName = formatCityNameOnly(locationName);
      this.latitude = latitude;
      this.longitude = longitude;
      this.timezone = timezone;
    }
  }

  public static Coordinates getSavedOrCurrentLocation(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String name = prefs.getString(KEY_LOC_NAME, AppConstants.DEFAULT_LOCATION_NAME);
    double lat = Double.longBitsToDouble(prefs.getLong(KEY_LATITUDE, Double.doubleToLongBits(AppConstants.DEFAULT_LATITUDE)));
    double lng = Double.longBitsToDouble(prefs.getLong(KEY_LONGITUDE, Double.doubleToLongBits(AppConstants.DEFAULT_LONGITUDE)));
    double tz = Double.longBitsToDouble(prefs.getLong(KEY_TIMEZONE, Double.doubleToLongBits(AppConstants.DEFAULT_TIMEZONE)));
    return new Coordinates(name, lat, lng, tz);
  }

  public static void saveLocation(Context context, Coordinates coordinates) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    prefs.edit()
        .putString(KEY_LOC_NAME, coordinates.locationName)
        .putLong(KEY_LATITUDE, Double.doubleToLongBits(coordinates.latitude))
        .putLong(KEY_LONGITUDE, Double.doubleToLongBits(coordinates.longitude))
        .putLong(KEY_TIMEZONE, Double.doubleToLongBits(coordinates.timezone))
        .apply();

    com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(context);
  }

  @SuppressLint("MissingPermission")
  public static void requestCurrentGpsLocation(Context context, LocationCallback callback) {
    boolean hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    boolean hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    if (!hasFine && !hasCoarse) {
      callback.onLocationFailed("লোকেশন পারমিশন মঞ্জুর করা হয়নি। ম্যানুয়াল লোকেশন ব্যবহৃত হচ্ছে।", getSavedOrCurrentLocation(context));
      return;
    }

    try {
      FusedLocationProviderClient fusedClient = LocationServices.getFusedLocationProviderClient(context);
      CancellationTokenSource cts = new CancellationTokenSource();

      fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.getToken())
          .addOnSuccessListener(location -> {
            if (location != null) {
              resolveAndSave(context, location, callback);
            } else {
              // Try last known location
              fusedClient.getLastLocation().addOnSuccessListener(lastLoc -> {
                if (lastLoc != null) {
                  resolveAndSave(context, lastLoc, callback);
                } else {
                  callback.onLocationFailed("জিপিএস সিগন্যাল পাওয়া যায়নি। ডিফল্ট লোকেশন ব্যবহৃত হচ্ছে।", getSavedOrCurrentLocation(context));
                }
              }).addOnFailureListener(e -> callback.onLocationFailed(e.getMessage(), getSavedOrCurrentLocation(context)));
            }
          })
          .addOnFailureListener(e -> callback.onLocationFailed(e.getMessage(), getSavedOrCurrentLocation(context)));
    } catch (Exception e) {
      callback.onLocationFailed(e.getMessage(), getSavedOrCurrentLocation(context));
    }
  }

  private static void resolveAndSave(Context context, Location location, LocationCallback callback) {
    double lat = location.getLatitude();
    double lng = location.getLongitude();
    double tz = (java.util.TimeZone.getDefault().getRawOffset()) / 3600000.0;
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

    // Auto-configure international calculation method based on geographical region
    if ("SA".equals(countryCode) || "AE".equals(countryCode) || "QA".equals(countryCode) || "KW".equals(countryCode) || "BH".equals(countryCode) || "OM".equals(countryCode) || (lat >= 16 && lat <= 32 && lng >= 34 && lng <= 56)) {
      // Saudi Arabia (Tabuk, Makkah, Madinah, Riyadh) & Gulf Countries -> Umm Al-Qura University
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.UMM_AL_QURA);
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.SHAFI);
    } else if ("EG".equals(countryCode) || (lat >= 22 && lat <= 32 && lng >= 24 && lng <= 37)) {
      // Egypt & North Africa -> Egyptian General Authority of Survey
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.EGYPTIAN);
    } else if ("PK".equals(countryCode) || "IN".equals(countryCode) || "AF".equals(countryCode)) {
      // Pakistan, India, Afghanistan -> Karachi University of Islamic Sciences
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.KARACHI);
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI);
    } else if ("BD".equals(countryCode)) {
      // Bangladesh -> Islamic Foundation Bangladesh
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.BANGLADESH_ISLAMIC_FOUNDATION);
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setJuristicMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.JuristicMethod.HANAFI);
    } else if ("US".equals(countryCode) || "CA".equals(countryCode)) {
      // North America -> ISNA
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.ISNA);
    } else {
      // Rest of the World / Europe -> Muslim World League (MWL)
      com.devflux.deenone.core.prayer.PrayerSettingsManager.setCalculationMethod(context, com.devflux.deenone.core.prayer.PrayerSettingsManager.CalculationMethod.MUSLIM_WORLD_LEAGUE);
    }

    Coordinates coords = new Coordinates(""+ resolvedName, lat, lng, tz);
    String currentMode = getLocationMode(context);
    if (!"MANUAL".equals(currentMode) && !"TIMEZONE_ONLY".equals(currentMode)) {
      saveLocation(context, coords);
      callback.onLocationResolved(coords);
    } else {
      callback.onLocationResolved(coords);
    }
  }

  public static Coordinates getDefaultLocation() {
    return new Coordinates(
        AppConstants.DEFAULT_LOCATION_NAME,
        AppConstants.DEFAULT_LATITUDE,
        AppConstants.DEFAULT_LONGITUDE,
        AppConstants.DEFAULT_TIMEZONE
    );
  }
}
