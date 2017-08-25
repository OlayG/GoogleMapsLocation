package com.example.admin.googlemapslocation.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Admin on 8/25/2017.
 */

public class MyStaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {

            case Intent.ACTION_LOCALE_CHANGED:
                Toast.makeText(context, "Location Toggled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
