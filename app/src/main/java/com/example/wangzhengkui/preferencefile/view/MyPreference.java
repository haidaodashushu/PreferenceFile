package com.example.wangzhengkui.preferencefile.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.KeyValue;
import com.example.wangzhengkui.preferencefile.R;

/**
 * @author Administrator on 2016-04-05 19:26
 */
public class MyPreference extends Preference {
    Context mContext;
    SparseArray<KeyValue> array;
    TextView keyTv, valueTv;
    KeyValue keyValue;
    public MyPreference(Context context) {
        this(context, null);
    }

    public MyPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setLayoutResource(R.layout.key_value_preference);

    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }


    public KeyValue getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(KeyValue keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        keyTv = (TextView) view.findViewById(R.id.keyTv);
        valueTv = (TextView) view.findViewById(R.id.valueTv);
        if (keyValue != null) {
            keyTv.setText("key-" + keyValue.getKey());
            if (keyValue.getValue() != null) {
                valueTv.setText(keyValue.getValue().toString());
            }
        }

    }
}
