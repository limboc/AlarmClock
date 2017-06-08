package com.limboc.archers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private TextView tvTime;
    private static final int DEFAULT_HOUR = 8, DEFAULT_MINUTE = 40;
    private final String MYACTION = "android.intent.action.open.dingding";
    private AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        tvTime = (TextView) findViewById(R.id.tv_time);
        long alarmTime = SpUtils.getAlarmTime(context);
        if (alarmTime == 0) {
            tvTime.setText(DEFAULT_HOUR + ":" + DEFAULT_MINUTE);
            setAlarm(DEFAULT_HOUR, DEFAULT_MINUTE);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(alarmTime);
            tvTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
            setAlarm(Calendar.HOUR_OF_DAY, calendar.get(Calendar.MINUTE));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                setAlarmtTime();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setAlarmtTime() {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        tvTime.setText(hour + ":" + minute);
                        setAlarm(hour, minute);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, hour);
                        cal.set(Calendar.MINUTE, minute);
                        cal.set(Calendar.SECOND, 0);
                        SpUtils.setAlarmTime(context, cal.getTimeInMillis());
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();
    }


    private void setAlarm(int hour, int minute) {
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        long intervalMillis = 0;
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 0);
        Intent intent = new Intent(MYACTION);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        am.setWindow(AlarmManager.RTC_WAKEUP, calMethod(0, calendar.getTimeInMillis()),
                intervalMillis, sender);
    }

    private long calMethod(int weekflag, long dateTime) {
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

    private int getDayOfWeek(int calendarWeek) {
        return (calendarWeek == 1 ? 7 : calendarWeek - 1);
    }

}
