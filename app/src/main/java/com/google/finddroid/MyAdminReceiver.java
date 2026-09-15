package com.google.finddroid;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class MyAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);

//        Log.i("PERmission","enabled");
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context,MyAdminReceiver.class);
        Log.d("ADMIN", "Device owner = " +
                dpm.isDeviceOwnerApp(context.getPackageName()));
        if(dpm.isDeviceOwnerApp(context.getPackageName())){
            String[] packages = {context.getPackageName()};
            dpm.setLockTaskPackages(componentName,packages);
            int flags = DevicePolicyManager.LOCK_TASK_FEATURE_NONE;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                dpm.setLockTaskFeatures(componentName,flags);
            }
        }
        }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }
}
