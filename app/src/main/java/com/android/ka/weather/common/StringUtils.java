package com.android.ka.weather.common;

/**
 * Created by ka on 28/11/2016.
 */

public class StringUtils {
    public static String capitalizeString(String desc) {
        StringBuilder tmp = new StringBuilder(desc);
        tmp.setCharAt(0, Character.toUpperCase(tmp.charAt(0)));
        desc = tmp.toString();
        return desc;
    }
}
