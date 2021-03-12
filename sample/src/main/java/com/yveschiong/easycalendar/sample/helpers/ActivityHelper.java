package com.yveschiong.easycalendar.sample.helpers;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.yveschiong.easycalendar.sample.activities.BaseActivity;

public class ActivityHelper {

    private Context context;

    public ActivityHelper(@NonNull Context context) {
        this.context = context;

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
    }

    public void startActivity(Class activityClass) {
        if (!BaseActivity.class.isAssignableFrom(activityClass)) {
            throw new IllegalArgumentException("Wrong class instance. Should be an instance of BaseActivity.");
        }

        Intent intent = new Intent(context, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
