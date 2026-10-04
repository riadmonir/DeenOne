package com.devflux.deenone.features.books.model;

import androidx.annotation.NonNull;

/**
 * Domain model representing a user's favorite book bookmark.
 */
public class BookFavorite {

    private String bookId;
    private long createdAt;

    public BookFavorite() {}

    public BookFavorite(@NonNull String bookId, long createdAt) {
        this.bookId = bookId;
        this.createdAt = createdAt;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
