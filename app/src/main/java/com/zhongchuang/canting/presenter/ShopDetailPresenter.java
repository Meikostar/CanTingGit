package com.zhongchuang.canting.presenter;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/9.
 */

public interface ShopDetailPresenter {

    void getGoodsDetail(String goodId);

    void addShopCar(Map<String,String> map);

}
