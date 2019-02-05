package com.example.admin.partypay.MainActivityFile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.R;
import com.example.admin.partypay.db.Showevent;

import java.util.ArrayList;

/**
 * This class is used to create ListView + ArrayAdapter for EventBar object in MainActivity
 */

//public class EventBarAdapter extends ArrayAdapter<EventBar> {
public class EventBarAdapter extends ArrayAdapter<Showevent> {
    private final int CATEGORY_MEAL = 0;
    private final int CATEGORY_TRIP = 1;
    private final int CATEGORY_GROUP = 2;
    private final int CATEGORY_OTHER = 3;

    private final int STATUS_NONE = 1;
    private final int STATUS_FAVOURITE = 0;
    private final int STATUS_DONE = 2;

    //private final int DUMMY_DATA_ID = -2;
    EventHelper db = new EventHelper(getContext());
    //Constructor method
    //public EventBarAdapter(Activity context, ArrayList<EventBar> eventBars){
    public EventBarAdapter(Activity context, ArrayList<Showevent> eventBars){
        super(context,0, eventBars);
    }

    //Override this method to show views on the ListView
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_event_main, parent, false);
        }
        // Use this code if you want to add backgroud color
        // P.S. add int color in constructor method too
        /*
        View textContainer = listItemView.findViewById((R.id.text_container));
        int color = ContextCompat.getColor(getContext(),mBackgroundColor);
        textContainer.setBackgroundColor(color);
        listItemView.setBackgroundColor(mBackgroundColor);
        */

        //Import informations to be shown on the screen
        //final EventBar currentEvent = getItem(position);
        final Showevent currentEvent = getItem(position);
        final RelativeLayout relativeLayout = (RelativeLayout) listItemView.findViewById(R.id.event_bar);
        final LinearLayout statusBar = (LinearLayout) listItemView.findViewById(R.id.status_bar);

        //Import ImageView of event's category
        final ImageView iconImageView = (ImageView) listItemView.findViewById(R.id.event_icon);
        setIconImage(iconImageView,currentEvent);

        //Import ImageView of dropdown arrow, EventBar will expand/collapse when clicked
        ImageView arrowImageView = (ImageView) listItemView.findViewById(R.id.event_status_arrow);
        arrowImageView.setImageResource(R.drawable.ic_arrow_drop_down_circle_black_36dp);
        arrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show the status bar if the bar is hidden or hide it otherwise
                if (statusBar.isShown()){
                    v.animate().rotation(0).setDuration(200).start();
                    statusBar.setVisibility(View.GONE);
                }else {
                    v.animate().rotation(180).setDuration(200).start();
                    statusBar.setVisibility(View.VISIBLE);
                    slide_down(getContext(), statusBar);
                }
                // This will make the statusBar show as long as it is not maually closed or destroy App
                currentEvent.toggleStatusBarShown();
            }
        });

        //Import Event's name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.event_name);
        nameTextView.setText(currentEvent.getEvent_name_col());

        // Import Event's date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.event_date);
        dateTextView.setText(currentEvent.getEvent_time_string());

        //Import ImageView of Favourite and Done status
        final ImageView favImageView = (ImageView) listItemView.findViewById(R.id.event_fav);
        final ImageView doneImageView = (ImageView) listItemView.findViewById(R.id.event_done);

        // Set listener on both button to change event status + update in database
        setFavouriteImage(favImageView,currentEvent.getEvent_status_col());
        favImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("EventBarAdapter","Favorite clicked");
                // Add command when favourite button is pressed
                if(currentEvent.getEvent_status_col()==STATUS_FAVOURITE){
                    currentEvent.setEvent_status_col(STATUS_NONE);
                } else {
                    currentEvent.setEvent_status_col(STATUS_FAVOURITE);
                }
                setIconImage(iconImageView,currentEvent);
                setFavouriteImage(favImageView,currentEvent.getEvent_status_col());
                setDoneImage(doneImageView,currentEvent.getEvent_status_col());
                updateDB(currentEvent);
            }
        });

        setDoneImage(doneImageView,currentEvent.getEvent_status_col());
        doneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("EventBarAdapter","Done clicked");
                // Add command when finish button is pressed
                if(currentEvent.getEvent_status_col()==STATUS_DONE){
                    currentEvent.setEvent_status_col(STATUS_NONE);
                } else {
                    currentEvent.setEvent_status_col(STATUS_DONE);
                }
                setIconImage(iconImageView,currentEvent);
                setFavouriteImage(favImageView,currentEvent.getEvent_status_col());
                setDoneImage(doneImageView,currentEvent.getEvent_status_col());
                updateDB(currentEvent);
            }
        });
        //Import ImageView of delete status + Alert dialog
        ImageView deleteImageView = (ImageView) listItemView.findViewById(R.id.event_delete);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add command when delete button is pressed
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setMessage(R.string.main_alert_message);
                mBuilder.setPositiveButton(R.string.main_alert_positive,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick (DialogInterface dialogInterface, int i){
                        db.clearEvent(currentEvent.getEvent_id_col());
                        EventBarAdapter.this.remove(currentEvent);
                        EventBarAdapter.this.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                mBuilder.setNegativeButton(R.string.main_alert_negative,new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();
            }
        });

        // Don't show anything if it's a dummy data
