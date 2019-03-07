package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MainChildBean {


    public List<MainChild> one;  //1--直播  2--点餐  3--游戏
    public List<MainChild> two;
    public List<MainChild> three;

    public List<MainChild> getOne() {
        return one;
    }

    public void setOne(List<MainChild> one) {
        this.one = one;
    }

    public List<MainChild> getTwo() {
        return two;
    }

    public void setTwo(List<MainChild> two) {
        this.two = two;
    }

    public List<MainChild> getThree() {
        return three;
    }

    public void setThree(List<MainChild> three) {
        this.three = three;
    }
}
