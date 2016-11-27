package com.android.ka.weather;

import android.app.Application;

import com.android.ka.weather.common.Logger;
import com.android.ka.weather.prefs.UseAppPrefs;

/**
 * Created by ka on 28/11/2016.
 */

public class WeatherApplication extends Application {

    private static final Logger LOGGER = Logger.getLogger(WeatherApplication.class);
    static volatile WeatherApplication instance;

    public static WeatherApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (UseAppPrefs.checkFirstTime()) {
            UseAppPrefs.isFirstTime(true);
        }
    }
}
