package com.devflux.deenone.domain.model;

public class PrayerSchedule {
    private final String location;
    private final String fajr;
    private final String sunrise;
    private final String chasht;
    private final String zohr;
    private final String asr;
    private final String maghrib;
    private final String isha;
    private final String sehri;
    private final String iftar;

    public PrayerSchedule(String location, String fajr, String sunrise, String chasht,
                          String zohr, String asr, String maghrib, String isha,
                          String sehri, String iftar) {
        this.location = location;
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.chasht = chasht;
        this.zohr = zohr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
        this.sehri = sehri;
        this.iftar = iftar;
    }

    public String getLocation() { return location; }
    public String getFajr() { return fajr; }
    public String getSunrise() { return sunrise; }
    public String getChasht() { return chasht; }
    public String getZohr() { return zohr; }
    public String getAsr() { return asr; }
    public String getMaghrib() { return maghrib; }
    public String getIsha() { return isha; }
    public String getSehri() { return sehri; }
    public String getIftar() { return iftar; }
}
