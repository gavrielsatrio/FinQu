package com.example.finqu.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static String convertDateToString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    };

    public static Date convertStringToDate(String str, String format) {
        try {
            return new SimpleDateFormat(format).parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    };

    public static Date convertToDateFromString(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy");
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertToStringFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMM yyyy");
        return formatter.format(date);
    }

    public static Date convertToDateFromShortString(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertToShortStringFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        return formatter.format(date);
    }

    public static String convertToSuperShortStringFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        return formatter.format(date);
    }

    public static Date convertToMonthAndYearFromString(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        try {
            return formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMonthAndYearFromString(String str) {
        Date date = convertToDateFromString(str);

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        return formatter.format(date);
    }

    public static String getMonthAndYearFromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");
        return formatter.format(date);
    }

    public static Date getDateNow() {
        return convertToDateFromString(convertToStringFromDate(Calendar.getInstance().getTime()));
    }
}
