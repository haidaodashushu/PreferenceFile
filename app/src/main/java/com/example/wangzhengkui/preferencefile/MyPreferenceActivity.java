package com.example.wangzhengkui.preferencefile;

import android.app.Activity;
import android.os.Bundle;

public class MyPreferenceActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLa(this));
    }
}
