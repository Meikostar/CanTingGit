package com.zhongchuang.canting.presenter;

/**
 * Created by Administrator on 2017/12/13.
 */

public interface ShopSureOrderPresenter {
    void submitOrder(String equipmentStatus,String buyerMessage,String userInfoId,String addressId,String quantity,String goodsCartIds,String payType );
    void wxPay(String orderid,String totaFee);
}
