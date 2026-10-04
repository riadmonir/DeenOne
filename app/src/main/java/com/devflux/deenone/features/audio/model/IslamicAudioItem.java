package com.devflux.deenone.features.audio.model;

import java.io.Serializable;

/**
 * Represents a single audio item in the Islamic Audio Hub.
 * Features verified, source-based Islamic content across the globe:
 *  - Bangla Waz & Lectures (বাংলা ওয়াজ ও ইসলামিক লেকচার)
 *  - International Scholars (মুফতি মেঙ্ক, নোমান আলী খান, ড. ওমর সুলাইমান ইত্যাদি)
 *  - Hadith Lessons & Explanations (সহীহ বুখারী, মুসলিম, রিয়াযুস সালেহীন)
 *  - Friday Khutbah (জুমার খুতবা)
 *  - Seerah & Islamic History (সীরাতুন্নবী ও ইসলামের ইতিহাস)
 *  - Adhkar & Halal Nasheed (জিকির ও ইসলামিক নাশিদ)
 *
 * (Note: Quran Surahs are strictly excluded as they reside in the dedicated Quran section).
 */
public class IslamicAudioItem implements Serializable {

  private String id;
  private String title;
  private String speaker;    // Scholar / Speaker / Qari / Khatib
  private String category;   // bangla_waz | international | hadith | khutbah | seerah | dhikr | downloaded
  private String language;   // bn | en | ar | ur
  private String description;
  private String publisher;
  private String reference;   // Reference or topic e.g. "সহীহ বুখারী - কিতাবুল ঈমান"
  private String publishedDate;

  // Source & legal
  private String source;    // "Archive CDN", "IslamHouse", "MuslimCentral", "Islamway"
  private String sourceUrl;   // Canonical link
  private String license;    // "Free to Listen / Public Domain"

  // Media & Fast CDN streaming
  private String streamUrl;   // Direct high-speed CDN stream URL (HTTPS)
  private String downloadUrl;  // Download link
  private String coverUrl;   // Thumbnail
  private String format;    // mp3
  private long durationMs;   // Milliseconds
  private long fileSizeBytes;

  // Offline download support
  private String localFilePath; // If downloaded locally
  private boolean isDownloaded;
  private boolean isDownloading;
  private int downloadProgress; // 0-100

  // Playback state (transient)
  private transient long lastPositionMs;
  private transient boolean isPlaying;

  public IslamicAudioItem() {}

  public IslamicAudioItem(String id, String title, String speaker, String category,
              String language, String streamUrl, String coverUrl,
              String source, String license, long durationMs) {
    this.id = id;
    this.title = title;
    this.speaker = speaker;
    this.category = category;
    this.language = language;
    this.streamUrl = streamUrl;
    this.coverUrl = coverUrl;
    this.source = source;
    this.license = license;
    this.durationMs = durationMs;
    this.format = "mp3";
  }

  // Getters & Setters
  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getTitle() { return title != null ? title : ""; }
  public void setTitle(String title) { this.title = title; }

  public String getSpeaker() { return speaker != null ? speaker : ""; }
  public void setSpeaker(String speaker) { this.speaker = speaker; }

  public String getCategory() { return category != null ? category : ""; }
  public void setCategory(String category) { this.category = category; }

  public String getLanguage() { return language != null ? language : ""; }
  public void setLanguage(String language) { this.language = language; }

  public String getDescription() { return description != null ? description : ""; }
  public void setDescription(String description) { this.description = description; }

  public String getPublisher() { return publisher; }
  public void setPublisher(String publisher) { this.publisher = publisher; }

  public String getReference() { return reference != null ? reference : ""; }
  public void setReference(String reference) { this.reference = reference; }

  public String getPublishedDate() { return publishedDate; }
  public void setPublishedDate(String publishedDate) { this.publishedDate = publishedDate; }

  public String getSource() { return source != null ? source : ""; }
  public void setSource(String source) { this.source = source; }

  public String getSourceUrl() { return sourceUrl; }
  public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }

  public String getLicense() { return license != null ? license : "Free to listen"; }
  public void setLicense(String license) { this.license = license; }

  public String getStreamUrl() { return streamUrl; }
  public void setStreamUrl(String streamUrl) { this.streamUrl = streamUrl; }

  public String getDownloadUrl() { return downloadUrl != null ? downloadUrl : streamUrl; }
  public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

  public String getCoverUrl() { return coverUrl; }
  public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

  public String getFormat() { return format != null ? format : "mp3"; }
  public void setFormat(String format) { this.format = format; }

  public long getDurationMs() { return durationMs; }
  public void setDurationMs(long durationMs) { this.durationMs = durationMs; }

  public long getFileSizeBytes() { return fileSizeBytes; }
  public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

  public String getLocalFilePath() { return localFilePath; }
  public void setLocalFilePath(String localFilePath) { this.localFilePath = localFilePath; }

  public boolean isDownloaded() { return isDownloaded; }
  public void setDownloaded(boolean downloaded) { isDownloaded = downloaded; }

  public boolean isDownloading() { return isDownloading; }
  public void setDownloading(boolean downloading) { isDownloading = downloading; }

  public int getDownloadProgress() { return downloadProgress; }
  public void setDownloadProgress(int downloadProgress) { this.downloadProgress = downloadProgress; }

  public long getLastPositionMs() { return lastPositionMs; }
  public void setLastPositionMs(long lastPositionMs) { this.lastPositionMs = lastPositionMs; }

  public boolean isPlaying() { return isPlaying; }
  public void setPlaying(boolean playing) { isPlaying = playing; }

  /** Returns effective playback URL (local file if downloaded, else CDN stream) */
  public String getEffectivePlayUrl() {
    if (isDownloaded && localFilePath != null && !localFilePath.isEmpty()) {
      return localFilePath;
    }
    return streamUrl;
  }

  public String getFormattedDuration() {
    if (durationMs <= 0) return "--:--";
    long totalSeconds = durationMs / 1000;
    long minutes = totalSeconds / 60;
    long seconds = totalSeconds % 60;
    if (minutes >= 60) {
      long hours = minutes / 60;
      minutes = minutes % 60;
      return String.format(java.util.Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
    }
    return String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds);
  }

  public String getCategoryLabel() {
    if (category == null) return "ইসলামিক অডিও";
    switch (category) {
      case "bangla_waz":
      case "lecture":
        return "বাংলা ওয়াজ ও লেকচার";
      case "international":
        return "আন্তর্জাতিক স্কলার";
      case "hadith":
        return "হাদিস লেসন";
      case "khutbah":
        return "জুমার খুতবা";
      case "seerah":
        return "সীরাত ও ইতিহাস";
      case "dhikr":
      case "nasheed":
        return "জিকির ও নাশিদ";
      case "downloaded":
        return "অফলাইন ডাউনলোড";
      default:
        return "ইসলামিক অডিও";
    }
  }

  public String getCategoryName() {
    return getCategoryLabel();
  }

  public String getCategoryEmoji() {
    if (category == null) return "";
    switch (category) {
      case "bangla_waz": return "";
      case "lecture": return "";
      case "tafsir": return "";
      case "hadith": return "";
      case "seerah": return "";
      case "nasheed": return "";
      case "ruqyah": return "";
      case "ramadan": return "";
      case "hajj": return "";
      case "salah": return "";
      default: return "";
    }
  }

  public int getDurationSeconds() {
    return (int) (durationMs / 1000);
  }

  public String getAudioUrl() {
    return getEffectivePlayUrl();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof IslamicAudioItem)) return false;
    IslamicAudioItem item = (IslamicAudioItem) o;
    return id != null && id.equals(item.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}