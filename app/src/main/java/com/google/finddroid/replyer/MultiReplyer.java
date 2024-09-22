package com.google.finddroid.replyer;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.RemoteInput;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.commands.ContectGrapper;
import com.google.finddroid.global.SwitchDBGlobalVar;

public class MultiReplyer {
    StatusBarNotification sbn;
    Context context;
//    String GOOGLE_SMS_REPLY_KEY = "android.intent.extra.TEXT";
    String WHATSAPP_REPLY_KEY = "direct_reply_input";
    String TELEGRAM_REPLY_KEY = "extra_voice_reply";
    public MultiReplyer(StatusBarNotification statusBarNotification , Context mContext){
        this.sbn=statusBarNotification;
        this.context=mContext;
    }
    public void sendReply(String reply){
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        String REPLY_KEY;
        if(sbn.getPackageName().contains("whatsapp") && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.WHATSAPP_ACCESS)){
            REPLY_KEY=WHATSAPP_REPLY_KEY;
            Reply(reply,REPLY_KEY);
        }else if (sbn.getPackageName().contains("telegram") && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.TELEGRAM_ACCESS)){
            REPLY_KEY=TELEGRAM_REPLY_KEY;
            Reply(reply,REPLY_KEY);
        }else{
            if(switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.SMS_ACCESS)){
                SMSReply(reply);
            }
        }

    }


    public void Reply(String reply,String REPLY_KEY){
        try {
            Bundle extras = sbn.getNotification().extras;
//        Notification.Action[] actions = sbn.getNotification().actions;
            PendingIntent replyPendingIntent = sbn.getNotification().actions[0].actionIntent;
//        Log.i("is_error",Boolean.toString(sbn.getNotification().actions[0].actionIntent == null));

//        Log.i("pandintent",replyPendingIntent.toString());
//        for (Notification.Action action : actions) {
//            if (action.getRemoteInputs() != null) {
//                for (android.app.RemoteInput remoteInput : action.getRemoteInputs()) {
//                    Log.i("KEYS",remoteInput.getResultKey());
//                }
//            }
//        }
//        if (replyPendingIntent != null) {
            try {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putCharSequence(REPLY_KEY, reply);

                RemoteInput.addResultsToIntent(sbn.getNotification().actions[0].getRemoteInputs(), intent, bundle);
                Log.i("running replyer","success");
                replyPendingIntent.send(context, 0, intent);
            } catch (PendingIntent.CanceledException e) {
                throw new RuntimeException(e);
            }
        }catch (java.lang.NullPointerException e){
        }
    }

    public void SMSReply(String reply){
        try {
            Bundle extras = sbn.getNotification().extras;
            //get title key of notification bundel
            String title = extras.getString("android.title");
            String ContectName = title;
            String regexStr = "^[0-9]$";
            String PhoneNumber;
            if (ContectName.matches(regexStr)){
                PhoneNumber = ContectName;
            }else {
                PhoneNumber = new ContectGrapper(context).getContactNumber(ContectName);
            }

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(PhoneNumber,null,reply,null,null);
        }catch (Exception e){}
    }
}
