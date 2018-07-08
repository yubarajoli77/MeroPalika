package com.softechfoundation.municipal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.softechfoundation.municipal.CheckInternet.CheckInternet;
import com.softechfoundation.municipal.Pojos.PicturePojo;

import java.util.List;

public class GloballyCommon {
    private List<PicturePojo> pictureList;
    private String description;

    private String selectedPalikaForWardFilter;

    public List<PicturePojo> getPic() {
        return pictureList;
    }

    public String getDescription() {
        return description;
    }

    public void setPic(List<PicturePojo>pictureList) {
        this.pictureList = pictureList;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectedPalikaForWardFilter() {
        return selectedPalikaForWardFilter;
    }

    public void setSelectedPalikaForWardFilter(String selectedPalikaForWardFilter) {
        this.selectedPalikaForWardFilter = selectedPalikaForWardFilter;
    }

    private static final GloballyCommon dataHolder = new GloballyCommon();
    public static GloballyCommon getInstance() {return dataHolder;}


    public static void checkErrorResponse(CoordinatorLayout coordinatorLayout, final Context context) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Wifi or mobile data may be off. Please turn it on", Snackbar.LENGTH_LONG)
                    .setActionTextColor(context.getResources().getColor(R.color.colorAccent))
                    .setAction("Go To Wifi Setting", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Activity)context).startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

        } else if (!(checkInternet.isOnline())) {
            Toast.makeText(context, "No Internet Availiable", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Our apology, Something wrong with our server.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void checkErrorResponse(RelativeLayout relativeLayout, final Context context) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Wifi or mobile data may be off. Please turn it on", Snackbar.LENGTH_LONG)
                    .setActionTextColor(context.getResources().getColor(R.color.colorAccent))
                    .setAction("Go To Wifi Setting", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Activity)context).startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();

        } else if (!(checkInternet.isOnline())) {
            Toast.makeText(context, "No Internet Availiable", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Our apology, Something wrong with our server.", Toast.LENGTH_SHORT).show();
        }
    }
    public static void checkErrorResponse(FrameLayout frameLayout, final Context context) {
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isNetworkAvailable(context)) {
            Snackbar snackbar = Snackbar
                    .make(frameLayout, "Wifi or mobile data may be off. Please turn it on", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(context.getResources().getColor(R.color.colorAccent))

                    .setAction("Go To Wifi Setting", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Activity)context).startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    });
            snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            snackbar.show();
        } else if (!(checkInternet.isOnline())) {
            Toast.makeText(context, "No Internet Availiable", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Our apology, Something wrong with our server.", Toast.LENGTH_SHORT).show();
        }
    }


}
