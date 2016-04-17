package com.example.wangzhengkui.preferencefile.preference.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wangzhengkui.preferencefile.preference.ConfigureImp;

/**
 * @author Administrator on 2016-04-05 15:23
 */
public class ConfigureAdapter extends BaseAdapter {
    Context mContext;
    SparseArray<ConfigureImp> date;
    public ConfigureAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void changeData(SparseArray<ConfigureImp> array) {
        if (array == null) {
            return;
        }
        this.date = array;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return date ==null?0: date.size();
    }

    @Override
    public Object getItem(int position) {
        return date.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.contentTv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }*/

        ConfigureImp preferenceImp = date.get(position);
        View view = preferenceImp.getView(convertView,parent);
        return view;
    }

    class Holder {
        TextView titleTv;
        TextView contentTv;
    }
}
