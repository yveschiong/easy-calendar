package com.yveschiong.easycalendar.sample.injection;

import android.content.Context;

import com.yveschiong.easycalendar.sample.factories.ModuleFactory;
import com.yveschiong.easycalendar.sample.helpers.ActivityHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideAppContext() {
        return context;
    }

    @Provides
    @Singleton
    public ActivityHelper provideActivityHelper(Context context) {
        return new ActivityHelper(context);
    }

    @Provides
    @Singleton
    public ModuleFactory provideModuleFactory() {
        return new ModuleFactory();
    }
}