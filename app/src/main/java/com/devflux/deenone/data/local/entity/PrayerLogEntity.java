package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "prayer_logs")
public class PrayerLogEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String date; // YYYY-MM-DD
    private String prayerName; // Fajr, Dhuhr, Asr, Maghrib, Isha, Tahajjud, Ishraq, Chasht, Awwabin
    private boolean isPrayed;
    private boolean isWithJamat;
    private boolean isQaza;
    private String status; // JAMAAT, EKAKI, DERI, QAZA, NAFL_ADAY, NONE
    private int points; // 20, 10, 5, 0, 15
    private long timestamp;

    public PrayerLogEntity(String date, String prayerName, boolean isPrayed, boolean isWithJamat, boolean isQaza, String status, int points, long timestamp) {
        this.date = date;
        this.prayerName = prayerName;
        this.isPrayed = isPrayed;
        this.isWithJamat = isWithJamat;
        this.isQaza = isQaza;
        this.status = status != null ? status : (isWithJamat ? "JAMAAT" : (isQaza ? "QAZA" : (isPrayed ? "EKAKI" : "NONE")));
        this.points = points;
        this.timestamp = timestamp;
    }

    @Ignore
    public PrayerLogEntity(String date, String prayerName, boolean isPrayed, boolean isWithJamat, boolean isQaza, long timestamp) {
        this(date, prayerName, isPrayed, isWithJamat, isQaza, isWithJamat ? "JAMAAT" : (isQaza ? "QAZA" : (isPrayed ? "EKAKI" : "NONE")), isWithJamat ? 20 : (isPrayed ? 10 : 0), timestamp);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPrayerName() { return prayerName; }
    public void setPrayerName(String prayerName) { this.prayerName = prayerName; }

    public boolean isPrayed() { return isPrayed; }
    public void setPrayed(boolean prayed) { isPrayed = prayed; }

    public boolean isWithJamat() { return isWithJamat; }
    public void setWithJamat(boolean withJamat) { isWithJamat = withJamat; }

    public boolean isQaza() { return isQaza; }
    public void setQaza(boolean qaza) { isQaza = qaza; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
