package com.hola.finddroid.commands;

public class LocationVar {
    String Latitude;
    String Longitude;
    String Address;

    public String getAddress() {
        return Address;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
