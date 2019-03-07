package com.zhongchuang.canting.been;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/6/25.
 */

public class DeliveryAddressBean implements Parcelable {

    /**
     * address_ (string, optional): 省市县 ,
     * create_time_ (string, optional): 创建时间 ,
     * detail_ (string, optional): 详细地址 ,
     * id_ (integer, optional): 主键id ,
     * is_default_ (boolean, optional): 是否默认 ,
     * name_ (string, optional): 收货人名字 ,
     * phone_ (string, optional): 手机号码 ,
     * user_ (integer, optional): 用户id
     */

    private String address_;
    private String create_time_;
    private String detail_;
    private long id_;
    private boolean is_default_;
    private String name_;
    private String phone_;
    private long user_;

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

    public String getDetail_() {
        return detail_;
    }

    public void setDetail_(String detail_) {
        this.detail_ = detail_;
    }

    public long getId_() {
        return id_;
    }

    public void setId_(long id_) {
        this.id_ = id_;
    }

    public boolean isIs_default_() {
        return is_default_;
    }

    public void setIs_default_(boolean is_default_) {
        this.is_default_ = is_default_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getPhone_() {
        return phone_;
    }

    public void setPhone_(String phone_) {
        this.phone_ = phone_;
    }

    public long getUser_() {
        return user_;
    }

    public void setUser_(long user_) {
        this.user_ = user_;
    }

//    private boolean isSelect;
//
//    public boolean isSelect() {
//        return isSelect;
//    }
//
//    public void setSelect(boolean select) {
//        isSelect = select;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address_);
        dest.writeString(this.create_time_);
        dest.writeString(this.detail_);
        dest.writeLong(this.id_);
        dest.writeByte(this.is_default_ ? (byte) 1 : (byte) 0);
        dest.writeString(this.name_);
        dest.writeString(this.phone_);
        dest.writeLong(this.user_);
        //dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    }

    public DeliveryAddressBean() {
    }

    protected DeliveryAddressBean(Parcel in) {
        this.address_ = in.readString();
        this.create_time_ = in.readString();
        this.detail_ = in.readString();
        this.id_ = in.readLong();
        this.is_default_ = in.readByte() != 0;
        this.name_ = in.readString();
        this.phone_ = in.readString();
        this.user_ = in.readLong();
        //this.isSelect = in.readByte() != 0;
    }

    public static final Creator<DeliveryAddressBean> CREATOR = new Creator<DeliveryAddressBean>() {
        @Override
        public DeliveryAddressBean createFromParcel(Parcel source) {
            return new DeliveryAddressBean(source);
        }

        @Override
        public DeliveryAddressBean[] newArray(int size) {
            return new DeliveryAddressBean[size];
        }
    };
}

