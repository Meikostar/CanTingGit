package com.zhongchuang.canting.been;

import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopFirst {

    public String classifyId;
    public String name;
    public String goodsLogo;
    public List<GoodsInfo> list;

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsInfo> getList() {
        return list;
    }

    public void setList(List<GoodsInfo> list) {
        this.list = list;
    }
}
