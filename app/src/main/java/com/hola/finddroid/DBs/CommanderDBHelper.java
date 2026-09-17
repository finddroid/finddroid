package com.hola.finddroid.DBs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** In this file make a DB (commander) and hold auth password & user/number/unique_id
 * Create commander DB to hold the commander name/user/number/unique_id
 * Auth commander and set value in db if the commander give the right password
 * DB Name (Commander)
 * Table Name (commander_number,commander_password)
 * commander_number Header (id,commander_number)
 * commander_password Header(password)
 *
 *
 * Command password is the password that user input when setup the app
 *  **/
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

        //create table 1 (Commander_number)
        sqLiteDatabase.execSQL(
                "CREATE TABLE "+
                        DB_TABLE_NAME_COMMANDER_NUMBER +
                "("+
                        TABLE_COMMANDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        TABLE_COMMANDER_NUMBER + " TEXT"+
                ")"
                );
        //create table 2 (Commander_password)
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
    /** Insert data to commander_number table **/
    public void InsertDataToCommandNumber(String CommanderNumber){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_COMMANDER_NUMBER,CommanderNumber);
        sqLiteDatabase.insert(DB_TABLE_NAME_COMMANDER_NUMBER,null,values);
        sqLiteDatabase.close();
    }
    /** Fetch data from commander number table **/
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
    /** auth the number of args is same which table have **/
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
    /** Check the message (the user who give command) in the table if it return true else false**/
    public boolean CheckNumber(String number){
        boolean access = this.FetchDataFromCommanderNumber().equals(number); // it chack number is in db if yes return true else false
        return access;
    }
    /** Delete Data from commander number table **/
    public void DeleteDataFromCommanderNumber(){
        SQLiteDatabase db = this.getWritableDatabase();
        // this delete all items from  table
        db.execSQL("DELETE FROM "+DB_TABLE_NAME_COMMANDER_NUMBER);
        db.close();
    }



    /** table 2 (commander_password) config **/
    /** insert data to commander password table **/
    public void InsertDataToCommanderPassword(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COMMANDER_PASSWORD,password);
        db.insert(DB_TABLE_NAME_COMMANDER_PASSWORD,null,contentValues);
        db.close();
    }
    /** fetch data from commander password table **/
    public String FetchDataFromCommanderPassword(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DB_TABLE_NAME_COMMANDER_PASSWORD,null);
        String currentPassword = "";
        while (cursor.moveToNext()){
            currentPassword=cursor.getString(0);
        }
        return currentPassword;
    }
    /** check commander password table is Empty **/
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

    /** auth the password of args is same which table have **/
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
    /** delete data from ccommander password db **/
    public void DeleteDataFromCommanderPassword(){
        SQLiteDatabase db = this.getWritableDatabase();
        // this delete all items from  table
        db.execSQL("DELETE FROM "+DB_TABLE_NAME_COMMANDER_PASSWORD);
        db.close();
    }

}
