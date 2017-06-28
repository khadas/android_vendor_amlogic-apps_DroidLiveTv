package com.droidlogic.droidlivetv;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

public class TimerSuspendReceiver extends BroadcastReceiver {
    private static final String TAG = "TimerSuspendReceiver";

    private Context mContext = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent);

        mContext = context;
        startSleepTimer();
    }

    public void startSleepTimer () {
        Log.d(TAG, "startSleepTimer");
        mContext.startService (new Intent(mContext, TimerSuspendService.class ));
    }
}
