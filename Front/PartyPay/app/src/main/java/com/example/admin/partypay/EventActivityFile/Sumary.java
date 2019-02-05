package com.example.admin.partypay.EventActivityFile;

import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import com.example.admin.partypay.db.DBHelper;
import com.example.admin.partypay.db.EventHelper;
import com.example.admin.partypay.db.GeneralHelper;
import com.example.admin.partypay.db.TranHelper;
import com.example.admin.partypay.db.Transaction;
import com.example.admin.partypay.MemberFile.Member;
import com.example.admin.partypay.MainActivityFile.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static java.lang.Boolean.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
 // change to arraylist money
public class Sumary {
	private ArrayList<Member> all_mem = new ArrayList<Member>();
    private ArrayList<Transaction> all_tran =  new ArrayList<Transaction>();
	public double total = 0;
    private double all_balance[];
	private int sum_event_id = -1;
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Sumary(int event_id,Context context_event,Context context_tran ) {
		// load all member /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		sum_event_id = event_id;
		//DBHelper = new DBHelper();
		EventHelper eve_help = new EventHelper(context_event);
		TranHelper tran_help = new TranHelper(context_tran);
		GeneralHelper gen_help = new GeneralHelper();
		Event eve = new Event();
		eve = eve_help.getEvent(event_id);
		this.all_mem = eve.member; // finished load member
        this.all_balance = new double[all_mem.size()];
		// load all tran /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		String sql =  "SELECT * FROM " + tran_help.TABLE + " WHERE "+ "event_id" + " = " + event_id;
		Cursor  cursor = tran_help.sqlDB.rawQuery(sql,null);

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {

				int tran_id = cursor.getInt(cursor.getColumnIndex("trans_id"));
				String tran_name = cursor.getString(cursor.getColumnIndex("tran_name"));
				int tran_type = cursor.getInt(cursor.getColumnIndex("type"));
				double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
				String json_creditor_id = cursor.getString(cursor.getColumnIndex("creditor"));
				String json_redit_id_money = cursor.getString(cursor.getColumnIndex("creditor_money"));
				String json_deptor_id = cursor.getString(cursor.getColumnIndex("deptor"));
				String json_dept_id_money = cursor.getString(cursor.getColumnIndex("deptor_money"));
				int tran_date = cursor.getInt(cursor.getColumnIndex("tran_date"));
				String tran_memo = cursor.getString(cursor.getColumnIndex("memo"));


				ArrayList<Integer> creditor_id = new ArrayList<Integer>();
				List<Double> credit_id_money = new ArrayList<Double>();
				ArrayList<Integer> deptor_id = new ArrayList<Integer>();
				List<Double> dept_id_money = new ArrayList<Double>();

				creditor_id = gen_help.json_int_translator(json_creditor_id);
				credit_id_money = gen_help.json_double_translator(json_redit_id_money);
				deptor_id = gen_help.json_int_translator(json_deptor_id);
				dept_id_money = gen_help.json_double_translator(json_dept_id_money);

				Transaction load_tran = new Transaction(tran_id,event_id,tran_name,tran_type,amount,creditor_id,credit_id_money,deptor_id,dept_id_money,tran_date,tran_memo);
				this.all_tran.add(load_tran);
				cursor.moveToNext();
			}
		}

