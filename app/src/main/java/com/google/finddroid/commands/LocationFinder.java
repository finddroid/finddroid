package com.google.finddroid.commands;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.finddroid.PermissionChacker;
import com.google.finddroid.replyer.MultiReplyer;

public class LocationFinder {
    Context context;
    FusedLocationProviderClient mFusedLocationClient;
    String locationInString;
    LocationVar locationVar;
    StatusBarNotification statusBarNotification;
    public LocationFinder(Context mContext,StatusBarNotification sbn) {
        this.context = mContext;
        this.statusBarNotification=sbn;
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        boolean locatioPermission = new PermissionChacker(context).location();
        if (isLocationEnabled() && locatioPermission){

            // getting last
            // location from
            // FusedLocationClient
            // object
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        Log.i("Location", String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));
                        new SendLocation(statusBarNotification,context).send(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));

                    }
                }
            });
            } else {
            new MultiReplyer(statusBarNotification,context).sendReply("location is not enabled ");
            }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            assert mLastLocation != null;
            Log.i("location2",String.valueOf(mLastLocation.getLongitude())+ " "+String.valueOf(mLastLocation.getLatitude()));
            new SendLocation(statusBarNotification,context).send(String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()));

//            locationVar = new LocationVar();
//            locationVar.setLongitude(String.valueOf(mLastLocation.getLongitude()));
//            locationVar.setLatitude(String.valueOf(mLastLocation.getLatitude()));
            locationInString = String.valueOf(mLastLocation.getLatitude())+","+String.valueOf(mLastLocation.getLongitude());

        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getLocationInString(){
        return locationInString;
    }
}
