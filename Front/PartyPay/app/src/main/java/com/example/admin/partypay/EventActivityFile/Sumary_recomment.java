package com.example.admin.partypay.EventActivityFile;

import com.example.admin.partypay.MemberFile.Member;

/**
 * Created by Admin on 9/10/2017.
 */

public class Sumary_recomment {
    private Member deptor = new Member();
    private Member creditor = new Member();
    private double money;
    public Sumary_recomment(Member deptor,Member creditor,double money){
        this.deptor = deptor;
        this.creditor = creditor;
        this.money = money;
    }
}