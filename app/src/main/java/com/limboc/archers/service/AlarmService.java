package com.limboc.archers.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.limboc.archers.utils.AlarmUtils;
import com.limboc.archers.IAlarmAidlInterface;
import com.limboc.archers.utils.Constants;

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
        return new IAlarmAidlInterface.Stub(){
            @Override
            public void setCheckInTime(long millis) throws RemoteException {
                setAlarm(millis);
            }
        };
    }

    @Override
    public boolean onUnbind(Intent intent) {
        AlarmUtils.cancelAlarm(this);
        return super.onUnbind(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
