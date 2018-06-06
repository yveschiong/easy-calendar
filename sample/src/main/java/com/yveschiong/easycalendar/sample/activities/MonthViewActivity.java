package com.yveschiong.easycalendar.sample.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.utils.CalendarUtils;
import com.yveschiong.easycalendar.views.MonthView;

import java.util.Calendar;

public class MonthViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);

        MonthView firstMonthView = findViewById(R.id.firstMonthView);
        firstMonthView.setOnSelectedDayListener(new MonthView.OnSelectedDayListener() {
            @Override
            public void onSelectedDay(Calendar day) {
                Toast.makeText(MonthViewActivity.this, CalendarUtils.toSimpleString(day), Toast.LENGTH_SHORT).show();
            }
        });

        MonthView secondMonthView = findViewById(R.id.secondMonthView);

        Calendar calendar = secondMonthView.getMonth().getStart();
        calendar.add(Calendar.MONTH, 1);
        secondMonthView.setMonth(calendar);
    }
}
