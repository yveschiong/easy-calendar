package com.yveschiong.easycalendar.sample.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.models.CalendarRange;
import com.yveschiong.easycalendar.models.Event;
import com.yveschiong.easycalendar.utils.CalendarUtils;
import com.yveschiong.easycalendar.views.DayView;

import java.util.Calendar;

public class DayViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_view);

        DayView dayView = findViewById(R.id.dayView);
        dayView.addEventClickedListener(new DayView.EventClickedListener() {
            @Override
            public void onEventClicked(Event event) {
                Toast.makeText(DayViewActivity.this, event.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        Calendar startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.DATE, -1);
        startCalendar.add(Calendar.HOUR_OF_DAY, 23);

        Calendar endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 2);

        dayView.addEvent(new Event("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam lobortis scelerisque ante, a porttitor eros interdum ut. Aliquam eu viverra ipsum. Vestibulum vel risus massa. Suspendisse ligula turpis, congue eu ipsum vestibulum, porta tincidunt nibh. Curabitur tincidunt dictum molestie. Quisque consectetur libero ac ornare pulvinar. In dapibus mi quis tristique molestie. Donec fermentum pretium enim ac sagittis. Duis ut venenatis sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed faucibus ornare diam, sed laoreet lacus commodo eget. In ultricies sodales odio eget imperdiet. Nunc lobortis turpis et est euismod, ac dapibus massa lobortis. Mauris porta odio vitae risus molestie fermentum. Praesent a arcu urna.", "Test Event Description", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 23);
        startCalendar.add(Calendar.MINUTE, 55);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.DATE, 1);
        endCalendar.add(Calendar.HOUR_OF_DAY, 23);
        endCalendar.add(Calendar.MINUTE, 56);

        dayView.addEvent(new Event("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam lobortis scelerisque ante, a porttitor eros interdum ut. Aliquam eu viverra ipsum. Vestibulum vel risus massa. Suspendisse ligula turpis, congue eu ipsum vestibulum, porta tincidunt nibh. Curabitur tincidunt dictum molestie. Quisque consectetur libero ac ornare pulvinar. In dapibus mi quis tristique molestie. Donec fermentum pretium enim ac sagittis. Duis ut venenatis sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed faucibus ornare diam, sed laoreet lacus commodo eget. In ultricies sodales odio eget imperdiet. Nunc lobortis turpis et est euismod, ac dapibus massa lobortis. Mauris porta odio vitae risus molestie fermentum. Praesent a arcu urna.", "Test Event Description", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 5);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 10);

        dayView.addEvent(new Event("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam lobortis scelerisque ante, a porttitor eros interdum ut. Aliquam eu viverra ipsum. Vestibulum vel risus massa. Suspendisse ligula turpis, congue eu ipsum vestibulum, porta tincidunt nibh. Curabitur tincidunt dictum molestie. Quisque consectetur libero ac ornare pulvinar. In dapibus mi quis tristique molestie. Donec fermentum pretium enim ac sagittis. Duis ut venenatis sapien. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed faucibus ornare diam, sed laoreet lacus commodo eget. In ultricies sodales odio eget imperdiet. Nunc lobortis turpis et est euismod, ac dapibus massa lobortis. Mauris porta odio vitae risus molestie fermentum. Praesent a arcu urna.", "Test Event Description", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.DATE, -1);
        startCalendar.add(Calendar.HOUR_OF_DAY, 14);
        startCalendar.add(Calendar.MINUTE, 20);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.DATE, -1);
        endCalendar.add(Calendar.HOUR_OF_DAY, 18);
        endCalendar.add(Calendar.MINUTE, 45);

        dayView.addEvent(new Event("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam lobortis scelerisque ante, a porttitor eros interdum ut.", "Test Event Description 2", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 20);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 22);

        dayView.addEvent(new Event("Test Event Name 3", "Test Event Description 3", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 19);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 19);
        endCalendar.add(Calendar.MINUTE, 55);

        dayView.addEvent(new Event("Test Event Name 4", "Test Event Description 4", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 18);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 19);
        endCalendar.add(Calendar.MINUTE, 35);

        dayView.addEvent(new Event("Test Event Name 5", "Test Event Description 5", new CalendarRange(startCalendar, endCalendar)));

        startCalendar = CalendarUtils.createCalendar();
        startCalendar.add(Calendar.HOUR_OF_DAY, 15);

        endCalendar = CalendarUtils.createCalendar();
        endCalendar.add(Calendar.HOUR_OF_DAY, 21);

        dayView.addEvent(new Event("Test Event Name 6", "Test Event Description 6", new CalendarRange(startCalendar, endCalendar)));
    }
}
