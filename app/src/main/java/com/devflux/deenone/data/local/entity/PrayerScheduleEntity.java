package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prayer_schedules")
public class PrayerScheduleEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String date; // YYYY-MM-DD
    private String location;
    private String fajr;
    private String sunrise;
    private String chasht;
    private String zohr;
    private String asr;
    private String maghrib;
    private String isha;
    private String sehri;
    private String iftar;

    public PrayerScheduleEntity(String date, String location, String fajr, String sunrise,
                                String chasht, String zohr, String asr, String maghrib,
                                String isha, String sehri, String iftar) {
        this.date = date;
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

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getFajr() { return fajr; }
    public void setFajr(String fajr) { this.fajr = fajr; }

    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }

    public String getChasht() { return chasht; }
    public void setChasht(String chasht) { this.chasht = chasht; }

    public String getZohr() { return zohr; }
    public void setZohr(String zohr) { this.zohr = zohr; }

    public String getAsr() { return asr; }
    public void setAsr(String asr) { this.asr = asr; }

    public String getMaghrib() { return maghrib; }
    public void setMaghrib(String maghrib) { this.maghrib = maghrib; }

    public String getIsha() { return isha; }
    public void setIsha(String isha) { this.isha = isha; }

    public String getSehri() { return sehri; }
    public void setSehri(String sehri) { this.sehri = sehri; }

    public String getIftar() { return iftar; }
    public void setIftar(String iftar) { this.iftar = iftar; }
}
