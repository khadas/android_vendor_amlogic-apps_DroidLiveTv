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

public class DroidLiveTvActivity extends Activity {
    private final static String TAG = "DroidLiveTvActivity";
    private SideFragmentManager mSideFragmentManager;
    private OverlayRootView mOverlayRootView;
    private Context mContext;
    
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
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown(" + keyCode + ", " + event + ")");
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //mSideFragmentManager.popSideFragment();
                finish();
                return true;
            default:
                // pass through
        }
        return super.onKeyDown(keyCode, event);
    }
	
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
