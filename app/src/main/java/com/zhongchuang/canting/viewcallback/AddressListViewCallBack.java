package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.AddressBase;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/9.
 */

public interface AddressListViewCallBack extends BaseViewCallBack<AddressBase> {

    void deleteSuccess(int pos);
    void defaultSuccess(int pos);
    void optFail(String msg);
}
