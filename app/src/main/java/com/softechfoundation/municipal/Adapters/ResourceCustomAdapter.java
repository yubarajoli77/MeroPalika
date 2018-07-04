package com.softechfoundation.municipal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.softechfoundation.municipal.Activities.ListOfServicesAndResources;
import com.softechfoundation.municipal.Activities.ResourcesAndServicesDetail;
import com.softechfoundation.municipal.Activities.ShowItemInMap;
import com.softechfoundation.municipal.GloballyCommon;
import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.Pojos.ResourcePojo;

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

    private Context getContext(){
        return context;
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
        holder.info.setText(currentResource.getInfo());

//        holder.location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getContext(), ShowItemInMap.class);
//                intent.putExtra("location",currentResource.getAddress());
//                intent.putExtra("name",currentResource.getName());
//                getContext().startActivity(intent);
//            }
//        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ResourcesAndServicesDetail.class);
                intent.putExtra("name",currentResource.getName());
                intent.putExtra("location",currentResource.getAddress());
                intent.putExtra("phone","NOPHONE");
                intent.putExtra("rsType",currentResource.getResourceType());
//                intent.putExtra("description",currentResource.getDescription());
                GloballyCommon.getInstance().setPic(currentResource.getImage());
                GloballyCommon.getInstance().setDescription(currentResource.getDescription());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }


    public class ResourcePojoViewHolder extends RecyclerView.ViewHolder{
        TextView name, address,info;
        ImageButton location;
        CardView cardView;
        public ResourcePojoViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.resource_list_list_name);
            address=itemView.findViewById(R.id.resource_list_list_address);
           // location=itemView.findViewById(R.id.resource_list_location);
            info=itemView.findViewById(R.id.resource_list_info);
            cardView=itemView.findViewById(R.id.resource_card_view);
        }
    }


}
