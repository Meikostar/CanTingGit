package com.zhongchuang.canting.presenter;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/2.
 */

public interface RegisterPresenter {


    void getYzm(String type, String phone, String code);
    void checkCode(Map<String, String> map);
    void register(Map<String, String> map);



}
