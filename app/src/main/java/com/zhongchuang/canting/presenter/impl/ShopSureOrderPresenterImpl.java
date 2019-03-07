package com.zhongchuang.canting.presenter.impl;

import android.content.Context;

import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.ShopSureOrderPresenter;
import com.zhongchuang.canting.viewcallback.ShopOrderViewCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/13.
 */

public class ShopSureOrderPresenterImpl  extends BasePresenterImpl implements ShopSureOrderPresenter {
    private Context mContext;
    private ShopOrderViewCallBack callBack;

    public ShopSureOrderPresenterImpl(Context mContext,ShopOrderViewCallBack callBack) {
        super();
        this.mContext=mContext;
        this.callBack = callBack;
    }

    @Override
    public void submitOrder(String equipmentStatus,String buyerMessage,String userInfoId,String addressId,String quantity,String goodsCartIds,String payType) {
        Map<String,String> map = new HashMap<>();
        map.put("equipmentStatus",equipmentStatus);
        map.put("buyerMessage",buyerMessage);
        map.put("userInfoId",userInfoId);
        map.put("payType",payType);
        map.put("addressId",addressId);
        map.put("quantity",quantity);
        map.put("goodsCartIds",goodsCartIds);

        api.submitOrder(map).enqueue(new BaseCallBack<WEIXINREQ>() {

            @Override
            public void onSuccess(WEIXINREQ weixinreq) {
                callBack.onWexinSuccess(weixinreq.data);
            }
            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
    @Override
    public void wxPay(String orderid,String totaFee) {
        Map<String,String> map = new HashMap<>();
        map.put("orderId",orderid);
        map.put("totalFee",totaFee);


        api.getpayInfo(map).enqueue(new BaseCallBack<WEIXINREQ>() {

            @Override
            public void onSuccess(WEIXINREQ weixinreq) {
//
                callBack.onWexinSuccess(weixinreq);
            }
            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
}
