package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.LiveStreamPresenter;
import com.zhongchuang.canting.viewcallback.GetLiveViewCallBack;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/6.
 */

public class LiveStreamPresenterImpl extends BasePresenterImpl implements LiveStreamPresenter {

    private GetLiveViewCallBack callBack;

    public LiveStreamPresenterImpl(GetLiveViewCallBack callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    public void getLiveRoomData(Map<String, String> map, final int loadtype) {
        api.getDirectRoomList(map).enqueue(new BaseCallBack<ZhiBo_GuanZhongBean>() {
            @Override
            public void onSuccess(ZhiBo_GuanZhongBean zhiBo_guanZhongBean) {
                callBack.successLive(zhiBo_guanZhongBean, loadtype);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
//                callBack.onFail(code,t);
            }
        });
    }

    @Override
    public void getLatestVideoList(Map<String, String> map, final int loadtype) {
        api.getLatestVideoList(map).enqueue(new BaseCallBack<ZhiBo_GuanZhongBean>() {

            @Override
            public void onSuccess(ZhiBo_GuanZhongBean userLoginBean) {
                callBack.successLive(userLoginBean, loadtype);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
//                callBack.onOtherErr(t);
            }
        });
    }
}