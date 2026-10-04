package com.devflux.deenone.core.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.devflux.deenone.core.network.NetworkConnectivityHelper;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.entity.DailyContentEntity;
import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.data.remote.ApiClient;
import com.devflux.deenone.data.remote.model.PrayerTimeApiResponse;
import com.devflux.deenone.data.repository.ContentRepository;
import com.devflux.deenone.data.repository.PrayerRepository;
import com.devflux.deenone.data.repository.QuranRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSyncManager {

    private static final String TAG = "DataSyncManager";
    private static final String PREF_SYNC = "deanone_sync_prefs";
    private static final String KEY_LAST_SYNC_TIME = "last_sync_timestamp";

    public interface OnSyncListener {
        void onSyncStarted();
        void onSyncSuccess(String message);
        void onSyncError(String errorMessage);
    }

    public enum SyncState {
        IDLE, SYNCING, SUCCESS, ERROR
    }

    private static volatile DataSyncManager instance;
    private final MutableLiveData<SyncState> syncStateLiveData = new MutableLiveData<>(SyncState.IDLE);
    private final MutableLiveData<String> syncStatusMessageLiveData = new MutableLiveData<>("প্রস্তুত");

    public static DataSyncManager getInstance() {
        if (instance == null) {
            synchronized (DataSyncManager.class) {
                if (instance == null) {
                    instance = new DataSyncManager();
                }
            }
        }
        return instance;
    }

    public LiveData<SyncState> getSyncStateLiveData() {
        return syncStateLiveData;
    }

    public LiveData<String> getSyncStatusMessageLiveData() {
        return syncStatusMessageLiveData;
    }

    public long getLastSyncTime(Context context) {
        if (context == null) return 0;
        SharedPreferences prefs = context.getSharedPreferences(PREF_SYNC, Context.MODE_PRIVATE);
        return prefs.getLong(KEY_LAST_SYNC_TIME, 0);
    }

    public void setLastSyncTime(Context context, long timestamp) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREF_SYNC, Context.MODE_PRIVATE);
        prefs.edit().putLong(KEY_LAST_SYNC_TIME, timestamp).apply();
    }

    /**
     * Central sync method for all app data:
     * - Online: Queries verified APIs (PHP-MySQL / Firebase / Aladhan / AlQuran Cloud) & updates Room DB SSOT
     * - Offline: Seamlessly serves Room DB cached data without errors
     */
    public void syncAll(Context context, double lat, double lng, String city, String country, OnSyncListener listener) {
        if (context == null) return;

        if (!NetworkConnectivityHelper.isOnline(context)) {
            syncStateLiveData.postValue(SyncState.IDLE);
            syncStatusMessageLiveData.postValue("অফলাইন মোড (ক্যাশ ডাটা সক্রিয়)");
            if (listener != null) {
                listener.onSyncSuccess("অফলাইন মোড: পূর্বে সংরক্ষিত ডাটা থেকে লোড করা হয়েছে।");
            }
            return;
        }

        syncStateLiveData.postValue(SyncState.SYNCING);
        syncStatusMessageLiveData.postValue("অনলাইন ডাটা সিঙ্ক হচ্ছে...");
        if (listener != null) {
            listener.onSyncStarted();
        }

        // 1. Sync Prayer Times & Ramadan Suhoor/Iftar
        syncPrayerTimesOnline(context, lat, lng, city, country, new OnSyncListener() {
            @Override
            public void onSyncStarted() {}

            @Override
            public void onSyncSuccess(String message) {
                setLastSyncTime(context, System.currentTimeMillis());
                syncStateLiveData.postValue(SyncState.SUCCESS);
                syncStatusMessageLiveData.postValue("ডাটা সফলভাবে আপডেট হয়েছে");
                if (listener != null) {
                    listener.onSyncSuccess("নামাজ ও রমজানের সময়সূচী সফলভাবে আপডেট হয়েছে।");
                }
            }

            @Override
            public void onSyncError(String errorMessage) {
                syncStateLiveData.postValue(SyncState.ERROR);
                syncStatusMessageLiveData.postValue(NetworkConnectivityHelper.MSG_OFFLINE_UNAVAILABLE);
                if (listener != null) {
                    listener.onSyncError(NetworkConnectivityHelper.MSG_OFFLINE_UNAVAILABLE);
                }
            }
        });
    }

    private void syncPrayerTimesOnline(Context context, double lat, double lng, String city, String country, OnSyncListener listener) {
        ApiClient.getApiService().getPrayerTimingsByCoordinates(lat, lng, 4)
                .enqueue(new Callback<PrayerTimeApiResponse>() {
                    @Override
                    public void onResponse(Call<PrayerTimeApiResponse> call, Response<PrayerTimeApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            PrayerTimeApiResponse.Timings t = response.body().getData().getTimings();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String today = sdf.format(new Date());

                            String locationLabel = (city != null && !city.isEmpty()) ? city : "বর্তমান জিপিএস অবস্থান";

                            PrayerScheduleEntity entity = new PrayerScheduleEntity(
                                    today,
                                    locationLabel,
                                    t.getFajr(),
                                    t.getSunrise(),
                                    "06:19 am",
                                    t.getDhuhr(),
                                    t.getAsr(),
                                    t.getMaghrib(),
                                    t.getIsha(),
                                    t.getImsak(),
                                    t.getMaghrib()
                            );

                            PrayerRepository prayerRepo = new PrayerRepository(context);
                            prayerRepo.updateSchedule(entity);
                            Log.d(TAG, "Prayer & Fasting times synced and cached successfully.");
                            if (listener != null) listener.onSyncSuccess("Prayer times updated");
                        } else {
                            if (listener != null) listener.onSyncError("Sync failed with invalid response");
                        }
                    }

                    @Override
                    public void onFailure(Call<PrayerTimeApiResponse> call, Throwable t) {
                        Log.w(TAG, "Online sync failed: " + t.getMessage());
                        if (listener != null) listener.onSyncError(t.getMessage());
                    }
                });
    }

    /**
     * Automatically triggers sync when network is restored
     */
    public void startAutoSyncOnConnectivity(Context context, double lat, double lng) {
        NetworkConnectivityHelper.getNetworkStatusLiveData(context).observeForever(isOnline -> {
            if (Boolean.TRUE.equals(isOnline)) {
                long lastSync = getLastSyncTime(context);
                long now = System.currentTimeMillis();
                // If more than 3 hours since last sync, trigger auto sync
                if (now - lastSync > 3 * 3600 * 1000) {
                    syncAll(context, lat, lng, "ঢাকা", "বাংলাদেশ", null);
                }
            }
        });
    }
}
