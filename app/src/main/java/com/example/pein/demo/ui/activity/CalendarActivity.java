package com.example.pein.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pein.demo.R;
import com.example.pein.demo.TimeUtils;
import com.orhanobut.logger.Logger;
import com.squareup.timessquare.CalendarPickerView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

//        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        DateFormat format = TimeUtils.getSimpleDateFormat();

        Date firstDay = null;
        try {
            firstDay = format.parse("20130520");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Date minDate = firstDay;
        final Date maxDate = new Date();

        CalendarPickerView calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 1);
        calendarPickerView.init(firstDay, calendar.getTime()).withSelectedDate(today);

        calendarPickerView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                Calendar calendar = TimeUtils.getCalendar(date);
                calendar.add(Calendar.DATE, 1);

                if (date.after(minDate) && date.before(maxDate)) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("date", TimeUtils.formatDate(calendar.getTime()));
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}
