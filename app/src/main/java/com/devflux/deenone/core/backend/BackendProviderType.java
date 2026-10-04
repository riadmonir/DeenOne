package com.devflux.deenone.core.backend;

public enum BackendProviderType {
    PHP_MYSQL_REST("PHP & MySQL REST API", "কাস্টম সিপ্যানেল বা ভিপিএস হোস্টিং ভিত্তিক ব্যাকএন্ড"),
    FIREBASE("Google Firebase", "ফায়ারস্টোর, এফসিএম ও ক্লাউড ফাংশন ভিত্তিক ব্যাকএন্ড"),
    HYBRID_STANDALONE("হাইব্রিড / অফলাইন-ফার্স্ট", "লোকাল রুম ডাটাবেস ও স্বীকৃত এপিআই ক্লায়েন্ট");

    public final String title;
    public final String description;

    BackendProviderType(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
