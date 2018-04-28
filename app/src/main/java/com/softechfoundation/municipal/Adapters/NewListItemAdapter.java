package com.softechfoundation.municipal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Activities.MainPage;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;
import com.softechfoundation.municipal.Fragments.MainFragment;
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
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 3/27/2018.
 */

public class NewListItemAdapter extends RecyclerView.Adapter<NewListItemAdapter.ListItemViewHolder> {
    private static final String MY_PREFS = "SharedValues";
    private Context context;
    private LayoutInflater inflator;
    // public static String state,district,localLevel;
    private List<ListItem> dataItem = Collections.emptyList();
    private RecyclerView recyclerView;
    private StringBuilder stringBuilder = new StringBuilder();
    private TextToSpeech tts;
    private int ACT_CHECK_TTS_DATA = 1000;
    public static String globalState, globalDistrict, globalLocalLevel;
    private String name;
    public static NewListItemAdapter adapterDistrict, adapterVdc, adapterMetroplitan, adapterAll, adapterOldVdc;
    public static NewListItemAdapter adapterSubMetropolitan, adapterMunicipal, adapterRuralMunicipal;

    private Context getContext() {
        return context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public NewListItemAdapter(Context context, List<ListItem> dataItem, RecyclerView recyclerView) {
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
//        Drawable topDrawable=getContext().getApplicationContext().getResources().getDrawable(currentItem.getIcon());
//        holder.listName.setCompoundDrawables(null,topDrawable,null,null);
        holder.listIcon.setImageResource(currentItem.getIcon());
        setName(currentItem.getName());

        holder.placeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainPage.pathView.setVisibility(View.VISIBLE)

                if ("state".equals(currentItem.getType())) {
                    globalState = currentItem.getName();
                    populateDistrictRecyclerView();
                    showMessage("Hey, dude you clicked me.");

                }

                if ("district".equals(currentItem.getType())) {
                    //Toast.makeText(context, "You clicked " + currentItem.getName(), Toast.LENGTH_SHORT).show();
                    globalDistrict = currentItem.getName();

                    String location = globalDistrict + ", " + "Nepal";
                    // MainFragment.findPlace(location,context);
                    SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();
                    editor.putString("location", location);
                    editor.apply();
                    MainFragment.trigger.performClick();
                    populateLocalLevelRecyclerView();

                }
                if ("ruralMunicipal".equals(currentItem.getType())) {
                    clickLocalLevelMenu("RuralMunicipal");
                } else if ("municipal".equals(currentItem.getType())) {
                    clickLocalLevelMenu("municipal");
                } else if ("subMetropolitan".equals(currentItem.getType())) {
                    clickLocalLevelMenu("subMetropolitan");
                } else if ("metropolitan".equals(currentItem.getType())) {
                    clickLocalLevelMenu("metropolitan");
                }

            }

            private void clickLocalLevelMenu(String type) {
                globalLocalLevel = currentItem.getName();
                MainFragment.stateBtn.setVisibility(View.VISIBLE);
                MainFragment.vdcBtn.setVisibility(View.VISIBLE);
                MainFragment.districtBtn.setVisibility(View.VISIBLE);
                MainFragment.vdcBtn.setText(currentItem.getName());
                MainFragment.catagories.setText("VDCs");

                String location = globalDistrict + ", " + globalLocalLevel + ", " + "Nepal";
                SharedPreferences.Editor editor = getContext().getApplicationContext().getSharedPreferences(MY_PREFS, MODE_PRIVATE).edit();

                editor.clear();
                editor.apply();
                editor.putString("location", location);
                editor.apply();
                editor.commit();
                MainFragment.trigger.performClick();

                getOldDetail(type);
            }
        });
