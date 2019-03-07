package com.zhongchuang.canting.presenter;

import java.util.Map;

/**
 * Created by Administrator on 2017/12/5.
 */

public interface PersonInfoPresenter {

    void submitChange( Map<String,String> map );
    void getUserInfo(  Map<String,String> map );
}
