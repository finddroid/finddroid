package com.google.finddroid.adapters.RecyclerViewAdapters.objectclasses;

public class RecyclerViewFragtwoAdapterObj {
    String title;
    String subtile;
    public RecyclerViewFragtwoAdapterObj(String Title,String SubTitle){
        this.title=Title;
        this.subtile=SubTitle;
    }
    public String getSubtile() {
        return subtile;
    }

    public String getTitle() {
        return title;
    }
}
