package com.softechfoundation.municipal.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.softechfoundation.municipal.Fragments.HomeFragment;
import com.softechfoundation.municipal.Fragments.MainFragment;
import com.softechfoundation.municipal.Fragments.MinistryFragment;
import com.softechfoundation.municipal.Fragments.SeperateDetailMapFragment;
import com.softechfoundation.municipal.Fragments.StateFragment;
import com.softechfoundation.municipal.R;

public class MainPage extends AppCompatActivity {
    public static BottomNavigationView navigation;
    private Fragment fragment;
    TapTargetSequence sequence;
    private boolean isHomeFirstTime = true;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        requestWritePermission();
        fragment = null;
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        helpUser();
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
    private void requestWritePermission(){
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
                        TapTarget.forView(findViewById(R.id.navigation_home), "Home", "Go to Home page")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.red)
                                .outerCircleAlpha(0.77f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(0),
                        TapTarget.forView(findViewById(R.id.navigation_new_nepal), "Old To New", "Know the local level from old places")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.red)
                                .outerCircleAlpha(0.77f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(1),
                        TapTarget.forView(findViewById(R.id.navigation_information), "State Information", "Know more information about state")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.red)
                                .outerCircleAlpha(0.77f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(2),
                        TapTarget.forView(findViewById(R.id.navigation_detail_map), "Detail Map of States", "Detail and separate map of States")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.red)
                                .outerCircleAlpha(0.77f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(3),
                        TapTarget.forView(findViewById(R.id.navigation_ministry), "Ministries Of Nepal", "Know the ministers of different ministries")
                                .dimColor(android.R.color.black)
                                .outerCircleColor(R.color.targetOuterCircleColor)
                                .targetCircleColor(R.color.red)
                                .outerCircleAlpha(0.77f)
                                .cancelable(true)
                                .textColor(R.color.black)
                                .descriptionTextColor(R.color.black)
                                .titleTypeface(Typeface.DEFAULT_BOLD)
                                .id(4)
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
                        builder.setTitle("Oops!");
                        builder.setMessage("You canceled the help tutorial");
                        builder.setPositiveButton("Restart Tutorial", null);

                        final AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));
                        TapTargetView.showFor(alert,

                                TapTarget.forView(alert.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())

                                        .dimColor(android.R.color.black)
                                        .outerCircleColor(R.color.targetOuterCircleColor)
                                        .targetCircleColor(R.color.maroon)
                                        .outerCircleAlpha(0.88f)
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
        return super.onOptionsItemSelected(item);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    getSupportActionBar().setTitle("Municipal");
                    break;
                case R.id.navigation_information:
                    fragment = new StateFragment();
                    getSupportActionBar().setTitle("State Info");
                    break;
                case R.id.navigation_new_nepal:
                    requestGpsPermissions();
                    fragment = new MainFragment();
                    getSupportActionBar().setTitle("Old To New");
                    break;
                case R.id.navigation_detail_map:
                    getSupportActionBar().setTitle("Detail In Map");
                    fragment=new SeperateDetailMapFragment();
                    break;
                case R.id.navigation_ministry:
                    getSupportActionBar().setTitle("Ministry Of Nepal");
                    fragment=new MinistryFragment();
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


}