		//
		cursor.close();
		eve = null;
		eve_help = null;
        /////////////////////////////// finished load data /////////////////////////////////////////////////////////////////////////////////////////////////////
        // set  0 (zerp in all_balance)
        for(int i = 0;i < this.all_balance.length ;i++){
            this.all_balance[i] = 0;
        }
        // update all tran to balance ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for(int i =0; i < all_tran.size() ;i++){
            ArrayList<Integer> credit = all_tran.get(i).creditor;
            ArrayList<Integer> dep = all_tran.get(i).deptor;
			List<Double> dep_money_use = all_tran.get(i).dept_money;
			List<Double> credit_money_use = all_tran.get(i).credit_money;
			double tran_amount = all_tran.get(i).amount;
            //////////////////////////////////////////////////////////////////////////////////////// paying //////////////////////////////////////////////////////////////////////////////////////
			if (this.all_tran.get(i).type == 0 || this.all_tran.get(i).type == 1 || this.all_tran.get(i).type == 2){
                if(dep.size() > 0 && credit.size() > 0){
                    for(int j = 0; j < dep.size(); j++){
                        int find_index_mem = 0;
                        //find index
                        for(int k = 0; k< this.all_mem.size(); k++){
                            if(this.all_mem.get(k).id == dep.get(j)){
                                find_index_mem = k;
                                break;
                            }
                        }
                        // find index finished
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] - dep_money_use.get(j);
                    }
                    for(int j = 0; j < credit.size(); j++){
                        int find_index_mem = 0;
                        //find index
                        for(int k = 0; k< this.all_mem.size(); k++){
                            if(this.all_mem.get(k).id == credit.get(j)){
                                find_index_mem = k;
                                break;
                            }
                        }
                        // find index finished
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] + credit_money_use.get(j);
                    }
                }else{
                    System.out.print("error in tran type id " + all_tran.get(i).id);
                }
            //////////////////////////////////////////////////////////////////////////////////////// recieve //////////////////////////////////////////////////////////////////////////////////////
			}else{
				System.out.print("error in tran type id " + all_tran.get(i).id);
			}

			if(this.all_tran.get(i).type == 0){
				total = total - tran_amount;
			}else if(this.all_tran.get(i).type == 1){
				total = total + tran_amount;
			}else if(this.all_tran.get(i).type == 2){
				total = total;
			}else{
				System.out.print("error in tran type id " + all_tran.get(i).id);
			}
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void update (Transaction add_tran){
		/*
			// if change to tran id
			// load all tran /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		String sql =  "SELECT * FROM " + tran_help.TABLE + " WHERE "+ "event_id" + " = " + event_id;
		Cursor  cursor = tran_help.sqlDB.rawQuery(sql,null);

		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {

				int tran_id = cursor.getInt(cursor.getColumnIndex("trans_id"));
				String tran_name = cursor.getString(cursor.getColumnIndex("tran_name"));
				int tran_type = cursor.getInt(cursor.getColumnIndex("type"));
				double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
				String json_creditor_id = cursor.getString(cursor.getColumnIndex("creditor"));
				String json_redit_id_money = cursor.getString(cursor.getColumnIndex("creditor_money"));
				String json_deptor_id = cursor.getString(cursor.getColumnIndex("deptor"));
				String json_dept_id_money = cursor.getString(cursor.getColumnIndex("deptor_money"));
				int tran_date = cursor.getInt(cursor.getColumnIndex("tran_date"));
				String tran_memo = cursor.getString(cursor.getColumnIndex("memo"));


				ArrayList<Integer> creditor_id = new ArrayList<Integer>();
				List<Double> credit_id_money = new ArrayList<Double>();
				ArrayList<Integer> deptor_id = new ArrayList<Integer>();
				List<Double> dept_id_money = new ArrayList<Double>();

				creditor_id = gen_help.json_int_translator(json_creditor_id);
				credit_id_money = gen_help.json_double_translator(json_redit_id_money);
				deptor_id = gen_help.json_int_translator(json_deptor_id);
				dept_id_money = gen_help.json_double_translator(json_dept_id_money);

				Transaction load_tran = new Transaction(tran_id,event_id,tran_name,tran_type,amount,creditor_id,credit_id_money,deptor_id,dept_id_money,tran_date,tran_memo);
				this.all_tran.add(load_tran);
				cursor.moveToNext();
			}
		}

		//
		cursor.close();
		eve = null;
		eve_help = null;
		*/
		// add tran
        ArrayList<Integer> credit = add_tran.creditor;
        ArrayList<Integer> dep = add_tran.deptor;
		List<Double> dep_money_use = add_tran.dept_money;
		List<Double> credit_money_use = add_tran.credit_money;
		double tran_amount = add_tran.amount;
		if (add_tran.type == 0 || add_tran.type == 1 || add_tran.type == 2){
                if(dep.size() > 0 && credit.size() > 0){
                    for(int j = 0; j < dep.size(); j++){
                        int find_index_mem = 0;
                        //find index
                        for(int k = 0; k< this.all_mem.size(); k++){
                            if(this.all_mem.get(k).id == dep.get(j)){
                                find_index_mem = k;
                                break;
                            }
                        }
                        // find index finished
                    this.all_balance[find_index_mem] = this.all_balance[find_index_mem] - dep_money_use.get(j);
                    }
                    for(int j = 0; j < credit.size(); j++){
                        int find_index_mem = 0;
                        //find index
                        for(int k = 0; k< this.all_mem.size(); k++){
                            if(this.all_mem.get(k).id == dep.get(j)){
                                find_index_mem = k;
                                break;
                            }
                        }
                        // find index finished
                    this.all_balance[find_index_mem] = this.all_balance[find_index_mem] + credit_money_use.get(j);
                    }
                }else{
                System.out.print("error in tran type id " + add_tran.id);
            }
		}else{
			System.out.print("error in tran type id " + add_tran.id);
        }

		if(add_tran.type == 0){
			total = total - tran_amount;
		}else if(add_tran.type == 1){
			total = total + tran_amount;
		}else if(add_tran.type == 2){
			total = total;
		}else{
			//System.out.print("error in tran type id " + all_tran.get(i).id);
		}
    }
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Sumary_obj sumary_result(){
		ArrayList<Member> dep_member = new ArrayList<Member>();
        ArrayList<Member> credit_member = new ArrayList<Member>();
		ArrayList<Member> bal_member = new ArrayList<Member>();
		List<Double> dep_money = new ArrayList<Double>();
        List<Double> credit_money = new ArrayList<Double>();
		/////
        for(int i = 0 ;i < this.all_mem.size();i++){
            if( this.all_balance[i] > 0 ){
                //ArrayList<Member> all_mem;
                credit_member.add(this.all_mem.get(i));
                credit_money.add(this.all_balance[i]);
            } else if ( this.all_balance[i] < 0 ){
                dep_member.add(this.all_mem.get(i));
                dep_money.add(this.all_balance[i]);
            }else{
                bal_member.add(this.all_mem.get(i));
            }
        }
		/////
		Member credit_mem = new Member();
		Member dep_mem = new Member();
		double ch_money = 0;
		double d_money = 0;
        //sort credit
        for(int i =0; i<credit_member.size() ;i++){
            for(int j=i-1; j>=0 ;j--){
                //compare index j < index j+1
                if(credit_money.get(j) < credit_money.get(j+1)){
                    ch_money  = credit_money.get(j);
                    credit_mem = credit_member.get(j);
                    // index j
                    credit_money.set( j,credit_money.get(j+1) );
                    credit_member.set(j,credit_member.get(j+1));
                    // index j+1
                    credit_money.set( j+1, ch_money );
                    credit_member.set(j+1, credit_mem);
                }
            }
        }
        //sort dep
        for(int i =0; i < dep_member.size() ;i++){
            for(int j=i-1; j>=0 ;j--){
                if(dep_money.get(j) > dep_money.get(j+1)){
                    d_money  = dep_money.get(j);
                    dep_mem = dep_member.get(j);
                    // index j
                    dep_money.set( j,dep_money.get(j+1) );
                    dep_member.set(j,dep_member.get(j+1));
                    // index j+1
                    dep_money.set( j+1, d_money );
                    dep_member.set(j+1, dep_mem);
                }
            }
        }
		/////
		Sumary_obj sumar = new Sumary_obj(dep_member,dep_money,credit_member,credit_money,bal_member);
		return sumar;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<Sumary_recomment> print_recomment(Sumary_obj sumar){
        ArrayList<Member> dep_member = new ArrayList<Member>();
        ArrayList<Member> credit_member = new ArrayList<Member>();
		ArrayList<Member> bal_member = new ArrayList<Member>();
        List<Double> credit_money = new ArrayList<Double>();
        List<Double> dep_money = new ArrayList<Double>();
        ArrayList<Sumary_recomment> result = new ArrayList<Sumary_recomment>();
		// load data
		dep_member = sumar.dep_member;
        credit_member = sumar.credit_member;
		bal_member = sumar.bal_member;
        credit_money = sumar.credit_money;
        dep_money = sumar.dep_money;

		/////////////////////////// could fix it
        // sort finished
        if(credit_member.size() == 0){
            for(int i = 0; i<dep_member.size() ;i++){
                Sumary_recomment only_dep = new Sumary_recomment(dep_member.get(i),null,Math.abs(dep_money.get(i)));
                result.add(only_dep);
            }
        }else if(dep_member.size() == 0){
            for(int i = 0; i<credit_member.size() ;i++){
                Sumary_recomment only_credit = new Sumary_recomment(null,credit_member.get(i),Math.abs(credit_money.get(i)));
                result.add(only_credit);
            }
        }else{
            int a = 0; // for credit
            int b = 0; // for dep
            double res_money = 0; // money_credit - money_dep 
            double pay_money = 0;
            while(true){
                if(a==0 && b==0){
                    res_money = credit_money.get(a) + dep_money.get(b);
                    pay_money = Math.min(Math.abs(dep_money.get(b)), credit_money.get(a));
                    Sumary_recomment normal = new Sumary_recomment(dep_member.get(b),credit_member.get(a),pay_money);
                    result.add(normal);
                    
                }else{
                    if(res_money >= 0){
                        pay_money = Math.min(Math.abs(res_money), credit_money.get(a));
                        res_money = res_money + credit_money.get(a);
                        Sumary_recomment normal = new Sumary_recomment(dep_member.get(b),credit_member.get(a),pay_money);
                        result.add(normal);
                    }else if(res_money < 0){
                        pay_money = Math.min(Math.abs(res_money), Math.abs(dep_money.get(a)));
                        res_money = res_money + dep_money.get(a);
                        Sumary_recomment normal = new Sumary_recomment(dep_member.get(b),credit_member.get(a),pay_money);
                        result.add(normal);
                    }else{
                        System.out.print("System error");
                        break;
                    }
                }
                //change index
                if(res_money >= 0) b++;
                else if(res_money < 0) a++;
                else System.out.print("error in while in sumary result");
                //check break while loop
                if(a == credit_member.size()){
                    for(int i = b; i<dep_member.size() ;i++){
                        // last residual
                            if(res_money != 0){
                                pay_money = res_money;
                                res_money = 0;
                            }else{
                                pay_money = dep_money.get(i);
                            }
                        //
                        Sumary_recomment normal = new Sumary_recomment(dep_member.get(i),null,pay_money);
                        result.add(normal);
                    }
                    break;
                }else if(b == dep_member.size()){
                    for(int i = a;i<credit_member.size();i++){
                        // last residual
                            if(res_money != 0){
                                pay_money = res_money + credit_money.get(i);
                                res_money =0;
                            }else{
                                pay_money = credit_money.get(i);
                            }
                        // 
                        Sumary_recomment normal = new Sumary_recomment(null,credit_member.get(i),pay_money);
                        result.add(normal);
                    }
                    break;
                }
            }
        }
        return result; 
    }
}



