package com.example.wangzhengkui.preferencefile;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.BaseAdapter;

import com.example.wangzhengkui.preferencefile.entity.ConfigureEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureListEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.entity.ConfigureType;
import com.example.wangzhengkui.preferencefile.manager.ConfigureManager;
import com.example.wangzhengkui.preferencefile.view.MyPreference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConfigureActivity extends PreferenceActivity {
    private static final Class[] mConstructorSignature = new Class[] {
            Context.class, AttributeSet.class};

    private final Object[] mConstructorArgs = new Object[2];
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
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

        ConfigureScreenEntity entity = ConfigureManager.getFirstScreen();
        SparseArray<Preference> child = initData(entity);
        preferences.clear();
        for (int i = 0; i < child.size(); i++) {
            preferences.append(i,child.valueAt(i));
        }
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public SparseArray<Preference> initData(ConfigureScreenEntity entity) {
        SparseArray<Preference> preferences = new SparseArray<>(0);
        if (entity == null) {
            return preferences;
        }
        LinkedList<ConfigureEntity> itemList = entity.getItemList();
        int length = itemList==null?0:itemList.size();
        for (int i = 0; i < length; i++) {
            ConfigureEntity childEntity = itemList.get(i);
            switch (childEntity.getType()) {
                case ConfigureType.SCREEN:
                    break;
                case ConfigureType.CATEGORY:
                    break;
                case ConfigureType.LIST:
                    ListPreference listPreference = new ListPreference(this);
                    listPreference.setEntries(((ConfigureListEntity)childEntity).getEntry());
                    listPreference.setEntryValues(((ConfigureListEntity)childEntity).getEntry());
                    listPreference.setTitle(childEntity.getTitle());
                    listPreference.setSummary(childEntity.getSummary());
                    listPreference.setOrder(i);
                    preferences.append(i,listPreference);
                    break;
                case ConfigureType.MULTILIST:
                    break;
                case ConfigureType.SWITCH:
                    SwitchPreference switchPreference = new SwitchPreference(this);
                    switchPreference.setKey(childEntity.getKey());
                    switchPreference.setTitle(childEntity.getTitle());
                    switchPreference.setSummary(childEntity.getSummary());
                    switchPreference.setDefaultValue(childEntity.getDefaultValue());
                    switchPreference.setSwitchTextOff("已打开开关");
                    switchPreference.setSwitchTextOn("已关闭开关");
                    switchPreference.setOrder(i);
                    preferences.append(i,switchPreference);
                    break;
                case ConfigureType.EDITOR:
                    break;
            }

        }
        return preferences;
    }
    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < preferences.size(); i++) {
            getPreferenceScreen().addItemFromInflater(preferences.get(i));
        }
        ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
    }
}
