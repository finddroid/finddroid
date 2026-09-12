package com.google.finddroid.UI;

/*
* The Tabs are define here in this file Setup / Passward and Commands
 */


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.finddroid.UI.fragments.FragmentOneSettings;
import com.google.finddroid.UI.fragments.FragmentTwoSettings;



public class ViewPagerAdapter extends  FragmentPagerAdapter{




    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if(position==0){
            fragment=new FragmentOneSettings();
        }else {
            fragment=new FragmentTwoSettings();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "Setup";
        }else if(position==1) {
            return "Password & Commands";
        }else{
            return null;
        }
    }
}
