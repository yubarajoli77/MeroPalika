package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softechfoundation.municipal.R;

import java.util.Locale;

public class StateDetails extends AppCompatActivity{
    private LinearLayout naturalResources,infrastructure,mainAttraction,urgentServices,avaliableContacts;
private TextView stateName,capitalName,area,population,density,governer,chiefMinister,website;
private  CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout=findViewById(R.id.state_detail_collapsing_tool_bar_layout);
        stateName=findViewById(R.id.detail_top_state_name);
        capitalName=findViewById(R.id.detail_top_capital_name);
        area=findViewById(R.id.area);
        population=findViewById(R.id.population);
        density=findViewById(R.id.density);
//        governer=findViewById(R.id.detail_top_state_governer);
//        website=findViewById(R.id.detail_top_state_website);
        chiefMinister=findViewById(R.id.detail_top_state_chiefMinister);

        avaliableContacts=findViewById(R.id.available_contacts_layout);
        naturalResources=findViewById(R.id.natural_resource_layout);
        infrastructure=findViewById(R.id.infrastructure_layout);
        mainAttraction=findViewById(R.id.main_attraction_layout);
        urgentServices=findViewById(R.id.urgent_services_layout);



        Intent intentGetValue=getIntent();
        String gorvernerName=intentGetValue.getStringExtra("governer");
        String chiefMinisterName=intentGetValue.getStringExtra("chiefMinister");
        String websiteName=intentGetValue.getStringExtra("website");
        final String state=intentGetValue.getStringExtra("stateName");
        stateName.setText(state);
        String capitalCity=intentGetValue.getStringExtra("capital");
        capitalName.setText(capitalCity);
        area.setText(intentGetValue.getStringExtra("area"));
        population.setText(intentGetValue.getStringExtra("population"));
        density.setText(intentGetValue.getStringExtra("density"));
        chiefMinister.setText(chiefMinisterName);
//        getSupportActionBar().setTitle("Detail about"+" "+state);
        collapsingToolbarLayout.setTitle("Detail about"+" "+state);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));


        naturalResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","RESOURCES");
                intent.putExtra("state",state);
                startActivity(intent);

            }
        });

        infrastructure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","INFRASTRUCTURES");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });

        urgentServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","URGENTSERVICES");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });

        mainAttraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","MAINATTRACTIONS");
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });

        avaliableContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","AVALIABLECONTACTS");
                intent.putExtra("state",state);
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