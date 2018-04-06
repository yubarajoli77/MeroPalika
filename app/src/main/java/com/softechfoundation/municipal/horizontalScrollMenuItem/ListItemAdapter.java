package com.softechfoundation.municipal.horizontalScrollMenuItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.CacheRequest;
import com.softechfoundation.municipal.MainPage;
import com.softechfoundation.municipal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 3/27/2018.
 */

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    private Context context;
    private LayoutInflater inflator;
    public static String state,district,localLevel;
    private List<ListItem> dataItem = Collections.emptyList();
    private RecyclerView recyclerView;
    private String name;
    public static ListItemAdapter adapterDistrict, adapterVdc, adapterMetroplitan,adapterAll;
    public static ListItemAdapter adapterSubMetropolitan,adapterMunicipal,adapterRuralMunicipal;

    private Context getContext() {
        return context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



    public ListItemAdapter(Context context, List<ListItem> dataItem, RecyclerView recyclerView) {
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design, parent, false);
        ListItemViewHolder holder = new ListItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder holder, int position) {
        final ListItem currentItem = dataItem.get(position);
        holder.listName.setText(currentItem.getName());
        holder.listIcon.setImageResource(currentItem.getIcon());
        setName(currentItem.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainPage.navigation.setVisibility(View.VISIBLE);
               // MainPage.pathView.setVisibility(View.VISIBLE);
                if ("state".equals(currentItem.getType())) {
                    state=currentItem.getName();
                   // Toast.makeText(context, "Inside state clicked: "+state, Toast.LENGTH_SHORT).show();
                    MainPage.searchBox.setHint("Search District");
                    MainPage.searchBox.setText("");
                    MainPage.catagories.setText("Districts");
                    MainPage.stateBtn.setText(currentItem.getName());
                   // Toast.makeText(context, "You clicked " + currentItem.getName(), Toast.LENGTH_SHORT).show();

                    populateDistrictRecyclerView();
//                    Toast.makeText(context, "Inside the room", Toast.LENGTH_SHORT).show();
//                    String finalDistrictListUrl =
//                            makeFinalUrl("http://192.168.100.237:8088/localLevel/rest/districts/state/",
//                                    currentItem.getName());
//
//                    final RequestQueue requestQueue = Volley.newRequestQueue(context);
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, finalDistrictListUrl, new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
////                    JSONObject jsonObject=new JSONObject(response);
////                        JSONArray jsonArray=jsonObject.getJSONArray("Name");
//                                List<ListItem> arrayList = new ArrayList<>();
//                                JSONArray jsonArray = new JSONArray(response);
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    ListItem listItem=new ListItem();
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    String nameList = jsonObject1.getString("district");
//                                   // String logo=jsonObject1.getString("icon");
//                                    String type=jsonObject1.getString("type");
//                                    listItem.setName("Hello");
//                                    listItem.setType("district");
//                                    listItem.setIcon(R.drawable.ministry);
//                                    arrayList.add(listItem);
//                                }
//
//                               ListItemAdapter adapter=new ListItemAdapter(context,arrayList,recyclerView);
//                                recyclerView.setAdapter(adapter);
////                    recyclerView.setAdapter(adapter);
////                    charSequence=arrayList.toArray(new CharSequence[arrayList.size()]);
////                    spinner.setList(charSequence);
//
//                                //  spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            error.printStackTrace();
//                        }
//                    });
//
//                    int socketTimeout = 30000;
//                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                    stringRequest.setRetryPolicy(policy);
//                    requestQueue.add(stringRequest);

                }
                if ("district".equals(currentItem.getType())) {
                    //Toast.makeText(context, "You clicked " + currentItem.getName(), Toast.LENGTH_SHORT).show();
                    district=currentItem.getName();
                    MainPage.searchBox.setHint("Search VDC");
                    MainPage.searchBox.setText("");
                    MainPage.catagories.setText("VDCs");
                    MainPage.catagories.setVisibility(View.GONE);
                    MainPage.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
                    MainPage.districtBtn.setText(currentItem.getName());
                    populateVdcRecyclerView();
                }
                if ("vdc".equals(currentItem.getType())) {
                   // Toast.makeText(context, "You clicked " + currentItem.getName(), Toast.LENGTH_SHORT).show();
                    localLevel=currentItem.getName();
                    MainPage.stateBtn.setVisibility(View.VISIBLE);
                    MainPage.vdcBtn.setVisibility(View.VISIBLE);
                    MainPage.districtBtn.setVisibility(View.VISIBLE);
                    MainPage.vdcBtn.setText(currentItem.getName());
                    MainPage.catagories.setText("VDCs");

                }

            }
        });

        MainPage.districtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateDistrictRecyclerView();
                //Toast.makeText(context, "Inside pathDistrictBtn: "+state, Toast.LENGTH_SHORT).show();
                MainPage.catagories.setText("Districts");
                MainPage.searchBox.setHint("Search Districts...");
                MainPage.stateBtn.setVisibility(View.VISIBLE);
                MainPage.districtBtn.setVisibility(View.VISIBLE);
                MainPage.districtBtn.setText("Districts");
                MainPage.vdcBtn.setVisibility(View.GONE);
                MainPage.catagories.setVisibility(View.GONE);
                MainPage.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
            }
        });

        MainPage.vdcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateVdcRecyclerView();
                MainPage.catagories.setText("VDCs");
                MainPage.vdcBtn.setText("VDCs");
                MainPage.searchBox.setHint("Search VDCs...");
                MainPage.stateBtn.setVisibility(View.VISIBLE);
                MainPage.stateBtn.setVisibility(View.VISIBLE);
                MainPage.districtBtn.setVisibility(View.VISIBLE);
                MainPage.catagories.setVisibility(View.GONE);
                MainPage.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
            }
        });


    }

    private void populateVdcRecyclerView() {
        final String[] allNames,ruralMunicipalNames,municipalNames,metropolitanNames,subMetropolitanNames;
       // final List<ListItem> vdcList=new ArrayList<>();
        final List<ListItem>allList=new ArrayList<>();
        final List<ListItem>metropolitanList=new ArrayList<>();
        final List<ListItem> subMetropolitanList=new ArrayList<>();
        final List<ListItem>municipalList=new ArrayList<>();
        final List<ListItem> ruralMunicipalList=new ArrayList<>();

        MainPage.stateBtn.setVisibility(View.VISIBLE);
        MainPage.vdcBtn.setVisibility(View.VISIBLE);
        MainPage.districtBtn.setVisibility(View.VISIBLE);

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl("http://192.168.100.178:8088/localLevel/rest/districts/localLevel/",
                district);


        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray ruralMuniJsonArray = jsonObject.getJSONArray("ruralMunicipal");
                    JSONArray municipalJsonArray=jsonObject.getJSONArray("municipal");
                    JSONArray metropolitanJsonArray=jsonObject.getJSONArray("metropolitan");
                    JSONArray subMetropolitanJsonArray=jsonObject.getJSONArray("subMetropolitan");

                    //Removing duplication of data from cache
                    allList.clear();
                    ruralMunicipalList.clear();
                    municipalList.clear();
                    subMetropolitanList.clear();
                    metropolitanList.clear();

                    //for ruralMunicipal
                    for (int i = 0; i < ruralMuniJsonArray.length(); i++) {
                        ListItem listItem=new ListItem();
                        JSONObject jsonObject1 = ruralMuniJsonArray.getJSONObject(i);
                        String vdcName = jsonObject1.getString("ruralMunicipal");
                        listItem.setName(vdcName);
                        listItem.setIcon(R.drawable.globe);
                        listItem.setType("ruralMunicipal");
                        ruralMunicipalList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterRuralMunicipal=new ListItemAdapter(getContext(),ruralMunicipalList,recyclerView);
                    //for municipal
                    for (int i = 0; i < municipalJsonArray.length(); i++) {
                        ListItem listItem=new ListItem();
                        JSONObject jsonObject1 = municipalJsonArray.getJSONObject(i);
                        String municipalName = jsonObject1.getString("municipal");
                        listItem.setName(municipalName);
                        listItem.setIcon(R.drawable.ministry);
                        listItem.setType("municipal");
                        municipalList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterMunicipal=new ListItemAdapter(getContext(),municipalList,recyclerView);
                    //for metropolitan
                    for (int i = 0; i < metropolitanJsonArray.length(); i++) {
                        ListItem listItem=new ListItem();
                        JSONObject jsonObject1 = metropolitanJsonArray.getJSONObject(i);
                        String metropolitanName = jsonObject1.getString("metropolitan");
                        listItem.setName(metropolitanName);
                        listItem.setIcon(R.drawable.ministry);
                        listItem.setType("metropolitan");
                        metropolitanList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterMetroplitan=new ListItemAdapter(getContext(),metropolitanList,recyclerView);
                    //for subMetropolitan
                    for (int i = 0; i < subMetropolitanJsonArray.length(); i++) {
                        ListItem listItem=new ListItem();
                        JSONObject jsonObject1 = subMetropolitanJsonArray.getJSONObject(i);
                        String subMetropolitanName = jsonObject1.getString("subMetropolitan");
                        listItem.setName(subMetropolitanName);
                        listItem.setIcon(R.drawable.ministry);
                        listItem.setType("subMetropolitan");
                        subMetropolitanList.add(listItem);
                        allList.add(listItem);
                    }
                    adapterSubMetropolitan=new ListItemAdapter(getContext(),subMetropolitanList,recyclerView);

                    adapterAll=new ListItemAdapter(getContext(),allList,recyclerView);

                    if(adapterAll!=null){
                        recyclerView.setAdapter(adapterAll);
                        MainPage.btnAll.performClick();
                    }else {
                        Toast.makeText(context, "No value in all Adapter", Toast.LENGTH_SHORT).show();
                    }

                    //mainPageToSetAdapter.setAdapters(adapterAll,adapterMetroplitan,adapterSubMetropolitan,adapterMunicipal,adapterRuralMunicipal);

                    //Toast.makeText(getContext(), "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
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

         allNames= new String[allList.size()];
        int j = 0;
        for (ListItem names : ruralMunicipalList) {
            allNames[j] = names.getName();
            j++;
        }

        final ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, allNames);
        MainPage.searchBox.setAdapter(autoComAdapter);
        MainPage.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                List<ListItem> newList = new ArrayList<>();
                for (ListItem list : allList) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                ListItemAdapter filteredAdapter = new ListItemAdapter(getContext(), newList, recyclerView);
                if(filteredAdapter!=null){
                    recyclerView.setAdapter(filteredAdapter);
                }else {
                    recyclerView.setAdapter(adapterDistrict);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void populateDistrictRecyclerView() {
       final String[] districtNames;
        final List<ListItem> districtList=new ArrayList<>();
       MainPage.stateBtn.setVisibility(View.VISIBLE);
       MainPage.vdcBtn.setVisibility(View.GONE);
       MainPage.districtBtn.setText("Districts");
        MainPage.districtBtn.setVisibility(View.VISIBLE);

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl("http://192.168.100.178:8088/localLevel/rest/districts/state/",
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
                        ListItem listItem=new ListItem();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String districtName = jsonObject1.getString("district");
                        listItem.setName(districtName);
                        listItem.setIcon(R.drawable.ministry);
                        listItem.setType("district");

                        districtList.add(listItem);
                    }


                    adapterDistrict=new ListItemAdapter(getContext(),districtList,recyclerView);

                    if(adapterDistrict!=null){
                        recyclerView.setAdapter(adapterDistrict);
                    }else {
                        Toast.makeText(context, "No value in district Adapter", Toast.LENGTH_SHORT).show();
                    }


                    //Toast.makeText(getContext(), "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        MainPage.searchBox.setAdapter(autoComAdapter);

//        adapterDistrict = new ListItemAdapter(getContext(), districtList, recyclerView);
//        recyclerView.setAdapter(adapterDistrict);

        MainPage.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                List<ListItem> newList = new ArrayList<>();
                for (ListItem list : districtList) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                ListItemAdapter filteredAdapter = new ListItemAdapter(getContext(), newList, recyclerView);
                if(filteredAdapter!=null){
                    recyclerView.setAdapter(filteredAdapter);
                }else {
                    recyclerView.setAdapter(adapterDistrict);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        ImageView listIcon;
        CardView cardView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            listIcon = itemView.findViewById(R.id.list_icon);
            listName = itemView.findViewById(R.id.list_name);
            cardView = itemView.findViewById(R.id.card_botton);

        }
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

}
