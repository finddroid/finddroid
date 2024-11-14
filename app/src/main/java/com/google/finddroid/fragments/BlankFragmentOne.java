package com.google.finddroid.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.DBs.DBModels.DBDataModel;
import com.google.finddroid.MainActivity;
import com.google.finddroid.R;
import com.google.finddroid.adapters.RecyclerViewAdapters.CheckAndRequestLocationPermission;
import com.google.finddroid.adapters.RecyclerViewAdapters.objectclasses.RecyclerItemObject;
import com.google.finddroid.adapters.RecyclerViewAdapters.RecyclerViewAdapter;
import com.google.finddroid.global.SwitchDBGlobalVar;

import java.util.ArrayList;


public class BlankFragmentOne extends Fragment implements CheckAndRequestLocationPermission {

    int LOCATION_PERMISSION_CODE = 2000;
    int BACKGROUND_LOCATION_PERMISSION_CODE = 3000;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlankFragmentOne() {
        // Required empty public constructor
    }

    /**
     * Use getContext() factory method to create a new instance of
     * getContext() fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragmentOne.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragmentOne newInstance(String param1, String param2) {
        BlankFragmentOne fragment = new BlankFragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getContext() fragment
        return inflater.inflate(R.layout.fragment_blank_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwitchDBHelper db = new SwitchDBHelper(getContext());

        if (db.isEmpty()) {
            SwitchDBHelper switchDBHelper = new SwitchDBHelper(getContext());
            switchDBHelper.InsertData(SwitchDBGlobalVar.NOTIFICATION_ACCESS,"necessary to enable", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.ACCESSIBILITY_ACCESS,"necessary for some functionality", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.ADMIN_ACCESS,"necessary for lock functionality", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.SET_PASSWORD,"set password to control", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.LOCATION_ACCESS,"necessary to get location",false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.LOCK_FROM_COMMAND,"necessary to lock device by command",false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.RING_MODE_ACCESS,"necessary to change ring mode",false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.SMS_ACCESS,"Control Device from SMS",false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.WHATSAPP_ACCESS,"Control Device from Whatsapp message", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.TELEGRAM_ACCESS,"Control Device from Telegram message", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.ANTI_SWITCH_OFF,"Device can't be power off until unlock", false);
            switchDBHelper.InsertData(SwitchDBGlobalVar.ANTI_MODE_CHANGE,"Can't Change Mode (flight mode,internet) until unlock",false);
        } else {
            try {
                int accessEnabled = Settings.Secure.getInt(MainActivity.main.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
//                Log.i("ACCESSABLITY", Integer.toString(accessEnabled));
                if (accessEnabled == 0) {
                    db.UpdateData(SwitchDBGlobalVar.ACCESSIBILITY_ACCESS, false);
                }
            } catch (Settings.SettingNotFoundException e) {
                throw new RuntimeException(e);
            }
            db.close();
        }
        db.close();
        RecyclerView rv = view.findViewById(R.id.main_recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        SwitchDBHelper switchDBHelper=new SwitchDBHelper(getContext());
        ArrayList<DBDataModel> data= switchDBHelper.FetchData();
        ArrayList<RecyclerItemObject> arrayList = new ArrayList<RecyclerItemObject>();
        for (int i=0;i<data.size();i++){
            String title=data.get(i).switchName;
            String subtitle=data.get(i).switchContext;
            Boolean state=data.get(i).switchState;
            arrayList.add(new RecyclerItemObject(title,subtitle,state));
        }
        RecyclerViewAdapter rva=new RecyclerViewAdapter(getContext(),arrayList,this,view);
        //set recycler view to not recycle for 12 items
        rv.setItemViewCacheSize(12);
        rv.setDrawingCacheEnabled(true);
        rv.setAdapter(rva);
    }

    //Don't play with bottom code because i don't know how it's work but it's work :)
    @Override
    public void LocationClicked() {
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Fine Location permission is granted
            // Check if current android version >= 11, if >= 11 check for Background Location permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Background Location Permission is granted so do your work here
                } else {
                    // Ask for Background Location Permission
                    askPermissionForBackgroundUsage();
                }
            }
        } else {
            // Fine Location Permission is not granted so ask for permission
            askForLocationPermission();
        }
    }

    private void askForLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Needed ")
                    .setMessage("Location Permission Needed!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Permission is denied by the user
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }
    private void askPermissionForBackgroundUsage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_CODE);
//            new AlertDialog.Builder(this)
//                    .setTitle("Permission Needed")
//                    .setMessage("Background Location Permission Needed, tap \"Allow all time in the next screen\"")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_CODE);
//                        }
//                    })
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // User declined for Background Location Permission.
//                        }
//                    })
//                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_PERMISSION_CODE);
        }
    }


}