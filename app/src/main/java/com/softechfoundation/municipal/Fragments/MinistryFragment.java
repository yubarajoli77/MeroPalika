package com.softechfoundation.municipal.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Adapters.MinistryCustomAdapter;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Pojos.MinistryPojo;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class MinistryFragment extends Fragment {
private RecyclerView recyclerView;
private MinistryCustomAdapter adapter;
private View loading;
    public MinistryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_ministry, container, false);
        defineView(view);
        executePorcess();
        return view;
    }

    private void executePorcess() {
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = CommonUrl.BaseUrl+"ministry";

        final List<MinistryPojo> ministryPojoList=new ArrayList<>();
        CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONArray ministryJsonArray=new JSONArray(jsonString);
                    ministryPojoList.clear();
                    for (int i = 0; i < ministryJsonArray.length(); i++) {
                        MinistryPojo listItem=new MinistryPojo();
                        JSONObject jsonObject1 = ministryJsonArray.getJSONObject(i);
                        String ministryName = jsonObject1.getString("ministryName");
                        String ministerName = jsonObject1.getString("ministerName");
                        String contactNumber = jsonObject1.getString("contactNumber");
                        String ministerImage = jsonObject1.getString("ministerImage");
                        String ministerEmail = jsonObject1.getString("ministerEmail");
                        String party = jsonObject1.getString("party");
                        listItem.setMinistryName(ministryName);
                        listItem.setMinisterEmail(ministerEmail);
                        listItem.setContactNumber(contactNumber);
                        listItem.setMinisterName(ministerName);
                        listItem.setMinisterImage(ministerImage);
                        listItem.setParty(party);
                        ministryPojoList.add(listItem);
                    }

                    adapter=new MinistryCustomAdapter(getActivity().getApplicationContext(),ministryPojoList);
                    loading.setVisibility(View.GONE);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);

            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching
    }

    private void defineView(View view) {
        loading=view.findViewById(R.id.dotted_loading);
        recyclerView=view.findViewById(R.id.ministry_recycler_view);
    }


}
