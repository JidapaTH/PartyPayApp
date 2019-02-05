
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Lenovo
 */
public class Summary {
    private ArrayList<Member> all_mem;
    private ArrayList<Transaction> all_tran;
    private double all_balance[];
    public Summary(ArrayList<Transaction> all_tran,ArrayList<Member> all_member) {
        // load all tran and member /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.all_balance = new double[all_member.size()];
        this.all_mem = all_member;
        this.all_tran = all_tran;
        // set  0 (zerp in all_balance)
        for(int i = 0;i < this.all_balance.length ;i++){
            this.all_balance[i] = 0;
        }
        // update all tran to balance ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        for(int i =0; i < all_tran.size() ;i++){
            ArrayList<Integer> dep = all_tran.get(i).creditor;
            ArrayList<Integer> credit = all_tran.get(i).deptor;
            ////////////////////////////////////////////////////////////////////////////////////////tranfer //////////////////////////////////////////////////////////////////////////////////////
            if (this.all_tran.get(i).type == 1){ 
                if(dep.size() > 0 && credit.size() > 0){
                    double money_dep = all_tran.get(i).money/dep.size();
                    double money_credit = all_tran.get(i).money/credit.size();
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
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] - money_dep;
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
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] + money_credit;
                    }
                }else{
                    System.out.print("error in tran type id " + all_tran.get(i).id);
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////// paying //////////////////////////////////////////////////////////////////////////////////////
            else if (this.all_tran.get(i).type == 2){ 
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
                if(dep.size() > 0 && credit.size() > 0){
                    double money_dep = all_tran.get(i).money/dep.size();
                    double money_credit = all_tran.get(i).money/credit.size();
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
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] - money_dep;
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
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] + money_credit;
                    }
                }else{
                    System.out.print("error in tran type id " + all_tran.get(i).id);
                }
                //////////////////////////// finish 
            }
            //////////////////////////////////////////////////////////////////////////////////////// recieve //////////////////////////////////////////////////////////////////////////////////////
            else if (this.all_tran.get(i).type == 3){ 
                if(dep.size() > 0 && credit.size() > 0){
                    double money_dep = all_tran.get(i).money/dep.size();
                    double money_credit = all_tran.get(i).money/credit.size();
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
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] - money_dep;
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
                        this.all_balance[find_index_mem] = this.all_balance[find_index_mem] + money_credit;
                    }
                }else{
                    System.out.print("error in tran id =  " + all_tran.get(i).id);
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////// change balance to summary result
        
    }
    public void update (Transaction add_tran){
        // add tran 
        
        // update balance
    }
    public ArrayList<Summary_result> print_result(){
        ArrayList<Integer> dep_id = new ArrayList<Integer>();
        ArrayList<Integer> credit_id = new ArrayList<Integer>();
        ArrayList<Double> credit_money = new ArrayList<Double>();
        ArrayList<Double> dep_money = new ArrayList<Double>();
        ArrayList<Summary_result> result = new ArrayList<Summary_result>();
        
        for(int i = 0 ;i < this.all_mem.size();i++){
            if( this.all_balance[i] > 0 ){
                //ArrayList<Member> all_mem;
                credit_id.add(this.all_mem.get(i).id);
                credit_money.add(this.all_balance[i]);
            } else if ( this.all_balance[i] < 0 ){
                dep_id.add(this.all_mem.get(i).id);
                dep_money.add(this.all_balance[i]);
            }else{
                bal_id.add(this.all_mem.get(i).id);
            }
        }
        //sort credit
        for(int i =0; i<credit_id.size() ;i++){
            for(int j=i-1; j>=0 ;j--){
                //compare index j < index j+1
                if(credit_money.get(j) < credit_money.get(j+1)){
                    double ch_money  = credit_money.get(j);
                    int ch_id = credit_id.get(j);
                    // index j
                    credit_money.set( j,credit_money.get(j+1) );
                    credit_id.set(j,credit_id.get(j+1));
                    // index j+1
                    credit_money.set( j+1, ch_money );
                    credit_id.set(j+1, ch_id);
                }
            }
        }
        //sort dep
        for(int i =0; i<dep_id.size() ;i++){
            for(int j=i-1; j>=0 ;j--){
                if(dep_money.get(j) > dep_money.get(j+1)){
                    double ch_money  = dep_money.get(j);
                    int ch_id = dep_id.get(j);
                    // index j
                    dep_money.set( j,dep_money.get(j+1) );
                    dep_id.set(j,dep_id.get(j+1));
                    // index j+1
                    dep_money.set( j+1, ch_money );
                    dep_id.set(j+1, ch_id);
                }
            }
        }
        // sort finished
        if(credit_id.size() == 0){
            for(int i = 0; i<dep_id.size() ;i++){
                Summary_result only_dep = new Summary_result(dep_id.get(i),-1,Math.abs(dep_money.get(i)));
                result.add(only_dep);
            }
        }else if(dep_id.size() == 0){
            for(int i = 0; i<credit_id.size() ;i++){
                Summary_result only_credit = new Summary_result(-1,credit_id.get(i),Math.abs(credit_money.get(i)));
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
                    Summary_result normal = new Summary_result(dep_id.get(b),credit_id.get(a),pay_money);
                    result.add(normal);
                    
                }else{
                    if(res_money >= 0){
                        pay_money = Math.min(Math.abs(res_money), credit_money.get(a));
                        res_money = res_money + credit_money.get(a);
                        Summary_result normal = new Summary_result(dep_id.get(b),credit_id.get(a),pay_money);
                        result.add(normal);
                    }else if(res_money < 0){
                        pay_money = Math.min(Math.abs(res_money), Math.abs(dep_money.get(a)));
                        res_money = res_money + dep_money.get(a);
                        Summary_result normal = new Summary_result(dep_id.get(b),credit_id.get(a),pay_money);
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
                if(a == credit_id.size()){
                    for(int i = b; i<dep_id.size() ;i++){
                        // last residual
                            if(res_money != 0){
                                pay_money = res_money;
                                res_money = 0;
                            }else{
                                pay_money = dep_money.get(i);
                            }
                        //
                        Summary_result normal = new Summary_result(dep_id.get(i),-1,pay_money);
                        result.add(normal);
                    }
                    break;
                }else if(b == dep_id.size()){
                    for(int i = a;i<credit_id.size();i++){
                        // last residual
                            if(res_money != 0){
                                pay_money = res_money + credit_money.get(i);
                                res_money =0;
                            }else{
                                pay_money = credit_money.get(i);
                            }
                        // 
                        Summary_result normal = new Summary_result(-1,credit_id.get(i),pay_money);
                        result.add(normal);
                    }
                    break;
                }
            }
        }
        return result; 
    }
}

public class Summary_result {
    private int deptor;
    private int creditor;
    private double money;
    public Summary_result(int deptor,int creditor,double money){
        this.deptor = deptor;
        this.creditor = creditor;
        this.money = money;
    }
}