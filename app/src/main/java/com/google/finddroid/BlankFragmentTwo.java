package com.google.finddroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlankFragmentTwo extends Fragment {

//****************************************not usable *************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        RecyclerView recyclerView = view.findViewById(R.id.fragment_two_recycler_view);
//        ArrayList<RecyclerViewFragtwoAdapterObj> itemList=new ArrayList<>();
//        itemList.add(new RecyclerViewFragtwoAdapterObj("Title","subtitle"));
//        recyclerView.setAdapter(new RecyclerViewFragtwoAdapter(getContext(),itemList));
    }
}