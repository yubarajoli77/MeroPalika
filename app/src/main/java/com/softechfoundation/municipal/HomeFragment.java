package com.softechfoundation.municipal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private SliderLayout mSlider;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStop() {
        mSlider.stopAutoCycle();
        super.onStop();
    }
    private void init(View view) {
        mSlider = view.findViewById(R.id.home_slider);
        TextSliderView textSliderView1 = new TextSliderView(getContext());
        TextSliderView textSliderView2 = new TextSliderView(getContext());
        TextSliderView textSliderView3 = new TextSliderView(getContext());
        textSliderView1.description("Description").image(R.drawable.bg_top);
        textSliderView2.description("").image(R.drawable.stone_gray);
        textSliderView3.description("").image(R.drawable.ux);
        mSlider.addSlider(textSliderView1);
        mSlider.addSlider(textSliderView2);
        mSlider.addSlider(textSliderView3);

    }

}
