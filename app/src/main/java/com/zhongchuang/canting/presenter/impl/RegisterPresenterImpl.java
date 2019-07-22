package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.app.CanTingAppLication;
import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.RegisterPresenter;
import com.zhongchuang.canting.viewcallback.RegisterViewCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/2.
 */

public class RegisterPresenterImpl extends BasePresenterImpl implements RegisterPresenter{

    private RegisterViewCallback  callBack;


    public RegisterPresenterImpl(RegisterViewCallback callBack) {
        super();
        this.callBack = callBack;
    }


    @Override
    public void getYzm(String type, String phone,String code) {
        Map<String,String> map = new HashMap<>();
        map.put("mobileNumber",phone);
        map.put("smsType",type);
        map.put("type", CanTingAppLication.code.equals("86")?"1":"2");
        map.put("companyType", CanTingAppLication.CompanyType);
        api.getCall(map).enqueue(new BaseCallBack<BaseResponse>(){
            @Override
            public void onSuccess(BaseResponse baseResponse) {
                callBack.getYzm(baseResponse);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void checkCode(Map<String, String> map) {
        api.getCode(map).enqueue(new BaseCallBack<CodeCheckBean>(){
            @Override
            public void onSuccess(CodeCheckBean baseResponse) {
                callBack.checkCode(baseResponse);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void register(Map<String, String> map) {
        api.getPassWord(map).enqueue(new BaseCallBack<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callBack.setPassWordSuccess(response);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }




}
