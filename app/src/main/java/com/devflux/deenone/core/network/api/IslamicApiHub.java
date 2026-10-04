package com.devflux.deenone.core.network.api;

import com.devflux.deenone.data.remote.ApiService;
import com.devflux.deenone.data.remote.model.HadithApiResponse;
import com.devflux.deenone.data.remote.model.PrayerTimeApiResponse;
import com.devflux.deenone.data.remote.model.QuranApiResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class IslamicApiHub {

    public static final String BASE_URL_ALADHAN = "https://api.aladhan.com/";
    public static final String BASE_URL_ALQURAN = "https://api.alquran.cloud/v1/";
    public static final String BASE_URL_HADITH = "https://hadithapi.com/api/";

    public interface QuranApiService {
        @GET("surah/{surahNumber}/quran-uthmani")
        Call<QuranApiResponse> getSurahUthmani(@Path("surahNumber") int surahNumber);

        @GET("surah/{surahNumber}/bn.bengali")
        Call<QuranApiResponse> getSurahBengaliTranslation(@Path("surahNumber") int surahNumber);

        @GET("surah/{surahNumber}/en.sahih")
        Call<QuranApiResponse> getSurahEnglishTranslation(@Path("surahNumber") int surahNumber);
    }

    public interface HadithApiService {
        @GET("hadiths")
        Call<HadithApiResponse> getHadithsByBook(
                @Query("apiKey") String apiKey,
                @Query("book") String bookSlug,
                @Query("paginate") int paginate
        );
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static ApiService getPrayerService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_ALADHAN)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    public static QuranApiService getQuranService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_ALQURAN)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(QuranApiService.class);
    }

    public static HadithApiService getHadithService() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL_HADITH)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HadithApiService.class);
    }
}
