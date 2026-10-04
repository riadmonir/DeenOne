package com.devflux.deenone.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "islamic_books")
public class IslamicBookEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String author;
    private String category; // Quran, Tafsir, Hadith, Fiqh, Aqeedah, Seerah, History, Dua, Salah, Ethics, Family, Children, Bengali, English
    private String language; // bn, en, ar
    private int totalPages;
    private String fileSize; // e.g. "4.5 MB"
    private String description;
    private String downloadUrl; // Real direct PDF URL
    private String localFilePath; // Path on device once downloaded
    private boolean isDownloaded;
    private int downloadProgress; // 0 - 100
    private int lastReadPage; // Last page read by user (for auto-resume)
    private int readingPercentage; // 0 - 100%
    private String readingStatus; // NOT_STARTED, IN_PROGRESS, COMPLETED
    private long lastOpenedTimestamp; // For sorting continue reading
    private boolean isFavorite;
    private String coverImageUrl; // Online or local cover asset
    private String publisher;
    private String verifiedSource; // e.g. "IslamHouse / Islamic Foundation / Darussalam"
    private String licenseInfo; // e.g. "Public Domain / Free Islamic Distribution"
    private String format; // "PDF"
    private long addedTimestamp;

    public IslamicBookEntity(@NonNull String id, String title, String author, String category,
                             String language, int totalPages, String fileSize, String description,
                             String downloadUrl, String localFilePath, boolean isDownloaded,
                             int downloadProgress, int lastReadPage, int readingPercentage,
                             String readingStatus, long lastOpenedTimestamp, boolean isFavorite,
                             String coverImageUrl, String publisher, String verifiedSource,
                             String licenseInfo, String format, long addedTimestamp) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.language = language;
        this.totalPages = totalPages;
        this.fileSize = fileSize;
        this.description = description;
        this.downloadUrl = downloadUrl;
        this.localFilePath = localFilePath;
        this.isDownloaded = isDownloaded;
        this.downloadProgress = downloadProgress;
        this.lastReadPage = lastReadPage;
        this.readingPercentage = readingPercentage;
        this.readingStatus = readingStatus != null ? readingStatus : "NOT_STARTED";
        this.lastOpenedTimestamp = lastOpenedTimestamp;
        this.isFavorite = isFavorite;
        this.coverImageUrl = coverImageUrl;
        this.publisher = publisher;
        this.verifiedSource = verifiedSource;
        this.licenseInfo = licenseInfo != null ? licenseInfo : "পাবলিক ডোমেইন / উন্মুক্ত ইসলামিক প্রকাশনা";
        this.format = format != null ? format : "PDF";
        this.addedTimestamp = addedTimestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public int getLastReadPage() {
        return lastReadPage;
    }

    public void setLastReadPage(int lastReadPage) {
        this.lastReadPage = lastReadPage;
    }

    public int getReadingPercentage() {
        return readingPercentage;
    }

    public void setReadingPercentage(int readingPercentage) {
        this.readingPercentage = readingPercentage;
    }

    public String getReadingStatus() {
        return readingStatus;
    }

    public void setReadingStatus(String readingStatus) {
        this.readingStatus = readingStatus;
    }

    public long getLastOpenedTimestamp() {
        return lastOpenedTimestamp;
    }

    public void setLastOpenedTimestamp(long lastOpenedTimestamp) {
        this.lastOpenedTimestamp = lastOpenedTimestamp;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVerifiedSource() {
        return verifiedSource;
    }

    public void setVerifiedSource(String verifiedSource) {
        this.verifiedSource = verifiedSource;
    }

    public String getLicenseInfo() {
        return licenseInfo;
    }

    public void setLicenseInfo(String licenseInfo) {
        this.licenseInfo = licenseInfo;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public long getAddedTimestamp() {
        return addedTimestamp;
    }

    public void setAddedTimestamp(long addedTimestamp) {
        this.addedTimestamp = addedTimestamp;
    }
}
