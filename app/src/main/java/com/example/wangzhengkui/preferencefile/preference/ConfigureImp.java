package com.example.wangzhengkui.preferencefile.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.R;
import com.example.wangzhengkui.preferencefile.preference.manager.ConfigureManager;

import java.util.Set;

/**
 * @author Administrator on 2016-04-15 17:52
 */
public class ConfigureImp extends Preference {
    Context mContext;
    private Object mDefaultValue;
    OnPreferenceChangeInternalListener listener;
    public ConfigureImp(Context context) {
        this(context,null);
    }

    public ConfigureImp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * 代码来自于Preference.performCLick()
     */
    public void performClick() {

        if (!isEnabled()) {
            return;
        }

        onClick();

        if (getOnPreferenceClickListener() != null && getOnPreferenceClickListener().onPreferenceClick(this)) {
            return;
        }
        if (getIntent() != null) {
            Context context = getContext();
            context.startActivity(getIntent());
        }
    }

    @Override
    protected void notifyChanged() {
        super.notifyChanged();
        if (listener != null) {
            listener.onPreferenceChange(this);
        }
    }

    public OnPreferenceChangeInternalListener getListener() {
        return listener;
    }

    public ConfigureImp setListener(OnPreferenceChangeInternalListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public View onCreateView(ViewGroup parent) {
        super.onCreateView(parent);
        final LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.preference, parent, false);
    }

    @Override
    public void onBindView(View view) {
        super.onBindView(view);
        final TextView titleView = (TextView) view.findViewById(R.id.title);
        if (titleView != null) {
            final CharSequence title = getTitle();
            if (!TextUtils.isEmpty(title)) {
                titleView.setText(title);
                titleView.setVisibility(View.VISIBLE);
            } else {
                titleView.setVisibility(View.GONE);
            }
        }

        final TextView summaryView = (TextView) view.findViewById(R.id.summary);
        if (summaryView != null) {
            final CharSequence summary = getSummary();
            if (!TextUtils.isEmpty(summary)) {
                summaryView.setText(summary);
                summaryView.setVisibility(View.VISIBLE);
            } else {
                summaryView.setVisibility(View.GONE);
            }
        }

        final ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        if (imageView != null) {
            if (getIcon() != null) {
                imageView.setImageDrawable(getIcon());
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }

        if (getShouldDisableView()) {
            setEnabledStateOnViews(view, isEnabled());
        }
    }
    /**
     * Makes sure the view (and any children) get the enabled state changed.
     */
    private void setEnabledStateOnViews(View v, boolean enabled) {
        v.setEnabled(enabled);

        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(vg.getChildAt(i), enabled);
            }
        }
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

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(defaultValue);
        mDefaultValue = defaultValue;
    }

    protected void dispatchSetInitialValue() {
        final boolean shouldPersist = shouldPersist();
        if (!shouldPersist || !getSharedPreferences().contains(getKey())) {
            if (mDefaultValue != null) {
                setInitialValue(false, mDefaultValue);
            }
        } else {
            setInitialValue(true, null);
        }
    }
    public void setInitialValue(boolean restorePersistedValue, Object defaultValue) {

    }
    @Override
    public boolean persistBoolean(boolean value) {
//        return false;
        return persist(getKey(),value);
    }

    @Override
    public boolean getPersistedBoolean(boolean defaultReturnValue) {
        return super.getPersistedBoolean(defaultReturnValue);
    }

    @Override
    public boolean persistFloat(float value) {
        return persist(getKey(),value);
    }

    @Override
    public float getPersistedFloat(float defaultReturnValue) {
        return super.getPersistedFloat(defaultReturnValue);
    }

    @Override
    public boolean persistInt(int value) {
        return persist(getKey(),value);
    }

    @Override
    public int getPersistedInt(int defaultReturnValue) {
        return super.getPersistedInt(defaultReturnValue);
    }

    @Override
    public boolean persistLong(long value) {
        return persist(getKey(),value);
    }

    @Override
    public long getPersistedLong(long defaultReturnValue) {
        return super.getPersistedLong(defaultReturnValue);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return super.getPersistedString(defaultReturnValue);
    }

    @Override
    public boolean persistString(String value) {
        return persist(getKey(),value);
    }

    /**
     * Attempts to persist a set of Strings to the {@link android.content.SharedPreferences}.
     * <p>
     * This will check if this Preference is persistent, get an editor from
     * the {@link PreferenceManager}, put in the strings, and check if we should commit (and
     * commit if so).
     *
     * @param values The values to persist.
     * @return True if the Preference is persistent. (This is not whether the
     *         value was persisted, since we may not necessarily commit if there
     *         will be a batch commit later.)
     * @hide Pending API approval
     */
    protected boolean persistStringSet(Set<String> values) {

        return false;
    }

    /**
     * Attempts to get a persisted set of Strings from the
     * {@link android.content.SharedPreferences}.
     * <p>
     * This will check if this Preference is persistent, get the SharedPreferences
     * from the {@link PreferenceManager}, and get the value.
     *
     * @param defaultReturnValue The default value to return if either the
     *            Preference is not persistent or the Preference is not in the
     *            shared preferences.
     * @return The value from the SharedPreferences or the default return
     *         value.
     * @see #persistStringSet(Set)
     *
     * @hide Pending API approval
     */
    protected Set<String> getPersistedStringSet(Set<String> defaultReturnValue) {
        return null;
    }
    @Override
    public boolean shouldPersist() {
        return super.shouldPersist();
    }

    @Override
    public boolean isPersistent() {
        return super.isPersistent();
    }

    @Override
    public void setPersistent(boolean persistent) {
        super.setPersistent(persistent);
    }


    public boolean persist(String key,Object value) {
        ConfigureManager.writeCacheValue(key,value.toString());
        return true;
    }

    interface OnPreferenceChangeInternalListener {
        /**
         * Called when this Preference has changed.
         *
         * @param preference This preference.
         */
        void onPreferenceChange(Preference preference);
    }
}
