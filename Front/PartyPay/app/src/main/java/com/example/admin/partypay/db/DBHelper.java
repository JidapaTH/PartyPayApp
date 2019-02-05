package com.example.admin.partypay.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.util.Log;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.Date;

/**
 * Created by WINDOWS on 13/6/2560.
 */

public class DBHelper extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;
    private static DBHelper instance;

    //Database
    private static final String DATABASE_NAME = "party_pay.db";
    private static final int DATABASE_VERSION = 9;
    public static final String EVENT_TABLE = "events_table";
    public static final String MEMBER_TABLE = "member_table";
    public static final String TRANS_TABLE = "transact_table";

    public class EColumn {
        public static final String EVENT_ID = "event_id";
        public static final String EVENT_NAME = "event_name";
        public static final String TYPE = "type";
        public static final String POOL = "pool";
        public static final String STATUS = "status";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String MEMBER = "member";
    }

    //CREATE
    public class MColumn {
        private static final String MEMBER_ID = "member_id";
        private static final String NAME = "name";
    }

    public class TColumn {
        public static final String TRANS_ID = "trans_id";
        public static final String EVENT_ID_LINK = "event_id";
        public static final String TRANS_NAME = "tran_name";
        public static final String TYPE = "type";
        public static final String AMOUNT = "amount";
        public static final String CREDITOR = "creditor";
        public static final String CREDIT_MONEY = "creditor_money";
        public static final String DEPTOR = "deptor";
        public static final String DEPTOR_MONEY = "deptor_money";
        public static final String DATE = "tran_date";
        public static final String MEMO = "memo";
    }
    private static String CREATE_EVENT_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT NOT NULL,%s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL,"+
                    " %s INTEGER DEFAULT CURRENT_TIMESTAMP NOT NULL, %s INTEGER, %s TEXT );",
            EVENT_TABLE, EColumn.EVENT_ID,EColumn.EVENT_NAME,EColumn.TYPE,EColumn.POOL, EColumn.STATUS, EColumn.START_TIME,
            EColumn.END_TIME, EColumn.MEMBER);
    private static String CREATE_MEM_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "%s TEXT NOT NULL );",
            MEMBER_TABLE, MColumn.MEMBER_ID,MColumn.NAME);
    private static String CREATE_TRANS_TABLE = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s INTEGER NOT NULL,%s TEXT NOT NULL,%s INTEGER NOT NULL, %s REAL NOT NULL, %s TEXT, %s TEXT,%s TEXT, %s TEXT,"+
                    " %s INTEGER DEFAULT CURRENT_TIMESTAMP NOT NULL, %s TEXT );",
            TRANS_TABLE,TColumn.TRANS_ID,TColumn.EVENT_ID_LINK,TColumn.TRANS_NAME,TColumn.TYPE,TColumn.AMOUNT,TColumn.CREDITOR,TColumn.CREDIT_MONEY,TColumn.DEPTOR,TColumn.DEPTOR_MONEY,TColumn.DATE,TColumn.MEMO );

    //DROP
    private static String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS "+ EVENT_TABLE;
    private static String DROP_MEMBER_TABLE = "DROP TABLE IF EXISTS "+ MEMBER_TABLE;
    private static String DROP_TRANS_TABLE = "DROP TABLE IF EXISTS "+ TRANS_TABLE;

    //TODO Concurrent Database Access
//    public static synchronized DBHelper getInstance(Context context)
//    {
//        if(instance == null)
//        {
//            instance = new DBHelper(context);
//        }
//
//        return instance;
//    }
//    public SQLiteDatabase getDatabase() {
//        return new DBHelper.getWritableDatabase();
//    }

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        Log.d("SQLite: ", CREATE_TRANS_TABLE);

        db.execSQL(CREATE_EVENT_TABLE);
        db.execSQL(CREATE_MEM_TABLE);
        db.execSQL(CREATE_TRANS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_EVENT_TABLE);
        db.execSQL(DROP_MEMBER_TABLE);
        db.execSQL(DROP_TRANS_TABLE);
        onCreate(db);
    }

    public void close(){
        sqLiteDatabase.close();
    }





}

