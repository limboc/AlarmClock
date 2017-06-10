package com.limboc.archers;

import android.app.Application;
import android.content.Context;

/**
 * Created by admin on 2017/6/10.
 */

public class App extends Application{
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
