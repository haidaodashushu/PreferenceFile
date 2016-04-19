package com.example.wangzhengkui.preferencefile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.wangzhengkui.preferencefile.preference.configure.ConfigureFrameParent;

public class MainActivity extends AppCompatActivity {
//    ListView listView;
//    ConfigureAdapter adapter;
//    SparseArray<KeyValue> array;
    Button switchId;
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
        switchId = (Button) findViewById(R.id.switchId);
        switchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configure = new ConfigureFrameParent(MainActivity.this).getConfigure(null);
                frameLv.addView(configure);
                String title = configure.getCurrentScreen().getScreenEntity().getTitle();
                if (title != null) {
                    setTitle(title);
                }
            }
        });
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
