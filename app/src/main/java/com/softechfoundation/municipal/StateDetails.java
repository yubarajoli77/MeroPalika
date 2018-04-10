package com.softechfoundation.municipal;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class StateDetails extends AppCompatActivity {
private Button mountain,hospital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("More About State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        hospital=findViewById(R.id.sub_btn_hospital);
        mountain=findViewById(R.id.sub_btn_mountains);
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StateDetails.this,ListOfServicesAndResources.class);
                intent.putExtra("catagory","SERVICES");
                startActivity(intent);

            }
        });
        mountain.setOnClickListener(new View.OnClickListener() {
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
