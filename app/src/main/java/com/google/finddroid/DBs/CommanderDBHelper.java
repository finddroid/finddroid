package com.google.finddroid.DBs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommanderDBHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "Commander";
    public static int DB_VERSION = 1;
    //table one var
    public static String DB_TABLE_NAME_COMMANDER_NUMBER = "commander_number";
    public static String TABLE_COMMANDER_ID = "id";
    public static String TABLE_COMMANDER_NUMBER = "number";

    //table two var
    public static String DB_TABLE_NAME_COMMANDER_PASSWORD = "commander_password";
    public static String TABLE_COMMANDER_PASSWORD = "password";

    public CommanderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //create table 1
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+
                        DB_TABLE_NAME_COMMANDER_NUMBER +
                "("+
                        TABLE_COMMANDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        TABLE_COMMANDER_NUMBER + " TEXT"+
                ")"
                );
        //create table 2
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+
                        DB_TABLE_NAME_COMMANDER_PASSWORD +
                        "("+
                        TABLE_COMMANDER_PASSWORD + " TEXT" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void InsertDataToCommandNumber(String CommanderNumber){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_COMMANDER_NUMBER,CommanderNumber);
        sqLiteDatabase.insert(DB_TABLE_NAME_COMMANDER_NUMBER,null,values);
        sqLiteDatabase.close();
    }
    public  String FetchDataFromCommanderNumber(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_TABLE_NAME_COMMANDER_NUMBER,null);
        String commanderNumber = "";
        while (cursor.moveToNext()){
            commanderNumber = cursor.getString(1);
        }
        db.close();
        return commanderNumber;
    }


    public boolean isEmpty_CommanderNumber(){
        boolean is_empty;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_NAME_COMMANDER_NUMBER,null);
        if(cursor.moveToNext()){
            is_empty=false;
        }else {
            is_empty=true;
        }
        return is_empty;
    }
    public boolean CheckNumber(String number){
        boolean access = this.FetchDataFromCommanderNumber().equals(number); // it chack number is in db if yes return true else false
        return access;
    }

    public void DeleteDataFromCommanderNumber(){
        SQLiteDatabase db = this.getWritableDatabase();
        // this delete all items from  table
        db.execSQL("DELETE FROM "+DB_TABLE_NAME_COMMANDER_NUMBER);
        db.close();
    }



    // table two methods

    public void InsertDataToCommanderPassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COMMANDER_PASSWORD,password);
        db.insert(DB_TABLE_NAME_COMMANDER_PASSWORD,null,contentValues);
        db.close();
    }
    public String FetchDataFromCommanderPassword(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_TABLE_NAME_COMMANDER_PASSWORD,null);
        String currentPassword = "";
        while (cursor.moveToNext()){
            currentPassword=cursor.getString(0);
        }
        return currentPassword;
    }
    public boolean isEmptyCommanderPassword(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_TABLE_NAME_COMMANDER_PASSWORD,null);
        boolean is_empty;
        if (cursor.moveToNext()){
            is_empty=false;
        }else {
            is_empty=true;
        }
        return is_empty;
    }

    public boolean CheckPassword(String InputPassword){
        String AdminPassword=this.FetchDataFromCommanderPassword();
        boolean passwordMatch;
        if (AdminPassword.equals(InputPassword)){
            passwordMatch=true;
        }else {
            passwordMatch=false;
        }
        return passwordMatch;
    }

    public void DeleteDataFromCommanderPassword(){
        SQLiteDatabase db = this.getWritableDatabase();
        // this delete all items from  table
        db.execSQL("DELETE FROM "+DB_TABLE_NAME_COMMANDER_PASSWORD);
        db.close();
    }



}
