package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.softechfoundation.municipal.Adapters.ImageGalleryAdapter;
import com.softechfoundation.municipal.Pojos.RSImageGalleryPojo;
import com.softechfoundation.municipal.R;

public class ResourcesAndServicesDetail extends AppCompatActivity {
    private TextView name,description,more;
    private ImageView address,phone;
    private boolean isExpanded;
    private View longDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_and_services_detail);


        name=findViewById(R.id.rs_detail_name);
        address=findViewById(R.id.rs_detail_location);
        phone=findViewById(R.id.rs_detail_call);
        description=findViewById(R.id.rs_description);
        more=findViewById(R.id.rs_more_expand);
        longDescription=findViewById(R.id.rs_long_description);

        Intent intent=getIntent();
        final String rsName=intent.getStringExtra("name");
        final String rsAddress=intent.getStringExtra("location");
        final String rsPhone=intent.getStringExtra("phone");
        phone.setEnabled(true);
        phone.setVisibility(View.VISIBLE);

        if("NOPHONE".equals(rsPhone)){
            phone.setVisibility(View.GONE);
        }
        if("".equals(rsPhone)){
            phone.setEnabled(false);
        }
        if(null==rsPhone){
            phone.setEnabled(false);
        }


        name.setText(rsName);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ResourcesAndServicesDetail.this, ShowItemInMap.class);
                intent.putExtra("location",rsAddress);
                intent.putExtra("name",rsName);
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(rsPhone);
            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isExpanded){
                    longDescription.setVisibility(View.VISIBLE);
                    more.setText("Less...");
                    isExpanded=true;
                }
                else if(isExpanded){
                    longDescription.setVisibility(View.GONE);
                    more.setText("More...");
                    isExpanded=false;
                }
            }
        });


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, RSImageGalleryPojo.getRSPhotos());
        recyclerView.setAdapter(adapter);

    }

    public void callNumber(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL); //use ACTION_CALL for direct call
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:"+phone));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                this.startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(this,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
