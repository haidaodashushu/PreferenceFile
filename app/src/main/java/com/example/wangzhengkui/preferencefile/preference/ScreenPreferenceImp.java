package com.example.wangzhengkui.preferencefile.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.R;

/**
 * @author Administrator on 2016-04-15 17:02
 */
public class ScreenPreferenceImp extends PreferenceImp {
    Context mContext;
    public ScreenPreferenceImp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public ScreenPreferenceImp(Context context) {
        this(context,null);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View layout = super.onCreateView(parent);
        final LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup widgetFrame = (ViewGroup) layout
                .findViewById(R.id.widget_frame);
        layoutInflater.inflate(R.layout.preference_widget_screen,widgetFrame);
        return layout;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        syncSummaryView(view);
    }

    @Override
    protected void onClick() {
        super.onClick();
        callChangeListener(getKey());
    }

    /**
     * Sync a summary view contained within view's subhierarchy with the correct summary text.
     * @param view View where a summary should be located
     */
    void syncSummaryView(View view) {
        // Sync the summary view
        TextView summaryView = (TextView) view.findViewById(R.id.summary);
        if (summaryView != null) {
            CharSequence summary = getSummary();
            int newVisibility = View.VISIBLE;
            if (summary == null || "".equals(summary.toString().trim())) {
                newVisibility = View.GONE;
            }
            if (newVisibility != summaryView.getVisibility()) {
                summaryView.setVisibility(newVisibility);
            }
        }
    }
}
