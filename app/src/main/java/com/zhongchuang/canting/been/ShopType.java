package com.zhongchuang.canting.been;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopType implements Parcelable{

    public String classifyName;
    public String classifyId;

    protected ShopType(Parcel in) {
        classifyName = in.readString();
        classifyId = in.readString();
    }

    public static final Creator<ShopType> CREATOR = new Creator<ShopType>() {
        @Override
        public ShopType createFromParcel(Parcel in) {
            return new ShopType(in);
        }

        @Override
        public ShopType[] newArray(int size) {
            return new ShopType[size];
        }
    };

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(classifyName);
        dest.writeString(classifyId);
    }
}
