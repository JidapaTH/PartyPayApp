package com.example.admin.partypay.EventActivityFile;

import static android.R.attr.name;

/**
 * Created by Admin on 6/21/2017.
 */

public class SummaryBar {

    /**
     * This object contain summary data of creditors/debtors
     * @param memberID = ID of member on the list
     * @param memberName = Name of member on the list
     * @param pendingAmount = Current money pending on this event (can be negative/positive)
     *
     */

    private int mMemberID;
    private String mMemberName;
    private double mPendingAmount;
    //private boolean mPendingType = false;

    public SummaryBar(int id, String name, double amount){
        mMemberID = id;
        mMemberName = name;
        mPendingAmount = amount;
        //mPendingType = pendingType;
    }

    public int getMemberID() { return  mMemberID;}
    // Method to get name of debtor/creditor
    public String getMemberName(){
        return mMemberName;
    }

    // Method to get name of debtor/creditor
    public double getPendingAmount(){
        return mPendingAmount;
    }

    // Method to get name of debtor/creditor
//    public boolean getPendingType(){
//        return mPendingType;
//    }


}
