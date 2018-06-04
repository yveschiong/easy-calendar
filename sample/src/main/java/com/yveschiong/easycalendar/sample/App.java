package com.yveschiong.easycalendar.sample;

import android.app.Application;

import com.yveschiong.easycalendar.sample.injection.AppComponent;
import com.yveschiong.easycalendar.sample.injection.AppModule;
import com.yveschiong.easycalendar.sample.injection.DaggerAppComponent;

public class App extends Application {

    private static App singleton;
    private AppComponent component;

    public App() {
        singleton = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setComponent(createComponent());
    }

    public static AppComponent getComponent() {
        return singleton.component;
    }

    public void setComponent(AppComponent component) {
        this.component = component;
    }

    public AppComponent createComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
}
