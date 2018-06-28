package com.softechfoundation.municipal.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.softechfoundation.municipal.Adapters.NewListItemAdapter;
import com.softechfoundation.municipal.Adapters.OldListItemAdapter;
import com.softechfoundation.municipal.CheckInternet.CheckInternet;
import com.softechfoundation.municipal.Activities.MainPage;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Pojos.ListItem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.RecyclerViewOnItemClickListener;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback {


    public MainFragment() {
        // Required empty public constructor
    }

    private String MY_PREFS = "SharedValues";
    private String[] stateNames;
    private RecyclerView recyclerView;
    private NewListItemAdapter newAdapter;
    private OldListItemAdapter oldAdapter;
    private Button stateBtn, districtBtn, vdcBtn, trigger;
    private Button readMessage;
    private AutoCompleteTextView searchBox;
    private TextView catagories;
    private ArrayAdapter<String> autoComAdapter;
    private Button btnOldVdc, btnMetropolitian, btnSubMetropolitian, btnMunicipality, btnRuralMunicipality;
    private Button btnAll;
    private HorizontalScrollView horizontalScrollViewMenu;
    private View pathView;
    private View topDetail;
    private Button vdcToLocalLevel, localLevelToVdc;
    private View mappingOptionView;
    private TextView chooseMappingOpt;
    private View loadingPlaces;


    private String globalState, globalDistrict, globalLocalLevel, globalOldVdc;
    private NewListItemAdapter adapterDistrict, adapterVdc, adapterMetroplitan, adapterAll;
    private OldListItemAdapter adapterOldVdc;
    private boolean isLocalToOldClicked = false;

    private NewListItemAdapter adapterSubMetropolitan, adapterMunicipal, adapterRuralMunicipal;
    private StringBuilder stringBuilder = new StringBuilder();
    private TextToSpeech tts;
    private int ACT_CHECK_TTS_DATA = 1000;

    private View fragmentMap;
    private GoogleMap mGoogleMap;
    private Marker marker;
    private Geocoder geocoder;
    private LatLng latLng;
    private String location;
    private List<Address> addresses = new ArrayList<>();

    private boolean isInfoWindowShown = false;
    private String TAG = MainPage.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // OnBoardingStart(view);
        initialization(view);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(getContext(), Locale.getDefault());

        return view;
    }

    private void OnBoardingStart(View view) {
        // We load a drawable and create a location to show a tap target here

        // We need the display to get the width and height at this point in time

        final Display display = getActivity().getWindowManager().getDefaultDisplay();

        // Load our little droid guy

        final Drawable droid = ContextCompat.getDrawable(getActivity(), R.drawable.ic_home_black_24dp);

        // Tell our droid buddy where we want him to appear

        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth() * 2, droid.getIntrinsicHeight() * 2);

        // Using deprecated methods makes you look way cool

        droidTarget.offset(display.getWidth() / 2, display.getHeight() / 2);


        final SpannableString sassyDesc = new SpannableString("It allows you to go back, sometimes");

        sassyDesc.setSpan(new StyleSpan(Typeface.ITALIC), sassyDesc.length() - "sometimes".length(), sassyDesc.length(), 0);


        // We have a sequence of targets, so lets build it!

