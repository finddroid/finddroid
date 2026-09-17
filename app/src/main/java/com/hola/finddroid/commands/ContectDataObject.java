package com.hola.finddroid.commands;

public class ContectDataObject {
    public String ContactName;
    public String ContactNumber;
    public ContectDataObject(String Name,String Number){
        this.ContactName=Name;
        this.ContactNumber=Number;
    }
    public String getContactName() {
        return ContactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
