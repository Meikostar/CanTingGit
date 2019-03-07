package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.ShopFirstBean;
import com.zhongchuang.canting.been.ShopHeaderBean;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/9.
 */

public interface ShopFirstViewBackCall extends BaseViewCallBack<ShopFirstBean>{

    void bannerData(ShopHeaderBean shopHeaderBean);

}
