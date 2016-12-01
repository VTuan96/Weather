package com.android.ka.weather.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.ka.weather.WeatherApplication;
import com.android.ka.weather.model.WeatherDisplay;

/**
 * Created by ka on 28/11/2016.
 */

public class WeatherPrefs {
    private static final String MAIN_WEATHER = "MAIN_WEATHER";
    private static final String DAY2 = "DAY2";
    private static final String DAY3 = "DAY3";
    private static final String DAY4 = "DAY4";
    private static final String DAY5 = "DAY5";
    private static final String ID = "ID";

    public static WeatherDisplay getMainWeather() {
        WeatherDisplay weatherDisplay = new WeatherDisplay();
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(MAIN_WEATHER, Context.MODE_PRIVATE);
        weatherDisplay.setTimeForecast(prefs.getString("timeForecast", ""));
        weatherDisplay.setWeatherId(prefs.getInt("weatherId", 1));
        weatherDisplay.setTemp(prefs.getString("temp", ""));
        weatherDisplay.setCity(prefs.getString("city", ""));
        weatherDisplay.setStatus(prefs.getString("status", ""));
        weatherDisplay.setWind(prefs.getString("wind", ""));
        weatherDisplay.setHumidity(prefs.getString("humidity", ""));
        weatherDisplay.setTempAva(prefs.getString("tempAva", ""));
        return weatherDisplay;
    }

    public static void setMainWeather(WeatherDisplay display) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(MAIN_WEATHER, Context.MODE_PRIVATE)
                .edit();
        editor.putInt("weatherId", display.getWeatherId());
        editor.putString("temp", display.getTemp());
        editor.putString("tempAva", display.getTempAva());
        editor.putString("city", display.getCity());
        editor.putString("timeForecast", display.getTimeForecast());
        editor.putString("status", display.getStatus());
        editor.putString("wind", display.getWind());
        editor.putString("humidity", display.getHumidity());
        editor.apply();
    }

    public static String getId() {
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(ID, Context.MODE_PRIVATE);
        return prefs.getString("_id", "yeah");
    }

    public static void setId(String id) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(ID, Context.MODE_PRIVATE).edit();
        editor.putString("_id", id);
        editor.apply();
    }

    public static WeatherDisplay getDay2() {
        WeatherDisplay weatherDisplay = new WeatherDisplay();
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(DAY2, Context.MODE_PRIVATE);
        weatherDisplay.setTempAva(prefs.getString("tempAva", ""));
        weatherDisplay.setTimeForecast(prefs.getString("timeForecast", ""));
        weatherDisplay.setWeatherId(prefs.getInt("weatherId", 1));
        return weatherDisplay;
    }

    public static void setDay2(WeatherDisplay display) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(DAY2, Context.MODE_PRIVATE)
                .edit();
        editor.putInt("weatherId", display.getWeatherId());
        editor.putString("tempAva", display.getTempAva());
        editor.putString("timeForecast", display.getTimeForecast());
        editor.apply();
    }

    public static WeatherDisplay getDay3() {
        WeatherDisplay weatherDisplay = new WeatherDisplay();
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(DAY3, Context.MODE_PRIVATE);
        weatherDisplay.setTempAva(prefs.getString("tempAva", ""));
        weatherDisplay.setTimeForecast(prefs.getString("timeForecast", ""));
        weatherDisplay.setWeatherId(prefs.getInt("weatherId", 1));
        return weatherDisplay;
    }

    public static void setDay3(WeatherDisplay display) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(DAY3, Context.MODE_PRIVATE)
                .edit();
        editor.putInt("weatherId", display.getWeatherId());
        editor.putString("tempAva", display.getTempAva());
        editor.putString("timeForecast", display.getTimeForecast());
        editor.apply();
    }

    public static WeatherDisplay getDay4() {
        WeatherDisplay weatherDisplay = new WeatherDisplay();
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(DAY4, Context.MODE_PRIVATE);
        weatherDisplay.setTempAva(prefs.getString("tempAva", ""));
        weatherDisplay.setTimeForecast(prefs.getString("timeForecast", ""));
        weatherDisplay.setWeatherId(prefs.getInt("weatherId", 1));
        return weatherDisplay;
    }

    public static void setDay4(WeatherDisplay display) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(DAY4, Context.MODE_PRIVATE)
                .edit();
        editor.putInt("weatherId", display.getWeatherId());
        editor.putString("tempAva", display.getTempAva());
        editor.putString("timeForecast", display.getTimeForecast());
        editor.apply();
    }

    public static WeatherDisplay getDay5() {
        WeatherDisplay weatherDisplay = new WeatherDisplay();
        SharedPreferences prefs = WeatherApplication.getInstance()
                .getSharedPreferences(DAY5, Context.MODE_PRIVATE);
        weatherDisplay.setTempAva(prefs.getString("tempAva", ""));
        weatherDisplay.setTimeForecast(prefs.getString("timeForecast", ""));
        weatherDisplay.setWeatherId(prefs.getInt("weatherId", 1));
        return weatherDisplay;
    }

    public static void setDay5(WeatherDisplay display) {
        SharedPreferences.Editor editor = WeatherApplication.getInstance()
                .getSharedPreferences(DAY5, Context.MODE_PRIVATE)
                .edit();
        editor.putInt("weatherId", display.getWeatherId());
        editor.putString("tempAva", display.getTempAva());
        editor.putString("timeForecast", display.getTimeForecast());
        editor.apply();
    }
}
