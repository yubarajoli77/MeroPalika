package com.softechfoundation.municipal.Pojos;

/**
 * Created by yubar on 5/9/2018.
 */

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RSImageGalleryPojo implements Parcelable {

    private String mUrl;
    private String mTitle;

    public RSImageGalleryPojo(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected RSImageGalleryPojo(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<RSImageGalleryPojo> CREATOR = new Creator<RSImageGalleryPojo>() {
        @Override
        public RSImageGalleryPojo createFromParcel(Parcel in) {
            return new RSImageGalleryPojo(in);
        }

        @Override
        public RSImageGalleryPojo[] newArray(int size) {
            return new RSImageGalleryPojo[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static  RSImageGalleryPojo[] getRSPhotos() {

        return new RSImageGalleryPojo[]{
                new RSImageGalleryPojo("http://i.imgur.com/zuG2bGQ.jpg", "Galaxy"),
                new RSImageGalleryPojo("http://i.imgur.com/ovr0NAF.jpg", "Space Shuttle"),
                new RSImageGalleryPojo("http://i.imgur.com/n6RfJX2.jpg", "Galaxy Orion"),
                new RSImageGalleryPojo("http://i.imgur.com/qpr5LR2.jpg", "Earth"),
                new RSImageGalleryPojo("http://i.imgur.com/pSHXfu5.jpg", "Astronaut"),
                new RSImageGalleryPojo("http://i.imgur.com/3wQcZeY.jpg", "Satellite"),
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}