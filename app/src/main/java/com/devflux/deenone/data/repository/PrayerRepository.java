package com.devflux.deenone.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.devflux.deenone.core.location.LocationProvider;
import com.devflux.deenone.data.local.AppDatabase;
import com.devflux.deenone.data.local.dao.PrayerScheduleDao;
import com.devflux.deenone.data.local.entity.PrayerScheduleEntity;
import com.devflux.deenone.data.remote.ApiClient;
import com.devflux.deenone.data.remote.model.PrayerTimeApiResponse;
import com.devflux.deenone.domain.repository.IPrayerRepository;
import com.devflux.deenone.utils.PrayerCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrayerRepository implements IPrayerRepository {

  private static final String TAG = "PrayerRepository";
  private final PrayerScheduleDao prayerScheduleDao;

  private final Context context;

  public PrayerRepository(Context context) {
    this.context = context.getApplicationContext();
    AppDatabase db = AppDatabase.getInstance(context);
    this.prayerScheduleDao = db.prayerScheduleDao();
    refreshRealtimeSchedule();
  }

  @Override
  public LiveData<PrayerScheduleEntity> getLatestSchedule() {
    return prayerScheduleDao.getLatestSchedule();
  }

  public void updateSchedule(PrayerScheduleEntity entity) {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      prayerScheduleDao.insertOrUpdate(entity);
    });
  }

  public final void refreshRealtimeSchedule() {
    AppDatabase.databaseWriteExecutor.execute(() -> {
      try {
        LocationProvider.Coordinates coords =
            LocationProvider.getSavedOrCurrentLocation(context);

        Calendar cal = Calendar.getInstance();
        PrayerCalculator.PrayerTimesResult pt =
            PrayerCalculator.calculateForLocationWithContext(
                context, coords.latitude, coords.longitude, coords.timezone, cal
            );

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String today = sdf.format(cal.getTime());
        String locName = LocationProvider.formatCityNameOnly(coords.locationName);

        PrayerScheduleEntity schedule = new PrayerScheduleEntity(
            today,
            locName,
            pt.fajrStr != null ? pt.fajrStr.toLowerCase() : "04:30 am",
            pt.sunriseStr != null ? pt.sunriseStr.toLowerCase() : "05:45 am",
            pt.sunriseStr != null ? pt.sunriseStr.toLowerCase() : "06:00 am",
            pt.zohrStr != null ? pt.zohrStr.toLowerCase() : "12:05 pm",
            pt.asrStr != null ? pt.asrStr.toLowerCase() : "04:30 pm",
            pt.maghribStr != null ? pt.maghribStr.toLowerCase() : "06:25 pm",
            pt.ishaStr != null ? pt.ishaStr.toLowerCase() : "07:45 pm",
            pt.sehriStr != null ? pt.sehriStr.toLowerCase() : "04:20 am",
            pt.iftarStr != null ? pt.iftarStr.toLowerCase() : "06:25 pm"
        );
        prayerScheduleDao.insertOrUpdate(schedule);
      } catch (Exception e) {
        Log.e(TAG, "refreshRealtimeSchedule error: "+ e.getMessage());
      }
    });
  }

  @Override
  public void syncPrayerTimes(String city, String country) {
    ApiClient.getApiService().getPrayerTimingsByCity(city, country, 4)
        .enqueue(new Callback<PrayerTimeApiResponse>() {
          @Override
          public void onResponse(Call<PrayerTimeApiResponse> call, Response<PrayerTimeApiResponse> response) {
            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
              PrayerTimeApiResponse.Timings t = response.body().getData().getTimings();
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
              String today = sdf.format(new Date());

              PrayerScheduleEntity entity = new PrayerScheduleEntity(
                  today,
                  city + ", "+ country,
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
              updateSchedule(entity);
            }
          }

          @Override
          public void onFailure(Call<PrayerTimeApiResponse> call, Throwable t) {
            Log.e(TAG, "Failed to sync prayer times: "+ t.getMessage());
          }
        });
  }
}
