package com.yveschiong.easycalendar.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yveschiong.easycalendar.R;
import com.yveschiong.easycalendar.models.Event;
import com.yveschiong.easycalendar.utils.CalendarUtils;
import com.yveschiong.easycalendar.views.DayView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 5);

        Calendar endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 10);

        DayView dayView = findViewById(R.id.dayView);
        dayView.addEvent(new Event("Test Event Name", "Test Event Description", startCalendar, endCalendar));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 20);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 22);

        dayView.addEvent(new Event("Test Event Name 2", "Test Event Description 2", startCalendar, endCalendar));
    }
}
