package com.zhongchuang.canting.been;


import java.io.Serializable;

/**
 * Created by rzb on 2019/6/28.
 */
public class ExtendDto implements Serializable {
    private String extra1;
    private String extra2;
    private String extra3;

    public String getExtra1() {
        return extra1;
    }

    public void setExtra1(String extra1) {
        this.extra1 = extra1;
    }

    public String getExtra2() {
        return extra2;
    }

    public void setExtra2(String extra2) {
        this.extra2 = extra2;
    }

    public String getExtra3() {
        return extra3;
    }

    public void setExtra3(String extra3) {
        this.extra3 = extra3;
    }
}