////Ask user to choose place mapping
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setCancelable(true);
//        builder.setTitle("Choose place mapping");
//        builder.setPositiveButton("Old To New", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                OldToNew(currentItem);
//            }
//        });
//        builder.setNegativeButton("New To Old", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                newToOld(holder,currentItem);
//
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//        //end of ask maping


        MainFragment.districtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateDistrictRecyclerView();
                //Toast.makeText(context, "Inside pathDistrictBtn: "+state, Toast.LENGTH_SHORT).show();
                MainFragment.catagories.setText("Districts");
                MainFragment.searchBox.setHint("Search Districts...");
                MainFragment.stateBtn.setVisibility(View.VISIBLE);
                MainFragment.districtBtn.setVisibility(View.VISIBLE);
                MainFragment.districtBtn.setText("Districts");
                MainFragment.vdcBtn.setVisibility(View.GONE);
                MainFragment.catagories.setVisibility(View.GONE);
                MainFragment.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
            }
        });

        MainFragment.vdcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateLocalLevelRecyclerView();
                MainFragment.catagories.setText("VDCs");
                MainFragment.vdcBtn.setText("VDCs");
                MainFragment.searchBox.setHint("Search VDCs...");
                MainFragment.stateBtn.setVisibility(View.VISIBLE);
                MainFragment.stateBtn.setVisibility(View.VISIBLE);
                MainFragment.districtBtn.setVisibility(View.VISIBLE);
                MainFragment.catagories.setVisibility(View.GONE);
                MainFragment.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
            }
        });


    }

    private void getOldDetail(final String type) {
        //Start Caching
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl("http://192.168.100.178:8080/locallevel/rest/vdcs/OldVdcList/",
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
        editor.putString("message", "Play voice is on so I am able to speak. If you want to stop voice go to setting and disable the play sound");
        editor.apply();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.mapping_result_custom_design, null);
        dialogBuilder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.mapping_place_result);
        textView.setText(message);
        Button okBtn = dialogView.findViewById(R.id.mapping_place_btn);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationLeftRight;
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

        MainFragment.searchBox.setHint("Search VDC");
        MainFragment.searchBox.setText("");
        MainFragment.catagories.setText("VDCs");
        MainFragment.catagories.setVisibility(View.GONE);
        MainFragment.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
        MainFragment.districtBtn.setText(globalDistrict);

        MainFragment.stateBtn.setVisibility(View.VISIBLE);
        MainFragment.vdcBtn.setVisibility(View.VISIBLE);
        MainFragment.districtBtn.setVisibility(View.VISIBLE);

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl("http://192.168.100.178:8080/locallevel/rest/districts/localLevel/",
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
                    adapterRuralMunicipal = new NewListItemAdapter(getContext(), ruralMunicipalList, recyclerView);
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
                    adapterMunicipal = new NewListItemAdapter(getContext(), municipalList, recyclerView);
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
                    adapterMetroplitan = new NewListItemAdapter(getContext(), metropolitanList, recyclerView);
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
                    adapterSubMetropolitan = new NewListItemAdapter(getContext(), subMetropolitanList, recyclerView);

                    adapterAll = new NewListItemAdapter(getContext(), allList, recyclerView);

                    if (adapterAll != null) {
                        recyclerView.setAdapter(adapterAll);
                        MainFragment.btnAll.performClick();
                    } else {
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

        allNames = new String[allList.size()];
        int j = 0;
        for (ListItem names : ruralMunicipalList) {
            allNames[j] = names.getName();
            j++;
        }

        final ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, allNames);
        MainFragment.searchBox.setAdapter(autoComAdapter);
        MainFragment.searchBox.addTextChangedListener(new TextWatcher() {
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
                NewListItemAdapter filteredAdapter = new NewListItemAdapter(getContext(), newList, recyclerView);
                if (filteredAdapter != null) {
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


    private void populateDistrictRecyclerView() {
        final String[] districtNames;
        final List<ListItem> districtList = new ArrayList<>();

        MainFragment.searchBox.setHint("Search District");
        MainFragment.searchBox.setText("");
        MainFragment.catagories.setText("Districts");
        MainFragment.stateBtn.setText(globalState);

        MainFragment.stateBtn.setVisibility(View.VISIBLE);
        MainFragment.vdcBtn.setVisibility(View.GONE);
        MainFragment.districtBtn.setText("Districts");
        MainFragment.districtBtn.setVisibility(View.VISIBLE);

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl("http://192.168.100.178:8080/locallevel/rest/districts/state/",
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


                    adapterDistrict = new NewListItemAdapter(getContext(), districtList, recyclerView);

                    if (adapterDistrict != null) {
                        recyclerView.setAdapter(adapterDistrict);
                    } else {
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
        MainFragment.searchBox.setAdapter(autoComAdapter);

//        adapterDistrict = new NewListItemAdapter(getContext(), districtList, recyclerView);
//        recyclerView.setAdapter(adapterDistrict);

        MainFragment.searchBox.addTextChangedListener(new TextWatcher() {
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
                NewListItemAdapter filteredAdapter = new NewListItemAdapter(getContext(), newList, recyclerView);
                if (filteredAdapter != null) {
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

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        ImageView listIcon;
        CardView placeCardView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            listIcon = itemView.findViewById(R.id.list_icon);
            listName = itemView.findViewById(R.id.list_name);
            placeCardView = itemView.findViewById(R.id.card_botton);

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

        Log.d("Final Url: ", encodedUrl.toString());
        return encodedUrl;


    }


}