//        final TapTargetSequence sequence = new TapTargetSequence(this)
//
//
//                .targets(
//
//                        // This tap target will target the back button, we just need to pass its containing toolbar
//
//                        TapTarget.forToolbarNavigationIcon(toolbar, "This is the back button", sassyDesc).id(1),
//
//                        // Likewise, this tap target will target the search button
//
//                        TapTarget.forToolbarMenuItem(toolbar, R.id.history, "This is a search icon", "As you can see, it has gotten pretty dark around here...")
//
//                                .dimColor(android.R.color.black)
//
//                                .outerCircleColor(R.color.colorAccent)
//
//                                .targetCircleColor(android.R.color.black)
//
//                                .transparentTarget(true)
//
//                                .textColor(android.R.color.black)
//
//                                .id(2),
//
//                        // You can also target the overflow button in your toolbar
//
//                        TapTarget.forToolbarOverflow(toolbar, "This will show more options", "But they're not useful :(").id(3),
//
//                        // This tap target will target our droid buddy at the given target rect
//
//                        TapTarget.forBounds(droidTarget, "Oh look!", "You can point to any part of the screen. You also can't cancel this one!")
//
//                                .cancelable(false)
//
//                                .icon(droid)
//
//                                .id(4)
//
//                )
//
//                .listener(new TapTargetSequence.Listener() {
//
//                    // This listener will tell us when interesting(tm) events happen in regards
//
//                    // to the sequence
//
//                    @Override
//
//                    public void onSequenceFinish() {
//
//                        Toast.makeText(MainPage.this, "Congrats, You know the functionality now", Toast.LENGTH_SHORT).show();
//                        //((TextView) findViewById(R.id.educated)).setText("Congratulations! You're educated now!");
//
//                    }
//
//
//
//                    @Override
//
//                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//
//                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
//
//                    }
//
//
//
//                    @Override
//
//                    public void onSequenceCanceled(TapTarget lastTarget) {
//
//                        final AlertDialog dialog = new AlertDialog.Builder(MainPage.this)
//
//                                .setTitle("Uh oh")
//
//                                .setMessage("You canceled the sequence")
//
//                                .setPositiveButton("Oops", null).show();
//
//                        TapTargetView.showFor(dialog,
//
//                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
//
//                                        .cancelable(false)
//
//                                        .tintTarget(false), new TapTargetView.Listener() {
//
//                                    @Override
//
//                                    public void onTargetClick(TapTargetView view) {
//
//                                        super.onTargetClick(view);
//
//                                        dialog.dismiss();
//
//                                    }
//
//                                });
//
//                    }
//
//                });


        // You don't always need a sequence, and for that there's a single time tap target

        final SpannableString spannedDesc = new SpannableString("This is the sample app for TapTargetView");

        spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "TapTargetView".length(), spannedDesc.length(), 0);

        TapTargetView.showFor(getActivity(),
                TapTarget.forView(view.findViewById(R.id.search_box),
                        "Search the state, district and vdc from here", spannedDesc)
                        .outerCircleColor(R.color.targetOuterCircleColor)      // Specify a color for the outer circle
                        .outerCircleAlpha(0.55f)            // Specify the alpha amount for the outer circle
                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.white)      // Specify the color of the title text
                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
                        .descriptionTextColor(R.color.red)  // Specify the color of the description text
                        .textColor(R.color.blue)            // Specify a color for both the title and description text
                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                        .drawShadow(true)                   // Whether to draw a drop shadow or not
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
                        //.icon(Drawable)                     // Specify a custom drawable to draw as the target
                        .targetRadius(60)
                        .cancelable(false)

                        .drawShadow(true)

                        .titleTextDimen(R.dimen.title_text_size)

                        .tintTarget(false), new TapTargetView.Listener() {


                    @Override

                    public void onTargetClick(TapTargetView view) {

                        super.onTargetClick(view);

                        // .. which evidently starts the sequence we defined earlier

                        //sequence.start();

                    }


                    @Override

                    public void onOuterCircleClick(TapTargetView view) {

                        super.onOuterCircleClick(view);

                        Toast.makeText(view.getContext(), "Click the target", Toast.LENGTH_SHORT).show();

                    }


                    @Override

                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {

                        Log.d("TapTargetViewSample", "You dismissed me :(");

                    }

                });

    }

    @Override
    public void onStart() {
        super.onStart();
        mappingOptionView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        chooseMappingOpt.setVisibility(View.VISIBLE);
        pathView.setVisibility(View.GONE);
        searchBox.setHint("Choose Option first");
        searchBox.setEnabled(false);
    }

    private void initialization(View view) {
        recyclerView = view.findViewById(R.id.list_recycleriew);
        stateBtn = view.findViewById(R.id.path_state_name);
        districtBtn = view.findViewById(R.id.path_district_name);
        vdcBtn = view.findViewById(R.id.path_vdc_name);
        searchBox = view.findViewById(R.id.search_box);
        catagories = view.findViewById(R.id.catagory);
        btnOldVdc = view.findViewById(R.id.btn_old_vdc);
        btnMetropolitian = view.findViewById(R.id.btn_metropolitian);
        btnSubMetropolitian = view.findViewById(R.id.btn_sub_metropolitian);
        btnMunicipality = view.findViewById(R.id.btn_municipality);
        btnRuralMunicipality = view.findViewById(R.id.btn_rural_municipality);
        btnAll = view.findViewById(R.id.btn_all);
        horizontalScrollViewMenu = view.findViewById(R.id.menu_horizontal_scroll_view);
        fragmentMap = view.findViewById(R.id.map_fragment);
        pathView = view.findViewById(R.id.path_view);
        trigger = view.findViewById(R.id.trigger);

        localLevelToVdc = view.findViewById(R.id.local_level_to_vdc);
        vdcToLocalLevel = view.findViewById(R.id.vdc_to_local_level);
        mappingOptionView = view.findViewById(R.id.choose_mapping_view);
        chooseMappingOpt = view.findViewById(R.id.choose_an_option);
        loadingPlaces = view.findViewById(R.id.dotted_place_loading);
        // topDetail=view.findViewById(R.id.top_detail_view);
        //checkNetConnection();

        mainWork();
    }

    private void mainWork() {

        localLevelToVdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateState("new");
            }
        });

        vdcToLocalLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateState("old");
            }
        });

        stateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocalToOldClicked) {
                    localLevelToVdc.performClick();
                } else {
                    vdcToLocalLevel.performClick();
                }

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

        districtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateDistrictRecyclerView("old");
                //Toast.makeText(context, "Inside pathDistrictBtn: "+state, Toast.LENGTH_SHORT).show();
                catagories.setText("Districts");
                searchBox.setHint("Search Districts...");
                stateBtn.setVisibility(View.VISIBLE);
                districtBtn.setVisibility(View.VISIBLE);
                districtBtn.setText("Districts");
                vdcBtn.setVisibility(View.GONE);
                catagories.setVisibility(View.VISIBLE);
                horizontalScrollViewMenu.setVisibility(View.GONE);
            }
        });

        vdcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocalToOldClicked) {
                    populateLocalLevelRecyclerView();
                    catagories.setText("Local Levels");
                    vdcBtn.setText("Local Levels");
                    searchBox.setHint("Search Local gov");
                    stateBtn.setVisibility(View.VISIBLE);
                    stateBtn.setVisibility(View.VISIBLE);
                    districtBtn.setVisibility(View.VISIBLE);
                    catagories.setVisibility(View.GONE);
                    horizontalScrollViewMenu.setVisibility(View.VISIBLE);
                } else {
                    populateOldVdcRecyclerView();
                    catagories.setText("VDCs");
                    vdcBtn.setText("VDCs");
                    searchBox.setHint("Search VDCs...");
                    stateBtn.setVisibility(View.VISIBLE);
                    stateBtn.setVisibility(View.VISIBLE);
                    districtBtn.setVisibility(View.VISIBLE);
                    catagories.setVisibility(View.VISIBLE);
                    horizontalScrollViewMenu.setVisibility(View.GONE);
                }
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnAll.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                btnAll.setTextColor(Color.WHITE);
                recyclerView.setAdapter(adapterAll);

                //scroll the horizontal scroll view programatically with animation and delay
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anim = ObjectAnimator.ofInt(horizontalScrollViewMenu, "scrollX", horizontalScrollViewMenu.getScrollX() + 400);
                        anim.setDuration(3000);
                        anim.start();
                    }
                }, 1000);


            }
        });

        btnMetropolitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnMetropolitian.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                btnMetropolitian.setTextColor(Color.WHITE);
                recyclerView.setAdapter(adapterMetroplitan);
            }
        });
        btnSubMetropolitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnSubMetropolitian.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                btnSubMetropolitian.setTextColor(Color.WHITE);
                recyclerView.setAdapter(adapterSubMetropolitan);
            }
        });

        btnMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnMunicipality.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                btnMunicipality.setTextColor(Color.WHITE);
                recyclerView.setAdapter(adapterMunicipal);
            }
        });
        btnRuralMunicipality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnRuralMunicipality.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                btnRuralMunicipality.setTextColor(Color.WHITE);
                recyclerView.setAdapter(adapterRuralMunicipal);
            }
        });

        btnOldVdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAllBtnColorToDefault();
                btnOldVdc.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                btnOldVdc.setTextColor(Color.WHITE);
            }
        });

        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace();
            }
        });


        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void populateState(String oldOrNew) {
        if ("new".equals(oldOrNew)) {
            isLocalToOldClicked = true;
            mappingOptionView.setVisibility(View.GONE);
            chooseMappingOpt.setVisibility(View.GONE);
            searchBox.setEnabled(true);
            searchBox.setHint("Search States...");
            pathView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            newAdapter = new NewListItemAdapter(getContext(), getData(), recyclerView, new RecyclerViewOnItemClickListener() {
                @Override
                public void onItemClickListener(int position, View view) {
                    // MainPage.pathView.setVisibility(View.VISIBLE)
                    loadingPlaces.setVisibility(View.VISIBLE);
                    globalState = getData().get(position).getName();
                    populateDistrictRecyclerView("new");

                }
            });
            autoComAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, stateNames);
            recyclerView.setAdapter(newAdapter);
            searchBox.setAdapter(autoComAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        } else if ("old".equals(oldOrNew)) {
            isLocalToOldClicked = false;
            mappingOptionView.setVisibility(View.GONE);
            chooseMappingOpt.setVisibility(View.GONE);
            searchBox.setEnabled(true);
            searchBox.setHint("Search States...");
            pathView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            oldAdapter = new OldListItemAdapter(getContext(), getData(), new RecyclerViewOnItemClickListener() {
                @Override
                public void onItemClickListener(int position, View view) {
                    globalState = getData().get(position).getName();
                    loadingPlaces.setVisibility(View.VISIBLE);
                    populateDistrictRecyclerView("old");
                }
            });
            autoComAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, stateNames);
            recyclerView.setAdapter(oldAdapter);
            searchBox.setAdapter(autoComAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                final List<ListItem> newList = new ArrayList<>();
                for (ListItem list : getData()) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                newAdapter = new NewListItemAdapter(getContext(), newList, recyclerView, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, View view) {
                        globalState = newList.get(position).getName();
                        loadingPlaces.setVisibility(View.VISIBLE);
                        if (isLocalToOldClicked) {
                            populateDistrictRecyclerView("new");
                        } else {
                            populateDistrictRecyclerView("old");
                        }
                    }
                });
                recyclerView.setAdapter(newAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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

    private List<ListItem> getData() {
        List<ListItem> listItems = new ArrayList<>();
        int[] icons = {R.drawable.state1_logo, R.drawable.state2_logo, R.drawable.state3_logo,
                R.drawable.state4_logo, R.drawable.state5_logo, R.drawable.state6_logo, R.drawable.state7_logo};
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        boolean success = mGoogleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getActivity().getApplicationContext(), R.raw.style_json));

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
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(getActivity().getApplicationContext(), android.Manifest
                        .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getActivity().getApplicationContext(), "Grant The permission first", Toast.LENGTH_LONG).show();
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
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest
                        .permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                        .checkSelfPermission(getActivity(), android.Manifest
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
                //Log.d("locationEmptyCheck:", location.toString());
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

//        Toast.makeText(getContext(), locality + "\n" + adminArea + ","
//                + subAdminArea + ", " + ", " + country, Toast.LENGTH_SHORT).show();


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

    public void findPlace() {
        SharedPreferences pref = getActivity().getApplicationContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE);

        String location = pref.getString("location", null);
        //location = district + ", " + vdc + ", " + "Nepal";
        Log.d("location::", String.valueOf(location));

        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);

            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                addMarker(latLng, address);
            } else {

                Toast.makeText(getActivity(), "Sorry,Unable to locate in Map", Toast.LENGTH_SHORT).show();
                // alternateChoice(district);
                // googleSearchBox();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkNetConnection() {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isNetworkAvailable(getActivity())) {

            Toast.makeText(getActivity(), "Please turn on wifi or mobile data", Toast.LENGTH_LONG).show();


        } else if (!checkInternet.isOnline()) {

            Toast.makeText(getContext(), "Internet is not available", Toast.LENGTH_LONG).show();

        }
    }

    private void getOldDetail(final String type) {
        //Start Caching
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl + "vdcs/OldVdcList/",
                globalLocalLevel);

        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONArray localLevelJsonArray = new JSONArray(jsonString);
                    stringBuilder.setLength(0);
                    stringBuilder.append(type + " " + globalLocalLevel + " includes following VDCs:\n\n");
                    for (int i = 0; i < localLevelJsonArray.length(); i++) {
                        String localLevel;
                        int j = i + 1;
                        JSONObject jsonObject1 = localLevelJsonArray.getJSONObject(i);
                        localLevel = jsonObject1.getString("oldVdc");
                        stringBuilder.append(j + ". " + localLevel + "\n");
                    }
                    showMessage(stringBuilder.toString());
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);
        //End of Caching


    }

    private void showMessage(String message) {
        SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences("TTSMessage", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        editor.putString("message", message);
        editor.apply();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.mapping_result_custom_design, null);
        dialogBuilder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.mapping_place_result);
        textView.setText(message);
        Button okBtn = dialogView.findViewById(R.id.mapping_place_btn);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationLeftRight;
        loadingPlaces.setVisibility(View.GONE);
        alertDialog.show();
        MainPage.readMessage.performClick();
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void populateLocalLevelRecyclerView() {
        final String[] allNames, ruralMunicipalNames, municipalNames, metropolitanNames, subMetropolitanNames;
        // final List<ListItem> vdcList=new ArrayList<>();
        final List<ListItem> allList = new ArrayList<>();
        final List<ListItem> metropolitanList = new ArrayList<>();
        final List<ListItem> subMetropolitanList = new ArrayList<>();
        final List<ListItem> municipalList = new ArrayList<>();
        final List<ListItem> ruralMunicipalList = new ArrayList<>();

        searchBox.setHint("Search Local gov");
        searchBox.setText("");
        catagories.setText("VDCs");
        catagories.setVisibility(View.GONE);
        horizontalScrollViewMenu.setVisibility(View.VISIBLE);
        districtBtn.setText(globalDistrict);

        stateBtn.setVisibility(View.VISIBLE);
        vdcBtn.setVisibility(View.VISIBLE);
        vdcBtn.setText("Local Levels");
        districtBtn.setVisibility(View.VISIBLE);

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl + "districts/localLevel/",
                globalDistrict);


        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray ruralMuniJsonArray = jsonObject.getJSONArray("ruralMunicipal");
                    JSONArray municipalJsonArray = jsonObject.getJSONArray("municipal");
                    JSONArray metropolitanJsonArray = jsonObject.getJSONArray("metropolitan");
                    JSONArray subMetropolitanJsonArray = jsonObject.getJSONArray("subMetropolitan");

                    //Removing duplication of data from cache
                    allList.clear();
                    ruralMunicipalList.clear();
                    municipalList.clear();
                    subMetropolitanList.clear();
                    metropolitanList.clear();

                    //for ruralMunicipal
                    for (int i = 0; i < ruralMuniJsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = ruralMuniJsonArray.getJSONObject(i);
                        String vdcName = jsonObject1.getString("ruralMunicipal");
                        listItem.setName(vdcName);
                        listItem.setIcon(R.drawable.rural_municipal);
                        listItem.setType("ruralMunicipal");
                        ruralMunicipalList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterRuralMunicipal = new NewListItemAdapter(getContext(), ruralMunicipalList, recyclerView, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            clickLocalLevelMenu("RuralMunicipal", ruralMunicipalList.get(position));
                        }
                    });
                    //for municipal
                    for (int i = 0; i < municipalJsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = municipalJsonArray.getJSONObject(i);
                        String municipalName = jsonObject1.getString("municipal");
                        listItem.setName(municipalName);
                        listItem.setIcon(R.drawable.municipal);
                        listItem.setType("municipal");
                        municipalList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterMunicipal = new NewListItemAdapter(getContext(), municipalList, recyclerView, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            clickLocalLevelMenu("municipal", municipalList.get(position));
                        }
                    });
                    //for metropolitan
                    for (int i = 0; i < metropolitanJsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = metropolitanJsonArray.getJSONObject(i);
                        String metropolitanName = jsonObject1.getString("metropolitan");
                        listItem.setName(metropolitanName);
                        listItem.setIcon(R.drawable.metropolitan);
                        listItem.setType("metropolitan");
                        metropolitanList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterMetroplitan = new NewListItemAdapter(getContext(), metropolitanList, recyclerView, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            clickLocalLevelMenu("metropolitan", metropolitanList.get(position));
                        }
                    });
                    //for subMetropolitan
                    for (int i = 0; i < subMetropolitanJsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = subMetropolitanJsonArray.getJSONObject(i);
                        String subMetropolitanName = jsonObject1.getString("subMetropolitan");
                        listItem.setName(subMetropolitanName);
                        listItem.setIcon(R.drawable.sub_metropolitan);
                        listItem.setType("subMetropolitan");
                        subMetropolitanList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterSubMetropolitan = new NewListItemAdapter(getContext(), subMetropolitanList, recyclerView, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            clickLocalLevelMenu("subMetropolitan", subMetropolitanList.get(position));
                        }
                    });

                    adapterAll = new NewListItemAdapter(getContext(), allList, recyclerView, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            clickLocalLevelMenu(allList.get(position).getType(), ruralMunicipalList.get(position));
                        }
                    });
                    loadingPlaces.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapterAll);
                    btnAll.performClick();

                    //mainPageToSetAdapter.setAdapters(adapterAll,adapterMetroplitan,adapterSubMetropolitan,adapterMunicipal,adapterRuralMunicipal);

                    //Toast.makeText(getContext(), "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPlaces.setVisibility(View.GONE);
                Toast.makeText(getContext(), "No value in district Adapter", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching

        allNames = new String[allList.size()];
        int j = 0;
        for (ListItem names : allList) {
            allNames[j] = names.getName();
            j++;
        }

        final ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, allNames);
        searchBox.setAdapter(autoComAdapter);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                final List<ListItem> newList = new ArrayList<>();
                for (ListItem list : allList) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                NewListItemAdapter filteredAdapter = new NewListItemAdapter(getContext(), newList, recyclerView, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, View view) {
                        clickLocalLevelMenu(newList.get(position).getType(), ruralMunicipalList.get(position));

                    }
                });
                recyclerView.setAdapter(filteredAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void populateDistrictRecyclerView(final String choice) {
        final String[] districtNames;
        final List<ListItem> districtList = new ArrayList<>();

        searchBox.setHint("Search District");
        searchBox.setText("");
        catagories.setText("Districts");
        stateBtn.setText(globalState);

        stateBtn.setVisibility(View.VISIBLE);
        vdcBtn.setVisibility(View.GONE);
        districtBtn.setText("Districts");
        districtBtn.setVisibility(View.VISIBLE);

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl + "districts/state/",
                globalState);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    //JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = new JSONArray(jsonString);
                    districtList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String districtName = jsonObject1.getString("district");
                        listItem.setName(districtName);
                        listItem.setIcon(R.drawable.district);
                        listItem.setType("district");

                        districtList.add(listItem);
                    }


                    adapterDistrict = new NewListItemAdapter(getContext(), districtList, recyclerView, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            ListItem listItem = districtList.get(position);
                            globalDistrict = listItem.getName();

                            String location = globalDistrict + ", " + "Nepal";
                            // findPlace(location,context);
                            SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.putString("location", location);
                            editor.apply();
                            trigger.performClick();
                            if (choice.equals("new")) {
                                populateLocalLevelRecyclerView();

                            } else if ("old".equals(choice)) {
                                populateOldVdcRecyclerView();
                            }
                        }
                    });

                    if (adapterDistrict != null) {
                        loadingPlaces.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapterDistrict);
                    } else {
                        loadingPlaces.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "No value in district Adapter", Toast.LENGTH_SHORT).show();
                    }


                    //Toast.makeText(getContext(), "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPlaces.setVisibility(View.GONE);
                Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_SHORT).show();

                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching


        districtNames = new String[districtList.size()];
        int j = 0;
        for (ListItem names : districtList) {
            districtNames[j] = names.getName();
            j++;
        }
        final ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, districtNames);
        searchBox.setAdapter(autoComAdapter);

