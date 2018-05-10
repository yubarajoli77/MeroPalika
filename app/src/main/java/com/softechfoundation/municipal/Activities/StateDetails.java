package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
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
import com.softechfoundation.municipal.Fragments.MainFragment;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.R;
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

public class StateDetails extends AppCompatActivity{
    private LinearLayout naturalResources,infrastructure,mainAttraction,urgentServices,avaliableContacts;
private TextView stateName,capitalName,area,population,density,governer,chiefMinister,website;
private  CollapsingToolbarLayout collapsingToolbarLayout;
private RecyclerView filterRecyclerview;
private FilterCustomAdapter adapter;
public static TextView filterRviewTitle,filterBackButton;
public static View filterLoading;
private static final String MY_PREFS = "districtOrPalika";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        collapsingToolbarLayout=findViewById(R.id.state_detail_collapsing_tool_bar_layout);
        stateName=findViewById(R.id.detail_top_state_name);
        capitalName=findViewById(R.id.detail_top_capital_name);
        area=findViewById(R.id.area);
        population=findViewById(R.id.population);
        density=findViewById(R.id.density);
//        governer=findViewById(R.id.detail_top_state_governer);
//        website=findViewById(R.id.detail_top_state_website);
        chiefMinister=findViewById(R.id.detail_top_state_chiefMinister);

        avaliableContacts=findViewById(R.id.available_contacts_layout);
        naturalResources=findViewById(R.id.natural_resource_layout);
        infrastructure=findViewById(R.id.infrastructure_layout);
        mainAttraction=findViewById(R.id.main_attraction_layout);
        urgentServices=findViewById(R.id.urgent_services_layout);

        filterRecyclerview=findViewById(R.id.filter_recycleriew);
        filterRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        filterRviewTitle=findViewById(R.id.filter_recyclerview_title);
        filterBackButton=findViewById(R.id.filter_back);
        filterLoading=findViewById(R.id.dotted_filter_loading);



        Intent intentGetValue=getIntent();
        String gorvernerName=intentGetValue.getStringExtra("governer");
        String chiefMinisterName=intentGetValue.getStringExtra("chiefMinister");
        String websiteName=intentGetValue.getStringExtra("website");
        final String state=intentGetValue.getStringExtra("stateName");
        stateName.setText(state);
        String capitalCity=intentGetValue.getStringExtra("capital");
        capitalName.setText(capitalCity);
        area.setText(intentGetValue.getStringExtra("area"));
        population.setText(intentGetValue.getStringExtra("population"));
        density.setText(intentGetValue.getStringExtra("density"));
        chiefMinister.setText(chiefMinisterName);
//        getSupportActionBar().setTitle("Detail about"+" "+state);
        collapsingToolbarLayout.setTitle("Detail about"+" "+state);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));

        naturalResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","RESOURCES");
                intent.putExtra("state",state);
                startActivity(intent);

            }
        });

        infrastructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","INFRASTRUCTURES");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });

        urgentServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","URGENTSERVICES");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });

        mainAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","MAINATTRACTIONS");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });

        avaliableContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","AVALIABLECONTACTS");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });
        getDistricts(state);

        filterBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    filterRviewTitle.setText("Districts");
                    getDistricts(state);
                    filterBackButton.setVisibility(View.GONE);
            }
        });

    }

    private void getDistricts(String state) {
        //Start Caching
        filterLoading.setVisibility(View.VISIBLE);
        final List<ListItem> districtList=new ArrayList<>(Collections.<ListItem>emptyList());
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl("http://103.198.9.242:8080/locallevel/rest/districts/state/",
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


                    adapter = new FilterCustomAdapter(StateDetails.this, districtList, filterRecyclerview);
                    filterLoading.setVisibility(View.GONE);
                    filterRecyclerview.setAdapter(adapter);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainFragment.loadingPlaces.setVisibility(View.GONE);
                Toast.makeText(StateDetails.this, "No Internet Available", Toast.LENGTH_SHORT).show();

                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
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