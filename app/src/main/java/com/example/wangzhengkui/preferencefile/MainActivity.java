package com.example.wangzhengkui.preferencefile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.example.wangzhengkui.preferencefile.preference.ConfigureFrameParent;

public class MainActivity extends AppCompatActivity {
//    ListView listView;
//    ConfigureAdapter adapter;
//    SparseArray<KeyValue> array;
    Switch switchId;
    FrameLayout frameLv;
    ConfigureFrameParent configure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        listView = (ListView) findViewById(R.id.listView);
//        adapter = new ConfigureAdapter(this);
//        listView.setAdapter(adapter);
        frameLv = (FrameLayout)findViewById(R.id.frameLv);
        switchId = (Switch)findViewById(R.id.switchId);
        switchId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                configure = new ConfigureFrameParent(MainActivity.this).getConfigure(null);
                frameLv.addView(configure);
                String title = configure.getCurrentScreen().getScreenEntity().getTitle();
                if (title != null) {
                    setTitle(title);
                }
            }
        });;
//        Object object = ConfigureManager.getValue(Constants.apn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (configure != null) {
            if (!configure.onBack()) {
                frameLv.removeAllViews();
                configure=null;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
