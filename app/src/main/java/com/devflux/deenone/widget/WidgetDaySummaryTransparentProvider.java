package com.devflux.deenone.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.devflux.deenone.R;

public class WidgetDaySummaryTransparentProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (context == null || appWidgetManager == null) return;

        WidgetDataHelper.WidgetPrayerData data = WidgetDataHelper.getWidgetData(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_day_summary_transparent);

        views.setTextViewText(R.id.tvWidgetHijriDate, data.hijriDateShort);
        views.setTextViewText(R.id.tvWidgetGregorianDate, data.gregorianDateBengali);
        views.setTextViewText(R.id.tvWidgetCurrentWaqt, data.currentWaqtName);
        views.setTextViewText(R.id.tvWidgetWaqtRange, data.currentWaqtRange);

        views.setTextViewText(R.id.tvWidgetSunrise, "সূর্যোদয়: " + data.sunriseTimeStr);
        views.setTextViewText(R.id.tvWidgetSunset, "সূর্যাস্ত: " + data.sunsetTimeStr);
        views.setTextViewText(R.id.tvWidgetSahri, "সাহরি: " + data.sehriTimeStr);
        views.setTextViewText(R.id.tvWidgetIftar, "ইফতার: " + data.iftarTimeStr);

        views.setOnClickPendingIntent(R.id.layoutWidgetRoot, WidgetDataHelper.getOpenAppPendingIntent(context));
        views.setOnClickPendingIntent(R.id.btnWidgetRefresh, WidgetDataHelper.getRefreshPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context) {
        if (context == null) return;
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetDaySummaryTransparentProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId);
            }
        } catch (Exception ignored) {}
    }
}
