package com.devflux.deenone.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class PrayerTimeApiResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private PrayerData data;

    public int getCode() { return code; }
    public String getStatus() { return status; }
    public PrayerData getData() { return data; }

    public static class PrayerData {
        @SerializedName("timings")
        private Timings timings;

        @SerializedName("date")
        private DateInfo date;

        public Timings getTimings() { return timings; }
        public DateInfo getDate() { return date; }
    }

    public static class Timings {
        @SerializedName("Fajr")
        private String fajr;
        @SerializedName("Sunrise")
        private String sunrise;
        @SerializedName("Dhuhr")
        private String dhuhr;
        @SerializedName("Asr")
        private String asr;
        @SerializedName("Sunset")
        private String sunset;
        @SerializedName("Maghrib")
        private String maghrib;
        @SerializedName("Isha")
        private String isha;
        @SerializedName("Imsak")
        private String imsak;

        public String getFajr() { return fajr; }
        public String getSunrise() { return sunrise; }
        public String getDhuhr() { return dhuhr; }
        public String getAsr() { return asr; }
        public String getSunset() { return sunset; }
        public String getMaghrib() { return maghrib; }
        public String getIsha() { return isha; }
        public String getImsak() { return imsak; }
    }

    public static class DateInfo {
        @SerializedName("readable")
        private String readable;

        @SerializedName("hijri")
        private HijriDate hijri;

        public String getReadable() { return readable; }
        public HijriDate getHijri() { return hijri; }
    }

    public static class HijriDate {
        @SerializedName("date")
        private String date;
        @SerializedName("day")
        private String day;
        @SerializedName("year")
        private String year;

        public String getDate() { return date; }
        public String getDay() { return day; }
        public String getYear() { return year; }
    }
}
