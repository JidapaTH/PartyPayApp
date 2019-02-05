package com.example.admin.partypay.EventActivityFile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.partypay.R;
import com.example.admin.partypay.db.Overview;
import com.example.admin.partypay.db.TranHelper;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Summary event contains detailed information of members' debt and credit
 * This activity use 2 ListView
 */
public class EventSummaryFragment extends Fragment {

    int eventID;

    public EventSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_summary, container, false);

        // Receive necessary data from MainActivity
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle!= null){
            eventID = bundle.getInt("EventID");
            Log.v("EventSummaryFragment", eventID +"");
        }

        //Create two array of EventBar object (check in SummaryBar.java)
//        final ArrayList<SummaryBar> debtSummary = new ArrayList<SummaryBar>();
//        final ArrayList<SummaryBar> creditSummary = new ArrayList<SummaryBar>();

        TranHelper th = new TranHelper(getActivity());
        Sumary sumary = new Sumary(eventID,getActivity(),getActivity());
        Sumary_obj sumary_obj = sumary.sumary_result();
        ArrayList<SummaryBar> debtor = sumary_obj.print_deptor();
        ArrayList<SummaryBar> creditor = sumary_obj.print_creditor();


//        debtSummary.add(new SummaryBar("User1",-1234.567));
//        debtSummary.add(new SummaryBar("Dummy data",-987654321));
//        debtSummary.add(new SummaryBar("Testing",-5.55));
//        debtSummary.add(new SummaryBar("Dummy data",-987654321));
//        debtSummary.add(new SummaryBar("User1",-1234.567));
//        debtSummary.add(new SummaryBar("Dummy data",-987654321));
//
//        creditSummary.add(new SummaryBar("User1",1234.567));
//        creditSummary.add(new SummaryBar("Dummy data",987654321));
//        creditSummary.add(new SummaryBar("Testing",5.55));
//        creditSummary.add(new SummaryBar("Dummy data",987654321));
//        creditSummary.add(new SummaryBar("User1",1234.567));
//        creditSummary.add(new SummaryBar("Dummy data",987654321));

        // Add amount of money to fill in
//        double debtBalance = sumary_obj.print_dep_total();
//        double creditBalance = sumary_obj.print_credit_total();
//        double totalBalance = debtBalance+creditBalance;
        double totalBalance = sumary.total;

        // Show "Total Balance" amount
        TextView totalTextView = (TextView) rootView.findViewById(R.id.total_balance_money);
        NumberFormat totalAmount = NumberFormat.getInstance();
        totalAmount.setMaximumFractionDigits(2);
        totalTextView.setText(totalAmount.format(totalBalance));
        TextView currencyTextView = (TextView) rootView.findViewById(R.id.total_balance_currency);
        // Set the color of "Total Balance" depending on positive or negative amount
        if(totalBalance>=0){
            totalTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.creditColor));
            currencyTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.creditColor));
        } else {
            totalTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.debtColor));
            currencyTextView.setTextColor(ContextCompat.getColor(getActivity(),R.color.debtColor));
        }

        // Show "Total debt" amount
//        TextView debtTextView = (TextView) rootView.findViewById(R.id.debt_total_money);
//        NumberFormat debtAmount = NumberFormat.getInstance();
//        debtAmount.setMaximumFractionDigits(2);
//        debtTextView.setText(debtAmount.format(debtBalance));

        // Show "Total credit" amount
//        TextView creditTextView = (TextView) rootView.findViewById(R.id.credit_total_money);
//        NumberFormat creditAmount = NumberFormat.getInstance();
//        creditAmount.setMaximumFractionDigits(2);
//        creditTextView.setText(creditAmount.format(creditBalance));

        //Create two ArrayAdapter: debt and credit list
        SummaryBarAdapter debtAdapter = new SummaryBarAdapter(getActivity(),debtor);
        SummaryBarAdapter creditAdapter = new SummaryBarAdapter(getActivity(),creditor);

        //Create listView object and set ArrayAdapter on it (to save memory resource)
        ListView debtList = (ListView) rootView.findViewById(R.id.debt_list);
        debtList.setAdapter(debtAdapter);
        debtList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** Add lines of action when listView is clicked
                 *  "view" is the view clicked,
                 *  "position" is the clicked row (start at position = 0)
                 */
            }
        });
        ListView creditList = (ListView) rootView.findViewById(R.id.credit_list);
        creditList.setAdapter(creditAdapter);
        creditList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** Add lines of action when listView is clicked
                 *  "view" is the view clicked,
                 *  "position" is the clicked row (start at position = 0)
                 */
            }
        });
        return rootView;
    }

}

