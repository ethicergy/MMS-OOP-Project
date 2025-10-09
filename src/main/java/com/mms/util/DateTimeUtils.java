package com.mms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    private static final String DISPLAY_FORMAT = "dd MMM yyyy HH:mm";
    private static final String DB_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Formats a LocalDate as dd MMM yyyy
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    // Formats a LocalTime as h:mm a
    public static String formatTime(LocalTime time) {
        if (time == null) return "";
        int hour = time.getHour();
        int minute = time.getMinute();
        String amPm = (hour >= 12) ? "PM" : "AM";
        int displayHour = (hour == 0) ? 12 : (hour > 12 ? hour - 12 : hour);
        return String.format("%d:%02d %s", displayHour, minute, amPm);
    }

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
