package com.example.admin.partypay.EventActivityFile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.partypay.R;
import com.example.admin.partypay.TransactionFile.AddTransactionActivity;
import com.example.admin.partypay.TransactionFile.EditTransactionActivity;
import com.example.admin.partypay.db.TranHelper;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * ExpandableListAdapter of DayTransaction object
 * This adapter is used in EventOverviewFragment
 */

public class DayTransactionAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> transDate = new ArrayList<>();
    private ArrayList<ArrayList<Showtrans>> transInfo = new ArrayList<>();

    private Context context;
    private int eventID;
    // Constructor method
    public DayTransactionAdapter(Context context, ArrayList<String> s, ArrayList<ArrayList<Showtrans>> d, int e){
        this.context = context;
        transDate = s;
        transInfo = d;
        eventID = e;
    }

    @Override
    public int getGroupCount() {
        return transDate.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = transInfo.get(groupPosition).size();
        if(count == -1){
            Log.v("DayTransactionAdapter","Error: Number of data in DayTransaction not matched");
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return transDate.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return transInfo.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //Show content in GroupView
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //Create textView, set textSize, color and padding
        TextView textView = new TextView(context);
        textView.setText(transDate.get(groupPosition));
        int valueInPixels = dp2px(8);
        textView.setPadding(0,valueInPixels,0,valueInPixels);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(22);
        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);
        // Uncomment 2 lines below to permanently expand groupView
        //ExpandableListView eLV = (ExpandableListView) parent;
        //eLV.expandGroup(groupPosition);
        return textView;
    }

    //Show content in childView
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Show View in list_event_overview
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater groupinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView = groupinflater.inflate(R.layout.list_event_overview, parent, false);
        }
        // Show transaction contents (Main)(Transaction name,amount and member's name whose responsible for transaction)
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.trans_name_text_view);
        nameTextView.setText(transInfo.get(groupPosition).get(childPosition).getTransName());

        TextView memberTextView = (TextView) listItemView.findViewById(R.id.trans_member_text_view);
//        memberTextView.setText(transInfo.get(groupPosition).get(childPosition).get);

        TextView amountTextView = (TextView) listItemView.findViewById(R.id.trans_amount_text_view);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        amountTextView.setText(transInfo.get(groupPosition).get(childPosition).getTransAmountWithSign());

        ImageView infoImageView = (ImageView) listItemView.findViewById(R.id.trans_edit_image_view);
        infoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTransactionActivity.class);
                int transID = 0;
                transID = transInfo.get(groupPosition).get(childPosition).getTransID();
                intent.putExtra("TransID", transID);
                intent.putExtra("EventID", eventID);
                context.startActivity(intent);
            }
        });

        return listItemView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //Use this to change dp unit to pixel unit
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
