package com.example.admin.partypay.db;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static java.lang.Boolean.*;

import com.example.admin.partypay.MemberFile.Member;

public class GeneralHelper {
    public boolean checkMember(ArrayList<Member> list0){
        Set set = new HashSet<String>();
        for (Member cell :  list0) {
            set.add(cell.get_name());
        }
        if (set.size() < list0.size()) { //check duplicate
            return FALSE;
        } else{
            return TRUE;
        }
    }
    public ArrayList<Integer> checkMemberTran(ArrayList<Integer> list1){
        Set set2 = new HashSet<Integer>(list1);
        return new ArrayList<Integer>(set2); // merge duplicate no.

    }
    ///////////////////////////////////////////////////// JSON /////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////// GSON  ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String jsonCreator_member(ArrayList <Member> mem){
        int size = mem.size();
        //create gson
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Member mem_array[] = new Member[size];
        for(int i =0; i < size ;i++){
            int id = mem.get(i).id;
            String name = mem.get(i).name;
            int status = mem.get(i).status;
            //mem_array[i] = new Member(id,name,status);
            mem_array[i] = new Member(name,status);
        }
        String jsonString = gson.toJson(mem_array);
        return jsonString;
    }

    public ArrayList<Member> jsonTranslator_member(String json_member){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Member mem_array[] = gson.fromJson(json_member, Member[].class);
        // Student student = gson.fromJson(jsonString, Student.class);
        int size = mem_array.length;
        // split to arrayList
        ArrayList<Member> mem = new ArrayList<Member>();
        for(int i=0; i <size ;i++){
            mem.add(mem_array[i]);
        }
        return mem;
    }

    public String jsonCreator_String(ArrayList<String> credit_or_deft){
        // change array to JsonArray
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        int size = credit_or_deft.size();
        String str_array[] = new String[size];
        for(int i =0;i<size;i++){
            str_array[i]=credit_or_deft.get(i);
        }
        String jsonText = gson.toJson(str_array);
        return jsonText;
    }

    public ArrayList<String> jsonTranslator(String json_member){
        //decode
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String str_array[] = gson.fromJson(json_member, String[].class);
        int size = str_array.length;
        // split to arrayList
        ArrayList<String> mem = new ArrayList<String>();
        for(int i=0; i <size ;i++){
            mem.add(str_array[i]);
        }
        return mem;
    }
    ///////////////////////////////////////////////////////////////// new
    public String json_int_creater(ArrayList<Integer> int_arraylist){
        //decode
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        int size = int_arraylist.size();
        int int_array[] = new int[size];
        for(int i =0;i<size ;i++){
            int_array[i]=int_arraylist.get(i);
        }
        String jsonText = gson.toJson(int_array);
        return jsonText;
    }
    public ArrayList<Integer> json_int_translator(String json_int_array){
        //decode
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        int int_array[] = gson.fromJson(json_int_array, int[].class);
        int size = int_array.length;
        // split to arrayList
        ArrayList<Integer> int_arraylist = new ArrayList<Integer>();
        for(int i=0; i <size ;i++){
            int_arraylist.add(int_array[i]);
        }
        return int_arraylist;
    }

    public String json_double_creater(List<Double> double_arraylist){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        int size = double_arraylist.size();
        double double_array[] = new double[size];
        for(int i =0;i<size;i++){
            double_array[i]=double_arraylist.get(i);
        }
        String jsonText = gson.toJson(double_array);
        return jsonText;
    }
    public List<Double> json_double_translator(String json_double_array){
        //decode
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        Log.d("GeneralHelper","json_double_array: "+json_double_array);

        double double_array[] = gson.fromJson(json_double_array, double[].class);

        int size = double_array.length;
        // split to arrayList
        List<Double> double_arraylist = new ArrayList<Double>();
        for(int i=0; i <size ;i++){
            double_arraylist.add(double_array[i]);
        }
        return double_arraylist;
    }
    public int date_str_to_int (String date_str){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        // input 08/18/2017T21:56:00
        String[] date_str_ar = date_str.split("T");
        String date_use = date_str_ar[0]+" "+date_str_ar[1];

        Date date = null;
        try {
            date = formatter.parse(date_use);
        } catch (ParseException e) {
            //handle exception
        }
        long date_int = date.getTime();


        return (int)(date_int/1000);  //editdate
    }
    public String date_int_to_str (int date_int){

        long date_l=(long)(date_int*1000);
        Date dNow = new Date(date_l);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        // output 08/18/2017 21:56
        String date_str_out=dNow.toString();
        return date_str_out;
    }
    public String jsonCreator_Int(ArrayList<Integer> credit_or_deft){
        // change array to JsonArray
        String json = new Gson().toJson(credit_or_deft);

        return json.toString();
    }
    public ArrayList<Integer> jsonTranslator_int(String json_member){
        //decode

        Type list=new TypeToken<ArrayList<Integer>>(){}.getType();
        ArrayList<Integer> list2 = new Gson().fromJson(json_member, list);
        return list2;
    }
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

