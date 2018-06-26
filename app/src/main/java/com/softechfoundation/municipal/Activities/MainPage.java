package com.softechfoundation.municipal.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.softechfoundation.municipal.BuildConfig;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Fragments.HomeFragment;
import com.softechfoundation.municipal.Fragments.MainFragment;
import com.softechfoundation.municipal.Fragments.MinistryFragment;
import com.softechfoundation.municipal.Fragments.SeperateDetailMapFragment;
import com.softechfoundation.municipal.Fragments.StateFragment;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.android.volley.Request.Method.GET;

public class MainPage extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final int ACT_CHECK_TTS_DATA = 1001;
    private static BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    TapTargetSequence sequence;
    private boolean isTutorialAlreadyShown;
    private boolean isHomeFirstTime = true;
    public static Button readMessage;
    private TextToSpeech tts=null;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        requestWritePermission();
        fragment = null;

        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottom_nav_home, R.drawable.ic_home_black_24dp, R.color.bottn_nav_color);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_nav_new_place_mapping, R.drawable.place_mapping, R.color.blue);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottom_nav_state_detail, R.drawable.state_detail, R.color.yellow);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.bottom_nav_map_gallery, R.drawable.ic_collections_black_24dp, R.color.red);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.bottom_nav_ministry, R.drawable.ic_group_black_24dp, R.color.green);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.botton_nav_bg_color));
       bottomNavigation.setBehaviorTranslationEnabled(false);
        //bottomNavigation.setColored(true);
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimaryDark));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.black));




        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
           @Override
           public boolean onTabSelected(int position, boolean wasSelected) {
               if(position==0){
                   getSupportActionBar().setTitle("MeroPalika");
                   loadFragment("HOME");
                   return true;
               }
               if(position==1){
                   requestGpsPermissions();
                   getSupportActionBar().setTitle("Place Mapping");
                   loadFragment("PLACEMAPPING");
                   return true;
               }
               if(position==2){
                   getSupportActionBar().setTitle("State Info");
                   loadFragment("STATEINFO");
                   return true;
               }
               if(position==3){
                   getSupportActionBar().setTitle("Map Gallery");
                   loadFragment("MAPGALLERY");
                   return  true;
               }
               if(position==4){
                   getSupportActionBar().setTitle("Current Ministry");
                   loadFragment("MINISTRY");
                   return true;
               }

               return false;
           }
       });

        final Display display = getWindowManager().getDefaultDisplay();

        // Load our little droid guy

        final Drawable droid = ContextCompat.getDrawable(this, R.drawable.ic_home_black_24dp);

        // Tell our droid buddy where we want him to appear

        final Rect droidTarget = new Rect(0, 0, droid.getIntrinsicWidth(), droid.getIntrinsicHeight() * display.getHeight());

        // Using deprecated methods makes you look way cool
        droidTarget.offset(display.getWidth(), display.getHeight() /display.getHeight());


        bottomNavigationView = findViewById(R.id.bottom_navigation2);
       // BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
       // bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        readMessage= findViewById(R.id.readMessage);
        SharedPreferences sharedPreferences=getSharedPreferences("CheckTutorialFirstTime",MODE_PRIVATE);
        isTutorialAlreadyShown=sharedPreferences.getBoolean("isTutorialAlreadyShown",false);
        if(!isTutorialAlreadyShown){
            helpUser();
            sequence.start();
            SharedPreferences.Editor editor=getSharedPreferences("CheckTutorialFirstTime",MODE_PRIVATE).edit();
            editor.putBoolean("isTutorialAlreadyShown",true);
            editor.apply();
            editor.commit();
        }

        //for text to speech intent
        Intent ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent, ACT_CHECK_TTS_DATA);


        readMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("TTSMessage", MODE_PRIVATE);
                String message = prefs.getString("message", "");

                SharedPreferences sharedPreferences = getSharedPreferences("Setting", MODE_PRIVATE);
                boolean onOrOff=sharedPreferences.getBoolean("switchState",true);
                float speed=sharedPreferences.getFloat("speed", (float) 1.0);
                float pitch=sharedPreferences.getFloat("pitch", (float) 1.0);

                if(onOrOff){
                    if(!(message.equals(""))){
                        //tts = new TextToSpeech(StateDetails.this,this);
                        tts.setLanguage(Locale.US);
                        tts.speak(message, TextToSpeech.QUEUE_ADD, null);
                        tts.setPitch(pitch);
                        tts.setSpeechRate(speed);
                    }
                }

            }
        });


        checkAppUpdate("fromAppStart");

    }

    private void checkAppUpdate(final String from) {
        final String currentVersion=getCurrentVersion();
        final String[] latestVersion = new String[1];
        //Start Caching
        String url = CommonUrl.BaseUrl2+"versions";
        Log.d("VersionURL: ",url);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                latestVersion[0] = response;
                Log.d("latestVersion::", latestVersion[0]);

                if(Float.valueOf(latestVersion[0])>Float.valueOf(currentVersion)){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                    builder.setTitle("An Update is Available");
                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Click button action
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.softechfoundation.municipal")));
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Cancel button action
                        }
                    });

                    builder.setCancelable(false);
                    builder.show();
                    return;
                }
                if("fromMenu".equals(from)){
                    Toast.makeText(MainPage.this, "Mero Palika is upto date", Toast.LENGTH_SHORT).show();
                }

                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                requestQueue.stop();
            }
        });

        int socketTimeout = 300;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == ACT_CHECK_TTS_DATA) {
            if (resultCode ==
                    TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                // Data exists, so we instantiate the TTS engine
                tts = new TextToSpeech(this, this);
            } else {
                // Data is missing, so we start the TTS installation
                // process
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts != null) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA || result ==
                        TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(MainPage.this, "TTS language is not supported",
                            Toast.LENGTH_LONG).show();
                } else {
                    // Do something here
                }
            }
        } else {
            Toast.makeText(MainPage.this, "TTS initialization failed",
                    Toast.LENGTH_LONG).show();
        }
    }




    private void requestGpsPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(this)
