package com.softechfoundation.municipal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jh.circularlist.CircularListView;
import com.jh.circularlist.CircularTouchListener;

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
        for(int i = 0 ; i < 7 ; i ++){
            itemTitles.add(String.valueOf(i));
        }

        final CircularListView circularListView = (CircularListView) view.findViewById(R.id.circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), itemTitles);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(120);

        circularListView.setOnItemClickListener(new CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
             //   Toast.makeText(getActivity(), "You clicked me "+index, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),StateDetails.class);
                startActivity(intent);
                // adapter.removeItemAt(index);

            }
        });
    }


    }
