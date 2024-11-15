package com.google.finddroid.adapters.RecyclerViewAdapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.finddroid.DBs.CommanderDBHelper;
import com.google.finddroid.MainActivity;
import com.google.finddroid.R;
import com.google.finddroid.adapters.RecyclerViewAdapters.objectclasses.RecyclerViewFragtwoAdapterObj;

import java.util.ArrayList;

/** In this file set the commands and explain
 * set command and explains
 * set for reset password
 * **/
public class RecyclerViewFragtwoAdapter extends RecyclerView.Adapter<RecyclerViewFragtwoAdapter.ViewHolder> {
    ArrayList<RecyclerViewFragtwoAdapterObj> arrayList;
    Context mContext;
    public RecyclerViewFragtwoAdapter(Context context, ArrayList<RecyclerViewFragtwoAdapterObj> ItemList){
        this.arrayList=ItemList;
//        this.mContext=context;
    }
    @Override
    public RecyclerViewFragtwoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_fragment_tow,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewFragtwoAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.title.setText(arrayList.get(position).getTitle());
        holder.subtitle.setText(arrayList.get(position).getSubtile());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subtitle;
        EditText pass;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.recycler_item_fragment_title);
            subtitle=itemView.findViewById(R.id.recycler_item_fragment_subtitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getPosition() == 0 && title.getText().equals("Reset Password")){
                        showAndGetPassword();
                    }
                }
            });
        }





        public void showAndGetPassword(){
            Dialog dialog = new Dialog(mContext);
//        dialog.setContentView(R.layout.custom_alert_box);
            View v = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_box,null,false);
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
                        CommanderDBHelper commanderDBHelper = new CommanderDBHelper(mContext);
                        commanderDBHelper.DeleteDataFromCommanderPassword();
                        commanderDBHelper.InsertDataToCommanderPassword(passwordString);
                        commanderDBHelper.close();
                        MainActivity.main.finish();
                        mContext.startActivity(MainActivity.main.getIntent());
                    }
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }
}
