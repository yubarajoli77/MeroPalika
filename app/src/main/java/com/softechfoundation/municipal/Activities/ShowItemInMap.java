package com.softechfoundation.municipal.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softechfoundation.municipal.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ShowItemInMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private Marker marker;
    private Geocoder geocoder;
    private LatLng latLng;
    private String resSerMapAddress;
    private String resSerAddress;
    private String name;
    private List<Address> addresses = new ArrayList<>();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_in_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.item_show_map_fragment);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(ShowItemInMap.this, Locale.getDefault());

        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        resSerAddress=intent.getStringExtra("location");
        resSerMapAddress=name+", "+resSerAddress;



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng sydney = new LatLng(-33.867, 151.206);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest
                .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest
                        .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mGoogleMap.setMyLocationEnabled(true);
       // addMarker("Sydney","The most populous city in Australia.",sydney);


//        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                LocationManager locationManager = (LocationManager) ShowItemInMap.this.getSystemService(Context.LOCATION_SERVICE);
//                if (ActivityCompat.checkSelfPermission(ShowItemInMap.this, android.Manifest
//                        .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
//                        .checkSelfPermission(ShowItemInMap.this, android.Manifest
//                                .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return false;
//                }
//                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                //Log.d("locationEmptyCheck:", location.toString());
//                double latti, longi;
//                if (location != null) {
//                    latti = location.getLatitude();
//                    longi = location.getLongitude();
//                    Log.d("lattiAndLongi::", latti + ", " + longi);
//                    LatLng latLng = new LatLng(latti, longi);
//
//                    try {
//                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                        android.location.Address address = addresses.get(0);
//                        addMarker(latLng, address);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return false;
//            }
//        });

        try {
            List<Address> addressList = geocoder.getFromLocationName(resSerMapAddress, 1);

            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                addMarkerInMap(name,resSerAddress,latLng);
            } else {

                Toast.makeText(this, "Sorry,Unable to locate in Map", Toast.LENGTH_SHORT).show();
                // alternateChoice(district);
                // googleSearchBox();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addMarkerInMap(String title,String discription,LatLng latLng){
        mGoogleMap.addMarker(new MarkerOptions()
                .title(title)
                .snippet(discription)
                .position(latLng));
        goToLocationZoom(latLng.latitude,latLng.longitude);
    }

    private void goToLocationZoom(double lat, double lng) {
        final LatLng[] latLng = {new LatLng(lat, lng)};
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng[0], 15);
        //add animation to zoom
        mGoogleMap.animateCamera(cameraUpdate, 1000, null);
    }

}
