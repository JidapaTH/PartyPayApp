package com.example.admin.partypay.EventActivityFile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.partypay.TransactionFile.AddTransactionActivity;
import com.example.admin.partypay.MemberFile.MemberActivity;
import com.example.admin.partypay.R;

/**
 * This activity shows summary of selected event (from MainActivity)
 * This contain overview and summary of event using ViewPager (More detail in EventCategoryAdapter)
 * TabLayout is used to display the fragment selection above
 */

public class EventActivity extends AppCompatActivity {

    int eventID = -1;
    //Variable for Views in EventActivity
    private boolean sortMethod = true; // True = sort by amount, false = sort by alphabet
    private boolean sortAlphabetAscend = false; // True = sort alphabet by ascending
    private boolean sortAmountAscend = false; // True = sort amount by ascending

    private int eventPage = 0;
    private boolean isSortOpen = false; //Check if Sort bar is up
    private boolean isMoreOpen = false; //Check if More bar is up
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            // Retrieve necessary data from previous activity
            eventID = bundle.getInt("EventID");
        }
        // Main Layouts
        final LinearLayout sortMenu = (LinearLayout) findViewById(R.id.sort_options);
        sortMenu.setVisibility(View.GONE);
        final LinearLayout moreMenu = (LinearLayout) findViewById(R.id.more_options);
        moreMenu.setVisibility(View.GONE);
        final TextView alpTextView = (TextView) findViewById(R.id.sort_alphabet_textview);
        final TextView amtTextView = (TextView) findViewById(R.id.sort_time_textview);

        // Setup View Pager
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // Create an adapter that knows which fragment should be shown on each page
        EventCategoryAdapter adapter = new EventCategoryAdapter(getSupportFragmentManager(),EventActivity.this);
        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.v("EventActivity","Clicked:"+i);
                if(isMoreOpen){
                    slide_down(getApplicationContext(),moreMenu);
                }
                moreMenu.setVisibility(View.GONE);
                isMoreOpen = false;
                if(isSortOpen) {
                    slide_down(getApplicationContext(), sortMenu);
                }
                sortMenu.setVisibility(View.GONE);
                isSortOpen = false;
                if(i == 0){
                   //Set new message on SummaryFragment
                    alpTextView.setText("Sort by Name");
                    amtTextView.setText("Sort by Amount");
                } else {
                    //Set new message on OverviewFragment
                    alpTextView.setText("Sort by Name");
                    amtTextView.setText("Sort by Time");
                }
                eventPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        RelativeLayout sortAZ = (RelativeLayout) findViewById(R.id.sort_alphabet_relative);
        RelativeLayout sortAmount = (RelativeLayout) findViewById(R.id.sort_time_relative);
        final ImageView imgAZ = (ImageView) findViewById(R.id.sort_alphabet_imageview);
        final ImageView imgAmount = (ImageView) findViewById(R.id.sort_time_imageview);
        setSortImages(imgAZ,imgAmount);

        setupBottomBar(sortMenu,moreMenu); //Setup Bottom navigation bar



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
                setSortImages(imgAZ,imgAmount);

                // Re-sort views
                //TODO: How to re-sort?

                slide_down(getApplicationContext(),sortMenu);
                sortMenu.setVisibility(View.GONE);
                isSortOpen = false;
            }
        });

        sortAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If it's already sort by alphabet, toggle sorting
                if(sortMethod){
                    sortAmountAscend = !sortAmountAscend;
                } else {
                    sortMethod = !sortMethod;
                }
                setSortImages(imgAZ,imgAmount);

                //Re-sort view
                //TODO: How to re-sort?

                slide_down(getApplicationContext(),sortMenu);
                sortMenu.setVisibility(View.GONE);
                isSortOpen = false;
            }
        });

        // More menu
        RelativeLayout recommend = (RelativeLayout) findViewById(R.id.recommend);
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(EventActivity.this);
                mBuilder.setMessage("Are you sure you want to DELETE all data?");
                mBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //TODO: Goto recommend page

                        slide_down(EventActivity.this,moreMenu);
                        moreMenu.setVisibility(View.GONE);
                        isMoreOpen = false;
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        slide_down(EventActivity.this,moreMenu);
                        moreMenu.setVisibility(View.GONE);
                        isMoreOpen = false;
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });

        RelativeLayout editMember = (RelativeLayout) findViewById(R.id.members);
        editMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMember();
                slide_down(EventActivity.this,moreMenu);
                moreMenu.setVisibility(View.GONE);
                isMoreOpen = false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //DBHelper db = new DBHelper(this);
        //db.close();
    }

    public void setupBottomBar(final LinearLayout sortMenu,final LinearLayout moreMenu){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.event_action_add:
                        // Go to add new transaction activity when (+) button is clicked
                        Intent intent = new Intent(EventActivity.this, AddTransactionActivity.class);
                        intent.putExtra("EventID",eventID);
                        startActivity(intent);
                        break;
                    case R.id.event_action_sort:
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
                    case R.id.event_action_more:
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
    }

    public void editMember(){
        // Go to edit member activity
        Intent intents = new Intent(EventActivity.this, MemberActivity.class);
        intents.putExtra("EventID",eventID);
        intents.putExtra("isNewEvent",false);
        startActivity(intents);
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
        if(sortAmountAscend){
            imgTime.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        } else {
            imgTime.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
    }
}

