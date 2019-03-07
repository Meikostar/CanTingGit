package com.zhongchuang.canting.presenter.impl;


import com.zhongchuang.canting.been.ShopChildBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.BaseViewCallBack;
import com.zhongchuang.canting.presenter.ShopScondListPresenter;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopScondListPresenterImpl extends BasePresenterImpl implements ShopScondListPresenter {

    private BaseViewCallBack callBack;

    public ShopScondListPresenterImpl(BaseViewCallBack callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    public void getShopListData(Map<String, String> map) {
        api.getShopListData(map).enqueue(new BaseCallBack<ShopChildBean>() {
            @Override
            public void onSuccess(ShopChildBean shopChildBean) {
                callBack.onResultSuccess(shopChildBean);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
}
