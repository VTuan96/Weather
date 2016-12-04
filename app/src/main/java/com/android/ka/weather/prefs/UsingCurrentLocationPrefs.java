package com.android.ka.weather.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.ka.weather.WeatherApplication;

/**
 * Created by ka on 04/12/2016.
 */

public class UsingCurrentLocationPrefs {
    private static final String CURRENT = "CURRENT";

    public static boolean getUsingCurrentLocation() {
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(CURRENT, Context.MODE_PRIVATE);
        return prefs.getBoolean("show", true);
    }

    public static void setUsingCurrentLocation(boolean isShow) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(CURRENT, Context.MODE_PRIVATE)
                .edit();
        editor.putBoolean("show", isShow);
        editor.apply();
    }
}
