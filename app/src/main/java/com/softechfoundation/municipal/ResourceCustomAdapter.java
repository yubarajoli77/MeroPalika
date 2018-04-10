package com.softechfoundation.municipal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by yubar on 4/9/2018.
 */

public class ResourceCustomAdapter extends RecyclerView.Adapter<ResourceCustomAdapter.ResourcePojoViewHolder> {
    private LayoutInflater inflator;
    private Context context;
    private List<ResourcePojo> dataItem = Collections.emptyList();


    public ResourceCustomAdapter(Context context, List<ResourcePojo> dataItem) {
        Log.d("DataList---",dataItem.toString());
        inflator = LayoutInflater.from(context);
        this.dataItem = dataItem;
        this.context = context;
    }


    @NonNull
    @Override
    public ResourcePojoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design_resource_list, parent, false);
        ResourcePojoViewHolder holder = new ResourcePojoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcePojoViewHolder holder, int position) {
        final ResourcePojo currentResource=dataItem.get(position);
        holder.name.setText(currentResource.getName());
        holder.address.setText(currentResource.getAddress());

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }


    public class ResourcePojoViewHolder extends RecyclerView.ViewHolder{
        TextView name, address;
        ImageButton location;
        public ResourcePojoViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.resource_list_list_name);
            address=itemView.findViewById(R.id.resource_list_list_address);
            location=itemView.findViewById(R.id.resource_list_location);
        }
    }
}
