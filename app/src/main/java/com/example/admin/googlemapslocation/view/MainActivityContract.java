package com.example.admin.googlemapslocation.view;

import android.content.Context;
import android.location.Location;

import com.example.admin.googlemapslocation.BasePresenter;
import com.example.admin.googlemapslocation.BaseView;
import com.example.admin.googlemapslocation.model.MessageEvent;

/**
 * Created by Admin on 8/24/2017.
 */

public interface MainActivityContract {

    interface view extends BaseView{

        void getLatLong(Location location);
        void getLocation();

        // TODO: 8/25/2017 add logic to alert user if location was not on
        void checkPermissions();

        void displayAddress(MessageEvent address);
    }

    interface presenter extends BasePresenter<view>{

        void getGeocodeAddress(double latitude, double longitude);
        void getReverseGeocode(String address);
    }
}
