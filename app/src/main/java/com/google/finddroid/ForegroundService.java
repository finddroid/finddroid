package com.google.finddroid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import com.google.finddroid.commands.RingModeChanger;

public class ForegroundService extends Service {

    public int NOTIFICATION_ID = 100;
    public String NOTIFICATION_CHANNEL_ID = "id";
    public String NOTIFICATION_CHANNEL_NAME = "FOREGROUDN service";
    public RingModeChanger ringModeChanger;
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals("STOP_SERVICE")) {
            try {
//                ringDevice.stop();
//                ringDevice = null;
                stopForegroundService();
                stopSelf();
            }catch (RuntimeException ignore){
//                ringDevice.stop();
//                ringDevice = null;
            }
        } else {
            try {
            startForegroundService();

            }catch (RuntimeException ignore){}
        }
//        startForegroundService();
        return START_STICKY;
    }

    private void startForegroundService() {
        // Create a notification with a stop action
        ringModeChanger = new RingModeChanger(getApplicationContext());
        NotificationCompat.Builder notification = createNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceCompat.startForeground(ForegroundService.this,NOTIFICATION_ID,notification.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);
        }else {
            startForeground(NOTIFICATION_ID, notification.build());
        }
//            ringDevice.play();
    }

    private void stopForegroundService() {
        stopForeground(true);
//        ringDevice.stop();
    }

    private NotificationCompat.Builder createNotification() {
        // Create the intent to stop the service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }
        Intent stopIntent = new Intent(getApplicationContext(), ForegroundService.class);
        stopIntent.setAction("STOP_SERVICE");
        PendingIntent stopPendingIntent = PendingIntent.getService(getApplicationContext(), 0, stopIntent,PendingIntent.FLAG_IMMUTABLE);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Ringing Device")
                .setContentText("Click on stop to stop")
                .setSmallIcon(R.drawable.findroid_dark_logo)
                .addAction(R.drawable.ic_launcher_foreground, "STOP RING", stopPendingIntent) // Stop button
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ringDevice.stop();
    }
}
