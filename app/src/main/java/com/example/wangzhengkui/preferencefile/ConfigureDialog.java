package com.example.wangzhengkui.preferencefile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.wangzhengkui.preferencefile.preference.adapter.ConfigureAdapter;
import com.example.wangzhengkui.preferencefile.preference.entity.ConfigureScreenEntity;
import com.example.wangzhengkui.preferencefile.preference.ConfigureFrameParent;
import com.example.wangzhengkui.preferencefile.preference.ConfigureImp;

import java.util.LinkedList;

/**
 * @author WangZhengkui on 2016-04-17 19:40
 */
public class ConfigureDialog extends Dialog implements AdapterView.OnItemClickListener,DialogInterface.OnDismissListener{
    Context mContext;
    ListView mListView;
    private ConfigureAdapter mRootAdapter;

    LinkedList<ConfigureImp> preferences = new LinkedList<>();

    public ConfigureDialog(Context context) {
        this(context,R.style.ConfigureDialog_Transparent_FullScreen);
    }

    public ConfigureDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

        ConfigureFrameParent frame  = new ConfigureFrameParent(mContext);
        frame.getConfigure(null);

        final CharSequence title = frame.getCurrentScreen().getScreenEntity().getTitle();
        if (TextUtils.isEmpty(title)) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        } else {
            setTitle(title);
        }
        setContentView(frame);
        setOnDismissListener(this);

    }


    public void initDialog(Bundle state,ConfigureScreenEntity preferenceScreen) {
        if (mListView != null) {
            mListView.setAdapter(null);
        }

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childPrefScreen = inflater.inflate(
                R.layout.preference_list_fragment, null);
        mListView = (ListView) childPrefScreen.findViewById(R.id.list);

        bind(mListView);
// Set the title bar if title is available, else no title bar
        final CharSequence title = preferenceScreen.getTitle();
        if (TextUtils.isEmpty(title)) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        } else {
            setTitle(title);
        }
        setContentView(childPrefScreen);
        setOnDismissListener(this);
        if (state != null) {
            onRestoreInstanceState(state);
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
        ConfigureAdapter rootAdapter = getRootAdapter();
        listView.setAdapter(rootAdapter);
        rootAdapter.changeData(preferences);

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
}
