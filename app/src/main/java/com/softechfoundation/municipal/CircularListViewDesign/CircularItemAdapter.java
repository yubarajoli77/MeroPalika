package com.softechfoundation.municipal.CircularListViewDesign;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.softechfoundation.municipal.Pojos.ListItem;
import com.softechfoundation.municipal.R;

import java.util.ArrayList;

/**
 * Created by yubar on 4/4/2018.
 */

public class CircularItemAdapter extends CircularAdapter {

    private ArrayList<ListItem> mItems;
    private ArrayList<Integer>mIcons;
    private LayoutInflater mInflater;

    private ArrayList<View> mItemViews;
    private ImageView stateImage;
    private TextView stateName;
    public static CardView cardView;

    public CircularItemAdapter(LayoutInflater inflater, ArrayList<ListItem> items){

        this.mItemViews = new ArrayList<>();
        this.mItems = items;
        this.mInflater = inflater;
        this.mIcons=mIcons;
        for(final ListItem s : mItems){
            View view=mInflater.inflate(R.layout.custom_circular_item,null);
            cardView=view.findViewById(R.id.circular_card_item);
           stateImage=view.findViewById(R.id.circular_card_image_item);
            stateImage.setImageResource(s.getIcon());
            stateName = view.findViewById(R.id.circular_description_item);
            stateName.setText(s.getName());
            mItemViews.add(view);

        }


    }


    @Override

    public ArrayList<View> getAllViews() {

        return mItemViews;

    }



    @Override

    public int getCount() {

        return mItemViews.size();

    }



    @Override

    public View getItemAt(int i) {

        return mItemViews.get(i);

    }



    @Override

    public void removeItemAt(int i) {

        if(mItemViews.size() > 0) {

            mItemViews.remove(i);

            notifyItemChange();

        }

    }



    @Override

    public void addItem(View view) {

        mItemViews.add(view);

        notifyItemChange();

    }
}
