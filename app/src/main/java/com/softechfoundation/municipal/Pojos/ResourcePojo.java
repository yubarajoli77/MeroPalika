package com.softechfoundation.municipal.Pojos;

/**
 * Created by yubar on 4/9/2018.
 */

public class ResourcePojo {
    private String name;
    private String address;
    private String info;
    private String district;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String phone) {
        this.address = phone;
    }
}
