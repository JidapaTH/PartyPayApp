package com.example.admin.partypay.EventActivityFile;

import java.util.ArrayList;

public class DayTransaction {
    /**
     * This object contain information of transaction in one day compose of
     * @param transName = Name of the transaction
     * @param transAmount = Amount of current transaction
     * @param transMember = Member name of current transaction
     * Note: transName and transAmount are ArrayList
     */

    // Almost not use

    private ArrayList<String> mTransName = new ArrayList<>();
    private ArrayList<Double> mTransAmount = new ArrayList<>();
    private ArrayList<String> mTransMember = new ArrayList<>();

    //Constructor method
    public DayTransaction( ArrayList<String> transName, ArrayList<Double> transAmount, ArrayList<String> transMember) {
        mTransName = transName;
        mTransAmount = transAmount;
        mTransMember = transMember;
    }

    //Alternative constructor method in case there's only one transaction
    public DayTransaction (String transName, Double transAmount, String transMember){
        mTransName.add(transName);
        mTransAmount.add(transAmount);
        mTransMember.add(transMember);
    }

    //Method to get the transaction name (input is order of transaction : start at 0)
    public String getTransName(int position){
        return mTransName.get(position);
    }

    //Method to get the transaction amount (input is order of transaction : start at 0)
    public double getTransAmount(int position){
        return mTransAmount.get(position);
    }

    public String getMembersName(int position) {
        return mTransMember.get(position);
    }
    //Method to add new transaction into array
    public void addTransaction(String newTransName, Double newTransAmount,String newTransMember){
        mTransName.add(newTransName);
        mTransAmount.add(newTransAmount);
        mTransMember.add(newTransMember);
    }

    //Return current number of transaction
    public int getAmountOfTrans(){
        if (mTransName.size() == mTransAmount.size()){
            return  mTransName.size();
        } else {
            return -1;
        }
    }

    //Edit transaction name (Reminder: position start at 0)
    public boolean setTransName(String newTransName,int position){
        mTransName.set(position,newTransName);
        return true;
    }

    //Edit transaction amount (Reminder: position start at 0)
    public boolean setTransAmount(double newTransAmount,int position){
        mTransAmount.set(position,newTransAmount);
        return true;
    }

    //Edit transaction member (Reminder: position start at 0)
    public boolean setTransMember(String newTransMember,int position){
        mTransMember.set(position,newTransMember);
        return true;
    }
}
