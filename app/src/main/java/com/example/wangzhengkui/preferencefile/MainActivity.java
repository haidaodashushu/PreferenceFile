package com.example.wangzhengkui.preferencefile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ListView;

import com.example.wangzhengkui.preferencefile.adapter.ListAdapter;
import com.example.wangzhengkui.preferencefile.entity.Constants;
import com.example.wangzhengkui.preferencefile.manager.ConfigureManager;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ListAdapter adapter;
    SparseArray<KeyValue> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListAdapter(this);
        listView.setAdapter(adapter);

        Object object = ConfigureManager.getValue(Constants.apn);
    }


}
