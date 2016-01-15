package com.example.pein.demo;

import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Pein on 1/15/16.
 */
public final class TimeUtils {
    private static final String dateFormat = "yyyyMMdd";
    private static final String timeZone = "Beijing";

    public static String getCurrentDate() {
        String strDate = formatDate(new Date());
        return strDate;
    }

    public static String getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        String strDate = formatCalendar(calendar);
        return strDate;
    }

    public static String getTomorrowDate(String strDate) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = getCalendar(date);
        calendar.add(Calendar.DATE, 1);
        Logger.e(calendar.getTime().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        Logger.e("TimeZone.getDefault: " + TimeZone.getDefault().toString());

        String formatDateStr = simpleDateFormat.format(calendar.getTime());

        Logger.e(formatDateStr);

        return formatDateStr;
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        return calendar;
    }

    public static String formatCalendar(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat();
        String strCalendar = simpleDateFormat.format(calendar.getTime());
        return strCalendar;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat();
        String strDate = simpleDateFormat.format(date);
        return strDate;
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleDateFormat;
    }
}
