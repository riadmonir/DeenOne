package com.devflux.deenone.features.community.model;

import java.io.Serializable;

public class CommunityCommentItem implements Serializable {
    private String id;
    private String postId;
    private String authorName;
    private String content;
    private long createdAt;
    private String parentCommentId;
    private String replyToAuthorName;
    private String authorAvatar;

    public CommunityCommentItem() {}

    public CommunityCommentItem(String id, String postId, String authorName, String content, long createdAt) {
        this(id, postId, authorName, content, createdAt, null, null, "avatar_1");
    }

    public CommunityCommentItem(String id, String postId, String authorName, String content, long createdAt,
                                String parentCommentId, String replyToAuthorName) {
        this(id, postId, authorName, content, createdAt, parentCommentId, replyToAuthorName, "avatar_1");
    }

    public CommunityCommentItem(String id, String postId, String authorName, String content, long createdAt,
                                String parentCommentId, String replyToAuthorName, String authorAvatar) {
        this.id = id;
        this.postId = postId;
        this.authorName = authorName;
        this.content = content;
        this.createdAt = createdAt;
        this.parentCommentId = parentCommentId;
        this.replyToAuthorName = replyToAuthorName;
        this.authorAvatar = authorAvatar != null ? authorAvatar : "avatar_1";
    }

    public String getId() { return id != null ? id : ""; }
    public void setId(String id) { this.id = id; }

    public String getPostId() { return postId != null ? postId : ""; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getAuthorName() { return authorName != null ? authorName : "মুসলিম ভাই"; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getContent() { return content != null ? content : ""; }
    public void setContent(String content) { this.content = content; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getParentCommentId() { return parentCommentId != null ? parentCommentId : ""; }
    public void setParentCommentId(String parentCommentId) { this.parentCommentId = parentCommentId; }

    public String getReplyToAuthorName() { return replyToAuthorName != null ? replyToAuthorName : ""; }
    public void setReplyToAuthorName(String replyToAuthorName) { this.replyToAuthorName = replyToAuthorName; }

    public String getAuthorAvatar() { return authorAvatar != null ? authorAvatar : "avatar_1"; }
    public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }

    public boolean isReply() {
        return parentCommentId != null && !parentCommentId.isEmpty();
    }

    public String getTimeAgoBengali() {
        return getTimeAgo(true);
    }

    public String getTimeAgo(boolean isBn) {
        long diffMs = System.currentTimeMillis() - createdAt;
        long mins = diffMs / (60 * 1000);
        if (mins < 1) return isBn ? "এইমাত্র" : "Just now";
        if (mins < 60) return isBn ? (toBengaliNumber(mins) + " মিনিট আগে") : (mins + "m ago");
        long hours = mins / 60;
        if (hours < 24) return isBn ? (toBengaliNumber(hours) + " ঘণ্টা আগে") : (hours + "h ago");
        long days = hours / 24;
        return isBn ? (toBengaliNumber(days) + " দিন আগে") : (days + "d ago");
    }

    private String toBengaliNumber(long number) {
        String numStr = String.valueOf(number);
        char[] bnDigits = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};
        StringBuilder sb = new StringBuilder();
        for (char c : numStr.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(bnDigits[c - '0']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}