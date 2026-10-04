package com.devflux.deenone.core.prayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.devflux.deenone.core.auth.AuthManager;
import com.devflux.deenone.core.backend.BackendConfigManager;
import com.devflux.deenone.core.gamification.GamificationManager;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.PrayerLogEntity;
import com.devflux.deenone.data.repository.AmalRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * QazaCalculatorManager — Manages persistent lifetime Qaza prayer counts,
 * automated missed-prayer calculation, make-up adjustments, and real-time cloud synchronization.
 */
public class QazaCalculatorManager {

    private static final String TAG = "QazaCalculatorManager";
    private static final String PREF_NAME = "qaza_calculator_prefs";

    public static final String KEY_FAJR = "qaza_fajr";
    public static final String KEY_DHUHR = "qaza_dhuhr";
    public static final String KEY_ASR = "qaza_asr";
    public static final String KEY_MAGHRIB = "qaza_maghrib";
    public static final String KEY_ISHA = "qaza_isha";
    public static final String KEY_WITR = "qaza_witr";

    private static final String KEY_FIRST_INSTALL_MILLIS = "first_install_millis";
    private static final String KEY_LAST_AUTO_CALC_DAY = "last_auto_calc_day";

    public static final String[] ALL_KEYS = {
            KEY_FAJR, KEY_DHUHR, KEY_ASR, KEY_MAGHRIB, KEY_ISHA, KEY_WITR
    };

    private static final OkHttpClient httpClient = new OkHttpClient();
    private static final MediaType JSON_MEDIA = MediaType.parse("application/json; charset=utf-8");

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int getQazaCount(Context context, String key) {
        if (context == null) return 0;
        return getPrefs(context).getInt(key, 0);
    }

    public static void setQazaCount(Context context, String key, int count) {
        if (context == null) return;
        getPrefs(context).edit().putInt(key, Math.max(0, count)).apply();
        syncQazaToServer(context);
    }

    public static int incrementQaza(Context context, String key) {
        if (context == null) return 0;
        int current = getQazaCount(context, key);
        int updated = current + 1;
        getPrefs(context).edit().putInt(key, updated).apply();
        syncQazaToServer(context);
        return updated;
    }

