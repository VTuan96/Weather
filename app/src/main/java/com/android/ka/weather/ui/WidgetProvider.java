package com.android.ka.weather.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.ka.weather.R;
import com.android.ka.weather.common.Logger;
import com.android.ka.weather.common.TempUtils;
import com.android.ka.weather.common.WeatherUtils;
import com.android.ka.weather.model.WeatherDisplay;

/**
 * Created by ka on 01/12/2016.
 */

public class WidgetProvider extends AppWidgetProvider {
    public static final String UPDATE_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";
    private String weatherId;
    private String city;
    private String tmp;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Logger.getLogger(getClass()).info("Get update");
        if (intent != null && intent.getAction().equals(UPDATE_WIDGET)) {
            WeatherDisplay weatherDisplay = intent.getParcelableExtra("main");
            if (weatherDisplay != null) {
                Logger.getLogger(getClass()).info("!=null");
                city = weatherDisplay.getCity();
                weatherId = weatherDisplay.getWeatherId() + "";
                tmp = weatherDisplay.getTemp();
                int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context,
                        WidgetProvider.class));
                onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int currentWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("_id", weatherId);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            PendingIntent pending = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            if (city != null && tmp != null) {
                remoteViews.setTextViewText(R.id.tvCity, city);
                remoteViews.setTextViewText(R.id.tvTemp, tmp);
                remoteViews.setImageViewResource(R.id.imgStatus, WeatherUtils.getArtResourceForWeatherCondition
                        (Integer.parseInt(weatherId)));
            } else {
                remoteViews.setTextViewText(R.id.tvCity, "Ha Noi");
                remoteViews.setTextViewText(R.id.tvTemp, TempUtils.convertKtoC(216));
            }
            remoteViews.setOnClickPendingIntent(R.id.widget, pending);
            appWidgetManager.updateAppWidget(currentWidgetId, remoteViews);

        }
    }
}
