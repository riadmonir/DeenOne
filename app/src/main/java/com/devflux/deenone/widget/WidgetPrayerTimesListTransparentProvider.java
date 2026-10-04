package com.devflux.deenone.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.devflux.deenone.R;

public class WidgetPrayerTimesListTransparentProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (context == null || appWidgetManager == null) return;

        WidgetDataHelper.WidgetPrayerData data = WidgetDataHelper.getWidgetData(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_prayer_times_list_transparent);

        views.setTextViewText(R.id.tvWaqtFajrRange, data.fajrRangeStr);
        views.setTextViewText(R.id.tvWaqtDhuhrRange, data.dhuhrRangeStr);
        views.setTextViewText(R.id.tvWaqtAsrRange, data.asrRangeStr);
        views.setTextViewText(R.id.tvWaqtMaghribRange, data.maghribRangeStr);
        views.setTextViewText(R.id.tvWaqtIshaRange, data.ishaRangeStr);

        // Highlight active prayer row
        views.setInt(R.id.rowFajr, "setBackgroundResource", "fajr".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_mint : 0);
        views.setInt(R.id.rowDhuhr, "setBackgroundResource", "dhuhr".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_mint : 0);
        views.setInt(R.id.rowAsr, "setBackgroundResource", "asr".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_mint : 0);
        views.setInt(R.id.rowMaghrib, "setBackgroundResource", "maghrib".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_mint : 0);
        views.setInt(R.id.rowIsha, "setBackgroundResource", "isha".equals(data.currentWaqtKey) ? R.drawable.bg_widget_pill_mint : 0);

        views.setOnClickPendingIntent(R.id.layoutWidgetRoot, WidgetDataHelper.getOpenAppPendingIntent(context));
        views.setOnClickPendingIntent(R.id.btnWidgetRefresh, WidgetDataHelper.getRefreshPendingIntent(context));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context) {
        if (context == null) return;
        try {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, WidgetPrayerTimesListTransparentProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            for (int widgetId : allWidgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId);
            }
        } catch (Exception ignored) {}
    }
}
