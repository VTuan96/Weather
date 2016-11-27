package com.android.ka.weather.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static String convertLongToStringFull(long time) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(time));
        return new SimpleDateFormat("EEEE, LLLL dd", Locale.ENGLISH).format(date);
    }

    public static String convertLongToDayName(long time) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(time));
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
    }
}
