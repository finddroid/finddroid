package com.google.finddroid;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.service.credentials.Action;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.commands.CommandRunner;
import com.google.finddroid.global.SwitchDBGlobalVar;

public class MyAccessibilityService extends AccessibilityService {
    public void sendHome(int i){
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.setFlags(i);
            startActivity(intent);
        }catch (Exception e){
            //pass
        }
    }
    public void block(){
        for (int i=0;i<4;i++){
            try {
                performGlobalAction(1);
            }catch (Exception e){
                //pass
            }
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String event = accessibilityEvent.getText().toString().toLowerCase();
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
//            sendHome(268435456);

//        Log.i("EVENT",event);
//        Log.i("EVENT TYPE",Integer.toString(accessibilityEvent.getEventType()));
//        try {
////            AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
////            String packageName = accessibilityNodeInfo.getPackageName().toString();
////            Log.i("pkg", packageName);
////            extractTextFromNode(accessibilityNodeInfo);
//        }catch (NullPointerException ignore){}




        SwitchDBHelper switchDBHelper = new SwitchDBHelper(getApplicationContext());
            boolean isAntipoweroff = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.ANTI_SWITCH_OFF);
            boolean isAnitModeChange = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.ANTI_MODE_CHANGE);
        if(event.contains("Phone options".toLowerCase()) && isAnitModeChange && keyguardManager.isDeviceLocked()){
                final boolean deviceLocked = keyguardManager.isDeviceLocked();
                new CommandRunner(getApplicationContext()).executeLockScreen(true);
//              sendHome(268435456);

        } else if (event.contains("quick settings") && keyguardManager.isDeviceLocked() && isAnitModeChange) {
            block();
            new CommandRunner(getApplicationContext()).executeLockScreen(true);
        }

    }
//    private void extractTextFromNode(AccessibilityNodeInfo node) {
//        if (node == null) {
//            return;
//        }
//
//        // Check if the node contains text and is visible
//        if (node.getText() != null && node.isVisibleToUser()) {
//            String nodeText = node.getText().toString();
//            // Handle the extracted text (e.g., log it or process it)
////            Log.d("MyAccessibilityService", "Captured Text: " + nodeText);
//        }
//
////         Recursively iterate over child nodes
//        for (int i = 0; i < node.getChildCount(); i++) {
//            extractTextFromNode(node.getChild(i));
//        }
//    }


//    public boolean XiaomiSwitchoffAction(AccessibilityNodeInfo nodeInfo){
//        boolean trySwitchOff = false;
//        if (nodeInfo != null && nodeInfo.getPackageName() != null) {
//            String packageName = nodeInfo.getPackageName().toString();
////            Log.i("pkg",packageName);
//            String manufacturer = Build.MANUFACTURER;
////            Log.i("manufactuerName",manufacturer);
//            if (manufacturer.equals("Xiaomi")) {
//                if (packageName.equals("android")) {
//                    // Look for the Power Off text in the dialog
//                    trySwitchOff = true;
//                    Log.d("PowerButtonService", "User tried to power off the phone!");
//
//                }
//            }
//        }
//        return trySwitchOff;
//    }

//    public boolean XiaomiModeChanger(String Event){
//        boolean tryToChangeMode = false;
//        if (Event.contains("notification shade")){
//            tryToChangeMode=true;
//        }
//        return tryToChangeMode;
//    }
    @Override
    public void onInterrupt() {

    }
}
