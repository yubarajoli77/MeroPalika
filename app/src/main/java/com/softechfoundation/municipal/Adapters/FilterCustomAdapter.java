package com.softechfoundation.municipal.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.softechfoundation.municipal.Activities.StateDetails;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 5/10/2018.
 */

public class FilterCustomAdapter  extends RecyclerView.Adapter<FilterCustomAdapter.FilterCustomAdapterHolder> {
    private static final String MY_PREFS = "districtOrPalika";
    private Context context;
    private LayoutInflater inflator;
    // public static String state,district,localLevel;
    private List<ListItem> dataItem = Collections.emptyList();
    private RecyclerView recyclerView;
    private StringBuilder stringBuilder = new StringBuilder();
   
    public static String globalState, globalDistrict, globalLocalLevel;
    private String name;
    public static FilterCustomAdapter adapterDistrict, adapterVdc, adapterMetroplitan, adapterAll, adapterOldVdc;
    public static FilterCustomAdapter adapterSubMetropolitan, adapterMunicipal, adapterRuralMunicipal;

    private Context getContext() {
        return context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public FilterCustomAdapter(Context context, List<ListItem> dataItem, RecyclerView recyclerView) {
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public FilterCustomAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design, parent, false);
        FilterCustomAdapterHolder holder = new FilterCustomAdapterHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterCustomAdapterHolder holder, int position) {
        final ListItem currentItem = dataItem.get(position);
        holder.listName.setText(currentItem.getName());
//        Drawable topDrawable=getContext().getApplicationContext().getResources().getDrawable(currentItem.getIcon());
//        holder.listName.setCompoundDrawables(null,topDrawable,null,null);
//        holder.listIcon.setImageResource(currentItem.getIcon());

        Glide
                .with(context)
                .load(currentItem.getIcon())
                .into( holder.listIcon);
        setName(currentItem.getName());

        holder.placeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StateDetails.filterBackButton.setVisibility(View.VISIBLE);
                StateDetails.filterLoading.setVisibility(View.VISIBLE);
                if ("district".equals(currentItem.getType())) {
                   StateDetails.filterRviewTitle.setText("Local Levels");
                    globalDistrict = currentItem.getName();
                    populateLocalLevelRecyclerView();
                }
                if ("ruralMunicipal".equals(currentItem.getType())) {
                } else if ("municipal".equals(currentItem.getType())) {
                } else if ("subMetropolitan".equals(currentItem.getType())) {
                } else if ("metropolitan".equals(currentItem.getType())) {
                }

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

        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = makeFinalUrl("http://103.198.9.242:8080/locallevel/rest/districts/localLevel/",
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
                    adapterRuralMunicipal = new FilterCustomAdapter(getContext(), ruralMunicipalList, recyclerView);
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
                    adapterMunicipal = new FilterCustomAdapter(getContext(), municipalList, recyclerView);
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
                    adapterMetroplitan = new FilterCustomAdapter(getContext(), metropolitanList, recyclerView);
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
                    adapterSubMetropolitan = new FilterCustomAdapter(getContext(), subMetropolitanList, recyclerView);

                    adapterAll = new FilterCustomAdapter(getContext(), allList, recyclerView);
                    StateDetails.filterLoading.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapterAll);

                    //mainPageToSetAdapter.setAdapters(adapterAll,adapterMetroplitan,adapterSubMetropolitan,adapterMunicipal,adapterRuralMunicipal);

                    //Toast.makeText(getContext(), "onResponse:\n\n" + jsonObject.toString(), Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               
                Toast.makeText(context, "No value in district Adapter", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class FilterCustomAdapterHolder extends RecyclerView.ViewHolder {
        TextView listName;
        ImageView listIcon;
        CardView placeCardView;

        public FilterCustomAdapterHolder(View itemView) {
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
