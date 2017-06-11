package com.limboc.archers.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

/**
 * Created by admin on 2017/6/10.
 */

public class AlarmUtils {
    public static void setHourMinute(Context context, int hour, int minute) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long intervalMillis = 0;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 0);
        Intent intent = new Intent(Constants.AlarmReceiverActionName);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        am.setWindow(AlarmManager.RTC_WAKEUP, calMethod(0, calendar.getTimeInMillis()),
                intervalMillis, sender);
    }

    public static void cancelAlarm(Context context){
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Constants.AlarmReceiverActionName);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        am.cancel(sender);
    }

    private static long calMethod(int weekflag, long dateTime) {
        long time = 0;
        if (weekflag != 0) {
            Calendar c = Calendar.getInstance();
            int week = getDayOfWeek(c.get(Calendar.DAY_OF_WEEK));
            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }

    private static int getDayOfWeek(int calendarWeek) {
        return (calendarWeek == 1 ? 7 : calendarWeek - 1);
    }

}
