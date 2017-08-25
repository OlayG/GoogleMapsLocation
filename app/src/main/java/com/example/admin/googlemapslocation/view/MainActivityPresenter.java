package com.example.admin.googlemapslocation.view;

import android.util.Log;

import com.example.admin.googlemapslocation.model.AddressResponse;
import com.example.admin.googlemapslocation.model.MessageEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 8/24/2017.
 */

public class MainActivityPresenter implements MainActivityContract.presenter {

    private static final String TAG = "MainActivityPresenter";

    private static final String GEO_KEY = "AIzaSyBBTUtu0bRrmhLHTKm9ZuhCgXhoPnrAMUw";
    MainActivityContract.view view;

    public void attachView(MainActivityContract.view view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void getGeocodeAddress(double latitude, double longitude) {
       // Location location = view.sendLocation();
        String currentLatLng = latitude + "," + longitude;
        OkHttpClient client = new OkHttpClient();


        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("latlng", currentLatLng)
                .addQueryParameter("key", GEO_KEY)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                Log.d(TAG, "onFailure: " + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Gson gson = new Gson();

                AddressResponse addressResponse = gson.fromJson(response.body().string(), AddressResponse.class);
                Log.d(TAG, "onResponse: " + addressResponse.getResults().get(0).getFormattedAddress());

                EventBus.getDefault().post(new MessageEvent(addressResponse.getResults().get(0).getFormattedAddress()));
            }
        });


    }

    @Override
    public void getReverseGeocode(String address) {

        OkHttpClient client = new OkHttpClient();


        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("address", address)
                .addQueryParameter("key", GEO_KEY)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


                Log.d(TAG, "onFailure: " + e.toString());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                Gson gson = new Gson();

                AddressResponse addressResponse = gson.fromJson(response.body().string(), AddressResponse.class);
                Log.d(TAG, "onResponse: " + addressResponse.getResults().get(0).getGeometry().getLocation().getLat()
                + ", " + addressResponse.getResults().get(0).getGeometry().getLocation().getLng());

                //EventBus.getDefault().post(new MessageEvent(addressResponse.getResults().get(0).getFormattedAddress()));
            }
        });
    }
}
