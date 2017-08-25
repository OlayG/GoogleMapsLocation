package com.example.admin.googlemapslocation.model;

/**
 * Created by Admin on 8/25/2017.
 */

public class MessageEvent {

   /* public final double latitude;
    public final double longitude;

    public MessageEvent(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }*/

   public final String address;

    public MessageEvent(String address) {
        this.address = address;
    }
}
