package com.devflux.deenone.features.community.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.devflux.deenone.core.network.NetworkConnectivityHelper;

public class CommunitySyncWorker extends Worker {

    private static final String TAG = "CommunitySyncWorker";
    public static final String WORK_NAME = "community_offline_sync_work";

    public CommunitySyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void enqueueSyncWork(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest syncRequest = new OneTimeWorkRequest.Builder(CommunitySyncWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context.getApplicationContext())
                .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, syncRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        Log.d(TAG, "Starting background Community Sync Worker...");

        if (!NetworkConnectivityHelper.isOnline(context)) {
            Log.d(TAG, "Offline in worker. Retrying when network is available.");
            return Result.retry();
        }

        try {
            boolean success = CommunitySyncManager.getInstance(context).syncPendingActionsSynchronously();
            if (success) {
                Log.d(TAG, "Community background sync successfully completed.");
                return Result.success();
            } else {
                Log.w(TAG, "Some actions failed to sync. Scheduling retry.");
                return Result.retry();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during background community sync", e);
            return Result.retry();
        }
    }
}
