package com.softechfoundation.municipal.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.softechfoundation.municipal.Activities.StateDetails;
import com.softechfoundation.municipal.CommonUrl;
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.RecyclerViewOnItemClickListener;
import com.softechfoundation.municipal.VolleyCache.CacheRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 5/10/2018.
 */

public class FilterCustomAdapter  extends RecyclerView.Adapter<FilterCustomAdapter.FilterCustomAdapterHolder> {
    private static final String MY_PREFS = "districtOrPalika";
    private Context context;
    private LayoutInflater inflator;
    // public static String state,district,localLevel;
    private List<ListItem> dataItem = Collections.emptyList();
    private RecyclerViewOnItemClickListener mOnItemClickListener;

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
    public FilterCustomAdapter(Context context, List<ListItem> dataItem,RecyclerViewOnItemClickListener onItemClickListener) {
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public FilterCustomAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design, parent, false);
        FilterCustomAdapterHolder holder = new FilterCustomAdapterHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final FilterCustomAdapterHolder holder, int position) {
        final ListItem currentItem = dataItem.get(position);
        holder.listName.setText(currentItem.getName());
        Glide.with(context)
                .load(currentItem.getIcon())
                .into( holder.listIcon);
        setName(currentItem.getName());

        holder.placeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(),v);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class FilterCustomAdapterHolder extends RecyclerView.ViewHolder{
        TextView listName;
        ImageView listIcon;
        CardView placeCardView;

        public FilterCustomAdapterHolder(View itemView) {
            super(itemView);
            listIcon = itemView.findViewById(R.id.list_icon);
            listName = itemView.findViewById(R.id.list_name);
            placeCardView = itemView.findViewById(R.id.card_botton);

        }

    }

}
