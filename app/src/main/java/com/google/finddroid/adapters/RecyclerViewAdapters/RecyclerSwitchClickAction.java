package com.google.finddroid.adapters.RecyclerViewAdapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.MainActivity;
import com.google.finddroid.MyAccessibilityService;
import com.google.finddroid.MyDeviceAdminReceiver;
import com.google.finddroid.NotificationListener;
import com.google.finddroid.R;
import com.google.finddroid.global.SwitchDBGlobalVar;
/***
 * In this file make switch to work.
 * In this file switch don't intrect with db here only switch work for frontend things like permission access.
 * ***/
public class RecyclerSwitchClickAction {
    /***
     * switch 0 for Notification permission
     * switch 1 for Accessibility permission
     * switch 2 for Admin permission
     * switch 3 for set password
     * switch 4 for Location permission
     * switch 5 for Lockdevice permission (Admin needed)
     * switch 6 for Ring change permission (Admin needed)
     * switch 7 for Controll with sms permission (Contect and sms permission needed)
     * switch 8 and 9 for control with whatsapp and telegram permission (Accessablity needed)
     * switch 10 and 11 for anti switch off and anti mode change permission (Accessablity needed)
     * ***/


    Context mContext;
    int mPostion;
    CheckAndRequestLocationPermission checkAndRequestLocationPermission;
    RecyclerViewAdapter.ViewHolder mholder;
    boolean SwitchState;
    View GlobelView;
    public RecyclerSwitchClickAction(RecyclerViewAdapter.ViewHolder holder, Context context, int postion, boolean state, CheckAndRequestLocationPermission checkAndRequestLocationPermission,View view){
        this.mPostion=postion;
        this.mContext=context;
        this.mholder=holder;
        this.SwitchState=state;
        this.checkAndRequestLocationPermission = checkAndRequestLocationPermission;
        this.GlobelView=view;
    }

    //execute and check the state of switch to perform action
    public void executeAction(){
        //if user enable switch then it run true function
        if(SwitchState){
            executeStateTrue(mPostion);
        }else {
            //if user disable switch then it run false function
            executeStateFalse(mPostion);
        }
    }

