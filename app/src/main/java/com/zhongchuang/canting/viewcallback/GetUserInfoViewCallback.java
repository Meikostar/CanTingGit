package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.UserInfo;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/2.
 */

public interface GetUserInfoViewCallback extends BaseViewCallBack<BaseResponse> {
   void  getUserSuccess(UserInfo response);


}
