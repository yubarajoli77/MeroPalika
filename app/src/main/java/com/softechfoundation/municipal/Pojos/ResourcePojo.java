package com.softechfoundation.municipal.Pojos;

import java.util.List;

/**
 * Created by yubar on 4/9/2018.
 */

public class ResourcePojo {
    private Integer id;
    private String name;
    private String description;
    private String Image;
    private String address;
    private String info;
    private String district;
    public List<LocalLevelResponsePojo> getLocalLevelResponsePojoList() {
        return localLevelResponsePojoList;
    }

    public void setLocalLevelResponsePojoList(List<LocalLevelResponsePojo> localLevelResponsePojoList) {
        this.localLevelResponsePojoList = localLevelResponsePojoList;
    }

    private List<LocalLevelResponsePojo> localLevelResponsePojoList;

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

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return Image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
