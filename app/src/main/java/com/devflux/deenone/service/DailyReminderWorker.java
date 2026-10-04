package com.devflux.deenone.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.devflux.deenone.data.repository.PrayerRepository;

public class DailyReminderWorker extends Worker {

    private static final String TAG = "DailyReminderWorker";

    public DailyReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "Executing Daily Reminder and Prayer Sync Work");
        try {
            PrayerRepository repository = new PrayerRepository(getApplicationContext());
            repository.syncPrayerTimes("Madinah", "Saudi Arabia");
            return Result.success();
        } catch (Exception e) {
            Log.e(TAG, "Error in DailyReminderWorker: " + e.getMessage());
            return Result.retry();
        }
    }
}
