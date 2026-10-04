package com.devflux.deenone;

import android.app.Application;

import com.devflux.deenone.core.notifications.NotificationHelper;
import com.devflux.deenone.core.theme.ThemeManager;

public class DeenOneApp extends Application {

    @Override
    protected void attachBaseContext(android.content.Context base) {
        super.attachBaseContext(com.devflux.deenone.core.localization.LocaleManager.wrapContext(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            com.devflux.deenone.core.localization.LocaleManager.applyLocale(this, com.devflux.deenone.core.localization.LocaleManager.getSavedLanguage(this));
        } catch (Exception ignored) {}
        // 1. Install Global Crash Protection Shield
        try {
            com.devflux.deenone.core.error.AppErrorHandler.installGlobalCrashProtection(this);
        } catch (Exception ignored) {}

        // 2. Apply Persistent Theme (Dark, Light, or System Default)
        try {
            ThemeManager.applySavedTheme(this);
        } catch (Exception ignored) {}

        // 3. Initialize Notification Channels
        try {
            NotificationHelper.createNotificationChannels(this);
        } catch (Exception ignored) {}

        // 4. Schedule Daily Reminders, Prayer Times Alarms & Dhikr/Salawat Reminders
        try {
            java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    com.devflux.deenone.core.alarms.AlarmRescheduler.rescheduleAll(DeenOneApp.this);
                    com.devflux.deenone.core.notifications.DailyIslamicReminderScheduler.scheduleAllDailyReminders(DeenOneApp.this);
                    com.devflux.deenone.core.notifications.DurudReminderScheduler.scheduleAll(DeenOneApp.this);
                    com.devflux.deenone.core.notifications.IstigfarReminderScheduler.scheduleAll(DeenOneApp.this);
                } catch (Exception ignored) {}
            });
        } catch (Exception ignored) {}

        // 5. Initialize AdManager with Application context
        try {
            com.devflux.deenone.core.ads.AdManager.getInstance().initialize(this);
        } catch (Exception ignored) {}

        // 6. Initialize IslamicQuizScraperEngine with Application Context for Asset Access
        try {
            com.devflux.deenone.core.ai.IslamicQuizScraperEngine.setApplicationContext(this);
        } catch (Exception ignored) {}

        // 7. Warm up QuizManager master pool in background executor for zero-lag 60 FPS experience
        try {
            com.devflux.deenone.core.quiz.QuizManager.getInstance().ensureMasterPoolLoaded(this);
        } catch (Exception ignored) {}

        // 8. Pre-cache Donation / Khedmat data and HTML in background immediately on app launch
        try {
            com.devflux.deenone.features.donation.DonationCacheManager.preloadDonationData(this);
        } catch (Exception ignored) {}
    }
}
