package com.zhongchuang.canting.presenter;

/**
 * Created by Administrator on 2017/12/2.
 */

public interface FoodShopPresenter {
    void getCare(String userInfoId, String cateShopId, String type);
    void getShopInfo(String cateShopId, String userInfoId);
    void getShopCommentList(String cateShopId);

}
