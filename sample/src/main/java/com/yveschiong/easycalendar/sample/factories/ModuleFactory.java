package com.yveschiong.easycalendar.sample.factories;

import android.content.Context;

import com.yveschiong.easycalendar.sample.App;
import com.yveschiong.easycalendar.sample.R;
import com.yveschiong.easycalendar.sample.activities.DayViewActivity;
import com.yveschiong.easycalendar.sample.models.Module;

import java.util.Arrays;
import java.util.List;

public class ModuleFactory {
    public Module getModule(@Module.ActivityName String name) {
        Context context = App.getComponent().getAppContext();
        if (name == null || name.isEmpty()) {
            return null;
        } else if (Module.DAY_VIEW_ACTIVITY.equals(name)) {
            return new Module(name, context.getString(R.string.day_view_activity_description), DayViewActivity.class);
        }

        return null;
    }

    public List<Module> getDefaultModuleList() {
        return Arrays.asList(
                getModule(Module.DAY_VIEW_ACTIVITY)
        );
    }
}
