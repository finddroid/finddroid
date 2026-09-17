package com.hola.finddroid.experiments;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.RequiresApi;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/* NOT USED YET MAYBE IN FUTURE */
@RequiresApi(api = Build.VERSION_CODES.P)
public class BiometricPromptRunner extends BiometricPrompt.AuthenticationCallback {
//    BiometricPrompt biometricPrompt;
    CancellationSignal cancellationSignal;
    Context context;
    public BiometricPromptRunner(Context context) {
        this.context=context;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void run(){
        Executor executor = Executors.newFixedThreadPool(2);
        BiometricPrompt biometricPrompt = new BiometricPrompt
                .Builder(context)
                .setTitle("Title of Prompt")
                .setSubtitle("Subtitle")
                .setDescription("Uses FP")
                .setConfirmationRequired(true)
                .setNegativeButton("Cancel", context.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void
                    onClick(DialogInterface dialogInterface, int i)
                    {
                    }
                }).build();
        biometricPrompt.authenticate(
                getCancellationSignal(),
                executor,
                this);
    }


    private CancellationSignal getCancellationSignal()
    {
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(
                new CancellationSignal.OnCancelListener() {
                    @Override public void onCancel()
                    {
//                        Log.i("BIOMATRIC","Authentication was Cancelled by the user");
                    }
                });
        return cancellationSignal;
    }


    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
//        Log.i("biomatrics", (String) errString);
    }

//    @Override
//    public void onAuthenticationFailed() {
//        super.onAuthenticationFailed();
//        Log.i("biomatrics","faild");
//    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
//        Log.i("Biomatrics","Success");
    }
}
