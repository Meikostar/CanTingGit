package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.MainPageBean;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/23.
 */

public interface ZhuYeViewCallBack extends BaseViewCallBack<ZhiBo_GuanZhongBean> {
    void onResultSuccess(MainPageBean mainPageBean) ; //登陆成功
}
