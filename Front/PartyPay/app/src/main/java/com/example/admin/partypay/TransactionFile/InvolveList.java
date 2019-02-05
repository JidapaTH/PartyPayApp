package com.example.admin.partypay.TransactionFile;

/**
 * Created by Admin on 7/31/2017.
 */

public class InvolveList {

    private int mID;
    private String mName;
    private boolean mChecked;

    public InvolveList(int i,String n){
        mID = i;
        mName = n;
        mChecked = false;
    }

    public InvolveList(String n,boolean c){
        mName = n;
        mChecked = c;
    }

    public int getID(){return  mID;}
    public String getName(){return mName;}
    public boolean getChecked(){return mChecked;}
    public void setID(int i){mID = i;}
    public void setName(String s){mName = s;}
    public void setChecked(boolean c){mChecked = c;}

}