//        adapterDistrict = new NewListItemAdapter(getContext(), districtList, recyclerView);
//        recyclerView.setAdapter(adapterDistrict);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                final List<ListItem> newList = new ArrayList<>();
                for (ListItem list : districtList) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                NewListItemAdapter filteredAdapter = new NewListItemAdapter(getContext(), newList, recyclerView, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, View view) {
                        ListItem listItem = newList.get(position);
                        globalDistrict = listItem.getName();

                        String location = globalDistrict + ", " + "Nepal";
                        // findPlace(location,context);
                        SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.putString("location", location);
                        editor.apply();
                        trigger.performClick();
                        populateLocalLevelRecyclerView();
                    }
                });
                recyclerView.setAdapter(filteredAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void clickLocalLevelMenu(String type, ListItem currentItem) {
        globalLocalLevel = currentItem.getName();
        stateBtn.setVisibility(View.VISIBLE);
        vdcBtn.setVisibility(View.VISIBLE);
        districtBtn.setVisibility(View.VISIBLE);
        vdcBtn.setText(currentItem.getName());
        catagories.setText("VDCs");

        String location = globalDistrict + ", " + globalLocalLevel + ", " + "Nepal";
        SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();

        editor.clear();
        editor.apply();
        editor.putString("location", location);
        editor.apply();
        editor.commit();
        trigger.performClick();

        getOldDetail(type);
    }

    private void populateOldVdcRecyclerView() {
        final String[] oldVdcNames;
        final List<ListItem> oldVdcList = new ArrayList<>();

        stateBtn.setVisibility(View.VISIBLE);
        vdcBtn.setVisibility(View.VISIBLE);
        vdcBtn.setText("VDCs");
        districtBtn.setVisibility(View.VISIBLE);
        districtBtn.setText(globalDistrict);
        searchBox.setHint("Search VDCs");
        catagories.setText("VDCs");

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl + "vdcs/district/",
                globalDistrict);


        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    // JSONObject jsonObject = new JSONObject(jsonString);
                    // JSONArray oldVdcJsonArray = jsonObject.getJSONArray("oldVdcs");
                    JSONArray oldVdcJsonArray = new JSONArray(jsonString);
                    //Removing duplication of data from cache
                    oldVdcList.clear();

                    for (int i = 0; i < oldVdcJsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = oldVdcJsonArray.getJSONObject(i);
                        String vdcName = jsonObject1.getString("oldVdc");
                        listItem.setName(vdcName);
                        listItem.setIcon(R.drawable.rural_municipal);
                        listItem.setType("oldVdc");
                        oldVdcList.add(listItem);
                    }
                    adapterOldVdc = new OldListItemAdapter(getContext(), oldVdcList, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            globalOldVdc = oldVdcList.get(position).getName();
                            stateBtn.setVisibility(View.VISIBLE);
                            vdcBtn.setVisibility(View.VISIBLE);
                            districtBtn.setVisibility(View.VISIBLE);
                            vdcBtn.setText(oldVdcList.get(position).getName());
                            catagories.setText("VDCs");

                            String location = globalDistrict + ", " + globalOldVdc + ", " + "Nepal";
                            SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                            editor.clear();
                            editor.apply();
                            editor.putString("location", location);
                            editor.apply();
                            editor.commit();
                            trigger.performClick();
                            getNewDetail();
                        }
                    });
                    loadingPlaces.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapterOldVdc);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching

        oldVdcNames = new String[oldVdcList.size()];
        int j = 0;
        for (ListItem names : oldVdcList) {
            oldVdcNames[j] = names.getName();
            j++;
        }

        final ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, oldVdcNames);
        searchBox.setAdapter(autoComAdapter);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                final List<ListItem> newList = new ArrayList<>();
                for (ListItem list : oldVdcList) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                OldListItemAdapter filteredAdapter = new OldListItemAdapter(getContext(), newList, new RecyclerViewOnItemClickListener() {
                    @Override
                    public void onItemClickListener(int position, View view) {
                        globalOldVdc = newList.get(position).getName();
                        stateBtn.setVisibility(View.VISIBLE);
                        vdcBtn.setVisibility(View.VISIBLE);
                        districtBtn.setVisibility(View.VISIBLE);
                        vdcBtn.setText(oldVdcList.get(position).getName());
                        catagories.setText("VDCs");

                        String location = globalDistrict + ", " + globalOldVdc + ", " + "Nepal";
                        SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                        editor.clear();
                        editor.apply();
                        editor.putString("location", location);
                        editor.apply();
                        editor.commit();
                        trigger.performClick();
                        getNewDetail();
                    }
                });
                if (null != filteredAdapter) {
                    recyclerView.setAdapter(filteredAdapter);
                } else {
                    recyclerView.setAdapter(adapterDistrict);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getNewDetail() {
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl + "vdcs/oldVdc/",
                globalOldVdc);


        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    JSONArray localLevelJsonArray = new JSONArray(jsonString);

                    //Removing duplication of data from cache
                    stringBuilder.setLength(0);
                    stringBuilder.append(globalOldVdc + " VDC" + " lies on\n\n");
                    for (int i = 0; i < localLevelJsonArray.length(); i++) {
                        String localLevel, type;
                        JSONObject jsonObject1 = localLevelJsonArray.getJSONObject(i);
                        localLevel = jsonObject1.getString("newVdc");
                        type = jsonObject1.getString("localLevelType");
                        stringBuilder.append(localLevel + " " + type + "\n");
                        showMessage(stringBuilder.toString());
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private String makeFinalUrl(String baseUrl, String param) {
        String parameter = param;
        String urlWithParameter = null;
        String encodedUrl = null;
        if (param != null) {
            if (param.contains(" ")) {

                parameter = param.replaceAll(" ", "%20");
            }
            try {
                urlWithParameter = baseUrl + java.net.URLEncoder.encode(parameter, "UTF-8");
                Log.d("Pre URL::", urlWithParameter);
                urlWithParameter = urlWithParameter.replaceAll("%2520", "%20");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                URL URL = new URL(urlWithParameter);
                encodedUrl = String.valueOf(URL);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            URL URL = null;
            try {
                URL = new URL(baseUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            encodedUrl = String.valueOf(URL);
        }

        Log.d("Final Url: ", encodedUrl.toString());
        return encodedUrl;


    }

}
