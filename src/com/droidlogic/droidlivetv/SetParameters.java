package com.droidlogic.droidlivetv;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.droidlogic.app.tv.TvControlManager;
import com.droidlogic.app.SystemControlManager;
import com.droidlogic.app.tv.TvDataBaseManager;
import com.droidlogic.app.tv.DroidLogicTvUtils;
import com.droidlogic.app.tv.TVInSignalInfo;

public class SetParameters {
    private final static String TAG = "SetParameters";
    private TvControlManager mTvControlManager;
    private TvDataBaseManager mTvDataBaseManager;
    private SystemControlManager mSystemControlManager;
    private TvControlManager.SourceInput mTvSourceInput;
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
        mTvSourceInput = DroidLogicTvUtils.parseTvSourceInputFromDeviceId(mDeviceId);
    }

    public  int getPictureModeStatus () {
        int pictureModeIndex = mTvControlManager.GetPQMode(mTvSourceInput);
        Log.d(TAG, "getPictureModeStatus:" + pictureModeIndex);
        return pictureModeIndex;
    }

    public void setPictureMode (int mode) {
        Log.d(TAG, "setPictureMode:" + mode);
        if (mode == 0) {
            mTvControlManager.SetPQMode(TvControlManager.PQMode.PQ_MODE_STANDARD, mTvSourceInput, 1);
        } else if (mode == 1) {
            mTvControlManager.SetPQMode(TvControlManager.PQMode.PQ_MODE_BRIGHT, mTvSourceInput, 1);
        } else if (mode == 2) {
            mTvControlManager.SetPQMode(TvControlManager.PQMode.PQ_MODE_SOFTNESS, mTvSourceInput, 1);
        } else if (mode == 3) {
            mTvControlManager.SetPQMode(TvControlManager.PQMode.PQ_MODE_USER, mTvSourceInput, 1);
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
        int itemPosition = mTvControlManager.GetDisplayMode(mTvSourceInput);
        Log.d(TAG, "getAspectRatioStatus:" + itemPosition);
        if (itemPosition == TvControlManager.Display_Mode.DISPLAY_MODE_169.toInt())
            return 0;
        else if (itemPosition == TvControlManager.Display_Mode.DISPLAY_MODE_MODE43.toInt())
            return 1;
        else if (itemPosition == TvControlManager.Display_Mode.DISPLAY_MODE_FULL.toInt())
            return 2;
        else
            return 3;
    }

    public void setAspectRatio(int mode) {
        Log.d(TAG, "setAspectRatio:" + mode);
        if (mode == 0) {
            mTvControlManager.SetDisplayMode(TvControlManager.Display_Mode.DISPLAY_MODE_169,
                    mTvSourceInput, TVInSignalInfo.SignalFmt.TVIN_SIG_FMT_NULL/*mTvControlManager.GetCurrentSignalInfo().sigFmt*/, 1);
        } else if (mode == 1) {
            mTvControlManager.SetDisplayMode(TvControlManager.Display_Mode.DISPLAY_MODE_MODE43,
                    mTvSourceInput, TVInSignalInfo.SignalFmt.TVIN_SIG_FMT_NULL/*mTvControlManager.GetCurrentSignalInfo().sigFmt*/, 1);
        } else if (mode == 2) {
            mTvControlManager.SetDisplayMode(TvControlManager.Display_Mode.DISPLAY_MODE_FULL,
                    mTvSourceInput, TVInSignalInfo.SignalFmt.TVIN_SIG_FMT_NULL/*mTvControlManager.GetCurrentSignalInfo().sigFmt*/, 1);
        } else if (mode == 3) {
            mTvControlManager.SetDisplayMode(TvControlManager.Display_Mode.DISPLAY_MODE_NORMAL,
                    mTvSourceInput, TVInSignalInfo.SignalFmt.TVIN_SIG_FMT_NULL/*mTvControlManager.GetCurrentSignalInfo().sigFmt*/, 1);
        }
    }

    public int getSleepTimerStatus () {
        String ret = "";
        int time = mSystemControlManager.getPropertyInt("tv.sleep_timer", 0);
        Log.d(TAG, "getSleepTimerStatus:" + time);
        return time;
    }

    public void setSleepTimer (int mins) {
        mSystemControlManager.setProperty("tv.sleep_timer", mins+"");
        Log.d(TAG, "setSleepTimer:" + mins);
        //Intent intent = new Intent(DroidLogicTvUtils.ACTION_TIMEOUT_SUSPEND);
        //mContext.sendBroadcast(intent);//to tvapp
        mContext.startService ( new Intent ( mContext, TimeSuspendService.class ) );
    }
}