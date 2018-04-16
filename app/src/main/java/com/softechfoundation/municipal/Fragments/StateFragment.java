package com.softechfoundation.municipal.Fragments;


import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.softechfoundation.municipal.Adapters.OldListItemAdapter;
import com.softechfoundation.municipal.CircularListViewDesign.CircularItemAdapter;
import com.softechfoundation.municipal.CircularListViewDesign.CircularListView;
import com.softechfoundation.municipal.CircularListViewDesign.CircularTouchListener;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.Activities.StateDetails;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StateFragment extends Fragment {
    private CircularItemAdapter adapter;

    public StateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_state, container, false);
        doTask(view);
        return view;
    }

    private void doTask(View view) {

        final ArrayList<String> itemTitles = new ArrayList<>();
        for(int i = 1 ; i < 8 ; i ++){
            itemTitles.add(String.valueOf(i));
        }

        final CircularListView circularListView = (CircularListView) view.findViewById(R.id.circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), itemTitles);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(120);

        circularListView.setOnItemClickListener(new  CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                int position=index+1;
                final String parameter="State "+(position);

                //Start Caching
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = makeFinalUrl("http://192.168.100.237:8088/localLevel/rest/states/stateDetails/",
                        parameter);


                CacheRequest cacheRequest = new CacheRequest(0, url, new Response.Listener<NetworkResponse>() {

                    @Override
                    public void onResponse(NetworkResponse response) {
                        String stateName,area,population,website,governer,chiefMinister,capital,density;

                        try {
                            final String jsonString = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonObject = new JSONObject(jsonString);

                            stateName=parameter;
                            area=jsonObject.getString("area");
                            population=jsonObject.getString("population");
                            website=jsonObject.getString("website");
                            governer=jsonObject.getString("governer");
                            chiefMinister=jsonObject.getString("chiefMinister");

                            capital=jsonObject.getString("capital");
                            density=jsonObject.getString("density");

                            Log.d("Total value::::",area+stateName+capital);
                            final Intent intent=new Intent(getActivity(),StateDetails.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("stateName",stateName);
                            intent.putExtra("area",area);
                            intent.putExtra("population",population);
                            intent.putExtra("website",website);
                            intent.putExtra("governer",governer);
                            intent.putExtra("chiefMinister",chiefMinister);
                            intent.putExtra("capital",capital);
                            intent.putExtra("density",density);
                            startActivity(intent);

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

        });
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
