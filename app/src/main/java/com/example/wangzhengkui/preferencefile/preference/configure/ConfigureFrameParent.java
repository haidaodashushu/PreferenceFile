package com.example.wangzhengkui.preferencefile.preference.configure;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
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

    /**
     * 返回上一层，如果当前层数>1，则返回true,如果当前层数小于等于1,则返回false
     * @return
     */
    public boolean onBack() {
        if (getChildCount() > 1) {
            removeViewAt(getChildCount() - 1);
            return true;
        } else {
            return false;
        }
    }
}
