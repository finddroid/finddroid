package com.google.finddroid.adapters.RecyclerViewAdapters.objectclasses;

public class RecyclerItemObject {
    private String Title;
    private String SubTitle;
    private Boolean SwitchState;
    public RecyclerItemObject(String s,String Subtitle,Boolean switchState){
        this.Title=s;
        this.SubTitle=Subtitle;
        this.SwitchState=switchState;
    }

    public String getTitle() {
        return Title;
    }
    public String getSubTitle(){return SubTitle;}
    public Boolean getSwitchState(){return SwitchState;}
}
