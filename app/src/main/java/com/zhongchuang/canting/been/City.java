package com.zhongchuang.canting.been;

/***
 * 功能描述:
 * 作者:chenwei
 * 时间:2016/12/26
 * 版本:
 ***/
public class City {
    //    create_time_ (string, optional): 创建时间 ,
//    id_ (integer, optional): 用户id ,
//    market_ (integer, optional): 市场id ,
//    mod_time_ (string, optional): 更新时间 ,
//    name_ (string, optional): 楼层名称 ,
//    sort_ (integer, optional): 序号 ,
//    status_ (integer, optional): 状态
    public String total;
    public String user_info_id;
    public String order_type;
    private String create_time_;
    private String mod_time_;
    private String name_;
    private long id_;
    private long market_;
    private int sort_;
    private int status_;
    public int cityCode;
    public String cityName;

    public String getCreate_time_() {
        return create_time_;
    }

    public void setCreate_time_(String create_time_) {
        this.create_time_ = create_time_;
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

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public long getMarket_() {
        return market_;
    }

    public void setMarket_(long market_) {
        this.market_ = market_;
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
        return "City{" +
                "create_time_='" + create_time_ + '\'' +
                ", mod_time_='" + mod_time_ + '\'' +
                ", name_='" + name_ + '\'' +
                ", id_=" + id_ +
                ", market_=" + market_ +
                ", sort_=" + sort_ +
                ", status_=" + status_ +
                '}';
    }
}
