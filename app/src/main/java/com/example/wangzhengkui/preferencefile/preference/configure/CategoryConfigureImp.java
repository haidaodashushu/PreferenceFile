package com.example.wangzhengkui.preferencefile.preference.configure;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.R;

/**
 * @author Administrator on 2016-04-15 20:31
 */
public class CategoryConfigureImp extends ConfigureImp {
    Context mContext;
    public CategoryConfigureImp(Context context) {
        this(context,null);
    }

    public CategoryConfigureImp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        View layout = LayoutInflater.from(mContext).inflate(R.layout.preference_widget_category,null);
        layout.setClickable(false);
        layout.setEnabled(false);

        setShouldDisableView(false);
        return layout;
    }

    @Override
    public void onBindView(View view) {
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
