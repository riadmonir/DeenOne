package com.devflux.deenone.features.books.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

/**
 * Manages background scheduling of Islamic Book metadata synchronization.
 */
public class BookSyncScheduler {

    private static final String UNIQUE_PERIODIC_WORK_NAME = "deenone_book_catalog_periodic_sync";
    private static final String UNIQUE_ONETIME_WORK_NAME = "deenone_book_catalog_instant_sync";

    /**
     * Schedules periodic background sync (every 24 hours when connected to network).
     */
    public static void schedulePeriodicSync(@NonNull Context context) {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build();

            PeriodicWorkRequest syncRequest = new PeriodicWorkRequest.Builder(
                    BookSyncWorker.class,
                    24, TimeUnit.HOURS,
                    6, TimeUnit.HOURS // Flex interval
            )
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(context.getApplicationContext()).enqueueUniquePeriodicWork(
                    UNIQUE_PERIODIC_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    syncRequest
            );
        } catch (Exception e) {
            android.util.Log.w("BookSyncScheduler", "Failed to schedule periodic book sync: " + e.getMessage());
        }
    }

    /**
     * Triggers an immediate one-time metadata synchronization request in the background.
     */
    public static void triggerImmediateSync(@NonNull Context context) {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest oneTimeRequest = new OneTimeWorkRequest.Builder(BookSyncWorker.class)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(context.getApplicationContext()).enqueueUniqueWork(
                    UNIQUE_ONETIME_WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    oneTimeRequest
            );
        } catch (Exception e) {
            android.util.Log.w("BookSyncScheduler", "Failed to trigger immediate book sync: " + e.getMessage());
        }
    }
}
