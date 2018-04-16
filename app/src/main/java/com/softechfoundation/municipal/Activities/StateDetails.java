package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softechfoundation.municipal.R;

public class StateDetails extends AppCompatActivity {
private LinearLayout naturalResources;
private TextView stateName,capitalName,area,population,density;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("More About State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        stateName=findViewById(R.id.detail_top_state_name);
        capitalName=findViewById(R.id.detail_top_capital_name);
        area=findViewById(R.id.area);
        population=findViewById(R.id.population);
        density=findViewById(R.id.density);

        Intent intentGetValue=getIntent();
        String state=intentGetValue.getStringExtra("stateName");
        stateName.setText(state);
        String capitalCity=intentGetValue.getStringExtra("capital");
        capitalName.setText(capitalCity);
        area.setText(intentGetValue.getStringExtra("area"));
        population.setText(intentGetValue.getStringExtra("population"));
        density.setText(intentGetValue.getStringExtra("density"));

        naturalResources=findViewById(R.id.natural_resource_layout);
        naturalResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","RESOURCES");
                startActivity(intent);

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
