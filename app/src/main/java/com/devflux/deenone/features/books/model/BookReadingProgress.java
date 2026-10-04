package com.devflux.deenone.features.books.model;

import androidx.annotation.NonNull;

/**
 * Domain model representing a user's reading progress on a specific Islamic book.
 */
public class BookReadingProgress {

    private String bookId;
    private int currentPage;
    private int totalPages;
    private int progress; // percentage (0 - 100)
    private long lastReadAt; // Unix timestamp in milliseconds
    private boolean isCompleted;

    public BookReadingProgress() {}

    public BookReadingProgress(@NonNull String bookId, int currentPage, int totalPages,
                               int progress, long lastReadAt, boolean isCompleted) {
        this.bookId = bookId;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.progress = progress;
        this.lastReadAt = lastReadAt;
        this.isCompleted = isCompleted;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getLastReadAt() {
        return lastReadAt;
    }

    public void setLastReadAt(long lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
