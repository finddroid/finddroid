package com.google.finddroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import com.android.volley.Response;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.finddroid.UI.ViewPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;
/***
 * This is Main file here only (location access), (viewpager) and (Nevbar/Actionbar).
 ***/
public class MainActivity extends AppCompatActivity {
    int BACKGROUND_LOCATION_PERMISSION_CODE = 1000;
    int LOCATION_PERMISSION_CODE = 2000;
    CardView gitcv;
    CardView webcv;
    //main permission define to access static function from other classes
    public static Activity main;
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        main = this;
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });/// don't make code on this fucking



        /*** HERE IS THE STARTING POINT OF CODE ***/
        //notification permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1000);
        }
        /* calling notification service to start listen notifications*/
        new NotificationListener();

        /* SETUP ACTIONBAR */
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        getSupportActionBar().setElevation(0);

        /** SETUP VIEWPAGER **/
        ViewPager vp = findViewById(R.id.main_viewpager);
        TabLayout tl = findViewById(R.id.main_tab_layout);
        /** FragmentSetAdapter is viewpager **/
        ViewPagerAdapter fsa = new ViewPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(fsa);
        tl.setupWithViewPager(vp);

        gitcv = findViewById(R.id.githubimg);
        webcv = findViewById(R.id.websiteimg);
        gitAndWebImgClickListener();


//        CheckForUpdate();
    }



    /* update box for rquest user to update app */
    public void ShowUpdateBox(){
        Dialog dialog = new Dialog(MainActivity.this);
        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.update_alert_box,null,false);
        dialog.setContentView(v);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 1100;

        Button cancel_btn = dialog.findViewById(R.id.update_cancel);
        Button update_btn= dialog.findViewById(R.id.update_update);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialog.dismiss();
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://finddroid.github.io/update.html"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {

                }
            }
        });
        dialog.show();
    }



    /*NOTE: don't play with bottom code it's part of BlankFragment location permission code i don't know how it's work but it's work :) */
    private void askPermissionForBackgroundUsage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_CODE);
        }
    }


    public void CheckForUpdate(){
        /* Check for update */
        RequestQueue volleyQueue = Volley.newRequestQueue(MainActivity.this);
        // url of the api through which we get random dog images
        String url = "https://finddroid.github.io/version.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    // get the image url from the JSON object
                    String version;
                    try {
                        PackageManager packageManager = getPackageManager();

                        // Get the package info for the current app
                        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

                        // Retrieve the version name and version code
                        String versionName = packageInfo.versionName;
                        int versionCode = packageInfo.versionCode;
                        version = response.getString("version");
                        Log.i(versionName,version);
                        if (!version.equals(versionName)){
                            ShowUpdateBox();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (PackageManager.NameNotFoundException e) {
//                        throw new RuntimeException(e);
                    }
                },

                // lambda function for handling the case
                // when the HTTP request fails
                (Response.ErrorListener) error -> {
//                    Toast.makeText(MainActivity.this, "Some error occurred! Cannot fetch dog image", Toast.LENGTH_LONG).show();
//                    // log the error message in the error stream
//                    Log.e("MainActivity", "loadDogImage error: ${error.localizedMessage}");
                }
        );

        // add the json request object created above
        // to the Volley request queue
        volleyQueue.add(jsonObjectRequest);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted location permission
                // Now check if android version >= 11, if >= 11 check for Background Location Permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // Background Location Permission is granted so do your work here
                    } else {
                        // Ask for Background Location Permission
                        askPermissionForBackgroundUsage();
                    }
                }
            } else {
                // User denied location permission
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // User granted for Background Location Permission.
            } else {
                // User declined for Background Location Permission.
            }
        }

    }

    public void gitAndWebImgClickListener(){
        gitcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/huihola/finddroid.git"));
                startActivity(myIntent);
            }
        });
        webcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://finddroid.github.io/update.html"));
                startActivity(myIntent);
            }
        });
    }

}