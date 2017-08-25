package com.example.admin.googlemapslocation;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.googlemapslocation.inject.DaggerMainActivityComponent;
import com.example.admin.googlemapslocation.model.MessageEvent;
import com.example.admin.googlemapslocation.receiver.MyStaticReceiver;
import com.example.admin.googlemapslocation.view.MainActivityContract;
import com.example.admin.googlemapslocation.view.MainActivityPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AbsRuntimePermission implements MainActivityContract.view {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION = 10;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Inject
    MainActivityPresenter presenter;
    @BindView(R.id.tvLatitude)
    TextView tvLatitude;
    @BindView(R.id.tvLongitude)
    TextView tvLongitude;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.tvCoordinatesB)
    TextView tvCoordinatesB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DaggerMainActivityComponent.create().inject(this);
        presenter.attachView(this);

        if (Build.VERSION.SDK_INT < 23) {
            checkPermissions();
        } else {
            requestAppPermissions(new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    R.string.msg,REQUEST_PERMISSION);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void checkPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(this, "Location not on?", Toast.LENGTH_SHORT).show();
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Toast.makeText(this, "Get Location Method", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Override
    public void displayAddress(MessageEvent address) {
        etAddress.setText(address.address);
    }

    public void getLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.d(TAG, "onSuccess: " + location.toString());
                        getLatLong(location);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }
                });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        // Do something when permision Granted
        Toast.makeText(this, "Success Permissions Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLocation();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void showError(String error) {

    }

    public void GroupOnClick(View view) {

        switch (view.getId()) {

            case R.id.btnShowLocation:
                getLocation();
                break;

            case R.id.btnLoadMap:
                double latitude = Double.valueOf(tvLatitude.getText().toString());
                double longitude = Double.valueOf(tvLongitude.getText().toString());
                presenter.getGeocodeAddress(latitude, longitude);
                break;

            case R.id.btnShowCoordinates2:
                presenter.getReverseGeocode(etAddress.getText().toString());
                break;
        }
    }

    @Override
    public void getLatLong(Location location) {
        if (location != null) {

            tvLatitude.setText("" + location.getLatitude());
            tvLongitude.setText("" + location.getLongitude());

        }
    }
}
