package com.google.finddroid.UI.RecyclerViewAdapters.objectclasses;

public class RecyclerViewFragTwoAdapterObj {
    String title;
    String subtile;
    public RecyclerViewFragTwoAdapterObj(String Title, String SubTitle){
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
