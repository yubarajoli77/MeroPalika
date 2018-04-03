package com.softechfoundation.municipal;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.softechfoundation.municipal.horizontalScrollMenuItem.ListItem;
import com.softechfoundation.municipal.horizontalScrollMenuItem.ListItemAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainPage extends AppCompatActivity implements OnMapReadyCallback {

    private static String[] stateNames;
    public static BottomNavigationView navigation;
    private RecyclerView recyclerView;
    private ListItemAdapter adapter;
    public static Button stateBtn, districtBtn, vdcBtn;
    public static AutoCompleteTextView searchBox;
    public static TextView catagories;
    private ArrayAdapter<String> autoComAdapter;
    private Button btnOldVdc, btnMetropolitian, btnSubMetropolitian, btnMunicipality, btnRuralMunicipality;
    public static Button btnAll;
    public static HorizontalScrollView horizontalScrollViewMenu;
    private View pathView;

    View fragmentMap;
    private GoogleMap mGoogleMap;
    private Marker marker;
    private Geocoder geocoder;
    private LatLng latLng;
    private String location;
    private List<Address> addresses = new ArrayList<>();

    private boolean isInfoWindowShown = false;
    private static final String TAG = MainPage.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        initialization();

    }

    @Override
    protected void onStart() {
        fragmentMap.setVisibility(View.GONE);
        super.onStart();
    }

    private void initialization() {
        recyclerView = findViewById(R.id.list_recycleriew);
        navigation = findViewById(R.id.navigation);
        stateBtn = findViewById(R.id.path_state_name);
        districtBtn = findViewById(R.id.path_district_name);
        vdcBtn = findViewById(R.id.path_vdc_name);
        searchBox = findViewById(R.id.search_box);
        catagories = findViewById(R.id.catagory);
        btnOldVdc = findViewById(R.id.btn_old_vdc);
        btnMetropolitian = findViewById(R.id.btn_metropolitian);
        btnSubMetropolitian = findViewById(R.id.btn_sub_metropolitian);
        btnMunicipality = findViewById(R.id.btn_municipality);
        btnRuralMunicipality = findViewById(R.id.btn_rural_municipality);
        btnAll = findViewById(R.id.btn_all);
        horizontalScrollViewMenu = findViewById(R.id.menu_horizontal_scroll_view);
        fragmentMap = findViewById(R.id.map_fragment);
        geocoder = new Geocoder(this, Locale.getDefault());
        pathView = findViewById(R.id.path_view);
        checkNetConnection();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        mainWork();
    }

    private void mainWork() {
        adapter = new ListItemAdapter(this, getData(), recyclerView);
        autoComAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, stateNames);
        recyclerView.setAdapter(adapter);
        searchBox.setAdapter(autoComAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(adapter);
                searchBox.setHint("Search States...");
                stateBtn.setText("States");
                districtBtn.setVisibility(View.GONE);
                catagories.setText("States");
                stateBtn.setVisibility(View.VISIBLE);
                vdcBtn.setVisibility(View.GONE);
                catagories.setVisibility(View.VISIBLE);
                horizontalScrollViewMenu.setVisibility(View.GONE);
            }
        });
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnAll.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnAll.setTextColor(Color.WHITE);
                recyclerView.setAdapter(ListItemAdapter.adapterAll);

                //scroll the horizontal scroll view programatically with animation and delay
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anim = ObjectAnimator.ofInt(horizontalScrollViewMenu, "scrollX", horizontalScrollViewMenu.getScrollX() + 400);
                        anim.setDuration(3000);
                        anim.start();
                    }
                }, 3000);


            }
        });

        btnMetropolitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnMetropolitian.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnMetropolitian.setTextColor(Color.WHITE);
                recyclerView.setAdapter(ListItemAdapter.adapterMetroplitan);
            }
        });
        btnSubMetropolitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnSubMetropolitian.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnSubMetropolitian.setTextColor(Color.WHITE);
                recyclerView.setAdapter(ListItemAdapter.adapterSubMetropolitan);
            }
        });

        btnMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnMunicipality.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnMunicipality.setTextColor(Color.WHITE);
                recyclerView.setAdapter(ListItemAdapter.adapterMunicipal);
            }
        });
        btnRuralMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnRuralMunicipality.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnRuralMunicipality.setTextColor(Color.WHITE);
                recyclerView.setAdapter(ListItemAdapter.adapterRuralMunicipal);
            }
        });

        btnOldVdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnOldVdc.setBackground(getResources().getDrawable(R.drawable.btn_clicked_style));
                btnOldVdc.setTextColor(Color.WHITE);
            }
        });


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                List<ListItem> newList = new ArrayList<>();
                for (ListItem list : getData()) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                adapter = new ListItemAdapter(MainPage.this, newList, recyclerView);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.setVisibility(View.GONE);
            }
        });


