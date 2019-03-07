package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.been.MainPageBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.MainFragmentPresenter;
import com.zhongchuang.canting.viewcallback.ZhuYeViewCallBack;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MainFragmentPresenterImpl extends BasePresenterImpl implements MainFragmentPresenter {

    private ZhuYeViewCallBack callBack;

    public MainFragmentPresenterImpl(ZhuYeViewCallBack callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    public void getBannerList() {
        api.getBannerList(null).enqueue(new BaseCallBack<MainPageBean>() {
            @Override
            public void onSuccess(MainPageBean response) {
                callBack.onResultSuccess(response);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
}
