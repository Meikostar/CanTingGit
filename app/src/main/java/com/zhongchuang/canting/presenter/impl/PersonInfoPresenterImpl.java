package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.PersonInfoPresenter;
import com.zhongchuang.canting.viewcallback.GetUserInfoViewCallback;


import java.util.Map;

/**
 * Created by Administrator on 2017/12/5.
 */

public class PersonInfoPresenterImpl extends BasePresenterImpl implements PersonInfoPresenter {

    private GetUserInfoViewCallback callBack;

    public PersonInfoPresenterImpl(GetUserInfoViewCallback callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    public void submitChange( Map<String,String> map ) {

        api.changePersonInfo(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callBack.onResultSuccess(response);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
    @Override
    public void getUserInfo(  Map<String,String> map ) {

        api.getUserInfo(map).enqueue(new BaseCallBack<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
               callBack.getUserSuccess(response.data);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }
}
