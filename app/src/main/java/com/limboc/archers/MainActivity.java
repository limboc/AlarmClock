package com.limboc.archers;

import android.app.ActivityManager;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.limboc.archers.service.AlarmService;
import com.limboc.archers.utils.SpUtils;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.limboc.archers.utils.Constants.DEFAULT_HOUR;
import static com.limboc.archers.utils.Constants.DEFAULT_MINUTE;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private Switch aSwitch;
    private TextView tvTime;
    private IAlarmAidlInterface iAlarmAidlInterface;
    private long alarmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        tvTime = (TextView) findViewById(R.id.tv_time);
        aSwitch = (Switch) findViewById(R.id.aSwitch);
        init();
    }


    private void init() {
        alarmTime = SpUtils.getAlarmTime();
        if(alarmTime != 0){
            aSwitch.setText(R.string.service_off);
            aSwitch.setChecked(true);
            startService();
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    aSwitch.setText(R.string.service_off);
                    startService();
                } else {
                    aSwitch.setText(R.string.service_on);
                    tvTime.setText("");
                    SpUtils.clear();
                    alarmTime = 0;
                    stopService();
                }
            }
        });
    }

    private void showAlarmTime(int hour, int minute) {
        StringBuilder sb = new StringBuilder();
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(hour + ":");
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute);
        tvTime.setText(sb.toString());
    }

    private void startService() {
        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        if (alarmTime == 0) {
            showAlarmTime(DEFAULT_HOUR, DEFAULT_MINUTE);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(alarmTime);
            showAlarmTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        }
    }

    private void stopService() {
        if (serviceConnection != null && isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }
        if (isRunning(AlarmService.class.getName())) {
            stopService(new Intent(this, AlarmService.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null && isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }
    }

    public void setAlarmtTime(View view) {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        showAlarmTime(hour, minute);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hour);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        SpUtils.setAlarmTime(cal.getTimeInMillis());
                        setCheckInTime(cal.getTimeInMillis());
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }

    Executor execute = Executors.newCachedThreadPool();
    private void setCheckInTime(final long millis) {
        execute.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (iAlarmAidlInterface != null)
                        iAlarmAidlInterface.setCheckInTime(millis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private boolean isRunning(String className) {

        boolean tag = false;
        android.app.ActivityManager mActivityManager = (android.app.ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mRunningTaskInfos = mActivityManager.getRunningServices(150);
        for (int i = 0; i < mRunningTaskInfos.size(); i++) {
            if (mRunningTaskInfos.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return tag;

    }

    private boolean isBind = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            iAlarmAidlInterface = IAlarmAidlInterface.Stub.asInterface(iBinder);
            isBind = true;
            setCheckInTime(alarmTime);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

}
