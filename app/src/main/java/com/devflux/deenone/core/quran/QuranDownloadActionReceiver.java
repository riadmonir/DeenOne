package com.devflux.deenone.core.quran;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * QuranDownloadActionReceiver — Handles Pause, Resume, and Cancel actions from
 * the background download notification bar.
 */
public class QuranDownloadActionReceiver extends BroadcastReceiver {

    private static final String TAG = "QuranDownloadReceiver";

    public static final String ACTION_PAUSE = "com.devflux.deenone.ACTION_QURAN_DOWNLOAD_PAUSE";
    public static final String ACTION_RESUME = "com.devflux.deenone.ACTION_QURAN_DOWNLOAD_RESUME";
    public static final String ACTION_CANCEL = "com.devflux.deenone.ACTION_QURAN_DOWNLOAD_CANCEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || intent.getAction() == null) return;
        String action = intent.getAction();
        Log.d(TAG, "Quran download action received: " + action);

        switch (action) {
            case ACTION_PAUSE:
                QuranUnifiedDownloadManager.getInstance().pauseDownload(context);
                break;
            case ACTION_RESUME:
                QuranUnifiedDownloadManager.getInstance().resumeDownload(context);
                break;
            case ACTION_CANCEL:
                QuranUnifiedDownloadManager.getInstance().cancelDownload(context);
                break;
        }
    }
}
