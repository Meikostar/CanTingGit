package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.been.GameDownBean;
import com.zhongchuang.canting.net.BaseCallBack;
import com.zhongchuang.canting.presenter.GameDownPresenter;
import com.zhongchuang.canting.viewcallback.GameDownViewCallback;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/2.
 */

public class GameDownPresenterImpl extends BasePresenterImpl implements GameDownPresenter {

    private GameDownViewCallback callback;

    public GameDownPresenterImpl(GameDownViewCallback callback) {
        super();
        this.callback = callback;
    }

    @Override
    public void requestAppDetail(Map<String, String> map) {
        api.getGameDown(map).enqueue(new BaseCallBack<GameDownBean>() {
            @Override
            public void onSuccess(GameDownBean gameDownBean) {
                callback.onResultSuccess(gameDownBean);
            }

            @Override
            public void onOtherErr(int code, String t) {
                super.onOtherErr(code, t);
                callback.onFail(code,t);
            }
        });
    }


}
