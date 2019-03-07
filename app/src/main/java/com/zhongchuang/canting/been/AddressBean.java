package com.zhongchuang.canting.been;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/9.
 */

public class AddressBean implements Parcelable{

    public String phone;
    public String adress;
    public int state;  //“1“为非默认地址，”2“为默认地址
    public String deliverName;
    public String addressId;
    public String xxdz;


    protected AddressBean(Parcel in) {
        phone = in.readString();
        adress = in.readString();
        state = in.readInt();
        deliverName = in.readString();
        addressId = in.readString();
        xxdz = in.readString();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel in) {
            return new AddressBean(in);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDeliverName() {
        return deliverName;
    }

    public void setDeliverName(String deliverName) {
        this.deliverName = deliverName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getXxdz() {
        return xxdz;
    }

    public void setXxdz(String xxdz) {
        this.xxdz = xxdz;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(adress);
        dest.writeInt(state);
        dest.writeString(deliverName);
        dest.writeString(addressId);
        dest.writeString(xxdz);
    }
}
