package com.example.wangzhengkui.preferencefile;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.InflateException;
import android.widget.BaseAdapter;

import com.example.wangzhengkui.preferencefile.preference.configure.CategoryConfigureImp;
import com.example.wangzhengkui.preferencefile.preference.configure.ConfigureImp;
import com.example.wangzhengkui.preferencefile.preference.configure.MultiSelectListConfigureImp;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureListEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureType;
import com.example.wangzhengkui.preferencefile.preference.manager.ConfigureManager;
import com.example.wangzhengkui.preferencefile.preference.configure.EditTextConfigureImp;
import com.example.wangzhengkui.preferencefile.preference.configure.ListConfigureImp;
import com.example.wangzhengkui.preferencefile.preference.configure.ScreenConfigureImp;
import com.example.wangzhengkui.preferencefile.preference.configure.SwitchConfigureImp;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

public class ConfigureActivity extends PreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            Log.i("ConfigureActivity","Preference = "+preference.getClass());
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
                Intent intent = new Intent();
                intent.putExtra("key",preference.getKey());
                intent.setClass(ConfigureActivity.this,ConfigureActivity.class);
                startActivity(intent);
            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };
    SparseArray<Preference> preferences = new SparseArray<>(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.my_pref_pref);
        String key = getIntent().getStringExtra("key");
        ConfigureScreenEntity entity = null;
        if (key == null) {
            entity = ConfigureManager.getFirstScreen();
        } else {
            entity = (ConfigureScreenEntity) ConfigureManager.getScreen(key);
        }
        SparseArray<Preference> child = initData(entity);
        preferences.clear();
        for (int i = 0; i < child.size(); i++) {
            preferences.append(i, child.valueAt(i));
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public SparseArray<Preference> initData(ConfigureScreenEntity entity) {
        SparseArray<Preference> preferences = new SparseArray<>(0);
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
                    configureImp = new ScreenConfigureImp(this);
                    break;
                case ConfigureType.CATEGORY:
                    configureImp = new CategoryConfigureImp(this);
                    break;
                case ConfigureType.LIST:
                    configureImp = new ListConfigureImp(this);
                    ((ListConfigureImp)configureImp).setEntries(((ConfigureListEntity) childEntity).getEntry());
                    ((ListConfigureImp)configureImp).setEntryValues(((ConfigureListEntity) childEntity).getEntry());
                    break;
                case ConfigureType.MULTILIST:
                    configureImp = new MultiSelectListConfigureImp(this);
                    ((MultiSelectListConfigureImp)configureImp).setEntries(((ConfigureListEntity) childEntity).getEntry());
                    ((MultiSelectListConfigureImp)configureImp).setEntryValues(((ConfigureListEntity) childEntity).getEntry());
                    break;
                case ConfigureType.SWITCH:
                    configureImp = new SwitchConfigureImp(this);
                    break;
                case ConfigureType.EDITOR:
                    configureImp = new EditTextConfigureImp(this);
                    ((EditTextConfigureImp)configureImp).getEditText().setSelectAllOnFocus(true);
                    break;
            }

//            configureImp.setListener(this);
            configureImp.setValue(childEntity.getValue());
            configureImp.setKey(childEntity.getKey());
            configureImp.setTitle(childEntity.getTitle());
            configureImp.setSummary(childEntity.getSummary());
            configureImp.setDefaultValue(childEntity.getDefaultValue());
            configureImp.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
            configureImp.setOrder(i);

            preferences.append(i, configureImp);
//            preferences.add(configureImp);

        }
        return preferences;
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < preferences.size(); i++) {
            getPreferenceScreen().addItemFromInflater(preferences.get(i));
        }
        ((BaseAdapter) getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
    }

    /**
     * Low-level function for instantiating by name. This attempts to
     * instantiate class of the given <var>name</var> found in this
     * inflater's ClassLoader.
     * <p/>
     * <p/>
     * There are two things that can happen in an error case: either the
     * exception describing the error will be thrown, or a null will be
     * returned. You must deal with both possibilities -- the former will happen
     * the first time createItem() is called for a class of a particular name,
     * the latter every time there-after for that class name.
     *
     * @param name  The full name of the class to be instantiated.
     * @param attrs The XML attributes supplied for this instance.
     * @return The newly instantied item, or null.
     */
    public final PreferenceScreen createPreferenceScreen(String name, String prefix, Context context,AttributeSet attrs){
        Class[] mConstructorSignature = new Class[]{
                Context.class, AttributeSet.class};

        final Object[] mConstructorArgs = new Object[2];
        try {
            Constructor constructor = null;
            Class clazz = getClassLoader().loadClass(
                    prefix != null ? (prefix + name) : name);
            constructor = clazz.getConstructor(mConstructorSignature);
            constructor.setAccessible(true);
            Object[] args = mConstructorArgs;
            args[0] = context;
            args[1] = attrs;
            return (PreferenceScreen) constructor.newInstance(args);

        } catch (NoSuchMethodException e) {
            InflateException ie = new InflateException(attrs
                    .getPositionDescription()
                    + ": Error inflating class "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;

        } catch (ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
        } catch (Exception e) {
            InflateException ie = new InflateException(attrs
                    .getPositionDescription()
                    + ": Error inflating class "
                    + name);
            ie.initCause(e);
        }
        return null;
    }
}
