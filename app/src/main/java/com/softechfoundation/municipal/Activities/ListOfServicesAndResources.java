package com.softechfoundation.municipal.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Adapters.ResourceCustomAdapter;
import com.softechfoundation.municipal.Adapters.ServiceCustomAdapter;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Pojos.LocalLevelResponsePojo;
import com.softechfoundation.municipal.Pojos.ResourcePojo;
import com.softechfoundation.municipal.Pojos.ServicePojo;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class ListOfServicesAndResources extends AppCompatActivity {

    private RecyclerView resourceRecyclerView;

    private ResourceCustomAdapter allResourceCustomAdapter, mountainCustomAdapter,
            riverCustomAdapter, lakeCustomAdapter, protectedAreaCustomAdapter, waterFallCustomAdapter;
    private ServiceCustomAdapter serviceCustomAdapter, hydroCustomAdapter,
            airportCustomAdapter, industryCustomAdapter, hotelCustomAdapter, academicInstiAdapter;
    private ServiceCustomAdapter urgentAllCustomAdapter, urgentHospitalCustomAdapter,
            urgentBloodBankCustomAdapter, urgentAtmCustomAdapter, urgentPoliceCustomAdapter;
    private ResourceCustomAdapter mainAttractionCustomAdapter;

    private View naturalResourceMenu, urgentServicesMenu, infraStructureMenu;

    private Button nResourceRiverBtn, nResourceMountainBtn,
            nResourceLakeBtn, nResourceWaterfallsBtn, nResourceProtectedAreaBtn, nReaourceAllBtn;
    private Button infraAirportBtn, infraHotelBtn, infraIndustryBtn, infraHydropowerBtn, infraAllBtn, infraAcademicInstiBtn;
    private Button urgentHospitalBtn, urgentBloodBankBtn, urgentAtmBtn, urgentPoliceBtn, urgentAllBtn;
    private HorizontalScrollView horizontalScrollViewMenu;
    private View resourceServiceLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of_services_and_resources);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        defineView();
        mainProcessing();
    }

    private void mainProcessing() {
        Intent intent = getIntent();
        String catagory = intent.getStringExtra("catagory");
        String state = intent.getStringExtra("state");
        String selectedFilter = intent.getStringExtra("selectedFilter");
        String selectedFilterType = intent.getStringExtra("selectedFilterType");
        if ("URGENTSERVICES".equals(catagory)) {
            urgentServicesMenu.setVisibility(View.VISIBLE);
            naturalResourceMenu.setVisibility(View.GONE);
            infraStructureMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Urgent Servicces");
            getUrgentServices(state, selectedFilter, selectedFilterType);
        } else if ("RESOURCES".equals(catagory)) {
            naturalResourceMenu.setVisibility(View.VISIBLE);
            urgentServicesMenu.setVisibility(View.GONE);
            infraStructureMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Natural Resources");
            getResources(state, selectedFilter, selectedFilterType);
        } else if ("INFRASTRUCTURES".equals(catagory)) {

            infraStructureMenu.setVisibility(View.VISIBLE);
            naturalResourceMenu.setVisibility(View.GONE);
            urgentServicesMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Infrastructures");
            getInfrastructure(state, selectedFilter, selectedFilterType);
        } else if ("MAINATTRACTIONS".equals(catagory)) {
            infraStructureMenu.setVisibility(View.GONE);
            naturalResourceMenu.setVisibility(View.GONE);
            urgentServicesMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Main Attractions");
            getMainAttraction(state, selectedFilter, selectedFilterType);

        } else if ("AVALIABLECONTACTS".equals(catagory)) {
            infraStructureMenu.setVisibility(View.GONE);
            naturalResourceMenu.setVisibility(View.GONE);
            urgentServicesMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Available Contacts");
            Toast.makeText(this, "Comming Soon :)", Toast.LENGTH_SHORT).show();
            resourceServiceLoading.setVisibility(View.GONE);
        }

        infraAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllInfraButtonTransparent();
                infraAllBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                infraAllBtn.setTextColor(Color.WHITE);
                if(serviceCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(serviceCustomAdapter);

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
        infraHydropowerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllInfraButtonTransparent();
                infraHydropowerBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                infraHydropowerBtn.setTextColor(Color.WHITE);
                if(hydroCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(hydroCustomAdapter);
            }
        });
        infraIndustryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllInfraButtonTransparent();
                infraIndustryBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                infraIndustryBtn.setTextColor(Color.WHITE);
                if(industryCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(industryCustomAdapter);
            }
        });
        infraAirportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllInfraButtonTransparent();
                infraAirportBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                infraAirportBtn.setTextColor(Color.WHITE);
                if(airportCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(airportCustomAdapter);
            }
        });
        infraHotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllInfraButtonTransparent();
                infraHotelBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                infraHotelBtn.setTextColor(Color.WHITE);
                Toast.makeText(ListOfServicesAndResources.this, "Comming Soon :) ", Toast.LENGTH_SHORT).show();
                resourceRecyclerView.setAdapter(hotelCustomAdapter);
            }
        });
        infraAcademicInstiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllInfraButtonTransparent();
                infraAcademicInstiBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                infraAcademicInstiBtn.setTextColor(Color.WHITE);
                if(academicInstiAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setAdapter(academicInstiAdapter);
            }
        });

        urgentAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUrgentButtonTransparent();
                urgentAllBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                urgentAllBtn.setTextColor(Color.WHITE);
                if(urgentAllCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(urgentAllCustomAdapter);
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
        urgentHospitalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUrgentButtonTransparent();
                urgentHospitalBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                urgentHospitalBtn.setTextColor(Color.WHITE);
                if(urgentHospitalCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(urgentHospitalCustomAdapter);
            }
        });
        urgentAtmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUrgentButtonTransparent();
                urgentAtmBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                urgentAtmBtn.setTextColor(Color.WHITE);
