package com.example.pein.demo.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.pein.demo.Constants;
import com.example.pein.demo.R;
import com.orhanobut.logger.Logger;
import com.squareup.timessquare.CalendarPickerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Logger.init();

        DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Date firstDay = null;
        try {
            firstDay = format.parse("20130520");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CalendarPickerView calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, -1);
        calendarPickerView.init(firstDay, today).withSelectedDate(calendar.getTime());

        calendarPickerView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {
                Calendar yesterday = Calendar.getInstance();
                yesterday.setTime(date);
                yesterday.add(Calendar.DATE, 1);
                String formatedDate = Constants.formatDate(yesterday);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("date", formatedDate);
                startActivity(intent);
                return true;
            }
        });
    }
}
