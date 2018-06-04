package com.yveschiong.easycalendar.sample.injection;


import android.content.Context;

import com.yveschiong.easycalendar.sample.factories.ModuleFactory;
import com.yveschiong.easycalendar.sample.helpers.ActivityHelper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context getAppContext();

    ActivityHelper getActivityHelper();

    ModuleFactory getModuleFactory();
}
