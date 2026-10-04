package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notification_history")
public class NotificationMessageEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String message;
    private String type; // "adhan", "prayer", "reminder", "daily_dua", "daily_hadith", "daily_amal", "quiz", "admin_announcement", "critical_update", "islamic_calendar"
    private long timestamp;
    private boolean isRead;
    private String deepLinkAction;
    private String importance; // "normal", "high", "critical"
    private String source; // "system", "server_admin", "scheduled"

    public NotificationMessageEntity(String title, String message, String type, long timestamp,
                                     boolean isRead, String deepLinkAction, String importance, String source) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.deepLinkAction = deepLinkAction;
        this.importance = importance;
        this.source = source;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }

    public String getDeepLinkAction() { return deepLinkAction; }
    public void setDeepLinkAction(String deepLinkAction) { this.deepLinkAction = deepLinkAction; }

    public String getImportance() { return importance; }
    public void setImportance(String importance) { this.importance = importance; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
