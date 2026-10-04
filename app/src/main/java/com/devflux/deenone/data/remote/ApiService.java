package com.devflux.deenone.data.remote;

import com.devflux.deenone.data.remote.model.PrayerTimeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v1/timingsByCity")
    Call<PrayerTimeApiResponse> getPrayerTimingsByCity(
            @Query("city") String city,
            @Query("country") String country,
            @Query("method") int method
    );

    @GET("v1/timings")
    Call<PrayerTimeApiResponse> getPrayerTimingsByCoordinates(
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("method") int method
    );
}
