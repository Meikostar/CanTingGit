package com.zhongchuang.canting.been;


import com.google.gson.annotations.SerializedName;
import com.zhongchuang.canting.been.pay.ExtDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/20.
 */
public class RecommendListDto implements Serializable {

    public String       mer_phone;
    public String       link_man;
    public String       front_id_card;
    public String       mer_password;
    public String       real_name;
    public String       industry;
    public String       license_img;
    public int       audit_status;
    @SerializedName("download_file_url")
    public String apk_url;//app下载地址
    @SerializedName("mer_name")
    public String       shop_name;
    public String       type;
    public String       category;
    public String       negative_id_card;
    public String       mer_idcard;

    public String       brand_img;
    public String       business_url;
    public String       shop_urls;


    public String       name;
    public String       area_id;
    @SerializedName("mer_address")
    public String       address;
    public String       link_phone;
    public String       phone;
    public double       longitude;
    public double       latitude;
    @SerializedName("shop_logo")
    public String       logo;
    public String photos;
    public List<String> service_arr;

    public ExtDto ext;

    public List<String> labels;

    public String       distance;

    public int       id;
    public List<String> area;
    public String       new_product;


    public String getShop_name() {
        return shop_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }



    public ExtDto getExt() {
        return ext;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public List<String> getArea() {
        return area;
    }

    public void setArea(List<String> area) {
        this.area = area;
    }
}
