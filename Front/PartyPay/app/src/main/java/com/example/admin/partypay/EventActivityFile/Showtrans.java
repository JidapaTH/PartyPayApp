package com.example.admin.partypay.EventActivityFile;



import com.example.admin.partypay.MemberFile.Member;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by WINDOWS on 25/7/2560.
 */

public class Showtrans {
    private Integer mTransID ;
    private String mTransName ;
    private int mTransType; //0=-,1=0,2=+
    private Double mTransAmount ;
    private Long mDate ;
    private ArrayList<Member> creditor ;
    private ArrayList<Member> deptor ;
    private List<Double> creditor_money ;
    private List<Double> deptor_money ;


    //Alternative constructor method in case there's only one transaction

    public Showtrans (Integer id,String transName, int type, Double transAmount, Long date,ArrayList<Member> creditor,ArrayList<Member> deptor,List<Double> creditor_money,List<Double> deptor_money){
        mTransID = id;
        this.mTransName=transName;
        this.mTransAmount=transAmount;
        this.mTransType=type;
        this.mDate=date;
        this.creditor=creditor;
        this.deptor=deptor;
        this.creditor_money=creditor_money;
        this.deptor_money=deptor_money;

    }

    //Method to get the transaction name (input is order of transaction : start at 0)
    public String getTransName(){
        return this.mTransName;
    }

    //Method to get the transaction amount (input is order of transaction : start at 0)
    public String getTransAmountWithSign(){
        if(this.mTransType==0){
            return "-"+this.mTransAmount;
        }else if(this.mTransType==1){
            return "+"+this.mTransAmount;
        }else{
            return ""+this.mTransAmount;
        }

    }
    public int getTransID() { return this.mTransID; }
    public double getTransType(){
        return this.mTransType;
    }
    public double getTransAmount(){
        return this.mTransAmount;
    }
    public Long getTransDate(){
        return this.mDate;
    }
    public ArrayList<Member> getCreditor(){
        return this.creditor;
    }
    public ArrayList<Member> getDeptor(){
        return this.deptor;
    }
    public List<Double> getCreditor_money(){
        return this.creditor_money;
    }
    public List<Double> getDeptor_money(){
        return this.deptor_money;
    }
    //Edit transaction name (Reminder: position start at 0)
    public boolean setTransName(String newTransName){
        this.mTransName=newTransName;
        return true;
    }

    //Edit transaction amount (Reminder: position start at 0)
    public boolean setTransAmount(double newTransAmount){
        this.mTransAmount=newTransAmount;
        return true;
    }

    public boolean setTransType(double newTransType){
        this.mTransAmount=newTransType;
        return true;
    }
    public void setCreditor(ArrayList<Member> c){
        this.creditor=c;
    }
    public void setDeptor(ArrayList<Member> d){
        this.deptor=d;
    }

}
