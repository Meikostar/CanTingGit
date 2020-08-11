package com.zhongchuang.canting.been;

import java.util.List;

public class SmgParam {
    public List<String> shop_category;
    public List<String> shop_platform;
    public Param children;

    public List<AreaDto> data;
    public List<BaseDto> service;
    public List<BaseDto> onsale;
    public List<String> search_distance;
    public List<List<String>> search_price;

    public List<AreaDto> getData() {
        return data;
    }

    public void setData(List<AreaDto> data) {
        this.data = data;
    }
}
