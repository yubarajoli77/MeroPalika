package com.softechfoundation.municipal.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.softechfoundation.municipal.GloballyCommon;
import com.softechfoundation.municipal.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MinistryProfileDetail extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private AppBarLayout appBarLayout;
    private ImageView ministerPicIv, ministerFacebookIv, ministerTwitterIv;
    private TextView ministerNameTv, ministerPartyTv;
    private Button ministerCallBtn, ministerMailBtn;
    private WebView ministryDescriptionWv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ministry_profile_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        defineView();
        doExecute();
    }

    private void defineView() {
        relativeLayout=findViewById(R.id.ministry_profile_detail_relative_layout);
        appBarLayout=findViewById(R.id.ministry_profile_detail_appbar_layout);
        ministerPicIv=findViewById(R.id.ministry_profile_detail_pic);
        ministerFacebookIv=findViewById(R.id.ministry_profile_detail_facebook);
        ministerTwitterIv=findViewById(R.id.ministry_profile_detail_twitter);
        ministerNameTv=findViewById(R.id.ministry_profile_detail_name);
        ministerPartyTv=findViewById(R.id.ministry_profile_detail_party);
        ministerCallBtn=findViewById(R.id.ministry_profile_detail_call);
        ministerMailBtn=findViewById(R.id.ministry_profile_detail_mail);
        ministryDescriptionWv=findViewById(R.id.ministry_profile_detail_description);
    }

    private void doExecute(){
        Intent intent=getIntent();
        String ministry=intent.getStringExtra("ministry");
        String ministerName=intent.getStringExtra("ministerName");
        String ministerParty=intent.getStringExtra("ministerParty");
        String ministerImage=intent.getStringExtra("ministerPic");
        final String ministerEmail=intent.getStringExtra("ministerEmail");
        final String ministerPhone=intent.getStringExtra("ministerPhone");
        final String ministerFacebook=intent.getStringExtra("ministerFacebook");
        final String ministerTwitter=intent.getStringExtra("ministerTwitter");
        String ministryDescription=intent.getStringExtra("ministryDescription");

        getSupportActionBar().setTitle(ministry);

        //Setting Image after decoding
        if(ministerImage==null || ministerImage.length()<100){
            ministerPicIv.setImageResource(R.drawable.user_derault_pic);

        }else{
            byte[] decodedString = Base64.decode(ministerImage, Base64.NO_WRAP);
            InputStream inputStream  = new ByteArrayInputStream(decodedString);
            Bitmap image  = BitmapFactory.decodeStream(inputStream);
            ministerPicIv.setImageBitmap(image);
        }

        ministerNameTv.setText(ministerName);
        ministerPartyTv.setText(ministerParty);

        Log.d("value:::", ministerEmail+ ministerPhone+ ministerFacebook);
        //Phone btn action
        if(ministerPhone.equals("null")){
            ministerCallBtn.setClickable(false);
            ministerCallBtn.setEnabled(false);
        }else{
            ministerCallBtn.setClickable(true);
            ministerCallBtn.setEnabled(true);
            ministerCallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callNumber(ministerPhone);
                }
            });

        }


        //Mail btn action
        if(ministerEmail.equals("null")){
            ministerMailBtn.setClickable(false);
            ministerMailBtn.setEnabled(false);
        }else{
            ministerMailBtn.setClickable(true);
            ministerMailBtn.setEnabled(true);
            ministerMailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMail(ministerEmail);
                }
            });

        }


        //Facebook btn Click
        if(ministerFacebook.equals("null")){
            ministerFacebookIv.setImageResource(R.drawable.disabled_facebook_32dp);
        }else {

            ministerFacebookIv.setImageResource(R.drawable.facebook_32dp);
            ministerFacebookIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(ministerFacebook);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }


        //Twitter btn click
        if(ministerTwitter.equals("null")){
            ministerTwitterIv.setImageResource(R.drawable.disabled_twitter_32dp);

        }else {
            ministerTwitterIv.setImageResource(R.drawable.twitter_32dp);
            ministerTwitterIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(ministerTwitter);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }


        if (ministryDescription.equals("null")) {
            String htmlDescription=GloballyCommon.convertIntoHtml("There is no ministry description available yet");

            ministryDescriptionWv.setVerticalScrollBarEnabled(false);
            ministryDescriptionWv.loadData(htmlDescription, "text/html; charset=utf-8", "utf-8");

        } else {
            String htmlMinistryDescription= GloballyCommon.convertIntoHtml(ministryDescription);
            ministryDescriptionWv.setVerticalScrollBarEnabled(false);
            ministryDescriptionWv.loadData(htmlMinistryDescription, "text/html; charset=utf-8", "utf-8");
        }
    }


    public void callNumber(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL); //use ACTION_CALL for direct call
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:"+phone));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                    new String[]{android.Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                getApplicationContext().startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void sendMail(String receiver) {

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String[] recipients = new String[]{receiver};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mail from one of your citizen");
        emailIntent.setType("text/plain");

        startActivity(Intent.createChooser(emailIntent, "Select mailing application"));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
