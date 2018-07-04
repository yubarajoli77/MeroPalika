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

import com.softechfoundation.municipal.Activities.ResourcesAndServicesDetail;
import com.softechfoundation.municipal.Activities.ShowItemInMap;
import com.softechfoundation.municipal.GloballyCommon;
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
    private Context getContext(){
        return context;
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

        if(currentService.getPhone() == null){
            holder.call.setImageDrawable(context.getResources().getDrawable(R.drawable.disabled_phone));
            holder.call.setClickable(false);
            holder.call.setEnabled(false);
        }else{
            holder.call.setImageDrawable(context.getResources().getDrawable(R.drawable.phone_24dp));
            holder.call.setClickable(true);
            holder.call.setEnabled(true);
        }
        if(currentService.getInfo()!=null){
            holder.info.setText(currentService.getInfo());
        } else {
            holder.info.setVisibility(View.GONE);
        }

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShowItemInMap.class);
                intent.putExtra("location",currentService.getAddress());
                intent.putExtra("name",currentService.getName());
                getContext().startActivity(intent);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ResourcesAndServicesDetail.class);
                intent.putExtra("name",currentService.getName());
                intent.putExtra("location",currentService.getAddress());
                intent.putExtra("phone",currentService.getPhone());
                intent.putExtra("rsType",currentService.getServiceType());
//                intent.putExtra("description",currentService.getDescription());
                GloballyCommon.getInstance().setDescription(currentService.getDescription());
                Log.d("AdapterImage::",currentService.getImage());
                GloballyCommon.getInstance().setPic(currentService.getImage());
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataItem.size();
    }

    public class ServicePojoViewHolder extends RecyclerView.ViewHolder{
        TextView name, address,info;
        ImageButton location,call;
        CardView cardView;
        public ServicePojoViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.service_list_list_name);
            address=itemView.findViewById(R.id.service_list_list_address);
            location=itemView.findViewById(R.id.service_list_location);
            call=itemView.findViewById(R.id.service_list_phone);
            info=itemView.findViewById(R.id.service_list_list_info);
            cardView=itemView.findViewById(R.id.service_card_view);
        }
    }

    public void callNumber(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL); //use ACTION_CALL for direct call
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:"+phone));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{android.Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                getContext().startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
