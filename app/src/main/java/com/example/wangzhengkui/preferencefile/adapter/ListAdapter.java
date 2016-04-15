package com.example.wangzhengkui.preferencefile.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.KeyValue;
import com.example.wangzhengkui.preferencefile.R;

import java.security.Key;

/**
 * @author Administrator on 2016-04-05 15:23
 */
public class ListAdapter extends BaseAdapter {
    Context mContext;
    SparseArray<KeyValue> array;
    public ListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void changeData(SparseArray<KeyValue> array) {
        if (array == null) {
            return;
        }
        this.array = array;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return array==null?0:array.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        KeyValue keyValue = array.get(position);
        holder.titleTv.setText(keyValue.getKey());
        holder.titleTv.setText(keyValue.getValue().toString());
        return convertView;
    }

    class Holder {
        TextView titleTv;
        TextView contentTv;
    }
}
