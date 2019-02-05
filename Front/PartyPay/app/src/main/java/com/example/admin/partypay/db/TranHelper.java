package com.example.admin.partypay.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Map;

import java.util.TreeMap;

import com.example.admin.partypay.EventActivityFile.Showtrans;
import com.example.admin.partypay.EventActivityFile.Sumary;
import com.example.admin.partypay.EventActivityFile.Sumary_obj;
import com.example.admin.partypay.MemberFile.Member;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//
public class TranHelper {
    private DBHelper dbh;
    public SQLiteDatabase sqlDB;
    public Context context;
    public String TABLE;
    public static class Column {
        private static String TRANS_ID;
        private static String EVENT_ID_LINK;
        private static String TRANS_NAME;
        private static String TYPE;
        private static String AMOUNT;
        private static String CREDITOR;
        private static String CREDIT_MONEY;
        private static String DEPTOR;
        private static String DEPT_MONEY;
        private static String DATE;
        private static String MEMO;
    }
    public TranHelper(Context context) {
        dbh = DBHelper.getInstance(context);
        sqlDB = dbh.getWritableDatabase();
        TABLE = DBHelper.TRANS_TABLE;
        Column.TRANS_ID = DBHelper.TColumn.TRANS_ID;
        Column.EVENT_ID_LINK = DBHelper.TColumn.EVENT_ID_LINK;
        Column.TRANS_NAME = DBHelper.TColumn.TRANS_NAME;
        Column.TYPE = DBHelper.TColumn.TYPE;
        Column.AMOUNT = DBHelper.TColumn.AMOUNT;
        Column.CREDITOR = DBHelper.TColumn.CREDITOR;
        Column.CREDIT_MONEY = DBHelper.TColumn.CREDIT_MONEY;
        Column.DEPTOR = DBHelper.TColumn.DEPTOR;
        Column.DEPT_MONEY = DBHelper.TColumn.DEPTOR_MONEY;
        Column.DATE = DBHelper.TColumn.DATE;
        Column.MEMO = DBHelper.TColumn.MEMO;
        this.context=context;
    }

