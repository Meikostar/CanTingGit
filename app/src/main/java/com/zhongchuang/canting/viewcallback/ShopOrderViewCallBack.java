package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.SureOrder;
import com.zhongchuang.canting.been.WEIXINREQ;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/23.
 */

public interface ShopOrderViewCallBack extends BaseViewCallBack<SureOrder> {
    void onWexinSuccess(WEIXINREQ weixinreq); //登陆成功
}
