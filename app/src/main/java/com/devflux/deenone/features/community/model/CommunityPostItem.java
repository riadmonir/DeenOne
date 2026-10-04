package com.devflux.deenone.features.community.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommunityPostItem implements Serializable {
  private String id;
  private String authorName;
  private String authorLocation;
  private String category; // all, dua_request, islamic_advice, qa, announcement
  private String title;
  private String content;
  private int ameenCount;
  private int commentCount;
  private int viewCount;
  private boolean isAmeenGiven;
  private boolean isVerified;
  private long createdAt;
  private String userId;
  private String authorAvatar;
  private List<CommunityCommentItem> comments = new ArrayList<>();

  public CommunityPostItem() {}

  public CommunityPostItem(String id, String authorName, String authorLocation, String category,
               String title, String content, int ameenCount, int commentCount, int viewCount,
               boolean isVerified, long createdAt) {
    this.id = id;
    this.authorName = authorName;
    this.authorLocation = authorLocation;
    this.category = category;
    this.title = title;
    this.content = content;
    this.ameenCount = ameenCount;
    this.commentCount = commentCount;
    this.viewCount = viewCount;
    this.isVerified = isVerified;
    this.createdAt = createdAt;
  }

  public String getAuthorAvatar() { return authorAvatar != null ? authorAvatar : ""; }
  public void setAuthorAvatar(String authorAvatar) { this.authorAvatar = authorAvatar; }

  public String getUserId() { return userId != null ? userId : ""; }
  public void setUserId(String userId) { this.userId = userId; }

  public String getId() { return id != null ? id : ""; }
  public void setId(String id) { this.id = id; }

  public String getAuthorName() { return authorName != null ? authorName : "মুসলিম ভাই"; }
  public void setAuthorName(String authorName) { this.authorName = authorName; }

  public String getAuthorLocation() { return authorLocation != null ? authorLocation : "বাংলাদেশ"; }
  public void setAuthorLocation(String authorLocation) { this.authorLocation = authorLocation; }

  public String getCategory() { return category != null ? category : "all"; }
  public void setCategory(String category) { this.category = category; }

  public String getTitle() { return title != null ? title : ""; }
  public void setTitle(String title) { this.title = title; }

  public String getContent() { return content != null ? content : ""; }
  public void setContent(String content) { this.content = content; }

  public int getAmeenCount() { return ameenCount; }
  public void setAmeenCount(int ameenCount) { this.ameenCount = ameenCount; }

  public int getCommentCount() { return commentCount; }
  public void setCommentCount(int commentCount) { this.commentCount = commentCount; }

  public int getViewCount() { return viewCount; }
  public void setViewCount(int viewCount) { this.viewCount = viewCount; }

  public boolean isAmeenGiven() { return isAmeenGiven; }
  public void setAmeenGiven(boolean ameenGiven) { isAmeenGiven = ameenGiven; }

  public boolean isVerified() { return isVerified; }
  public void setVerified(boolean verified) { isVerified = verified; }

  public long getCreatedAt() { return createdAt; }
  public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

  public List<CommunityCommentItem> getComments() { return comments; }
  public void setComments(List<CommunityCommentItem> comments) { this.comments = comments; }

  public String getAuthorInitial() {
    if (authorName == null || authorName.trim().isEmpty()) return "ম";
    return String.valueOf(authorName.trim().charAt(0));
  }

  public String getCategoryBadgeText() {
    return getCategoryBadgeText(true);
  }

  public String getCategoryBadgeText(boolean isBn) {
    if ("dua_request".equalsIgnoreCase(category)) return isBn ? "দোয়া" : "Dua";
    if ("islamic_advice".equalsIgnoreCase(category)) return isBn ? "নসীহত" : "Advice";
    if ("qa".equalsIgnoreCase(category)) return isBn ? "জিজ্ঞাসা" : "Q&A";
    if ("announcement".equalsIgnoreCase(category)) return isBn ? "ঘোষণা" : "Notice";
    return isBn ? "আলোচনা" : "General";
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

  public String getViewCountBengali() {
    return toBengaliNumber(viewCount > 0 ? viewCount : 12);
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