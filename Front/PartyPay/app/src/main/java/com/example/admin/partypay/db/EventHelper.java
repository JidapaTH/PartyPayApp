package com.example.admin.partypay.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

import com.example.admin.partypay.EventActivityFile.Sumary;
import com.example.admin.partypay.EventActivityFile.Sumary_obj;
import com.example.admin.partypay.MainActivityFile.Event;
import com.example.admin.partypay.MemberFile.Member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.lang.Boolean.*;



/**
 * Created by WINDOWS on 28/6/2560.
 */

public class EventHelper {


    private static DBHelper dbh;
    private static SQLiteDatabase sqlDB;
    public Context context;

    private String TABLE;
    private static class Column {
        private static  String EVENT_ID ;
        private static  String EVENT_NAME ;
        private static  String TYPE ;
        private static  String POOL ;
        private static  String STATUS ;
        private static  String START_TIME ;
        private static  String END_TIME ;
        private static  String MEMBER ;
    }
    public EventHelper(Context context) {
        dbh = DBHelper.getInstance(context);
        sqlDB = dbh.getWritableDatabase();
        TABLE = DBHelper.EVENT_TABLE;
        Column.EVENT_ID = DBHelper.EColumn.EVENT_ID;
        Column.EVENT_NAME = DBHelper.EColumn.EVENT_NAME;
        Column.TYPE = DBHelper.EColumn.TYPE;
        Column.POOL = DBHelper.EColumn.POOL;
        Column.STATUS = DBHelper.EColumn.STATUS;
        Column.START_TIME = DBHelper.EColumn.START_TIME;
        Column.END_TIME = DBHelper.EColumn.END_TIME;
        Column.MEMBER = DBHelper.EColumn.MEMBER;
        this.context=context;
    }
    //close connection
    public void close(){
        sqlDB.close();
    }

    public void dropTable(){
        sqlDB.execSQL("DROP TABLE "+ TABLE);
    }

    public void clearALLEvent(){
        sqlDB.execSQL("delete from "+ TABLE);
    }

    public void clearEvent(int id){
        sqlDB.execSQL("DELETE FROM "+ TABLE + " WHERE EVENT_ID = " + id);
    }

    // Adding new event
    public int addEvent(String name, int status,int type,int pool, ArrayList<Member> member) {
        new Member("tmp").reset_id();
        int seconds = (int)(System.currentTimeMillis() / 1000);
        return addEvent(name, status,type,pool, seconds,member );
    }

    public int addEvent(String name, int status,int type,int pool, int sttime,ArrayList<Member> member) {

        GeneralHelper gh= new GeneralHelper();
        if (!gh.checkMember(member)){
            return -1;
        }else{
            int id ;
            try {
                //TODO ArrayList<member> member -> JSON
                String memberJSON = gh.jsonCreator_member(member);


                ContentValues values = new ContentValues();
                values.put(Column.EVENT_NAME, name);
                values.put(Column.STATUS, status);
                values.put(Column.TYPE, type);
                values.put(Column.POOL, pool);
                values.put(Column.START_TIME, sttime);
                values.put(Column.MEMBER, memberJSON);

                // Inserting Row
                //TODO the convertion will have problem if id is longer than maximum int
                id = (int)(sqlDB.insert(TABLE, null, values));


            } finally {
                //sqlDB.close(); // Closing database connection
            }
            return id;
        }
    }

    public boolean checkEventName(String name){
        ArrayList<Showevent> foundName = searchEvent(name);
        if( foundName.size()==1){
            return FALSE;
        } else{
            return TRUE;
        }

    }


    public boolean editEvent(int id, String name, int status,int type, ArrayList<Member> member) {
        Log.v("EventHelper","EditEvent: "+id + " Status: "+status);

        new Member("tmp").reset_id();
        //TODO check delete or new member.
        // if delete member, what will happen to old transaction
        //TODO check if person is deleted or not -> if is deleted, his dept have to distribution to others

        GeneralHelper gh= new GeneralHelper();
       if (!gh.checkMember(member)){
            return FALSE;
        }else {

            String memberJSON = gh.jsonCreator_member(member);

            ContentValues values = new ContentValues();
            values.put(Column.EVENT_NAME, name);
            values.put(Column.STATUS, status);
            values.put(Column.TYPE, type);
            values.put(Column.MEMBER, memberJSON);
            sqlDB.update(TABLE, values, Column.EVENT_ID+"="+id, null);
            return TRUE;
        }
    }


