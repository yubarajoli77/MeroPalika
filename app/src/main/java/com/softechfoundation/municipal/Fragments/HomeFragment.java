package com.softechfoundation.municipal.Fragments;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.backgroundService.MeroPalikaService;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isApplicationOpenFirstTime) {
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
            tomorrowPopulation=intent.getIntExtra("tomorrowPopulation",0);
            // Toast.makeText(getActivity(), "Total Population"+ todayPopulations, Toast.LENGTH_SHORT).show();
            Log.d("Under BroadCast===", todayPopulations.toString());
            setPopulation();
            setPopulationTomorrow();

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
}
