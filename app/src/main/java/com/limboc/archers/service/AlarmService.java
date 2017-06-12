package com.limboc.archers.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.limboc.archers.IAlarmAidlInterface;
import com.limboc.archers.utils.AlarmUtils;

import java.util.Calendar;

import static com.limboc.archers.utils.Constants.DEFAULT_HOUR;
import static com.limboc.archers.utils.Constants.DEFAULT_MINUTE;

/**
 * Created by admin on 2017/6/10.
 */

public class AlarmService extends Service {

    private static final String TAG = "Archers";

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onbind");
        return new IAlarmAidlInterface.Stub(){
            @Override
            public void setCheckInTime(long millis) throws RemoteException {
                setAlarm(millis);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        AlarmUtils.cancelAlarm(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "unbind");
        return true;
    }


    private void setAlarm(long millis){
        int hour = DEFAULT_HOUR;
        int minute = DEFAULT_MINUTE;
        if(millis != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
        Log.d(TAG, "打卡时间：" + hour + ":" + minute);
        AlarmUtils.setHourMinute(this, hour, minute);

    }


}
