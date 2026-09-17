package com.hola.finddroid.commands;

import android.content.Context;
import android.os.AsyncTask;

public class BackgroundWorker extends AsyncTask<Void,Void,Void> {
    boolean isEnable;
    Context context;
    public BackgroundWorker(Context context,boolean enable){
        this.isEnable=enable;
        this.context=context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        RingModeChanger ringModeChanger = new RingModeChanger(context);
//        if (this.isEnable) {
//            startRing(ringDevice);
//        }else {
//            stopRing(ringDevice);
//        }
        return null;
    }

//    public void startRing(RingDevice ringDevice){
//        ringDevice.play();
//    }
//    public void stopRing(RingDevice ringDevice){
//        ringDevice.stop();
//    }
}
