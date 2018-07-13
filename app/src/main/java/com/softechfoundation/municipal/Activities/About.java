package com.softechfoundation.municipal.Activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.softechfoundation.municipal.R;

public class About extends AppCompatActivity {
private TextView phone1, phone2, phone3;
private WebView aboutMeroPalika;
private TextView aboutAddress;
private ImageView aboutLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("About");

        phone1=findViewById(R.id.about_phone1);
        phone2=findViewById(R.id.about_phone2);
        phone3=findViewById(R.id.about_phone3);
        aboutMeroPalika=findViewById(R.id.about_app_intro);
        aboutAddress=findViewById(R.id.about_location);
        aboutLogo=findViewById(R.id.about_softech_logo);

        aboutMeroPalika.setVerticalScrollBarEnabled(false);
        aboutMeroPalika.loadData(getString(R.string.mero_palika_about), "text/html; charset=utf-8", "utf-8");

        phone1.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        phone1.setText("+977014435890");
        phone1.setPaintFlags(phone1.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));

        phone2.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        phone2.setText("+977014435890");
        phone3.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        phone3.setText("+977014435890");

        aboutAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geoIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=Softech Foundation Pvt.Ltd,Sahabhagita Marga, Kathmandu 44600"));
                startActivity(geoIntent);
            }
        });

        aboutLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.softechfoundation.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
