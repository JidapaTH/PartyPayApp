package com.example.admin.partypay;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.partypay.R;
import com.example.admin.partypay.MemberFile.Member;

import java.util.ArrayList;

import static android.R.attr.defaultValue;

/**
 * Ref: https://devahoy.com/posts/android-spinner-example/
 *
 */
public class CustomSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mData = new ArrayList<String>();
    private LayoutInflater mInflater;

    public CustomSpinnerAdapter(ArrayList<String> data,Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
        //mDefaultValue = defaultValue;
    }

    public CustomSpinnerAdapter(Context context, ArrayList<Member> data) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        for (int i = 0;i<data.size();i++){
            mData.add(data.get(i).get_name());
        }

        //mDefaultValue = defaultValue;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_adapter, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.spinner_name_textview);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(mData.get(position));

        convertView.setTag(holder);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_adapter, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.spinner_name_textview);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.name.setText(mData.get(position));

        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder {
        TextView name;
    }
}