package com.mms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    private static final String DISPLAY_FORMAT = "dd MMM yyyy HH:mm";
    private static final String DB_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatForDisplay(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DISPLAY_FORMAT).format(date);
    }

    public static String formatForDatabase(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(DB_FORMAT).format(date);
    }

    public static Date parseFromDisplay(String dateStr) throws ParseException {
        return new SimpleDateFormat(DISPLAY_FORMAT).parse(dateStr);
    }

    public static Date parseFromDatabase(String dateStr) throws ParseException {
        return new SimpleDateFormat(DB_FORMAT).parse(dateStr);
    }
}
