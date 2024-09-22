package com.google.wareply;

public class somethingCommanted {
  //        Intent i = new Intent(MainActivity.this,ForegroundService.class);
//        startService(i);
    //*******************check sqldb*************************
//        SwitchDBHelper db = new SwitchDBHelper(getApplicationContext());
//        if (db.isEmpty()) {
//            SwitchDBHelper switchDBHelper = new SwitchDBHelper(getApplicationContext());
//            switchDBHelper.InsertData(SwitchDBGlobalVar.NOTIFICATION_ACCESS,"necessary to enable", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.ACCESSIBILITY_ACCESS,"necessary for some functionality", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.ADMIN_ACCESS,"necessary for lock and unlock functionality", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.SET_PASSWORD,"set password to control", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.LOCATION_ACCESS,"necessary to get location",false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.LOCK_FROM_COMMAND,"necessary to lock device by command",false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.RING_DEVICE_ACCESS,"necessary to ring device by command",false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.SMS_ACCESS,"Control Device from SMS",false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.WHATSAPP_ACCESS,"Control Device from Whatsapp message", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.TELEGRAM_ACCESS,"Control Device from Telegram message", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.ANTI_SWITCH_OFF,"Device can't be power off until unlock", false);
//            switchDBHelper.InsertData(SwitchDBGlobalVar.ANTI_MODE_CHANGE,"Can't Change Mode (flight mode,internet) until unlock",false);
//
//        } else {
//            try {
//
//                int accessEnabled = Settings.Secure.getInt(getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
//                Log.i("ACCESSABLITY", Integer.toString(accessEnabled));
//                if (accessEnabled == 0) {
//                    db.UpdateData(SwitchDBGlobalVar.ACCESSIBILITY_ACCESS, false);
//                }
//            } catch (Settings.SettingNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//            db.close();
//        }
//        db.close();
}
