package com.softechfoundation.municipal.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Adapters.FilterCustomAdapter;
import com.softechfoundation.municipal.CheckInternet.CheckInternet;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Fragments.MainFragment;
import com.softechfoundation.municipal.GloballyCommon;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.RecyclerViewOnItemClickListener;
import com.softechfoundation.municipal.SpeedyLinearLayoutManager;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static com.android.volley.Request.Method.GET;

public class StateDetails extends AppCompatActivity {
    private LinearLayout naturalResources, infrastructure, mainAttraction, urgentServices, avaliableContacts;
    private TextView stateName, capitalName, area, population, density, governer, chiefMinister, website, chiefMinisterLabel, capitalCityLabel;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView filterRecyclerview;
    private FilterCustomAdapter adapter;
    private TextView filterRviewTitle, filterBackButton;
    private ImageView filterOnOff;
    private View filterLoading;
    private boolean isFilterExpand = false;
    private String whereToJumpOnFilterBackBtn = "state";
    private static final String MY_PREFS = "districtOrPalika";
    private String gorvernerName, websiteName, state, capitalCity, areaValue, populationValue, densityValue, chiefMinisterName;
    private String selectedFilter, selectedFilterType;
    private CoordinatorLayout coordinatorLayout;

    public static String globalState, globalDistrict, globalLocalLevel;
    private FloatingActionButton resourceFilterFab;
    private StringBuilder stringBuilder = new StringBuilder();
    private FilterCustomAdapter adapterDistrict, adapterVdc, adapterMetroplitan, adapterAll, adapterOldVdc;
    private FilterCustomAdapter adapterSubMetropolitan, adapterMunicipal, adapterRuralMunicipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        collapsingToolbarLayout = findViewById(R.id.state_detail_collapsing_tool_bar_layout);
        stateName = findViewById(R.id.detail_top_state_name);
        capitalCityLabel = findViewById(R.id.detail_capital_city_label);
        chiefMinisterLabel = findViewById(R.id.detail_top_chiefMinister_label);
        capitalName = findViewById(R.id.detail_top_capital_name);
        area = findViewById(R.id.area);
        population = findViewById(R.id.population);
        density = findViewById(R.id.density);
        chiefMinister = findViewById(R.id.detail_top_state_chiefMinister);

        avaliableContacts = findViewById(R.id.available_contacts_layout);
        naturalResources = findViewById(R.id.natural_resource_layout);
        infrastructure = findViewById(R.id.infrastructure_layout);
        mainAttraction = findViewById(R.id.main_attraction_layout);
        urgentServices = findViewById(R.id.urgent_services_layout);

        filterRecyclerview = findViewById(R.id.filter_recycleriew);
        filterRecyclerview.setLayoutManager(new SpeedyLinearLayoutManager(this, SpeedyLinearLayoutManager.HORIZONTAL, false));
        filterRviewTitle = findViewById(R.id.filter_recyclerview_title);
        filterBackButton = findViewById(R.id.filter_back);
        filterLoading = findViewById(R.id.dotted_filter_loading);
        filterOnOff = findViewById(R.id.filter_on_off);
        resourceFilterFab = findViewById(R.id.resource_filter_fab);
        coordinatorLayout = findViewById(R.id.state_detail_coordinator_layout);

        Intent intentGetValue = getIntent();
        gorvernerName = intentGetValue.getStringExtra("governer");
        chiefMinisterName = intentGetValue.getStringExtra("chiefMinister");
        websiteName = intentGetValue.getStringExtra("website");
        state = intentGetValue.getStringExtra("stateName");
        capitalCity = intentGetValue.getStringExtra("capital");
        areaValue = intentGetValue.getStringExtra("area");
        populationValue = intentGetValue.getStringExtra("population");
        densityValue = intentGetValue.getStringExtra("density");
        selectedFilter = state;
        selectedFilterType = "state";
        showStateDetail();
        expandOrHideFilter();
//        getSupportActionBar().setTitle("Detail about"+" "+state);


        naturalResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfServicesAndResources.class);
                intent.putExtra("catagory", "RESOURCES");
                intent.putExtra("state", state);
                intent.putExtra("selectedFilter", selectedFilter);
                intent.putExtra("selectedFilterType", selectedFilterType);
                startActivity(intent);

            }
        });

        infrastructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfServicesAndResources.class);
                intent.putExtra("catagory", "INFRASTRUCTURES");
                intent.putExtra("state", state);
                intent.putExtra("selectedFilter", selectedFilter);
                intent.putExtra("selectedFilterType", selectedFilterType);
                startActivity(intent);
            }
        });

        urgentServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfServicesAndResources.class);
                intent.putExtra("catagory", "URGENTSERVICES");
                intent.putExtra("state", state);
                intent.putExtra("selectedFilter", selectedFilter);
                intent.putExtra("selectedFilterType", selectedFilterType);
                startActivity(intent);
            }
        });

        mainAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfServicesAndResources.class);
                intent.putExtra("catagory", "MAINATTRACTIONS");
                intent.putExtra("state", state);
                intent.putExtra("selectedFilter", selectedFilter);
                intent.putExtra("selectedFilterType", selectedFilterType);
                startActivity(intent);
            }
        });

        avaliableContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListOfServicesAndResources.class);
                intent.putExtra("catagory", "AVALIABLECONTACTS");
                intent.putExtra("state", state);
                intent.putExtra("selectedFilter", selectedFilter);
                intent.putExtra("selectedFilterType", selectedFilterType);
                startActivity(intent);
            }
        });

        filterBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("district".equals(whereToJumpOnFilterBackBtn)) {
                    filterRviewTitle.setText("Filter information by Districts");
                    getDistricts(state);
                    selectedFilterType = "state";
                    selectedFilter = state;
                    showStateDetail();
                    filterBackButton.setVisibility(View.GONE);
                } else if ("palika".equals(whereToJumpOnFilterBackBtn)) {
                    filterRviewTitle.setText("Filter information by LocalLevel");
                    whereToJumpOnFilterBackBtn = "district";
                    populateLocalLevelRecyclerView();
                    selectedFilterType = "district";
                    selectedFilter = globalDistrict;
                    showDistrictDetail(globalDistrict);
                    filterBackButton.setVisibility(View.VISIBLE);
                }

            }
        });

        filterOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrHideFilter();
            }
        });
        filterRviewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrHideFilter();
            }
        });
        resourceFilterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StateDetails.this, "This can be used to filter the Services and Resources", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void expandOrHideFilter() {
        if (!isFilterExpand) {
            filterOnOff.setImageResource(R.drawable.layer_ic_keyboard_arrow_up_black_24dp);
            isFilterExpand = true;
            filterRecyclerview.setVisibility(View.VISIBLE);
            getDistricts(state);
            filterRviewTitle.setText("Filter information by Districts");
            return;
        }
        if (isFilterExpand) {
            filterOnOff.setImageResource(R.drawable.layer_ic_keyboard_arrow_down_black_24dp);
            isFilterExpand = false;
            filterRecyclerview.setVisibility(View.GONE);
            filterRviewTitle.setText("Filter information");
            showStateDetail();
            selectedFilterType = "state";
            selectedFilter = state;
        }
    }

    private void showStateDetail() {
        chiefMinister.setVisibility(View.VISIBLE);
        chiefMinisterLabel.setVisibility(View.VISIBLE);
        stateName.setText(state);
        area.setText(areaValue);
        capitalName.setText(capitalCity);
        population.setText(populationValue);
        density.setText(densityValue);
        chiefMinister.setText(chiefMinisterName);
        capitalCityLabel.setText("(Capital");
        chiefMinisterLabel.setText("(Chief Minister)");
        collapsingToolbarLayout.setTitle("Detail about" + " " + state);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

    }

    private void getDistricts(String state) {
        whereToJumpOnFilterBackBtn = "state";
        //Start Caching
        filterLoading.setVisibility(View.VISIBLE);
        final List<ListItem> districtList = new ArrayList<>(Collections.<ListItem>emptyList());
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl(CommonUrl.BaseUrl + "districts/state/",
                state);

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

                    adapter = new FilterCustomAdapter(getApplicationContext(), districtList, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            filterBackButton.setVisibility(View.VISIBLE);
                            filterLoading.setVisibility(View.VISIBLE);

                            filterRviewTitle.setText("Filter info by Local Levels");
                            globalDistrict = districtList.get(position).getName();
                            selectedFilter = globalDistrict;
                            selectedFilterType = districtList.get(position).getType();
//                            Toast.makeText(StateDetails.this, "District " + districtList.get(position).getName() + " is successfully selected and\nServices and Resources are filtered accordingly", Toast.LENGTH_LONG).show();

                            showDistrictDetail(globalDistrict);
                            populateLocalLevelRecyclerView();

                        }
                    });
                    filterLoading.setVisibility(View.GONE);
                    filterRecyclerview.setAdapter(adapter);
                    filterRecyclerview.post(new Runnable() {
                        @Override
                        public void run() {
                            filterRecyclerview.smoothScrollToPosition(adapter.getItemCount() - 3);
                        }
                    });


                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                filterLoading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_SHORT).show();

                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private void getWards(String palika) {
        whereToJumpOnFilterBackBtn = "palika";
        final List<ListItem> wardList = new ArrayList<>();
        //Start Caching
        filterLoading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl2 + "locallevels/",
                palika);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("wardNo");
                    wardList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ListItem listItem = new ListItem();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String wardName = jsonObject1.getString("wardNo");
                        listItem.setName(wardName);
                        listItem.setIcon(R.drawable.district);
                        listItem.setType("ward");
                        wardList.add(listItem);
                    }

                    adapter = new FilterCustomAdapter(getApplicationContext(), wardList,
                            new RecyclerViewOnItemClickListener() {
                                @Override
                                public void onItemClickListener(int position, View view) {
                                    ListItem item = wardList.get(position);
                                    selectedFilter = item.getName();
                                    selectedFilterType = item.getType();
//                                    showWardDetail(item.getName());
                                    Toast.makeText(StateDetails.this, "Ward " + item.getName() + " is successfully selected and\nServices and Resources are filtered", Toast.LENGTH_LONG).show();
//                                    showWardDetail(item.getName());

                                }
                            });
                    filterLoading.setVisibility(View.GONE);
                    filterRecyclerview.setAdapter(adapter);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                filterLoading.setVisibility(View.GONE);
                GloballyCommon.checkErrorResponse(coordinatorLayout,getApplicationContext());
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showWardDetail(final String name) {
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl2 + "locallevels/",
                name);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);

                    String chairMan = jsonObject.getString("chairmen");
                    String chairManContact = jsonObject.getString("chairmenContact");
                    String chairManEmail = jsonObject.getString("chairmenEmail");
                    String viceChairMan = jsonObject.getString("viceChairmen");
                    String viceChairManContact = jsonObject.getString("viceChairmenContact");
                    String viceChairManEmail = jsonObject.getString("viceChairmenEmail");
                    String palikaArea = jsonObject.getString("area");
                    String palikaPopulation = jsonObject.getString("population");
                    String website = jsonObject.getString("website");
                    String palikaDensity = jsonObject.getString("density");
                    String localLevelType = jsonObject.getString("localLevelType");


                    stateName.setText(name);
                    chiefMinisterLabel.setVisibility(View.VISIBLE);
                    chiefMinister.setVisibility(View.VISIBLE);
                    if ("RURAL".equals(localLevelType)) {
                        capitalCityLabel.setText("(Chairman)");
                        chiefMinisterLabel.setText(" (Vice-Chairman)");
                    } else {
                        capitalCityLabel.setText("(Mayor)");
                        chiefMinisterLabel.setText(" (Deputy-Mayor)");
                    }
                    capitalName.setText(chairMan);
                    chiefMinister.setText(viceChairMan);
                    area.setText(palikaArea);
                    population.setText(palikaPopulation);
                    density.setText(palikaDensity);
                    collapsingToolbarLayout.setTitle("Detail about" + " " + name);
                    collapsingToolbarLayout.setExpandedTitleColor(getApplicationContext().getResources().getColor(R.color.white));
                    filterLoading.setVisibility(View.GONE);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GloballyCommon.checkErrorResponse(coordinatorLayout,getApplicationContext());
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private void showDistrictDetail(final String globalDistrict) {
        //Start Caching
        final List<ListItem> districtList = new ArrayList<>(Collections.<ListItem>emptyList());
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl + "districts/district/",
                globalDistrict);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    districtList.clear();

                    String districtArea = jsonObject.getString("area");
                    String districtPopulation = jsonObject.getString("population");
                    String districtHeadquater = jsonObject.getString("headquater");
                    String districtState = jsonObject.getString("state");
                    String districtPicture = jsonObject.getString("districtPicture");

                    stateName.setText(globalDistrict);
                    capitalName.setText(districtHeadquater);
                    capitalCityLabel.setText("(Headquarter)");
                    chiefMinisterLabel.setVisibility(View.INVISIBLE);
                    chiefMinister.setVisibility(View.INVISIBLE);
                    area.setText(districtArea);
                    population.setText(districtPopulation);
                    collapsingToolbarLayout.setTitle("Detail about" + " " + globalDistrict);
                    collapsingToolbarLayout.setExpandedTitleColor(getApplicationContext().getResources().getColor(R.color.white));


                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GloballyCommon.checkErrorResponse(coordinatorLayout,getApplicationContext());
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private void showLocalLevelDetail(final String name) {
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = makeFinalUrl(CommonUrl.BaseUrl2 + "locallevels/",
                name);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);

                    String chairMan = jsonObject.getString("chairmen");
                    String chairManContact = jsonObject.getString("chairmenContact");
                    String chairManEmail = jsonObject.getString("chairmenEmail");
                    String viceChairMan = jsonObject.getString("viceChairmen");
                    String viceChairManContact = jsonObject.getString("viceChairmenContact");
                    String viceChairManEmail = jsonObject.getString("viceChairmenEmail");
                    String palikaArea = jsonObject.getString("area");
                    String palikaPopulation = jsonObject.getString("population");
                    String website = jsonObject.getString("website");
                    String palikaDensity = jsonObject.getString("density");
                    String localLevelType = jsonObject.getString("localLevelType");


                    stateName.setText(name);
                    chiefMinisterLabel.setVisibility(View.VISIBLE);
                    chiefMinister.setVisibility(View.VISIBLE);
                    if ("RURAL".equals(localLevelType)) {
                        capitalCityLabel.setText("(Chairman)");
                        chiefMinisterLabel.setText(" (Vice-Chairman)");
                    } else {
                        capitalCityLabel.setText("(Mayor)");
                        chiefMinisterLabel.setText(" (Deputy-Mayor)");
                    }
                    capitalName.setText(chairMan);
                    chiefMinister.setText(viceChairMan);
                    area.setText(palikaArea);
                    population.setText(palikaPopulation);
                    density.setText(palikaDensity);
                    collapsingToolbarLayout.setTitle("Detail about" + " " + name);
                    collapsingToolbarLayout.setExpandedTitleColor(getApplicationContext().getResources().getColor(R.color.white));
                    filterLoading.setVisibility(View.GONE);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GloballyCommon.checkErrorResponse(coordinatorLayout,getApplicationContext());
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private void populateLocalLevelRecyclerView() {
        whereToJumpOnFilterBackBtn = "district";
        final String[] allNames, ruralMunicipalNames, municipalNames, metropolitanNames, subMetropolitanNames;
        // final List<ListItem> vdcList=new ArrayList<>();
        final List<ListItem> allList = new ArrayList<>();
        final List<ListItem> metropolitanList = new ArrayList<>();
        final List<ListItem> subMetropolitanList = new ArrayList<>();
        final List<ListItem> municipalList = new ArrayList<>();
        final List<ListItem> ruralMunicipalList = new ArrayList<>();

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                    // adapterRuralMunicipal = new FilterCustomAdapter(getContext(), ruralMunicipalList, recyclerView);
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
                    //adapterMunicipal = new FilterCustomAdapter(getContext(), municipalList, recyclerView);
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
                    //adapterMetroplitan = new FilterCustomAdapter(getContext(), metropolitanList, recyclerView);
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
                    //  adapterSubMetropolitan = new FilterCustomAdapter(getContext(), subMetropolitanList, recyclerView);

                    adapterAll = new FilterCustomAdapter(getApplicationContext(), allList, new RecyclerViewOnItemClickListener() {
                        @Override
                        public void onItemClickListener(int position, View view) {
                            ListItem currentItem = allList.get(position);
                            showLocalLevelDetail(currentItem.getName());
                            selectedFilter = currentItem.getName();
                            selectedFilterType = "palika";
                            globalLocalLevel = currentItem.getName();
                            GloballyCommon.getInstance().setSelectedPalikaForWardFilter(currentItem.getName());
                            filterRviewTitle.setText("Filter information from ward");
//                            Toast.makeText(StateDetails.this, currentItem.getName() + " is successfully selected and\nServices and Resources are filtered accordingly", Toast.LENGTH_LONG).show();

                            getWards(currentItem.getName());
                        }
                    });
                    filterLoading.setVisibility(View.GONE);
                    filterRecyclerview.setAdapter(adapterAll);
                    filterRecyclerview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            filterRecyclerview.smoothScrollToPosition(adapterAll.getItemCount() - 3);
                        }
                    }, 100);

                    //mainPageToSetAdapter.setAdapters(adapterAll,adapterMetroplitan,adapterSubMetropolitan,adapterMunicipal,adapterRuralMunicipal);

                    //Toast.makeText(getContext(), "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GloballyCommon.checkErrorResponse(coordinatorLayout,getApplicationContext());
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