//        if(currentEvent.getEvent_id_col()==DUMMY_DATA_ID){
//            relativeLayout.setBackgroundResource(0);
//            iconImageView.setVisibility(View.INVISIBLE);
//            arrowImageView.setVisibility(View.INVISIBLE);
//            nameTextView.setVisibility(View.INVISIBLE);
//            dateTextView.setVisibility(View.INVISIBLE);
//            statusBar.setVisibility(View.INVISIBLE);
//        } else {
//            relativeLayout.setBackgroundResource(R.drawable.default_border);
//            iconImageView.setVisibility(View.VISIBLE);
//            arrowImageView.setVisibility(View.VISIBLE);
//            nameTextView.setVisibility(View.VISIBLE);
//            dateTextView.setVisibility(View.VISIBLE);
//            statusBar.setVisibility(View.VISIBLE);
//        }
        //Show the status bar if dropdown arrow is pressed
        //if(currentEvent.getStatusBarShown()){
        if(currentEvent.getStatusBarShown()){
            statusBar.setVisibility(View.VISIBLE);
        } else {
            statusBar.setVisibility(View.GONE);
        }
        return listItemView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    //Animation to make transition more smooth
    private static void slide_down(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx,R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private void setFavouriteImage(ImageView imageView,int status){
        if(status == STATUS_FAVOURITE){
            imageView.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            imageView.setImageResource(android.R.drawable.btn_star_big_off);
        }
    }

    private void setDoneImage(ImageView imageView,int status){
        if(status == STATUS_DONE){
            imageView.setImageResource(R.drawable.event_done);
        } else {
            imageView.setImageResource(R.drawable.event_undone);
        }
    }

    private void setIconImage(ImageView imageView,Showevent event){
        switch (event.getEvent_status_col()){
            case STATUS_NONE:
                switch(event.getType_id_col()) {
                    case CATEGORY_MEAL:
                        imageView.setImageResource(R.drawable.event_type_meal); break;
                    case CATEGORY_TRIP:
                        imageView.setImageResource(R.drawable.event_type_trip);break;
                    case CATEGORY_GROUP:
                        imageView.setImageResource(R.drawable.event_type_group);break;
                    case CATEGORY_OTHER:
                        imageView.setImageResource(R.drawable.event_type_other);break;
                    default:
                        imageView.setImageResource(R.drawable.event_type_other);break;
                }
                break;
            case STATUS_FAVOURITE:
                imageView.setImageResource(android.R.drawable.btn_star_big_on); break;
            case STATUS_DONE:
                imageView.setImageResource(R.drawable.event_done); break;
        }
    }

    private void updateDB(Showevent event){
        Event eventTemp = db.getEvent(event.getEvent_id_col());
        Log.v("EventBarAdapter","ID: "+event.getEvent_id_col());
        Log.v("EventBarAdapter","Status: "+event.getEvent_status_col());
        if(!db.editEvent(event.getEvent_id_col(),event.getEvent_name_col(),event.getEvent_status_col(),event.getType_id_col(),eventTemp.getEvent_member())){
            Toast toast = Toast.makeText(getContext(),"Error change status",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}


