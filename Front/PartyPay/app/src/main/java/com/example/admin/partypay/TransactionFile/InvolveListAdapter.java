package com.example.admin.partypay.TransactionFile;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.admin.partypay.R;

import java.util.ArrayList;

/**
 * Created by Admin on 7/29/2017.
 */

public class InvolveListAdapter extends ArrayAdapter<InvolveList> {


    // All views in Member Activity (including TextWatcher)
    private static class ViewHolder{
        private CheckBox memberCheckBox;
    }

    public InvolveListAdapter(Activity c, ArrayList<InvolveList> membersList){
        super(c,0,membersList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_involve_add_transaction, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.memberCheckBox = (CheckBox) listItemView.findViewById(R.id.involve_checkbox);
            listItemView.setTag(viewHolder);
        }


        final InvolveList currentMember = getItem(position);
        ViewHolder vHolder = (ViewHolder) listItemView.getTag();

        vHolder.memberCheckBox.setText(currentMember.getName());
        vHolder.memberCheckBox.setChecked(currentMember.getChecked());
        vHolder.memberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentMember.setChecked(isChecked);
            }
        });

        return listItemView;
    }
}

