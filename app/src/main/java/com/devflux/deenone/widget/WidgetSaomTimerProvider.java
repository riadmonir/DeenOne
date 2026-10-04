package com.devflux.deenone.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.devflux.deenone.R;

public class WidgetSaomTimerProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (context == null || appWidgetManager == null) return;

        WidgetDataHelper.WidgetPrayerData data = WidgetDataHelper.getWidgetData(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_saom_timer);

        views.setTextViewText(R.id.tvWidgetIftarCountdown, data.iftarCountdownStr);
        views.setTextViewText(R.id.tvWidgetIftarEndTime, "ইফতার: " + data.iftarEndTimeStr);

        views.setTextViewText(R.id.tvWidgetSahriCountdown, data.sehriCountdownStr);
        views.setTextViewText(R.id.tvWidgetSahriStartTime, "শেষ সময়: " + data.sehriStartTimeStr);

        views.setOnClickPendingIntent(R.id.layoutWidgetRoot, WidgetDataHelper.getOpenAppPendingIntent(context));
        views.setOnClickPendingIntent(R.id.btnWidgetRefresh, WidgetDataHelper.getRefreshPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context) {
        if (context == null) return;
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetSaomTimerProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId);
            }
        } catch (Exception ignored) {}
    }
}
