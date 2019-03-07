package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.ShopCarBean;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/9.
 */

public interface ShopCarViewCallBack extends BaseViewCallBack<ShopCarBean> {

    void deleteSuccess(int pos);

    void deleteFail(String msg);

}