    //add Tran
    public int add_payment(String name,int event_id_link,ArrayList<Integer> payer,List<Double> pay_money,ArrayList<Integer> deptor,String date_str,String memo){
        //check_member
        GeneralHelper gh = new GeneralHelper();
        //type payment int = 0
        int type = 0;
        //payer is creditor
        double amount =0;
        for(int i =0; i<pay_money.size() ;i++){
            amount = amount + pay_money.get(i);
        }
        double dep_mon = amount/deptor.size();
        List<Double>   dept_money = new ArrayList<Double>();
        for(int i =0 ; i< deptor.size() ; i++){
            dept_money.add(dep_mon);
        }
        String jsonCredit = gh.json_int_creater(payer);
        String jsonDept = gh.json_int_creater(deptor);
        String jsonCredit_money = gh.json_double_creater(pay_money);
        String jsonDept_money = gh.json_double_creater(dept_money);
        //change date_str to date_int
        int date_int = gh.date_str_to_int (date_str);
        //database
        String sql =String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (%s,'%s',%s,%s,'%s','%s','%s','%s',%s,'%s');",
                TABLE,Column.EVENT_ID_LINK,Column.TRANS_NAME,Column.TYPE,Column.AMOUNT,Column.CREDITOR,Column.CREDIT_MONEY,Column.DEPTOR,Column.DEPT_MONEY,Column.DATE,Column.MEMO,
                event_id_link,name,type,amount,jsonCredit,jsonCredit_money,jsonDept,jsonDept_money,date_int,memo);
        sqlDB.execSQL(sql);// �?�?�?สำหรั�?อั�?ที�?�?ม�?มี�?าร return
        String sql2 = "SELECT MAX("+Column.TRANS_ID+")  FROM "+TABLE;
        Cursor A = sqlDB.rawQuery(sql2,null);
        A.moveToFirst();
        int tran_id = A.getInt(0);
        Log.d("json","id:" + sql);
        return tran_id;
    }

    public int add_recieve(String name,int event_id_link,ArrayList<Integer> reciever,List<Double> rec_money,ArrayList<Integer> creditor,String date_str,String memo){
        //check_member
        // type recieve  1
        int type =1;
        double amount =0;
        for(int i =0; i<rec_money.size() ;i++){
            amount = amount + rec_money.get(i);
        }
        GeneralHelper gh = new GeneralHelper();
        double credit_mon = amount/creditor.size();
        List<Double>   credit_money = new ArrayList<Double>();
        for(int i =0;i < creditor.size(); i++){
            credit_money.add(credit_mon);
        }
        String jsonCredit = gh.json_int_creater(creditor);
        String jsonDept = gh.json_int_creater(reciever);
        String jsonCredit_money = gh.json_double_creater(credit_money);
        String jsonDept_money = gh.json_double_creater(rec_money);//gh.json_double_creater(dep_money);
        //database
         //change date_str to date_int
        int date_int = gh.date_str_to_int (date_str);
        //database
        String sql =String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (%s,'%s',%s,%s,'%s','%s','%s','%s',%s,'%s');",
                TABLE,Column.EVENT_ID_LINK,Column.TRANS_NAME,Column.TYPE,Column.AMOUNT,Column.CREDITOR,Column.CREDIT_MONEY,Column.DEPTOR,Column.DEPT_MONEY,Column.DATE,Column.MEMO,
                event_id_link,name,type,amount,jsonCredit,jsonCredit_money,jsonDept,jsonDept_money,date_int,memo);
        sqlDB.execSQL(sql);// �?�?�?สำหรั�?อั�?ที�?�?ม�?มี�?าร return
        String sql2 = "SELECT MAX("+Column.TRANS_ID+")  FROM "+TABLE;
        Cursor A = sqlDB.rawQuery(sql2,null);
        A.moveToFirst();
        int tran_id = A.getInt(0);
        Log.d("json","id:" + sql);
        return tran_id;
    }

    public int add_tranfer(String name,int event_id_link,double amount,int creditor,int deptor,String date_str,String memo){
        //check_member
        GeneralHelper gh = new GeneralHelper();
        int type =2;
        ArrayList<Integer> cred = new ArrayList<Integer>();
        ArrayList<Integer>  dept = new ArrayList<Integer>();
        List<Double> cred_money = new ArrayList<Double>();
        List<Double>   dept_money = new ArrayList<Double>();
        cred.add(creditor);
        dept.add(deptor);
        cred_money.add(amount);
        dept_money.add(amount);
        String jsonCredit = gh.json_int_creater(cred);
        String jsonDept = gh.json_int_creater(dept);
        String jsonCredit_money = gh.json_double_creater(cred_money);
        String jsonDept_money = gh.json_double_creater(dept_money);
        //database
         //change date_str to date_int
        int date_int =gh.date_str_to_int (date_str);
        //database
        String sql =String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (%s,'%s',%s,%s,'%s','%s','%s','%s',%s,'%s');",
                TABLE,Column.EVENT_ID_LINK,Column.TRANS_NAME,Column.TYPE,Column.AMOUNT,Column.CREDITOR,Column.CREDIT_MONEY,Column.DEPTOR,Column.DEPT_MONEY,Column.DATE,Column.MEMO,
                event_id_link,name,type,amount,jsonCredit,jsonCredit_money,jsonDept,jsonDept_money,date_int,memo);
        sqlDB.execSQL(sql);// �?�?�?สำหรั�?อั�?ที�?�?ม�?มี�?าร return
        String sql2 = "SELECT MAX("+Column.TRANS_ID+")  FROM "+TABLE;
        Cursor A = sqlDB.rawQuery(sql2,null);
        A.moveToFirst();
        int tran_id = A.getInt(0);
        Log.d("json","id:" + sql);
        return tran_id;
    }

    public void edit_payment(int tran_id,String name,int event_id_link,ArrayList<Integer> payer,List<Double> pay_money,ArrayList<Integer> deptor,String date_str,String memo){
        GeneralHelper gh = new GeneralHelper();
        //ArrayList<Integer> check_credit = gh.checkMemberTran(payer);// รวมที�?�?�?ำ
        //ArrayList<Integer> check_dept = gh.checkMemberTran(deptor);// รวมที�?�?�?ำ
        int type =0;
        int date_int = gh.date_str_to_int (date_str);
        double amount =0;
        for(int i =0;i<pay_money.size();i++){
            amount =amount + pay_money.get(i);
        }
        double dep_mon = amount/deptor.size();
        List<Double>   dept_money = new ArrayList<Double>();
        for(int i =0 ; i< deptor.size() ; i++){
            dept_money.add(dep_mon);
        }
        String jsonCredit = gh.json_int_creater(payer);
        String jsonDept = gh.json_int_creater(deptor);
        String jsonCredit_money = gh.json_double_creater(pay_money);
        String jsonDept_money = gh.json_double_creater(dept_money);

        String sql = String.format("UPDATE INTO %s"+"(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (%s,'%s',%s,%s,'%s','%s','%s','%s',%s,'%s') WHERE %s = %s;",
                 TABLE,Column.EVENT_ID_LINK,Column.TRANS_NAME,Column.TYPE,Column.AMOUNT,Column.CREDITOR,Column.CREDIT_MONEY,Column.DEPTOR,Column.DEPT_MONEY,Column.DATE,Column.MEMO,
                event_id_link,name,type,amount,jsonCredit,jsonCredit_money,jsonDept,jsonDept_money,date_int,memo
                ,Column.TRANS_ID,tran_id);
        //UPDATE table_nameSET column1 = value1, column2 = value2...., columnN = valueN WHERE [condition]
        sqlDB.execSQL(sql);
    }

    public void edit_recieve(int tran_id,String name,int event_id_link,ArrayList<Integer> reciever,List<Double> rec_money,ArrayList<Integer> creditor,String date_str,String memo){
        GeneralHelper gh = new GeneralHelper();
        int type =1;
        int date_int = gh.date_str_to_int (date_str);

        double amount =0;
        for(int i =0;i<rec_money.size();i++){
            amount =amount + rec_money.get(i);
        }
        double credit_mon = amount/creditor.size();
        List<Double>   credit_money = new ArrayList<Double>();
        for(int i =0;i < creditor.size(); i++){
            credit_money.add(credit_mon);
        }
        String jsonCredit = gh.json_int_creater(creditor);
        String jsonDept = gh.json_int_creater(reciever);
        String jsonCredit_money = gh.json_double_creater(credit_money);
        String jsonDept_money = gh.json_double_creater(rec_money);

        String sql = String.format("UPDATE INTO %s"+"(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (%s,'%s',%s,%s,'%s','%s','%s','%s',%s,'%s') WHERE %s = %s;",
                TABLE,Column.EVENT_ID_LINK,Column.TRANS_NAME,Column.TYPE,Column.AMOUNT,Column.CREDITOR,Column.CREDIT_MONEY,Column.DEPTOR,Column.DEPT_MONEY,Column.DATE,Column.MEMO,
                event_id_link,name,type,amount,jsonCredit,jsonCredit_money,jsonDept,jsonDept_money,date_int,memo
                ,Column.TRANS_ID,tran_id);
        //UPDATE table_nameSET column1 = value1, column2 = value2...., columnN = valueN WHERE [condition]
        sqlDB.execSQL(sql);
    }

    //////////////////////////////////////////////////////////
    public void edit_tranfer(int tran_id,String name,int event_id_link,double amount,int creditor,int deptor,String date_str,String memo){
        GeneralHelper gh = new GeneralHelper();
        //ArrayList<Integer> check_credit = gh.checkMemberTran(creditor);// รวมที�?�?�?ำ
        //ArrayList<Integer> check_dept = gh.checkMemberTran(deptor);// รวมที�?�?�?ำ
        int type =2;
        int date_int = gh.date_str_to_int (date_str);

        ArrayList<Integer> cred = new ArrayList<Integer>();
        ArrayList<Integer>  dept = new ArrayList<Integer>();
        List<Double> cred_money = new ArrayList<Double>();
        List<Double>   dept_money = new ArrayList<Double>();
        cred.add(creditor);
        dept.add(deptor);
        cred_money.add(amount);
        dept_money.add(amount);
        String jsonCredit = gh.json_int_creater(cred);
        String jsonDept = gh.json_int_creater(dept);
        String jsonCredit_money = gh.json_double_creater(cred_money);
        String jsonDept_money = gh.json_double_creater(dept_money);

        String sql = String.format("UPDATE INTO %s"+"(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (%s,'%s',%s,%s,'%s','%s','%s','%s',%s,'%s') WHERE %s = %s;",
                 TABLE,Column.EVENT_ID_LINK,Column.TRANS_NAME,Column.TYPE,Column.AMOUNT,Column.CREDITOR,Column.CREDIT_MONEY,Column.DEPTOR,Column.DEPT_MONEY,Column.DATE,Column.MEMO,
                event_id_link,name,type,amount,jsonCredit,jsonCredit_money,jsonDept,jsonDept_money,date_int,memo
                ,Column.TRANS_ID,tran_id);
        //UPDATE table_nameSET column1 = value1, column2 = value2...., columnN = valueN WHERE [condition]
        sqlDB.execSQL(sql);
    }


    public void close(){
        sqlDB.close();
    }
    public void dropTable(){
        sqlDB.execSQL("DROP TABLE "+ TABLE);
    }

    public ArrayList<Member> matchMember(String json, ArrayList<Member> mem){

        GeneralHelper gh = new GeneralHelper();

        ArrayList<Integer> id = gh.jsonTranslator_int(json);
        ArrayList<Member> Member = new ArrayList<>();
//        if(mem.size()>1) {
//            Collections.sort(mem, new Comparator<Member>() {
//                @Override
//                public int compare(Member e1, Member e2) {
//                    return Integer.valueOf(e1.get_id()).compareTo(Integer.valueOf(e2.get_id()));
//                }
//            });
//        }
//        if(id.size()>1) {
//            Collections.sort(id, new Comparator<Integer>() {
//                @Override
//                public int compare(Integer e1, Integer e2) {
//                    //compare
//                    return e1.compareTo(e2);
//                }
//            });
//        }


        for(int i=0;i < id.size();i++ ){
            for(int j=0;j < mem.size();j++ ){
            if(mem.get(j).get_id()==id.get(i)){
                    Member.add(new Member(id.get(i),mem.get(j).get_name(),1));
            }
        }
        }

        Log.d("aftermemsize", " " + Member.size());
        return Member;
    }
    public Overview searchTrans(int id){
        return searchTrans(id,"","");
    }
    public Overview searchTrans(int id,int type){
        return searchTrans(id,"",""+type);
    }
    public Overview searchTrans(int id,String name,int type){
        return searchTrans(id,name,""+type);
    }
    public Overview searchTrans(int id,String name,String type){


//        if(new EventHelper(context).CanDelMem(id,6)){
//            Log.d("getmembertran", "CAN");
//        }else{
//            Log.d("getmembertran", "CANNOT");
//        }
//        get_tran(id);

        GeneralHelper gh = new GeneralHelper();
        Map<String, ArrayList<Showtrans>> transList = new LinkedHashMap<String, ArrayList<Showtrans>>();
        Overview ov ;
        //String selectQuery = "SELECT " + Column.TRANS_ID + ", " + Column.TRANS_NAME + ", "+ Column.TYPE + ", " + Column.AMOUNT +", "+ Column.DATE +", "+Column.CREDITOR+", "+Column.DEPTOR +", "+Column.CREDIT_MONEY+", "+Column.DEPT_MONEY+ " FROM " + TABLE + " WHERE " + Column.TRANS_NAME + " LIKE ? AND "+Column.TYPE +" LIKE ?"  ;
        //Cursor cursor = sqlDB.rawQuery(selectQuery, new String[] { "%"+String.valueOf(name)+"%","%"+String.valueOf(type)+"%" });

        String selectQuery = "SELECT " + Column.TRANS_ID + ", " + Column.TRANS_NAME + ", "+ Column.TYPE + ", " + Column.AMOUNT +", "+ Column.DATE +", "+Column.CREDITOR+", "+Column.DEPTOR +", "+Column.CREDIT_MONEY+", "+Column.DEPT_MONEY+ " FROM " + TABLE + " WHERE " + Column.TRANS_NAME + " LIKE ? AND "+Column.TYPE +" LIKE ?  AND "+Column.EVENT_ID_LINK +" = ?"  ;
        Cursor cursor = sqlDB.rawQuery(selectQuery, new String[] { "%"+String.valueOf(name)+"%","%"+String.valueOf(type)+"%",String.valueOf(id) });
        EventHelper eh = new EventHelper(context);

        ArrayList<Member> mem = eh.getEvent(id).getEvent_member();

        if(mem.size()>0) {
            if (cursor.moveToFirst()) {
                do {

                    Date dt = new java.util.Date(((long) cursor.getInt(4)* 1000) );
                    //Log.d("GeneralHelper","dsssssssssss: "+((long) cursor.getInt(4)* 1000));
                    SimpleDateFormat dtFormat = new SimpleDateFormat("dd-MM-yy");
                    String reportDate = dtFormat.format(dt);
                    // Adding contact to list

                    String key = reportDate;
                    if (!transList.containsKey(key))
                        transList.put(key, new ArrayList<Showtrans>());


                    ArrayList<Member> cre = matchMember(cursor.getString(5), mem);
                    ArrayList<Member> de = matchMember(cursor.getString(6), mem);


                    List<Double> cre_m = gh.json_double_translator(cursor.getString(7));
                    List<Double> de_m = gh.json_double_translator(cursor.getString(8));
//                    Collections.sort(de_m, new Comparator<Double>() {
//                        //override compare function to select sort items
//                        @Override
//                        public int compare(Double e1, Double e2) {
//                            return e1.compareTo(e2);
//                        }
//                   });
//                    Collections.sort(cre_m, new Comparator<Double>() {
//                        //override compare function to select sort items
//                        @Override
//                        public int compare(Double e1, Double e2) {
//                            return e1.compareTo(e2);
//                        }
//                    });
                    Log.d("getmembertran", "cre_size " + cre.size()+", cre_m_size " + cre_m.size());
//                    for(int i=0;i<=(cre.size()-1);i++){
//                        Log.d("getmembertran", "" + i);
//                        Log.d("getmembertran", "name: " + cre.get(i).get_name() );
//                        Log.d("getmembertran", "price: " + cre_m.get(i));
//                    }



                    transList.get(key).add(new Showtrans(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getDouble(3), cursor.getLong(4), cre, de,cre_m,de_m));
                } while (cursor.moveToNext());

                ArrayList<String> transdate = new ArrayList<>(transList.keySet());
                ArrayList<ArrayList<Showtrans>> trans = new ArrayList<ArrayList<Showtrans>>(transList.values());
                System.out.print("size " + transdate.get(0) + "\n");
                ov = new Overview(transdate,trans);
            } else {
                ov = new Overview(new ArrayList<>(transList.keySet()),new ArrayList<ArrayList<Showtrans>>(transList.values()));
            }
            //how to call
//        for (Map.Entry<String, ArrayList<Transaction>> entry : transList.entrySet())
//        {
//            System.out.print(entry.getKey()+"\n");
//            for (Transaction a : entry.getValue()) {
//                String logcat = "Date " + a.get_date() +" Name: " + a.get_name() + "\n";
//                // Writing Contacts to log
//                System.out.print(logcat);
//            }
//        }

            //Get data from map
            //List<String> keylist = new ArrayList<String>(transList.keyset());
            //transList.get("key");
        }else{

            ov = new Overview(new ArrayList<>(transList.keySet()),new ArrayList<ArrayList<Showtrans>>(transList.values()));
        }

        return ov;
    }

    public ArrayList<Transaction> get_tran(int id) {
        GeneralHelper gh = new GeneralHelper();
        String selectQuery = "SELECT " + Column.TRANS_ID + ", " + Column.EVENT_ID_LINK + ", " + Column.TRANS_NAME + ", " + Column.TYPE + ", " + Column.AMOUNT + ", " + Column.CREDITOR + ", " + Column.CREDIT_MONEY + ", " + Column.DEPTOR + ", " + Column.DEPT_MONEY + ", " + Column.DATE + ", " + Column.MEMO + " FROM " + TABLE + " WHERE " + Column.EVENT_ID_LINK + " = ?";
        ArrayList<Transaction> tran = new ArrayList<Transaction>();
        Cursor cursor = sqlDB.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            do {
                ArrayList cre = gh.jsonTranslator_int(cursor.getString(5));
                List cre_m = gh.json_double_translator(cursor.getString(6));
                ArrayList de = gh.jsonTranslator_int(cursor.getString(7));
                List de_m = gh.json_double_translator(cursor.getString(8));
                tran.add(new Transaction(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getDouble(4), cre, cre_m,
                        de, de_m, cursor.getInt(9), cursor.getString(10)));
            }while (cursor.moveToNext());
        }
