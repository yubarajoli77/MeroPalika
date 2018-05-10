package com.softechfoundation.municipal.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.softechfoundation.municipal.Activities.PhotoViewer;
import com.softechfoundation.municipal.Pojos.RSImageGalleryPojo;
import com.softechfoundation.municipal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yubar on 5/9/2018.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.MyViewHolder>  {
    public static ArrayList<Image> imageList;

    @Override
    public ImageGalleryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.rs_detail_image_item, parent, false);
        ImageGalleryAdapter.MyViewHolder viewHolder = new ImageGalleryAdapter.MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageGalleryAdapter.MyViewHolder holder, int position) {

        RSImageGalleryPojo rsImageGalleryPojo = mRSPhotos[position];
        ImageView imageView = holder.mPhotoImageView;
        Glide.with(mContext)
                .load(rsImageGalleryPojo.getUrl())
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return (mRSPhotos.length);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mPhotoImageView;

        public MyViewHolder(View itemView) {

            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                RSImageGalleryPojo rsImageGalleryPojo = mRSPhotos[position];
                Intent intent = new Intent(mContext, PhotoViewer.class);
                intent.putExtra(PhotoViewer.EXTRA_SPACE_PHOTO, rsImageGalleryPojo);
                mContext.startActivity(intent);
            }
        }
    }

    private RSImageGalleryPojo[] mRSPhotos;
    private Context mContext;

    public ImageGalleryAdapter(Context context, RSImageGalleryPojo[] rSPhotos) {
        mContext = context;
        mRSPhotos = rSPhotos;

    }


}
