package com.example.vescalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {
    public static  final String DATABASE_NAME =  "Events.db";
    public static  final String TABLE_NAME =  "MyEvents";
    public static  final String COL_1=  "ID";
    public static  final String COL_2 =  "TITLE";
    public static  final String COL_3 =  "DESCRIPTION";
    public static  final String COL_4 =  "EVENT_DATE";
    public static  final String COL_5 =  "EVENT_TIME";




    public SqliteDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table "+ TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT ,TITLE TEXT, DESCRIPTION TEXT, EVENT_DATE TEXT, EVENT_TIME TEXT) ");
        Log.w("Oncreate Warning", "Oncreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.w("This is Upgrade warnnig", "onUpgrade: ");
    }



    public boolean insertDate(String title, String desp, String date, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.w("Thi is Test", "title: "+title+", Desp:"+desp+", Date:"+date+", Time:"+time);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, title);
        contentValues.put(COL_3, desp);
        contentValues.put(COL_4, date);
        contentValues.put(COL_5, time);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

}