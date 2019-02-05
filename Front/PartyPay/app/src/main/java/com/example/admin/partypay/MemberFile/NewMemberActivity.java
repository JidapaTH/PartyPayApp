package com.example.admin.partypay.MemberFile;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.admin.partypay.AddEventFile.AddEventActivity;
import com.example.admin.partypay.ExpandableHeightGridView;
import com.example.admin.partypay.R;

import java.util.ArrayList;

/**
 * Created by Admin on 6/29/2017.
 */

public class NewMemberActivity extends AppCompatActivity {

    ArrayList<Member> members = new ArrayList<>();
    String eventName;
    int eventCategory;
    ArrayList<String> membersName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            // Retrieve necessary data from previous activity
            eventName = bundle.getString("EventName");
            eventCategory = bundle.getInt("EventCategory");
            membersName = (ArrayList<String>) getIntent().getSerializableExtra("MembersName");

        }

        if(membersName != null){
            for (int x = 0; x < membersName.size(); x = x + 1) {
                members.add(new Member(membersName.get(x)));
            }
        }

        final MemberAdapter memberAdapter = new MemberAdapter(NewMemberActivity.this,members);

        ExpandableHeightGridView listView = (ExpandableHeightGridView) findViewById(R.id.member_list);
        listView.setExpanded(true);
        listView.setAdapter(memberAdapter);
        final LayoutTransition transition = listView.getLayoutTransition();
        transition.disableTransitionType(LayoutTransition.APPEARING);

        final Button button = (Button) findViewById(R.id.add_member_button);
        // Setup "Add Member" button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition.enableTransitionType(LayoutTransition.APPEARING);
                memberAdapter.add(new Member(""));
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
    }

    // Add code to save data here
    private  void saveData(){
        //Toast toast = Toast.makeText(NewMemberActivity.this,members.get(members.size()-2).getMemberName(),Toast.LENGTH_SHORT);
        //toast.show();
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
        ArrayList<String> newMembersName= new ArrayList<>();
        Log.v("NewMemberActivity","Member size = "+members.size());
        for(int x = 0;x<members.size();x=x+1){
            Log.v("NewMemberActivity","Member Name:"+members.get(x).get_name());
            if(members.get(x).get_name() != null && !members.get(x).get_name().trim().isEmpty() ){
                newMembersName.add(members.get(x).get_name());
            }
        }
        switch (item.getItemId()) {
            case R.id.action_save:
                if(!newMembersName.isEmpty()) {
                    saveData();
                    Intent intent = new Intent(NewMemberActivity.this, AddEventActivity.class);
                    Log.v("MemberActivity", "(Save) Members count:" + members.size());
                    intent.putExtra("MembersName", newMembersName);
                    intent.putExtra("EventName", eventName);
                    intent.putExtra("EventCategory", eventCategory);
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(NewMemberActivity.this,"Please add at least one member name",Toast.LENGTH_SHORT);
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
        Intent intent = new Intent(NewMemberActivity.this, AddEventActivity.class);
        Log.v("MemberActivity","(Back) Members count:"+members.size());
        Log.v("NewMemberActivity","mSearch"+eventCategory);
        intent.putExtra("MembersName",membersName);
        intent.putExtra("EventName",eventName);
        intent.putExtra("EventCategory",eventCategory);
        startActivity(intent);
        finish();
    }

    private final void focusOnView(final ScrollView scroll, final View view) {
        Log.i("NewMemberActivity","focusOnView triggered: "+view.getTop());
        scroll.post(new Runnable() {
            @Override
            public void run() {
                //scroll.smoothScrollTo(0, view.getTop());
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}
