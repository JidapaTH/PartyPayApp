package com.example.admin.partypay.EventActivityFile;

import com.example.admin.partypay.MemberFile.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 9/10/2017.
 */
public class Sumary_obj {
	public ArrayList<Member> dep_member = new ArrayList<Member>();
    public ArrayList<Member> credit_member = new ArrayList<Member>();
    public ArrayList<Member> bal_member = new ArrayList<Member>();
    public List<Double> credit_money = new ArrayList<Double>();
    public List<Double> dep_money = new ArrayList<Double>();

    public Sumary_obj(ArrayList<Member> dep_ID,List<Double> dep_MONEY,ArrayList<Member> credit_ID,List<Double> credit_MONEY,ArrayList<Member> bal_ID){
        this.dep_member = dep_ID;
        this.dep_money = dep_MONEY;
        this.credit_member = credit_ID;
        this.credit_money = credit_MONEY;
        this.bal_member = bal_ID;
    }
	public ArrayList<SummaryBar> print_deptor(){
		/*
		public SummaryBar(String name, double amount){
        mMemberName = name;
        mPendingAmount = amount;
        //mPendingType = pendingType;
		}
		*/
		ArrayList<SummaryBar> deptor_array = new ArrayList<SummaryBar>();
		for(int i =0;i < dep_member.size();i++){
			deptor_array.add(new SummaryBar(dep_member.get(i).id,dep_member.get(i).name, dep_money.get(i)));
		}
		return deptor_array;
	}

	public ArrayList<SummaryBar> print_creditor(){

		ArrayList<SummaryBar> creditor_array = new ArrayList<SummaryBar>();
		for(int i =0;i < credit_member.size();i++){
			creditor_array.add(new SummaryBar(credit_member.get(i).id,credit_member.get(i).name, credit_money.get(i)));
		}
		return creditor_array;
	}

	public double print_dep_total(){
        double total = 0;
        for(int i =0;i < dep_money.size();i++){
            total = total+dep_money.get(i);
        }
        return total;
    }

    public double print_credit_total(){
        double total = 0;
        for(int i =0;i < credit_money.size();i++){
            total = total+credit_money.get(i);
        }
        return total;
    }
}