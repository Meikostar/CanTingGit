package com.zhongchuang.canting.net;

/**
 * Created by Administrator on 2017/12/2.
 */

public interface BaseViewCallBack<T> {


    void onResultSuccess(T t); //登陆成功
    void onFail(int code,String msg);

}
