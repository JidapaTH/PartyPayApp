package com.example.admin.partypay.MemberFile;

import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

//replace in each project name

import java.util.concurrent.atomic.AtomicInteger;

public class Member {
    private static final AtomicInteger count = new AtomicInteger(0);
    public int id;
    public String name;
    public int status;
    public Member() {

    }
    // public boolean sex; // M =1, F=0
    // public String tel;
    // public String facebook;
    // public String email;
    ///1/////////////////////////////////////////////////////////// set command /////////////////////////////////////////////////////
    public Member (String name){
        this.name = name;
        this.status = 0;
        this.id = count.incrementAndGet(); ;
    }

    public Member (String name,int status){
        this.name = name;
        this.status = status;
        this.id = count.incrementAndGet(); ;
    }
    public Member(int id,String name,int status){

        this.name = name;
        this.status = status;
        this.id = id; ;
    }
    public void change_name(String name){
        this.name = name;
    }
    public void change_status(int s){
        this.status = s;
    }
    public void change_cc(int id){
        this.id = id;
    }

    public String get_name(){
        return this.name;
    }
    public int get_status(){
        return this.status;
    }
    public int get_id(){
        return this.id;
    }
    public void reset_id(){count.set(0);}
}

//public class member {
//
//    public String name;
//    public int status;
//    public int changecheck;
//    // public boolean sex; // M =1, F=0
//    // public String tel;
//    // public String facebook;
//    // public String email;
//    ///1/////////////////////////////////////////////////////////// set command /////////////////////////////////////////////////////
//    public member (int id,String n){
//        this.name = n;
//        this.id = id;
//        //database
//    }
//    public void change_name(String name){
//        this.name = name;
//        //database
//    }
//    public void change_id(int id){
//        this.id = id;
//        // data base
//    }
//    public int get_id(){
//        return this.id;
//    }
//    public String get_name(){
//        return this.name;
//    }
//
//}
