package com.softechfoundation.municipal.CircularListViewDesign;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by yubar on 4/11/2018.
 */


public abstract class CircularAdapter {


    interface CircularItemChangeListener {
        void onCircularItemChange();
    }

    /**
     * get item count
     * @return numbers of item
     */
    public abstract int getCount();


    /**
     * get all custom views, you should put all views into an ArrayList
     * @return a list of views
     */
    public abstract ArrayList<View> getAllViews();


    /**
     * get item at index i
     * @param i index of item
     * @return view at position i
     */
    public abstract View getItemAt(int i);


    /**
     * remove move an item from the list
     * @param i index of item to be removed
     */
    public abstract void removeItemAt(int i);


    /**
     * add an item into the list from last
     */
    public abstract void addItem(View view);


    /**
     * need to notify parent view when item has been changed
     */
    public void notifyItemChange(){
        circularItemChangeListener.onCircularItemChange();
    }

    private CircularItemChangeListener circularItemChangeListener;


    public void setOnItemChangeListener(CircularItemChangeListener listener){
        this.circularItemChangeListener = listener;
    }

}