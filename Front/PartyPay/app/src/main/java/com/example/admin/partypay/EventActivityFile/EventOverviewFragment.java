package com.example.admin.partypay.EventActivityFile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.admin.partypay.R;
import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.db.Overview;
import com.example.admin.partypay.db.TranHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Overview of event contains expandableListView show date of event and detailed transaction
 */
public class EventOverviewFragment extends Fragment {

    int eventID;

    public EventOverviewFragment() {
        // Required empty public constructor
    }

    ExpandableListView expandableListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event_overview, container, false);

        // Receive necessary data from MainActivity
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle!= null){
            eventID = bundle.getInt("EventID");
            Log.v("EventOverviewFragment","EventID:"+ eventID);
        }

        // Create dummy data
//        ArrayList<String> transDate = new ArrayList<>();
//        ArrayList<DayTransaction> transInfo = new ArrayList<>();
//
//        transDate.add("10/01/2017");
//        transDate.add("15/02/2017");
//        transDate.add("01/03/2017");
//        transDate.add("10/05/2017");
//        transDate.add("11/10/2017");
//
//
//        for (int i =1;i<=transDate.size();i++){
//            ArrayList<String> transName = new ArrayList<>();
//            ArrayList<Double> transAmount = new ArrayList<>();
//            ArrayList<String> transMember = new ArrayList<>();
//            transName.add("Transaction 1"); transAmount.add(1000.00); transMember.add("Jon");
//            transName.add("Eating dinner"); transAmount.add(123456789.0); transMember.add("Snow");
//            transName.add("Dummy data");transAmount.add(515.75); transMember.add("Ed Sheeran");
//            transInfo.add(new DayTransaction(transName,transAmount,transMember));
//        }

        // TODO: Backend data (Null data is not valid)
        EventHelper eh = new EventHelper(getActivity());
        TranHelper th = new TranHelper(getActivity());
        Overview ov = th.searchTrans(eventID);
//        Overview ov = null;
        TextView textView = (TextView) rootView.findViewById(R.id.summary_no_transaction);
        textView.setVisibility(View.GONE);
        Log.v("EventOverviewFragment","size"+ov.getsize());
        if(ov == null || ov.getsize()==0){
            textView.setVisibility(View.VISIBLE);
        } else {
            // Create adapter contain necessary information & setAdapter
            DayTransactionAdapter adapter = new DayTransactionAdapter(getActivity(), ov.getDate(), ov.getTrans(), eventID);
            expandableListView = (ExpandableListView) rootView.findViewById(R.id.summary_expandable);
            expandableListView.setAdapter(adapter);
        }
        return rootView;
    }

}
