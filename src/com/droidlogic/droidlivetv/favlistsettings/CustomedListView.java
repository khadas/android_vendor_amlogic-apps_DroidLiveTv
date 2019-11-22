package com.droidlogic.droidlivetv.favlistsettings;


import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ListView;

public abstract class CustomedListView extends ListView {
    private static final String TAG = CustomedListView.class.getSimpleName();

    public static final String SORT_KEY_LIST = "sort_key_list";
    public static final String SORT_KEY_CONTENT_LIST = "sort_key_content_list";
    public static final String SORT_ALL_LIST = "sort_all_list";
    public static final String SORT_FAV_LIST = "sort_fav_list";

    public static final String KEY_ACTION_CODE = "action_keycode";
    public static final String KEY_LIST_TYPE = "listview_type";

    private String mName;

    public CustomedListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, String name) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mName = name;
    }

    public String getCustomedTile() {
        return mName;
    }

    public interface KeyEventListener {
        void onKeyEventCallbak(Bundle data);
    }
}
