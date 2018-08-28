package com.droidlogic.droidlivetv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.media.tv.TvContract;

import com.droidlogic.app.tv.TvControlManager;
import com.droidlogic.app.SystemControlManager;
import com.droidlogic.app.tv.TvDataBaseManager;
import com.droidlogic.app.tv.DroidLogicTvUtils;
import com.droidlogic.app.tv.TvInSignalInfo;
import com.droidlogic.app.tv.ChannelInfo;

public class SetParameters {
    private final static String TAG = "SetParameters";
    private TvControlManager mTvControlManager;
    private TvDataBaseManager mTvDataBaseManager;
    private SystemControlManager mSystemControlManager;
    private TvControlManager.SourceInput mTvSourceInput;
    private TvControlManager.SourceInput_Type mVirtualTvSource;
    private TvControlManager.SourceInput_Type mTvSource;
    private Resources mResources;
    private Context mContext;
    private Bundle mBundle;
    private int mDeviceId;
	
    public SetParameters(Context context, Bundle bundle) {
        this.mContext = context;
        this.mBundle = bundle;
        this.mDeviceId = bundle.getInt("deviceid", -1);
        mTvDataBaseManager = new TvDataBaseManager(mContext);
        mSystemControlManager = new SystemControlManager(mContext);
        mTvControlManager = TvControlManager.getInstance();
        mResources = mContext.getResources();
        setCurrentChannelData(bundle);
    }

    private void setCurrentChannelData(Bundle bundle) {
        ChannelInfo currentChannel;

        mTvSource = DroidLogicTvUtils.parseTvSourceTypeFromDeviceId(mDeviceId);
        mTvSourceInput = DroidLogicTvUtils.parseTvSourceInputFromDeviceId(mDeviceId);
        mVirtualTvSource = mTvSource;

        if (mTvSource == TvControlManager.SourceInput_Type.SOURCE_TYPE_ADTV) {
            long channelId = bundle.getLong("channelid", -1);
            currentChannel = mTvDataBaseManager.getChannelInfo(TvContract.buildChannelUri(channelId));
            if (currentChannel != null) {
                mTvSource = DroidLogicTvUtils.parseTvSourceTypeFromSigType(DroidLogicTvUtils.getSigType(currentChannel));
                mTvSourceInput = DroidLogicTvUtils.parseTvSourceInputFromSigType(DroidLogicTvUtils.getSigType(currentChannel));
            }
            if (mVirtualTvSource == mTvSource) {//no channels in adtv input, DTV for default.
                mTvSource = TvControlManager.SourceInput_Type.SOURCE_TYPE_DTV;
                mTvSourceInput = TvControlManager.SourceInput.DTV;
            }
        }
    }

    public  int getPictureModeStatus () {
        int pictureModeIndex = mSystemControlManager.GetPQMode();
        Log.d(TAG, "getPictureModeStatus:" + pictureModeIndex);
        return pictureModeIndex;
    }

    public void setPictureMode (int mode) {
        Log.d(TAG, "setPictureMode:" + mode);
        if (mode == 0) {
            mSystemControlManager.SetPQMode(SystemControlManager.PQMode.PQ_MODE_STANDARD.toInt(), 1, 0);
        } else if (mode == 1) {
            mSystemControlManager.SetPQMode(SystemControlManager.PQMode.PQ_MODE_BRIGHT.toInt(), 1, 0);
        } else if (mode == 2) {
            mSystemControlManager.SetPQMode(SystemControlManager.PQMode.PQ_MODE_SOFTNESS.toInt(), 1, 0);
        } else if (mode == 3) {
            mSystemControlManager.SetPQMode(SystemControlManager.PQMode.PQ_MODE_USER.toInt(), 1, 0);
        }
    }

    public  int getSoundModeStatus () {
        int itemPosition = mTvControlManager.GetCurAudioSoundMode();
        Log.d(TAG, "getSoundModeStatus:" + itemPosition);
        return itemPosition;
    }

    public void setSoundMode (int mode) {
        Log.d(TAG, "setSoundMode:" + mode);
        if (mode == 0) {
            mTvControlManager.SetAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_STD);
            mTvControlManager.SaveCurAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_STD.toInt());
        } else if (mode == 1) {
            mTvControlManager.SetAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_MUSIC);
            mTvControlManager.SaveCurAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_MUSIC.toInt());
        } else if (mode == 2) {
            mTvControlManager.SetAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_NEWS);
            mTvControlManager.SaveCurAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_NEWS.toInt());
        } else if (mode == 3) {
            mTvControlManager.SetAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_THEATER);
            mTvControlManager.SaveCurAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_THEATER.toInt());
        } else if (mode == 4) {
            mTvControlManager.SetAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_USER);
            mTvControlManager.SaveCurAudioSoundMode(TvControlManager.Sound_Mode.SOUND_MODE_USER.toInt());
        }
    }

    public int getAspectRatioStatus () {
        int itemPosition = mSystemControlManager.GetDisplayMode(mTvSourceInput.toInt());
        Log.d(TAG, "getAspectRatioStatus:" + itemPosition);
        if (itemPosition == SystemControlManager.Display_Mode.DISPLAY_MODE_MODE43.toInt())
            return 1;
        else if (itemPosition == SystemControlManager.Display_Mode.DISPLAY_MODE_FULL.toInt())
            return 2;
        else if (itemPosition == SystemControlManager.Display_Mode.DISPLAY_MODE_169.toInt())
            return 3;
        else
            return 0;
    }

    public void setAspectRatio(int mode) {
        Log.d(TAG, "setAspectRatio:" + mode);
        if (mode == 0) {
            mSystemControlManager.SetDisplayMode(mTvSourceInput.toInt(), SystemControlManager.Display_Mode.DISPLAY_MODE_NORMAL, 1);
        } else if (mode == 1) {
            mSystemControlManager.SetDisplayMode(mTvSourceInput.toInt(), SystemControlManager.Display_Mode.DISPLAY_MODE_MODE43, 1);
        } else if (mode == 2) {
            mSystemControlManager.SetDisplayMode(mTvSourceInput.toInt(), SystemControlManager.Display_Mode.DISPLAY_MODE_FULL, 1);
        } else if (mode == 3) {
            mSystemControlManager.SetDisplayMode(mTvSourceInput.toInt(), SystemControlManager.Display_Mode.DISPLAY_MODE_169,  1);
        }
    }

    public int getSleepTimerStatus () {
        String ret = "";
        int time = mSystemControlManager.getPropertyInt("persist.sys.tv.sleep_timer", 0);
        Log.d(TAG, "getSleepTimerStatus:" + time);
        return time;
    }

    public void setSleepTimer (int mode) {
        AlarmManager alarm = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                new Intent("droidlogic.intent.action.TIMER_SUSPEND"), 0);
        alarm.cancel(pendingIntent);

        mSystemControlManager.setProperty("persist.sys.tv.sleep_timer", mode+"");

        long timeout = 0;
        if (mode == 0) {
            return;
        } else if (mode < 5) {
            timeout = (mode * 15  - 1) * 60 * 1000;
        } else {
            timeout = ((mode - 4) * 30 + 4 * 15  - 1) * 60 * 1000;
        }

        alarm.setExact(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + timeout, pendingIntent);
        Log.d(TAG, "start time count down after " + timeout + " ms");
    }
}
