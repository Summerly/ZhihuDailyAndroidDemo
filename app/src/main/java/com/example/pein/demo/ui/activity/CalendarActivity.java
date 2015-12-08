package com.example.pein.demo.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.pein.demo.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView calendarPickerView = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendarPickerView.init(today, nextYear.getTime()).withSelectedDate(today);
    }
}
