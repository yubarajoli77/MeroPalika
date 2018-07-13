package com.softechfoundation.municipal.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softechfoundation.municipal.Adapters.ViewpagerAdapter;
import com.softechfoundation.municipal.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MinistryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewpagerAdapter adapter;
    public static View loading;

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
        adapter=new ViewpagerAdapter(getFragmentManager());
        adapter.AddFragment(new CentralMinistryFragment(),"Central");
        adapter.AddFragment(new State1MinistryFragment(),"State 1");
        adapter.AddFragment(new State2MinistryFragment(),"State 2");
        adapter.AddFragment(new State3MinistryFragment(),"State 3");
        adapter.AddFragment(new State4MinistryFragment(),"State 4");
        adapter.AddFragment(new State5MinistryFragment(),"State 5");
        adapter.AddFragment(new State6MinistryFragment(),"State 6");
        adapter.AddFragment(new State7MinistryFragment(),"State 7");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void defineView(View view) {
        loading=view.findViewById(R.id.dotted_loading);
        tabLayout=view.findViewById(R.id.tablayout);
        viewPager=view.findViewById(R.id.view_pager);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
