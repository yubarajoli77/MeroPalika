package com.softechfoundation.municipal.backgroundService;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.softechfoundation.municipal.Activities.ListOfServicesAndResources;
import com.softechfoundation.municipal.Adapters.ResourceCustomAdapter;
import com.softechfoundation.municipal.Fragments.HomeFragment;
import com.softechfoundation.municipal.Pojos.PopulationPojo;
import com.softechfoundation.municipal.Pojos.ResourcePojo;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 4/18/2018.
 */

public class MeroPalikaService extends Service {
    private boolean isRandomNumberGeneratorOn;
    private int totalPop;
    private int tomorrow;

    public class MeroPalikaServiceBinder extends Binder {
        public MeroPalikaService getService() {
            return MeroPalikaService.this;
        }

    }

    private IBinder mBinder = new MeroPalikaServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopRandomNumberGenerator();
//        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRandomNumberGeneratorOn = true;
     getPopulationByDate(getDate());
        new Thread(new Runnable() {
            @Override
            public void run() {
                calculateTodayPopulation();
            }
        }).start();
        return START_STICKY;

    }

    private void getPopulationByDate(String parameter) {
        //Start Caching
        RequestQueue queue = Volley.newRequestQueue(this);
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
                        totalPop = Integer.valueOf(population);
                    } else {
                        tomorrow = Integer.valueOf(population);
                    }
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

    private String getDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateformat.format(c.getTime());
        Log.d("CurrentDate:::", date);

        return date;
    }
//
//    private String getTomorrowDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c = Calendar.getInstance();
//        try {
//            c.setTime(sdf.parse(getDate()));
//            c.add(Calendar.DATE, 1);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date resultdate = new Date(c.getTimeInMillis());
//        Log.d("Tomorrow Date::", sdf.format(resultdate));
//        return sdf.format(resultdate);
//    }

    public int getRandomNumber() {
        Random r = new Random();
        int MAX = 9458;
        int MIN = 9452;
        int randomNo = r.nextInt((MAX - MIN) + 1) + MIN;
        return (randomNo * 10);
    }

    public int getTotalPop() {
        return totalPop;
    }

    public int getTomorrow() {
        return tomorrow;
    }

    private void stopRandomNumberGenerator() {
        isRandomNumberGeneratorOn = false;
    }

    private void calculateTodayPopulation() {

        while (isRandomNumberGeneratorOn) {
            try {
                Thread.sleep(getRandomNumber());
                totalPop = totalPop + 1;
                Intent intent1 = new Intent("livePopulation");
                intent1.putExtra("todayPopulation", getTotalPop());
                intent1.putExtra("tomorrowPopulation", getTomorrow());
                sendBroadcast(intent1);
                Log.i("Thread Log: ", "ThreadId:" + Thread.currentThread().getId());
                Log.i("Live Birth Log: ", "Birth Number:" + getTotalPop() + ", " + getRandomNumber());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopSelf();
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
