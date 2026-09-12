package com.google.finddroid;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MyAdminReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
//        Log.i("PERmission","enabled");

        //**** DISABLE NOTIFICATION PANNEL ON LOCKSCREEN ***
        DevicePolicyManager dpm =
                (DevicePolicyManager) context.getSystemService(
                        Context.DEVICE_POLICY_SERVICE);

        ComponentName admin =
                new ComponentName(context, MyAdminReceiver.class);

        if (dpm.isDeviceOwnerApp(context.getPackageName())) {

            // Allow this app to enter Lock Task mode
            dpm.setLockTaskPackages(
                    admin,
                    new String[]{context.getPackageName()}
            );

            // Disable Lock Task system UI features
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                dpm.setLockTaskFeatures(
                        admin,
                        DevicePolicyManager.LOCK_TASK_FEATURE_NONE
                );
            }
        }

        //*** Disable power off manu while device is locked ***


        if (dpm.isDeviceOwnerApp(context.getPackageName())) {
            dpm.setLockTaskPackages(
                    admin,
                    new String[]{context.getPackageName()}
            );

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                dpm.setLockTaskFeatures(
                        admin,
                        DevicePolicyManager.LOCK_TASK_FEATURE_NONE
                );
            }
        }
        }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
    }
}