//                        .setTitle("Permission Required")
//                        .setMessage("Grand the permission first")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(MainPage.this,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        1);
//                            }
//                        })
//                        .create()
//                        .show();

            } else {
                ActivityCompat.requestPermissions(MainPage.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
    }

    private void requestWritePermission() {
        if (ContextCompat.checkSelfPermission(MainPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainPage.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainPage.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

    }

    private void helpUser() {

        sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.navigation_home), "Home", "Return back to homepage")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.white)
                                .outerCircleAlpha(0.88f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(0),
                        TapTarget.forView(findViewById(R.id.navigation_new_nepal), "Place Mapping", "Know the local level from VDC and vice-versa")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.white)
                                .outerCircleAlpha(0.88f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(1),
                        TapTarget.forView(findViewById(R.id.navigation_information), "State Information",
                                "Know more information about state such as capital, natural resources, urgent services, etc ")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.white)
                                .outerCircleAlpha(0.88f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(2),
                        TapTarget.forView(findViewById(R.id.navigation_detail_map), "Map Gallery", "Image gallery of maps, such as detail and separate maps of states, districts, etc")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.white)
                                .outerCircleAlpha(0.88f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(3),
                        TapTarget.forView(findViewById(R.id.navigation_ministry), "Ministry",
                                "Know the current minister of different ministries")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.white)
                                .outerCircleAlpha(0.88f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(4)
//                        TapTarget.forView(findViewById(R.id.help), "Help", "Find the help to operate application")
//                                .dimColor(android.R.color.black)
//                                .outerCircleColor(R.color.targetOuterCircleColor)
//                                .targetCircleColor(R.color.white)
//                                .outerCircleAlpha(0.88f)
//                                .cancelable(true)
//                                .textColor(R.color.black)
//                                .descriptionTextColor(R.color.black)
//                                .titleTypeface(Typeface.DEFAULT_BOLD)
//                                .id(5)
//                        TapTarget.forToolbarOverflow(toolbar, "This will show more options", "But they're not useful :(").id(6)
//                        TapTarget.forView(findViewById(R.id.clear_cache), "Clear app cache", "You can clear the app cache from here")
//                                .dimColor(android.R.color.black)
//                                .outerCircleColor(R.color.targetOuterCircleColor)
//                                .targetCircleColor(R.color.white)
//                                .outerCircleAlpha(0.77f)
//                                .cancelable(true)
//                                .textColor(R.color.black)
//                                .descriptionTextColor(R.color.black)
//                                .titleTypeface(Typeface.DEFAULT_BOLD)
//                                .id(6),
//                        TapTarget.forView(findViewById(R.id.setting), "Application Setting", "Manage your setting from here")
//                                .dimColor(android.R.color.black)
//                                .outerCircleColor(R.color.targetOuterCircleColor)
//                                .targetCircleColor(R.color.white)
//                                .outerCircleAlpha(0.77f)
//                                .cancelable(true)
//                                .textColor(R.color.black)
//                                .descriptionTextColor(R.color.black)
//                                .titleTypeface(Typeface.DEFAULT_BOLD)
//                                .id(7)

                        )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        Toast.makeText(MainPage.this, "If you want the tutorial again click help", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
//                      final AlertDialog dialog = new AlertDialog.Builder(MainPage.this)
//
//                               .setTitle("Uh oh")
//
//                               .setMessage("You canceled the sequence")
//
//                               .setPositiveButton("Restart Help", new DialogInterface.OnClickListener() {
//                                   @Override
//                                   public void onClick(DialogInterface dialog, int which) {
////                                       helpUser();
////                                       sequence.start();
//                                   }
//                               }).show();
                        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                        builder.setCancelable(true);
                        builder.setPositiveButton("Restart Tutorial", null);

                        final AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
                        TapTargetView.showFor(alert,

                                TapTarget.forView(alert.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!",
                                        "You canceled the help tutorial. Click restart to restart tutorial or click outside blue circle to exit.")

                                        .dimColor(android.R.color.black)
                                        .outerCircleColor(R.color.targetOuterCircleColor)
                                        .targetCircleColor(R.color.maroon)
                                        .outerCircleAlpha(0.95f)
                                        .cancelable(true)
                                        .targetRadius(70)
                                        .descriptionTextColor(R.color.black)
                                        .descriptionTypeface(Typeface.DEFAULT_BOLD)
                                        .titleTextColor(R.color.black)
                                        .titleTypeface(Typeface.DEFAULT_BOLD)

                                        .tintTarget(false), new TapTargetView.Listener() {

                                    @Override

                                    public void onTargetClick(TapTargetView view) {

                                        super.onTargetClick(view);
                                        helpUser();
                                        sequence.start();
                                        // alert.dismiss();

                                    }

                                    @Override

                                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {

                                        alert.dismiss();

                                    }

                                });
                    }
                });

