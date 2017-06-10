package com.limboc.archers.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.limboc.archers.R;

import java.util.List;

/**
 * Created by admin on 2017/6/8.
 */
public class AlarmReceiver extends BroadcastReceiver
{
    public final String TAG = "Archers";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"I am AlarmReceiver, I receive the message");
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getString(R.string.dingding_pkg_name));
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(isIntentAvailable(context, launchIntent)){
            context.startActivity(launchIntent);
        }
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}