package com.kejian.one;

import android.app.Application;
import android.content.Context;

/**
 * @author Zhenxi on 2020-08-31
 */
public class App extends Application {
    private static Context ApplicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContext=getBaseContext();
    }
    public static Context getAppContext(){
        return ApplicationContext;
    }
}