//        //Start Caching
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://jsonplaceholder.typicode.com/posts/1";
//
//        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                try {
//                    final String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers));
//                    JSONObject jsonObject = new JSONObject(jsonString);
//                   // textView.setText(jsonObject.toString(5));
//                    Toast.makeText(MainPage.this, "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
//                } catch (UnsupportedEncodingException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainPage.this, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(cacheRequest);
//
//    //End of Caching

    }

    private void SetAllBtnColorToDefault() {
        btnAll.setBackgroundColor(Color.TRANSPARENT);
        btnMetropolitian.setBackgroundColor(Color.TRANSPARENT);
        btnMunicipality.setBackgroundColor(Color.TRANSPARENT);
        btnRuralMunicipality.setBackgroundColor(Color.TRANSPARENT);
        btnSubMetropolitian.setBackgroundColor(Color.TRANSPARENT);
        btnOldVdc.setBackgroundColor(Color.TRANSPARENT);

        btnAll.setTextColor(getResources().getColor(R.color.black));
        btnMetropolitian.setTextColor(getResources().getColor(R.color.black));
        btnSubMetropolitian.setTextColor(getResources().getColor(R.color.black));
        btnMunicipality.setTextColor(getResources().getColor(R.color.black));
        btnRuralMunicipality.setTextColor(getResources().getColor(R.color.black));
        btnOldVdc.setTextColor(getResources().getColor(R.color.black));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history) {
            Toast.makeText(this, "There is no history yet.", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public static List<ListItem> getData() {
        List<ListItem> listItems = new ArrayList<>();
        int[] icons = {R.drawable.map, R.drawable.ministry, R.drawable.globe, R.drawable.map, R.drawable.map, R.drawable.ministry, R.drawable.globe};
        String[] names = {"State 1", "State 2", "State 3", "State 4", "State 5", "State 6", "State 7"};
        stateNames = names;
        for (int i = 0; i < names.length && i < icons.length; i++) {
            ListItem current = new ListItem();
            current.setIcon(icons[i]);
            current.setName(names[i]);
            current.setType("state");
            listItems.add(current);
        }
        return listItems;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    stateBtn.setText("States");
                    stateBtn.setVisibility(View.VISIBLE);
                    districtBtn.setVisibility(View.GONE);
                    vdcBtn.setVisibility(View.GONE);
                    catagories.setVisibility(View.VISIBLE);
                    horizontalScrollViewMenu.setVisibility(View.GONE);
                    catagories.setText("States");
                    recyclerView.setAdapter(adapter);
                    searchBox.setAdapter(autoComAdapter);
                    fragmentMap.setVisibility(View.GONE);

                    searchBox.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String typedText = s.toString().toLowerCase();
                            List<ListItem> newList = new ArrayList<>();
                            for (ListItem list : getData()) {
                                String stateName = list.getName().toLowerCase();
                                if (stateName.contains(typedText)) {
                                    newList.add(list);
                                }
                            }
                            adapter = new ListItemAdapter(MainPage.this, newList, recyclerView);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                    return true;
                case R.id.navigation_new_nepal:
                    fragmentMap.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_detail_map:
                    return true;
                case R.id.navigation_ministry:

                    return true;
            }
            return false;
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        boolean success = mGoogleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.style_json));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

        if (mGoogleMap != null) {

            //restrict the user to go outside the given latlng
            //get latlong for corners for specified place
            LatLng one = new LatLng(27.037782, 78.947213);
            LatLng two = new LatLng(29.615528, 88.627444);

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            //add them to builder
            builder.include(one);
            builder.include(two);

            LatLngBounds bounds = builder.build();

            //get width and height to current display screen
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;

            // 20% padding
            int padding = (int) (width * 0.20);

            //set latlong bounds
            mGoogleMap.setLatLngBoundsForCameraTarget(bounds);

            //move camera to fill the bound to screen
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));

            //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
            //mGoogleMap.setMinZoomPreference(mGoogleMap.getCameraPosition().zoom);
            mGoogleMap.setMinZoomPreference(6.0f);
            setUpMap();
        }

    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest
                        .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Grant The permission first", Toast.LENGTH_LONG).show();
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                //save current locationq
                latLng = point;
                Log.d("latlng::", latLng.toString());

                try {
                    addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
                    android.location.Address address = addresses.get(0);
                    addMarker(point, address);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkNetConnection();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainPage.this, android.Manifest
                        .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                        .checkSelfPermission(MainPage.this, android.Manifest
                                .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return false;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("locationEmptyCheck:", location.toString());
                double latti, longi;
                if (location != null) {
                    latti = location.getLatitude();
                    longi = location.getLongitude();
                    Log.d("lattiAndLongi::", latti + ", " + longi);
                    LatLng latLng = new LatLng(latti, longi);

                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        android.location.Address address = addresses.get(0);
                        addMarker(latLng, address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });


    }

    private void addMarker(LatLng point, Address address) {

        //remove previously placed Marker
        if (marker != null) {
            marker.remove();
            marker = null;
        }

        //place marker where user just clicked
        String locality = address.getLocality();
        String adminArea = address.getAdminArea();
        String subAdminArea = address.getSubAdminArea();
        String country = address.getCountryName();
        Log.d("address", locality + ", " + adminArea + ", " + subAdminArea + ", " + country);
        MarkerOptions markerOptions = new MarkerOptions()
                .title(locality)
                .position(point)
                .snippet(adminArea + ", " + subAdminArea + ", " + country)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true);

        marker = mGoogleMap.addMarker(markerOptions);
        // marker.showInfoWindow();
        goToLocationZoom(point.latitude, point.longitude, 10);

        Toast.makeText(MainPage.this, locality + "\n" + adminArea + ","
                + subAdminArea + ", " + ", " + country, Toast.LENGTH_SHORT).show();


        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                // isShowInfoWindow() always give false value so we use this method to show and hide infoWindow
                if (!isInfoWindowShown) {

                    marker.showInfoWindow();
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    isInfoWindowShown = true;
                } else {

                    marker.hideInfoWindow();
                    isInfoWindowShown = false;
                }

                return true;
            }
        });
        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                Log.d("System out", "onMarkerDragEnd...");
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                double latti, longi;
                if (arg0 != null) {
                    latti = arg0.getPosition().latitude;
                    longi = arg0.getPosition().longitude;
                    Log.d("lattiAndLongi::", latti + ", " + longi);
                    LatLng latLng = new LatLng(latti, longi);
                    try {
                        List<Address> addressess = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    android.location.Address address = addresses.get(0);
                    addMarker(latLng, address);
                }
            }

            @Override
            public void onMarkerDrag(Marker arg0) {

            }
        });

    }

    private void goToLocationZoom(double lat, double lng, int zoom) {
        final LatLng[] latLng = {new LatLng(lat, lng)};
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng[0], zoom);
        //add animation to zoom
        mGoogleMap.animateCamera(cameraUpdate, 1000, null);

        //zoom without animation
//        mGoogleMap.moveCamera(cameraUpdate);
//        mGoogleMap.setMinZoomPreference(10.0f);

//        LatLngBounds ADELAIDE = new LatLngBounds(
//                new LatLng(26.726658, 88.242621), new LatLng(29.774378, 80.311209));
//        // Constrain the camera target to the Adelaide bounds.
//        mGoogleMap.setLatLngBoundsForCameraTarget(ADELAIDE);
    }

    private void checkNetConnection() {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isNetworkAvailable(this)) {

            Toast.makeText(this, "Please turn on wifi or mobile data", Toast.LENGTH_LONG).show();


        } else if (!checkInternet.isOnline()) {

            Toast.makeText(this, "Internet is not available", Toast.LENGTH_LONG).show();

        }
    }

    // Ask user to exit or not
    @Override
    public void onBackPressed() {

        navigation.setVisibility(View.VISIBLE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
