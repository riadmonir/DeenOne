package com.devflux.deenone.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.devflux.deenone.R;

public class WidgetSalatCountdownScheduleProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (context == null || appWidgetManager == null) return;

        WidgetDataHelper.WidgetPrayerData data = WidgetDataHelper.getWidgetData(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_salat_countdown_schedule);

        views.setTextViewText(R.id.tvWidgetTimeSmall, data.updatedTimeStr);
        views.setTextViewText(R.id.tvWidgetCountdownTitle, data.currentWaqtName + " ওয়াক্ত শেষ হতে বাকি");
        views.setTextViewText(R.id.tvWidgetBigCountdown, data.currentWaqtCountdown);

        views.setTextViewText(R.id.tvWaqtFajr, data.fajrTimeStr.replace(" AM", "").replace(" PM", ""));
        views.setTextViewText(R.id.tvWaqtDhuhr, data.dhuhrTimeStr.replace(" AM", "").replace(" PM", ""));
        views.setTextViewText(R.id.tvWaqtAsr, data.asrTimeStr.replace(" AM", "").replace(" PM", ""));
        views.setTextViewText(R.id.tvWaqtMaghrib, data.maghribTimeStr.replace(" AM", "").replace(" PM", ""));
        views.setTextViewText(R.id.tvWaqtIsha, data.ishaTimeStr.replace(" AM", "").replace(" PM", ""));

        // Highlight active prayer pill
        views.setInt(R.id.pillFajr, "setBackgroundResource", "fajr".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_amber : 0);
        views.setInt(R.id.pillDhuhr, "setBackgroundResource", "dhuhr".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_amber : 0);
        views.setInt(R.id.pillAsr, "setBackgroundResource", "asr".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_amber : 0);
        views.setInt(R.id.pillMaghrib, "setBackgroundResource", "maghrib".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_amber : 0);
        views.setInt(R.id.pillIsha, "setBackgroundResource", "isha".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_amber : 0);

        views.setOnClickPendingIntent(R.id.layoutWidgetRoot, WidgetDataHelper.getOpenAppPendingIntent(context));
        views.setOnClickPendingIntent(R.id.btnWidgetRefresh, WidgetDataHelper.getRefreshPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context) {
        if (context == null) return;
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetSalatCountdownScheduleProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId);
            }
        } catch (Exception ignored) {}
    }
}
