package com.zhongchuang.canting.net;

import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.SubscriptionBean;
import com.zhongchuang.canting.widget.RxBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/12/2.
 */

public abstract class BaseCallBack<T extends BaseResponse> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T t = response.body();
        if (t == null) {
            onOtherErr(0, "响应失败");
        } else {
            switch (t.getStatus()) {
                case 301:
                case 10000:
                    onSuccess(t);
                    break;
                case 809:
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.LOGIN_FINISH, ""));
                    onOtherErr(809, "登录失效，请重新登录");
                    break;
                case 808:
                    RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.LOGIN_FINISH, ""));
                    onOtherErr(808, "您还未登录，快去登录！");
                    break;
                default:
                    onOtherErr(t.getStatus(), t.getMessage());
                    break;
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onOtherErr(0, t.toString());
    }

    public abstract void onSuccess(T t);

    public void onOtherErr(int code, String t) {

    }
}