    // reading
    public Event getEvent(int id) {

        GeneralHelper gh= new GeneralHelper();
        Cursor cursor = sqlDB.query(TABLE, new String[] { Column.EVENT_ID,
                        Column.EVENT_NAME, Column.STATUS, Column.TYPE,Column.POOL,Column.START_TIME,Column.END_TIME,Column.MEMBER }, Column.EVENT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        boolean pool_b;
        if(cursor.getInt(4)==0){
            pool_b=TRUE;
        } else {
            pool_b=FALSE;
        }

        //TODO JSON->ARRAYLIST
        ArrayList<Member> arr = gh.jsonTranslator_member(cursor.getString(7));

        Event event = new Event(cursor.getInt(0),cursor.getString(1), cursor.getInt(2),cursor.getInt(3), pool_b,cursor.getInt(5),cursor.getInt(6),arr);


        // return contact
        return event;
    }


    // search events
    public ArrayList<Showevent> searchEvent() {
        return searchEvent( "", 0,0);
    }
    public ArrayList<Showevent> searchEvent( String name) {
        return searchEvent(name, 0,0);
    }
    public ArrayList<Showevent> searchEvent(String name, int sortID, int dir) {
        ArrayList<Showevent> eventList = new ArrayList<Showevent>();
        ArrayList<Showevent> sortEventList = new ArrayList<Showevent>();
        // Select All Query

        String selectQuery = "SELECT EVENT_ID ,EVENT_NAME, TYPE, STATUS, START_TIME FROM " + TABLE + " WHERE  EVENT_NAME LIKE ? ORDER BY STATUS, START_TIME" ;

        Cursor cursor = sqlDB.rawQuery(selectQuery, new String[] { "%"+String.valueOf(name)+"%" });

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Date dt = new java.util.Date(((long)cursor.getInt(4))*1000);
                Showevent s_event = new Showevent(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),dt);
                // Adding contact to list
                eventList.add(s_event);
            } while (cursor.moveToNext());

            // SORT
            //sortEventList = sortEvent(eventList, dir,sortID);

            // dummy row
            //eventList.add(new Showevent(-2,"",0,0,null));

        }

        cursor.close();
        // return contact list
        return eventList;
    }


    //SORT
    public ArrayList<Showevent> sortEvent(ArrayList<Showevent> event, final int dir, int condition) {
        if(event.size()>0){
            event.remove(event.size() - 1);
            if(condition == 0){
                Collections.sort(event, new Comparator<Showevent>() {
                    //override compare function to select sort items
                    @Override
                    public int compare(Showevent e1, Showevent e2) {
                        //compare
                        if (dir == 0){
                            //sort by name
                            //int comparison;
                            int comparison = Integer.valueOf(e1.getEvent_status_col()).compareTo(e2.getEvent_status_col());

                            if (comparison == 0) {
                                comparison = e1.getEvent_name_col().compareTo(e2.getEvent_name_col());
                            }
                            return comparison;
                        } else {
                            //sort by name
                            int comparison = Integer.valueOf(e1.getEvent_status_col()).compareTo(e2.getEvent_status_col());
                            if (comparison == 0) {
                                comparison = e2.getEvent_name_col().compareTo(e1.getEvent_name_col());
                            }
                            return comparison;
                        }
                    }
                    //sort resource link
                    //https://stackoverflow.com/questions/9109890/android-java-how-to-sort-a-list-of-objects-by-a-certain-value-within-the-object
                });
                event.add(new Showevent(-2,"",0,0,null));
                return event;
            } else {
                Collections.sort(event, new Comparator<Showevent>() {
                    //override compare function to select sort items
                    @Override
                    public int compare(Showevent e1, Showevent e2) {
                        //compare
                        if (dir ==0){
                            //sort by date
                            int comparison = Integer.valueOf(e1.getEvent_status_col()).compareTo(e2.getEvent_status_col());
                            if (comparison == 0) {
                                comparison = e1.getEvent_time_col().compareTo(e2.getEvent_time_col());
                            }
                            return comparison;
                        } else {
                            //sort by name
                            int comparison = Integer.valueOf(e1.getEvent_status_col()).compareTo(e2.getEvent_status_col());
                            if (comparison == 0) {
                                comparison = e2.getEvent_time_col().compareTo(e1.getEvent_time_col());
                            }
                            return comparison;
                            //below is to sort by UNIX
                            //return Integer.valueOf(e2.getEvent_time_col()).compareTo(e1.getEvent_time_col());
                        }
                    }
                    //sort resource link
                    //https://stackoverflow.com/questions/9109890/android-java-how-to-sort-a-list-of-objects-by-a-certain-value-within-the-object
                });
                event.add(new Showevent(-2,"",0,0,null));
                return event;
            }

        } else{
            return event;
        }



    }
//    public ArrayList<Showevent> sortAZShowEvent(ArrayList<Showevent> event, final int dir) {
//
//
//    }
    public boolean CanDelMem(int eventid,int id) {
        Sumary su = new Sumary(eventid,this.context,this.context);
        Sumary_obj sumob = su.sumary_result();
        ArrayList<Member> cre = sumob.credit_member;
        ArrayList<Member> de = sumob.dep_member;
    //        Log.d("getmembertran", "arr");
        for(int i = 0;i < cre.size();i++ ){
    //            Log.d("getmembertran", "arr1");
            if(id == cre.get(i).get_id()){
                return FALSE;
            }
        }
        for(int i = 0;i < de.size();i++ ){
    //            Log.d("getmembertran", "arr2" +de.size());
            if(id == de.get(i).get_id()){
                return FALSE;
            }
        }
        return TRUE;
    }


}
