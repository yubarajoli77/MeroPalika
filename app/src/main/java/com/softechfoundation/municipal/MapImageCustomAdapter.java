package com.softechfoundation.municipal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by yubar on 4/10/2018.
 */

public class MapImageCustomAdapter extends RecyclerView.Adapter<MapImageCustomAdapter.GridImageViewPojoViewHolder> {
    private LayoutInflater inflator;
    private Context context;
    private List<GridImageViewPojo> dataItem = Collections.emptyList();


    public MapImageCustomAdapter(Context context, List<GridImageViewPojo> dataItem) {

        Log.d("DataList---",dataItem.toString());
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
    }


    @NonNull
    @Override
    public MapImageCustomAdapter.GridImageViewPojoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design_grid_item, parent, false);
        MapImageCustomAdapter.GridImageViewPojoViewHolder holder = new MapImageCustomAdapter.GridImageViewPojoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MapImageCustomAdapter.GridImageViewPojoViewHolder holder, int position) {
        final GridImageViewPojo currentResource=dataItem.get(position);
        holder.name.setText(currentResource.getName());
        holder.imageView.setImageResource(currentResource.getImage());

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }


    public class GridImageViewPojoViewHolder extends RecyclerView.ViewHolder{
        TextView name;
       ImageView imageView;
        public GridImageViewPojoViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.grid_image);
            name=itemView.findViewById(R.id.grid_image_name);
        }
    }
}
