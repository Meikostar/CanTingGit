package com.zhongchuang.canting.presenter.impl;

import android.content.Context;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.ShopCarBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.ShopCarPresenter;
import com.zhongchuang.canting.utils.SpUtil;
import com.zhongchuang.canting.viewcallback.ShopCarViewCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopCarPresenterImpl extends BasePresenterImpl implements ShopCarPresenter {

    private Context mContext;
    private ShopCarViewCallBack carViewCallBack;

    public ShopCarPresenterImpl(Context mContext, ShopCarViewCallBack carViewCallBack) {
        super();
        this.mContext = mContext;
        this.carViewCallBack = carViewCallBack;
    }

    @Override
    public void getShopCarList() {
        Map<String,String> map = new HashMap<>();
        map.put("token", SpUtil.getToken(mContext));
//        map.put("userInfoId", SpUtil.getUserInfoId(mContext));
        if(!CanTingAppLication.getInstance().isLogin()){
            CanTingAppLication.goLogin(mContext);
            return;
        }
        map.put("userInfoId", CanTingAppLication.userId);
        api.getShopCarList(map).enqueue(new BaseCallBack<ShopCarBean>() {
            @Override
            public void onSuccess(ShopCarBean shopCarBean) {
                carViewCallBack.onResultSuccess(shopCarBean);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                carViewCallBack.onFail(code,t);
            }
        });
    }

    @Override
    public void deleteShopCar(String goodid , final int pos) {
        Map<String,String> map = new HashMap<>();
        map.put("token", SpUtil.getToken(mContext));
//        map.put("userInfoId", SpUtil.getUserInfoId(mContext));
        if(!CanTingAppLication.getInstance().isLogin()){
            CanTingAppLication.goLogin(mContext);
            return;
        }
        map.put("userInfoId", CanTingAppLication.userId);
        map.put("goodsCartId", goodid);

        api.deleteShopCar(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                carViewCallBack.deleteSuccess(pos);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                carViewCallBack.deleteFail(t);
            }
        });
    }


}
