package com.android.ka.weather.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.ka.weather.WeatherApplication;

/**
 * Created by ka on 04/12/2016.
 */

public class ShowWallpaperPrefs {
    private static final String SHOW_WALLPAPER = "SHOW_WALLPAPER";

    public static boolean getShowWallpaper() {
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(SHOW_WALLPAPER, Context.MODE_PRIVATE);
        return prefs.getBoolean("show", false);
    }

    public static void setShowWallpaper(boolean isShow) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(SHOW_WALLPAPER, Context.MODE_PRIVATE)
                .edit();
        editor.putBoolean("show", isShow);
        editor.apply();
    }
}
