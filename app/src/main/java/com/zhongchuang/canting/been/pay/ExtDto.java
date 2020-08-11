package com.zhongchuang.canting.been.pay;


import com.zhongchuang.canting.been.NewListItemDto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/28
 */
public class ExtDto implements Serializable {
    private String       city;
    private String       district;
    private String       province;
    private String       min_buy;
    public String       is_brand;
    public String       num;
    public String       end;
    public String       buy_num;
    public String       price;
    public  String       slogan;
    public List<String> imgs;
    public  String       name;
    public NewListItemDto origin_info;
    public  String       logo;
    public  String       id;
    public  String       title;
    public  String       icon;
    public  ExtDto       category;
    public  ExtDto       data;
    private String       services;
    private String       open_time;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMin_buy() {
        return min_buy;
    }

    public void setMin_buy(String min_buy) {
        this.min_buy = min_buy;
    }

    public String getIs_brand() {
        return is_brand;
    }

    public void setIs_brand(String is_brand) {
        this.is_brand = is_brand;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }
}
