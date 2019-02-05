package com.example.admin.partypay.MemberFile;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.admin.partypay.EventActivityFile.EventActivity;
import com.example.admin.partypay.EventActivityFile.Sumary;
import com.example.admin.partypay.EventActivityFile.Sumary_obj;
import com.example.admin.partypay.EventActivityFile.SummaryBar;
import com.example.admin.partypay.ExpandableHeightGridView;
import com.example.admin.partypay.db.DBHelper;
import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.MainActivityFile.Event;
import com.example.admin.partypay.R;
import com.example.admin.partypay.db.Showevent;
import com.example.admin.partypay.db.TranHelper;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.name;
import static java.security.AccessController.getContext;

/**
 * Created by Admin on 6/29/2017.
 */

public class MemberActivity extends AppCompatActivity {

    ArrayList<Member> members = new ArrayList<>();
    int eventID = -1;
    EventHelper db;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        final Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            // Retrieve necessary data from previous activity
            eventID = bundle.getInt("EventID");
        }

        db = new EventHelper(this);
        event = db.getEvent(eventID);
        members = event.getEvent_member();
        for (int i =0;i<members.size();i++){
            Log.i("MemberActivity","First Members#"+i+": ID="+members.get(i).get_id());
        }

        TranHelper th = new TranHelper(MemberActivity.this);
        Sumary sumary = new Sumary(eventID,MemberActivity.this,MemberActivity.this);
        Sumary_obj sumary_obj = sumary.sumary_result();
        ArrayList<SummaryBar> debtor = sumary_obj.print_deptor();
        ArrayList<SummaryBar> creditor = sumary_obj.print_creditor();
        final MemberAdapter memberAdapter = new MemberAdapter(MemberActivity.this,members,debtor,creditor);

        ExpandableHeightGridView listView = (ExpandableHeightGridView) findViewById(R.id.member_list);
        listView.setExpanded(true);
        listView.setAdapter(memberAdapter);
        final LayoutTransition transition = listView.getLayoutTransition();
        transition.disableTransitionType(LayoutTransition.APPEARING);
        Log.v("MemberActivity","First Focus:"+getCurrentFocus());
        final Button button = (Button) findViewById(R.id.add_member_button);
        // Setup "Add Member" button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MemberActivity.this);
                LayoutInflater inflater = MemberActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_member_edit, null);
                dialogBuilder.setView(dialogView);

                final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
                String name = null;
                dialogBuilder.setTitle("Add Member");
                dialogBuilder.setMessage("Type new member's name");
                dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        transition.enableTransitionType(LayoutTransition.APPEARING);
                        memberAdapter.add(new Member(edt.getText().toString()));
                        memberAdapter.notifyDataSetChanged();
                        ScrollView parentView = (ScrollView) findViewById(R.id.activity_member);
                        focusOnView(parentView,button);
                        if(memberAdapter.getCount()>=20){
                            button.setVisibility(View.INVISIBLE);
                        } else {
                            button.setVisibility(View.VISIBLE);
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
                Log.v("MemberActivity","Focus:"+getCurrentFocus());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                ArrayList<Member> newMembers = new ArrayList<>();
                for(int i = 0; i<members.size();i++){
                    // Removed all name without no text or space only
                    if(!members.get(i).get_name().trim().isEmpty()){
                        newMembers.add(members.get(i));
                        Log.i("MemberActivity","Second Members#"+i+": ID="+members.get(i).get_id());
                    }
                }
                if(!newMembers.isEmpty()) {
                    for(int i = 0; i<members.size();i++) {
                        if (!newMembers.get(i).get_name().trim().isEmpty()) {
                            Log.i("Third MemberActivity", "Members#" + i + ": ID=" + newMembers.get(i).get_id());
                        }
                    }
                    db.editEvent(event.getEvent_id(), event.getEvent_name(), event.getEvent_status(), event.getEvent_type(), newMembers);
                    Intent intent = new Intent(MemberActivity.this, EventActivity.class);
                    intent.putExtra("EventID", eventID);
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(MemberActivity.this,"Please add at least one member name",Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MemberActivity.this, EventActivity.class);
        intent.putExtra("EventID",eventID);
        startActivity(intent);
        finish();
    }

    private final void focusOnView(final ScrollView scroll, final View view) {
        Log.i("MemberActivity","focusOnView triggered: "+view.getTop());
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
                //scroll.smoothScrollTo(0, view.getTop());
            }
        });
    }

}
