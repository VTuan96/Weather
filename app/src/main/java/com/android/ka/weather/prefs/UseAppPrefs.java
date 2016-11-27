package com.android.ka.weather.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.ka.weather.WeatherApplication;

/**
 * Created by ka on 28/11/2016.
 */

public class UseAppPrefs {
    private static final String USE_APP = "USE_APP";

    public static void isFirstTime(boolean isFirstTime) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(USE_APP, Context.MODE_PRIVATE)
                .edit();
        editor.putBoolean("first", isFirstTime);
        editor.apply();
    }

    public static boolean checkFirstTime() {
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(USE_APP, Context.MODE_PRIVATE);
        return prefs.getBoolean("first", true);
    }
}
