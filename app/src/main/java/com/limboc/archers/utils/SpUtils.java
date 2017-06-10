package com.limboc.archers.utils;

import android.content.Context;

import com.limboc.archers.App;

/**
 * Created by admin on 2017/6/8.
 */

public class SpUtils {

    public static void setAlarmTime(long timemillis){
        PreferenceUtils.putLong(PreferenceContract.ALARM_TIME, timemillis, App.context);
    }

    public static long getAlarmTime(){
        return PreferenceUtils.getLong(PreferenceContract.ALARM_TIME, 0, App.context);
    }

    public static void clear(){
        PreferenceUtils.clear(App.context);
    }
}
