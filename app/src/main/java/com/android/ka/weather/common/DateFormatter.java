package com.android.ka.weather.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by roma on 04.11.15.
 */
public class DateFormatter {

    public static String convertLongToStringFull(long time) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(time));
        return new SimpleDateFormat("EEEE, LLLL dd", Locale.ENGLISH).format(date);
    }

    public static String convertLongToDayName(long time) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(time));
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
    }

    public static String formatString(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            Logger.getLogger(DateFormatter.class).error(e);
        }
        return new SimpleDateFormat("EEEE, LLLL dd", Locale.ENGLISH).format(d);

    }
}
