package com.zhongchuang.canting.presenter;

/**
 * Created by Administrator on 2017/12/2.
 */

public interface FoodShopPresenter {
    public void getCare(String userInfoId , String cateShopId , String type );
    public void getShopInfo(String cateShopId , String userInfoId);
    public void getShopCommentList(String cateShopId );

}
