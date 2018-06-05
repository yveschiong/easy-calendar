package com.yveschiong.easycalendar.sample.activities;

import android.os.Bundle;

import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.views.MonthView;

import java.util.Calendar;

public class MonthViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view);

        MonthView monthView = findViewById(R.id.monthView);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -4);

        monthView.setMonth(calendar);
    }
}
