package com.hola.finddroid.commands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class ContectGrapper {
    Context context;
    ArrayList<ContectDataObject> contectDataObjects = new ArrayList<>();
    public ContectGrapper(Context context){
        this.context=context;
        GrepContacts();
    }

    public void GrepContacts(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null);
            assert cursor != null;
            while (cursor.moveToNext()){
                @SuppressLint("Range") String Name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String Number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contectDataObjects.add(new ContectDataObject(Name,Number));

            }
        }
    }

    public String getContactNumber(String Name){
        String Number = null;
        for (int i =0 ; contectDataObjects.size()>i;i++){
            if(contectDataObjects.get(i).getContactName().equals(Name)){
                Number = contectDataObjects.get(i).getContactNumber();
            }
        }
        return Number;
    }
}
