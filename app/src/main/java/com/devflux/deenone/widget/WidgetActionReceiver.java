package com.devflux.deenone.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.devflux.deenone.core.content.DailyAyahHadithManager;

public class WidgetActionReceiver extends BroadcastReceiver {

    public static final String ACTION_WIDGET_REFRESH = "com.devflux.deenone.widget.ACTION_REFRESH";
    public static final String ACTION_TASBIH_COUNT = "com.devflux.deenone.widget.ACTION_TASBIH_COUNT";
    public static final String ACTION_TASBIH_RESET = "com.devflux.deenone.widget.ACTION_TASBIH_RESET";
    public static final String ACTION_NEXT_AYAH_HADITH = "com.devflux.deenone.widget.ACTION_NEXT_AYAH_HADITH";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) return;
        String action = intent.getAction();
        if (action == null) return;

        if (ACTION_WIDGET_REFRESH.equals(action)) {
            WidgetDataHelper.updateAllWidgets(context);
            Toast.makeText(context, "উইজেট আপডেট সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show();
        } else if (ACTION_TASBIH_COUNT.equals(action)) {
            SharedPreferences prefs = context.getSharedPreferences(WidgetDataHelper.PREFS_TASBIH, Context.MODE_PRIVATE);
            int count = prefs.getInt(WidgetDataHelper.KEY_TASBIH_COUNT, 0) + 1;
            prefs.edit().putInt(WidgetDataHelper.KEY_TASBIH_COUNT, count).apply();
            WidgetDigitalTasbihProvider.updateAllWidgets(context);
        } else if (ACTION_TASBIH_RESET.equals(action)) {
            SharedPreferences prefs = context.getSharedPreferences(WidgetDataHelper.PREFS_TASBIH, Context.MODE_PRIVATE);
            int idx = prefs.getInt(WidgetDataHelper.KEY_TASBIH_INDEX, 0);
            idx = (idx + 1) % WidgetDataHelper.TASBIH_ITEMS.length;
            prefs.edit()
                .putInt(WidgetDataHelper.KEY_TASBIH_COUNT, 0)
                .putInt(WidgetDataHelper.KEY_TASBIH_INDEX, idx)
                .apply();
            WidgetDigitalTasbihProvider.updateAllWidgets(context);
            Toast.makeText(context, "তাসবীহ রিসেট ও জিকির পরিবর্তিত হয়েছে", Toast.LENGTH_SHORT).show();
        } else if (ACTION_NEXT_AYAH_HADITH.equals(action)) {
            try {
                DailyAyahHadithManager.getInstance().getOrRefreshDailyContent(context);
            } catch (Exception ignored) {}
            WidgetDailyAyahProvider.updateAllWidgets(context);
            WidgetDailyHadithProvider.updateAllWidgets(context);
            Toast.makeText(context, "দৈনিক আয়াত ও হাদিস রিফ্রেশ করা হয়েছে", Toast.LENGTH_SHORT).show();
        }
    }
}
