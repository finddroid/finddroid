package com.google.finddroid.commands;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.net.Uri;


public class RingModeChanger {
    Context context;
    AudioManager audioManager;
    Uri ringtoneUri;
    Ringtone ringtone;
    int currentMode;
    int MaxValume;
    public RingModeChanger(Context context){
        this.context=context;
        playSetup();
    }



    public void playSetup(){
        try {
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            currentMode = audioManager.getRingerMode();
            // Change the ringer mode to normal
            MaxValume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            // Optionally, you can increase the volume to maximum flog 0 don't show volume up ui
//            audioManager.setStreamVolume(AudioManager.STREAM_RING, MaxValume, 0);
            //getting the default ring of device
//            ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            //this grep ringtone a wait for play
//            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), ringtoneUri);
        }catch (RuntimeException ignore){}
    }

    public void RingMode(){
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING,MaxValume,0);
    }
    public void VibrationMode(){
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
    }




}
