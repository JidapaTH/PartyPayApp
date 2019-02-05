package com.example.admin.partypay.TransactionFile;

/**
 * Created by Admin on 7/28/2017.
 */

public class PayerList {

    /**
     * This object contains necessary information for AddTransactionActivity: Payer/Receiver views
     * @param mPosition is position of members who is selected to be paid/received
     * @param mMember is id of members who is selected to be paid/received
     * @param mAmount is amount of money to be paid/received
     */

    private int mPosition;
    private int mMember;
    private double mAmount;

    public PayerList() {
        mPosition = 0;
        mMember = 0;
        mAmount = 0;
    }

    public PayerList(int name) {
        mPosition = 0;
        mMember = name;
        mAmount = 0;
    }

    public PayerList(int name, Double amount) {
        mPosition = 0;
        mMember = name;
        mAmount = amount;
    }

    public int getMemberName() { return mMember;}
    public int getMemberPos() { return mPosition;}
    public double getAmount() {return mAmount;}
    public void setMemberName(int member) {mMember = member;}
    public void setMemberPos(int Pos) {mPosition = Pos;}
    public void setAmount(double amount) {mAmount = amount;}
}
