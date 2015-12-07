package com.example.pein.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Pein on 15/12/3.
 */
public final class Constants {
    public static final class URL {
        public static final String latestURL = "http://news-at.zhihu.com/api/4/news/latest";
        public static final String newsURL = "http://news-at.zhihu.com/api/4/news/";
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Beijing"));
        Date now = new Date();
        String strDate = simpleDateFormat.format(now);
        return strDate;
    }
}
