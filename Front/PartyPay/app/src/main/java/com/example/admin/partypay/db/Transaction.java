package com.example.admin.partypay.db;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by WINDOWS on 4/7/2560.
 */

/*
public class Transaction {
    public int id;
    public String name;
    public int type;
    public double money;
    public Transaction (int id,double mo_ney,String name,int type){
        this.id = id;
        this.money = mo_ney;
        this.name = name;
        this.type = type;
    }
    public double[] update (int[] mem_id,double[] mem_bal){
        return mem_bal;
    }
}
*/


public class Transaction {
    public int id;
    public int event_id_link;
    public String name;
    public int type;
    public double amount;
    public ArrayList<Integer> creditor;
    public ArrayList<Integer> deptor;
    public List<Double> credit_money;
    public List<Double> dept_money;
    public int date;
    public String memo;
    //Constructor
    public Transaction(int id, int event_id_link, String name, int type, double amount, ArrayList<Integer> creditor,List<Double> credit_money,
                       ArrayList<Integer> deptor,List<Double> dept_money, int date,String memo) {
        this.id = id;
        this.event_id_link = event_id_link;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.creditor = creditor;
        this.credit_money = credit_money;
        this.deptor = deptor;
        this.dept_money = dept_money;
        this.date = date;
        this.memo = memo;
    }

    //Getter, Setter

    //id
    public int get_id(){
        return this.id;
    }
    public void set_id(int id){
        this.id = id;
    }
    //event_id_link
    public int get_event_id_link(){
        return this.event_id_link;
    }
    public void set_event_id_link(int event_id_link){
        this.event_id_link = event_id_link;
    }
    //name
    public String get_name(){ return this.name;}
    public void set_name(String name){
        this.name = name;
    }
    //type
    public int get_type(){
        return this.type;
    }
    public void set_type(int type){
        this.type = type;
    }
    //amount
    public double get_amount(){
        return this.amount;
    }
    public void set_amount(int amount){
        this.amount = amount;
    }
    //creditor
    public ArrayList<Integer> get_creditor(){
        return this.creditor;
    }
    public void set_creditor(ArrayList<Integer> creditor){
        this.creditor = creditor;
    }
    //deptor
    public ArrayList<Integer> get_deptor(){
        return this.deptor;
    }
    public void set_deptor(ArrayList<Integer> deptor){
        this.deptor = deptor;
    }
    //creator
    public int get_date(){
        return this.date;
    }
    public void set_date(int date){
        this.date =  date;
    }

}
