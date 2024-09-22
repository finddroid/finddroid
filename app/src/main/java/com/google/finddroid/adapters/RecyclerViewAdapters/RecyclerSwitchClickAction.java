package com.google.finddroid.adapters.RecyclerViewAdapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.MainActivity;
import com.google.finddroid.MyAccessibilityService;
import com.google.finddroid.MyDeviceAdminReceiver;
import com.google.finddroid.NotificationListener;
import com.google.finddroid.R;
import com.google.finddroid.global.SwitchDBGlobalVar;

public class RecyclerSwitchClickAction {
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
    public void executeAction(){
        if(SwitchState){
            executeStateTrue(mPostion);
        }else {
            executeStateFalse(mPostion);
        }
    }
    public void executeStateTrue(int postion) {
        if(mPostion==0){
            ComponentName cn = new ComponentName(mContext, NotificationListener.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_notification_listeners");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            if (!enabled){
                ShowNotificationPermissionBox();
            }
        }
        else if(mPostion==1){
            ComponentName cn = new ComponentName(mContext, MyAccessibilityService.class);
            String flat = Settings.Secure.getString(mContext.getContentResolver(), "enabled_accessibility_services");
            final boolean enabled = flat != null && flat.contains(cn.flattenToString());
            if(!enabled) {
//                AccessibilityPermissionOpener();
                ShowAssesablityPermissionDialogBox();
            }
        }
        else if(mPostion==2){
            ShowAdminPermissionDialogBox();
        }
        else if(mPostion==3){
            passwordListItemRunner();
        }
        else if(mPostion==4){
            /*this method implepantable and implement in fragment one
            * because we can't request directly for background location permission so we use this
            * */
            checkAndRequestLocationPermission.LocationClicked(true);
        }
        else if (mPostion==5) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            mContext.startActivity(intent);
        }else if(mPostion==6){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            ComponentName componentName = new ComponentName(mContext,MyDeviceAdminReceiver.class);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You need to enable the app as a device administrator to turn off the screen.");
            mContext.startActivity(intent);
        }else if(mPostion==7){
            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS};
            MainActivity.main.requestPermissions(permissions,3000);
        }
    }

    public void executeStateFalse(int position){
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(mContext);
        if (position == 0){

        }
        else if(position==3){
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
//            ViewGroup.LayoutParams params = mholder.cardView.getLayoutParams();
        mholder.passwordPerent.setVisibility(View.VISIBLE);

//            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, mContext.getResources().getDisplayMetrics());
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
