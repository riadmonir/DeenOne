package com.devflux.deenone.features.books.model;

import androidx.annotation.NonNull;

import com.devflux.deenone.data.local.entity.IslamicBookEntity;

/**
 * Pure Domain and API Data Model for Islamic Books.
 */
public class IslamicBookModel {

    private String id;
    private String title;
    private String author;
    private String description;
    private String language;
    private String category;
    private String publisher;
    private String coverUrl;
    private String bookUrl;
    private String fileUrl;
    private String format;
    private String source;
    private String sourceUrl;
    private String license;
    private String publishedDate;
    private String fileSize;
    private int totalPages;
    private long createdAt;
    private long updatedAt;

    public IslamicBookModel() {}

    public IslamicBookModel(String id, String title, String author, String description,
                            String language, String category, String publisher, String coverUrl,
                            String bookUrl, String fileUrl, String format, String source,
                            String sourceUrl, String license, String publishedDate,
                            String fileSize, int totalPages, long createdAt, long updatedAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
        this.language = language;
        this.category = category;
        this.publisher = publisher;
        this.coverUrl = coverUrl;
        this.bookUrl = bookUrl;
        this.fileUrl = fileUrl;
        this.format = format;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.license = license;
        this.publishedDate = publishedDate;
        this.fileSize = fileSize;
        this.totalPages = totalPages;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Converts from Room Entity to Domain Model.
     */
    public static IslamicBookModel fromEntity(@NonNull IslamicBookEntity entity) {
        return new IslamicBookModel(
                entity.getId(),
                entity.getTitle(),
                entity.getAuthor(),
                entity.getDescription(),
                entity.getLanguage(),
                entity.getCategory(),
                entity.getPublisher(),
                entity.getCoverImageUrl(),
                entity.getDownloadUrl(),
                entity.getDownloadUrl(),
                entity.getFormat(),
                entity.getVerifiedSource(),
                "https://islamhouse.com",
                entity.getLicenseInfo(),
                "হিজরি সংকলন",
                entity.getFileSize(),
                entity.getTotalPages(),
                entity.getAddedTimestamp(),
                entity.getLastOpenedTimestamp()
        );
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
