package com.devflux.deenone.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.devflux.deenone.R;

public class WidgetDailyAyahProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (context == null || appWidgetManager == null) return;

        WidgetDataHelper.WidgetPrayerData data = WidgetDataHelper.getWidgetData(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_daily_ayah);

        views.setTextViewText(R.id.tvWidgetAyahArabic, data.dailyAyahArabic);
        views.setTextViewText(R.id.tvWidgetAyahBengali, data.dailyAyahBengali);
        views.setTextViewText(R.id.tvWidgetAyahReference, data.dailyAyahReference);

        views.setOnClickPendingIntent(R.id.layoutWidgetRoot, WidgetDataHelper.getOpenAppPendingIntent(context));
        views.setOnClickPendingIntent(R.id.btnWidgetRefresh, WidgetDataHelper.getNextAyahHadithPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context) {
        if (context == null) return;
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetDailyAyahProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId);
            }
        } catch (Exception ignored) {}
    }
}
