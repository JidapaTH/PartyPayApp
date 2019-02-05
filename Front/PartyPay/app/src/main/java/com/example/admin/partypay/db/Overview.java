package com.example.admin.partypay.db;

import com.example.admin.partypay.EventActivityFile.Showtrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WINDOWS on 3/8/2560.
 */

public class Overview {
    private ArrayList<String> date ;
    private ArrayList<ArrayList<Showtrans>> showtrans ;

    public Overview (ArrayList<String> date, ArrayList<ArrayList<Showtrans>> showtrans){
        this.date=date;
        this.showtrans=showtrans;

    }
    public ArrayList<String> getDate(){
        return this.date;
    }
    public ArrayList<ArrayList<Showtrans>> getTrans(){
        return this.showtrans;
    }
    public void setDate(ArrayList<String> date){
        this.date = date;
    }
    public void setTrans(ArrayList<ArrayList<Showtrans>> showtrans){
        this.showtrans=showtrans;
    }
    public int getsize(){
        return this.date.size();
    }
}
