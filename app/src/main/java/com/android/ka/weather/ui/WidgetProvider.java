package com.android.ka.weather.ui;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.android.ka.weather.R;

/**
 * Created by ka on 01/12/2016.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int currentWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.tvCity, "Ha Noi");
            remoteViews.setTextViewText(R.id.tvTemp, "19 C");
            appWidgetManager.updateAppWidget(currentWidgetId, remoteViews);
        }
    }
}