//        TapTargetView.showFor(MainPage.this,                 // `this` is an Activity
//                TapTarget.forView(findViewById(R.id.navigation_new_nepal), "Local Level Finder",
//                        "Find the local level from old vdcs")
//                        // All options below are optional
//                        .outerCircleColor(R.color.targetOuterCircleColor)      // Specify a color for the outer circle
//                        .outerCircleAlpha(0.5f)            // Specify the alpha amount for the outer circle
//                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
//                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
//                        .titleTextColor(R.color.white)      // Specify the color of the title text
//                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
//                        .descriptionTextColor(R.color.red)  // Specify the color of the description text
//                        .textColor(R.color.blue)            // Specify a color for both the title and description text
//                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
//                        .drawShadow(true)                   // Whether to draw a drop shadow or not
//                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
//                        .tintTarget(true)                   // Whether to tint the target view's color
//                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
//                        //.icon(Drawable)                     // Specify a custom drawable to draw as the target
//                        .targetRadius(60),                  // Specify the target radius (in dp)
//                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
//                    @Override
//                    public void onTargetClick(TapTargetView view) {
//                        super.onTargetClick(view);      // This call is optional
//                        fragment=new MainFragment();
//                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentMain, fragment);
//                        fragmentTransaction.commit();
//                    }
//                });
//
//        TapTargetView.showFor(MainPage.this,                 // `this` is an Activity
//                TapTarget.forView(findViewById(R.id.navigation_information), "States Details",
//                        "Find the more details about the states")
//                        // All options below are optional
//                        .outerCircleColor(R.color.targetOuterCircleColor)      // Specify a color for the outer circle
//                        .outerCircleAlpha(0.55f)            // Specify the alpha amount for the outer circle
//                        .targetCircleColor(R.color.white)   // Specify a color for the target circle
//                        .titleTextSize(20)                  // Specify the size (in sp) of the title text
//                        .titleTextColor(R.color.white)      // Specify the color of the title text
//                        .descriptionTextSize(10)            // Specify the size (in sp) of the description text
//                        .descriptionTextColor(R.color.red)  // Specify the color of the description text
//                        .textColor(R.color.blue)            // Specify a color for both the title and description text
//                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
//                        .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
//                        .drawShadow(true)                   // Whether to draw a drop shadow or not
//                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
//                        .tintTarget(true)                   // Whether to tint the target view's color
//                        .transparentTarget(false)           // Specify whether the target is transparent (displays the content underneath)
//                        //.icon(Drawable)                     // Specify a custom drawable to draw as the target
//                        .targetRadius(60),                  // Specify the target radius (in dp)
//                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
//                    @Override
//                    public void onTargetClick(TapTargetView view) {
//                        super.onTargetClick(view);      // This call is optional
//                        fragment=new MainFragment();
//                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.fragmentMain, fragment);
//                        fragmentTransaction.commit();
//                    }
//                });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isHomeFirstTime) {
            fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentMain, fragment);
            fragmentTransaction.commit();
            isHomeFirstTime = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.help) {
            helpUser();
            sequence.start();
        }
        if (id == R.id.clear_cache) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setIcon(getResources().getDrawable(R.drawable.ic_warning_black_24dp));
            builder.setTitle("Delete All Cached Data");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestQueue queue = Volley.newRequestQueue(MainPage.this);
                    queue.getCache().clear();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        if(id==R.id.setting){
            Intent intent=new Intent(MainPage.this,Setting.class);
            startActivity(intent);
        }
        if(id==R.id.feedback){
            Intent intent=new Intent(MainPage.this,Feedback.class);
            startActivity(intent);
        }
        if(id==R.id.check_update){
            checkAppUpdate("fromMenu");
        }
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    getSupportActionBar().setTitle("MeroPalika");
                    break;
                case R.id.navigation_information:
                    fragment = new StateFragment();
                    getSupportActionBar().setTitle("State Info");
                    break;
                case R.id.navigation_new_nepal:
                    requestGpsPermissions();
                    fragment = new MainFragment();
                    getSupportActionBar().setTitle("Place Mapping");
                    break;
                case R.id.navigation_detail_map:
                    getSupportActionBar().setTitle("Separate Map");
                    fragment = new SeperateDetailMapFragment();
                    break;
                case R.id.navigation_ministry:
                    getSupportActionBar().setTitle("Current Ministry");
                    fragment = new MinistryFragment();
                    break;
            }
            if (fragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentMain, fragment);
                fragmentTransaction.commit();
            }
            return false;

        }


    };
   private void loadFragment(String fragmentChooser){
       switch (fragmentChooser) {
           case "HOME":
               fragment = new HomeFragment();
               break;
           case "STATEINFO":
               fragment = new StateFragment();
               break;
           case "PLACEMAPPING":
               fragment = new MainFragment();
               break;
           case "MAPGALLERY":
               fragment = new SeperateDetailMapFragment();
               break;
           case "MINISTRY":
               fragment = new MinistryFragment();
               break;
       }
       if (fragment != null) {
           FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
           fragmentTransaction.replace(R.id.fragmentMain, fragment);
           fragmentTransaction.commit();

       }
   }

    // Ask user to exit or not
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }
//Start

    private String getCurrentVersion(){
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(this.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        return pInfo.versionName;
    }

}
