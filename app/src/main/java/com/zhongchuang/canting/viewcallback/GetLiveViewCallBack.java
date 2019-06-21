package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.VideoData;
import com.zhongchuang.canting.been.ZhiBo_GuanZhongBean;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/23.
 */

public interface GetLiveViewCallBack extends BaseViewCallBack<ZhiBo_GuanZhongBean> {
    void successLive(ZhiBo_GuanZhongBean weixinreq,int loadtype); //登陆成功
    void successRecordLive(ZhiBo_GuanZhongBean weixinreq,int loadtype); //登陆成功

}