//                if(urgentAtmCustomAdapter.getItemCount()==0){
//                    Toast.makeText(ListOfServicesAndResources.this, "There are items in this section", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(ListOfServicesAndResources.this, "Comming Soon :)", Toast.LENGTH_SHORT).show();
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(urgentAtmCustomAdapter);
            }
        });
        urgentBloodBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUrgentButtonTransparent();
                urgentBloodBankBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                urgentBloodBankBtn.setTextColor(Color.WHITE);
//                if(urgentBloodBankCustomAdapter.getItemCount()==0){
//                    Toast.makeText(ListOfServicesAndResources.this, "There are items in this section", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(ListOfServicesAndResources.this, "Comming Soon :)", Toast.LENGTH_SHORT).show();
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(urgentBloodBankCustomAdapter);
            }
        });
        urgentPoliceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUrgentButtonTransparent();
                urgentPoliceBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                urgentPoliceBtn.setTextColor(Color.WHITE);
//                if(urgentPoliceCustomAdapter.getItemCount()==0){
//                    Toast.makeText(ListOfServicesAndResources.this, "There are items in this section", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(ListOfServicesAndResources.this, "Comming Soon :)", Toast.LENGTH_SHORT).show();
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(urgentPoliceCustomAdapter);
            }
        });

        nReaourceAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNresoureButtonTransparent();
                nReaourceAllBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                nReaourceAllBtn.setTextColor(Color.WHITE);
                if(allResourceCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(allResourceCustomAdapter);
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
        nResourceProtectedAreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNresoureButtonTransparent();
                nResourceProtectedAreaBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                nResourceProtectedAreaBtn.setTextColor(Color.WHITE);
                if(protectedAreaCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(protectedAreaCustomAdapter);
            }
        });
        nResourceWaterfallsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNresoureButtonTransparent();
                nResourceWaterfallsBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                nResourceWaterfallsBtn.setTextColor(Color.WHITE);
                if(waterFallCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(waterFallCustomAdapter);
            }
        });
        nResourceMountainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNresoureButtonTransparent();
                nResourceMountainBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                nResourceMountainBtn.setTextColor(Color.WHITE);
                if(mountainCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(mountainCustomAdapter);
            }
        });
        nResourceLakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNresoureButtonTransparent();
                nResourceLakeBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                nResourceLakeBtn.setTextColor(Color.WHITE);
                if(lakeCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(lakeCustomAdapter);
            }
        });
        nResourceRiverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllNresoureButtonTransparent();
                nResourceRiverBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                nResourceRiverBtn.setTextColor(Color.WHITE);
                if(riverCustomAdapter.getItemCount()==0){
                    Toast.makeText(ListOfServicesAndResources.this, "There are no items in this section", Toast.LENGTH_SHORT).show();
                }
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(riverCustomAdapter);
            }
        });


    }

    private void getMainAttraction(String parameter, final String selectedFilter, final String selectedFilterType) {
        final List<ResourcePojo> mainAttractionList = new ArrayList<>();
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl + "famousFor/getAttraction/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    //JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = new JSONArray(jsonString);
                    mainAttractionList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String attractionName = jsonObject1.getString("item");
                        String attractionDistrict = jsonObject1.getString("district");
                        String attractionDescription = jsonObject1.getString("description");
                        //String addresString=jsonObject1.getString("addresString");
                        listItem.setName(attractionName);
                        listItem.setAddress(attractionDistrict + ", " + "Nepal");
                        mainAttractionList.add(listItem);
                    }

                    if ("state".equals(selectedFilterType)) {
                        mainAttractionCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, mainAttractionList);
                        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(mainAttractionCustomAdapter);
                    } else if ("district".equals(selectedFilterType)) {
                        List<ResourcePojo> filteredList = new ArrayList<>();
                        for (ResourcePojo item : mainAttractionList) {
                            if (item.getDistrict().equals(selectedFilter)) {
                                filteredList.add(item);
                            }
                        }
                        mainAttractionCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(mainAttractionCustomAdapter);
                    }
                    infraAllBtn.performClick();

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resourceServiceLoading.setVisibility(View.GONE);
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private void setAllNresoureButtonTransparent() {
        nReaourceAllBtn.setBackgroundColor(Color.TRANSPARENT);
        nResourceRiverBtn.setBackgroundColor(Color.TRANSPARENT);
        nResourceLakeBtn.setBackgroundColor(Color.TRANSPARENT);
        nResourceMountainBtn.setBackgroundColor(Color.TRANSPARENT);
        nResourceWaterfallsBtn.setBackgroundColor(Color.TRANSPARENT);
        nResourceProtectedAreaBtn.setBackgroundColor(Color.TRANSPARENT);


        nReaourceAllBtn.setTextColor(getResources().getColor(R.color.black));
        nResourceRiverBtn.setTextColor(getResources().getColor(R.color.black));
        nResourceLakeBtn.setTextColor(getResources().getColor(R.color.black));
        nResourceMountainBtn.setTextColor(getResources().getColor(R.color.black));
        nResourceWaterfallsBtn.setTextColor(getResources().getColor(R.color.black));
        nResourceProtectedAreaBtn.setTextColor(getResources().getColor(R.color.black));

    }

    private void setAllUrgentButtonTransparent() {
        urgentAllBtn.setBackgroundColor(Color.TRANSPARENT);
        urgentPoliceBtn.setBackgroundColor(Color.TRANSPARENT);
        urgentBloodBankBtn.setBackgroundColor(Color.TRANSPARENT);
        urgentAtmBtn.setBackgroundColor(Color.TRANSPARENT);
        urgentHospitalBtn.setBackgroundColor(Color.TRANSPARENT);

        urgentAllBtn.setTextColor(getResources().getColor(R.color.black));
        urgentPoliceBtn.setTextColor(getResources().getColor(R.color.black));
        urgentBloodBankBtn.setTextColor(getResources().getColor(R.color.black));
        urgentAtmBtn.setTextColor(getResources().getColor(R.color.black));
        urgentHospitalBtn.setTextColor(getResources().getColor(R.color.black));
    }

    private void setAllInfraButtonTransparent() {
        infraAllBtn.setBackgroundColor(Color.TRANSPARENT);
        infraHotelBtn.setBackgroundColor(Color.TRANSPARENT);
        infraAirportBtn.setBackgroundColor(Color.TRANSPARENT);
        infraIndustryBtn.setBackgroundColor(Color.TRANSPARENT);
        infraHydropowerBtn.setBackgroundColor(Color.TRANSPARENT);
        infraAcademicInstiBtn.setBackgroundColor(Color.TRANSPARENT);

        infraAllBtn.setTextColor(getResources().getColor(R.color.black));
        infraHotelBtn.setTextColor(getResources().getColor(R.color.black));
        infraAirportBtn.setTextColor(getResources().getColor(R.color.black));
        infraIndustryBtn.setTextColor(getResources().getColor(R.color.black));
        infraHydropowerBtn.setTextColor(getResources().getColor(R.color.black));
        infraAcademicInstiBtn.setTextColor(getResources().getColor(R.color.black));
    }

    private void defineView() {
        resourceRecyclerView = findViewById(R.id.state_detail_list);
        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        naturalResourceMenu = findViewById(R.id.natural_resources_catagory);
        urgentServicesMenu = findViewById(R.id.urgent_services_catagory);
        infraStructureMenu = findViewById(R.id.infrastructure_catagory);

        nResourceRiverBtn = findViewById(R.id.natural_resources_btn_rivers);
        nResourceLakeBtn = findViewById(R.id.natural_resources_btn_lakes);
        nResourceMountainBtn = findViewById(R.id.natural_resources_btn_mountains);
        nResourceWaterfallsBtn = findViewById(R.id.natural_resources_btn_water_fall);
        nResourceProtectedAreaBtn = findViewById(R.id.natural_resources_btn_protected_areas);
        nReaourceAllBtn = findViewById(R.id.natural_resources_btn_all);

        infraAirportBtn = findViewById(R.id.services_btn_airport);
        infraHotelBtn = findViewById(R.id.services_btn_hotel);
        infraIndustryBtn = findViewById(R.id.services_btn_industry);
        infraHydropowerBtn = findViewById(R.id.services_btn_hydropower);
        infraAllBtn = findViewById(R.id.services_btn_all);
        infraAcademicInstiBtn = findViewById(R.id.services_btn_academic_insti);

        urgentHospitalBtn = findViewById(R.id.urgent_services_btn_hospital);
        urgentAtmBtn = findViewById(R.id.urgent_services_btn_atm);
        urgentBloodBankBtn = findViewById(R.id.urgent_services_btn_blood_bank);
        urgentPoliceBtn = findViewById(R.id.urgent_services_btn_police);
        urgentAllBtn = findViewById(R.id.urgent_services_btn_all);

        horizontalScrollViewMenu = findViewById(R.id.horizontal_scroll_catagories);

        resourceServiceLoading = findViewById(R.id.dotted_res_ser_loading);

    }

    private void getInfrastructure(String parameter, final String selectedFilter, final String selectedFilterType) {
        final List<ServicePojo> allInfrastructureList = new ArrayList<>();
        final List<ServicePojo> airportList = new ArrayList<>();
        final List<ServicePojo> hydropowerList = new ArrayList<>();
        final List<ServicePojo> industryList = new ArrayList<>();
        final List<ServicePojo> academicInstiList = new ArrayList<>();
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl2 + "states/Infrastructure/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonAirportArray = jsonObject.getJSONArray("airports");
                    JSONArray jsonHydropower = jsonObject.getJSONArray("hydropower");
                    JSONArray jsonIndustry = jsonObject.getJSONArray("industry");
                    JSONArray jsonAcademicInsti = jsonObject.getJSONArray("school");

                    allInfrastructureList.clear();
                    airportList.clear();
                    hydropowerList.clear();
                    industryList.clear();
                    academicInstiList.clear();
                    for (int i = 0; i < jsonAirportArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonAirportArray.getJSONObject(i);
                        Integer airportId = Integer.valueOf(jsonObject1.getString("id"));
                        String airportName = jsonObject1.getString("airportName");
                        String airportDistrict = jsonObject1.getString("district");
                        String airportPhone = jsonObject1.getString("phoneNo");
                        String airportLocalAddress = jsonObject1.getString("airportAddress");
                        String airportState = jsonObject1.getString("state");
                        String airportImage = jsonObject1.getString("airportImage");
                        String airportDescription = jsonObject1.getString("description");

                        listItem.setId(airportId);
                        listItem.setDistrict(airportDistrict);
                        listItem.setName(airportName);
                        listItem.setAddress(airportLocalAddress + ", " + airportDistrict + ", " + "Nepal");
                        listItem.setPhone(airportPhone);
                        listItem.setState(airportState);
                        listItem.setImage(airportImage);
                        listItem.setDescription(airportDescription);
                        listItem.setLocalLevel(getLocalLevel(jsonObject1));
                        airportList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }
                    for (int i = 0; i < jsonHydropower.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonHydropower.getJSONObject(i);
                        Integer hydroId = Integer.valueOf(jsonObject1.getString("id"));
                        String hydroName = jsonObject1.getString("hydropower");
                        String hydroDistrict = jsonObject1.getString("district");
                        String hydroCapacity = jsonObject1.getString("capacity");
                        String hydroAddress = jsonObject1.getString("address");
                        String hydroStatus = jsonObject1.getString("hydroStatus");
                        String hydroImage = jsonObject1.getString("hydropowerImage");
                        String hydroDescription = jsonObject1.getString("description");
                        String hydroPhoneNo = jsonObject1.getString("phoneNo");


                        listItem.setDistrict(hydroDistrict);
                        listItem.setId(hydroId);
                        listItem.setName(hydroName);
                        listItem.setImage(hydroImage);
                        listItem.setDescription(hydroDescription);
                        listItem.setAddress(hydroAddress + ", " + hydroDistrict + ", " + "Nepal");
                        listItem.setInfo(hydroCapacity + "\n" + hydroStatus);
                        listItem.setPhone(hydroPhoneNo);
                        listItem.setLocalLevel(getLocalLevel(jsonObject1));
                        hydropowerList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }
                    for (int i = 0; i < jsonIndustry.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonIndustry.getJSONObject(i);
                        Integer industryId = Integer.valueOf(jsonObject1.getString("id"));
                        String industryName = jsonObject1.getString("industry");
                        String industryDistrict = jsonObject1.getString("district");
                        String industryLocalAddress = jsonObject1.getString("address");
                        String industryImage = jsonObject1.getString("industryImage");
                        String industryState = jsonObject1.getString("state");
                        String industryDescription = jsonObject1.getString("description");
                        String industryPhoneNo = jsonObject1.getString("phoneNo");

                        listItem.setDistrict(industryDistrict);
                        listItem.setName(industryName);
                        listItem.setId(industryId);
                        listItem.setImage(industryImage);
                        listItem.setState(industryState);
                        listItem.setDescription(industryDescription);
                        listItem.setAddress(industryLocalAddress + ", " + industryDistrict + ", " + "Nepal");
                        listItem.setPhone(industryPhoneNo);
                        listItem.setLocalLevel(getLocalLevel(jsonObject1));
                        industryList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }

                    for (int i = 0; i < jsonAcademicInsti.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonAcademicInsti.getJSONObject(i);
                        Integer academicInstId = Integer.valueOf(jsonObject1.getString("id"));
                        String academicInstName = jsonObject1.getString("schoolName");
                        String academicInstDistrict = jsonObject1.getString("district");
                        String academicInstLocalAddress = jsonObject1.getString("address");
                        String academicInstImage = jsonObject1.getString("schoolImage");
                        String academicInstState = jsonObject1.getString("state");
                        String academicInstDescription = jsonObject1.getString("description");
                        String academicInstPhoneNo = jsonObject1.getString("contactNo");


                        listItem.setDistrict(academicInstDistrict);
                        listItem.setName(academicInstName);
                        listItem.setId(academicInstId);
                        listItem.setImage(academicInstImage);
                        listItem.setState(academicInstState);
                        listItem.setDescription(academicInstDescription);
                        listItem.setAddress(academicInstLocalAddress + ", " + academicInstDistrict + ", " + "Nepal");
                        listItem.setPhone(academicInstPhoneNo);
                        listItem.setLocalLevel(getLocalLevel(jsonObject1));
                        academicInstiList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }
                    if ("state".equals(selectedFilterType)) {
                        hydroCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, hydropowerList);
                        airportCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, airportList);
                        industryCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, industryList);
                        serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, allInfrastructureList);
                        academicInstiAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, academicInstiList);

                        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(serviceCustomAdapter);
                    } else if ("district".equals(selectedFilterType)) {
                        List<ServicePojo> filteredAllList = getFilteredServicesFromDistrict(allInfrastructureList);
                        List<ServicePojo> filteredAirportList = getFilteredServicesFromDistrict(airportList);
                        List<ServicePojo> filteredAcademicInstList = getFilteredServicesFromDistrict(academicInstiList);
                        List<ServicePojo> filteredIndustryList = getFilteredServicesFromDistrict(industryList);
                        List<ServicePojo> filteredHydropowerList = getFilteredServicesFromDistrict(hydropowerList);


                        serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        airportCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAirportList);
                        academicInstiAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAcademicInstList);
                        industryCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredIndustryList);
                        hydroCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredHydropowerList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);

                    } else if ("palika".equals(selectedFilterType)) {
                        List<ServicePojo> filteredAllList = getFilteredServicesFromPalika(allInfrastructureList);
                        List<ServicePojo> filteredAirportList = getFilteredServicesFromPalika(airportList);
                        List<ServicePojo> filteredAcademicInstList = getFilteredServicesFromPalika(academicInstiList);
                        List<ServicePojo> filteredIndustryList = getFilteredServicesFromPalika(industryList);
                        List<ServicePojo> filteredHydropowerList = getFilteredServicesFromPalika(hydropowerList);


                        serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        airportCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAirportList);
                        academicInstiAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAcademicInstList);
                        industryCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredIndustryList);
                        hydroCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredHydropowerList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);

                    } else if ("ward".equals(selectedFilterType)) {
                        List<ServicePojo> filteredAllList = getFilteredServicesFromWard(allInfrastructureList);
                        List<ServicePojo> filteredAirportList = getFilteredServicesFromWard(airportList);
                        List<ServicePojo> filteredAcademicInstList = getFilteredServicesFromWard(academicInstiList);
                        List<ServicePojo> filteredIndustryList = getFilteredServicesFromWard(industryList);
                        List<ServicePojo> filteredHydropowerList = getFilteredServicesFromWard(hydropowerList);


                        serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        airportCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAirportList);
                        academicInstiAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredAcademicInstList);
                        industryCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredIndustryList);
                        hydroCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredHydropowerList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);


                    }
                    infraAllBtn.performClick();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @NonNull
            private List<ServicePojo> getFilteredServicesFromDistrict(List<ServicePojo> unfilteredList) {
                List<ServicePojo> filteredList = new ArrayList<>();
                for (ServicePojo item : unfilteredList) {
                    if (item.getDistrict().equals(selectedFilter)) {
                        filteredList.add(item);

                    }
                }
                return filteredList;
            }

            @NonNull
            private List<ServicePojo> getFilteredServicesFromWard(List<ServicePojo> unfilteredList) {
                List<ServicePojo> filteredList = new ArrayList<>();
                for (ServicePojo item : unfilteredList) {

                    if (item.getLocalLevel().getWardNo().equals(selectedFilter)) {
                        filteredList.add(item);
                    }

                }
                return filteredList;
            }

            @NonNull
            private List<ServicePojo> getFilteredServicesFromPalika(List<ServicePojo> unfilteredList) {
                List<ServicePojo> filteredList = new ArrayList<>();
                for (ServicePojo item : unfilteredList) {
                    if (item.getLocalLevel().getMetropolitan() != null) {
                        if (item.getLocalLevel().getMetropolitan().equals(selectedFilter)) {
                            filteredList.add(item);
                        }
                    }
                    if (item.getLocalLevel().getSubMetropolitan() != null) {
                        if (item.getLocalLevel().getSubMetropolitan().equals(selectedFilter)) {
                            filteredList.add(item);
                        }
                    }
                    if (item.getLocalLevel().getMunicipality() != null) {
                        if (item.getLocalLevel().getMunicipality().equals(selectedFilter)) {
                            filteredList.add(item);
                        }
                    }
                    if (item.getLocalLevel().getRuralMunicipality() != null) {
                        if (item.getLocalLevel().getRuralMunicipality().equals(selectedFilter)) {
                            filteredList.add(item);
                        }
                    }

                }
                return filteredList;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resourceServiceLoading.setVisibility(View.GONE);
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching


    }

    @NonNull
    private LocalLevelResponsePojo getLocalLevel(JSONObject jsonObject1) throws JSONException {
        JSONObject localLevelObject = jsonObject1.getJSONObject("localLevel");
        Integer localLevelId = Integer.valueOf(localLevelObject.getString("id"));
        String localLevelRuralMunicipality = localLevelObject.getString("ruralMunicipality");
        String localLevelMunicipality = localLevelObject.getString("municipality");
        String localLevelMetropolitan = localLevelObject.getString("metropolitan");
        String localLevelSubMetropolitan = localLevelObject.getString("subMetropolitan");
        String localLevelWardNo = localLevelObject.getString("wardNo");

        return new LocalLevelResponsePojo(localLevelId, localLevelRuralMunicipality, localLevelMunicipality, localLevelMetropolitan, localLevelSubMetropolitan, localLevelWardNo);
    }

    public void getResources(String parameter, final String selectedFilter, final String selectedFilterType) {

        final List<ResourcePojo> allResourceList = new ArrayList<>();
        final List<ResourcePojo> mountainList = new ArrayList<>();
        final List<ResourcePojo> lakeList = new ArrayList<>();
        final List<ResourcePojo> waterFallList = new ArrayList<>();
        final List<ResourcePojo> protectedAreaList = new ArrayList<>();

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl2 + "states/naturalResources/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonMountainArray = jsonObject.getJSONArray("mountains");
                    JSONArray jsonLakeArray = jsonObject.getJSONArray("lakes");
                    JSONArray jsonWaterFallArray = jsonObject.getJSONArray("waterfalls");
                    JSONArray jsonProtectedAreaArray = jsonObject.getJSONArray("protectedAreases");

                    allResourceList.clear();
                    mountainList.clear();
                    lakeList.clear();
                    waterFallList.clear();
                    protectedAreaList.clear();

                    for (int i = 0; i < jsonMountainArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonMountainArray.getJSONObject(i);
                        String mountainName = jsonObject1.getString("mountain");
                        String mountainDistrict = jsonObject1.getString("district");
                        String mountainHeight = jsonObject1.getString("mountainHeight");
                        Integer mountainId = Integer.parseInt(jsonObject1.getString("id"));
                        String mountainImage = jsonObject1.getString("mountainImage");
                        String mountainDescription = jsonObject1.getString("description");
                        String mountainLocalAddress = jsonObject1.getString("address");


                        JSONArray localLevelArray = jsonObject1.getJSONArray("localLevel");
                        listItem.setId(mountainId);
                        listItem.setDescription(mountainDescription);
                        listItem.setImage(mountainImage);
                        listItem.setDistrict(mountainDistrict);
                        listItem.setName(mountainName);
                        listItem.setInfo(mountainHeight);
                        listItem.setAddress(mountainLocalAddress + ", " + mountainDistrict + ", " + "Nepal");
                        listItem.setLocalLevelResponsePojoList(getListOfLocalLevel(localLevelArray));
                        mountainList.add(listItem);
                        allResourceList.add(listItem);
                    }

                    for (int i = 0; i < jsonLakeArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonLakeArray.getJSONObject(i);
                        Integer lakeId = Integer.parseInt(jsonObject1.getString("id"));
                        String lakeName = jsonObject1.getString("lake");
                        String lakeDistrict = jsonObject1.getString("district");
                        String lakeDescription = jsonObject1.getString("description");
                        String lakeLocalAddress = jsonObject1.getString("address");
                        String lakeImage = jsonObject1.getString("lakeImage");

                        JSONArray localLevelArray = jsonObject1.getJSONArray("localLevel");

                        listItem.setDistrict(lakeDistrict);
                        listItem.setName(lakeName);
                        listItem.setDescription(lakeDescription);
                        listItem.setId(lakeId);
                        listItem.setImage(lakeImage);
                        listItem.setAddress(lakeLocalAddress + ", " + lakeDistrict + ", " + "Nepal");
                        listItem.setLocalLevelResponsePojoList(getListOfLocalLevel(localLevelArray));
                        lakeList.add(listItem);
                        allResourceList.add(listItem);
                    }
                    for (int i = 0; i < jsonWaterFallArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonWaterFallArray.getJSONObject(i);
                        Integer waterfallId = Integer.parseInt(jsonObject1.getString("id"));
                        String waterfallName = jsonObject1.getString("waterfall");
                        String waterfallDistrict = jsonObject1.getString("district");
                        String waterfallDescription = jsonObject1.getString("description");
                        String waterfallHeight = jsonObject1.getString("height");
                        String waterfallImage = jsonObject1.getString("waterfallImage");
                        String waterfallLocalAddress = jsonObject1.getString("address");

                        JSONArray localLevelArray = jsonObject1.getJSONArray("localLevel");

                        listItem.setId(waterfallId);
                        listItem.setImage(waterfallImage);
                        listItem.setDescription(waterfallDescription);
                        listItem.setInfo(waterfallHeight);
                        listItem.setDistrict(waterfallDistrict);
                        listItem.setName(waterfallName);
                        listItem.setAddress(waterfallLocalAddress + ", " + waterfallDistrict + ", " + "Nepal");
                        listItem.setLocalLevelResponsePojoList(getListOfLocalLevel(localLevelArray));

                        waterFallList.add(listItem);
                        allResourceList.add(listItem);
                    }
                    for (int i = 0; i < jsonProtectedAreaArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonProtectedAreaArray.getJSONObject(i);
                        Integer protectedAreaId = Integer.parseInt(jsonObject1.getString("id"));
                        String protectedAreaName = jsonObject1.getString("protectedAreas");
                        String protectedAreaDistrict = jsonObject1.getString("district");
                        String protectedAreasArea = jsonObject1.getString("area");
                        String protectedAreaImage = jsonObject1.getString("protectedAreasImage");
                        String protectedAreaDescription = jsonObject1.getString("description");
                        String protectedAreaLocalAddress = jsonObject1.getString("address");
                        JSONArray localLevelArray = jsonObject1.getJSONArray("localLevel");

                        listItem.setId(protectedAreaId);
                        listItem.setInfo(protectedAreasArea);
                        listItem.setDescription(protectedAreaDescription);
                        listItem.setImage(protectedAreaImage);
                        listItem.setDistrict(protectedAreaDistrict);
                        listItem.setName(protectedAreaName);
                        listItem.setAddress(protectedAreaLocalAddress + ", " + protectedAreaDistrict + ", " + "Nepal\n" + protectedAreasArea);
                        listItem.setLocalLevelResponsePojoList(getListOfLocalLevel(localLevelArray));
                        protectedAreaList.add(listItem);
                        allResourceList.add(listItem);
                    }

                    if ("state".equals(selectedFilterType)) {
                        allResourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, allResourceList);
                        mountainCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, mountainList);
                        lakeCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, lakeList);
                        waterFallCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, waterFallList);
                        protectedAreaCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, protectedAreaList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);
                    } else if ("district".equals(selectedFilterType)) {
                        List<ResourcePojo> filteredAllList = getFilteredResourceFromDistrict(allResourceList);
                        List<ResourcePojo> filteredMountainList = getFilteredResourceFromDistrict(mountainList);
                        List<ResourcePojo> filteredLakeList = getFilteredResourceFromDistrict(lakeList);
                        List<ResourcePojo> filteredWaterFallList = getFilteredResourceFromDistrict(waterFallList);
                        List<ResourcePojo> filteredProtectedAreaList = getFilteredResourceFromDistrict(protectedAreaList);


                        allResourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        mountainCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredMountainList);
                        lakeCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredLakeList);
                        waterFallCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredWaterFallList);
                        protectedAreaCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredProtectedAreaList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);

                    } else if ("palika".equals(selectedFilterType)) {
                        List<ResourcePojo> filteredAllList = getFilteredResourceFromPalika(allResourceList);
                        List<ResourcePojo> filteredMountainList = getFilteredResourceFromPalika(mountainList);
                        List<ResourcePojo> filteredLakeList = getFilteredResourceFromPalika(lakeList);
                        List<ResourcePojo> filteredWaterFallList = getFilteredResourceFromPalika(waterFallList);
                        List<ResourcePojo> filteredProtectedAreaList = getFilteredResourceFromPalika(protectedAreaList);


                        allResourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        mountainCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredMountainList);
                        lakeCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredLakeList);
                        waterFallCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredWaterFallList);
                        protectedAreaCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredProtectedAreaList);
                        allResourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);

                    } else if ("ward".equals(selectedFilterType)) {
                        List<ResourcePojo> filteredAllList = getFilteredResourceFromWard(allResourceList);
                        List<ResourcePojo> filteredMountainList = getFilteredResourceFromWard(mountainList);
                        List<ResourcePojo> filteredLakeList = getFilteredResourceFromWard(lakeList);
                        List<ResourcePojo> filteredWaterFallList = getFilteredResourceFromWard(waterFallList);
                        List<ResourcePojo> filteredProtectedAreaList = getFilteredResourceFromWard(protectedAreaList);


                        allResourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredAllList);
                        mountainCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredMountainList);
                        lakeCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredLakeList);
                        waterFallCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredWaterFallList);
                        protectedAreaCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredProtectedAreaList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(allResourceCustomAdapter);

                    }
                    nReaourceAllBtn.performClick();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @NonNull
            private List<ResourcePojo> getFilteredResourceFromDistrict(List<ResourcePojo> unfilteredList) {
                List<ResourcePojo> filteredList = new ArrayList<>();
                for (ResourcePojo item : unfilteredList) {
                    if (item.getDistrict().equals(selectedFilter)) {
                        filteredList.add(item);

                    }
                }
                return filteredList;
            }

            @NonNull
            private List<ResourcePojo> getFilteredResourceFromWard(List<ResourcePojo> unfilteredList) {
                List<ResourcePojo> filteredList = new ArrayList<>();
                for (ResourcePojo item : unfilteredList) {
                    for (int i = 0; i < item.getLocalLevelResponsePojoList().size(); i++) {
                        if (item.getLocalLevelResponsePojoList().get(i).getWardNo().equals(selectedFilter)) {
                            filteredList.add(item);
                        }

                    }
                }
                return filteredList;
            }

            @NonNull
            private List<ResourcePojo> getFilteredResourceFromPalika(List<ResourcePojo> unfilteredList) {
                List<ResourcePojo> filteredList = new ArrayList<>();
                for (ResourcePojo item : unfilteredList) {
                    for (int i = 0; i < item.getLocalLevelResponsePojoList().size(); i++) {
                        if (item.getLocalLevelResponsePojoList().get(i).getMetropolitan() != null) {
                            if (item.getLocalLevelResponsePojoList().get(i).getMetropolitan().equals(selectedFilter)) {
                                filteredList.add(item);
                            }
                        }
                        if (item.getLocalLevelResponsePojoList().get(i).getSubMetropolitan() != null) {
                            if (item.getLocalLevelResponsePojoList().get(i).getSubMetropolitan().equals(selectedFilter)) {
                                filteredList.add(item);
                            }
                        }
                        if (item.getLocalLevelResponsePojoList().get(i).getMunicipality() != null) {
                            if (item.getLocalLevelResponsePojoList().get(i).getMunicipality().equals(selectedFilter)) {
                                filteredList.add(item);
                            }
                        }
                        if (item.getLocalLevelResponsePojoList().get(i).getRuralMunicipality() != null) {
                            if (item.getLocalLevelResponsePojoList().get(i).getRuralMunicipality().equals(selectedFilter)) {
                                filteredList.add(item);
                            }
                        }
                    }
                }
                return filteredList;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resourceServiceLoading.setVisibility(View.GONE);
                Log.d("NaturalError", error.toString());
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        cacheRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        queue.add(cacheRequest);
        //End of Caching
    }

    private List<LocalLevelResponsePojo> getListOfLocalLevel(JSONArray localLevelArray) throws JSONException {
        final List<LocalLevelResponsePojo> localLevelList = new ArrayList<>();
        for (int j = 0; j < localLevelArray.length(); j++) {
            LocalLevelResponsePojo localLevelResponsePojo = new LocalLevelResponsePojo();
            JSONObject localLevelJsonObj = localLevelArray.getJSONObject(j);
            String localLevelId = localLevelJsonObj.getString("id");
            String localLevelRuralMunicipality = localLevelJsonObj.getString("ruralMunicipality");
            String localLevelMunicipality = localLevelJsonObj.getString("municipality");
            String localLevelMetropolitan = localLevelJsonObj.getString("metropolitan");
            String localLevelSubMetropolitan = localLevelJsonObj.getString("subMetropolitan");
            String localLevelWardNo = localLevelJsonObj.getString("wardNo");

            localLevelResponsePojo.setId(Integer.parseInt(localLevelId));
            localLevelResponsePojo.setRuralMunicipality(localLevelRuralMunicipality);
            localLevelResponsePojo.setSubMetropolitan(localLevelSubMetropolitan);
            localLevelResponsePojo.setMetropolitan(localLevelMetropolitan);
            localLevelResponsePojo.setMunicipality(localLevelMunicipality);
            localLevelResponsePojo.setWardNo(localLevelWardNo);

            localLevelList.add(localLevelResponsePojo);
        }
        return localLevelList;
    }

    public void getUrgentServices(String parameter, final String selectedFilter, final String selectedFilterType) {
        final List<ServicePojo> allUrgentServices = new ArrayList<>();
        final List<ServicePojo> atmList = new ArrayList<>();
        final List<ServicePojo> bloodBankList = new ArrayList<>();
        final List<ServicePojo> hospitalList = new ArrayList<>();
        final List<ServicePojo> policeStationList = new ArrayList<>();

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl + "states/UrgentService/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonBloodBankArray = jsonObject.getJSONArray("bloodbank");
                    JSONArray jsonHospitalArray = jsonObject.getJSONArray("hospital");
                    JSONArray jsonPoliceStationArray = jsonObject.getJSONArray("policeStation");
                    JSONArray jsonAtmArray = jsonObject.getJSONArray("atm");

                    allUrgentServices.clear();
                    bloodBankList.clear();
                    atmList.clear();
                    hospitalList.clear();
                    policeStationList.clear();
                    for (int i = 0; i < jsonBloodBankArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonBloodBankArray.getJSONObject(i);
                        String bloodBankName = jsonObject1.getString("bloodBankName");
                        String localAddress = jsonObject1.getString("localAddress");
                        String bloodBankDistrict = jsonObject1.getString("district");
                        String contactNo = jsonObject1.getString("contactNo");
                        listItem.setName(bloodBankName);
                        listItem.setAddress(localAddress + ", " + bloodBankDistrict + ", " + "Nepal");
                        listItem.setPhone(contactNo);
                        bloodBankList.add(listItem);
                        allUrgentServices.add(listItem);
                    }
                    for (int i = 0; i < jsonPoliceStationArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonPoliceStationArray.getJSONObject(i);
                        String policeStationName = jsonObject1.getString("policeStationName");
                        String policeLocalAddress = jsonObject1.getString("localAddress");
                        String policeContact = jsonObject1.getString("contactNo");
                        String policeDistrict = jsonObject1.getString("district");
                        listItem.setName(policeStationName);
                        listItem.setPhone(policeContact);
                        listItem.setDistrict(policeDistrict);
                        listItem.setAddress(policeLocalAddress + ", " + policeDistrict + ", " + "Nepal");
                        policeStationList.add(listItem);
                        allUrgentServices.add(listItem);
                    }
                    for (int i = 0; i < jsonAtmArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonAtmArray.getJSONObject(i);
                        String atmName = jsonObject1.getString("atmName");
                        String atmLocalAddress = jsonObject1.getString("localAddress");
                        String atmDistrict = jsonObject1.getString("district");
                        listItem.setName(atmName);
                        listItem.setAddress(atmLocalAddress + ", " + atmDistrict + ", " + "Nepal");
                        atmList.add(listItem);
                        allUrgentServices.add(listItem);
                    }
                    for (int i = 0; i < jsonHospitalArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonHospitalArray.getJSONObject(i);
                        String hospitalName = jsonObject1.getString("hospital");
                        String hospitalContactNumber = jsonObject1.getString("hospitalContactNumber");
                        String hospitalDistrict = jsonObject1.getString("district");
                        listItem.setDistrict(hospitalDistrict);
                        listItem.setName(hospitalName);
                        listItem.setPhone(hospitalContactNumber);
                        listItem.setAddress(hospitalDistrict + ", " + "Nepal");
                        hospitalList.add(listItem);
                        allUrgentServices.add(listItem);
                    }

                    if ("state".equals(selectedFilterType)) {
                        urgentAllCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, allUrgentServices);
                        urgentBloodBankCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, bloodBankList);
                        urgentAtmCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, atmList);
                        urgentHospitalCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, hospitalList);
                        urgentPoliceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, policeStationList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(urgentAllCustomAdapter);
                        urgentAllBtn.performClick();
                    } else if ("district".equals(selectedFilterType)) {
                        List<ServicePojo> filteredServices = new ArrayList<>();
                        for (ServicePojo item : allUrgentServices) {
                            if (item.getDistrict().equals(selectedFilter)) {
                                filteredServices.add(item);
                            }
                        }
                        urgentAllCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredServices);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(urgentAllCustomAdapter);
                        urgentAllBtn.performClick();
                    }


                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resourceServiceLoading.setVisibility(View.GONE);
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
