package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.been.UserLoginBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.net.BaseViewCallBack;
import com.zhongchuang.canting.presenter.LoginPresenter;

/**
 * Created by Administrator on 2017/12/2.
 */

public class LoginPresenterImpl extends BasePresenterImpl implements LoginPresenter{

    private BaseViewCallBack<UserLoginBean> callBack;

    public LoginPresenterImpl(BaseViewCallBack<UserLoginBean> callBack) {
        super();
        this.callBack = callBack;
    }



    @Override
    public void login(String type, String name, String paw) {
        api.getLoginMess(type,name,paw).enqueue(new BaseCallBack<UserLoginBean>(){

            @Override
            public void onSuccess(UserLoginBean userLoginBean) {
                if (callBack!=null){
                    callBack.onResultSuccess(userLoginBean);
                }
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }


}
