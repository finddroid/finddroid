package com.google.finddroid.commands;

import android.content.Context;

import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.global.SwitchDBGlobalVar;

public class CommandRequirementChecker {
    Context context;
    String number;
    public CommandRequirementChecker(Context mContext, String phonenumber){
        this.context=mContext;
        this.number=phonenumber;
    }
    public boolean check(){
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(context);
        boolean NotificationSwitch = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.NOTIFICATION_ACCESS);
        boolean AccessibilitySwitch = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.ACCESSIBILITY_ACCESS);
        boolean AdminSwitch = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.ADMIN_ACCESS);
        boolean PhoneNumber;
        // this first check number colum is empty or not and if not empty then check number is in colum or not
        PhoneNumber = !commanderDBHelper.isEmpty_CommanderNumber() && (commanderDBHelper.CheckNumber(this.number));
        // if all permisson and phonenumber is true then access is true
        boolean access = NotificationSwitch && AdminSwitch && PhoneNumber;
        return access;
    }
}
