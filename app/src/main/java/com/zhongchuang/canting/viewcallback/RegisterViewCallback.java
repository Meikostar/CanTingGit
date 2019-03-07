package com.zhongchuang.canting.viewcallback;

import com.zhongchuang.canting.been.BaseResponse;
import com.zhongchuang.canting.been.CodeCheckBean;
import com.zhongchuang.canting.been.UserLoginBean;
import com.zhongchuang.canting.net.BaseViewCallBack;

/**
 * Created by Administrator on 2017/12/2.
 */

public interface RegisterViewCallback extends BaseViewCallBack<UserLoginBean> {
   void  getYzm(BaseResponse response);
   void checkCode(CodeCheckBean codeCheckBean);
   void setPassWordSuccess(BaseResponse baseResponse);

}
