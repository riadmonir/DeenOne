package com.devflux.deenone.core.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DailyContentMidnightReceiver extends BroadcastReceiver {

    private static final String TAG = "DailyMidnightReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Midnight 24-hour rollover alarm received. Refreshing daily Ayah and Sahih Hadith.");
        try {
            DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(context);
        } catch (Exception e) {
            Log.e(TAG, "Error processing midnight daily content: " + e.getMessage());
        }
    }
}
