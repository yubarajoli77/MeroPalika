package com.softechfoundation.municipal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.jh.circularlist.CircularListView;
import com.jh.circularlist.CircularTouchListener;

import java.util.ArrayList;

public class MoreInfo extends AppCompatActivity {
private Button mAdd,mIncreaseRadius,mDecreaseRadius;
    private ExpandableRelativeLayout expandableLayout1;
    private CircularItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

//        mAdd=findViewById(R.id.add_member);
//        mIncreaseRadius=findViewById(R.id.increase_radius);
//        mDecreaseRadius=findViewById(R.id.decrease_radius);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ArrayList<String> itemTitles = new ArrayList<>();
        for(int i = 0 ; i < 7 ; i ++){
            itemTitles.add(String.valueOf(i));
        }

        final CircularListView circularListView = (CircularListView) findViewById(R.id.my_circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), itemTitles);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(120);

        circularListView.setOnItemClickListener(new CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
               // adapter.removeItemAt(index);

            }
        });


//       mAdd.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//                add to list
//                inflate a layout
//               View view = getLayoutInflater().inflate(R.layout.view_circular_item, null);
//
//               TextView itemView = (TextView) view.findViewById(R.id.btn_item);
//
//               itemView.setText(String.valueOf(adapter.getCount() + 1));
//               itemView.setBackgroundColor(getResources().getColor(R.color.teal));
//               itemView.setTextColor(getResources().getColor(R.color.white));
//               itemView.setWidth(100);
//               itemView.setHeight(100);
//
//               adapter.addItem(view);
//
//
//           }
//       });

//       mIncreaseRadius.setOnClickListener(new View.OnClickListener() {
//
//           @Override
//           public void onClick(View v) {
//               circularListView.setRadius(circularListView.radius += 15);
//           }
//       });
//
//        mDecreaseRadius.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                circularListView.setRadius(circularListView.radius -= 15);
//            }
//        });

        CircularItemAdapter.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                expandableLayout1 = findViewById(R.id.expandableLayout);
//                expandableLayout1.toggle(); // toggle expand and collapse
            }
        });
    }

}
