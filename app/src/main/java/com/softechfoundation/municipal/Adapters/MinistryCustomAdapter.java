package com.softechfoundation.municipal.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
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
import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.Pojos.MinistryPojo;
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

import static android.content.Context.MODE_PRIVATE;
import static com.android.volley.Request.Method.GET;

/**
 * Created by yubar on 3/27/2018.
 */

public class MinistryCustomAdapter extends RecyclerView.Adapter<MinistryCustomAdapter.MinistryViewHolder> {
    private static final String MY_PREFS = "SharedValues";
    private Context context;
    private LayoutInflater inflator;
    private List<MinistryPojo> dataItem = Collections.emptyList();

    private Context getContext() {
        return context;
    }


    public MinistryCustomAdapter(Context context, List<MinistryPojo> dataItem) {
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
    }

    @NonNull
    @Override
    public MinistryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design_ministry, parent, false);
        return new MinistryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MinistryViewHolder holder, int position) {
        final MinistryPojo currentItem = dataItem.get(position);
        holder.ministryName.setText(currentItem.getMinistryName());
        holder.ministerName.setText(currentItem.getMinisterName()+" (Minister)");
        holder.party.setText(currentItem.getParty());

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class MinistryViewHolder extends RecyclerView.ViewHolder {
        TextView ministryName, ministerName, party;

        public MinistryViewHolder(View itemView) {
            super(itemView);
            ministryName = itemView.findViewById(R.id.ministry_name);
            ministerName = itemView.findViewById(R.id.ministry_minister_name);
            party = itemView.findViewById(R.id.ministry_minister_party);

        }
    }


}