//
//        for (int i = 0; i < tran.size(); i++) {
//            Log.d("TranHelpersssss",""+tran.get(i).get_name());
//            Log.d("TranHelpersssss",""+tran.get(i).dept_money.size());
//        }
        return tran;
    }

    public Overview sortOverviewByDate(Overview ov,boolean dir){
        Map<Date,ArrayList<Showtrans>> map = new LinkedHashMap<Date,ArrayList<Showtrans>>();
        Overview ove ;
        if(ov.getsize()>0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            for (int i = 0; i < ov.getDate().size(); i++) {
                Date date = null;
                try {
                    date = new java.sql.Date(dateFormat.parse(ov.getDate().get(i)).getTime());
                } catch (ParseException e) {
                    //handle exception
                }
                map.put(date, ov.getTrans().get(i));
            }
            Map<Date, ArrayList<Showtrans>> treeMap;
            if (dir) {
                treeMap = new TreeMap<Date, ArrayList<Showtrans>>(map);
            } else {
                treeMap = new TreeMap(Collections.reverseOrder());
                treeMap.putAll(map);
            }

            ArrayList<String> transdate = new ArrayList<>();
            for (Date date : treeMap.keySet()) {
                transdate.add(dateFormat.format(date));
            }
            ArrayList<ArrayList<Showtrans>> trans = new ArrayList<ArrayList<Showtrans>>(treeMap.values());
            ove = new Overview(transdate, trans);
            return ove;
        } else{
            return ov;
        }
    }
    public ArrayList<Showtrans> sortOverviewByAZ(ArrayList<Showtrans> st, final boolean dir){
        if(st.size()>0) {
                Collections.sort(st, new Comparator<Showtrans>() {
                    //override compare function to select sort items
                    @Override
                    public int compare(Showtrans e1, Showtrans e2) {
                        //compare
                        if (dir){
                            //sort by name
                            //int comparison;
                            int comparison = e1.getTransName().compareTo(e2.getTransName());
                            return comparison;
                        } else {
                            //sort by name
                            int comparison = e2.getTransName().compareTo(e1.getTransName());
                            return comparison;
                        }
                    }
                    //sort resource link
                    //https://stackoverflow.com/questions/9109890/android-java-how-to-sort-a-list-of-objects-by-a-certain-value-within-the-object
                });
            return st;
        }else{
            return st;
        }
    }


}





//////////////////////////////////////

///////////////////////////////////////


