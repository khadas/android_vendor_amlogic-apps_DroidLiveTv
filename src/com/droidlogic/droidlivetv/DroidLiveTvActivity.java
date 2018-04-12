package com.droidlogic.droidlivetv;

import com.droidlogic.droidlivetv.ui.MultiOptionFragment;
import com.droidlogic.droidlivetv.ui.OverlayRootView;
import com.droidlogic.droidlivetv.ui.SideFragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

public class DroidLiveTvActivity extends Activity {
    private final static String TAG = "DroidLiveTvActivity";
    private SideFragmentManager mSideFragmentManager;
    private OverlayRootView mOverlayRootView;
    private Context mContext;
    private static final String KEY_MENU_TIME = "menu_time";
    private static final int DEFUALT_MENU_TIME = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Intent intent = getIntent();
        Bundle bundle= intent.getExtras();
        if (bundle != null) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_droid_live_tv);
            mOverlayRootView = (OverlayRootView) getLayoutInflater().inflate(R.layout.overlay_root_view, null, false);
            mSideFragmentManager = new SideFragmentManager(this);
            Display display = getWindowManager().getDefaultDisplay();

            int keyvalue = bundle.getInt("eventkey");
            int deviceid = bundle.getInt("deviceid");
            Log.d(TAG, "GETKEY: " + keyvalue);
            mSideFragmentManager.show(new MultiOptionFragment(bundle, mContext));
            startShowActivityTimer();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown(" + keyCode + ", " + event + ")");
        startShowActivityTimer();
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //mSideFragmentManager.popSideFragment();
                finish();
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                startShowActivityTimer();
                break;
            default:
                // pass through
        }
        return super.onKeyDown(keyCode, event);
    }

    private void startShowActivityTimer () {
        handler.removeMessages(0);
        int seconds = Settings.System.getInt(getContentResolver(), KEY_MENU_TIME, DEFUALT_MENU_TIME);
        if (seconds == 0) {
            seconds = 10;
        } else if (seconds == 1) {
            seconds = 20;
        } else if (seconds == 2) {
            seconds = 30;
        } else if (seconds == 3) {
            seconds = 60;
        }
        Log.d(TAG, "[startShowActivityTimer] seconds = " + seconds);
        handler.sendEmptyMessageDelayed(0, seconds * 1000);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            finish();
        }
    };

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL, 0, PixelFormat.TRANSPARENT);
        windowParams.token = getWindow().getDecorView().getWindowToken();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).addView(mOverlayRootView,
                windowParams);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).removeView(mOverlayRootView);
    }

    @Override
    public View findViewById(int id) {
        // In order to locate fragments in non-application window, we should override findViewById.
        // Internally, Activity.findViewById is called to attach a view of a fragment into its
        // container. Without the override, we'll get crash during the fragment attachment.
        View v = mOverlayRootView != null ? mOverlayRootView.findViewById(id) : null;
        return v == null ? super.findViewById(id) : v;
    }

    public SideFragmentManager getSideFragmentManager() {
        return mSideFragmentManager;
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
