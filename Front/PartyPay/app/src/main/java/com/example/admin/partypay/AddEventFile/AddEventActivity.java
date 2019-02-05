package com.example.admin.partypay.AddEventFile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.partypay.EventActivityFile.EventActivity;
import com.example.admin.partypay.db.DBHelper;
import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.MemberFile.Member;
import com.example.admin.partypay.MemberFile.NewMemberActivity;
import com.example.admin.partypay.R;

import java.util.ArrayList;

/**
 * Created by Admin on 6/18/2017.
 */

public class AddEventActivity extends AppCompatActivity {
    Spinner mSearchSpinner;
    //Type of search: 0 = Meal, 1 = Trip, 2 = Group, 3 =Other
    private final int CATEGORY_MEAL = 0;
    private final int CATEGORY_TRIP = 1;
    private final int CATEGORY_GROUP = 2;
    private final int CATEGORY_OTHER = 3;

    private final int STATUS_NONE = 1;
    
    private int mSearch;
    private ArrayList<String> membersName;
    private String eventName;
    EventHelper db = new EventHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            membersName = (ArrayList<String>) getIntent().getSerializableExtra("MembersName");
            eventName = bundle.getString("EventName");
            mSearch = bundle.getInt("EventCategory");
        } else {
            membersName = new ArrayList<>();
            membersName.add(getString(R.string.default_user_name));
            mSearch = CATEGORY_MEAL;
        }

        Log.v("AddEventActivity","Add Event ...");
        //Setup for search spinner in AddEventAcitivity
        mSearchSpinner = (Spinner) findViewById(R.id.event_type_spinner);
        setupSpinner();

        //Setup necessary views
        // Current purse currently disabled
        // CheckBox checkBox = (CheckBox) findViewById(R.id.common_purse_checkbox);
        // TextView show number of members
        Log.v("AddEventActivity","TextView set");
        TextView textView = (TextView) findViewById(R.id.current_memb_textview);
        String countString = getResources().getString(R.string.crrt_member1)+ membersName.size() +getResources().getString(R.string.crrt_member2);
        textView.setText(countString);

        // Add event name if exists
        final EditText editText = (EditText) findViewById(R.id.crt_event_name);
        if(eventName != null){
            editText.setText(eventName);
        }

        // Add Member button
        Button memberButton = (Button) findViewById(R.id.add_memb_button);
        memberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to MemberActivity, send temporary data to next Activity too
                Intent intent = new Intent(AddEventActivity.this, NewMemberActivity.class);
                intent.putExtra("EventName",editText.getText().toString());
                intent.putExtra("EventCategory",mSearch);
                intent.putExtra("MembersName",membersName);
                startActivity(intent);
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to search member or event.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter typeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_addeventactivity_search_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSearchSpinner.setAdapter(typeSpinnerAdapter);
        mSearchSpinner.setSelection(mSearch);

        // Set the integer mSelected to the constant values
        mSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.event_type_0))) {
                        mSearch = CATEGORY_MEAL; // meal
                    } else if (selection.equals(getString(R.string.event_type_1))){
                        mSearch = CATEGORY_TRIP; // trip
                    } else if (selection.equals(getString(R.string.event_type_2))){
                        mSearch = CATEGORY_GROUP; // group
                    } else {
                        mSearch = CATEGORY_OTHER; // others
                    }
                }
            }
            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSearch = 0; // Unknown
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

    /**
     * Actions when user click on save button
     * If there's no event name or error for adding data to database, show Toast
     * // Otherwise, add event to database & go to EventActivity
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                EditText editText = (EditText) findViewById(R.id.crt_event_name);
                String eventName = editText.getText().toString();
                //Check if Event Name is empty
                if(eventName.trim().isEmpty()){
                    Toast toast = Toast.makeText(this,R.string.toast_no_event_name,Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    ArrayList<Member> members = new ArrayList<Member>();
                    for (int x = 0; x < membersName.size(); x = x + 1) {
                        members.add(new Member(membersName.get(x)));
                    }
                    //Add new event to database
                    int addEvent = db.addEvent(eventName, STATUS_NONE, mSearch, 0, members);
                    if (addEvent != -1) {
                        Intent intent = new Intent(AddEventActivity.this, EventActivity.class);
                        intent.putExtra("EventID", addEvent);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(this,R.string.toast_add_error, Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //db.close();
    }
}
