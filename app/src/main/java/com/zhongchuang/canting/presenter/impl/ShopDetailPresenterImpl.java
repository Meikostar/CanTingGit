package com.zhongchuang.canting.presenter.impl;

import android.content.Context;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.GoodsDetailBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.ShopDetailPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.viewcallback.ShopDetailViewCallBack;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopDetailPresenterImpl extends BasePresenterImpl implements ShopDetailPresenter {

    private Context mContext;
    private ShopDetailViewCallBack callBack;


    public ShopDetailPresenterImpl(Context mContext, ShopDetailViewCallBack callBack) {
        super();
        this.mContext = mContext;
        this.callBack = callBack;
    }



    @Override
    public void getGoodsDetail(String goodId) {
        api.getGoodsDetail(goodId).enqueue(new BaseCallBack<GoodsDetailBean>() {
            @Override
            public void onSuccess(GoodsDetailBean goddsDetailBean) {
                callBack.onResultSuccess(goddsDetailBean);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void addShopCar(Map<String, String> map) {
        map.put("token",SpUtil.getToken(mContext));
//        map.put("userInfoId", SpUtil.getUserInfoId(mContext));
        if(!CanTingAppLication.getInstance().isLogin()){
            CanTingAppLication.goLogin(mContext);
            return;
        }
        map.put("userInfoId", CanTingAppLication.userId);
        if(!CanTingAppLication.getInstance().isLogin()){
            CanTingAppLication.goLogin(mContext);
            return;
        }
        api.addShopCar(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callBack.addShopCarSuccess(response.getMessage());
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
}
