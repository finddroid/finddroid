package com.google.finddroid.adapters.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.finddroid.R;
import com.google.finddroid.DBs.SwitchDBHelper;
import com.google.finddroid.adapters.RecyclerViewAdapters.objectclasses.RecyclerItemObject;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    ArrayList<RecyclerItemObject> arrayList;
    CheckAndRequestLocationPermission chackAndSendshowLocation=null;
    Context context;
    View GlobleView;
    public RecyclerViewAdapter(Context mContext, ArrayList<RecyclerItemObject> marrayList, CheckAndRequestLocationPermission checkAndRequestLocationPermission,View view){
        this.arrayList=marrayList;
        this.chackAndSendshowLocation= checkAndRequestLocationPermission;
//        this.context=mContext;
        this.GlobleView=view;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflaterView= LayoutInflater.from(context).inflate(R.layout.recycler_item_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(inflaterView);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int ButtonNumber=position;
        holder.Title.setText(arrayList.get(position).getTitle());
        holder.SubTitle.setText(arrayList.get(position).getSubTitle());
        holder.mButton.setChecked(arrayList.get(position).getSwitchState());
        //on touch listener to make switch on on touch
        holder.mButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Log.i("bool",Boolean.toString(b));
                       new RecyclerSwitchClickAction(holder, context, ButtonNumber,b,chackAndSendshowLocation,GlobleView).executeAction();

            }
        });


        //On touch condition on switch
        holder.mButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    touchConditionSetter(holder,ButtonNumber);
                }else{
                    //pass
                }
                return false;
            }
        });
    }

    public void touchConditionSetter(ViewHolder holder,int ButtonNumber){
        SwitchDBHelper switchDBHelper=new SwitchDBHelper(context);
        // i don't know how this code work so don't touch it because it works :)
        // button state and switch state to false
        if(holder.mButton.isChecked()) {
            holder.mButton.setChecked(true);
            switchDBHelper.UpdateData(holder.Title.getText().toString(),false);
            switchDBHelper.close();

        }
        // button state and switch state to true
        else if(!holder.mButton.isChecked()){
            holder.mButton.setChecked(false);
            switchDBHelper.UpdateData(holder.Title.getText().toString(),true);
            switchDBHelper.close();
        }
    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView Title;
        TextView SubTitle;
        SwitchCompat mButton;
        LinearLayout firstLinearLayout , mainLinearLayout;
        CardView cardView;
        LinearLayout passwordPerent,passwordButtons;
        EditText password;
        Button passSetButton,passCancelButton;
        RecyclerView rv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title=itemView.findViewById(R.id.list_item_title);
            SubTitle=itemView.findViewById(R.id.listview_subtitle);
            mButton=itemView.findViewById(R.id.list_item_button);
            cardView=itemView.findViewById(R.id.recycler_item_cardView);
            firstLinearLayout=itemView.findViewById(R.id.recycler_item_first_linearLayout);
            passwordPerent=itemView.findViewById(R.id.password_perent);
            passwordButtons=itemView.findViewById(R.id.password_button);
            passSetButton=itemView.findViewById(R.id.pass_set_btn);
            passCancelButton=itemView.findViewById(R.id.pass_cancel_btn);
            password=itemView.findViewById(R.id.pass_edit_text);
            mainLinearLayout = itemView.findViewById(R.id.recycler_item_main_linear_layout);
        }
    }
}
