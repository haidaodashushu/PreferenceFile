package com.example.wangzhengkui.preferencefile.preference;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.R;

import java.util.prefs.PreferencesFactory;

/**
 * @author Administrator on 2016-04-15 20:31
 */
public class CategoryPreferenceImp extends PreferenceImp {
    Context mContext;
    public CategoryPreferenceImp(Context context) {
        this(context,null);
    }

    public CategoryPreferenceImp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View layout = super.onCreateView(parent);
        View viewById = layout.findViewById(R.id.summary);
        viewById.setVisibility(View.GONE);
        return layout;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        syncSummaryView(view);
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
