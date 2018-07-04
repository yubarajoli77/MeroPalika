package com.softechfoundation.municipal.Fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.softechfoundation.municipal.GloballyCommon;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;
import com.softechfoundation.municipal.backgroundService.MeroPalikaService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private SliderLayout mSlider;
    private TextView popToday, popTomorrow;
    private boolean isApplicationOpenFirstTime = true;
    private Intent serviceIntent;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;
    private MeroPalikaService meroPalikaService;
    private Integer todayPopulations,tomorrowPopulation;
    public static Integer tdPop;
    private FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getPopulationByDate(getDate());
        getPopulationByDate(getTomorrowDate());
        View view = inflater.inflate(R.layout.fragment_home, container, false);

//        new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(3000);
//                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("RefreshPopulation", MODE_PRIVATE).edit();
//                        editor.putBoolean("is24HourPassed", true);
//                        editor.apply();
//                        editor.commit();
//                        hitMeroPalikaService();
//                        Log.d("=====","Under 24 hr Thread");
////                    SharedPreferences.Editor editor = getSharedPreferences("RefreshPopulation", MODE_PRIVATE).edit();
////                    editor.putBoolean("is24HourPassed", true);
////                    editor.apply();
////                    editor.commit();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

        init(view);
        return view;
    }

    private void hitMeroPalikaService() {
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("RefreshPopulation", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("is24HourPassed", true)) {

            SharedPreferences.Editor editor = getActivity().getSharedPreferences("RefreshPopulation", MODE_PRIVATE).edit();
            editor.putBoolean("is24HourPassed", false);
            editor.apply();
            editor.commit();
            getActivity().startService(serviceIntent);
            bindService();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isApplicationOpenFirstTime) {
            getPopulationByDate(getDate());
            serviceIntent.putExtra("population",tdPop);
            getActivity().startService(serviceIntent);
            bindService();
            isApplicationOpenFirstTime = false;
        }
        getActivity().registerReceiver(receiver, new IntentFilter("livePopulation"));
    }

    @Override
    public void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }

    private void init(View view) {
        mSlider = view.findViewById(R.id.home_slider);
        TextSliderView textSliderView2 = new TextSliderView(getContext());
        TextSliderView textSliderView3 = new TextSliderView(getContext());
        textSliderView2.description("").image(R.drawable.slider2);
        textSliderView3.description("").image(R.drawable.slider3);
        mSlider.addSlider(textSliderView2);
        mSlider.addSlider(textSliderView3);
        popToday = view.findViewById(R.id.home_today_population);
        popTomorrow = view.findViewById(R.id.home_tomorrow_population);
        serviceIntent = new Intent(getActivity().getApplicationContext(), MeroPalikaService.class);
        frameLayout=view.findViewById(R.id.home_frame_layout);
        process();
    }

    private void process() {

    }

    private void bindService() {
        if (this.serviceConnection == null) {
            this.serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MeroPalikaService.MeroPalikaServiceBinder meroPalikaServiceBinder = (MeroPalikaService.MeroPalikaServiceBinder) service;
                    meroPalikaService = meroPalikaServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };

        }

        getActivity().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        if (isServiceBound) {
            getActivity().unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    public void setPopulation() {
        popToday.setText(String.valueOf(todayPopulations));
    }
    public void setPopulationTomorrow(){
        popTomorrow.setText(String.valueOf(tomorrowPopulation));
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            todayPopulations = intent.getIntExtra("todayPopulation", 0);
          //  tomorrowPopulation=intent.getIntExtra("tomorrowPopulation",0);
            // Toast.makeText(getActivity(), "Total Population"+ todayPopulations, Toast.LENGTH_SHORT).show();
            Log.d("Under BroadCast===", todayPopulations.toString());
            setPopulation();
            //setPopulationTomorrow();

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter("livePopulation"));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private void getPopulationByDate(String parameter) {
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = makeFinalUrl("http://api.population.io:80/1.0/population/Nepal/", parameter);

        CacheRequest cacheRequest = new CacheRequest(GET, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("total_population");

                    String date = jsonObject1.getString("date");
                    String population = jsonObject1.getString("population");
                    Log.d("Pop============", population + ", " + date);
                    if (date.equals(getDate())) {
                      tdPop = Integer.valueOf(population);
                        popToday.setText(population);
                    } else {
                       popTomorrow.setText(population);
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GloballyCommon.checkErrorResponse(frameLayout,getContext());
                // Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(cacheRequest);

        //End of Caching

    }

    private String getDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateformat.format(c.getTime());
        Log.d("CurrentDate:::", date);

        return date;
    }

    private String getTomorrowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(getDate()));
            c.add(Calendar.DATE, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date resultdate = new Date(c.getTimeInMillis());
        Log.d("Tomorrow Date::", sdf.format(resultdate));
        return sdf.format(resultdate);
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
