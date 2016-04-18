package com.example.wangzhengkui.preferencefile.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.example.wangzhengkui.preferencefile.R;

/**
 * @author Administrator on 2016-04-15 11:45
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class SwitchConfigureImp extends ConfigureImp {
    // Switch text for on and off states
    Context mContext;
    boolean mChecked;
    private boolean mCheckedSet;

    /**
     * Construct a new SwitchPreference with the given style options.
     *
     * @param context The Context that will style this preference
     * @param attrs Style attributes that differ from the default
     */
    public SwitchConfigureImp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * Construct a new SwitchPreference with default style options.
     *
     * @param context The Context that will style this preference
     */
    public SwitchConfigureImp(Context context) {
        this(context, null);
    }

    @Override
    protected void onClick() {
        super.onClick();
        final boolean newValue = !isChecked();
//        if (callChangeListener(newValue)) {
            setChecked(newValue);
//        }
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        final LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View layout = layoutInflater.inflate(R.layout.preference, parent, false);

        final ViewGroup widgetFrame = (ViewGroup) layout
                .findViewById(R.id.widget_frame);
        layoutInflater.inflate(R.layout.preference_widget_switch,widgetFrame);

        return layout;

    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        View checkableView = view.findViewById(R.id.switchWidget);
        if (checkableView != null && checkableView instanceof Checkable) {
            ((Checkable) checkableView).setChecked(isChecked());
        }
        syncSummaryView(view);
    }

    /**
     * Sets the checked state and saves it to the {@link SharedPreferences}.
     *
     * @param checked The checked state.
     */
    public void setChecked(boolean checked) {
        // Always persist/notify the first time; don't assume the field's default of false.
        final boolean changed = mChecked != checked;
        if (changed || !mCheckedSet) {
            mChecked = checked;
            mCheckedSet = true;
            persistBoolean(checked);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    /**
     * Returns the checked state.
     *
     * @return The checked state.
     */
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setInitialValue(Object value) {
        setChecked(Boolean.parseBoolean(value.toString()));
    }
}
