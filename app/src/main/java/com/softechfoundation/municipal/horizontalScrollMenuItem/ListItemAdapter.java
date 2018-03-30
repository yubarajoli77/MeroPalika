package com.softechfoundation.municipal.horizontalScrollMenuItem;

import android.content.Context;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

/**
 * Created by yubar on 3/27/2018.
 */

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    private Context context;
    private LayoutInflater inflator;
    private List<ListItem> dataItem = Collections.emptyList();
    private RecyclerView recyclerView;
    private ListItemAdapter adapterDistrict, adapterVdc;

    private Context getContext() {
        return context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;

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
                if (currentItem.getType().equals("state")) {
                    MainPage.searchBox.setHint("Search District");
                    MainPage.searchBox.setText("");
                    MainPage.catagories.setText("Districts");
                    MainPage.stateBtn.setText(currentItem.getName());
                    Toast.makeText(context, "You clicked " + currentItem.getName(), Toast.LENGTH_SHORT).show();

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
                if (currentItem.getType().equals("district")) {
                    MainPage.searchBox.setHint("Search VDC");
                    MainPage.searchBox.setText("");
                    MainPage.catagories.setText("VDCs");
                    MainPage.catagories.setVisibility(View.GONE);
                    MainPage.horizontalScrollViewMenu.setVisibility(View.VISIBLE);
                    MainPage.districtBtn.setText(currentItem.getName());
                    populateVdcRecyclerView();
                }
                if (currentItem.getType().equals("vdc")) {
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
        List<ListItem> listItemss = new ArrayList<>();
        MainPage.stateBtn.setVisibility(View.VISIBLE);
        MainPage.vdcBtn.setVisibility(View.VISIBLE);
        MainPage.districtBtn.setVisibility(View.VISIBLE);
        for (int i = 0; i < 20; i++) {
            ListItem current = new ListItem();
            current.setIcon(R.drawable.ministry);
            current.setName("VDC " + i);
            current.setType("vdc");
            listItemss.add(current);
        }
        adapterVdc = new ListItemAdapter(context, listItemss, recyclerView);
        recyclerView.setAdapter(adapterVdc);
    }

    private void populateDistrictRecyclerView() {
        final String[] districtNames;
        final List<ListItem> listItems = new ArrayList<>();
        MainPage.stateBtn.setVisibility(View.VISIBLE);
        MainPage.vdcBtn.setVisibility(View.GONE);
        MainPage.districtBtn.setText("Districts");
        MainPage.districtBtn.setVisibility(View.VISIBLE);
        for (int i = 0; i < 10; i++) {
            ListItem current = new ListItem();
            current.setIcon(R.drawable.ministry);
            current.setName("District " + i);
            current.setType("district");
            listItems.add(current);
        }
        districtNames = new String[listItems.size()];
        int j = 0;
        for (ListItem names : listItems) {
            districtNames[j] = names.getName();
            j++;
        }
        final ArrayAdapter<String> autoComAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, districtNames);
        MainPage.searchBox.setAdapter(autoComAdapter);

        adapterDistrict = new ListItemAdapter(context, listItems, recyclerView);
        recyclerView.setAdapter(adapterDistrict);

        MainPage.searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String typedText = s.toString().toLowerCase();
                List<ListItem> newList = new ArrayList<>();
                for (ListItem list : listItems) {
                    String stateName = list.getName().toLowerCase();
                    if (stateName.contains(typedText)) {
                        newList.add(list);
                    }
                }
                adapterDistrict = new ListItemAdapter(getContext(), newList, recyclerView);
                recyclerView.setAdapter(adapterDistrict);
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
        return encodedUrl;
    }

}