    public static int decrementQaza(Context context, String key) {
        if (context == null) return 0;
        int current = getQazaCount(context, key);
        if (current <= 0) return 0;
        int updated = current - 1;
        getPrefs(context).edit().putInt(key, updated).apply();

        // 1. Award XP for making up Qaza
        GamificationManager.addXP(context, 10);

        // 2. Map key to actual Waqt name & log
        String waqtName = keyToWaqtName(key);
        if (waqtName != null) {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                String todayIso = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
                AppDatabase db = AppDatabase.getInstance(context);
                PrayerLogEntity existing = db.prayerLogDao().getLog(todayIso, waqtName);

                // If today's prayer was missed or unlogged, mark it as performed Qaza to update profile stats
                if (existing == null || !existing.isPrayed() || "QAZA".equalsIgnoreCase(existing.getStatus())) {
                    PrayerLogEntity performedLog = new PrayerLogEntity(
                            todayIso, waqtName, true, false, false, "EKAKI", 10, System.currentTimeMillis()
                    );
                    db.prayerLogDao().insertOrUpdate(performedLog);
                    new AmalRepository(context).recalculateAndSyncTodayRecord();
                }
            });
        }

        // 3. Sync updated counts to cloud
        syncQazaToServer(context);

        return updated;
    }

    public static int getTotalQazaCount(Context context) {
        if (context == null) return 0;
        int total = 0;
        for (String key : ALL_KEYS) {
            total += getQazaCount(context, key);
        }
        return total;
    }

    public static Map<String, Integer> getAllCounts(Context context) {
        Map<String, Integer> map = new HashMap<>();
        if (context == null) return map;
        for (String key : ALL_KEYS) {
            map.put(key, getQazaCount(context, key));
        }
        return map;
    }

    public static String keyToWaqtName(String key) {
        switch (key) {
            case KEY_FAJR: return "Fajr";
            case KEY_DHUHR: return "Dhuhr";
            case KEY_ASR: return "Asr";
            case KEY_MAGHRIB: return "Maghrib";
            case KEY_ISHA: return "Isha";
            default: return null;
        }
    }

    public static String waqtNameToKey(String waqtName) {
        if (waqtName == null) return null;
        switch (waqtName.toLowerCase(Locale.US)) {
            case "fajr": return KEY_FAJR;
            case "dhuhr":
            case "duhr":
            case "johr": return KEY_DHUHR;
            case "asr": return KEY_ASR;
            case "maghrib": return KEY_MAGHRIB;
            case "isha": return KEY_ISHA;
            default: return null;
        }
    }

    /**
     * Retrieves the persistent first installation time in milliseconds.
     * Uses PackageManager firstInstallTime as source of truth and caches in SharedPreferences.
     */
    public static long getFirstInstallTimeMillis(Context context) {
        if (context == null) return System.currentTimeMillis();
        SharedPreferences prefs = getPrefs(context);
        long firstInstall = prefs.getLong(KEY_FIRST_INSTALL_MILLIS, 0L);
        if (firstInstall == 0L) {
            try {
                firstInstall = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
            } catch (Exception ignored) {
                firstInstall = System.currentTimeMillis();
            }
            if (firstInstall <= 0L) {
                firstInstall = System.currentTimeMillis();
            }
            prefs.edit().putLong(KEY_FIRST_INSTALL_MILLIS, firstInstall).apply();
        }
        return firstInstall;
    }

    public static void markWaqtEvaluated(Context context, String dateIso, String waqtName) {
        if (context == null || dateIso == null || waqtName == null) return;
        getPrefs(context).edit().putBoolean("qaza_eval_" + dateIso + "_" + waqtName, true).apply();
    }

    public static boolean isWaqtEvaluated(Context context, String dateIso, String waqtName) {
        if (context == null || dateIso == null || waqtName == null) return false;
        return getPrefs(context).getBoolean("qaza_eval_" + dateIso + "_" + waqtName, false);
    }

    /**
     * Automated missed prayers calculation since installation date/time.
     * Continuously evaluates unlogged or missed waqts from install time up to the current moment.
     * Any waqt whose time has ended after app installation and was not prayed on time
     * is automatically added to the Qaza counter.
     * Each waqt is evaluated idempotently (no double counting).
     */
    public static void autoCalculateMissedPrayers(Context context) {
        if (context == null) return;

        final Context appContext = context.getApplicationContext();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                SharedPreferences prefs = getPrefs(appContext);
                long firstInstall = getFirstInstallTimeMillis(appContext);
                long now = System.currentTimeMillis();

                AppDatabase db = AppDatabase.getInstance(appContext);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                Calendar startCal = Calendar.getInstance();
                // Cap scan window to maximum 30 days to ensure 60 FPS fast performance
                startCal.setTimeInMillis(Math.max(firstInstall, now - (30L * 24 * 3600 * 1000L)));
                startCal.set(Calendar.HOUR_OF_DAY, 0);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);
                startCal.set(Calendar.MILLISECOND, 0);

                Calendar todayCal = Calendar.getInstance();
                todayCal.setTimeInMillis(now);
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.set(Calendar.MINUTE, 0);
                todayCal.set(Calendar.SECOND, 0);
                todayCal.set(Calendar.MILLISECOND, 0);

                com.devflux.deenone.core.location.LocationProvider.Coordinates coords =
                        com.devflux.deenone.core.location.LocationProvider.getSavedOrCurrentLocation(appContext);
                double lat = coords != null ? coords.latitude : 23.8103;
                double lng = coords != null ? coords.longitude : 90.4125;
                double tz = coords != null ? coords.timezone : 6.0;

                int addedFajr = 0, addedDhuhr = 0, addedAsr = 0, addedMaghrib = 0, addedIsha = 0;
                boolean anyChange = false;

                Calendar currentCal = (Calendar) startCal.clone();
                // Iterate from install day up to today (inclusive)
                while (!currentCal.after(todayCal)) {
                    String dateIso = sdf.format(currentCal.getTime());

                    com.devflux.deenone.utils.PrayerCalculator.PrayerTimesResult pt =
                            com.devflux.deenone.utils.PrayerCalculator.calculateForLocationWithContext(
                                    appContext, lat, lng, tz, currentCal);

                    Calendar nextCal = (Calendar) currentCal.clone();
                    nextCal.add(Calendar.DAY_OF_YEAR, 1);
                    com.devflux.deenone.utils.PrayerCalculator.PrayerTimesResult ptNext =
                            com.devflux.deenone.utils.PrayerCalculator.calculateForLocationWithContext(
                                    appContext, lat, lng, tz, nextCal);

                    long fajrEnd = (pt != null) ? pt.sunriseMillis : 0;
                    long dhuhrEnd = (pt != null) ? pt.asrMillis : 0;
                    long asrEnd = (pt != null) ? pt.maghribMillis : 0;
                    long maghribEnd = (pt != null) ? pt.ishaMillis : 0;
                    long ishaEnd = (ptNext != null) ? ptNext.fajrMillis : (pt != null ? pt.fajrMillis + (24 * 3600 * 1000L) : 0);

                    String[] coreWaqts = {"Fajr", "Dhuhr", "Asr", "Maghrib", "Isha"};
                    long[] waqtEndTimes = {fajrEnd, dhuhrEnd, asrEnd, maghribEnd, ishaEnd};

                    for (int wIdx = 0; wIdx < coreWaqts.length; wIdx++) {
                        String w = coreWaqts[wIdx];
                        long endTime = waqtEndTimes[wIdx];

                        // Waqt has ended and ended AFTER app installation time
                        if (endTime > 0 && now >= endTime && endTime > firstInstall) {
                            String evalKey = "qaza_eval_" + dateIso + "_" + w;
                            if (!prefs.getBoolean(evalKey, false)) {
                                PrayerLogEntity log = db.prayerLogDao().getLog(dateIso, w);
                                boolean isPrayedOnTime = (log != null && log.isPrayed() && !"QAZA".equalsIgnoreCase(log.getStatus()));

                                if (isPrayedOnTime) {
                                    // User prayed on time
                                    prefs.edit().putBoolean(evalKey, true).apply();
                                } else {
                                    // User missed this prayer! Auto increment Qaza
                                    switch (w) {
                                        case "Fajr": addedFajr++; break;
                                        case "Dhuhr": addedDhuhr++; break;
                                        case "Asr": addedAsr++; break;
                                        case "Maghrib": addedMaghrib++; break;
                                        case "Isha": addedIsha++; break;
                                    }
                                    if (log == null || !log.isPrayed()) {
                                        PrayerLogEntity qazaLog = new PrayerLogEntity(
                                                dateIso, w, false, false, true, "QAZA", -20, endTime
                                        );
                                        db.prayerLogDao().insertOrUpdate(qazaLog);
                                    }
                                    prefs.edit().putBoolean(evalKey, true).apply();
                                    anyChange = true;
                                }
                            }
                        }
                    }

                    currentCal.add(Calendar.DAY_OF_YEAR, 1);
                }

                if (anyChange) {
                    SharedPreferences.Editor ed = prefs.edit();
                    if (addedFajr > 0) ed.putInt(KEY_FAJR, prefs.getInt(KEY_FAJR, 0) + addedFajr);
                    if (addedDhuhr > 0) ed.putInt(KEY_DHUHR, prefs.getInt(KEY_DHUHR, 0) + addedDhuhr);
                    if (addedAsr > 0) ed.putInt(KEY_ASR, prefs.getInt(KEY_ASR, 0) + addedAsr);
                    if (addedMaghrib > 0) ed.putInt(KEY_MAGHRIB, prefs.getInt(KEY_MAGHRIB, 0) + addedMaghrib);
                    if (addedIsha > 0) ed.putInt(KEY_ISHA, prefs.getInt(KEY_ISHA, 0) + addedIsha);
                    ed.apply();

                    syncQazaToServer(appContext);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in autoCalculateMissedPrayers: " + e.getMessage());
            }
        });
    }

    /**
     * Real-time Cloud Synchronization to PHP MySQL Backend.
     */
    public static void syncQazaToServer(Context context) {
        if (context == null) return;
        String userId = AuthManager.getCurrentSession(context).userId;
        if (userId == null || userId.isEmpty() || "usr_guest".equalsIgnoreCase(userId)) {
            return;
        }

        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "qaza.php");
            String apiKey = BackendConfigManager.getPhpApiKey(context);

            JsonObject body = new JsonObject();
            body.addProperty("user_id", userId);
            body.addProperty("fajr_qaza", getQazaCount(context, KEY_FAJR));
            body.addProperty("dhuhr_qaza", getQazaCount(context, KEY_DHUHR));
            body.addProperty("asr_qaza", getQazaCount(context, KEY_ASR));
            body.addProperty("maghrib_qaza", getQazaCount(context, KEY_MAGHRIB));
            body.addProperty("isha_qaza", getQazaCount(context, KEY_ISHA));
            body.addProperty("witr_qaza", getQazaCount(context, KEY_WITR));
            body.addProperty("total_qaza", getTotalQazaCount(context));

            Request req = new Request.Builder()
                    .url(endpoint)
                    .addHeader("X-API-KEY", apiKey != null ? apiKey : "deenone_secure_token_2026")
                    .post(RequestBody.create(body.toString(), JSON_MEDIA))
                    .build();

            httpClient.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) {
                    Log.w(TAG, "Qaza sync failed (offline): " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Qaza sync succeeded with backend");
                        }
                    } finally {
                        response.close();
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Failed to initiate Qaza sync: " + e.getMessage());
        }
    }

    /**
     * Fetches Qaza counts from Cloud Server on Login / Refresh.
     */
    public static void fetchQazaFromServer(Context context, Runnable onDone) {
        if (context == null) {
            if (onDone != null) onDone.run();
            return;
        }
        String userId = AuthManager.getCurrentSession(context).userId;
        if (userId == null || userId.isEmpty() || "usr_guest".equalsIgnoreCase(userId)) {
            if (onDone != null) onDone.run();
            return;
        }

        try {
            String endpoint = BackendConfigManager.getPhpApiEndpoint(context, "qaza.php?user_id=" + userId);
            String apiKey = BackendConfigManager.getPhpApiKey(context);

            Request req = new Request.Builder()
                    .url(endpoint)
                    .addHeader("X-API-KEY", apiKey != null ? apiKey : "deenone_secure_token_2026")
                    .get()
                    .build();

            httpClient.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) {
                    if (onDone != null) onDone.run();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String resStr = response.body().string();
                            JsonObject json = new Gson().fromJson(resStr, JsonObject.class);
                            if (json != null && json.has("success") && json.get("success").getAsBoolean()) {
                                JsonObject data = json.getAsJsonObject("data");
                                if (data != null) {
                                    SharedPreferences.Editor ed = getPrefs(context).edit();
                                    // Merge taking max between local and remote
                                    ed.putInt(KEY_FAJR, Math.max(getQazaCount(context, KEY_FAJR), data.get("fajr_qaza").getAsInt()));
                                    ed.putInt(KEY_DHUHR, Math.max(getQazaCount(context, KEY_DHUHR), data.get("dhuhr_qaza").getAsInt()));
                                    ed.putInt(KEY_ASR, Math.max(getQazaCount(context, KEY_ASR), data.get("asr_qaza").getAsInt()));
                                    ed.putInt(KEY_MAGHRIB, Math.max(getQazaCount(context, KEY_MAGHRIB), data.get("maghrib_qaza").getAsInt()));
                                    ed.putInt(KEY_ISHA, Math.max(getQazaCount(context, KEY_ISHA), data.get("isha_qaza").getAsInt()));
                                    ed.putInt(KEY_WITR, Math.max(getQazaCount(context, KEY_WITR), data.get("witr_qaza").getAsInt()));
                                    ed.apply();
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing remote Qaza data: " + e.getMessage());
                    } finally {
                        response.close();
                        if (onDone != null) onDone.run();
                    }
                }
            });
        } catch (Exception e) {
            if (onDone != null) onDone.run();
        }
    }
}
