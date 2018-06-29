package com.softechfoundation.municipal.Pojos;

/**
 * Created by yubar on 4/9/2018.
 */

public class ServicePojo {
    private Integer id;
    private String name;
    private String address;
    private String phone;
    private String info;
    private String district;
    private String Image;
    private String description;
    private String state;
    private LocalLevelResponsePojo localLevel;

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

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return Image;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalLevelResponsePojo getLocalLevel() {
        return localLevel;
    }

    public void setLocalLevel(LocalLevelResponsePojo localLevel) {
        this.localLevel = localLevel;
    }
}
