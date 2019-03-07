package com.zhongchuang.canting.presenter.impl;

import android.text.TextUtils;

import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.BusinssBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.FoodShopPresenter;
import com.zhongchuang.canting.viewcallback.FoodCareViewCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/2.
 */

public class FoodShopPresenterImpl extends BasePresenterImpl implements FoodShopPresenter{

    private FoodCareViewCallback callBack;

    public FoodShopPresenterImpl(FoodCareViewCallback  callBack) {
        super();
        this.callBack = callBack;
    }






    @Override
    public void getCare(String userInfoId, String cateShopId, String type) {
        Map<String,String> map = new HashMap<>();

//        map.put("userInfoId",SpUtil.getUserInfoId(mContext));
        map.put("userInfoId",userInfoId);
        map.put("cateShopId",cateShopId);
        map.put("type",type);
        api.cateShop(map).enqueue(new BaseCallBack<BaseResponse>(){

            @Override
            public void onSuccess(BaseResponse baseResponse) {
                if (callBack!=null){
                    callBack.careSuccess(baseResponse);
//                    callBack.onResultSuccess(userLoginBean);
                }
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void getShopInfo(String cateShopId , String userInfoId) {
        Map<String,String> map = new HashMap<>();

//        map.put("userInfoId",SpUtil.getUserInfoId(mContext));
        map.put("cateShopId",cateShopId);
        if(!TextUtils.isEmpty(userInfoId)){
            map.put("userInfoId",userInfoId);
        }
        api.getShopInfo(map).enqueue(new BaseCallBack<BusinssBean>(){

            @Override
            public void onSuccess(BusinssBean userLoginBean) {
                if (callBack!=null){
                    callBack.onResultSuccess(userLoginBean.data);
                }
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void getShopCommentList(String cateShopId) {
        Map<String,String> map = new HashMap<>();

//        map.put("userInfoId",SpUtil.getUserInfoId(mContext));
        map.put("cateShopId",cateShopId);

        api.getCommentList(map).enqueue(new BaseCallBack<BusinssBean>(){

            @Override
            public void onSuccess(BusinssBean userLoginBean) {
                if (callBack!=null){
                    callBack.onResultSuccess(userLoginBean.data);
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
