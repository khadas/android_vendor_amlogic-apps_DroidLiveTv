package com.droidlogic.droidlivetv;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.util.Log;

import com.droidlogic.app.tv.DroidLogicTvUtils;

public class TimerSuspendReceiver extends BroadcastReceiver {
    private static final String TAG = "TimerSuspendReceiver";

    private Context mContext = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent);

        mContext = context;
        startSleepTimer(intent);
    }

    public void startSleepTimer (Intent intent) {
        Log.d(TAG, "startSleepTimer");
        Intent intentservice = new Intent(mContext, TimerSuspendService.class );
        intentservice.putExtra(DroidLogicTvUtils.KEY_ENABLE_NOSIGNAL_TIMEOUT, intent.getBooleanExtra(DroidLogicTvUtils.KEY_ENABLE_NOSIGNAL_TIMEOUT, false));
        intentservice.putExtra(DroidLogicTvUtils.KEY_ENABLE_SUSPEND_TIMEOUT, intent.getBooleanExtra(DroidLogicTvUtils.KEY_ENABLE_SUSPEND_TIMEOUT, false));
        mContext.startService (intentservice);
    }
}
