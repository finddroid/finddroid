package com.google.finddroid.commands;

import android.content.Context;
import android.service.notification.StatusBarNotification;

import com.google.finddroid.replyer.MultiReplyer;

public class SendLocation {
    Context context;
    StatusBarNotification sbn;
    public SendLocation(StatusBarNotification sbn,Context context){
        this.sbn=sbn;
        this.context=context;
    }
    public void send(String Latitude, String Longitude){
        new MultiReplyer(sbn,context).sendReply("https://www.google.com/maps/search/?api=1&query="+Latitude+","+Longitude);
    }
}
