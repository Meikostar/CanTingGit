package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.been.ShopFirstBean;
import com.zhongchuang.canting.been.ShopHeaderBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.ShopFirstListPresenter;
import com.zhongchuang.canting.viewcallback.ShopFirstViewBackCall;

/**
 * Created by Administrator on 2017/12/9.
 */

public class ShopFirstListPresenterImpl extends BasePresenterImpl implements ShopFirstListPresenter {

    private ShopFirstViewBackCall backCall;

    public ShopFirstListPresenterImpl(ShopFirstViewBackCall backCall) {
        super();
        this.backCall = backCall;
    }

    @Override
    public void headerInfo() {
        api.getShopFirstHeaderData(null).enqueue(new BaseCallBack<ShopHeaderBean>() {
            @Override
            public void onSuccess(ShopHeaderBean shopHeaderBean) {
                backCall.bannerData(shopHeaderBean);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                backCall.onFail(code,t);
            }
        });
    }

    @Override
    public void shopListInfo() {
        api.getShopFirstListData(null).enqueue(new BaseCallBack<ShopFirstBean>() {
            @Override
            public void onSuccess(ShopFirstBean shopFirstBean) {
                backCall.onResultSuccess(shopFirstBean);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                backCall.onFail(code,t);
            }
        });
    }
}
