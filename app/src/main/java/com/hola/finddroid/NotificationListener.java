package com.hola.finddroid;

import android.app.Notification;
import android.app.PendingIntent;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.hola.finddroid.DBs.SwitchDBHelper;
import com.hola.finddroid.commands.CommandReceiver;
import com.hola.finddroid.global.SwitchDBGlobalVar;


/*
* here all notification listens
 */
public class NotificationListener extends NotificationListenerService {
    private String TAG = this .getClass().getSimpleName() ;
    CameraManager cameraManager;
    @Override
    public void onCreate () {
        super.onCreate() ;
    }

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        try {
            String ShortcutID = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ShortcutID = sbn.getNotification().getShortcutId();
            }
            //request for notification bundel
            Bundle extras = sbn.getNotification().extras;
            //get title key of notification bundel
            String title = extras.getString("android.title");
            //get text key from bandel and convert it to string for usage
            String text = extras.getCharSequence("android.text").toString();
            runNotificationCommands(sbn.getPackageName(), ShortcutID, title, text, sbn);
        }catch (NullPointerException e){
            //pass
        }
    }
    @Override
    public void onNotificationRemoved (StatusBarNotification sbn) {
//        Log. i ( TAG , "********** onNotificationRemoved" ) ;
//        Log. i ( TAG , "ID :" + sbn.getId() + " \t " + sbn.getNotification(). tickerText + " \t " + sbn.getPackageName()) ;

    }

    public void runNotificationCommands(String packageName,String shorId,String title,String textMessage,StatusBarNotification sbn){
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(getApplicationContext());
        if(packageName.contains("whatsapp") && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.WHATSAPP_ACCESS)){
            String PhoneNumber = shorId.split("@")[0];
            Log.i("PhoneNumber",PhoneNumber);
            new CommandReceiver(getApplicationContext(),PhoneNumber,textMessage,sbn).runCommand();
        }else if (packageName.contains("telegram") && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.TELEGRAM_ACCESS)){
            String PhoneNumber = title;
            new CommandReceiver(getApplicationContext(),PhoneNumber,textMessage,sbn).runCommand();
        }
        else if(!packageName.equals("whatsapp") && !packageName.equals("telegram") && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.SMS_ACCESS)){
            String PhoneNumber = title;
            new CommandReceiver(getApplicationContext(),PhoneNumber,textMessage,sbn).runCommand();
        }
    }


    public void keyGraper(StatusBarNotification sbn){
        Bundle extras = sbn.getNotification().extras;
        Notification.Action[] actions = sbn.getNotification().actions;
        PendingIntent replyPendingIntent = sbn.getNotification().actions[0].actionIntent;
        Log.i("is_error",Boolean.toString(sbn.getNotification().actions[0].actionIntent == null));

        Log.i("pandintent",replyPendingIntent.toString());
        for (Notification.Action action : actions) {
            if (action.getRemoteInputs() != null) {
                for (android.app.RemoteInput remoteInput : action.getRemoteInputs()) {
//                    Log.i("KEYS",remoteInput.getResultKey());
                }
            }
        }
    }
}
