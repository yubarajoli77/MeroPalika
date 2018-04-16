package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Adapters.ResourceCustomAdapter;
import com.softechfoundation.municipal.Adapters.ServiceCustomAdapter;
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
    private ResourceCustomAdapter resourceCustomAdapter;
    private ServiceCustomAdapter serviceCustomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of_services_and_resources);
        resourceRecyclerView=findViewById(R.id.state_detail_list);

        Intent intent= getIntent();
        String catagory=intent.getStringExtra("catagory");
        if("SERVICES".equals(catagory)){
            getSupportActionBar().setTitle("Hospitals");
            getServices("State 1");

        }
        else if("RESOURCES".equals(catagory)){
            getSupportActionBar().setTitle("Mountains");
            getResources("State 1");
        }


    }

public void getResources(String parameter) {

   final List<ResourcePojo> resourcesList=new ArrayList<>();
    //Start Caching
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = makeFinalUrl("http://192.168.100.237:8088/localLevel/rest/mountains/getMountains/",
            parameter);

    CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
        @Override
        public void onResponse(NetworkResponse response) {
            try {
                final String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                //JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = new JSONArray(jsonString);
                resourcesList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ResourcePojo listItem = new ResourcePojo();
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String resourceName = jsonObject1.getString("mountain");
                    String resourceAddress=jsonObject1.getString("district");
                    listItem.setName(resourceName);
                    listItem.setAddress(resourceName+", "+resourceAddress+", "+"Nepal");

                    resourcesList.add(listItem);
                }

                resourceCustomAdapter = new ResourceCustomAdapter(ListOfServicesAndResources.this,resourcesList);
                resourceRecyclerView.setAdapter(resourceCustomAdapter);
                resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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
}
    public void getServices(String parameter) {

        final List<ServicePojo> serviceList=new ArrayList<>();
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = makeFinalUrl("http://192.168.100.237:8088/localLevel/rest/hospital/getHospital/",
                parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    //JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = new JSONArray(jsonString);
                    serviceList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ServicePojo listItem = new ServicePojo();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String serviceName = jsonObject1.getString("hospital");
                        String serviceAddress=jsonObject1.getString("district");
                        String  servicePhone=jsonObject1.getString("hospitalContactNumber");
                        listItem.setName(serviceName);
                        listItem.setAddress(serviceName+", "+serviceAddress+", "+"Nepal");
                        listItem.setPhone(servicePhone);
                        serviceList.add(listItem);
                    }

                    serviceCustomAdapter = new ServiceCustomAdapter(ListOfServicesAndResources.this,serviceList);
                    resourceRecyclerView.setAdapter(serviceCustomAdapter);
                    resourceRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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
