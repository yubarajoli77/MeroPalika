package com.softechfoundation.municipal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.softechfoundation.municipal.Activities.MainPage;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.RecyclerViewOnItemClickListener;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;
import com.softechfoundation.municipal.Fragments.MainFragment;
import com.softechfoundation.municipal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 3/27/2018.
 */

public class NewListItemAdapter extends RecyclerView.Adapter<NewListItemAdapter.ListItemViewHolder> {
    private static final String MY_PREFS = "SharedValues";
    private Context context;
    private LayoutInflater inflator;
    // public static String state,district,localLevel;
    private List<ListItem> dataItem = Collections.emptyList();
    private RecyclerView recyclerView;
    private String name;


    private Context getContext() {
        return context;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    private RecyclerViewOnItemClickListener mOnItemClickListener;



    public NewListItemAdapter(Context context, List<ListItem> dataItem, RecyclerView recyclerView,RecyclerViewOnItemClickListener mOnItemClickListener) {
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
        this.recyclerView = recyclerView;
        this.mOnItemClickListener=mOnItemClickListener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design, parent, false);
        ListItemViewHolder holder = new ListItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder holder, int position) {
        final ListItem currentItem = dataItem.get(position);
        holder.listName.setText(currentItem.getName());
//        Drawable topDrawable=getContext().getApplicationContext().getResources().getDrawable(currentItem.getIcon());
//        holder.listName.setCompoundDrawables(null,topDrawable,null,null);
//        holder.listIcon.setImageResource(currentItem.getIcon());

        Glide
                .with(context)
                .load(currentItem.getIcon())
                .into( holder.listIcon);
        setName(currentItem.getName());

        holder.placeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(),v);
                }
            }

        });
////Ask user to choose place mapping
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setCancelable(true);
//        builder.setTitle("Choose place mapping");
//        builder.setPositiveButton("Old To New", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                OldToNew(currentItem);
//            }
//        });
//        builder.setNegativeButton("New To Old", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                newToOld(holder,currentItem);
//
//            }
//        });
//        AlertDialog alert = builder.create();
//        alert.show();
//        //end of ask maping

    }


    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView listName;
        ImageView listIcon;
        CardView placeCardView;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            listIcon = itemView.findViewById(R.id.list_icon);
            listName = itemView.findViewById(R.id.list_name);
            placeCardView = itemView.findViewById(R.id.card_botton);

        }
    }



}
