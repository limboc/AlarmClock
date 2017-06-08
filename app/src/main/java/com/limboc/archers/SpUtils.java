package com.limboc.archers;

import android.content.Context;

/**
 * Created by admin on 2017/6/8.
 */

public class SpUtils {

    public static void setAlarmTime(Context context, long timemillis){
        PreferenceUtils.putLong(PreferenceContract.ALARM_TIME, timemillis, context);
    }

    public static long getAlarmTime(Context context){
        return PreferenceUtils.getLong(PreferenceContract.ALARM_TIME, 0, context);
    }

}
