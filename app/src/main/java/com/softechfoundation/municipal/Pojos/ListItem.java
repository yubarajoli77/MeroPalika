package com.softechfoundation.municipal.Pojos;

import android.graphics.Bitmap;

/**
 * Created by yubar on 3/27/2018.
 */

public class ListItem {
    int icon;
    String name;
    String type;

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
