package com.yveschiong.easycalendar.sample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yveschiong.easycalendar.sample.App;
import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.sample.adapters.ActivityListAdapter;
import com.yveschiong.easycalendar.sample.injection.AppComponent;
import com.yveschiong.easycalendar.sample.presenters.ActivityListPresenter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppComponent component = App.getComponent();

        RecyclerView activityList = findViewById(R.id.activityList);
        activityList.setLayoutManager(new LinearLayoutManager(this));
        activityList.setAdapter(new ActivityListAdapter(new ActivityListPresenter(component.getModuleFactory().getDefaultModuleList(), component.getActivityHelper())));
    }
}
