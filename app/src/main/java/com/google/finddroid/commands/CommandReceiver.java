package com.google.finddroid.commands;

import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.ForegroundService;
import com.google.finddroid.global.CommandsGlobalVar;
import com.google.finddroid.global.SwitchDBGlobalVar;
import com.google.finddroid.replyer.MultiReplyer;

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
        String USER_COMMANDS = "FINDDROID COMMANDS : \n\n"+
                CommandsGlobalVar.FLESH_CONTROL_COMMAND + "on/" + "off \n" +
                CommandsGlobalVar.LOCATION_COMMAND+"\n"+
                CommandsGlobalVar.RING_MODE_COMMAND +CommandsGlobalVar.SILENT_RING + "/" + CommandsGlobalVar.NORMAL_RING +"\n"+
                CommandsGlobalVar.LOCK_DEVICE_COMMAND+"\n"+
                CommandsGlobalVar.END_FINDDROID_COMMAND+"\n";
        if(command.contains("fd".toLowerCase()) || command.contains("fd".toUpperCase()) || command.contains("Fd")){
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
            }else{
                String[] checkCommand = command.split(" ");
                int checkCommandLength = checkCommand.length;
                if(checkCommandLength == 2){
                    String password = checkCommand[1];
                    boolean checkPassword = commanderDBHelper.CheckPassword(password);
                    if(checkPassword){
                        setNumberToDB(this.PhoneNumber);
                        MultiReplyer multiReplyer = new MultiReplyer(statusBarNotification,context);
                        multiReplyer.sendReply("FindDroid Access Granted");
                        multiReplyer.sendReply(USER_COMMANDS);
                    }else {
                        new MultiReplyer(statusBarNotification,context).sendReply("FindDroid Access denied");
                    }
                }
            }



        }
    }
    public void runCommand(){
        CheckAndAccess();
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(context);
        MultiReplyer multiReplyer = new MultiReplyer(statusBarNotification,context);
        CommandRunner commandRunner = new CommandRunner(context,statusBarNotification);
        CommandRequirementChecker commandRequirementChecker = new CommandRequirementChecker(context,this.PhoneNumber);
        boolean accessCMD = commandRequirementChecker.check();
        if (accessCMD) {
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
                multiReplyer.sendReply("Ring mode changed to Normal");
            }else if (command.equalsIgnoreCase(CommandsGlobalVar.RING_MODE_COMMAND+CommandsGlobalVar.SILENT_RING) && switchDBHelper.CheckSwitchState(SwitchDBGlobalVar.RING_MODE_ACCESS)){
                new RingModeChanger(context).VibrationMode();
                multiReplyer.sendReply("Ring mode change to Silent");
            }
        }
    }
}
