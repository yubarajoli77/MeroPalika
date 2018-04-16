package com.softechfoundation.municipal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.softechfoundation.municipal.R;
import com.softechfoundation.municipal.Pojos.ServicePojo;

import java.util.Collections;
import java.util.List;

/**
 * Created by yubar on 4/9/2018.
 */

public class ServiceCustomAdapter extends RecyclerView.Adapter<ServiceCustomAdapter.ServicePojoViewHolder>  {

    private LayoutInflater inflator;
    private Context context;
    private List<ServicePojo> dataItem = Collections.emptyList();

    public ServiceCustomAdapter(Context context, List<ServicePojo> dataItem) {
        inflator = LayoutInflater.from(context);
        this.context = context;
        this.dataItem = dataItem;
    }


    @NonNull
    @Override
    public ServiceCustomAdapter.ServicePojoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_design_state_service_list, parent, false);
        ServiceCustomAdapter.ServicePojoViewHolder holder = new ServiceCustomAdapter.ServicePojoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceCustomAdapter.ServicePojoViewHolder holder, int position) {
        final ServicePojo currentService=dataItem.get(position);
        holder.name.setText(currentService.getName());
        holder.address.setText(currentService.getAddress());

    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class ServicePojoViewHolder extends RecyclerView.ViewHolder{
        TextView name, address;
        ImageButton location,call;
        public ServicePojoViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.service_list_list_name);
            address=itemView.findViewById(R.id.service_list_list_address);
            location=itemView.findViewById(R.id.service_list_location);
            call=itemView.findViewById(R.id.service_list_phone);
        }
    }
}
