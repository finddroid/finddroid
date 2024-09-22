package com.google.finddroid.commands;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.service.notification.StatusBarNotification;

public class CommandRunner {
    Context mainActivityContext;
    StatusBarNotification sbn;

    public CommandRunner(Context mainActivityContextVer, StatusBarNotification sbn) {
        this.mainActivityContext = mainActivityContextVer;
        this.sbn = sbn;
    }
    public CommandRunner(Context mainActivityContext){
        this.mainActivityContext=mainActivityContext;
    }

    public void executeTorchEnable(Boolean enable) {
        CameraManager cameraManager = (CameraManager) mainActivityContext.getSystemService(Context.CAMERA_SERVICE);
        try {
            String flash = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(flash, enable);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeLockScreen(Boolean off) {
        DevicePolicyManager devicePolicyManager;
        devicePolicyManager = (DevicePolicyManager) mainActivityContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (off) {
            devicePolicyManager.lockNow();
        }
    }

}