package com.example.wangzhengkui.preferencefile.preference;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

/**
 * @author WangZhengkui on 2016-04-17 19:40
 */
public class ConfigureFrameParent extends FrameLayout{
    Context mContext;

    public ConfigureFrameParent(Context context) {
        this(context, null);
    }

    public ConfigureFrameParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setBackgroundColor(Color.parseColor("#fafafa"));
    }

    public ConfigureFrameParent getConfigure(String key) {
        ConfigureFrame configureFrame = new ConfigureFrame(mContext,this);
        configureFrame.getConfigure(key);
        this.addView(configureFrame, -1, -1);
        return this;
    }

    public ConfigureFrame getCurrentScreen() {
        if (getChildCount() >= 1) {
            return (ConfigureFrame) getChildAt(getChildCount() - 1);
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("ConfigureFrameParent", "onKeyDown = " + event.getKeyCode());
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getChildCount() >= 1) {
                this.removeViewAt(getChildCount()-1);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
