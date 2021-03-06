package com.softechfoundation.municipal.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Adapters.ResourceCustomAdapter;
import com.softechfoundation.municipal.Adapters.ServiceCustomAdapter;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Pojos.ListItem;
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

    private ResourceCustomAdapter resourceCustomAdapter,mountainCustomAdapter,
            riverCustomAdapter,lakeCustomAdapter,protectedAreaCustomAdapter,waterFallCustomAdapter;
    private ServiceCustomAdapter serviceCustomAdapter,hydroCustomAdapter,
            airportCustomAdapter,industryCustomAdapter,hotelCustomAdapter,academicInstiAdapter;
    private ServiceCustomAdapter urgentAllCustomAdapter,urgentHospitalCustomAdapter,
            urgentBloodBankCustomAdapter,urgentAtmCustomAdapter,urgentPoliceCustomAdapter;
    private ResourceCustomAdapter mainAttractionCustomAdapter;

    private View naturalResourceMenu,urgentServicesMenu,infraStructureMenu;

    private Button nResourceRiverBtn,nResourceMountainBtn,
            nResourceLakeBtn,nResourceWaterfallsBtn,nResourceProtectedAreaBtn,nReaourceAllBtn;
    private Button infraAirportBtn,infraHotelBtn,infraIndustryBtn,infraHydropowerBtn,infraAllBtn,infraAcademicInstiBtn;
    private Button urgentHospitalBtn,urgentBloodBankBtn,urgentAtmBtn,urgentPoliceBtn,urgentAllBtn;
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
        Intent intent= getIntent();
        String catagory=intent.getStringExtra("catagory");
        String state =intent.getStringExtra("state");
        String selectedFilter=intent.getStringExtra("selectedFilter");
        String selectedFilterType=intent.getStringExtra("selectedFilterType");
        if("URGENTSERVICES".equals(catagory)){
            urgentServicesMenu.setVisibility(View.VISIBLE);
            naturalResourceMenu.setVisibility(View.GONE);
            infraStructureMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Urgent Servicces");
            getUrgentServices(state,selectedFilter,selectedFilterType);
        }
        else if("RESOURCES".equals(catagory)){
            naturalResourceMenu.setVisibility(View.VISIBLE);
            urgentServicesMenu.setVisibility(View.GONE);
            infraStructureMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Natural Resources");
            getResources(state,selectedFilter,selectedFilterType);
        }
        else if ("INFRASTRUCTURES".equals(catagory)){

            infraStructureMenu.setVisibility(View.VISIBLE);
            naturalResourceMenu.setVisibility(View.GONE);
            urgentServicesMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Infrastructures");
            getInfrastructure(state,selectedFilter,selectedFilterType);
        }
        else if("MAINATTRACTIONS".equals(catagory)){
            infraStructureMenu.setVisibility(View.GONE);
            naturalResourceMenu.setVisibility(View.GONE);
            urgentServicesMenu.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Main Attractions");
            getMainAttraction(state,selectedFilter,selectedFilterType);

        }
        else if("AVALIABLECONTACTS".equals(catagory)){
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
                Toast.makeText(ListOfServicesAndResources.this, "Comming Soon :)", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ListOfServicesAndResources.this, "Comming Soon :) ", Toast.LENGTH_SHORT).show();
                resourceRecyclerView.setAdapter(academicInstiAdapter);
            }
        });

        urgentAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllUrgentButtonTransparent();
                urgentAllBtn.setBackground(getResources().getDrawable(R.drawable.path_btn_clicked_style));
                urgentAllBtn.setTextColor(Color.WHITE);
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
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(resourceCustomAdapter);
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
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                resourceRecyclerView.setAdapter(riverCustomAdapter);
            }
        });


    }

    private void getMainAttraction(String parameter, final String selectedFilter, final String selectedFilterType) {
        final List<ResourcePojo> mainAttractionList=new ArrayList<>();
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl+"famousFor/getAttraction/",
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
                        String attractionDistrict=jsonObject1.getString("district");
                        String  attractionDescription=jsonObject1.getString("description");
                        //String addresString=jsonObject1.getString("addresString");
                        listItem.setName(attractionName);
                        listItem.setAddress(attractionDistrict+", "+"Nepal");
                        mainAttractionList.add(listItem);
                    }

                    if("state".equals(selectedFilterType)) {
                        mainAttractionCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this,mainAttractionList);
                        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(mainAttractionCustomAdapter);
                    }
                    else if("district".equals(selectedFilterType)){
                        List<ResourcePojo> filteredList=new ArrayList<>();
                        for(ResourcePojo item:mainAttractionList){
                            if(item.getDistrict().equals(selectedFilter)){
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

        infraAllBtn.setTextColor(getResources().getColor(R.color.black));
        infraHotelBtn.setTextColor(getResources().getColor(R.color.black));
        infraAirportBtn.setTextColor(getResources().getColor(R.color.black));
        infraIndustryBtn.setTextColor(getResources().getColor(R.color.black));
        infraHydropowerBtn.setTextColor(getResources().getColor(R.color.black));
    }

    private void defineView() {
        resourceRecyclerView=findViewById(R.id.state_detail_list);
        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        naturalResourceMenu=findViewById(R.id.natural_resources_catagory);
        urgentServicesMenu=findViewById(R.id.urgent_services_catagory);
        infraStructureMenu=findViewById(R.id.infrastructure_catagory);

        nResourceRiverBtn=findViewById(R.id.natural_resources_btn_rivers);
        nResourceLakeBtn=findViewById(R.id.natural_resources_btn_lakes);
        nResourceMountainBtn=findViewById(R.id.natural_resources_btn_mountains);
        nResourceWaterfallsBtn=findViewById(R.id.natural_resources_btn_water_fall);
        nResourceProtectedAreaBtn=findViewById(R.id.natural_resources_btn_protected_areas);
        nReaourceAllBtn=findViewById(R.id.natural_resources_btn_all);

        infraAirportBtn=findViewById(R.id.services_btn_airport);
        infraHotelBtn=findViewById(R.id.services_btn_hotel);
        infraIndustryBtn=findViewById(R.id.services_btn_industry);
        infraHydropowerBtn=findViewById(R.id.services_btn_hydropower);
        infraAllBtn=findViewById(R.id.services_btn_all);
        infraAcademicInstiBtn=findViewById(R.id.services_btn_academic_insti);

        urgentHospitalBtn=findViewById(R.id.urgent_services_btn_hospital);
        urgentAtmBtn=findViewById(R.id.urgent_services_btn_atm);
        urgentBloodBankBtn=findViewById(R.id.urgent_services_btn_blood_bank);
        urgentPoliceBtn=findViewById(R.id.urgent_services_btn_police);
        urgentAllBtn=findViewById(R.id.urgent_services_btn_all);

        horizontalScrollViewMenu=findViewById(R.id.horizontal_scroll_catagories);

        resourceServiceLoading=findViewById(R.id.dotted_res_ser_loading);
        
    }

    private void getInfrastructure(String parameter, final String selectedFilter, final String selectedFilterType) {
        final List<ServicePojo> allInfrastructureList=new ArrayList<>();
        final List<ServicePojo>airportList=new ArrayList<>();
        final List<ServicePojo>hydropowerList=new ArrayList<>();
        final List<ServicePojo>industryList=new ArrayList<>();
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl+"states/Infrastructure/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonAirportArray = jsonObject.getJSONArray("airports");
                    JSONArray jsonHydropower=jsonObject.getJSONArray("hydropower");
                    JSONArray jsonIndustry=jsonObject.getJSONArray("industry");
                    allInfrastructureList.clear();
                    airportList.clear();
                    hydropowerList.clear();
                    industryList.clear();
                    for (int i = 0; i < jsonAirportArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonAirportArray.getJSONObject(i);
                        String airportName = jsonObject1.getString("airportName");
                        String airportDistrict=jsonObject1.getString("disrict");
 //                       String  airportPhone=jsonObject1.getString("hospitalContactNumber");
                        String airportAddress=jsonObject1.getString("airportAddress");
                        listItem.setDistrict(airportDistrict);
                        listItem.setName(airportName);
                        listItem.setAddress(airportAddress+", "+airportDistrict+", "+"Nepal");
                        listItem.setPhone("");
                        airportList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }
                    for (int i = 0; i < jsonHydropower.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonHydropower.getJSONObject(i);
                        String hydroName = jsonObject1.getString("hydropower");
                        String hydroDistrict=jsonObject1.getString("district");
                        String  hydroCapacity=jsonObject1.getString("capacity");
                        String hydroAddress=jsonObject1.getString("address");
                        String hydroStatus=jsonObject1.getString("hydroStatus");
                        listItem.setDistrict(hydroDistrict);
                        listItem.setName(hydroName);
                        listItem.setAddress(hydroAddress+", "+hydroDistrict+", "+"Nepal");
                        listItem.setInfo(hydroCapacity+"\n"+hydroStatus);
                        listItem.setPhone("");
                        hydropowerList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }
                    for (int i = 0; i < jsonIndustry.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonIndustry.getJSONObject(i);
                        String industryName = jsonObject1.getString("industry");
                        String industryDistrict=jsonObject1.getString("district");
                        String industryAddress=jsonObject1.getString("address");
                        listItem.setDistrict(industryDistrict);
                        listItem.setName(industryName);
                        listItem.setAddress(industryAddress+", "+industryDistrict+", "+"Nepal");
                        listItem.setPhone("");
                        hydropowerList.add(listItem);
                        allInfrastructureList.add(listItem);
                    }
                    if("state".equals(selectedFilterType)) {
                        hydroCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,hydropowerList);
                        airportCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,airportList);
                        industryCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,industryList);
                        serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,allInfrastructureList);
                        resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(serviceCustomAdapter);
                    }
                    else if("district".equals(selectedFilterType)){
                        List<ServicePojo> filteredList=new ArrayList<>();
                        for(ServicePojo item:allInfrastructureList){
                            if(item.getDistrict().equals(selectedFilter)){
                                filteredList.add(item);
                            }
                        }
                        serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this, filteredList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(serviceCustomAdapter);
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

    public void getResources(String parameter, final String selectedFilter, final String selectedFilterType) {

        final List<ResourcePojo> allResourceList=new ArrayList<>();
        final List<ResourcePojo>mountainList=new ArrayList<>();
        final List<ResourcePojo>riverList=new ArrayList<>();
        final List<ResourcePojo>lakeList=new ArrayList<>();
        final List<ResourcePojo>waterFallList=new ArrayList<>();
        final List<ResourcePojo>protectedAreaList=new ArrayList<>();

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl+"states/naturalResources/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonMountainArray = jsonObject.getJSONArray("mountains");
                    JSONArray jsonRiverArray=jsonObject.getJSONArray("rivers");
                    JSONArray jsonLakeArray=jsonObject.getJSONArray("lakes");
                    JSONArray jsonWaterFallArray=jsonObject.getJSONArray("waterfalls");
                    JSONArray jsonProtectedAreaArray=jsonObject.getJSONArray("protectedAreases");

                    allResourceList.clear();
                    mountainList.clear();
                    riverList.clear();
                    lakeList.clear();
                    waterFallList.clear();
                    protectedAreaList.clear();
                    for (int i = 0; i < jsonMountainArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonMountainArray.getJSONObject(i);
                        String mountainName = jsonObject1.getString("mountain");
                        String mountainDistrict=jsonObject1.getString("district");
                        String mountainHeight=jsonObject1.getString("mountainHeight");
                        listItem.setDistrict(mountainDistrict);
                        listItem.setName(mountainName);
                        listItem.setInfo(mountainHeight );
                        listItem.setAddress(mountainDistrict+", "+"Nepal");
                        mountainList.add(listItem);
                        allResourceList.add(listItem);
                    }
                    for (int i = 0; i < jsonRiverArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonRiverArray.getJSONObject(i);
                        String riverName = jsonObject1.getString("river");
                        String riverDistrict=jsonObject1.getString("district");
                        listItem.setDistrict(riverDistrict);
                        listItem.setName(riverName);
                        listItem.setAddress(riverDistrict+", "+"Nepal");
                        riverList.add(listItem);
                        allResourceList.add(listItem);
                    }
                    for (int i = 0; i < jsonLakeArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonLakeArray.getJSONObject(i);
                        String lakeName = jsonObject1.getString("lake");
                        String lakeDistrict=jsonObject1.getString("district");
//                        String lakeAddress=jsonObject1.getString("address");
                        listItem.setDistrict(lakeDistrict);
                        listItem.setName(lakeName);
                        listItem.setAddress(lakeDistrict+", "+"Nepal");
                        lakeList.add(listItem);
                        allResourceList.add(listItem);
                    }
                    for (int i = 0; i < jsonWaterFallArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonWaterFallArray.getJSONObject(i);
                        String waterfallName = jsonObject1.getString("waterfall");
                        String waterfallDistrict=jsonObject1.getString("district");
                        listItem.setDistrict(waterfallDistrict);
                        listItem.setName(waterfallName);
                        listItem.setAddress(waterfallDistrict+", "+"Nepal");
                        waterFallList.add(listItem);
                        allResourceList.add(listItem);
                    }
                    for (int i = 0; i < jsonProtectedAreaArray.length(); i++) {
                        ResourcePojo listItem = new ResourcePojo();
                        JSONObject jsonObject1 = jsonProtectedAreaArray.getJSONObject(i);
                        String protectedAreaName = jsonObject1.getString("protectedAreas");
                        String protectedAreaDistrict=jsonObject1.getString("district");
                        String protectedAreasArea=jsonObject1.getString("area");
                        listItem.setDistrict(protectedAreaDistrict);
                        listItem.setName(protectedAreaName);
                        listItem.setAddress(protectedAreaDistrict+", "+"Nepal\n"+protectedAreasArea);
                        protectedAreaList.add(listItem);
                        allResourceList.add(listItem);
                    }

                    if("state".equals(selectedFilterType)) {
                        resourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, allResourceList);
                        mountainCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, mountainList);
                        riverCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, riverList);
                        lakeCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, lakeList);
                        waterFallCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, waterFallList);
                        protectedAreaCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, protectedAreaList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(resourceCustomAdapter);
                    }
                    else if("district".equals(selectedFilterType)){
                        List<ResourcePojo> filteredList=new ArrayList<>();
                        for(ResourcePojo item:allResourceList){
                            if(item.getDistrict().equals(selectedFilter)){
                                filteredList.add(item);

                            }
                        }
                        resourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this, filteredList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(resourceCustomAdapter);
                    }
                    nReaourceAllBtn.performClick();
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
    public void getUrgentServices(String parameter, final String selectedFilter, final String selectedFilterType) {
        final List<ServicePojo> allUrgentServices=new ArrayList<>();
        final List<ServicePojo>atmList=new ArrayList<>();
        final List<ServicePojo>bloodBankList=new ArrayList<>();
        final List<ServicePojo>hospitalList=new ArrayList<>();
        final List<ServicePojo>policeStationList=new ArrayList<>();

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl+"states/UrgentService/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonBloodBankArray = jsonObject.getJSONArray("bloodbank");
                    JSONArray jsonHospitalArray=jsonObject.getJSONArray("hospital");
                    JSONArray jsonPoliceStationArray=jsonObject.getJSONArray("policeStation");
                    JSONArray jsonAtmArray=jsonObject.getJSONArray("atm");

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
                        String bloodBankDistrict=jsonObject1.getString("district");
                        String contactNo=jsonObject1.getString("contactNo");
                        listItem.setName(bloodBankName);
                        listItem.setAddress(localAddress+", "+bloodBankDistrict+", "+"Nepal");
                        listItem.setPhone(contactNo);
                        bloodBankList.add(listItem);
                        allUrgentServices.add(listItem);
                    }
                    for (int i = 0; i < jsonPoliceStationArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonPoliceStationArray.getJSONObject(i);
                        String policeStationName = jsonObject1.getString("policeStationName");
                        String policeLocalAddress=jsonObject1.getString("localAddress");
                        String policeContact=jsonObject1.getString("contactNo");
                        String policeDistrict=jsonObject1.getString("district");
                        listItem.setName(policeStationName);
                        listItem.setPhone(policeContact);
                        listItem.setDistrict(policeDistrict);
                        listItem.setAddress(policeLocalAddress+", "+policeDistrict+", "+"Nepal");
                        policeStationList.add(listItem);
                        allUrgentServices.add(listItem);
                    }
                    for (int i = 0; i < jsonAtmArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonAtmArray.getJSONObject(i);
                        String atmName = jsonObject1.getString("atmName");
                        String atmLocalAddress=jsonObject1.getString("localAddress");
                        String atmDistrict=jsonObject1.getString("district");
                        listItem.setName(atmName);
                        listItem.setAddress(atmLocalAddress+", "+atmDistrict+", "+"Nepal");
                        atmList.add(listItem);
                        allUrgentServices.add(listItem);
                    }
                    for (int i = 0; i < jsonHospitalArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonHospitalArray.getJSONObject(i);
                        String hospitalName = jsonObject1.getString("hospital");
                        String hospitalContactNumber=jsonObject1.getString("hospitalContactNumber");
                        String hospitalDistrict=jsonObject1.getString("district");
                        listItem.setDistrict(hospitalDistrict);
                        listItem.setName(hospitalName);
                        listItem.setPhone(hospitalContactNumber);
                        listItem.setAddress(hospitalDistrict+", "+"Nepal");
                        hospitalList.add(listItem);
                        allUrgentServices.add(listItem);
                    }

                    if("state".equals(selectedFilterType)){
                        urgentAllCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,allUrgentServices);
                        urgentBloodBankCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,bloodBankList);
                        urgentAtmCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,atmList);
                        urgentHospitalCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,hospitalList);
                        urgentPoliceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,policeStationList);
                        resourceServiceLoading.setVisibility(View.GONE);
                        resourceRecyclerView.setAdapter(urgentAllCustomAdapter);
                        urgentAllBtn.performClick();
                    }
                    else if("district".equals(selectedFilterType)){
                        List<ServicePojo>filteredServices=new ArrayList<>();
                        for(ServicePojo item: allUrgentServices){
                            if(item.getDistrict().equals(selectedFilter)){
                                filteredServices.add(item);
                            }
                        }
                        urgentAllCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,filteredServices);
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

        Log.d("Final Url: ",encodedUrl.toString());
        return encodedUrl;


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
