package com.google.finddroid.DBs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.finddroid.DBs.DBModels.DBDataModel;

import java.util.ArrayList;

public class SwitchDBHelper extends SQLiteOpenHelper {
    //DB related var
    public static final String DB_NAME = "SwitchDB"; //DB NAME
    public static final int DB_VERSION = 1; //DB VESRION
    //table related var
    public static final String DB_TABLE_NAME = "switch"; //TABLE NAME
    public static final String TABLE_KEY_ID = "id";//TABLE HEADER
    public static final String TABLE_SWITCH_NAME = "switch_name";//TABLE HEADER
    public static final String TABLE_SWITCH_CONTEXT = "switch_context";//TABLE HEADER
    public static final String TABLE_SWITCH_STATE = "switch_state";//TABLE HEADER
    public SwitchDBHelper(@Nullable Context context) {
        super(context, DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //if table exists and user open app first time it delete old and create new with default value
        String DROP_TABLE = "DROP TABLE IF EXISTS " + DB_TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE);
        //      ********SwitchDB*********
        // id       switch_name     switch_state
        // value        value           value
        //create table
        sqLiteDatabase.execSQL("CREATE TABLE "+DB_TABLE_NAME+
                "("
                +TABLE_KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TABLE_SWITCH_NAME+" TEXT,"+
                TABLE_SWITCH_CONTEXT+" TEXT,"+
                TABLE_SWITCH_STATE+" BOOLEAN"+
                ")"
        );
    }
    //NOT needed yet
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //Check if table value is Empty
    public boolean isEmpty(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DB_TABLE_NAME, null);
        boolean isEmpty;
        if (mCursor.moveToFirst())
        {
            isEmpty = false;
        } else
        {
           isEmpty = true;
        }
        db.close();
        return isEmpty;
    }
    //Insert new data
    public void InsertData(String switchName,String switchContext,Boolean switchState){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABLE_SWITCH_NAME,switchName);
        values.put(TABLE_SWITCH_CONTEXT,switchContext);
        values.put(TABLE_SWITCH_STATE,switchState);
        db.insert(DB_TABLE_NAME,null,values);
    }
    //fetch data
    public ArrayList<DBDataModel> FetchData(){
        ArrayList<DBDataModel> dbList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+DB_TABLE_NAME,null);
        while (cursor.moveToNext()){
            DBDataModel model= new DBDataModel();
            model.switchId=cursor.getInt(0);
            model.switchName=cursor.getString(1);
            model.switchContext=cursor.getString(2);
            // cursor don't have getBoolean method curser return boolean as 0 or 1
            model.switchState=cursor.getInt(3) > 0;
            dbList.add(model);
        }
        db.close();
        return dbList;
    }
    //Update data
    public void UpdateData(String SwitchName,Boolean SwitchState){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_SWITCH_STATE,SwitchState);
       db.update(DB_TABLE_NAME,contentValues,TABLE_SWITCH_NAME + " = ?", new String[]{SwitchName});
        db.close();
    }
    //Check Switch state is it off or on
    public Boolean CheckSwitchState(String SwitchName){
        ArrayList<DBDataModel> arrayList=this.FetchData();
        Boolean switchSatate=null;
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).switchName.equals(SwitchName)){
//                Log.i("check",arrayList.get(i).switchName+ " "+Boolean.toString(arrayList.get(i).switchState));
                switchSatate=arrayList.get(i).switchState;
            }else {
                //pass
            }
        }
        return switchSatate;
    }
    //delete table
    public void deleteTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(DROP_TABLE);
    }
}
