package com.devflux.deenone.features.books.model;

public class BookChapter {
    public final String title;
    public final String content;

    public BookChapter(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
