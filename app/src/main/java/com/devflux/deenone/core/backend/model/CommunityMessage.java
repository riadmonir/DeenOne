package com.devflux.deenone.core.backend.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CommunityMessage implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("sender_name")
    private String senderName;

    @SerializedName("sender_id")
    private String senderId;

    @SerializedName("category")
    private String category;

    @SerializedName("content")
    private String content;

    @SerializedName("timestamp")
    private long timestamp;

    @SerializedName("likes_count")
    private int likesCount;

    public CommunityMessage() {}

    public CommunityMessage(String id, String senderName, String senderId, String category,
                            String content, long timestamp, int likesCount) {
        this.id = id;
        this.senderName = senderName;
        this.senderId = senderId;
        this.category = category;
        this.content = content;
        this.timestamp = timestamp;
        this.likesCount = likesCount;
    }

    public String getId() {
        return id;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getLikesCount() {
        return likesCount;
    }
}
