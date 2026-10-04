package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dua_logs")
public class DuaLogEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long duaId;
    private String duaTitle;
    private String category;
    private long readTimestamp;
    private String dateString; // YYYY-MM-DD

    public DuaLogEntity() {
    }

    @Ignore
    public DuaLogEntity(long duaId, String duaTitle, String category, long readTimestamp, String dateString) {
        this.duaId = duaId;
        this.duaTitle = duaTitle;
        this.category = category;
        this.readTimestamp = readTimestamp;
        this.dateString = dateString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDuaId() {
        return duaId;
    }

    public void setDuaId(long duaId) {
        this.duaId = duaId;
    }

    public String getDuaTitle() {
        return duaTitle;
    }

    public void setDuaTitle(String duaTitle) {
        this.duaTitle = duaTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getReadTimestamp() {
        return readTimestamp;
    }

    public void setReadTimestamp(long readTimestamp) {
        this.readTimestamp = readTimestamp;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
