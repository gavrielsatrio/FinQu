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

    public static Date getDateNow() {
        return convertStringToDate(convertDateToString(Calendar.getInstance().getTime(), "dd MMM yyyy"), "dd MMM yyyy");
    }
}
