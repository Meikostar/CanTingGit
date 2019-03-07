package com.zhongchuang.canting.presenter.impl;

import com.zhongchuang.canting.net.HttpUtil;
import com.zhongchuang.canting.net.netService;

/**
 * Created by Administrator on 2017/12/2.
 */

public class BasePresenterImpl {

    protected netService api;

    public BasePresenterImpl() {
        api = HttpUtil.getInstance().create(netService.class);
    }
}
