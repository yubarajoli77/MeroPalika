package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.softechfoundation.municipal.GloballyCommon;
import com.softechfoundation.municipal.ImageSlider.ImageModel;
import com.softechfoundation.municipal.ImageSlider.SlidingImage_Adapter;
import com.softechfoundation.municipal.Pojos.PicturePojo;
import com.softechfoundation.municipal.R;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ResourcesAndServicesDetail extends AppCompatActivity {
    private TextView name, description, lessBtn, more;
    private ImageView address, phone;
    private View longDescriptionContainer;
    private WebView longDescription;


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private List<Bitmap> rsImageList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources_and_services_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.rs_detail_name);
        address = findViewById(R.id.rs_detail_location);
        phone = findViewById(R.id.rs_detail_call);
        description = findViewById(R.id.rs_description);
        longDescription = findViewById(R.id.rs_long_description);
        more = findViewById(R.id.rs_more_expand);
        longDescriptionContainer = findViewById(R.id.rs_long_description_container);
        lessBtn = findViewById(R.id.rs_less);

        Intent intent = getIntent();
        final String rsName = intent.getStringExtra("name");
        final String rsAddress = intent.getStringExtra("location");
        final String rsPhone = intent.getStringExtra("phone");
        final List<PicturePojo> rsImage = GloballyCommon.getInstance().getPic();
        final String rsType=intent.getStringExtra("rsType");
        final String rsDescription = GloballyCommon.getInstance().getDescription();

        Log.d("Description::", rsDescription);
        if (rsDescription != null) {
            description.setText(rsDescription);
            String htmlDescription=GloballyCommon.convertIntoHtml(rsDescription);
            longDescription.setVerticalScrollBarEnabled(false);
            longDescription.loadData(htmlDescription, "text/html; charset=utf-8", "utf-8");
        } else {
            description.setText("There is no description yet");
            String htmlDescription=GloballyCommon.convertIntoHtml("There is no description available yet");

            longDescription.setVerticalScrollBarEnabled(false);
            longDescription.loadData(htmlDescription, "text/html; charset=utf-8", "utf-8");
        }

        if (rsImage == null || rsImage.isEmpty()) {

            if("hospital".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.hotel_default_pic);
                rsImageList.add(image);
            }else if("bloodBank".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.blood_bank_default_pic);
                rsImageList.add(image);
            }else if("atm".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.atm_default_pic);
                rsImageList.add(image);
            }else if("policeStation".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.police_station_default_pic);
                rsImageList.add(image);
            }else if("mountain".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.mountain_default_pic);
                rsImageList.add(image);
            }else if("lake".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.lake_default_pic);
                rsImageList.add(image);
            }else if("waterfall".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.waterfall_default_pic);
                rsImageList.add(image);
            }else if("protectedArea".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.protected_area_default_pic);
                rsImageList.add(image);
            }else if("academicInsti".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.educational_insti_default_pic);
                rsImageList.add(image);
            }else if("airport".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.airport_default_pic);
                rsImageList.add(image);
            }else if("industry".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.industry_default_pic);
                rsImageList.add(image);
            }else if("hydropower".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.hydropower_default_pic);
                rsImageList.add(image);
            }else if("hotel".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.hotel_default_pic);
                Bitmap image1=BitmapFactory.decodeResource(getResources(),R.drawable.hotel_default_pic1);
                rsImageList.add(image);
                rsImageList.add(image1);
            }else if("mainAttraction".equals(rsType)){
                Bitmap image=BitmapFactory.decodeResource(getResources(),R.drawable.protected_area_default_pic);
                rsImageList.add(image);
            }

        }else{
            for(PicturePojo picturePojo: rsImage){
                Log.d("rsImage",picturePojo.getPicture());
                byte[] decodedString = Base64.decode(picturePojo.getPicture(), Base64.NO_WRAP);
                InputStream inputStream  = new ByteArrayInputStream(decodedString);
                Bitmap image  = BitmapFactory.decodeStream(inputStream);
                rsImageList.add(image);
            }
        }

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(ResourcesAndServicesDetail.this,imageModelArrayList));
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setFillColor(getResources().getColor(R.color.colorPrimary));
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);

            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 40000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


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
                    longDescriptionContainer.setVisibility(View.VISIBLE);
                    description.setVisibility(View.GONE);
                    lessBtn.setVisibility(View.VISIBLE);
                    more.setVisibility(View.GONE);
            }
        });
        lessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                longDescriptionContainer.setVisibility(View.GONE);
                description.setVisibility(View.VISIBLE);
                lessBtn.setVisibility(View.GONE);
                more.setVisibility(View.VISIBLE);
            }
        });


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

    private ArrayList<ImageModel> populateList(){
        Bitmap[] imageArray= rsImageList.toArray(new Bitmap[rsImageList.size()]);
        ArrayList<ImageModel> list = new ArrayList<>();

        Log.d("size of arraylist",String.valueOf(rsImageList.size()));
        for(int i = 0; i < imageArray.length; i++){
            Log.d("Inside loop",String.valueOf(imageArray.length));
            ImageModel imageModel = new ImageModel();
            imageModel.setSliderImages(imageArray[i]);
            list.add(imageModel);
        }

        return list;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
