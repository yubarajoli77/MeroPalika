package com.softechfoundation.municipal.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

import com.softechfoundation.municipal.R;

public class About extends AppCompatActivity {
private TextView phone1, phone2, phone3;
private TextView aboutMeroPalika;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        phone1=findViewById(R.id.about_phone1);
        phone2=findViewById(R.id.about_phone2);
        phone3=findViewById(R.id.about_phone3);
        aboutMeroPalika=findViewById(R.id.about_app_intro);

        phone1.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        phone1.setText("+977014435890");
        phone2.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        phone2.setText("+977014435890");
        phone3.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        phone3.setText("+977014435890");

    }
}
