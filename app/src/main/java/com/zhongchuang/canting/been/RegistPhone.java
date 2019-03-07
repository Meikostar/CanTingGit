package com.zhongchuang.canting.been;

/**
 * Created by Administrator on 2017/11/11.
 */

public class RegistPhone {

    private String mobileNubmer;


    /*
     * status : 301
     * message : 成功
     * data : null
     */


    public RegistPhone(String mPhone) {
        this.mobileNubmer = mPhone;
    }

    public void setMobileNubmer(String mobileNubmer) {
        this.mobileNubmer = mobileNubmer;
    }

    public String getMobileNubmer() {
        return mobileNubmer;
    }

    @Override
    public String toString() {
        return "RegistPhone{" +
                "mobileNumber='" + mobileNubmer + '\'' +
                '}';
    }


}
