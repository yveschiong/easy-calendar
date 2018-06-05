package com.yveschiong.easycalendar.sample.models;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Module {
    @Retention(RetentionPolicy.CLASS)
    @StringDef({DAY_VIEW_ACTIVITY, MONTH_VIEW_ACTIVITY})
    public @interface ActivityName {}
    public static final String DAY_VIEW_ACTIVITY = "DayViewActivity";
    public static final String MONTH_VIEW_ACTIVITY = "MonthViewActivity";

    @ActivityName
    private String name;
    private String description;
    private Class moduleClass;

    public Module(String name, String description, Class moduleClass) {
        this.name = name;
        this.description = description;
        this.moduleClass = moduleClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class getModuleClass() {
        return moduleClass;
    }

    public void setModuleClass(Class moduleClass) {
        this.moduleClass = moduleClass;
    }
}
