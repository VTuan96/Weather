package com.android.ka.weather.prefs;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by minhpt on 11/2/2016.
 */

public class QueryPreferences {

    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final String PREFS_IS_ALARM_ON = "PREFS_IS_ALARM_ON";

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREFS_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREFS_IS_ALARM_ON, isOn)
                .apply();
    }

    public static void setLocation(Context context, android.location.Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString("lat", lat + "")
                .putString("lon", lon + "")
                .apply();
    }

    public static String getLatitude(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("lat", null);
    }

    public static String getLongitude(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString("lon", null);
    }
}
