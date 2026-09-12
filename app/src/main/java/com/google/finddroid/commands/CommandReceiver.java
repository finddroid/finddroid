package com.google.finddroid.commands;

import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.experiments.ForegroundService;
import com.google.finddroid.global.CommandsGlobalVar;
import com.google.finddroid.global.SwitchDBGlobalVar;
import com.google.finddroid.replyer.MultiReplyer;
/** In this file auth the user and exec the command
 * Auth the user (who want to login to controll device) with password (that the owner set when setup the app)
 * **/
public class CommandReceiver{
    Context context;
    String PhoneNumber;
    String command;
    String commanderApp;
    StatusBarNotification statusBarNotification;
    public CommandReceiver(Context mContext, String number, String command, StatusBarNotification sbn){
        this.command=command;
        this.PhoneNumber= number;
        this.context=mContext;
        this.commanderApp=commanderApp;
        this.statusBarNotification=sbn;
    }

    public void setNumberToDB(String number){
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(context);
        if(commanderDBHelper.isEmpty_CommanderNumber()){
            commanderDBHelper.InsertDataToCommandNumber(number);
        }else {
            commanderDBHelper.DeleteDataFromCommanderNumber();
            commanderDBHelper.InsertDataToCommandNumber(number);
        }
    }
    public void CheckAndAccess(){
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(context);
        // set string for command explain
        String USER_COMMANDS = "FindMyDroid : \n\n"+
                "* "+CommandsGlobalVar.FLESH_CONTROL_COMMAND + "on/" + "off \n" +
                "* "+CommandsGlobalVar.LOCATION_COMMAND+"\n"+
                "* "+CommandsGlobalVar.RING_MODE_COMMAND +CommandsGlobalVar.SILENT_RING + "/" + CommandsGlobalVar.NORMAL_RING +"\n"+
                "* "+CommandsGlobalVar.LOCK_DEVICE_COMMAND+"\n"+
                "* "+CommandsGlobalVar.END_FINDDROID_COMMAND+"\n";

        //Auth the user given password is same the owner set if it is then set the user/number/id on commander_table and make to authenticated
        if(command.contains("fd".toLowerCase()) || command.contains("fd".toUpperCase()) || command.contains("Fd")){ //It run when user give fd in message
            //if the owner don't set the password then it auth with only fd command.
            if (command.length()==2){
                boolean isPasswordEmpty = commanderDBHelper.isEmptyCommanderPassword();
                if (isPasswordEmpty){
                    setNumberToDB(this.PhoneNumber);
                    MultiReplyer multiReplyer = new MultiReplyer(statusBarNotification,context);
                    multiReplyer.sendReply("Access Granted");
                    multiReplyer.sendReply(USER_COMMANDS);
                }
                else {
                    //pass
                }
            }
            else{ // it run when user give fd with some value and owner set password.
                String[] checkCommand = command.split(" ");
                int checkCommandLength = checkCommand.length;
                if(checkCommandLength == 2){
                    String password = checkCommand[1];
                    boolean checkPassword = commanderDBHelper.CheckPassword(password);
                    if(checkPassword){ //auth the password is same which given by owner.
                        setNumberToDB(this.PhoneNumber);
                        MultiReplyer multiReplyer = new MultiReplyer(statusBarNotification,context);
                        multiReplyer.sendReply("FindMyDroid Access Granted");
                        multiReplyer.sendReply(USER_COMMANDS);
                    }else {// else not auth.
                        //send reply
                        new MultiReplyer(statusBarNotification,context).sendReply("FindMyDroid Access denied");
                    }
                }
            }



        }
    }

    //Check is given number/username/id is auth or not
    public boolean is_auth(String phoneNumber){
        SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(context);
        boolean NotificationSwitch = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.NOTIFICATION_ACCESS);
        boolean AdminSwitch = switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.ADMIN_ACCESS);
        boolean PhoneNumber;
        // this first check number colum is empty or not and if not empty then check number is in colum or not
        PhoneNumber = !commanderDBHelper.isEmpty_CommanderNumber() && (commanderDBHelper.CheckNumber(phoneNumber));
        // if all permisson and phonenumber is true then access is true
        boolean access = NotificationSwitch && AdminSwitch && PhoneNumber;
        return access;
    }
    /** this is the function running first in this file **/
    public void runCommand(){
        CheckAndAccess(); //ask to auth
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(context);
        MultiReplyer multiReplyer = new MultiReplyer(statusBarNotification,context);
        CommandRunner commandRunner = new CommandRunner(context,statusBarNotification);
//        CommandRequirementChecker commandRequirementChecker = new CommandRequirementChecker(context,this.PhoneNumber);
//        boolean accessCMD = commandRequirementChecker.check(); // check the user is auth or not
        boolean accessCMD = is_auth(this.PhoneNumber); // auth the user number/user/id
        if (accessCMD) { //if user is auth then exec the commands
            Intent notificationWithRing = new Intent(context, ForegroundService.class);
            SwitchDBHelper switchDBHelper = new SwitchDBHelper(context);
            // eqaulsIgnoreCase can match upper lower and all posible case of command
            if (command.equalsIgnoreCase(CommandsGlobalVar.FLESH_CONTROL_COMMAND+CommandsGlobalVar.ENABLE)) {
                commandRunner.executeTorchEnable(true);
                multiReplyer.sendReply("torch on successful");
            } else if (command.equalsIgnoreCase(CommandsGlobalVar.FLESH_CONTROL_COMMAND+CommandsGlobalVar.DISABLE)) {
                commandRunner.executeTorchEnable(false);
                multiReplyer.sendReply("torch off successful");
            } else if (command.equalsIgnoreCase(CommandsGlobalVar.LOCK_DEVICE_COMMAND) && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.LOCK_FROM_COMMAND)) {
                commandRunner.executeLockScreen(true);
                multiReplyer.sendReply("screen lock successful");
            }else if(command.equalsIgnoreCase(CommandsGlobalVar.LOCATION_COMMAND) && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.LOCATION_ACCESS)){
                try {
//                    Log.i("locationFromCommander", locationForUrlInString.getLatitude()+" "+locationForUrlInString.getLongitude());
                    new LocationFinder(context,statusBarNotification).getLastLocation();
                }catch (NullPointerException e){
                    Log.i("error","eror");
                }catch (SecurityException e){
                    multiReplyer.sendReply("Don't have location Access Permission");
                }
            }else if (command.equalsIgnoreCase(CommandsGlobalVar.END_FINDDROID_COMMAND)) {
                commanderDBHelper.DeleteDataFromCommanderNumber();
                multiReplyer.sendReply("FindDroid Connection Stop");
            }else if(command.equalsIgnoreCase(CommandsGlobalVar.RING_MODE_COMMAND+CommandsGlobalVar.NORMAL_RING) && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.RING_MODE_ACCESS)){
                new RingModeChanger(context).RingMode();
                multiReplyer.sendReply("Ring mode changed to Loud");
            }else if (command.equalsIgnoreCase(CommandsGlobalVar.RING_MODE_COMMAND+CommandsGlobalVar.SILENT_RING) && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.RING_MODE_ACCESS)){
                new RingModeChanger(context).VibrationMode();
                multiReplyer.sendReply("Ring mode change to Silent");
            }
        }
    }
}
