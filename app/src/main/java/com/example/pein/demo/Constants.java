package com.example.pein.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Pein on 15/12/3.
 */
public final class Constants {
    public static final class URL {
        public static final String latestURL = "http://news-at.zhihu.com/api/4/news/latest";
        public static final String newsURL = "http://news-at.zhihu.com/api/4/news/";
        public static final String beforeURL = "http://news.at.zhihu.com/api/4/news/before/";
    }

    public static final class TAG {
        public static final String latestTAG = "latest";
        public static final String errorTAG = "error";
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Beijing"));
        Date now = new Date();
        String strDate = simpleDateFormat.format(now);
        return strDate;
    }

    public static String getTomorrowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Beijing"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        String strDate = simpleDateFormat.format(calendar.getTime());
        return strDate;
    }

    public static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Beijing"));
        String strDate = simpleDateFormat.format(calendar.getTime());
        return strDate;
    }
}
