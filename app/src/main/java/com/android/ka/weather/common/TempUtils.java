package com.android.ka.weather.common;

/**
 * Created by ka on 26/11/2016.
 */

public class TempUtils {
    public static String convertKtoC(double temp) {
        int tmp = (int) (temp - 273.15);
        return tmp + "\u2103";
    }

    public static String convert(double temp) {
        return (int) (temp - 273.15) + "-";
    }
}
