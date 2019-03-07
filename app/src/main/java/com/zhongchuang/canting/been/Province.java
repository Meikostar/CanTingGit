package com.zhongchuang.canting.been;

import java.util.List;

/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/26
 * 版本:
 ***/
public class Province {
//     (string, optional): 详细地址 ,
//    area_ (integer, optional): 楼层数量 ,
//    create_time_ (string, optional): 创建时间 ,
//    id_ (integer, optional): 用户id ,
//    img_ (string, optional): 市场图片 ,
//    mod_time_ (string, optional): 更新时间 ,
//    name_ (string, optional): 市场名称 ,
//    region_ (string, optional): 省市县 ,
//    site_ (integer, optional): 所属站点id ,
//    sort_ (integer, optional): 序号 ,
//    status_ (integer, optional): 状态(-1:作废,0:无效,1:有效)
    private String address_;
    private String create_time_;
    private String img_;
    private String mod_time_;
    private String name_;
    private String region_;
    private long id_;
    private long site_;
    private int area_;
    private int sort_;
    private int status_;
    private List<City> floorList;
    public List<Province> provinceCityList;
    public List<City> cityList;
    public int provinceCode;
    public String provinceName;
    public List<City> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<City> floorList) {
        this.floorList = floorList;
    }

    public String getAddress_() {
        return address_;
    }

    public void setAddress_(String address_) {
        this.address_ = address_;
    }

    public String getCreate_time_() {
        return create_time_;
    }

    public void setCreate_time_(String create_time_) {
        this.create_time_ = create_time_;
    }

    public String getImg_() {
        return img_;
    }

    public void setImg_(String img_) {
        this.img_ = img_;
    }

    public String getMod_time_() {
        return mod_time_;
    }

    public void setMod_time_(String mod_time_) {
        this.mod_time_ = mod_time_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getRegion_() {
        return region_;
    }

    public void setRegion_(String region_) {
        this.region_ = region_;
    }

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public long getSite_() {
        return site_;
    }

    public void setSite_(long site_) {
        this.site_ = site_;
    }

    public int getArea_() {
        return area_;
    }

    public void setArea_(int area_) {
        this.area_ = area_;
    }

    public int getSort_() {
        return sort_;
    }

    public void setSort_(int sort_) {
        this.sort_ = sort_;
    }

    public int getStatus_() {
        return status_;
    }

    public void setStatus_(int status_) {
        this.status_ = status_;
    }

    @Override
    public String toString() {
        return "Province{" +
                "address_='" + address_ + '\'' +
                ", create_time_='" + create_time_ + '\'' +
                ", img_='" + img_ + '\'' +
                ", mod_time_='" + mod_time_ + '\'' +
                ", name_='" + name_ + '\'' +
                ", region_='" + region_ + '\'' +
                ", id_=" + id_ +
                ", site_=" + site_ +
                ", area_=" + area_ +
                ", sort_=" + sort_ +
                ", status_=" + status_ +
                '}';
    }
}
