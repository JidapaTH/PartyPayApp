package grouptransactionapp.sqllite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static java.lang.Boolean.*;
/**
 * Created by WINDOWS on 29/6/2560.
 */

public class memberHelper {

    private DBHelper dbh;
    private SQLiteDatabase sqlDB;

    private static final String TABLE = "member_table";
    private class Column {
        private static final String MEMBER_ID = "member_id";
        private static final String NAME = "name";
    }
    public memberHelper(Context context) {
        dbh =  DBHelper.getInstance(context);
        sqlDB = dbh.getWritableDatabase();

    }
    //close connection
    public void close(){
        sqlDB.close();
    }
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_MEM_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                        "%s TEXT NOT NULL );",
//                TABLE, Column.MEMBER_ID,Column.NAME);
//        db.execSQL(CREATE_MEM_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        String DROP_EVENT_TABLE = "DROP TABLE IF EXISTS "+ TABLE;
//
//        db.execSQL(DROP_EVENT_TABLE);
//        onCreate(db);
//    }

    public boolean addMember(String name) {
        Log.d("SQLite: ", "add member");
        if(checkMemberName(name)){
            return FALSE;
        }else {
            try {
                ContentValues values = new ContentValues();
                values.put(Column.NAME, name);
                // Inserting Row
                sqlDB.insert(TABLE, null, values);

            } finally {
                //sqlDB.close();
                //TODO thread problem
            }
            return TRUE;
        }
    }
    public boolean checkMemberName(String name){
        ArrayList<member> foundName = searchMember(name);
        if( foundName.size()==0){
            Log.d("SQLite: ", "new name OK");
            return FALSE;
        } else{
            Log.d("SQLite: ", "repeat name");
            return TRUE;
        }

    }

    public void dropTable(){
        sqlDB.execSQL("DROP TABLE "+ TABLE);
    }

    public void clearALLMember(){
        sqlDB.execSQL("DELETE from "+ TABLE);
    }

    public void clearMember(int id){
        sqlDB.execSQL("DELETE from "+ TABLE + "WHERE MEMBER_ID = " + id);
    }


    // search members
    public ArrayList<member> searchMember() {
        return searchMember( "", 0);
    }
    public ArrayList<member> searchMember(String name) {
        return searchMember( name, 0);
    }
    public ArrayList<member> searchMember(String name,int dir) {
        ArrayList<member> memberList = new ArrayList<member>();
        // Select All Query
        Log.d("SQLite: ", "search member");
        String selectQuery = "SELECT MEMBER_ID, NAME FROM " + TABLE + " WHERE NAME LIKE ? " ;

        Cursor cursor = sqlDB.rawQuery(selectQuery, new String[] { "%"+String.valueOf(name)+"%" });
        Log.d("SQLite: ", "found member");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                member s_member = new member(cursor.getInt(0),cursor.getString(1));
                // Adding contact to list
                memberList.add(s_member);
            } while (cursor.moveToNext());
        }

        //SORT
        ArrayList<member> sortMemberList = new ArrayList<member>();


        sortMemberList = sortAZShowMember(memberList, dir);

        // return contact list
        return sortMemberList;
    }

    public ArrayList<member> sortAZShowMember(ArrayList<member> mem, final int dir) {

        Collections.sort(mem, new Comparator<member>() {
            //override compare function to select sort items
            @Override
            public int compare(member e1, member e2) {
                //compare
                if (dir == 0){
                    //sort by name
                    return e1.get_name().compareTo(e2.get_name());
                } else {
                    //sort by name
                    return e2.get_name().compareTo(e1.get_name());
                }
            }
            //sort resource link
            //https://stackoverflow.com/questions/9109890/android-java-how-to-sort-a-list-of-objects-by-a-certain-value-within-the-object
        });
        return mem;
    }

    //TODO 1 UPDATE
}
