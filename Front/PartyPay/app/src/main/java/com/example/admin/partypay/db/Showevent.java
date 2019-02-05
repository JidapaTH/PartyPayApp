package com.example.admin.partypay.db;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Showevent {
    public Integer id_col;
    public String event_name_col;
    public Date start_time_col;
    public Integer type_col;
    public Integer status_col;
    private boolean mStatusBarShown = false;
    public Showevent(Integer id_col,String event_name_col,Integer type_col, Integer status_col, Date start_time_col ) {
        this.id_col = id_col;
        this.type_col = type_col;
        this.event_name_col = event_name_col;
        this.start_time_col = start_time_col;
        this.status_col = status_col;
    }

    // getter
    public Integer getEvent_id_col(){
        return this.id_col;
    }
    public String getEvent_name_col(){
        return this.event_name_col;
    }
    public Integer getType_id_col(){
        return this.type_col;
    }
    public Integer getEvent_status_col(){
        return this.status_col;
    }
    public Date getEvent_time_col(){
        return this.start_time_col;
    }
    public String getEvent_time_string(){
        if(this.start_time_col != null){
            SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
            String reportDate = dtFormat.format(this.start_time_col);
            return reportDate;
        }else{
            return "";
        }
    }

    //setter
    public void setEvent_id_col(Integer id_col){
         this.id_col=id_col;
    }
    public void setEvent_name_col(String event_name_col){
         this.event_name_col=event_name_col;
    }
    public void setEvent_time_col(Date start_time_col){
        this.start_time_col=start_time_col;
    }
    public void setType_id_col(Integer type_col){
        this.type_col=type_col;
    }
    public void setEvent_status_col(Integer status_col){
        this.status_col=status_col;
    }


    public void toggleStatusBarShown() {
        mStatusBarShown = !mStatusBarShown;
    }
    public boolean getStatusBarShown() {
        return mStatusBarShown;
    }
}