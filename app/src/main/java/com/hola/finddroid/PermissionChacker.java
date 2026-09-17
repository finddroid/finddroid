package com.hola.finddroid;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionChacker {
    Context context;
    public PermissionChacker(Context context){
        this.context=context;
    }

    public boolean location(){

        if(ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }
}
