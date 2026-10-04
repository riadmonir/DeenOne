package com.devflux.deenone.core.wearable;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.devflux.deenone.R;
import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.utils.BengaliNumberUtil;
import com.devflux.deenone.utils.PrayerCalculator;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WearOsSyncManager {

  private static final String TAG = "WearOsSyncManager";
  private static final String PREF_NAME = "deanone_wear_os_prefs";
  public static final String KEY_WEAR_SYNC_ENABLED = "key_wear_sync_enabled";
  public static final String KEY_WEAR_THEME = "key_wear_theme"; // EMERALD, OLED_DARK, NAVY, GOLD

  private static volatile WearOsSyncManager instance;

  public static WearOsSyncManager getInstance() {
    if (instance == null) {
      synchronized (WearOsSyncManager.class) {
        if (instance == null) {
          instance = new WearOsSyncManager();
        }
      }
    }
    return instance;
  }

  public static void syncToWearable(Context context) {
    getInstance().syncWithWearableDevices(context);
  }

  public static boolean isWearSyncEnabled(Context context) {
    if (context == null) return true;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getBoolean(KEY_WEAR_SYNC_ENABLED, true);
  }

  public static void setWearSyncEnabled(Context context, boolean enabled) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    prefs.edit().putBoolean(KEY_WEAR_SYNC_ENABLED, enabled).apply();
  }

  public static String getWearTheme(Context context) {
    if (context == null) return "EMERALD";
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getString(KEY_WEAR_THEME, "EMERALD");
  }

  public static void setWearTheme(Context context, String theme) {
    if (context == null) return;
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    prefs.edit().putString(KEY_WEAR_THEME, theme).apply();
  }

  public static class WearablePrayerPayload {
    public String currentWaqt;
    public String currentRange;
    public String nextWaqt;
    public String nextTime;
    public String remainingTime;
    public int progressPercent;
    public double qiblaBearing;
    public String locationName;
    public String theme;
    public String sehriTime;
    public String iftarTime;

    public JSONObject toJson() {
      JSONObject obj = new JSONObject();
      try {
        obj.put("currentWaqt", currentWaqt);
        obj.put("currentRange", currentRange);
        obj.put("nextWaqt", nextWaqt);
        obj.put("nextTime", nextTime);
        obj.put("remainingTime", remainingTime);
        obj.put("progressPercent", progressPercent);
        obj.put("qiblaBearing", qiblaBearing);
        obj.put("locationName", locationName);
        obj.put("theme", theme);
        obj.put("sehriTime", sehriTime);
        obj.put("iftarTime", iftarTime);
      } catch (Exception ignored) {}
      return obj;
    }
  }

  public WearablePrayerPayload computeCurrentPayload(Context context) {
    LocationProvider.Coordinates coords = LocationProvider.getSavedOrCurrentLocation(context);
    Calendar nowCal = Calendar.getInstance();
    PrayerCalculator.PrayerTimesResult res = PrayerCalculator.calculateForLocationWithContext(
        context, coords.latitude, coords.longitude, coords.timezone, nowCal
    );

    long now = nowCal.getTimeInMillis();
    String currentWaqt = "ফজর";
    String currentRange = res.fajrStr + "- "+ res.sunriseStr;
    String nextWaqt = "জোহর";
    String nextTime = res.zohrStr;
    long nextTarget = res.zohrMillis;
    long waqtStart = res.fajrMillis;

    if (now < res.fajrMillis) {
      currentWaqt = "তাহাজ্জুদ";
      currentRange = res.lastThirdOfNightStr + "- "+ res.fajrStr;
      nextWaqt = "ফজর";
      nextTime = res.fajrStr;
      nextTarget = res.fajrMillis;
      waqtStart = res.lastThirdOfNightMillis;
    } else if (now < res.sunriseMillis) {
      currentWaqt = "ফজর";
      currentRange = res.fajrStr + "- "+ res.sunriseStr;
      nextWaqt = "সূর্যোদয়";
      nextTime = res.sunriseStr;
      nextTarget = res.sunriseMillis;
      waqtStart = res.fajrMillis;
    } else if (now < res.zohrMillis) {
      currentWaqt = "চাশত";
      currentRange = res.sunriseStr + "- "+ res.zohrStr;
      nextWaqt = res.isFriday ? "জুম্মা" : "জোহর";
      nextTime = res.zohrStr;
      nextTarget = res.zohrMillis;
      waqtStart = res.sunriseMillis;
    } else if (now < res.asrMillis) {
      currentWaqt = res.isFriday ? "জুম্মা" : "জোহর";
      currentRange = res.zohrStr + "- "+ res.asrStr;
      nextWaqt = "আসর";
      nextTime = res.asrStr;
      nextTarget = res.asrMillis;
      waqtStart = res.zohrMillis;
    } else if (now < res.maghribMillis) {
      currentWaqt = "আসর";
      currentRange = res.asrStr + "- "+ res.maghribStr;
      nextWaqt = "মাগরিব";
      nextTime = res.maghribStr;
      nextTarget = res.maghribMillis;
      waqtStart = res.asrMillis;
    } else if (now < res.ishaMillis) {
      currentWaqt = "মাগরিব";
      currentRange = res.maghribStr + "- "+ res.ishaStr;
      nextWaqt = "এশা";
      nextTime = res.ishaStr;
      nextTarget = res.ishaMillis;
      waqtStart = res.maghribMillis;
    } else {
      currentWaqt = "এশা";
      currentRange = res.ishaStr + "- "+ res.fajrStr;
      nextWaqt = "ফজর";
      nextTime = res.fajrStr;
      nextTarget = res.fajrMillis + (24 * 3600 * 1000);
      waqtStart = res.ishaMillis;
    }

    long diffSec = Math.max(0, (nextTarget - now) / 1000);
    long hrs = diffSec / 3600;
    long mins = (diffSec % 3600) / 60;
    String remStr = (hrs > 0 ? BengaliNumberUtil.toBengali(hrs) + "ঘণ্টা ": "") + BengaliNumberUtil.toBengali(mins) + "মিনিট";

    long totalDuration = Math.max(1, nextTarget - waqtStart);
    long elapsed = Math.max(0, now - waqtStart);
    int prog = (int) Math.min(100, Math.max(0, (elapsed * 100) / totalDuration));

    // Calculate Qibla Bearing to Kaaba (21.4225 N, 39.8262 E)
    double kaabaLat = Math.toRadians(21.4225);
    double kaabaLng = Math.toRadians(39.8262);
    double userLat = Math.toRadians(coords.latitude);
    double userLng = Math.toRadians(coords.longitude);
    double dLng = kaabaLng - userLng;

    double y = Math.sin(dLng);
    double x = Math.cos(userLat) * Math.tan(kaabaLat) - Math.sin(userLat) * Math.cos(dLng);
    double qiblaDegrees = (Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0;

    WearablePrayerPayload payload = new WearablePrayerPayload();
    payload.currentWaqt = currentWaqt;
    payload.currentRange = currentRange;
    payload.nextWaqt = nextWaqt;
    payload.nextTime = nextTime;
    payload.remainingTime = remStr;
    payload.progressPercent = prog;
    payload.qiblaBearing = Math.round(qiblaDegrees * 10.0) / 10.0;
    payload.locationName = coords.locationName.replace("", "");
    payload.theme = getWearTheme(context);
    payload.sehriTime = res.sehriStr;
    payload.iftarTime = res.iftarStr;

    return payload;
  }

  public void syncWithWearableDevices(Context context) {
    if (context == null || !isWearSyncEnabled(context)) return;

    try {
      WearablePrayerPayload payload = computeCurrentPayload(context);
      Log.d(TAG, "Broadcasting sync payload to Wear OS companion: "+ payload.toJson().toString());

      // Build WearableExtender for companion notification delivery on smartwatch
      NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
          .setContentIntentAvailableOffline(true)
          .setHintHideIcon(false);

      // Also broadcast locally for any listening Wear OS bridging modules
      android.content.Intent syncIntent = new android.content.Intent("com.devflux.deenone.WEAR_OS_PRAYER_SYNC");
      syncIntent.putExtra("payload_json", payload.toJson().toString());
      context.sendBroadcast(syncIntent);

    } catch (Exception e) {
      Log.e(TAG, "Error in Wear OS sync pipeline: "+ e.getMessage());
    }
  }
}