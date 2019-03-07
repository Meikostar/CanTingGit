package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.GoodsDetailBean;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/9.
 */

public interface ShopDetailViewCallBack extends BaseViewCallBack<GoodsDetailBean> {

    void addShopCarSuccess(String msg);
}
