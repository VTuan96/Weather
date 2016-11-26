package com.android.ka.weather.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by roma on 04.11.15.
 */
public class DateFormatter {

    public static String convertDateToString(Date date) {
        String DATE_PATTERN = "HH:mm";
        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(date);
    }

    public static String convertDateToStringFull(Date date) {
        String DATE_PATTERN = "dd/MM/yyyy";
        return new SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(date);
    }

    public static String convertToTime(String s) {
        s = s.substring(2, s.length() - 1);
        int length = s.length();
        String time = "";
        for (int i = 0; i < length; i++) {
            if (s.charAt(i) == 'M') {
                time = s.substring(0, i) + ":" + s.substring(i + 1, length);

            }
        }
        return time;
    }
}
