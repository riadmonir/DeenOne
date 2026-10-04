package com.devflux.deenone.features.quran;

import android.app.Activity;

/**
 * QuranSleepAudioDownloadDialog — Backward-compatible wrapper that delegates to
 * {@link QuranUnifiedDownloadDialog} ensuring a single, unified Quran download system.
 */
public class QuranSleepAudioDownloadDialog {

    public static void showIfNeeded(Activity activity, Runnable onCompleteOffline, Runnable onPlayOnline) {
        QuranUnifiedDownloadDialog.showIfNeeded(activity, onCompleteOffline, onPlayOnline);
    }

    public static void show(Activity activity, Runnable onCompleteOffline) {
        QuranUnifiedDownloadDialog.show(activity, onCompleteOffline);
    }
}
