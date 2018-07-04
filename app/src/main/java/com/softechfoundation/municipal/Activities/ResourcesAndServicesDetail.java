package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.softechfoundation.municipal.GloballyCommon;
import com.softechfoundation.municipal.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResourcesAndServicesDetail extends AppCompatActivity {
    private TextView name, description, lessBtn, more;
    private ImageView address, phone;
    private boolean isExpanded;
    private View longDescriptionContainer;
    private SliderLayout mSlider;
    private TextView longDescription;
    private ImageView detailImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_and_services_detail);


        name = findViewById(R.id.rs_detail_name);
        address = findViewById(R.id.rs_detail_location);
        phone = findViewById(R.id.rs_detail_call);
        description = findViewById(R.id.rs_description);
        longDescription = findViewById(R.id.rs_long_description);
        more = findViewById(R.id.rs_more_expand);
        longDescriptionContainer = findViewById(R.id.rs_long_description_container);
        lessBtn = findViewById(R.id.rs_less);
        detailImageView = findViewById(R.id.service_resource_detail_pic);
//        mSlider = findViewById(R.id.service_resource_image_slider);

        Intent intent = getIntent();
        final String rsName = intent.getStringExtra("name");
        final String rsAddress = intent.getStringExtra("location");
        final String rsPhone = intent.getStringExtra("phone");
        final String rsImage = GloballyCommon.getInstance().getPic();
        final String rsType=intent.getStringExtra("rsType");
        final String rsDescription = GloballyCommon.getInstance().getDescription();

        Log.d("Description::", rsDescription);
        if (rsDescription != null) {
            description.setText(rsDescription);
            longDescription.setText(rsDescription);
        } else {
            description.setText("There is no description yet");
            longDescription.setText("There is no description yet");
        }

        Log.d("PreImage::::", rsImage);
//        convertImageToFileAndLoadInSlider(rsImage);

        if (rsImage != null) {
            if (rsImage.length() < 100) {
                if("hospital".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.hospital_default_pic)
                            .into(detailImageView);
                }else if("bloodBank".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.blood_bank_default_pic)
                            .into(detailImageView);
                }else if("atm".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.atm_default_pic)
                            .into(detailImageView);
                }else if("policeStation".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.police_station_default_pic)
                            .into(detailImageView);
                }else if("mountain".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.mountain_default_pic)
                            .into(detailImageView);
                }else if("lake".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.lake_default_pic)
                            .into(detailImageView);
                }else if("waterfall".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.waterfall_default_pic)
                            .into(detailImageView);
                }else if("protectedArea".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.protected_area_default_pic)
                            .into(detailImageView);
                }else if("academicInsti".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.educational_insti_default_pic)
                            .into(detailImageView);
                }else if("airport".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.airport_default_pic)
                            .into(detailImageView);
                }else if("industry".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.industry_default_pic)
                            .into(detailImageView);
                }else if("hydropower".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.hydropower_default_pic)
                            .into(detailImageView);
                }else if("mainAttraction".equals(rsType)){
                    Glide.with(getApplicationContext())
                            .load(R.drawable.mountain_default_pic)
                            .into(detailImageView);
                }
            } else {
                byte[] decodedString = Base64.decode(rsImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Glide.with(getApplicationContext())
                        .load(decodedByte)
                        .into(detailImageView);
            }
        }
        phone.setEnabled(true);
        phone.setVisibility(View.VISIBLE);

        if ("NOPHONE".equals(rsPhone)) {
            phone.setVisibility(View.GONE);
        }
        if ("".equals(rsPhone)) {
            phone.setEnabled(false);
            phone.setVisibility(View.GONE);

        }
        if (null == rsPhone) {
            phone.setEnabled(false);
            phone.setVisibility(View.GONE);

        }


        name.setText(rsName);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResourcesAndServicesDetail.this, ShowItemInMap.class);
                intent.putExtra("location", rsAddress);
                intent.putExtra("name", rsName);
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
                if (!isExpanded) {
                    longDescriptionContainer.setVisibility(View.VISIBLE);
                    more.setText("Less...");
//                    mSlider.setVisibility(View.GONE);
                    detailImageView.setVisibility(View.GONE);
                    description.setVisibility(View.GONE);
                    lessBtn.setVisibility(View.VISIBLE);
                    isExpanded = true;
                } else if (isExpanded) {
                    longDescriptionContainer.setVisibility(View.GONE);
                    more.setText("More...");
//                    mSlider.setVisibility(View.VISIBLE);
                    detailImageView.setVisibility(View.VISIBLE);
                    description.setVisibility(View.VISIBLE);
//                    convertImageToFileAndLoadInSlider(GloballyCommon.getInstance().getPic());
                    isExpanded = false;
                }
            }
        });
        lessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longDescriptionContainer.setVisibility(View.GONE);
                more.setText("More...");
//                mSlider.setVisibility(View.VISIBLE);
                detailImageView.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
                lessBtn.setVisibility(View.GONE);
                isExpanded = false;
            }
        });


//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_images);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);
//
//        ImageGalleryAdapter adapter = new ImageGalleryAdapter(this, RSImageGalleryPojo.getRSPhotos());
//        recyclerView.setAdapter(adapter);

    }

    private void convertImageToFileAndLoadInSlider(String rsImage) {
        Log.d("Image::::", rsImage);
        TextSliderView textSliderView1 = new TextSliderView(getApplicationContext());

        if (rsImage != null) {
            if (rsImage.length() < 100) {
                int f = R.drawable.slider2;
                textSliderView1.description("").image(f);
            } else {
                byte[] decodedString = Base64.decode(rsImage, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                //converting bitmap to file object
                //create a file to write bitmap data
                File f = new File(getApplicationContext().getCacheDir(), "sliderImage");
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textSliderView1.description("").image(f);

            }


        } else {
            int f = R.drawable.slider3;
            textSliderView1.description("").image(f);
        }
        mSlider.addSlider(textSliderView1);
    }

    public void callNumber(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL); //use ACTION_CALL for direct call
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + phone));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        } else {     //have got permission
            try {
                this.startActivity(callIntent);  //call activity and make phone call
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "yourActivity is not founded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
