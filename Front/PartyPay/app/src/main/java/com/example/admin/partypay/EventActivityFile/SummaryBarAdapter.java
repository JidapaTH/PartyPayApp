package com.example.admin.partypay.EventActivityFile;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.admin.partypay.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * ExpandableListAdapter of SummaryBar object
 * This adapter is used in EventSummaryFragment
 */

public class SummaryBarAdapter extends ArrayAdapter<SummaryBar> {

    //Constructor method
    public SummaryBarAdapter(Activity context, ArrayList<SummaryBar> summaryBar){
        super(context,0, summaryBar);
    }

    //Overide this method to show views on the ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_event_summary, parent, false);
        }

        final SummaryBar currentSummary = getItem(position);

        //Import User's name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.summary_name);
        nameTextView.setText(currentSummary.getMemberName());


        //Import User's amount of debt/credits
        TextView amountTextView = (TextView) listItemView.findViewById(R.id.summary_amount);
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        amountTextView.setText(numberFormat.format(currentSummary.getPendingAmount()));
        TextView currencyTextView = (TextView) listItemView.findViewById(R.id.list_view_currency);
        if(currentSummary.getPendingAmount()>0){
            amountTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.creditColor));
            currencyTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.creditColor));
        } else {
            amountTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.debtColor));
            currencyTextView.setTextColor(ContextCompat.getColor(getContext(),R.color.debtColor));
        }
        return listItemView;
    }
}