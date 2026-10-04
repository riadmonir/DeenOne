package com.devflux.deenone.core.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.devflux.deenone.data.repository.NotificationRepository;

public class TimezoneChangedReceiver extends BroadcastReceiver {

    private static final String TAG = "TimezoneChangedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        String action = intent.getAction();
        Log.d(TAG, "Received system broadcast action: " + action);

        if (Intent.ACTION_TIMEZONE_CHANGED.equals(action) ||
            Intent.ACTION_TIME_CHANGED.equals(action) ||
            "android.intent.action.TIME_SET".equals(action) ||
            Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action) ||
            "com.htc.intent.action.QUICKBOOT_POWERON".equals(action)) {

            Log.d(TAG, "Recalculating and automatically rescheduling all daily prayer alarms.");
            try {
                AlarmRescheduler.rescheduleAll(context);
                new NotificationRepository(context).performMidnightCleanup();
                Log.d(TAG, "Successfully rescheduled all alarms after " + action);
            } catch (Exception e) {
                Log.e(TAG, "Error rescheduling alarms on " + action + ": " + e.getMessage());
            }
        }
    }
}
