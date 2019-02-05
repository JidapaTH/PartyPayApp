package com.example.admin.partypay.MainActivityFile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.partypay.AddEventFile.AddEventActivity;
import com.example.admin.partypay.EventActivityFile.EventActivity;
import com.example.admin.partypay.MemberFile.MemberAdapter;
import com.example.admin.partypay.SettingActivity;
import com.example.admin.partypay.db.DBHelper;
import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.R;
import com.example.admin.partypay.db.Showevent;
import com.example.admin.partypay.db.TranHelper;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 *  MainActivity is a start page of this application.
 *  This activity contain list of created events
 */
public class MainActivity extends AppCompatActivity {

    //Variable for Views in MainActivity
    private boolean sortMethod = true; // True = sort by time, false = sort by alphabet
    private boolean sortAlphabetAscend = false; // True = sort alphabet by ascending
    private boolean sortTimeAscend = false; // True = sort time by ascending

    private boolean isSortOpen = false; //Check if Sort bar is up
    private boolean isMoreOpen = false; //Check if More bar is up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EventHelper db = new EventHelper(this);
        final ArrayList<Showevent> events;
        events = db.searchEvent();

        // Sort menu
        final LinearLayout sortMenu = (LinearLayout) findViewById(R.id.sort_options);
        RelativeLayout sortAZ = (RelativeLayout) findViewById(R.id.sort_alphabet_relative);
        RelativeLayout sortTime = (RelativeLayout) findViewById(R.id.sort_time_relative);
        final ImageView imgAZ = (ImageView) findViewById(R.id.sort_alphabet_imageview);
        final ImageView imgTime = (ImageView) findViewById(R.id.sort_time_imageview);
        setSortImages(imgAZ,imgTime);

        // Don't forget setSortImage method
        sortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If it's already sort by alphabet, toggle sorting
                if(!sortMethod){
                    sortAlphabetAscend = !sortAlphabetAscend;
                } else {
                    sortMethod = !sortMethod;
                }
                setSortImages(imgAZ,imgTime);

                // Re-sort views
                ListView listView = (ListView) findViewById(R.id.event_list);
                EventBarAdapter adapter = new EventBarAdapter(MainActivity.this,db.sortEvent(events,sortAlphabetAscend? 1:0,sortMethod? 1:0));
                listView.setAdapter(adapter);

                slide_down(getApplicationContext(),sortMenu);
                sortMenu.setVisibility(View.GONE);
                isSortOpen = false;
            }
        });

        sortTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If it's already sort by alphabet, toggle sorting
                if(sortMethod){
                    sortTimeAscend = !sortTimeAscend;
                } else {
                    sortMethod = !sortMethod;
                }
                setSortImages(imgAZ,imgTime);

                //Re-sort view
                ListView listView = (ListView) findViewById(R.id.event_list);
                EventBarAdapter adapter = new EventBarAdapter(MainActivity.this,db.sortEvent(events,sortTimeAscend? 1:0,sortMethod? 1:0));
                listView.setAdapter(adapter);

                slide_down(getApplicationContext(),sortMenu);
                sortMenu.setVisibility(View.GONE);
                isSortOpen = false;
            }
        });

        // More menu
        final LinearLayout moreMenu = (LinearLayout) findViewById(R.id.more_options);
        RelativeLayout clearAllData = (RelativeLayout) findViewById(R.id.clear_all_data);
        clearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setMessage("Are you sure you want to DELETE all data?");
                mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.clearALLEvent();

                        // Re-sort views
                        ListView listView = (ListView) findViewById(R.id.event_list);
                        EventBarAdapter adapter = new EventBarAdapter(MainActivity.this,new ArrayList<Showevent>());
                        listView.setAdapter(adapter);
                        slide_down(MainActivity.this,moreMenu);
                        moreMenu.setVisibility(View.GONE);
                        isMoreOpen = false;
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        slide_down(MainActivity.this,moreMenu);
                        moreMenu.setVisibility(View.GONE);
                        isMoreOpen = false;
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });

        RelativeLayout setting = (RelativeLayout) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        //Bottom bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_add:
                        //Go to AddEventActivity when (+) button is clicked
                        Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_sort:
                        //Close 'moreMenu' if more is up
                        if(isMoreOpen){
                            slide_down(getApplicationContext(),moreMenu);
                        }
                        moreMenu.setVisibility(View.GONE);
                        isMoreOpen = false;

                        //Open/close 'sortMenu'
                        if(!isSortOpen) {
                            sortMenu.setVisibility(View.VISIBLE);
                            slide_up(getApplicationContext(), sortMenu);
                        } else {
                            slide_down(getApplicationContext(),sortMenu);
                            sortMenu.setVisibility(View.GONE);
                        }
                        isSortOpen = !isSortOpen;
                        break;
                    case R.id.action_more:
                        // Close 'sortMenu' if sort is up
                        if(isSortOpen) {
                            slide_down(getApplicationContext(), sortMenu);
                        }
                        sortMenu.setVisibility(View.GONE);
                        isSortOpen = false;

                        //Open/close 'moreMenu'
                        if(!isMoreOpen) {
                            moreMenu.setVisibility(View.VISIBLE);
                            slide_up(getApplicationContext(), moreMenu);
                        } else {
                            slide_down(getApplicationContext(),moreMenu);
                            moreMenu.setVisibility(View.GONE);
                        }
                        isMoreOpen = !isMoreOpen;
                        break;
                }
                return  true;
            }
        });

        //Create an ArrayAdapter for event list
        final EventBarAdapter itemsAdapter = new EventBarAdapter(MainActivity.this,events);

        //Create listView object and set ArrayAdapter on it (to save memory resource)
        ListView listView = (ListView) findViewById(R.id.event_list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** Add lines of action when listView is clicked
                 *  "view" is the view clicked,
                 *  "position" is the clicked row (start at position = 0)
                 */
                // Go to EventActivity, send "position" to new activity when (+) button is clicked
                Log.v("MainActivity","View clicked");
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                intent.putExtra("EventID", events.get(position).getEvent_id_col());
                startActivity(intent);
            }
        });

        EditText searchEditText = (EditText) findViewById(R.id.search_edittext);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Showevent> eventsTemp = db.searchEvent(s.toString());
                ListView listView = (ListView) findViewById(R.id.event_list);
                EventBarAdapter adapter = new EventBarAdapter(MainActivity.this,eventsTemp);
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        searchEditText.addTextChangedListener(textWatcher);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
    }

    //Animation to make transition more smooth
    private static void slide_down(Context ctx, LinearLayout v){
        Animation a = AnimationUtils.loadAnimation(ctx,R.anim.slide_down_main);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private static void slide_up(Context ctx, LinearLayout v){
        Animation a = AnimationUtils.loadAnimation(ctx,R.anim.slide_up_main);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    // Set correct image for sortBar
    private void setSortImages(ImageView imgAZ, ImageView imgTime){
        if(!sortMethod){
            imgAZ.setVisibility(View.VISIBLE);
            imgTime.setVisibility(View.INVISIBLE);
        } else {
            imgAZ.setVisibility(View.INVISIBLE);
            imgTime.setVisibility(View.VISIBLE);
        }
        if(!sortAlphabetAscend){
            //Set image for ascending sort
            imgAZ.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        } else {
            imgAZ.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
        if(sortTimeAscend){
            imgTime.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        } else {
            imgTime.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MainActivity","Destroyed");
        DBHelper db = new DBHelper(this);
        //db.close();
    }
}


