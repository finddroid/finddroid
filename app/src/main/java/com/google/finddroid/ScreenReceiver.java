package com.google.finddroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            // Screen turned off
            Toast.makeText(context, "Screen turned off", Toast.LENGTH_SHORT).show();
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            // Screen turned on
            Toast.makeText(context, "Screen turned on", Toast.LENGTH_SHORT).show();
        }
    }
}
