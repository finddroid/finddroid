package com.google.finddroid.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.MainActivity;
import com.google.finddroid.R;
import com.google.finddroid.adapters.RecyclerViewAdapters.RecyclerViewFragtwoAdapter;
import com.google.finddroid.adapters.RecyclerViewAdapters.objectclasses.RecyclerViewFragtwoAdapterObj;
import com.google.finddroid.global.CommandsGlobalVar;
//import com.google.finddroid.adapters.RecyclerViewFragtwoClickable;;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class FragmentTwoSettings extends Fragment{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    public boolean isRestShow = false;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentTwoSettings() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentTwoSettings newInstance(int columnCount) {
        FragmentTwoSettings fragment = new FragmentTwoSettings();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_two_recycler_view);
        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(getContext());
        String currentPassword=commanderDBHelper.FetchDataFromCommanderPassword();
        ArrayList<RecyclerViewFragtwoAdapterObj> itemList=new ArrayList<>();
        if(commanderDBHelper.isEmptyCommanderPassword()) {
//            itemList.add(new RecyclerViewFragtwoAdapterObj("Set Password","set password to control"));
        }else {
            itemList.add(new RecyclerViewFragtwoAdapterObj("Reset Password", "current is " + currentPassword));
            isRestShow = true;
        }
        itemList.add(new RecyclerViewFragtwoAdapterObj(CommandsGlobalVar.START_FINDDROID_COMMAND +currentPassword,CommandsGlobalVar.Description.START_FINDDROID_COMMAND_DESCRIPTION));
        itemList.add(new RecyclerViewFragtwoAdapterObj(CommandsGlobalVar.FLESH_CONTROL_COMMAND +"on/off",CommandsGlobalVar.Description.FLESH_CONTROL_COMMAND_DESCRIPTION));
        itemList.add(new RecyclerViewFragtwoAdapterObj(CommandsGlobalVar.LOCATION_COMMAND,CommandsGlobalVar.Description.LOCATION_COMMAND_DESCRIPTION));
        itemList.add(new RecyclerViewFragtwoAdapterObj(CommandsGlobalVar.LOCK_DEVICE_COMMAND,CommandsGlobalVar.Description.LOCK_DEVICE_COMMAND_DESCRIPTION));
        itemList.add(new RecyclerViewFragtwoAdapterObj(CommandsGlobalVar.RING_MODE_COMMAND + CommandsGlobalVar.NORMAL_RING + "/" + CommandsGlobalVar.SILENT_RING,CommandsGlobalVar.Description.RING_MODE_COMMAND_DESCRIPTION));
        itemList.add(new RecyclerViewFragtwoAdapterObj(CommandsGlobalVar.END_FINDDROID_COMMAND,CommandsGlobalVar.Description.END_FINDDROI_COMMAND_DESCRIPTION));
        recyclerView.setAdapter(new RecyclerViewFragtwoAdapter(getContext(),itemList));
    }


    public void ShowPasswordBox(){
        Dialog dialog = new Dialog(getContext());
//        dialog.setContentView(R.layout.custom_alert_box);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_alert_box,null,false);
        dialog.setContentView(v);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 900;

        Button set_btn = dialog.findViewById(R.id.custom_alert_box_set_btn);
        Button cancel_btn= dialog.findViewById(R.id.custom_alert_box_cancel_btn);
        EditText passward = dialog.findViewById(R.id.custom_alert_box_password);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordString = passward.getText().toString();
//                Log.i("passwoard",passwordString.toString());
                if (passwordString != null){
                    CommanderDBHelper commanderDBHelper = new CommanderDBHelper(getContext());
                    commanderDBHelper.DeleteDataFromCommanderPassword();
                    commanderDBHelper.InsertDataToCommanderPassword(passwordString);
                    commanderDBHelper.close();
                    MainActivity.main.finish();
                    startActivity(MainActivity.main.getIntent());
                    Toast.makeText(getContext(),"Reset password successful",Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(PasswardActivity.this,MainActivity.class);
//                    startActivity(i);
                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.show();
    }
}