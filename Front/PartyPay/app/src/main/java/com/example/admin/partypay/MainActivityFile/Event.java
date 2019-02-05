package com.example.admin.partypay.MainActivityFile;

import java.util.ArrayList;
import com.example.admin.partypay.MemberFile.Member;
/**
 * Created by WINDOWS on 13/6/2560.
 */

public class Event {

//    //private variables
//    private int event_id;
//    private String event_name;
//    private int status;
//    private int start_time;
//    private int end_time;
//    private int creator;

    private int event_id;
    private String event_name;
    private int status; // 0=open/1=fav/2=close
    private int type; //category
    private boolean pool; // กองกลาง
    private int start_time;
    private int end_time;
    public ArrayList<Member> member;


    //Default Constructor
    public Event() {

    }
    //Constructor
    public Event(int event_id, String event_name, int status,int type,boolean pool, int start_time,
                 int end_time, ArrayList<Member> member) {

        this.event_id = event_id;
        this.event_name = event_name;
        this.status = status;
        this.type = type;
        this.pool = pool;
        this.start_time = start_time;
        this.end_time = end_time;
        this.member = member;

    }

    //Getter, Setter

    //Event_id
    public int getEvent_id(){
        return this.event_id;
    }
    public void setEvent_id(int id){
        this.event_id = id;
    }
    //Event_name
    public String getEvent_name(){
        return this.event_name;
    }
    public void setEvent_name(String event_name){
        this.event_name = event_name;
    }
    //Type
    public int getEvent_type(){
        return this.type;
    }
    public void setEvent_type(int type){
        this.type = type;
    }
    //Pool
    public boolean getEvent_pool(){
        return this.pool;
    }
    public void setEvent_pool(boolean pool){
        this.pool = pool;
    }
    //Status
    public int getEvent_status(){
        return this.status;
    }
    public void setEvent_status(int status){
        this.status = status;
    }
    //start time
    public int getEvent_start_time(){
        return this.start_time;
    }
    public void setEvent_start_time(int start_time){
        this.start_time = start_time;
    }
    //end time
    public int getEvent_end_time(){
        return this.end_time;
    }
    public void setEvent_end_time(int end_time){
        this.end_time = end_time;
    }
    //creator
    public ArrayList<Member> getEvent_member(){
        return this.member;
    }
    public void setEvent_member(ArrayList<Member> member){
        this.member =  member;
    }
}

