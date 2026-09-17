package com.hola.finddroid.UI.RecyclerViewAdapters.objectclasses;

public class RecyclerViewFragOneAdapterObj {
    private String Title;
    private String SubTitle;
    private Boolean SwitchState;
    public RecyclerViewFragOneAdapterObj(String s, String Subtitle, Boolean switchState){
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