    //THIS IS RUN WHEN USER ENABLE SWITCH
    public void executeStateTrue(int postion) {
        if(mPostion==0){ //NOTIFICATION PERMISSION SWITCH
            //Check notification permission is granted or not
            ComponentName cn = new ComponentName(mContext, NotificationListener.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_notification_listeners");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            //If permission not granted it show the lottie anim and ask user to enable it
            if (!enabled){
                ShowNotificationPermissionBox();
            }
        }
        else if(mPostion==1){//ACCESSibility PERMISSION SWITCH
            //Check accessibility permission granted or not
            ComponentName cn = new ComponentName(mContext, MyAccessibilityService.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_accessibility_services");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            //If permission not granted it show the lottie anim and ask user to enable it
            if(!enabled) {
                ShowAssesablityPermissionDialogBox();
            }
        }
        else if(mPostion==2){//ADMIN SWITCH
            //This function check and send user to Admin permission
            ShowAdminPermissionDialogBox();
        }
        else if(mPostion==3){//SET PASSWORD SWITCH
            passwordListItemRunner();//it show user box to insert password
        }
        else if(mPostion==4){
            /*this method implepantable and implement in fragment one
            * because we can't request directly for background location permission so we use this
            * */
            checkAndRequestLocationPermission.LocationClicked();
        }
        else if (mPostion==5) {//LOCK DEVICE SWITCH
            ShowAdminPermissionDialogBox();
        }else if(mPostion==6){//RING MODE SWITCH
            ShowAdminPermissionDialogBox();
        }else if(mPostion==7){//CONTECT PERMISSION SWITCH
            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
            MainActivity.main.requestPermissions(permissions,3000);
        }
        else if(mPostion==8){//CONTROL WITH WHATSAPP PERMISSION
            //Check for notification permission
            ComponentName cn = new ComponentName(mContext, MyAccessibilityService.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_accessibility_services");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            //If permission not granted it show the lottie anim and ask user to enable it
            if(!enabled) {
                ShowAssesablityPermissionDialogBox();
            }
        }
        else if (mPostion==9) {//CONTROL WITH TELEGRAM PERMISSION
            //Check for notification permission
            ComponentName cn = new ComponentName(mContext, MyAccessibilityService.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_accessibility_services");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            //If permission not granted it show the lottie anim and ask user to enable it
            if(!enabled) {
                ShowAssesablityPermissionDialogBox();
            }
        }
        else if(mPostion==10){//ANTI SWITCH OFF PERMISSION
            //Check for notification permission
            ComponentName cn = new ComponentName(mContext, MyAccessibilityService.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_accessibility_services");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            //If permission not granted it show the lottie anim and ask user to enable it
            if(!enabled) {
                ShowAssesablityPermissionDialogBox();
            }
        }
        else if(mPostion==11){//ANTI MODE CHANGE PERMISSION
            //Check for notification permission
            ComponentName cn = new ComponentName(mContext, MyAccessibilityService.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_accessibility_services");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            //If permission not granted it show the lottie anim and ask user to enable it

            if(!enabled) {
                ShowAssesablityPermissionDialogBox();
            }
        }
    }

    public void executeStateFalse(int position){
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(mContext);
        if(position==3){
//            Log.i("excuteState False",Boolean.toString(SwitchState));
//            Log.i("Visiblity",Integer.toString(mholder.passwordPerent.getVisibility()));
            if (mholder.passwordPerent.getVisibility() == View.VISIBLE && commanderDBHelper.isEmptyCommanderPassword()){
                mholder.passwordPerent.setVisibility(View.GONE);

            } else{
                commanderDBHelper.DeleteDataFromCommanderPassword();
                commanderDBHelper.close();
                MainActivity.main.finish();
                Intent reStart= new Intent(MainActivity.main.getIntent());
                mContext.startActivity(reStart);
            }
//
        }
    }


    /** this function ask for password input **/
    public void passwordListItemRunner(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animate = new TranslateAnimation(0, 0,-mholder.passwordPerent.getHeight(),1);
                // duration of animation
                animate.setDuration(500);
                animate.setFillAfter(true);
                mholder.passwordPerent.startAnimation(animate);
            }
        },50);
        mholder.passwordPerent.setVisibility(View.VISIBLE);
        mholder.passSetButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onClick(View view) {
                String passwordString = mholder.password.getText().toString();
//                    Log.i("setting pass",passwordString);
                if(!passwordString.isEmpty()){
                    CommanderDBHelper commanderDBHelper = new CommanderDBHelper(mContext);
                    commanderDBHelper.DeleteDataFromCommanderPassword();
                    commanderDBHelper.InsertDataToCommanderPassword(passwordString);
                    commanderDBHelper.close();
                    MainActivity.main.finish();
                    mContext.startActivity(MainActivity.main.getIntent());
                    Toast.makeText(mContext,"password successfully set",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,"Enter valid text",Toast.LENGTH_LONG).show();
                }
            }
        });
        mholder.passCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mholder.mButton.setChecked(false);
            }
        });
    }
    /** Accessibility Permission Setting sender it send to permission page not show the lottie anim **/
    public void AccessibilityPermissionOpener(){
        Intent accessibilityIntent= new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        Bundle bundle = new Bundle();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mContext.getPackageName().toString());
        stringBuilder.append("/");
        stringBuilder.append(MyAccessibilityService.class.getName());
        String stringBuilderToString=stringBuilder.toString();
        bundle.putString(":settings:fragment_args_key",stringBuilderToString);
        accessibilityIntent.putExtra(":settings:fragment_args_key",stringBuilderToString);
        accessibilityIntent.putExtra(":settings:show_fragment_args",bundle);
        mContext.startActivity(accessibilityIntent);
    }

    /** Notification Permission Setting page sender it send to permission page setting not show the lottie anim **/
    public void NotificationPermissionOpener(){
        Intent accessibilityIntent= new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        Bundle bundle = new Bundle();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mContext.getPackageName().toString());
        stringBuilder.append("/");
        stringBuilder.append(NotificationListener.class.getName());
        String stringBuilderToString=stringBuilder.toString();
        bundle.putString(":settings:fragment_args_key",stringBuilderToString);
        accessibilityIntent.putExtra(":settings:fragment_args_key",stringBuilderToString);
        accessibilityIntent.putExtra(":settings:show_fragment_args",bundle);
        mContext.startActivity(accessibilityIntent);
    }

    /** Admin permission Asker **/
    public void ShowAdminPermissionDialogBox(){
        Dialog dialog = new Dialog(GlobelView.getContext());
        dialog.setContentView(R.layout.admin_lottie_alert_box);  // Set the custom layout

        // Optionally set dialog properties
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_lotte_shape); // To remove dialog background
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // Find the LottieAnimationView and play the animation
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.playAnimation();
        Button OkButton= dialog.findViewById(R.id.admin_lottie_alert_box_Ok);
        Button CancelButton = dialog.findViewById(R.id.admin_lottie_alert_box_cancel);
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                ComponentName componentName = new ComponentName(mContext, MyDeviceAdminReceiver.class);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You need to enable the app as a device administrator to turn off the screen.");
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mholder.mButton.setChecked(false);
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(mContext);
                switchDBHelper.UpdateData(SwitchDBGlobalVar.ADMIN_ACCESS,false);
            }
        });
        // Show the dialog
        dialog.show();
    }

    /** Show the lottie anim and then send to permission setting by calling  AccessibilityPermissionOpener() **/
    public void ShowAssesablityPermissionDialogBox(){
        Dialog dialog = new Dialog(GlobelView.getContext());
        dialog.setContentView(R.layout.accessablity_lottie_alert_box);  // Set the custom layout

        // Optionally set dialog properties
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_lotte_shape); // To remove dialog background
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // Find the LottieAnimationView and play the animation
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.playAnimation();
        Button OkButton= dialog.findViewById(R.id.accessablity_lottie_alert_box_Ok);
        Button CancelButton = dialog.findViewById(R.id.accessablity_lottie_alert_box_cancel);
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccessibilityPermissionOpener();
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mholder.mButton.setChecked(false);
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(mContext);
                switchDBHelper.UpdateData(SwitchDBGlobalVar.ACCESSIBILITY_ACCESS,false);
            }
        });
        // Show the dialog
        dialog.show();
    }

    /** Show lottie anim and send to permission page  by calling NotificationPermissionOpener()**/
    public void ShowNotificationPermissionBox(){
        Dialog dialog = new Dialog(GlobelView.getContext());
        dialog.setContentView(R.layout.notification_lotte_alert_box);  // Set the custom layout

        // Optionally set dialog properties
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.alert_lotte_shape); // To remove dialog background
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        // Find the LottieAnimationView and play the animation
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimationView);
        lottieAnimationView.playAnimation();
        Button OkButton= dialog.findViewById(R.id.notification_lottie_alert_box_Ok);
        Button CancelButton = dialog.findViewById(R.id.notification_lottie_alert_box_cancel);
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationPermissionOpener();
                dialog.dismiss();
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mholder.mButton.setChecked(false);
                SwitchDBHelper switchDBHelper = new SwitchDBHelper(mContext);
                switchDBHelper.UpdateData(SwitchDBGlobalVar.NOTIFICATION_ACCESS,false);
            }
        });
        // Show the dialog
        dialog.show();
    }

}
