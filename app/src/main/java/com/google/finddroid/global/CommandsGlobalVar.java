package com.google.finddroid.global;

public class CommandsGlobalVar {
    public static String START_FINDDROID_COMMAND = "Fd"+" "; // not used in command receiver file
    public static String RING_MODE_COMMAND = "Ring";
    public static String LOCATION_COMMAND = "Location";
    public static String FLESH_CONTROL_COMMAND = "torch"+" ";
    public static String LOCK_DEVICE_COMMAND = "Lock";
    public static String END_FINDDROID_COMMAND = "Fd end";
    public static String ENABLE = "on";
    public static String DISABLE = "off";
    public static String NORMAL_RING= " loud";
    public static String SILENT_RING= " silent";
//    public static String DISABLE_ANTI_SWITCHOFF= "FD_DISABLE_ANTI_SWITCHOFF";
//    public static String ENABLE_ANTI_SWITCHOFF="FD_ENABLE_ANTI_SWITCHOFF";
    public static class Description{
        public static String START_FINDDROID_COMMAND_DESCRIPTION = "Starting command it's necessary to make connection";
        public static String RING_MODE_COMMAND_DESCRIPTION = "Change Ring mode by command silent to normal or normal to silent";
        public static String LOCATION_COMMAND_DESCRIPTION = "Locate your device and send location by command";
        public static String FLESH_CONTROL_COMMAND_DESCRIPTION = "Flesh light control by command";
        public static String LOCK_DEVICE_COMMAND_DESCRIPTION = "Lock device by command";
        public static String  END_FINDDROI_COMMAND_DESCRIPTION = "End Connection to commander";
//        public static String DISABLE_ANIT_SWITCHOFF_DESCRIPTION = "command force to disable anti switch off mode by command.";
//        public static String ENABLE_ANTI_SWITCHOFF_DESCRIPTION = "command force to enable anti switch off mode by command";
    }

}
