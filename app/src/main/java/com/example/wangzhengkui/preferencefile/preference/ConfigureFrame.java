package com.example.wangzhengkui.preferencefile.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.wangzhengkui.preferencefile.R;
import com.example.wangzhengkui.preferencefile.SlideConfig;
import com.example.wangzhengkui.preferencefile.SlideFrame;
import com.example.wangzhengkui.preferencefile.preference.adapter.ConfigureAdapter;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureListEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureType;
import com.example.wangzhengkui.preferencefile.preference.manager.ConfigureManager;

import java.util.LinkedList;

/**
 * @author WangZhengkui on 2016-04-17 19:40
 */
public class ConfigureFrame extends FrameLayout implements
        AdapterView.OnItemClickListener,DialogInterface.OnDismissListener,ConfigureImp.OnPreferenceChangeInternalListener{
    Context mContext;
    ListView mListView;
    private ConfigureScreenEntity entity;
    ConfigureFrameParent configureFrameParent;
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Log.i("ConfigureActivity", "Preference = " + preference.getClass());
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof ScreenConfigureImp) {
                ConfigureFrame contentView = new ConfigureFrame(mContext,configureFrameParent);
                contentView.getConfigure(preference.getKey());
                contentView.setId(R.id.slide_content);

                final SlideConfig config = new SlideConfig.Builder()
                        .setSensitivity(1)
                        .setEdgeDirect(1<<0)
                        .setEdgeOnly(false)
                        .setDistanceForRelease(0.4f)
                        .setMinVelocity(2000)
                        .setOnFrameSlideListener(new SlideFrame.OnSimpleFrameSlideListener(){
                            @Override
                            public void onOpened() {
                                configureFrameParent.onBack();
                            }
                        }).build();

                SlideFrame slideFrame = new SlideFrame(mContext,config,contentView);
                slideFrame.setId(R.id.slide_frame);

                AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                alphaAnimation.setDuration(300);
                slideFrame.setAnimation(alphaAnimation);
                alphaAnimation.start();


                configureFrameParent.addView(slideFrame);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    SparseArray<ConfigureImp> preferences = new SparseArray<>(0);
    private ConfigureScreenEntity screenEntity;
    private ConfigureAdapter mRootAdapter;

    public ConfigureFrame(Context context,ConfigureFrameParent parent) {
        this(context,parent,null);
    }

    public ConfigureFrame(Context context, ConfigureFrameParent parent,AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.configureFrameParent = parent;
        setBackgroundColor(Color.parseColor("#fafafa"));
        requestFocus();
        setFocusable(true);
    }

    /**
     * @param key
     * @return
     */
    ConfigureFrame getConfigure(String key) {
        screenEntity = null;
        if (key == null) {
            screenEntity = ConfigureManager.getFirstScreen();
        } else {
            screenEntity = (ConfigureScreenEntity) ConfigureManager.getScreen(key);
        }
        SparseArray<ConfigureImp> child = initData(screenEntity);
        preferences.clear();
        for (int i = 0; i < child.size(); i++) {
            preferences.append(i, child.valueAt(i));
        }
        getListView(null, screenEntity);
        return this;
    }

    public ConfigureScreenEntity getScreenEntity() {
        return screenEntity;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public SparseArray<ConfigureImp> initData(ConfigureScreenEntity entity) {
        SparseArray<ConfigureImp> preferences = new SparseArray<>(0);
        if (entity == null) {
            return preferences;
        }
        LinkedList<ConfigureEntity> itemList = entity.getItemList();
        int length = itemList == null ? 0 : itemList.size();
        for (int i = 0; i < length; i++) {
            ConfigureEntity childEntity = itemList.get(i);
            ConfigureImp configureImp = null;
            switch (childEntity.getType()) {
                case ConfigureType.SCREEN:
                    configureImp = new ScreenConfigureImp(mContext);
                    break;
                case ConfigureType.CATEGORY:
                    configureImp = new CategoryConfigureImp(mContext);
                    break;
                case ConfigureType.LIST:
                    configureImp = new ListConfigureImp(mContext);
                    ((ListConfigureImp)configureImp).setEntries(((ConfigureListEntity) childEntity).getEntry());
                    ((ListConfigureImp)configureImp).setEntryValues(((ConfigureListEntity) childEntity).getEntry());
                    break;
                case ConfigureType.MULTILIST:
                    configureImp = new MultiSelectListConfigureImp(mContext);
                    ((MultiSelectListConfigureImp)configureImp).setEntries(((ConfigureListEntity) childEntity).getEntry());
                    ((MultiSelectListConfigureImp)configureImp).setEntryValues(((ConfigureListEntity) childEntity).getEntry());
                    break;
                case ConfigureType.SWITCH:
                    configureImp = new SwitchConfigureImp(mContext);
                    break;
                case ConfigureType.EDITOR:
                    configureImp = new EditTextConfigureImp(mContext);
                    ((EditTextConfigureImp)configureImp).getEditText().setSelectAllOnFocus(true);
                    break;
            }

            configureImp.setListener(this);
            configureImp.setValue(childEntity.getValue());
            configureImp.setKey(childEntity.getKey());
            configureImp.setTitle(childEntity.getTitle());
            configureImp.setSummary(childEntity.getSummary());
            configureImp.setDefaultValue(childEntity.getDefaultValue());
            configureImp.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
            configureImp.setOrder(i);

            preferences.append(i, configureImp);

        }
        return preferences;
    }



    public void getListView(Bundle state, ConfigureScreenEntity preferenceScreen) {
        if (mListView != null) {
            mListView.setAdapter(null);
        }

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childPrefScreen = inflater.inflate(
                R.layout.preference_list_fragment, null);
        mListView = (ListView) childPrefScreen.findViewById(R.id.list);
        bind(mListView);
        addView(mListView,-1,-1);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < preferences.size(); i++) {
            preferences.get(i).dispatchSetInitialValue();
        }
    }

    /**
     * Binds a {@link ListView} to the preferences contained in this {@link PreferenceScreen} via
     * {@link #getRootAdapter()}. It also handles passing list item clicks to the corresponding
     * {@link Preference} contained by this {@link PreferenceScreen}.
     *
     * @param listView The list view to attach to.
     */
    public void bind(ListView listView) {
        listView.setOnItemClickListener(this);
        if (mRootAdapter == null) {
            mRootAdapter = getRootAdapter();
            listView.setAdapter(mRootAdapter);
        }
        mRootAdapter.changeData(preferences);

//        onAttachedToActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // If the list has headers, subtract them from the index.
        if (parent instanceof ListView) {
            position -= ((ListView) parent).getHeaderViewsCount();
        }
        Object item = getRootAdapter().getItem(position);
        if (!(item instanceof ConfigureImp)) return;

        final ConfigureImp preference = (ConfigureImp) item;
        preference.performClick();
    }


    public ConfigureAdapter getRootAdapter() {
        if (mRootAdapter == null) {
            mRootAdapter = onCreateRootAdapter();
        }

        return mRootAdapter;
    }

    protected ConfigureAdapter onCreateRootAdapter() {
        return new ConfigureAdapter(mContext) {
        };
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onPreferenceChange(Preference preference) {
        if (mRootAdapter!=null) {
            mRootAdapter.notifyDataSetChanged();
        }
    }
}
